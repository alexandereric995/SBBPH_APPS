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
import bph.entities.portal.CmsPautan;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class CmsPautanRecordModule extends LebahRecordTemplateModule<CmsPautan> {

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
	public void afterSave(CmsPautan r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void begin() {
		// TODO Auto-generated method stub
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
	public boolean delete(CmsPautan rekod) throws Exception {
		return true;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/cms/pautan";
	}

	@Override
	public Class<CmsPautan> getPersistenceClass() {
		// TODO Auto-generated method stub
		return CmsPautan.class;
	}

	@Override
	public void getRelatedData(CmsPautan rekod) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void save(CmsPautan simpan) throws Exception {
		
		userId = (String) request.getSession().getAttribute("_portal_login");		
		simpan.setTajuk(getParam("tajuk"));
		simpan.setUrl(getParam("url"));
		simpan.setFlagAktif(getParam("flagAktif"));
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
	
	/************************************* START FUNCTION PAUTAN *************************************/
	@Command("kemaskiniMaklumat")
	public String kemaskiniMaklumat() throws Exception {
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		String id = getParam("id");
		CmsPautan pautan = null;
		
		mp = new MyPersistence();
		try {			
			pautan = (CmsPautan) mp.find(CmsPautan.class, id);
			
			if (pautan != null) {
				mp.begin();
				pautan.setTajuk(getParam("tajuk"));
				pautan.setUrl(getParam("url"));
				pautan.setFlagAktif(getParam("flagAktif"));
				pautan.setKemaskiniOleh((Users) mp.find(Users.class, userId));
				pautan.setTarikhKemaskini(new Date());				
				mp.commit();
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}		
		
		context.put("r", pautan);
		return getPath() + "/entry_page.vm";
	}
	/************************************* END FUNCTION PAUTAN *************************************/
	
	@SuppressWarnings("rawtypes")
	@Command("uploadGambar")
	public String uploadGambar() throws Exception {
		String id = getParam("id");
		userId = (String) request.getSession().getAttribute("_portal_login");
		mp = new MyPersistence();

		try {			
			CmsPautan pautan = (CmsPautan) mp.find(CmsPautan.class, id);
			if (pautan != null) {
				mp.begin();
				Util.deleteFile(pautan.getFileName());
				Util.deleteFile(pautan.getAvatarName());
							
				String uploadDir = "portal/pautan/";
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
		
					String imgName = uploadDir + id + "_"+ UID.getUID() + fileName.substring(fileName.lastIndexOf("."));
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
								53, 52, 100);
					}
					
					pautan.setFileName(imgName);
					pautan.setAvatarName(avatarName);	
				}
				pautan.setKemaskiniOleh((Users) mp.find(Users.class, userId));
				pautan.setTarikhKemaskini(new Date());			
				mp.commit();
			}			
		} catch (Exception ex) {
			System.out.println("ERROR UPDATE GAMBAR : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/uploadGambar.vm";
	}
	
	@Command("refreshGambar")
	public String refreshGambar() throws Exception {
		
		String id = getParam("id");
		CmsPautan pautan = null;
		mp = new MyPersistence();
		try {			
			pautan = (CmsPautan) mp.find(CmsPautan.class, id);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("r", pautan);
		return getPath() + "/entry_page.vm";
	}
}
