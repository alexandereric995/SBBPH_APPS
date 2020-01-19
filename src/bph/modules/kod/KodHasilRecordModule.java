package bph.modules.kod;

import java.util.HashMap;
import java.util.Map;

import lebah.template.LebahRecordTemplateModule;
import bph.entities.kod.KodHasil;

public class KodHasilRecordModule extends LebahRecordTemplateModule<KodHasil> {

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
	public void afterSave(KodHasil r) {
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
	public boolean delete(KodHasil r) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/kod/kewangan/kodHasil";
	}

	@Override
	public Class<KodHasil> getPersistenceClass() {
		// TODO Auto-generated method stub
		return KodHasil.class;
	}

	@Override
	public void getRelatedData(KodHasil r) {
		// TODO Auto-generated method stub
	}

	@Override
	public void save(KodHasil r) throws Exception {
		if(r.getId()==null){
			r.setId(get("kod"));
		}
		r.setKod(get("kod"));
		r.setKeterangan(get("keterangan"));
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		String find_kod = get("find_kod");
		String find_keterangan = get("find_keterangan");

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", find_kod);
		map.put("keterangan", find_keterangan);
		return map;
	}
}
