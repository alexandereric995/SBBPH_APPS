package bph.modules.integrasi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorEqualTo;
import bph.entities.integrasi.IntJANMArkib;
import bph.entities.integrasi.IntJANMRekodArkib;
import bph.utils.DataUtil;
import db.persistence.MyPersistence;

public class JANMArkibRecordModule extends LebahRecordTemplateModule<IntJANMArkib> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private MyPersistence mp;
	private DataUtil dataUtil;

	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public void afterSave(IntJANMArkib r) {
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
		this.setDisableBackButton(true);
		this.setDisableDefaultButton(true);
	}
	
	private void addfilter() {
		
		this.setOrderBy("fileName");
		this.setOrderType("asc");
	
	}
	
	//TODO TO BE OVERIDE BY SUB-CLASSESS
	public void doOverideFilterRecord() {
				
	}

	@Override
	public boolean delete(IntJANMArkib r) throws Exception {
		
		return false;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/integrasi/JANMArkib";
	}

	@Override
	public Class<IntJANMArkib> getPersistenceClass() {
		// TODO Auto-generated method stub
		return IntJANMArkib.class;
	}

	@Override
	public void getRelatedData(IntJANMArkib r) {
		try {
			mp = new MyPersistence();	
			List<IntJANMRekodArkib> listRekod = mp.list("select x from IntJANMRekodArkib x where x.janm.id = '" + r.getId() + "' order by x.id asc");
			context.put("listRekod", listRekod);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
	}

	@Override
	public void save(IntJANMArkib r) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		String findFileName = get("findFileName").trim();
		String findType = get("findType").trim();
		String findDate = get("findDate").trim();
		String findAgBranchCode = get("findAgBranchCode").trim();

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("fileName", findFileName);
		map.put("type", findType);
		map.put("date", findDate);
		map.put("agBranchCode", new OperatorEqualTo(findAgBranchCode));

		return map;
	}
}
