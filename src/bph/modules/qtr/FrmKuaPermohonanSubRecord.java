package bph.modules.qtr;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import lebah.portal.action.Command;

import org.apache.log4j.Logger;

import portal.module.entity.Users;
import portal.module.entity.UsersJob;
import portal.module.entity.UsersSpouse;
import bph.entities.kod.Agensi;
import bph.entities.kod.BadanBerkanun;
import bph.entities.kod.Bandar;
import bph.entities.kod.Bangsa;
import bph.entities.kod.Etnik;
import bph.entities.kod.Gelaran;
import bph.entities.kod.GredPerkhidmatan;
import bph.entities.kod.Jantina;
import bph.entities.kod.Jawatan;
import bph.entities.kod.JawatanAPMM;
import bph.entities.kod.JawatanATM;
import bph.entities.kod.JawatanPDRM;
import bph.entities.kod.JenisPengenalan;
import bph.entities.kod.JenisPerkhidmatan;
import bph.entities.kod.KelasPerkhidmatan;
import bph.entities.kod.LokasiPermohonan;
import bph.entities.kod.Negeri;
import bph.entities.kod.Poskod;
import bph.entities.kod.Status;
import bph.entities.kod.StatusPekerjaan;
import bph.entities.kod.StatusPerkahwinan;
import bph.entities.kod.StatusPerkhidmatan;
import bph.entities.qtr.KuaAgihan;
import bph.entities.qtr.KuaKelainanUpaya;
import bph.entities.qtr.KuaPermohonan;
import bph.entities.qtr.KuaPinjamanPemohon;
import bph.entities.qtr.KuaSenaraiHitam;
import bph.utils.DataUtil;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class FrmKuaPermohonanSubRecord extends FrmKuaPermohonanRecord {

	private static final long serialVersionUID = -302245884484195171L;
	static Logger myLogger = Logger.getLogger("bph/modules/qtr/FrmKuaPermohonanSubRecord");
	private DataUtil dataUtil;
	private KuartersUtils kuaUtil = new KuartersUtils();
	private String uploadDir = ResourceBundle.getBundle("dbconnection").getString("folder");
	private MyPersistence mp;

	@SuppressWarnings("unchecked")
	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);
		userId = (String) request.getSession().getAttribute("_portal_login");
		setRecordOnly(true);
		setHideDeleteButton(true);
		setDisableDefaultButton(true);
		setDisableBackButton(true);
		setDisableKosongkanUpperButton(true);
		setDisableUpperBackButton(true);
		setDisableSaveAddNewButton(true);
		setDisableAddNewRecordButton(false);
		userRole = (String) request.getSession().getAttribute("_portal_role");
		addFilter("pemohon.id = '" + userId + "'");
		addFilter("status.id NOT in ('1431903258428','1419601227598','1431327994521','1431327994524')");
		context.remove("blacklisted");
		context.remove("currentRoleQTR");
		context.put("Awam", true);
		//start check jika staff kuarters (13/7/2017)
		if ("(QTR) Penyedia".equalsIgnoreCase(userRole)|| "(QTR) Pelulus".equalsIgnoreCase(userRole)) {
			context.put("qtrstaff", "true");
		} else {
			context.put("qtrstaff", "false");
		}
		//end check jika staff kuarters (13/7/2017)
		try {
			mp = new MyPersistence();
			userId = (String) request.getSession().getAttribute("_portal_login");
			Users u = (Users) mp.find(Users.class, userId);
			context.put("users", u);
			List<KuaPermohonan> kpList = mp.list("SELECT kp FROM KuaPermohonan kp WHERE kp.pemohon.id = '"+ userId + "'");
			// +
			// "' and kp.status.id NOT IN ('1431903258428','1419601227598','1431327994521','1431327994524')");
			boolean a = false;

			// AZAM CHANGE ON 16/1/2016
			for (KuaPermohonan r : kpList) {
				if (r.getStatus() != null) {
					if ("1431903258428".equals(r.getStatus().getId()) // KELUAR KUARTERS
						|| "1419601227598".equals(r.getStatus().getId())// PERMOHONAN DITOLAK
						|| "1431327994521".equals(r.getStatus().getId())// TOLAK TAWARAN
						|| "1431327994524".equals(r.getStatus().getId()) // BATAL TAWARAN
					) {
						a = false;
					} else {
						a = true;
						break;
					}
				}
			}
			
			// start check blacklisted (25/7/2016)
			Boolean pemohonBlacklisted = false;
			KuaSenaraiHitam ksh = (KuaSenaraiHitam) mp.get("SELECT c FROM KuaSenaraiHitam c WHERE c.pemohon.id = '"+ userId + "'");
			if (ksh != null) {
				pemohonBlacklisted = true;
				context.put("blacklisted", pemohonBlacklisted);
			}
			if (pemohonBlacklisted == true) {
				setDisableAddNewRecordButton(true);
			} else {
				setDisableAddNewRecordButton(a);
			}
			//end check blacklisted(25/7/2016)
			
			List<KuaPermohonan> listPermohonan = mp.list("SELECT kp FROM KuaPermohonan kp WHERE kp.pemohon.id = '"+ userId + "'");
			context.put("listPermohonan", listPermohonan);
			context.put("selectNegeriPekerjaan", dataUtil.getListNegeri());
			context.put("selectNegeriPinjaman", dataUtil.getListNegeri());
			context.put("selectLokasiPermohonan", dataUtil.getListLokasiPermohonan());
			context.put("newRecord", "true");
			getNoGiliran();
		} catch (Exception e) {
			System.out.println("Error begin : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
	}

	@Override
	public void getRelatedData(KuaPermohonan r) {
		boolean kelulusan = false;
		try {
			mp = new MyPersistence();
			UsersJob uj = (UsersJob) mp.get("SELECT uj FROM UsersJob uj WHERE uj.users.id = '"+ r.getPemohon().getId() + "'");
			context.remove("resultPermohonanCompletion");
			context.put("resultPermohonanCompletion", getCompletionPermohonan(mp, r.getId()));
			KuaPermohonan kp = (KuaPermohonan) mp.find(KuaPermohonan.class, r.getId());
			context.remove("statusPerkahwinan");
			context.put("statusPerkahwinan", getStatusPerkahwinanPemohon(mp, getParam("idPermohonan")));
			context.put("r", kp);
			context.put("statusPermohonan", kp.getStatus().getId());
			
			if(kp.getStatus().getId().equalsIgnoreCase("1419483289675")||kp.getStatus().getId().equalsIgnoreCase("1419483289678")){
				context.put("tukarLokasi", true);
			}
			
			if(kp.getStatus().getId().equalsIgnoreCase("1423101446117")){
				context.put("penghuni", true);
			}
			
			if ("true".equals(kp.getKelulusan1())&& "true".equals(kp.getKelulusan3())) {
				context.put("disableAddNewRecordButton", true);
				kelulusan = true;
			}

			context.put("kelulusan", kelulusan);
			context.remove("kpp");
			
			KuaPinjamanPemohon kpp = (KuaPinjamanPemohon) mp.get("SELECT kpp FROM KuaPinjamanPemohon kpp WHERE kpp.users.id = '"+ userId + "'");

			if (kpp != null) {
				context.put("kpp", mp.find(KuaPinjamanPemohon.class, kpp.getId()));
			}

			context.put("uj", uj);
			context.put("newRecord", "false");

			// AZAM CHANGE ON 16/1/2016
			String idStatus = "";
			if (r.getStatus() != null) {
				idStatus = r.getStatus().getId();
			}

			if (idStatus.equalsIgnoreCase("1431405647299")|| idStatus.equalsIgnoreCase("1419483289678")) {
				context.put("kemaskini", "true");
				context.put("menunggu", "true");
			} else {
				context.put("kemaskini", "false");
			}

			//start checking if area Kuala Lumpur
			String NegeriArea = "";
			if (kp.getLokasi().getBandar().getNegeri().getId().toString() != null) {
				NegeriArea = kp.getLokasi().getId().toString();
			}
			if (NegeriArea.equalsIgnoreCase("02")) {
				context.put("areaKL", true);
			} else {
				context.put("areaKL", false);
			}
			//end checking area Kuala Lumpur

			Calendar mydate = new GregorianCalendar();
			String mystring = "December 31, 2014";
			Date thedate = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).parse(mystring);
			mydate.setTime(thedate);
			// Date tarikhMohon = kp.getTarikhPermohonan();
			// if(tarikhMohon.after(thedate)){
			// //jika permohonan bermula dari 1.1.2015,perlu semak radius
			// }
		} catch (Exception e) {
			System.out.println("Error getRelatedData : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
	}

	@Command("newPermohonan")
	public String newPermohonan() {
		return getPath() + "/new/start.vm";
	}

	@Command("getPermohonan")
	public String getPermohonan() {
		boolean kelulusan = false;
		try {
			mp = new MyPersistence();
			String idPermohonan = getParam("idPermohonan");

			KuaPermohonan kp = (KuaPermohonan) mp.find(KuaPermohonan.class,
					idPermohonan);

			if ("true".equals(kp.getKelulusan1())
					&& "true".equals(kp.getKelulusan3())) {
				context.put("disableAddNewRecordButton", true);
				kelulusan = true;
			}
			context.put("kelulusan", kelulusan);
			context.put("r", kp);
			context.put("kpp",mp.get("SELECT pp FROM KuaPinjamanPemohon pp WHERE pp.users.id = '"+ userId + "'"));
		} catch (Exception e) {
			System.out.println("Error getPermohonan : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/entry_sub/entry_permohonan.vm";
	}

	@Command("tarikhJangkaSiap")
	public String tarikhJangkaSiap() {
		return getPath() + "/sub_page/pinjaman/tarikhJangkaSiap.vm";
	}

	/*--------------------------------------------------------------- SEMAK KELAYAKAN 1 ---------------------------------------------------------------*/
	@Command("kelulusan1")
	public String kelulusan1() {// fungsi untuk semak kelayakan pemohon
		Boolean kelulusan = false;
		boolean newRecord = false;
		boolean newRecordKerja = false;
		boolean newRecordPinjaman = false;
		boolean result = false;// tarikh jangka siap
		boolean result2 = false;// radius rumah dimiliki
		boolean result3 = false;// prima/ppa1m mesti diduduki
		int jenisAktiviti = 2;
		int jenisAktivitiKerja = 2;
		int jenisAktivitiPinjaman = 2;
		String idPermohonan = getParam("idPermohonan");
		//boolean checkPoskod = false;// poskod ikut state
		boolean checkPoskod = true;// poskod ikut state
		// rozai add 31-5-2016
		int valueSemakanTarikhPermohonan = 0;
		valueSemakanTarikhPermohonan = compareDatePermohonanDateRadius(idPermohonan);

		try {
			mp = new MyPersistence();
			LokasiPermohonan lokasiPermohonan = (LokasiPermohonan) mp.find(LokasiPermohonan.class, getParam("idLokasiPermohonan"));
			Bandar bandarLokasi = (Bandar) mp.find(Bandar.class,getParam("idBandarPekerjaan"));
			context.put("checkPoskod", checkPoskod);
			
//			Poskod poskod = (Poskod) mp.find(Poskod.class,
//					getParam("poskodPekerjaan"));
//			if (poskod != null) {
//				if (poskod.getIdBandar().equalsIgnoreCase(bandarLokasi.getId())) {
//					checkPoskod = true;
//					context.put("checkPoskod", checkPoskod);
//				} else {
//					checkPoskod = false;
//					context.put("checkPoskod", checkPoskod);
//				}
//			} else {
//				checkPoskod = false;
//				context.put("checkPoskod", checkPoskod);
//			}

			if ("15".equals(lokasiPermohonan.getNegeri().getId())|| "16".equals(lokasiPermohonan.getNegeri().getId())|| "01".equals(lokasiPermohonan.getNegeri().getId())) 
			{
				if (bandarLokasi.getNegeri().getId().equals(lokasiPermohonan.getNegeri().getId())) {
					result = true;
					kelulusan = true;
					context.put("kelulusan", kelulusan);
				} else {
					result = false;
					kelulusan = false;
					context.put("kelulusan", kelulusan);
				}
//				if ("16".equals(lokasiPermohonan.getNegeri().getId())
//						&& bandarLokasi.getId().equals("1052")) {
//					// PUTRAJAYA--CYBERJAYA
//					result = true;
//					kelulusan = true;
//					context.put("kelulusan", kelulusan);
//				}
//
//				if ("10".equals(lokasiPermohonan.getNegeri().getId())
//						&& bandarLokasi.getId().equals("1401")) {
//					// PETALING JAYA--KUALA LUMPUR
//					result = true;
//					kelulusan = true;
//					context.put("kelulusan", kelulusan);
//				}

			} else if ("05".equals(lokasiPermohonan.getNegeri().getId())) { // KLIA
				// NILAI //SEPANG //B.B.ENSTEK
				if (bandarLokasi.getId().equals("0517")|| bandarLokasi.getId().equals("1035")|| bandarLokasi.getId().equals("0543")) {
				//KLIA
				//if (bandarLokasi.getId().equals("0546")) {
					result = true;
					kelulusan = true;
					context.put("kelulusan", kelulusan);
				} else {
					result = false;
					kelulusan = false;
					context.put("kelulusan", kelulusan);
				}
				
				/* WILAYAH PERSEKUTUAN KUALA LUMPUR(KUALA LUMPUR,AMPANG HILIR,CHERAS) */
			} else if ("14".equals(lokasiPermohonan.getNegeri().getId())) {
				if (bandarLokasi.getId().equals("1401")|| bandarLokasi.getId().equals("1404")|| bandarLokasi.getId().equals("1416")) 
				{
					result = true;
					kelulusan = true;
					context.put("kelulusan", kelulusan);
				}else{
					result = false;
					kelulusan = false;
					context.put("kelulusan", kelulusan);
				}
			}
			
			/* Kiraan Radius dan Tarikh Jangka Siap Kuarters */
			int pinjamanPerumahan = getParamAsInteger("valuePinjamanPerumahan");
			String jenisPerumahan = null;
			String pembiayaan = null;
			String pembelian = null;
			int statusRumah = 0;
			Date t = null;

			if (pinjamanPerumahan == 1) // checkPinjaman(ada)
			{
				jenisPerumahan = getParam("jenisPerumahan");
				pembiayaan = getParam("valuePembiayaan");
				pembelian = getParam("valuePembelian");
				statusRumah = getParamAsInteger("valueStatusRumah");
				if ("swasta".equals(jenisPerumahan)) // checkPerumahan(swasta)
				{
					if ("bank".equals(pembiayaan)) // checkPembiayaan
					{
						if (statusRumah == 1) // checkPembinaan(siap)
						{
							String rumahLuarRadius = checkJarakLangitudeLotitude(mp);
							if (valueSemakanTarikhPermohonan < 0)
							{
								rumahLuarRadius = "true";
							}
							
							if (rumahLuarRadius == "true") // luar radius
							{
								if ("sendiri".equals(pembelian)) // checkPembelian(sendiri)
								{
									result2 = true;
									
								} else if ("pasangan".equals(pembelian)) // checkPembelian(pasangan)
								{
									result2 = true;
									
								} else if ("bersama".equals(pembelian)) // checkPembelian(bersama)
								{
									result2 = true;
								}
								
							} else {
								if ("sendiri".equals(pembelian)) // checkPembelian(sendiri)
								{
									result2 = true;
								} else if ("pasangan".equals(pembelian)) // checkPembelian(pasangan)
								{

									result2 = true;
								} else if ("bersama".equals(pembelian)) // checkPembelian(bersama)
								{
									result2 = true;
								}
							}
							context.put("result2", result2);
						} else if (statusRumah == 2) // checkPembinaan(dalam
						// pembinaan)
						{
							String[] tarikhJangkaSiap = getParam(
									"tarikhJangkaSiap").split(",");
							String tarikhJangkaSiapBaru = "01-"
									+ getMonth(tarikhJangkaSiap[0].trim())
									+ "-" + tarikhJangkaSiap[1].trim();
							int days = 0;

							try {
								t = new SimpleDateFormat("dd-MM-yyyy")
										.parse(tarikhJangkaSiapBaru);
								days = Util.daysBetween(new Date(), t);
							} catch (ParseException e1) {
								e1.printStackTrace();
							}

							if (days < 365) // kurang dari setahun
							{
								String rumahLuarRadius = checkJarakLangitudeLotitude(mp);
								// rozai add 31-5-2016
								if (valueSemakanTarikhPermohonan < 0) {
									rumahLuarRadius = "true";
								}
								;
								if (rumahLuarRadius == "true") // luar radius
								{
									if ("sendiri".equals(pembelian)) // checkPembelian(sendiri)
									{
										result2 = true;
									} else if ("pasangan".equals(pembelian)) // checkPembelian(pasangan)
									{
										result2 = true;
									} else if ("bersama".equals(pembelian)) // checkPembelian(bersama)
									{
										result2 = true;
									}
								} else // dalam radius
								{
									if ("sendiri".equals(pembelian)) // checkPembelian(sendiri)
									{
										result2 = true;
									} else if ("pasangan".equals(pembelian)) // checkPembelian(pasangan)
									{
										result2 = true;
									} else if ("bersama".equals(pembelian)) // checkPembelian(bersama)
									{
										result2 = true;
									}
								}
								context.put("result2", result2);
							} else // lebih dari setahun
							{
								String rumahLuarRadius = checkJarakLangitudeLotitude(mp);
								// rozai add 31-5-2016
								if (valueSemakanTarikhPermohonan < 0) {
									rumahLuarRadius = "true";
								}
								;
								if (rumahLuarRadius == "true") // luar radius
								{
									if ("sendiri".equals(pembelian)) // checkPembelian(sendiri)
									{
										result2 = true;
									} else if ("pasangan".equals(pembelian)) // checkPembelian(pasangan)
									{
										result2 = true;
									} else if ("bersama".equals(pembelian)) // checkPembelian(bersama)
									{
										result2 = true;
									}
									context.put("result2", result2);
								} else // dalam radius
								{
									if ("sendiri".equals(pembelian)) // checkPembelian(sendiri)
									{
										result2 = true;
									} else if ("pasangan".equals(pembelian)) // checkPembelian(pasangan)
									{
										result2 = true;
									} else if ("bersama".equals(pembelian)) // checkPembelian(bersama)
									{
										result2 = true;
									}
								}

							}
						}
					} else // if pinjaman kerajaan
					{
						if (statusRumah == 1) // checkPembinaan(siap)
						{
							String rumahLuarRadius = checkJarakLangitudeLotitude(mp);
							// rozai add 31-5-2016
							if (valueSemakanTarikhPermohonan < 0) {
								rumahLuarRadius = "true";
							}
							;
							if (rumahLuarRadius == "true") // luar radius
							{
								if ("sendiri".equals(pembelian)) // checkPembelian(sendiri)
								{
									result2 = true;
								} else if ("pasangan".equals(pembelian)) // checkPembelian(pasangan)
								{
									result2 = true;
								} else if ("bersama".equals(pembelian)) // checkPembelian(bersama)
								{
									result2 = true;
								}
							} else {
								if ("sendiri".equals(pembelian)) // checkPembelian(sendiri)
								{
									result2 = false;
								} else if ("pasangan".equals(pembelian)) // checkPembelian(pasangan)
								{
									result2 = true;
								} else if ("bersama".equals(pembelian)) // checkPembelian(bersama)
								{
									result2 = false;
								}
							}
							context.put("result2", result2);
						} else if (statusRumah == 2) // checkPembinaan(dalam
						// pembinaan)
						{
							String[] tarikhJangkaSiap = getParam(
									"tarikhJangkaSiap").split(",");
							String tarikhJangkaSiapBaru = "01-"
									+ getMonth(tarikhJangkaSiap[0].trim())
									+ "-" + tarikhJangkaSiap[1].trim();
							int days = 0;

							try {
								t = new SimpleDateFormat("dd-MM-yyyy")
										.parse(tarikhJangkaSiapBaru);
								days = Util.daysBetween(new Date(), t);
							} catch (ParseException e1) {
								e1.printStackTrace();
							}

							if (days < 365) // kurang dari setahun
							{
								String rumahLuarRadius = checkJarakLangitudeLotitude(mp);
								// rozai add 31-5-2016
								if (valueSemakanTarikhPermohonan < 0) {
									rumahLuarRadius = "true";
								}
								;
								if (rumahLuarRadius == "true") // luar radius
								{
									if ("sendiri".equals(pembelian)) // checkPembelian(sendiri)
									{
										result2 = true;
									} else if ("pasangan".equals(pembelian)) // checkPembelian(pasangan)
									{
										result2 = true;

									} else if ("bersama".equals(pembelian)) // checkPembelian(bersama)
									{
										result2 = true;
									}
								} else // dalam radius
								{
									if ("sendiri".equals(pembelian)) // checkPembelian(sendiri)
									{
										result2 = false;
										result = false;// popup tarikh
									} else if ("pasangan".equals(pembelian)) // checkPembelian(pasangan)
									{
										result2 = true;
										result = true;// popup tarikh
									} else if ("bersama".equals(pembelian)) // checkPembelian(bersama)
									{
										result2 = false;
										result = false;// popup tarikh
									}
								}
								context.put("result2", result2);
							} else // lebih dari setahun
							{
								String rumahLuarRadius = checkJarakLangitudeLotitude(mp);
								// rozai add 31-5-2016
								if (valueSemakanTarikhPermohonan < 0) {
									rumahLuarRadius = "true";
								}
								;
								if (rumahLuarRadius == "true") // luar radius
								{
									if ("sendiri".equals(pembelian)) // checkPembelian(sendiri)
									{
										result2 = true;
									} else if ("pasangan".equals(pembelian)) // checkPembelian(pasangan)
									{
										result2 = true;
									} else if ("bersama".equals(pembelian)) // checkPembelian(bersama)
									{
										result2 = true;
									}
									context.put("result2", result2);
								} else // dalam radius
								{
									if ("sendiri".equals(pembelian)) // checkPembelian(sendiri)
									{
										result2 = true;
									} else if ("pasangan".equals(pembelian)) // checkPembelian(pasangan)
									{
										result2 = true;
									} else if ("bersama".equals(pembelian)) // checkPembelian(bersama)
									{
										result2 = true;
									}
								}

							}
						}
					}
				} else // checkPerumahan(PRIMA/PPA1M)
				{
					if (statusRumah == 1) // checkPembinaan(siap)
					{
						String rumahLuarRadius = checkJarakLangitudeLotitude(mp);
						// rozai add 31-5-2016
						if (valueSemakanTarikhPermohonan < 0) {
							rumahLuarRadius = "true";
						}
						;
						if (rumahLuarRadius == "true")// luar radius
						{
							if ("sendiri".equals(pembelian)) // checkPembelian(sendiri)
							{
								result2 = true;
							} else if ("pasangan".equals(pembelian)) // checkPembelian(pasangan)
							{
								result2 = true;
							} else if ("bersama".equals(pembelian)) // checkPembelian(bersama)
							{
								result2 = true;
							}
						} else {
							if ("sendiri".equals(pembelian)) // checkPembelian(sendiri)
							{
								result2 = false;
							} else if ("pasangan".equals(pembelian)) // checkPembelian(pasangan)
							{
								result2 = false;
							} else if ("bersama".equals(pembelian)) // checkPembelian(bersama)
							{
								result2 = false;
							}
						}
						context.put("result2", result2);
					} else if (statusRumah == 2) // checkPembinaan(dalam
					// pembinaan)
					{
						String[] tarikhJangkaSiap = getParam("tarikhJangkaSiap")
								.split(",");
						String tarikhJangkaSiapBaru = "01-"
								+ getMonth(tarikhJangkaSiap[0].trim()) + "-"
								+ tarikhJangkaSiap[1].trim();
						int days = 0;

						try {
							t = new SimpleDateFormat("dd-MM-yyyy")
									.parse(tarikhJangkaSiapBaru);
							days = Util.daysBetween(new Date(), t);
						} catch (ParseException e1) {
							e1.printStackTrace();
						}

						if (days < 365) // kurang dari setahun
						{
							String rumahLuarRadius = checkJarakLangitudeLotitude(mp);
							// rozai add 31-5-2016
							if (valueSemakanTarikhPermohonan < 0) {
								rumahLuarRadius = "true";
							}
							;
							if (rumahLuarRadius == "true") // luar radius
							{
								if ("sendiri".equals(pembelian)) // checkPembelian(sendiri)
								{
									result = true;
								} else if ("pasangan".equals(pembelian)) // checkPembelian(pasangan)
								{
									result = true;
								} else if ("bersama".equals(pembelian)) // checkPembelian(bersama)
								{
									result = true;
								}
							} else // dalam radius
							{
								if ("sendiri".equals(pembelian)) // checkPembelian(sendiri)
								{
									result = false;
								} else if ("pasangan".equals(pembelian)) // checkPembelian(pasangan)
								{
									result = true;
								} else if ("bersama".equals(pembelian)) // checkPembelian(bersama)
								{
									result = false;
								}
							}
							context.put("result", result);
						} else // lebih dari setahun
						{
							String rumahLuarRadius = checkJarakLangitudeLotitude(mp);
							// rozai add 31-5-2016
							if (valueSemakanTarikhPermohonan < 0) {
								rumahLuarRadius = "true";
							}
							;
							if (rumahLuarRadius == "true") // luar radius
							{
								if ("sendiri".equals(pembelian)) // checkPembelian(sendiri)
								{
									result2 = true;
								} else if ("pasangan".equals(pembelian)) // checkPembelian(pasangan)
								{
									result2 = true;
								} else if ("bersama".equals(pembelian)) // checkPembelian(bersama)
								{
									result2 = true;
								}
								context.put("result2", result2);
							} else // dalam radius
							{
								if ("sendiri".equals(pembelian)) // checkPembelian(sendiri)
								{
									result2 = true;
									context.put("result", result);
								} else if ("pasangan".equals(pembelian)) // checkPembelian(pasangan)
								{
									result2 = true;
									context.put("result", result);
								} else if ("bersama".equals(pembelian)) // checkPembelian(bersama)
								{
									result2 = true;
									context.put("result", result);
								}
							}

						}
					}
				}
			} else // checkPinjaman(tiada)
			{
				//result = true;
				result2 = true;

				context.put("result", result);
				context.put("result2", result2);

			}

			Users pemohon = (Users) mp.find(Users.class, userId);
			KuaPermohonan kp = null;
			KuaPermohonan permohonan = (KuaPermohonan) mp.get("SELECT p FROM KuaPermohonan p WHERE p.pemohon.id = '"+ userId + "' AND p.status.id = '1419483289675'");
			
			mp.begin();
			Status status = (Status) mp.find(Status.class, "1419483289675");
			if (!"".equals(idPermohonan)) {
				kp = (KuaPermohonan) mp.find(KuaPermohonan.class, idPermohonan);
			} else {
				if (permohonan != null) {
					kp = (KuaPermohonan) mp.find(KuaPermohonan.class,permohonan.getId());
				}
			}

			if (kp == null) {
				// jenisAktiviti = 1;
				newRecord = true;
				kp = new KuaPermohonan();
			} else {
				String statusTerkini = kp.getStatus().getId().toString();
				if ("1419483289675".equals(statusTerkini))// deraf permohonan
				{
					status = (Status) mp.find(Status.class, "1419483289675");
				} else if ("1419483289678".equals(statusTerkini))// permohonan
				// baru
				{
					status = (Status) mp.find(Status.class, "1419483289678");
				} else if ("1431405647299".equals(statusTerkini))// permohonan
				// kemaskini
				{
					status = (Status) mp.find(Status.class, "1431405647299");
				} else {
					status = (Status) mp.find(Status.class, statusTerkini);
				}

				// if (!"".equals(kp.getNoPermohonan()))
				// noPermohonan = kp.getNoPermohonan();
				// if (!"".equals(kp.getNoFail()))
				// noFail = kp.getNoFail();
			}

			kp.setPemohon(pemohon);
			kp.setLokasi(lokasiPermohonan);
			// kp.setNoFail(noFail);
			// kp.setNoPermohonan(noPermohonan);
			//kp.setTarikhMasuk(new Date());
			if (newRecord == false) {
				kp.setTarikhKemaskini(new Date());
			}
			kp.setStatus(status);
			kp.setKelulusan1(kelulusan.toString());

			if (result == true && result2 == true) {
				kp.setKelulusan3("true");
				kp.setPinjaman(1);
			} else {
				kp.setKelulusan3("false");
				kp.setPinjaman(0);
			}

			if (newRecord == true) {
				mp.persist(kp);
			}

			idPermohonan = kp.getId();

			// db.commit(request, "PROCESSING FILE (Permohonan Kuarters) : "+
			// idPermohonan, jenisAktiviti);

			context.put("idPermohonan", idPermohonan);

			UsersJob uj = null;
			UsersJob usersJob = (UsersJob) mp
					.get("SELECT usersJob FROM UsersJob usersJob WHERE usersJob.users.id = '"
							+ userId + "'");
			if (usersJob != null) {
				uj = (UsersJob) mp.find(UsersJob.class,
						usersJob.getId() != null ? usersJob.getId() : "");
			}

			Bandar bandarPekerjaan = (Bandar) mp.find(Bandar.class,
					getParam("idBandarPekerjaan"));

			if (uj == null) {
				jenisAktivitiKerja = 1;
				newRecordKerja = true;
				uj = new UsersJob();
			}

			uj.setUsers(pemohon);
			// uj.setAlamat1(getParam("alamatPekerjaan1"));
			// uj.setAlamat2(getParam("alamatPekerjaan2"));
			// uj.setAlamat3(getParam("alamatPekerjaan3"));
			uj.setPoskod(getParam("poskodPekerjaan"));
			uj.setBandar(bandarPekerjaan);

			context.put("uj", uj);

			if (newRecordKerja == true) {
				mp.persist(uj);
			}

			// db.commit(request, "PROCESSING FILE (Pekerjaan Pemohon) : "+
			// uj.getId(), jenisAktivitiKerja);

			KuaPinjamanPemohon kpp = null;

			KuaPinjamanPemohon pinjaman = (KuaPinjamanPemohon) mp
					.get("SELECT p FROM KuaPinjamanPemohon p WHERE p.users.id = '"
							+ userId + "'");
			if (pinjaman != null)
				kpp = (KuaPinjamanPemohon) mp.find(KuaPinjamanPemohon.class,
						pinjaman.getId() != null ? pinjaman.getId() : "");

			Bandar bandarPinjaman = (Bandar) mp.find(Bandar.class,
					getParam("idBandarPinjaman"));

			if (kpp == null) {
				jenisAktivitiPinjaman = 1;
				newRecordPinjaman = true;
				kpp = new KuaPinjamanPemohon();
			}

			kpp.setUsers(pemohon);
			kpp.setPinjamanPerumahan(pinjamanPerumahan);
			kpp.setStatusPembinaan(statusRumah);
			kpp.setTarikhJangkaSiap(t);
			kpp.setPoskodPinjaman(getParam("poskodPinjaman"));
			kpp.setBandar(bandarPinjaman);
			kpp.setJenisPerumahan(jenisPerumahan);
			kpp.setPembiayaan(pembiayaan);
			kpp.setPembelian(getParam("valuePembelian"));
			kpp.setAlamat1(getParam("alamatPinjaman1"));
			kpp.setAlamat2(getParam("alamatPinjaman2"));
			kpp.setAlamat3(getParam("alamatPinjaman3"));

			if (newRecordPinjaman == true) {
				mp.persist(kpp);
			}

			// db.commit(request, "PROCESSING FILE (Pinjaman Pemohon) : "+
			// uj.getId(), jenisAktivitiPinjaman);

			mp.commit();
			context.remove("resultPermohonanCompletion");
			// context.put("resultPermohonanCompletion",
			// getCompletionPermohonan(kp.getId()));
			// context.put("resultPermohonanCompletion", kelulusan.toString());
		} catch (Exception e) {
			System.out.println("Error kelulusan1 : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		// rozai add 29/3/2016
		// context.remove("resultPermohonanCompletion");
		context.put("resultPermohonanCompletion", getCompletionPermohonan(mp,
				getParam("idPermohonan")));
		return getPath() + "/result/kelulusan1.vm";
	}

	@Command("kelulusan1Retrieve")
	public String kelulusan1Retrieve() {
		try {
			mp = new MyPersistence();
			userId = (String) request.getSession()
					.getAttribute("_portal_login");
			Users u = (Users) mp.find(Users.class, userId);
			context.put("users", u);
			KuaPermohonan kp = (KuaPermohonan) mp.find(KuaPermohonan.class,
					getParam("idPermohonan"));
			context.put("kelulusan", Boolean.parseBoolean(kp.getKelulusan1()));
			context.put("result", true);
			context.put("result2", true);
			context.remove("resultPermohonanCompletion");
			context.put("resultPermohonanCompletion", getCompletionPermohonan(
					mp, getParam("idPermohonan")));
			context.put("selectJenisPengenalan", dataUtil
					.getListFewJenisPengenalan());
			context.put("selectAgama", dataUtil.getListAgama());
			context.put("selectJantina", dataUtil.getListJantina());
			context.put("selectBangsa", dataUtil.getListBangsa());
			context.put("selectEtnik", dataUtil.getListEtnik());
			context.put("selectStatusPerkahwinan", dataUtil
					.getListStatusPerkahwinan());
			context.put("selectNegeri", dataUtil.getListNegeri());
			context.put("selectNegeriSemasa", dataUtil.getListNegeri());
			//context.put("selectGelaran", dataUtil.getListGelaran());
			context.put("users", u);
			
		} catch (Exception e) {
			System.out.println("Error kelulusan1Retrieve : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/result/kelulusan1.vm";
	}

	@Command("getPerakuan")
	public String getPerakuan() {
		try {
			mp = new MyPersistence();

			String kelasLayak = "";
			String kelasDowngrade = "";
			
			KuaPermohonan kp = (KuaPermohonan) mp.find(KuaPermohonan.class,
					getParam("idPermohonan"));
			
			UsersJob pekerjaan = (UsersJob) mp.get("SELECT uj FROM UsersJob uj WHERE uj.users.id = '"+ userId + "'");

			if (pekerjaan != null) {
				String gredKelasKuarters = "";
				String idPerkhidmatan = "";
				String gredJawatan = "";
				// if ( pekerjaan.getIdGredJawatan() != null ) {
				if (pekerjaan.getGredJawatan() != null) { // rozai add
					// 11/11/2015
					gredKelasKuarters = pekerjaan.getGredJawatan().getKelasKuarters();
					
					/*UNTUK KES BADAN BERUNIFORM*/
					idPerkhidmatan=pekerjaan.getKelasPerkhidmatan().getId();
					gredJawatan=pekerjaan.getGredJawatan().getKeterangan();
					if(idPerkhidmatan.equalsIgnoreCase("XA") || 
							idPerkhidmatan.equalsIgnoreCase("VUXA") ||
							//idPerkhidmatan.equalsIgnoreCase("T") || 
							//idPerkhidmatan.equalsIgnoreCase("VUT") || 
							idPerkhidmatan.equalsIgnoreCase("YA") || 
							idPerkhidmatan.equalsIgnoreCase("VUYA") ||
							idPerkhidmatan.equalsIgnoreCase("ZA") ||
							idPerkhidmatan.equalsIgnoreCase("VUZ"))
					{
						String gredUniform=idPerkhidmatan+gredJawatan;
						if(idPerkhidmatan.equalsIgnoreCase("XA") || idPerkhidmatan.equalsIgnoreCase("VUXA")){
							JawatanAPMM jawatan = (JawatanAPMM) mp.get("SELECT j FROM JawatanAPMM j WHERE j.id = '"+ gredUniform + "'");
							if(jawatan!=null){
								gredKelasKuarters=jawatan.getKelasLayak();
							}
						}
						else if (idPerkhidmatan.equalsIgnoreCase("YA") || idPerkhidmatan.equalsIgnoreCase("VUYA")){
							JawatanPDRM jawatan = (JawatanPDRM) mp.get("SELECT j FROM JawatanPDRM j WHERE j.id = '"+ gredUniform + "'");
							if(jawatan!=null){
								gredKelasKuarters=jawatan.getKelasLayak();				
							}
						}
						else if (idPerkhidmatan.equalsIgnoreCase("ZA") || idPerkhidmatan.equalsIgnoreCase("VUZ")){
							JawatanATM jawatan = (JawatanATM) mp.get("SELECT j FROM JawatanATM j WHERE j.id = '"+ gredUniform + "'");
							if(jawatan!=null){
								gredKelasKuarters=jawatan.getKelasLayak();					
							}
						}
					}
					/*END UNTUK KES BADAN BERUNIFORM*/
				}
				kelasLayak = gredKelasKuarters;
				
				//KUARTERS PUTRAJAYA
				if (kp.getLokasi().getId().equalsIgnoreCase("01")){
					kelasDowngrade = getKelasDowngrade(gredKelasKuarters);
				}
				
				//KUARTERS KUALA LUMPUR/PETALING JAYA
				if (kp.getLokasi().getId().equalsIgnoreCase("02")){
					kelasDowngrade = "";
				}
				
				//KUARTERS KLIA
				if (kp.getLokasi().getId().equalsIgnoreCase("06")){
					if(kelasLayak.equalsIgnoreCase("A") || kelasLayak.equalsIgnoreCase("B") ||kelasLayak.equalsIgnoreCase("C") || kelasLayak.equalsIgnoreCase("D") || kelasLayak.equalsIgnoreCase("E")){
						kelasLayak = "F";
					}
					kelasDowngrade = "";
				}
				
				//KUARTERS TANJUNG KUPANG
				if (kp.getLokasi().getId().equalsIgnoreCase("04")){
					
					if(kelasLayak.equalsIgnoreCase("A") || kelasLayak.equalsIgnoreCase("B") ||kelasLayak.equalsIgnoreCase("C")){
						kelasLayak = "D";
					}
					if(kelasLayak.equalsIgnoreCase("F")){
						kelasLayak = "G";
					}
					kelasDowngrade = "";
				}
				
				//KUARTERS LABUAN
				if (kp.getLokasi().getId().equalsIgnoreCase("03")){
					if(kelasLayak.equalsIgnoreCase("A") || kelasLayak.equalsIgnoreCase("B") ||kelasLayak.equalsIgnoreCase("C") ||kelasLayak.equalsIgnoreCase("D") ||kelasLayak.equalsIgnoreCase("E")){
						kelasLayak = "E";
					}else{
						kelasLayak = "F";
					}
					kelasDowngrade = "";
				}
			}

			String lokasi = pekerjaan.getBandar().getNegeri().getKeterangan();
			context.put("kelasLayak", kelasLayak);
			context.put("kelasDowngrade", kelasDowngrade);
			context.put("lokasi", lokasi);

			context.remove("resultPermohonanCompletion");
			context.put("resultPermohonanCompletion", getCompletionPermohonan(
					mp, getParam("idPermohonan")));
			context.remove("statusPerkahwinan");
			context.put("statusPerkahwinan", getStatusPerkahwinanPemohon(
					mp, getParam("idPermohonan")));
			
			// dapatkan KL area
			String nogori = "";
			if (kp.getLokasi().getBandar().getNegeri().getId().toString() != null) {
				nogori = kp.getLokasi().getId().toString();
			}

			if (nogori.equalsIgnoreCase("02")) {
				context.put("areaKL", true);
			} else {
				context.put("areaKL", false);
			}
			context.put("r", kp);
		} catch (Exception e) {
			System.out.println("Error getPerakuan : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return getPath() + "/entry_sub/entry_page_sub_bottom.vm";
	}

	/*--------------------------------------------------------------- DETAIL PERIBADI ---------------------------------------------------------------*/
	@Command("getPeribadi")
	public String getPeribadi() {
		try {
			mp = new MyPersistence();
			context.put("selectGelaran", dataUtil.getListGelaranPeribadi());
			context.put("selectJenisPengenalan", dataUtil.getListFewJenisPengenalan());
			context.put("selectAgama", dataUtil.getListAgama());
			context.put("selectJantina", dataUtil.getListJantina());
			context.put("selectBangsa", dataUtil.getListBangsa());
			context.put("selectEtnik", dataUtil.getListEtnik());
			context.put("selectStatusPerkahwinan", dataUtil.getListStatusPerkahwinan());
			context.put("selectNegeri", dataUtil.getListNegeri());
			context.put("selectNegeriSemasa", dataUtil.getListNegeri());
			Users u = (Users) mp.find(Users.class, userId);
			context.put("users", u);
			context.put("imgName", getParam("imgName"));
			Date tarikhLahir = kuaUtil.getTarikhLahir(u.getNoKP());
			context.put("tarikhLahir", tarikhLahir);
			context.put("activity", "peribadi");
			context.put("dirUpload", uploadDir + "qtr/permohonan/"+ getParam("idPermohonan") + "/");
		} catch (Exception e) {
			System.out.println("Error getPeribadi : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/sub_page/peribadi.vm";
	}

	@Command("selectBandar")
	public String selectBandar() throws Exception {
		dataUtil = DataUtil.getInstance(db);
		String idNegeri = "0";

		if (get("idNegeri").trim().length() > 0) {
			idNegeri = get("idNegeri");
		}

		context.put("selectBandar", dataUtil.getListBandar(idNegeri));
		return getPath() + "/sub_page/peribadi/selectBandar.vm";
	}

	@Command("selectBandarSemasa")
	public String selectBandarSemasa() throws Exception {
		dataUtil = DataUtil.getInstance(db);
		String idNegeriSemasa = "0";

		if (get("idNegeriSemasa").trim().length() > 0)
			idNegeriSemasa = get("idNegeriSemasa");

		context.put("selectBandarSemasa", dataUtil
				.getListBandar(idNegeriSemasa));
		return getPath() + "/sub_page/peribadi/selectBandarSemasa.vm";
	}

	@Command("simpanPeribadi")
	public String simpanPeribadi() {
		boolean success = false;
		String idPermohonan = getParam("idPermohonan");
		try {
			mp = new MyPersistence();

			String idBilAnak = getParam("idBilAnak");
			int bilAnak = 0;
			if (!"".equals(idBilAnak)) {
				if ("99".equals(idBilAnak)) {
					bilAnak = getParamAsInteger("userBilAnak");
				} else if ("TB".equals(idBilAnak)) {
					bilAnak = 0;
				} else {
					bilAnak = Integer.parseInt(idBilAnak);
				}
			}

			Gelaran gelaran = (Gelaran) mp.find(Gelaran.class,
					getParam("idGelaran"));
			JenisPengenalan jenisPengenalan = (JenisPengenalan) mp.find(
					JenisPengenalan.class, getParam("idJenisPengenalan"));
			Jantina jantina = (Jantina) mp.find(Jantina.class,
					getParam("idJantina"));
			Bangsa bangsa = (Bangsa) mp
					.find(Bangsa.class, getParam("idBangsa"));
			Etnik etnik = (Etnik) mp.find(Etnik.class, getParam("idEtnik"));
			StatusPerkahwinan statusPerkahwinan = (StatusPerkahwinan) mp.find(
					StatusPerkahwinan.class, getParam("idStatusPerkahwinan"));
			Bandar bandar = (Bandar) mp
					.find(Bandar.class, getParam("idBandar"));
			Bandar bandarSemasa = (Bandar) mp.find(Bandar.class,
					getParam("idBandarSemasa"));
			KuaPermohonan kp = (KuaPermohonan) mp.find(KuaPermohonan.class,
					getParam("idPermohonan"));

			Users users = (Users) mp.find(Users.class, userId);

			users.setGelaran(gelaran);
			users.setUserName(getParam("userName"));
			// users.setAvatar(getParam("gambarPersonalImage"));
			users.setJenisPengenalan(jenisPengenalan);
			users.setNoKP(getParam("userNoKP"));
			users.setNoKP2(getParam("userNoKP2"));
			users.setTarikhLahir(getDate("userTarikhLahir"));
			users.setJantina(jantina);
			users.setBangsa(bangsa);
			users.setStatusPerkahwinan(statusPerkahwinan);
			users.setBilAnak(bilAnak);
			users.setFlagAnak(idBilAnak);
			users.setNoTelefon(getParam("userNoTel"));
			users.setNoTelefonBimbit(getParam("userNoTelBimbit"));
			users.setEmel(getParam("email"));
			users.setAlamat1(getParam("userAlamat1"));
			users.setAlamat2(getParam("userAlamat2"));
			users.setAlamat3(getParam("userAlamat3"));
			users.setPoskod(getParam("userPoskod"));
			users.setBandar(bandar);
			users.setUserAddress(getParam("userAlamatSemasa1"));
			users.setUserAddress2(getParam("userAlamatSemasa2"));
			users.setUserAddress3(getParam("userAlamatSemasa3"));
			users.setUserPostcode(getParam("userPoskodSemasa"));
			users.setUserBandar(bandarSemasa);
			users.setEtnik(etnik);

			mp.begin();

			kp.setPeribadi(1);
			kp.setTarikhKemaskini(new Date());
			kuaUtil.kuartersLog(2, "Users", (Users) mp
					.find(Users.class, userId), users.getId());

			// db.commit(request, "PROCESSING FILE (Penghuni - Peribadi) : "+
			// users.getId(), 2);
			mp.commit();

			success = true;

		} catch (Exception e) {
			System.out.println("Error simpanPeribadi : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("success", success);

		context.remove("resultPermohonanCompletion");
		context.put("resultPermohonanCompletion", getCompletionPermohonan(mp,
				getParam("idPermohonan")));
		simpanAuditTrail(idPermohonan, "simpanPeribadi");
		return getPath() + "/sub_page/result/simpanPeribadi.vm";
	}

	@Command("uploadGambar")
	public String uploadGambar() throws Exception {
		context.put("imgName", kuaUtil.uploadFile(request, "permohonan",
				getParam("idPermohonan")));
		return getPath() + "/sub_page/peribadi/uploaddoc.vm";
	}

	@Command("checkEmail")
	public String checkEmail() {
		boolean result = false;

		boolean firstValidate = Util.isValidEmailAddress(getParam("email"));
		boolean secondValidate = Util.validateEmail(getParam("email"));

		if (firstValidate == true && secondValidate == true) {
			result = true;
		}

		context.put("result", result);

		return getPath() + "/sub_page/result/resultCheckEmail.vm";
	}

	/*--------------------------------------------------------------- DETAIL PEKERJAAN ---------------------------------------------------------------*/
	@Command("getPekerjaan")
	public String getPekerjaan() throws Exception {

		userId = (String) request.getSession().getAttribute("_portal_login");

		context.put("selectJawatan", dataUtil.getListJawatan());
		context.put("selectKelasPerkhidmatan", dataUtil
				.getListKelasPerkhidmatan());
		context.put("selectGredJawatan", dataUtil.getListGredPerkhidmatan());
		context.put("selectJenisPerkhidmatan", dataUtil
				.getListJenisPerkhidmatan());
		context.put("selectStatusPerkhidmatan", dataUtil
				.getListStatusPerkhidmatan());
		context.put("selectKementerian", dataUtil.getListKementerian());

		context.put("activity", "pekerjaan");

		context.remove("uj");

		try {
			mp = new MyPersistence();

			UsersJob usersJob = (UsersJob) mp
					.get("SELECT uj FROM UsersJob uj WHERE uj.users.id = '"
							+ userId + "'");

			if (usersJob != null) {
				context.put("uj", mp.find(UsersJob.class, usersJob.getId()));
			}

			context.put("users", mp.find(Users.class, userId));
			context.remove("resultPermohonanCompletion");
			context.put("resultPermohonanCompletion", getCompletionPermohonan(
					mp, getParam("idPermohonan")));

		} catch (Exception e) {
			System.out.println("Error getPekerjaan : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/sub_page/pekerjaan.vm";
	}

	@Command("selectBandarPekerjaan")
	public String selectBandarPekerjaan() throws Exception {
		dataUtil = DataUtil.getInstance(db);
		String idNegeri = "0";

		if (get("idNegeriPekerjaan").trim().length() > 0) {
			idNegeri = get("idNegeriPekerjaan");
		}

		context.put("selectBandarPekerjaan", dataUtil.getListBandarKuarters(idNegeri));
		return getPath() + "/select/selectBandarPekerjaan.vm";
	}

	@Command("selectJabatan")
	public String selectJabatan() throws Exception {
		dataUtil = DataUtil.getInstance(db);
		String idKementerian = "0";

		if (get("idKementerian").trim().length() > 0) {
			idKementerian = get("idKementerian");
		}

		context.put("selectJabatan", dataUtil.getListAgensi(idKementerian));
		return getPath() + "/sub_page/pekerjaan/selectJabatan.vm";
	}

	@Command("selectJenisPerkhidmatan")
	public String selectJenisPerkhidmatan() {
		dataUtil = DataUtil.getInstance(db);
		String idJenisPerkhidmatan = getParam("idJenisPerkhidmatan");
		String subPath = "jenisPerkhidmatan1";

		if ("01".equals(idJenisPerkhidmatan)) {
			context.put("selectKementerian", dataUtil.getListKementerian());

			subPath = "jenisPerkhidmatan1";
		} else if ("03".equals(idJenisPerkhidmatan)) {
			context.put("selectBadanBerkanun", dataUtil.getListBadanBerkanun());

			subPath = "jenisPerkhidmatan2";
		}

		return getPath() + "/sub_page/pekerjaan/" + subPath + ".vm";
	}

	@Command("selectJawatan")
	public String selectJawatan() {
		dataUtil = DataUtil.getInstance(db);
		String idKelasPerkhidmatan = "";
		if (get("idKelasPerkhidmatan").trim().length() > 0) {
			idKelasPerkhidmatan = get("idKelasPerkhidmatan");
			idKelasPerkhidmatan = idKelasPerkhidmatan.substring(0, 1);
		}
		context.put("selectJawatan", dataUtil
				.getListJawatan(idKelasPerkhidmatan));
		return getPath() + "/sub_page/pekerjaan/selectJawatan.vm";
	}

	@Command("simpanPekerjaan")
	public String simpanPekerjaan() {
		userId = (String) request.getSession().getAttribute("_portal_login");
		Boolean result = true;
		Boolean result2 = true;
		boolean success = false;
		boolean newRecord = false;
		boolean tukar = false;
		boolean upgrade = false;
		context.put("tukar", "");
		context.put("upgrade", "");
		context.remove("tukar");
		context.remove("upgrade");
		int jenisAktiviti = 2;
		String idPermohonan = getParam("idPermohonan");
		try {
			mp = new MyPersistence();
			Users users = (Users) mp.find(Users.class, userId);
			KelasPerkhidmatan kelasPerkhidmatan = (KelasPerkhidmatan) mp.find(KelasPerkhidmatan.class, getParam("idKelasPerkhidmatan"));
			GredPerkhidmatan gredJawatan = (GredPerkhidmatan) mp.find(GredPerkhidmatan.class, getParam("idGredJawatan"));
			Jawatan jawatan = (Jawatan) mp.find(Jawatan.class,getParam("idJawatan"));
			JenisPerkhidmatan jenisPerkhidmatan = (JenisPerkhidmatan) mp.find(JenisPerkhidmatan.class, getParam("idJenisPerkhidmatan"));
			StatusPerkhidmatan statusPerkhidmatan = (StatusPerkhidmatan) mp.find(StatusPerkhidmatan.class,getParam("idStatusPerkhidmatan"));
			Agensi agensi = (Agensi) mp.find(Agensi.class,getParam("idJabatan"));
			BadanBerkanun badanBerkanun = (BadanBerkanun) mp.find(BadanBerkanun.class, getParam("idBadanBerkanun"));

			KuaPermohonan kp = (KuaPermohonan) mp.find(KuaPermohonan.class,getParam("idPermohonan"));

			if ("02".equals(statusPerkhidmatan.getId())) {
				int days = Util.daysBetween(new Date(), getDate("tarikhTamat"));
				if (days < 365) {
					result = false;
				}
				if (getParamAsInteger("valueFlagITP") == 0 && getParamAsInteger("valueFlagEPW") == 0) {
					result2 = false;
				}
			}
			
			//check jika mohon upgrade (05/10/2016)
			UsersJob pekerjaanAsal = (UsersJob) mp.get("SELECT uj FROM UsersJob uj WHERE uj.users.id = '"+ userId + "'");
			String kelasLayakAsal = "";
			String kelasDowngradeAsal = "";
			String gredKelasKuartersAsal = "";
			String gredAsal = "";
			String lokasiPekerjaanAsal="";
			String alamat1Asal="";
			String alamat2Asal="";
			String alamat3Asal="";
			String poskodAsal="";
			String bandarAsal="";
			String negeriAsal="";
			if (pekerjaanAsal != null) {
				if (pekerjaanAsal.getGredJawatan() != null) {
					gredAsal=pekerjaanAsal.getGredJawatan().getId();
					gredKelasKuartersAsal = pekerjaanAsal.getGredJawatan().getKelasKuarters();
					kelasLayakAsal = gredKelasKuartersAsal;
					kelasDowngradeAsal = getKelasDowngrade(gredKelasKuartersAsal);
					lokasiPekerjaanAsal=pekerjaanAsal.getBandar().getNegeri().getId();
					alamat1Asal=pekerjaanAsal.getAlamat1();
					alamat2Asal=pekerjaanAsal.getAlamat2();
					alamat3Asal=pekerjaanAsal.getAlamat3();
					poskodAsal=pekerjaanAsal.getPoskod();
					bandarAsal=pekerjaanAsal.getBandar().getKeterangan();
					negeriAsal=pekerjaanAsal.getBandar().getNegeri().getKeterangan();
				}
			}
			UsersJob uj = null;
			UsersJob usersJob = (UsersJob) mp.get("SELECT uj FROM UsersJob uj WHERE uj.users.id = '"+ userId + "'");
			
			if (usersJob != null) 
			{
				uj = (UsersJob) mp.find(UsersJob.class, usersJob.getId());
			}

			if (uj == null) 
			{
				newRecord = true;
				jenisAktiviti = 1;
				uj = new UsersJob();
			}

			uj.setUsers(users);
			uj.setKelasPerkhidmatan(kelasPerkhidmatan);
			uj.setGredJawatan(gredJawatan);
			uj.setJawatan(jawatan);
			uj.setTarikhLantikan(getDate("tarikhLantikan"));
			uj.setNoGaji(getParam("noGaji"));
			uj.setGajiPokok(Double.parseDouble(getParam("gajiPokok").replaceAll("RM", "").replaceAll(",", "")));
			uj.setJenisPerkhidmatan(jenisPerkhidmatan);
			uj.setStatusPerkhidmatan(statusPerkhidmatan);
			uj.setTarikhTamat(getDate("tarikhTamat"));
			uj.setFlagITP(getParamAsInteger("valueFlagITP"));
			uj.setFlagEPW(getParamAsInteger("valueFlagEPW"));
			uj.setAgensi(agensi);
			uj.setBadanBerkanun(badanBerkanun);
			uj.setBahagian(getParam("bahagian"));
			uj.setAlamat1(getParam("alamatPekerjaan1"));
			uj.setAlamat2(getParam("alamatPekerjaan2"));
			uj.setAlamat3(getParam("alamatPekerjaan3"));
			uj.setNoTelPejabat(getParam("noTelPejabat"));
			uj.setNoFaks(getParam("noFaks"));
			uj.setEmel(getParam("email"));
			uj.setFlagCola(getParamAsInteger("valueFlagCola"));

			mp.begin();

			if (newRecord == true) {
				mp.persist(uj);
			}

			// if (!"03".equals(statusPerkhidmatan.getId())) {
			if (statusPerkhidmatan.getId() != null) {
				if (result == true && result2 == true) {
					kp.setKelulusan2("true");
				} else {
					kp.setKelulusan2("false");
				}
			} else {
				kp.setKelulusan2("false");
			}
			kp.setPekerjaan(1);
			kp.setTarikhKemaskini(new Date());
			
			//check jika ada pertukaran gred untuk kes naik pangkat.
			if(kp.getStatus().getId().equalsIgnoreCase("1419601227590") || kp.getStatus().getId().equalsIgnoreCase("1423101446117"))//menunggu
			{
				String gredLayakAsal=gredAsal;
				String gredLama=gredKelasKuartersAsal;
				String gredLayakBaru=uj.getGredJawatan().getId();
				String gredBaru=uj.getGredJawatan().getKelasKuarters();
				String alamat1Baru=uj.getAlamat1();
				String alamat2Baru=uj.getAlamat2();
				String alamat3Baru=uj.getAlamat3();
				String poskodBaru=uj.getPoskod();
				String bandarBaru=uj.getBandar().getKeterangan();
				String negeriBaru=uj.getBandar().getNegeri().getKeterangan();
				if(gredLama.equalsIgnoreCase(gredBaru)==false || alamat1Baru.equalsIgnoreCase(alamat1Asal)==false || alamat2Baru.equalsIgnoreCase(alamat2Asal)==false || alamat3Baru.equalsIgnoreCase(alamat3Asal)==false || negeriBaru.equalsIgnoreCase(negeriAsal)==false || bandarBaru.equalsIgnoreCase(bandarAsal)==false)
				{
					if(alamat1Baru.equalsIgnoreCase(alamat1Asal)==false || alamat2Baru.equalsIgnoreCase(alamat2Asal)==false || alamat3Baru.equalsIgnoreCase(alamat3Asal)==false || negeriBaru.equalsIgnoreCase(negeriAsal)==false || bandarBaru.equalsIgnoreCase(bandarAsal)==false )
					{
						tukar = true;
						context.put("tukar", tukar);
						String lokasiLama=alamat1Asal+", "+alamat2Asal+", "+alamat3Asal+", "+poskodAsal+", "+bandarAsal+", "+ negeriAsal;
						String lokasiBaru=alamat1Baru+", "+alamat2Baru+", "+alamat3Baru+", "+poskodBaru+", "+bandarBaru+", "+ negeriBaru;
						simpanCatatanPemohonTukarLokasi(kp.getId(),lokasiLama,lokasiBaru);
					}
					else
					{
						upgrade = true;
						context.put("upgrade", upgrade);
						simpanCatatanPemohonNaikTaraf(kp.getId(),gredLayakAsal,gredLayakBaru);
					}
				}
			}
			mp.commit();
			success = true;
			context.put("success", success);
			context.put("result", result);
			context.put("result2", result2);
			context.remove("resultPermohonanCompletion");
			context.put("resultPermohonanCompletion", getCompletionPermohonan(mp, getParam("idPermohonan")));
			context.remove("statusPerkahwinan");
			context.put("statusPerkahwinan", getStatusPerkahwinanPemohon(
					mp, getParam("idPermohonan")));
		} catch (Exception e) {
			System.out.println("Error simpanPekerjaan : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		simpanAuditTrail(idPermohonan, "simpanPekerjaan");
		if(upgrade==true){
			return getPath() + "/sub_page/result/simpanNaikTaraf.vm";
		}else if(tukar==true){
			return getPath() + "/sub_page/result/simpanTukarLokasi.vm";
		}else{
			return getPath() + "/sub_page/result/simpanPekerjaan.vm";
		}
	}

	/*--------------------------------------------------------------- DETAIL PASANGAN ---------------------------------------------------------------*/
	@Command("getPasangan")
	public String getPasangan() throws Exception {
		
		context.put("selectJenisPengenalanPasangan", dataUtil.getListJenisPengenalan());
		context.put("selectStatusPekerjaanPasangan", dataUtil.getListStatusPekerjaan());
		context.put("selectNegeriPasangan", dataUtil.getListNegeri());
		context.put("selectKelasPerkhidmatanPasangan", dataUtil.getListKelasPerkhidmatan());
		context.put("selectGredJawatanPasangan", dataUtil.getListGredPerkhidmatan());
		context.put("selectKementerianPasangan", dataUtil.getListKementerian());
		context.put("selectJawatanPasangan", dataUtil.getListJawatan());
		context.put("selectBadanBerkanunPasangan", dataUtil.getListBadanBerkanun());
		context.put("activity", "pasangan");
		context.remove("us");

		try {
			mp = new MyPersistence();
			UsersSpouse usersSpouse = (UsersSpouse) mp
					.get("SELECT us FROM UsersSpouse us WHERE us.users.id = '"+ userId + "'");
			if (usersSpouse != null) {
				context.put("us", mp.find(UsersSpouse.class, usersSpouse.getId()));
			}
			context.put("users", mp.find(Users.class, userId));
			context.remove("resultPermohonanCompletion");
			context.put("resultPermohonanCompletion", getCompletionPermohonan(
					mp, getParam("idPermohonan")));
			context.remove("statusPerkahwinan");
			context.put("statusPerkahwinan", getStatusPerkahwinanPemohon(
					mp, getParam("idPermohonan")));
		} catch (Exception e) {
			System.out.println("Error getPasangan : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/sub_page/pasangan.vm";
	}

	@Command("selectBandarPasangan")
	public String selectBandarPasangan() throws Exception {
		dataUtil = DataUtil.getInstance(db);
		String idNegeri = "0";

		if (get("idNegeriPasangan").trim().length() > 0) {
			idNegeri = get("idNegeriPasangan");
		}

		context.put("selectBandarPasangan", dataUtil.getListBandar(idNegeri));
		return getPath() + "/sub_page/pasangan/selectBandarPasangan.vm";
	}

	@Command("selectJabatanPasangan")
	public String selectJabatanPasangan() throws Exception {
		dataUtil = DataUtil.getInstance(db);
		String idKementerian = "0";

		if (get("idKementerianPasangan").trim().length() > 0) {
			idKementerian = get("idKementerianPasangan");
		}

		context.put("selectJabatanPasangan", dataUtil
				.getListAgensi(idKementerian));

		return getPath() + "/sub_page/pasangan/selectJabatanPasangan.vm";
	}

	@Command("simpanPasangan")
	public String simpanPasangan() {

		boolean success = false;
		boolean newRecord = false;
		int jenisAktiviti = 2;
		String idPermohonan = getParam("idPermohonan");

		try {
			mp = new MyPersistence();

			Users users = (Users) mp.find(Users.class, userId);
			JenisPengenalan jenisPengenalan = (JenisPengenalan) mp.find(
					JenisPengenalan.class,
					getParam("idJenisPengenalanPasangan"));
			StatusPekerjaan statusPekerjaan = (StatusPekerjaan) mp.find(
					StatusPekerjaan.class,
					getParam("idStatusPekerjaanPasangan"));
			KelasPerkhidmatan kelasPerkhidmatan = (KelasPerkhidmatan) mp.find(
					KelasPerkhidmatan.class,
					getParam("idKelasPerkhidmatanPasangan"));
			GredPerkhidmatan gredJawatan = (GredPerkhidmatan) mp.find(
					GredPerkhidmatan.class, getParam("idGredJawatanPasangan"));
			Jawatan jawatan = (Jawatan) mp.find(Jawatan.class,
					getParam("idJawatanPasangan"));
			Agensi agensi = (Agensi) mp.find(Agensi.class,
					getParam("idJabatanPasangan"));
			BadanBerkanun badanBerkanun = (BadanBerkanun) mp.find(
					BadanBerkanun.class, getParam("idBadanBerkanunPasangan"));
			Bandar bandar = (Bandar) mp.find(Bandar.class,
					getParam("idBandarPasangan"));
			KuaPermohonan kp = (KuaPermohonan) mp.find(KuaPermohonan.class,
					getParam("idPermohonan"));

			UsersSpouse us = null;

			UsersSpouse usersSpouse = (UsersSpouse) mp
					.get("SELECT us FROM UsersSpouse us WHERE us.users.id = '"
							+ userId + "'");

			if (usersSpouse != null) {
				us = (UsersSpouse) mp.find(UsersSpouse.class, usersSpouse
						.getId());
			}

			if (us == null) {
				newRecord = true;
				jenisAktiviti = 1;
				us = new UsersSpouse();
			}

			us.setUsers(users);
			us.setNamaPasangan(getParam("namaPasangan"));
			us.setJenisPengenalan(jenisPengenalan);
			us.setNoKPPasangan(getParam("pasanganNoKP"));
			us.setStatusPekerjaanPasangan(statusPekerjaan);
			us.setJenisPekerjaan(getParam("pasanganJenisPekerjaan"));
			if (!"".equals(getParam("pasanganGaji"))) {
				us.setGajiPasangan(Double.parseDouble(getParam("pasanganGaji")
						.replaceAll("RM", "").replaceAll(",", "")));
			}
			us.setNamaSyarikat(getParam("pasanganSyarikat"));
			us.setAlamatPejabat1(getParam("pasanganAlamatKerja1"));
			us.setAlamatPejabat2(getParam("pasanganAlamatKerja2"));
			us.setAlamatPejabat3(getParam("pasanganAlamatKerja3"));
			us.setPoskodPejabat(getParam("pasanganPoskodKerja"));
			us.setBandarPejabat(bandar);
			us.setNoTelPejabat(getParam("pasanganNoTelKerja"));
			us.setNoFaksPejabat(getParam("pasanganNoFaksKerja"));
			us.setNoTelBimbit(getParam("pasanganNoTelBimbit"));
			us.setBadanBerkanun(badanBerkanun);
			us.setGredJawatan(gredJawatan);
			us.setJawatan(jawatan);
			us.setAgensi(agensi);
			us.setKelasPerkhidmatan(kelasPerkhidmatan);

			mp.begin();

			if (newRecord == true) {
				mp.persist(us);
			}

			kp.setPasangan(1);
			kp.setTarikhKemaskini(new Date());
			kuaUtil.kuartersLog(jenisAktiviti, "UsersSpouse", (Users) mp.find(
					Users.class, userId), us.getId());

			// db.commit(request, "PROCESSING FILE (Pasangan Penghuni) : "+
			// us.getId(), jenisAktiviti);
			mp.commit();
			success = true;

		} catch (Exception e) {
			System.out.println("Error simpanPasangan : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}

		context.put("success", success);

		context.remove("resultPermohonanCompletion");
		context.put("resultPermohonanCompletion", getCompletionPermohonan(mp,
				getParam("idPermohonan")));
		context.remove("statusPerkahwinan");
		context.put("statusPerkahwinan", getStatusPerkahwinanPemohon(
				mp, getParam("idPermohonan")));
		simpanAuditTrail(idPermohonan, "simpanPasangan");
		return getPath() + "/sub_page/result/simpanPasangan.vm";
	}

	/*--------------------------------------------------------------- DETAIL PINJAMAN ---------------------------------------------------------------*/
	@Command("getPinjaman")
	public String getPinjaman() throws Exception {

		context.put("selectNegeriPinjaman", dataUtil.getListNegeri());

		context.put("activity", "pinjaman");

		context.remove("kpp");

		try {
			mp = new MyPersistence();

			KuaPinjamanPemohon kpp = (KuaPinjamanPemohon) mp
					.get("SELECT kpp FROM KuaPinjamanPemohon kpp WHERE kpp.users.id = '"
							+ userId + "'");

			if (kpp != null) {
				context.put("kpp", mp.find(KuaPinjamanPemohon.class, kpp
						.getId()));
			}

			context.put("users", mp.find(Users.class, userId));

			context.remove("resultPermohonanCompletion");
			context.put("resultPermohonanCompletion", getCompletionPermohonan(
					mp, getParam("idPermohonan")));
			context.remove("statusPerkahwinan");
			context.put("statusPerkahwinan", getStatusPerkahwinanPemohon(
					mp, getParam("idPermohonan")));
		} catch (Exception e) {
			System.out.println("Error getPinjaman : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/sub_page/pinjaman.vm";
	}

	@Command("selectBandarPinjaman")
	public String selectBandarPinjaman() throws Exception {
		dataUtil = DataUtil.getInstance(db);
		String idNegeri = "0";

		if (get("idNegeriPinjaman").trim().length() > 0) {
			idNegeri = get("idNegeriPinjaman");
		}

		context.put("selectBandarPinjaman", dataUtil.getListBandar(idNegeri));
		return getPath() + "/sub_page/pinjaman/selectBandarPinjaman.vm";
	}

	@Command("simpanPinjaman")
	public String simpanPinjaman() {

		boolean success = false;
		boolean newRecord = false;
		int jenisAktiviti = 2;
		String idPermohonan = getParam("idPermohonan");

		try {
			mp = new MyPersistence();
			Users users = (Users) mp.find(Users.class, userId);
			Bandar bandar = (Bandar) mp.find(Bandar.class,
					getParam("idBandarPinjaman"));
			KuaPermohonan kp = (KuaPermohonan) mp.find(KuaPermohonan.class,
					getParam("idPermohonan"));
			KuaPinjamanPemohon kpp = null;
			KuaPinjamanPemohon pinjamanPemohon = (KuaPinjamanPemohon) mp
					.get("SELECT p FROM KuaPinjamanPemohon p WHERE p.users.id = '"
							+ userId + "'");
			if (pinjamanPemohon != null) {
				kpp = (KuaPinjamanPemohon) mp.find(KuaPinjamanPemohon.class,
						pinjamanPemohon.getId());
			}
			
			if (kpp == null) {
				newRecord = true;
				jenisAktiviti = 1;
				kpp = new KuaPinjamanPemohon();
			}

			Date t = null;
			String[] tarikhJangkaSiap = getParam("tarikhJangkaSiap").split(",");
			String tarikhJangkaSiapBaru = "01-"
					+ getMonth(tarikhJangkaSiap[0].trim()) + "-"
					+ tarikhJangkaSiap[1].trim();

			t = new SimpleDateFormat("dd-MM-yyyy").parse(tarikhJangkaSiapBaru);

			kpp.setUsers(users);
			kpp.setPinjamanPerumahan(getParamAsInteger("valuePinjamanPerumahan"));
			kpp.setStatusPembinaan(getParamAsInteger("valueStatusRumah"));
			kpp.setTarikhJangkaSiap(t);
			kpp.setAlamat1(getParam("alamatPinjaman1"));
			kpp.setAlamat2(getParam("alamatPinjaman2"));
			kpp.setAlamat3(getParam("alamatPinjaman3"));
			kpp.setPoskodPinjaman(getParam("poskodPinjaman"));
			kpp.setBandar(bandar);
			kpp.setJenisPerumahan(getParam("jenisPerumahan"));
			kpp.setPembiayaan(getParam("valuePembiayaan"));
			kpp.setPembelian(getParam("valuePembelian"));
			mp.begin();
			if (newRecord == true) 
			{
				mp.persist(kpp);
			}
			if (getParamAsInteger("valuePinjamanPerumahan") == 0) 
			{
				kp.setKelulusan3("true");
			}
			kp.setPinjaman(1);
			kp.setTarikhKemaskini(new Date());
			kuaUtil.kuartersLog(jenisAktiviti, "KuaPinjamanPemohon", (Users) mp.find(Users.class, userId), kpp.getId());
			mp.commit();
			success = true;
		} catch (Exception e) {
			System.out.println("Error simpanPinjaman : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("success", success);
		context.remove("resultPermohonanCompletion");
		context.put("resultPermohonanCompletion", getCompletionPermohonan(mp,getParam("idPermohonan")));
		context.remove("statusPerkahwinan");
		context.put("statusPerkahwinan", getStatusPerkahwinanPemohon(
				mp, getParam("idPermohonan")));
		simpanAuditTrail(idPermohonan, "simpanPinjaman");
		return getPath() + "/sub_page/result/simpanPinjaman.vm";
	}

	/*--------------------------------------------------------------- DETAIL KELAINAN UPAYA ---------------------------------------------------------------*/
	@Command("getKelainanUpaya")
	public String getKelainanUpaya() {

		context.put("activity", "kelainanUpaya");

		try {
			mp = new MyPersistence();

			context.put("users", mp.find(Users.class, userId));

			context.remove("resultPermohonanCompletion");
			context.put("resultPermohonanCompletion", getCompletionPermohonan(
					mp, getParam("idPermohonan")));
			context.remove("statusPerkahwinan");
			context.put("statusPerkahwinan", getStatusPerkahwinanPemohon(
					mp, getParam("idPermohonan")));
		} catch (Exception e) {
			System.out.println("Error getKelainanUpaya : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/sub_page/kelainanUpaya.vm";
	}

	@SuppressWarnings("unchecked")
	@Command("getRecordKelainanUpaya")
	public String getRecordKelainanUpaya() {
		try {
			mp = new MyPersistence();

			List<KuaKelainanUpaya> kku = mp
					.list("SELECT ku FROM KuaKelainanUpaya ku WHERE ku.permohonan.id = '"
							+ getParam("idPermohonan") + "'");
			context.put("kku", kku);

		} catch (Exception e) {
			System.out.println("Error getRecordKelainanUpaya : "
					+ e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/sub_page/kelainanUpaya/record.vm";
	}

	@Command("tambahKelainanUpaya")
	public String tambahKelainanUpaya() {
		return getPath() + "/sub_page/kelainanUpaya/start.vm";
	}

	@Command("deleteKelainanUpaya")
	public String deleteKelainanUpaya() {
		boolean success = false;

		try {
			mp = new MyPersistence();

			KuaKelainanUpaya kku = (KuaKelainanUpaya) mp.find(
					KuaKelainanUpaya.class, getParam("idKelainanUpaya"));

			mp.begin();
			if (kku != null) {
				mp.remove(kku);
			}

			kuaUtil.kuartersLog(3, "KuaKelainanUpaya", (Users) mp.find(
					Users.class, userId), kku.getId());

			// db.commit(request,
			// "PROCESSING FILE (Kelainan Upaya Penghuni) : "+ kku.getId(), 3);
			mp.commit();

			success = true;

		} catch (Exception e) {
			System.out.println("Error deleteKelainanUpaya : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}

		context.put("success", success);

		return getPath() + "/sub_page/kelainanUpaya/resultDelete.vm";
	}

	@Command("simpanKelainanUpaya")
	public String simpanKelainanUpaya() {
		boolean success = false;

		try {
			mp = new MyPersistence();

			KuaPermohonan permohonan = (KuaPermohonan) mp.find(
					KuaPermohonan.class, getParam("idPermohonan"));
			KuaKelainanUpaya kku = new KuaKelainanUpaya();

			kku.setNoKad(getParam("kelainanUpayaNoKad"));
			kku.setNoMyKad(getParam("kelainanUpayaNoKP"));
			kku.setHubungan(getParam("idHubunganKelainanUpaya"));
			kku.setImgName(getParam("imgNameKelainanUpaya"));
			kku.setAvatarName(getParam("avatarNameKelainanUpaya"));
			kku.setPermohonan(permohonan);

			mp.begin();
			mp.persist(kku);

			kuaUtil.kuartersLog(1, "KuaKelainanUpaya", (Users) mp.find(
					Users.class, userId), kku.getId());

			// db.commit(request,
			// "PROCESSING FILE (Kelainan Upaya Penghuni) : "+ kku.getId(), 1);
			mp.commit();

			success = true;

		} catch (Exception e) {
			System.out.println("Error simpanKelainanUpaya : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}

		context.put("success", success);

		return getPath() + "/sub_page/kelainanUpaya/result.vm";
	}

	@Command("uploadDoc")
	public String uploadDoc() throws Exception {
		String imgName = kuaUtil.uploadFile(request, "kelainanUpaya",
				getParam("idPermohonan"));
		context.put("imgName", imgName);
		context.put("avatarName", imgName
				.substring(0, imgName.lastIndexOf("."))
				+ "_thumbnail" + imgName.substring(imgName.lastIndexOf(".")));

		return getPath() + "/sub_page/kelainanUpaya/uploaddoc.vm";
	}

	@Command("refreshList")
	public String refreshList() throws Exception {
		String imgName = getParam("imgName");
		String avatarName = getParam("avatarName");

		context.put("imgName", imgName);
		context.put("avatarName", avatarName);

		return getPath() + "/sub_page/kelainanUpaya/listDokumen.vm";
	}

	@SuppressWarnings("unchecked")
	@Command("semakKelayakkan")
	public String semakKelayakkan() {

		boolean result = false;
		boolean result2 = false;

		try {
			mp = new MyPersistence();

			int days = Util
					.daysBetween(new Date(), getDate("tarikhJangkaSiap"));

			if (days > 365) {
				result = true;
			}

			List l = new ArrayList();

			KuaPermohonan kp = (KuaPermohonan) mp.find(KuaPermohonan.class,
					getParam("idPermohonan"));

			String dist = getParam("poskodPinjaman");
			String lokasiAsal = kp.getLokasi().getLokasi();

			if ("".equals(lokasiAsal) || lokasiAsal == null) {
				lokasiAsal = getParam("negeriLokasi");
			}

			Bandar bandar = (Bandar) mp.find(Bandar.class,
					getParam("idBandarPinjaman"));
			if (bandar != null) {
				dist = dist + "|" + bandar.getKeterangan().trim();
			}

			Negeri negeri = (Negeri) mp.find(Negeri.class,
					getParam("idNegeriPinjaman"));
			if (negeri != null) {
				dist = dist + "|" + negeri.getKeterangan().trim();
			}

			l = KuartersUtils.getListRangeMap(lokasiAsal, dist);

			if (l != null) {
				Double minRange = Double.parseDouble(Collections.min(l)
						.toString());
				if (minRange > 25) {
					result2 = true;
				}
			}
			if (result == true && result2 == true) {// 365 hari dan
				kp.setKelulusan3("true");
			} else {
				kp.setKelulusan3("false");
			}
			mp.begin();
			mp.commit();

		} catch (Exception e) {
			System.out.println("Error semakKelayakkan : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("result", result);
		context.put("result2", result2);
		return getPath() + "/sub_page/result/resultSemakKelayakkan.vm";
	}

	/*--------------------------------------------------------------- HANTAR PERMOHONAN ---------------------------------------------------------------*/
	@SuppressWarnings("unchecked")
	@Command("hantarPermohonan")
	public String hantarPermohonan() {
		boolean success = false;
		String idPermohonan = getParam("idPermohonan");
		boolean samaKelasLayak = true;
		boolean samaKelasDowngrade = true;

		try {
			mp = new MyPersistence();

			KuaPermohonan kp = (KuaPermohonan) mp.find(KuaPermohonan.class,
					idPermohonan);
			UsersJob uj = (UsersJob) mp
					.get("SELECT uj FROM UsersJob uj WHERE uj.users.id = '"
							+ kp.getPemohon().getId() + "'");

			List<KuaAgihan> agihan = mp
					.list("SELECT a FROM KuaAgihan a WHERE a.permohonan.id = '"
							+ idPermohonan
							+ "' AND a.kuarters.id IS NULL ORDER BY a.kelasKuarters ASC");

			if (agihan.size() > 0) {
				if (!uj.getGredJawatan().getKelasKuarters().equals(
						agihan.get(0).getKelasKuarters())) {
					samaKelasLayak = false;
				}
				if (agihan.size() > 1) {
					if (!getKelasDowngrade(uj.getGredJawatan().getKelasKuarters()).equals(agihan.get(1).getKelasKuarters())) {
						samaKelasDowngrade = false;
					}
				}
			}

			kp.setStatus((Status) mp.find(Status.class, "1419483289678"));
			kp.setTarikhPermohonan(new Date());
			kp.setDatePermohonan(new Date());
			kp.setTarikhKemaskini(new Date());
			mp.begin();

			String noPermohonan = generateNoPermohonan(mp);
			kp.setNoPermohonan(noPermohonan);
			if (kp.getLokasi().getId().toString().equalsIgnoreCase("02")) {
				kp.setFlagDowngrade("0");
			} else {
				kp.setFlagDowngrade(getParam("valueFlagDowngrade"));
			}
			kp.setPerakuan(getParamAsInteger("valuePerakuan"));

			if (samaKelasLayak == false) {
				if (samaKelasDowngrade == false) {
					for (int i = 0; i < agihan.size(); i++) {
						mp.remove(mp.find(KuaAgihan.class, agihan.get(i)
								.getId()));
					}
				} else {
					mp.remove(mp.find(KuaAgihan.class, agihan.get(0).getId()));
				}
			}

			mp.commit();
			success = true;

		} catch (Exception e) {
			System.out.println("Error hantarPermohonan : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}

		context.put("success", success);
		simpanAuditTrail(idPermohonan, "hantarPermohonan");
		return getPath() + "/result/hantarPermohonan.vm";
	}

	/*--------------------------------------------------------------- KEMASKINI PERMOHONAN ---------------------------------------------------------------*/
	@SuppressWarnings("unchecked")
	@Command("kemaskiniPermohonan")
	public String kemaskiniPermohonan() {
		boolean success = false;
		String idPermohonan = getParam("idPermohonan");
		boolean samaKelasLayak = true;
		boolean samaKelasDowngrade = true;

		try {
			mp = new MyPersistence();

			KuaPermohonan kp = (KuaPermohonan) mp.find(KuaPermohonan.class,
					idPermohonan);
			UsersJob uj = (UsersJob) mp
					.get("SELECT uj FROM UsersJob uj WHERE uj.users.id = '"
							+ kp.getPemohon().getId() + "'");
			List<KuaAgihan> agihan = mp
					.list("SELECT a FROM KuaAgihan a WHERE a.permohonan.id = '"
							+ idPermohonan
							+ "' AND a.kuarters.id IS NULL ORDER BY a.kelasKuarters ASC");
			String flagDowngradeAsal=kp.getFlagDowngrade();
			if (agihan.size() > 0) {
				if (!uj.getGredJawatan().getKelasKuarters().equals(
						agihan.get(0).getKelasKuarters())) {
					samaKelasLayak = false;
				}
				if (agihan.size() > 1) {
					if (!getKelasDowngrade(uj.getGredJawatan().getKelasKuarters()).equals(agihan.get(1).getKelasKuarters())) {
						samaKelasDowngrade = false;
					}
				}
			}

			if (samaKelasLayak == false) {
				kp.setStatus((Status) mp.find(Status.class, "1438090674821"));
			}
			kp.setTarikhKemaskini(new Date());
			
			if (kp.getLokasi().getId().toString().equalsIgnoreCase("02")) {
				kp.setFlagDowngrade("0");
			} else {
				kp.setFlagDowngrade(getParam("valueFlagDowngrade"));
			}
			kp.setPerakuan(getParamAsInteger("valuePerakuan"));
	
			//check jika kes mohon downgrade			
			String flagDowngradeBaru=getParam("valueFlagDowngrade");
			if(flagDowngradeAsal!=null && flagDowngradeBaru!=null)
			{
				if(flagDowngradeAsal.equalsIgnoreCase(flagDowngradeBaru)==false)
				{
					if(kp.getStatus().getId().equalsIgnoreCase("1419601227590"))//menunggu
					{
						kp.setFlagMohonDowngrade("Y");
						kp.setDateMohonDowngrade(new Date());
						simpanCatatanPemohonDowngrade(kp.getId());
					}	
				}
			}else if(flagDowngradeAsal==null && flagDowngradeBaru!=null)
			{
				if(kp.getStatus().getId().equalsIgnoreCase("1419601227590"))//menunggu
				{
					kp.setFlagMohonDowngrade("Y");
					kp.setDateMohonDowngrade(new Date());
					simpanCatatanPemohonDowngrade(kp.getId());
				}	
			}
			
			mp.begin();

			if (samaKelasLayak == false) {
				if (samaKelasDowngrade == false) {
					for (int i = 0; i < agihan.size(); i++) {
						mp.remove(mp.find(KuaAgihan.class, agihan.get(i)
								.getId()));
					}
				} else {
					mp.remove(mp.find(KuaAgihan.class, agihan.get(0).getId()));
				}
			}
			mp.commit();
			success = true;

		} catch (Exception e) {
			System.out.println("Error kemaskiniPermohonan : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}

		context.put("success", success);
		simpanAuditTrail(idPermohonan, "kemaskiniPermohonan");
		return getPath() + "/result/kemaskiniPermohonan.vm";
		
	}
	
	/*START GET NO.GILIRAN LAMA*/
	/*@SuppressWarnings("unchecked")
	@Command("getNoGiliran")
	public String getNoGiliran() throws Exception {
		try {
			mp = new MyPersistence();
			userId = (String) request.getSession()
					.getAttribute("_portal_login");
			List<Giliran> giliran = mp
					.list("SELECT x FROM Giliran x WHERE x.noKP = '" + userId
							+ "'");
			// if (giliran.isEmpty() != true) {
			if (giliran.size() > 0) {
				if (giliran.get(0) != null) {
					context.put("giliran", giliran);
					context.put("rekod", true);
				} else {
					context.put("rekod", false);
				}
			}

		} catch (Exception e) {
			System.out.println("Error getNoGiliran : " + e.getMessage());
		}
		return getPath() + "/footer.vm";
	}*/
	/*END GET NO.GILIRAN LAMA*/

	/*START GET NO.GILIRAN BARU DARI KUA AGIHAN*/
	@SuppressWarnings("unchecked")
	@Command("getNoGiliran")
	public String getNoGiliran() throws Exception {
		try {
			mp = new MyPersistence();
			userId = (String) request.getSession().getAttribute("_portal_login");
			List<KuaAgihan> giliran = mp.list("SELECT x FROM KuaAgihan x WHERE x.pemohon.id = '" + userId+ "'");
			// if (giliran.isEmpty() != true) {
			if (giliran.size() > 0) {
				if (giliran.get(0) != null) {
					context.put("giliran", giliran);
					context.put("rekod", true);
				} else {
					context.put("rekod", false);
				}
			}
		} catch (Exception e) {
			System.out.println("Error getNoGiliran : " + e.getMessage());
		}
		return getPath() + "/footer.vm";
	}
	/*END GET NO.GILIRAN BARU DARI KUA AGIHAN*/
	
	
	// rozai add 31-5-2016
	public Integer compareDatePermohonanDateRadius(String idPermohonan) {
		Date date1 = null;
		Date date2 = null;
		int num = 0;
		try {
			mp = new MyPersistence();
			KuaPermohonan rekodPermohonan = (KuaPermohonan) mp.find(
					KuaPermohonan.class, getParam("idPermohonan"));
			if (rekodPermohonan != null) {
				date1 = rekodPermohonan.getTarikhPermohonan();
				if (date1 == null) {
					date1 = new Date();
				}
			} else {
				date1 = new Date();
			}
			String StringTarikhRadius = "01-01-2015";
			date2 = new SimpleDateFormat("dd-MM-yyyy")
					.parse(StringTarikhRadius);
			num = date1.compareTo(date2);
		} catch (Exception e) {
			System.out.println("Error compareDatePermohonanDateRadius : "
					+ e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		return num;
	}
	
	public void simpanCatatanPemohonNaikTaraf(String idPermohonan,String gredLama,String gredBaru) {
		userId = (String) request.getSession().getAttribute("_portal_login");
		try {
			Users pemohon=((Users) mp.find(Users.class, userId));
			//String nama=pemohon.getUserName();
			KuaPermohonan r = (KuaPermohonan) mp.find(KuaPermohonan.class, idPermohonan);
			Date t = new Date();
			Format formatter = new SimpleDateFormat("dd/MM/yyyy");
			String s = formatter.format(t);
			String catatanPemohon="PEMOHON MEMBUAT PERTUKARAN GRED PERKHIDMATAN DARI GRED("+gredLama+") KEPADA GRED("+gredBaru+")";
			String catatanDulu=r.getCatatan();
			if(catatanDulu!=null){
				r.setCatatan(catatanDulu+"&#13;&#10;"+"("+s+") "+catatanPemohon);
			}else
			{
				r.setCatatan("("+s+") "+catatanPemohon);
			}
		} catch (Exception e) {
			System.out.println("Error simpanCatatan : " + e.getMessage());
		} 
	}
	
	public void simpanCatatanPemohonTukarLokasi(String idPermohonan,String alamatLama,String alamatBaru){
		userId = (String) request.getSession().getAttribute("_portal_login");
		try {
			Users pemohon=((Users) mp.find(Users.class, userId));
			KuaPermohonan r = (KuaPermohonan) mp.find(KuaPermohonan.class, idPermohonan);
			Date t = new Date();
			Format formatter = new SimpleDateFormat("dd/MM/yyyy");
			String s = formatter.format(t);
			String catatanPemohon="PEMOHON MEMBUAT PERMOHONAN PERTUKARAN ALAMAT PEKERJAAN DARI ("+alamatLama+") KEPADA ("+alamatBaru+")";
			String catatanDulu=r.getCatatan();
			if(catatanDulu!=null){
				r.setCatatan(catatanDulu+"&#13;&#10;"+"("+s+") "+catatanPemohon);
			}else
			{
				r.setCatatan("("+s+") "+catatanPemohon);
			}
		} catch (Exception e) {
			System.out.println("Error simpanCatatan : " + e.getMessage());
		} 
	}
	
	public void simpanCatatanPemohonDowngrade(String idPermohonan){
		userId = (String) request.getSession().getAttribute("_portal_login");
		try {
			Users pemohon=((Users) mp.find(Users.class, userId));
			KuaPermohonan r = (KuaPermohonan) mp.find(KuaPermohonan.class, idPermohonan);
			Date t = new Date();
			Format formatter = new SimpleDateFormat("dd/MM/yyyy");
			String s = formatter.format(t);
			String catatanPemohon="PEMOHON MEMBUAT PERMOHONAN DOWNGRADE KUARTERS ";
			String catatanDulu=r.getCatatan();
			if(catatanDulu!=null){
				r.setCatatan(catatanDulu+"&#13;&#10;"+"("+s+") "+catatanPemohon);
			}else
			{
				r.setCatatan("("+s+") "+catatanPemohon);
			}
		} catch (Exception e) {
			System.out.println("Error simpanCatatan : " + e.getMessage());
		} 
	}
	@Command("kelulusan1TukarLokasi")
	public String kelulusan1TukarLokasi() {// fungsi untuk semak kelayakan pemohon
		Boolean kelulusan = false;
		boolean newRecord = false;
		boolean newRecordKerja = false;
		boolean newRecordPinjaman = false;
		boolean result = false;// tarikh jangka siap
		boolean result2 = false;// radius rumah dimiliki
		@SuppressWarnings("unused")
		boolean result3 = false;// prima/ppa1m mesti diduduki
		@SuppressWarnings("unused")
		int jenisAktiviti = 2;
		@SuppressWarnings("unused")
		int jenisAktivitiKerja = 2;
		@SuppressWarnings("unused")
		int jenisAktivitiPinjaman = 2;
		String idPermohonan = getParam("idPermohonan");
		boolean checkPoskod = false;// poskod ikut state

		// rozai add 31-5-2016
		int valueSemakanTarikhPermohonan = 0;
		valueSemakanTarikhPermohonan = compareDatePermohonanDateRadius(idPermohonan);

		try {
			mp = new MyPersistence();

			// dapatkan lokasi permohonan dan lokasi kuarters
			LokasiPermohonan lokasiPermohonan = (LokasiPermohonan) mp.find(
					LokasiPermohonan.class, getParam("idLokasiPermohonan"));
			Bandar bandarLokasi = (Bandar) mp.find(Bandar.class,
					getParam("idBandarPekerjaan"));
			Poskod poskod = (Poskod) mp.find(Poskod.class,
					getParam("poskodPekerjaan"));

			if (poskod != null) {
				if (poskod.getIdBandar().equalsIgnoreCase(bandarLokasi.getId())) {
					checkPoskod = true;
					context.put("checkPoskod", checkPoskod);
				} else {
					checkPoskod = false;
					context.put("checkPoskod", checkPoskod);
				}
			} else {
				checkPoskod = false;
				context.put("checkPoskod", checkPoskod);
			}

			// ------------------start rozai upgrade
			// ------18/11/2015--------------------------
			if ("14".equals(lokasiPermohonan.getNegeri().getId())
					|| "15".equals(lokasiPermohonan.getNegeri().getId())
					|| "16".equals(lokasiPermohonan.getNegeri().getId())
					|| "01".equals(lokasiPermohonan.getNegeri().getId())
					|| "10".equals(lokasiPermohonan.getNegeri().getId())) {

				if (bandarLokasi.getNegeri().getId().equals(
						lokasiPermohonan.getNegeri().getId())) {
					result = true;
					kelulusan = true;
					context.put("kelulusan", kelulusan);
				} else {
					result = false;
					kelulusan = false;
					context.put("kelulusan", kelulusan);
				}

				if ("16".equals(lokasiPermohonan.getNegeri().getId())
						&& bandarLokasi.getId().equals("1052")) {
					// PUTRAJAYA--CYBERJAYA
					result = true;
					kelulusan = true;
					context.put("kelulusan", kelulusan);
				}

				if ("10".equals(lokasiPermohonan.getNegeri().getId())
						&& bandarLokasi.getId().equals("1401")) {
					// PETALING JAYA--KUALA LUMPUR
					result = true;
					kelulusan = true;
					context.put("kelulusan", kelulusan);
				}

			} else if ("05".equals(lokasiPermohonan.getNegeri().getId())) { // KLIA

				// NILAI //SEPANG //B.B.ENSTEK
				if (bandarLokasi.getId().equals("0517")
						|| bandarLokasi.getId().equals("1035")
						|| bandarLokasi.getId().equals("0543")) {
					result = true;
					kelulusan = true;
					context.put("kelulusan", kelulusan);
				} else {
					result = false;
					kelulusan = false;
					context.put("kelulusan", kelulusan);
				}
			}

			// kelulusan = true;
			// context.put("kelulusan", kelulusan);
			// ---------------------------end rozai upgrade
			// 18/11/2015----------------------------------------------------------------------
			/* Kiraan Radius dan Tarikh Jangka Siap Kuarters */
			int pinjamanPerumahan = getParamAsInteger("valuePinjamanPerumahan");
			String jenisPerumahan = getParam("jenisPerumahan");
			String pembiayaan = getParam("valuePembiayaan");
			String pembelian = getParam("valuePembelian");
			int statusRumah = getParamAsInteger("valueStatusRumah");
			Date t = null;

			if (pinjamanPerumahan == 1) // checkPinjaman(ada)
			{
				if ("swasta".equals(jenisPerumahan)) // checkPerumahan(swasta)
				{
					if ("bank".equals(pembiayaan)) // checkPembiayaan
					{
						if (statusRumah == 1) // checkPembinaan(siap)
						{
							String rumahLuarRadius = checkJarakLangitudeLotitude(mp);
							// rozai add 31-5-2016
							if (valueSemakanTarikhPermohonan < 0) {
								rumahLuarRadius = "true";
							}
							;
							if (rumahLuarRadius == "true") // luar radius
							{
								if ("sendiri".equals(pembelian)) // checkPembelian(sendiri)
								{
									result2 = true;
								} else if ("pasangan".equals(pembelian)) // checkPembelian(pasangan)
								{
									result2 = true;
								} else if ("bersama".equals(pembelian)) // checkPembelian(bersama)
								{
									result2 = true;
								}
							} else {
								if ("sendiri".equals(pembelian)) // checkPembelian(sendiri)
								{
									result2 = true;
								} else if ("pasangan".equals(pembelian)) // checkPembelian(pasangan)
								{

									result2 = true;
								} else if ("bersama".equals(pembelian)) // checkPembelian(bersama)
								{
									result2 = true;
								}
							}
							context.put("result2", result2);
						} else if (statusRumah == 2) // checkPembinaan(dalam
						// pembinaan)
						{
							String[] tarikhJangkaSiap = getParam(
									"tarikhJangkaSiap").split(",");
							String tarikhJangkaSiapBaru = "01-"
									+ getMonth(tarikhJangkaSiap[0].trim())
									+ "-" + tarikhJangkaSiap[1].trim();
							int days = 0;

							try {
								t = new SimpleDateFormat("dd-MM-yyyy")
										.parse(tarikhJangkaSiapBaru);
								days = Util.daysBetween(new Date(), t);
							} catch (ParseException e1) {
								e1.printStackTrace();
							}

							if (days < 365) // kurang dari setahun
							{
								String rumahLuarRadius = checkJarakLangitudeLotitude(mp);
								// rozai add 31-5-2016
								if (valueSemakanTarikhPermohonan < 0) {
									rumahLuarRadius = "true";
								}
								;
								if (rumahLuarRadius == "true") // luar radius
								{
									if ("sendiri".equals(pembelian)) // checkPembelian(sendiri)
									{
										result2 = true;
									} else if ("pasangan".equals(pembelian)) // checkPembelian(pasangan)
									{
										result2 = true;
									} else if ("bersama".equals(pembelian)) // checkPembelian(bersama)
									{
										result2 = true;
									}
								} else // dalam radius
								{
									if ("sendiri".equals(pembelian)) // checkPembelian(sendiri)
									{
										result2 = true;
									} else if ("pasangan".equals(pembelian)) // checkPembelian(pasangan)
									{
										result2 = true;
									} else if ("bersama".equals(pembelian)) // checkPembelian(bersama)
									{
										result2 = true;
									}
								}
								context.put("result2", result2);
							} else // lebih dari setahun
							{
								String rumahLuarRadius = checkJarakLangitudeLotitude(mp);
								// rozai add 31-5-2016
								if (valueSemakanTarikhPermohonan < 0) {
									rumahLuarRadius = "true";
								}
								;
								if (rumahLuarRadius == "true") // luar radius
								{
									if ("sendiri".equals(pembelian)) // checkPembelian(sendiri)
									{
										result2 = true;
									} else if ("pasangan".equals(pembelian)) // checkPembelian(pasangan)
									{
										result2 = true;
									} else if ("bersama".equals(pembelian)) // checkPembelian(bersama)
									{
										result2 = true;
									}
									context.put("result2", result2);
								} else // dalam radius
								{
									if ("sendiri".equals(pembelian)) // checkPembelian(sendiri)
									{
										result2 = true;
									} else if ("pasangan".equals(pembelian)) // checkPembelian(pasangan)
									{
										result2 = true;
									} else if ("bersama".equals(pembelian)) // checkPembelian(bersama)
									{
										result2 = true;
									}
								}

							}
						}
					} else // if pinjaman kerajaan
					{
						if (statusRumah == 1) // checkPembinaan(siap)
						{
							String rumahLuarRadius = checkJarakLangitudeLotitude(mp);
							// rozai add 31-5-2016
							if (valueSemakanTarikhPermohonan < 0) {
								rumahLuarRadius = "true";
							}
							;
							if (rumahLuarRadius == "true") // luar radius
							{
								if ("sendiri".equals(pembelian)) // checkPembelian(sendiri)
								{
									result2 = true;
								} else if ("pasangan".equals(pembelian)) // checkPembelian(pasangan)
								{
									result2 = true;
								} else if ("bersama".equals(pembelian)) // checkPembelian(bersama)
								{
									result2 = true;
								}
							} else {
								if ("sendiri".equals(pembelian)) // checkPembelian(sendiri)
								{
									result2 = false;
								} else if ("pasangan".equals(pembelian)) // checkPembelian(pasangan)
								{
									result2 = true;
								} else if ("bersama".equals(pembelian)) // checkPembelian(bersama)
								{
									result2 = false;
								}
							}
							context.put("result2", result2);
						} else if (statusRumah == 2) // checkPembinaan(dalam
						// pembinaan)
						{
							String[] tarikhJangkaSiap = getParam(
									"tarikhJangkaSiap").split(",");
							String tarikhJangkaSiapBaru = "01-"
									+ getMonth(tarikhJangkaSiap[0].trim())
									+ "-" + tarikhJangkaSiap[1].trim();
							int days = 0;

							try {
								t = new SimpleDateFormat("dd-MM-yyyy")
										.parse(tarikhJangkaSiapBaru);
								days = Util.daysBetween(new Date(), t);
							} catch (ParseException e1) {
								e1.printStackTrace();
							}

							if (days < 365) // kurang dari setahun
							{
								String rumahLuarRadius = checkJarakLangitudeLotitude(mp);
								// rozai add 31-5-2016
								if (valueSemakanTarikhPermohonan < 0) {
									rumahLuarRadius = "true";
								}
								;
								if (rumahLuarRadius == "true") // luar radius
								{
									if ("sendiri".equals(pembelian)) // checkPembelian(sendiri)
									{
										result2 = true;
									} else if ("pasangan".equals(pembelian)) // checkPembelian(pasangan)
									{
										result2 = true;

									} else if ("bersama".equals(pembelian)) // checkPembelian(bersama)
									{
										result2 = true;
									}
								} else // dalam radius
								{
									if ("sendiri".equals(pembelian)) // checkPembelian(sendiri)
									{
										result2 = false;
										result = false;// popup tarikh
									} else if ("pasangan".equals(pembelian)) // checkPembelian(pasangan)
									{
										result2 = true;
										result = true;// popup tarikh
									} else if ("bersama".equals(pembelian)) // checkPembelian(bersama)
									{
										result2 = false;
										result = false;// popup tarikh
									}
								}
								context.put("result2", result2);
							} else // lebih dari setahun
							{
								String rumahLuarRadius = checkJarakLangitudeLotitude(mp);
								// rozai add 31-5-2016
								if (valueSemakanTarikhPermohonan < 0) {
									rumahLuarRadius = "true";
								}
								;
								if (rumahLuarRadius == "true") // luar radius
								{
									if ("sendiri".equals(pembelian)) // checkPembelian(sendiri)
									{
										result2 = true;
									} else if ("pasangan".equals(pembelian)) // checkPembelian(pasangan)
									{
										result2 = true;
									} else if ("bersama".equals(pembelian)) // checkPembelian(bersama)
									{
										result2 = true;
									}
									context.put("result2", result2);
								} else // dalam radius
								{
									if ("sendiri".equals(pembelian)) // checkPembelian(sendiri)
									{
										result2 = true;
									} else if ("pasangan".equals(pembelian)) // checkPembelian(pasangan)
									{
										result2 = true;
									} else if ("bersama".equals(pembelian)) // checkPembelian(bersama)
									{
										result2 = true;
									}
								}

							}
						}
					}
				} else // checkPerumahan(PRIMA/PPA1M)
				{
					if (statusRumah == 1) // checkPembinaan(siap)
					{
						String rumahLuarRadius = checkJarakLangitudeLotitude(mp);
						// rozai add 31-5-2016
						if (valueSemakanTarikhPermohonan < 0) {
							rumahLuarRadius = "true";
						}
						;
						if (rumahLuarRadius == "true")// luar radius
						{
							if ("sendiri".equals(pembelian)) // checkPembelian(sendiri)
							{
								result2 = true;
							} else if ("pasangan".equals(pembelian)) // checkPembelian(pasangan)
							{
								result2 = true;
							} else if ("bersama".equals(pembelian)) // checkPembelian(bersama)
							{
								result2 = true;
							}
						} else {
							if ("sendiri".equals(pembelian)) // checkPembelian(sendiri)
							{
								result2 = false;
							} else if ("pasangan".equals(pembelian)) // checkPembelian(pasangan)
							{
								result2 = false;
							} else if ("bersama".equals(pembelian)) // checkPembelian(bersama)
							{
								result2 = false;
							}
						}
						context.put("result2", result2);
					} else if (statusRumah == 2) // checkPembinaan(dalam
					// pembinaan)
					{
						String[] tarikhJangkaSiap = getParam("tarikhJangkaSiap")
								.split(",");
						String tarikhJangkaSiapBaru = "01-"
								+ getMonth(tarikhJangkaSiap[0].trim()) + "-"
								+ tarikhJangkaSiap[1].trim();
						int days = 0;

						try {
							t = new SimpleDateFormat("dd-MM-yyyy")
									.parse(tarikhJangkaSiapBaru);
							days = Util.daysBetween(new Date(), t);
						} catch (ParseException e1) {
							e1.printStackTrace();
						}

						if (days < 365) // kurang dari setahun
						{
							String rumahLuarRadius = checkJarakLangitudeLotitude(mp);
							// rozai add 31-5-2016
							if (valueSemakanTarikhPermohonan < 0) {
								rumahLuarRadius = "true";
							}
							;
							if (rumahLuarRadius == "true") // luar radius
							{
								if ("sendiri".equals(pembelian)) // checkPembelian(sendiri)
								{
									result = true;
								} else if ("pasangan".equals(pembelian)) // checkPembelian(pasangan)
								{
									result = true;
								} else if ("bersama".equals(pembelian)) // checkPembelian(bersama)
								{
									result = true;
								}
							} else // dalam radius
							{
								if ("sendiri".equals(pembelian)) // checkPembelian(sendiri)
								{
									result = false;
								} else if ("pasangan".equals(pembelian)) // checkPembelian(pasangan)
								{
									result = true;
								} else if ("bersama".equals(pembelian)) // checkPembelian(bersama)
								{
									result = false;
								}
							}
							context.put("result", result);
						} else // lebih dari setahun
						{
							String rumahLuarRadius = checkJarakLangitudeLotitude(mp);
							// rozai add 31-5-2016
							if (valueSemakanTarikhPermohonan < 0) {
								rumahLuarRadius = "true";
							}
							;
							if (rumahLuarRadius == "true") // luar radius
							{
								if ("sendiri".equals(pembelian)) // checkPembelian(sendiri)
								{
									result2 = true;
								} else if ("pasangan".equals(pembelian)) // checkPembelian(pasangan)
								{
									result2 = true;
								} else if ("bersama".equals(pembelian)) // checkPembelian(bersama)
								{
									result2 = true;
								}
								context.put("result2", result2);
							} else // dalam radius
							{
								if ("sendiri".equals(pembelian)) // checkPembelian(sendiri)
								{
									result2 = true;
									context.put("result", result);
								} else if ("pasangan".equals(pembelian)) // checkPembelian(pasangan)
								{
									result2 = true;
									context.put("result", result);
								} else if ("bersama".equals(pembelian)) // checkPembelian(bersama)
								{
									result2 = true;
									context.put("result", result);
								}
							}

						}
					}
				}
			} else // checkPinjaman(tiada)
			{
				result = true;
				result2 = true;

				context.put("result", result);
				context.put("result2", result2);

			}

			Users pemohon = (Users) mp.find(Users.class, userId);

			KuaPermohonan kp = null;

			KuaPermohonan permohonan = (KuaPermohonan) mp
					.get("SELECT p FROM KuaPermohonan p WHERE p.pemohon.id = '"
							+ userId + "' AND p.status.id = '1419483289675'");

			mp.begin();

			// String noPermohonan = generateNoPermohonan(mp);

			// String noFail = noPermohonan;
			Status status = (Status) mp.find(Status.class, "1419483289675");

			if (!"".equals(idPermohonan)) {
				kp = (KuaPermohonan) mp.find(KuaPermohonan.class, idPermohonan);
			} else {
				if (permohonan != null) {
					kp = (KuaPermohonan) mp.find(KuaPermohonan.class,
							permohonan.getId());
				}
			}

			if (kp == null) {
				// jenisAktiviti = 1;
				newRecord = true;
				kp = new KuaPermohonan();
			} else {
				String statusTerkini = kp.getStatus().getId().toString();
				if ("1419483289675".equals(statusTerkini))// deraf permohonan
				{
					status = (Status) mp.find(Status.class, "1419483289675");
				} else if ("1419483289678".equals(statusTerkini))// permohonan
				// baru
				{
					status = (Status) mp.find(Status.class, "1419483289678");
				} else if ("1431405647299".equals(statusTerkini))// permohonan
				// kemaskini
				{
					status = (Status) mp.find(Status.class, "1431405647299");
				} else {
					status = (Status) mp.find(Status.class, statusTerkini);
				}

				// if (!"".equals(kp.getNoPermohonan()))
				// noPermohonan = kp.getNoPermohonan();
				// if (!"".equals(kp.getNoFail()))
				// noFail = kp.getNoFail();
			}

			kp.setPemohon(pemohon);
			kp.setLokasi(lokasiPermohonan);
			// kp.setNoFail(noFail);
			// kp.setNoPermohonan(noPermohonan);
			//kp.setTarikhMasuk(new Date());
			if (newRecord == false) {
				kp.setTarikhKemaskini(new Date());
			}
			kp.setStatus(status);
			kp.setKelulusan1(kelulusan.toString());

			if (result == true && result2 == true) {
				kp.setKelulusan3("true");
				kp.setPinjaman(1);
			} else {
				kp.setKelulusan3("false");
				kp.setPinjaman(0);
			}

			if (newRecord == true) {
				mp.persist(kp);
			}

			idPermohonan = kp.getId();

			// db.commit(request, "PROCESSING FILE (Permohonan Kuarters) : "+
			// idPermohonan, jenisAktiviti);

			context.put("idPermohonan", idPermohonan);

			UsersJob uj = null;
			UsersJob usersJob = (UsersJob) mp
					.get("SELECT usersJob FROM UsersJob usersJob WHERE usersJob.users.id = '"
							+ userId + "'");
			if (usersJob != null) {
				uj = (UsersJob) mp.find(UsersJob.class,
						usersJob.getId() != null ? usersJob.getId() : "");
			}

			Bandar bandarPekerjaan = (Bandar) mp.find(Bandar.class,
					getParam("idBandarPekerjaan"));

			if (uj == null) {
				jenisAktivitiKerja = 1;
				newRecordKerja = true;
				uj = new UsersJob();
			}

			uj.setUsers(pemohon);
			// uj.setAlamat1(getParam("alamatPekerjaan1"));
			// uj.setAlamat2(getParam("alamatPekerjaan2"));
			// uj.setAlamat3(getParam("alamatPekerjaan3"));
			uj.setPoskod(getParam("poskodPekerjaan"));
			uj.setBandar(bandarPekerjaan);

			context.put("uj", uj);

			if (newRecordKerja == true) {
				mp.persist(uj);
			}

			// db.commit(request, "PROCESSING FILE (Pekerjaan Pemohon) : "+
			// uj.getId(), jenisAktivitiKerja);

			KuaPinjamanPemohon kpp = null;

			KuaPinjamanPemohon pinjaman = (KuaPinjamanPemohon) mp
					.get("SELECT p FROM KuaPinjamanPemohon p WHERE p.users.id = '"
							+ userId + "'");
			if (pinjaman != null)
				kpp = (KuaPinjamanPemohon) mp.find(KuaPinjamanPemohon.class,
						pinjaman.getId() != null ? pinjaman.getId() : "");

			Bandar bandarPinjaman = (Bandar) mp.find(Bandar.class,
					getParam("idBandarPinjaman"));

			if (kpp == null) {
				jenisAktivitiPinjaman = 1;
				newRecordPinjaman = true;
				kpp = new KuaPinjamanPemohon();
			}

			kpp.setUsers(pemohon);
			kpp.setPinjamanPerumahan(pinjamanPerumahan);
			kpp.setStatusPembinaan(statusRumah);
			kpp.setTarikhJangkaSiap(t);
			kpp.setPoskodPinjaman(getParam("poskodPinjaman"));
			kpp.setBandar(bandarPinjaman);
			kpp.setJenisPerumahan(jenisPerumahan);
			kpp.setPembiayaan(pembiayaan);
			kpp.setPembelian(getParam("valuePembelian"));
			kpp.setAlamat1(getParam("alamatPinjaman1"));
			kpp.setAlamat2(getParam("alamatPinjaman2"));
			kpp.setAlamat3(getParam("alamatPinjaman3"));

			if (newRecordPinjaman == true) {
				mp.persist(kpp);
			}

			// db.commit(request, "PROCESSING FILE (Pinjaman Pemohon) : "+
			// uj.getId(), jenisAktivitiPinjaman);

			mp.commit();
			context.remove("resultPermohonanCompletion");
			// context.put("resultPermohonanCompletion",
			// getCompletionPermohonan(kp.getId()));
			// context.put("resultPermohonanCompletion", kelulusan.toString());
		} catch (Exception e) {
			System.out.println("Error kelulusan1 : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		// rozai add 29/3/2016
		// context.remove("resultPermohonanCompletion");
		context.put("resultPermohonanCompletion", getCompletionPermohonan(mp,
				getParam("idPermohonan")));
		context.remove("statusPerkahwinan");
		context.put("statusPerkahwinan", getStatusPerkahwinanPemohon(
				mp, getParam("idPermohonan")));
		return getPath() + "/result/kelulusan1.vm";
	}
	
	@Command("simpanTukarLokasi")
	public String simpanTukarLokasi() {
		try {
			mp = new MyPersistence();
			KuaPermohonan kp = (KuaPermohonan) mp.find(KuaPermohonan.class,getParam("idPermohonan"));
			kp.setFlagMohonTukar("Y");
			kp.setDateMohonTukar(new Date());
			mp.begin();
			mp.commit();
			Boolean success = true;
			context.put("success", success);
		} catch (Exception e) {
			System.out.println("Error simpanTukarLokasi : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/sub_page/result/simpanResultTukarLokasi.vm";
	}
	
	@Command("simpanNaikTaraf")
	public String simpanNaikTaraf() {
		boolean addBaru=false;
		KuaPermohonan kpBaru=null;
		try {
			mp = new MyPersistence();
			KuaPermohonan kp = (KuaPermohonan) mp.find(KuaPermohonan.class,getParam("idPermohonan"));
//			String statusPermohonan=kp.getStatus().getId();			
			mp.begin();
			
			/*KALAU PEMOHON ADALAH STATUS PENGHUNI,DAPAT NO.DAFTAR YANG BARU*/
//			if(statusPermohonan.equalsIgnoreCase("1423101446117"))
//			{
//				kpBaru=new KuaPermohonan();
//				kpBaru.setPemohon(kp.getPemohon());
//				kpBaru.setLokasi(kp.getLokasi());
//				kpBaru.setStatus((Status) mp.find(Status.class,"1419483289678"));
//				kpBaru.setKelulusan1(kp.getKelulusan1());
//				kpBaru.setKelulusan2(kp.getKelulusan2());
//				kpBaru.setKelulusan3(kp.getKelulusan3());
//				
//				kpBaru.setPekerjaan(kp.getPekerjaan());
//				kpBaru.setPeribadi(kp.getPeribadi());
//				kpBaru.setPasangan(kp.getPasangan());
//				kpBaru.setPinjaman(kp.getPinjaman());
//				
//				kpBaru.setTarikhPermohonan(new Date());
//				kpBaru.setDatePermohonan(new Date());
//				kpBaru.setTarikhKemaskini(new Date());
//
//				String noPermohonan = generateNoPermohonan(mp);
//				kpBaru.setNoPermohonan(noPermohonan);
//				if (kp.getLokasi().getId().toString().equalsIgnoreCase("02")) {
//					kpBaru.setFlagDowngrade("0");
//				} else {
//					kpBaru.setFlagDowngrade("1");
//				}
//				kpBaru.setFlagMohonUpgrade("Y");
//				kpBaru.setDateMohonUpgrade(new Date());
//				kpBaru.setPerakuan(kp.getPerakuan());
//				addBaru=true;
//			}
			
			kp.setFlagMohonUpgrade("Y");
			kp.setDateMohonUpgrade(new Date());
			if (addBaru == true) {
				mp.persist(kpBaru);
			}
			mp.commit();
			Boolean success = true;
			context.put("success", success);
		} catch (Exception e) {
			System.out.println("Error simpanTukarLokasi : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/sub_page/result/simpanResultNaikTaraf.vm";
	}
	
}
