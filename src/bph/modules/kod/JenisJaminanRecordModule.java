package bph.modules.kod;

import java.util.HashMap;
import java.util.Map;

import lebah.template.LebahRecordTemplateModule;
import bph.entities.kod.JenisJaminan;

public class JenisJaminanRecordModule extends LebahRecordTemplateModule<JenisJaminan>{

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
	public Class<JenisJaminan> getPersistenceClass() {
		// TODO Auto-generated method stub
		return JenisJaminan.class;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/kod/jenisJaminan";
	}

	@Override
	public void begin() {
		// TODO Auto-generated method stub
		
		context.put("path", getPath());
	}

	@Override
	public void save(JenisJaminan simpan) throws Exception {
		// TODO Auto-generated method stub
		
		simpan.setId(get("id"));
		simpan.setKeterangan(get("keterangan"));
	}

	@Override
	public boolean delete(JenisJaminan r) throws Exception {
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
	public void afterSave(JenisJaminan r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getRelatedData(JenisJaminan r) {
		// TODO Auto-generated method stub
		
	}

}
