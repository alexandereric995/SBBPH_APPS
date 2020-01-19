package bph.modules.qtr;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import lebah.portal.action.Command;

import org.apache.log4j.Logger;

import portal.module.entity.Users;
import portal.module.entity.UsersJob;
import bph.entities.kod.Bandar;
import bph.entities.kod.LokasiPermohonan;
import bph.entities.kod.Status;
import bph.entities.qtr.KuaPenghuni;
import bph.entities.qtr.KuaPermohonan;
import bph.entities.qtr.KuaPinjamanPemohon;
import bph.utils.DataUtil;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class FrmKuaPermohonanSubRecordTukarLokasi extends
		FrmKuaPermohonanSubRecord {

	private static final long serialVersionUID = -302245884484195171L;
	static Logger myLogger = Logger
			.getLogger("bph/modules/qtr/FrmKuaPermohonanSubRecord");
	private DataUtil dataUtil;
	// private KuartersUtils kuaUtil = new KuartersUtils();
	private MyPersistence mp;

	@SuppressWarnings("unchecked")
	public void begin() {
		try {
			mp = new MyPersistence();
			userId = (String) request.getSession()
					.getAttribute("_portal_login");
			KuaPenghuni kp = (KuaPenghuni) mp
					.get("SELECT p FROM KuaPenghuni p WHERE p.pemohon.id = '"
							+ userId + "' and p.tarikhKeluar is null");
			dataUtil = DataUtil.getInstance(db);
			setRecordOnly(true);
			setHideDeleteButton(true);
			setDisableDefaultButton(true);
			setDisableBackButton(true);
			setDisableKosongkanUpperButton(true);
			setDisableUpperBackButton(true);
			setDisableSaveAddNewButton(true);
			setDisableAddNewRecordButton(false);
			userRole = (String) request.getSession().getAttribute(
					"_portal_role");
			addFilter("pemohon.id = '" + userId + "'");
			context.remove("currentRoleQTR");
			context.put("Awam", true);
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
			List<KuaPermohonan> listPermohonan = mp
					.list("SELECT kp FROM KuaPermohonan kp WHERE kp.pemohon.id = '"
							+ userId + "'");
			context.put("listPermohonan", listPermohonan);
			context.put("selectNegeriPekerjaan", dataUtil.getListNegeri());
			context.put("selectNegeriPinjaman", dataUtil.getListNegeri());
			context.put("selectLokasiPermohonan", dataUtil
					.getListLokasiPermohonan());
			context.put("newRecord", "true");
			context.put("kp", kp);

		} catch (Exception e) {
			System.out.println("Error begin : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
	}

	/*--------------------------------------------------------------- HANTAR PERMOHONAN ---------------------------------------------------------------*/
	@Command("hantarPermohonan")
	public String hantarPermohonan() {
		boolean success = false;
		Boolean kelulusan = false;
		boolean newRecord = false;
		boolean newRecordKerja = false;
		boolean newRecordPinjaman = false;
		boolean result = false;// tarikh jangka siap
		boolean result2 = false;// radius rumah dimiliki
		// int jenisAktiviti = 2;
		// int jenisAktivitiKerja = 2;
		// int jenisAktivitiPinjaman = 2;
		String idPermohonan = getParam("idPermohonan");
		mp = new MyPersistence();

		LokasiPermohonan lokasiPermohonan = (LokasiPermohonan) mp.find(
				LokasiPermohonan.class, getParam("idLokasiPermohonan"));
		Bandar bandarLokasi = (Bandar) mp.find(Bandar.class,
				getParam("idBandarPekerjaan"));

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
		} else if ("05".equals(lokasiPermohonan.getNegeri().getId())) // KLIA
		{
			// NILAI //SEPANG
			if (bandarLokasi.getId().equals("0517")
					|| bandarLokasi.getId().equals("1035")) {
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

		Users pemohon = (Users) mp.find(Users.class, userId);
		KuaPermohonan kp = null;
		KuaPermohonan permohonan = (KuaPermohonan) mp
				.get("SELECT p FROM KuaPermohonan p WHERE p.pemohon.id = '"
						+ userId + "' AND p.status.id = '1419483289675'");

		Status status = (Status) mp.find(Status.class, "1419483289675");

		if (!"".equals(idPermohonan)) {
			kp = (KuaPermohonan) mp.find(KuaPermohonan.class, idPermohonan);
		} else {
			if (permohonan != null) {
				kp = (KuaPermohonan) mp.find(KuaPermohonan.class, permohonan
						.getId());
			}
		}

		if (kp == null) {
			// jenisAktiviti = 1;
			newRecord = true;
			kp = new KuaPermohonan();
		} else {
			String statusTerkini=kp.getStatus().getId().toString();
			status = (Status) mp.find(Status.class, statusTerkini);
		}

		kp.setPemohon(pemohon);
		kp.setLokasi(lokasiPermohonan);
		//kp.setTarikhMasuk(new Date());
		if (newRecord == false) {
			kp.setTarikhKemaskini(new Date());
		}
		kp.setStatus(status);
		kp.setFlagMohonTukar("Y");
		kp.setDateMohonTukar(new Date());
		kp.setKelulusan1(kelulusan.toString());

		if (result == true && result2 == true) {
			kp.setKelulusan3("true");
			kp.setPinjaman(1);
		} else {
			kp.setKelulusan3("false");
			kp.setPinjaman(0);
		}

		try {
			mp.begin();
			if (newRecord == true) {
				mp.persist(kp);
			}
			mp.commit();
			idPermohonan = kp.getId();
		} catch (Exception e) {
			System.out.println("Error hantarPermohonan : " + e.getMessage());
		}

		context.put("idPermohonan", idPermohonan);

		UsersJob uj = null;
		UsersJob usersJob = (UsersJob) mp
				.get("SELECT usersJob FROM UsersJob usersJob WHERE usersJob.users.id = '"
						+ userId + "'");
		if (usersJob != null)
			uj = (UsersJob) mp.find(UsersJob.class,
					usersJob.getId() != null ? usersJob.getId() : "");

		Bandar bandarPekerjaan = (Bandar) mp.find(Bandar.class,
				getParam("idBandarPekerjaan"));

		if (uj == null) {
			// jenisAktivitiKerja = 1;
			newRecordKerja = true;
			uj = new UsersJob();
		}

		uj.setUsers(pemohon);
		uj.setPoskod(getParam("poskodPekerjaan"));
		uj.setBandar(bandarPekerjaan);
		context.put("uj", uj);

		try {
			mp.begin();
			if (newRecordKerja == true)
				mp.persist(uj);
			mp.commit();
		} catch (Exception e) {

			e.printStackTrace();
		}

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

		try {
			mp.begin();
			if (newRecordPinjaman == true)
				mp.persist(kpp);
			mp.commit();
			success = true;
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("success", success);
		return getPath() + "/result/hantarPermohonan.vm";
	}

	public String getPath() {
		return "bph/modules/qtr/tukarLokasi";
	}
}
