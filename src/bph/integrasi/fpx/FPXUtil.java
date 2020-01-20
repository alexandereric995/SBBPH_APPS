package bph.integrasi.fpx;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpSession;

import lebah.db.Db;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import portal.module.entity.Users;
import bph.entities.integrasi.FPXRecords;
import bph.entities.integrasi.FPXRecordsRequest;
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


public class FPXUtil {

	static Logger myLogger = Logger.getLogger("FPXUtil");

	private MyPersistence mp;
	String userId;
	private boolean successRequery;

	/**
	 * @param args
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		
		FPXUtil x = new FPXUtil();
//		x.reQueryFPX(sellerOrderNo, sellerExOrderNo, txnAmount);
//		x.reQueryFPX("1838566605391550", "20170324182829", "100");
	}

	public FPXUtil(HttpSession session) {
		this.userId = (String) session.getAttribute("_portal_login");
	}
	
	public FPXUtil() {
		
	}
	
	/** ADD BY PEJE - DEFINE MODUL BAGI FLAG_MODUL IS NULL WHEN RETURN FROM MYCLEAR 
	 * @throws SQLException 
	 */
	public void defineModulFPXPayment(String fpxTxnId, String idPermohonan) throws SQLException {
		try {
			mp = new MyPersistence();
			FPXRecords fpxRecords = (FPXRecords) mp.find(FPXRecords.class, fpxTxnId);
			
			if (idPermohonan != null) {
				//UTIL
				UtilPermohonan util = (UtilPermohonan) mp.find(UtilPermohonan.class, idPermohonan);
				if (util != null) {
					
					try {
						mp.begin();
						updateBayarTempahan(fpxTxnId, util, mp);// fpx		
						if (fpxRecords != null){
							fpxRecords.setFlagModul("UTIL");
							fpxRecords.setFlagManagePayment("Y");
						} else {
							doAddFPXLog(fpxTxnId, idPermohonan, "UPDATE PAYMENT FLAG - FPXRECORDS DETECTED NULL");
						}
						mp.commit();
						doAddFPXLog(fpxTxnId, idPermohonan, "DEFINE MODUL = UTIL.");
					} catch (Exception ex) {
						doAddFPXLog(fpxTxnId, idPermohonan, "ERROR UPDATE BAYAR TEMPAHAN");
					}
				}
				
				//IR
				RppPermohonan ir = (RppPermohonan) mp.find(RppPermohonan.class, idPermohonan);
				if (ir != null) {
					try {
						mp.begin();
						transaksiBayarTempahan(fpxTxnId, ir, mp);// fpx
						if (fpxRecords != null){
							fpxRecords.setFlagModul("IR");
							fpxRecords.setFlagManagePayment("Y");
						} else {
							doAddFPXLog(fpxTxnId, idPermohonan, "UPDATE PAYMENT FLAG - FPXRECORDS DETECTED NULL");
						}
						mp.commit();
						doAddFPXLog(fpxTxnId, idPermohonan, "DEFINE MODUL = IR.");
					} catch (Exception ex) {
						doAddFPXLog(fpxTxnId, idPermohonan, "ERROR UPDATE BAYAR TEMPAHAN");
					}
				}
				
				//LONDON
				RppRekodTempahanLondon london = (RppRekodTempahanLondon) mp.find(RppRekodTempahanLondon.class, idPermohonan);
				if (london != null) {
					try {
						mp.begin();
						transaksiBayarTempahanLondon(fpxTxnId, london, mp);// fpx				
						if (fpxRecords != null){
							fpxRecords.setFlagModul("LONDON");
							fpxRecords.setFlagManagePayment("Y");
						} else {
							doAddFPXLog(fpxTxnId, idPermohonan, "UPDATE PAYMENT FLAG - FPXRECORDS DETECTED NULL");
						}
						mp.commit();
						doAddFPXLog(fpxTxnId, idPermohonan, "DEFINE MODUL = LONDON.");
					} catch (Exception ex) {
						doAddFPXLog(fpxTxnId, idPermohonan, "ERROR UPDATE BAYAR TEMPAHAN");
					}					
				}
			} else {
				doAddFPXLog(fpxTxnId, idPermohonan, "ID_PERMOHONAN DETECTED NULL");
			}			
		} catch (Exception ex) {
			doAddFPXLog(fpxTxnId, idPermohonan, ex.getMessage() + ":: ID_PERMOHONAN = '" + idPermohonan + "'");
			ex.printStackTrace();
		} finally {
			if (mp != null)
				if (mp != null) { mp.close(); }
		}
	}
	
	/**
	 * EDIT BY PEJE ON 7/1/2016 TUKA DBPERSISTENCE TO MYPERSISTENCE
	 * @param idp
	 * @throws SQLException 
	 */
	public void UTILsimpanFPX(String fpxTxnId, String idp) throws SQLException {
		mp = new MyPersistence();

		try {
			UtilPermohonan permohonan = (UtilPermohonan) mp.find(UtilPermohonan.class, idp);
			if (permohonan != null) {
				mp.begin();
				updateBayarTempahan(fpxTxnId, permohonan, mp);// fpx
				FPXRecords fpxRecords = (FPXRecords) mp.find(FPXRecords.class, fpxTxnId);
				if (fpxRecords != null) {
					fpxRecords.setFlagModul("UTIL");
					fpxRecords.setFlagManagePayment("Y");
				}
				mp.commit();
			} else {
				doAddFPXLog(fpxTxnId, idp, "ERROR UTILsimpanFPX :PERMOHONAN IS NULL ");
				myLogger.error("UTILsimpanFPX : PERMOHONAN IS NULL");
			}
		} catch (Exception e) {
			doAddFPXLog(fpxTxnId, idp, e.getMessage());
			myLogger.error(e.getMessage());
			// e.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
	}	

	public void updateBayarTempahan(String fpxTxnId, UtilPermohonan permohonan, MyPersistence mp) { // real using fpx
		
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
		if (this.userId != null)
			resit.setUserPendaftar((Users) mp.find(Users.class, this.userId));
		resit.setJumlahAmaunBayaran(permohonan.getAmaun());
		resit.setFlagJenisResit("2"); // INVOIS
		resit.setIdPermohonan(permohonan.getId());
		resit.setIdTransaksiBank(fpxTxnId);
		mp.persist(resit);

		// CREATE KAEDAH BAYARAN
		KewResitKaedahBayaran kb = new KewResitKaedahBayaran();
		kb.setResit(resit);
		kb.setAmaunBayaran(resit.getJumlahAmaunBayaran());
		kb.setModBayaran((CaraBayar) mp.find(CaraBayar.class, "FPX"));
		mp.persist(kb);

		// UPDATE LEJAR
		UtilAkaun mn = (UtilAkaun) mp.get("select x from UtilAkaun x where x.permohonan.id = '" + permohonan.getId() + "' ");
		mn.setFlagBayar("Y");
		if (this.userId != null)
			mn.setIdKemaskini((Users) mp.find(Users.class, this.userId));
		mn.setKredit(mn.getDebit());
		mn.setNoResit(resit.getNoResit());
		mn.setTarikhKemaskini(new Date());
		mn.setTarikhResit(resit.getTarikhResit());
		mn.setTarikhTransaksi(resit.getTarikhBayaran());

		if (mn.getKodHasil().getId().equalsIgnoreCase("74299")) {
			KewInvois inv = (KewInvois) mp.get("select x from KewInvois x where x.idLejar = '" + mn.getId() + "' ");
			inv.setFlagBayar("Y");
			inv.setKredit(mn.getDebit());
			inv.setFlagQueue("Y");

			KewResitSenaraiInvois rsi = new KewResitSenaraiInvois();
			rsi.setInvois(inv);
			rsi.setResit(resit);
			rsi.setFlagJenisResit("INVOIS");
			mp.persist(rsi);
		}

		// UPDATE MAIN TABLE
		permohonan.setStatusBayaran("Y");
		permohonan.setTarikhBayaran(new Date());
		permohonan.setResitSewa(resit);
	}

	/**
	 * EDIT BY PEJE ON 7/1/2016 TUKA DBPERSISTENCE TO MYPERSISTENCE
	 * @param fpxTxnId
	 * @param idp
	 */
	public void RPPsimpanFPX(String fpxTxnId, String idp) throws SQLException {

		try {
			mp = new MyPersistence();
			RppPermohonan permohonan = (RppPermohonan) mp.find(RppPermohonan.class, idp);
			if (permohonan != null) {
				mp.begin();
				transaksiBayarTempahan(fpxTxnId, permohonan, mp);// fpx
				FPXRecords fpxRecords = (FPXRecords) mp.find(FPXRecords.class,fpxTxnId);
				if (fpxRecords != null)
					fpxRecords.setFlagManagePayment("Y");
				mp.commit();
			} else {
				doAddFPXLog(fpxTxnId, idp, "ERROR RPPsimpanFPX :PERMOHONAN IS NULL ");
				myLogger.error("RPPsimpanFPX : PERMOHONAN IS NULL");
			}
		} catch (Exception e) {
			doAddFPXLog(fpxTxnId, idp, e.getMessage());
			myLogger.error(e.getMessage());
			// e.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
	}

	public void transaksiBayarTempahan(String fpxTxnId, RppPermohonan r, MyPersistence mp) {
		String statusPermohonanLulus = "1425259713415";

		/** Checking existing rekod bayaran */
		String existIdResitSewa = r.getIdResitSewa();
		KewBayaranResit existResitSewa = null;
		if (existIdResitSewa != null) {
			existResitSewa = (KewBayaranResit) mp.find(KewBayaranResit.class, existIdResitSewa);
		}

		String existIdResitDeposit = r.getIdResitDeposit();
		KewBayaranResit existResitDeposit = null;
		if (existIdResitDeposit != null) {
			existResitDeposit = (KewBayaranResit) mp.find(KewBayaranResit.class, existIdResitDeposit);
		}

		/** RESIT SEWA * */
		Double kadarsewa = r.amaunTotalSewaRpWithoutDeposit() != null ? r.amaunTotalSewaRpWithoutDeposit() : 0d;
		KewBayaranResit rstSewa = new KewBayaranResit();
		if (existResitSewa == null) {
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
			if (this.userId != null)
				rstSewa.setUserPendaftar((Users) mp.find(Users.class, this.userId));
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

		KewResitKaedahBayaran existResitKaedahBayaranSewa = (KewResitKaedahBayaran) mp.get("select x from KewResitKaedahBayaran x where x.resit.id = '" + existIdResitSewa + "' ");
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
				rstDeposit.setAlamatPembayar(UtilKewangan.getAlamatPembayar(pembayar));
			}
			rstDeposit.setNoResit(UtilKewangan.generateReceiptNoOnline(mp, new Date()));
			rstDeposit.setTarikhBayaran(new Date());
			rstDeposit.setTarikhResit(new Date());
			rstDeposit.setMasaResit(new Date());
			rstDeposit.setFlagJenisBayaran("ONLINE");
			rstDeposit.setTarikhDaftar(new Date());
			if (this.userId != null)
				rstDeposit.setUserPendaftar((Users) mp.find(Users.class,this.userId));
			rstDeposit.setJumlahAmaunBayaran(kadardeposit);
			rstDeposit.setJuruwangKod("09");
			rstDeposit.setFlagJenisResit("1"); // DEPOSIT
			rstDeposit.setIdPermohonan(r.getId());
			rstDeposit.setIdTransaksiBank(fpxTxnId);
			mp.persist(rstDeposit);
		} else {
			rstDeposit = existResitDeposit;
			rstDeposit.setIdPermohonan(r.getId());
			rstDeposit.setIdTransaksiBank(fpxTxnId);
		}

		KewResitKaedahBayaran existResitKaedahBayaranDeposit = (KewResitKaedahBayaran) mp.get("select x from KewResitKaedahBayaran x where x.resit.id = '" + existIdResitDeposit + "' ");
		KewResitKaedahBayaran kbdep = new KewResitKaedahBayaran();
		if (existResitKaedahBayaranDeposit == null) {
			kbdep.setResit(rstDeposit);
			kbdep.setAmaunBayaran(kadardeposit);
			kbdep.setModBayaran((CaraBayar) mp.find(CaraBayar.class, "FPX"));
			mp.persist(kbdep);
		} else {
			kbdep = existResitKaedahBayaranDeposit;
		}

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
			if (this.userId != null)
				lj.setIdKemaskini((Users) mp.find(Users.class, this.userId));
			lj.setKredit(lj.getDebit());
			lj.setTarikhKemaskini(new Date());

			// create ref resit Invois/Deposit
			if (lj.getKodHasil().getId().equalsIgnoreCase("72311")) {

				// deposit
				KewDeposit dep = (KewDeposit) mp.get("select x from KewDeposit x where x.idLejar = '" + lj.getId() + "' ");
				if (dep != null) {
					KewResitSenaraiInvois rsi = (KewResitSenaraiInvois) mp.get("select x from KewResitSenaraiInvois x where x.deposit.id = '" + dep.getId() + "' ");
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
				KewInvois inv = (KewInvois) mp.get("select x from KewInvois x where x.idLejar = '" + lj.getId() + "' ");
				if (inv != null) {
					KewResitSenaraiInvois rsi = (KewResitSenaraiInvois) mp.get("select x from KewResitSenaraiInvois x where x.invois.id = '" + inv.getId() + "' ");
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
			if (lj.getKodHasil().getId().equalsIgnoreCase("72311")) { // DEPOSIT
				updateDepositInFinance(lj, r.getPemohon(), mp);
			} else {
				updateInvoisInFinance(lj, r.getPemohon(), mp);				
			}
		}
	}

	public void updateInvoisInFinance(RppAkaun ak, Users pemohon, MyPersistence mp) {
		KewInvois inv = (KewInvois) mp.get("select x from KewInvois x where x.idLejar = '" + ak.getId() + "' and x.jenisBayaran.id = '02' ");
		if (inv != null) {
			inv.setFlagBayar("Y");
			inv.setKredit(ak.getDebit());
			inv.setFlagQueue("Y");
		}
	}

	public void updateDepositInFinance(RppAkaun ak, Users pemohon, MyPersistence mp) {
		KewDeposit dep = (KewDeposit) mp.get("select x from KewDeposit x where x.idLejar = '" + ak.getId() + "' and x.jenisBayaran.id = '02' ");
		if (dep != null) {
			dep.setFlagBayar("Y");
			dep.setFlagQueue("T");
			dep.setNoResit(ak.getNoResit());
			dep.setTarikhBayaran(ak.getTarikhResit());
			dep.setBakiDeposit(ak.getDebit());
		}
	}

	/**
	 * EDIT BY PEJE ON 7/1/2016 TUKA DBPERSISTENCE TO MYPERSISTENCE
	 * @param fpxTxnId
	 * @param idp
	 */
	public void LondonSimpanFPX(String fpxTxnId, String idp) throws SQLException {

		try {
			mp = new MyPersistence();
			RppRekodTempahanLondon permohonan = (RppRekodTempahanLondon) mp.find(RppRekodTempahanLondon.class, idp);
			if (permohonan != null) {
				mp.begin();
				transaksiBayarTempahanLondon(fpxTxnId, permohonan, mp);// fpx
				
				FPXRecords fpxRecords = (FPXRecords) mp.find(FPXRecords.class, fpxTxnId);
				if (fpxRecords != null)
					fpxRecords.setFlagManagePayment("Y");
				mp.commit();
			} else {
				doAddFPXLog(fpxTxnId, idp, "ERROR LondonSimpanFPX :PERMOHONAN IS NULL ");
				myLogger.error("LondonSimpanFPX : PERMOHONAN IS NULL");
			}
		} catch (Exception e) {
			doAddFPXLog(fpxTxnId, idp, e.getMessage());
			myLogger.error(e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
	}

	@SuppressWarnings("unchecked")
	public void transaksiBayarTempahanLondon(String fpxTxnId, RppRekodTempahanLondon r, MyPersistence mp) {

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
		rstSewa.setNoResit(UtilKewangan.generateReceiptNoOnline(mp, new Date()));
		rstSewa.setTarikhBayaran(new Date());
		rstSewa.setTarikhResit(new Date());
		rstSewa.setMasaResit(new Date());
		rstSewa.setFlagJenisBayaran("ONLINE");
		rstSewa.setTarikhDaftar(new Date());
		if (this.userId != null)
			rstSewa.setUserPendaftar((Users) mp.find(Users.class, this.userId));
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
		r.setTarikhTransaksi(new Date());
		r.setKredit(r.getDebit());

		// update lejar
		List<RppAkaun> listAkaun = mp.list("select x from RppAkaun x where x.rekodTempahanLondon.id = '" + r.getId() + "' ");

		for (int i = 0; i < listAkaun.size(); i++) {

			RppAkaun lj = listAkaun.get(i);
			lj.setFlagBayar("Y");
			if (this.userId != null)
				lj.setIdKemaskini((Users) mp.find(Users.class, this.userId));
			lj.setKredit(lj.getDebit());
			lj.setTarikhKemaskini(new Date());
			lj.setNoResit(rstSewa.getNoResit());
			lj.setTarikhResit(rstSewa.getTarikhResit());
			lj.setTarikhTransaksi(rstSewa.getTarikhBayaran());

			// invois
			KewInvois inv = (KewInvois) mp.get("select x from KewInvois x where x.idLejar = '" + lj.getId() + "' ");
			KewResitSenaraiInvois rsi = new KewResitSenaraiInvois();
			rsi.setInvois(inv);
			rsi.setResit(rstSewa);
			rsi.setFlagJenisResit("INVOIS");
			mp.persist(rsi);

			// update KewInvois / KewDeposit
			updateInvoisLondonInFinance(mp, lj, r.getPemohon());
		}
	}

	public void updateInvoisLondonInFinance(MyPersistence mp, RppAkaun ak, Users pemohon) {
		KewInvois inv = (KewInvois) mp.get("select x from KewInvois x where x.idLejar = '" + ak.getId() + "' and x.jenisBayaran.id = '12' ");
		inv.setFlagBayar("Y");
		inv.setKredit(ak.getDebit());
		inv.setFlagQueue("Y");
	}

	// FPX AUDIT TRAIL
	private static java.sql.Date getCurrentDate() {
		java.util.Date today = new java.util.Date();
		return new java.sql.Date(today.getTime());
	}

	public void addUpdatePayment_Transaction(String user_login, String sellerOrderNo, String fpx_txnAmount, String flagModul) throws SQLException {

		Db db = null;
		Connection conn = null;
		PreparedStatement preparedStmt = null;
		String sql = "";
		boolean isExist = false;
		try {
			db = new Db();
			conn = db.getConnection();

			// CHECK IF EXIST THEN UPDATE, ELSE INSERT
			sql = "SELECT COUNT(*) as cnt FROM payment_transaction WHERE sellerOrderNo=?";
			preparedStmt = conn.prepareStatement(sql);
			preparedStmt.setString(1, sellerOrderNo);
			ResultSet rs = preparedStmt.executeQuery();
			if (rs.next()) {
				if (rs.getInt("cnt") > 0) {
					isExist = true;
				}
			}

			if (isExist) {
				// UPDATE
				myLogger.debug("UPDATE payment_transaction");
				sql = "UPDATE payment_transaction SET tarikh_kemaskini=?,trans_counter=trans_counter+1 WHERE sellerOrderNo=?";
				preparedStmt = conn.prepareStatement(sql);
				preparedStmt.setDate(1, getCurrentDate());
				preparedStmt.setString(2, sellerOrderNo);
				preparedStmt.execute();

			} else {
				// INSERT
				myLogger.debug("INSERT payment_transaction");
				sql = "INSERT INTO payment_transaction (user_login,sellerOrderNo,txnAmount,flagModul,tarikh_transaksi,tarikh_kemaskini,trans_counter)  "
						+ "VALUES (?,?,?,?,?,?,?)";
				preparedStmt = conn.prepareStatement(sql);
				preparedStmt.setString(1, user_login);
				preparedStmt.setString(2, sellerOrderNo);
				preparedStmt.setString(3, fpx_txnAmount);
				preparedStmt.setString(4, flagModul);
				preparedStmt.setDate(5, getCurrentDate());
				preparedStmt.setDate(6, getCurrentDate());
				preparedStmt.setInt(7, 1);
				preparedStmt.execute();
			}
		} catch (Exception e) {
			e.printStackTrace();
			myLogger.error("ERROR addFPX_Records:" + e.getMessage());
		} finally {
			if (preparedStmt != null)
				preparedStmt.close();
			if (conn != null)
				conn.close();
			if (db != null)
				db.close();
		}
	}

	public void UpdateFPX_Records(String fpx_fpxTxnId,
			String fpx_creditAuthCode, String fpx_creditAuthNo,
			String fpx_debitAuthCode, String fpx_debitAuthNo,
			String fpx_fpxTxnTime, String fpx_sellerExId,
			String fpx_sellerExOrderNo, String fpx_sellerId,
			String px_sellerOrderNo, String fpx_sellerTxnTime,
			String fpx_txnAmount) throws SQLException {

		Db db = null;
		Connection conn = null;
		PreparedStatement preparedStmt = null;
		String sql = "";
		try {
			db = new Db();
			conn = db.getConnection();
			sql = "INSERT INTO fpx_records(fpxTxnId,creditAuthCode,creditAuthNo,debitAuthCode,debitAuthNo,fpxTxnTime ,"
					+ "sellerExId,sellerExOrderNo,sellerId,"
					+ "sellerOrderNo ,sellerTxnTime,txnAmount)  "
					+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
			preparedStmt = conn.prepareStatement(sql);
			preparedStmt.setString(1, fpx_fpxTxnId);
			preparedStmt.setString(2, fpx_creditAuthCode);
			preparedStmt.setString(3, fpx_creditAuthNo);
			preparedStmt.setString(4, fpx_debitAuthCode);
			preparedStmt.setString(5, fpx_debitAuthNo);
			preparedStmt.setString(6, fpx_sellerExId);
			preparedStmt.setString(7, fpx_fpxTxnId);
			preparedStmt.setString(8, fpx_fpxTxnId);
			preparedStmt.setString(9, fpx_fpxTxnId);
			preparedStmt.setString(10, fpx_fpxTxnId);
			preparedStmt.setString(11, fpx_fpxTxnId);
			preparedStmt.setString(12, fpx_fpxTxnId);

			preparedStmt.execute();
		} catch (Exception e) {
			myLogger.error("ERROR addUpdateFPX_Records:" + e.getMessage());
		} finally {
			if (preparedStmt != null)
				preparedStmt.close();
			if (conn != null)
				conn.close();
			if (db != null)
				db.close();
		}
	}

	public String getStatusBayaranRPP(String sellerOrderNo) throws SQLException {
		String out = "";

		Db db = null;
		Connection conn = null;
		PreparedStatement preparedStmt = null;
		String sql = "";
		try {
			db = new Db();
			conn = db.getConnection();
			sql = "SELECT no_tempahan,status_bayaran FROM rpp_permohonan WHERE id=?";
			preparedStmt = conn.prepareStatement(sql);
			preparedStmt.setString(1, sellerOrderNo);
			ResultSet rs = preparedStmt.executeQuery();
			if (rs.next()) {
				out = rs.getString("status_bayaran");
			}
			// IF status = Y THEN LOG TO TABLE
			if ("Y".equals(out)) {
				sql = "UPDATE payment_transaction set flagStatus='Y' where sellerOrderNo=? ";
				preparedStmt = conn.prepareStatement(sql);
				preparedStmt.setString(1, sellerOrderNo);
				preparedStmt.execute();
			}

		} catch (Exception e) {
			myLogger.error("ERROR getStatusBayaranRPP:" + e.getMessage());
		} finally {
			if (preparedStmt != null)
				preparedStmt.close();
			if (conn != null)
				conn.close();
			if (db != null)
				db.close();
		}
		return out;
	}

	public void doAddFPXLog(String fpxTxnId, String sellerOrderNo,
			String info_transaksi) throws SQLException {

		Db db = null;
		Connection conn = null;
		PreparedStatement preparedStmt = null;
		String sql = "";
		try {
			db = new Db();
			conn = db.getConnection();
			sql = "INSERT INTO fpx_audit (user_login,fpxTxnId,sellerOrderNo,info_transaksi,tarikh_transaksi) "
					+ "VALUES (?,?,?,?,now())";

			preparedStmt = conn.prepareStatement(sql);
			preparedStmt.setString(1, this.userId);
			preparedStmt.setString(2, fpxTxnId);
			preparedStmt.setString(3, sellerOrderNo);
			preparedStmt.setString(4, info_transaksi);
			preparedStmt.execute();
		} catch (Exception e) {
			// LOG TO FILE
			myLogger.error(" *** ERROR INSERT FPX AUDIT ***");
			myLogger.error("fpxTxnId:" + fpxTxnId);
			myLogger.error("sellerOrderNo:" + sellerOrderNo);
			myLogger.error("info_transaksi:" + info_transaksi);
		} finally {
			if (preparedStmt != null)
				preparedStmt.close();
			if (conn != null)
				conn.close();
			if (db != null)
				db.close();
		}
	}
	
	public String getStatusBayaranRPPLondon(String sellerOrderNo) throws SQLException {
		String out = "";

		Db db = null;
		Connection conn = null;
		PreparedStatement preparedStmt = null;
		String sql = "";
		try {
			db = new Db();
			conn = db.getConnection();
			sql = "SELECT no_tempahan,flag_bayar FROM rpp_rekod_tempahan_london WHERE id=?";
			preparedStmt = conn.prepareStatement(sql);
			preparedStmt.setString(1, sellerOrderNo);
			ResultSet rs = preparedStmt.executeQuery();
			if (rs.next()) {
				out = rs.getString("flag_bayar");
			}
			// IF status = Y THEN LOG TO TABLE
			if ("Y".equals(out)) {
				sql = "UPDATE payment_transaction set flagStatus='Y' where sellerOrderNo=? ";
				preparedStmt = conn.prepareStatement(sql);
				preparedStmt.setString(1, sellerOrderNo);
				preparedStmt.execute();
			}

		} catch (Exception e) {
			myLogger.error("ERROR getStatusBayaranRPP:" + e.getMessage());
		} finally {
			if (preparedStmt != null)
				preparedStmt.close();
			if (conn != null)
				conn.close();
			if (db != null)
				db.close();
		}
		return out;
	}
	
	/** ADD BY PEJE 28022017 - REGISTER FPX REQUEST 
	 * @param mp **/
	public void registerFPXRequest(String sellerId, String sellerExId, String sellerOrderNo, String sellerExOrderNo,
			String txnAmount, String productDesc, String flagModul, MyPersistence mp) {

		try {
			mp.begin();
			FPXRecordsRequest recordsRequest = new FPXRecordsRequest();
			recordsRequest.setSellerId(sellerId);
			recordsRequest.setSellerExId(sellerExId);
			recordsRequest.setSellerOrderNo(sellerOrderNo);
			recordsRequest.setSellerExOrderNo(sellerExOrderNo);
			recordsRequest.setTxnAmount(txnAmount);
			recordsRequest.setProductDesc(productDesc);
			recordsRequest.setFlagModul(flagModul);
			mp.persist(recordsRequest);
			mp.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}	
	
	/** REQUERY FPX 
	 * FUNCTION CREATE FOR SCHEDULER - UPDATE FPXRECORDREQUEST.
	 * FOR OTHER USE PLEASE REVISE USING DIFFERENT FUNCTION
	 * @return 
	 * **/
	public FPXRecords reQueryFPX(String sellerOrderNo, String sellerExOrderNo, String txnAmount) {
		successRequery = false;
		FPXRecords fpxRecords = null;
		
		String fpx_msgType=null;
		String fpx_msgToken=null;
		String fpx_sellerExId=null;
		String fpx_sellerExOrderNo=null;
		String fpx_sellerTxnTime=null;
		String fpx_sellerOrderNo=null;
		String fpx_sellerId=null;
		String fpx_sellerBankCode=null;
		String fpx_txnCurrency=null;
		String fpx_txnAmount=null;
		String fpx_buyerEmail=null;
		String fpx_checkSum=null;
		String fpx_buyerName=null;
		String fpx_buyerBankId=null;
		String fpx_buyerBankBranch=null;
		String fpx_buyerAccNo=null;
		String fpx_buyerId=null;
		String fpx_makerName=null;
		String fpx_buyerIban=null;
		String fpx_productDesc=null;
		String fpx_version=null;
		String final_checkSum=null;
		
		try {
			fpx_msgType = "AE"; 
			fpx_msgToken = "01";
			fpx_sellerExId = ResourceBundle.getBundle("dbconnection").getString("FPX_SELLER_EX_ID");
			fpx_sellerId = ResourceBundle.getBundle("dbconnection").getString("FPX_SELLER_ID");
			
			fpx_sellerExOrderNo = sellerExOrderNo;//MERCHANT ORDER NO
			fpx_sellerOrderNo = sellerOrderNo;
			
			fpx_txnAmount = txnAmount;
			
			fpx_sellerTxnTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			fpx_sellerBankCode = "01";
			fpx_txnCurrency = "MYR";
			
			fpx_buyerEmail="";
			fpx_buyerName="";
			fpx_buyerBankId="";
			fpx_buyerBankBranch = "";
			fpx_buyerAccNo="";
			fpx_buyerId="";
			fpx_makerName="";
			fpx_buyerIban="";
			fpx_productDesc="X";
			fpx_version="7.0";
		
			fpx_checkSum=fpx_buyerAccNo+"|"+fpx_buyerBankBranch+"|"+fpx_buyerBankId+"|"+fpx_buyerEmail+"|"+fpx_buyerIban+"|"+fpx_buyerId+"|"+fpx_buyerName+"|";
			fpx_checkSum+=fpx_makerName+"|"+fpx_msgToken+"|"+fpx_msgType+"|"+fpx_productDesc+"|"+fpx_sellerBankCode+"|"+fpx_sellerExId+"|";
			fpx_checkSum+=fpx_sellerExOrderNo+"|"+fpx_sellerId+"|"+fpx_sellerOrderNo+"|"+fpx_sellerTxnTime+"|"+fpx_txnAmount+"|"+fpx_txnCurrency+"|"+fpx_version;
			
			String keyPath = ResourceBundle.getBundle("dbconnection").getString("FPX_SELLER_ID");
			
			final_checkSum = FPXPkiImplementation.signData(keyPath, fpx_checkSum,"SHA1withRSA");
			
			//GET ALL DATA TO MAP & SEND			
			///SEND HTTP
			String url = "https://www.mepsfpx.com.my/FPXMain/sellerNVPTxnStatus.jsp";
			URL obj = new URL(url);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
			
			con.setRequestMethod("POST"); // PUT is another valid option
			con.setDoOutput(true);
			
			StringBuffer sb = new StringBuffer();
			
			Map<String,String> arguments = new HashMap<>();
			arguments.put("fpx_msgType",fpx_msgType);
			arguments.put("fpx_msgToken", fpx_msgToken); 
			arguments.put("fpx_sellerExId", fpx_sellerExId); 
			arguments.put("fpx_sellerId", fpx_sellerId); 
			arguments.put("fpx_sellerExOrderNo", fpx_sellerExOrderNo); 
			arguments.put("fpx_sellerTxnTime", fpx_sellerTxnTime); 
			arguments.put("fpx_sellerOrderNo", fpx_sellerOrderNo); 
			arguments.put("fpx_sellerBankCode", fpx_sellerBankCode); 
			arguments.put("fpx_txnCurrency", fpx_txnCurrency); 
			arguments.put("fpx_txnAmount", fpx_txnAmount); 
			arguments.put("fpx_buyerEmail", fpx_buyerEmail); 
			
			arguments.put("fpx_buyerName", fpx_buyerName); 
			arguments.put("fpx_buyerBankId", fpx_buyerBankId); 
			arguments.put("fpx_buyerBankBranch", fpx_buyerBankBranch); 
			arguments.put("fpx_buyerAccNo", fpx_buyerAccNo); 
			arguments.put("fpx_buyerId", fpx_buyerId); 
			arguments.put("fpx_makerName", fpx_makerName); 
			arguments.put("fpx_buyerIban", fpx_buyerIban);
			
			arguments.put("fpx_productDesc", fpx_productDesc); 
			
			arguments.put("fpx_version", fpx_version); 
			
			arguments.put("fpx_checkSum", final_checkSum); 
			
		
			for(Map.Entry<String,String> entry : arguments.entrySet()) {
				sb.append(entry.getKey() + "=" + entry.getValue());
				sb.append("&");
			}
			
			//NOW SEND DATA			
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(sb.toString());
			wr.flush();
			wr.close();

			int responseCode = con.getResponseCode();
			
			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				
				//READ RESPONSE
				boolean transactionExist = false;
				fpxRecords = new FPXRecords();
				String[] parts = response.toString().split("&");				
				for (String r:parts) {
				
					String fieldName = StringUtils.substringBefore(r, "=").trim();
					String value = StringUtils.substringAfter(r, "=").trim();
					
					if ("fpx_fpxTxnId".equals(fieldName)) {
						fpxRecords.setId(value);
						if (!"".equals(value))
							transactionExist = true;
					}					
					if ("fpx_buyerBankBranch".equals(fieldName)) {
						if (!"".equals(value))
							fpxRecords.setBuyerBankBranch(value);
					}
					if ("fpx_buyerBankId".equals(fieldName)) {
						if (!"".equals(value))
							fpxRecords.setBuyerBankId(value);
					}
					if ("fpx_buyerIban".equals(fieldName)) {
						if (!"".equals(value))
							fpxRecords.setBuyerIban(value);
					}
					if ("fpx_buyerId".equals(fieldName)) {
						if (!"".equals(value))
							fpxRecords.setBuyerId(value);
					}
					if ("fpx_buyerName".equals(fieldName)) {
						if (!"".equals(value))
							fpxRecords.setBuyerName(value);
					}
					if ("fpx_creditAuthCode".equals(fieldName)) {
						if (!"".equals(value))
							fpxRecords.setCreditAuthCode(value);
					}
					if ("fpx_creditAuthNo".equals(fieldName)) {
						if (!"".equals(value))
							fpxRecords.setCreditAuthNo(value);
					}
					if ("fpx_debitAuthCode".equals(fieldName)) {
						if (!"".equals(value))
							fpxRecords.setDebitAuthCode(value);
					}
					if ("fpx_debitAuthNo".equals(fieldName)) {
						if (!"".equals(value))
							fpxRecords.setDebitAuthNo(value);
					}
					if ("fpx_fpxTxnTime".equals(fieldName)) {
						if (!"".equals(value))
							fpxRecords.setFpxTxnTime(value);
					}
					if ("fpx_makerName".equals(fieldName)) {
						if (!"".equals(value))
							fpxRecords.setMakerName(value);
					}
					if ("fpx_msgToken".equals(fieldName)) {
						if (!"".equals(value))
							fpxRecords.setMsgToken(value);
					}
					if ("fpx_msgType".equals(fieldName)) {
						if (!"".equals(value))
							fpxRecords.setMsgType(value);
					}
					if ("fpx_sellerExId".equals(fieldName)) {
						if (!"".equals(value))
							fpxRecords.setSellerExId(value);
					}
					if ("fpx_sellerExOrderNo".equals(fieldName)) {
						if (!"".equals(value))
							fpxRecords.setSellerExOrderNo(value);
					}
					if ("fpx_sellerId".equals(fieldName)) {
						if (!"".equals(value))
							fpxRecords.setSellerId(value);
					}
					if ("fpx_sellerOrderNo".equals(fieldName)) {
						if (!"".equals(value))
							fpxRecords.setSellerOrderNo(value);
					}
					if ("fpx_sellerTxnTime".equals(fieldName)) {
						if (!"".equals(value))
							fpxRecords.setSellerTxnTime(value);
					}
					if ("fpx_txnAmount".equals(fieldName)) {
						if (!"".equals(value))
							fpxRecords.setTxnAmount(value);
					}
					if ("fpx_txnCurrency".equals(fieldName)) {
						if (!"".equals(value))
							fpxRecords.setTxnCurrency(value);
					}
				}
				//IF transactionExist = false : means fpx_fpxTxnId is null and record tidak wujud di FPX. so set fpxRecords as null since no record in FPX
				if (!transactionExist)
					fpxRecords = null;
			}
			successRequery = true;
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return fpxRecords;
	}

	public boolean isSuccessRequery() {
		return successRequery;
	}

	public void setSuccessRequery(boolean successRequery) {
		this.successRequery = successRequery;
	}
}
