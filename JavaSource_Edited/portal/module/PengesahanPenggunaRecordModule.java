package portal.module;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;
import java.util.ResourceBundle;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import portal.module.entity.Users;
import bph.entities.kod.GredPerkhidmatan;
import bph.mail.mailer.DaftarAkaunBaruMailer;
import bph.utils.DataUtil;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class PengesahanPenggunaRecordModule extends LebahRecordTemplateModule<Users> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Util util = new Util();
	private DataUtil dataUtil;
	private MyPersistence mp;

	@Override
	public Class getIdType() {
		return String.class;
	}
	
	@Override
	public String getPath() {
		return "vtl/pengesahanPengguna/";
	}
	
	@Override
	public Class<Users> getPersistenceClass() {
		return Users.class;
	}
	
	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Hashtable<String, Object> map = new Hashtable<String, Object>();

		map.put("id", get("findLogPengguna").trim());
		map.put("userName", get("findNamaPengguna").trim());
				
		return map;
	}
	
	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		userName = (String) request.getSession().getAttribute("_portal_username");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		context.put("userRole", userRole);
		
		context.put("selectRole", dataUtil.getListRole());
		
		context.put("selectGredPerkhidmatan", dataUtil.getListGredPerkhidmatan()); //add byzul
		
		defaultButtonOption();
		addfilter();
		//TODO IMPLEMENT BILA ADA SUBCLASS		
		doOverideFilterRecord();
	}
	
	//TODO TO BE OVERIDE BY SUB-CLASSESS
	public void doOverideFilterRecord() {
			
	}

	private void defaultButtonOption() {
		
		this.setReadonly(true);
		this.setDisableDefaultButton(true);
		this.setDisableBackButton(true);
	}
		
	private void addfilter() {
		
		this.addFilter("flagDaftarSBBPH = 'Y'");
		this.addFilter("flagAktif != 'Y'");
		this.addFilter("dokumenSokongan != null");
		this.addFilter("flagMenungguPengesahan = 'Y'");
		this.addFilter("role.name in ('(AWAM) Badan Berkanun', '(AWAM) Polis / Tentera', '(AWAM) Pesara Polis / Tentera')");
		
		this.setOrderBy("dateRegistered");
		this.setOrderType("asc");
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void save(Users users) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void afterSave(Users users) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void getRelatedData(Users users) {
		try {
			mp = new MyPersistence();
			defaultValue(mp, users.getId());	
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.remove("updateStatusPengesahan");
		context.remove("updateNotaPengesahan");		
		context.put("selectedTab", 1);		
	}

	@Override
	public boolean delete(Users users) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}
	
	private void defaultValue(MyPersistence mp, String idPengguna) {
		dataUtil = DataUtil.getInstance(db);	
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		userName = (String) request.getSession().getAttribute("_portal_username");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		context.put("userId", userId);
		context.put("userName", userName);
		context.put("userRole", userRole);
		
		Users pengguna = (Users) mp.find(Users.class, idPengguna);
		context.put("pengguna", pengguna);
				
		//PROFILE PHOTO
		if (pengguna != null && pengguna.getProfilePhoto() != "") {
			String dir = ResourceBundle.getBundle("dbconnection").getString("folder");
			String urlSource = dir + pengguna.getAvatar();
			File source = new File(urlSource);
			if (source.exists()) {
				context.put("profilePhotoName", pengguna.getProfilePhoto());
				context.put("avatarPhotoName", pengguna.getAvatar());
			} else {
				context.put("profilePhotoName", "profilePhoto/no_photo.jpg");
				context.put("avatarPhotoName", "profilePhoto/no_photo_avatar.jpg");
			}
		} else {
			context.put("profilePhotoName", "profilePhoto/no_photo.jpg");
			context.put("avatarPhotoName", "profilePhoto/no_photo_avatar.jpg");
		}
		context.put("command", command);		
	}
	
	/** START SENARAI TAB **/
	@Command("getMaklumatProfil")
	public String getMaklumatProfil() {

		try {
			mp = new MyPersistence();
			defaultValue(mp, getParam("idPengguna"));	
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.remove("updateStatusPengesahan");
		context.remove("updateNotaPengesahan");		
		context.put("selectedTab", "1");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getMaklumatPerjawatan")
	public String getMaklumatPerjawatan() {

		try {
			mp = new MyPersistence();
			defaultValue(mp, getParam("idPengguna"));	
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}

		context.put("selectedTab", "2");
		return getPath() + "/entry_page.vm";
	}
	/** END SENARAI TAB **/
	
	/** START PENGESAHAN **/
	@Command("sahPengguna")
	public String sahPengguna() {
		context.remove("updateStatusPengesahan");
		context.remove("updateNotaPengesahan");		
		return getMaklumatPerjawatan();
	}
	
	@Command("tolakPengguna")
	public String tolakPengguna() {
		context.remove("updateStatusPengesahan");
		context.remove("updateNotaPengesahan");		
		return getMaklumatPerjawatan();
	}
	
	@Command("batalPengesahan")
	public String batalPengesahan() {
		context.remove("updateStatusPengesahan");
		context.remove("updateNotaPengesahan");		
		return getMaklumatPerjawatan();
	}
	
	@Command("simpanPengesahan")
	public String simpanPengesahan() {
		boolean updateStatusPengesahan = false;
		try {
			mp = new MyPersistence();
			Users pengguna = (Users) mp.find(Users.class, getParam("idPengguna"));
			
			GredPerkhidmatan gredPerkhidmatan = null;
			if (!"".equals(getParam("idGredPerkhidmatan"))) {
				gredPerkhidmatan = (GredPerkhidmatan) mp.find(GredPerkhidmatan.class, getParam("idGredPerkhidmatan"));
			}
			
			if (pengguna != null) {
				mp.begin();
				pengguna.setCatatanPengesahan(getParam("catatanPengesahan"));
				pengguna.setNotaPengesahan(getParam("notaPengesahan"));
				pengguna.setFlagMenungguPengesahan("T");
				pengguna.setFlagAktif("Y");
				pengguna.setTarikhPengesahan(new Date());
				
				Calendar expiryDate = Calendar.getInstance();
				expiryDate.setTime(new Date());
				expiryDate.add(Calendar.YEAR, 1);
				pengguna.setTarikhLuputPengesahan(expiryDate.getTime());
				pengguna.setPengesah(db.find(Users.class, request.getSession().getAttribute("_portal_login")));				
				pengguna.setGredPerkhidmatan(gredPerkhidmatan);
				mp.commit();
				updateStatusPengesahan = true;
				
				// GENERATE EMEL
				generateEmel(pengguna, pengguna.getEmel(), "Y");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("updateStatusPengesahan", updateStatusPengesahan);
		context.remove("updateNotaPengesahan");
		return getMaklumatPerjawatan();
	}
	
	@Command("tolakPengesahan")
	public String tolakPengesahan() {
		boolean updateStatusPengesahan = false;
		try {
			mp = new MyPersistence();
			Users pengguna = (Users) mp.find(Users.class, getParam("idPengguna"));
			if (pengguna != null) {
				mp.begin();
				pengguna.setCatatanPengesahan(getParam("catatanPengesahan"));
				pengguna.setNotaPengesahan(getParam("notaPengesahan"));
				pengguna.setFlagMenungguPengesahan("T");
				pengguna.setPengesah(db.find(Users.class, request.getSession().getAttribute("_portal_login")));
				mp.commit();
				updateStatusPengesahan = true;
				
				// GENERATE EMEL
				generateEmel(pengguna, pengguna.getEmel(), "T");				
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("updateStatusPengesahan", updateStatusPengesahan);
		context.remove("updateNotaPengesahan");
		return getMaklumatPerjawatan();
	}
	
	private void generateEmel(Users pengguna, String emel, String flagKeputusan) {
		if (flagKeputusan.equals("Y")) {
			DaftarAkaunBaruMailer.get().kelulusanPengesahanPengguna(pengguna, emel);
		} else if (flagKeputusan.equals("T")) {
			DaftarAkaunBaruMailer.get().penolakanPengesahanPengguna(pengguna, emel);
		}		
	}
	/** END PENGESAHAN **/
	
	@Command("kemaskiniNotaPengesahan")
	public String kemaskiniNotaPengesahan() {
		boolean updateNotaPengesahan = false;
		try {
			mp = new MyPersistence();
			Users pengguna = (Users) mp.find(Users.class, getParam("idPengguna"));
			if (pengguna != null) {
				mp.begin();
				pengguna.setNotaPengesahan(getParam("notaPengesahan"));
				mp.commit();
				updateNotaPengesahan = true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("updateNotaPengesahan", updateNotaPengesahan);
		context.remove("updateStatusPengesahan");
		return getPath() + "/status.vm";
	}
}