package bph.modules.kewangan.deposit.lejar.individu;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorDateBetween;
import portal.module.entity.Users;
import bph.entities.kewangan.KewDeposit;
import bph.utils.DataUtil;
import bph.utils.Util;

public class LejarIndividu extends LebahRecordTemplateModule<KewDeposit> {
	
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
		
		Users user = db.find(Users.class, userId);
		
		disabledButton();
		filtering();
		
		context.put("user", user);
		context.put("path", getPath());
		context.put("util", new Util());
		context.put("userRole",userRole);
		context.put("command", command);
		context.put("uploadDir", ResourceBundle.getBundle("dbconnection").getString("folder"));
	}
	
	public void filtering(){
		//Bergantung pada class extend
	}
	
	public void disabledButton(){
		this.setDisableKosongkanUpperButton(true);
		this.setReadonly(true);
	}

	@Override
	public boolean delete(KewDeposit r) throws Exception {
		return false;
	}

	@Override
	public String getPath() { return "bph/modules/kewangan/deposit/lejar/individu/kuarters"; }

	@Override
	public Class<KewDeposit> getPersistenceClass() {
		return KewDeposit.class;
	}

	@Override
	public void getRelatedData(KewDeposit r) {

		
	}

	@Override
	public void beforeSave() { 

	}
	
	@Override
	public void save(KewDeposit r) throws Exception {

	}
	
	@Override
	public void afterSave(KewDeposit r) { 

	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		System.out.println("ceking flag ====== " + getParam("findFlagPulangDeposit"));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pendeposit.userName", getParam("findUserName"));
		map.put("pendeposit.noKP", getParam("findNoKP"));
		map.put("flagPulangDeposit", getParam("findFlagPulangDeposit"));
		map.put("tarikhBayaran", new OperatorDateBetween(getDate("findTarikhDari"), getDate("findTarikhHingga")));
		return map;
	}
}
