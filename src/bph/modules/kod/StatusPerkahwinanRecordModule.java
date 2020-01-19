package bph.modules.kod;

import java.util.HashMap;
import java.util.Map;

import lebah.template.LebahRecordTemplateModule;
import bph.entities.kod.StatusPerkahwinan;


public class StatusPerkahwinanRecordModule extends
		LebahRecordTemplateModule<StatusPerkahwinan> {

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
	public void afterSave(StatusPerkahwinan arg0) {
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
	public boolean delete(StatusPerkahwinan arg0) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/kod/statusPerkahwinan";
	}

	@Override
	public Class<StatusPerkahwinan> getPersistenceClass() {
		// TODO Auto-generated method stub
		return StatusPerkahwinan.class;
	}

	@Override
	public void getRelatedData(StatusPerkahwinan arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save(StatusPerkahwinan r) throws Exception {
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
