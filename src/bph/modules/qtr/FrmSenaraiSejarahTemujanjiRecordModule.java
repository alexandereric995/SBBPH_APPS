package bph.modules.qtr;

import java.util.HashMap;
import java.util.Map;

import lebah.template.LebahRecordTemplateModule;
import bph.entities.qtr.KuaTemujanjiHistory;
import bph.utils.DataUtil;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class FrmSenaraiSejarahTemujanjiRecordModule extends LebahRecordTemplateModule<KuaTemujanjiHistory> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;
	private Util util = new Util();
	private MyPersistence mp;	

	@Override
	public Class getIdType() {
		return String.class;
	}

	@Override
	public Class<KuaTemujanjiHistory> getPersistenceClass() {
		return KuaTemujanjiHistory.class;
	}

	@Override
	public String getPath() {
		return "bph/modules/qtr/temujanji/senaraiTemujanji";
	}

	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		userName = (String) request.getSession().getAttribute("_portal_username");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		context.put("userRole", userRole);
		
		context.put("selectNegeri", dataUtil.getListNegeri());
		context.put("selectSebabBertukar", dataUtil.getListSebabBertukar());
		context.put("selectKementerian", dataUtil.getListKementerian());
		context.put("selectPetugas", dataUtil.getListPenyediaKuarters());
		
		context.put("util", util);
		context.put("flagSejarahTemujanji", "Y");

		defaultButtonOption();
		addfilter();
		//TODO IMPLEMENT BILA ADA SUBCLASS		
		doOverideFilterRecord();
	}
	
	//TODO TO BE OVERIDE BY SUB-CLASSESS
	public void doOverideFilterRecord() {
					
	}
	
	private void defaultButtonOption() {
		this.setReadonly(true);
		this.setDisableBackButton(true);
	}

	private void addfilter() {
		this.addFilter("flagInternal != 1");
		this.setOrderBy("tarikhTemujanji");
		this.setOrderType("desc");		
	}
	
	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
	}

	@Override
	public void save(KuaTemujanjiHistory r) throws Exception {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void afterSave(KuaTemujanjiHistory r) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void getRelatedData(KuaTemujanjiHistory r) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean delete(KuaTemujanjiHistory r) throws Exception {
		return false;
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		HashMap<String, Object> r = new HashMap<String, Object>();
		r.put("pemohon.id", getParam("findNoPengenalan"));
		r.put("tarikhMohonTemujanji", getDate("findTarikhMohonTemujanji"));
		r.put("tarikhTemujanji", getDate("findTarikhTemujanji"));
		return r;
	}
}
