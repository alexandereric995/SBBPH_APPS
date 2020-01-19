package bph.modules.rpp;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import portal.module.entity.Users;
import bph.entities.rpp.RppMaklumBalas;
import bph.utils.DataUtil;
import bph.utils.Util;

public class SenaraiMaklumBalasRecordModule extends LebahRecordTemplateModule<RppMaklumBalas> {

	/**
	 * 
	 */
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
	public void begin() {
		
		dataUtil = DataUtil.getInstance(db);
		
		context.remove("selectJenisMaklumBalas");
		context.remove("selectPemohon");
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		userName = (String) request.getSession().getAttribute("_portal_username");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		context.put("userRole", userRole);
		
//		context.put("selectJenisMaklumBalas", dataUtil.getListjenisMaklumbalasRpp());
		context.put("selectPemohon", dataUtil.getListPemohonRpp());
		
		//defaultButtonOption();
		//addfilter();
		
		Users users = db.find(Users.class, userId);
		context.put("users", users);
		
		context.put("command", command);
		context.put("util", new Util());
		context.put("path", getPath());
		context.put("uploadDir", ResourceBundle.getBundle("dbconnection").getString("folder"));
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/rpp/senaraiMaklumBalas";
	}

	@Override
	public Class<RppMaklumBalas> getPersistenceClass() {
		// TODO Auto-generated method stub
		return RppMaklumBalas.class;
	}
	
	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		String findNoMaklumBalas = get("findNoMaklumBalas");
		//String findIdPermohonan = get("findIdPermohonan");
		String findJenisMaklumBalas = get("findJenisMaklumBalas");
		String findIdPemohon = get("findIdPemohon");
		String findNamaPemohon = get("findNamaPemohon");


		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", findNoMaklumBalas);
		//map.put("permohonan.id", findIdPermohonan);
		map.put("jenisMaklumBalas.id", findJenisMaklumBalas);
		map.put("pemohon.id", findIdPemohon);
		map.put("pemohon.userName", findNamaPemohon);
		

		return map;
	}
	
//	private void addfilter() {
//		if(userRole.equalsIgnoreCase("(RPP) Penyelia")){
//			RppPenyeliaPeranginan rppPenyeliaPeranginan = (RppPenyeliaPeranginan) db.get("select x from RppPenyeliaPeranginan x where x.penyelia.id = '"+userId+"' and x.statusPerkhidmatan = 'Y'");
//			String idPeranginan = (rppPenyeliaPeranginan!=null?rppPenyeliaPeranginan.getPeranginan().getId():"");
//			this.addFilter("permohonan.rppPeranginan.id = '" + idPeranginan + "'");
//		}else if(userRole.equalsIgnoreCase("(AWAM) Penjawat Awam")){
//			this.addFilter("pemohon.id = '" + userId + "'");
//		}
//	}
	
//	private void defaultButtonOption() {
//		
//		if ("(RPP) Penyelia".equals(userRole)){
//			this.setDisableBackButton(true);
//		}
//		/*
//		this.setDisableSaveAddNewButton(true);
//		if (!"add_new_record".equals(command)){
//			this.setDisableBackButton(true);
//			this.setDisableDefaultButton(true);
//		}*/		
//	}

	@Override
	public boolean delete(RppMaklumBalas r) throws Exception {
		if(userRole.equalsIgnoreCase("(RPP) Penyemak") || userRole.equalsIgnoreCase("(RPP) Pelulus"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public void getRelatedData(RppMaklumBalas r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save(RppMaklumBalas r) throws Exception {
		// TODO Auto-generated method stub
		//String statusInfo = "";
		
		/*RppMaklumBalas mb = db.find(RppMaklumBalas.class, get("idMaklumBalas"));
		db.begin();*/
		
		r.setNoMaklumbalas(get("noMaklumbalas"));
//		r.setJenisMaklumBalas(db.find(JenisMaklumbalas.class, get("selectJenisMaklumBalas")));
		r.setPemohon(db.find(Users.class, get("idPemohon")));
		r.setKeterangan(get("keterangan"));
		r.setUlasanMaklumbalas(get("ulasanMaklumbalas"));
		r.setTarikhMaklumbalas(getDate("tarikhMaklumbalas"));
		r.setIdPengulas(db.find(Users.class, userId));
		r.setTarikhUlasan(new Date());
		//db.persist(mb);

		/*try {
			db.commit();
			statusInfo = "success";
			
		} catch (Exception e) {
			statusInfo = "error";
		}
		
		context.put("statusInfo", statusInfo);*/

	}
	
	/*@Command("saveMaklumBalas")
	public String saveMaklumBalas() throws Exception {
		
		
	}*/
	
	@Command("getPemohon")
	public String getPemohon() throws Exception {
		
		String namaPemohon = "";
		Users pemohon = db.find(Users.class, get("idPemohon"));
		if (pemohon != null)
			namaPemohon = pemohon.getUserName().toUpperCase();
		
		context.put("namaPemohon", namaPemohon);
		
		return getPath() + "/namaPemohon.vm";
	}
}
