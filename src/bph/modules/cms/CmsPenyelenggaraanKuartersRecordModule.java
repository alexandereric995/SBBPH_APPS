//done by ain nadia
package bph.modules.cms;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorEqualTo;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import bph.entities.portal.CmsPenyelenggaraanKuarters;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class CmsPenyelenggaraanKuartersRecordModule extends LebahRecordTemplateModule<CmsPenyelenggaraanKuarters> {
	
	private static final long serialVersionUID = 1L;
	private MyPersistence mp;
	private Util util = new Util();

	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public Class<CmsPenyelenggaraanKuarters> getPersistenceClass() {
		// TODO Auto-generated method stub
		return CmsPenyelenggaraanKuarters.class;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/cms/penyelenggaraanKuarters";
	}
	
	@Override
	public void save(CmsPenyelenggaraanKuarters r) throws Exception {
		// TODO Auto-generated method stub

		r.setTajuk(getParam("tajuk"));
		r.setFlagAktif(getParam("flagAktif"));
		//r.setFileName(getParam("fileName"));
		
	}
	
	@Override
	public void afterSave(CmsPenyelenggaraanKuarters r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub

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
		this.setOrderBy("id");
		this.setOrderType("asc");
	}

	private void doOverideFilterRecord() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean delete(CmsPenyelenggaraanKuarters r) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void getRelatedData(CmsPenyelenggaraanKuarters r) {
		// TODO Auto-generated method stub
		
//			CmsPenyelenggaraanKuarters cmspk = (CmsPenyelenggaraanKuarters) db.get("select x from CmsPenyelenggaraanKuarters x where x.flagAktif = 'Y'");
//		CmsPenyelenggaraanKuarters cmspk = (CmsPenyelenggaraanKuarters)	db.find(CmsPenyelenggaraanKuarters.class, getParam("idPenyelenggaraanKuarters"));
//		context.put("r", cmspk);
   }
		

		
	//	context.put("selectTurutan", turutan);	
	
	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
				
		map.put("tajuk", get("findTajuk").trim());
		map.put("flagAktif", new OperatorEqualTo(get("findFlagAktif")));
				
		return map;
	}
	
	@Command("getKontraktor")
	public String getKontraktor() {
		userId = (String) request.getSession().getAttribute("_portal_login");
		String id = getParam("idpk");
		CmsPenyelenggaraanKuarters kontraktor = null;
		
		mp = new MyPersistence();
		try {			
			kontraktor = (CmsPenyelenggaraanKuarters) mp.find(CmsPenyelenggaraanKuarters.class, id);			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}		
		
		  context.put("r", kontraktor);
		//context.put("selectedTab", "1");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("kemaskiniMaklumat")
	public String kemaskiniMaklumat() throws Exception {
		userId = (String) request.getSession().getAttribute("_portal_login");
		String id = getParam("idpk");
//		int turutan = getParamAsInteger("turutan");
		CmsPenyelenggaraanKuarters kontraktor = null;
		
		mp = new MyPersistence();
		try {			
			kontraktor = (CmsPenyelenggaraanKuarters) mp.find(CmsPenyelenggaraanKuarters.class, id);
			
			if (kontraktor != null) {
				mp.begin();
				kontraktor.setTajuk(getParam("tajuk"));
				kontraktor.setFlagAktif(getParam("flagAktif"));
		//		kontraktor.setButiran(getParam("butiran"));
		//		if ("T".equals(getParam("flagAktif"))) {
		//			kontraktor.setTurutan(99999);
		//		} else {
		//			if (kontraktor.getTurutan() != turutan) {
		//				reArrangeTurutan(kontraktor.getId(), turutan, mp);
		//			}
		//			kontraktor.setTurutan(turutan);
		//		}
				
			//	kontraktor.setKemaskiniOleh((Users) mp.find(Users.class, userId));
			//	kontraktor.setTarikhKemaskini(new Date());	
				
				mp.commit();
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}		
				
		return getKontraktor();
	}
	
	
	
	
//	@SuppressWarnings("rawtypes")
//	@Command("uploadDokumen")
//	public String uploadDokumen() throws Exception {
//		String id = getParam("id");
//			
//		String uploadDir = "portal/penyelenggaraanKuarters/";
//		File dir = new File(ResourceBundle.getBundle("dbconnection").getString("folder") + uploadDir);
//		if (!dir.exists())
//			dir.mkdir();
//	
//		DiskFileItemFactory factory = new DiskFileItemFactory();
//		ServletFileUpload upload = new ServletFileUpload(factory);
//		List items = upload.parseRequest(request);
//		Iterator itr = items.iterator();
//		List<FileItem> files = new ArrayList<FileItem>();
//		while (itr.hasNext()) {
//			FileItem item = (FileItem) itr.next();
//			if ((!(item.isFormField())) && (item.getName() != null)
//					&& (!("".equals(item.getName())))) {
//				files.add(item);
//			}
//		}
//		
//		for (FileItem item : files) {
//			String avatarName = "";
//			String fileName = item.getName();
//
//			String fName = uploadDir + id + "_"+ UID.getUID()
//					+ fileName.substring(fileName.lastIndexOf("."));
//			fName = fName.replaceAll(" ", "_");	
//			item.write(new File(ResourceBundle.getBundle("dbconnection").getString("folder") + fName));
//			
//			String mimetype = item.getContentType();
//			String type = mimetype.split("/")[0];
//			if (type.equals("image")) {
//				avatarName = fName.substring(0, fName.lastIndexOf("."))
//						+ "_avatar"
//						+ fName.substring(fName.lastIndexOf("."));
//				avatarName = avatarName.replaceAll(" ", "_");
//				lebah.repository.Thumbnail.create(ResourceBundle.getBundle("dbconnection").getString("folder")
//						+ fName, ResourceBundle.getBundle("dbconnection").getString("folder") + avatarName,
//						150, 90, 100);
//			}
//			
//			updateDokumen(id, fName, avatarName);
//		}
//		return getPath() + "/uploadDokumen.vm";
//	}
//
//
//	private void updateDokumen(String id, String fName, String avatarName) { 
//		userId = (String) request.getSession().getAttribute("_portal_login");
//		mp = new MyPersistence();
//		try {			
//			CmsPenyelenggaraanKuarters cmspk = (CmsPenyelenggaraanKuarters) mp.find(CmsPenyelenggaraanKuarters.class, id);
//			if (cmspk != null) {
//				mp.begin();
//				Util.deleteFile(cmspk.getFileName());
//			//	Util.deleteFile(slideShow.getThumbFileName());
//				cmspk.setFileName(fName);
//			//	slideShow.setThumbFileName(avatarName);
//			//	slideShow.setKemaskiniOleh((Users) mp.find(Users.class, userId));
//			//	slideShow.setTarikhKemaskini(new Date());				
//				mp.commit();
//			}			
//		} catch (Exception ex) {
//			System.out.println("ERROR UPDATE DOKUMEN : " + ex.getMessage());
//			ex.printStackTrace();
//		} finally {
//			if (mp != null) { mp.close(); }
//		}
//	}
//	
	
	
	@SuppressWarnings("rawtypes")
	@Command("uploadGambar")
	public String uploadGambar() throws Exception {
		String id = getParam("idpk");
			
		String uploadDir = "portal/penyelenggaraanKuarters/";
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
			CmsPenyelenggaraanKuarters kontraktor = (CmsPenyelenggaraanKuarters) mp.find(CmsPenyelenggaraanKuarters.class, id);
			if (kontraktor != null) {
				mp.begin();
				kontraktor.setFileName(imgName);
//				pk.setKemaskiniOleh((Users) mp.find(Users.class, userId));
//				pk.setTarikhKemaskini(new Date());				
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
		String id = getParam("idpk");
		CmsPenyelenggaraanKuarters kontraktor = null;
		
		mp = new MyPersistence();
		try {			
			kontraktor = (CmsPenyelenggaraanKuarters) mp.find(CmsPenyelenggaraanKuarters.class, id);
			
			if (kontraktor != null) {
				mp.begin();
				Util.deleteFile(kontraktor.getFileName());
				kontraktor.setFileName(null);
		//		kontraktor.setKemaskiniOleh((Users) mp.find(Users.class, userId));
		//		kontraktor.setTarikhKemaskini(new Date());				
				mp.commit();
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}		
			
		return getKontraktor();
	}
	
	
	@Command("refreshGambar")
	public String refreshGambar() throws Exception {
		String id = getParam("idpk");
		CmsPenyelenggaraanKuarters rf = null;
		mp = new MyPersistence();
		try {			
			rf = (CmsPenyelenggaraanKuarters) mp.find(CmsPenyelenggaraanKuarters.class, id);		
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("r", rf);
		return getPath() + "/entry_page.vm";
	}
}