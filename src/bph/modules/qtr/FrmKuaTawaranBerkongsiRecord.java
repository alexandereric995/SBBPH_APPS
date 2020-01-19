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
import bph.entities.qtr.KuaKuarters;
import bph.entities.qtr.KuaPermohonan;
import bph.entities.qtr.KuaTawaran;
import bph.utils.DataUtil;
import db.persistence.MyPersistence;

public class FrmKuaTawaranBerkongsiRecord extends
		LebahRecordTemplateModule<KuaAgihan> {

	private static final long serialVersionUID = -4694709486982124681L;
	static Logger myLogger = Logger
			.getLogger("bph/modules/qtr/FrmKuaAgihanRecord");
	private DataUtil dataUtil;
	private MyPersistence mp;

	@SuppressWarnings("unchecked")
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
		return "bph/modules/qtr/tawaranBerkongsi";
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
		try {
			mp = new MyPersistence();
			String[] role = userRole.split(" ");
			for (int i = 0; i < role.length; i++) {
				if ("Penyemak".equals(role[i]) || "Pelulus".equals(role[i])) {
					context.put("currentRoleQTR", role[i]);
				}
			}
			addFilter("status.id = '1419601227590'");
			addFilter("noGiliran > 0");
			addFilter("pemohon.statusPerkahwinan.id ='01'");// bujang sahaja
			setOrderBy("kelasKuarters ASC");
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
			if (r.getKuarters() != null)
				kk = (KuaKuarters) mp.find(KuaKuarters.class, r.getKuarters().getId());
			KuaPermohonan kp = (KuaPermohonan) mp.find(KuaPermohonan.class, r.getPermohonan().getId());
			kp.setStatus((Status) mp.find(Status.class, "1419483289678"));// tukar jadi permohonan baru
			kp.setTarikhKemaskini(null);
			if (kk != null)
				kk.setFlagAgihan(0);
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
		r.put("permohonan.lokasi.id", getParam("findLokasiKuarters"));
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
			KelasKuarters k1 = (KelasKuarters) mp.find(KelasKuarters.class, r
					.getKelasKuarters());
			String idLokasi = r.getPermohonan().getLokasi().getId();
			if (k1 != null)
				kelas1 = k1.getId();
			else
				kelas1 = r.getKelasKuarters();

			context.put("kuarters", dataUtil.getListKuartersBerkongsi(idLokasi,
					kelas1.replaceAll("1", "")));
			context.put("kelas1", kelas1);

			if ("".equals(r.getPermohonan().getFlagDowngrade()))
				context.put("kelas2", dataUtil.getKelasDowngrade(r
						.getKelasKuarters()));
			if (r.getKuarters() != null)
				context.put("kuarters", mp.find(KuaKuarters.class, r
						.getKuarters().getId()));

			List<Users> users = mp
					.list("SELECT u FROM Users u WHERE u.id IN ("
							+ getPT()
							+ ") OR u.role.description = '(QTR) Penyedia' ORDER BY u.userName ASC");
			context.put("selectPetugas", users);

			List<SebabPenolakan> sp = mp.list("SELECT u FROM SebabPenolakan u");
			context.put("selectPenolakan", sp);

		} catch (Exception e) {
			System.out.println("Error getRelatedData : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}

	}

	@Command("agihKuarters")
	public String agihKuarters() {
		boolean success = false;
		try {
			mp = new MyPersistence();
			KuaAgihan ka = (KuaAgihan) mp.find(KuaAgihan.class,
					getParam("idAgihan"));
			KuaPermohonan kp = (KuaPermohonan) mp.find(KuaPermohonan.class, ka
					.getPermohonan().getId());
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
								"1423101446188")); // permohonan sedang disemak
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
			KuaAgihan ka = (KuaAgihan) mp.find(KuaAgihan.class,
					getParam("idAgihan"));
			ka.setStatus((Status) mp.find(Status.class, "1431327994521"));
			ka.setSebabTolak((SebabPenolakan) mp.find(SebabPenolakan.class,
					getParam("idPenolakan")));
			ka.setTarikhKemaskini(new Date());
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
}
