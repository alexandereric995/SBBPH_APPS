package bph.modules.integrasi;

import java.util.HashMap;
import java.util.Map;

import lebah.template.LebahRecordTemplateModule;
import bph.entities.integrasi.MIGSRecords;

public class MIGSRecordModule extends LebahRecordTemplateModule<MIGSRecords> {

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
	public void afterSave(MIGSRecords r) {
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
		
		this.setOrderBy("createdDate");
		this.setOrderType("desc");
	
	}
	
	//TODO TO BE OVERIDE BY SUB-CLASSESS
	public void doOverideFilterRecord() {
				
	}

	@Override
	public boolean delete(MIGSRecords r) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/integrasi/MIGS";
	}

	@Override
	public Class<MIGSRecords> getPersistenceClass() {
		// TODO Auto-generated method stub
		return MIGSRecords.class;
	}

	@Override
	public void getRelatedData(MIGSRecords r) {
		// TODO Auto-generated method stub
	}

	@Override
	public void save(MIGSRecords r) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		String findVpcTransactionNo = get("findVpcTransactionNo").trim();		
		String findVpcMerchTxnRef = get("findVpcMerchTxnRef").trim();
		String findIdPermohonan= get("findIdPermohonan").trim();
		String findVpcMessage = get("findVpcMessage").trim();
		String findVpcBatchNo = get("findVpcBatchNo").trim();		

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("vpcTransactionNo", findVpcTransactionNo);
		map.put("vpcMerchTxnRef", findVpcMerchTxnRef);
		map.put("idPermohonan", findIdPermohonan);
		map.put("vpcMessage", findVpcMessage);
		map.put("vpcBatchNo", findVpcBatchNo);		

		return map;
	}
}
