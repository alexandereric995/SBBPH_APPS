package bph.modules.kod;

import java.util.HashMap;
import java.util.Map;

import lebah.template.LebahRecordTemplateModule;
import bph.entities.kod.KodJenisPerolehan;

public class KodJenisPerolehanRecordModule extends LebahRecordTemplateModule<KodJenisPerolehan> {

	/**
	* @author muhdsyazreen
	*/
	
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public void afterSave(KodJenisPerolehan r) {
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
	public boolean delete(KodJenisPerolehan r) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/kod/kewangan/kodJenisPerolehan";
	}

	@Override
	public Class<KodJenisPerolehan> getPersistenceClass() {
		// TODO Auto-generated method stub
		return KodJenisPerolehan.class;
	}

	@Override
	public void getRelatedData(KodJenisPerolehan r) {
		// TODO Auto-generated method stub
	}

	@Override
	public void save(KodJenisPerolehan r) throws Exception {
		if(r.getId()==null){
			r.setId(get("id"));
		}
		r.setKod(get("kod"));
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
