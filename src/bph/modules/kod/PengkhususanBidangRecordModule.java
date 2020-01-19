package bph.modules.kod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorEqualTo;
import bph.entities.kod.KategoriBidangKontraktor;
import bph.entities.kod.PengkhususanBidangKontraktor;
import bph.utils.DataUtil;

public class PengkhususanBidangRecordModule extends LebahRecordTemplateModule<PengkhususanBidangKontraktor> {

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
	public void afterSave(PengkhususanBidangKontraktor r) {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
	}

	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);

		context.remove("selectKategori");

		List<KategoriBidangKontraktor> kategoriList = dataUtil.getListKategoriBidangKontraktor();
		context.put("selectKategori", kategoriList);

		context.put("path", getPath());
	}

	@Override
	public boolean delete(PengkhususanBidangKontraktor r) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/kod/pengkhususanBidangKontraktor";
	}

	@Override
	public Class<PengkhususanBidangKontraktor> getPersistenceClass() {
		// TODO Auto-generated method stub
		return PengkhususanBidangKontraktor.class;
	}

	@Override
	public void getRelatedData(PengkhususanBidangKontraktor r) {
		// TODO Auto-generated method stub
	}

	@Override
	public void save(PengkhususanBidangKontraktor r) throws Exception {
		r.setId(get("id"));
		r.setKeterangan(get("keterangan"));
		r.setKategori(db.find(KategoriBidangKontraktor.class, get("idKategori")));
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		String find_id = get("find_id");
		String find_keterangan = get("find_keterangan");
		String findKategori = get("findKategori");

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", find_id);
		map.put("keterangan", find_keterangan);
		map.put("kategori.id", new OperatorEqualTo(findKategori));

		return map;
	}
}
