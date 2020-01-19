package bph.modules.kod;

import java.util.HashMap;
import java.util.Map;

import lebah.template.LebahRecordTemplateModule;
import bph.entities.kod.KategoriBil;

public class KategoriBilRecordModule extends LebahRecordTemplateModule<KategoriBil>{

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
	public Class<KategoriBil> getPersistenceClass() {
		// TODO Auto-generated method stub
		return KategoriBil.class;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/kod/kategoriBil";
	}

	@Override
	public void begin() {
		// TODO Auto-generated method stub
		
		context.put("path", getPath());
	}

	@Override
	public void save(KategoriBil simpan) throws Exception {
		// TODO Auto-generated method stub
		
		simpan.setKod(get("kod"));
		simpan.setKeterangan(get("keterangan"));
		simpan.setCatatan(get("catatan"));
	}

	@Override
	public boolean delete(KategoriBil r) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		// TODO Auto-generated method stub
		String findKod = getParam("find_kod");
		String findKeterangan = getParam("find_keterangan");
		
		HashMap<String, Object> cari = new HashMap<String, Object>();
		cari.put("kod", findKod);
		cari.put("keterangan", findKeterangan);
		return cari;
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterSave(KategoriBil r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getRelatedData(KategoriBil r) {
		// TODO Auto-generated method stub
		
	}

	
	
}
