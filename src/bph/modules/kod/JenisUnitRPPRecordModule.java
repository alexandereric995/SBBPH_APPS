package bph.modules.kod;

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

import bph.entities.kod.JenisDokumen;
import bph.entities.kod.JenisUnitRPP;
import bph.entities.rpp.RppGambarJenisUnit;
import bph.entities.rpp.RppPenyeliaPeranginan;
import bph.entities.rpp.RppPeranginan;
import bph.utils.DataUtil;
import bph.utils.Util;

public class JenisUnitRPPRecordModule extends LebahRecordTemplateModule<JenisUnitRPP> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;

	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public void afterSave(JenisUnitRPP r) {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
	}

	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		userName = (String) request.getSession().getAttribute("_portal_username");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		
		context.put("selectPeranginan", dataUtil.getListPeranginanRpp());
		
		addfilter();
		defaultButtonOption();
		
		context.put("userRole", userRole);
		context.put("util", new Util());
		context.put("path", getPath());
	}
	
	private void defaultButtonOption() {
		this.setDisableDefaultButton(true);
		this.setDisableBackButton(true);
		this.setDisableKosongkanUpperButton(true);
		this.setDisableSaveAddNewButton(true);
	}
	
	private void addfilter() {
		//Users users = db.find(Users.class, userId);
		
		RppPenyeliaPeranginan penyelia = (RppPenyeliaPeranginan) db.get("select x from RppPenyeliaPeranginan x where x.statusPerkhidmatan = 'Y' and x.penyelia.id = '" + userId + "'");
		
		if ("(RPP) Penyelia".equals(userRole)){
			if (penyelia != null)
				this.addFilter("peranginan.id = '" + penyelia.getPeranginan().getId() + "'");
		}
		
		this.setOrderBy("keterangan");
		this.setOrderType("asc");
	}

	@Override
	public boolean delete(JenisUnitRPP r) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/kod/jenisUnitRPP";
	}

	@Override
	public Class<JenisUnitRPP> getPersistenceClass() {
		// TODO Auto-generated method stub
		return JenisUnitRPP.class;
	}

	@Override
	public void getRelatedData(JenisUnitRPP r) {
		// TODO Auto-generated method stub
	}

	@Override
	public void save(JenisUnitRPP r) throws Exception {
		RppPenyeliaPeranginan penyelia = (RppPenyeliaPeranginan) db.get("select x from RppPenyeliaPeranginan x where x.penyelia.id = '" + userId + "'");
		if ("(RPP) Penyelia".equals(userRole)) {
			r.setPeranginan(penyelia.getPeranginan());
		} else {
			r.setPeranginan(db.find(RppPeranginan.class, getParam("idPeranginan")));
		}
		
		r.setKeterangan(get("keterangan"));
		r.setHadDewasa(getParamAsInteger("hadDewasa"));
		r.setHadKanakKanak(getParamAsInteger("hadKanakKanak"));
		r.setHadKuantiti(getParamAsInteger("hadKuantiti"));
		r.setKadarSewa(getParamAsDouble("kadarSewa"));
		r.setGredMinimumKelayakan(getParamAsInteger("gredMinimumKelayakan"));
		r.setKadarSewaWaktuPuncak(getParamAsDouble("kadarSewaWaktuPuncak"));
		r.setGredKelayakanWaktuPuncak(getParamAsInteger("gredKelayakanWaktuPuncak"));
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		if (!"(RPP) Penyelia".equals(userRole)) {
			map.put("peranginan.id", new OperatorEqualTo(get("findPeranginan")));
		}			
		map.put("keterangan", get("findKeterangan"));

		return map;
	}
	
	@Command("getMaklumatRekod")
	public String getMaklumatRekod() throws Exception {
		context.put("r",getDataJenisUnitRPP());
		return getPath() + "/form/maklumatRekod.vm";
	}
	
	@SuppressWarnings("unchecked")
	@Command("getGambar")
	public String getGambar() throws Exception {
		
		List<RppGambarJenisUnit> listGambar = db.list("select x from RppGambarJenisUnit x where x.jenisUnit.id = '"+getParam("idJenisUnitRpp")+"' ");
		context.put("listGambar",listGambar);
		
		context.put("r",getDataJenisUnitRPP());
		return getPath() + "/form/gambar.vm";
	}
	
	@SuppressWarnings("rawtypes")
	@Command("uploadDoc")
	public String uploadPhoto() throws Exception {
		String idJenisUnitRpp = getParam("idJenisUnitRpp");
		String tajuk = getParam("tajuk");
		String idJenisDokumen = "01"; // 01 = GAMBAR
		String keterangan = getParam("keteranganDokumen");
		System.out.println("1keterangan "+keterangan);
		//String uploadDir = ResourceBundle.getBundle("dbconnection").getString("folder") + "/rpp/jenisUnitRpp/";
		String uploadDir = "/rpp/jenisUnitRpp/";
		File dir = new File(ResourceBundle.getBundle("dbconnection").getString("folder") + uploadDir);
		if ( !dir.exists() ) dir.mkdir();
		
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		List items = upload.parseRequest(request);
		Iterator itr = items.iterator();
		List<FileItem> files = new ArrayList<FileItem>();
		while (itr.hasNext()) {
			FileItem item = (FileItem)itr.next();
			if ((!(item.isFormField())) && (item.getName() != null) && (!("".equals(item.getName())))) {
				files.add(item);
			}
		}

		for ( FileItem item : files ) {
			String avatarName = "";
			String fileName = item.getName();
			String imgName = uploadDir + fileName;
			
			imgName = imgName.replaceAll(" ", "_");
			item.write(new File(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName));

			String mimetype= item.getContentType();
	        String type = mimetype.split("/")[0];
	        if(type.equals("image"))
	        {
	        	avatarName = imgName.substring(0, imgName.lastIndexOf(".")) + "_dev" + imgName.substring(imgName.lastIndexOf("."));
				avatarName = avatarName.replaceAll(" ", "_");
	        	lebah.repository.Thumbnail.create(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName, ResourceBundle.getBundle("dbconnection").getString("folder") + imgName, 600, 560, 100);
				lebah.repository.Thumbnail.create(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName, ResourceBundle.getBundle("dbconnection").getString("folder") + avatarName, 150, 90, 100);
	        }
			
			if(!imgName.equals("")) {
				simpanDokumen(idJenisUnitRpp, imgName, avatarName, tajuk, idJenisDokumen, keterangan);
			}
		}
		
		return getPath() + "/form/uploadDoc.vm";
	}
	
	public void simpanDokumen(String idJenisUnitRpp, String imgName, String avatarName, String tajuk, String idJenisDokumen, String keterangan) throws Exception {
		RppGambarJenisUnit a = new RppGambarJenisUnit();
		System.out.println("2keterangan "+keterangan);
		db.begin();
		a.setJenisUnit(db.find(JenisUnitRPP.class, idJenisUnitRpp));
		a.setPhotofilename(imgName);
		a.setThumbfilename(avatarName);	
		a.setTajuk(tajuk);
		a.setJenisDokumen(db.find(JenisDokumen.class, idJenisDokumen));
		a.setKeterangan(keterangan);
		
		db.persist(a);
		db.commit();
	}
	
	@SuppressWarnings("unchecked")
	@Command("refreshList")
	public String refreshList() throws Exception {
		List<RppGambarJenisUnit> listGambar = db.list("select x from RppGambarJenisUnit x where x.jenisUnit.id = '"+getParam("idJenisUnitRpp")+"' ");
		context.put("listGambar",listGambar);
		return getPath() + "/form/listGambar.vm";
	}
	
	@Command("deleteDokumen")
	public String deleteDokumen() throws Exception {
		
		RppGambarJenisUnit rg = db.find(RppGambarJenisUnit.class, getParam("dokumenId"));
		db.begin();
		db.remove(rg);
		db.commit();
		
		return refreshList();
	}
	
	//SHARED
	public JenisUnitRPP getDataJenisUnitRPP(){
		context.remove("r");
		JenisUnitRPP r = db.find(JenisUnitRPP.class, getParam("idJenisUnitRpp"));
		return r;
	}
	
}
