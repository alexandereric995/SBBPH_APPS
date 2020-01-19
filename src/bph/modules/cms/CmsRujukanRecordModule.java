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
import bph.entities.portal.CmsRujukan;
import bph.entities.portal.CmsSubRujukan;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class CmsRujukanRecordModule extends LebahRecordTemplateModule<CmsRujukan>{

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
	public Class<CmsRujukan> getPersistenceClass() {
		// TODO Auto-generated method stub
		return CmsRujukan.class;
	}
	
	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/cms/rujukan";
	}
	
	@Override
	public void begin() {
		int count = 1;
		try {
			mp = new MyPersistence();
			CmsRujukan rujukan = (CmsRujukan) mp.get("select x from CmsRujukan x where x.flagAktif = 'Y' order by x.turutan desc");
			if (rujukan != null) {
				count = rujukan.getTurutan() + 1;
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
	public void save(CmsRujukan r) throws Exception {
		
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
	public void afterSave(CmsRujukan r) {
		// TODO Auto-generated method stub
		context.put("selectedTab", 1);
	}	
	
	@Override
	public boolean delete(CmsRujukan r) throws Exception {
		
		boolean allowDelete = false;
		try {
			mp = new MyPersistence();
			CmsSubRujukan subRujukan = (CmsSubRujukan) db.get("select x from CmsSubRujukan x where x.rujukan.id = '" + r.getId() + "'");
			if (subRujukan == null) {
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
	public void getRelatedData(CmsRujukan r) {
		int count = 1;
		try {
			mp = new MyPersistence();
			CmsRujukan rujukan = (CmsRujukan) mp.get("select x from CmsRujukan x where x.flagAktif = 'Y' order by x.turutan desc");
			if (rujukan != null) {
				if ("Y".equals(r.getFlagAktif())) {
					count = rujukan.getTurutan();
				} else {
					count = rujukan.getTurutan() + 1;
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
	
	/************************************************** START FUNCTION TAB *************************************************/
	@Command("getRujukan")
	public String getRujukan() {
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		String id = getParam("idRujukan");
		CmsRujukan rujukan = null;
		
		mp = new MyPersistence();
		try {			
			rujukan = (CmsRujukan) mp.find(CmsRujukan.class, id);			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}		
		
		context.put("r", rujukan);
		context.put("selectedTab", "1");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getSubRujukan")
	public String getSubRujukan() {
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		String id = getParam("idRujukan");
		CmsRujukan rujukan = null;
		List <CmsSubRujukan> listSubRujukan = null;
		
		mp = new MyPersistence();
		try {			
			rujukan = (CmsRujukan) mp.find(CmsRujukan.class, id);	
			if (rujukan != null) {
				listSubRujukan = mp.list("select x from CmsSubRujukan x where x.rujukan.id = '" + rujukan.getId() + "' order by x.id asc");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}		
		
		context.remove("subRujukan");
		context.put("listSubRujukan", listSubRujukan);
		context.put("r", rujukan);
		context.put("selectedTab", "2");
		return getPath() + "/entry_page.vm";
	}
	/************************************************** END FUNCTION TAB *************************************************/
	
	/****************************************** START FUNCTION MAIN MENU RUJUKAN *****************************************/
	@Command("kemaskiniMaklumat")
	public String kemaskiniMaklumat() throws Exception {
		userId = (String) request.getSession().getAttribute("_portal_login");
		String id = getParam("idRujukan");
		int turutan = getParamAsInteger("turutan");
		CmsRujukan rujukan = null;
		
		mp = new MyPersistence();
		try {			
			rujukan = (CmsRujukan) mp.find(CmsRujukan.class, id);
			
			if (rujukan != null) {
				mp.begin();
				rujukan.setTajuk(getParam("tajuk"));
				rujukan.setButiran(getParam("butiran"));
				if ("T".equals(getParam("flagAktif"))) {
					rujukan.setTurutan(99999);
				} else {
					if (rujukan.getTurutan() != turutan) {
						reArrangeTurutan(rujukan.getId(), turutan, mp);
					}
					rujukan.setTurutan(turutan);
				}
				rujukan.setFlagAktif(getParam("flagAktif"));
				rujukan.setKemaskiniOleh((Users) mp.find(Users.class, userId));
				rujukan.setTarikhKemaskini(new Date());				
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
		
		return getRujukan();
	}
	
	private void reArrangeTurutan(String id, int turutan, MyPersistence mp) {
		int count = 1;
		List <CmsRujukan> listRujukan = mp.list("select x from CmsRujukan x where x.id not in (" + id + ") and x.flagAktif = 'Y' order by x.turutan asc");
		for (int i = 0; i < listRujukan.size(); i++) {
			CmsRujukan rujukan = (CmsRujukan) mp.find(CmsRujukan.class, listRujukan.get(i).getId());
			if (rujukan != null) {
				if (count == turutan) {
					count++;
				}
				rujukan.setTurutan(count);
				count++;
			}
		}		
	}
	
	private void reArrangeTurutanTidakAktif() {
		try {
			mp = new MyPersistence();
			mp.begin();
			List <CmsRujukan> listRujukan = mp.list("select x from CmsRujukan x where x.flagAktif = 'Y' order by x.turutan asc");
			for (int i = 0; i < listRujukan.size(); i++) {
				CmsRujukan rujukan = (CmsRujukan) mp.find(CmsRujukan.class, listRujukan.get(i).getId());
				if (rujukan != null) {
					rujukan.setTurutan(i+1);
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
		String id = getParam("idRujukan");
			
		String uploadDir = "portal/rujukan/";
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
			CmsRujukan rujukan = (CmsRujukan) mp.find(CmsRujukan.class, id);
			if (rujukan != null) {
				mp.begin();
				rujukan.setFileName(imgName);
				rujukan.setKemaskiniOleh((Users) mp.find(Users.class, userId));
				rujukan.setTarikhKemaskini(new Date());				
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
		
		String idRujukan = get("idRujukan");
		try {			
			mp = new MyPersistence();			
			CmsRujukan rujukan = (CmsRujukan) mp.find(CmsRujukan.class, idRujukan);
			if (rujukan != null) {
				mp.begin();
				Util.deleteFile(rujukan.getFileName());
				rujukan.setFileName(null);
				mp.commit();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}

		return getRujukan();
	}
	/****************************************** END FUNCTION MAIN MENU RUJUKAN *****************************************/
	
	
	/********************************************** START SUB MENU RUJUKAN **********************************************/
	@Command("daftarSubRujukan")
	public String daftarSubRujukan() {
		context.remove("subRujukan");
		return getPath() + "/subRujukan/maklumatSubRujukan.vm";
	}
	
	@Command("simpanSubRujukan")
	public String simpanSubRujukan() throws Exception {
		
		String idRujukan = get("idRujukan");
		CmsSubRujukan subRujukan = new CmsSubRujukan();
		String uploadDir = "portal/rujukan/";
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
			String imgName = uploadDir + idRujukan + "_" + subRujukan.getId()
					+ fileName.substring(fileName.lastIndexOf("."));

			imgName = imgName.replaceAll(" ", "_");
			item.write(new File(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName));

			if (!imgName.equals("")) {
				simpanSubRujukan(idRujukan, imgName, subRujukan);
			}
		}

		return getPath() + "/subRujukan/uploadDokumen.vm";
	}

	public void simpanSubRujukan(String idRujukan, String imgName, CmsSubRujukan subRujukan) throws Exception {
		userId = (String) request.getSession().getAttribute("_portal_login");
		mp = new MyPersistence();
		try {			
			CmsRujukan rujukan = (CmsRujukan) mp.find(CmsRujukan.class, idRujukan);
			if (rujukan != null) {
				mp.begin();
				subRujukan.setRujukan(rujukan);
				subRujukan.setTajuk(getParam("tajukSubRujukan"));
				subRujukan.setFileName(imgName);
				subRujukan.setFlagAktif(getParam("flagAktifSubRujukan"));
				subRujukan.setDaftarOleh((Users) mp.find(Users.class, userId));
				subRujukan.setTarikhMasuk(new Date());	
				mp.persist(subRujukan);
				
				rujukan.setKemaskiniOleh((Users) mp.find(Users.class, userId));
				rujukan.setTarikhKemaskini(new Date());
				
				mp.commit();
			}			
		} catch (Exception ex) {
			System.out.println("ERROR INSERT DOKUMEN : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
	}
	
	@Command("deleteSubRujukan")
	public String deleteSubRujukan() throws Exception {
		
		String idSubRujukan = get("idSubRujukan");
		try {			
			mp = new MyPersistence();			
			CmsSubRujukan subRujukan = (CmsSubRujukan) mp.find(CmsSubRujukan.class, idSubRujukan);
			if (subRujukan != null) {
				mp.begin();
				CmsRujukan rujukan = (CmsRujukan) mp.find(CmsRujukan.class, subRujukan.getRujukan().getId());
				
				Util.deleteFile(subRujukan.getFileName());
				
				mp.remove(subRujukan);
				
				if (rujukan != null) {
					rujukan.setKemaskiniOleh((Users) mp.find(Users.class, userId));
					rujukan.setTarikhKemaskini(new Date());			
				}				
				mp.commit();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}

		return getSubRujukan();
	}
	
	@Command("kemaskiniSubRujukan")
	public String kemaskiniSubRujukan() throws Exception {
		
		String idSubRujukan = get("idSubRujukanList");
		try {			
			mp = new MyPersistence();			
			CmsSubRujukan subRujukan = (CmsSubRujukan) mp.find(CmsSubRujukan.class, idSubRujukan);
			if (subRujukan != null) {
				context.put("subRujukan", subRujukan);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}

		return getPath() + "/subRujukan/maklumatSubRujukan.vm";
	}
	
	@Command("simpanKemaskiniSubRujukan")
	public String simpanKemaskiniSubRujukan() throws Exception {
		try {	
			mp = new MyPersistence();
			String uploadDir = "portal/rujukan/";
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

			String idSubRujukan = get("idSubRujukan");
			CmsSubRujukan subRujukan = (CmsSubRujukan) mp.find(CmsSubRujukan.class, idSubRujukan);
			if (subRujukan != null) {
				mp.begin();
				subRujukan.setTajuk(getParam("tajukSubRujukan"));
				subRujukan.setFlagAktif(getParam("flagAktifSubRujukan"));
				subRujukan.setKemaskiniOleh((Users) mp.find(Users.class, userId));
				subRujukan.setTarikhKemaskini(new Date());
				
				CmsRujukan rujukan = (CmsRujukan) mp.find(CmsRujukan.class, subRujukan.getRujukan().getId());
				if (rujukan != null) {
					rujukan.setKemaskiniOleh((Users) mp.find(Users.class, userId));
					rujukan.setTarikhKemaskini(new Date());
				}
				for (FileItem item : files) {
					String avatarName = "";
					String fileName = item.getName();
					String imgName = uploadDir + subRujukan.getRujukan().getId() + "_" + subRujukan.getId()
							+ fileName.substring(fileName.lastIndexOf("."));

					imgName = imgName.replaceAll(" ", "_");
					Util.deleteFile(subRujukan.getFileName());
					item.write(new File(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName));
					subRujukan.setFileName(imgName);					
				}
				mp.commit();
			}			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}		

		return getPath() + "/subRujukan/uploadDokumen.vm";
	}
	/********************************************** END SUB MENU RUJUKAN **********************************************/
}
