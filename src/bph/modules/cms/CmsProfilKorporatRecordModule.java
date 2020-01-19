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
import bph.entities.portal.CmsProfilKorporat;
import bph.entities.portal.CmsSubProfilKorporat;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class CmsProfilKorporatRecordModule extends LebahRecordTemplateModule<CmsProfilKorporat>{

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
	public Class<CmsProfilKorporat> getPersistenceClass() {
		// TODO Auto-generated method stub
		return CmsProfilKorporat.class;
	}
	
	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/cms/profilKorporat";
	}
	
	@Override
	public void begin() {
		int count = 1;
		try {
			mp = new MyPersistence();
			CmsProfilKorporat profilKorporat = (CmsProfilKorporat) mp.get("select x from CmsProfilKorporat x where x.flagAktif = 'Y' order by x.turutan desc");
			if (profilKorporat != null) {
				count = profilKorporat.getTurutan() + 1;
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
		
		defaultButtonOption();
		addfilter();
		//TODO IMPLEMENT BILA ADA SUBCLASS		
		doOverideFilterRecord();
	}
	
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

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void save(CmsProfilKorporat r) throws Exception {
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		r.setTajuk(getParam("tajuk"));
		r.setButiran(getParam("butiran"));
		r.setFlagAktif(getParam("flagAktif"));
		
		if ("Y".equals(getParam("flagAktif"))) {
			r.setTurutan(getParamAsInteger("turutan"));
		} else {
			r.setTurutan(99999);
		}
		
		r.setDaftarOleh(db.find(Users.class, userId));
		r.setTarikhMasuk(new Date());		
	}

	@Override
	public void afterSave(CmsProfilKorporat r) {
		// TODO Auto-generated method stub
		context.put("selectedTab", 1);
	}	
	
	@Override
	public boolean delete(CmsProfilKorporat r) throws Exception {
		boolean allowDelete = false;
		try {
			mp = new MyPersistence();
			CmsSubProfilKorporat subProfilKorporat = (CmsSubProfilKorporat) db.get("select x from CmsSubProfilKorporat x where x.profilKorporat.id = '" + r.getId() + "'");
			if (subProfilKorporat == null) {
				allowDelete = true;
				Util.deleteFile(r.getFileName());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		return allowDelete;
	}	
	
	@Override
	public void getRelatedData(CmsProfilKorporat r) {
		int count = 1;
		try {
			mp = new MyPersistence();
			CmsProfilKorporat profilKorporat = (CmsProfilKorporat) mp.get("select x from CmsProfilKorporat x where x.flagAktif = 'Y' order by x.turutan desc");
			if (profilKorporat != null) {
				if ("Y".equals(r.getFlagAktif())) {
					count = profilKorporat.getTurutan();
				} else {
					count = profilKorporat.getTurutan() + 1;
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
		context.put("selectedTab", "1");
	}
	
	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("tajuk", get("findTajuk").trim());
		map.put("flagAktif", new OperatorEqualTo(get("findFlagAktif")));
		
		return map;
	}
	
	/** START TAB **/
	@Command("getProfilKorporat")
	public String getProfilKorporat() {
		userId = (String) request.getSession().getAttribute("_portal_login");
		String id = getParam("idProfilKorporat");
		CmsProfilKorporat profilKorporat = null;
		
		mp = new MyPersistence();
		try {			
			profilKorporat = (CmsProfilKorporat) mp.find(CmsProfilKorporat.class, id);			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}		
		
		context.put("r", profilKorporat);
		context.put("selectedTab", "1");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getSubProfilKorporat")
	public String getSubProfilKorporat() {
		userId = (String) request.getSession().getAttribute("_portal_login");
		String id = getParam("idProfilKorporat");
		CmsProfilKorporat profilKorporat = null;
		List <CmsSubProfilKorporat> listSubProfilKorporat = null;
		
		mp = new MyPersistence();
		try {			
			profilKorporat = (CmsProfilKorporat) mp.find(CmsProfilKorporat.class, id);	
			if (profilKorporat != null) {
				listSubProfilKorporat = mp.list("select x from CmsSubProfilKorporat x where x.profilKorporat.id = '" + profilKorporat.getId() + "' order by x.id asc");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}		
		
		context.remove("subProfilKorporat");
		context.put("listSubProfilKorporat", listSubProfilKorporat);
		context.put("r", profilKorporat);
		context.put("selectedTab", "2");
		return getPath() + "/entry_page.vm";
	}
	/** END TAB **/
	
	/** START PROFIL KORPORAT **/
	@Command("kemaskiniMaklumat")
	public String kemaskiniMaklumat() throws Exception {
		userId = (String) request.getSession().getAttribute("_portal_login");
		String id = getParam("idProfilKorporat");
		int turutan = getParamAsInteger("turutan");
		CmsProfilKorporat profilKorporat = null;
		
		mp = new MyPersistence();
		try {			
			profilKorporat = (CmsProfilKorporat) mp.find(CmsProfilKorporat.class, id);
			
			if (profilKorporat != null) {
				mp.begin();
				profilKorporat.setTajuk(getParam("tajuk"));
				profilKorporat.setButiran(getParam("butiran"));
				if ("T".equals(getParam("flagAktif"))) {
					profilKorporat.setTurutan(99999);
				} else {
					if (profilKorporat.getTurutan() != turutan) {
						reArrangeTurutan(profilKorporat.getId(), turutan, mp);
					}
					profilKorporat.setTurutan(turutan);
				}
				profilKorporat.setFlagAktif(getParam("flagAktif"));
				profilKorporat.setKemaskiniOleh((Users) mp.find(Users.class, userId));
				profilKorporat.setTarikhKemaskini(new Date());				
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
		
		return getProfilKorporat();
	}
	
	private void reArrangeTurutan(String id, int turutan, MyPersistence mp) {
		int count = 1;
		List <CmsProfilKorporat> listProfilKorporat = mp.list("select x from CmsProfilKorporat x where x.id not in (" + id + ") and x.flagAktif = 'Y' order by x.turutan asc");
		for (int i = 0; i < listProfilKorporat.size(); i++) {
			CmsProfilKorporat profilKorporat = (CmsProfilKorporat) mp.find(CmsProfilKorporat.class, listProfilKorporat.get(i).getId());
			if (profilKorporat != null) {
				if (count == turutan) {
					count++;
				}
				profilKorporat.setTurutan(count);
				count++;
			}
		}		
	}
	
	private void reArrangeTurutanTidakAktif() {
		try {
			mp = new MyPersistence();
			mp.begin();
			List <CmsProfilKorporat> listProfilKorporat = mp.list("select x from CmsProfilKorporat x where x.flagAktif = 'Y' order by x.turutan asc");
			for (int i = 0; i < listProfilKorporat.size(); i++) {
				CmsProfilKorporat profilKorporat = (CmsProfilKorporat) mp.find(CmsProfilKorporat.class, listProfilKorporat.get(i).getId());
				if (profilKorporat != null) {
					profilKorporat.setTurutan(i+1);
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
		String id = getParam("idProfilKorporat");
			
		String uploadDir = "portal/profilKorporat/";
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
			CmsProfilKorporat profilKorporat = (CmsProfilKorporat) mp.find(CmsProfilKorporat.class, id);
			if (profilKorporat != null) {
				mp.begin();
				profilKorporat.setFileName(imgName);
				profilKorporat.setKemaskiniOleh((Users) mp.find(Users.class, userId));
				profilKorporat.setTarikhKemaskini(new Date());				
				mp.commit();
			}			
		} catch (Exception ex) {
			System.out.println("ERROR UPDATE GAMBAR : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
	}
	
	@Command("hapusGambar")
	public String hapusGambar() throws Exception {
		userId = (String) request.getSession().getAttribute("_portal_login");
		String id = getParam("idProfilKorporat");
		CmsProfilKorporat profilKorporat = null;
		
		mp = new MyPersistence();
		try {			
			profilKorporat = (CmsProfilKorporat) mp.find(CmsProfilKorporat.class, id);
			
			if (profilKorporat != null) {
				mp.begin();
				Util.deleteFile(profilKorporat.getFileName());
				profilKorporat.setFileName(null);
				profilKorporat.setKemaskiniOleh((Users) mp.find(Users.class, userId));
				profilKorporat.setTarikhKemaskini(new Date());				
				mp.commit();
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}		
			
		return getProfilKorporat();
	}
	/** END PROFIL KORPORAT **/
	
	/** START SUBPROFIL KORPORAT **/
	@Command("daftarSubProfilKorporat")
	public String daftarSubProfilKorporat() {
		context.remove("subProfilKorporat");
		return getPath() + "/subProfilKorporat/maklumatSubProfilKorporat.vm";
	}
	
	@Command("simpanSubProfilKorporat")
	public String simpanSubProfilKorporat() throws Exception {
		String idProfilKorporat = get("idProfilKorporat");
		CmsSubProfilKorporat subProfilKorporat = new CmsSubProfilKorporat();
		String uploadDir = "portal/profilKorporat/";
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
			String imgName = uploadDir + idProfilKorporat + "_" + subProfilKorporat.getId()
					+ fileName.substring(fileName.lastIndexOf("."));

			imgName = imgName.replaceAll(" ", "_");
			item.write(new File(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName));

			if (!imgName.equals("")) {
				simpanSubProfilKorporat(idProfilKorporat, imgName, subProfilKorporat);
			}
		}

		return getPath() + "/subProfilKorporat/uploadDokumen.vm";
	}
	
	public void simpanSubProfilKorporat(String idProfilKorporat, String imgName, CmsSubProfilKorporat subProfilKorporat) throws Exception {
		userId = (String) request.getSession().getAttribute("_portal_login");
		mp = new MyPersistence();
		try {			
			CmsProfilKorporat profilKorporat = (CmsProfilKorporat) mp.find(CmsProfilKorporat.class, idProfilKorporat);
			if (profilKorporat != null) {
				mp.begin();
				subProfilKorporat.setProfilKorporat(profilKorporat);
				subProfilKorporat.setTajuk(getParam("tajukSubProfilKorporat"));
				subProfilKorporat.setFileName(imgName);
				subProfilKorporat.setFlagAktif(getParam("flagAktifSubProfilKorporat"));
				subProfilKorporat.setDaftarOleh((Users) mp.find(Users.class, userId));
				subProfilKorporat.setTarikhMasuk(new Date());	
				mp.persist(subProfilKorporat);
				
				profilKorporat.setKemaskiniOleh((Users) mp.find(Users.class, userId));
				profilKorporat.setTarikhKemaskini(new Date());
				
				mp.commit();
			}			
		} catch (Exception ex) {
			System.out.println("ERROR INSERT DOKUMEN : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
	}
	
	@Command("deleteSubProfilKorporat")
	public String deleteSubProfilKorporat() throws Exception {
		String idSubProfilKorporat = get("idSubProfilKorporat");
		
		try {			
			mp = new MyPersistence();			
			CmsSubProfilKorporat subProfilKorporat = (CmsSubProfilKorporat) mp.find(CmsSubProfilKorporat.class, idSubProfilKorporat);
			if (subProfilKorporat != null) {
				mp.begin();
				CmsProfilKorporat profilKorporat = (CmsProfilKorporat) mp.find(CmsProfilKorporat.class, subProfilKorporat.getProfilKorporat().getId());
				
				Util.deleteFile(subProfilKorporat.getFileName());
				
				mp.remove(subProfilKorporat);
				
				if (profilKorporat != null) {
					profilKorporat.setKemaskiniOleh((Users) mp.find(Users.class, userId));
					profilKorporat.setTarikhKemaskini(new Date());			
				}				
				mp.commit();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}

		return getSubProfilKorporat();
	}
	
	@Command("kemaskiniSubProfilKorporat")
	public String kemaskiniSubProfilKorporat() throws Exception {
		String idSubProfilKorporat = get("idSubProfilKorporatList");
		
		try {			
			mp = new MyPersistence();			
			CmsSubProfilKorporat subProfilKorporat = (CmsSubProfilKorporat) mp.find(CmsSubProfilKorporat.class, idSubProfilKorporat);
			if (subProfilKorporat != null) {
				context.put("subProfilKorporat", subProfilKorporat);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}

		return getPath() + "/subProfilKorporat/maklumatSubProfilKorporat.vm";
	}
	
	@Command("simpanKemaskiniSubProfilKorporat")
	public String simpanKemaskiniSubProfilKorporat() throws Exception {
		try {	
			mp = new MyPersistence();
			String uploadDir = "portal/profilKorporat/";
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

			String idSubProfilKorporat = get("idSubProfilKorporat");
			CmsSubProfilKorporat subProfilKorporat = (CmsSubProfilKorporat) mp.find(CmsSubProfilKorporat.class, idSubProfilKorporat);
			if (subProfilKorporat != null) {
				
				mp.begin();
				subProfilKorporat.setTajuk(getParam("tajukSubProfilKorporat"));
				for (FileItem item : files) {
					String avatarName = "";
					String fileName = item.getName();
					String imgName = uploadDir + subProfilKorporat.getProfilKorporat().getId() + "_" + subProfilKorporat.getId()
							+ fileName.substring(fileName.lastIndexOf("."));

					imgName = imgName.replaceAll(" ", "_");
					Util.deleteFile(subProfilKorporat.getFileName());
					item.write(new File(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName));

					subProfilKorporat.setFileName(imgName);
				}
				subProfilKorporat.setFlagAktif(getParam("flagAktifSubProfilKorporat"));
				subProfilKorporat.setKemaskiniOleh((Users) mp.find(Users.class, userId));
				subProfilKorporat.setTarikhKemaskini(new Date());
				
				CmsProfilKorporat profilKorporat = (CmsProfilKorporat) mp.find(CmsProfilKorporat.class, subProfilKorporat.getProfilKorporat().getId());
				if (profilKorporat != null) {
					profilKorporat.setKemaskiniOleh((Users) mp.find(Users.class, userId));
					profilKorporat.setTarikhKemaskini(new Date());
				}					
				mp.commit();
			}			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}		

		return getPath() + "/subProfilKorporat/uploadDokumen.vm";
	}
	/** END SUBPROFIL KORPORAT **/
}
