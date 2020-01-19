package bph.modules.rpp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import lebah.db.Db;
import lebah.db.DbException;
import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorDateBetween;
import lebah.template.OperatorEqualTo;
import lebah.template.UID;
import portal.module.entity.Users;
import bph.entities.kod.Bank;
import bph.entities.kod.JenisUnitRPP;
import bph.entities.kod.KodJuruwang;
import bph.entities.kod.Status;
import bph.entities.rpp.RppAkaun;
import bph.entities.rpp.RppJadualTempahan;
import bph.entities.rpp.RppPenyeliaPeranginan;
import bph.entities.rpp.RppPeranginan;
import bph.entities.rpp.RppPermohonan;
import bph.entities.rpp.RppTetapanCajTambahan;
import bph.entities.rpp.RppUnit;
import bph.utils.DataUtil;
import bph.utils.Util;
import bph.utils.UtilRpp;
import db.persistence.MyPersistence;

public class TempahanWalkIn extends LebahRecordTemplateModule<RppPermohonan> {

	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;
	private MyPersistence mp;
	
	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {return String.class;}

	@Override
	public void beforeSave() {}

	@Override
	public String getPath() {return "bph/modules/rpp/tempahanWalkIn";}

	@Override
	public Class<RppPermohonan> getPersistenceClass() {return RppPermohonan.class;}
	
	@Override
	public boolean delete(RppPermohonan r) throws Exception { return false; }
	
	@Override
	public void save(RppPermohonan r) throws Exception { }
	
	@Override
	public void afterSave(RppPermohonan r) { }
	
	public void redirectSkrinTempahan(){ }
	
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
		//TEMPORARY - REQUEST BY EN WAN NAK CEK PERMOHONAN BY NO_TEMPAHAN
		map.put("noTempahan", getParam("findNoTempahan"));
		return map;
	}
	
	@Override
	public void begin() {
		
		dataUtil = DataUtil.getInstance(db);
		
		String loginId = (String) request.getSession().getAttribute("_portal_login");
		String userRole = (String) request.getSession().getAttribute("_portal_role");
		
		clearContext();
		try {
			mp = new MyPersistence();
			
			checkingIfPenyelia(mp,loginId,userRole);
			defaultButtonOption();
			filtering(mp,loginId,userRole);
			tambahanDewasa(mp);
			tambahanKanakKanak(mp);
			
		} catch (Exception e) {
			System.out.println("Error begin : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		
		context.put("path", getPath());
		context.put("util", new Util());
		context.put("userRole",userRole);
		context.put("command", command);
		context.put("uploadDir", ResourceBundle.getBundle("dbconnection").getString("folder"));
		
		redirectSkrinTempahan();
		
	}
	
	private void clearContext() { 
		context.remove("txtSearchGuest");
		context.remove("guest");
		context.remove("isPenyelia");
		context.remove("blockBooking");
		context.remove("blacklisted");
		context.put("accountInfoExist",true);
	}
	
	private void defaultButtonOption() {
		this.setDisableDefaultButton(true);
		this.setDisableBackButton(true);
		this.setDisableKosongkanUpperButton(true);
		this.setDisableSaveAddNewButton(true);
		this.setDisabledInfoNextTab(true);
		this.setDisableUpperBackButton(true);
		this.setHideDeleteButton(true);
	}
	
	@SuppressWarnings("unchecked")
	private void checkingIfPenyelia(MyPersistence mp,String loginId,String userRole) {
		List<RppPenyeliaPeranginan> pp = mp.list("select x from RppPenyeliaPeranginan x where x.penyelia.id = '"+loginId+"' and x.statusPerkhidmatan = 'Y'");
		boolean value = false;
		if(pp.size() > 0){
			value = true;
		}
		context.put("isPenyelia",value);
	}
	
	@SuppressWarnings("unchecked")
	private void filtering(MyPersistence mp,String loginId,String userRole) {
		this.addFilter("status.id in ('1425259713412','1425259713415')"); 
		this.addFilter("jenisPemohon = 'INDIVIDU' ");
		this.addFilter("jenisPermohonan = 'WALKIN' ");
		
		otherFiltering();
		
		if(userRole.equalsIgnoreCase("(RPP) Penyelia")){
			String listPeranginan = "";
			List<RppPenyeliaPeranginan> listPeranginanSeliaan = mp.list("select x from RppPenyeliaPeranginan x where x.penyelia.id = '" + loginId + "'");
			for (int i = 0; i< listPeranginanSeliaan.size(); i++) {
				if (listPeranginan.length() == 0) {
					listPeranginan = "'" + listPeranginanSeliaan.get(i).getPeranginan().getId() + "'";
				} else {
					listPeranginan = listPeranginan + "," + "'" + listPeranginanSeliaan.get(i).getPeranginan().getId() + "'";
				}
			}
			if (listPeranginan.length() == 0) {
				this.addFilter("rppPeranginan.id = ''");
			} else {
				this.addFilter("rppPeranginan.id in (" + listPeranginan + ")");
			}			
		}
	}
	
	public void otherFiltering(){
		this.addFilter("flagDaftarOffline = 'T' ");
	}
	
	@SuppressWarnings("unchecked")
	public String listRppByPenyelia(MyPersistence mp,String loginId,String userRole) { 
		String listPeranginan = "";
		if(userRole.equalsIgnoreCase("(RPP) Penyelia")){
			List<RppPenyeliaPeranginan> listPeranginanSeliaan = mp.list("select x from RppPenyeliaPeranginan x where x.penyelia.id = '" + loginId + "'");
			for (int i = 0; i< listPeranginanSeliaan.size(); i++) {
				if (listPeranginan.length() == 0) {
					listPeranginan = "'" + listPeranginanSeliaan.get(i).getPeranginan().getId() + "'";
				} else {
					listPeranginan = listPeranginan + "," + "'" + listPeranginanSeliaan.get(i).getPeranginan().getId() + "'";
				}
			}
			if (listPeranginan.length() == 0) {
				listPeranginan = "";
			} else {
				listPeranginan = "(" + listPeranginan + ")";
			}	
		}
		return listPeranginan;
	}

	public void tambahanDewasa(MyPersistence mp){
		RppTetapanCajTambahan caj = (RppTetapanCajTambahan) mp.find(RppTetapanCajTambahan.class, "1432867359415");
		Double tmbh = (caj!=null?caj.getCajBayaran():0d);
		context.put("extbedcharge",Util.formatDecimal(tmbh)); //tambahan dewasa
	}
	
	public void tambahanKanakKanak(MyPersistence mp){
		RppTetapanCajTambahan caj = (RppTetapanCajTambahan) mp.find(RppTetapanCajTambahan.class, "1436755298337");
		Double tmbh = (caj!=null?caj.getCajBayaran():0d);
		context.put("extChargeKid",Util.formatDecimal(tmbh)); //tambahan kanak kanak
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void getRelatedData(RppPermohonan r) { 
		try{
			mp = new MyPersistence();
			RppPermohonan rr = (RppPermohonan) mp.find(RppPermohonan.class, r.getId());
			
			List<RppJadualTempahan> unitBooked = mp.list("select x from RppJadualTempahan x where x.permohonan.id = '"+r.getId()+"' ");
			context.put("unitBooked",unitBooked);
			context.put("guest",r.getPemohon());
			context.put("r", rr);
			context.put("selectedTab", "1");
		}finally{
			if (mp != null) { mp.close(); }
		}
	}
	
	
	/**
	 * 
	 * OPEN POPUP SEARCH GUEST
	 * 
	 * */
	@Command("callPopupSearchGuest")
	public String callPopupSearchGuest(){
		try {
			mp = new MyPersistence();
			
			String txtSearchGuest = getParam("txtSearchGuest");
			List<Users> userList = searchUsers(mp,txtSearchGuest);
			context.put("userList", userList);
			
		} catch (Exception e) {
			System.out.println("Error callPopupSearchGuest : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/popup/guestList.vm";
	}
	
	private List<Users> searchUsers(MyPersistence mp,String param) {
		ArrayList<Users> list = new ArrayList<Users>();
		Db db1 = null;
		try {
			db1 = new Db();
			String sql = "SELECT DISTINCT a.user_login FROM users a "+
						 " WHERE (upper(user_name) LIKE upper('%"+param+"%') OR upper(no_kp) LIKE upper('%"+param+"%')) ";
			
			ResultSet rs = db1.getStatement().executeQuery(sql);			
			while (rs.next()) {
				Users us = (Users) mp.find(Users.class, rs.getString("user_login"));
				list.add(us);
			}	
			
		}catch(Exception e){
			System.out.println("Error searchUsers : "+e.getMessage());
			e.printStackTrace();
		}finally { 
			if ( db1 != null ) db1.close();
		}
		return list;
	}
	
	
	/**
	 * 
	 * SAVE PILIHAN GUEST
	 * 
	 * */
	@Command("savePilihanGuest")
	public String savePilihanGuest() {
		try {
			mp = new MyPersistence();
			
			String guestId = getParam("radTetamu");
			Users guest = (Users) mp.find(Users.class, guestId);
			String txtSearchGuest = guest.getUserName().toUpperCase();
			
			checkingHadPermohonan(mp,guestId);
			checkingBlacklist(mp,guestId);
			checkingMaklumatBank(mp,guest);
			
			context.put("listBank", dataUtil.getListBank());
			context.put("txtSearchGuest", txtSearchGuest);
			context.put("guest", guest);
			
			/**SHOW/HIDE KALAU WALKIN / OFFLINE*/
			enabledEditDate();
			
		} catch (Exception e) {
			System.out.println("Error savePilihanGuest : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/form/maklumatTetamu.vm";
	}
	
	public void enabledEditDate(){
		context.put("enabledEditDate", false);
	}
	
	public void checkingHadPermohonan(MyPersistence mp,String guestId){
		context.put("blockBooking",UtilRpp.getTotalBilPrmhnByYear(mp,guestId,new Date()));
	}
	
	public void checkingBlacklist(MyPersistence mp,String guestId){
		context.put("blacklisted",UtilRpp.checkingBlacklist(mp,guestId));
	}
	
	public void checkingMaklumatBank(MyPersistence mp,Users objUser){
		context.put("accountInfoExist", UtilRpp.checkingAccountInfoExist(objUser));
	}
	
	
	/**
	 * 
	 * KEMASKINI MAKLUMAT BANK
	 * 
	 * */
	@Command("kemaskiniBank")
	public String kemaskiniBank(){
		
		try {
			mp = new MyPersistence();
			
			String guestId = getParam("guestId");
			
			Users guest = (Users) mp.find(Users.class,guestId);
			String txtSearchGuest = guest.getUserName().toUpperCase();
			
			mp.begin();
			
			guest.setNoAkaunBank(getParam("noAkaunBank"));
			guest.setBank((Bank) mp.find(Bank.class, getParam("bank")));
			guest.setFlagSahMaklumatBank(getParam("cbSyarat"));
			
			mp.commit();
			
			checkingHadPermohonan(mp,guestId);
			checkingBlacklist(mp,guestId);
			checkingMaklumatBank(mp,guest);
			
			context.put("txtSearchGuest", txtSearchGuest);
			context.put("guest", guest);
			
		} catch (Exception e) {
			System.out.println("Error kemaskiniBank : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/form/maklumatTetamu.vm";
	}
	
	
	/**
	 * 
	 * PAPAR MAKLUMAT RPP
	 * 
	 * */
	@SuppressWarnings("unchecked")
	@Command("paparMaklumatRpp")
	public String paparMaklumatRpp(){
		
		String loginId = (String) request.getSession().getAttribute("_portal_login");
		String userRole = (String) request.getSession().getAttribute("_portal_role");
		
		try {
			mp = new MyPersistence();
			
			//List<RppPeranginan> listRp = mp.list("select x from RppPeranginan x where x.id in "+listRppByPenyelia(mp,loginId,userRole)+" and x.id not in ('3','14','11','12','13') order by x.namaPeranginan asc "); 
			List<RppPeranginan> listRp = mp.list("select x from RppPeranginan x where x.id in "+listRppByPenyelia(mp,loginId,userRole)+" and x.id not in ('11','12','13') order by x.namaPeranginan asc "); 
			context.put("listRp",listRp);
			
			/**SHOW/HIDE KALAU WALKIN / OFFLINE*/
			enabledEditDate();
			
		} catch (Exception e) {
			System.out.println("Error paparMaklumatRpp : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/form/maklumatRpp.vm";
	}
	
	/**
	 * 
	 * FILTER JENIS UNIT BY RPP
	 * 
	 * */
	@SuppressWarnings("unchecked")
	@Command("filterJenisUnitByRpp")
	public String filterJenisUnitByRpp(){
		try {
			mp = new MyPersistence();
			
			String idRpp = getParam("idRpp");
			RppPeranginan rp = (RppPeranginan) mp.find(RppPeranginan.class, idRpp);
			List<JenisUnitRPP> listJenisUnitRpp = mp.list("select x from JenisUnitRPP x where x.peranginan.id = '"+idRpp+"' order by x.keterangan asc ");
			context.put("listJenisUnitRpp",listJenisUnitRpp);
			context.put("rp",rp);
			
		} catch (Exception e) {
			System.out.println("Error filterJenisUnitByRpp : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/form/maklumatJenisUnit.vm";
	}
	
	/**
	 * 
	 * FILTER UNIT RPP BY JENIS UNIT
	 * 
	 * */
	@Command("filterUnitByJenis")
	public String filterUnitByJenis(){
		try {
			mp = new MyPersistence();
			
			String idJenisUnit = getParam("idJenisUnit");
			JenisUnitRPP jenisUnit = (JenisUnitRPP) mp.find(JenisUnitRPP.class, idJenisUnit);
			
			Date tarikhMasuk = getDate("tarikhMasuk");
			Date tarikhKeluar = getDate("tarikhKeluar");
			
			context.put("listUnit", getListUnit(mp,tarikhMasuk,tarikhKeluar,idJenisUnit));
			context.put("jenisUnit",jenisUnit);
			
			String strTarikhMasuk = new SimpleDateFormat("yyyy-MM-dd").format(tarikhMasuk);
			String strTarikhKeluar = new SimpleDateFormat("yyyy-MM-dd").format(tarikhKeluar);
			context.put("strTarikhMasuk", strTarikhMasuk);
			context.put("strTarikhKeluar", strTarikhKeluar);
			
		} catch (Exception e) {
			System.out.println("Error filterUnitByJenis : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/form/senaraiUnit.vm";
	}
	
	public List<RppUnit> getListUnit(MyPersistence mp,Date tarikhMasuk,Date tarikhKeluar,String idJenisUnit){
		return UtilRpp.walkInListUnitAvailable(mp,tarikhMasuk,tarikhKeluar,idJenisUnit);
	}
	
	
	/**
	 * 
	 * SIMPAN REKOD WALKIN
	 * 
	 * */
	@SuppressWarnings("unchecked")
	@Command("simpanRekod")
	public String simpanRekod(){
		System.out.println("simpanRekod (Walkin/offline)...");
		String vm = "";
		boolean saveFailed = false;
		String loginId = (String) request.getSession().getAttribute("_portal_login");
		
		try {
			mp = new MyPersistence();
			
			String guestId = getParam("guestId");
			
			String permohonanId = UID.getUID();
			boolean success = saveMainRecordInSql(mp,permohonanId,guestId);
			RppPermohonan r = null;
			
			if(success==true){
				
				saveFailed = false;
				
				r = (RppPermohonan) mp.find(RppPermohonan.class, permohonanId);
				
				UtilRpp.saveTarikhAkhirBayaranInSql(r); /**UPDATE TARIKH AKHIR BAYARAN*/
				createRecordJadualTempahan(r); /**CREATE TABLE JADUAL TEMPAHAN DAN TABLE TEMPAHAN CHALET*/
				UtilRpp.createRecordBayaran(mp,guestId,r); /**CREATE RPPAKAUN & INVOIS & DEPOSIT*/
				
				List<RppAkaun> listak = UtilRpp.getListTempahanDanBayaran(mp,r);
				
				/** CHECKING KALAU DATA TEMPAHAN TIDAK LENGKAP. (TIADA RPP AKAUN) KECUALI PREMIER * */
				List<RppJadualTempahan> listJadual = mp.list("select x from RppJadualTempahan x where x.permohonan.id = '"+r.getId()+"' ");
				if(listak.size() == 0 || listJadual.size() == 0){
					System.out.println(":: BOOKING FAILED (Walkin/offline) : 1."+listak.size()+" 2."+listJadual.size());
					saveFailed = true;
					UtilRpp.deleteAndUpdateStatusGagal(mp,r,listak); // DB.BEGIN, DB.REMOVE, DB.COMMIT 
				}
	
				r = (RppPermohonan) mp.find(RppPermohonan.class, permohonanId);
				
				context.put("r",r);
				context.put("selectedTab", "2");
				context.put("listTempahanDanBayaran", UtilRpp.getListTempahanDanBayaran(mp,r));
				
				//get maklumat juruwang
				contextPutMaklumatJuruwang(mp,loginId);
				
				vm = getPath() + "/form/maklumatBayaran.vm";
				
			}else{
				saveFailed = true;
				deleteRecordInSql(permohonanId,guestId);
				vm = getPath() + "/form/notis.vm";
			}
			
		} catch (Exception e) {
			System.out.println("Error simpanRekod : "+e.getMessage());
			saveFailed = true;
		}finally{
			if (mp != null) { mp.close(); }
		}
		
		context.put("saveFailed",saveFailed);
		return vm;
	}
	
	public String getFlagDaftarOffline(){
		return "T";
	}
	
	@SuppressWarnings("rawtypes")
	public boolean saveMainRecordInSql(MyPersistence mp,String permohonanId,String pemohonId) throws Exception {
		
		String penyeliaId = (String) request.getSession().getAttribute("_portal_login");
		
		/**VALUE KALAU WALKIN / OFFLINE*/
		String flagDaftarOffline = getFlagDaftarOffline();
		
		String jenisPemohon = "INDIVIDU";
		String statusBayaran = "T"; 
		String jenisPermohonan = "WALKIN";
		String idJenisUnit = getParam("hdjenisUnitId");

		int bilDewasa=0;
		int bilKanakKanak=0;
		
		try{
			bilDewasa = getParamAsInteger("bilDewasa"); 
		}catch(Exception ex){
			bilDewasa = 0; 
	    }
		
		try{
			bilKanakKanak = getParamAsInteger("bilKanakKanak");
		}catch(Exception ex){
			bilKanakKanak = 0; 
	    }
		
		//int bilDewasa = getParamAsInteger("bilDewasa");
		//int bilUnit = getParamAsInteger("bilUnit");
		//int bilKanakKanak = getParamAsInteger("bilKanakKanak");
		Date tarikhMasukRpp = getDate("tarikhMasuk");
		Date tarikhKeluarRpp = getDate("tarikhKeluar");
		
		String[] cbUnit = request.getParameterValues("cbUnit");
		int bilUnit = 0;
		for(int i = 0; i < cbUnit.length; i++){
			bilUnit++;
		}
		
		String idrp = "";
		JenisUnitRPP jenisUnit = (JenisUnitRPP) mp.find(JenisUnitRPP.class, idJenisUnit);
		if(jenisUnit != null){
			idrp = jenisUnit.getPeranginan()!=null?jenisUnit.getPeranginan().getId():null;
		}
		int tambahanDewasa=0;
		int tambahanKanakKanak=0;
		
		try{
			tambahanDewasa = getParamAsInteger("bilTambahanDewasa");  
		}catch(Exception ex){
			tambahanDewasa = 0; 
	    }
		
		try{
			tambahanKanakKanak = getParamAsInteger("bilTambahanKanakKanak");
		}catch(Exception ex){
			tambahanKanakKanak = 0; 
	    }
		
		String status = "1425259713412"; //Permohonan Baru

		String dtIn = new SimpleDateFormat("yyyy-MM-dd").format(tarikhMasukRpp);
		String dtOut = new SimpleDateFormat("yyyy-MM-dd").format(tarikhKeluarRpp);
		
		List listrp = UtilRpp.hashtableDetailRumahPeranginan(idrp);
		String kodLokasi = "";
		if(listrp.size() > 0){
			Hashtable dh = (Hashtable) listrp.get(0);
			kodLokasi = (String)dh.get("kodLokasi");
		}
		
		String waktupuncak = "T";
		if(UtilRpp.checkWaktuPuncak(dtIn)){
			waktupuncak = "Y";
		}
		
		String generatedNoTempahan = UtilRpp.generateNoTempahanIndividuSQL(kodLokasi, tarikhMasukRpp);
		
		String sql = " INSERT INTO `rpp_permohonan` (`id`, `id_peranginan`, `jenis_pemohon`, `status_bayaran`, `jenis_permohonan`, `id_pemohon`, `no_tempahan`, " +
					 " `id_status`, `id_masuk`, `tarikh_masuk`, `tarikh_permohonan`, `id_jenis_unit_rpp`, `bil_dewasa`, `bil_tambahan_dewasa`, " +
					 " `bil_kanak_kanak`, `bil_unit`, `tarikh_masuk_rpp`, `tarikh_keluar_rpp`, `flag_waktu_puncak`, "+
					 " `bil_tambahan_kanak_kanak`, `flag_daftar_offline` ) "+
					 " VALUES "+
					 " ('"+permohonanId+"', '"+idrp+"', '"+jenisPemohon+"', '"+statusBayaran+"', '"+jenisPermohonan+"', '"+pemohonId+"', '"+generatedNoTempahan+"', "+
					 " '"+status+"', '"+penyeliaId+"', now() , now(), '"+idJenisUnit+"', "+bilDewasa+", "+tambahanDewasa+", "+ 
					 " "+bilKanakKanak+", "+bilUnit+", '"+dtIn+"', '"+dtOut+"', '"+waktupuncak+"', "+
					 " '"+tambahanKanakKanak+"', '"+flagDaftarOffline+"' ) ";
		
		boolean success = true;
		
		Db database = new Db();
        try{
            Statement stmt = database.getStatement();
            stmt.executeUpdate(sql);
        }catch(SQLException ex){
        	success = false;
        	System.out.println(":: ERROR saveMainRecordInSql (Walkin/offline) : "+ex.getMessage());
        }finally { if ( database != null ) database.close(); }
        
        if(idrp == null){
        	success = false;
        }
        
        return success;
	}
	
	public void createRecordJadualTempahan(RppPermohonan r) throws DbException{
		
		String dtIn = new SimpleDateFormat("yyyy-MM-dd").format(r.getTarikhMasukRpp());
		String dtOut = new SimpleDateFormat("yyyy-MM-dd").format(r.getTarikhKeluarRpp());
		
		String[] cbUnit = request.getParameterValues("cbUnit");
		for(int i = 0; i < cbUnit.length; i++){
			String idUnit = cbUnit[i];
			
			String idjadual = UID.getUID();
			
			String sqlJadual = "INSERT INTO `rpp_jadual_tempahan` (`id`, `id_unit`, `tarikh_mula`, `tarikh_tamat`, `status`, "
							+ " `id_permohonan`, `flag_status_tempahan`, `id_unit_confirm` ) "
							+ " VALUES "
							+ " ('"+idjadual+"', '"+idUnit+"', '"+dtIn+"', '"+dtOut+"', 'B', "
							+ " '"+r.getId()+"', 'CONFIRM', '"+idUnit+"' ) "; 
		
			Db database = new Db();
			
	        try{
	            Statement stmt = database.getStatement();
	            stmt.executeUpdate(sqlJadual);
	        }catch(SQLException ex){
	        	System.out.println(":: ERROR daftarChalet (Walkin/offline) : "+ex.getMessage());
	        }finally { if ( database != null ) database.close(); }
	        
		}
		
	}
	
	public void deleteRecordInSql(String permohonanId,String guestId) throws DbException{
		String sqlJadual = "DELETE FROM rpp_permohonan where id = '"+permohonanId+"' and id_pemohon = '"+guestId+"' ";
		Db database = new Db();
        try{
            Statement stmt = database.getStatement();
            stmt.executeUpdate(sqlJadual);
        }catch(SQLException ex){
        	System.out.println(":: ERROR deleteRecordInSql (Walkin/offline) : "+ex.getMessage());
        }finally { if ( database != null ) database.close(); }
        
	}
	
	/**
	 * 
	 * MAKLUMAT BAYARAN
	 * 
	 * */
	@Command("maklumatBayaran")
	public String maklumatBayaran(){
		
		String loginId = (String) request.getSession().getAttribute("_portal_login");
		
		try {
			mp = new MyPersistence();
			
			String idpermohonan = getParam("idpermohonan");
			RppPermohonan r = (RppPermohonan) mp.find(RppPermohonan.class, idpermohonan);
			context.put("r", r);
			context.put("selectedTab", "2");
			context.put("listTempahanDanBayaran", UtilRpp.getListTempahanDanBayaran(mp,r));
			
			/**SHOW/HIDE KALAU WALKIN / OFFLINE*/
			enabledEditDate();
			
			//get maklumat juruwang
			contextPutMaklumatJuruwang(mp,loginId);
			
		} catch (Exception e) {
			System.out.println("Error maklumatBayaran : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/form/maklumatBayaran.vm";
	}
	
	public void contextPutMaklumatJuruwang(MyPersistence mp,String loginId){
		KodJuruwang kodj = (KodJuruwang)mp.get("select x from KodJuruwang x where x.juruwang.id = '"+loginId+"' and x.flagJuruwang = 'IR' and x.flagAktif = 'Y'");
		String noPusatTerima = kodj!=null?kodj.getKodPusatTerima():"";
		String noKodJuruwang = kodj!=null?kodj.getKodJuruwang():"";
		context.put("noPusatTerima", noPusatTerima);
		context.put("noKodJuruwang", noKodJuruwang);
	}
	
	@Command("pilihCaraBayaran")
	public String pilihCaraBayaran(){
		try {
			mp = new MyPersistence();
			
			String flagJenisBayaran = getParam("flagJenisBayaran");
			context.put("flagJenisBayaran", flagJenisBayaran);
			
		} catch (Exception e) {
			System.out.println("Error pilihCaraBayaran : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/form/noResit.vm";
	}
	
	@Command("simpanBayaran")
	public String simpanBayaran(){
		String loginId = (String) request.getSession().getAttribute("_portal_login");
		try {
			mp = new MyPersistence();
			
			String idpermohonan = getParam("idpermohonan");
			RppPermohonan r = (RppPermohonan) mp.find(RppPermohonan.class, idpermohonan);
			
			String flagJenisBayaran = getParam("flagJenisBayaran");
			String noResitSewaDeposit = getParam("noResitSewaDeposit");
			Date tarikhBayaran = getDate("tarikhBayaran");
			
			mp.begin();
			
			UtilRpp.createPengurusanBilik(r);
			UtilRpp.createWalkinResitSenaraiInvoisAndUpdateLejar(mp,r,loginId,flagJenisBayaran,noResitSewaDeposit,tarikhBayaran);
			
			r.setStatus((Status) mp.find(Status.class, "1425259713421")); //daftar masuk
			
			mp.commit();
			
			context.put("r", r);
			context.put("flagJenisBayaran", flagJenisBayaran);
			
		} catch (Exception e) {
			System.out.println("Error simpanBayaran : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		return maklumatBayaran();
	}
	
	/**
	 * DELETE REKOD TEMPAHAN
	 * Boleh delete jika belum bayar
	 * */
	@Command("deletePermohonan")
	public String deletePermohonan(){
		try {
			mp = new MyPersistence();
			
			String idpermohonan = getParam("id");
			RppPermohonan r = (RppPermohonan) mp.find(RppPermohonan.class, idpermohonan);
			
			if( r!=null ){
				mp.begin();
				UtilRpp.deletePermohonan(mp,r);
				mp.commit();
			}
			
		} catch (Exception e) {
			System.out.println("Error deletePermohonan : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/refresh.vm";
	}
	
}

