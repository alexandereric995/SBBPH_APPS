package bph.modules.rpp.tetapan;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import lebah.template.LebahRecordTemplateModule;
import bph.entities.rpp.RppSebabMohonRT;
import bph.utils.DataUtil;
import bph.utils.Util;

public class RppSebabMohonRTRecordModule extends LebahRecordTemplateModule<RppSebabMohonRT>{

	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;

	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() { return String.class; }

	@Override
	public void afterSave(RppSebabMohonRT r) { }

	@Override
	public void beforeSave() { }

	@Override
	public boolean delete(RppSebabMohonRT r) throws Exception { 
		context.put("error_flag","record_delete_success" );
		return true; 
	}

	@Override
	public String getPath() { return "bph/modules/rpp/tetapan/sebabMohonRT"; }

	@Override
	public Class<RppSebabMohonRT> getPersistenceClass() { return RppSebabMohonRT.class; }
	
	
	
	@Override
	public void begin() {

		dataUtil = DataUtil.getInstance(db);
		userRole = (String) request.getSession().getAttribute("_portal_role");
		
		if(userRole.equalsIgnoreCase("(RPP) Penyedia")){
			this.setReadonly(true);
		}else{
			this.setReadonly(false);
		}
		
		this.setDisableKosongkanUpperButton(true);
		this.setDisableUpperBackButton(true);
		
		context.put("listSebabMohonRT", dataUtil.getListRppSebabMohonRT());
		context.put("userRole", userRole);
		context.put("uploadDir", ResourceBundle.getBundle("dbconnection").getString("folder"));
		context.put("command", command);
		context.put("util", new Util());
		context.put("path", getPath());
		
		this.setOrderBy("flagKeutamaan asc");
	}

	@Override
	public void getRelatedData(RppSebabMohonRT r) {
		
	}

	@Override
	public void save(RppSebabMohonRT r) throws Exception {
		r.setFlagKeutamaan(getParamAsInteger("flagKeutamaan"));
		r.setKeterangan(getParam("keterangan"));
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("keterangan", getParam("findKeterangan"));
		return map;
	}

	
}
