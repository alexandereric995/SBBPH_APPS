package bph.modules.rpp;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import lebah.db.Db;
import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import portal.module.entity.Users;
import bph.entities.kod.Status;
import bph.entities.rpp.RppSenaraiHitam;
import bph.utils.DataUtil;
import bph.utils.Util;

public class SenaraiHitamRecordModule extends LebahRecordTemplateModule<RppSenaraiHitam> {

	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;

	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public void afterSave(RppSenaraiHitam r) {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
	}

	@Override
	public void begin() {
		
		dataUtil = DataUtil.getInstance(db);
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		userName = (String) request.getSession().getAttribute("_portal_username");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		context.put("userRole", userRole);
		
		defaultButtonOption();
		
		Users users = db.find(Users.class, userId);
		context.put("users", users);
		
		//context.put("selectNama", dataUtil.getListPenjawatAwam());
		context.put("selectStatusSenaraiHitam", dataUtil.getListStatusKeputusanSenaraiHitam());
		
		context.put("command", command);
		context.put("util", new Util());
		context.put("path", getPath());
		context.put("uploadDir", ResourceBundle.getBundle("dbconnection").getString("folder"));
	}

	public void defaultButtonOption() {
		this.setReadonly(true);	
		this.setDisableDefaultButton(true);
		this.setDisableBackButton(true);
		this.setDisableKosongkanUpperButton(true);
		this.setDisableSaveAddNewButton(true);
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/rpp/senaraiHitam";
	}

	@Override
	public Class<RppSenaraiHitam> getPersistenceClass() {
		// TODO Auto-generated method stub
		return RppSenaraiHitam.class;
	}
	
	@Override
	public Map<String, Object> searchCriteria() throws Exception {

		String findIdPemohon = get("findIdPemohon");
		String findNamaPemohon = get("findNamaPemohon");
		String flag = get("flag");

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("pemohon.noKP", findIdPemohon);
		map.put("pemohon.userName", findNamaPemohon);
		map.put("flagAktif", flag);
		map.put("status.id", getParam("findStatus"));
		
		return map;
	}
	
	@Override
	public boolean delete(RppSenaraiHitam r) throws Exception {
		boolean val = false;
		if(userRole.equalsIgnoreCase("(RPP) Penyemak") || userRole.equalsIgnoreCase("(RPP) Pelulus"))
		{
			if(r.getStatus().getId().equalsIgnoreCase("1428990717386")){
				val = true;
			}
		}
		return val;
	}

	@Override
	public void getRelatedData(RppSenaraiHitam r) {
		// TODO Auto-generated method stub
		context.put("objUser","");
	}

	@Override
	public void save(RppSenaraiHitam r) throws Exception {

		String flag = "";
		String status = getParam("status");
		
		r.setTarikhMula(getDate("tarikhMula"));
		r.setTarikhTamat(getDate("tarikhTamat"));
		r.setStatus(db.find(Status.class, status));
		
		if(status.equalsIgnoreCase("1428990717389")){ //SENARAI HITAM DILULUSKAN
			flag = "Y";
		}else{
			flag = "T";
		}
		
		r.setFlagAktif(flag);
	}
	

	@Command("callPopupCarianTetamu")
	public String callPopupCarianTetamu() throws Exception {
		String carianTetamu = getParam("carianTetamu");
		List<Users> userList = searchUsers(carianTetamu);
		context.put("userList", userList);
		return getPath() + "/popup/popupCarianTetamu.vm";
	}
	
	private List<Users> searchUsers(String param) {
		ArrayList<Users> list = new ArrayList<Users>();
		Db db1 = null;
		try {
			db1 = new Db();
			String sql = "SELECT DISTINCT a.user_login FROM users a "+
						 " WHERE (upper(user_name) LIKE upper('%"+param+"%') OR upper(no_kp) LIKE upper('%"+param+"%')) ";
			
			ResultSet rs = db1.getStatement().executeQuery(sql);			
			while (rs.next()) {
				Users us = db.find(Users.class, rs.getString("user_login"));
				list.add(us);
			}	
			
		}catch(Exception e){
			System.out.println("error searchUsers : "+e.getMessage());
			e.printStackTrace();
		}finally { 
			if ( db1 != null ) db1.close();
		}
		return list;
	}
	
	@Command("savePilihanTetamu")
	public String savePilihanTetamu() throws Exception {
		String guestId = getParam("radTetamu");
		Users objUser = db.find(Users.class, guestId);
		context.put("objUser", objUser);
		return getPath() + "/maklumatTetamu.vm";
	}
	
}
