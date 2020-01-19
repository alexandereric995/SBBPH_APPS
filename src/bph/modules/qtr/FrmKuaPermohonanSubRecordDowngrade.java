package bph.modules.qtr;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import lebah.db.UniqueID;
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
import bph.entities.kod.JenisPengenalan;
import bph.entities.kod.JenisPerkhidmatan;
import bph.entities.kod.KelasPerkhidmatan;
import bph.entities.kod.LokasiPermohonan;
import bph.entities.kod.Negeri;
import bph.entities.kod.Status;
import bph.entities.kod.StatusPekerjaan;
import bph.entities.kod.StatusPerkahwinan;
import bph.entities.kod.StatusPerkhidmatan;
import bph.entities.qtr.KuaAgihan;
import bph.entities.qtr.KuaKelainanUpaya;
import bph.entities.qtr.KuaPermohonan;
import bph.entities.qtr.KuaPinjamanPemohon;
import bph.utils.DataUtil;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class FrmKuaPermohonanSubRecordDowngrade extends FrmKuaPermohonanRecord {

	/**
	 * 
	 */
	private static final long serialVersionUID = -302245884484195171L;
	static Logger myLogger = Logger
			.getLogger("bph/modules/qtr/FrmKuaPermohonanSubRecord");
	private DataUtil dataUtil;
	private KuartersUtils kuaUtil = new KuartersUtils();
	private String uploadDir = ResourceBundle.getBundle("dbconnection")
			.getString("folder");
	private MyPersistence mp;

	@SuppressWarnings("unchecked")
	@Override
	public void begin() {
		// TODO Auto-generated method stub
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

		addFilter("pemohon.id = '" + userId + "'");

		context.remove("currentRoleQTR");

		mp = new MyPersistence();
		List<KuaPermohonan> kpList = mp
				.list("SELECT kp FROM KuaPermohonan kp WHERE kp.pemohon.id = '"
						+ userId + "'");
		boolean a = false;

		// AZAM CHANGE ON 16/1/2016
		for (KuaPermohonan r : kpList) {
			if (r.getStatus() != null) {
				if ("1431903258428".equals(r.getStatus().getId()) // KELUAR
						// KUARTERS
						|| "1419601227598".equals(r.getStatus().getId())// PERMOHONAN
						// DITOLAK
						|| "1431327994521".equals(r.getStatus().getId())// KUARTERS
						// DITOLAK
						|| "1431327994524".equals(r.getStatus().getId()) // PERMOHONAN
				// DIBATALKAN
				) {
					a = false;
				} else {
					a = true;
					break;
				}
			}
		}

		setDisableAddNewRecordButton(a);

		List<KuaPermohonan> listPermohonan = db
				.list("SELECT kp FROM KuaPermohonan kp WHERE kp.pemohon.id = '"
						+ userId + "'");
		context.put("listPermohonan", listPermohonan);

		context.put("selectNegeriPekerjaan", dataUtil.getListNegeri());
		context.put("selectNegeriPinjaman", dataUtil.getListNegeri());
		context.put("selectLokasiPermohonan", dataUtil
				.getListLokasiPermohonan());

		context.put("newRecord", "true");

		getPerakuan();
	}

	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/qtr/downgrade";
	}

	@Override
	public void getRelatedData(KuaPermohonan r) {
		// TODO Auto-generated method stub
		boolean kelulusan = false;

		UsersJob uj = (UsersJob) db
				.get("SELECT uj FROM UsersJob uj WHERE uj.users.id = '"
						+ r.getPemohon().getId() + "'");

		context.put("resultPermohonanCompletion", getCompletionPermohonan(r
				.getId()));

		KuaPermohonan kp = db.find(KuaPermohonan.class, r.getId());

		if ("true".equals(kp.getKelulusan1())
				&& "true".equals(kp.getKelulusan3())) {
			context.put("disableAddNewRecordButton", true);
			kelulusan = true;
		}

		context.put("kelulusan", kelulusan);

		context.remove("kpp");
		KuaPinjamanPemohon kpp = (KuaPinjamanPemohon) db
				.get("SELECT kpp FROM KuaPinjamanPemohon kpp WHERE kpp.users.id = '"
						+ userId + "'");

		if (kpp != null)
			context.put("kpp", db.find(KuaPinjamanPemohon.class, kpp.getId()));

		context.put("uj", uj);
		context.put("newRecord", "false");

		if (r.getStatus().getId().equalsIgnoreCase("1431405647299")
				|| r.getStatus().getId().equalsIgnoreCase("1419483289678")) {
			context.put("kemaskini", "true");
		}
		// if(r.status.id="1419483289675")
	}

	@Command("newPermohonan")
	public String newPermohonan() {
		return getPath() + "/new/start.vm";
	}

	@Command("getPermohonan")
	public String getPermohonan() {
		boolean kelulusan = false;
		String idPermohonan = getParam("idPermohonan");

		// myLogger.debug("ID PERMOHONAN ::: " + idPermohonan);

		KuaPermohonan kp = db.find(KuaPermohonan.class, idPermohonan);

		if ("true".equals(kp.getKelulusan1())
				&& "true".equals(kp.getKelulusan3())) {
			context.put("disableAddNewRecordButton", true);
			kelulusan = true;
		}

		context.put("kelulusan", kelulusan);
		context.put("r", kp);
		context
				.put(
						"kpp",
						db
								.get("SELECT pp FROM KuaPinjamanPemohon pp WHERE pp.users.id = '"
										+ userId + "'"));

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
		boolean result = false;
		boolean result2 = false;
		@SuppressWarnings("unused")
		int jenisAktiviti = 2;
		@SuppressWarnings("unused")
		int jenisAktivitiKerja = 2;
		@SuppressWarnings("unused")
		int jenisAktivitiPinjaman = 2;
		String idPermohonan = getParam("idPermohonan");

		/* Dapatkan Lokasi Permohonan dan Lokasi Kuarters */
		LokasiPermohonan lp = db.find(LokasiPermohonan.class,
				getParam("idLokasiPermohonan"));
		Bandar b = db.find(Bandar.class, getParam("idBandarPekerjaan"));

		// ------------------start rozai upgrade
		// 18/11/2015--------------------------

		if ("14".equals(lp.getNegeri().getId())
				|| "15".equals(lp.getNegeri().getId())
				|| "16".equals(lp.getNegeri().getId())) {
			if (b.getNegeri().getId().equals(lp.getNegeri().getId())) {
				kelulusan = true;
				context.put("kelulusan", kelulusan);
			} else {
				context.put("kelulusan", kelulusan);
			}
		} else if ("05".equals(lp.getNegeri().getId())) {// N9
			if (b.getId().equals("0517") || b.getId().equals("1035")
					|| b.getId().equals("1043")) {
				kelulusan = true;
				context.put("kelulusan", kelulusan);
			} else {
				context.put("kelulusan", kelulusan);
			}
		} else {
			if (b.getNegeri().getId().equals(lp.getNegeri().getId())) {
				if (b.getId().equals(lp.getBandar().getId())) {
					kelulusan = true;
					context.put("kelulusan", kelulusan);
				}
			} else {
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
						String jarak = checkJarakLangitudeLotitude(mp);
						if (jarak == "true") // luar radius
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
							String jarak = checkJarakLangitudeLotitude(mp);
							if (jarak == "true") // luar radius
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
							String jarak = checkJarakLangitudeLotitude(mp);
							if (jarak == "true") // luar radius
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
						String jarak = checkJarakLangitudeLotitude(mp);
						if (jarak == "true") // luar radius
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
							String jarak = checkJarakLangitudeLotitude(mp);
							if (jarak == "true") // luar radius
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
								} else if ("pasangan".equals(pembelian)) // checkPembelian(pasangan)
								{
									result2 = true;
								} else if ("bersama".equals(pembelian)) // checkPembelian(bersama)
								{
									result2 = false;
								}
							}
							context.put("result2", result2);
						} else // lebih dari setahun
						{
							String jarak = checkJarakLangitudeLotitude(mp);
							if (jarak == "true") // luar radius
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
					String jarak = checkJarakLangitudeLotitude(mp);
					if (jarak == "true")// luar radius
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
				} else if (statusRumah == 2) // checkPembinaan(dalam pembinaan)
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
						String jarak = checkJarakLangitudeLotitude(mp);
						if (jarak == "true") // luar radius
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
								result = false;
							} else if ("bersama".equals(pembelian)) // checkPembelian(bersama)
							{
								result = false;
							}
						}
						context.put("result", result);
					} else // lebih dari setahun
					{
						String jarak = checkJarakLangitudeLotitude(mp);
						if (jarak == "true") // luar radius
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

		Users pemohon = db.find(Users.class, userId);

		KuaPermohonan kp = null;

		KuaPermohonan permohonan = (KuaPermohonan) db
				.get("SELECT p FROM KuaPermohonan p WHERE p.pemohon.id = '"
						+ userId + "' AND p.status.id = '1419483289675'");

		String noPermohonan = UniqueID.getTransactionNo();
		String noFail = noPermohonan;

		Status status = db.find(Status.class, "1419483289675");

		if (!"".equals(idPermohonan)) {
			kp = db.find(KuaPermohonan.class, idPermohonan);
		} else {
			if (permohonan != null) {
				kp = db.find(KuaPermohonan.class, permohonan.getId());
			}
		}

		if (kp == null) {
			jenisAktiviti = 1;
			newRecord = true;
			kp = new KuaPermohonan();
		} else {
			if (!"1419483289675".equals(kp.getStatus().getId())
					&& !"1419483289678".equals(kp.getStatus().getId())) {
				status = db.find(Status.class, "1431405647299");
			}

			if (!"".equals(kp.getNoPermohonan()))
				noPermohonan = kp.getNoPermohonan();
			if (!"".equals(kp.getNoFail()))
				noFail = kp.getNoFail();
		}

		kp.setPemohon(pemohon);
		kp.setLokasi(lp);
		kp.setNoFail(noFail);
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

		mp.begin();
		if (newRecord == true)
			mp.persist(kp);
		try {
			idPermohonan = kp.getId();

			// db.commit(request, "PROCESSING FILE (Permohonan Kuarters) : "
			// + idPermohonan, jenisAktiviti);
			mp.commit();

			context.put("idPermohonan", idPermohonan);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		UsersJob uj = null;

		UsersJob usersJob = (UsersJob) db
				.get("SELECT usersJob FROM UsersJob usersJob WHERE usersJob.users.id = '"
						+ userId + "'");
		if (usersJob != null)
			uj = db.find(UsersJob.class, usersJob.getId() != null ? usersJob
					.getId() : "");

		Bandar bandarPekerjaan = db.find(Bandar.class,
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
		mp.begin();
		if (newRecordKerja == true)
			mp.persist(uj);
		try {
			// db.commit(request, "PROCESSING FILE (Pekerjaan Pemohon) : "
			// + uj.getId(), jenisAktivitiKerja);
			mp.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		KuaPinjamanPemohon kpp = null;

		KuaPinjamanPemohon pinjaman = (KuaPinjamanPemohon) db
				.get("SELECT p FROM KuaPinjamanPemohon p WHERE p.users.id = '"
						+ userId + "'");
		if (pinjaman != null)
			kpp = db.find(KuaPinjamanPemohon.class, pinjaman.getId() != null
					? pinjaman.getId()
					: "");

		Bandar bandarPinjaman = db.find(Bandar.class,
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

		mp.begin();
		if (newRecordPinjaman == true)
			mp.persist(kpp);
		try {
			// db.commit(request, "PROCESSING FILE (Pinjaman Pemohon) : "
			// + uj.getId(), jenisAktivitiPinjaman);
			mp.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		context.put("resultPermohonanCompletion", getCompletionPermohonan(kp
				.getId()));

		return getPath() + "/result/kelulusan1.vm";
	}

	@Command("kelulusan1Retrieve")
	public String kelulusan1Retrieve() {
		KuaPermohonan kp = db.find(KuaPermohonan.class,
				getParam("idPermohonan"));

		context.put("kelulusan", Boolean.parseBoolean(kp.getKelulusan1()));
		context.put("result", true);
		context.put("result2", true);

		context.put("resultPermohonanCompletion",
				getCompletionPermohonan(getParam("idPermohonan")));

		return getPath() + "/result/kelulusan1.vm";
	}

	@Command("getPerakuan")
	public String getPerakuan() {
		String kelasLayak = "";
		String kelasDowngrade = "";

		UsersJob pekerjaan = (UsersJob) db
				.get("SELECT uj FROM UsersJob uj WHERE uj.users.id = '"
						+ userId + "'");

		// context.put("kelasLayak",
		// pekerjaan.getGredJawatan().getKelasKuarters());
		// context.put("kelasDowngrade",
		// dataUtil.getKelasDowngrade(pekerjaan.getGredJawatan().getKelasKuarters()));

		if (pekerjaan != null) {
			String gredKelasKuarters = "";
			// if ( pekerjaan.getIdGredJawatan() != null ) {
			if (pekerjaan.getGredJawatan() != null) { // rozai add 11/11/2015
				gredKelasKuarters = pekerjaan.getGredJawatan()
						.getKelasKuarters();
			}
			kelasLayak = gredKelasKuarters;
			kelasDowngrade = dataUtil.getKelasDowngrade(gredKelasKuarters);
		}

		context.put("kelasLayak", kelasLayak);
		context.put("kelasDowngrade", kelasDowngrade);

		context.put("resultPermohonanCompletion",
				getCompletionPermohonan(getParam("idPermohonan")));

		return getPath() + "/entry_sub/entry_page_sub_bottom.vm";
	}

	/*--------------------------------------------------------------- DETAIL PERIBADI ---------------------------------------------------------------*/
	@Command("getPeribadi")
	public String getPeribadi() {
		context.put("selectGelaran", dataUtil.getListGelaran());
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

		Users u = db.find(Users.class, userId);

		context.put("users", u);

		// myLogger.debug("IMG NAME ::: " + getParam("imgName"));
		context.put("imgName", getParam("imgName"));

		Date tarikhLahir = new Date();

		try {
			tarikhLahir = kuaUtil.getTarikhLahir(u.getNoKP());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		context.put("tarikhLahir", tarikhLahir);

		context.put("activity", "peribadi");

		context.put("dirUpload", uploadDir + "qtr/permohonan/"
				+ getParam("idPermohonan") + "/");

		return getPath() + "/sub_page/peribadi.vm";
	}

	@Command("selectBandar")
	public String selectBandar() throws Exception {
		dataUtil = DataUtil.getInstance(db);
		String idNegeri = "0";

		if (get("idNegeri").trim().length() > 0)
			idNegeri = get("idNegeri");

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

		Gelaran gelaran = db.find(Gelaran.class, getParam("idGelaran"));
		JenisPengenalan jenisPengenalan = db.find(JenisPengenalan.class,
				getParam("idJenisPengenalan"));
		Jantina jantina = db.find(Jantina.class, getParam("idJantina"));
		Bangsa bangsa = db.find(Bangsa.class, getParam("idBangsa"));
		Etnik etnik = db.find(Etnik.class, getParam("idEtnik"));
		StatusPerkahwinan statusPerkahwinan = db.find(StatusPerkahwinan.class,
				getParam("idStatusPerkahwinan"));
		Bandar bandar = db.find(Bandar.class, getParam("idBandar"));
		Bandar bandarSemasa = db.find(Bandar.class, getParam("idBandarSemasa"));
		KuaPermohonan kp = db.find(KuaPermohonan.class,
				getParam("idPermohonan"));

		Users users = db.find(Users.class, userId);

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
		try {
			kp.setPeribadi(1);

			kuaUtil.kuartersLog(2, "Users", db.find(Users.class, userId), users
					.getId());

			// db.commit(request, "PROCESSING FILE (Penghuni - Peribadi) : "
			// + users.getId(), 2);
			mp.commit();

			success = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		context.put("success", success);

		context.put("resultPermohonanCompletion",
				getCompletionPermohonan(getParam("idPermohonan")));

		return getPath() + "/sub_page/result/simpanPeribadi.vm";
	}

	@Command("uploadGambar")
	public String uploadGambar() throws Exception {
		// myLogger.debug("GAMBAR PERSONAL ::: " + getParam("gambarPersonal"));
		// myLogger.debug(kuaUtil.uploadFile(request, "permohonan",
		// getParam("idPermohonan")));

		context.put("imgName", kuaUtil.uploadFile(request, "permohonan",
				getParam("idPermohonan")));

		return getPath() + "/sub_page/peribadi/uploaddoc.vm";
	}

	// @Command("refreshListPeribadi")
	// public String refreshListPeribadi() throws Exception {
	// String imgName = getParam("imgName");
	// // String avatarName = getParam("avatarName");
	//		
	// context.put("imgName", imgName);
	// // context.put("avatarName", avatarName);
	//		
	// return getPath() + "/sub_page/peribadi/listDokumen.vm";
	// }

	@Command("checkEmail")
	public String checkEmail() {
		boolean result = false;

		boolean firstValidate = Util.isValidEmailAddress(getParam("email"));
		boolean secondValidate = Util.validateEmail(getParam("email"));

		if (firstValidate == true && secondValidate == true)
			result = true;

		context.put("result", result);

		return getPath() + "/sub_page/result/resultCheckEmail.vm";
	}

	/*--------------------------------------------------------------- DETAIL PEKERJAAN ---------------------------------------------------------------*/
	@Command("getPekerjaan")
	public String getPekerjaan() throws Exception {
		context.put("selectKelasPerkhidmatan", dataUtil
				.getListKelasPerkhidmatan());
		context.put("selectGredJawatan", dataUtil.getListGredPerkhidmatan());
		context.put("selectJenisPerkhidmatan", dataUtil
				.getListJenisPerkhidmatan());
		context.put("selectStatusPerkhidmatan", dataUtil
				.getListStatusPerkhidmatan());
		context.put("selectKementerian", dataUtil.getListKementerian());
		// context.put("selectJawatan", dataUtil.getListJawatan());

		context.put("activity", "pekerjaan");

		context.remove("uj");
		UsersJob usersJob = (UsersJob) db
				.get("SELECT uj FROM UsersJob uj WHERE uj.users.id = '"
						+ userId + "'");

		if (usersJob != null)
			context.put("uj", db.find(UsersJob.class, usersJob.getId()));

		context.put("users", db.find(Users.class, userId));
		context.put("resultPermohonanCompletion",
				getCompletionPermohonan(getParam("idPermohonan")));

		return getPath() + "/sub_page/pekerjaan.vm";
	}

	@Command("selectBandarPekerjaan")
	public String selectBandarPekerjaan() throws Exception {
		dataUtil = DataUtil.getInstance(db);
		String idNegeri = "0";

		if (get("idNegeriPekerjaan").trim().length() > 0)
			idNegeri = get("idNegeriPekerjaan");

		context.put("selectBandarPekerjaan", dataUtil.getListBandar(idNegeri));

		return getPath() + "/select/selectBandarPekerjaan.vm";
	}

	@Command("selectJabatan")
	public String selectJabatan() throws Exception {
		dataUtil = DataUtil.getInstance(db);
		String idKementerian = "0";

		if (get("idKementerian").trim().length() > 0)
			idKementerian = get("idKementerian");

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

		if (get("idKelasPerkhidmatan").trim().length() > 0)
			idKelasPerkhidmatan = get("idKelasPerkhidmatan");

		context.put("selectJawatan", dataUtil
				.getListJawatan(idKelasPerkhidmatan));

		return getPath() + "/sub_page/pekerjaan/selectJawatan.vm";
	}

	@Command("simpanPekerjaan")
	public String simpanPekerjaan() {
		Boolean result = true;
		Boolean result2 = true;
		boolean success = false;
		boolean newRecord = false;
		int jenisAktiviti = 2;

		Users users = db.find(Users.class, userId);
		KelasPerkhidmatan kelasPerkhidmatan = db.find(KelasPerkhidmatan.class,
				getParam("idKelasPerkhidmatan"));
		GredPerkhidmatan gredJawatan = db.find(GredPerkhidmatan.class,
				getParam("idGredJawatan"));
		Jawatan jawatan = db.find(Jawatan.class, getParam("idJawatan"));
		JenisPerkhidmatan jenisPerkhidmatan = db.find(JenisPerkhidmatan.class,
				getParam("idJenisPerkhidmatan"));
		StatusPerkhidmatan statusPerkhidmatan = db.find(
				StatusPerkhidmatan.class, getParam("idStatusPerkhidmatan"));
		Agensi agensi = db.find(Agensi.class, getParam("idJabatan"));
		BadanBerkanun badanBerkanun = db.find(BadanBerkanun.class,
				getParam("idBadanBerkanun"));
		// Bandar bandar = db.find(Bandar.class, getParam("idBandarPekerjaan"));
		KuaPermohonan kp = db.find(KuaPermohonan.class,
				getParam("idPermohonan"));

		if ("02".equals(statusPerkhidmatan.getId())) {
			int days = Util.daysBetween(new Date(), getDate("tarikhTamat"));

			if (days < 365)
				result = false;

			if (getParamAsInteger("valueFlagITP") == 0
					&& getParamAsInteger("valueFlagEPW") == 0)
				result2 = false;
		}

		UsersJob uj = null;

		UsersJob usersJob = (UsersJob) db
				.get("SELECT uj FROM UsersJob uj WHERE uj.users.id = '"
						+ userId + "'");
		if (usersJob != null)
			uj = db.find(UsersJob.class, usersJob.getId());

		if (uj == null) {
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
		uj.setGajiPokok(Double.parseDouble(getParam("gajiPokok").replaceAll(
				"RM", "").replaceAll(",", "")));
		uj.setJenisPerkhidmatan(jenisPerkhidmatan);
		uj.setStatusPerkhidmatan(statusPerkhidmatan);
		uj.setTarikhTamat(getDate("tarikhTamat"));
		uj.setFlagITP(getParamAsInteger("valueFlagITP"));
		uj.setFlagEPW(getParamAsInteger("valueFlagEPW"));
		uj.setAgensi(agensi);
		uj.setBadanBerkanun(badanBerkanun);
		uj.setBahagian(getParam("bahagian"));
		// uj.setAlamat1(getParam("alamatPekerjaan1"));
		// uj.setAlamat2(getParam("alamatPekerjaan2"));
		// uj.setAlamat3(getParam("alamatPekerjaan3"));
		// uj.setPoskod(getParam("poskodPekerjaan"));
		// uj.setBandar(bandar);
		uj.setNoTelPejabat(getParam("noTelPejabat"));
		uj.setNoFaks(getParam("noFaks"));
		uj.setEmel(getParam("email"));
		uj.setFlagCola(getParamAsInteger("valueFlagCola"));

		mp.begin();
		if (newRecord == true)
			mp.persist(uj);
		try {
			if (!"03".equals(statusPerkhidmatan.getId())) {
				if (result == true && result2 == true) {
					kp.setKelulusan2("true");
				} else {
					kp.setKelulusan2("false");
				}
			} else {
				kp.setKelulusan2("false");
			}

			kp.setPekerjaan(1);

			kuaUtil.kuartersLog(jenisAktiviti, "UsersJob", db.find(Users.class,
					userId), uj.getId());

			// db.commit(request, "PROCESSING FILE (Pekerjaan Penghuni) : "
			// + uj.getId(), jenisAktiviti);
			mp.commit();

			success = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		context.put("success", success);
		context.put("result", result);
		context.put("result2", result2);
		context.put("resultPermohonanCompletion",
				getCompletionPermohonan(getParam("idPermohonan")));

		return getPath() + "/sub_page/result/simpanPekerjaan.vm";
	}

	// @Command("checkTarikhTamatKontrak")
	// public String checkTarikhTamatKontrak() {
	// Boolean result = false;
	//
	// int days = Util.daysBetween(new Date(), getDate("tarikhTamat"));
	//
	// if ( days > 365 ) result = true;
	//
	// context.put("result", result);
	//
	// return getPath() + "/sub_page/result/resultTarikhTamatKontrak.vm";
	// }

	/*--------------------------------------------------------------- DETAIL PASANGAN ---------------------------------------------------------------*/
	@Command("getPasangan")
	public String getPasangan() throws Exception {
		context.put("selectJenisPengenalanPasangan", dataUtil
				.getListJenisPengenalan());
		context.put("selectStatusPekerjaanPasangan", dataUtil
				.getListStatusPekerjaan());
		context.put("selectNegeriPasangan", dataUtil.getListNegeri());
		context.put("selectKelasPerkhidmatanPasangan", dataUtil
				.getListKelasPerkhidmatan());
		context.put("selectGredJawatanPasangan", dataUtil
				.getListGredPerkhidmatan());
		context.put("selectKementerianPasangan", dataUtil.getListKementerian());
		context.put("selectJawatanPasangan", dataUtil.getListJawatan());
		context.put("selectBadanBerkanunPasangan", dataUtil
				.getListBadanBerkanun());

		context.put("activity", "pasangan");

		context.remove("us");
		UsersSpouse usersSpouse = (UsersSpouse) db
				.get("SELECT us FROM UsersSpouse us WHERE us.users.id = '"
						+ userId + "'");

		if (usersSpouse != null)
			context.put("us", db.find(UsersSpouse.class, usersSpouse.getId()));

		context.put("users", db.find(Users.class, userId));
		context.put("resultPermohonanCompletion",
				getCompletionPermohonan(getParam("idPermohonan")));

		return getPath() + "/sub_page/pasangan.vm";
	}

	@Command("selectBandarPasangan")
	public String selectBandarPasangan() throws Exception {
		dataUtil = DataUtil.getInstance(db);
		String idNegeri = "0";

		if (get("idNegeriPasangan").trim().length() > 0)
			idNegeri = get("idNegeriPasangan");

		context.put("selectBandarPasangan", dataUtil.getListBandar(idNegeri));

		return getPath() + "/sub_page/pasangan/selectBandarPasangan.vm";
	}

	@Command("selectJabatanPasangan")
	public String selectJabatanPasangan() throws Exception {
		dataUtil = DataUtil.getInstance(db);
		String idKementerian = "0";

		if (get("idKementerianPasangan").trim().length() > 0)
			idKementerian = get("idKementerianPasangan");

		context.put("selectJabatanPasangan", dataUtil
				.getListAgensi(idKementerian));

		return getPath() + "/sub_page/pasangan/selectJabatanPasangan.vm";
	}

	@Command("simpanPasangan")
	public String simpanPasangan() {
		boolean success = false;
		boolean newRecord = false;
		int jenisAktiviti = 2;

		Users users = db.find(Users.class, userId);
		JenisPengenalan jenisPengenalan = db.find(JenisPengenalan.class,
				getParam("idJenisPengenalanPasangan"));
		StatusPekerjaan statusPekerjaan = db.find(StatusPekerjaan.class,
				getParam("idStatusPekerjaanPasangan"));
		KelasPerkhidmatan kelasPerkhidmatan = db.find(KelasPerkhidmatan.class,
				getParam("idKelasPerkhidmatanPasangan"));
		GredPerkhidmatan gredJawatan = db.find(GredPerkhidmatan.class,
				getParam("idGredJawatanPasangan"));
		Jawatan jawatan = db.find(Jawatan.class, getParam("idJawatanPasangan"));
		Agensi agensi = db.find(Agensi.class, getParam("idJabatanPasangan"));
		BadanBerkanun badanBerkanun = db.find(BadanBerkanun.class,
				getParam("idBadanBerkanunPasangan"));
		Bandar bandar = db.find(Bandar.class, getParam("idBandarPasangan"));
		KuaPermohonan kp = db.find(KuaPermohonan.class,
				getParam("idPermohonan"));

		UsersSpouse us = null;

		UsersSpouse usersSpouse = (UsersSpouse) db
				.get("SELECT us FROM UsersSpouse us WHERE us.users.id = '"
						+ userId + "'");
		if (usersSpouse != null)
			us = db.find(UsersSpouse.class, usersSpouse.getId());

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
		if (!"".equals(getParam("pasanganGaji")))
			us.setGajiPasangan(Double.parseDouble(getParam("pasanganGaji")
					.replaceAll("RM", "").replaceAll(",", "")));
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
		if (newRecord == true)
			mp.persist(us);
		try {
			kp.setPasangan(1);

			kuaUtil.kuartersLog(jenisAktiviti, "UsersSpouse", db.find(
					Users.class, userId), us.getId());

			// db.commit(request, "PROCESSING FILE (Pasangan Penghuni) : "
			// + us.getId(), jenisAktiviti);
			mp.commit();

			success = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		context.put("success", success);
		context.put("resultPermohonanCompletion",
				getCompletionPermohonan(getParam("idPermohonan")));

		return getPath() + "/sub_page/result/simpanPasangan.vm";
	}

	/*--------------------------------------------------------------- DETAIL PINJAMAN ---------------------------------------------------------------*/
	@Command("getPinjaman")
	public String getPinjaman() throws Exception {
		context.put("selectNegeriPinjaman", dataUtil.getListNegeri());

		context.put("activity", "pinjaman");

		context.remove("kpp");
		KuaPinjamanPemohon kpp = (KuaPinjamanPemohon) db
				.get("SELECT kpp FROM KuaPinjamanPemohon kpp WHERE kpp.users.id = '"
						+ userId + "'");

		if (kpp != null)
			context.put("kpp", db.find(KuaPinjamanPemohon.class, kpp.getId()));

		context.put("users", db.find(Users.class, userId));
		context.put("resultPermohonanCompletion",
				getCompletionPermohonan(getParam("idPermohonan")));

		return getPath() + "/sub_page/pinjaman.vm";
	}

	@Command("selectBandarPinjaman")
	public String selectBandarPinjaman() throws Exception {
		dataUtil = DataUtil.getInstance(db);
		String idNegeri = "0";

		if (get("idNegeriPinjaman").trim().length() > 0)
			idNegeri = get("idNegeriPinjaman");

		context.put("selectBandarPinjaman", dataUtil.getListBandar(idNegeri));

		return getPath() + "/sub_page/pinjaman/selectBandarPinjaman.vm";
	}

	@Command("simpanPinjaman")
	public String simpanPinjaman() {
		boolean success = false;
		boolean newRecord = false;
		int jenisAktiviti = 2;

		Users users = db.find(Users.class, userId);
		Bandar bandar = db.find(Bandar.class, getParam("idBandarPinjaman"));
		KuaPermohonan kp = db.find(KuaPermohonan.class,
				getParam("idPermohonan"));

		KuaPinjamanPemohon kpp = null;

		KuaPinjamanPemohon pinjamanPemohon = (KuaPinjamanPemohon) db
				.get("SELECT p FROM KuaPinjamanPemohon p WHERE p.users.id = '"
						+ userId + "'");

		if (pinjamanPemohon != null)
			kpp = db.find(KuaPinjamanPemohon.class, pinjamanPemohon.getId());

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

		try {
			t = new SimpleDateFormat("dd-MM-yyyy").parse(tarikhJangkaSiapBaru);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		kpp.setUsers(users);
		kpp.setPinjamanPerumahan(getParamAsInteger("valuePinjamanPerumahan"));
		kpp.setStatusPembinaan(getParamAsInteger("valueStatusRumah"));
		kpp.setTarikhJangkaSiap(t);
		kpp.setPoskodPinjaman(getParam("poskodPinjaman"));
		kpp.setBandar(bandar);
		kpp.setJenisPerumahan(getParam("jenisPerumahan"));
		kpp.setPembiayaan(getParam("valuePembiayaan"));
		kpp.setPembelian(getParam("valuePembelian"));

		mp.begin();
		if (newRecord == true)
			mp.persist(kpp);
		try {
			if (getParamAsInteger("valuePinjamanPerumahan") == 0)
				kp.setKelulusan3("true");
			kp.setPinjaman(1);

			kuaUtil.kuartersLog(jenisAktiviti, "KuaPinjamanPemohon", db.find(
					Users.class, userId), kpp.getId());

			// db.commit(request, "PROCESSING FILE (Pinjaman Penghuni) : "
			// + kpp.getId(), jenisAktiviti);
			mp.commit();

			success = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		context.put("success", success);
		context.put("resultPermohonanCompletion",
				getCompletionPermohonan(getParam("idPermohonan")));

		return getPath() + "/sub_page/result/simpanPinjaman.vm";
	}

	// @Command("checkTarikhJangkaSiap")
	// public String checkTarikhJangkaSiap() {
	// Boolean result = false;
	//
	// int days = Util.daysBetween(new Date(), getDate("tarikhJangkaSiap"));
	//
	// if ( days > 365 ) result = true;
	//
	// context.put("result", result);
	//
	// return getPath() + "/sub_page/result/resultTarikhJangkaSiap.vm";
	// }

	/*--------------------------------------------------------------- DETAIL KELAINAN UPAYA ---------------------------------------------------------------*/
	@Command("getKelainanUpaya")
	public String getKelainanUpaya() {
		context.put("activity", "kelainanUpaya");

		context.put("users", db.find(Users.class, userId));
		context.put("resultPermohonanCompletion",
				getCompletionPermohonan(getParam("idPermohonan")));

		return getPath() + "/sub_page/kelainanUpaya.vm";
	}

	@SuppressWarnings("unchecked")
	@Command("getRecordKelainanUpaya")
	public String getRecordKelainanUpaya() {
		List<KuaKelainanUpaya> kku = db
				.list("SELECT ku FROM KuaKelainanUpaya ku WHERE ku.permohonan.id = '"
						+ getParam("idPermohonan") + "'");

		context.put("kku", kku);

		return getPath() + "/sub_page/kelainanUpaya/record.vm";
	}

	@Command("tambahKelainanUpaya")
	public String tambahKelainanUpaya() {

		return getPath() + "/sub_page/kelainanUpaya/start.vm";
	}

	@Command("deleteKelainanUpaya")
	public String deleteKelainanUpaya() {
		boolean success = false;

		KuaKelainanUpaya kku = db.find(KuaKelainanUpaya.class,
				getParam("idKelainanUpaya"));

		mp.begin();
		if (kku != null) {
			mp.remove(kku);
		}
		try {
			kuaUtil.kuartersLog(3, "KuaKelainanUpaya", db.find(Users.class,
					userId), kku.getId());

			// db.commit(request, "PROCESSING FILE (Kelainan Upaya Penghuni) : "
			// + kku.getId(), 3);
			mp.commit();
			success = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		context.put("success", success);

		return getPath() + "/sub_page/kelainanUpaya/resultDelete.vm";
	}

	@Command("simpanKelainanUpaya")
	public String simpanKelainanUpaya() {
		boolean success = false;
		KuaPermohonan permohonan = db.find(KuaPermohonan.class,
				getParam("idPermohonan"));
		KuaKelainanUpaya kku = new KuaKelainanUpaya();

		kku.setNoKad(getParam("kelainanUpayaNoKad"));
		kku.setNoMyKad(getParam("kelainanUpayaNoKP"));
		kku.setHubungan(getParam("idHubunganKelainanUpaya"));
		kku.setImgName(getParam("imgNameKelainanUpaya"));
		kku.setAvatarName(getParam("avatarNameKelainanUpaya"));
		kku.setPermohonan(permohonan);

		mp.begin();
		mp.persist(kku);
		try {
			kuaUtil.kuartersLog(1, "KuaKelainanUpaya", db.find(Users.class,
					userId), kku.getId());

			// db.commit(request, "PROCESSING FILE (Kelainan Upaya Penghuni) : "
			// + kku.getId(), 1);
			mp.commit();

			success = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

		int days = Util.daysBetween(new Date(), getDate("tarikhJangkaSiap"));

		if (days > 365)
			result = true;

		List l = new ArrayList();

		KuaPermohonan kp = db.find(KuaPermohonan.class,
				getParam("idPermohonan"));

		String dist = getParam("poskodPinjaman");
		String lokasiAsal = kp.getLokasi().getLokasi();

		if ("".equals(lokasiAsal) || lokasiAsal == null) {
			lokasiAsal = getParam("negeriLokasi");
		}

		Bandar bandar = db.find(Bandar.class, getParam("idBandarPinjaman"));
		if (bandar != null)
			dist = dist + "|" + bandar.getKeterangan().trim();

		Negeri negeri = db.find(Negeri.class, getParam("idNegeriPinjaman"));
		if (negeri != null)
			dist = dist + "|" + negeri.getKeterangan().trim();

		try {
			l = KuartersUtils.getListRangeMap(lokasiAsal, dist);
		} catch (Exception ex) {
			myLogger.debug("ERROR (getListRangeMap) :::: " + ex.getMessage());
		}

		if (l != null) {
			Double minRange = Double.parseDouble(Collections.min(l).toString());
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
		try {
			mp.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		context.put("result", result);
		context.put("result2", result2);

		return getPath() + "/sub_page/result/resultSemakKelayakkan.vm";
	}

	/*--------------------------------------------------------------- HANTAR PERMOHONAN ---------------------------------------------------------------*/
	@Command("hantarPermohonan")
	public String hantarPermohonan() {
		boolean success = false;
		String idPermohonan = getParam("idPermohonan");
		boolean samaKelasLayak = true;
		boolean samaKelasDowngrade = true;

		mp = new MyPersistence();
		KuaPermohonan kp = (KuaPermohonan) mp.find(KuaPermohonan.class,
				idPermohonan);
		UsersJob uj = (UsersJob) db
				.get("SELECT uj FROM UsersJob uj WHERE uj.users.id = '"
						+ kp.getPemohon().getId() + "'");

		@SuppressWarnings("unchecked")
		List<KuaAgihan> agihan = db
				.list("SELECT a FROM KuaAgihan a WHERE a.permohonan.id = '"
						+ idPermohonan
						+ "' AND a.kuarters.id IS NULL ORDER BY a.kelasKuarters ASC");

		if (agihan.size() > 0) {
			if (!uj.getGredJawatan().getKelasKuarters().equals(
					agihan.get(0).getKelasKuarters())) {
				samaKelasLayak = false;
			}
			if (agihan.size() > 1) {
				if (!dataUtil.getKelasDowngrade(
						uj.getGredJawatan().getKelasKuarters()).equals(
						agihan.get(1).getKelasKuarters())) {
					samaKelasDowngrade = false;
				}
			}
		}

		// kp.setStatus(db.find(Status.class, "1423101446114"));
		// kp.setTarikhPermohonan(new Date());
		kp.setFlagMohonDowngrade("Y");
		kp.setDateMohonDowngrade(new Date());
		kp.setFlagDowngrade(getParam("valueFlagDowngrade"));
		kp.setPerakuan(getParamAsInteger("valuePerakuan"));

		mp.begin();
		if (samaKelasLayak == false) {
			if (samaKelasDowngrade == false) {
				for (int i = 0; i < agihan.size(); i++) {
					mp.remove((KuaAgihan) mp.find(KuaAgihan.class, agihan.get(i).getId()));
				}
			} else {
				mp.remove((KuaAgihan) mp.find(KuaAgihan.class, agihan.get(0).getId()));
			}
		}
		
		try {
			mp.commit();
			success = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("success", success);
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
		mp = new MyPersistence();
		KuaPermohonan kp = (KuaPermohonan) mp.find(KuaPermohonan.class,
				idPermohonan);
		UsersJob uj = (UsersJob) db
				.get("SELECT uj FROM UsersJob uj WHERE uj.users.id = '"
						+ kp.getPemohon().getId() + "'");

		List<KuaAgihan> agihan = db
				.list("SELECT a FROM KuaAgihan a WHERE a.permohonan.id = '"
						+ idPermohonan
						+ "' AND a.kuarters.id IS NULL ORDER BY a.kelasKuarters ASC");

		if (agihan.size() > 0) {
			if (!uj.getGredJawatan().getKelasKuarters().equals(
					agihan.get(0).getKelasKuarters())) {
				samaKelasLayak = false;
			}
			if (agihan.size() > 1) {
				if (!dataUtil.getKelasDowngrade(
						uj.getGredJawatan().getKelasKuarters()).equals(
						agihan.get(1).getKelasKuarters())) {
					samaKelasDowngrade = false;
				}
			}
		}

//		if (samaKelasLayak == false)
//			kp.setStatus(db.find(Status.class, "1438090674821"));
//		kp.setStatus(db.find(Status.class, "1423101446114"));
//		kp.setFlagMohonDowngrade("Y");
//		kp.setDateMohonDowngrade(new Date());
		kp.setTarikhKemaskini(new Date());
		kp.setFlagDowngrade(getParam("valueFlagDowngrade"));
		kp.setPerakuan(getParamAsInteger("valuePerakuan"));

		mp.begin();

		if (samaKelasLayak == false) {
			if (samaKelasDowngrade == false) {
				for (int i = 0; i < agihan.size(); i++) {
					mp.remove(db.find(KuaAgihan.class, agihan.get(i).getId()));
				}
			} else {
				mp.remove(db.find(KuaAgihan.class, agihan.get(0).getId()));
			}
		}

		try {
			mp.commit();
			success = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}

		context.put("success", success);

		return getPath() + "/result/kemaskiniPermohonan.vm";
	}

	/*--------------------------------------------------------------- STATUS COMPLETION PERMOHONAN ---------------------------------------------------------------*/
	public boolean getCompletionPermohonan(String idPermohonan) {
		boolean result = false;

		KuaPermohonan kp = db.find(KuaPermohonan.class, idPermohonan);

		if (kp != null) {
			if ("true".equals(kp.getKelulusan1())) {
				if ("true".equals(kp.getKelulusan2())) {
					if ("true".equals(kp.getKelulusan3())) {
						if (kp.getPekerjaan() == 1) {
							if (kp.getPeribadi() == 1) {
								if (kp.getPinjaman() == 1) {
									result = true;
									if (kp.getPemohon().getStatusPerkahwinan() != null) {
										if ("02"
												.equals(kp.getPemohon()
														.getStatusPerkahwinan()
														.getId())) {
											if (kp.getPasangan() == 1) {
												result = true;
											} else {
												result = false;
											}
										}
									}
								} else {
									result = false;
								}
							} else {
								result = false;
							}
						} else {
							result = false;
						}
					} else {
						result = false;
					}
				} else {
					result = false;
				}
			} else {
				result = false;
			}
		} else {
			result = false;
		}

		return result;
	}

	public String getMonth(String month) {
		String result = "";

		if ("Jan".equals(month)) {
			result = "01";
		} else if ("Feb".equals(month)) {
			result = "02";
		} else if ("Mar".equals(month) || "Mac".equals(month)) {
			result = "03";
		} else if ("Apr".equals(month)) {
			result = "04";
		} else if ("May".equals(month) || "Mei".equals(month)) {
			result = "05";
		} else if ("Jun".equals(month)) {
			result = "06";
		} else if ("Jul".equals(month)) {
			result = "07";
		} else if ("Aug".equals(month) || "Ogo".equals(month)) {
			result = "08";
		} else if ("Sep".equals(month)) {
			result = "09";
		} else if ("Oct".equals(month) || "Okt".equals(month)) {
			result = "10";
		} else if ("Nov".equals(month)) {
			result = "11";
		} else if ("Dec".equals(month) || "Dis".equals(month)) {
			result = "12";
		}

		return result;
	}
}
