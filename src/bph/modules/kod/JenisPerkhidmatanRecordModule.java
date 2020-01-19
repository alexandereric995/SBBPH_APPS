package bph.modules.kod;

import java.util.HashMap;
import java.util.Map;

import lebah.template.LebahRecordTemplateModule;
import bph.entities.kod.JenisPerkhidmatan;

public class JenisPerkhidmatanRecordModule extends
		LebahRecordTemplateModule<JenisPerkhidmatan> {

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
	public void afterSave(JenisPerkhidmatan r) {
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
	public boolean delete(JenisPerkhidmatan r) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/kod/jenisPerkhidmatan";
	}

	@Override
	public Class<JenisPerkhidmatan> getPersistenceClass() {
		// TODO Auto-generated method stub
		return JenisPerkhidmatan.class;
	}

	@Override
	public void getRelatedData(JenisPerkhidmatan r) {
		// TODO Auto-generated method stub
	}

	@Override
	public void save(JenisPerkhidmatan r) throws Exception {
		r.setId(get("id"));
		r.setKeterangan(get("keterangan"));
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		String find_id = get("find_id");
		String find_keterangan = get("find_keterangan");

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", find_id);
		map.put("keterangan", find_keterangan);

		return map;
	}
}
