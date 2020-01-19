package bph.scheduler.fpx;

import java.util.Date;
import java.util.List;

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
import bph.entities.rpp.RppRekodTempahanLondon;
import bph.entities.utiliti.UtilAkaun;
import bph.entities.utiliti.UtilPermohonan;
import bph.utils.UtilKewangan;
import bph.utils.UtilRpp;
import db.persistence.MyPersistence;

public class UtilFPX {

	/** START RPP **/
	public void transaksiBayarTempahan(String fpxTxnId, RppPermohonan r,
			Date tarikhBayaran, MyPersistence mp) {
		String statusPermohonanLulus = "1425259713415";

		/** Checking existing rekod bayaran */
		String existIdResitSewa = r.getIdResitSewa();
		KewBayaranResit existResitSewa = null;
		if (existIdResitSewa != null) {
			existResitSewa = (KewBayaranResit) mp.find(KewBayaranResit.class,
					existIdResitSewa);
		}

		String existIdResitDeposit = r.getIdResitDeposit();
		KewBayaranResit existResitDeposit = null;
		if (existIdResitDeposit != null) {
			existResitDeposit = (KewBayaranResit) mp.find(
					KewBayaranResit.class, existIdResitDeposit);
		}

		/** RESIT SEWA * */
		Double kadarsewa = r.amaunTotalSewaRpWithoutDeposit() != null ? r
				.amaunTotalSewaRpWithoutDeposit() : 0d;
		KewBayaranResit rstSewa = new KewBayaranResit();
		if (existResitSewa == null) {
			Users pembayar = r.getPemohon();
			if (pembayar != null) {
				rstSewa.setPembayar(pembayar);
				rstSewa.setNoPengenalanPembayar(pembayar.getId());
				rstSewa.setNamaPembayar(pembayar.getUserName());
				rstSewa.setAlamatPembayar(UtilKewangan
						.getAlamatPembayar(pembayar));
			}
			rstSewa.setNoResit(UtilKewangan.generateReceiptNoOnline(mp,
					tarikhBayaran));
			rstSewa.setTarikhBayaran(tarikhBayaran);
			rstSewa.setTarikhResit(tarikhBayaran);
			rstSewa.setMasaResit(tarikhBayaran);
			rstSewa.setFlagJenisBayaran("ONLINE");
			rstSewa.setTarikhDaftar(tarikhBayaran);
			rstSewa.setUserPendaftar(r.getPemohon());
			rstSewa.setJumlahAmaunBayaran(kadarsewa);
			rstSewa.setJuruwangKod("09");
			rstSewa.setFlagJenisResit("2"); // INVOIS
			rstSewa.setIdPermohonan(r.getId());
			rstSewa.setIdTransaksiBank(fpxTxnId);
			mp.persist(rstSewa);
		} else {
			rstSewa = existResitSewa;
			rstSewa.setIdPermohonan(r.getId());
			rstSewa.setIdTransaksiBank(fpxTxnId);
		}

		KewResitKaedahBayaran existResitKaedahBayaranSewa = (KewResitKaedahBayaran) mp
				.get("select x from KewResitKaedahBayaran x where x.resit.id = '"
						+ existIdResitSewa + "' ");
		KewResitKaedahBayaran kb = new KewResitKaedahBayaran();
		if (existResitKaedahBayaranSewa == null) {
			kb.setResit(rstSewa);
			kb.setAmaunBayaran(kadarsewa);
			kb.setModBayaran((CaraBayar) mp.find(CaraBayar.class, "FPX"));
			mp.persist(kb);
		} else {
			kb = existResitKaedahBayaranSewa;
		}

		/** RESIT DEPOSIT * */
		Double kadardeposit = r.amaunDeposit() != null ? r.amaunDeposit() : 0d;
		KewBayaranResit rstDeposit = new KewBayaranResit();
		if (existResitDeposit == null) {
			Users pembayar = r.getPemohon();
			if (pembayar != null) {
				rstDeposit.setPembayar(pembayar);
				rstDeposit.setNoPengenalanPembayar(pembayar.getId());
				rstDeposit.setNamaPembayar(pembayar.getUserName());
				rstDeposit.setAlamatPembayar(UtilKewangan
						.getAlamatPembayar(pembayar));
			}
			rstDeposit.setNoResit(UtilKewangan.generateReceiptNoOnline(mp,
					tarikhBayaran));
			rstDeposit.setTarikhBayaran(tarikhBayaran);
			rstDeposit.setTarikhResit(tarikhBayaran);
			rstDeposit.setMasaResit(tarikhBayaran);
			rstDeposit.setFlagJenisBayaran("ONLINE");
			rstDeposit.setTarikhDaftar(tarikhBayaran);
			rstDeposit.setUserPendaftar(r.getPemohon());
			rstDeposit.setJumlahAmaunBayaran(kadardeposit);
			rstDeposit.setJuruwangKod("09");
			rstDeposit.setFlagJenisResit("1"); // DEPOSIT
			rstDeposit.setIdPermohonan(r.getId());
			rstDeposit.setIdTransaksiBank(fpxTxnId);
			mp.persist(rstDeposit);
		} else {
			rstDeposit.setIdPermohonan(r.getId());
			rstDeposit.setIdTransaksiBank(fpxTxnId);
		}

		KewResitKaedahBayaran existResitKaedahBayaranDeposit = (KewResitKaedahBayaran) mp
				.get("select x from KewResitKaedahBayaran x where x.resit.id = '"
						+ existIdResitDeposit + "' ");
		KewResitKaedahBayaran kbdep = new KewResitKaedahBayaran();
		if (existResitKaedahBayaranDeposit == null) {
			kbdep.setResit(rstDeposit);
			kbdep.setAmaunBayaran(kadardeposit);
			kbdep.setModBayaran((CaraBayar) mp.find(CaraBayar.class, "FPX"));
			mp.persist(kbdep);
		} else {
			kbdep = existResitKaedahBayaranDeposit;
		}

		/** UPDATE RPPPERMOHONAN */
		r.setResitSewa(rstSewa);
		r.setResitDeposit(rstDeposit);
		r.setStatusBayaran("Y");
		r.setTarikhBayaran(tarikhBayaran);
		if (r.getStatus().getId().equalsIgnoreCase("1425259713412")
				|| r.getStatus().getId().equalsIgnoreCase("1430809277102")) {
			r.setStatus((Status) mp.find(Status.class, statusPermohonanLulus));
		}

		updateAndCreateMaklumatBayaran(r, rstSewa, rstDeposit, mp,
				tarikhBayaran);
	}

	public void updateAndCreateMaklumatBayaran(RppPermohonan r,
			KewBayaranResit resitSewa, KewBayaranResit resitDeposit,
			MyPersistence mp, Date tarikhBayaran) {

		// update lejar, create record.
		List<RppAkaun> listAkaun = UtilRpp.getListTempahanDanBayaran(mp, r);
		for (int i = 0; i < listAkaun.size(); i++) {

			RppAkaun lj = listAkaun.get(i);
			lj.setFlagBayar("Y");
			lj.setIdKemaskini(r.getPemohon());
			lj.setKredit(lj.getDebit());
			lj.setTarikhKemaskini(tarikhBayaran);

			// create ref resit Invois/Deposit
			if (lj.getKodHasil().getId().equalsIgnoreCase("72311")) {

				// deposit
				KewDeposit dep = (KewDeposit) mp
						.get("select x from KewDeposit x where x.idLejar = '"
								+ lj.getId() + "' ");
				if (dep != null) {
					KewResitSenaraiInvois rsi = (KewResitSenaraiInvois) mp
							.get("select x from KewResitSenaraiInvois x where x.deposit.id = '"
									+ dep.getId() + "' ");
					if (rsi == null) {
						rsi = new KewResitSenaraiInvois();
						rsi.setDeposit(dep);
						rsi.setResit(resitDeposit);
						rsi.setFlagJenisResit("DEPOSIT");
						mp.persist(rsi);
					}
				}

				lj.setNoResit(resitDeposit.getNoResit());
				lj.setTarikhResit(resitDeposit.getTarikhResit());
				lj.setTarikhTransaksi(resitDeposit.getTarikhBayaran());

			} else {
				// invois
				KewInvois inv = (KewInvois) mp
						.get("select x from KewInvois x where x.idLejar = '"
								+ lj.getId() + "' ");
				if (inv != null) {
					KewResitSenaraiInvois rsi = (KewResitSenaraiInvois) mp
							.get("select x from KewResitSenaraiInvois x where x.invois.id = '"
									+ inv.getId() + "' ");
					if (rsi == null) {
						rsi = new KewResitSenaraiInvois();
						rsi.setInvois(inv);
						rsi.setResit(resitSewa);
						rsi.setFlagJenisResit("INVOIS");
						mp.persist(rsi);
					}
				}

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

	public void updateInvoisInFinance(RppAkaun ak, Users pemohon,
			MyPersistence mp) {
		KewInvois inv = (KewInvois) mp
				.get("select x from KewInvois x where x.idLejar = '"
						+ ak.getId() + "' and x.jenisBayaran.id = '02' ");
		if (inv != null) {
			inv.setFlagBayar("Y");
			inv.setKredit(ak.getDebit());
			inv.setFlagQueue("Y");
		}
	}

	public void updateDepositInFinance(RppAkaun ak, Users pemohon,
			MyPersistence mp) {
		KewDeposit dep = (KewDeposit) mp
				.get("select x from KewDeposit x where x.idLejar = '"
						+ ak.getId() + "' and x.jenisBayaran.id = '02' ");
		if (dep != null) {
			dep.setFlagBayar("Y");
			dep.setFlagQueue("T");
			dep.setNoResit(ak.getNoResit());
			dep.setTarikhBayaran(ak.getTarikhResit());
			dep.setBakiDeposit(ak.getDebit());
		}
	}

	/** END RPP **/

	/** START LONDON **/
	public void transaksiBayarTempahanLondon(String fpxTxnId,
			RppRekodTempahanLondon r, Date tarikhBayaran, MyPersistence mp) {

		// save resit dalam rpptempahanlondon
		KewBayaranResit rstSewa = new KewBayaranResit();

		/**
		 * RESIT SEWA
		 * */
		Users pembayar = r.getPemohon();
		if (pembayar != null) {
			rstSewa.setPembayar(pembayar);
			rstSewa.setNoPengenalanPembayar(pembayar.getId());
			rstSewa.setNamaPembayar(pembayar.getUserName());
			rstSewa.setAlamatPembayar(UtilKewangan.getAlamatPembayar(pembayar));
		}
		rstSewa.setNoResit(UtilKewangan.generateReceiptNoOnline(mp,
				tarikhBayaran));
		rstSewa.setTarikhBayaran(tarikhBayaran);
		rstSewa.setTarikhResit(tarikhBayaran);
		rstSewa.setMasaResit(tarikhBayaran);
		rstSewa.setFlagJenisBayaran("ONLINE");
		rstSewa.setTarikhDaftar(tarikhBayaran);
		rstSewa.setUserPendaftar(r.getPemohon());
		rstSewa.setJumlahAmaunBayaran(r.getDebit());
		rstSewa.setJuruwangKod("09");
		rstSewa.setFlagJenisResit("2"); // INVOIS
		rstSewa.setIdPermohonan(r.getId());
		rstSewa.setIdTransaksiBank(fpxTxnId);
		mp.persist(rstSewa);

		KewResitKaedahBayaran kb = new KewResitKaedahBayaran();
		kb.setResit(rstSewa);
		kb.setAmaunBayaran(rstSewa.getJumlahAmaunBayaran());
		kb.setModBayaran((CaraBayar) mp.find(CaraBayar.class, "FPX"));
		mp.persist(kb);

		/**
		 * SAVE ID RESIT DALAM RPPPERMOHONAN
		 * */
		r.setResitSewa(rstSewa);
		r.setFlagBayar("Y");
		r.setTarikhTransaksi(tarikhBayaran);
		r.setKredit(r.getDebit());

		// update lejar
		List<RppAkaun> listAkaun = mp
				.list("select x from RppAkaun x where x.rekodTempahanLondon.id = '"
						+ r.getId() + "' ");

		for (int i = 0; i < listAkaun.size(); i++) {

			RppAkaun lj = listAkaun.get(i);
			lj.setFlagBayar("Y");
			lj.setIdKemaskini(r.getPemohon());
			lj.setKredit(lj.getDebit());
			lj.setTarikhKemaskini(tarikhBayaran);
			lj.setNoResit(rstSewa.getNoResit());
			lj.setTarikhResit(rstSewa.getTarikhResit());
			lj.setTarikhTransaksi(rstSewa.getTarikhBayaran());

			// invois
			KewInvois inv = (KewInvois) mp
					.get("select x from KewInvois x where x.idLejar = '"
							+ lj.getId() + "' ");
			KewResitSenaraiInvois rsi = new KewResitSenaraiInvois();
			rsi.setInvois(inv);
			rsi.setResit(rstSewa);
			rsi.setFlagJenisResit("INVOIS");
			mp.persist(rsi);

			// update KewInvois / KewDeposit
			updateInvoisLondonInFinance(mp, lj, r.getPemohon());
		}
	}

	public void updateInvoisLondonInFinance(MyPersistence mp, RppAkaun ak,
			Users pemohon) {
		KewInvois inv = (KewInvois) mp
				.get("select x from KewInvois x where x.idLejar = '"
						+ ak.getId() + "' and x.jenisBayaran.id = '12' ");
		if (inv != null) {
			inv.setFlagBayar("Y");
			inv.setKredit(ak.getDebit());
			inv.setFlagQueue("Y");
		}
	}

	/** END LONDON **/

	/** START DEWAN GELANGGANG **/
	public void transaksiBayarTempahanDewanGelanggang(String fpxTxnId,
			UtilPermohonan r, Date tarikhBayaran, MyPersistence mp) {

		// save resit dalam utilPermohonan
		KewBayaranResit rstSewa = new KewBayaranResit();

		/**
		 * RESIT SEWA
		 * */
		Users pembayar = r.getPemohon();
		if (pembayar != null) {
			rstSewa.setPembayar(pembayar);
			rstSewa.setNoPengenalanPembayar(pembayar.getId());
			rstSewa.setNamaPembayar(pembayar.getUserName());
			rstSewa.setAlamatPembayar(UtilKewangan.getAlamatPembayar(pembayar));
		}
		rstSewa.setNoResit(UtilKewangan.generateReceiptNoOnline(mp,
				tarikhBayaran));
		rstSewa.setTarikhBayaran(tarikhBayaran);
		rstSewa.setTarikhResit(tarikhBayaran);
		rstSewa.setMasaResit(tarikhBayaran);
		rstSewa.setFlagJenisBayaran("ONLINE");
		rstSewa.setTarikhDaftar(tarikhBayaran);
		rstSewa.setUserPendaftar(r.getPemohon());
		rstSewa.setJumlahAmaunBayaran(r.getAmaun());
		rstSewa.setJuruwangKod("09");
		rstSewa.setFlagJenisResit("2"); // INVOIS
		rstSewa.setIdPermohonan(r.getId());
		rstSewa.setIdTransaksiBank(fpxTxnId);
		mp.persist(rstSewa);

		KewResitKaedahBayaran kb = new KewResitKaedahBayaran();
		kb.setResit(rstSewa);
		kb.setAmaunBayaran(rstSewa.getJumlahAmaunBayaran());
		kb.setModBayaran((CaraBayar) mp.find(CaraBayar.class, "FPX"));
		mp.persist(kb);

		/**
		 * SAVE ID RESIT DALAM RPPPERMOHONAN
		 * */
		r.setResitSewa(rstSewa);
		r.setStatusBayaran("Y");
		r.setTarikhBayaran(tarikhBayaran);

		// UPDATE LEJAR
		UtilAkaun mn = (UtilAkaun) mp
				.get("select x from UtilAkaun x where x.permohonan.id = '"
						+ r.getId() + "' ");
		mn.setFlagBayar("Y");
		mn.setIdKemaskini(r.getPemohon());
		mn.setKredit(mn.getDebit());
		mn.setNoResit(rstSewa.getNoResit());
		mn.setTarikhKemaskini(tarikhBayaran);
		mn.setTarikhResit(rstSewa.getTarikhResit());
		mn.setTarikhTransaksi(rstSewa.getTarikhBayaran());

		if (mn.getKodHasil().getId().equalsIgnoreCase("74299")) {
			KewInvois inv = (KewInvois) mp
					.get("select x from KewInvois x where x.idLejar = '"
							+ mn.getId() + "' ");
			inv.setFlagBayar("Y");
			inv.setKredit(mn.getDebit());
			inv.setFlagQueue("Y");

			KewResitSenaraiInvois rsi = new KewResitSenaraiInvois();
			rsi.setInvois(inv);
			rsi.setResit(rstSewa);
			rsi.setFlagJenisResit("INVOIS");
			mp.persist(rsi);
		}
	}
	/** END DEWAN GELANGGANG **/
}
