package bph.modules.qtr;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lebah.template.LebahRecordTemplateModule;
import portal.module.entity.Users;
import bph.entities.kod.JenisRayuan;
import bph.entities.qtr.KuaRayuan;
import bph.utils.DataUtil;

public class FrmKuaRayuanRecordAll extends LebahRecordTemplateModule<KuaRayuan> {

	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;

	@SuppressWarnings("unchecked")
	@Override
	public Class getIdType() {
		return String.class;
	}

	@Override
	public void afterSave(KuaRayuan arg0) {

	}

	@Override
	public void beforeSave() {

	}

	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);
		List<JenisRayuan> senaraiJenisRayuan = dataUtil.getListJenisRayuan();
		context.put("selectJenisRayuan", senaraiJenisRayuan);
	}

	@Override
	public boolean delete(KuaRayuan arg0) throws Exception {
		return true;
	}

	@Override
	public String getPath() {
		return "bph/modules/qtr/senaraiRayuan";
	}

	@Override
	public Class<KuaRayuan> getPersistenceClass() {
		return KuaRayuan.class;
	}

	@Override
	public void getRelatedData(KuaRayuan arg0) {
		context.put("selectJenisRayuan", dataUtil.getListJenisRayuan());
	}

	@Override
	public void save(KuaRayuan arg0) throws Exception {
		JenisRayuan jenisRayuan = db.find(JenisRayuan.class,
				getParam("idJenisRayuan"));
		Users pemohon = db.find(Users.class, getParam("idPemohon"));
		arg0.setPemohon(pemohon);
		arg0.setJenisRayuan(jenisRayuan);
		arg0.setLainJenisRayuan(getParam("lainJenisRayuan"));
		arg0.setSebabRayuan(getParam("sebabRayuan"));
		arg0.setTarikhRayuanDibuat(getDate("tarikhRayuanDibuat"));
		arg0.setTarikhMaklumbalas(getDate("tarikhMaklumbalas"));
		arg0.setTarikhRayuan(getDate("tarikhRayuan"));
		arg0.setTarikhKemaskini(new Date());
		arg0.setStatusRayuan(getParam("statusRayuan"));
		arg0.setCatatan(getParam("catatan"));
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		HashMap<String, Object> r = new HashMap<String, Object>();
		r.put("pemohon.noKP", getParam("findUserNoKP"));
		r.put("pemohon.userName", getParam("findUserName"));
		return r;
	}
}
