package bph.modules.kod;

import java.util.HashMap;
import java.util.Map;

import lebah.template.LebahRecordTemplateModule;
import bph.entities.kod.JenisPelanggaranSyaratUtk;
import bph.utils.DataUtil;

public class JenisPelanggaranSyaratUTK extends LebahRecordTemplateModule<JenisPelanggaranSyaratUtk>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;

	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public void afterSave(JenisPelanggaranSyaratUtk r) {
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
	public boolean delete(JenisPelanggaranSyaratUtk r) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/kod/jenisPelanggaranSyaratUTK";
	}

	@Override
	public Class<JenisPelanggaranSyaratUtk> getPersistenceClass() {
		// TODO Auto-generated method stub
		return JenisPelanggaranSyaratUtk.class;
	}

	@Override
	public void getRelatedData(JenisPelanggaranSyaratUtk r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save(JenisPelanggaranSyaratUtk r) throws Exception {
		// TODO Auto-generated method stub
		
		r.setKeterangan(get("keterangan"));
		r.setFlagKes(get("idflagKes"));
		
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		// TODO Auto-generated method stub
		String find_keterangan = get("find_keterangan");
		String find_flagKes = get("find_flagKes");

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("keterangan", find_keterangan);
		map.put("flagKes", find_flagKes);

		return map;
	}
	
}
