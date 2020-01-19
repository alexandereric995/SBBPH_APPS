package bph.modules.rpp;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import lebah.template.LebahRecordTemplateModule;
import bph.entities.kod.Status;
import bph.entities.rpp.RppMaklumBalas;
import bph.utils.DataUtil;
import bph.utils.Util;

public class HQMaklumBalasRecordModule extends LebahRecordTemplateModule<RppMaklumBalas> {

	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;

	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public void afterSave(RppMaklumBalas r) {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/rpp/hQMaklumBalas";
	}

	@Override
	public Class<RppMaklumBalas> getPersistenceClass() {
		// TODO Auto-generated method stub
		return RppMaklumBalas.class;
	}
	
	@Override
	public void begin() {
		
		dataUtil = DataUtil.getInstance(db);
		userRole = (String) request.getSession().getAttribute("_portal_role");
//		context.put("selectJenisMaklumBalas", dataUtil.getListjenisMaklumbalasRpp());
		
		defaultButtonOption();
		
		context.put("path", getPath());
		context.put("util", new Util());
		context.put("userRole",userRole);
		context.put("command", command);
		context.put("uploadDir", ResourceBundle.getBundle("dbconnection").getString("folder"));
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		
		String findNoMaklumBalas = get("findNoMaklumBalas");
		String findJenisMaklumBalas = get("findJenisMaklumBalas");
		String findIdPemohon = get("findIdPemohon");
		//String findNamaPemohon = get("findNamaPemohon");

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("noMaklumbalas", findNoMaklumBalas);
		map.put("jenisMaklumBalas.id", findJenisMaklumBalas);
		map.put("pemohon.noKP", findIdPemohon);
		//map.put("pemohon.userName", findNamaPemohon);

		return map;
	}

	private void defaultButtonOption() {
		this.setReadonly(true);
		this.setDisableBackButton(true);
		this.setDisableSaveAddNewButton(true);
		this.setDisableDefaultButton(true);
	}

	@Override
	public boolean delete(RppMaklumBalas r) throws Exception {
		boolean val=false;
		if(userRole.equalsIgnoreCase("(RPP) Penyemak") || userRole.equalsIgnoreCase("(RPP) Pelulus"))
		{	
			val = true;		
		}
		return val;
	}

	@Override
	public void getRelatedData(RppMaklumBalas r) {
		String status = (r.getStatus()!=null?r.getStatus().getId():"");
		if(status.equalsIgnoreCase("1430024161503")){  //MAKLUMBALAS BARU
			updateStatus(r);
		}
	}
	
	private void updateStatus(RppMaklumBalas r){
		db.begin();
		r.setStatus(db.find(Status.class,"1430024161510")); //MAKLUMBALAS DITERIMA
		r.setTarikhTerima(new Date());
		try {
			db.commit();
		} catch (Exception e) {
			System.out.println("error updateStatus : "+e.getMessage());
		}
	}

	@Override
	public void save(RppMaklumBalas r) throws Exception {
		r.setUlasanHq(getParam("ulasanHq"));
		r.setTarikhUlasanHq(new Date());
	}

}
