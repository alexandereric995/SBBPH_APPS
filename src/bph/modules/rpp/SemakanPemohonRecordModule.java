package bph.modules.rpp;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import portal.module.entity.Users;
import bph.entities.rpp.RppCatatanPengguna;
import bph.entities.rpp.RppPermohonan;
import bph.entities.rpp.RppRekodTempahanLondon;
import bph.utils.DataUtil;
import bph.utils.Util;
import bph.utils.UtilRpp;
import db.persistence.MyPersistence;

public class SemakanPemohonRecordModule extends LebahRecordTemplateModule<Users> {

	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;
	private MyPersistence mp;
	
	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {return String.class;}

	@Override
	public void afterSave(Users r) {}

	@Override
	public void beforeSave() {}
	
	@Override
	public Class<Users> getPersistenceClass() {return Users.class;}
	
	@Override
	public String getPath() {return "bph/modules/rpp/semakanPemohon";}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("noKP", getParam("findNoKP"));	
		map.put("userName", getParam("findUserName"));	
		return map;
	}
	
	@Override
	public boolean delete(Users r) throws Exception {return false;}
	
	@Override
	public void begin() {

		dataUtil = DataUtil.getInstance(db);
		
		try {
			mp = new MyPersistence();
			
			String userLoginId = (String) request.getSession().getAttribute("_portal_login");
			userRole = (String) request.getSession().getAttribute("_portal_role");
			
			defaultButtonOption();
			addfilter(userId);
			
			context.remove("profilePhotoName");
			context.remove("avatarPhotoName");
			context.remove("updatecatatan");
			
			context.put("userLoginId", userLoginId);
			context.put("userRole", userRole);
			context.put("command", command);
			context.put("path", getPath());
			context.put("util", new Util());
			context.put("uploadDir", ResourceBundle.getBundle("dbconnection").getString("folder"));
			
			this.setOrderBy("userName asc");
			
		} catch (Exception e) {
			System.out.println("Error begin : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		
	}

	public void addfilter(String userId) { 
		this.addFilter("id not in ('anon','admin') ");
	}
	
	public void defaultButtonOption() {
		this.setReadonly(true);
		this.setDisableDefaultButton(true);
		this.setDisableBackButton(true);
		this.setDisableKosongkanUpperButton(true);
		this.setDisableSaveAddNewButton(true);
		this.setDisabledInfoNextTab(true);
		this.setDisableUpperBackButton(true);
	}

	@Override
	public void getRelatedData(Users r) {

		try {
			mp = new MyPersistence();
			
			Users rr = (Users) mp.find(Users.class, r.getId());
			
			//PROFILE PHOTO
			if (rr != null && rr.getProfilePhoto() != "") {
				String dir = ResourceBundle.getBundle("dbconnection").getString("folder");
				String urlSource = dir + rr.getAvatar();
				File source = new File(urlSource);
				if (source.exists()) {
					context.put("profilePhotoName", rr.getProfilePhoto());
					context.put("avatarPhotoName", rr.getAvatar());
				}else{
					context.put("profilePhotoName", "profilePhoto/no_photo.jpg");
					context.put("avatarPhotoName", "profilePhoto/no_photo_avatar.jpg");
				}
			} else {
				context.put("profilePhotoName", "profilePhoto/no_photo.jpg");
				context.put("avatarPhotoName", "profilePhoto/no_photo_avatar.jpg");
			}
			
			context.put("listRpp", getListRpp(mp,rr));
			context.put("listLondon", getListLondon(mp,rr));
			context.put("listCatatan", getListCatatanPengguna(mp,rr));
			context.put("blacklisted",UtilRpp.checkingBlacklist(mp,rr.getId()));
			
		} catch (Exception e) {
			System.out.println("Error getRelatedData : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		
	}
	
	@Override
	public void save(Users r) throws Exception { }
	
	@Command("saveCatatanHqPengguna")
	public String saveCatatanHqPengguna() {
		
		try {
			mp = new MyPersistence();
			
			String userLoginId = (String) request.getSession().getAttribute("_portal_login");
			Users userlogin = (Users) mp.find(Users.class, userLoginId);
			
			String idpengguna = getParam("id");
			String catatan = getParam("rppCatatanPengguna");
			Users r = (Users) mp.find(Users.class, idpengguna);
			
			mp.begin();
			
			RppCatatanPengguna cp = new RppCatatanPengguna();
			cp.setPengguna(r);
			cp.setCatatan(catatan);
			cp.setTarikhDaftar(new Date());
			cp.setUserDaftar(userlogin);
			mp.persist(cp);
			
			mp.commit();
			
			context.put("listCatatan", getListCatatanPengguna(mp,r));
			context.put("r",r);
			
		} catch (Exception e) {
			System.out.println("Error saveCatatanHqPengguna : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		context.put("updatecatatan","Y");
		return getPath() + "/catatanHqPengguna.vm";
	}
	
	@Command("openPopupCatatanHQ")
	public String openPopupCatatanHQ() {
		
		try {
			mp = new MyPersistence();
			
			String idprmhn = getParam("idprmhn");
			RppPermohonan rp = (RppPermohonan) mp.find(RppPermohonan.class, idprmhn);
			context.put("rp",rp);
			
		} catch (Exception e) {
			System.out.println("Error openPopupCatatanHQ : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/popup/popupCatatanHQ.vm";
	}
	
	@Command("openPopupCatatanLondon")
	public String openPopupCatatanLondon() {
		
		try {
			mp = new MyPersistence();
			
			String idLondon = getParam("idLondon");
			RppRekodTempahanLondon ldn = (RppRekodTempahanLondon) mp.find(RppRekodTempahanLondon.class, idLondon);
			context.put("ldn",ldn);
			
		} catch (Exception e) {
			System.out.println("Error openPopupCatatanLondon : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/popup/popupCatatanLondon.vm";
	}
	
	@Command("openPopupListCatatanPengguna")
	public String openPopupListCatatanPengguna(){
		
		try {
			mp = new MyPersistence();
			
			String idpengguna = getParam("id");
			Users r = (Users) mp.find(Users.class, idpengguna);
			context.put("listCatatan", getListCatatanPengguna(mp,r));
			
		} catch (Exception e) {
			System.out.println("Error openPopupListCatatanPengguna : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/popup/popupListCatatanPengguna.vm";
	}
	
	@SuppressWarnings("unchecked")
	public List<RppCatatanPengguna> getListCatatanPengguna(MyPersistence mp,Users r) {
		List<RppCatatanPengguna> listCatatan = mp.list("select x from RppCatatanPengguna x where x.pengguna.id = '"+r.getId()+"' ");
		return listCatatan;
	}
	
	@SuppressWarnings("unchecked")
	public List<RppRekodTempahanLondon> getListLondon(MyPersistence mp,Users r) {
		List<RppRekodTempahanLondon> listLondon = mp.list("select x from RppRekodTempahanLondon x where x.pemohon.id = '"+r.getId()+"' ");
		return listLondon;
	}
	
	@SuppressWarnings("unchecked")
	public List<RppPermohonan> getListRpp(MyPersistence mp,Users r) {
		List<RppPermohonan> listRpp = mp.list("select x from RppPermohonan x where x.pemohon.id = '"+r.getId()+"' ");
		return listRpp;
	}
	
}

