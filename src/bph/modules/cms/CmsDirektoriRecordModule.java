package bph.modules.cms;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import lebah.template.UID;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import portal.module.entity.Users;
import bph.entities.kod.Bahagian;
import bph.entities.kod.Seksyen;
import bph.entities.portal.CmsDirektori;
import bph.utils.DataUtil;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class CmsDirektoriRecordModule extends LebahRecordTemplateModule<Bahagian> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MyPersistence mp;
	private DataUtil dataUtil;
	
	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		return String.class;
	}

	@Override
	public void afterSave(Bahagian r) {
		context.put("selectedTab", 1);
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
	}

	@Override
	public void begin() {
		
		setReadonly(true);
		setDisableBackButton(true);		
	}

	@Override
	public boolean delete(Bahagian r) throws Exception {
		return false;
	}

	@Override
	public String getPath() {
		return "bph/modules/cms/direktori";
	}

	@Override
	public Class<Bahagian> getPersistenceClass() {
		return Bahagian.class;
	}

	@Override
	public void getRelatedData(Bahagian r) {
		int count = 1;
		try {			
			mp = new MyPersistence();
			List<CmsDirektori> listKetuaBahagian = mp.list("SELECT x FROM CmsDirektori x WHERE x.bahagian.id = '" + r.getId() + "' and x.seksyen is null ORDER BY x.turutan ASC");
			context.put("listKetuaBahagian", listKetuaBahagian);
			count = listKetuaBahagian.size();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		Vector turutan = new Vector();
		Hashtable h = null;
		for (int i = 1; i <= count; i++) {			
			h = new Hashtable();
			h.put("id", i);
			turutan.addElement(h);
		}	
		
		context.put("selectTurutan", turutan);		
		context.put("selectedTab", 1);
	}

	@Override
	public void save(Bahagian r) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		String findBahagian = get("findBahagian");

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("keterangan", findBahagian);
		return map;
	}
	
	
	/******************************************* START TAB ********************************************/
	@Command("getKetuaBahagian")
	public String getKetuaBahagian() {
		String idBahagian = getParam("idBahagian");
		int count = 1;
		try {			
			mp = new MyPersistence();
			List<CmsDirektori> listKetuaBahagian = mp.list("SELECT x FROM CmsDirektori x WHERE x.bahagian.id = '" + idBahagian + "' and x.seksyen is null ORDER BY x.turutan ASC");
			context.put("listKetuaBahagian", listKetuaBahagian);
			count = listKetuaBahagian.size();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		Vector turutan = new Vector();
		Hashtable h = null;
		for (int i = 1; i <= count; i++) {			
			h = new Hashtable();
			h.put("id", i);
			turutan.addElement(h);
		}	
		
		context.put("selectTurutan", turutan);	
		context.put("selectedTab", "1");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getSenaraiUnit")
	public String getSenaraiUnit() {
		String idBahagian = getParam("idBahagian");
		Seksyen unit = null;		
		int count = 1;
		try {			
			mp = new MyPersistence();	
			
			List<Seksyen> listUnit = mp.list("SELECT x FROM Seksyen x WHERE x.bahagian.id = '" + idBahagian + "' ORDER BY x.keterangan ASC");
			context.put("listUnit", listUnit);
			
			//MAKLUMAT 1ST TAB
			if (listUnit.size() > 0) {
				unit = listUnit.get(0);
				if (unit != null){
					
					List<CmsDirektori> listStafUnit = mp.list("Select x from CmsDirektori x where x.seksyen.id = '" + unit.getId() + "' ORDER BY x.turutan ASC") ;
					context.put("listStafUnit", listStafUnit);
					count = listStafUnit.size();
					context.put("selectedSubTab", unit.getId());
					context.put("idUnit", unit.getId());
				}
			}		
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		Vector turutan = new Vector();
		Hashtable h = null;
		for (int i = 1; i <= count; i++) {			
			h = new Hashtable();
			h.put("id", i);
			turutan.addElement(h);
		}	
		
		context.put("selectTurutan", turutan);
		context.put("selectedTab", "2");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getTabUnit")
	public String getTabUnit(){
		String idUnit = getParam("idUnit");
		int count = 1;
		
		try {			
			mp = new MyPersistence();		
			List<CmsDirektori> listStafUnit = mp.list("Select x from CmsDirektori x where x.seksyen.id = '" + idUnit + "' ORDER BY x.turutan ASC") ;
			context.put("listStafUnit", listStafUnit);
			count = listStafUnit.size();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
				
		Vector turutan = new Vector();
		Hashtable h = null;
		for (int i = 1; i <= count; i++) {			
			h = new Hashtable();
			h.put("id", i);
			turutan.addElement(h);
		}	
		
		context.put("selectTurutan", turutan);
		context.put("selectedTab", "2");
		context.put("selectedSubTab", idUnit);
		context.put("idUnit", idUnit);
		return getPath() + "/entry_page.vm";
	}
	/******************************************** END TAB ********************************************/

	
	/******************************************** START DIREKTORI KETUA BAHAGIAN ********************************************/
	@Command("addKetuaBahagian")
	public String addKetuaBahagian() {
		
		context.remove("rekod");
		
		return getPath() + "/ketuaBahagian/popupAddMaklumat.vm";
	}
	
	@Command("editKetuaBahagian")
	public String editKetuaBahagian() {		
		try {			
			mp = new MyPersistence();
			
			CmsDirektori ketuaBahagian = (CmsDirektori) mp.find(CmsDirektori.class, getParam("idKetuaBahagian"));
			context.put("rekod", ketuaBahagian);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return getPath() + "/ketuaBahagian/popupAddMaklumat.vm";
	}
	
	@Command("saveKetuaBahagian")
	public String saveKetuaBahagian() throws Exception{
		
		userId = (String) request.getSession().getAttribute("_portal_login");	
		boolean addKetuaBahagian = false;
		String idBahagian = getParam("idBahagian");
		String idKetuaBahagian = getParam("idKetuaBahagian");
		
		try {	
			mp = new MyPersistence();
			mp.begin();
			
			CmsDirektori ketuaBahagian = (CmsDirektori) mp.find(CmsDirektori.class, idKetuaBahagian);
			if (ketuaBahagian == null) {
				addKetuaBahagian = true;
				ketuaBahagian = new CmsDirektori();				
			}
			ketuaBahagian.setNama(getParam("nama"));
			ketuaBahagian.setJawatan(getParam("jawatan"));
			ketuaBahagian.setEmail(getParam("email"));			
			ketuaBahagian.setNoTelefon(getParam("noTelefon"));
			ketuaBahagian.setNoFaks(getParam("noFaks"));
			ketuaBahagian.setFlagKetua("Y");
			ketuaBahagian.setFlagAktif(getParam("flagAktif"));
			ketuaBahagian.setBahagian((Bahagian) mp.find(Bahagian.class, idBahagian));
			ketuaBahagian.setSeksyen(null);
			
			if (addKetuaBahagian){
				ketuaBahagian.setTurutan(getMaxTurutan(idBahagian, null, mp));
				ketuaBahagian.setDaftarOleh((Users) mp.find(Users.class, userId));
				ketuaBahagian.setTarikhMasuk(new Date()); 
				mp.persist(ketuaBahagian);
			} else {
				ketuaBahagian.setKemaskiniOleh((Users) mp.find(Users.class, userId));
				ketuaBahagian.setTarikhKemaskini(new Date()); 
			}
			mp.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}

		return getKetuaBahagian();
	}
	
	@Command("deleteKetuaBahagian")
	public String deleteKetuaBahagian() {
		try {
			mp = new MyPersistence();
			mp.begin();
			CmsDirektori ketuaBahagian = (CmsDirektori) mp.find(CmsDirektori.class, getParam("idKetuaBahagian"));
			if (ketuaBahagian != null) {
				Util.deleteFile(ketuaBahagian.getFileName());
				Util.deleteFile(ketuaBahagian.getAvatar());
				mp.remove(ketuaBahagian);
			}
			mp.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}

		return getKetuaBahagian();
	}	
	
	@Command("updateTurutanKetuaBahagian")
	public String updateTurutanKetuaBahagian() {
		String idBahagian = getParam("idBahagian");
		String idDirektori = getParam("idDirektori");
		try {
			mp = new MyPersistence();
			mp.begin();
			CmsDirektori direktori = (CmsDirektori) mp.find(CmsDirektori.class, idDirektori);			
			if (direktori != null) {
				direktori.setTurutan(getParamAsInteger("turutan" + idDirektori));
			}
			mp.commit();
			
			reArrangeTurutan(idBahagian, null, idDirektori, getParamAsInteger("turutan" + idDirektori), mp);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}

		return getKetuaBahagian();
	}
	/******************************************** END DIREKTORI KETUA BAHAGIAN********************************************/
	
	/******************************************** START SENARAI STAF UNIT ********************************************/
	@Command("addStafUnit")
	public String addStafUnit() {
		String idUnit = getParam("idUnit");
		context.remove("rekod");
		context.put("idUnit", idUnit);
		return getPath() + "/senaraiUnit/popupFormMaklumatStafUnit.vm";
	}
	
	@Command("editStafUnit")
	public String editStafUnit() {
		try {			
			mp = new MyPersistence();
			
			CmsDirektori stafUnit = (CmsDirektori) mp.find(CmsDirektori.class, getParam("idStafUnit"));
			context.put("rekod", stafUnit);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return getPath() + "/senaraiUnit/popupFormMaklumatStafUnit.vm";
	}
		
	@Command("saveStafUnit")
	public String saveStafUnit() throws Exception{
		
		userId = (String) request.getSession().getAttribute("_portal_login");	
		boolean addStafUnit = false;
		String idUnit = getParam("idUnit");
		Seksyen unit = null;		
		String idStafUnit = getParam("idStafUnit");
		
		try {	
			mp = new MyPersistence();
			mp.begin();
			if (idUnit != "") {
				unit = (Seksyen) mp.find(Seksyen.class, idUnit);
			}
			
			CmsDirektori stafUnit = (CmsDirektori) mp.find(CmsDirektori.class, idStafUnit);
			if (stafUnit == null) {
				addStafUnit = true;
				stafUnit = new CmsDirektori();				
			}
			stafUnit.setNama(getParam("nama"));
			stafUnit.setJawatan(getParam("jawatan"));
			stafUnit.setEmail(getParam("email"));			
			stafUnit.setNoTelefon(getParam("noTelefon"));
			stafUnit.setNoFaks(getParam("noFaks"));
			stafUnit.setFlagKetua(getParam("flagKetua"));
			stafUnit.setFlagAktif(getParam("flagAktif"));
			stafUnit.setBahagian(unit.getBahagian());
			stafUnit.setSeksyen(unit);
			
			if (addStafUnit){
				stafUnit.setTurutan(getMaxTurutan(unit.getBahagian().getId(), unit.getId(), mp));
				stafUnit.setDaftarOleh((Users) mp.find(Users.class, userId));
				stafUnit.setTarikhMasuk(new Date()); 
				mp.persist(stafUnit);
			} else {
				stafUnit.setKemaskiniOleh((Users) mp.find(Users.class, userId));
				stafUnit.setTarikhKemaskini(new Date()); 
			}
			mp.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}

		return getTabUnit();
	}
	
	@Command("deleteStafUnit")
	public String deleteStafUnit() {
		String idUnit = "";
		try {
			mp = new MyPersistence();
			mp.begin();
			CmsDirektori stafUnit = (CmsDirektori) mp.find(CmsDirektori.class, getParam("idStafUnit"));			
			if (stafUnit != null) {
				idUnit = stafUnit.getSeksyen().getId();
				Util.deleteFile(stafUnit.getFileName());
				Util.deleteFile(stafUnit.getAvatar());
				mp.remove(stafUnit);
			}
			mp.commit();
			
			List<CmsDirektori> listStafUnit = mp.list("Select x from CmsDirektori x where x.seksyen.id = '" + idUnit + "' ORDER BY x.turutan ASC") ;
			context.put("listStafUnit", listStafUnit);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}

		context.put("selectedTab", "2");
		context.put("selectedSubTab", idUnit);
		context.put("idUnit", idUnit);
		return getPath() + "/entry_page.vm";
	}	
	
	@Command("updateTurutanStafUnit")
	public String updateTurutanStafUnit() {
		String idSeksyen = "";
		String idDirektori = getParam("idDirektori");
		int count = 1;
		
		try {
			mp = new MyPersistence();
			mp.begin();
			CmsDirektori direktori = (CmsDirektori) mp.find(CmsDirektori.class, idDirektori);			
			if (direktori != null) {
				idSeksyen = direktori.getSeksyen().getId();
				direktori.setTurutan(getParamAsInteger("turutan" + idDirektori));
			}
			mp.commit();
			
			reArrangeTurutan(null, idSeksyen, idDirektori, getParamAsInteger("turutan" + idDirektori), mp);
			
			List<CmsDirektori> listStafUnit = mp.list("Select x from CmsDirektori x where x.seksyen.id = '" + idSeksyen + "' ORDER BY x.turutan ASC") ;
			context.put("listStafUnit", listStafUnit);
			count = listStafUnit.size();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}

		Vector turutan = new Vector();
		Hashtable h = null;
		for (int i = 1; i <= count; i++) {			
			h = new Hashtable();
			h.put("id", i);
			turutan.addElement(h);
		}	
		
		context.put("selectTurutan", turutan);	
		context.put("selectedTab", "2");
		context.put("selectedSubTab", idSeksyen);
		context.put("idUnit", idSeksyen);
		return getPath() + "/entry_page.vm";
	}
	/******************************************** END SENARAI UNIT 
	 * @return ********************************************/
	
	private int getMaxTurutan(String idBahagian, String idSeksyen, MyPersistence mp) throws Exception {
		int turutan = 0;
		
		CmsDirektori direktori = null;
		if (idSeksyen != null) {
			direktori = (CmsDirektori) mp.get("select x from CmsDirektori x where x.seksyen.id = '" + idSeksyen + "' order by x.turutan desc");
		} else {
			direktori = (CmsDirektori) mp.get("select x from CmsDirektori x where x.bahagian.id = '" + idBahagian + "' and x.seksyen is null order by x.turutan desc");
		}
		if (direktori != null) {
			turutan = direktori.getTurutan() + 1;
		}
		
		return turutan;
	}
	
	private void reArrangeTurutan(String idBahagian, String idSeksyen, String idDirektori, int turutan, MyPersistence mp) throws Exception {
		mp.begin();
		int count = 1;
		
		List <CmsDirektori> listDirektori = null;
		if (idSeksyen != null) {
			listDirektori = mp.list("select x from CmsDirektori x where x.seksyen.id = '" + idSeksyen + "' and x.id not in (" + idDirektori + ") order by x.turutan asc");
		} else {
			listDirektori = mp.list("select x from CmsDirektori x where x.bahagian.id = '" + idBahagian + "' and x.seksyen is null and x.id not in (" + idDirektori + ") order by x.turutan asc");
		}
		
		for (int i = 0; i < listDirektori.size(); i++) {
			CmsDirektori direktori = listDirektori.get(i);
			if (count == turutan) {
				count++;
			}
			direktori.setTurutan(count);
			count++;
		}	
		mp.commit();
	}
	
	@SuppressWarnings("rawtypes")
	@Command("uploadProfilPhoto")
	public String uploadProfilPhoto() throws Exception {
		String idDirektori = getParam("idDirektori");
			
		String uploadDir = "portal/direktori/";
		File dir = new File(ResourceBundle.getBundle("dbconnection").getString("folder") + uploadDir);
		if (!dir.exists())
			dir.mkdir();
	
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		List items = upload.parseRequest(request);
		Iterator itr = items.iterator();
		List<FileItem> files = new ArrayList<FileItem>();
		while (itr.hasNext()) {
			FileItem item = (FileItem) itr.next();
			if ((!(item.isFormField())) && (item.getName() != null)
					&& (!("".equals(item.getName())))) {
				files.add(item);
			}
		}
		
		for (FileItem item : files) {
			String avatarName = "";
			String fileName = item.getName();

			String imgName = uploadDir + idDirektori + "_"+ UID.getUID()
					+ fileName.substring(fileName.lastIndexOf("."));
			imgName = imgName.replaceAll(" ", "_");	
			item.write(new File(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName));
			
			String mimetype = item.getContentType();
			String type = mimetype.split("/")[0];
			if (type.equals("image")) {
				avatarName = imgName.substring(0, imgName.lastIndexOf("."))
						+ "_avatar"
						+ imgName.substring(imgName.lastIndexOf("."));
				avatarName = avatarName.replaceAll(" ", "_");
				lebah.repository.Thumbnail.create(ResourceBundle.getBundle("dbconnection").getString("folder")
						+ imgName, ResourceBundle.getBundle("dbconnection").getString("folder") + imgName,
						250, 130, 100);
				lebah.repository.Thumbnail.create(ResourceBundle.getBundle("dbconnection").getString("folder")
						+ imgName, ResourceBundle.getBundle("dbconnection").getString("folder") + avatarName,
						38, 38, 100);
			}
			
			updateProfilePhoto(idDirektori, imgName, avatarName);
		}
		return getPath() + "/uploadProfilePhoto.vm";
	}

	private void updateProfilePhoto(String idDirektori, String profilePhoto, String avatarName) { 
		boolean updateProfilePhoto = false;
		mp = new MyPersistence();
		try {			
			CmsDirektori direktori = (CmsDirektori) mp.find(CmsDirektori.class, idDirektori);
			if (direktori != null) {
				mp.begin();
				Util.deleteFile(direktori.getFileName());
				Util.deleteFile(direktori.getAvatar());
				direktori.setFileName(profilePhoto);
				direktori.setAvatar(avatarName);
				mp.commit();
				updateProfilePhoto = true;
				
				context.put("idDirektori", idDirektori);
				context.put("idBahagian", direktori.getBahagian().getId());
				if (direktori.getSeksyen() != null) {
					context.put("idSeksyen", direktori.getSeksyen().getId());
				} else {
					context.put("idSeksyen", "");
				}				
			}			
		} catch (Exception ex) {
			System.out.println("ERROR UPDATE PROFILE PHOTO : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}		
	}

	@Command("refreshProfilePhoto")
	public String refreshProfilePhoto() throws Exception {
		String idBahagian = getParam("idBahagian");
		String idSeksyen = getParam("idSeksyen");
		int count = 1;
		
		try {
			mp = new MyPersistence();
			if (idSeksyen != "") {
				List<CmsDirektori> listStafUnit = mp.list("Select x from CmsDirektori x where x.seksyen.id = '" + idSeksyen + "' ORDER BY x.turutan ASC") ;
				context.put("listStafUnit", listStafUnit);
				count = listStafUnit.size();
			}	
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}		
		
		if (idSeksyen != "") {
			Vector turutan = new Vector();
			Hashtable h = null;
			for (int i = 1; i <= count; i++) {			
				h = new Hashtable();
				h.put("id", i);
				turutan.addElement(h);
			}
			
			context.put("selectTurutan", turutan);	
			context.put("selectedTab", "2");
			context.put("selectedSubTab", idSeksyen);
			context.put("idUnit", idSeksyen);
			return getPath() + "/entry_page.vm";
		} else {
			return getKetuaBahagian();
		}	
	}
}
