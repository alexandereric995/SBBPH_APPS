package bph.modules.rpp;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import portal.module.entity.Users;
import bph.entities.kod.JenisDokumen;
import bph.entities.kod.Status;
import bph.entities.rpp.RppAduanKerosakan;
import bph.entities.rpp.RppGambarAduan;
import bph.entities.rpp.RppTetapanBarangDeposit;
import bph.utils.DataUtil;
import bph.utils.Util;
import bph.utils.UtilRpp;
import db.persistence.MyPersistence;

public class SenaraiAduanKerosakanRecordModule extends LebahRecordTemplateModule<RppAduanKerosakan> {

	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;
	private MyPersistence mp;
	
	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public void afterSave(RppAduanKerosakan r) {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public Class<RppAduanKerosakan> getPersistenceClass() {
		// TODO Auto-generated method stub
		return RppAduanKerosakan.class;
	}
	
	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/rpp/senaraiAduanKerosakan";
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("barangDeposit.id", getParam("findBarangDeposit"));	
		map.put("tarikhAduan", getDate("findTarikhAduan"));
		map.put("status.id", getParam("findStatus"));
		return map;
	}
	
	@Override
	public boolean delete(RppAduanKerosakan r) throws Exception {
		boolean val=false;
		if(userRole.equalsIgnoreCase("(RPP) Penyemak") || userRole.equalsIgnoreCase("(RPP) Pelulus"))
		{	
			val = true;		
		}
		return val;
	}
	
	@Override
	public void begin() {

		dataUtil = DataUtil.getInstance(db);
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		userName = (String) request.getSession().getAttribute("_portal_username");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		
		try {
			mp = new MyPersistence();
			
			defaultButtonOption();
			addfilter(mp,userId);
			
			//use by HQ only
			hQdefaultButtonOption();
			
			context.put("userRole", userRole);
			context.put("listBarangDeposit", dataUtil.getListBarangDeposit());
			context.put("listStatusAduanKerosakan", dataUtil.getListStatusAduan());
			context.put("command", command);
			context.put("path", getPath());
			context.put("util", new Util());
			context.put("uploadDir", ResourceBundle.getBundle("dbconnection").getString("folder"));
			
		} catch (Exception e) {
			System.out.println("Error begin : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}

	}

	//OVERWRITE BY HQAduanRecordModule
	public void hQdefaultButtonOption() {/*DO NOTHING*/}

	public void addfilter(MyPersistence mp,String userId) {
		
		String idrp = "";
		if(userRole.equalsIgnoreCase("(RPP) Penyelia")){
			idrp = UtilRpp.getPeranginanByIdPenyelia(mp,userId)!=null?UtilRpp.getPeranginanByIdPenyelia(mp,userId).getId():null;
			if(idrp!=null && !idrp.equalsIgnoreCase("")){
				this.setDisableAddNewRecordButton(false);
			}else{
				this.setDisableAddNewRecordButton(true);
			}
		}
		
		this.addFilter("peranginan.id = '" + idrp + "'");
		//this.addFilter("permohonan IS NULL "); //display yang tidak berkaitan tempahan.
	}
	
	public void defaultButtonOption() {
		this.setDisableDefaultButton(true);
		this.setDisableBackButton(true);
		this.setDisableKosongkanUpperButton(true);
		this.setDisableSaveAddNewButton(true);
	}

	@Override
	public void getRelatedData(RppAduanKerosakan r) {
		//context.put("selectNoTempahan", dataUtil.getListNoTempahanRpp());
	}
	
	@Command("getMaklumatAduan")
	public String getMaklumatAduan() throws Exception {
		context.put("r", db.find(RppAduanKerosakan.class, getParam("idAduanKerosakan")));
		return getPath() + "/form/maklumatAduan.vm";
	}

	@Override
	public void save(RppAduanKerosakan r) throws Exception {

		userId = (String) request.getSession().getAttribute("_portal_login");
		
		try {
			
			mp = new MyPersistence();
			
			r.setPeranginan(UtilRpp.getPeranginanByIdPenyelia(mp, userId));
			r.setTarikhAduan(getDate("tarikhAduan"));
			r.setBarangDeposit((RppTetapanBarangDeposit) mp.find(RppTetapanBarangDeposit.class, getParam("barangDeposit")));
			r.setKeterangan(get("keterangan"));
			r.setKuantiti(getParamAsInteger("kuantiti"));
			r.setHarga(Util.getDoubleRemoveComma(getParam("harga")));
			r.setPengadu((Users) mp.find(Users.class, userId));
			
			if(r.getStatus()==null){
				r.setStatus((Status) mp.find(Status.class,"1429870728744")); //ADUAN BARU
			}
			
		} catch (Exception e) {
			System.out.println("Error begin : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}	
			
	}
	
	
	@SuppressWarnings("unchecked")
	@Command("getGambar")
	public String getGambar() throws Exception {
		List<RppGambarAduan> listGambar = db.list("select x from RppGambarAduan x where x.aduanKerosakan.id = '"+getParam("idAduanKerosakan")+"' ");
		context.put("listGambar",listGambar);
		
		RppAduanKerosakan r = db.find(RppAduanKerosakan.class, getParam("idAduanKerosakan"));
		context.put("r",r);
		return getPath() + "/form/gambar.vm";
	}
	
	@SuppressWarnings("rawtypes")
	@Command("uploadDoc")
	public String uploadDoc() throws Exception {
		String idAduanKerosakan = getParam("idAduanKerosakan");
		String tajuk = getParam("tajuk");
		String idJenisDokumen = "01"; // 01 = GAMBAR
		String keterangan = getParam("keteranganDokumen");

		//String uploadDir = ResourceBundle.getBundle("dbconnection").getString("folder") + "/rpp/aduanKerosakan/";
		String uploadDir = "/rpp/aduanKerosakan/";
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
			String yearMonthDayHourMinSec = Util.getCurrentDate("yyMMddHHmmss");
			String imgName = uploadDir + yearMonthDayHourMinSec+"_"+fileName;

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
				simpanDokumen(idAduanKerosakan, imgName, avatarName, tajuk, idJenisDokumen, keterangan);
			}
		}
		
		return getPath() + "/form/uploadDoc.vm";
	}
	
	public void simpanDokumen(String idAduanKerosakan, String imgName, String avatarName, String tajuk, String idJenisDokumen, String keterangan) throws Exception {
		RppGambarAduan a = new RppGambarAduan();

		db.begin();
		a.setAduanKerosakan(db.find(RppAduanKerosakan.class, idAduanKerosakan));
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
		List<RppGambarAduan> listGambar = db.list("select x from RppGambarAduan x where x.aduanKerosakan.id = '"+getParam("idAduanKerosakan")+"' ");
		context.put("listGambar",listGambar);
		return getPath() + "/form/listGambar.vm";
	}
	
	@Command("deleteDokumen")
	public String deleteDokumen() throws Exception {
		
		RppGambarAduan rg = db.find(RppGambarAduan.class, getParam("dokumenId"));
		db.begin();
		db.remove(rg);
		db.commit();
		
		return refreshList();
	}
	
	@Command("getHargaJenisKerosakan")
	public String getHargaJenisKerosakan() throws Exception {
		RppTetapanBarangDeposit brg = db.find(RppTetapanBarangDeposit.class, getParam("idBarangDeposit"));
		context.put("brg",brg);
		context.put("util", new Util());
		return getPath() + "/form/hargaBarangDeposit.vm";
	}
	
}
