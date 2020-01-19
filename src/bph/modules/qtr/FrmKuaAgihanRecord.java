package bph.modules.qtr;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lebah.db.Db;
import lebah.db.DbException;
import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;

import org.apache.log4j.Logger;

import portal.module.entity.Users;
import bph.entities.kod.KelasKuarters;
import bph.entities.kod.SebabPenolakan;
import bph.entities.kod.Status;
import bph.entities.qtr.KuaAgihan;
import bph.entities.qtr.KuaCatatanPermohonan;
import bph.entities.qtr.KuaKuarters;
import bph.entities.qtr.KuaPermohonan;
import bph.entities.qtr.KuaTawaran;
import bph.utils.DataUtil;
import db.persistence.MyPersistence;

public class FrmKuaAgihanRecord extends LebahRecordTemplateModule<KuaAgihan> {

	private static final long serialVersionUID = -4694709486982124681L;
	static Logger myLogger = Logger.getLogger("bph/modules/qtr/FrmKuaAgihanRecord");
	private DataUtil dataUtil;
	private MyPersistence mp;

	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		return String.class;
	}

	@Override
	public Class<KuaAgihan> getPersistenceClass() {
		return KuaAgihan.class;
	}

	@Override
	public String getPath() {
		return "bph/modules/qtr/agihan";
	}

	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);
		setDisableKosongkanUpperButton(true);
		setDisableSaveAddNewButton(true);
		setDisableDefaultButton(true);
		setDisableUpperBackButton(true);
		setDisableAddNewRecordButton(true);
		setDisableBackButton(true);
		//setHideDeleteButton(true);
		userId = (String) request.getSession().getAttribute("_portal_login");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		try {
			mp = new MyPersistence();
			context.put("currentRoleQTR", userRole);
			addFilter("status.id = '1419601227590'");// status menunggu
			addFilter("noGiliran <> 0");
			/*if(userRole.equalsIgnoreCase("(QTR) Penyedia")){
				addFilter("flagSemakanPelulus = '0'");
			}
			if(userRole.equalsIgnoreCase("(QTR) Pelulus")){
				addFilter("flagSemakanPelulus = '1'");
			}*/
			setOrderBy("noGiliran ASC");
			context.put("bersyarat", "tidak");
			context.put("personal", "tidak");
			context.put("findKelasKuarters", dataUtil.getListKelasPeranginan());
			context.put("findLokasiKuarters", dataUtil.getListLokasiPermohonan());
			context.put("path", getPath());
		} catch (Exception e) {
			System.out.println("Error begin : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
	}

	@Override
	public void save(KuaAgihan r) throws Exception {
	}

	@Override
	public boolean delete(KuaAgihan r) throws Exception {
		try {
			mp = new MyPersistence();
			KuaKuarters kk = null;
			if (r.getKuarters() != null){
				kk = (KuaKuarters) mp.find(KuaKuarters.class, r.getKuarters().getId());
			}
			KuaPermohonan kp = (KuaPermohonan) mp.find(KuaPermohonan.class, r.getPermohonan().getId());
			kp.setStatus((Status) mp.find(Status.class, "1419483289678"));// tukar jadi permohonan baru
			kp.setTarikhKemaskini(null);
			if (kk != null){
				kk.setFlagAgihan(0);
			}	
		} catch (Exception e) {
			System.out.println("Error delete : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		return true;
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		HashMap<String, Object> r = new HashMap<String, Object>();
		r.put("permohonan.noPermohonan", getParam("findNoPermohonan"));
		r.put("pemohon.userName", getParam("findNamaPemohon"));
		r.put("pemohon.noKP", getParam("findNoKPPemohon"));
		r.put("kelasKuarters", getParam("findKelasKuarters"));
		r.put("idLokasi.id", getParam("findLokasiKuarters"));
		r.put("permohonan.statusDalaman", getParam("findJenisPermohonan"));
		return r;
	}

	@Override
	public void beforeSave() {
	}

	@Override
	public void afterSave(KuaAgihan r) {
	}

	@SuppressWarnings("unchecked")
	@Override
	public void getRelatedData(KuaAgihan r) {
		try {
			mp = new MyPersistence();
			KuaAgihan rr = (KuaAgihan) mp.find(KuaAgihan.class, r.getId());
			context.put("r", rr);
			String kelas1 = "";
			KelasKuarters k1 = (KelasKuarters) mp.find(KelasKuarters.class, r.getKelasKuarters());
			//String idLokasi = r.getPermohonan().getLokasi().getId();
			String idLokasi = r.getIdLokasi().getId();
			if (k1 != null) {
				kelas1 = k1.getId();
			} else {
				kelas1 = r.getKelasKuarters();
			}
			if(idLokasi.equalsIgnoreCase("02"))
			{
				context.put("listKuarters", dataUtil.getListKuartersKLTawaran(idLokasi));
			}
			else
			{
				List<KuaKuarters> listKuarters = dataUtil.getListKuartersTawaran(idLokasi, kelas1.replaceAll("1", ""));
				context.put("listKuarters", listKuarters);
			}
			context.put("kelas1", kelas1);
			if ("".equals(r.getPermohonan().getFlagDowngrade())){
				context.put("kelas2", dataUtil.getKelasDowngrade(r.getKelasKuarters()));
			}	
			if (r.getKuarters() != null){
				context.put("kuarters", mp.find(KuaKuarters.class, r.getKuarters().getId()));
			}
			List<Users> users = mp.list("SELECT u FROM Users u WHERE u.id IN (" + getPT()+ ") OR u.role.description = '(QTR) Penyedia' ORDER BY u.userName ASC");
			context.put("selectPetugas", users);
			List<SebabPenolakan> sp = mp.list("SELECT u FROM SebabPenolakan u");
			context.put("selectPenolakan", sp);
		} catch (Exception e) {
			System.out.println("Error getRelatedData : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
	}

	@Command("agihKuarters")//pelulus buat penawaran
	public String agihKuarters() {
		boolean success = false;
		try {
			mp = new MyPersistence();
			KuaAgihan ka = (KuaAgihan) mp.find(KuaAgihan.class, getParam("idAgihan"));
			KuaAgihan kaLain = (KuaAgihan) mp.get("SELECT kp FROM KuaAgihan kp WHERE kp.pemohon.id = '"+ ka.getPemohon().getId() + "' and kp.permohonan.id !='" + ka.getPermohonan().getId() + "'");
			if (kaLain != null) {
				mp.remove(kaLain);
			}
			KuaKuarters kk = (KuaKuarters) mp.find(KuaKuarters.class, getParam("valuePilihKuarters"));
			Users petugas = (Users) mp.find(Users.class, getParam("idPetugas"));
			Users pengagih = (Users) mp.find(Users.class, userId);
			KuaTawaran p = new KuaTawaran();
			if (kk != null) {
				if (ka != null) {
					ka.setKuarters(kk);
					ka.setTarikhKemaskini(new Date());
					ka.setStatus((Status) mp.find(Status.class, "1435817077476"));
					ka.setPetugas(petugas);
					ka.setPengagih(pengagih);
					ka.setDateAgih(new Date());
					ka.setFlagSemakanPelulus(2);
				}
				kk.setFlagAgihan(1);
			}
			p.setAgihan(ka);
			p.setTarikhMasuk(new Date());
			mp.begin();
			mp.persist(p);
			mp.commit();
			
			//06082018 - ADD BY PEJE - ADD CATATAN TO CATATAN PERMOHONAN
			if (getParam("catatanAgihanTugas") != "") {
				if (ka != null) {
					if (ka.getPermohonan() != null) {
						mp.begin();
						KuaCatatanPermohonan catatanPermohonan = new KuaCatatanPermohonan();
						catatanPermohonan.setPermohonan(ka.getPermohonan());
						catatanPermohonan.setCatatan(getParam("catatanAgihanTugas"));
						catatanPermohonan.setDaftarOleh(pengagih);
						catatanPermohonan.setTarikhDaftar(new Date());
						mp.persist(catatanPermohonan);
						mp.commit();
					}
				}
			}
			success = true;
		} catch (Exception e) {
			System.out.println("Error agihKuarters : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("success", success);
		return getPath() + "/result/agihKuarters.vm";
	}

	@Command("hantarBersyarat")
	public String hantarBersyarat() {
		boolean success = false;
		try {
			mp = new MyPersistence();
			KuaAgihan ka = (KuaAgihan) mp.find(KuaAgihan.class, getParam("idAgihan"));
			ka.setStatus((Status) mp.find(Status.class, "1431405647231"));	// penolakan bersyarat
			ka.setSebabTolak((SebabPenolakan) mp.find(SebabPenolakan.class, getParam("idPenolakan")));
			ka.setTarikhKemaskini(new Date());
			ka.setFlagSemakanPelulus(3);//flag untuk bersyarat
			mp.begin();
			mp.commit();
			success = true;
		} catch (Exception e) {
			System.out.println("Error hantarBersyarat : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("success", success);
		return getPath() + "/result/hantarBersyarat.vm";
	}

	public String getPT() throws SQLException {
		Db db = null;
		String sql = "";
		String ptName = "";
		try {
			db = new Db();
			sql = "SELECT user_id, role_id FROM user_role WHERE role_id = '(QTR) Penyedia'";
			ResultSet rs = db.getStatement().executeQuery(sql);
			while (rs.next()) {
				if (ptName == "") {
					ptName = "'" + rs.getString("user_id") + "'";
				} else {
					ptName = ptName + ", '" + rs.getString("user_id") + "'";
				}
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
		return ptName;
	}
	
	public String getPelulus() throws SQLException {
		Db db = null;
		String sql = "";
		String ptName = "";
		try {
			db = new Db();
			sql = "SELECT user_id, role_id FROM user_role WHERE role_id = '(QTR) Pelulus'";
			ResultSet rs = db.getStatement().executeQuery(sql);
			while (rs.next()) {
				if (ptName == "") {
					ptName = "'" + rs.getString("user_id") + "'";
				} else {
					ptName = ptName + ", '" + rs.getString("user_id") + "'";
				}
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
		return ptName;
	}
	
	@Command("simpanCatatan")
	public String simpanCatatan() {
		boolean success = false;
		String idAgihan = getParam("idAgihan");
		KuaAgihan agih = null;
		try {
			mp = new MyPersistence();
			agih = (KuaAgihan) mp.find(KuaAgihan.class, idAgihan);
			agih.setCatatan(getParam("catatan"));
			mp.begin();
			mp.persist(agih);
			mp.commit();
			success = true;
		} catch (Exception e) {
			System.out.println("Error simpanCatatan : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("success", success);
		return getPath() + "/result/simpanCatatan.vm";
	}
	
	@Command("pengesahanHantarBersyarat")//dapatkan pengesahan untuk pelulus hantar ke senarai bersyarat
	public String pengesahanHantarBersyarat() {
		boolean success = false;
		try {
			mp = new MyPersistence();
			KuaAgihan ka = (KuaAgihan) mp.find(KuaAgihan.class, getParam("idAgihan"));
			ka.setFlagSemakanPelulus(1);
			mp.begin();
			mp.commit();
			success = true;
		} catch (Exception e) {
			System.out.println("Error hantarBersyarat : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("success", success);
		return getPath() + "/result/hantarBersyarat.vm";
	}
	
	@Command("pengesahanAgihKuarters")//hantar untuk dapatkan pengesahan pelulus
	public String pengesahanAgihKuarters() {
		boolean success = false;
		try {
			mp = new MyPersistence();
			KuaAgihan ka = (KuaAgihan) mp.find(KuaAgihan.class, getParam("idAgihan"));
			ka.setFlagSemakanPelulus(1);
			mp.begin();
			mp.commit();
			success = true;
		} catch (Exception e) {
			System.out.println("Error semakan agihan : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("success", success);
		return getPath() + "/result/semakAgihKuarters.vm";
	}
}
