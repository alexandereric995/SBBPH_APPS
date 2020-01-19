package bph.modules.kewangan.subsidiari.lejar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import lebah.template.LebahRecordTemplateModule;
import portal.module.entity.Users;
import bph.entities.kewangan.KewInvois;
import bph.entities.qtr.KuaPenghuni;
import bph.utils.DataUtil;
import bph.utils.Util;

public class LejarIndividu extends LebahRecordTemplateModule<KuaPenghuni> {
	
	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;
	
	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() { return String.class; }

	@Override
	public void begin() { 
		dataUtil = DataUtil.getInstance(db);
		userId = (String) request.getSession().getAttribute("_portal_login");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		
		Users objUser = db.find(Users.class, userId);
		context.put("objUser", objUser);
		
		disabledButton();
		filtering(userId);
		
		context.put("path", getPath());
		context.put("util", new Util());
		context.put("userRole",userRole);
		context.put("command", command);
		context.put("uploadDir", ResourceBundle.getBundle("dbconnection").getString("folder"));
	}
	
	public void filtering(String userId){
		this.addFilter("tarikhMasukKuarters is not null ");
		/**
		 * TODO
		 * TEMPORARY FILTER
		 * PERMOHONAN TAK LINK DNGAN TABLE YANG BETUL
		 */
		//this.addFilter("pemohon.id in (select distinct y.pembayar.id from KuaAkaun y where y.kodHasil.id = '92401') ");
	}
	
	public void disabledButton(){
		this.setHideDeleteButton(true);
		this.setDisableBackButton(true);
		this.setDisableDefaultButton(true);
		this.setDisableAddNewRecordButton(true);
		this.setDisableSaveAddNewButton(true);
		this.setDisableKosongkanUpperButton(true);
		this.setReadonly(true);
	}

	@Override
	public boolean delete(KuaPenghuni r) throws Exception {
		return false;
	}

	@Override
	public String getPath() { return "bph/modules/kewangan/subsidiari/lejar/kuarters"; }

	@Override
	public Class<KuaPenghuni> getPersistenceClass() {
		return KuaPenghuni.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void getRelatedData(KuaPenghuni r) {
		List<KewInvois> listInvois = 
				db.list("select x from KewInvois x where x.pembayar.id = '"+r.getPemohon().getId()+"' "+
						" and x.jenisBayaran.id = '01' and x.kodHasil.id = '92401' ");
		context.put("listInvois",listInvois);
	}

	@Override
	public void beforeSave() { 

	}
	
	@Override
	public void save(KuaPenghuni r) throws Exception {

	}
	
	@Override
	public void afterSave(KuaPenghuni r) { 

	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pemohon.userName", getParam("findUserName"));
		map.put("pemohon.noKP", getParam("findNoKP"));
		return map;
	}
}
