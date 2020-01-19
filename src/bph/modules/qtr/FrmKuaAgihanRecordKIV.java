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
import portal.module.entity.UsersJob;
import bph.entities.kod.Status;
import bph.entities.qtr.KuaAgihan;
import bph.entities.qtr.KuaKuarters;
import bph.entities.qtr.KuaPermohonan;
import bph.entities.qtr.KuaTawaran;
import bph.utils.DataUtil;
import db.persistence.MyPersistence;

public class FrmKuaAgihanRecordKIV extends
		LebahRecordTemplateModule<KuaPermohonan> {

	private static final long serialVersionUID = -4694709486982124681L;
	static Logger myLogger = Logger
			.getLogger("bph/modules/qtr/FrmKuaAgihanRecord");
	private DataUtil dataUtil;
	private MyPersistence mp;


	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		return String.class;
	}

	@Override
	public Class<KuaPermohonan> getPersistenceClass() {
		return KuaPermohonan.class;
	}

	@Override
	public String getPath() {
		return "bph/modules/qtr/agihan/kiv";
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

		userId = (String) request.getSession().getAttribute("_portal_login");
		userRole = (String) request.getSession().getAttribute("_portal_role");

		String[] role = userRole.split(" ");

		for (int i = 0; i < role.length; i++) {
			if ("Penyemak".equals(role[i]) || "Pelulus".equals(role[i])) {
				context.put("currentRoleQTR", role[i]);
			}
		}

		addFilter("status.id IN ('1435817077478', '1435817077475','1435817077472','1431405647234')");

		context.put("bersyarat", "tidak");
		context.put("personal", "tidak");
		context.put("findKelasKuarters", dataUtil.getListKelasPeranginan());
		context.put("findLokasiKuarters", dataUtil.getListLokasiPermohonan());
		context.put("path", getPath());
	}

	@Override
	public void save(KuaPermohonan r) throws Exception {

	}

	@SuppressWarnings("unused")
	@Override
	public boolean delete(KuaPermohonan r) throws Exception {
		KuaKuarters kk = null;
		if (kk != null)
			kk.setFlagAgihan(0);
		return true;
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		HashMap<String, Object> r = new HashMap<String, Object>();
		r.put("permohonan.id", getParam("findNoPermohonan"));
		r.put("pemohon.userName", getParam("findNamaPemohon"));
		r.put("pemohon.noKP", getParam("findNoKPPemohon"));
		r.put("kelasKuarters", getParam("findKelasKuarters"));
		r.put("permohonan.lokasi.id", getParam("findLokasiKuarters"));
		return r;
	}

	@Override
	public void beforeSave() {

	}

	@Command("agihKuarters")
	public String agihKuarters() {
		boolean success = false;
		KuaAgihan ka = null;
		try {
			mp = new MyPersistence();
			ka = new KuaAgihan();
			KuaPermohonan permohonan = (KuaPermohonan) mp
					.get("SELECT kp FROM KuaPermohonan kp WHERE kp.pemohon.id = '"
							+ userId + "'");
			UsersJob uj = (UsersJob) mp
					.get("SELECT uj FROM UsersJob uj WHERE uj.users.id = '"
							+ userId + "'");
			ka.setPermohonan(permohonan);
			ka.setPemohon((Users) mp.find(Users.class, userId));
			ka.setPekerjaan(uj);
			ka.setStatus((Status) mp.find(Status.class, "1419601227601"));
			ka.setNoGiliran(0);
			ka.setIdLokasi(permohonan.getLokasi());
			ka.setTarikhKemaskini(new Date());
			mp.begin();
			mp.persist(ka);
			mp.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}

		try {
			mp = new MyPersistence();
			KuaPermohonan kp = (KuaPermohonan) mp.find(KuaPermohonan.class,
					getParam("idAgihan"));
			KuaKuarters kk = (KuaKuarters) mp.find(KuaKuarters.class,
					getParam("valuePilihKuarters"));
			Users petugas = (Users) mp.find(Users.class, getParam("idPetugas"));
			Users pengagih = (Users) mp.find(Users.class, userId);
			KuaTawaran p = new KuaTawaran();

			if (kk != null) {
				if (ka != null) {
					ka.setKuarters(kk);
					ka.setTarikhKemaskini(new Date());
					ka.setStatus((Status) mp
							.find(Status.class, "1431294274203"));
					ka.setPetugas(petugas);
					ka.setPengagih(pengagih);
					if (kp != null) {
						kp.setStatus((Status) mp.find(Status.class,
								"1423101446188"));
					}
				}
				kk.setFlagAgihan(1);
			}
			p.setAgihan(ka);
			p.setTarikhMasuk(new Date());
			mp.begin();
			mp.persist(p);
			mp.commit();
			success = true;
		} catch (Exception e) {
			e.printStackTrace();
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
			KuaAgihan ka = (KuaAgihan) mp.find(KuaAgihan.class,
					getParam("idAgihan"));
			ka.setStatus((Status) mp.find(Status.class, "1431327994521"));
			mp.begin();
			mp.commit();
			success = true;
		} catch (Exception e) {
			e.printStackTrace();
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

	@Override
	public void afterSave(KuaPermohonan arg0) {

	}

	@SuppressWarnings("unchecked")
	@Override
	public void getRelatedData(KuaPermohonan r) {
		try {
			mp = new MyPersistence();
			String kelas1 = "E";
			String idLokasi = r.getLokasi().getId();
			context.put("kuarters", dataUtil.getListKuartersTawaran(idLokasi, kelas1
					.replaceAll("1", "")));
			context.put("kelas1", kelas1);
			List<Users> users = mp
					.list("SELECT u FROM Users u WHERE u.id IN ("
							+ getPT()
							+ ") OR u.role.description = '(QTR) Penyedia' ORDER BY u.userName ASC");
			context.put("selectPetugas", users);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
	}
}
