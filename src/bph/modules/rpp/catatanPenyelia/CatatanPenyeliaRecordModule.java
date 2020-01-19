package bph.modules.rpp.catatanPenyelia;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import portal.module.entity.Users;
import bph.entities.rpp.RppListCatatan;
import bph.entities.rpp.RppMainCatatanPenyelia;
import bph.entities.rpp.RppPenyeliaPeranginan;
import bph.entities.rpp.RppPeranginan;
import bph.utils.DataUtil;
import bph.utils.Util;

public class CatatanPenyeliaRecordModule extends LebahRecordTemplateModule<RppMainCatatanPenyelia> {

	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;
	
	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public void afterSave(RppMainCatatanPenyelia r){}

	@Override
	public void beforeSave() {}

	@Override
	public void begin() {
		
		dataUtil = DataUtil.getInstance(db);
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		userName = (String) request.getSession().getAttribute("_portal_username");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		
		String idPeranginan = "";
		if(userRole.equalsIgnoreCase("(RPP) Penyelia")){
			RppPenyeliaPeranginan rppPenyeliaPeranginan = (RppPenyeliaPeranginan) db.get("select x from RppPenyeliaPeranginan x where x.penyelia.id = '"+userId+"' and x.statusPerkhidmatan = 'Y'");
			idPeranginan = (rppPenyeliaPeranginan!=null?rppPenyeliaPeranginan.getPeranginan().getId():"");
			this.addFilter("peranginan.id = '" + idPeranginan + "'");
		}
		
		defaultButtonOption();
		
		context.remove("r");
		context.remove("idPeranginan");
		context.put("listPeranginan",dataUtil.getListPeranginanRpp());
		context.put("idPeranginan", idPeranginan);
		
		context.put("path", getPath());
		context.put("util", new Util());
		context.put("userRole",userRole);
		context.put("command", command);
		context.put("uploadDir", ResourceBundle.getBundle("dbconnection").getString("folder"));
		
	}

	private void defaultButtonOption() {
		if(userRole.equalsIgnoreCase("(RPP) Penyelia")){
			this.setReadonly(false);
		}else{
			this.setReadonly(true);
		}
		this.setDisableDefaultButton(true);
		this.setDisableBackButton(true);
		this.setDisableKosongkanUpperButton(true);
		this.setDisableSaveAddNewButton(true);
		this.setDisabledInfoNextTab(true);
		this.setDisableUpperBackButton(true);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean delete(RppMainCatatanPenyelia r) throws Exception {
		
		List<RppListCatatan> listCatatan = db.list("select x from RppListCatatan x where x.mainCatatanPenyelia.id = '"+r.getId()+"' ");
		db.begin();
		for(int i=0;i<listCatatan.size();i++){
			db.remove(listCatatan.get(i));
		}
		db.commit();
		
		return true;
	}

	@Override
	public String getPath() {
		return "bph/modules/rpp/catatanPenyelia";
	}

	@Override
	public Class<RppMainCatatanPenyelia> getPersistenceClass() {
		return RppMainCatatanPenyelia.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void getRelatedData(RppMainCatatanPenyelia r) {
		List<RppListCatatan> listCatatan = 
				db.list("select x from RppListCatatan x where x.mainCatatanPenyelia.id = '"+r.getId()+"' order by x.id asc ");
		context.put("listCatatan", listCatatan);
	}

	@Override
	public void save(RppMainCatatanPenyelia r) throws Exception {
		
		userRole = (String) request.getSession().getAttribute("_portal_role");
		userId = (String) request.getSession().getAttribute("_portal_login");
		
		if(userRole.equalsIgnoreCase("(RPP) Penyelia")){
			r.setPeranginan(db.find(RppPeranginan.class, getParam("peranginanId")));
		}
		
		if(r.getPendaftar()==null){
			r.setPendaftar(db.find(Users.class, userId));
			r.setTarikhCatatan(new Date());
		}
		
		r.setTajuk(getParam("tajuk"));
		r.setCatatan(getParam("catatan"));

	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("peranginan.id", getParam("findPeranginan"));
		map.put("tarikhCatatan", getDate("findTarikhCatatan"));
		map.put("tajuk", getParam("findTajuk"));
		return map;
	}
	
	@SuppressWarnings("unchecked")
	@Command("saveCatatanBalas")
	public String saveCatatanBalas() throws Exception {

		userId = (String) request.getSession().getAttribute("_portal_login");
		RppMainCatatanPenyelia r = db.find(RppMainCatatanPenyelia.class, getParam("idMainCatatan"));
				
		RppListCatatan c = new RppListCatatan();
		db.begin();
		c.setCatatan(getParam("catatanBalas"));
		c.setMainCatatanPenyelia(r);
		c.setPengguna(db.find(Users.class, userId));
		c.setTarikhCatatan(new Date());
		db.persist(c);
		
		try{
			db.commit();
			List<RppListCatatan> listCatatan = 
					db.list("select x from RppListCatatan x where x.mainCatatanPenyelia.id = '"+r.getId()+"' order by x.id asc ");
			context.put("listCatatan", listCatatan);
			context.put("r",r);
		}catch(Exception ex){
			System.out.println("error saveCatatanBalas : "+ex.getMessage());
			ex.printStackTrace();
		}
		return getPath() + "/senaraiUlasan.vm";
	}
	
}






