package bph.modules.kewangan.subsidiari.agihan;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorDateBetween;
import portal.module.entity.Users;
import bph.entities.kewangan.KewSubsidiari;
import bph.entities.kewangan.KewSubsidiariAgihan;
import bph.entities.kod.Status;
import bph.utils.DataUtil;
import bph.utils.Util;
import bph.utils.UtilKewangan;

public class AgihanSubsidiari extends LebahRecordTemplateModule<KewSubsidiari> {
	
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
		
		//this.addFilter("status.id IN ('1436510785697','1436510785718') ");
		this.addFilter("status.id IN ('1436510785697','1436510785718','1436510785721','1436510785725','1436510785729','1438023402951') ");
		
		context.put("listPenyedia", dataUtil.getListPenyediaSubsidiari());
		context.put("user", user);
		context.put("path", getPath());
		context.put("util", new Util());
		context.put("userRole",userRole);
		context.put("command", command);
		context.put("uploadDir", ResourceBundle.getBundle("dbconnection").getString("folder"));
		context.put("findStatusPermohonan", dataUtil.getListStatusSubsidari());
		context.put("utilKewangan", new UtilKewangan());
		
	}
	
	public void filtering(){
		
	}
	
	public void disabledButton(){
		this.setDisableAddNewRecordButton(true);
		this.setDisableSaveAddNewButton(true);
		this.setDisableKosongkanUpperButton(true);
		this.setHideDeleteButton(true);
	}

	@Override
	public boolean delete(KewSubsidiari r) throws Exception {
		return false;
	}

	@Override
	public String getPath() { return "bph/modules/kewangan/subsidiari/agihan/kuarters"; }

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

	}
	
	@Override
	public void save(KewSubsidiari r) throws Exception {
		
		Users user = db.find(Users.class, userId);
		
		KewSubsidiariAgihan ag = r.getAgihan();
		if(ag == null){
			ag = new KewSubsidiariAgihan();
		}
		
		ag.setSubsidiari(r);
		ag.setPenyedia(db.find(Users.class, getParam("penyedia")));
		ag.setPenyelia(user);
		ag.setTarikhAgihan(getDate("tarikhAgihan"));
		ag.setCatatanPenyelia(getParam("catatanPenyelia"));
		db.persist(ag);
		
		r.setAgihan(ag);
		r.setStatus(db.find(Status.class, "1436510785718")); //PERMOHONAN TELAH DISAHKAN
		
	}
	
	@Override
	public void afterSave(KewSubsidiari r) { 

	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pemohon.userName", getParam("findPemohon"));
		map.put("pemohon.noKP", getParam("findNoKP"));
		map.put("status.id", getParam("findStatusPermohonan"));
		map.put("tarikhPermohonan", new OperatorDateBetween(
				getDate("findTarikhPermohonan"),
				getDate("findTarikhPermohonanHingga")));
		return map;
	}
}
