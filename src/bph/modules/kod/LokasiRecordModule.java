/**
 * 
 */
package bph.modules.kod;

import java.util.HashMap;
import java.util.Map;

import lebah.template.LebahRecordTemplateModule;
import bph.entities.kod.Lokasi;

public class LokasiRecordModule extends LebahRecordTemplateModule<Lokasi>{

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
	public void afterSave(Lokasi r) {
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
	public boolean delete(Lokasi r) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/kod/lokasi";
	}

	@Override
	public Class<Lokasi> getPersistenceClass() {
		// TODO Auto-generated method stub
		return Lokasi.class;
	}

	@Override
	public void getRelatedData(Lokasi r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save(Lokasi r) throws Exception {
		r.setKeterangan(get("keterangan"));
		r.setJenisKuaters(get("jenisKuaters"));
		r.setCatatan(get("catatan"));
		r.setStatus(get("status"));
	}

	@Override	
	public Map<String, Object> searchCriteria() throws Exception {
	String find_keterangan = get("find_keterangan");
	String find_jenisKuaters = get("find_jenisKuaters");
	
	Map<String, Object> map = new HashMap<String, Object>();
	
	map.put("keterangan", find_keterangan);
	map.put("jenisKuaters", find_jenisKuaters);
	return map;

	}

}
