package bph.modules.kod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lebah.template.LebahRecordTemplateModule;
import bph.entities.kod.Daerah;
import bph.entities.kod.Negeri;
import bph.utils.DataUtil;

public class DaerahRecordModule extends LebahRecordTemplateModule<Daerah> {

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
	public void afterSave(Daerah r) {
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
	public boolean delete(Daerah r) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/kod/daerah";
	}

	@Override
	public Class<Daerah> getPersistenceClass() {
		// TODO Auto-generated method stub
		return Daerah.class;
	}

	@Override
	public void getRelatedData(Daerah r) {
		// TODO Auto-generated method stub
	}

	@Override
	public void save(Daerah r) throws Exception {
		r.setId(get("id"));
		r.setKeterangan(get("keterangan"));
		r.setNegeri(db.find(Negeri.class, get("idNegeri")));
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		String find_id = get("find_id");
		String find_keterangan = get("find_keterangan");
		String findNegeri = get("findNegeri");

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", find_id);
		map.put("keterangan", find_keterangan);
		map.put("negeri.id", findNegeri);

		return map;
	}
	
//	@Command("findBandar")
//	public String findBandar() throws Exception {	
//		
//		String idNegeri = "0";
//		if (get("findNegeri").trim().length() > 0)
//			idNegeri = get("findNegeri");
//		List<Bandar> list = dataUtil.getListBandar(idNegeri);
//		context.put("selectBandar", list);
//		
//		return getPath() + "/findBandar.vm";
//	}
}
