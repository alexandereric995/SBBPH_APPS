package bph.modules.kod;

import java.util.HashMap;
import java.util.Map;

import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorEqualTo;
import bph.entities.kod.KategoriBil;
import bph.entities.kod.KodBil;
import bph.utils.DataUtil;

public class KodBilRecordModule extends LebahRecordTemplateModule<KodBil>{

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
	public Class<KodBil> getPersistenceClass() {
		// TODO Auto-generated method stub
		return KodBil.class;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/kod/kodBil";
	}

	@Override
	public void begin() {
		// TODO Auto-generated method stub
		
		dataUtil = DataUtil.getInstance(db); //untuk buat list dropdown kene letak kod ini
		context.put("selectKategoriBil", dataUtil.getListKategoriBil());
		
		context.put("dataUtil", dataUtil);
		context.put("path", getPath());
	}

	@Override
	public void save(KodBil simpan) throws Exception {
		// TODO Auto-generated method stub
		
		simpan.setKategori(db.find(KategoriBil.class, get("idKategori")));
		simpan.setKod(get("kod"));
		simpan.setKeterangan(get("keterangan"));
		simpan.setCatatan(get("catatan"));
		
	}

	@Override
	public boolean delete(KodBil r) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		// TODO Auto-generated method stub
		String findKategoriBil = get("findKategoriBil");
		String findKod = get("find_kod");
		String findNama = get("find_nama");
		
		HashMap<String, Object> cari = new HashMap<String, Object>();
		cari.put("kategoriBil.id", new OperatorEqualTo(findKategoriBil));
		cari.put("kod", findKod);
		cari.put("nama", findNama);
		return cari;
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterSave(KodBil r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getRelatedData(KodBil r) {
		// TODO Auto-generated method stub
		
	}

}
