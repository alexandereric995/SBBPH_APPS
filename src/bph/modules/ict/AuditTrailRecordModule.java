package bph.modules.ict;

import java.util.HashMap;
import java.util.Map;

import lebah.template.LebahRecordTemplateModule;
import bph.entities.ict.AuditTrailBaru;

public class AuditTrailRecordModule extends LebahRecordTemplateModule<AuditTrailBaru> {

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
	public void afterSave(AuditTrailBaru arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void begin() {
		// TODO Auto-generated method stub
		//setRecordOnly(true);
		setHideDeleteButton(true);
		this.setDisableInfoPaparLink(true);
		setDisableDefaultButton(true);
		setDisableUpperBackButton(true);
		setDisableSaveAddNewButton(true);
		setDisableAddNewRecordButton(true);
		setOrderBy("masaAktiviti DESC");
	}

	@Override
	public boolean delete(AuditTrailBaru arg0) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/audit";
	}

	@Override
	public Class<AuditTrailBaru> getPersistenceClass() {
		// TODO Auto-generated method stub
		return AuditTrailBaru.class;
	}

	@Override
	public void getRelatedData(AuditTrailBaru arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void save(AuditTrailBaru customer) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userID", getParam("findNoPengenalan").trim());
//		map.put("tajuk", getParam("findTajuk").trim());
		return map;
	}
}
