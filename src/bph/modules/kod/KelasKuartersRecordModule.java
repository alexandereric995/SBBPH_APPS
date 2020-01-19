package bph.modules.kod;

import java.util.HashMap;
import java.util.Map;

import lebah.template.LebahRecordTemplateModule;
import bph.entities.kod.KelasKuarters;

public class KelasKuartersRecordModule extends
		LebahRecordTemplateModule<KelasKuarters> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public void afterSave(KelasKuarters r) {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
	}

	@Override
	public void begin() {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean delete(KelasKuarters r) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/kod/kelasKuarters";
	}

	@Override
	public Class<KelasKuarters> getPersistenceClass() {
		// TODO Auto-generated method stub
		return KelasKuarters.class;
	}

	@Override
	public void getRelatedData(KelasKuarters r) {
		// TODO Auto-generated method stub
	}

	@Override
	public void save(KelasKuarters r) throws Exception {
		r.setId(get("id"));
		r.setGredMula(get("gredMula"));
		r.setGredAkhir(get("gredAkhir"));
		r.setKadarSewa(getParamAsDouble("kadarSewa"));
		r.setDeposit(getParamAsDouble("deposit"));
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		String find_id = get("find_id");

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", find_id);

		return map;
	}
}
