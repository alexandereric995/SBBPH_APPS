/**
* AUTHOR : zufazdliabuas@gmail.com
* Date : 14/6/2017
*/

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
import bph.entities.portal.CmsAkrab;
import bph.entities.portal.CmsAkrabAktiviti;
import bph.entities.portal.CmsAkrabGambar;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class CmsAkrabRecordModule extends LebahRecordTemplateModule<CmsAkrab> {

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
	public void afterSave(CmsAkrab r) {
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
		this.setOrderBy("turutan");
		this.setOrderType("asc");
	}

	private void doOverideFilterRecord() {
		// TODO Auto-generated method stub		
	}

	@Override
	public boolean delete(CmsAkrab r) throws Exception {
		boolean allowDelete = false;
		try {
			mp = new MyPersistence();
			CmsAkrabAktiviti aktiviti = (CmsAkrabAktiviti) db.get("select x from CmsAkrabAktiviti x where x.akrab.id = '" + r.getId() + "'");
			if (aktiviti == null) {
				allowDelete = true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		return allowDelete;
//		return false;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/cms/akrab";
	}

	@Override
	public Class<CmsAkrab> getPersistenceClass() {
		// TODO Auto-generated method stub
		return CmsAkrab.class;
	}

	@Override
	public void getRelatedData(CmsAkrab r) {
		// TODO Auto-generated method stub		
		context.put("selectedTab", "1");
	}

	@Override
	public void save(CmsAkrab r) throws Exception {
		// TODO Auto-generated method stub
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		r.setTajuk(getParam("tajuk"));
		r.setKeterangan(getParam("keterangan"));
		r.setFlagAktif(getParam("flagAktif"));
		r.setTurutan(getParam("turutan"));
		r.setDaftarOleh(db.find(Users.class, userId));
		r.setTarikhMasuk(new Date());		
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
	@Command("getAkrab")
	public String getAkrab() {
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		String idAkrab = getParam("idAkrab");
		CmsAkrab akrab = null;
		
		mp = new MyPersistence();
		try {			
			akrab = (CmsAkrab) mp.find(CmsAkrab.class, idAkrab);			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}		
		
		context.put("r", akrab);
		context.put("selectedTab", "1");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getAktiviti")
	public String getAktiviti() {
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		String idAkrab = getParam("idAkrab");
		CmsAkrab akrab = null;
		List <CmsAkrabAktiviti> listAktiviti = null;
		
		mp = new MyPersistence();
		try {			
			akrab = (CmsAkrab) mp.find(CmsAkrab.class, idAkrab);	
			if (akrab != null) {
				listAktiviti = mp.list("select x from CmsAkrabAktiviti x where x.akrab.id = '" + akrab.getId() + "' order by x.tarikhAktiviti asc");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}		
		
		context.remove("aktiviti");
		context.put("listAktiviti", listAktiviti);
		context.put("r", akrab);
		context.put("selectedTab", "2");
		return getPath() + "/entry_page.vm";
	}
	/******************************************** END TAB ********************************************/
	
	/** START MAKLUMAT AKRAB **/
	@Command("kemaskiniMaklumat")
	public String kemaskiniMaklumat() {
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		String id = getParam("idAkrab");
		CmsAkrab akrab = null;
		
		mp = new MyPersistence();
		try {			
			akrab = (CmsAkrab) mp.find(CmsAkrab.class, id);	
			if (akrab != null) {
				mp.begin();
				akrab.setTajuk(getParam("tajuk"));
				akrab.setKeterangan(getParam("keterangan"));
				akrab.setFlagAktif(getParam("flagAktif"));
				akrab.setTurutan(getParam("turutan"));
				akrab.setKemaskiniOleh((Users) mp.find(Users.class, userId));
				akrab.setTarikhKemaskini(new Date());
				mp.commit();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}		
		
		return getAkrab();
	}
	/** END MAKLUMAT AKRAB **/
	
	/** START MAKLUMAT AKTIVITI **/
	@Command("daftarAktiviti")
	public String daftarAktiviti() {
		
		String idAkrab = getParam("idAkrab");
		CmsAkrab akrab = null;
		
		mp = new MyPersistence();
		try {			
			akrab = (CmsAkrab) mp.find(CmsAkrab.class, idAkrab);	
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}	
		
		context.remove("aktviti");	
		context.put("r", akrab);
		context.put("selectedTab", "2");
		return getPath() + "/entry_page.vm";
	}
		
	@Command("simpanAktiviti")
	public String simpanAktiviti() {
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		String idAkrab = getParam("idAkrab");
		String idAktiviti = getParam("idAktiviti");
		CmsAkrab akrab = null;
		CmsAkrabAktiviti aktiviti = null;
		List<CmsAkrabGambar> listGambar = null;
		boolean addAktiviti = false;
		
		mp = new MyPersistence();
		try {	
			mp.begin();
			akrab = (CmsAkrab) mp.find(CmsAkrab.class, idAkrab);	
			
			aktiviti = (CmsAkrabAktiviti) mp.find(CmsAkrabAktiviti.class, idAktiviti);
			if (aktiviti == null) {
				aktiviti = new CmsAkrabAktiviti();
				addAktiviti = true;
			}
			aktiviti.setAkrab(akrab);
			aktiviti.setNamaAktiviti(getParam("namaAktiviti"));
			aktiviti.setKeterangan(getParam("keterangan"));
			aktiviti.setTarikhAktiviti(getDate("tarikhAktiviti"));
			aktiviti.setFlagAktif(getParam("flagAktif"));
			if (addAktiviti) {
				aktiviti.setDaftarOleh((Users) mp.find(Users.class, userId));
				aktiviti.setTarikhMasuk(new Date());
				mp.persist(aktiviti);
			} else {
				aktiviti.setKemaskiniOleh((Users) mp.find(Users.class, userId));
				aktiviti.setTarikhKemaskini(new Date());
			}
			mp.commit();
			
			if (aktiviti != null) {
				listGambar = mp.list("select x from CmsAkrabGambar x where x.aktiviti.id = '" + aktiviti.getId() + "' order by x.id asc");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}		
		
		context.put("listGambar", listGambar);
		context.put("aktiviti", aktiviti);
		context.put("r", akrab);
		context.put("selectedTab", "2");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("kemaskiniAktiviti")
	public String kemaskiniAktiviti() {
		
		String idAktiviti = getParam("idAktiviti");
		CmsAkrabAktiviti aktiviti = null;
		List<CmsAkrabGambar> listGambar = null;
		CmsAkrab akrab = null;
		
		mp = new MyPersistence();
		try {			
			aktiviti = (CmsAkrabAktiviti) mp.find(CmsAkrabAktiviti.class, idAktiviti);	
			if (aktiviti != null) {
				akrab = aktiviti.getAkrab();
				listGambar = mp.list("select x from CmsAkrabGambar x where x.aktiviti.id = '" + aktiviti.getId() + "' order by x.id asc");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}	
		
		context.put("listGambar", listGambar);
		context.put("aktiviti", aktiviti);
		context.put("r", akrab);
		context.put("selectedTab", "2");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("deleteAktiviti")
	public String deleteAktiviti() {
		
		String idAktiviti = getParam("idAktiviti");
		CmsAkrabAktiviti aktiviti = null;
		List<CmsAkrabGambar> listGambar = null;
		
		mp = new MyPersistence();
		try {			
			mp.begin();
			aktiviti = (CmsAkrabAktiviti) mp.find(CmsAkrabAktiviti.class, idAktiviti);	
			if (aktiviti != null) {
				listGambar = mp.list("select x from CmsAkrabGambar x where x.aktiviti.id = '" + aktiviti.getId() + "' order by x.id asc");
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
		CmsAkrabGambar gambar = new CmsAkrabGambar();
		String uploadDir = "portal/akrab/";
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
	
	public void simpanGambar(String idAktiviti, String imgName, String avatarName, CmsAkrabGambar gambar) throws Exception {
		userId = (String) request.getSession().getAttribute("_portal_login");
		mp = new MyPersistence();
		try {			
			CmsAkrabAktiviti aktiviti = (CmsAkrabAktiviti) mp.find(CmsAkrabAktiviti.class, idAktiviti);
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
			CmsAkrabGambar gambar = (CmsAkrabGambar) mp.find(CmsAkrabGambar.class, idGambar);
			if (gambar != null) {
				mp.begin();
				CmsAkrabAktiviti aktiviti = (CmsAkrabAktiviti) mp.find(CmsAkrabAktiviti.class, gambar.getAktiviti().getId());
				
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
