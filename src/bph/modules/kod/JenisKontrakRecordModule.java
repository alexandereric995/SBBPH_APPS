package bph.modules.kod;

import java.util.HashMap;
import java.util.Map;

import lebah.template.LebahRecordTemplateModule;
import bph.entities.kod.JenisKontrak;

public class JenisKontrakRecordModule extends LebahRecordTemplateModule<JenisKontrak>{

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
	public Class<JenisKontrak> getPersistenceClass() {
		// TODO Auto-generated method stub
		return JenisKontrak.class;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/kod/jenisKontrak";
	}

	@Override
	public void begin() {
		// TODO Auto-generated method stub
		
		context.put("path", getPath());
	}

	@Override
	public void save(JenisKontrak simpan) throws Exception {
		// TODO Auto-generated method stub
		
		simpan.setId(get("id"));
		simpan.setKeterangan(get("keterangan"));
	}

	@Override
	public boolean delete(JenisKontrak r) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		// TODO Auto-generated method stub
		String findId = get("find_id");
		String findKeterangan = get("find_keterangan");
		
		HashMap<String, Object> cari = new HashMap<String, Object>();	
		cari.put("id", findId);
		cari.put("keterangan", findKeterangan);
		return cari;
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterSave(JenisKontrak r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getRelatedData(JenisKontrak r) {
		// TODO Auto-generated method stub
		
	}

}
