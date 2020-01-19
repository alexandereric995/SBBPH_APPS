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
import lebah.template.UID;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import portal.module.entity.Users;
import bph.entities.portal.CmsSlideshow;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class CmsSlideShowRecordModule extends LebahRecordTemplateModule<CmsSlideshow> {

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
	public Class<CmsSlideshow> getPersistenceClass() {
		// TODO Auto-generated method stub
		return CmsSlideshow.class;
	}
	
	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/cms/slideshow";
	}
	
	@Override
	public void begin() {
		int count = 1;
		try {
			mp = new MyPersistence();
			CmsSlideshow slideshow = (CmsSlideshow) mp.get("select x from CmsSlideshow x where x.flagAktif = 'Y' order by x.turutan desc");
			if (slideshow != null) {
				count = slideshow.getTurutan() + 1;
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
	public void save(CmsSlideshow r) throws Exception {
		userId = (String) request.getSession().getAttribute("_portal_login");
		r.setTajuk(getParam("tajuk"));
		r.setHref(getParam("href"));
		r.setOnclick(getParam("onclick"));
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
	public void afterSave(CmsSlideshow r) {
		try {
			mp = new MyPersistence();
			if ("Y".equals(r.getFlagAktif())) {
				mp.begin();
				reArrangeTurutan(r.getId(), r.getTurutan(), mp);
				mp.commit();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
	}	

	@Override
	public boolean delete(CmsSlideshow r) throws Exception {
		Util.deleteFile(r.getFileName());
		Util.deleteFile(r.getThumbFileName());
		return true;
	}	
	
	@Override
	public void getRelatedData(CmsSlideshow r) {
		int count = 1;
		try {
			mp = new MyPersistence();
			CmsSlideshow slideshow = (CmsSlideshow) mp.get("select x from CmsSlideshow x where x.flagAktif = 'Y' order by x.turutan desc");
			if (slideshow != null) {
				if ("Y".equals(r.getFlagAktif())) {
					count = slideshow.getTurutan();
				} else {
					count = slideshow.getTurutan() + 1;
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
	}
	
	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("tajuk", get("findTajuk").trim());
		map.put("flagAktif", new OperatorEqualTo(get("findFlagAktif")));
		
		return map;
	}
	
	@Command("kemaskiniMaklumat")
	public String kemaskiniMaklumat() throws Exception {
		userId = (String) request.getSession().getAttribute("_portal_login");
		String id = getParam("id");
		int turutan = getParamAsInteger("turutan");
		CmsSlideshow slideShow = null;
		
		mp = new MyPersistence();
		try {			
			slideShow = (CmsSlideshow) mp.find(CmsSlideshow.class, id);
			
			if (slideShow != null) {
				mp.begin();
				slideShow.setTajuk(getParam("tajuk"));
				slideShow.setHref(getParam("href"));
				slideShow.setOnclick(getParam("onclick"));
				slideShow.setFlagAktif(getParam("flagAktif"));
				if ("T".equals(getParam("flagAktif"))) {
					slideShow.setTurutan(99999);
				} else {
					if (slideShow.getTurutan() != turutan) {
						reArrangeTurutan(slideShow.getId(), turutan, mp);
					}
					slideShow.setTurutan(turutan);
				}
				slideShow.setKemaskiniOleh((Users) mp.find(Users.class, userId));
				slideShow.setTarikhKemaskini(new Date());	
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
		
		context.put("r", slideShow);
		return getPath() + "/entry_page.vm";
	}
	
	private void reArrangeTurutan(String id, int turutan, MyPersistence mp) {
		int count = 1;
		List <CmsSlideshow> listSlideShow = mp.list("select x from CmsSlideshow x where x.id not in (" + id + ") and x.flagAktif = 'Y' order by x.turutan asc");
		for (int i = 0; i < listSlideShow.size(); i++) {
			CmsSlideshow slideShow = listSlideShow.get(i);
			if (count == turutan) {
				count++;
			}
			slideShow.setTurutan(count);
			count++;
		}		
	}
	
	private void reArrangeTurutanTidakAktif() {
		try {
			mp = new MyPersistence();
			mp.begin();
			List <CmsSlideshow> listSlideShow = mp.list("select x from CmsSlideshow x where x.flagAktif = 'Y' order by x.turutan asc");
			for (int i = 0; i < listSlideShow.size(); i++) {
				CmsSlideshow slideShow = listSlideShow.get(i);
				slideShow.setTurutan(i+1);
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
		String id = getParam("id");
			
		String uploadDir = "portal/slideShow/";
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

			String imgName = uploadDir + id + "_"+ UID.getUID()
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
						+ imgName, ResourceBundle.getBundle("dbconnection").getString("folder") + avatarName,
						150, 90, 100);
			}
			
			updateGambar(id, imgName, avatarName);
		}
		return getPath() + "/uploadGambar.vm";
	}

	private void updateGambar(String id, String imgName, String avatarName) { 
		userId = (String) request.getSession().getAttribute("_portal_login");
		mp = new MyPersistence();
		try {			
			CmsSlideshow slideShow = (CmsSlideshow) mp.find(CmsSlideshow.class, id);
			if (slideShow != null) {
				mp.begin();
				Util.deleteFile(slideShow.getFileName());
				Util.deleteFile(slideShow.getThumbFileName());
				slideShow.setFileName(imgName);
				slideShow.setThumbFileName(avatarName);
				slideShow.setKemaskiniOleh((Users) mp.find(Users.class, userId));
				slideShow.setTarikhKemaskini(new Date());				
				mp.commit();
			}			
		} catch (Exception ex) {
			System.out.println("ERROR UPDATE GAMBAR : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
	}

	@Command("refreshGambar")
	public String refreshGambar() throws Exception {
		String id = getParam("id");
		CmsSlideshow slideShow = null;
		mp = new MyPersistence();
		try {			
			slideShow = (CmsSlideshow) mp.find(CmsSlideshow.class, id);		
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("r", slideShow);
		return getPath() + "/entry_page.vm";
	}
}
