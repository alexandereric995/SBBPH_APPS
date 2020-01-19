package bph.modules.qtr;

import java.util.HashMap;
import java.util.Map;

import lebah.template.LebahRecordTemplateModule;
import portal.module.entity.UsersActivity;
import bph.utils.DataUtil;
import db.persistence.MyPersistence;

public class FrmQTRAuditTrailRecord extends LebahRecordTemplateModule<UsersActivity> {
	private static final long serialVersionUID = 7859853801709412089L;
	private DataUtil dataUtil;
	private MyPersistence mp;
	
	@SuppressWarnings("unchecked")
	@Override
	public Class getIdType() {
		return String.class;
	}

	@Override
	public Class<UsersActivity> getPersistenceClass() {
		return UsersActivity.class;
	}

	@Override
	public String getPath() {
		return "bph/modules/qtr/audit";
	}

	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);
		context.put("selectNegeri", dataUtil.getListNegeri());
		context.put("selectKelasKuarters", dataUtil.getListKelasPeranginan());
		context.put("selectJenisKediaman", dataUtil.getListJenisKediaman());
		context.put("selectJenisKegunaanKuarters", dataUtil.getListJenisKegunaanKuarters());
		context.put("selectStatusKuarters", dataUtil.getListStatusKuartersBaru());
		context.put("selectLokasiKuarters", dataUtil.getListLokasiKuarters());
		context.put("selectLokasiDibenar", dataUtil.getListLokasiDibenar());
		context.put("selectFasa", dataUtil.getListFasa());
		context.put("findLokasiKuarters", dataUtil.getListLokasiKuarters());
		context.put("findKelasKuarters", dataUtil.getListKelasPeranginan());
		context.put("findJenisKediaman", dataUtil.getListJenisKediaman());
		context.put("findStatusKuarters", dataUtil.getListStatusKuartersBaru());
		context.put("findFasa", dataUtil.getListFasa());
		context.put("findNegeri", dataUtil.getListNegeri());
		context.put("path", getPath());
		this.setDisableSaveAddNewButton(true);
		this.setDisableKosongkanUpperButton(true);
		this.setDisableUpperBackButton(true);
		this.setReadonly(true);
		setOrderBy("tarikhKemaskini DESC");
	}

	@Override
	public void save(UsersActivity r) throws Exception {

	}

	@Override
	public boolean delete(UsersActivity r) throws Exception {
		return false;
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		HashMap<String, Object> r = new HashMap<String, Object>();
		r.put("userLogin", getParam("findNoKPPemohon"));
		r.put("userName", getParam("findNamaPemohon"));
		return r;
	}

	@Override
	public void beforeSave() {

	}

	@Override
	public void afterSave(UsersActivity r) {

	}

	@Override
	public void getRelatedData(UsersActivity r) {
		try {
			mp = new MyPersistence();
			UsersActivity pilihan = (UsersActivity) mp.find(UsersActivity.class, r.getId());
			UsersActivity dulu=null;
			context.put("pilihan", pilihan);
			int turutan=pilihan.getTurutan();
			if(turutan>0){
				turutan=turutan-1;
				dulu = (UsersActivity) mp.get("SELECT k FROM UsersActivity k WHERE k.userLogin = '"+ pilihan.getUserLogin() + "' and k.turutan = "+turutan+"");
			}
			context.put("history", dulu);
		} catch (Exception e) {
			System.out.println("Error getRelatedData : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
	}
}
