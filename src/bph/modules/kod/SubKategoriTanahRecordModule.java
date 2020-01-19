package bph.modules.kod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lebah.template.LebahRecordTemplateModule;
import bph.entities.kod.KategoriTanah;
import bph.entities.kod.SubKategoriTanah;
import bph.utils.DataUtil;

public class SubKategoriTanahRecordModule extends
		LebahRecordTemplateModule<SubKategoriTanah> {

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
	public void afterSave(SubKategoriTanah r) {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
	}

	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);

		context.remove("selectKategoriTanah");

		List<KategoriTanah> kategoriTanahList = dataUtil.getListKategoriTanah();
		context.put("selectKategoriTanah", kategoriTanahList);

		context.put("path", getPath());
	}

	@Override
	public boolean delete(SubKategoriTanah r) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/kod/subKategoriTanah";
	}

	@Override
	public Class<SubKategoriTanah> getPersistenceClass() {
		// TODO Auto-generated method stub
		return SubKategoriTanah.class;
	}

	@Override
	public void getRelatedData(SubKategoriTanah r) {
		// TODO Auto-generated method stub
	}

	@Override
	public void save(SubKategoriTanah r) throws Exception {
		r.setId(get("id"));
		r.setKeterangan(get("keterangan"));
		r.setKategoriTanah(db.find(KategoriTanah.class, get("idKategoriTanah")));
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		String find_id = get("find_id");
		String find_keterangan = get("find_keterangan");
		String findKategoriTanah = get("findKategoriTanah");

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", find_id);
		map.put("keterangan", find_keterangan);
		map.put("kategoriTanah.id", findKategoriTanah);

		return map;
	}
}
