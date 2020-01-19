/**
	* @author nurasna
	*/

package bph.modules.kod;

import java.util.HashMap;
import java.util.Map;

import lebah.template.LebahRecordTemplateModule;
import portal.module.entity.Users;
import bph.entities.kod.KodMesin;
import bph.utils.DataUtil;

public class KodMesinRecordModule extends LebahRecordTemplateModule<KodMesin> {
	
	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;
	
	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		return String.class;
	}

	@Override
	public void afterSave(KodMesin r) {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
	}

	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);
		context.put("listJuruwang", dataUtil.getListJuruwang());
	}

	@Override
	public boolean delete(KodMesin r) throws Exception {
		return true;
	}

	@Override
	public String getPath() {
		return "bph/modules/kod/kewangan/kodMesin";
	}

	@Override
	public Class<KodMesin> getPersistenceClass() {
		return KodMesin.class;
	}

	@Override
	public void getRelatedData(KodMesin r) {

	}

	@Override
	public void save(KodMesin r) throws Exception {
		if(getParam("juruwang") != null)
			r.setPemilik(db.find(Users.class, getParam("juruwang")));
		r.setKodMesin(getParam("kodMesin"));
		r.setKodPusatTerima(getParam("kodPusatTerima"));
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("kodMesin", getParam("findKodMesin"));
		map.put("kodPusatTerima", getParam("findKodPusatTerima"));
		map.put("pemilik.id", getParam("findJuruwang"));
		return map;
	}
}
