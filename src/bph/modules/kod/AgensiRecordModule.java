package bph.modules.kod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lebah.template.LebahRecordTemplateModule;
import bph.entities.kod.Agensi;
import bph.entities.kod.Kementerian;
import bph.utils.DataUtil;

public class AgensiRecordModule extends LebahRecordTemplateModule<Agensi> {

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
	public void afterSave(Agensi r) {
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
	public boolean delete(Agensi r) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/kod/agensi";
	}

	@Override
	public Class<Agensi> getPersistenceClass() {
		// TODO Auto-generated method stub
		return Agensi.class;
	}

	@Override
	public void getRelatedData(Agensi r) {

	}

	@Override
	public void save(Agensi r) throws Exception {
		r.setId(get("id"));
		r.setKeterangan(get("keterangan"));
		r.setKementerian(db.find(Kementerian.class, get("idKementerian")));
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		String find_id = get("find_id");
		String findKementerian = get("findKementerian");
		String find_keterangan = get("find_keterangan");

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", find_id);
		map.put("kementerian.id", findKementerian);
		map.put("keterangan", find_keterangan);

		return map;
	}
}
