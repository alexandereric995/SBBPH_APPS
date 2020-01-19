package bph.modules.kod;

import java.util.HashMap;
import java.util.Map;

import lebah.template.LebahRecordTemplateModule;
import bph.entities.kod.StatusHakmilik;

public class StatusHakmilikRecordModule extends
		LebahRecordTemplateModule<StatusHakmilik> {

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
	public void afterSave(StatusHakmilik r) {
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
	public boolean delete(StatusHakmilik r) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/kod/statusHakMilik";
	}

	@Override
	public Class<StatusHakmilik> getPersistenceClass() {
		// TODO Auto-generated method stub
		return StatusHakmilik.class;
	}

	@Override
	public void getRelatedData(StatusHakmilik r) {
		// TODO Auto-generated method stub
	}

	@Override
	public void save(StatusHakmilik r) throws Exception {
		r.setId(get("status"));
		r.setKeterangan(get("keterangan"));
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		String find_id = get("find_id");
		String find_keterangan = get("find_keterangan");

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", find_id);
		map.put("keterangan", find_keterangan);

		return map;
	}
}
