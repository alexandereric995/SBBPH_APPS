package bph.modules.integrasi;

import java.util.HashMap;
import java.util.Map;

import lebah.template.LebahRecordTemplateModule;
import bph.entities.integrasi.FPXRecords;

public class FPXRecordModule extends LebahRecordTemplateModule<FPXRecords> {

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
	public void afterSave(FPXRecords r) {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
	}

	@Override
	public void begin() {
		defaultButtonOption();
		addfilter();
				
		//TODO IMPLEMENT BILA ADA SUBCLASS		
		doOverideFilterRecord();
	}
	
	private void defaultButtonOption() {
		this.setReadonly(true);
		this.setHideDeleteButton(true);
		this.setDisableSaveAddNewButton(true);
		this.setDisableKosongkanUpperButton(true);
		if (!"add_new_record".equals(command)){
			this.setDisableBackButton(true);
			this.setDisableDefaultButton(true);
		}		
	}
	
	private void addfilter() {
		
		this.setOrderBy("fpxTxnTime");
		this.setOrderType("desc");
	
	}
	
	//TODO TO BE OVERIDE BY SUB-CLASSESS
	public void doOverideFilterRecord() {
				
	}

	@Override
	public boolean delete(FPXRecords r) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/integrasi/FPX";
	}

	@Override
	public Class<FPXRecords> getPersistenceClass() {
		// TODO Auto-generated method stub
		return FPXRecords.class;
	}

	@Override
	public void getRelatedData(FPXRecords r) {
		// TODO Auto-generated method stub
	}

	@Override
	public void save(FPXRecords r) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		String findId = get("findId").trim();
		String findDebitAuthCode = get("findDebitAuthCode").trim();
		String findFpxTxnTime = get("findFpxTxnTime").trim();
		String findSellerOrderNo = get("findSellerOrderNo").trim();

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("id", findId);
		map.put("debitAuthCode", findDebitAuthCode);
		map.put("fpxTxnTime", findFpxTxnTime);
		map.put("sellerOrderNo", findSellerOrderNo);

		return map;
	}
}
