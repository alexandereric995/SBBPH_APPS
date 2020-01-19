/**
 * 
 */
package bph.modules.kod;

import java.util.HashMap;
import java.util.Map;

import lebah.template.LebahRecordTemplateModule;
import bph.entities.kod.JenisRayuan;

public class JenisRayuanRecordModule extends LebahRecordTemplateModule<JenisRayuan>{

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
	public void afterSave(JenisRayuan arg0) {
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
	public boolean delete(JenisRayuan arg0) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/kod/jenisRayuan";
	}

	@Override
	public Class<JenisRayuan> getPersistenceClass() {
		// TODO Auto-generated method stub
		return JenisRayuan.class;
	}

	@Override
	public void getRelatedData(JenisRayuan arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save(JenisRayuan r) throws Exception {
		// TODO Auto-generated method stub

		r.setCatatan(get("catatan"));
		r.setStatus(get("status"));
		r.setKeterangan(get("keterangan"));
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		// TODO Auto-generated method stub
		String find_status = get("find_status");
		String find_keterangan = get("find_keterangan");
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("status", find_status);
		map.put("keterangan", find_keterangan);
		
		return map;

	}

}
