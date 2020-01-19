package bph.modules.qtr;

import java.util.Map;

import lebah.template.LebahRecordTemplateModule;
import bph.entities.qtr.KuaLog;

public class FrmKuaLog extends LebahRecordTemplateModule<KuaLog> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3700707236805725971L;


	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public Class<KuaLog> getPersistenceClass() {
		// TODO Auto-generated method stub
		return KuaLog.class;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/qtr/log";
	}

	@Override
	public void begin() {
		// TODO Auto-generated method stub

	}

	@Override
	public void save(KuaLog r) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean delete(KuaLog r) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterSave(KuaLog r) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getRelatedData(KuaLog r) {
		// TODO Auto-generated method stub

	}

}
