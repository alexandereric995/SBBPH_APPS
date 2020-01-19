package bph.modules.qtr;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lebah.portal.action.Command;
import lebah.template.OperatorDateBetween;
import portal.module.entity.Users;
import portal.module.entity.UsersJob;
import bph.entities.kod.GelaranDalamSurat;
import bph.entities.qtr.KuaTawaran;
import bph.utils.DataUtil;
import db.persistence.MyPersistence;

public class FrmKuaTawaranOverallRecord extends FrmKuaTawaranRecord {

	private static final long serialVersionUID = 8517309942213366361L;
	private DataUtil dataUtil;
	private MyPersistence mp;

	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);
		setDisableAddNewRecordButton(true);
		setDisableDefaultButton(true);
		setDisableBackButton(true);
		setDisableUpperBackButton(true);
		setReadonly(true);
		userId = (String) request.getSession().getAttribute("_portal_login");
		userRole= (String) request.getSession().getAttribute("_portal_role");
		context.put("selectJenisPenolakan", dataUtil.getListJenisPenolakan());
		context.put("selectSebabPenolakan", dataUtil.getListSebabPenolakan());
		context.put("selectGelaranDalamSurat", dataUtil.getListGelaranDalamSurat());
		context.put("overall", "ya");
		context.put("path", getPath());
	}

	@Override
	public void save(KuaTawaran r) throws Exception {

	}

	@Override
	public boolean delete(KuaTawaran r) throws Exception {
		return true;
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		HashMap<String, Object> r = new HashMap<String, Object>();
		r.put("agihan.dateAgih",new OperatorDateBetween(getDate("findTarikhAgihan"), getDate("findTarikhAgihan")));
		r.put("agihan.permohonan.noPermohonan", getParam("findNoPermohonan"));
		r.put("agihan.pemohon.userName", getParam("findNamaPemohon"));
		r.put("agihan.pemohon.noKP", getParam("findNoKPPemohon"));
		return r;
	}

	@Override
	public void beforeSave() {

	}

	@Override
	public void afterSave(KuaTawaran r) {

	}

	@SuppressWarnings("unchecked")
	@Override
	public void getRelatedData(KuaTawaran r) {
		try {
			mp = new MyPersistence();
			UsersJob uj = (UsersJob) mp.get("SELECT uj FROM UsersJob uj WHERE uj.users.id = '" + r.getAgihan().getPemohon().getId() + "'");
			KuaTawaran tawaran = (KuaTawaran) mp.find(KuaTawaran.class, r.getId());
			userId = (String) request.getSession().getAttribute("_portal_login");
			userRole= (String) request.getSession().getAttribute("_portal_role");	
			context.put("uj", uj);
			context.put("offer", tawaran);
			context.put("r", tawaran);
			context.put("currentRole", userRole);
			List<Users> users = mp.list("SELECT u FROM Users u WHERE u.id IN (" + getPelulus()
			+ ") OR u.role.description = '(QTR) Pelulus' ORDER BY u.userName ASC");
			context.put("selectPegawai", users);
		} catch (Exception e) {
			System.out.println("Error getRelatedData : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
	}

	@Command("getSuratTawaran")
	public String getSuratTawaran() {
		try {
			mp = new MyPersistence();
			KuaTawaran tawaran = (KuaTawaran) mp.find(KuaTawaran.class, getParam("idTawaran"));
			GelaranDalamSurat gds = (GelaranDalamSurat) mp.find(GelaranDalamSurat.class,getParam("idGelaranDalamSurat"));
			Date d = getDate("tarikhSurat");
			if (d == null)d = new Date();
			if (tawaran.getTarikhSurat() == null)tawaran.setTarikhSurat(new Date());
			tawaran.setTarikhSuratSebenar(getDate("tarikhSurat"));
			tawaran.setTitleDalamSurat(gds);
			tawaran.setNoFail(getParam("noFail"));
			tawaran.setBil(getParam("bil"));
			tawaran.setCetakSurat("ya");
			tawaran.setFlagSelesai(1);
			tawaran.setKepada(getParam("kepadaSiapa"));
			tawaran.setGenerateEmail("Y");
			tawaran.setPegawai((Users) mp.find(Users.class, getParam("idPegawai")));
			tawaran.setNamaKementerian(getParam("kementerian"));
			tawaran.setNamaJabatan(getParam("jabatan"));
			tawaran.setNamaBahagian(getParam("bahagian"));
			tawaran.setNamaAlamat1(getParam("alamatPejabat1"));
			tawaran.setNamaAlamat2(getParam("alamatPejabat2"));
			tawaran.setNamaAlamat3(getParam("alamatPejabat3"));
			tawaran.setNamaPoskod(getParam("alamatPoskod"));
			tawaran.setNamaNegeri(getParam("alamatNegeri"));
			tawaran.setNamaBandar(getParam("alamatBandar"));
			mp.begin();
			mp.commit();
		} catch (Exception e) {
			System.out.println("Error getSuratTawaran : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("idTawaran", getParam("idTawaran"));
		return getPath() + "/sub_page/suratTawaran.vm";
	}
}
