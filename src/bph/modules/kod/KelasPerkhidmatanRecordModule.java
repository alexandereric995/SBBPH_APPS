package bph.modules.kod;

import java.util.HashMap;
import java.util.Map;

import lebah.template.LebahRecordTemplateModule;
import bph.entities.kod.KelasPerkhidmatan;

public class KelasPerkhidmatanRecordModule extends
		LebahRecordTemplateModule<KelasPerkhidmatan> {

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
	public void afterSave(KelasPerkhidmatan arg0) {
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
	public boolean delete(KelasPerkhidmatan arg0) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/kod/kelasPerkhidmatan";
	}

	@Override
	public Class<KelasPerkhidmatan> getPersistenceClass() {
		// TODO Auto-generated method stub
		return KelasPerkhidmatan.class;
	}

	@Override
	public void getRelatedData(KelasPerkhidmatan arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save(KelasPerkhidmatan r) throws Exception {
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
