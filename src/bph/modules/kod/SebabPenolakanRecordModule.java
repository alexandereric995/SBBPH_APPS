package bph.modules.kod;

import java.util.HashMap;
import java.util.Map;

import lebah.template.LebahRecordTemplateModule;
import bph.entities.kod.SebabPenolakan;

public class SebabPenolakanRecordModule extends LebahRecordTemplateModule<SebabPenolakan> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public void afterSave(SebabPenolakan arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void begin() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean delete(SebabPenolakan arg0) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/kod/sebabPenolakan";
	}

	@Override
	public Class<SebabPenolakan> getPersistenceClass() {
		// TODO Auto-generated method stub
		return SebabPenolakan.class;
	}

	@Override
	public void getRelatedData(SebabPenolakan arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save(SebabPenolakan r) throws Exception {
		// TODO Auto-generated method stub
		r.setId(get("id"));
		r.setKeterangan(get("keterangan"));
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		// TODO Auto-generated method stub
		String find_id = get("find_id");
		String find_keterangan = get("find_keterangan");

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", find_id);
		map.put("keterangan", find_keterangan);
		return map;
	}


}
