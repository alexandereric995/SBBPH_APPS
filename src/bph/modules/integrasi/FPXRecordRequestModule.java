package bph.modules.integrasi;

import java.util.HashMap;
import java.util.Map;

import lebah.template.LebahRecordTemplateModule;
import bph.entities.integrasi.FPXRecordsRequest;

public class FPXRecordRequestModule extends LebahRecordTemplateModule<FPXRecordsRequest> {

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
	public void afterSave(FPXRecordsRequest r) {
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
		this.setDisableInfoPaparLink(true);
		this.setHideDeleteButton(true);	
	}
	
	private void addfilter() {
		this.addFilter("fpxTxnId is null");
		this.setOrderBy("id");
		this.setOrderType("asc");
	
	}
	
	//TODO TO BE OVERIDE BY SUB-CLASSESS
	public void doOverideFilterRecord() {
				
	}

	@Override
	public boolean delete(FPXRecordsRequest r) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/integrasi/FPXRecordsRequest";
	}

	@Override
	public Class<FPXRecordsRequest> getPersistenceClass() {
		// TODO Auto-generated method stub
		return FPXRecordsRequest.class;
	}

	@Override
	public void getRelatedData(FPXRecordsRequest r) {
		// TODO Auto-generated method stub
	}

	@Override
	public void save(FPXRecordsRequest r) throws Exception {
		//TODO TO BE OVERIDE BY SUB-CLASSESS
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {

		String findSellerOrderNo = get("findSellerOrderNo").trim();
		String findSellerExOrderNo = get("findSellerExOrderNo").trim();
		String findFlagModul = get("findFlagModul").trim();		

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("sellerOrderNo", findSellerOrderNo);
		map.put("sellerExOrderNo", findSellerExOrderNo);
		map.put("flagModul", findFlagModul);

		return map;
	}
}
