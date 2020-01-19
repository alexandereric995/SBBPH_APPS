package bph.modules.kod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lebah.template.LebahRecordTemplateModule;
import bph.entities.kod.Seksyen;
import bph.entities.kod.Status;
import bph.utils.DataUtil;

public class StatusRecordModule extends LebahRecordTemplateModule<Status> {

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
	public void afterSave(Status r) {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
	}

	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);

		context.remove("selectSeksyen");

		List<Seksyen> seksyenList = dataUtil.getListSeksyen();
		context.put("selectSeksyen", seksyenList);
		
		this.setOrderBy("seksyen.id");
		this.setOrderBy("turutan");
		this.setOrderType("asc");

		context.put("path", getPath());
	}

	@Override
	public boolean delete(Status r) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/kod/status";
	}

	@Override
	public Class<Status> getPersistenceClass() {
		// TODO Auto-generated method stub
		return Status.class;
	}

	@Override
	public void getRelatedData(Status r) {

	}

	@Override
	public void save(Status r) throws Exception {

		r.setKeterangan(get("keterangan"));
		r.setTurutan(getParamAsInteger("turutan"));
		r.setSeksyen(db.find(Seksyen.class, get("idSeksyen")));
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		String findSeksyen = get("findSeksyen");
		String find_keterangan = get("find_keterangan");

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("seksyen.id", findSeksyen);
		map.put("keterangan", find_keterangan);

		return map;
	}
}
