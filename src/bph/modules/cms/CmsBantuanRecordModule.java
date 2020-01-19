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
import lebah.template.OperatorEqualTo;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import portal.module.entity.Users;
import bph.entities.portal.CmsBantuan;
import bph.entities.portal.CmsSubBantuan;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class CmsBantuanRecordModule extends LebahRecordTemplateModule<CmsBantuan> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MyPersistence mp;
	private Util util = new Util();

	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public void afterSave(CmsBantuan arg0) {
		// TODO Auto-generated method stub
		context.put("selectedTab", 1);
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void begin() {
		// TODO Auto-generated method stub
		
		//--------- start Function Turutan ---------
		int count = 1;
		try {
			mp = new MyPersistence();
			CmsBantuan bantuan = (CmsBantuan) mp.get("select x from CmsBantuan x where x.flagAktif = 'Y' order by x.turutan desc");
			if (bantuan != null) {
				count = bantuan.getTurutan() + 1;
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
		//--------- End Function Turutan ---------
		
		defaultButtonOption();
		addfilter();
		//TODO IMPLEMENT BILA ADA SUBCLASS		
		doOverideFilterRecord();
		
	}
	
	//--------- add byzul ---------
	private void defaultButtonOption() {
		this.setDisableSaveAddNewButton(true);
		this.setDisableKosongkanUpperButton(true);
		if (!"add_new_record".equals(command)){
			this.setDisableBackButton(true);
			this.setDisableDefaultButton(true);
		}
	}

	private void addfilter() {
		this.setOrderBy("turutan");
		this.setOrderType("asc");
	}

	private void doOverideFilterRecord() {
		// TODO Auto-generated method stub
		
	}
	//--------- add byzul ---------

	@Override
	public boolean delete(CmsBantuan r) throws Exception {
		// TODO Auto-generated method stub
		//------- Start Function Delete ------
		boolean allowDelete = false;
		try {
			mp = new MyPersistence();
			CmsSubBantuan subBantuan = (CmsSubBantuan) db.get("select x from CmsSubBantuan x where x.bantuan.id = '" + r.getId() + "'");
			if (subBantuan == null) {
				allowDelete = true;
				Util.deleteFile(r.getFileName());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		return allowDelete;
		//------- End Function Delete ------
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/cms/bantuan";
	}

	@Override
	public Class<CmsBantuan> getPersistenceClass() {
		// TODO Auto-generated method stub
		return CmsBantuan.class;
	}

	@Override
	public void getRelatedData(CmsBantuan r) {
		// TODO Auto-generated method stub
		
		//------- Start Function Turutan ------
		int count = 1;
		try {
			mp = new MyPersistence();
			CmsBantuan bantuan = (CmsBantuan) mp.get("select x from CmsBantuan x where x.flagAktif = 'Y' order by x.turutan desc");
			if (bantuan != null) {
				count = bantuan.getTurutan();
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
		context.put("selectedTab", "1");
		//------- End Function Turutan ------
	}

	@Override
	public void save(CmsBantuan simpan) throws Exception {
		// TODO Auto-generated method stub
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		simpan.setTajuk(getParam("tajuk"));
		simpan.setButiran(getParam("butiran"));
		simpan.setFlagAktif(getParam("flagAktif"));
		
		if ("Y".equals(getParam("flagAktif"))) {
			simpan.setTurutan(getParamAsInteger("turutan"));
		} else {
			simpan.setTurutan(99999);
		}
		
		simpan.setDaftarOleh(db.find(Users.class, userId));
		simpan.setTarikhMasuk(new Date());		
		
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("tajuk", get("findTajuk").trim());
		map.put("flagAktif", new OperatorEqualTo(get("findFlagAktif")));
		
		return map;
	}
	
	
	/******************************************* START TAB ********************************************/
	@Command("getBantuan")
	public String getBantuan() {
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		String id = getParam("idBantuan");
		CmsBantuan bantuan = null;
		
		mp = new MyPersistence();
		try {			
			bantuan = (CmsBantuan) mp.find(CmsBantuan.class, id);			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}		
		
		context.put("r", bantuan);
		context.put("selectedTab", "1");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getSubBantuan")
	public String getSubBantuan() {
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		String id = getParam("idBantuan");
		CmsBantuan bantuan = null;
		List <CmsSubBantuan> listSubBantuan = null;
		
		mp = new MyPersistence();
		try {			
			bantuan = (CmsBantuan) mp.find(CmsBantuan.class, id);	
			if (bantuan != null) {
				listSubBantuan = mp.list("select x from CmsSubBantuan x where x.bantuan.id = '" + bantuan.getId() + "' order by x.id asc");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}		
		
		context.remove("subBantuan");
		context.put("listSubBantuan", listSubBantuan);
		context.put("r", bantuan);
		context.put("selectedTab", "2");
		return getPath() + "/entry_page.vm";
	}
	/******************************************** END TAB ********************************************/

	
	/*************************************** START FUNCTION MAIN BANTUAN ***************************************/
	//------- start function kemaskini ------
	@Command("kemaskiniMaklumat")
	public String kemaskiniMaklumat() throws Exception {
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		String id = getParam("idBantuan");
		int turutan = getParamAsInteger("turutan");
		CmsBantuan bantuan = null;
		
		mp = new MyPersistence();
		try {			
			bantuan = (CmsBantuan) mp.find(CmsBantuan.class, id);
			
			if (bantuan != null) {
				mp.begin();
				bantuan.setTajuk(getParam("tajuk"));
				bantuan.setButiran(getParam("butiran"));
				if ("T".equals(getParam("flagAktif"))) {
					bantuan.setTurutan(99999);
				} else {
					if (bantuan.getTurutan() != turutan) {
						reArrangeTurutan(bantuan.getId(), turutan, mp);
					}
					bantuan.setTurutan(turutan);
				}
				bantuan.setFlagAktif(getParam("flagAktif"));
				bantuan.setKemaskiniOleh((Users) mp.find(Users.class, userId));
				bantuan.setTarikhKemaskini(new Date());				
				mp.commit();
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}		
		
		if ("T".equals(getParam("flagAktif"))) {
			reArrangeTurutanTidakAktif();
		}
		
		return getBantuan();
	}
	//------- end function kemaskini ------
	
	private void reArrangeTurutan(String id, int turutan, MyPersistence mp) {
		int count = 1;
		List <CmsBantuan> listBantuan = mp.list("select x from CmsBantuan x where x.id not in (" + id + ") and x.flagAktif = 'Y' order by x.turutan asc");
		for (int i = 0; i < listBantuan.size(); i++) {
			CmsBantuan bantuan = (CmsBantuan) mp.find(CmsBantuan.class, listBantuan.get(i).getId());
			if (bantuan != null) {
				if (count == turutan) {
					count++;
				}
				bantuan.setTurutan(count);
				count++;
			}
		}		
	}
	
	private void reArrangeTurutanTidakAktif() {
		try {
			mp = new MyPersistence();
			mp.begin();
			List <CmsBantuan> listBantuan = mp.list("select x from CmsBantuan x where x.flagAktif = 'Y' order by x.turutan asc");
			for (int i = 0; i < listBantuan.size(); i++) {
				CmsBantuan bantuan = (CmsBantuan) mp.find(CmsBantuan.class, listBantuan.get(i).getId());
				if (bantuan != null) {
					bantuan.setTurutan(i+1);
				}
			}
			mp.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}		
	}
	
	@SuppressWarnings("rawtypes")
	@Command("uploadGambar")
	public String uploadGambar() throws Exception {
		String id = getParam("idBantuan");
			
		String uploadDir = "portal/bantuan/";
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
			String fileName = item.getName();

			String imgName = uploadDir + id + fileName.substring(fileName.lastIndexOf("."));
			imgName = imgName.replaceAll(" ", "_");
			Util.deleteFile(imgName);
			item.write(new File(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName));
			
			updateGambar(id, imgName);
		}
		return getPath() + "/uploadGambar.vm";
	}

	private void updateGambar(String id, String imgName) { 
		userId = (String) request.getSession().getAttribute("_portal_login");
		mp = new MyPersistence();
		try {			
			CmsBantuan bantuan = (CmsBantuan) mp.find(CmsBantuan.class, id);
			if (bantuan != null) {
				mp.begin();
				bantuan.setFileName(imgName);
				bantuan.setKemaskiniOleh((Users) mp.find(Users.class, userId));
				bantuan.setTarikhKemaskini(new Date());				
				mp.commit();
			}			
		} catch (Exception ex) {
			System.out.println("ERROR UPDATE GAMBAR : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
	}
	/*************************************** END FUNCTION MAIN BANTUAN ***************************************/
	
	
	/*************************************** START SUB BANTUAN ***************************************/
	@Command("daftarSubBantuan")
	public String daftarSubBantuan() {
		context.remove("subBantuan");
		return getPath() + "/subBantuan/maklumatSubBantuan.vm";
	}
	
	@Command("simpanSubBantuan")
	public String simpanSubBantuan() throws Exception {
		String idBantuan = get("idBantuan");
		CmsSubBantuan subBantuan = new CmsSubBantuan();
		String uploadDir = "portal/bantuan/";
		File dir = new File(ResourceBundle.getBundle("dbconnection").getString("folder") + uploadDir);
		if (!dir.exists()){
			dir.mkdir();
		}

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
			String imgName = uploadDir + idBantuan + "_" + subBantuan.getId()
					+ fileName.substring(fileName.lastIndexOf("."));

			imgName = imgName.replaceAll(" ", "_");
			item.write(new File(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName));

			if (!imgName.equals("")) {
				simpanSubBantuan(idBantuan, imgName, subBantuan);
			}
		}

		return getPath() + "/subBantuan/uploadDokumen.vm";
	}
	
	public void simpanSubBantuan(String idBantuan, String imgName, CmsSubBantuan subBantuan) throws Exception {
		userId = (String) request.getSession().getAttribute("_portal_login");
		mp = new MyPersistence();
		try {			
			CmsBantuan bantuan = (CmsBantuan) mp.find(CmsBantuan.class, idBantuan);
			if (bantuan != null) {
				mp.begin();
				subBantuan.setBantuan(bantuan);
				subBantuan.setTajuk(getParam("tajukSubBantuan"));
				subBantuan.setFileName(imgName);
				subBantuan.setFlagAktif(getParam("flagAktifSubBantuan"));
				subBantuan.setDaftarOleh((Users) mp.find(Users.class, userId));
				subBantuan.setTarikhMasuk(new Date());	
				mp.persist(subBantuan);
				
				bantuan.setKemaskiniOleh((Users) mp.find(Users.class, userId));
				bantuan.setTarikhKemaskini(new Date());
				
				mp.commit();
			}			
		} catch (Exception ex) {
			System.out.println("ERROR INSERT DOKUMEN : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
	}
	
	@Command("deleteSubBantuan")
	public String deleteSubBantuan() throws Exception {
		
		String idSubBantuan = get("idSubBantuan");
		try {			
			mp = new MyPersistence();			
			CmsSubBantuan subBantuan = (CmsSubBantuan) mp.find(CmsSubBantuan.class, idSubBantuan);
			if (subBantuan != null) {
				mp.begin();
				CmsBantuan bantuan = (CmsBantuan) mp.find(CmsBantuan.class, subBantuan.getBantuan().getId());
				
				Util.deleteFile(subBantuan.getFileName());
				
				mp.remove(subBantuan);
				
				if (bantuan != null) {
					bantuan.setKemaskiniOleh((Users) mp.find(Users.class, userId));
					bantuan.setTarikhKemaskini(new Date());			
				}				
				mp.commit();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}

		return getSubBantuan();
	}
	
	@Command("kemaskiniSubBantuan")
	public String kemaskiniSubBantuan() throws Exception {
		String idSubBantuan = get("idSubBantuanList");
		
		try {			
			mp = new MyPersistence();			
			CmsSubBantuan subBantuan = (CmsSubBantuan) mp.find(CmsSubBantuan.class, idSubBantuan);
			if (subBantuan != null) {
				context.put("subBantuan", subBantuan);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}

		return getPath() + "/subBantuan/maklumatSubBantuan.vm";
	}
	
	@Command("simpanKemaskiniSubBantuan")
	public String simpanKemaskiniSubBantuan() throws Exception {
		try {	
			mp = new MyPersistence();
			String uploadDir = "portal/bantuan/";
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

			String idSubBantuan = get("idSubBantuan");
			CmsSubBantuan subBantuan = (CmsSubBantuan) mp.find(CmsSubBantuan.class, idSubBantuan);
			if (subBantuan != null) {
				mp.begin();
				for (FileItem item : files) {
					String avatarName = "";
					String fileName = item.getName();
					String imgName = uploadDir + subBantuan.getBantuan().getId() + "_" + subBantuan.getId()
							+ fileName.substring(fileName.lastIndexOf("."));

					imgName = imgName.replaceAll(" ", "_");
					Util.deleteFile(subBantuan.getFileName());
					item.write(new File(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName));
					
					subBantuan.setFileName(imgName);
				}
				subBantuan.setTajuk(getParam("tajukSubBantuan"));				
				subBantuan.setFlagAktif(getParam("flagAktifSubBantuan"));
				subBantuan.setKemaskiniOleh((Users) mp.find(Users.class, userId));
				subBantuan.setTarikhKemaskini(new Date());
				
				CmsBantuan bantuan = (CmsBantuan) mp.find(CmsBantuan.class, subBantuan.getBantuan().getId());
				if (bantuan != null) {
					bantuan.setKemaskiniOleh((Users) mp.find(Users.class, userId));
					bantuan.setTarikhKemaskini(new Date());
				}					
				mp.commit();
			}			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}		

		return getPath() + "/subBantuan/uploadDokumen.vm";
	}
	/*************************************** END SUB BANTUAN ***************************************/
	
	
}
