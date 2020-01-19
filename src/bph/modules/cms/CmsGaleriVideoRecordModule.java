package bph.modules.cms;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorEqualTo;
import lebah.template.UID;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import portal.module.entity.Users;
import bph.entities.portal.CmsGaleri;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class CmsGaleriVideoRecordModule extends LebahRecordTemplateModule<CmsGaleri>{

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
	public Class<CmsGaleri> getPersistenceClass() {
		// TODO Auto-generated method stub
		return CmsGaleri.class;
	}
	
	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/cms/galeriVideo";
	}
	
	@Override
	public void begin() {
		
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
		this.addFilter("kategori = '02'"); // VIDEO
		this.setOrderBy("id");
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
	public void save(CmsGaleri r) throws Exception {
		userId = (String) request.getSession().getAttribute("_portal_login");
		
		r.setKategori("02");
		r.setTajuk(getParam("tajuk"));
		r.setFlagAktif(getParam("flagAktif"));
		r.setDaftarOleh(db.find(Users.class, userId));
		r.setTarikhMasuk(new Date());		
	}

	@Override
	public void afterSave(CmsGaleri r) {
		// TODO Auto-generated method stub
	}	
	
	@Override
	public boolean delete(CmsGaleri r) throws Exception {
		Util.deleteFile(r.getFileName());
		return true;
	}	
	
	@Override
	public void getRelatedData(CmsGaleri r) {
		// TODO Auto-generated method stub
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
		CmsGaleri galeri = null;
		
		mp = new MyPersistence();
		try {			
			galeri = (CmsGaleri) mp.find(CmsGaleri.class, id);
			
			if (galeri != null) {
				mp.begin();
				galeri.setKategori("02");
				galeri.setTajuk(getParam("tajuk"));
				galeri.setFlagAktif(getParam("flagAktif"));
				galeri.setKemaskiniOleh((Users) mp.find(Users.class, userId));
				galeri.setTarikhKemaskini(new Date());				
				mp.commit();
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}		
		
		context.put("r", galeri);
		return getPath() + "/entry_page.vm";
	}
	
	@SuppressWarnings("rawtypes")
	@Command("uploadVideo")
	public String uploadVideo() throws Exception {
		String id = getParam("id");
			
		String uploadDir = "portal/galeri/video/";
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

			String imgName = uploadDir + id + "_"+ UID.getUID()
					+ fileName.substring(fileName.lastIndexOf("."));
			imgName = imgName.replaceAll(" ", "_");	
			item.write(new File(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName));
						
			updateVideo(id, imgName);
		}
		return getPath() + "/uploadVideo.vm";
	}

	private void updateVideo(String id, String imgName) { 
		userId = (String) request.getSession().getAttribute("_portal_login");
		mp = new MyPersistence();
		try {			
			CmsGaleri galeri = (CmsGaleri) mp.find(CmsGaleri.class, id);
			if (galeri != null) {
				mp.begin();
				Util.deleteFile(galeri.getFileName());
				galeri.setFileName(imgName);
				galeri.setKemaskiniOleh((Users) mp.find(Users.class, userId));
				galeri.setTarikhKemaskini(new Date());				
				mp.commit();
			}			
		} catch (Exception ex) {
			System.out.println("ERROR UPDATE VIDEO : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
	}

	@Command("refreshVideo")
	public String refreshVideo() throws Exception {
		String id = getParam("id");
		CmsGaleri galeri = null;
		mp = new MyPersistence();
		try {			
			galeri = (CmsGaleri) mp.find(CmsGaleri.class, id);		
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("r", galeri);
		return getPath() + "/entry_page.vm";
	}
}
