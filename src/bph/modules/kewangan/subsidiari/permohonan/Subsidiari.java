package bph.modules.kewangan.subsidiari.permohonan;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import lebah.template.LebahRecordTemplateModule;
import portal.module.entity.Users;
import bph.entities.kewangan.KewSubsidiari;
import bph.utils.DataUtil;
import bph.utils.Util;

public class Subsidiari extends LebahRecordTemplateModule<KewSubsidiari> {
	
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
		
		this.addFilter("pemohon.id = '" + user.getId() + "'");
		
		context.put("user", user);
		context.put("path", getPath());
		context.put("util", new Util());
		context.put("userRole",userRole);
		context.put("command", command);
		context.put("uploadDir", ResourceBundle.getBundle("dbconnection").getString("folder"));
	}
	
	public void filtering(){
		
	}
	
	public void disabledButton(){
		this.setRecordOnly(true);
		this.setDisableSaveAddNewButton(true);
		this.setDisableKosongkanUpperButton(true);
	}

	@Override
	public boolean delete(KewSubsidiari r) throws Exception {
		boolean pass = true;
		if(!r.getStatus().getId().equalsIgnoreCase("1436510785697")){
			pass = false;
		}
		return pass;
	}

	@Override
	public String getPath() { return "bph/modules/kewangan/subsidiari/permohonan/kuarters"; }

	@Override
	public Class<KewSubsidiari> getPersistenceClass() {
		return KewSubsidiari.class;
	}

	@Override
	public void getRelatedData(KewSubsidiari r) {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeSave() { 
		Users user = db.find(Users.class, userId);
		db.begin();
		user.setNoAkaunBank(getParam("noAkaunBank"));
		user.setNamaBank(getParam("namaBank"));
		
		try {
			db.commit();
		} catch (Exception e) {
			System.out.println("error beforeSave : "+e.getMessage());
			e.printStackTrace();
		}
	}
	
	@Override
	public void save(KewSubsidiari r) throws Exception {

	}
	
	@Override
	public void afterSave(KewSubsidiari r) { 

	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", getParam(""));
		return map;
	}
}
