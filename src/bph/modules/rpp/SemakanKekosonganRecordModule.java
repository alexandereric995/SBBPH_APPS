package bph.modules.rpp;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import lebah.db.Db;
import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import bph.entities.kod.JenisUnitRPP;
import bph.entities.rpp.RppPenyeliaPeranginan;
import bph.entities.rpp.RppPeranginan;
import bph.entities.rpp.RppPermohonan;
import bph.entities.rpp.RppTetapanBukaTempahan;
import bph.entities.rpp.RppUnit;
import bph.utils.DataUtil;
import bph.utils.Util;
import bph.utils.UtilRpp;
import db.persistence.MyPersistence;

public class SemakanKekosonganRecordModule extends LebahRecordTemplateModule<RppPermohonan> {

	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;
	private MyPersistence mp;
	
	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() { return String.class; }

	@Override
	public void afterSave(RppPermohonan r) {}

	@Override
	public void beforeSave() {}
	
	@Override
	public Class<RppPermohonan> getPersistenceClass() {return RppPermohonan.class;}
	
	@Override
	public String getPath() { return "bph/modules/rpp/semakanKekosongan"; }

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("barangDeposit.id", getParam(""));	
		return map;
	}
	
	@Override
	public boolean delete(RppPermohonan r) throws Exception {return false;}
	
	@Override
	public void begin() {

		dataUtil = DataUtil.getInstance(db);
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		userName = (String) request.getSession().getAttribute("_portal_username");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		
		defaultButtonOption();
		addfilter(userId,userRole);
		
		try{
			mp = new MyPersistence();
			
			RppTetapanBukaTempahan tetapanBookingDate = (RppTetapanBukaTempahan) mp.get("select x from RppTetapanBukaTempahan x where x.flagAktif = 'Y' ");
			if(tetapanBookingDate != null){
				
				/** COMMENT BY PEJE - RANGE DATE TO FAR
				String dtfrom = Util.getDateTime(tetapanBookingDate.getTarikhBukaTempahan(), "dd-MM-yyyy");
				String today = Util.getDateTime(new Date(), "dd-MM-yyyy");

				if(tetapanBookingDate.getTarikhBukaTempahan().before(new Date())){
					dtfrom = today;
				}
				
				String dtto = Util.getDateTime(tetapanBookingDate.getTarikhMenginapHingga(), "dd-MM-yyyy");
				*/		
				
				
				String dtfrom = Util.getDateTime(new Date(), "dd-MM-yyyy");
				Calendar calTo = new GregorianCalendar();
				calTo.setTime(new Date());
				calTo.add(Calendar.WEEK_OF_MONTH, 1);
				String dtto = Util.getDateTime(calTo.getTime(), "dd-MM-yyyy");
				
				context.put("dtfrom", dtfrom);
				context.put("dtto", dtto);
			}else{
				context.remove("dtfrom");
				context.remove("dtto");
			}
			
			/**NAK FILTER LIST RPP UNTUK PENYELIA*/
			String listPeranginan = listRppByPenyelia(mp,userId,userRole);
			boolean checkingPenyelia = true;
			if(userRole.equalsIgnoreCase("(RPP) Penyelia")){
				if(listPeranginan==null || listPeranginan.equalsIgnoreCase("")){
					checkingPenyelia = false;
				}
			}
			context.put("checkingPenyelia", checkingPenyelia);
			
			context.remove("gredId");
			context.put("senaraiPeranginan", senaraiRPbyGred(mp,null,listPeranginan));
			context.put("senaraiGred", dataUtil.getListGredPerkhidmatan());
			
			context.put("userRole", userRole);
			context.put("command", command);
			context.put("path", getPath());
			context.put("util", new Util());
			
		}catch (Exception e) {
			System.out.println("Error begin : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		
	}

	@SuppressWarnings("unchecked")
	public String listRppByPenyelia(MyPersistence mp,String userId,String userRole) { 
		String listPeranginan = "";
		if(userRole.equalsIgnoreCase("(RPP) Penyelia")){
			List<RppPenyeliaPeranginan> listPeranginanSeliaan = mp.list("select x from RppPenyeliaPeranginan x where x.penyelia.id = '" + userId + "'");
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
	
	public void addfilter(String userId,String userRole) { }
	
	public void defaultButtonOption() {
		this.setDisableDefaultButton(true);
		this.setDisableBackButton(true);
		this.setDisableKosongkanUpperButton(true);
		this.setDisableSaveAddNewButton(true);
		this.setDisabledInfoNextTab(true);
		this.setDisableUpperBackButton(true);
	}

	@Override
	public void getRelatedData(RppPermohonan r) { }
	
	@Override
	public void save(RppPermohonan r) throws Exception { }
	
	
	@Command("viewListJenisUnit")
	public String viewListJenisUnit() throws Exception {
		
		context.remove("isSelenggaraKeseluruhan");
		
		try{
			mp = new MyPersistence();
			
			String idListPeranginan = getParam("idListPeranginan");
			RppPeranginan peranginan = (RppPeranginan) mp.find(RppPeranginan.class, idListPeranginan);
			
			context.put("tarikhMasukRpp", Util.getDateTime(getDate("tarikhMasukRpp"), "dd/MM/yyyy"));
			context.put("tarikhKeluarRpp", Util.getDateTime(getDate("tarikhKeluarRpp"), "dd/MM/yyyy"));

			context.put("peranginan", peranginan);
			context.put("senaraiJenisUnit", hashtableJenisUnit(Util.getDateTime(getDate("tarikhMasukRpp"), "yyyy-MM-dd"),Util.getDateTime(getDate("tarikhKeluarRpp"), "yyyy-MM-dd"),idListPeranginan));
			
			/**Checking kalau selenggara keseluruhan rpp*/
			boolean isSelenggaraKeseluruhan = UtilRpp.checkSelenggaraSeluruhRpp(idListPeranginan,Util.getDateTime(getDate("tarikhMasukRpp"), "yyyy-MM-dd"),Util.getDateTime(getDate("tarikhKeluarRpp"), "yyyy-MM-dd"));
			context.put("isSelenggaraKeseluruhan", isSelenggaraKeseluruhan);
			
		}catch (Exception e) {
			System.out.println("Error viewListJenisUnit : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/result.vm";
	}
	
	@Command("filterByGredJawatan")
	public String filterByGredJawatan() throws Exception {
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		
		try{
			mp = new MyPersistence();

			/**NAK FILTER LIST RPP UNTUK PENYELIA*/
			String listPeranginan = listRppByPenyelia(mp,userId,userRole);
			
			String carianGred = getParam("carianGred");
			context.put("senaraiPeranginan", senaraiRPbyGred(mp,carianGred,listPeranginan));
			context.put("gredId",carianGred);
			context.put("dtfrom", Util.getDateTime(getDate("tarikhMasukRpp"), "dd-MM-yyyy"));
			if (getDate("tarikhKeluarRpp") != null) {
				context.put("dtto", Util.getDateTime(getDate("tarikhKeluarRpp"), "dd-MM-yyyy"));
			} else {
				Calendar calTo = new GregorianCalendar();
				calTo.setTime(getDate("tarikhMasukRpp"));
				calTo.add(Calendar.WEEK_OF_MONTH, 1);
				String dtto = Util.getDateTime(calTo.getTime(), "dd-MM-yyyy");
			}			
			
		}catch (Exception e) {
			System.out.println("Error filterByGredJawatan : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		return templateDir + "/entry_fields.vm";
	}
	
	@Command("doChangeTarikhMula")
	public String doChangeTarikhMula() throws Exception {
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		
		try{
			mp = new MyPersistence();

			/**NAK FILTER LIST RPP UNTUK PENYELIA*/
			String listPeranginan = listRppByPenyelia(mp,userId,userRole);
			
			String carianGred = getParam("carianGred");
			context.put("senaraiPeranginan", senaraiRPbyGred(mp,carianGred,listPeranginan));
			context.put("gredId",carianGred);
			
			String dtfrom = Util.getDateTime(getDate("tarikhMasukRpp"), "dd-MM-yyyy");
			Calendar calTo = new GregorianCalendar();
			calTo.setTime(getDate("tarikhMasukRpp"));
			calTo.add(Calendar.WEEK_OF_MONTH, 1);
			String dtto = Util.getDateTime(calTo.getTime(), "dd-MM-yyyy");
			
			context.put("dtfrom", dtfrom);
			context.put("dtto", dtto);
			
		}catch (Exception e) {
			System.out.println("Error doChangeTarikhMula : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		return templateDir + "/entry_fields.vm";
	}
	
	@Command("openPopupSelenggara")
	public String openPopupSelenggara() throws Exception {
		
		String idJenisUnit = getParam("listIdJenisUnit");
		try{
			mp = new MyPersistence();
			
			JenisUnitRPP jns = (JenisUnitRPP) mp.find(JenisUnitRPP.class, idJenisUnit);
			context.put("senaraiSelenggara", hashtableDetailListSelenggara(Util.getDateTime(getDate("tarikhMasukRpp"), "yyyy-MM-dd"),Util.getDateTime(getDate("tarikhKeluarRpp"), "yyyy-MM-dd"),idJenisUnit,jns.getPeranginan().getId()));
			context.put("jns",jns);
			
		}catch (Exception e) {
			System.out.println("Error openPopupSelenggara : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/popup/senaraiSelenggara.vm";
	}
	
	@Command("openPopupKelompok")
	public String openPopupKelompok() throws Exception {
		
		try{
			mp = new MyPersistence();
			
			String idJenisUnit = getParam("listIdJenisUnit");
			JenisUnitRPP jns = (JenisUnitRPP) mp.find(JenisUnitRPP.class, idJenisUnit);
			context.put("senaraiKelompok", hashtableDetailListKelompok(Util.getDateTime(getDate("tarikhMasukRpp"), "yyyy-MM-dd"),Util.getDateTime(getDate("tarikhKeluarRpp"), "yyyy-MM-dd"),idJenisUnit));
			context.put("jns",jns);
			
		}catch (Exception e) {
			System.out.println("Error openPopupKelompok : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/popup/senaraiKelompok.vm";
	}
	
	@Command("openPopupIndividu")
	public String openPopupIndividu() throws Exception {
		try{
			mp = new MyPersistence();

			String idJenisUnit = getParam("listIdJenisUnit");
			JenisUnitRPP jns = (JenisUnitRPP) mp.find(JenisUnitRPP.class, idJenisUnit);
			context.put("senaraiIndividu", hashtableDetailListIndividu(Util.getDateTime(getDate("tarikhMasukRpp"), "yyyy-MM-dd"),Util.getDateTime(getDate("tarikhKeluarRpp"), "yyyy-MM-dd"),idJenisUnit));
			context.put("jns",jns);
		
		}catch (Exception e) {
			System.out.println("Error openPopupIndividu : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/popup/senaraiIndividu.vm";
	}
	
	@Command("openPopupTidakLengkap")
	public String openPopupTidakLengkap() throws Exception {
		
		try{
			mp = new MyPersistence();

			String idJenisUnit = getParam("listIdJenisUnit");
			JenisUnitRPP jns = (JenisUnitRPP) mp.find(JenisUnitRPP.class, idJenisUnit);
			context.put("senaraiTidakLengkap", hashtableDetailListTidakLengkap(Util.getDateTime(getDate("tarikhMasukRpp"), "yyyy-MM-dd"),Util.getDateTime(getDate("tarikhKeluarRpp"), "yyyy-MM-dd"),idJenisUnit));
			context.put("jns",jns);
			
		}catch (Exception e) {
			System.out.println("Error openPopupTidakLengkap : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/popup/senaraiTidakLengkap.vm";
	}
	
	@Command("openPopupTawaranUnit")
	public String openPopupTawaranUnit() throws Exception {
		
		try{
			mp = new MyPersistence();
			
			String idJenisUnit = getParam("listIdJenisUnit");
			JenisUnitRPP jns = (JenisUnitRPP) mp.find(JenisUnitRPP.class, idJenisUnit);
			context.put("senaraiTawaranUnit", hashtableDetailListTawaranUnit(idJenisUnit));
			context.put("jns",jns);
			
		}catch (Exception e) {
			System.out.println("Error openPopupTawaranUnit : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/popup/senaraiTawaranUnit.vm";
	}
	
	@SuppressWarnings("unchecked")
	@Command("openPopupDetailKekosongan")
	public String openPopupDetailKekosongan() throws Exception {
		String idJenisUnit = getParam("listIdJenisUnit");
		
		try{
			mp = new MyPersistence();

			JenisUnitRPP jns = (JenisUnitRPP) mp.find(JenisUnitRPP.class, idJenisUnit);
			List<RppUnit> listUnitAvailable = mp.list("select x from RppUnit x where x.jenisUnit.id = '"+jns.getId()+"' and COALESCE(x.status,'') <> 'RESERVED' ");
			context.put("jns",jns);
			context.put("listUnitAvailable",listUnitAvailable);
			context.put("tarikhMula", getDate("tarikhMasukRpp"));
			context.put("tarikhAkhir", getDate("tarikhKeluarRpp"));
			context.put("dtfrom", Util.getDateTime(getDate("tarikhMasukRpp"), "yyyy-MM-dd"));
			context.put("dtto", Util.getDateTime(getDate("tarikhKeluarRpp"), "yyyy-MM-dd"));
			
		}catch (Exception e) {
			System.out.println("Error begin : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/popup/senaraiKekosonganByUnit.vm";
	}
	
	@Command("openPopupInfoJenisUnit")
	public String openPopupInfoJenisUnit() throws Exception {
		
		try{
			mp = new MyPersistence();
			
			String idJenisUnit = getParam("listIdJenisUnit");
			JenisUnitRPP ju = (JenisUnitRPP) mp.find(JenisUnitRPP.class, idJenisUnit);
			
			String flagLuarPuncak = "";
			String flagPuncak = "";
			if(ju != null){
				if(ju.getFlagTiadaHadKelayakan().equalsIgnoreCase("Y")){
					flagLuarPuncak = "";
				}else if(ju.getFlagJulatGredKelayakan().equalsIgnoreCase("Y")){
					flagLuarPuncak = "JULAT";
				}else{
					flagLuarPuncak = "HAD";
				}
				
				if(ju.getFlagTiadaHadKelayakanWaktuPuncak().equalsIgnoreCase("Y")){
					flagPuncak = "";
				}else if(ju.getFlagJulatGredKelayakanWaktuPuncak().equalsIgnoreCase("Y")){
					flagPuncak = "JULATWP";
				}else{
					flagPuncak = "HADWP";
				}
			}
			
			context.put("listGredPerkhidmatan", dataUtil.getListGredPerkhidmatan());
			context.put("flagPuncak", flagPuncak);
			context.put("flagLuarPuncak", flagLuarPuncak);
			context.put("ju",ju);
			
		}catch (Exception e) {
			System.out.println("Error openPopupInfoJenisUnit : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/popup/infoJenisUnit.vm";
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List hashtableJenisUnit(String dateIn,String dateOut,String peranginanId){
		
		Vector list = new Vector();
		Hashtable h = null;
		Db db1 = null;
		
		try {
			db1 = new Db();
			
			String sql = " select *, (bil_total_unit - (bil_hari_booked_individu + bil_hari_booked_kelompok + bil_selenggara + bil_hari_tempahan_rosak) ) as bil_kekosongan from ( "+
							" select a.id,a.keterangan as jenis_unit, "+ 
							" (select count(b1.id) from rpp_unit b1 where b1.id_jenis_unit = a.id and ifnull(b1.status,'') <> 'RESERVED') * datediff('"+dateOut+"','"+dateIn+"') as bil_total_unit, "+
							
							" (select count(b1.id) from rpp_unit b1 where b1.id_jenis_unit = a.id and ifnull(b1.status,'') <> 'RESERVED') as bil_unit, "+

							" (select count(b1.id) "+
							" from rpp_unit b1, rpp_jadual_tempahan c1, rpp_permohonan d1 "+ 
							" where c1.id_permohonan = d1.id "+
							" and ifnull(c1.flag_status_tempahan,'') in ('CONFIRM','TEMP') "+
//							" and d1.id_pemohon NOT IN ('anon') "+
							" and d1.id_status in ('1425259713412','1435512646303','1425259713415','1430809277102','1425259713421','1425259713424') "+  
							" and b1.id = c1.id_unit "+
							" and b1.id_jenis_unit = a.id "+
							" and ifnull(d1.jenis_pemohon,'') = 'INDIVIDU' "+
							" and ifnull(b1.status,'') <> 'RESERVED' "+
							" and ((c1.tarikh_mula <= '"+dateIn+"' AND c1.tarikh_tamat > '"+dateIn+"')  "+
								" OR (c1.tarikh_mula < '"+dateOut+"' AND c1.tarikh_tamat >= '"+dateOut+"')  "+
								" OR (c1.tarikh_mula >= '"+dateIn+"' AND c1.tarikh_tamat < '"+dateOut+"')))as bil_permohonan_individu, "+
			
							" (select ifnull(sum( "+
							" datediff( case when tarikh_tamat > '"+dateOut+"' then '"+dateOut+"' else tarikh_tamat end , case when tarikh_mula < '"+dateIn+"' then '"+dateIn+"' else tarikh_mula end) "+
							" ),0) "+
							" from rpp_unit b1, "+ 
							" rpp_jadual_tempahan c1 left join rpp_permohonan d1 on c1.id_permohonan = d1.id "+ 
							" where ifnull(c1.flag_status_tempahan,'') in ('CONFIRM','TEMP') "+
//							" and d1.id_pemohon NOT IN ('anon') "+
							" and d1.id_status in ('1425259713412','1435512646303','1425259713415','1430809277102','1425259713421','1425259713424') "+  
							" and b1.id = c1.id_unit "+
							" and b1.id_jenis_unit = a.id "+
							" and ifnull(d1.jenis_pemohon,'') = 'INDIVIDU' "+
							" and ifnull(b1.status,'') <> 'RESERVED' "+
							" and ((c1.tarikh_mula <= '"+dateIn+"' AND c1.tarikh_tamat > '"+dateIn+"') "+ 
								" OR (c1.tarikh_mula < '"+dateOut+"' AND c1.tarikh_tamat >= '"+dateOut+"') "+
								" OR (c1.tarikh_mula >= '"+dateIn+"' AND c1.tarikh_tamat < '"+dateOut+"')))as bil_hari_booked_individu, "+	
		
							" (select count(b1.id) "+
							" from rpp_unit b1, rpp_jadual_tempahan c1, rpp_permohonan d1 "+
							" where c1.id_permohonan = d1.id "+
							" and ifnull(c1.flag_status_tempahan,'') in ('CONFIRM','TEMP') "+
//							" and d1.id_pemohon NOT IN ('anon')  "+ 
							" and d1.id_status in ('1433097397170','1425259713412','1435512646303','1425259713415','1425259713421','1425259713424') "+  
							" and b1.id = c1.id_unit "+
							" and b1.id_jenis_unit = a.id "+
							" and ifnull(d1.jenis_pemohon,'') = 'KELOMPOK' "+
							" and ifnull(b1.status,'') <> 'RESERVED' "+
							" and ((c1.tarikh_mula <= '"+dateIn+"' AND c1.tarikh_tamat > '"+dateIn+"') "+
								" OR (c1.tarikh_mula < '"+dateOut+"' AND c1.tarikh_tamat >= '"+dateOut+"')  "+
								" OR (c1.tarikh_mula >= '"+dateIn+"' AND c1.tarikh_tamat < '"+dateOut+"')))as bil_permohonan_kelompok, "+
			
							" (select ifnull(sum( "+
							" datediff( case when tarikh_tamat > '"+dateOut+"' then '"+dateOut+"' else tarikh_tamat end , case when tarikh_mula < '"+dateIn+"' then '"+dateIn+"' else tarikh_mula end) "+
							" ),0) "+
							" from rpp_unit b1, rpp_jadual_tempahan c1, rpp_permohonan d1  "+
							" where c1.id_permohonan = d1.id "+
							" and ifnull(c1.flag_status_tempahan,'') in ('CONFIRM','TEMP') "+
//							" and d1.id_pemohon NOT IN ('anon') "+
							" and d1.id_status in ('1433097397170','1425259713412','1435512646303','1425259713415','1425259713421','1425259713424') "+
							" and b1.id = c1.id_unit "+
							" and b1.id_jenis_unit = a.id "+
							" and ifnull(d1.jenis_pemohon,'') = 'KELOMPOK' "+
							" and ifnull(b1.status,'') <> 'RESERVED' "+
							" and ((c1.tarikh_mula <= '"+dateIn+"' AND c1.tarikh_tamat > '"+dateIn+"') "+ 
								" OR (c1.tarikh_mula < '"+dateOut+"' AND c1.tarikh_tamat >= '"+dateOut+"')  "+
								" OR (c1.tarikh_mula >= '"+dateIn+"' AND c1.tarikh_tamat < '"+dateOut+"')))as bil_hari_booked_kelompok, "+	
			
							" (select ifnull(sum( "+
							" datediff( case when a1.tarikh_keluar_rpp > '"+dateOut+"' then '"+dateOut+"' else a1.tarikh_keluar_rpp end , case when a1.tarikh_masuk_rpp < '"+dateIn+"' then '"+dateIn+"' else a1.tarikh_masuk_rpp end) "+
							" ),0) "+
							" from rpp_permohonan a1 left outer join rpp_jadual_tempahan c1 on a1.id = c1.id_permohonan , ruj_jenis_unit_rpp b1 "+
							" where a1.id_jenis_unit_rpp = b1.id "+
//							" and a1.id_pemohon NOT IN ('anon') "+
							" and a1.id_status in ('1425259713412','1435512646303','1425259713415','1430809277102','1425259713421','1425259713424') "+  
							" and a1.id_jenis_unit_rpp = a.id "+
							//" ##and ifnull(a1.jenis_pemohon,'') = 'INDIVIDU'
							" and ((a1.tarikh_masuk_rpp <= '"+dateIn+"' AND a1.tarikh_keluar_rpp > '"+dateIn+"')  "+
							" OR (a1.tarikh_masuk_rpp < '"+dateOut+"' AND a1.tarikh_keluar_rpp >= '"+dateOut+"') "+ 
							" OR (a1.tarikh_masuk_rpp >= '"+dateIn+"' AND a1.tarikh_keluar_rpp < '"+dateOut+"')) "+
							" and c1.id is null)as bil_hari_tempahan_rosak, "+	
							
							" (select ifnull(sum( "+
							" datediff( case when tarikh_tamat > '"+dateOut+"' then '"+dateOut+"' else tarikh_tamat end , case when tarikh_mula < '"+dateIn+"' then '"+dateIn+"' else tarikh_mula end) "+
							" ),0) "+
							" from rpp_selenggara f1, rpp_selenggara_unit_lokasi g1, rpp_unit h1 "+
							" where g1.id_selenggara = f1.id "+
							
							//" and f1.flag_jenis_selenggara = 'UNIT' "+
							
							//include selenggara LOKASI by id in....
							" and ((f1.flag_jenis_selenggara = 'LOKASI' and g1.id_peranginan = '"+peranginanId+"' ) "+ 
							" OR (f1.flag_jenis_selenggara = 'UNIT' and g1.id_unit = h1.id )) "+
							
							//" and g1.id_unit = h1.id "+
							" and h1.id_jenis_unit = a.id "+
							" and ifnull(h1.status,'') <> 'RESERVED' "+
							" and ((f1.tarikh_mula <= '"+dateIn+"' AND f1.tarikh_tamat > '"+dateIn+"')  "+
								" OR (f1.tarikh_mula < '"+dateOut+"' AND f1.tarikh_tamat >= '"+dateOut+"')  "+
								" OR (f1.tarikh_mula >= '"+dateIn+"' AND f1.tarikh_tamat < '"+dateOut+"')))as bil_selenggara "+

						" from ruj_jenis_unit_rpp a "+
						" where a.id_peranginan = '"+peranginanId+"' "+
						" ) as q ";
			
			ResultSet rs = db1.getStatement().executeQuery(sql);			
			while (rs.next()) {
				h = new Hashtable();
				h.put("bil_unit", rs.getInt("bil_unit"));
				h.put("idJenisUnit", rs.getString("id")== null?"":rs.getString("id"));
				h.put("jenisUnit", rs.getString("jenis_unit")== null?"":rs.getString("jenis_unit"));
				h.put("bilTotalUnit", rs.getInt("bil_total_unit"));
				h.put("bilHariBookedIndividu", rs.getInt("bil_hari_booked_individu"));
				h.put("bilHariBookedKelompok", rs.getInt("bil_hari_booked_kelompok"));
				
				// 16072018 - ADJUST BY PEJE - TO CATER PROBLEM 1 UNIT DI SELENGGARA 2 KALI PADA TARIKH YG SAMA
				h.put("bilSelenggara", rs.getInt("bil_selenggara") > rs.getInt("bil_total_unit") ? rs.getInt("bil_total_unit") : rs.getInt("bil_selenggara"));
				h.put("bilKekosongan", rs.getInt("bil_kekosongan") < 0 ? 0 : rs.getInt("bil_kekosongan"));
				
				h.put("bilHariTempahanRosak", rs.getInt("bil_hari_tempahan_rosak"));
				
				list.addElement(h);
			}	
			
		}catch(Exception e){
			System.out.println("error hashtableJenisUnit() : "+e.getMessage());
			e.printStackTrace();
		}finally { 
			if ( db1 != null ) db1.close();
		}
		
		return list;
	}
	
	
	public static List<RppPeranginan> senaraiRPbyGred(MyPersistence mp, String gredId, String listPeranginan){
		
		List<RppPeranginan> list = new ArrayList<RppPeranginan>();
		Db db1 = null;
		String sql = "";
		
		try{
			
			db1 = new Db();
			
			sql = "SELECT a.id as id_peranginan "+
				  " from rpp_peranginan a, ruj_jenis_unit_rpp b, rpp_unit c "+
				  " where a.id = b.id_peranginan "+
				  " and b.id = c.id_jenis_unit "+
				  " and a.id not in ('11','12','13','17') ";

			//display dulu luar waktu puncak.
			if(gredId!=null && !gredId.equalsIgnoreCase("")){
				sql += " AND ( (flag_tiada_had_kelayakan = 'Y') "+ /*Semua gred*/
					   " 	OR (flag_tiada_had_kelayakan = 'N' AND CAST(gred_minimum_kelayakan AS UNSIGNED) <= '"+gredId+"' ) "+ /* Gred minimum dan keatas */
					   " 	OR (flag_tiada_had_kelayakan = 'N' AND julat_gred_kelayakan = 'Y' "+ 
					   " 		AND '"+gredId+"' BETWEEN CAST(gred_minimum_kelayakan AS UNSIGNED) AND CAST(gred_maksimum_kelayakan AS UNSIGNED)) ) ";
			}

			if(listPeranginan != null && listPeranginan != "" ){
				sql += " AND a.id in "+listPeranginan+" ";
			}
			
			sql += " group by a.nama_peranginan ";
			
			ResultSet rs = db1.getStatement().executeQuery(sql);
			
			while (rs.next()){
				RppPeranginan data = (RppPeranginan) mp.find(RppPeranginan.class, rs.getString("id_peranginan"));
				list.add(data);
			}
			
		}catch(Exception e){ 
			System.out.println("error get senaraiRPbyGred : "+e.getMessage());
			e.printStackTrace();
		}finally { 
			if ( db1 != null ) db1.close();
		}
		
		return list;
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List hashtableDetailListSelenggara(String dateIn,String dateOut,String idJenisUnit,String peranginanId){
		
		Vector list = new Vector();
		Hashtable h = null;
		Db db1 = null;
		
		try {
			db1 = new Db();
			
			String sql = " select f1.id as id_selenggara, f1.perkara, f1.catatan, f1.tarikh_mula, f1.tarikh_tamat, h1.no_unit, h1.nama_unit "+
							" from rpp_selenggara f1, rpp_selenggara_unit_lokasi g1, rpp_unit h1 "+
							" where g1.id_selenggara = f1.id "+
							
							//include selenggara LOKASI by id in....
							" and ((f1.flag_jenis_selenggara = 'LOKASI' and g1.id_peranginan = '"+peranginanId+"' ) "+ 
							" OR (f1.flag_jenis_selenggara = 'UNIT' and g1.id_unit = h1.id )) "+
							
							//" and f1.flag_jenis_selenggara = 'UNIT' "+
							//" and g1.id_unit = h1.id "+
							" and ifnull(h1.status,'') <> 'RESERVED' "+
							" and ((f1.tarikh_mula <= '"+dateIn+"' AND f1.tarikh_tamat > '"+dateIn+"')  "+
								" OR (f1.tarikh_mula < '"+dateOut+"' AND f1.tarikh_tamat >= '"+dateOut+"')  "+
								" OR (f1.tarikh_mula >= '"+dateIn+"' AND f1.tarikh_tamat < '"+dateOut+"')) "+
							" and h1.id_jenis_unit = '"+idJenisUnit+"' ";	

			ResultSet rs = db1.getStatement().executeQuery(sql);			
			while (rs.next()) {
				h = new Hashtable();
				h.put("idSelenggara", rs.getString("id_selenggara")== null?"":rs.getString("id_selenggara"));
				h.put("perkara", rs.getString("perkara")== null?"":rs.getString("perkara"));
				h.put("catatan", rs.getString("catatan")== null?"":rs.getString("catatan"));
				h.put("tarikhMula", rs.getDate("tarikh_mula")== null?"":rs.getDate("tarikh_mula"));
				h.put("tarikhTamat", rs.getDate("tarikh_tamat")== null?"":rs.getDate("tarikh_tamat"));
				h.put("noUnit", rs.getString("no_unit")== null?"":rs.getString("no_unit"));
				h.put("namaUnit", rs.getString("nama_unit")== null?"":rs.getString("nama_unit"));
				list.addElement(h);
			}	
			
		}catch(Exception e){
			System.out.println("error hashtableDetailListSelenggara() : "+e.getMessage());
			e.printStackTrace();
		}finally { 
			if ( db1 != null ) db1.close();
		}
		
		return list;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List hashtableDetailListKelompok(String dateIn,String dateOut,String idJenisUnit){
		
		Vector list = new Vector();
		Hashtable h = null;
		Db db1 = null;
		
		try {
			db1 = new Db();
			
			String sql = "select us.*"
						+ ", rpp.nama_peranginan, d1.tarikh_permohonan, d1.tarikh_masuk_rpp, d1.tarikh_keluar_rpp, st.keterangan as status, d1.status_bayaran, d1.bahagian_unit  "
						+ " from rpp_unit b1, rpp_jadual_tempahan c1, rpp_permohonan d1, users us, rpp_peranginan rpp, ruj_status st "  
						+ " where c1.id_permohonan = d1.id "
						+ " and d1.id_status = st.id "
						+ " and d1.id_peranginan = rpp.id "
						+ " and d1.id_pemohon = us.user_login "
						+ " and ifnull(c1.flag_status_tempahan,'') in ('CONFIRM','TEMP')  "
//						+ " and d1.id_pemohon NOT IN ('anon')  "
						+ " and d1.id_status in ('1433097397170','1425259713412','1435512646303','1425259713415','1425259713421','1425259713424')  "
						+ " and b1.id = c1.id_unit  "
						+ " and ifnull(d1.jenis_pemohon,'') = 'KELOMPOK'  "
						+ " and ifnull(b1.status,'') <> 'RESERVED'  "
						+ " and ((c1.tarikh_mula <= '"+dateIn+"' AND c1.tarikh_tamat > '"+dateIn+"')  "
							+ " OR (c1.tarikh_mula < '"+dateOut+"' AND c1.tarikh_tamat >= '"+dateOut+"')   "
							+ " OR (c1.tarikh_mula >= '"+dateIn+"' AND c1.tarikh_tamat < '"+dateOut+"')) "
						+ " and b1.id_jenis_unit = '"+idJenisUnit+"' ";
			
			ResultSet rs = db1.getStatement().executeQuery(sql);			
			while (rs.next()) {
				h = new Hashtable();
				h.put("namaPeranginan", rs.getString("nama_peranginan")== null?"":rs.getString("nama_peranginan"));
				h.put("bahagianUnit", rs.getString("bahagian_unit")== null?"":rs.getString("bahagian_unit"));
				h.put("tarikhPermohonan", rs.getDate("tarikh_permohonan")== null?"":rs.getDate("tarikh_permohonan"));
				h.put("tarikhMasukRpp", rs.getDate("tarikh_masuk_rpp")== null?"":rs.getDate("tarikh_masuk_rpp"));
				h.put("tarikhKeluarRpp", rs.getDate("tarikh_keluar_rpp")== null?"":rs.getDate("tarikh_keluar_rpp"));
				h.put("status", rs.getString("status")== null?"":rs.getString("status"));
				h.put("statusBayaran", rs.getString("status_bayaran")== null?"":rs.getString("status_bayaran"));
				h.put("userName", rs.getString("user_name")== null?"":rs.getString("user_name"));
				h.put("noKp", rs.getString("no_kp")== null?"":rs.getString("no_kp"));
				h.put("emel", rs.getString("emel")== null?"":rs.getString("emel"));
				h.put("noTelefon", rs.getString("no_telefon")== null?"":rs.getString("no_telefon"));
				h.put("noTelefonBimbit", rs.getString("no_telefon_bimbit")== null?"":rs.getString("no_telefon_bimbit"));
				list.addElement(h);
			}	
			
		}catch(Exception e){
			System.out.println("error hashtableDetailListKelompok() : "+e.getMessage());
			e.printStackTrace();
		}finally { 
			if ( db1 != null ) db1.close();
		}
		
		return list;
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List hashtableDetailListIndividu(String dateIn,String dateOut,String idJenisUnit){
		
		Vector list = new Vector();
		Hashtable h = null;
		Db db1 = null;
		
		try {
			db1 = new Db();
			
			String sql = "select us.*"
						+ ", rpp.nama_peranginan, d1.tarikh_permohonan, d1.tarikh_masuk_rpp, d1.tarikh_keluar_rpp, st.keterangan as status, d1.status_bayaran "
						+ " from rpp_unit b1, rpp_jadual_tempahan c1, rpp_permohonan d1, users us, rpp_peranginan rpp, ruj_status st "  
						+ " where c1.id_permohonan = d1.id "
						+ " and d1.id_status = st.id "
						+ " and d1.id_peranginan = rpp.id "
						+ " and d1.id_pemohon = us.user_login "
						+ " and ifnull(c1.flag_status_tempahan,'') in ('CONFIRM','TEMP')  "
//						+ " and d1.id_pemohon NOT IN ('anon')  "
						+ " and d1.id_status in ('1425259713412','1435512646303','1425259713415','1430809277102','1425259713421','1425259713424')  "
						+ " and b1.id = c1.id_unit  "
						+ " and ifnull(d1.jenis_pemohon,'') = 'INDIVIDU'  "
						+ " and ifnull(b1.status,'') <> 'RESERVED'  "
						+ " and ((c1.tarikh_mula <= '"+dateIn+"' AND c1.tarikh_tamat > '"+dateIn+"')  "
							+ " OR (c1.tarikh_mula < '"+dateOut+"' AND c1.tarikh_tamat >= '"+dateOut+"')   "
							+ " OR (c1.tarikh_mula >= '"+dateIn+"' AND c1.tarikh_tamat < '"+dateOut+"')) "
						+ " and b1.id_jenis_unit = '"+idJenisUnit+"' ";
			
			ResultSet rs = db1.getStatement().executeQuery(sql);			
			while (rs.next()) {
				h = new Hashtable();
				h.put("namaPeranginan", rs.getString("nama_peranginan")== null?"":rs.getString("nama_peranginan"));
				h.put("tarikhPermohonan", rs.getDate("tarikh_permohonan")== null?"":rs.getDate("tarikh_permohonan"));
				h.put("tarikhMasukRpp", rs.getDate("tarikh_masuk_rpp")== null?"":rs.getDate("tarikh_masuk_rpp"));
				h.put("tarikhKeluarRpp", rs.getDate("tarikh_keluar_rpp")== null?"":rs.getDate("tarikh_keluar_rpp"));
				h.put("status", rs.getString("status")== null?"":rs.getString("status"));
				h.put("statusBayaran", rs.getString("status_bayaran")== null?"":rs.getString("status_bayaran"));
				h.put("userName", rs.getString("user_name")== null?"":rs.getString("user_name"));
				h.put("noKp", rs.getString("no_kp")== null?"":rs.getString("no_kp"));
				h.put("emel", rs.getString("emel")== null?"":rs.getString("emel"));
				h.put("noTelefon", rs.getString("no_telefon")== null?"":rs.getString("no_telefon"));
				h.put("noTelefonBimbit", rs.getString("no_telefon_bimbit")== null?"":rs.getString("no_telefon_bimbit"));
				list.addElement(h);
			}	
			
		}catch(Exception e){
			System.out.println("error hashtableDetailListIndividu() : "+e.getMessage());
			e.printStackTrace();
		}finally { 
			if ( db1 != null ) db1.close();
		}
		
		return list;
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List hashtableDetailListTidakLengkap(String dateIn,String dateOut,String idJenisUnit){
		
		Vector list = new Vector();
		Hashtable h = null;
		Db db1 = null;
		
		try {
			db1 = new Db();
			
			String sql = "select us.*"
						+ " , rpp.nama_peranginan, a1.tarikh_permohonan, a1.tarikh_masuk_rpp, a1.tarikh_keluar_rpp, st.keterangan as status, a1.status_bayaran "
						+ " from rpp_permohonan a1 left outer join rpp_jadual_tempahan c1 on a1.id = c1.id_permohonan , ruj_jenis_unit_rpp b1, users us "
						+ " ,rpp_peranginan rpp, ruj_status st "
						+ " where a1.id_jenis_unit_rpp = b1.id "
						+ " and a1.id_status = st.id "
						+ " and a1.id_pemohon = us.user_login "
						+ " and a1.id_peranginan = rpp.id "
//						+ " and a1.id_pemohon NOT IN ('anon') " 
						+ " and a1.id_status in ('1425259713412','1435512646303','1425259713415','1430809277102','1425259713421','1425259713424') "
						+ " and ((a1.tarikh_masuk_rpp <= '"+dateIn+"' AND a1.tarikh_keluar_rpp > '"+dateIn+"') " 
							+" OR (a1.tarikh_masuk_rpp < '"+dateOut+"' AND a1.tarikh_keluar_rpp >= '"+dateOut+"') "
							+" OR (a1.tarikh_masuk_rpp >= '"+dateIn+"' AND a1.tarikh_keluar_rpp < '"+dateOut+"')) "
						+ " and c1.id is null "
						+ " and a1.id_jenis_unit_rpp = '"+idJenisUnit+"' ";
			
			ResultSet rs = db1.getStatement().executeQuery(sql);			
			while (rs.next()) {
				h = new Hashtable();
				h.put("namaPeranginan", rs.getString("nama_peranginan")== null?"":rs.getString("nama_peranginan"));
				h.put("tarikhPermohonan", rs.getDate("tarikh_permohonan")== null?"":rs.getDate("tarikh_permohonan"));
				h.put("tarikhMasukRpp", rs.getDate("tarikh_masuk_rpp")== null?"":rs.getDate("tarikh_masuk_rpp"));
				h.put("tarikhKeluarRpp", rs.getDate("tarikh_keluar_rpp")== null?"":rs.getDate("tarikh_keluar_rpp"));
				h.put("status", rs.getString("status")== null?"":rs.getString("status"));
				h.put("statusBayaran", rs.getString("status_bayaran")== null?"":rs.getString("status_bayaran"));
				h.put("userName", rs.getString("user_name")== null?"":rs.getString("user_name"));
				h.put("noKp", rs.getString("no_kp")== null?"":rs.getString("no_kp"));
				h.put("emel", rs.getString("emel")== null?"":rs.getString("emel"));
				h.put("noTelefon", rs.getString("no_telefon")== null?"":rs.getString("no_telefon"));
				h.put("noTelefonBimbit", rs.getString("no_telefon_bimbit")== null?"":rs.getString("no_telefon_bimbit"));
				list.addElement(h);
			}	
			
		}catch(Exception e){
			System.out.println("error hashtableDetailListTidakLengkap() : "+e.getMessage());
			e.printStackTrace();
		}finally { 
			if ( db1 != null ) db1.close();
		}
		
		return list;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List hashtableDetailListTawaranUnit(String idJenisUnit){
		
		Vector list = new Vector();
		Hashtable h = null;
		Db db1 = null;
		
		try {
			db1 = new Db();
			
			String sql = "select * from rpp_unit b1 where b1.id_jenis_unit = '"+idJenisUnit+"' ";
			
			ResultSet rs = db1.getStatement().executeQuery(sql);			
			while (rs.next()) {
				h = new Hashtable();
				h.put("namaUnit", rs.getString("nama_unit")== null?"":rs.getString("nama_unit"));
				h.put("noUnit", rs.getString("no_unit")== null?"":rs.getString("no_unit"));
				h.put("catatan", rs.getString("catatan")== null?"":rs.getString("catatan"));
				h.put("status", rs.getString("status")== null?"":rs.getString("status"));
				list.addElement(h);
			}	
			
		}catch(Exception e){
			System.out.println("error hashtableDetailListTawaranUnit() : "+e.getMessage());
			e.printStackTrace();
		}finally { 
			if ( db1 != null ) db1.close();
		}
		
		return list;
	}
	
}

