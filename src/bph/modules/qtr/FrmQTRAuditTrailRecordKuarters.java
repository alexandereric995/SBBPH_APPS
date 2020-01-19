package bph.modules.qtr;

import java.util.HashMap;
import java.util.Map;

import lebah.template.LebahRecordTemplateModule;
import bph.entities.qtr.LogKuarters;
import bph.utils.DataUtil;
import db.persistence.MyPersistence;

public class FrmQTRAuditTrailRecordKuarters extends LebahRecordTemplateModule<LogKuarters> {
	private static final long serialVersionUID = 7859853801709412089L;
	private DataUtil dataUtil;
	private MyPersistence mp;
	
	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		return String.class;
	}

	@Override
	public Class<LogKuarters> getPersistenceClass() {
		return LogKuarters.class;
	}

	@Override
	public String getPath() {
		return "bph/modules/qtr/auditKuarters";
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
		context.put("findKelasKuarters", dataUtil.getListKelasPeranginan());
		context.put("findJenisKediaman", dataUtil.getListJenisKediaman());
		context.put("findStatusKuarters", dataUtil.getListStatusKuartersBaru());
		context.put("findNegeri", dataUtil.getList6Negeri());
		context.put("path", getPath());
		this.setDisableSaveAddNewButton(true);
		this.setDisableKosongkanUpperButton(true);
		this.setDisableUpperBackButton(true);
		this.setReadonly(true);
		setOrderBy("tarikhKemaskini DESC");
	}

	@Override
	public void save(LogKuarters r) throws Exception {

	}

	@Override
	public boolean delete(LogKuarters r) throws Exception {
		return false;
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		HashMap<String, Object> r = new HashMap<String, Object>();
		r.put("noRujukan", getParam("findNoRujukan"));
		r.put("noUnit", getParam("findNoUnit"));
		return r;
	}

	@Override
	public void beforeSave() {

	}

	@Override
	public void afterSave(LogKuarters r) {

	}

	@Override
	public void getRelatedData(LogKuarters r) 
	{
		try {
			mp = new MyPersistence();
			LogKuarters pilihan = (LogKuarters) mp.find(LogKuarters.class, r.getId());
			LogKuarters dulu=null;
			context.put("pilihan", pilihan);
			int turutan=pilihan.getTurutan();
			if(turutan>0){
				turutan=turutan-1;
				dulu = (LogKuarters) mp.get("SELECT k FROM LogKuarters k WHERE k.noRujukan = '"+ pilihan.getNoRujukan() + "' and k.turutan = "+turutan+"");
			}
			context.put("history", dulu);
		} catch (Exception e) {
			System.out.println("Error getRelatedData : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
	}
}
