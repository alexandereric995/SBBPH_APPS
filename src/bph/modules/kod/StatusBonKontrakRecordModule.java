package bph.modules.kod;

import java.util.HashMap;
import java.util.Map;

import lebah.template.LebahRecordTemplateModule;
import bph.entities.kod.StatusBonKontrak;

public class StatusBonKontrakRecordModule extends LebahRecordTemplateModule<StatusBonKontrak> {

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
	public void afterSave(StatusBonKontrak r) {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
	}

	@Override
	public void begin() {
		// TODO Auto-generated method stub
		this.setDisableSaveAddNewButton(true);
	}

	@Override
	public boolean delete(StatusBonKontrak r) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/kod/statusBonKontrak";
	}

	@Override
	public Class<StatusBonKontrak> getPersistenceClass() {
		// TODO Auto-generated method stub
		return StatusBonKontrak.class;
	}

	@Override
	public void getRelatedData(StatusBonKontrak r) {
		// TODO Auto-generated method stub
	}

	@Override
	public void save(StatusBonKontrak r) throws Exception {
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
