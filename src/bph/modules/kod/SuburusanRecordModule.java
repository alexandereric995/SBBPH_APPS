package bph.modules.kod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lebah.template.LebahRecordTemplateModule;
import bph.entities.kod.SubUrusan;
import bph.entities.kod.Urusan;
import bph.utils.DataUtil;

public class SuburusanRecordModule extends LebahRecordTemplateModule<SubUrusan> {

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
	public void afterSave(SubUrusan r) {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
	}

	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);

		context.remove("selectUrusan");

		List<Urusan> urusanList = dataUtil.getListUrusan();
		context.put("selectUrusan", urusanList);

		context.put("path", getPath());
	}

	@Override
	public boolean delete(SubUrusan r) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/kod/subUrusan";
	}

	@Override
	public Class<SubUrusan> getPersistenceClass() {
		// TODO Auto-generated method stub
		return SubUrusan.class;
	}

	@Override
	public void getRelatedData(SubUrusan r) {
		// TODO Auto-generated method stub
	}

	@Override
	public void save(SubUrusan r) throws Exception {
		r.setId(get("id"));
		r.setKeterangan(get("keterangan"));
		r.setUrusan(db.find(Urusan.class, get("idUrusan")));
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		String find_id = get("find_id");
		String find_keterangan = get("find_keterangan");
		String findUrusan = get("findUrusan");

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", find_id);
		map.put("keterangan", find_keterangan);
		map.put("urusan.id", findUrusan);

		return map;
	}
}
