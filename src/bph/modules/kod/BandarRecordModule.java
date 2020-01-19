package bph.modules.kod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lebah.template.LebahRecordTemplateModule;
import bph.entities.kod.Bandar;
import bph.entities.kod.Negeri;
import bph.utils.DataUtil;

public class BandarRecordModule extends LebahRecordTemplateModule<Bandar> {

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
	public void afterSave(Bandar r) {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
	}

	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);

		context.remove("selectNegeri");

		List<Negeri> negeriList = dataUtil.getListNegeri();
		context.put("selectNegeri", negeriList);

		context.put("path", getPath());
	}

	@Override
	public boolean delete(Bandar r) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/kod/bandar";
	}

	@Override
	public Class<Bandar> getPersistenceClass() {
		// TODO Auto-generated method stub
		return Bandar.class;
	}

	@Override
	public void getRelatedData(Bandar r) {

	}

	@Override
	public void save(Bandar r) throws Exception {
		r.setId(get("id"));
		r.setKeterangan(get("keterangan"));
		r.setNegeri(db.find(Negeri.class, get("idNegeri")));
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		String find_id = get("find_id");
		String findNegeri = get("findNegeri");
		String find_keterangan = get("find_keterangan");

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", find_id);
		map.put("negeri.id", findNegeri);
		map.put("keterangan", find_keterangan);

		return map;
	}
}
