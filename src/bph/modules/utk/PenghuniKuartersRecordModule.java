/* Author :
 * zulfazdliabuas@gmail.com Data 2015-2017
 */

package bph.modules.utk;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;

import org.apache.log4j.Logger;

import portal.module.entity.Users;
import portal.module.entity.UsersJob;
import portal.module.entity.UsersSpouse;
import bph.entities.kew.KewJenisBayaran;
import bph.entities.kewangan.KewInvois;
import bph.entities.kod.Bandar;
import bph.entities.kod.JenisOperasiUtk;
import bph.entities.kod.JenisPelanggaranSyaratUtk;
import bph.entities.kod.KodHasil;
import bph.entities.kod.SebabHilangKelayakanUtk;
import bph.entities.qtr.KuaPenghuni;
import bph.entities.utk.UtkAbt;
import bph.entities.utk.UtkAkaun;
import bph.entities.utk.UtkHilangKelayakan;
import bph.entities.utk.UtkKesPeguam;
import bph.entities.utk.UtkKesalahan;
import bph.utils.DataUtil;
import bph.utils.Util;

public class PenghuniKuartersRecordModule extends LebahRecordTemplateModule<KuaPenghuni>{

	/**
	 * 
	 */
	static Logger myLogger = Logger.getLogger("PenghuniKuartersRecordModulev");
	private static final long serialVersionUID = 1L;
	private Util util = new Util();
	private DataUtil dataUtil;
	
	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public void afterSave(KuaPenghuni arg0) {
		// TODO Auto-generated method stub
		context.put("selectedTab", "1");
		
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
		
	} 	

	@Override
	public void begin() {
		// TODO Auto-generated method stub
		this.setDisableAddNewRecordButton(true);
		this.setDisableDefaultButton(true);
		context.remove("pekerjaan");
		context.remove("selectNegeri");
		
		dataUtil = DataUtil.getInstance(db);
		
		List<JenisOperasiUtk> jenisOperasiUtkList = dataUtil.getListJenisOperasiUtk();
		context.put("selectJenisOperasi", jenisOperasiUtkList);

		List<SebabHilangKelayakanUtk> sebabHilangKelayakanUtkList = dataUtil.getListSebabHilangKelayakanUtk();
		context.put("selectSebabHk", sebabHilangKelayakanUtkList);

		context.put("selectNegeri", dataUtil.getListNegeri());
		context.put ("path",getPath());
		context.put ("util",util);
		
		addFilter();
	}
	
	public void addFilter(){
		
//		this.addFilter("tarikhKeluar is null");
		
	}

	@Override
	public boolean delete(KuaPenghuni r) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/utk/penghuniKuarters";
	}

	@Override
	public Class<KuaPenghuni> getPersistenceClass() {
		// TODO Auto-generated method stub
		return KuaPenghuni.class;
	}

	@Override
	public void getRelatedData(KuaPenghuni r) {
		// TODO Auto-generated method stub
		/*List<UsersJob> listPekerjaan = db.list("Select x from UsersJob x where x.users.id='" + r.getPemohon().getId() + "'");
		for(UsersJob pekerjaan : listPekerjaan)*/
		
		UsersJob pekerjaan = (UsersJob) db.get("Select x from UsersJob x where x.users.id='" + r.getPemohon().getId() + "'");
			context.put("pekerjaan", pekerjaan);

		UsersSpouse pasangan = (UsersSpouse) db.get("SELECT pa FROM UsersSpouse pa WHERE pa.users.id = '" + r.getPemohon().getId() + "'"); 
		if (pasangan != null) {
			context.put("pasangan", db.find(UsersSpouse.class, pasangan.getId()));
		}
		
			
		context.put("selectedTab", "1");
		
	}

	@Override
	public void save(KuaPenghuni r) throws Exception {
		// TODO Auto-generated method stub
//		r.setStatus(get("status"));
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		// TODO Auto-generated method stub

//		String noPenghuni = get("noPenghuni");
		String noKp = get("noKp");
		String namaPenghuni = get("namaPenghuni");
		String noUnit = get("noUnit");
//		String findNegeri = get("findNegeri");
//		String findBandar = get("findBandar");
		String findNoFailLama = get("findNoFailLama");
		String findPresint = get("findFresint");
		String findFasa = get("findFasa");
		String findBlok = get("findBlok");
		
		
		Map<String, Object> map = new HashMap<String, Object>();

//		map.put("id", noPenghuni);
		map.put("pemohon.noKP", noKp);
		map.put("pemohon.userName", namaPenghuni);
		map.put("kuarters.noUnit", noUnit);
//		map.put("kuarters.bandar.negeri.id", findNegeri);
//		map.put("kuarters.bandar.id", findBandar);
		map.put("kuarters.alamat2", findPresint);
		map.put("kuarters.fasa.keterangan", findFasa);
		map.put("kuarters.blok", findBlok);
		map.put("noFailLama", findNoFailLama);
		
		return map;
	}
	
	/************************************************************** START SENARAI TAB******************************************/
	/*----------------------- START TAB MAKLUMAT PENGHUNI --------------*/
	@Command("getMaklumatPenghuni")
	public String getMaklumatPenghuni() {
		
//		System.out.println("ceking id penghuni ====== " + get("idPenghuni"));
		
		context.remove("pekerjaan");
		
		KuaPenghuni penghuni = db.find(KuaPenghuni.class, get("idPenghuni"));
		context.put("r", penghuni);
		
//		List<UsersJob> listPekerjaan = db.list("Select x from UsersJob x where x.users.id='" + penghuni.getPemohon().getId() + "'");
		UsersJob pekerjaan = (UsersJob) db.get("Select x from UsersJob x where x.users.id='" + penghuni.getPemohon().getId() + "'");
//		context.put("pekerjaan", pekerjaan);
//		for(UsersJob pekerjaan : listPekerjaan)
		context.put("pekerjaan", pekerjaan);
				
		UsersSpouse pasangan = (UsersSpouse) db.get("SELECT pa FROM UsersSpouse pa WHERE pa.users.id = '" + penghuni.getPemohon().getId() + "'"); 
		if (pasangan != null) {
			context.put("pasangan", db.find(UsersSpouse.class, pasangan.getId()));
		}
		
		context.put("selectedTab", "1");
		return getPath() + "/entry_page.vm";
	}
	/*-----------------------END TAB MAKLUMAT PENGHUNI --------------*/
	
	/*----------------------- START TAB PELANGGARAN SYARAT --------------*/
	@SuppressWarnings("unchecked")
	@Command("getPelanggaranSyarat")
	public String getPelanggaranSyarat() {
		
//		System.out.println("ceking ====== " + get("idPenghuni"));

		List<UtkKesalahan> psList = db.list("Select x from UtkKesalahan x where x.penghuni.id ='" + get("idPenghuni") + "' and x.idJenisOperasi in ('CP','PS','KT')");  // CP = PENGUNCIAN TAYAR, PS = PELANGGARAN SYARAT,'KT' = KENDERAAN TERSADAI 
		context.put("psList", psList);
		for(UtkKesalahan k : psList){
			myLogger.debug("PRINT ID KESALAHAN " + k.getId());
		}
		
		context.put("selectedTab", "2");
		return getPath() + "/entry_page.vm";
	}
	/*----------------------- END TAB PELANGGARAN SYARAT --------------*/
	
	/*-------------------- START FUNCTION TAB HILANG KELAYAKAN ------------*/
	@SuppressWarnings("unchecked")
	@Command("getHilangKelayakan")
	public String getHilangKelayakan() {

		List<UtkHilangKelayakan> hkList = db.list("Select x from UtkHilangKelayakan x where x.penghuni.id='" + get("idPenghuni") + "'");
		
		context.put("hkList", hkList);
		context.put("selectedTab", "3");

		return getPath() + "/entry_page.vm";
	}
	/*-------------------- END FUNCTION TAB HILANG KELAYAKAN ------------*/
	
	/*---------------- START FUNCTION TAB ABT ------------*/
	@SuppressWarnings("unchecked")
	@Command("getABT")
	public String getABT() {

		List<UtkAbt> abtList = db.list("Select x from UtkAbt x where x.penghuni.id='" + get("idPenghuni") + "'");
		
		context.put("abtList", abtList);
		context.put("selectedTab", "4");
		return getPath() + "/entry_page.vm";
	}
	/*---------------- END FUNCTION TAB ABT ------------*/
	
	/*---------------- START FUNCTION TAB KES PEGUAM ------------*/
	@SuppressWarnings("unchecked")
	@Command("getKesPeguam")
	public String getKesPeguam() {

		List<UtkKesPeguam> kesPeguamList = db.list("Select x from UtkKesPeguam x where x.penghuni.id='" + get("idPenghuni") + "'");
		
		context.put("kesPeguamList", kesPeguamList);
		context.put("selectedTab", "5");
		return getPath() + "/entry_page.vm";
	}
	/*---------------- END FUNCTION TAB KES PEGUAM ------------*/
	
//	/*------------ START FUNCTION TAB MAKLUMAT NOTIS ------------*/
//	@Command("getNotis")
//		context.remove("idJenisNotis");
//		context.remove("flagPeringatan");
//						
//		UtkKesalahan kesalahan = db.find(UtkKesalahan.class, getParam("idPenghuni"));
//		context.put("kesalahan", kesalahan);
//		
//		List<UtkNotis> notisList = db.list("Select x from UtkNotis x where x.kesalahan.id='" + kesalahan.getId() + "'") ;
//		context.put("listNotis", notisList);
//		
//		context.put("selectedTab", "6");
//
//		return getPath() + "/entry_page.vm";
//	}
//	/*------------ END FUNCTION TAB MAKLUMAT NOTIS ------------*/
//	
//	/*------------ START FUNCTIN TAB MAKLUMAT RAYUAN --------*/
//	@SuppressWarnings("unchecked")
//	@Command("getRayuan")
//	public String getRayuan() {
//
//		context.put("selectedTab", "7");
//
//		return getPath() + "/entry_page.vm";
//	}
//	/*------------ START FUNCTIN TAB MAKLUMAT RAYUAN --------*/
	/************************************************************** END SENARAI TAB******************************************/

	
	
	/************************************ START PELANGGARAN SYARAT ************************************/
	/*-------------------- START FUNCTION TAMBAH PELANGGARAN SYARAT ------------*/
	@Command("addLanggarSyarat")
	public String addLanggarSyarat() {
		String rekod = getParam("rekod");
		myLogger.debug("rekod === " + rekod);
//		context.remove("rekod");
		context.put("rekod","");
		return getPath() + "/pelanggaranSyarat/entry_page.vm";
	}
	/*-------------------- END FUNCTION TAMBAH PELANGGARAN SYARAT ------------*/
	
	/*-------------------- START FUNCTION EDIT PELANGGARAN SYARAT ------------*/
	@Command("editPS")
	public String editPS() {
		context.put("rekod", "");
		
		UtkKesalahan ps = db.find(UtkKesalahan.class,get("idPS"));
		String jenisOperasi = ps.getIdJenisOperasi();
		myLogger.debug("PRINT jenisOperasi ===== ::::: " + jenisOperasi);
		
		 UtkAkaun ak = (UtkAkaun) db.get("Select x from UtkAkaun x where x.kesalahan.id='" + get("idPS") + "'");
	        
		if("PS".equals(jenisOperasi)){
//			System.out.println("ceking jenis operasi ====== " + jenisOperasi);
			List<JenisPelanggaranSyaratUtk> jenisPelanggaranSyaratUtkList = dataUtil.getListJenisPelanggaranSyaratUtk(ps.getJenisPelanggaranSyarat().getFlagKes());
			context.put("selectJenisPelanggaran", jenisPelanggaranSyaratUtkList);
		}
		
		context.put("rekod", ps);
		context.put("ak", ak);
		context.put("selectedTab", "2");
		return getPath() + "/pelanggaranSyarat/entry_page.vm";
	}
	/*-------------------- END FUNCTION EDIT PELANGGARAN SYARAT ------------*/
	
	/*-------------------- START FUNCTION SAVE PELANGGARAN SYARAT ------------*/
	@Command("savePelanggaranSyarat")
	public String savePelanggaranSyarat() throws Exception {
		String statusInfo="";
		myLogger.debug("PRINT idPS ===== " + getParam("idPS"));
		
		UtkKesalahan rekod = db.find(UtkKesalahan.class, get("idPS"));
		Boolean addMaklumatPS = false;
		
		if(rekod == null){
			addMaklumatPS = true;
			rekod = new UtkKesalahan();
		}
		
		String idJenisOperasi =  get("idJenisOperasi"); // JENIS PELANGGARAN SYARAT
		myLogger.debug("PRINT idJenisOperasi ===== " + idJenisOperasi);
		
		if("PS".equals(idJenisOperasi)){
			
			String idJenisPS = get("idJenisPelanggaran");
			myLogger.debug("PRINT idJenisPS ===== " + idJenisPS);
			
			rekod.setJenisPelanggaranSyarat(db.find(JenisPelanggaranSyaratUtk.class, idJenisPS));
			
			if("1435633886800".equals(idJenisPS)){ // 1435633886800 = PENGUNCIAN TAYAR KENDERAAN
				rekod.setJenisKenderaan(get("jenisKenderaan"));
				rekod.setNoPlat(get("noPlat"));
			}
		} else if("CP".equals(idJenisOperasi)){
			rekod.setJenisKenderaan(get("jenisKenderaan"));
			rekod.setNoPlat(get("noPlat"));
		}
		
		rekod.setJenisOperasi(db.find(JenisOperasiUtk.class, get("idJenisOperasi")));
		rekod.setPenghuni(db.find(KuaPenghuni.class, get("idPenghuni")));
		rekod.setCatatan(get("catatan"));
		rekod.setTarikh(getDate("tarikh"));
		rekod.setStatusPenghuni(get("flagStatusTindakan"));
		
//		System.out.println("ceking id kesalahan = '" + rekod.getId() + "'");
		myLogger.debug("PRINT ID KESALAHAN ===== " + rekod.getId());
		
		db.begin();
		if ( addMaklumatPS ){
			db.persist(rekod);
		}try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			statusInfo = "error";
		}

		if("PS".equals(idJenisOperasi)){
			String idJenisPS = get("idJenisPelanggaran");
			if("1435633886800".equals(idJenisPS)){
				Boolean akaun = saveAkaun(rekod.getId());
			}
		} else if("CP".equals(idJenisOperasi)){
			Boolean akaun = saveAkaun(rekod.getId());
		}
        
		context.put("selectedTab", "2");
		return getPath() + "/pelanggaranSyarat/entry_page.vm";
	}
	
//	@Command("savePelanggaranSyarat")
//	public String savePelanggaranSyarat() throws Exception {
//		String statusInfo="";
//		myLogger.debug("PRINT idPS ===== " + getParam("idPS"));
//		
//		UtkKesalahan rekod = db.find(UtkKesalahan.class, get("idPS"));
//		Boolean addMaklumatPS = false;
//		
//		if(rekod == null){
//			addMaklumatPS = true;
//			rekod = new UtkKesalahan();
//		}
//		
//		String idJenisOperasi =  get("idJenisOperasi"); // JENIS PELANGGARAN SYARAT
//		myLogger.debug("PRINT idJenisOperasi ===== " + idJenisOperasi);
//		
//		if("PS".equals(idJenisOperasi)){
//			
//			String idJenisPS = get("idJenisPelanggaran");
//			myLogger.debug("PRINT idJenisPS ===== " + idJenisPS);
//			
//			rekod.setJenisPelanggaranSyarat(db.find(JenisPelanggaranSyaratUtk.class, idJenisPS));
//			
//			if("1435633886800".equals(idJenisPS)){ // 1435633886800 = PENGUNCIAN TAYAR KENDERAAN
//				rekod.setJenisKenderaan(get("jenisKenderaan"));
//				rekod.setNoPlat(get("noPlat"));
//			}
//		} else if("CP".equals(idJenisOperasi)){
//			rekod.setJenisKenderaan(get("jenisKenderaan"));
//			rekod.setNoPlat(get("noPlat"));
//		}
//		
//		rekod.setJenisOperasi(db.find(JenisOperasiUtk.class, get("idJenisOperasi")));
//		rekod.setPenghuni(db.find(KuaPenghuni.class, get("idPenghuni")));
//		rekod.setCatatan(get("catatan"));
//		rekod.setTarikh(getDate("tarikh"));
//		rekod.setStatusPenghuni(get("flagStatusTindakan"));
//		
////		System.out.println("ceking id kesalahan = '" + rekod.getId() + "'");
//		myLogger.debug("PRINT ID KESALAHAN ===== " + rekod.getId());
//		
//		db.begin();
//		if ( addMaklumatPS ){
//			db.persist(rekod);
//		}try {
//			db.commit();
//			statusInfo = "success";
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			statusInfo = "error";
//		}
//
//		if("PS".equals(idJenisOperasi)){
//			String idJenisPS = get("idJenisPelanggaran");
//			if("1435633886800".equals(idJenisPS)){
//				Boolean akaun = saveAkaun(rekod.getId());
//			}
//		} else if("CP".equals(idJenisOperasi)){
//			Boolean akaun = saveAkaun(rekod.getId());
//		}
//        
//		context.put("selectedTab", "2");
//		return getPath() + "/pelanggaranSyarat/entry_page.vm";
//	}
	/*-------------------- END FUNCTION SAVE PELANGGARAN SYARAT ------------*/
	
	/************************************ START PUSH DATA TO table utk_akaun ************************************/
	
	public Boolean saveAkaun(String idPS) throws ParseException {
		Boolean statusInfo = false;
		
		UtkAkaun ak = (UtkAkaun) db.get("Select x from UtkAkaun x where x.kesalahan.id='" + idPS + "'");
		Boolean addMaklumatAkaun = false;
		
		if(ak == null){
			addMaklumatAkaun = true;
			ak = new UtkAkaun();
		}
//		System.out.println("ceking id dalam akaun ======== " + idPS);
		ak.setKesalahan(db.find(UtkKesalahan.class, idPS));
		ak.setDebit(Util.getDouble(Util.RemoveComma(get("amaun"))));
		ak.setFlagBayaran("DENDA"); // SEWA / DEPOSIT
		ak.setFlagBayar("T"); // BELUM BAYAR
		ak.setFlagQueue("T");
		ak.setJenisBayaran(db.find(KewJenisBayaran.class,"08")); // BAYARAN LAIN
		ak.setKodHasil(db.find(KodHasil.class,"76199")); // PELBAGAI BAYARAN HUKUMAN
		ak.setNoInvois(idPS);
		ak.setUserPendaftar(db.find(Users.class,userId)); //user login
		ak.setTarikhDaftar(new Date()); 
		
		db.begin();
		if ( addMaklumatAkaun ) db.persist(ak);
		
		try {
			db.commit();
			Boolean kewInvois = pushKew(idPS);
			if(kewInvois){
				statusInfo = true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			statusInfo = false;
		}
		return statusInfo;
	}
	
	public Boolean pushKew(String idPS) throws ParseException {
		Boolean statusInfo = false;
		
		UtkAkaun ak = (UtkAkaun) db.get("Select x from UtkAkaun x where x.kesalahan.id='" + idPS + "'");
		KewInvois inv = (KewInvois) db.get("Select x from KewInvois x where x.idLejar='" + ak.getId() + "'");
		Boolean addMaklumatInv = false;
		
		if(inv == null){
			addMaklumatInv = true;
			inv = new KewInvois();
			inv.setTarikhDaftar(new Date()); 
		}
		inv.setDebit(ak.getDebit());
		inv.setFlagBayaran(ak.getFlagBayaran()); // SEWA / DEPOSIT
		inv.setFlagQueue("T");
		inv.setIdLejar(ak.getId());
		inv.setJenisBayaran(db.find(KewJenisBayaran.class,"08")); // BAYARAN LAIN
		inv.setKeteranganBayaran(ak.getKodHasil().getKeterangan());
		inv.setKodHasil(ak.getKodHasil());  // PELBAGAI BAYARAN HUKUMAN
		inv.setNoInvois(ak.getNoInvois());
		inv.setNoRujukan(ak.getKesalahan().getOperasi().getNoFail()); // NO FAIL OPERASI
		inv.setPembayar(db.find(Users.class,ak.getKesalahan().getPenghuni().getPemohon().getId()));
		inv.setTarikhInvois(ak.getKesalahan().getOperasi().getTarikhOperasi());
		inv.setUserPendaftar(db.find(Users.class, userId)); //user login
		inv.setTarikhKemaskini(new Date());
		inv.setUserKemaskini(db.find(Users.class, userId));
		
		db.begin();
		if ( addMaklumatInv ) db.persist(inv);
		
		try {
			db.commit();
			statusInfo = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			statusInfo = false;
		}
		return statusInfo;
	}
	/************************************ END PUSH DATA ************************************/
	
	/*-------------------- START FUNCTION REMOVE PELANGGARAN SYARAT ------------*/
	@Command("removePelanggaranSyarat")
	public String removePelanggaranSyarat() {
		String statusInfo = "";
		
		UtkKesalahan rekod = db.find(UtkKesalahan.class, get("idPS"));

		db.begin();
		db.remove(rekod);
		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			statusInfo = "error";
		}
		
		List<UtkKesalahan> psList = db.list("Select x from UtkKesalahan x where x.penghuni.id ='" + get("idPenghuni") + "' and x.idJenisOperasi in ('CP','PS','KT')");
		context.put("psList", psList);
		
        context.put("selectedTab", "2");
		
		return getPath() + "/entry_page.vm";
	}
	/*-------------------- END FUNCTION REMOVE PELANGGARAN SYARAT ------------*/
	/************************************ END PELANGGARAN SYARAT ************************************/
	
	
	/************************************ START HILANG KELAYAKAN ************************************/	
	/*-------------------- START FUNCTION TAMBAH HILANG KELAYAKAN ------------*/
	@Command("addHilang")
	public String addHilang() {
		context.put("rekod", "");
		return getPath() + "/hilangKelayakan/entry_page.vm";
	}
	/*-------------------- END FUNCTION TAMBAH HILANG KELAYAKAN ------------*/
	
	/*-------------------- START FUNCTION EDIT HILANG KELAYAKAN ------------*/
	@Command("editHK")
	public String editHK() {
		context.put("rekod", "");
		
		UtkHilangKelayakan hk = db.find(UtkHilangKelayakan.class,get("idHK"));
		
		context.put("rekod", hk);
		context.put("selectedTab", "3");
		return getPath() + "/hilangKelayakan/entry_page.vm";
	}
	/*-------------------- END FUNCTION EDIT HILANG KELAYAKAN ------------*/
	
	/*-------------------- START FUNCTION SIMPAN HILANG KELAYAKAN ------------*/
	@Command("saveHilangKelayakan")
	public String saveHilangKelayakan() throws Exception {
		String statusInfo="";
		
		UtkHilangKelayakan rekod = db.find(UtkHilangKelayakan.class, get("idHK"));
		Boolean addMaklumatHK = false;
		
		if(rekod == null){
			addMaklumatHK = true;
			rekod = new UtkHilangKelayakan();
		}
		
		rekod.setFlagSebab(db.find(SebabHilangKelayakanUtk.class, get("idSebabHk")));
		rekod.setPenghuni(db.find(KuaPenghuni.class, get("idPenghuni")));
		rekod.setCatatan(get("catatan"));
		rekod.setTarikh(getDate("tarikh"));
		rekod.setStatusPenghuni(get("flagStatusTindakan"));
		//new add
		rekod.setCola(getParam("cola"));
		rekod.setIwk(getParam("iwk"));
		rekod.setKadarBiasa(getParam("kadarBiasa"));
		rekod.setKadarPasaran(getParam("kadarPasaran"));
		rekod.setTarikhHilangKelayakan(getDate("tarikhHilangKelayakan"));
		rekod.setTarikhMulaBiasa(getDate("tarikhMulaBiasa"));
		rekod.setTarikhMulaCola(getDate("tarikhMulaCola"));
		rekod.setTarikhMulaIwk(getDate("tarikhMulaIwk"));
		rekod.setTarikhMulaPasaran(getDate("tarikhMulaPasaran"));
		rekod.setTarikhTamatBiasa(getDate("tarikhTamatBiasa"));
		rekod.setTarikhTamatCola(getDate("tarikhTamatCola"));
		rekod.setTarikhTamatHilangKelayakan(getDate("tarikhTamatHilangKelayakan"));
		rekod.setTarikhTamatIwk(getDate("tarikhTamatIwk"));
		rekod.setTarikhTamatPasaran(getDate("tarikhTamatPasaran"));
		rekod.setSurathilangKelayakan(getParam("suratHk"));
		rekod.setSlipGaji(getParam("slipGaji"));
		rekod.setTarikhMasuk(new Date());
		rekod.setDaftarOleh(db.find(Users.class, userId));
		
		db.begin();
		if ( addMaklumatHK ){
			db.persist(rekod);
		}else {
			rekod.setKemaskiniOleh(db.find(Users.class, userId));
			rekod.setTarikhKemaskini(new Date());
		}
		
		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			statusInfo = "error";
		}

		context.put("selectedTab", "3");
		return getPath() + "/hilangKelayakan/entry_page.vm";
	}
	/*-------------------- END FUNCTION SIMPAN HILANG KELAYAKAN ------------*/
	
	/*-------------------- START FUNCTION REMOVE HILANG KELAYAKAN ------------*/
	@Command("removeHilangKelayakan")
	public String removeHilangKelayakan() {
		String statusInfo = "";
		
		UtkHilangKelayakan rekod = db.find(UtkHilangKelayakan.class, get("idHK"));

		db.begin();
		db.remove(rekod);
		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			statusInfo = "error";
		}
		
		List<UtkHilangKelayakan> hkList = db.list("Select x from UtkHilangKelayakan x where x.penghuni.id='" + get("idPenghuni") + "'");
		
		context.put("hkList", hkList);
        context.put("selectedTab", "3");
		
		return getPath() + "/entry_page.vm";
	}
	/*-------------------- END FUNCTION REMOVE HILANG KELAYAKAN ------------*/
	/************************************ END HILANG KELAYAKAN ************************************/

	
	/************************************ START ABT ************************************/
	/*---------------- START FUNCTION EDIT ABT ------------*/
	@Command("funcTambahAbt")
	public String funcTambahAbt() {
		context.put("rekod", "");
		
		UtkAbt abt = db.find(UtkAbt.class,get("idABT"));
		
		context.put("rekod", abt);
		context.put("selectedTab", "4");
		return getPath() + "/ABT/popupFormAddMaklumatAbt.vm";
	}
	
	@Command("editABT")
	public String editABT() {
		context.put("rekod", "");
		
		UtkAbt abt = db.find(UtkAbt.class,get("idABT"));
		
		context.put("rekod", abt);
		context.put("selectedTab", "4");
		return getPath() + "/ABT/entry_page.vm";
	}
	/*---------------- END FUNCTION EDIT ABT ------------*/
	
	@Command("funcSimpanAtauKemaskiniAbt")
	public String funcSimpanAtauKemaskiniAbt(){
		String statusInfo="";
		context.put("rekod", "");
		
//		UtkAbt abt = db.find(UtkAbt.class,get("idABT"));
		
		UtkAbt rekod = db.find(UtkAbt.class, get("idABT"));
		Boolean simpanMaklumatABT = false;
		
		if(rekod == null){
			simpanMaklumatABT = true;
			rekod = new UtkAbt();
		}
		rekod.setPenghuni(db.find(KuaPenghuni.class, get("idPenghuni")));
//		rekod.setAmaun(Double.valueOf(Util.RemoveComma(get("amaun"))));
		rekod.setJumlahTunggakan(Double.valueOf(Util.RemoveComma(get("jumlahTunggakan"))));
		rekod.setJumlahBayaran(Double.valueOf(Util.RemoveComma(get("jumlahBayaran"))));
		rekod.setFlagBayaran(getParam("flagBayaran")); // BELUM BAYAR = 1 TELAH BAYAR = 2 TUNGGAKAN = 3
		rekod.setCatatan(getParam("catatan"));
		rekod.setNoFail(getParam("noFail"));
		rekod.setBakiTunggakan(Double.valueOf(Util.RemoveComma(getParam("bakiTunggakan"))));
//		rekod.setBezaTunggakan(Double.valueOf(Util.RemoveComma(getParam("bezaTunggakan"))));
		rekod.setTarikhMulaTunggakan(getDate("tarikhMulaTunggakan"));
		rekod.setTarikhTamatTunggakan(getDate("tarikhTamatTunggakan"));
		rekod.setIdMasuk(db.find(Users.class, userId));
		rekod.setTarikhMasuk(new Date());
		rekod.setBulanTunggakan(getParamAsInteger("bulanTunggakan"));
		rekod.setTarikhBayaran(getDate("tarikhBayaran"));
		
		db.begin();
		if ( simpanMaklumatABT ){
			db.persist(rekod);
		}else {
			rekod.setIdKemaskini(db.find(Users.class, userId));
			rekod.setTarikhKemaskini(new Date());
		}
		
		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			statusInfo = "error";
		}
		
		context.put("rekod", rekod);
		context.put("selectedTab", "4");
		return getPath() + "/ABT/entry_page.vm";
	}
	
	
	/************************************ END ABT ************************************/
	
	
	/************************************ START KES PEGUAM ************************************/	
	/*---------------- START FUNCTION TAMBAH KES PEGUAM ------------*/
	@Command("addPeguam")
	public String addPeguam() {
		context.put("rekod", "");
		
		return getPath() + "/kesPeguam/entry_page.vm";
	}
	/*---------------- END FUNCTION TAMBAH KES PEGUAM ------------*/
	
	/*---------------- START FUNCTION EDIT KES PEGUAM ------------*/
	@Command("editKP")
	public String editKP() {
		context.put("rekod", "");
		
		UtkKesPeguam kp = db.find(UtkKesPeguam.class,getParam("idKP"));
		
		context.put("rekod", kp);
		context.put("selectedTab", "5");
		return getPath() + "/kesPeguam/entry_page.vm";
	}
	/*---------------- END FUNCTION EDIT KES PEGUAM ------------*/
	
	/*---------------- START FUNCTION SIMPAN KES PEGUAM ------------*/
	@Command("simpanPeguam")
	public String simpanPeguam() throws Exception {
		String statusInfo = "";
		
		UtkKesPeguam rekod = db.find(UtkKesPeguam.class, get("idKP"));
		Boolean addMaklumatKP = false;
		
		if(rekod == null){
			addMaklumatKP = true;
			rekod = new UtkKesPeguam();
		}
		
		rekod.setPenghuni(db.find(KuaPenghuni.class, get("idPenghuni")));
		rekod.setCatatan(get("catatan"));
		rekod.setTarikhKeputusan(getDate("tarikh"));
		rekod.setFlagKeputusan(get("flagKeputusan"));
		rekod.setStatusJenisKes(getParam("statusJenisKes"));
		
		db.begin();
		if ( addMaklumatKP ) db.persist(rekod);
		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			statusInfo = "error";
		}

		context.put("selectedTab", "5");
		return getPath() + "/kesPeguam/entry_page.vm";
	}
	/*---------------- END FUNCTION SIMPAN KES PEGUAM ------------*/

	/*---------------- START FUNCTION REMOVE KES PEGUAM ------------*/
	@Command("removePeguam")
	public String removePeguam() {
		String statusInfo = "";
		
		UtkKesPeguam rekod = db.find(UtkKesPeguam.class, get("idKP"));

		db.begin();
		db.remove(rekod);
		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			statusInfo = "error";
		}
		
		List<UtkKesPeguam> kesPeguamList = db.list("Select x from UtkKesPeguam x where x.penghuni.id='" + get("idPenghuni") + "'");
		
		context.put("kesPeguamList", kesPeguamList);
        context.put("selectedTab", "5");
		
		return getPath() + "/entry_page.vm";
	}
	/*---------------- END FUNCTION REMOVE KES PEGUAM ------------*/
	/************************************ END KES PEGUAM ************************************/

	
//	/************************************ START MAKLUMAT NOTIS ************************************/	
//	/*------------ START FUNCTION TAMBAH MAKLUMAT NOTIS ------------*/
//	@Command("addNotis")
//	public String addNotis() {
//		
//		context.remove("rekod");
//		
//		context.put("selectedTab", "6");
//		return getPath() + "/notis/popupMaklumatNotis.vm";
//	}
//	/*------------ END FUNCTION TAMBAH MAKLUMAT NOTIS ------------*/
//	
//	/*------------ START FUNCTION EDIT MAKLUMAT NOTIS ------------*/
//	@Command("editNotis")
//	public String editNotis() {
//		int flagPeringatan = 1; 
//		context.remove("rekod");
//		
//		UtkNotis rekod = (UtkNotis) db.get("Select x from UtkNotis x where x.id='" + get("idNotis") + "'") ;
//		
//		context.put("idJenisNotis", rekod.getFlagJenisNotis());
//		context.put("flagPeringatan", rekod.getFlagPeringatan());
//		context.put("rekod", rekod);
//		
//		context.put("selectedTab", "2");
//		
//		return getPath() + "/notis/popupMaklumatNotis.vm";
//	}
//	/*------------ END FUNCTION EDIT MAKLUMAT NOTIS ------------*/
//	
//	/*------------ START FUNCTION SIMPAN MAKLUMAT NOTIS ------------*/
//	@Command("saveNotis")
//	public String saveNotis() throws ParseException {
//		String statusInfo = "";
//		String jenisNotis = "";
//		
//		UtkNotis n = db.find(UtkNotis.class, get("idNotis"));
//		
//		Boolean addMaklumatNotis = false;
//		
//		if(n == null){
//			addMaklumatNotis = true;
//			n = new UtkNotis();
//		}
//		
//		n.setNoSiri(get("noSiri"));
//		n.setKesalahan(db.find(UtkKesalahan.class,get("idKesalahan")));
//		n.setFlagJenisNotis(get("idJenisNotis"));
//		if(get("idJenisNotis").equals("1"))
//			n.setFlagPeringatan(get("idJenisPeringatan"));
//		n.setTarikhHantar(getDate("tarikhHantar"));
//		n.setCatatan(get("catatan"));
//		
//		db.begin();
//		if ( addMaklumatNotis ) db.persist(n);
//		try {
//			db.commit();
//			statusInfo = "success";
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			statusInfo = "error";
//		}
//		
//		if ( addMaklumatNotis ){
//			if("1".equals(n.getFlagJenisNotis()))
//				jenisNotis = "PENGELUARAN NOTIS PERINGATAN";
//			else if("2".equals(n.getFlagJenisNotis()))
//				jenisNotis = "PENGELUARAN NOTIS PENGOSONGAN";
//			else if("3".equals(n.getFlagJenisNotis()))
//				jenisNotis = "PENGELUARAN NOTIS BAYARAN SEWA";
//			
//			
//			if("1".equals(n.getFlagJenisNotis())){
//				if("1".equals(n.getFlagPeringatan())){
//					updateStatusKesalahan(jenisNotis + " PERTAMA");
//				}else{
//					updateStatusKesalahan(jenisNotis + " KE-" + n.getFlagPeringatan());
//				}
//			}else{
//				updateStatusKesalahan(jenisNotis);
//			}
//		}
//		
//		context.put("statusInfo", statusInfo);
//		context.put("selectedTab", "2");
//		
//		return getNotis();
//	}
//	/*------------ END FUNCTION SIMPAN MAKLUMAT NOTIS ------------*/
//	
//	/*------------ START FUNCTION REMOVE MAKLUMAT NOTIS ------------*/
//	@Command("removeNotis")
//	public String removeNotis() {
//		String statusInfo = "";
//		
//		UtkNotis maklumatNotis = db.find(UtkNotis.class, get("idNotis"));
//		String maxFlagPeringatan = (String) db.get("Select max(x.flagPeringatan) from UtkNotis x where x.kesalahan.id='" + get("idKesalahan") + "'");
//		
//		db.begin();
//		db.remove(maklumatNotis);
//		try {
//			db.commit();
//			statusInfo = "success";
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			statusInfo = "error";
//		}
//		
//		List<UtkNotis> notisList = db.list("Select x from UtkNotis x where x.kesalahan.id='" + get("idKesalahan") + "'") ;
//			
//		context.put("listNotis", notisList);
//		context.put("selectedTab", "2");
//		
//		return getPath() + "/entry_page.vm";
//	}
//	/*------------ END FUNCTION REMOVE MAKLUMAT NOTIS ------------*/
//	/************************************ END MAKLUMAT NOTIS ************************************/

	
//	/************************************ START MAKLUMAT RAYUAN ************************************/
//	/*------------ START FUNCTIN TAMBAH MAKLUMAT RAYUAN --------*/
//	@Command("addRayuan")
//	public String addRayuan() {
//		
//		context.remove("rekod");
//		context.remove("idJenisRayuan");
//		context.remove("flagBilRayuan");
//		
//		context.put("selectedTab", "3");
//		return getPath() + "/rayuan/popupMaklumatRayuan.vm";
//	}
//	/*------------ END FUNCTIN TAMBAH MAKLUMAT RAYUAN --------*/
//	
//	/*------------ START FUNCTIN EDIT MAKLUMAT RAYUAN --------*/
//	@Command("editRayuan")
//	public String editRayuan() {
//		
//		context.remove("rekod");
//		
//		UtkRayuan rekod = (UtkRayuan) db.get("Select x from UtkRayuan x where x.id='" + get("idRayuan") + "'") ;
//		
//		context.put("idJenisRayuan", rekod.getFlagJenisRayuan());
//		context.put("flagBilRayuan", rekod.getFlagRayuan());
//		context.put("rekod", rekod);
//		
//		context.put("selectedTab", "3");
//		return getPath() + "/rayuan/popupMaklumatRayuan.vm";
//	}
//	/*------------ END FUNCTIN EDIT MAKLUMAT RAYUAN --------*/
//	
//	/*------------ START FUNCTIN SIMPAN MAKLUMAT RAYUAN --------*/
//	@Command("saveRayuan")
//	public String saveRayuan() throws ParseException {
//		String statusInfo = "";
//		String keputusanSub = "";
//		
//		UtkRayuan n = db.find(UtkRayuan.class, get("idRayuan"));
//		
//		Boolean addMaklumatRayuan = false;
//		Boolean addMaklumatKelulusan = false;
//		
//		if(n == null){
//			addMaklumatRayuan = true;
//			n = new UtkRayuan();
//			if(!"".equals(get("idKelulusan"))){
//				addMaklumatKelulusan = true;
//			}
//		}
//		else{
//			if((n.getFlagKelulusanSub() == null || "".equals(n.getFlagKelulusanSub()))){
//				if(!"".equals(get("idKelulusan")))
//					addMaklumatKelulusan = true;
//			}
//		}
//		
//		n.setNoRayuan(get("noRayuan"));
//		n.setKesalahan(db.find(UtkKesalahan.class,get("idKesalahan")));
//		n.setFlagJenisRayuan(get("idJenisRayuan"));
//		n.setFlagRayuan(get("idBilanganRayuan"));
//		n.setTarikhRayuan(getDate("tarikhRayuan"));
//		n.setCatatanRayuan(get("catatanRayuan"));
//		n.setTarikhKelulusan(getDate("tarikhKelulusan"));
//		n.setFlagKelulusanSub(get("idKelulusan"));
//		n.setCatatanKelulusan(get("catatanKelulusan"));
//		
//		db.begin();
//		if ( addMaklumatRayuan ) db.persist(n);
//		try {
//			db.commit();
//			statusInfo = "success";
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			statusInfo = "error";
//		}
//		System.out.println("cekkkkkkkkkkkk =============" + addMaklumatKelulusan);
//		if ( addMaklumatRayuan ){	
//			if("1".equals(n.getFlagRayuan())){
//				updateStatusKesalahan("RAYUAN PERTAMA DALAM PROSES");
//			}else{
//				updateStatusKesalahan("RAYUAN KE-" + n.getFlagRayuan() + " DALAM PROSES");
//			}
//		}
//		
//		if( addMaklumatKelulusan){
//			
//			if("L".equals(n.getFlagKelulusanSub()))
//				keputusanSub = "LULUS";
//			else if("T".equals(n.getFlagKelulusanSub()))
//				keputusanSub = "TOLAK";
//			
//			if("1".equals(n.getFlagRayuan())){
//				updateStatusKesalahan("RAYUAN PERTAMA " + keputusanSub);
//			}else{
//				updateStatusKesalahan("RAYUAN KE-" + n.getFlagRayuan() + " " + keputusanSub);
//			}
//			
//		}
//			
//		context.put("statusInfo", statusInfo);
//		context.put("selectedTab", "3");
//		
//		return getRayuan();
//	}
//	/*------------ END FUNCTIN SIMPAN MAKLUMAT RAYUAN --------*/
//	
//	/*------------ START FUNCTIN REMOVE MAKLUMAT RAYUAN --------*/
//	@Command("removeRayuan")
//	public String removeRayuan() {
//		String statusInfo = "";
//		
//		UtkRayuan maklumatRayuan = db.find(UtkRayuan.class, get("idRayuan"));
//		
//		db.begin();
//		db.remove(maklumatRayuan);
//		try {
//			db.commit();
//			statusInfo = "success";
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			statusInfo = "error";
//		}
//		
//		List<UtkRayuan> rayuanList = db.list("Select x from UtkRayuan x where x.kesalahan.id='" + get("idKesalahan") + "'") ;
//			
//		context.put("listRayuan", rayuanList);
//		context.put("selectedTab", "3");
//		
//		return getPath() + "/entry_page.vm";
//	}
//	/*------------ END FUNCTIN REMOVE MAKLUMAT RAYUAN --------*/
//	/************************************ END MAKLUMAT RAYUAN ************************************/
	
	
	/************************************ START DROP DOWN ************************************/
	@Command("findBandar")
	public String findBandar() throws Exception {
		String idNegeri = "0";
		if (get("findNegeri").trim().length() > 0)
			idNegeri = get("findNegeri");
		
		List<Bandar> list = dataUtil.getListBandar(idNegeri);
		context.put("selectBandar", list);

		return getPath() + "/findBandar.vm";
	}
	
	@Command("selectKes")
	public String selectKes() throws Exception {
		String idJenisPelanggaranSyarat = "";
//		System.out.println("ceking kes = " + get("idKes"));
//		System.out.println("ceking idoperasi = " + getParam("idJenisOperasi"));
		if (get("idJenisPelanggaran").trim().length() > 0)
			idJenisPelanggaranSyarat = get("idJenisPelanggaran");
		
		context.put("flagKes",get("idKes"));
		
		List<JenisPelanggaranSyaratUtk> jenisPelanggaranSyaratUtkList = dataUtil.getListJenisPelanggaranSyaratUtk(get("idKes"));
		context.put("selectJenisPelanggaran", jenisPelanggaranSyaratUtkList);

		context.put("jenisPelanggaranSyarat",get("idJenisPelanggaran"));
		context.put("showOperasi", getParam("idJenisOperasi"));
		context.put("showPelanggaran", idJenisPelanggaranSyarat);
		context.put("selectedTab", "2");
		return getPath() + "/pelanggaranSyarat/divKes.vm";
	}

	@Command("selectPelanggaranOperasi")
	public String selectPelanggaranOperasi() throws Exception {
		
		context.remove("jenisPelanggaranSyarat");
		String idKes = "0";
		if (get("idKes").trim().length() > 0)
			idKes = get("idKes");

		List<JenisPelanggaranSyaratUtk> jenisPelanggaranSyaratUtkList = dataUtil.getListJenisPelanggaranSyaratUtk(idKes);
		context.put("selectJenisPelanggaran", jenisPelanggaranSyaratUtkList);

		return getPath() + "/pelanggaranSyarat/selectJenisPelanggaranSyarat.vm";
	}
	
	@Command("selectFlagPeringatan")
	public String selectFlagPeringatan() throws Exception {
		String idJenisNotis = "0";
		int flagPeringatan = 1;
		
		if (get("idJenisNotis").trim().length() > 0)
			idJenisNotis = get("idJenisNotis");

		String maxFlagPeringatan = (String) db.get("Select max(x.flagPeringatan) from UtkNotis x where x.kesalahan.id='" + get("idKesalahan") + "'");

		if(maxFlagPeringatan != null){
			if(idJenisNotis.equals("1")){
				flagPeringatan = Integer.parseInt(maxFlagPeringatan);
				if(flagPeringatan >= 1){
					flagPeringatan = flagPeringatan + 1;
				}
			}
		}
			
		context.put("idJenisNotis", idJenisNotis);
		context.put("flagPeringatan", flagPeringatan);
		return getPath() + "/notis/selectPeringatan.vm";
	}
	
	@Command("selectFlagRayuan")
	public String selectFlagRayuan() throws Exception {
		String idJenisRayuan = "0";
		int flagRayuan = 0;
		
		if (get("idJenisRayuan").trim().length() > 0)
			idJenisRayuan = get("idJenisRayuan");

		String maxFlagRayuan = (String) db.get("Select max(x.flagRayuan) from UtkRayuan x where x.kesalahan.id='" + get("idKesalahan") + "' and x.flagJenisRayuan='"+ get("idJenisRayuan") +"'");

			if(maxFlagRayuan != null){
				System.out.println("dalam mex");
				flagRayuan = Integer.parseInt(maxFlagRayuan);
				if(flagRayuan >= 1){
					flagRayuan = flagRayuan + 1;
				}
			}else{
				System.out.println("luarr");
				flagRayuan = 1;
			}
			
		context.put("idJenisRayuan", idJenisRayuan);
		context.put("flagBilRayuan", flagRayuan);
		return getPath() + "/rayuan/selectBilanganRayuan.vm";
	}
	
	/************************************ END DROP DOWN ************************************/
	
	/************************************ START UPDATE STATUS ************************************/
	public void updateStatusKesalahan(String status){
		String statusInfo = "";
		UtkKesalahan k = db.find(UtkKesalahan.class, get("idPenghuni"));
		
		k.setStatus(status);
		
		db.begin();
		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			statusInfo = "error";
		}
	}
	/************************************ END UPDATE STATUS ************************************/
	
}

