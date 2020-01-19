package bph.modules.qtr;

import java.util.Date;
import java.util.List;

import lebah.portal.action.Command;

import org.apache.log4j.Logger;

import portal.module.entity.Users;
import portal.module.entity.UsersJob;
import bph.entities.qtr.KuaAgihan;
import bph.entities.qtr.KuaPenghuni;
import bph.entities.qtr.KuaPermohonan;
import bph.entities.qtr.KuaPinjamanPemohon;
import bph.utils.DataUtil;
import db.persistence.MyPersistence;

public class FrmKuaPermohonanSubRecordNaikTaraf extends
		FrmKuaPermohonanSubRecord {
	private static final long serialVersionUID = -302245884484195171L;
	static Logger myLogger = Logger
			.getLogger("bph/modules/qtr/FrmKuaPermohonanSubRecord");
	private DataUtil dataUtil;
	// private KuartersUtils kuaUtil = new KuartersUtils();
	// private String uploadDir =
	// ResourceBundle.getBundle("dbconnection").getString("folder");
	private MyPersistence mp;

	@Override
	public void begin() {
		try {
			mp = new MyPersistence();
			userId = (String) request.getSession()
					.getAttribute("_portal_login");
			KuaPenghuni kp = (KuaPenghuni) mp
					.get("SELECT p FROM KuaPenghuni p WHERE p.pemohon.id = '"
							+ userId + "' and p.tarikhKeluar is null");
			dataUtil = DataUtil.getInstance(db);
			userId = (String) request.getSession()
					.getAttribute("_portal_login");
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
			setDisableAddNewRecordButton(true);
			// List<KuaPermohonan> listPermohonan =
			// mp.list("SELECT kp FROM KuaPermohonan kp WHERE kp.pemohon.id = '"+
			// userId + "'");
			// context.put("listPermohonan", listPermohonan);
			KuaPermohonan listPermohonan = (KuaPermohonan) mp
					.get("SELECT p FROM KuaPermohonan p WHERE p.pemohon.id = '"
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

	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/qtr/naikTaraf";
	}

	@Override
	public void getRelatedData(KuaPermohonan r) {
		// TODO Auto-generated method stub
		boolean kelulusan = false;
		try {
			mp = new MyPersistence();
			UsersJob uj = (UsersJob) mp
					.get("SELECT uj FROM UsersJob uj WHERE uj.users.id = '"
							+ r.getPemohon().getId() + "'");
			KuaPermohonan kp = (KuaPermohonan) mp.find(KuaPermohonan.class, r
					.getId());

			if ("true".equals(kp.getKelulusan1())
					&& "true".equals(kp.getKelulusan3())) {
				context.put("disableAddNewRecordButton", true);
				kelulusan = true;
			}
			context.put("kelulusan", kelulusan);
			context.remove("kpp");
			KuaPinjamanPemohon kpp = (KuaPinjamanPemohon) mp
					.get("SELECT kpp FROM KuaPinjamanPemohon kpp WHERE kpp.users.id = '"
							+ userId + "'");
			if (kpp != null)
				context.put("kpp", mp.find(KuaPinjamanPemohon.class, kpp
						.getId()));
			context.put("uj", uj);
			context.put("newRecord", "false");
			context.put("kemaskini", "true");
			getPekerjaan();
			getPerakuan(r);
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

			// myLogger.debug("ID PERMOHONAN ::: " + idPermohonan);

			KuaPermohonan kp = (KuaPermohonan) mp.find(KuaPermohonan.class,
					idPermohonan);

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
							mp
									.get("SELECT pp FROM KuaPinjamanPemohon pp WHERE pp.users.id = '"
											+ userId + "'"));

		} catch (Exception e) {
			System.out.println("Error getPermohonan : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}

		return getPath() + "/entry_sub/entry_permohonan.vm";
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

		context.put("selectBandarPekerjaan", dataUtil.getListBandar(idNegeri));
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

	@Command("getPerakuan")
	public String getPerakuan(KuaPermohonan r) {
		try {
			mp = new MyPersistence();

			String kelasLayak = "";
			String kelasDowngrade = "";

			UsersJob pekerjaan = (UsersJob) mp
					.get("SELECT uj FROM UsersJob uj WHERE uj.users.id = '"
							+ userId + "'");

			// context.put("kelasLayak",
			// pekerjaan.getGredJawatan().getKelasKuarters());
			// context.put("kelasDowngrade",
			// dataUtil.getKelasDowngrade(pekerjaan.getGredJawatan().getKelasKuarters()));

			if (pekerjaan != null) {
				String gredKelasKuarters = "";
				// if ( pekerjaan.getIdGredJawatan() != null ) {
				if (pekerjaan.getGredJawatan() != null) { // rozai add
					// 11/11/2015
					gredKelasKuarters = pekerjaan.getGredJawatan()
							.getKelasKuarters();
				}
				kelasLayak = gredKelasKuarters;
				kelasDowngrade = dataUtil.getKelasDowngrade(gredKelasKuarters);
			}

			String lokasi = pekerjaan.getBandar().getNegeri().getKeterangan();
			context.put("kelasLayak", kelasLayak);
			context.put("kelasDowngrade", kelasDowngrade);
			context.put("lokasi", lokasi);

			// dapatkan KL area
			String nogori = "";

			KuaPermohonan kp = (KuaPermohonan) mp.find(KuaPermohonan.class, r
					.getId());
			if (kp.getLokasi().getBandar().getNegeri().getId().toString() != null) {
				nogori = kp.getLokasi().getId().toString();
			}

			if (nogori.equalsIgnoreCase("02")) {
				context.put("areaKL", true);
			} else {
				context.put("areaKL", false);
			}
			context.put("kelasLayak", kelasLayak);
			context.put("kelasDowngrade", kelasDowngrade);
		} catch (Exception e) {
			System.out.println("Error getPerakuan : " + e.getMessage());
		}
		return getPath() + "/sub_page/pekerjaan.vm";
	}

	/*--------------------------------------------------------------- HANTAR PERMOHONAN ---------------------------------------------------------------*/
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

			@SuppressWarnings("unchecked")
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
					if (!dataUtil.getKelasDowngrade(
							uj.getGredJawatan().getKelasKuarters()).equals(
							agihan.get(1).getKelasKuarters())) {
						samaKelasDowngrade = false;
					}
				}
			}
			kp.setFlagMohonUpgrade("Y");
			kp.setDateMohonUpgrade(new Date());
			// kp.setStatus((Status) mp.find(Status.class, "1431263251958"));
			// kp.setTarikhPermohonan(new Date());
			kp.setTarikhKemaskini(new Date());
			kp.setFlagDowngrade(getParam("valueFlagDowngrade"));
			kp.setPerakuan(getParamAsInteger("valuePerakuan"));

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
					if (!dataUtil.getKelasDowngrade(
							uj.getGredJawatan().getKelasKuarters()).equals(
							agihan.get(1).getKelasKuarters())) {
						samaKelasDowngrade = false;
					}
				}
			}

//			if (samaKelasLayak == false)
//				kp.setStatus((Status) mp.find(Status.class, "1438090674821"));
//			kp.setStatus((Status) mp.find(Status.class, "1431263251958"));
			kp.setTarikhKemaskini(new Date());
			kp.setFlagDowngrade(getParam("valueFlagDowngrade"));
			kp.setPerakuan(getParamAsInteger("valuePerakuan"));

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}

		context.put("success", success);

		return getPath() + "/result/kemaskiniPermohonan.vm";
	}
}
