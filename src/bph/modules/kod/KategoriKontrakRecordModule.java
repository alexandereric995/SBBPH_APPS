package bph.modules.kod;

import java.util.HashMap;
import java.util.Map;

import lebah.template.LebahRecordTemplateModule;
import bph.entities.kod.KategoriKontrak;

public class KategoriKontrakRecordModule extends LebahRecordTemplateModule<KategoriKontrak>{

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
	public Class<KategoriKontrak> getPersistenceClass() {
		// TODO Auto-generated method stub
		return KategoriKontrak.class;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/kod/kategoriKontrak";
	}

	@Override
	public void begin() {
		// TODO Auto-generated method stub
		
		context.put("path", getPath());
	}

	@Override
	public void save(KategoriKontrak simpan) throws Exception {
		// TODO Auto-generated method stub
		
		simpan.setId(get("id"));
		simpan.setKeterangan(get("keterangan"));
	}

	@Override
	public boolean delete(KategoriKontrak r) throws Exception {
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
	public void afterSave(KategoriKontrak r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getRelatedData(KategoriKontrak r) {
		// TODO Auto-generated method stub
		
	}

}
