package bph.modules.kod;

import java.util.Map;

import lebah.template.LebahRecordTemplateModule;
import bph.entities.kod.SumberAduan;
import bph.utils.DataUtil;

public class SumberAduanRecordModule extends LebahRecordTemplateModule<SumberAduan>{

	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;
		
	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<SumberAduan> getPersistenceClass() {
		// TODO Auto-generated method stub
		return SumberAduan.class;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/kod/sumberAduan";
	}

	@Override
	public void begin() {
		// TODO Auto-generated method stub
		
		dataUtil = DataUtil.getInstance(db); //untuk buat list dropdown kene letak kod ini
		context.put("selectSumberAduan", dataUtil.getListSumberAduan());
		
		context.put("dataUtil", dataUtil);
		context.put("path", getPath());
		
	}

	@Override
	public void save(SumberAduan r) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean delete(SumberAduan r) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterSave(SumberAduan r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getRelatedData(SumberAduan r) {
		// TODO Auto-generated method stub
		
	}



}
