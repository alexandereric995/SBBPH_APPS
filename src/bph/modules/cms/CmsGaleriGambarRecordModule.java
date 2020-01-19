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

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import portal.module.entity.Users;
import bph.entities.portal.CmsGaleri;
import bph.entities.portal.CmsSubGaleri;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class CmsGaleriGambarRecordModule extends LebahRecordTemplateModule<CmsGaleri>{

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
		return "bph/modules/cms/galeriGambar";
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
		this.addFilter("kategori = '01'"); // GAMBAR
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
		
		r.setKategori("01");
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
		boolean allowDelete = false;
		try {
			mp = new MyPersistence();
			CmsSubGaleri subGaleri = (CmsSubGaleri) db.get("select x from CmsSubGaleri x where x.galeri.id = '" + r.getId() + "'");
			if (subGaleri == null) {
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
	public void getRelatedData(CmsGaleri r) {
		List<CmsSubGaleri> listSubGaleri = null;
		mp = new MyPersistence();
		try {			
			listSubGaleri = mp.list("select x from CmsSubGaleri x where x.galeri.id = '" + r.getId() + "'");
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("listSubGaleri", listSubGaleri);
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
				galeri.setKategori("01");
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
	
	/** START GAMBAR **/
	@Command("uploadGambar")
	public String uploadGambar() throws Exception {
		String id = get("id");
		CmsSubGaleri subGaleri = new CmsSubGaleri();
		String uploadDir = "portal/galeri/gambar/";
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
			String imgName = uploadDir + id + "_" + subGaleri.getId()
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
//				lebah.repository.Thumbnail.create(ResourceBundle.getBundle("dbconnection").getString("folder")
//						+ imgName, ResourceBundle.getBundle("dbconnection").getString("folder") + imgName,
//						600, 560, 100);
				lebah.repository.Thumbnail.create(ResourceBundle.getBundle("dbconnection").getString("folder")
						+ imgName, ResourceBundle.getBundle("dbconnection").getString("folder") + avatarName,
						150, 90, 100);
			}

			if (!imgName.equals("")) {
				simpanGambar(id, imgName, avatarName, subGaleri);
			}
		}

		return getPath() + "/subGaleri/uploadGambar.vm";
	}

	public void simpanGambar(String id, String imgName, String avatarName, CmsSubGaleri subGaleri) throws Exception {
		userId = (String) request.getSession().getAttribute("_portal_login");
		mp = new MyPersistence();
		try {			
			CmsGaleri galeri = (CmsGaleri) mp.find(CmsGaleri.class, id);
			if (galeri != null) {
				mp.begin();
				subGaleri.setGaleri(galeri);
				subGaleri.setFileName(imgName);
				subGaleri.setThumbFileName(avatarName);
				subGaleri.setDaftarOleh((Users) mp.find(Users.class, userId));
				subGaleri.setTarikhMasuk(new Date());	
				mp.persist(subGaleri);
				
				galeri.setKemaskiniOleh((Users) mp.find(Users.class, userId));
				galeri.setTarikhKemaskini(new Date());			
				mp.commit();
			}			
		} catch (Exception ex) {
			System.out.println("ERROR UPDATE GAMBAR : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
	}

	@Command("deleteGambar")
	public String deleteGambar() throws Exception {
		String idGambar = get("idGambar");
		
		try {			
			mp = new MyPersistence();			
			CmsSubGaleri subGaleri = (CmsSubGaleri) mp.find(CmsSubGaleri.class, idGambar);
			if (subGaleri != null) {
				mp.begin();
				CmsGaleri galeri = (CmsGaleri) mp.find(CmsGaleri.class, subGaleri.getGaleri().getId());
				
				Util.deleteFile(subGaleri.getFileName());
				Util.deleteFile(subGaleri.getThumbFileName());
				
				mp.remove(subGaleri);
				
				if (galeri != null) {
					galeri.setKemaskiniOleh((Users) mp.find(Users.class, userId));
					galeri.setTarikhKemaskini(new Date());			
				}				
				mp.commit();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}

		return refreshList();
	}

	@Command("refreshList")
	public String refreshList() throws Exception {
		String id = getParam("id");
		CmsGaleri galeri = null;
		List<CmsSubGaleri> listSubGaleri = null;
		mp = new MyPersistence();
		try {			
			galeri = (CmsGaleri) mp.find(CmsGaleri.class, id);
			listSubGaleri = mp.list("select x from CmsSubGaleri x where x.galeri.id = '" + id + "'");
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("r", galeri);
		context.put("listSubGaleri", listSubGaleri);
		return getPath() + "/entry_page.vm";
	}
	/** END GAMBAR **/
}
