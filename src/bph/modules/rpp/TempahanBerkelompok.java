package bph.modules.rpp;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorDateBetween;
import lebah.template.OperatorEqualTo;
import lebah.template.UID;
import lebah.util.Util;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import portal.module.entity.Role;
import portal.module.entity.Users;
import bph.entities.kewangan.KewDeposit;
import bph.entities.kewangan.KewInvois;
import bph.entities.kod.AktivitiRpp;
import bph.entities.kod.CaraBayar;
import bph.entities.kod.JenisDokumen;
import bph.entities.kod.JenisUnitRPP;
import bph.entities.kod.KodHasil;
import bph.entities.kod.Negeri;
import bph.entities.kod.Status;
import bph.entities.rpp.RppAkaun;
import bph.entities.rpp.RppDokumenKelompok;
import bph.entities.rpp.RppJadualTempahan;
import bph.entities.rpp.RppKelompokSenaraiAktiviti;
import bph.entities.rpp.RppKelompokUnit;
import bph.entities.rpp.RppPeranginan;
import bph.entities.rpp.RppPermohonan;
import bph.entities.rpp.RppSelenggara;
import bph.entities.rpp.RppUnit;
import bph.utils.DataUtil;
import bph.utils.UtilRpp;
import db.persistence.MyPersistence;


public class TempahanBerkelompok extends LebahRecordTemplateModule<RppPermohonan> {

	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;
	private MyPersistence mp;
	
	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {return String.class;}

	@Override
	public void afterSave(RppPermohonan r) {}

	@Override
	public void beforeSave() {}

	@Override
	public void save(RppPermohonan r) throws Exception { }
	
	@Override
	public String getPath() {return "bph/modules/rpp/tempahanBerkelompok";}

	@Override
	public Class<RppPermohonan> getPersistenceClass() {return RppPermohonan.class;}
	
	@Override
	public void begin() {
		
		context.remove("r");
		
		dataUtil = DataUtil.getInstance(db);
		userId = (String) request.getSession().getAttribute("_portal_login");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		
		try{
			mp = new MyPersistence();
			Users userLogin = (Users) mp.find(Users.class, userId);
			
			filtering(userRole);
			defaultButtonOption(userRole);
			reportFiltering();
			filteringIndividu(userId);
			
			context.put("selectedTab", "1");
			context.put("listPeranginan", dataUtil.getListPeranginanKelompok());
			context.put("listStatus", dataUtil.getListStatusKelompok());
			context.put("listAktiviti", dataUtil.getListAktivitiRpp());
			context.put("listNegeri", dataUtil.getListNegeri());
			context.put("listJenisDokumen", dataUtil.getListJenisDokumenRppKelompok());
			
			context.put("noUnitAvailable", false);
			context.put("path", getPath());
			context.put("util", new Util());
			context.put("userRole",userRole);
			context.put("command", command);
			context.put("userLogin", userLogin);
			context.put("uploadDir", ResourceBundle.getBundle("dbconnection").getString("folder"));
			context.put("rppUtil", new UtilRpp());
			
			this.setOrderBy("tarikhPermohonan desc");
			
		} catch (Exception e) {
			System.out.println("[TempahanBerkelompok] Error begin : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		
	}//close begin
	
	public void filteringIndividu(String userId) { }
	
	private void filtering(String userRole) {
		this.addFilter("jenisPemohon = 'KELOMPOK' ");
		if(userRole.equalsIgnoreCase("(RPP) Penyedia") || userRole.equalsIgnoreCase("(RPP) Penyemak")
		|| userRole.equalsIgnoreCase("(RPP) Pelulus") || userRole.equalsIgnoreCase("(RPP) Penyelia")){
			this.addFilter("status.id in ('1425259713412','1425259713415','1433097397170')");
		}
	}
	
	private void defaultButtonOption(String userRole) {
		if(userRole.equalsIgnoreCase("(RPP) Penyedia") || userRole.equalsIgnoreCase("(RPP) Penyemak")
		 || userRole.equalsIgnoreCase("(RPP) Pelulus") || userRole.equalsIgnoreCase("(RPP) Penyelia")){
			this.setReadonly(true);
		}else{
			this.setReadonly(false);
		}
		this.setDisableBackButton(true);
		this.setDisableSaveAddNewButton(true);
		this.setDisableDefaultButton(true);
		this.setDisableKosongkanUpperButton(true);
		this.setDisableUpperBackButton(true);
	}
	
	public void reportFiltering() {
		context.put("jenisReport", "KELOMPOK");
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean delete(RppPermohonan r) throws Exception {
		
		/**Delete child / yang berkaitan*/
		boolean val = false;
		if(r.getStatus().getId().equalsIgnoreCase("1439694210108")) { //DRAF PERMOHONAN BERKELOMPOK
			
			try {
				mp = new MyPersistence();
				
				mp.begin();
				
				List<RppKelompokSenaraiAktiviti> ksa = mp.list("select x from RppKelompokSenaraiAktiviti x where x.permohonan.id = '"+r.getId()+"' ");
				for(int i=0;i<ksa.size();i++){
					mp.remove(ksa.get(i));
				}
				
				List<RppKelompokUnit> un = mp.list("select x from RppKelompokUnit x where x.permohonan.id = '"+r.getId()+"' ");
				for(int i=0;i<un.size();i++){
					mp.remove(un.get(i));
				}
				
				mp.commit();
				
			} catch (Exception e) {
				System.out.println("[TempahanBerkelompok] Error delete : "+e.getMessage());
				val = false;
			}finally{
				if (mp != null) { mp.close(); }
				val = true;
			}
		}
		return val;
		
	}//close delete
	
	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pemohon.noKP", getParam("findNoKp"));
		map.put("pemohon.userName", getParam("findUserName"));
		map.put("rppPeranginan.id", new OperatorEqualTo(getParam("findRpp")));
		map.put("status.id", new OperatorEqualTo(getParam("findStatus")));
		map.put("statusBayaran", getParam("findStatusBayaran"));
		map.put("tarikhMasukRpp", new OperatorDateBetween(getDate("findTarikhMasukRpp"), getDate("findTarikhKeluarRpp")));
		map.put("tarikhKeluarRpp", new OperatorDateBetween(getDate("findTarikhMasukRpp"), getDate("findTarikhKeluarRpp")));
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void getRelatedData(RppPermohonan r) { 
		String userId = (String) request.getSession().getAttribute("_portal_login");
		String userRole = (String) request.getSession().getAttribute("_portal_role");
		try {
			mp = new MyPersistence();
			
			RppPermohonan rr = (RppPermohonan) mp.find(RppPermohonan.class, r.getId());
			
			context.put("listJenisUnit", dataUtil.getListJenisUnitByRpp(rr.getRppPeranginan().getId()));
			List<RppAkaun> listak = UtilRpp.getListTempahanDanBayaran(mp,r);
			context.put("listTempahanDanBayaran", listak);
			List<RppDokumenKelompok> listDokumen = mp.list("select x from RppDokumenKelompok x where x.permohonan.id = '"+r.getId()+"' ");
			context.put("listDokumen",listDokumen);
			context.put("selectedTab", "1");
			
			context.put("r", rr);
			
			/**Read notification*/
			mp.begin();
			UtilRpp.readNotification(mp,r.getId(),userRole,userId,"TEMPAHAN_KELOMPOK_BARU");
			mp.commit();
			
		} catch (Exception e) {
			System.out.println("Error getRelatedData : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
	}
	
	@SuppressWarnings("unchecked")
	@Command("getDokumenKelompok")
	public String getDokumenKelompok() throws Exception {
		
		String idpermohonan = getParam("idpermohonan");
		
		try {
			mp = new MyPersistence();
			
			List<RppDokumenKelompok> listDokumen = mp.list("select x from RppDokumenKelompok x where x.permohonan.id = '"+idpermohonan+"' ");
			context.put("listDokumen",listDokumen);
			
			RppPermohonan r = (RppPermohonan) mp.find(RppPermohonan.class, idpermohonan);
			context.put("r",r);
			context.put("selectedTab", "2");
			
		} catch (Exception e) {
			System.out.println("Error getDokumenKelompok : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/form/dokumen/start.vm";
	}
	
	@Command("findJenisUnit")
	public String findJenisUnit() throws Exception {
		String idrp = getParam("idrp");
		
		Date tarikhMasukRpp = getDate("tarikhMasukRpp");
		Date tarikhKeluarRpp = getDate("tarikhKeluarRpp");
		String dtIn = new SimpleDateFormat("yyyy-MM-dd").format(tarikhMasukRpp);
		String dtOut = new SimpleDateFormat("yyyy-MM-dd").format(tarikhKeluarRpp);
		
		try {
			mp = new MyPersistence();
			
			int bilAvailableUnit = UtilRpp.getBilanganUnitAvailableByRangeAndRpp(mp,idrp,dtIn,dtOut);
			if(bilAvailableUnit == 0){
				context.put("noUnitAvailable", true);
				return getPath() + "/form/notis.vm";
			}
			context.put("listJenisUnit", dataUtil.getListJenisUnitByRpp(idrp));
			
		} catch (Exception e) {
			System.out.println("Error findJenisUnit : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/form/senaraiJenisUnit.vm";
	}
	
	@Command("countTotalUnitMohon")
	public String countTotalUnitMohon() throws Exception {
		List<JenisUnitRPP> lst = dataUtil.getListJenisUnitByRpp(getParam("idrp"));
		int count = 0;
		for (int i = 0;i < lst.size(); i++){
			int strBilUnit = getParamAsInteger("bilUnit"+lst.get(i).getId());
			count += strBilUnit;
		} 
		context.put("totalUnitMohon",count);
		return getPath() + "/form/countUnitMohon.vm";
	}
	
	@Command("countTotalUnitLulus")
	public String countTotalUnitLulus() throws Exception {
		List<JenisUnitRPP> lst = dataUtil.getListJenisUnitByRpp(getParam("idrp"));
		int count = 0;
		for (int i = 0;i < lst.size(); i++){
			int strBilUnit = getParamAsInteger("bilUnitKelompok"+lst.get(i).getId());
			count += strBilUnit;
		} 
		context.put("totalUnitLulus",count);
		return getPath() + "/form/countUnitLulus.vm";
	}
	
	@SuppressWarnings("unchecked")
	@Command("savePermohonanKelompok")
	public String savePermohonanKelompok() throws Exception {
		
		String userId = (String) request.getSession().getAttribute("_portal_login");
		String idrp = getParam("idrp");
		String idpermohonan = getParam("idpermohonan");
		String userRole = (String) request.getSession().getAttribute("_portal_role");
		
		String vm = "";
		
		try {
			mp = new MyPersistence();
			
			RppPermohonan r = (RppPermohonan) mp.find(RppPermohonan.class, idpermohonan);
			
			if(r==null){r = new RppPermohonan();}
			
			String dtIn = new SimpleDateFormat("yyyy-MM-dd").format(getDate("tarikhMasukRpp"));
			String waktupuncak = "T";
			if(UtilRpp.checkWaktuPuncak(dtIn)){
				waktupuncak = "Y";
			}
			
			mp.begin();
			
			r.setRppPeranginan((RppPeranginan) mp.find(RppPeranginan.class, idrp));
			r.setJenisPemohon("KELOMPOK");
			r.setStatus((Status) mp.find(Status.class, "1439694210108")); //DRAF permohonan
			r.setTarikhMasukRpp(getDate("tarikhMasukRpp"));
			r.setTarikhKeluarRpp(getDate("tarikhKeluarRpp"));
			r.setBilDewasa(getParamAsInteger("bilDewasa"));
			r.setBilKanakKanak(getParamAsInteger("bilKanakKanak"));
			r.setAktivitiUtama1(getParam("aktivitiUtama1"));
			r.setAktivitiUtama2(getParam("aktivitiUtama2"));
			r.setAktivitiUtama3(getParam("aktivitiUtama3"));
			r.setBahagianUnit(getParam("bahagianUnit"));
			r.setAlamatSurat1(getParam("alamatSurat1"));
			r.setAlamatSurat2(getParam("alamatSurat2"));
			r.setAlamatSurat3(getParam("alamatSurat3"));
			r.setPoskodSurat(getParam("poskodSurat"));
			r.setStatusBayaran("T");
			r.setJenisPermohonan("ONLINE");
			r.setPemohon((Users) mp.find(Users.class,userId));
			r.setTarikhPermohonan(new Date());
			r.setNegeriSurat((Negeri) mp.find(Negeri.class, getParam("negeriSurat")));
			r.setFlagSyspintar("T");
			r.setFlagDaftarOffline("T");
			r.setFlagWaktuPuncak(waktupuncak);
			
			if(userRole.equalsIgnoreCase("(RPP) Penyemak") || userRole.equalsIgnoreCase("(RPP) Pelulus")){
				r.setCatatanPenyemak(getParam("catatanPenyemak"));
			}
			
			r.setTarikhMasuk(new Date());
			r.setIdMasuk((Users) mp.find(Users.class,userId));
			
			mp.persist(r);
			
			List<RppKelompokSenaraiAktiviti> ksa = mp.list("select x from RppKelompokSenaraiAktiviti x where x.permohonan.id = '"+r.getId()+"' ");
			for(int i=0;i<ksa.size();i++){
				mp.remove(ksa.get(i));
			}
			
			//add jenis aktiviti
			String[] aktivitiRpp = request.getParameterValues("aktivitiRpp");
			for (String aktivitiRppId : aktivitiRpp){
				RppKelompokSenaraiAktiviti rks = new RppKelompokSenaraiAktiviti();
				rks.setPermohonan(r);
				rks.setAktiviti((AktivitiRpp) mp.find(AktivitiRpp.class,aktivitiRppId));
				if(aktivitiRppId.equalsIgnoreCase("06")){
					rks.setKeteranganAktivitiLain(getParam("keteranganLain"));
				}
				mp.persist(rks);
			}
			
			List<RppKelompokUnit> un = mp.list("select x from RppKelompokUnit x where x.permohonan.id = '"+r.getId()+"' ");
			for(int i=0;i<un.size();i++){
				mp.remove(un.get(i));
			}
			
			List<JenisUnitRPP> lst = dataUtil.getListJenisUnitByRpp(idrp);
			for (int i = 0;i < lst.size(); i++){
				int strBilUnit = getParamAsInteger("bilUnit"+lst.get(i).getId());
				if(strBilUnit > 0){
					RppKelompokUnit ku = new RppKelompokUnit();
					ku.setPermohonan(r);
					ku.setJenisUnitRpp(lst.get(i));
					ku.setBilUnit(strBilUnit);
					ku.setBilUnitLulus(0);
					mp.persist(ku);
				}
			} 
			
			mp.commit();
			
			context.put("listJenisUnit", dataUtil.getListJenisUnitByRpp(r.getRppPeranginan().getId()));
			context.put("r", r);
			List<RppDokumenKelompok> listDokumen = mp.list("select x from RppDokumenKelompok x where x.permohonan.id = '"+r.getId()+"' ");
			context.put("listDokumen",listDokumen);
			
			if(listDokumen.size() > 0){
				context.put("selectedTab", "1");
				vm = templateDir + "/entry_fields.vm";
			}else{
				context.put("selectedTab", "2");
				vm = getPath() + "/form/dokumen/start.vm";
			}
			
		} catch (Exception e) {
			System.out.println("Error savePermohonanKelompok : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		
		return vm;
		
	}//close savePermohonanKelompok
	
	@SuppressWarnings("rawtypes")
	@Command("uploadDoc")
	public String uploadPhoto() throws Exception {
		
		String idpermohonan = getParam("idpermohonan");
		String tajuk = getParam("tajuk");
		String idJenisDokumen = getParam("idJenisDokumen");
		String keterangan = getParam("keteranganDokumen");
		String uploadDir = "/rpp/kelompok/";
		
		try {
			mp = new MyPersistence();
			
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
				//String imgName = uploadDir + Util.getDateTime(new Date(), "ddMMyyyhhmmss") + fileName;
				String imgName = uploadDir + idpermohonan+"_"+UID.getUID() + fileName.substring(fileName.lastIndexOf("."));;
				
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
					
					RppDokumenKelompok a = new RppDokumenKelompok();

					mp.begin();
					a.setPermohonan((RppPermohonan) mp.find(RppPermohonan.class, idpermohonan));
					a.setPhotofilename(imgName);
					a.setThumbfilename(avatarName);	
					a.setTajuk(tajuk);
					a.setJenisDokumen((JenisDokumen) mp.find(JenisDokumen.class, idJenisDokumen));
					a.setKeterangan(keterangan);
					
					mp.persist(a);
					
					mp.commit();
				}
			}
			
		} catch (Exception e) {
			System.out.println("Error uploadPhoto : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		
		return getPath() + "/form/dokumen/uploadDoc.vm";
	}
	
	@SuppressWarnings("unchecked")
	@Command("refreshListDokumen")
	public String refreshListDokumen() throws Exception {
		String idpermohonan = getParam("idpermohonan");
		try {
			mp = new MyPersistence();
			RppPermohonan r = (RppPermohonan) mp.find(RppPermohonan.class, idpermohonan);
			List<RppDokumenKelompok> listDokumen = mp.list("select x from RppDokumenKelompok x where x.permohonan.id = '"+idpermohonan+"' ");
			context.put("listDokumen",listDokumen);
			context.put("r",r);
		} catch (Exception e) {
			System.out.println("Error refreshListDokumen : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/form/dokumen/listDokumen.vm";
	}
	
	@Command("deleteDokumen")
	public String deleteDokumen() throws Exception{
		try {
			mp = new MyPersistence();
			RppDokumenKelompok rg = (RppDokumenKelompok) mp.find(RppDokumenKelompok.class, getParam("dokumenId"));
			if(rg != null){
				mp.begin();
				mp.remove(rg);
				bph.utils.Util.deleteFile(rg.getPhotofilename());
				bph.utils.Util.deleteFile(rg.getThumbfilename());
				mp.commit();
			}
		} catch (Exception e) {
			System.out.println("Error deleteDokumen : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		return refreshListDokumen();
	}
	
	
	@SuppressWarnings("unchecked")
	@Command("hantarPermohonan")
	public String hantarPermohonan() throws Exception {
		
		String idpermohonan = getParam("idpermohonan");
		
		try {
			mp = new MyPersistence();
			RppPermohonan r = (RppPermohonan) mp.find(RppPermohonan.class, idpermohonan);
			
			mp.begin();
			
			r.setStatus((Status) mp.find(Status.class, "1425259713412")); //Permohonan Baru
			r.setCatatanKelompokTidakLulus(null); //clear balik catatan jika guest resend permohonan
			
			/**Notifikasi kepada HQ Penyemak dan Pelulus*/
			List<Role> roles = mp.list("select x from Role x where x.name in ('(RPP) Penyemak','(RPP) Pelulus') ");
			UtilRpp.saveNotifikasi(mp,roles,r.getId(),"Y",getClass().getName(),"TEMPAHAN_KELOMPOK_BARU");
			
			mp.commit();
			
			List<RppDokumenKelompok> listDokumen = mp.list("select x from RppDokumenKelompok x where x.permohonan.id = '"+r.getId()+"' ");
			context.put("listDokumen",listDokumen);
			context.put("r", r);
			
		} catch (Exception e) {
			System.out.println("Error hantarPermohonan : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		
		context.put("selectedTab", "1");
		return templateDir + "/entry_fields.vm";
		
	}
	
	@Command("openPopupTidakLulus")
	public String openPopupTidakLulus() throws Exception {
		
		String idpermohonan = getParam("idpermohonan");
		try {
			mp = new MyPersistence();
			RppPermohonan r = (RppPermohonan) mp.find(RppPermohonan.class, idpermohonan);
			context.put("r", r);
		} catch (Exception e) {
			System.out.println("Error openPopupTidakLulus : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/form/popup/catatanTidakLulus.vm";
	}
	
	@Command("savePermohonanTidakLulus")
	public String savePermohonanTidakLulus() throws Exception {
		
		String idpermohonan = getParam("idpermohonan");
		
		System.out.println("idpermohonan "+idpermohonan);
		
		try {
			mp = new MyPersistence();
			RppPermohonan r = (RppPermohonan) mp.find(RppPermohonan.class, idpermohonan);
			
			if( r!=null ){
				mp.begin();
				r.setCatatanKelompokTidakLulus(getParam("catatanKelompokTidakLulus"));
				r.setStatus((Status) mp.find(Status.class, "2155884463563036")); //PERMOHONAN KELOMPOK TIDAK LULUS
				mp.commit();
			}
			
		} catch (Exception e) {
			System.out.println("Error savePermohonanTidakLulus : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		return getMaklumatPermohonan();
	}
	
	
	@SuppressWarnings("unchecked")
	@Command("savePermohonanLengkap")
	public String savePermohonanLengkap() throws Exception {
		
		try {
			mp = new MyPersistence();
			
			RppPermohonan r = (RppPermohonan) mp.find(RppPermohonan.class, getParam("idpermohonan"));
			
			mp.begin();
			
			r.setCatatanPenyemak(getParam("catatanPenyemak"));
			
			/**
			 * Save bil unit lulus
			 * **/
			List<RppKelompokUnit> listUnit = mp.list("select x from RppKelompokUnit x where x.permohonan.id = '"+r.getId()+"' ");;
			for (int i = 0;i < listUnit.size(); i++){
				RppKelompokUnit unit = listUnit.get(i);
				int strBilUnitKel = getParamAsInteger("bilUnitKelompok"+unit.getJenisUnitRpp().getId());
				unit.setBilUnitLulus(strBilUnitKel);
			} 
			
			daftarChalet(mp,r);
			createRecordBayaran(mp,r);
			
			if( r.getRppPeranginan().getId().equalsIgnoreCase("4") ){ 
				UtilRpp.saveLejarSewaBotInSql(r.getPemohon().getId(),r);
			}
			
			mp.commit();
			
			context.put("r",r);
			
		} catch (Exception e) {
			System.out.println("Error savePermohonanLengkap : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		
		return getMaklumatPermohonan();
	}
	
	@SuppressWarnings("unchecked")
	@Command("createRecordBayaran")
	public void createRecordBayaran(MyPersistence mp, RppPermohonan r) throws Exception {

		String userId = (String) request.getSession().getAttribute("_portal_login");
		Users user = (Users) mp.find(Users.class, userId);
		
		List<RppKelompokUnit> listUnit = mp.list("select x from RppKelompokUnit x where x.permohonan.id = '"+r.getId()+"' ");

		for(int i=0;i<listUnit.size();i++){
			
			int bilUnitLulus = listUnit.get(i).getBilUnitLulus();
			
			if(bilUnitLulus > 0){
				
				Double kadarSewa = listUnit.get(i).getJenisUnitRpp()!=null?listUnit.get(i).getJenisUnitRpp().getKadarSewa():0d;
				Double debitRpp = (kadarSewa * bilUnitLulus * r.getTotalBilMalam());
				
				RppAkaun mn = new RppAkaun();
				mn.setPermohonan(r);
				mn.setAmaunBayaranSeunit(kadarSewa);
				mn.setBilanganUnit(bilUnitLulus);
				mn.setDebit(debitRpp);
				mn.setKredit(0d);
				mn.setFlagBayar("T");
				mn.setFlagVoid("T");
				mn.setKeterangan("PERMOHONAN BERKELOMPOK "+listUnit.get(i).getJenisUnitRpp().getKeterangan().toUpperCase()+" DI "+r.getRppPeranginan().getNamaPeranginan().toUpperCase()+".");
				mn.setKodHasil((KodHasil) mp.find(KodHasil.class, "74299")); //BAYARAN-BAYARAN SEWA YANG LAIN
				mn.setNoInvois(r.getNoTempahan()); 
				mn.setTarikhInvois(new Date());
				mn.setIdMasuk(user);
				mn.setTarikhMasuk(new Date());
				mp.persist(mn);
			}
			
		}
		
	}
	
	
	@SuppressWarnings("unchecked")
	public void daftarChalet(MyPersistence mp, RppPermohonan r) throws Exception {
	
		boolean avroom = false;
		Date tarikhMasuk = r.getTarikhMasukRpp();
		Date tarikhKeluar = r.getTarikhKeluarRpp();
		
		String dtIn = new SimpleDateFormat("yyyy-MM-dd").format(tarikhMasuk);
		String dtOut = new SimpleDateFormat("yyyy-MM-dd").format(tarikhKeluar);
		
		r.setNoTempahan(UtilRpp.generateNoTempahanKelompok(mp, r));
		r.setStatus((Status) mp.find(Status.class, "1433097397170")); //PERMOHONAN KELOMPOK LENGKAP
		
		List<RppKelompokUnit> listUnit = mp.list("select x from RppKelompokUnit x where x.permohonan.id = '"+r.getId()+"' ");
		for(int i=0;i<listUnit.size();i++){
			
			int bilUnit = listUnit.get(i).getBilUnitLulus();
			
			List<RppUnit> rp = mp.list("select x from RppUnit x where x.jenisUnit.id = '"+listUnit.get(i).getJenisUnitRpp().getId()+"' and COALESCE(x.status,'') <> 'RESERVED' ");
			
			for(int j = 0; j < rp.size(); j++){
				
				//check bil unit yg telah didaftarkan
				int totalRegisteredUnit = mp.list("select x from RppJadualTempahan x where x.permohonan.id = '"+r.getId()+"' and x.kelompokUnit.id = '"+listUnit.get(i).getId()+"' ").size();
				if(totalRegisteredUnit < bilUnit){
					avroom = UtilRpp.checkingAvailableRoom(rp.get(j),dtIn,dtOut,r.getJenisPemohon());
					if(avroom){
						RppJadualTempahan jt = new RppJadualTempahan();

						jt.setPermohonan(r);
						jt.setKelompokUnit(listUnit.get(i));
						jt.setStatus("B");
						jt.setTarikhMula(r.getTarikhMasukRpp());
						jt.setTarikhTamat(r.getTarikhKeluarRpp());
						jt.setUnit(rp.get(j));
						jt.setFlagStatusTempahan("TEMP"); //Flag TEMP - bilik TEMPORARY. 
						mp.persist(jt);
					}
				}
			}
			
		}//end loop listunit
		
	}
	
	
	@SuppressWarnings("unchecked")
	@Command("batalPermohonanLengkap")
	public String batalPermohonanLengkap() throws Exception {
		
		String userId = (String) request.getSession().getAttribute("_portal_login");
		
		try {
			mp = new MyPersistence();
			
			RppPermohonan r = (RppPermohonan) mp.find(RppPermohonan.class, getParam("idpermohonan"));
			
			if(r!=null){
				
				mp.begin();
				
				//filter button by no.lo belum isi.
				
				/**Update data rpppermohonan*/
				r.setStatus((Status) mp.find(Status.class, "1435093978588")); //TEMPAHAN DIBATALKAN
				r.setTarikhPembatalan(new Date());
				r.setCatatanPembatalan("PERMOHONAN BERKELOMPOK DIBATALKAN");
				r.setPemohonBatal((Users) mp.find(Users.class, userId));
				
				/**Delete jadual tempahan*/
				List<RppJadualTempahan> listTempahan = mp.list("select x from RppJadualTempahan x where x.permohonan.id = '"+r.getId()+"' ");
				if(listTempahan != null){
					for(int i=0;i<listTempahan.size();i++){
						mp.remove(listTempahan.get(i));
					}
				}
				
				/**Update rpp akaun to void*/
				List<RppAkaun> lk = mp.list("select x from RppAkaun x where x.permohonan.id = '"+r.getId()+"' ");
				if(lk != null){
					for(int i=0;i<lk.size();i++){
						RppAkaun lj = lk.get(i);
						lj.setAmaunVoid(lj.getDebit());
						lj.setFlagVoid("Y");
						lj.setTarikhVoid(new Date());
					}
				}
				
				mp.commit();
				
				context.put("r",r);
			}
			
		} catch (Exception e) {
			System.out.println("Error batalPermohonanLengkap : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		return getMaklumatPermohonan();
	}
	
	
	@Command("getMaklumatLO")
	public String getMaklumatLO() throws Exception {
		
		String idpermohonan = getParam("idpermohonan");
		
		try {
			mp = new MyPersistence();
			
			RppPermohonan r = (RppPermohonan) mp.find(RppPermohonan.class, idpermohonan);
			context.put("r",r);
			context.put("selectedTab", "3");
			
		} catch (Exception e) {
			System.out.println("Error getMaklumatLO : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/form/maklumatLO/start.vm";
	}
	
	@Command("saveLOTempahan")
	public String saveLOTempahan() throws Exception {
		String idpermohonan = getParam("idpermohonan");
		String kaedahBayaran = getParam("kaedahBayaran");
		try {
			
			mp = new MyPersistence();
			RppPermohonan r = (RppPermohonan) mp.find(RppPermohonan.class, idpermohonan);
			
			mp.begin();
			r.setCatatanLo(getParam("catatanLo")); //catatan general.
			r.setKaedahBayaran((CaraBayar) mp.find(CaraBayar.class,kaedahBayaran));
			
			if(kaedahBayaran.equalsIgnoreCase("T")){
				//create invois/depoist
				r.setNoLoTempahan(null);
				UtilRpp.saveInvoisDepositInSql(mp,r.getPemohon().getId(),r);
			}else{
				
				r.setNoLoTempahan(getParam("noLoTempahan"));
				
				//delete invois/depoist
				List<RppAkaun> lk = UtilRpp.getListTempahanDanBayaran(mp,r);
				if(lk.size() > 0){
					for(int i=0;i<lk.size();i++){
						RppAkaun lj = lk.get(i);
			
						KewInvois inv = (KewInvois) mp.get("select x from KewInvois x where x.idLejar = '"+lj.getId()+"' ");
						if(inv!=null){ mp.remove(inv); }
						
						KewDeposit dep = (KewDeposit) mp.get("select x from KewDeposit x where x.idLejar = '"+lj.getId()+"' ");
						if(dep!=null){ mp.remove(dep); }
					}
				}
			}
			
			mp.commit();
			
		} catch (Exception e) {
			System.out.println("Error saveLOTempahan "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		return getMaklumatLO();
	}
	
	@SuppressWarnings("unchecked")
	@Command("openPopupSenaraiSelenggara")
	public String openPopupSenaraiSelenggara() throws Exception {
		
		String today = bph.utils.Util.getCurrentDate("yyyy-MM-dd");
		
		try{
			mp = new MyPersistence();
			
			RppPeranginan peranginan = (RppPeranginan) mp.find(RppPeranginan.class, getParam("idrp"));
			
			List<RppSelenggara> list = null;
			if(peranginan != null){
				list = mp.list("select x from RppSelenggara x where x.id in "+
							   " (select y.rppSelenggara.id from RppSelenggaraUnitLokasi y where y.rppPeranginan.id = '"+peranginan.getId()+"' ) "+
							   " and x.tarikhMula >= '"+today+"' "+
							   " order by x.tarikhMula asc"); // tarikh sysdate dan kedepan dan by rpp
			}
			
			context.put("listSelenggara", list);
			context.put("peranginan", peranginan);
			
		}catch (Exception ex){
			System.out.println("ERROR openPopupSenaraiSelenggara : "+ex.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}	
		return getPath() + "/form/popup/senaraiSelenggara.vm";
	}
	
	@Command("changeField")
	public String changeField() throws Exception {
		String idpermohonan = getParam("idpermohonan");
		try{
			mp = new MyPersistence();
			RppPermohonan r = (RppPermohonan) mp.find(RppPermohonan.class, idpermohonan);
			context.put("r",r);
			context.put("valLo", getParam("kaedahBayaran"));
		}catch (Exception ex){
			System.out.println("ERROR changeField : "+ex.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}	
		return getPath() + "/form/maklumatLO/fieldLO.vm";
	}
	
	//ADD BY PEJE
	@Command("getMaklumatPermohonan")
	public String getMaklumatPermohonan() throws Exception {
		
		String idpermohonan = getParam("idpermohonan");
		
		try {
			mp = new MyPersistence();
			
			RppPermohonan r = (RppPermohonan) mp.find(RppPermohonan.class, idpermohonan);
			context.put("r",r);
			context.put("selectedTab", "1");
			
		} catch (Exception e) {
			System.out.println("Error getMaklumatPermohonan : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/entry_page.vm";
	}
	
	
//	STNBY
//	@Command("checkingDateInOut")
//	public String checkingDateInOut() throws Exception {
//		
//		Date dateIn = getDate("tarikhMasukRpp");
//		Date dateOut = getDate("tarikhKeluarRpp");
//		String dtIn = new SimpleDateFormat("yyyy-MM-dd").format(dateIn);
//		String dtOut = new SimpleDateFormat("yyyy-MM-dd").format(dateOut);
//		
//		boolean isPeak = true;
//		try {
//			isPeak = UtilRpp.checkWaktuPuncak(dtIn);
//		} catch (Exception e) {
//			System.out.println("error checkWaktuPuncak : "+e.getMessage());
//		}
//		context.put("isPeak",isPeak);
//		
//		Calendar cal = Calendar.getInstance(); 
//		cal.add(Calendar.MONTH, 3);
//		Date afterMonth = cal.getTime();
//		
//		REQUEST BY EN.SHAM IR 13082015		
//		boolean less3Month = true;
//		if(dateIn.after(afterMonth) || dateIn.equals(afterMonth)){
//			less3Month = false;
//		}else{
//			less3Month = true;
//		}
//		context.put("less3Month",false);
//		
//		return getPath() + "/form/errorDate.vm";
//	}

}




