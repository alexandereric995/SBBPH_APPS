package bph.modules.kod;

import java.util.HashMap;
import java.util.Map;

import lebah.template.LebahRecordTemplateModule;
import bph.entities.kod.SebabHilangKelayakanUtk;
import bph.utils.DataUtil;

public class SebabHilangKelayakanUtkRecordModule extends LebahRecordTemplateModule<SebabHilangKelayakanUtk>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;

	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public Class<SebabHilangKelayakanUtk> getPersistenceClass() {
		// TODO Auto-generated method stub
		return SebabHilangKelayakanUtk.class;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/kod/sebabHilangKelayakanUtk";
	}

	@Override
	public void begin() {
		// TODO Auto-generated method stub
		
		dataUtil = DataUtil.getInstance(db); //untuk buat list dropdown kene letak kod ini
//		context.put("selectKategoriBil", dataUtil.getListKategoriBil());
		
		context.put("dataUtil", dataUtil);
		context.put("path", getPath());
	}

	@Override
	public void save(SebabHilangKelayakanUtk simpan) throws Exception {
		// TODO Auto-generated method stub
		
//		simpan.setKategori(db.find(KategoriBil.class, get("idKategori")));
		simpan.setId(get("id"));
		simpan.setKeterangan(get("keterangan"));
	}

	@Override
	public boolean delete(SebabHilangKelayakanUtk r) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		// TODO Auto-generated method stub
		String findId = get("find_id");
		String findNama = get("find_nama");
		
		HashMap<String, Object> cari = new HashMap<String, Object>();
		cari.put("id", findId);
		cari.put("keterangan", findNama);
		return cari;
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterSave(SebabHilangKelayakanUtk r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getRelatedData(SebabHilangKelayakanUtk r) {
		// TODO Auto-generated method stub
		
	}

}
