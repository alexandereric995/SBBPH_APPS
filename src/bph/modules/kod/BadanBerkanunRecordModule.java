package bph.modules.kod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lebah.template.LebahRecordTemplateModule;
import bph.entities.kod.BadanBerkanun;
import bph.entities.kod.Kementerian;
import bph.utils.DataUtil;

public class BadanBerkanunRecordModule extends LebahRecordTemplateModule<BadanBerkanun> {

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
	public void afterSave(BadanBerkanun r) {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
	}

	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);

		context.remove("selectKementerian");

		List<Kementerian> kementerianList = dataUtil.getListKementerian();
		context.put("selectKementerian", kementerianList);

		context.put("path", getPath());
	}

	@Override
	public boolean delete(BadanBerkanun r) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/kod/badanBerkanun";
	}

	@Override
	public Class<BadanBerkanun> getPersistenceClass() {
		// TODO Auto-generated method stub
		return BadanBerkanun.class;
	}

	@Override
	public void getRelatedData(BadanBerkanun r) {

	}

	@Override
	public void save(BadanBerkanun r) throws Exception {
		r.setId(get("id"));
		r.setKeterangan(get("keterangan"));
		r.setKementerian(db.find(Kementerian.class, get("idKementerian")));
		r.setSingkatan(get("singkatan"));
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		String find_id = get("find_id");
		String findKementerian = get("findKementerian");
		String find_keterangan = get("find_keterangan");
		String find_singkatan = get("find_singkatan");

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", find_id);
		map.put("kementerian.id", findKementerian);
		map.put("keterangan", find_keterangan);
		map.put("singkatan", find_singkatan);
		return map;
	}
}
