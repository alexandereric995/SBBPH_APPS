package bph.modules.kod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lebah.template.LebahRecordTemplateModule;
import bph.entities.kod.Daerah;
import bph.entities.kod.Mukim;
import bph.utils.DataUtil;

public class MukimRecordModule extends LebahRecordTemplateModule<Mukim> {

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
	public void afterSave(Mukim r) {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
	}

	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);

		context.remove("selectDaerah");

		List<Daerah> daerahList = dataUtil.getListDaerah();
		context.put("selectDaerah", daerahList);

		context.put("path", getPath());
	}

	@Override
	public boolean delete(Mukim r) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/kod/mukim";
	}

	@Override
	public Class<Mukim> getPersistenceClass() {
		// TODO Auto-generated method stub
		return Mukim.class;
	}

	@Override
	public void getRelatedData(Mukim r) {

	}

	@Override
	public void save(Mukim r) throws Exception {
		r.setId(get("id"));
		r.setKeterangan(get("keterangan"));
		r.setDaerah(db.find(Daerah.class, get("idDaerah")));
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		String find_id = get("find_id");
		String find_keterangan = get("find_keterangan");
		String findDaerah = get("findDaerah");

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", find_id);
		map.put("keterangan", find_keterangan);
		map.put("daerah.id", findDaerah);

		return map;
	}
}
