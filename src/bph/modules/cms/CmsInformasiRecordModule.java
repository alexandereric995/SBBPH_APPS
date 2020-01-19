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
import bph.entities.portal.CmsInformasi;
import bph.entities.portal.CmsSubInformasi;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class CmsInformasiRecordModule extends LebahRecordTemplateModule<CmsInformasi>{

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
	public Class<CmsInformasi> getPersistenceClass() {
		// TODO Auto-generated method stub
		return CmsInformasi.class;
	}
	
	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/cms/informasi";
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
	public void beforeSave() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void save(CmsInformasi r) throws Exception {
		userId = (String) request.getSession().getAttribute("_portal_login");
		
		r.setTajuk(getParam("tajuk"));
		r.setButiran(getParam("butiran"));
		r.setFlagBaru("Y");
		r.setFlagAktif(getParam("flagAktif"));
		r.setDaftarOleh(db.find(Users.class, userId));
		r.setTarikhMasuk(new Date());		
	}

	@Override
	public void afterSave(CmsInformasi r) {
		// TODO Auto-generated method stub
		context.put("selectedTab", 1);
	}	
	
	@Override
	public boolean delete(CmsInformasi r) throws Exception {
		boolean allowDelete = false;
		try {
			mp = new MyPersistence();
			CmsSubInformasi subInformasi = (CmsSubInformasi) db.get("select x from CmsSubInformasi x where x.informasi.id = '" + r.getId() + "'");
			if (subInformasi == null) {
				allowDelete = true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		return allowDelete;
	}	
	
	@Override
	public void getRelatedData(CmsInformasi r) {
		// TODO Auto-generated method stub
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
	@Command("getInformasi")
	public String getInformasi() {
		userId = (String) request.getSession().getAttribute("_portal_login");
		String id = getParam("idInformasi");
		CmsInformasi informasi = null;
		
		mp = new MyPersistence();
		try {			
			informasi = (CmsInformasi) mp.find(CmsInformasi.class, id);			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}		
		
		context.put("r", informasi);
		context.put("selectedTab", "1");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getSubInformasi")
	public String getSubInformasi() {
		userId = (String) request.getSession().getAttribute("_portal_login");
		String id = getParam("idInformasi");
		CmsInformasi informasi = null;
		List <CmsSubInformasi> listSubInformasi = null;
		
		mp = new MyPersistence();
		try {			
			informasi = (CmsInformasi) mp.find(CmsInformasi.class, id);	
			if (informasi != null) {
				listSubInformasi = mp.list("select x from CmsSubInformasi x where x.informasi.id = '" + informasi.getId() + "' order by x.tarikhLuput desc");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}		
		
		context.remove("subInformasi");
		context.put("listSubInformasi", listSubInformasi);
		context.put("r", informasi);
		context.put("selectedTab", "2");
		return getPath() + "/entry_page.vm";
	}
	/** END TAB **/
	
	/** START INFORMASI **/
	@Command("kemaskiniMaklumat")
	public String kemaskiniMaklumat() throws Exception {
		userId = (String) request.getSession().getAttribute("_portal_login");
		String id = getParam("idInformasi");
		CmsInformasi informasi = null;
		
		mp = new MyPersistence();
		try {			
			informasi = (CmsInformasi) mp.find(CmsInformasi.class, id);
			
			if (informasi != null) {
				mp.begin();
				informasi.setTajuk(getParam("tajuk"));
				informasi.setButiran(getParam("butiran"));
				informasi.setFlagBaru("Y");
				informasi.setFlagAktif(getParam("flagAktif"));
				informasi.setKemaskiniOleh((Users) mp.find(Users.class, userId));
				informasi.setTarikhKemaskini(new Date());				
				mp.commit();
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}		
		
		return getInformasi();
	}
	/** END INFORMASI **/
	
	/** START SUBINFORMASI **/
	@Command("daftarSubInformasi")
	public String daftarSubInformasi() {
		context.remove("subInformasi");
		return getPath() + "/subInformasi/maklumatSubInformasi.vm";
	}
	
	@Command("simpanSubInformasi")
	public String simpanSubInformasi() throws Exception {
		String idInformasi = get("idInformasi");
		CmsSubInformasi subInformasi = new CmsSubInformasi();
		String uploadDir = "portal/informasi/";
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
			String imgName = uploadDir + idInformasi + "_" + subInformasi.getId()
					+ fileName.substring(fileName.lastIndexOf("."));

			imgName = imgName.replaceAll(" ", "_");
			item.write(new File(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName));

			if (!imgName.equals("")) {
				simpanSubInformasi(idInformasi, imgName, subInformasi);
			}
		}

		return getPath() + "/subInformasi/uploadDokumen.vm";
	}
	
	public void simpanSubInformasi(String idInformasi, String imgName, CmsSubInformasi subInformasi) throws Exception {
		userId = (String) request.getSession().getAttribute("_portal_login");
		mp = new MyPersistence();
		try {			
			CmsInformasi informasi = (CmsInformasi) mp.find(CmsInformasi.class, idInformasi);
			if (informasi != null) {
				mp.begin();
				subInformasi.setInformasi(informasi);
				subInformasi.setKategori("-");
				subInformasi.setTajuk(getParam("tajukSubInformasi"));
				subInformasi.setTarikhIklan(getDate("tarikhIklanSubInformasi"));
				subInformasi.setTarikhLuput(getDate("tarikhLuputSubInformasi"));
				subInformasi.setFileName(imgName);
				subInformasi.setFlagAktif(getParam("flagAktifSubInformasi"));
				subInformasi.setFlagBaru("Y");
				subInformasi.setDaftarOleh((Users) mp.find(Users.class, userId));
				subInformasi.setTarikhMasuk(new Date());	
				mp.persist(subInformasi);
				
				if (getParam("flagAktifSubInformasi").equals("Y")) {
					informasi.setFlagBaru("Y");
				}
				informasi.setKemaskiniOleh((Users) mp.find(Users.class, userId));
				informasi.setTarikhKemaskini(new Date());
				
				mp.commit();
			}			
		} catch (Exception ex) {
			System.out.println("ERROR INSERT SUBINFORMASI : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
	}
	
	@Command("deleteSubInformasi")
	public String deleteSubInformasi() throws Exception {
		String idSubInformasi = get("idSubInformasi");
		
		try {			
			mp = new MyPersistence();			
			CmsSubInformasi subInformasi = (CmsSubInformasi) mp.find(CmsSubInformasi.class, idSubInformasi);
			if (subInformasi != null) {
				mp.begin();
				CmsInformasi informasi = (CmsInformasi) mp.find(CmsInformasi.class, subInformasi.getInformasi().getId());
				
				Util.deleteFile(subInformasi.getFileName());
				
				mp.remove(subInformasi);
				
				if (informasi != null) {
					informasi.setKemaskiniOleh((Users) mp.find(Users.class, userId));
					informasi.setTarikhKemaskini(new Date());			
				}				
				mp.commit();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}

		return getSubInformasi();
	}
	
	@Command("kemaskiniSubInformasi")
	public String kemaskiniSubInformasi() throws Exception {
		String idSubInformasi = get("idSubInformasiList");
		
		try {			
			mp = new MyPersistence();			
			CmsSubInformasi subInformasi = (CmsSubInformasi) mp.find(CmsSubInformasi.class, idSubInformasi);
			if (subInformasi != null) {
				context.put("subInformasi", subInformasi);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}

		return getPath() + "/subInformasi/maklumatSubInformasi.vm";
	}
	
	@Command("simpanKemaskiniSubInformasi")
	public String simpanKemaskiniSubInformasi() throws Exception {
		try {	
			mp = new MyPersistence();
			String uploadDir = "portal/informasi/";
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

			String idSubInformasi = get("idSubInformasi");
			CmsSubInformasi subInformasi = (CmsSubInformasi) mp.find(CmsSubInformasi.class, idSubInformasi);
			if (subInformasi != null) {
				mp.begin();
				subInformasi.setKategori("-");
				subInformasi.setTajuk(getParam("tajukSubInformasi"));
				subInformasi.setTarikhIklan(getDate("tarikhIklanSubInformasi"));
				subInformasi.setTarikhLuput(getDate("tarikhLuputSubInformasi"));
				
				for (FileItem item : files) {
					String fileName = item.getName();
					String imgName = uploadDir + subInformasi.getInformasi().getId() + "_" + subInformasi.getId()
							+ fileName.substring(fileName.lastIndexOf("."));

					imgName = imgName.replaceAll(" ", "_");
					Util.deleteFile(subInformasi.getFileName());
					item.write(new File(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName));
					
					subInformasi.setFileName(imgName);					
				}
				
				subInformasi.setFlagAktif(getParam("flagAktifSubInformasi"));
				subInformasi.setFlagBaru("Y");
				subInformasi.setKemaskiniOleh((Users) mp.find(Users.class, userId));
				subInformasi.setTarikhKemaskini(new Date());
				
				CmsInformasi informasi = (CmsInformasi) mp.find(CmsInformasi.class, subInformasi.getInformasi().getId());
				if (informasi != null) {
					informasi.setKemaskiniOleh((Users) mp.find(Users.class, userId));
					informasi.setTarikhKemaskini(new Date());
				}					
				mp.commit();
			}			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}		

		return getPath() + "/subInformasi/uploadDokumen.vm";
	}
	/** END SUBINFORMASI **/
}
