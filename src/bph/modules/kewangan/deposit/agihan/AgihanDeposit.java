package bph.modules.kewangan.deposit.agihan;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import lebah.template.LebahRecordTemplateModule;
import portal.module.entity.Users;
import bph.entities.kew.KewTuntutanDeposit;
import bph.entities.kewangan.KewDepositAgihan;
import bph.entities.kod.Status;
import bph.utils.DataUtil;
import bph.utils.Util;

public class AgihanDeposit extends LebahRecordTemplateModule<KewTuntutanDeposit> {
	
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
		
		this.addFilter("status.id IN ('1436464445665','1436464445668') ");
		
		context.put("listStatus",dataUtil.getListStatusAgihanDeposit());
		context.put("listPeranginan",dataUtil.getListPeranginanRppHaveDeposit());
		context.put("listPenyedia", dataUtil.getListPenyediaDeposit());
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
		this.setDisableAddNewRecordButton(true);
		this.setDisableSaveAddNewButton(true);
		this.setDisableKosongkanUpperButton(true);
		this.setHideDeleteButton(true);
	}

	@Override
	public boolean delete(KewTuntutanDeposit r) throws Exception {
		return false;
	}

	@Override
	public String getPath() { return "bph/modules/kewangan/tuntutanDeposit/agihan/kuarters"; }

	@Override
	public Class<KewTuntutanDeposit> getPersistenceClass() {
		return KewTuntutanDeposit.class;
	}

	@Override
	public void getRelatedData(KewTuntutanDeposit r) {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeSave() { 

	}
	
	@Override
	public void save(KewTuntutanDeposit r) throws Exception {
		
		Users user = db.find(Users.class, userId);
		
		KewDepositAgihan ag = r.getAgihan();
		if(ag == null){
			ag = new KewDepositAgihan();
		}
		
		ag.setTuntutanDeposit(r);
		ag.setPenyedia(db.find(Users.class, getParam("penyedia")));
		ag.setPenyelia(user);
		ag.setTarikhAgihan(new Date());
		ag.setCatatanPenyelia(getParam("catatanPenyelia"));
		db.persist(ag);
		
		r.setAgihan(ag);
		r.setStatus(db.find(Status.class, "1436464445668")); //PERMOHONAN TELAH DISAHKAN
		
	}
	
	@Override
	public void afterSave(KewTuntutanDeposit r) { 

	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {

		String findPemohon = getParam("findPemohon");
		String findNoKp = getParam("findNoKp");
		Date findTarikhMohonDeposit = getDate("findTarikhMohonDeposit");
		
		String strdate = "";
		if(findTarikhMohonDeposit!=null){
			strdate = new SimpleDateFormat("yyyy-MM-dd").format(findTarikhMohonDeposit);
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("penuntut.userName", findPemohon);
		map.put("penuntut.id", findNoKp);
		map.put("tarikhPermohonan", strdate);
		
		return map;
	}
}
