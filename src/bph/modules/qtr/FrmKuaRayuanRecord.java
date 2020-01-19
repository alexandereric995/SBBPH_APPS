package bph.modules.qtr;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;

import org.apache.log4j.Logger;

import portal.module.entity.Users;
import portal.module.entity.UsersJob;
import bph.entities.kod.Agensi;
import bph.entities.kod.GredPerkhidmatan;
import bph.entities.kod.Jawatan;
import bph.entities.kod.JenisRayuan;
import bph.entities.kod.KelasPerkhidmatan;
import bph.entities.qtr.KuaPenghuni;
import bph.entities.qtr.KuaPermohonan;
import bph.entities.qtr.KuaRayuan;
import bph.utils.DataUtil;
import db.persistence.MyPersistence;

public class FrmKuaRayuanRecord extends
		LebahRecordTemplateModule<KuaPermohonan> {

	private static final long serialVersionUID = -3025772107828718257L;
	static Logger myLogger = Logger
			.getLogger("bph/modules/qtr/FrmKuaRayuanRecord");
	private DataUtil dataUtil;
	private MyPersistence mp;

	@SuppressWarnings("unchecked")
	public Class getIdType() {
		return String.class;
	}

	public Class<KuaPermohonan> getPersistenceClass() {
		return KuaPermohonan.class;
	}

	@Override
	public String getPath() {
		return "bph/modules/qtr/rayuan";
	}

	@Override
	public void begin() {
		context.put("pemohon", "");
		context.put("permohonan", "");
		context.put("r", "");
		context.put("pekerjaan", "");
		context.put("noPendaftaran", "");
		dataUtil = DataUtil.getInstance(db);
		//setReadonly(true);
		this.setDisableUpperBackButton(true);
		this.setDisableKosongkanUpperButton(true);
		this.setDisableSaveAddNewButton(true);	
		this.setDisableDefaultButton(true);
		this.setHideDeleteButton(true);
		context.put("selectGredJawatan", dataUtil.getListGredPerkhidmatan());
		context.put("selectKementerian", dataUtil.getListKementerian());
		context.put("selectKelasPerkhidmatan", dataUtil.getListKelasPerkhidmatan());
		context.put("selectJawatan", dataUtil.getListJawatan());
		context.put("selectKelasKuarters", dataUtil.getListKelasPeranginan());
		context.put("path", getPath());
		this.addFilter("flagRayuan = 'Y'");
	}

	@Override
	public void save(KuaPermohonan r) throws Exception {

	}

	@Override
	public boolean delete(KuaPermohonan r) throws Exception {
		return false;
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		HashMap<String, Object> r = new HashMap<String, Object>();
		r.put("id", getParam("findNoDaftar"));
		r.put("pemohon.noKP", getParam("findUserNoKP"));
		r.put("pemohon.userName", getParam("findUserName"));
		return r;
	}

	@Override
	public void beforeSave() {

	}

	@Override
	public void afterSave(KuaPermohonan r) {

	}

	@Override
	public void getRelatedData(KuaPermohonan r) {
		try {
			mp = new MyPersistence();
			Users pemohon = (Users) mp.get("SELECT u FROM Users u WHERE u.id = '"
					+ r.getPemohon().getId() + "'");
			KuaPermohonan permohonan = (KuaPermohonan) mp.find(
					KuaPermohonan.class, r.getId());
			UsersJob usersJob = (UsersJob) mp.get("SELECT uj FROM UsersJob uj WHERE uj.users.id = '"
							+ r.getPemohon().getId() + "'");
			KuaPenghuni penghuni = (KuaPenghuni) mp.get("SELECT p FROM KuaPenghuni p WHERE p.pemohon.id = '"
							+ r.getPemohon().getId() + "'");
			context.put("penghuni", penghuni);
			context.put("pemohon", pemohon);
			context.put("uj", usersJob);
			context.put("pekerjaan", usersJob);
			context.put("permohonan", permohonan);
		} catch (Exception e) {
			System.out.println("Error getRelatedData : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
	}

	@SuppressWarnings("unchecked")
	@Command("listRayuan")
	public String listRayuan() {
		try {
			mp = new MyPersistence();
			KuaPermohonan permohonan = (KuaPermohonan) mp.find(
					KuaPermohonan.class, getParam("idPermohonan"));
			List<KuaRayuan> rayuan = mp
					.list("SELECT r FROM KuaRayuan r WHERE r.pemohon.id = '"
							+ permohonan.getPemohon().getId() + "'");
			context.put("rayuan", rayuan);
		} catch (Exception e) {
			System.out.println("Error listRayuan : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/sub_page/listRayuan.vm";
	}

	@Command("daftarRayuan")
	public String daftarRayuan() {
		context.put("selectJenisRayuan", dataUtil.getListJenisRayuan());
		return getPath() + "/sub_page/detailRayuan.vm";
	}

	@Command("paparRayuan")
	public String paparRayuan() {
		try {
			mp = new MyPersistence();
			KuaRayuan rayuan = (KuaRayuan) mp.find(KuaRayuan.class,
					getParam("idRayuan"));
			context.put("rayuan", rayuan);
		} catch (Exception e) {
			System.out.println("Error paparRayuan : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/sub_page/detailRayuan.vm";
	}

	@Command("simpanRayuan")
	public String simpanRayuan() {
		boolean newRecord = false;
		boolean success = false;
		try {
			mp = new MyPersistence();
			KuaPermohonan kp = (KuaPermohonan) mp.find(KuaPermohonan.class,getParam("idPermohonan"));
			KuaRayuan kr = (KuaRayuan) mp.find(KuaRayuan.class,getParam("idRayuan"));
			if (kr == null) {
				kr = new KuaRayuan();
				newRecord = true;
			}
			JenisRayuan jenisRayuan = (JenisRayuan) mp.find(JenisRayuan.class,getParam("idJenisRayuan"));
			Users pemohon = (Users) mp.find(Users.class, kp.getPemohon().getId());
			kr.setJenisRayuan(jenisRayuan);
			kr.setLainJenisRayuan(getParam("lainJenisRayuan"));
			kr.setPemohon(pemohon);
			kr.setSebabRayuan(getParam("sebabRayuan"));
			kr.setTarikhRayuanDibuat(getDate("tarikhRayuanDibuat"));
			kr.setTarikhMaklumbalas(getDate("tarikhMaklumbalas"));
			kr.setTarikhRayuan(getDate("tarikhRayuan"));
			if (newRecord == false)
			kr.setTarikhKemaskini(new Date());
			kr.setStatusRayuan(getParam("statusRayuan"));
			kr.setCatatan(getParam("catatan"));
			mp.begin();
			if (newRecord == true){
				mp.persist(kr);
			}
			kp.setFlagRayuan("Y");
			mp.commit();
			success = true;
			context.put("success", success);
		} catch (Exception e) {
			System.out.println("Error simpanRayuan : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/result/simpanRayuan.vm";
	}

	@Command("selectJabatan")
	public String selectJabatan() throws Exception {
		String idKementerian = "0";
		if (get("idKementerian").trim().length() > 0)
			idKementerian = get("idKementerian");
		context.put("selectJabatan", dataUtil.getListAgensi(idKementerian));
		return getPath() + "/sub_page/select/selectJabatan.vm";
	}

	@Command("kemaskiniDataPemohon")
	public String kemaskiniDataPemohon() {
		boolean success = false;
		try {
			mp = new MyPersistence();
			KuaPermohonan kp = (KuaPermohonan) mp.find(KuaPermohonan.class,
					getParam("idPermohonan"));
			Users users = (Users) mp.get("SELECT u FROM Users u WHERE u.id = '"
					+ kp.getPemohon().getId() + "'");
			Users u = (Users) mp.find(Users.class, users.getId());
			UsersJob usersJob = (UsersJob) mp
					.get("SELECT uj FROM UsersJob uj WHERE uj.users.id = '"
							+ kp.getPemohon().getId() + "'");
			UsersJob uj = (UsersJob) mp.find(UsersJob.class, usersJob.getId());
			KelasPerkhidmatan kelasPerkhidmatan = (KelasPerkhidmatan) mp.find(
					KelasPerkhidmatan.class, getParam("idKelasPerkhidmatan"));
			GredPerkhidmatan gredJawatan = (GredPerkhidmatan) mp.find(
					GredPerkhidmatan.class, getParam("idGredJawatan"));
			Jawatan jawatan = (Jawatan) mp.find(Jawatan.class,
					getParam("idJawatan"));
			Agensi jabatan = (Agensi) mp.find(Agensi.class,
					getParam("idJabatan"));
			u.setEmel(getParam("userEmel"));
			uj.setKelasPerkhidmatan(kelasPerkhidmatan);
			uj.setGredJawatan(gredJawatan);
			uj.setJawatan(jawatan);
			uj.setAgensi(jabatan);

			mp.begin();
			mp.commit();
			success = true;
		} catch (Exception e) {
			System.out
					.println("Error kemaskiniDataPemohon : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("success", success);
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getMaklumatPemohonBerdaftar")
	public String getMaklumatPemohonBerdaftar() throws Exception {
		String noPendaftaran = get("noPendaftaran").trim();
		try {
			mp = new MyPersistence();
			Users pemohon = (Users) mp.find(Users.class, noPendaftaran);
			KuaPermohonan permohonan = (KuaPermohonan) mp.get("SELECT u FROM KuaPermohonan u WHERE u.pemohon.id = '"+ pemohon.getId() + "'");
			UsersJob pekerjaan = (UsersJob) mp.get("SELECT u FROM UsersJob u WHERE u.users.id = '"+ pemohon.getId() + "'");
			context.put("pemohon", pemohon);
			context.put("permohonan", permohonan);
			context.put("r", permohonan);
			context.put("pekerjaan", pekerjaan);
			context.put("noPendaftaran", noPendaftaran);
		} catch (Exception e) {
			System.out.println("Error getMaklumatPemohonBerdaftar : "
					+ e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/entry_page.vm";
	}
}
