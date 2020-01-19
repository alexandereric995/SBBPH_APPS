package bph.modules.rpp;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorDateBetween;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import portal.module.entity.Users;
import bph.entities.kew.KewJenisBayaran;
import bph.entities.kew.KewTuntutanDeposit;
import bph.entities.kewangan.KewDeposit;
import bph.entities.kod.JenisDokumen;
import bph.entities.kod.Status;
import bph.entities.rpp.RppAduanKerosakan;
import bph.entities.rpp.RppAkaun;
import bph.entities.rpp.RppGambarAduan;
import bph.entities.rpp.RppJadualTempahan;
import bph.entities.rpp.RppMaklumBalas;
import bph.entities.rpp.RppPengurusanBilik;
import bph.entities.rpp.RppPermohonan;
import bph.entities.rpp.RppSenaraiHitam;
import bph.entities.rpp.RppTetapanBarangDeposit;
import bph.entities.rpp.RppUnit;
import bph.utils.DataUtil;
import bph.utils.HTML;
import bph.utils.Util;
import bph.utils.UtilRpp;
import db.persistence.MyPersistence;

public class RppPengurusanBilikRecordModule extends LebahRecordTemplateModule<RppPermohonan> {

	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;
	private MyPersistence mp;
	
	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() { return String.class; }

	@Override
	public void afterSave(RppPermohonan r) { }

	@Override
	public void beforeSave() { }

	@Override
	public String getPath() { return "bph/modules/rpp/rppPengurusanBilik"; }
	
	@Override
	public void begin() {
		
		dataUtil = DataUtil.getInstance(db);
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		userName = (String) request.getSession().getAttribute("_portal_username");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		
		try{
			mp = new MyPersistence();
			filtering(mp,userId,userRole);
			getDataPemohon(mp,userId);
		}finally{
			if (mp != null) { mp.close(); }
		}
		
		//String currentdate = Util.getDateTime(new Date(), "yyyy-MM-dd");
		this.addFilter("status.id in ('1425259713415','1425259713421','1425259713424')"); //permohonan lulus dan daftar masuk dan daftar keluar
		//this.addFilter("status.id in ('1425259713415','1425259713421')"); //permohonan lulus dan daftar masuk
		this.addFilter("statusBayaran = 'Y' "); //dah bayar
		//this.addFilter("tarikhMasukRpp >= '"+currentdate+"' ");
		
		defaultButtonOption();
		removeContext();
		
		try {
			context.put("carianJenisPeranginan", HTML.SelectJenisPeranginan("carianJenisPeranginan",null, "id=\"carianJenisPeranginan\" style=\"width:100%\" ", "onchange=\"doFilterPeranginan()\""));
			context.put("carianStatusPermohonan", HTML.SelectStatusPengurusanBilik("carianStatusPermohonan",null, "id=\"carianStatusPermohonan\" style=\"width:100%\" ", ""));
		} catch (Exception e) {
			System.out.println("error getting dropdown list "+e.getMessage());
		}
		
		context.remove("r");
		context.put("command", command);
		context.put("userRole", userRole);
		context.put("path", getPath());
		context.put("util", new Util());
		context.put("uploadDir", ResourceBundle.getBundle("dbconnection").getString("folder"));
	}

	private void filtering(MyPersistence mp, String userId, String userRole) {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.YEAR, -2);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DATE, 1);
		
		this.addFilter("tarikhPermohonan >= '2016-01-01'");
		
		if(userRole.equalsIgnoreCase("(RPP) Penyelia")){
			String listPeranginan = UtilRpp.multipleListSeliaan(mp,userId);
			if (listPeranginan.length() == 0) {
				this.addFilter("rppPeranginan.id = ''");
			} else {
				this.addFilter("rppPeranginan.id in (" + listPeranginan + ")");
			}	
			this.setOrderBy("rppPeranginan.namaPeranginan asc");
		}
	}
	
	private void defaultButtonOption() {
		this.setReadonly(true);
		this.setDisableDefaultButton(true);
		this.setDisableBackButton(true);
		this.setDisableKosongkanUpperButton(true);
		this.setDisableSaveAddNewButton(true);
	}

	private void removeContext(){
		context.remove("users");
		context.remove("bilik");
	}
	
	@Override
	public boolean delete(RppPermohonan r) throws Exception { return false; }

	@Override
	public Class<RppPermohonan> getPersistenceClass() { return RppPermohonan.class; }

	@SuppressWarnings("unchecked")
	@Override
	public void getRelatedData(RppPermohonan r) {
		
		try{
			mp = new MyPersistence();
			context.remove("bilik");
			
			RppPermohonan rr = (RppPermohonan) mp.find(RppPermohonan.class, r.getId());
			RppPengurusanBilik bilik = (RppPengurusanBilik) mp.get("select x from RppPengurusanBilik x where x.permohonan.id = '"+r.getId()+"' ");
			List<RppJadualTempahan> currentUnitList = mp.list("select x from RppJadualTempahan x where x.permohonan.id = '"+r.getId()+"' ");
			
			context.put("listBilik",currentUnitList);
			context.put("r", rr);
			context.put("bilik",bilik);
			context.put("listTempahanDanBayaran", UtilRpp.getListTempahanDanBayaran(mp,r));
			context.put("selectedTab","1");
		}finally{
			if (mp != null) { mp.close(); }
		}
		
	}

	@Override
	public void save(RppPermohonan r) throws Exception { }

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
	
		String carianStatusPermohonan = getParam("carianStatusPermohonan");
		String carianJenisPeranginan = getParam("carianJenisPeranginan");
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status.id", carianStatusPermohonan);
		map.put("rppPeranginan.jenisPeranginan.id", carianJenisPeranginan);
		map.put("pemohon.noKP", getParam("noKP"));
		map.put("pemohon.userName", getParam("userName"));
		//FATIN
		map.put("tarikhMasukRpp", new OperatorDateBetween(getDate("findTarikhMasukRpp"), getDate("findTarikhKeluarRpp")));
		map.put("tarikhKeluarRpp", new OperatorDateBetween(getDate("findTarikhMasukRpp"), getDate("findTarikhKeluarRpp")));
		return map;
	}
	
	private void getDataPemohon(MyPersistence mp,String userId){
		Users users = (Users) mp.find(Users.class, userId);
		context.put("users", users);
	}
	
	@SuppressWarnings("unchecked")
	@Command("getMaklumatDaftarMasuk")
	public String getMaklumatDaftarMasuk() throws Exception {
		String idRppPermohonan = getParam("idRppPermohonan");
		context.remove("r");
		try{
			mp = new MyPersistence();
			RppPermohonan r = (RppPermohonan) mp.find(RppPermohonan.class, idRppPermohonan);
			RppPengurusanBilik bilik = (RppPengurusanBilik) mp.get("select x from RppPengurusanBilik x where x.permohonan.id = '"+r.getId()+"' ");
			
			List<RppJadualTempahan> currentUnitList = mp.list("select x from RppJadualTempahan x where x.permohonan.id = '"+idRppPermohonan+"' ");
			context.put("listBilik",currentUnitList);
			
			context.put("bilik",bilik);
			context.put("r",r);
			context.put("selectedTab","1");
		}finally{
			if (mp != null) { mp.close(); }
		}
		return getPath()+ "/daftarMasuk/start.vm";
	}
	
	@Command("getMaklumatDaftarKeluar")
	public String getMaklumatDaftarKeluar() throws Exception {
		String idRppPermohonan = getParam("idRppPermohonan");
		context.remove("r");
		try{
			mp = new MyPersistence();
			RppPermohonan r = (RppPermohonan) mp.find(RppPermohonan.class, idRppPermohonan);
			RppPengurusanBilik bilik = (RppPengurusanBilik) mp.get("select x from RppPengurusanBilik x where x.permohonan.id = '"+r.getId()+"' ");
			context.put("bilik",bilik);
			context.put("r",r);
			context.put("selectedTab","2");
		}finally{
			if (mp != null) { mp.close(); }
		}
		return getPath()+ "/daftarKeluar/start.vm";
	}
	
	@Command("daftarMasuk")
	public String daftarMasuk() throws Exception {
		
		try{
			mp = new MyPersistence();
			
			Users pegawai = (Users) mp.find(Users.class, userId);
			String idRppPermohonan = getParam("idRppPermohonan");
			RppPermohonan r = (RppPermohonan) mp.find(RppPermohonan.class, idRppPermohonan);
			Status status = (Status) mp.find(Status.class, "1425259713421");
			String catatanMasuk = getParam("catatanMasuk");
			Date tarikhDaftarMasuk = getDate("tarikhDaftarMasuk");
			
			RppPengurusanBilik bilik = (RppPengurusanBilik) mp.get("select x from RppPengurusanBilik x where x.permohonan.id = '"+r.getId()+"' ");
			
			if(bilik == null){ bilik = new RppPengurusanBilik(); }
			
			mp.begin();
			
			bilik.setPermohonan(r);
			bilik.setCatatanMasuk(catatanMasuk);
			bilik.setTarikhDaftarMasuk(tarikhDaftarMasuk);
			bilik.setMasaDaftarJam(getParamAsInteger("masaDaftarJam"));
			bilik.setMasaDaftarMinit(getParamAsInteger("masaDaftarMinit"));
			bilik.setMasaDaftarAmPm(getParam("masaDaftarAmPm"));
			bilik.setPegawaiDaftarMasuk(pegawai);
			bilik.setTarikhMasuk(new Date());
			
			mp.persist(bilik);

			if(!r.getStatus().getId().equalsIgnoreCase("1425259713424")){ //dah daftar keluar xleh update ke daftar masuk
				r.setStatus(status);
			}
			
			mp.commit();
			
		}finally{
			if (mp != null) { mp.close(); }
		}
		
		return getMaklumatDaftarMasuk();
	}
	
	@Command("daftarKeluar")
	public String daftarKeluar() throws Exception {
		
		
		try{
			mp = new MyPersistence();
			Users pegawai = (Users) mp.find(Users.class, userId);
			String idRppPermohonan = getParam("idRppPermohonan");
			RppPermohonan r = (RppPermohonan) mp.find(RppPermohonan.class, idRppPermohonan);
			Status status = (Status) mp.find(Status.class, "1425259713424");
			String catatanKeluar = getParam("catatanKeluar");
			Date tarikhDaftarKeluar = getDate("tarikhDaftarKeluar");
			
			RppPengurusanBilik bilik = (RppPengurusanBilik) mp.get("select x from RppPengurusanBilik x where x.permohonan.id = '"+r.getId()+"' ");
			if(bilik == null){ bilik = new RppPengurusanBilik(); }
			
			mp.begin();
			
			bilik.setPermohonan(r);
			bilik.setCatatanKeluar(catatanKeluar);
			bilik.setTarikhDaftarKeluar(tarikhDaftarKeluar);
			bilik.setMasaKeluarJam(getParamAsInteger("masaKeluarJam"));
			bilik.setMasaKeluarMinit(getParamAsInteger("masaKeluarMinit"));
			bilik.setMasaKeluarAmPm(getParam("masaKeluarAmPm"));
			bilik.setPegawaiDaftarKeluar(pegawai);
			bilik.setTarikhKemaskini(new Date());
			
			mp.persist(bilik);
			
			if(r.getStatus().getId().equalsIgnoreCase("1425259713421")){ //klau status daftar masuk je leh update ke daftar keluar
				r.setStatus(status);
			}
			
			mp.commit();
			
		}finally{
			if (mp != null) { mp.close(); }
		}
				
		String flagRedirect = getParam("flagRedirect");
		if(flagRedirect.equalsIgnoreCase("YA")){
			return getAduanKerosakan();
		}else if(flagRedirect.equalsIgnoreCase("TIDAK")){
			return getSkrinPulanganDeposit();
		}else{
			return getMaklumatDaftarKeluar();
		}
			
	}
	
	@Command("getMaklumbalas")
	public String getMaklumbalas() throws Exception {

		try{
			mp = new MyPersistence();
			
			String idRppPermohonan = getParam("idRppPermohonan");
			context.remove("r");
			context.remove("mb");
			RppPermohonan r = (RppPermohonan) mp.find(RppPermohonan.class, idRppPermohonan);
			RppMaklumBalas mb = (RppMaklumBalas) mp.get("select x from RppMaklumBalas x where x.permohonan.id = '"+r.getId()+"' ");
			
			context.put("mb",mb);
			context.put("r",r);
			context.put("selectedTab","3");
			
		}finally{
			if (mp != null) { mp.close(); }
		}
		
		return getPath()+ "/maklumbalas/start.vm";
	}
	
	@Command("saveMaklumBalas")
	public String saveMaklumBalas() throws Exception {
		
		try{
			mp = new MyPersistence();
			
			RppPermohonan r = (RppPermohonan) mp.find(RppPermohonan.class, getParam("idRppPermohonan"));
			RppMaklumBalas mb = (RppMaklumBalas) mp.get("select x from RppMaklumBalas x where x.permohonan.id = '"+r.getId()+"' ");
			
			mp.begin();
			
			if(mb==null){
				mb = new RppMaklumBalas();
				mb.setPermohonan(r);
			};
			
			mb.setNoMaklumbalas(getParam("noMaklumbalas"));
			mb.setKeterangan(getParam("keteranganMaklumbalas"));
			mb.setUlasanMaklumbalas(getParam("ulasanMaklumbalas"));
			mb.setTarikhMaklumbalas(getDate("tarikhMaklumbalas"));
			
			if(mb.getStatus()==null){
				mb.setStatus((Status) mp.find(Status.class,"1430024161503")); //MAKLUMBALAS BARU
			}
			
			mp.persist(mb);
			
			mp.commit();
			
		}finally{
			if (mp != null) { mp.close(); }
		}

		return getMaklumbalas();
	}
	

	@Command("getAduanKerosakan")
	public String getAduanKerosakan() throws Exception {
		dataUtil = DataUtil.getInstance(db);
		try{
			mp = new MyPersistence();
			String idRppPermohonan = getParam("idRppPermohonan");
			context.remove("r");
			context.remove("ak");
			
			RppPermohonan r = (RppPermohonan) mp.find(RppPermohonan.class, idRppPermohonan);
			RppAduanKerosakan ak = (RppAduanKerosakan) mp.get("select x from RppAduanKerosakan x where x.permohonan.id = '"+r.getId()+"' ");
			
			context.put("ak",ak);
			context.put("listBarangDeposit", dataUtil.getListBarangDeposit());
			context.put("r",r);
			context.put("selectedTab","4");
		}finally{
			if (mp != null) { mp.close(); }
		}
		return getPath()+ "/aduanKerosakan/start.vm";
	}
	
	@Command("saveAduan")
	public String saveAduan() throws Exception {
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		try{
			mp = new MyPersistence();
			
			RppPermohonan r = (RppPermohonan) mp.find(RppPermohonan.class, getParam("idRppPermohonan"));
			RppAduanKerosakan ak = (RppAduanKerosakan) mp.get("select x from RppAduanKerosakan x where x.permohonan.id = '"+r.getId()+"' ");
			
			mp.begin();
			
			if(ak==null){
				ak = new RppAduanKerosakan();
				ak.setPermohonan(r);
			};
			
			ak.setPeranginan(r.getRppPeranginan());
			ak.setTarikhAduan(getDate("tarikhAduan"));
			ak.setKeterangan(getParam("keterangan"));
			ak.setBarangDeposit((RppTetapanBarangDeposit) mp.find(RppTetapanBarangDeposit.class, getParam("barangDeposit")));
			ak.setKuantiti(getParamAsInteger("kuantiti"));
			ak.setHarga(Util.getDoubleRemoveComma(getParam("harga")));
			ak.setPengadu((Users) mp.find(Users.class, userId));
			
			if(ak.getStatus()==null){
				ak.setStatus((Status) mp.find(Status.class,"1429870728744")); //ADUAN BARU
			}
			
			mp.persist(ak);

			mp.commit();
			
		}finally{
			if (mp != null) { mp.close(); }
		}
		
		return getAduanKerosakan();
	}
	
	@SuppressWarnings("unchecked")
	@Command("getGambarAduan")
	public String getGambarAduan() throws Exception {
		try{
			mp = new MyPersistence();
			List<RppGambarAduan> listGambar = mp.list("select x from RppGambarAduan x where x.aduanKerosakan.id = '"+getParam("idAduanKerosakan")+"' ");
			RppAduanKerosakan ak = (RppAduanKerosakan) mp.find(RppAduanKerosakan.class, getParam("idAduanKerosakan"));
			context.put("listGambar",listGambar);
			context.put("ak",ak);
		}finally{
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/aduanKerosakan/gambar.vm";
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
				simpanDokumen(idAduanKerosakan, imgName, avatarName, tajuk, idJenisDokumen, keterangan);
			}
		}
		
		return getPath() + "/aduanKerosakan/uploadDoc.vm";
	}
	
	public void simpanDokumen(String idAduanKerosakan, String imgName, String avatarName, String tajuk, String idJenisDokumen, String keterangan) throws Exception {
		
		try{
			mp = new MyPersistence();
			RppGambarAduan a = new RppGambarAduan();

			mp.begin();
			
			a.setAduanKerosakan((RppAduanKerosakan) mp.find(RppAduanKerosakan.class, idAduanKerosakan));
			a.setPhotofilename(imgName);
			a.setThumbfilename(avatarName);	
			a.setTajuk(tajuk);
			a.setJenisDokumen((JenisDokumen) mp.find(JenisDokumen.class, idJenisDokumen));
			a.setKeterangan(keterangan);
			
			mp.persist(a);
			mp.commit();
		}finally{
			if (mp != null) { mp.close(); }
		}
		
	}
	
	@SuppressWarnings("unchecked")
	@Command("refreshList")
	public String refreshList() throws Exception {
		
		try{
			mp = new MyPersistence();
			List<RppGambarAduan> listGambar = mp.list("select x from RppGambarAduan x where x.aduanKerosakan.id = '"+getParam("idAduanKerosakan")+"' ");
			context.put("listGambar",listGambar);
		}finally{
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/aduanKerosakan/listGambar.vm";
	}
	
	@Command("deleteDokumen")
	public String deleteDokumen() throws Exception {
		try{
			mp = new MyPersistence();
			RppGambarAduan rg = (RppGambarAduan) mp.find(RppGambarAduan.class, getParam("dokumenId"));
			if(rg != null){
				mp.begin();
				mp.remove(rg);
				mp.commit();
			}
		}finally{
			if (mp != null) { mp.close(); }
		}
		return refreshList();
	}
	
	@Command("getPermohonanSenaraiHitam")
	public String getPermohonanSenaraiHitam() throws Exception {
		try{
			mp = new MyPersistence();
			String idRppPermohonan = getParam("idRppPermohonan");
			context.remove("r");
			context.remove("sh");
			RppPermohonan r = (RppPermohonan) mp.find(RppPermohonan.class, idRppPermohonan);
			RppSenaraiHitam sh = (RppSenaraiHitam) mp.get("select x from RppSenaraiHitam x where x.permohonan.id = '"+r.getId()+"' ");
			context.put("sh",sh);
			context.put("r",r);
		}finally{
			if (mp != null) { mp.close(); }
		}
		return getPath()+ "/senaraiHitam/start.vm";
	}
	
	@Command("saveSenaraiHitam")
	public String saveSenaraiHitam() throws Exception {
		
		try{
			mp = new MyPersistence();
			RppPermohonan r = (RppPermohonan) mp.find(RppPermohonan.class, getParam("idRppPermohonan"));
			RppSenaraiHitam sh = (RppSenaraiHitam) mp.get("select x from RppSenaraiHitam x where x.permohonan.id = '"+r.getId()+"' ");
			String strStatus = "1428990717386"; //STATUS PERMOHONAN SENARAI HITAM
			
			mp.begin();
			
			if(sh==null){
				sh = new RppSenaraiHitam();
				sh.setPermohonan(r);
			};
			
			sh.setPermohonan(r);
			sh.setPemohon(r.getPemohon());
			sh.setTarikhPermohonan(getDate("tarikhPermohonan"));
			sh.setSebab(getParam("sebab"));
			sh.setCatatan(getParam("catatan"));
			sh.setFlagAktif("T");
			if(sh.getStatus()==null){
				sh.setStatus((Status) mp.find(Status.class, strStatus));
			}
			
			mp.persist(sh);
			mp.commit();
			
		}finally{
			if (mp != null) { mp.close(); }
		}
		
		return getPermohonanSenaraiHitam();
	}
	
	@Command("batalSenaraiHitam")
	public String batalSenaraiHitam() throws Exception {
		
		try{
			mp = new MyPersistence();
			
			RppPermohonan r = (RppPermohonan) mp.find(RppPermohonan.class, getParam("idRppPermohonan"));
			RppSenaraiHitam sh = (RppSenaraiHitam) mp.get("select x from RppSenaraiHitam x where x.permohonan.id = '"+r.getId()+"' ");
			
			if( sh != null ){
				mp.begin();
				mp.remove(sh);
				mp.commit();
			}
			
		}finally{
			if (mp != null) { mp.close(); }
		}
		return getPermohonanSenaraiHitam();
	}
	
	@Command("callPopupPilihBilik")
	public String callPopupPilihBilik() throws Exception {
		
		try{
			mp = new MyPersistence();
			RppPermohonan r = (RppPermohonan) mp.find(RppPermohonan.class, getParam("idRppPermohonan"));
			RppUnit rppUnit = (RppUnit) mp.find(RppUnit.class, getParam("idUnit"));
			context.put("currentRppUnit",rppUnit);
			context.put("r",r);
			context.put("listAvailableUnit", UtilRpp.listPilihBilikCheckinAvailable(mp, r.getTarikhMasukRpp(),r.getTarikhKeluarRpp(), rppUnit.getJenisUnit().getId()));
			
			String strTarikhMasuk = new SimpleDateFormat("yyyy-MM-dd").format(r.getTarikhMasukRpp());
			String strTarikhKeluar = new SimpleDateFormat("yyyy-MM-dd").format(r.getTarikhKeluarRpp());
			
			context.put("strTarikhMasuk", strTarikhMasuk);
			context.put("strTarikhKeluar", strTarikhKeluar);
		}finally{
			if (mp != null) { mp.close(); }
		}
		
		return getPath()+ "/daftarMasuk/popupBilik/start.vm";
	}
	
	@SuppressWarnings("unchecked")
	@Command("savePilihanBilik")
	public String savePilihanBilik() throws Exception {
		
		String selectedIdRppUnit = getParam("radUnit");
		String currentIdUnit = getParam("currentIdUnit");
		
		try{
			mp = new MyPersistence();
			
			RppPermohonan r = (RppPermohonan) mp.find(RppPermohonan.class, getParam("idRppPermohonan"));
			RppJadualTempahan jt = (RppJadualTempahan) mp.get("select x from RppJadualTempahan x where x.permohonan.id = '"+r.getId()+"' and x.unit.id = '"+currentIdUnit+"' ");
			
			mp.begin();
			
			//BIARKAN.XPERLU.UPDATE jt.setUnit((RppUnit) mp.find(RppUnit.class, selectedIdRppUnit));
			jt.setFlagStatusTempahan("CONFIRM");
			jt.setUnitConfirm((RppUnit) mp.find(RppUnit.class, selectedIdRppUnit));
			
			mp.commit();

			List<RppJadualTempahan> currentUnitList = mp.list("select x from RppJadualTempahan x where x.permohonan.id = '"+getParam("idRppPermohonan")+"' ");
			context.put("listBilik",currentUnitList);
			context.put("r",r);
			
//			RppJadualTempahan objt = (RppJadualTempahan) mp.get("select x from RppJadualTempahan x where x.unit.id = '"+selectedIdRppUnit+"' and x.flagStatusTempahan = 'TEMP' ");
//			if(objt!=null){
//				objt.setUnit((RppUnit) mp.find(RppUnit.class, currentIdUnit));
//			}
			
		}catch (Exception ex){
			System.out.println("ERROR savePilihanBilik : "+ex.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		
		return getPath()+ "/daftarMasuk/pilihanBilik.vm";
	}
	
	@Command("getHargaJenisKerosakan")
	public String getHargaJenisKerosakan() throws Exception {
		try{
			mp = new MyPersistence();
			RppTetapanBarangDeposit brg = (RppTetapanBarangDeposit) mp.find(RppTetapanBarangDeposit.class, getParam("idBarangDeposit"));
			context.put("brg",brg);
			context.put("util", new Util());
		}finally{
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/aduanKerosakan/hargaBarangDeposit.vm";
	}
	
	@SuppressWarnings("unchecked")
	@Command("getSkrinPulanganDeposit")
	public String getSkrinPulanganDeposit() throws Exception {
		
		try{
			mp = new MyPersistence();
			String idRppPermohonan = getParam("idRppPermohonan");
			context.remove("r");
			context.remove("listRosak");
			RppPermohonan r = (RppPermohonan) mp.find(RppPermohonan.class, idRppPermohonan);
			
			RppAkaun lejar = (RppAkaun) mp.get("select w from RppAkaun w where w.permohonan.id = '"+r.getId()+"' and w.kodHasil.id = '72311' ");
			KewDeposit dep = null;
			if(lejar!=null){
				dep = (KewDeposit) mp.get("select x from KewDeposit x where x.idLejar = '"+lejar.getId()+"' ");
			}
			context.put("dep",dep);
			
			List<RppAduanKerosakan> listRosak = mp.list("select x from RppAduanKerosakan x where x.permohonan.id = '"+r.getId()+"' ");
			context.put("listRosak",listRosak);
			context.put("r",r);
			context.put("selectedTab","5");
		}finally{
			if (mp != null) { mp.close(); }
		}
		
		return getPath()+ "/pulanganDeposit/start.vm";
	}
	
	@SuppressWarnings("unchecked")
	@Command("simpanPulanganDeposit")
	public String simpanPulanganDeposit() throws Exception {
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		try{
			mp = new MyPersistence();
			
			String idRppPermohonan = getParam("idRppPermohonan");
			RppPermohonan r = (RppPermohonan) mp.find(RppPermohonan.class, idRppPermohonan);
			String idKewDeposit = getParam("idKewDeposit");
			KewDeposit dep = (KewDeposit) mp.find(KewDeposit.class, idKewDeposit);
			
			KewTuntutanDeposit t = new KewTuntutanDeposit();
			
			mp.begin();
			
			r.setStatus((Status) mp.find(Status.class, "1435512646303"));
			t.setDeposit(dep);
			t.setJenisTuntutan((KewJenisBayaran) mp.find(KewJenisBayaran.class, "02"));
			t.setPenuntut(r.getPemohon());
			t.setStatus((Status) mp.find(Status.class, "1436464445665"));
			t.setTarikhPermohonan(new Date());
			t.setSuratPengesahanDeposit("Y");
			t.setCatatanPenyeliaRpp(getParam("catatanPenyeliaRpp"));
			t.setIdDaftar((Users) mp.find(Users.class, userId));
			t.setTarikhDaftar(new Date());
			mp.persist(t);
			
			dep.setTuntutanDeposit(t);
			
			/**Notifikasi kepada HQ untuk tuntutan deposit*/
			//List<Role> roles = mp.list("select x from Role x where x.name in ('(RPP) Penyemak','(RPP) Pelulus') ");
			//UtilRpp.saveNotifikasi(mp,roles,r.getId(),"Y",getClass().getName(),"TUNTUTAN_DEPOSIT");
			
			mp.commit();
			
		}finally{
			if (mp != null) { mp.close(); }
		}
		return getSkrinPulanganDeposit();
	}
	
	@Command("callPopupDaftarKeluar")
	public String callPopupDaftarKeluar() throws Exception {
		return getPath()+ "/daftarKeluar/popupkeluar.vm";
	}
	
}





