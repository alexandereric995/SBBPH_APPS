package bph.modules.kod;

import java.util.HashMap;
import java.util.Map;

import lebah.template.LebahRecordTemplateModule;
import bph.entities.kod.JenisKegunaanRuang;

public class JenisKegunaanRuangRecordModule extends LebahRecordTemplateModule<JenisKegunaanRuang> {

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
	public void afterSave(JenisKegunaanRuang r) {
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
	public boolean delete(JenisKegunaanRuang r) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/kod/jenisKegunaanRuang";
	}

	@Override
	public Class<JenisKegunaanRuang> getPersistenceClass() {
		// TODO Auto-generated method stub
		return JenisKegunaanRuang.class;
	}

	@Override
	public void getRelatedData(JenisKegunaanRuang r) {
		// TODO Auto-generated method stub

	}

	@Override
	public void save(JenisKegunaanRuang r) throws Exception {
		r.setId(get("id"));
		r.setKeterangan(get("keterangan"));
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		String find_id = get("find_id");
		String find_kod = get("find_kod");
		String find_keterangan = get("find_keterangan");

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", find_id);
		map.put("kod", find_kod);
		map.put("keterangan", find_keterangan);

		return map;
	}
}
