package bph.modules.kod;

import java.util.HashMap;
import java.util.Map;

import lebah.template.LebahRecordTemplateModule;
import bph.entities.kod.Agensi;

public class JabatanRecordModule extends LebahRecordTemplateModule<Agensi> {

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
	public void afterSave(Agensi arg0) {
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
	public boolean delete(Agensi arg0) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/kod/jabatan";
	}

	@Override
	public Class<Agensi> getPersistenceClass() {
		// TODO Auto-generated method stub
		return Agensi.class;
	}

	@Override
	public void getRelatedData(Agensi arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save(Agensi r) throws Exception {
		// TODO Auto-generated method stub
		r.setId(get("id"));
		r.setKeterangan(get("keterangan"));
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		// TODO Auto-generated method stub
		String find_kod = get("find_kod");
		String find_keterangan = get("find_keterangan");
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("id", find_kod);
		map.put("keterangan", find_keterangan);
		return map;
	}

	
}
