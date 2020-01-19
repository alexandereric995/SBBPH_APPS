package bph.integrasi.fpx;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import portal.module.entity.Users;
import bph.entities.kewangan.KewBayaranResit;
import bph.entities.kewangan.KewDeposit;
import bph.entities.kewangan.KewInvois;
import bph.entities.kewangan.KewResitKaedahBayaran;
import bph.entities.kewangan.KewResitSenaraiInvois;
import bph.entities.kod.CaraBayar;
import bph.entities.kod.Status;
import bph.entities.rpp.RppAkaun;
import bph.entities.rpp.RppPermohonan;
import bph.entities.utiliti.UtilAkaun;
import bph.entities.utiliti.UtilPermohonan;
import bph.utils.UtilKewangan;
import bph.utils.UtilRpp;
import db.persistence.MyPersistence;

public class MIGSUtil {

	private MyPersistence mp;
	String userId;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public MIGSUtil(HttpSession session) {
		this.userId = (String) session.getAttribute("_portal_login");

	}

	// ----------Gelanggang/Dewan---------------------------------------
	/**
	 * EDIT BY PEJE ON 7/1/2016
	 * TUKA DBPERSISTENCE TO MYPERSISTENCE
	 * @param idp
	 */
	public void UTILsimpanMIGS(String idp) {
		mp = new MyPersistence();

		try {			
			UtilPermohonan permohonan = (UtilPermohonan) mp.find(UtilPermohonan.class, idp);
			if (permohonan != null) {
				mp.begin();
				updateBayarTempahan(permohonan, mp);// migs
				mp.commit();
			} else {
				System.out.println("UTILsimpanMIGS : PERMOHONAN IS NULL");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
	}

	public void updateBayarTempahan(UtilPermohonan permohonan, MyPersistence mp) { // real using MIGS
		// CREATE RESIT
		KewBayaranResit resit = new KewBayaranResit();
		Users pembayar = permohonan.getPemohon();
		if (pembayar != null) {
			resit.setPembayar(pembayar);
			resit.setNoPengenalanPembayar(pembayar.getId());
			resit.setNamaPembayar(pembayar.getUserName());				
			resit.setAlamatPembayar(UtilKewangan.getAlamatPembayar(pembayar));
		}
		resit.setNoResit(UtilKewangan.generateReceiptNoOnline(mp, new Date()));
		resit.setTarikhBayaran(new Date());
		resit.setTarikhResit(new Date());
		resit.setMasaResit(new Date());
		resit.setFlagJenisBayaran("ONLINE");
		resit.setTarikhDaftar(new Date());
		resit.setJuruwangKod("09");// ONLINE
		resit.setUserPendaftar((Users) mp.find(Users.class, userId));
		resit.setJumlahAmaunBayaran(permohonan.getAmaun());
		resit.setFlagJenisResit("2"); //INVOIS
		resit.setIdPermohonan(permohonan.getId());
		mp.persist(resit);

		// CREATE KAEDAH BAYARAN
		KewResitKaedahBayaran kb = new KewResitKaedahBayaran();
		kb.setResit(resit);
		kb.setAmaunBayaran(resit.getJumlahAmaunBayaran());
		kb.setModBayaran((CaraBayar) mp.find(CaraBayar.class, "MIGS"));
		mp.persist(kb);

		// UPDATE LEJAR
		UtilAkaun mn = (UtilAkaun) mp.get("select x from UtilAkaun x where x.permohonan.id = '" + permohonan.getId() + "' ");
		mn.setFlagBayar("Y");
		mn.setIdKemaskini((Users) mp.find(Users.class, userId));
		mn.setKredit(mn.getDebit());
		mn.setNoResit(resit.getNoResit());
		mn.setTarikhKemaskini(new Date());
		mn.setTarikhResit(resit.getTarikhResit());
		mn.setTarikhTransaksi(resit.getTarikhBayaran());

		if (mn.getKodHasil().getId().equalsIgnoreCase("74299")) {
			KewInvois inv = (KewInvois) mp.get("select x from KewInvois x where x.idLejar = '" + mn.getId() + "' ");
			KewResitSenaraiInvois rsi = new KewResitSenaraiInvois();
			rsi.setInvois(inv);
			rsi.setResit(resit);
			rsi.setFlagJenisResit("INVOIS");
			mp.persist(rsi);
		}

		// UPDATE MAIN TABLE
		permohonan.setStatusBayaran("Y");
		permohonan.setResitSewa(resit);
	}

	/**
	 * EDIT BY PEJE ON 7/1/2016
	 * TUKA DBPERSISTENCE TO MYPERSISTENCE
	 * @param idp
	 */
	public void RPPsimpanMIGS(String idp) {
		mp = new MyPersistence();

		try {			
			RppPermohonan permohonan = (RppPermohonan) mp.find(RppPermohonan.class, idp);
			if (permohonan != null) {
				mp.begin();
				transaksiBayarTempahanCC(permohonan, mp);// migs
				mp.commit();
			} else {
				System.out.println("RPPsimpanMIGS : PERMOHONAN IS NULL");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
	}

	// MIGS
	public void transaksiBayarTempahanCC(RppPermohonan r, MyPersistence mp) {
		String statusPermohonanLulus = "1425259713415";

		/** RESIT SEWA * */
		Double kadarsewa = r.amaunTotalSewaRpWithoutDeposit() != null ? r.amaunTotalSewaRpWithoutDeposit() : 0d;
		KewBayaranResit rstSewa = new KewBayaranResit();
		Users pembayar = r.getPemohon();
		if (pembayar != null) {
			rstSewa.setPembayar(pembayar);
			rstSewa.setNoPengenalanPembayar(pembayar.getId());
			rstSewa.setNamaPembayar(pembayar.getUserName());				
			rstSewa.setAlamatPembayar(UtilKewangan.getAlamatPembayar(pembayar));
		}
		rstSewa.setNoResit(UtilKewangan.generateReceiptNoOnline(mp, new Date()));
		rstSewa.setTarikhBayaran(new Date());
		rstSewa.setTarikhResit(new Date());
		rstSewa.setMasaResit(new Date());
		rstSewa.setFlagJenisBayaran("ONLINE");
		rstSewa.setTarikhDaftar(new Date());
		rstSewa.setUserPendaftar((Users) mp.find(Users.class, this.userId));
		rstSewa.setJumlahAmaunBayaran(kadarsewa);
		rstSewa.setJuruwangKod("09");
		rstSewa.setFlagJenisResit("2"); //INVOIS
		rstSewa.setIdPermohonan(r.getId());
		mp.persist(rstSewa);

		KewResitKaedahBayaran kb = new KewResitKaedahBayaran();
		kb.setResit(rstSewa);
		kb.setAmaunBayaran(kadarsewa);
		kb.setModBayaran((CaraBayar) mp.find(CaraBayar.class, "MIGS"));
		mp.persist(kb);

		/** RESIT DEPOSIT * */
		Double kadardeposit = r.amaunDeposit() != null ? r.amaunDeposit() : 0d;
		KewBayaranResit rstDeposit = new KewBayaranResit();

		if (pembayar != null) {
			rstDeposit.setPembayar(pembayar);
			rstDeposit.setNoPengenalanPembayar(pembayar.getId());
			rstDeposit.setNamaPembayar(pembayar.getUserName());				
			rstDeposit.setAlamatPembayar(UtilKewangan.getAlamatPembayar(pembayar));
		}
		rstDeposit.setNoResit(UtilKewangan.generateReceiptNoOnline(mp, new Date()));
		rstDeposit.setTarikhBayaran(new Date());
		rstDeposit.setTarikhResit(new Date());
		rstDeposit.setMasaResit(new Date());
		rstDeposit.setFlagJenisBayaran("ONLINE");
		rstDeposit.setTarikhDaftar(new Date());
		rstDeposit.setUserPendaftar((Users) mp.find(Users.class, this.userId));
		rstDeposit.setJumlahAmaunBayaran(kadardeposit);
		rstDeposit.setJuruwangKod("09");
		rstDeposit.setFlagJenisResit("1"); //DEPOSIT
		rstDeposit.setIdPermohonan(r.getId());
		mp.persist(rstDeposit);
		
		KewResitKaedahBayaran kbdep = new KewResitKaedahBayaran();
		kbdep.setResit(rstDeposit);
		kbdep.setAmaunBayaran(kadardeposit);
		kbdep.setModBayaran((CaraBayar) mp.find(CaraBayar.class, "MIGS"));
		mp.persist(kbdep);
		
		/** SAVE ID RESIT DALAM RPPPERMOHONAN */
		r.setResitSewa(rstSewa);
		r.setResitDeposit(rstDeposit);
		
		/** UPDATE RPPPERMOHONAN */
		r.setStatusBayaran("Y");
		r.setTarikhBayaran(new Date());
		r.setStatus((Status) mp.find(Status.class, statusPermohonanLulus));
		
		updateAndCreateMaklumatBayaran(r, rstSewa, rstDeposit, kb, kbdep, mp);

	}

	public void updateAndCreateMaklumatBayaran(RppPermohonan r,
			KewBayaranResit resitSewa, KewBayaranResit resitDeposit,
			KewResitKaedahBayaran kaedahSewa,
			KewResitKaedahBayaran kaedahDeposit, MyPersistence mp) {
		
		// update lejar, create record.
		List<RppAkaun> listAkaun = UtilRpp.getListTempahanDanBayaran(mp, r);
		for (int i = 0; i < listAkaun.size(); i++) {

			RppAkaun lj = listAkaun.get(i);
			lj.setFlagBayar("Y");
			lj.setIdKemaskini((Users) mp.find(Users.class, this.userId));
			lj.setKredit(lj.getDebit());
			lj.setTarikhKemaskini(new Date());

			// create ref resit Invois/Deposit
			if (lj.getKodHasil().getId().equalsIgnoreCase("72311")) {
				// deposit
				KewDeposit dep = (KewDeposit) mp
						.get("select x from KewDeposit x where x.idLejar = '"
								+ lj.getId() + "' ");
				KewResitSenaraiInvois rsi = new KewResitSenaraiInvois();
				rsi.setDeposit(dep);
				rsi.setResit(resitDeposit);
				rsi.setFlagJenisResit("DEPOSIT");
				mp.persist(rsi);

				lj.setNoResit(resitDeposit.getNoResit());
				lj.setTarikhResit(resitDeposit.getTarikhResit());
				lj.setTarikhTransaksi(resitDeposit.getTarikhBayaran());				

			} else {
				// invois
				KewInvois inv = (KewInvois) mp
						.get("select x from KewInvois x where x.idLejar = '"
								+ lj.getId() + "' ");
				KewResitSenaraiInvois rsi = new KewResitSenaraiInvois();
				rsi.setInvois(inv);
				rsi.setResit(resitSewa);
				rsi.setFlagJenisResit("INVOIS");
				mp.persist(rsi);

				lj.setNoResit(resitSewa.getNoResit());
				lj.setTarikhResit(resitSewa.getTarikhResit());
				lj.setTarikhTransaksi(resitSewa.getTarikhBayaran());
			}

			// update KewInvois / KewDeposit
			if (lj.getKodHasil().getId().equalsIgnoreCase("72311")) { // deposit
				updateDepositInFinance(lj, r.getPemohon(), mp);
			} else {
				updateInvoisInFinance(lj, r.getPemohon(), mp);				
			}
		}
	}

	public void updateInvoisInFinance(RppAkaun ak, Users pemohon, MyPersistence mp) {
		KewInvois inv = (KewInvois) mp
				.get("select x from KewInvois x where x.idLejar = '"
						+ ak.getId() + "' and x.jenisBayaran.id = '02' ");
		if (inv != null) {
			inv.setFlagBayar("Y");
			inv.setKredit(ak.getDebit());
			inv.setFlagQueue("Y");
		}
	}

	public void updateDepositInFinance(RppAkaun ak, Users pemohon, MyPersistence mp) {
		KewDeposit dep = (KewDeposit) mp
				.get("select x from KewDeposit x where x.idLejar = '"
						+ ak.getId() + "' and x.jenisBayaran.id = '02' ");
		if (dep != null) {
			dep.setFlagBayar("Y");
			dep.setFlagQueue("Y");
			dep.setNoResit(ak.getNoResit());
		}
	}
}

