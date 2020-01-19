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
import bph.entities.portal.CmsKeharta;
import bph.entities.portal.CmsKehartaAktiviti;
import bph.entities.portal.CmsKehartaGambarAktiviti;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class CmsKehartaRecordModule extends LebahRecordTemplateModule<CmsKeharta> {

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
	public void afterSave(CmsKeharta r) {
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
		this.setOrderBy("tahun");
		this.setOrderType("desc");
	}

	private void doOverideFilterRecord() {
		// TODO Auto-generated method stub		
	}

	@Override
	public boolean delete(CmsKeharta r) throws Exception {
		boolean allowDelete = false;
		try {
			mp = new MyPersistence();
			CmsKehartaAktiviti aktiviti = (CmsKehartaAktiviti) db.get("select x from CmsKehartaAktiviti x where x.keharta.id = '" + r.getId() + "'");
			if (aktiviti == null) {
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
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/cms/keharta";
	}

	@Override
	public Class<CmsKeharta> getPersistenceClass() {
		// TODO Auto-generated method stub
		return CmsKeharta.class;
	}

	@Override
	public void getRelatedData(CmsKeharta r) {
		// TODO Auto-generated method stub		
		context.put("selectedTab", "1");
	}

	@Override
	public void save(CmsKeharta r) throws Exception {
		// TODO Auto-generated method stub
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		r.setTahun(getParamAsInteger("tahun"));
		r.setKeterangan(getParam("keterangan"));
		r.setFlagAktif(getParam("flagAktif"));
			
		r.setDaftarOleh(db.find(Users.class, userId));
		r.setTarikhMasuk(new Date());		
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("tahun", get("findTahun").trim());
		map.put("flagAktif", new OperatorEqualTo(get("findFlagAktif")));
		
		return map;
	}
	
	
	/******************************************* START TAB ********************************************/
	@Command("getKeharta")
	public String getKeharta() {
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		String idKeharta = getParam("idKeharta");
		CmsKeharta keharta = null;
		
		mp = new MyPersistence();
		try {			
			keharta = (CmsKeharta) mp.find(CmsKeharta.class, idKeharta);			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}		
		
		context.put("r", keharta);
		context.put("selectedTab", "1");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getAktiviti")
	public String getAktiviti() {
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		String idKeharta = getParam("idKeharta");
		CmsKeharta keharta = null;
		List <CmsKehartaAktiviti> listAktiviti = null;
		
		mp = new MyPersistence();
		try {			
			keharta = (CmsKeharta) mp.find(CmsKeharta.class, idKeharta);	
			if (keharta != null) {
				listAktiviti = mp.list("select x from CmsKehartaAktiviti x where x.keharta.id = '" + keharta.getId() + "' order by x.tarikhAktiviti asc");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}		
		
		context.remove("aktiviti");
		context.put("listAktiviti", listAktiviti);
		context.put("r", keharta);
		context.put("selectedTab", "2");
		return getPath() + "/entry_page.vm";
	}
	/******************************************** END TAB ********************************************/
	
	/** START MAKLUMAT KEHARTA **/
	@Command("kemaskiniMaklumat")
	public String kemaskiniMaklumat() {
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		String id = getParam("idKeharta");
		CmsKeharta keharta = null;
		
		mp = new MyPersistence();
		try {			
			keharta = (CmsKeharta) mp.find(CmsKeharta.class, id);	
			if (keharta != null) {
				mp.begin();
				keharta.setTahun(getParamAsInteger("tahun"));
				keharta.setKeterangan(getParam("keterangan"));
				keharta.setFlagAktif(getParam("flagAktif"));
					
				keharta.setKemaskiniOleh(db.find(Users.class, userId));
				keharta.setTarikhKemaskini(new Date());
				mp.commit();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}		
		
		return getKeharta();
	}
	/** END MAKLUMAT KEHARTA **/
	
	/** START MAKLUMAT AKTIVITI **/
	@Command("daftarAktiviti")
	public String daftarAktiviti() {
		
		String idKeharta = getParam("idKeharta");
		CmsKeharta keharta = null;
		
		mp = new MyPersistence();
		try {			
			keharta = (CmsKeharta) mp.find(CmsKeharta.class, idKeharta);	
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}	
		
		context.remove("aktviti");	
		context.put("r", keharta);
		context.put("selectedTab", "2");
		return getPath() + "/entry_page.vm";
	}
		
	@Command("simpanAktiviti")
	public String simpanAktiviti() {
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		String idKeharta = getParam("idKeharta");
		String idAktiviti = getParam("idAktiviti");
		CmsKeharta keharta = null;
		CmsKehartaAktiviti aktiviti = null;
		List<CmsKehartaGambarAktiviti> listGambar = null;
		boolean addAktiviti = false;
		
		mp = new MyPersistence();
		try {	
			mp.begin();
			keharta = (CmsKeharta) mp.find(CmsKeharta.class, idKeharta);	
			
			aktiviti = (CmsKehartaAktiviti) mp.find(CmsKehartaAktiviti.class, idAktiviti);
			if (aktiviti == null) {
				aktiviti = new CmsKehartaAktiviti();
				addAktiviti = true;
			}
			aktiviti.setKeharta(keharta);
			aktiviti.setNamaAktiviti(getParam("namaAktiviti"));
			aktiviti.setKeterangan(getParam("keterangan"));
			aktiviti.setTarikhAktiviti(getDate("tarikhAktiviti"));
			aktiviti.setFlagAktif(getParam("flagAktif"));
			if (addAktiviti) {
				aktiviti.setDaftarOleh(db.find(Users.class, userId));
				aktiviti.setTarikhMasuk(new Date());
				mp.persist(aktiviti);
			} else {
				aktiviti.setKemaskiniOleh(db.find(Users.class, userId));
				aktiviti.setTarikhKemaskini(new Date());
			}
			mp.commit();
			
			if (aktiviti != null) {
				listGambar = mp.list("select x from CmsKehartaGambarAktiviti x where x.aktiviti.id = '" + aktiviti.getId() + "' order by x.id asc");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}		
		
		context.put("listGambar", listGambar);
		context.put("aktiviti", aktiviti);
		context.put("r", keharta);
		context.put("selectedTab", "2");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("kemaskiniAktiviti")
	public String kemaskiniAktiviti() {
		
		String idAktiviti = getParam("idAktiviti");
		CmsKehartaAktiviti aktiviti = null;
		List<CmsKehartaGambarAktiviti> listGambar = null;
		CmsKeharta keharta = null;
		
		mp = new MyPersistence();
		try {			
			aktiviti = (CmsKehartaAktiviti) mp.find(CmsKehartaAktiviti.class, idAktiviti);	
			if (aktiviti != null) {
				keharta = aktiviti.getKeharta();
				listGambar = mp.list("select x from CmsKehartaGambarAktiviti x where x.aktiviti.id = '" + aktiviti.getId() + "' order by x.id asc");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}	
		
		context.put("listGambar", listGambar);
		context.put("aktiviti", aktiviti);
		context.put("r", keharta);
		context.put("selectedTab", "2");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("deleteAktiviti")
	public String deleteAktiviti() {
		
		String idAktiviti = getParam("idAktiviti");
		CmsKehartaAktiviti aktiviti = null;
		List<CmsKehartaGambarAktiviti> listGambar = null;
		
		mp = new MyPersistence();
		try {			
			mp.begin();
			aktiviti = (CmsKehartaAktiviti) mp.find(CmsKehartaAktiviti.class, idAktiviti);	
			if (aktiviti != null) {
				listGambar = mp.list("select x from CmsKehartaGambarAktiviti x where x.aktiviti.id = '" + aktiviti.getId() + "' order by x.id asc");
				for (int i = 0; i < listGambar.size(); i++) {
					util.deleteFile(listGambar.get(i).getFileName());
					util.deleteFile(listGambar.get(i).getAvatarName());
					mp.remove(listGambar.get(i));
				}
				mp.remove(aktiviti);
			}
			mp.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}	
		
		return getAktiviti();
	}
	/** END MAKLUMAT AKTIVITI **/
	
	/** START GAMBAR **/
	@Command("uploadGambar")
	public String uploadGambar() throws Exception {
		String idAktiviti = get("idAktiviti");
		CmsKehartaGambarAktiviti gambar = new CmsKehartaGambarAktiviti();
		String uploadDir = "portal/keharta/";
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
			String imgName = uploadDir + idAktiviti + "_" + gambar.getId()
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
				simpanGambar(idAktiviti, imgName, avatarName, gambar);
			}
		}

		return getPath() + "/aktiviti/uploadGambar.vm";
	}
	
	public void simpanGambar(String idAktiviti, String imgName, String avatarName, CmsKehartaGambarAktiviti gambar) throws Exception {
		userId = (String) request.getSession().getAttribute("_portal_login");
		mp = new MyPersistence();
		try {			
			CmsKehartaAktiviti aktiviti = (CmsKehartaAktiviti) mp.find(CmsKehartaAktiviti.class, idAktiviti);
			if (aktiviti != null) {
				mp.begin();
				gambar.setAktiviti(aktiviti);
				gambar.setFileName(imgName);
				gambar.setAvatarName(avatarName);
				mp.persist(gambar);
				
				aktiviti.setKemaskiniOleh((Users) mp.find(Users.class, userId));
				aktiviti.setTarikhKemaskini(new Date());			
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
			CmsKehartaGambarAktiviti gambar = (CmsKehartaGambarAktiviti) mp.find(CmsKehartaGambarAktiviti.class, idGambar);
			if (gambar != null) {
				mp.begin();
				CmsKehartaAktiviti aktiviti = (CmsKehartaAktiviti) mp.find(CmsKehartaAktiviti.class, gambar.getAktiviti().getId());
				
				Util.deleteFile(gambar.getFileName());
				Util.deleteFile(gambar.getAvatarName());
				
				mp.remove(gambar);
				
				if (aktiviti != null) {
					aktiviti.setKemaskiniOleh((Users) mp.find(Users.class, userId));
					aktiviti.setTarikhKemaskini(new Date());			
				}				
				mp.commit();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}

		return kemaskiniAktiviti();
	}
	/** END GAMBAR **/
}
