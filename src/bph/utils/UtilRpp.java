package bph.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;

import lebah.db.Db;
import lebah.db.DbException;
import lebah.template.DbPersistence;
import lebah.template.UID;
import portal.module.entity.Role;
import portal.module.entity.Users;
import bph.entities.kewangan.KewDeposit;
import bph.entities.kewangan.KewInvois;
import bph.entities.kod.JenisUnitRPP;
import bph.entities.kod.KodJuruwang;
import bph.entities.kod.Status;
import bph.entities.rpp.RppAkaun;
import bph.entities.rpp.RppJadualTempahan;
import bph.entities.rpp.RppNotifikasi;
import bph.entities.rpp.RppPemohonVIP;
import bph.entities.rpp.RppPengurusanBilik;
import bph.entities.rpp.RppPenyeliaPeranginan;
import bph.entities.rpp.RppPeranginan;
import bph.entities.rpp.RppPermohonan;
import bph.entities.rpp.RppRekodTempahanLondon;
import bph.entities.rpp.RppSenaraiHitam;
import bph.entities.rpp.RppSeqPermohonan;
import bph.entities.rpp.RppTetapanCajTambahan;
import bph.entities.rpp.RppTransaksiNotifikasi;
import bph.entities.rpp.RppUnit;
import bph.entities.rpp.RppUserRoleNotifikasi;
import bph.mail.mailer.RppMailer;
import db.persistence.MyPersistence;
//import bph.entities.rpp.RppTempahanChalet;

public class UtilRpp {
	
	public static synchronized String generateNoTempahanIndividuSQL(String kodLokasi, Date tarikhMasuk) throws Exception {
	
		String ruj = "";
		String base = "BPH/IR";
		Integer thisyear = Integer.parseInt(Util.getCurrentDate("yyyy"));
		if (tarikhMasuk != null) {
			Calendar cal = new GregorianCalendar();
			cal.setTime(tarikhMasuk);
			thisyear = cal.get(Calendar.YEAR);
		}
		
		String defaultKodLokasi = "00";
		if(kodLokasi==null || kodLokasi.equalsIgnoreCase("")){kodLokasi = defaultKodLokasi;};
		
		int runningNo = 0;		
		
		Db dtbs = null;
		String sql = "";
		try {
			dtbs = new Db();
			sql = "select * from rpp_seq_permohonan where tahun = "+thisyear+" and lokasi = '"+kodLokasi+"' and jenis_permohonan = 'INDIVIDU' ";
			
			ResultSet rs = dtbs.getStatement().executeQuery(sql);
			String subsql = "";
			if (rs.next()){
				//UPDATE
				String id = rs.getString("id");
				runningNo = rs.getInt("bil")+1;
				subsql = "UPDATE rpp_seq_permohonan SET bil = "+runningNo+" WHERE id = '"+id+"' ";
			}else{
				//INSERT
				String id = UID.getUID();
				subsql = " INSERT INTO `rpp_seq_permohonan` (`id`, `tahun`, `lokasi`, `bil`, `jenis_permohonan`) "+
						 " VALUES "+
						 " ('"+id+"', '"+thisyear+"', '"+kodLokasi+"', 1, 'INDIVIDU') ";
				runningNo = 1;
			}
			
			Db dtbs2 = new Db();
			try{
	            Statement stmt = dtbs2.getStatement();
	            stmt.executeUpdate(subsql);
	        }catch(SQLException exsub){
	        	System.out.println(":: ERROR SUB Generate no fail individu : "+exsub.getMessage());
	        } finally { if ( dtbs2 != null ) dtbs2.close(); }
			
		} catch(SQLException ex){
			System.out.println(":: ERROR MAIN Generate no fail individu : "+ex.getMessage());
		}finally { if ( dtbs != null ) dtbs.close(); }
		
		String seq = String.format("%06d",runningNo);
		ruj = base+"/"+kodLokasi+"/"+thisyear+"/"+seq;
		
		return ruj;
	}

	public static synchronized String generateNoTempahanIndividu(DbPersistence db, String kodLokasi)  {
		
		String ruj = "";
		String base = "BPH/IR";
		Integer thisyear = Integer.parseInt(Util.getCurrentDate("yyyy"));
		
		int runningNo = 0;		
		
		RppSeqPermohonan sq = (RppSeqPermohonan) db.get("select x from RppSeqPermohonan x where x.tahun = "+thisyear+" and x.lokasi = '"+kodLokasi+"' and x.jenisPermohonan = 'INDIVIDU' ");

		try {
			
			/**BEGIN!**/
			db.begin();
			
			if(sq==null){
				sq = new RppSeqPermohonan();
				sq.setTahun(thisyear);
				sq.setLokasi(kodLokasi);
				sq.setBil(1);
				sq.setJenisPermohonan("INDIVIDU");
				db.persist(sq);
				runningNo = 1;
			}else{
				runningNo = sq.getBil()+1;
				sq.setBil(runningNo);
			}
			
			/**COMMIT!**/
			db.commit();
			
		} catch (Exception e) {
			System.out.println("ERROR generateNoTempahanIndividu : "+e.getMessage());
			e.printStackTrace();
		}
		
		String seq = String.format("%06d",runningNo);
		ruj = base+"/"+kodLokasi+"/"+thisyear+"/"+seq;
		
		return ruj;
	}
	
	public static String generateNoTempahanKelompok(MyPersistence mp,RppPermohonan r) throws Exception {
		
		String ruj = "";
		String base = "BPH/IR/KELOMPOK";
		Integer thisyear = Integer.parseInt(Util.getCurrentDate("yyyy"));
		String kodLokasi = r.getRppPeranginan().getKodLokasi()!=null?r.getRppPeranginan().getKodLokasi():"00";
		int runningNo = 0;		
		
		RppSeqPermohonan sq = (RppSeqPermohonan) mp.get("select x from RppSeqPermohonan x where x.tahun = "+thisyear+" and x.lokasi = '"+kodLokasi+"' and x.jenisPermohonan = 'INDIVIDU' ");

		if(sq==null){
			sq = new RppSeqPermohonan();
			sq.setTahun(thisyear);
			sq.setLokasi(kodLokasi);
			sq.setBil(1);
			sq.setJenisPermohonan("KELOMPOK");
			runningNo = 1;
		}else{
			runningNo = sq.getBil()+1;
			sq.setBil(runningNo);
		}
		
		mp.persist(sq);
		
		String seq = String.format("%06d",runningNo);
		ruj = base+"/"+kodLokasi+"/"+thisyear+"/"+seq;
		
		return ruj;
	}
	
	public static boolean checkingTarikhTempahanOnlineDibuka() throws Exception {
		
		boolean pass = false;
		Db db1 = null;
		String sql = "";
		
		try {
			db1 = new Db();
			sql = "select * from rpp_tetapan_buka_tempahan "+ 
				  " where tarikh_buka_tempahan <= current_date() "+
				  " and tarikh_menginap_hingga >= current_date() ";
			
			ResultSet rs = db1.getStatement().executeQuery(sql);
			
			//Ada data mean ada tempahan dibuka dari tarikh buka sehingga tarikh last menginap.
			if (rs.next()){
				pass = true;
			}
			
		} finally { 
			if ( db1 != null ) db1.close();
		}
		
		return pass;
	}
	
	public static boolean checkingAvailableRoom(RppUnit rppUnit,String tarikhMasuk,String tarikhKeluar,String jenisPemohon) throws Exception {
		
		
		String idUnit = rppUnit.getId();
		//RppPeranginan peranginan = rppUnit.getJenisUnit().getPeranginan();
		
		boolean roomAvailable = true;
		
		boolean checking1 = checkingByJadualTempahan(idUnit,tarikhMasuk,tarikhKeluar);
		//boolean checking2 = true;
		
		//disable sebab konflik pd tarikh first buka baru
		//if( jenisPemohon.equalsIgnoreCase("INDIVIDU")){ //KELOMPOK TIDAK PERLU TUNGGU TARIKH BUKA
		//	checking2 = checkingByTarikhBukaTempahanPeranginan(peranginan,tarikhMasuk,tarikhKeluar);
		//}
		
		boolean checking3 = false;
		checking3 = checkSelenggaraAvailableUnit(rppUnit, tarikhMasuk);
		
		//if(checking1==false || checking2==false  || checking3 == true ){
		if(checking1==false || checking3 == true ){
			roomAvailable = false;
		}
		
		return roomAvailable;
	}
	
	/** Kalau takde data/rekod = ada kekosongan pada tarikh tersebut */
	public static boolean checkingByJadualTempahan(String idUnit,String tarikhMasuk,String tarikhKeluar) throws Exception {
		Db db1 = null;
		String sql = "";
		boolean roomAvailable = true;
		
		try {
			db1 = new Db();
			sql = "select * from rpp_jadual_tempahan where id_unit = '"+idUnit+"' "+
				  " and ((tarikh_mula <= '"+tarikhMasuk+"' AND tarikh_tamat > '"+tarikhMasuk+"') OR (tarikh_mula < '"+tarikhKeluar+"' AND tarikh_tamat >= '"+tarikhKeluar+"') "+
				  " OR (tarikh_mula >= '"+tarikhMasuk+"' AND tarikh_tamat < '"+tarikhKeluar+"')) ";
			
			ResultSet rs = db1.getStatement().executeQuery(sql);
			
			if (rs.next()){
				roomAvailable = false;
			}
			
		} finally { 
			if ( db1 != null ) db1.close();
		}
		return roomAvailable;
	}
	
	
	/** Kalau ada data/rekod = tempahan dibuka pada tarikh checkin/checkout */
//	public static boolean checkingByTarikhBukaTempahanPeranginan(RppPeranginan peranginan,String tarikhMasuk,String tarikhKeluar) throws Exception {
//		Db db1 = null;
//		String sql = "";
//		boolean checking = false;
//		
//		try {
//			db1 = new Db();
//			/*
//			sql = "select * from rpp_tetapan_tarikh_tempahan_peranginan a, rpp_tetapan_buka_tempahan b "+
//					" where a.id_buka_tempahan = b.id "+
//					" and a.flag_aktif = 'Y' "+
//					" and a.id_peranginan = '"+peranginan.getId()+"' "+
//					" and '"+tarikhMasuk+"' between b.tarikh_menginap_dari and b.tarikh_menginap_hingga "+
//					" and '"+tarikhKeluar+"' between b.tarikh_menginap_dari and b.tarikh_menginap_hingga ";
//			*/
//			
//			sql = "select * from rpp_tetapan_buka_tempahan b "+
//					" where '"+tarikhMasuk+"' between b.tarikh_menginap_dari and b.tarikh_menginap_hingga "+
//					" and '"+tarikhKeluar+"' between b.tarikh_menginap_dari and b.tarikh_menginap_hingga ";
//			
//			ResultSet rs = db1.getStatement().executeQuery(sql);
//			
//			if (rs.next())
//				checking = true;
//			
//		} finally { 
//			if ( db1 != null ) db1.close();
//		}
//		
//		return checking;
//		
//	}
	
	/***
	 * @desc Kira total bil tempahan pemohon by tahun.
	 * @desc checking true = permohonan telah melebihi 3 kali pada tahun semasa
	 * Not include batal permohonan.
	 */
	public static boolean getTotalBilPrmhnByYear(MyPersistence mp, String userId, Date tarikhMasukRpp){
		
		boolean checking = false;
		//String currentYear = Util.getDateTime(new Date(), "yyyy");
		String selectedYear = Util.getDateTime(tarikhMasukRpp, "yyyy");
		ArrayList<RppPermohonan> list = new ArrayList<RppPermohonan>();
		Db db1 = null;
		try {
			db1 = new Db();
			
			//String sql = "select a.id from rpp_permohonan a, users b where a.id_pemohon = b.user_login and b.user_login = '"+userId+"' "+
			//		     " and a.id_status not in ('1425259713418','1435093978588','1433083787409') and year(a.tarikh_permohonan) = '"+currentYear+"' and a.jenis_pemohon = 'INDIVIDU' ";
			
			//15092015. En.Al-Sham bagitau Rumah Transit tidak termasuk dalam Quota.
			String sql = " select a.id from rpp_permohonan a, users b, rpp_peranginan c "+
						" where a.id_pemohon = b.user_login "+
						" and a.id_peranginan = c.id "+
						" and b.user_login = '"+userId+"' "+
						" and a.id_status not in ('1425259713418','1435093978588','1433083787409','1688545253001455')  "+
						//" and year(a.tarikh_permohonan) = '"+currentYear+"'  "+
						" and year(a.tarikh_masuk_rpp) = '"+selectedYear+"'  "+
						" and a.jenis_pemohon = 'INDIVIDU' "+
						" and c.id_jenis_peranginan not in ('RT') "+
						" and a.id in (select d.id_permohonan from rpp_akaun d where d.id_permohonan = a.id) ";
						
			//Tambahan filter kecualikan permohonan lama yang telah selesai, belum bayar dan telah tamat tarikh masuk. edit on 17092015
			sql += " and a.id not in (select x.id from rpp_permohonan x where x.id_status = '1435512646303' and x.status_bayaran = 'T' and x.tarikh_masuk_rpp < curdate() and x.jenis_pemohon = 'INDIVIDU') ";
			
			ResultSet rs = db1.getStatement().executeQuery(sql);			
			while (rs.next()) {
				RppPermohonan rp = (RppPermohonan) mp.find(RppPermohonan.class, rs.getString("id"));
				list.add(rp);
			}	
		}catch(Exception e){
			System.out.println("error getTotalBilPrmhnByYear() : "+e.getMessage());
		}finally { 
			if ( db1 != null ) db1.close();
		}
		
		if(list.size() >= 3){
			checking = true;
		}else{
			checking = false;
		}
		
		RppPemohonVIP vip = (RppPemohonVIP) mp.get("select x from RppPemohonVIP x where x.pemohon.id = '"+userId+"' ");
		
		if(vip!=null){
			checking = false;
		}
		
		return checking;
	}
	
	/***
	 * @desc validate jika sudah ada permohonan pada hari yang sama.
	 * @desc checking true = Ada permohonan telah dibuat pada tarikh yang sama pada tempahan lain
	 */
	public static boolean getCheckingSameDateBooking(MyPersistence mp, String userId, String tarikhCheckin){
		
		boolean checking = false;
		ArrayList<RppPermohonan> list = new ArrayList<RppPermohonan>();
		Db db1 = null;
		try {
			db1 = new Db();

			String sql = " select a.id from rpp_permohonan a, users b, rpp_peranginan c "+
						" where a.id_pemohon = b.user_login "+ 
						" and a.id_peranginan = c.id "+ 
						" and b.user_login = '"+userId+"' "+
						" and a.jenis_pemohon = 'INDIVIDU' "+ 
						" and ifnull(c.flag_kelulusan_sub,'T') <> 'Y' "+ 
						" and a.id_status not in ('1435093978588','1425259713418','1688545253001455') "+
						" and (a.tarikh_masuk_rpp <= '"+tarikhCheckin+"' AND a.tarikh_keluar_rpp > '"+tarikhCheckin+"') "; //Modified on 20102015. Tarikh Keluar tak include dalam same date
						//" and '"+tarikhCheckin+"' between a.tarikh_masuk_rpp and a.tarikh_keluar_rpp ";
			
			ResultSet rs = db1.getStatement().executeQuery(sql);			
			while (rs.next()) {
				RppPermohonan rp = (RppPermohonan) mp.find(RppPermohonan.class, rs.getString("id"));
				list.add(rp);
			}	
		}catch(Exception e){
			System.out.println("error getCheckingSameDateBooking() : "+e.getMessage());
		}finally { 
			if ( db1 != null ) db1.close();
		}
		
		if(list.size() > 0){
			checking = true;
		}else{
			checking = false;
		}
		
		/**VIP SAHAJA YANG LEPAS*/
		RppPemohonVIP vip = (RppPemohonVIP) mp.get("select x from RppPemohonVIP x where x.pemohon.id = '"+userId+"' ");
		if(vip!=null){
			checking = false;
		}
		
		return checking;
	}

	/***
	 * @desc validation utk badan berkanun bagi tempahan dibuat mesti lebih 7 hari sebelum menginap
	 * @desc checking true = tempahan yang dibuat kurang 7 hari dari tarikh menginap
	 */
	public static boolean getOnlineValidateCheckInTimeAndDate(String userId, String tarikhCheckin){
		
		boolean checking = false;
		Db db1 = null;
		int count = 0;
		try {
			db1 = new Db();
			
			String sql = " select * from users b  where b.user_login = '"+userId+"' "+ 
						 " and (( b.id_jenis_perkhidmatan = '03' and (date('"+tarikhCheckin+"') - (current_date()) > '7' )) "+
						 " OR (ifnull(b.id_jenis_perkhidmatan,'00') <> '03' and (( date('"+tarikhCheckin+"') <> (current_date()) ) "+
						 " OR ( date('"+tarikhCheckin+"') = (current_date()) and hour(sysdate()) < '12') ) ) ) ";
			
			ResultSet rs = db1.getStatement().executeQuery(sql);			
			while (rs.next()) {
				count ++;
			}	
			
		}catch(Exception e){
			System.out.println("error getCheckingCheckInDateForBadanBerkanun() : "+e.getMessage());
		}finally { 
			if ( db1 != null ) db1.close();
		}
		
		if(count > 0){
			checking = false;
		}else{
			checking = true;
		}
		return checking;
	}
	
	public static RppPeranginan getPeranginanByIdPenyelia(MyPersistence mp, String userId){
		RppPenyeliaPeranginan rppPenyeliaPeranginan = 
				(RppPenyeliaPeranginan) mp.get("select x from RppPenyeliaPeranginan x where x.penyelia.id = '"+userId+"' and x.statusPerkhidmatan = 'Y'");
		String idPeranginan = (rppPenyeliaPeranginan!=null?rppPenyeliaPeranginan.getPeranginan().getId():"");
		return (RppPeranginan) mp.find(RppPeranginan.class, idPeranginan);
	}
	
	
	/***
	 * @desc validation checking if pemohon masih disenaraihitam.
	 * @desc checking true = still disenaraihitam
	 */
	public static boolean checkingBlacklist(MyPersistence mp, String userId){
		
		boolean blacklist = false;
		
		ArrayList<RppSenaraiHitam> list = new ArrayList<RppSenaraiHitam>();
		Db db1 = null;
		try {
			db1 = new Db();
			String sql = "select a.id from rpp_senarai_hitam a, users b where a.id_pemohon = b.user_login and b.user_login = '"+userId+"' and ifnull(a.flag_aktif,'T') = 'Y' "+
						 " and current_date() <= a.tarikh_tamat ";
			ResultSet rs = db1.getStatement().executeQuery(sql);			
			while (rs.next()) {
				RppSenaraiHitam s = (RppSenaraiHitam) mp.find(RppSenaraiHitam.class, rs.getString("id"));
				list.add(s);
			}	
		}catch(Exception e){
			System.out.println("error checkingBlacklist() : "+e.getMessage());
		}finally { 
			if ( db1 != null ) db1.close();
		}
		
		if(list.size()>0){
			blacklist = true;
		}
		
		return blacklist;
	}
	
	
	public static String checkedActivity(String idpermohonan, String activityId){
		String str = "";
		Db db1 = null;
		try {
			db1 = new Db();
			String sql = "select * from rpp_kelompok_senarai_aktiviti where id_permohonan = '"+idpermohonan+"' and id_aktiviti_rpp = '"+activityId+"' ";
			ResultSet rs = db1.getStatement().executeQuery(sql);			
			if (rs.next()) {
				str = "checked";
			}	
		}catch(Exception e){
			System.out.println("error checkedActivity : "+e.getMessage());
		}finally { 
			if ( db1 != null ) db1.close();
		}
		return str;
	}
	
	public static List<RppUnit> listPilihBilikCheckinAvailable(MyPersistence mp, Date dateIn,Date dateOut,String idJenisUnit) {
		
		String tarikhMasuk = new SimpleDateFormat("yyyy-MM-dd").format(dateIn);
		String tarikhKeluar = new SimpleDateFormat("yyyy-MM-dd").format(dateOut);
		
		ArrayList<RppUnit> list = new ArrayList<RppUnit>();
		
		Db db1 = null;
		String sql = "";
		
		try{
			db1 = new Db();
			
//			sql = "select * from rpp_unit a "+
//				" LEFT JOIN rpp_jadual_tempahan b "+
//				" ON a.id = b.id_unit "+
//				" and ((tarikh_mula <= '"+tarikhMasuk+"' AND tarikh_tamat > '"+tarikhMasuk+"') "+
//				" OR (tarikh_mula < '"+tarikhKeluar+"' AND tarikh_tamat >= '"+tarikhKeluar+"') "+
//				" OR (tarikh_mula >= '"+tarikhMasuk+"' AND tarikh_tamat < '"+tarikhKeluar+"')) "+
//				" where a.id_jenis_unit = '"+idJenisUnit+"' ";
				//" and ifnull(b.flag_status_tempahan,'') not in ('CONFIRM') ";
				
			sql = "select * from rpp_unit a "
				  +" where a.id_jenis_unit = '"+idJenisUnit+"' "
				  +" and a.id not in (select b.id_unit_confirm from rpp_jadual_tempahan b where b.id_unit_confirm in ( select c.id from rpp_unit c where c.id_jenis_unit = '"+idJenisUnit+"' ) "
				  +" and ((b.tarikh_mula <= '"+tarikhMasuk+"' AND b.tarikh_tamat > '"+tarikhMasuk+"') OR (b.tarikh_mula < '"+tarikhKeluar+"' AND b.tarikh_tamat >= '"+tarikhKeluar+"') "
				  +" OR (b.tarikh_mula >= '"+tarikhMasuk+"' AND b.tarikh_tamat < '"+tarikhKeluar+"'))"
				  +" and ifnull(b.flag_status_tempahan,'') <> 'TEMP' ) ";

			//CHECKING SELENGGARA
//				  + " and a.id not in ( select a1.id_unit from rpp_selenggara_unit_lokasi a1, rpp_selenggara b "
//					  + " where a1.id_selenggara = b.id " 
//					  + " and (b.flag_jenis_selenggara = 'UNIT' and a1.id_unit in ( select c.id from rpp_unit c where c.id_jenis_unit = '"+idJenisUnit+"' ) " 
//					  + " OR (b.flag_jenis_selenggara = 'LOKASI' and a1.id_peranginan = '"+idperanginan+"' )) "
//					  + " and ((b.tarikh_mula <= '"+tarikhMasuk+"' AND b.tarikh_tamat > '"+tarikhMasuk+"') " 
//					  + " OR (b.tarikh_mula < '"+tarikhKeluar+"' AND b.tarikh_tamat >= '"+tarikhKeluar+"') " 
//					  + " OR (b.tarikh_mula >= '"+tarikhMasuk+"' AND b.tarikh_tamat < '"+tarikhKeluar+"')) )";
			
			ResultSet rs = db1.getStatement().executeQuery(sql);
			
			while (rs.next()){
				RppUnit rpu = (RppUnit) mp.find(RppUnit.class, rs.getString("id"));
				list.add(rpu);
			}
		
		}catch(Exception e){
			System.out.println("error listUnitAvailable : "+e.getMessage());
		}finally { 
			if ( db1 != null ) db1.close();
		}
		
		return list;
	}
	
	public static List<RppUnit> walkInListUnitAvailable(MyPersistence mp, Date dateIn,Date dateOut,String idJenisUnit) {
		
		String tarikhMasuk = new SimpleDateFormat("yyyy-MM-dd").format(dateIn);
		String tarikhKeluar = new SimpleDateFormat("yyyy-MM-dd").format(dateOut);
		
		ArrayList<RppUnit> list = new ArrayList<RppUnit>();
		
		Db db1 = null;
		String sql = "";
		
		try{
			db1 = new Db();
			
			sql = "select * from rpp_unit a "
				  +" where a.id_jenis_unit = '"+idJenisUnit+"' "
				  +" and a.id not in (select b.id_unit from rpp_jadual_tempahan b where b.id_unit in ( select c.id from rpp_unit c where c.id_jenis_unit = '"+idJenisUnit+"' ) "
				  +" and ((b.tarikh_mula <= '"+tarikhMasuk+"' AND b.tarikh_tamat > '"+tarikhMasuk+"') OR (b.tarikh_mula < '"+tarikhKeluar+"' AND b.tarikh_tamat >= '"+tarikhKeluar+"') "
				  +" OR (b.tarikh_mula >= '"+tarikhMasuk+"' AND b.tarikh_tamat < '"+tarikhKeluar+"'))) ";
				//  +" and ifnull(b.flag_status_tempahan,'') <> 'CONFIRM' ) ";

			ResultSet rs = db1.getStatement().executeQuery(sql);
			
			while (rs.next()){
				RppUnit rpu = (RppUnit) mp.find(RppUnit.class, rs.getString("id"));
				list.add(rpu);
			}
		
		}catch(Exception e){
			System.out.println("Error walkInListUnitAvailable : "+e.getMessage());
		}finally { 
			if ( db1 != null ) db1.close();
		}
		
		return list;
	}

	public static Integer gredKelayakanBiasaAtauWaktuPuncak(JenisUnitRPP obj,String tarikhMasuk,String tarikhKeluar) throws Exception{
		Integer gred = 0;
		boolean waktupuncak = checkWaktuPuncak(tarikhMasuk);
		if(waktupuncak){
			gred = obj.getGredKelayakanWaktuPuncak();
		}else{
			gred = obj.getGredMinimumKelayakan();
		} 
		return gred;
	}
	
	public static Double kadarSewaBiasaAtauWaktuPuncak(JenisUnitRPP obj,String tarikhMasuk,String tarikhKeluar){
		Double kadarsewa = 0d;
		boolean waktupuncak = false;
		
		try{
			waktupuncak = checkWaktuPuncak(tarikhMasuk);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
		
		if(waktupuncak){
			kadarsewa = obj.getKadarSewaWaktuPuncak();
		}else{
			kadarsewa = obj.getKadarSewa();
		} 
		return kadarsewa;
	}
	
	/**CHECK BALIK DENGAN USER WAKTU PUNCAK MELIBATKAN WAKTU MASUK SAHAJA ATAU TIDAK */
	public static boolean checkWaktuPuncak(String tarikhMasuk){
		
		boolean waktupuncak = false;
		Db db1 = null;
		String sql = "";
		
		try{
			
			db1 = new Db();
			
			sql = "select * from ruj_cuti where '"+tarikhMasuk+"' between tarikh_dari and tarikh_hingga ";
			
			ResultSet rs = db1.getStatement().executeQuery(sql);
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat fday = new SimpleDateFormat("EEEE");
			Date date = formatter.parse(tarikhMasuk);
			String hari = fday.format(date);
			
			if (rs.next() || hari.equalsIgnoreCase("saturday") || hari.equalsIgnoreCase("sunday")){
				waktupuncak = true;
			}
		
		}catch(Exception e){ 
			System.out.println(e.getMessage());
			e.printStackTrace();
		}finally { 
			if ( db1 != null ) db1.close();
		}
		
		return waktupuncak;
	}
	
	public static Date getTarikhTamatBayaran(RppPermohonan r){
		
		Date dtExpired = new Date();
		
		if(r.getTarikhMasukRpp()!=null && (r.getTarikhPermohonan()!=null || r.getTarikhKelulusanSub()!=null )){
			
			Calendar cal = Calendar.getInstance();
			
			String kelulusanHq = r.getRppPeranginan().getFlagKelulusanSub()!=null?r.getRppPeranginan().getFlagKelulusanSub():"";
			int diffInDays = 0;
			if(kelulusanHq.equalsIgnoreCase("Y")){
				diffInDays = (int)( (r.getTarikhMasukRpp().getTime() - r.getTarikhKelulusanSub().getTime()) / (1000 * 60 * 60 * 24) );
				cal.setTime(r.getTarikhKelulusanSub());
			}else{
				diffInDays = (int)( (r.getTarikhMasukRpp().getTime() - r.getTarikhPermohonan().getTime()) / (1000 * 60 * 60 * 24) );
				cal.setTime(r.getTarikhPermohonan());
			}
			
			if(diffInDays >= 7){
				//mohon lebih 7 hari. exp dalam masa 7 hari
				cal.add(Calendar.DATE, 7);
				dtExpired = cal.getTime();
			}else if(diffInDays < 7 && diffInDays > 2){
				//mohon dalam tempoh masa kurang 3-7 hari. exp 48jam
				cal.add(Calendar.DATE, 2);
				dtExpired = cal.getTime();
			}else if(diffInDays < 3){
				//add 12/01/2018 //mohon dalam tempoh masa kurang 3 hari. exp 24jam
				cal.add(Calendar.DATE, 0);
				dtExpired = cal.getTime();
			}	
		}
		
		//mohon lebih dari 7 hari tarikh menginap
		//tarikh permohonan / tarikh kelulusan sub
		
		//mohon dalam 7 hari dari tarikh menginap
		
		//mohon 24jam
		
		return dtExpired;
	}
	
	@SuppressWarnings("unchecked")
	public static List<RppAkaun> getListTempahanDanBayaran(MyPersistence mp, RppPermohonan r) {
		List<RppAkaun> list = mp.list("select x from RppAkaun x where x.permohonan.id = '"+r.getId()+"' ");
		return list;
	}
	
	
	@SuppressWarnings("unchecked")
	public static List<RppAkaun> getListTempahanDanBayaran(DbPersistence mp, RppPermohonan r) {
		List<RppAkaun> list = mp.list("select x from RppAkaun x where x.permohonan.id = '"+r.getId()+"' ");
		return list;
	}
	
	public static boolean checkingJadualTempahanDaily(String idUnit,String tarikhMasuk) throws Exception {
		Db db1 = null;
		String sql = "";
		boolean roomAvailable = true;
		
		try {
			db1 = new Db();
			
			//maybe kene join rpppermohonan dan where permohonan is not null
			
			sql = "select * from rpp_jadual_tempahan where id_unit = '"+idUnit+"' "+
				  //" and (tarikh_mula <= '"+tarikhMasuk+"' AND tarikh_tamat >= '"+tarikhMasuk+"') "+
				  // REMOVE EQUAL untuk checkin hari orang keluar.
				  " and (tarikh_mula <= '"+tarikhMasuk+"' AND tarikh_tamat > '"+tarikhMasuk+"') "+
				  " and ifnull(flag_status_tempahan,'') not in ('CANCEL','NOSHOW') ";
			
			ResultSet rs = db1.getStatement().executeQuery(sql);
			
			if (rs.next()){
				roomAvailable = false; //ada data
			}
			
		} finally { 
			if ( db1 != null ) db1.close();
		}
		return roomAvailable;
	}
	
	@SuppressWarnings("unchecked")
	public static void deleteChildTempahan(MyPersistence mp, String idpermohonan) {
		List<RppJadualTempahan> listbooked = mp.list("select x from RppJadualTempahan x where x.permohonan.id = '"+idpermohonan+"' ");
		if(listbooked != null){
			for(int i=0;i<listbooked.size();i++){
				mp.remove(listbooked.get(i));
			}
		}
	}
	
	
	public static List<SummaryRp> senaraiRPbyGred(Users user, boolean isRT, Map<String, Object> paramsMap){
		
		List<SummaryRp> list = new ArrayList<SummaryRp>();
		Db db1 = null;
		String sql = "";
		
		//boolean searching = Boolean.valueOf(paramsMap.get("searching").toString()); 
		String peranginanId = (String) paramsMap.get("peranginanId");
		String gredId = (String) paramsMap.get("gredId");
		
		String gredJawatanPemohon = "";
		if(user!=null){
			gredJawatanPemohon = user.getGredPerkhidmatan()!=null?user.getGredPerkhidmatan().getId():"";
		}else{
			gredJawatanPemohon = gredId;
		}
		
		
		try{
			
			db1 = new Db();
			
			sql = "SELECT a.id as id_peranginan, a.nama_peranginan "+
				  " from rpp_peranginan a, ruj_jenis_unit_rpp b, rpp_unit c "+
				  " where a.id = b.id_peranginan "+
				  " and b.id = c.id_jenis_unit "+
			
				  " and a.id not in ('11','12','13','14') ";

			//display dulu luar waktu puncak.
			if(user!=null || (gredJawatanPemohon!=null && !gredJawatanPemohon.equalsIgnoreCase("")) ){
				sql += " AND ( (flag_tiada_had_kelayakan = 'Y') "+ /*Semua gred*/
					   " 	OR (flag_tiada_had_kelayakan = 'N' AND CAST(gred_minimum_kelayakan AS UNSIGNED) <= '"+gredJawatanPemohon+"' ) "+ /* Gred minimum dan keatas */
					   " 	OR (flag_tiada_had_kelayakan = 'N' AND julat_gred_kelayakan = 'Y' "+ 
					   " 		AND '"+gredJawatanPemohon+"' BETWEEN CAST(gred_minimum_kelayakan AS UNSIGNED) AND CAST(gred_maksimum_kelayakan AS UNSIGNED)) ) ";
			}

			
			if(isRT){
				/**KECUALIKAN BANGLO TRANSIT SEBAB RESERVED*/
				sql += " AND a.id_jenis_peranginan in ('RT') AND a.id <> '17' ";
			}else{
				sql += " AND a.id_jenis_peranginan in ('RPP','RP') ";
			}
			
			if(peranginanId != null && peranginanId != ""){
				sql += " AND a.id = '"+peranginanId+"' ";
			}
			
			sql += " group by a.nama_peranginan ";
			
			ResultSet rs = db1.getStatement().executeQuery(sql);
			
			while (rs.next()){
				SummaryRp data = new SummaryRp();
				data.setPeranginanId(rs.getString("id_peranginan"));
				data.setNamaPeranginan(rs.getString("nama_peranginan")!=null?rs.getString("nama_peranginan"):"");
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
	
	/** ADD BY PEJE
	 * TO CATER SEMAKAN KEKOSONGAN UNTUK PENYELIA
	 * Get list jenis unit by peranginan
	 * Filter by penyelia
	 */
	public static List<SummaryRp> senaraiRPbyPenyelia(Users user) {

		List<SummaryRp> list = new ArrayList<SummaryRp>();
		Db db1 = null;
		String sql = "";

		try {
			db1 = new Db();

			sql = "SELECT * FROM rpp_peranginan, rpp_penyelia_peranginan"
					+ " WHERE rpp_penyelia_peranginan.id_peranginan = rpp_peranginan.id AND rpp_penyelia_peranginan.id_penyelia = '" + user.getId() +"'";

			ResultSet rs = db1.getStatement().executeQuery(sql);
			
			while (rs.next()) {
				SummaryRp data = new SummaryRp();
				data.setPeranginanId(rs.getString("id_peranginan"));
				data.setNamaPeranginan(rs.getString("nama_peranginan") != null ? rs.getString("nama_peranginan") : "");
				list.add(data);
			}

		} catch (Exception e) {
			System.out.println("error get senaraiRPbyPenyelia : " + e.getMessage());
			e.printStackTrace();
		} finally {
			if (db1 != null)
				db1.close();
		}
		return list;
	}
	
	public static List<JenisUnitRPP> senaraiJenisUnitByPeranginanAndJawatan(MyPersistence mp,String idrp,Users user, String gredId){
		
		List<JenisUnitRPP> list = new ArrayList<JenisUnitRPP>();
		Db db1 = null;
		String sql = "";
		
		String gredJawatanPemohon = "";
		if(user!=null){
			gredJawatanPemohon = user.getGredPerkhidmatan()!=null?user.getGredPerkhidmatan().getId():"";
		}else{
			gredJawatanPemohon = gredId;
		}
		
		try{
			
			db1 = new Db();
			sql = "SELECT b.id FROM ruj_jenis_unit_rpp b "
				 +" WHERE b.id_peranginan = '"+idrp+"' "
				 +" AND b.id in (select c.id_jenis_unit from rpp_unit c where ifnull(c.status,'') <> 'RESERVED') "
				 +" AND b.id not in ('31','35') ";
			
			//display dulu luar waktu puncak.
			if(user!=null || (gredJawatanPemohon!=null && !gredJawatanPemohon.equalsIgnoreCase("")) ){
				sql += " AND ( (flag_tiada_had_kelayakan = 'Y') "+ /*Semua gred*/
					   " 	OR (flag_tiada_had_kelayakan = 'N' AND CAST(gred_minimum_kelayakan AS UNSIGNED) <= '"+gredJawatanPemohon+"' ) "+ /* Gred minimum dan keatas */
					   " 	OR (flag_tiada_had_kelayakan = 'N' AND julat_gred_kelayakan = 'Y' "+ 
					   " 		AND '"+gredJawatanPemohon+"' BETWEEN CAST(gred_minimum_kelayakan AS UNSIGNED) AND CAST(gred_maksimum_kelayakan AS UNSIGNED)) ) ";
			}
			
			ResultSet rs = db1.getStatement().executeQuery(sql);
			
			while (rs.next()){
				JenisUnitRPP obj = (JenisUnitRPP) mp.find(JenisUnitRPP.class, rs.getString("id"));
				list.add(obj);
			}
			
		}catch(Exception e){ 
			System.out.println("error get senaraiJenisUnitByPeranginanAndJawatan : "+e.getMessage());
			e.printStackTrace();
		}finally { 
			if ( db1 != null ) db1.close();
		}
		
		return list;
	}
	
	
	public static boolean queryAvailabilityGredWaktuPuncak(String gred, String idJenisUnit){
		
		boolean available = false;
		Db db1 = null;
		String sql = "";
		
		try{
			db1 = new Db();
			
			sql = "select * from ruj_jenis_unit_rpp b "+	
					" where ( (flag_tiada_had_kelayakan_waktu_puncak = 'Y') "+	
					" OR (flag_tiada_had_kelayakan_waktu_puncak = 'N' AND CAST(gred_kelayakan_waktu_puncak AS UNSIGNED) <= '"+gred+"' ) "+	
					" OR (flag_tiada_had_kelayakan_waktu_puncak = 'N' AND julat_gred_kelayakan_waktu_puncak = 'Y' "+	
					" AND '"+gred+"' BETWEEN CAST(gred_kelayakan_waktu_puncak AS UNSIGNED) AND CAST(gred_maksimum_kelayakan_waktu_puncak AS UNSIGNED)) ) "+	
					" AND b.id = '"+idJenisUnit+"' ";
			
			ResultSet rs = db1.getStatement().executeQuery(sql);
			
			if (rs.next()){
				available = true;
			}
		
		}catch(Exception e){ 
			System.out.println("queryAvailabilityGredWaktuPuncak : "+e.getMessage());
			e.printStackTrace();
		}finally { 
			if ( db1 != null ) db1.close();
		}
		return available;
	}
	
	public static boolean checkSelenggaraAvailableUnit(RppUnit objUnit, String strdate){
		
		boolean checkBlocking = false;
		Db db1 = null;
		String sql = "";
		
		try{
			db1 = new Db();
			
			sql = "select * from rpp_selenggara_unit_lokasi a, rpp_selenggara b "+
					" where a.id_selenggara = b.id "+
					" and b.tarikh_mula <= '"+strdate+"' and b.tarikh_tamat >= '"+strdate+"' "+
					//" and a.id_unit = '"+objUnit.getId()+"' ";
					" and ((b.flag_jenis_selenggara = 'UNIT' and a.id_unit = '"+objUnit.getId()+"') "+
					" 	OR (b.flag_jenis_selenggara = 'LOKASI' and a.id_peranginan = '"+objUnit.getJenisUnit().getPeranginan().getId()+"' ) )";
					
			ResultSet rs = db1.getStatement().executeQuery(sql);
			
			if (rs.next()){
				checkBlocking = true;
			}
		
		}catch(Exception e){ 
			System.out.println("checkSelenggaraAvailableUnit : "+e.getMessage());
			e.printStackTrace();
		}finally { 
			if ( db1 != null ) db1.close();
		}
		return checkBlocking;
	}
	
	public static boolean checkSelenggaraAvailableUnitRange(RppUnit objUnit, String tarikhMasuk, String tarikhKeluar){
		
		boolean checkBlocking = false;
		Db db1 = null;
		String sql = "";
		
		try{
			db1 = new Db();
			
			sql = "select * from rpp_selenggara_unit_lokasi a, rpp_selenggara b "+
					" where a.id_selenggara = b.id "+
					" and ((b.tarikh_mula <= '"+tarikhMasuk+"' AND b.tarikh_tamat > '"+tarikhMasuk+"') "+ 
					" OR (b.tarikh_mula < '"+tarikhKeluar+"' AND b.tarikh_tamat >= '"+tarikhKeluar+"') "+ 
				    " OR (b.tarikh_mula >= '"+tarikhMasuk+"' AND b.tarikh_tamat < '"+tarikhKeluar+"') ) "+
					" and ((b.flag_jenis_selenggara = 'UNIT' and a.id_unit = '"+objUnit.getId()+"') "+
					" 	OR (b.flag_jenis_selenggara = 'LOKASI' and a.id_peranginan = '"+objUnit.getJenisUnit().getPeranginan().getId()+"' ) )";
					
			ResultSet rs = db1.getStatement().executeQuery(sql);
			
			if (rs.next()){
				checkBlocking = true;
			}
		
		}catch(Exception e){ 
			System.out.println("checkSelenggaraAvailableUnitRange : "+e.getMessage());
			e.printStackTrace();
		}finally { 
			if ( db1 != null ) db1.close();
		}
		return checkBlocking;
	}
	
	public static boolean checkingAccountInfoExist(Users user){
		boolean accountInfoExist = false;
		if( ( user.getNoAkaunBank() !=null && !user.getNoAkaunBank().equalsIgnoreCase("") )  &&  
			( user.getBank() !=null )){
			accountInfoExist = true;
		}
		return accountInfoExist;
	}
	
	@SuppressWarnings("unchecked")
	public static String multipleListSeliaan(MyPersistence mp, String penyeliaId) {
		String listPeranginan = "";
		List<RppPenyeliaPeranginan> listPeranginanSeliaan = mp.list("select x from RppPenyeliaPeranginan x where x.penyelia.id = '" + penyeliaId + "' and x.statusPerkhidmatan = 'Y' ");
		for (int i = 0; i< listPeranginanSeliaan.size(); i++) {
			if (listPeranginan.length() == 0) {
				listPeranginan = "'" + listPeranginanSeliaan.get(i).getPeranginan().getId() + "'";
			} else {
				listPeranginan = listPeranginan + "," + "'" + listPeranginanSeliaan.get(i).getPeranginan().getId() + "'";
			}
		}
		return listPeranginan;
	}
	
	
	/**
	 * IF salah 1 ada record, unit tak available for today
	 * */
	public static boolean checkingAvailableAndSelenggaraDaily(RppUnit unit,String tarikhMasuk) throws Exception {
		String idUnit = unit.getId();
		boolean roomAvailable = false;
		if(idUnit!=null && !idUnit.equalsIgnoreCase("")){
			boolean checkingAvailableDaily = checkingJadualTempahanDaily(idUnit,tarikhMasuk); //if TRUE, selected unit ada kekosongan
			boolean checkingSelenggara = checkSelenggaraAvailableUnit(unit, tarikhMasuk); //if FALSE, selected unit ada kekosongan
			if(checkingAvailableDaily==true && checkingSelenggara==false ){
				roomAvailable = true;
			}
		}
		return roomAvailable;
	}
	
	
	public static boolean checkSelenggaraLokasi(RppPeranginan peranginan){
		boolean hideRpp = false;
		Db db1 = null;
		String sql = "";
		
		String today = Util.getDateTime(new Date(), "yyyy-MM-dd");
		
		try{
			db1 = new Db();
			
			sql = "select * from rpp_selenggara_unit_lokasi a, rpp_selenggara b "+
					" where a.id_selenggara = b.id "+
					" and b.tarikh_mula <= '"+today+"' and b.tarikh_tamat >= '"+today+"' "+
					" and b.flag_jenis_selenggara = 'LOKASI' and a.id_peranginan = '"+peranginan.getId()+"' ";
			
			ResultSet rs = db1.getStatement().executeQuery(sql);
			
			if (rs.next()){
				hideRpp = true;
			}
		
		}catch(Exception e){ 
			System.out.println("checkSelenggaraAvailableUnit : "+e.getMessage());
			e.printStackTrace();
		}finally { 
			if ( db1 != null ) db1.close();
		}
		return hideRpp;
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List hashtableDetailRumahPeranginan(String rppId){
		Vector list = new Vector();
		Hashtable h = null;
		Db db1 = null;
		
		try {
			db1 = new Db();
			
			String sql = "select * from rpp_peranginan where id = '"+rppId+"' ";
			
			ResultSet rs = db1.getStatement().executeQuery(sql);			
			while (rs.next()) {
				h = new Hashtable();
				h.put("id", rs.getString("id")== null?"":rs.getString("id"));
				h.put("idJenisPeranginan", rs.getString("id_jenis_peranginan")== null?"":rs.getString("id_jenis_peranginan"));
				h.put("namaPeranginan", rs.getString("nama_peranginan")== null?"":rs.getString("nama_peranginan"));
				h.put("kodLokasi", rs.getString("kod_lokasi")== null?"":rs.getString("kod_lokasi"));
				h.put("flagKelulusanSub", rs.getString("flag_kelulusan_sub")== null?"":rs.getString("flag_kelulusan_sub"));
				list.addElement(h);
			}	
			
		}catch(Exception e){
			System.out.println(":: ERROR hashtableDetailRumahPeranginan : "+e.getMessage());
			e.printStackTrace();
		}finally { 
			if ( db1 != null ) db1.close();
		}
		return list;
	}
	
	
	
	public static RppPermohonan getRppPermohonanInSql(DbPersistence db, String permohonanId) {
		
		RppPermohonan r = null;
		Db db1 = null;
		
		try{
			
			db1 = new Db();
			
			String sql = "SELECT id, id_status FROM rpp_permohonan WHERE id = '"+permohonanId+"' ";
				
			ResultSet rs = db1.getStatement().executeQuery(sql);
			
			if (rs.next()){
				r = db.find(RppPermohonan.class, rs.getString("id"));
				System.out.println("dlm sql "+ rs.getString("id_status"));
			}
		
		}catch(Exception e){
			System.out.println("error getRppPermohonanInSql : "+e.getMessage());
		}finally { 
			if ( db1 != null ) db1.close();
		}
		
		System.out.println("r(getRppPermohonanInSql) "+r.getStatus().getId());
		
		return r;
	}
	
	
	public static void saveTarikhAkhirBayaranInSql(RppPermohonan r) {
		
		Date dtExpired = UtilRpp.getTarikhTamatBayaran(r);
		String strdate = new SimpleDateFormat("yyyy-MM-dd").format(dtExpired);
		String id = r!=null?r.getId():null;
		
		if(r!=null){
			String sql = "UPDATE rpp_permohonan SET tarikh_akhir_bayaran = '"+strdate+"' WHERE id = '"+id+"' ";
			Db database = null;
			try{
				database = new Db();
	            Statement stmt = database.getStatement();
	            stmt.executeUpdate(sql);
	        }catch(SQLException | DbException ex){
	        	System.out.println(":: ERROR saveTarikhAkhirBayaranInSql : "+ex.getMessage());
	        }finally { if ( database != null ) database.close(); }
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public static void daftarChalet(MyPersistence db, RppPermohonan r) throws Exception {
		
		boolean avroom = false;
		String jenisUnit = r.getJenisUnitRpp().getId();
		Date tarikhMasuk = r.getTarikhMasukRpp();
		Date tarikhKeluar = r.getTarikhKeluarRpp();
		int bilUnit = r.getKuantiti();
		
		String dtIn = new SimpleDateFormat("yyyy-MM-dd").format(tarikhMasuk);
		String dtOut = new SimpleDateFormat("yyyy-MM-dd").format(tarikhKeluar);
		
		List<RppUnit> rp = db.list("select x from RppUnit x where x.jenisUnit.id = '"+jenisUnit+"' and COALESCE(x.status,'') <> 'RESERVED' ");
		
		for(int i = 0; i < rp.size(); i++){
				
			//check bil unit yg telah didaftarkan
			int totalRegisteredUnit = db.list("select x from RppJadualTempahan x where x.permohonan.id = '"+r.getId()+"' ").size();
				
			if(totalRegisteredUnit < bilUnit){
					
				avroom = UtilRpp.checkingAvailableRoom(rp.get(i),dtIn,dtOut,r.getJenisPemohon());
				if(avroom){
					
					String idjadual = UID.getUID();
//					String idchalet = UID.getUID();
					
					String sqlJadual = "INSERT INTO `rpp_jadual_tempahan` (`id`, `id_unit`, `tarikh_mula`, `tarikh_tamat`, `status`, "
									+ " `id_permohonan`, `flag_status_tempahan` ) "
									+ " VALUES "
									+ " ('"+idjadual+"', '"+rp.get(i).getId()+"', '"+dtIn+"', '"+dtOut+"', 'B', "
									+ " '"+r.getId()+"', 'TEMP' ) "; 
				
//					String sqlChalet = "INSERT INTO `rpp_tempahan_chalet` (`id`, `id_permohonan`, `id_unit`) "
//									+" VALUES "
//									+" ('"+idchalet+"', '"+r.getId()+"', '"+rp.get(i).getId()+"') ";
					
					
					Db database = new Db();
			        try{
			            Statement stmt = database.getStatement();
			            stmt.executeUpdate(sqlJadual);
//			            stmt.executeUpdate(sqlChalet);
			        }catch(SQLException ex){
			        	System.out.println(":: ERROR daftarChalet : "+ex.getMessage());
			        }finally { if ( database != null ) database.close(); }
						
				}
			}
			
		}
			
	}
	
	public static void createRecordBayaran(MyPersistence mp, String pemohonId,RppPermohonan r) {
		
		int bilUnit = r.getKuantiti();
		
		int bilTambahanDewasa = 0;
		int bilTambahanKanakKanak = 0;
		if(!r.getRppPeranginan().getId().equalsIgnoreCase("3") && !r.getRppPeranginan().getId().equalsIgnoreCase("14")){
			bilTambahanDewasa = r.getBilTambahanDewasa()!=null?r.getBilTambahanDewasa():0;
			bilTambahanKanakKanak = r.getBilTambahanKanakKanak()!=null?r.getBilTambahanKanakKanak():0;
		}
		
		String dtIn = new SimpleDateFormat("yyyy-MM-dd").format(r.getTarikhMasukRpp());
		String dtOut = new SimpleDateFormat("yyyy-MM-dd").format(r.getTarikhKeluarRpp());
		Double kadarSewa = UtilRpp.kadarSewaBiasaAtauWaktuPuncak(r.getJenisUnitRpp(), dtIn, dtOut);
		
		/**TOTAL*/
		Double debitRpp = (kadarSewa * bilUnit * r.getTotalBilMalam());
		
		try {
			
			/** LEJAR TEMPAHAN */
			saveLejarTempahanInSql(pemohonId,r,kadarSewa,dtIn,bilUnit,debitRpp); 
			
			/** LANGKAWI DAN PD TAKPERLU BAYAR DEPOSIT**/
			if( !r.getRppPeranginan().getId().equalsIgnoreCase("3") && !r.getRppPeranginan().getId().equalsIgnoreCase("14") ){ 
				saveLejarDepositInSql(mp,pemohonId,r);
			}
			
			/** LEJAR TAMBAHAN DEWASA */
			if(bilTambahanDewasa > 0){ 
				saveLejarTambahanDewasaInSql(mp,pemohonId,r,bilTambahanDewasa);
			}
			
			/** LEJAR TAMBAHAN KANAK - KANAK */
			if(bilTambahanKanakKanak > 0){ 
				saveLejarTambahanKanakKanakInSql(mp,pemohonId,r,bilTambahanKanakKanak);
			}
			
			/** LEJAR (SEWA BOT) JIKA DI TASIK KENYIR. */
			if( r.getRppPeranginan().getId().equalsIgnoreCase("4") ){ 
				saveLejarSewaBotInSql(pemohonId,r);
			}
			
		} catch (Exception e1) {
			System.out.println("::ERROR Save Lejar : "+e1.getMessage());
		}
		
		
		try {
			
			/**TIDAK MELIBATKAN EKSEKUTIF PD DAN LANGKAWI*/
			if( !r.getRppPeranginan().getId().equalsIgnoreCase("3") && !r.getRppPeranginan().getId().equalsIgnoreCase("14")){
				saveInvoisDepositInSql(mp,pemohonId,r);
			}
			
		} catch (Exception e2) {
			System.out.println("::ERROR SAVE KEWINVOIS / KEWDEPOSIT : "+e2.getMessage());
		}
		
	}
	
	public static void saveLejarTempahanInSql(String pemohonId,RppPermohonan r, Double kadarSewa,String dtIn, int bilUnit, Double debit) throws Exception {
		
		String id = UID.getUID();
		String kodHasil = "74299";
		String permohonanId = r.getId();
		String noTempahan = r.getNoTempahan();
		String keterangan = ("SEWA "+r.getTotalBilMalam()+" MALAM DI  "+r.getJenisUnitRpp().getKeterangan()+", "+r.getRppPeranginan().getNamaPeranginan().toUpperCase());
		
		String catatanWP = "";
		if(UtilRpp.checkWaktuPuncak(dtIn)){
			catatanWP = "WAKTU PUNCAK";
		}
		
		String sql = "INSERT INTO `rpp_akaun` (`id`, `id_permohonan`, `id_kod_hasil`, `no_invois`, `tarikh_invois`, `keterangan`, "
					+ " `catatan`, `amaun_bayaran_seunit`, `bilangan_unit`, `debit`, `kredit`, `flag_void`, `flag_bayar`, "
					+ " `id_masuk`, `id_kemaskini`, `tarikh_masuk`, `tarikh_kemaskini` ) "
					+ " VALUES "
					+ " ('"+id+"', '"+permohonanId+"', '"+kodHasil+"', '"+noTempahan+"', now(), '"+keterangan+"', "
					+ " '"+catatanWP+"', "+kadarSewa+", "+bilUnit+", "+debit+", 0.00, 'T', 'T', "
					+ " '"+pemohonId+"', '"+pemohonId+"', now(), now() ) ";
		
		Db database = new Db();
        try{
            Statement stmt = database.getStatement();
            stmt.executeUpdate(sql);
        }catch(SQLException ex){
        	System.out.println(":: ERROR saveLejarTempahanInSql : "+ex.getMessage());
        }finally { if ( database != null ) database.close(); }
        
	}

	public static void saveLejarDepositInSql(MyPersistence mp,String pemohonId,RppPermohonan r) throws Exception {
		
		String id = UID.getUID();
		String kodHasil = "72311"; // KOD HASIL DEPOSIT
		String permohonanId = r.getId();
		String noTempahan = r.getNoTempahan();
		String keterangan = ("DEPOSIT SEWA "+r.getJenisUnitRpp().getKeterangan()+", "+r.getRppPeranginan().getNamaPeranginan().toUpperCase());
		
		RppTetapanCajTambahan objdep = (RppTetapanCajTambahan) mp.find(RppTetapanCajTambahan.class, "1432887883848");
		Double deposit = objdep!=null?objdep.getCajBayaran():0d;
		Double debitdep = (deposit * 1);
		
		String sql = "INSERT INTO `rpp_akaun` (`id`, `id_permohonan`, `id_kod_hasil`, `no_invois`, `tarikh_invois`, `keterangan`, "
					+ " `amaun_bayaran_seunit`, `bilangan_unit`, `debit`, `kredit`, `flag_void`, `flag_bayar`, "
					+ " `id_masuk`, `id_kemaskini`, `tarikh_masuk`, `tarikh_kemaskini` ) "
					+ " VALUES "
					+ " ('"+id+"', '"+permohonanId+"', '"+kodHasil+"', '"+noTempahan+"', now(), '"+keterangan+"', "
					+ " "+deposit+", 1, "+debitdep+", 0.00, 'T', 'T', "
					+ " '"+pemohonId+"', '"+pemohonId+"', now(), now() ) ";
		
		Db database = new Db();
        try{
            Statement stmt = database.getStatement();
            stmt.executeUpdate(sql);
        }catch(SQLException ex){
        	System.out.println(":: ERROR saveLejarDepositInSql : "+ex.getMessage());
        }finally { if ( database != null ) database.close(); }
        
	}
	
	
	public static void saveLejarTambahanDewasaInSql(MyPersistence mp,String pemohonId,RppPermohonan r, int bilTambahanDewasa) throws Exception {
		
		String id = UID.getUID();
		String kodHasil = "74299";
		String permohonanId = r.getId();
		String noTempahan = r.getNoTempahan();
		String keterangan = "TAMBAHAN DEWASA";
		
		RppTetapanCajTambahan extbed = (RppTetapanCajTambahan) mp.find(RppTetapanCajTambahan.class, "1432867359415");
		Double extbedprice = extbed!=null?extbed.getCajBayaran():0d;
		Double akdebit = (extbedprice * bilTambahanDewasa); // total harga seunit * bil tambahan dewasa
		
		String sql = "INSERT INTO `rpp_akaun` (`id`, `id_permohonan`, `id_kod_hasil`, `no_invois`, `tarikh_invois`, `keterangan`, "
					+ " `amaun_bayaran_seunit`, `bilangan_unit`, `debit`, `kredit`, `flag_void`, `flag_bayar`, "
					+ " `id_masuk`, `id_kemaskini`, `tarikh_masuk`, `tarikh_kemaskini` ) "
					+ " VALUES "
					+ " ('"+id+"', '"+permohonanId+"', '"+kodHasil+"', '"+noTempahan+"', now(), '"+keterangan+"', "
					+ " "+extbedprice+", "+bilTambahanDewasa+", "+akdebit+", 0.00, 'T', 'T', "
					+ " '"+pemohonId+"', '"+pemohonId+"', now(), now() ) ";
		
		Db database = new Db();
        try{
            Statement stmt = database.getStatement();
            stmt.executeUpdate(sql);
        }catch(SQLException ex){
        	System.out.println(":: ERROR saveLejarTambahanDewasaInSql : "+ex.getMessage());
        }finally { if ( database != null ) database.close(); }
        
	}
	
	public static void saveLejarTambahanKanakKanakInSql(MyPersistence mp,String pemohonId,RppPermohonan r, int bilTambahanKanakKanak) throws Exception {
		
		String id = UID.getUID();
		String kodHasil = "74299";
		String permohonanId = r.getId();
		String noTempahan = r.getNoTempahan();
		String keterangan = "TAMBAHAN KANAK - KANAK";
		
		RppTetapanCajTambahan extbed = (RppTetapanCajTambahan) mp.find(RppTetapanCajTambahan.class, "1436755298337");
		Double extbedprice = extbed!=null?extbed.getCajBayaran():0d;
		Double akdebit = (extbedprice * bilTambahanKanakKanak); // total harga seunit * bil tambahan kanak kanak
		
		String sql = "INSERT INTO `rpp_akaun` (`id`, `id_permohonan`, `id_kod_hasil`, `no_invois`, `tarikh_invois`, `keterangan`, "
					+ " `amaun_bayaran_seunit`, `bilangan_unit`, `debit`, `kredit`, `flag_void`, `flag_bayar`, "
					+ " `id_masuk`, `id_kemaskini`, `tarikh_masuk`, `tarikh_kemaskini` ) "
					+ " VALUES "
					+ " ('"+id+"', '"+permohonanId+"', '"+kodHasil+"', '"+noTempahan+"', now(), '"+keterangan+"', "
					+ " "+extbedprice+", "+bilTambahanKanakKanak+", "+akdebit+", 0.00, 'T', 'T', "
					+ " '"+pemohonId+"', '"+pemohonId+"', now(), now() ) ";
		
		Db database = new Db();
        try{
            Statement stmt = database.getStatement();
            stmt.executeUpdate(sql);
        }catch(SQLException ex){
        	System.out.println(":: ERROR saveLejarTambahanKanakKanakInSql : "+ex.getMessage());
        }finally { if ( database != null ) database.close(); }
        
	}
	
	public static void saveLejarSewaBotInSql(String pemohonId,RppPermohonan r) throws Exception {
		
		String id = UID.getUID();
		String kodHasil = "74304";
		String permohonanId = r.getId();
		String noTempahan = r.getNoTempahan();
		String keterangan = "SEWA BOT PERGI DAN BALIK";
		
		int bilBotDewasa = ((r.getBilDewasa()!=null?r.getBilDewasa():0) + (r.getBilTambahanDewasa()!=null?r.getBilTambahanDewasa():0));
		int bilBotKanakKanak = ((r.getBilKanakKanak()!=null?r.getBilKanakKanak():0) + (r.getBilTambahanKanakKanak()!=null?r.getBilTambahanKanakKanak():0));
		int totalHead = (bilBotDewasa + bilBotKanakKanak);
		
		Double totalHargaDewasa = (bilBotDewasa * 10.00);
		Double totalHargaKanakKanak =  (bilBotKanakKanak * 5.00);
		Double totalHargaSewaBot = (totalHargaDewasa + totalHargaKanakKanak);
		
		if(totalHead <= 3){
			totalHargaSewaBot = 30.00;
		}
		
		String sql = "INSERT INTO `rpp_akaun` (`id`, `id_permohonan`, `id_kod_hasil`, `no_invois`, `tarikh_invois`, `keterangan`, "
					+ " `amaun_bayaran_seunit`, `bilangan_unit`, `debit`, `kredit`, `flag_void`, `flag_bayar`, "
					+ " `id_masuk`, `id_kemaskini`, `tarikh_masuk`, `tarikh_kemaskini` ) "
					+ " VALUES "
					+ " ('"+id+"', '"+permohonanId+"', '"+kodHasil+"', '"+noTempahan+"', now(), '"+keterangan+"', "
					+ " 0.00, "+totalHead+", "+totalHargaSewaBot+", 0.00, 'T', 'T', "
					+ " '"+pemohonId+"', '"+pemohonId+"', now(), now() ) ";
		
		Db database = new Db();
        try{
            Statement stmt = database.getStatement();
            stmt.executeUpdate(sql);
        }catch(SQLException ex){
        	System.out.println(":: ERROR saveLejarSewaBotInSql : "+ex.getMessage());
        }finally { if ( database != null ) database.close(); }
		
	}
	
	@SuppressWarnings("unchecked")
	public static void saveInvoisDepositInSql(MyPersistence mp,String pemohonId,RppPermohonan r) throws Exception {
		
		List<RppAkaun> listAkaun = mp.list("select x from RppAkaun x where x.permohonan.id = '"+r.getId()+"' ");
		for(int i=0;i<listAkaun.size();i++){
			String kodHasil = listAkaun.get(i).getKodHasil()!=null?listAkaun.get(i).getKodHasil().getKod():"";
			if(kodHasil.equalsIgnoreCase("72311")){ //deposit
				createDepositInFinance(listAkaun.get(i));
			}else{
				createInvoisInFinance(listAkaun.get(i),r);
			}
		}
		
	}
	
	public static void createInvoisInFinance(RppAkaun ak, RppPermohonan r) throws Exception {
		
		String id = UID.getUID();
		String noInvois = ak.getNoInvois();
		String lejarId = ak.getId();
		String pemohonId = ak.getPermohonan().getPemohon().getId();
		String keterangan = ak.getKeterangan().toUpperCase();
		Double debit = ak.getDebit();
		String dtIn = new SimpleDateFormat("yyyy-MM-dd").format(r.getTarikhMasukRpp());
		String dtOut = new SimpleDateFormat("yyyy-MM-dd").format(r.getTarikhKeluarRpp());
		
		String sql = " INSERT INTO `kew_invois` (`id`, `id_kod_hasil`, `flag_bayaran`, `no_invois`, `tarikh_invois`, `no_rujukan`, `id_lejar`, `id_pembayar`,"
					+" `id_jenis_bayaran`, `keterangan_bayaran`, `debit`, `kredit`, `flag_bayar`, `flag_queue`, `id_pendaftar`, "
					+" `tarikh_daftar`, `tarikh_dari`, `tarikh_hingga` ) "
					+" VALUES "
					+" ('"+id+"', '"+ak.getKodHasil().getId()+"', 'SEWA', '"+noInvois+"', now(), '"+noInvois+"', '"+lejarId+"', '"+pemohonId+"', "
					+" '02', '"+keterangan+"', "+debit+", 0.00, 'T', 'T', '"+pemohonId+"', "
					+" now(), '"+dtIn+"', '"+dtOut+"') ";
	
		Db database = new Db();
	    try{
	        Statement stmt = database.getStatement();
	        stmt.executeUpdate(sql);
	    }catch(SQLException ex){
	    	System.out.println(":: ERROR createInvoisInFinance : "+ex.getMessage());
	    }finally { if ( database != null ) database.close(); }
    
	}
	
	public static void createDepositInFinance(RppAkaun ak) throws Exception {
		
		String id = UID.getUID();
		String lejarId = ak.getId();
		String pemohonId = ak.getPermohonan().getPemohon().getId();
		String keterangan = ak.getKeterangan().toUpperCase();
		Double amaunDeposit = ak.getDebit()!=null?ak.getDebit():0d;
		String dtIn = new SimpleDateFormat("yyyy-MM-dd").format(ak.getPermohonan().getTarikhMasukRpp());
		String dtOut = new SimpleDateFormat("yyyy-MM-dd").format(ak.getPermohonan().getTarikhKeluarRpp());
		String noInvois = ak.getNoInvois();
		
		String sql = " INSERT INTO `kew_deposit` (`id`, `id_kod_hasil`, `id_lejar`, `id_jenis_bayaran`, `id_pendeposit`, "
				 	+" `keterangan_deposit`, `tarikh_deposit`, `flag_pulang_deposit`, "
				 	+" `jumlah_deposit`, `baki_deposit`,"
				 	+" `flag_warta`, `flag_bayar`, `flag_queue`, `tarikh_dari`, `tarikh_hingga`, `no_invois`) "
					+" VALUES "
					+" ('"+id+"', '"+ak.getKodHasil().getId()+"', '"+lejarId+"', '02', '"+pemohonId+"', "
					+" '"+keterangan+"', now(), 'T', "+amaunDeposit+", 0.00, 'T', 'T', 'T', '"+dtIn+"', '"+dtOut+"', '"+noInvois+"') ";
		
		Db database = new Db();
        try{
            Statement stmt = database.getStatement();
            stmt.executeUpdate(sql);
        }catch(SQLException ex){
        	System.out.println(":: ERROR createDepositInFinance : "+ex.getMessage());
        }finally { if ( database != null ) database.close(); }
		
	}
	
	/**
	 * DELETE KEWINVOIS
	 * VOID RPPAKAUN 
	 * UPDATE STATUS TO GAGAL 1688545253001455
	 * */
	public static void deleteAndUpdateStatusGagal(MyPersistence mp, RppPermohonan r, List<RppAkaun> listak) throws Exception {
		
		/**UPDATE STATUS KEPADA PERMOHONAN GAGAL
		 * SQL DAN DBPERSIST */
		String sqlp = "UPDATE rpp_permohonan SET id_status = '1688545253001455' WHERE id = '"+r.getId()+"' "; 
		Db dbasep = null;
	    try{
	    	dbasep = new Db();
	        Statement stmtm = dbasep.getStatement();
	        stmtm.executeUpdate(sqlp);
	    }catch(SQLException ex){
	    	System.out.println(":: ERROR deleteAndUpdateStatusGagal(kemaskini akaun) : "+ex.getMessage());
	    }finally { 
	    	if ( dbasep != null ) dbasep.close(); 
	    	mp.begin();
			r.setStatus((Status) mp.find(Status.class, "1688545253001455"));
			mp.persist(r);
			mp.commit();
	    }
		
        
        /**UPDATE AKAUN DAN DELETE KEWINVOIS & KEWDEPOSIT */
		if(listak != null){
			if(listak.size() > 0){
				for(int i = 0; i < listak.size(); i++){
					RppAkaun xa = listak.get(i);
					
					String sql = "UPDATE rpp_akaun SET flag_void = 'Y', tarikh_void = now(), amaun_void = "+xa.getDebit()+" WHERE id = '"+xa.getId()+"' ";
					Db database = new Db();
			        try{
			            Statement stmt = database.getStatement();
			            stmt.executeUpdate(sql);
			        }catch(SQLException ex){
			        	System.out.println(":: ERROR deleteAndUpdateStatusGagal(kemaskini akaun) : "+ex.getMessage());
			        }finally { if ( database != null ) database.close(); }
			        
					
					if(!xa.getKodHasil().getId().equals("72311")){ //INVOIS
						
						KewInvois xki = (KewInvois) mp.get("select x from KewInvois x where x.idLejar = '"+xa.getId()+"' and x.kodHasil.id != '72311' ");
						if(xki != null){
							String sqlxki = "DELETE FROM kew_invois WHERE id = '"+xki.getId()+"' ";
							Db databasexki = new Db();
					        try{
					            Statement stmt = databasexki.getStatement();
					            stmt.executeUpdate(sqlxki);
					        }catch(SQLException ex){
					        	System.out.println(":: ERROR deleteAndUpdateStatusGagal(delete kewInvois) : "+ex.getMessage());
					        }finally { if ( databasexki != null ) databasexki.close(); }
						}
						
					}else if(xa.getKodHasil().getId().equals("72311")){
						
						KewDeposit xkd = (KewDeposit) mp.get("select x from KewDeposit x where x.idLejar = '"+xa.getId()+"' and x.kodHasil.id = '72311' ");
						if(xkd != null){
							
							String sqlxkd = "DELETE FROM kew_deposit WHERE id = '"+xkd.getId()+"' ";
							Db databasexkd = new Db();
					        try{
					            Statement stmt = databasexkd.getStatement();
					            stmt.executeUpdate(sqlxkd);
					        }catch(SQLException ex){
					        	System.out.println(":: ERROR deleteAndUpdateStatusGagal(delete kewDeposit) : "+ex.getMessage());
					        }finally { if ( databasexkd != null ) databasexkd.close(); }
						}
						
					}
				}
			}
		}
			
	}
	
	/**TAK PAKAI*/
	public static void saveNotifikasi(String idrekod, String flagAktiviti, String moduleClass, Date tarikhAktiviti, String flagBuka, Users dibukaOleh, Date tarikhBuka ) throws Exception {
		
		String id = UID.getUID();
		String sql = " INSERT INTO `rpp_notifikasi` (`id`, `id_rekod`, `flag_aktif`, `module_class`, `tarikh_aktiviti`) "+
				 	 " VALUES "+
				 	 " ('"+id+"', '"+idrekod+"', '"+flagAktiviti+"', '"+moduleClass+"', now()) ";
		
		Db database = new Db();
        try{
            Statement stmt = database.getStatement();
            stmt.executeUpdate(sql);
        }catch(SQLException ex){
        	System.out.println(":: ERROR saveNotifikasi : "+ex.getMessage());
        }finally { if ( database != null ) database.close(); }
		
	}
	
	
	public static boolean checkSelenggaraSeluruhRpp(String idperanginan, String strDateIn, String strDateOut){
		
		boolean isSelenggara = false;
		Db db1 = null;
		String sql = "";
		
		try{
			db1 = new Db();
			
			sql = "select * from rpp_selenggara_unit_lokasi a, rpp_selenggara b "+ 
					" where a.id_selenggara = b.id "+
					" and b.flag_jenis_selenggara = 'LOKASI' "+ 
					" and a.id_peranginan = '"+idperanginan+"' "+
					" and ((b.tarikh_mula <= '"+strDateIn+"' AND b.tarikh_tamat > '"+strDateIn+"') "+  
					" OR (b.tarikh_mula < '"+strDateOut+"' AND b.tarikh_tamat >= '"+strDateOut+"') "+
					" OR (b.tarikh_mula >= '"+strDateIn+"' AND b.tarikh_tamat < '"+strDateOut+"')) ";
					
			ResultSet rs = db1.getStatement().executeQuery(sql);
			
			if (rs.next()){
				isSelenggara = true;
			}
		
		}catch(Exception e){ 
			System.out.println("checkSelenggaraSeluruhRpp : "+e.getMessage());
			e.printStackTrace();
		}finally { 
			if ( db1 != null ) db1.close();
		}
		return isSelenggara;
	}
	
	public static boolean checkingFlagPenggunaAktif(MyPersistence mp,String userId){
		boolean flagPenggunaAktif = false;
		Users user = (Users) mp.find(Users.class, userId);
		if(user!=null){
			if(user.getFlagAktif().equalsIgnoreCase("Y")){
				flagPenggunaAktif = true;
			}
		}
		return flagPenggunaAktif;
	}
	
	/***
	 * Termasuk validation selenggara
	 */
	public static int getBilanganUnitAvailable(MyPersistence mp, String idJenisUnit, String tarikhMasuk) throws Exception {
		
		Db db1 = null;
		int bil = 0;
		String sql = "";
		
		try {
			
			db1 = new Db();
			sql = "select a.id from rpp_unit a "+
						" where a.id in (select b.id from rpp_unit b where b.id_jenis_unit = '"+idJenisUnit+"' ) "+
						" and ifnull(a.status,'') <> 'RESERVED' "+
						" and a.id not in (select c.id_unit from rpp_jadual_tempahan c left join rpp_permohonan rp on c.id_permohonan = rp.id  "+
						" where c.id_unit = a.id and (c.tarikh_mula <= '"+tarikhMasuk+"' AND c.tarikh_tamat > '"+tarikhMasuk+"') ) ";
//						" and a.id not in (select d.id_unit from rpp_selenggara_unit_lokasi d, rpp_selenggara e  "+
//						" where d.id_selenggara = e.id  "+
//						" and (e.tarikh_mula <= '"+tarikhMasuk+"' and e.tarikh_tamat > '"+tarikhMasuk+"') "+
//						" and e.flag_jenis_selenggara = 'UNIT'  "+
//						" and d.id_unit = a.id) ";
			
			ResultSet rs = db1.getStatement().executeQuery(sql);
			
			while (rs.next()){
				String idUnit = rs.getString("id");
				RppUnit objUnit = (RppUnit) mp.find(RppUnit.class,idUnit);
				boolean checkingSelenggara = checkSelenggaraAvailableUnit(objUnit, tarikhMasuk);
				if( !checkingSelenggara){
					bil++;
				}
			}
				
		} finally { 
			if ( db1 != null ) db1.close();
		}

		return bil;
	}
	
	public static int getBilanganUnitAvailableByRange(MyPersistence mp, String idJenisUnit, String tarikhMasuk, String tarikhKeluar) throws Exception {
		
		Db db1 = null;
		int bil = 0;
		
		try {
			
			db1 = new Db();
			String sql = "select a.* from rpp_unit a "+
						" where a.id in (select b.id from rpp_unit b where b.id_jenis_unit = '"+idJenisUnit+"' ) "+ 
						" and ifnull(a.status,'') <> 'RESERVED' "+
						" and a.id not in (select c.id_unit from rpp_jadual_tempahan c left join rpp_permohonan rp on c.id_permohonan = rp.id  "+ 
						" where c.id_unit = a.id and ((c.tarikh_mula <= '"+tarikhMasuk+"' AND c.tarikh_tamat > '"+tarikhMasuk+"') "+
						" OR (c.tarikh_mula < '"+tarikhKeluar+"' AND c.tarikh_tamat >= '"+tarikhKeluar+"') "+ 
						" OR (c.tarikh_mula >= '"+tarikhMasuk+"' AND c.tarikh_tamat < '"+tarikhKeluar+"')) ) "+ 
						" and a.id not in (select d.id_unit from rpp_selenggara_unit_lokasi d, rpp_selenggara e "+  
						" where d.id_selenggara = e.id "+  
						" and ((e.tarikh_mula <= '"+tarikhMasuk+"' AND e.tarikh_tamat > '"+tarikhMasuk+"') "+ 
						" OR (e.tarikh_mula < '"+tarikhKeluar+"' AND e.tarikh_tamat >= '"+tarikhKeluar+"') "+ 
					    " OR (e.tarikh_mula >= '"+tarikhMasuk+"' AND e.tarikh_tamat < '"+tarikhKeluar+"')) "+
						" and e.flag_jenis_selenggara = 'UNIT' "+ 
						" and d.id_unit = a.id ) "; 
			
			ResultSet rs = db1.getStatement().executeQuery(sql);
			
			while (rs.next()){
				String idUnit = rs.getString("id");
				RppUnit objUnit = (RppUnit) mp.find(RppUnit.class,idUnit);
				boolean checkingSelenggara = checkSelenggaraAvailableUnit(objUnit, tarikhKeluar);
				if( !checkingSelenggara){
					bil++;
				}
			}
				
		} finally { 
			if ( db1 != null ) db1.close();
		}
		
		return bil;
	}
	
	public static int getBilanganUnitAvailableByRangeAndRpp(MyPersistence mp, String idrp, String tarikhMasuk, String tarikhKeluar) throws Exception {
		
		Db db1 = null;
		int bil = 0;
		
		try {
			
			db1 = new Db();
			String sql = "select a.* from rpp_unit a "+
						" where a.id in (select cx.id from rpp_peranginan ax, ruj_jenis_unit_rpp bx, rpp_unit cx where ax.id = bx.id_peranginan and bx.id = cx.id_jenis_unit and ax.id = '"+idrp+"' ) "+ 
						" and ifnull(a.status,'') <> 'RESERVED' "+
						" and a.id not in (select c.id_unit from rpp_jadual_tempahan c left join rpp_permohonan rp on c.id_permohonan = rp.id  "+ 
						" where c.id_unit = a.id and ((c.tarikh_mula <= '"+tarikhMasuk+"' AND c.tarikh_tamat > '"+tarikhMasuk+"') "+
						" OR (c.tarikh_mula < '"+tarikhKeluar+"' AND c.tarikh_tamat >= '"+tarikhKeluar+"') "+ 
						" OR (c.tarikh_mula >= '"+tarikhMasuk+"' AND c.tarikh_tamat < '"+tarikhKeluar+"')) ) "+ 
						" and a.id not in (select d.id_unit from rpp_selenggara_unit_lokasi d, rpp_selenggara e "+  
						" where d.id_selenggara = e.id "+  
						" and ((e.tarikh_mula <= '"+tarikhMasuk+"' AND e.tarikh_tamat > '"+tarikhMasuk+"') "+ 
						" OR (e.tarikh_mula < '"+tarikhKeluar+"' AND e.tarikh_tamat >= '"+tarikhKeluar+"') "+ 
					    " OR (e.tarikh_mula >= '"+tarikhMasuk+"' AND e.tarikh_tamat < '"+tarikhKeluar+"')) "+
						" and e.flag_jenis_selenggara = 'UNIT' "+ 
						" and d.id_unit = a.id ) "; 
			
			ResultSet rs = db1.getStatement().executeQuery(sql);
			
			while (rs.next()){
				String idUnit = rs.getString("id");
				RppUnit objUnit = (RppUnit) mp.find(RppUnit.class,idUnit);
				boolean checkingSelenggara = checkSelenggaraAvailableUnitRange(objUnit, tarikhKeluar, tarikhKeluar);
				if( !checkingSelenggara){
					bil++;
				}
			}
				
		} finally { 
			if ( db1 != null ) db1.close();
		}
		
		return bil;
	}
	
	public static int getBilanganApplyWithinQuota(String idJenisUnit, String tarikhMasuk) throws Exception {
		
		Db db1 = null;
		int bil = 0;
		
		try {
			
			db1 = new Db();
			String sql = "select * from rpp_jadual_tempahan c, rpp_permohonan rp "+
						" where c.id_unit in (select b.id from rpp_unit b where b.id_jenis_unit = '"+idJenisUnit+"' ) "+ 
						" and (c.tarikh_mula <= '"+tarikhMasuk+"' AND c.tarikh_tamat > '"+tarikhMasuk+"') "+ 
						" and c.id_permohonan = rp.id "+
						//" and rp.tarikh_permohonan = current_date() ";
						" and rp.tarikh_permohonan >= current_date() and rp.tarikh_permohonan <= '"+tarikhMasuk+"'  ";
			
			ResultSet rs = db1.getStatement().executeQuery(sql);
			
			while (rs.next()){
				bil++;
			}
				
		} finally { 
			if ( db1 != null ) db1.close();
		}
		
		return bil;
	}
	
	/**
	 * Create Resit
	 * Update Lejar
	 * Update Invois
	 * @throws Exception 
	 * */
	@SuppressWarnings("unchecked")
	public static void createWalkinResitSenaraiInvoisAndUpdateLejar(MyPersistence mp, RppPermohonan r, String loginId, String flag, String noResitManual, Date dtTarikhBayaran) throws Exception{
		
		KodJuruwang kodj = (KodJuruwang)mp.get("select x from KodJuruwang x where x.juruwang.id = '"+loginId+"' and x.flagJuruwang = 'IR' and x.flagAktif = 'Y' ");
		String noPusatTerima = kodj!=null?kodj.getKodPusatTerima():"XX";
		String noKodJuruwang = kodj!=null?kodj.getKodJuruwang():"10";
		String idJuruwang = kodj!=null?kodj.getId():null;
		
		String tarikhBayaran = "";
		if(dtTarikhBayaran != null){
			tarikhBayaran = new SimpleDateFormat("yyyy-MM-dd").format(dtTarikhBayaran);
		}else{
			Date paymentDate = new Date();
			tarikhBayaran = new SimpleDateFormat("yyyy-MM-dd").format(paymentDate);
		}
		
		String flagJenisResit = "";
		String noResitSewa = "";
		if(flag.equalsIgnoreCase("KAUNTER")){
			flagJenisResit = "3";
			noResitSewa = noResitManual; 
		}else{ //SISTEM
			flagJenisResit = "2";
			noResitSewa = generateReceiptNoWalkin(r,noPusatTerima,noKodJuruwang,idJuruwang); 
		}
		
		/**RESIT SEWA & KAEDAH BAYARAN*/
		String idResitSewa = UID.getUID();
		String idResitKaedahBayaranSewa = UID.getUID();
		String sqlSewa = "INSERT INTO `kew_bayaran_resit` (`id`, `id_pembayar`, `no_resit`, `tarikh_bayaran`, `flag_jenis_bayaran`, `id_juruwang`, "
				+ " `tarikh_daftar`, `id_pendaftar`, `jumlah_amaun_bayaran`, `kod_juruwang`, `flag_jenis_resit`, `id_permohonan`, `flag_void`) "
				+ " VALUES "
				+ " ('"+idResitSewa+"', '"+r.getPemohon().getId()+"', '"+noResitSewa+"', '"+tarikhBayaran+"', 'KAUNTER', '" + idJuruwang + "', "
				+ " now(), '"+loginId+"', "+r.amaunTotalSewaRpWithoutDeposit()+", '"+noKodJuruwang+"', '"+flagJenisResit+"', '"+r.getId()+"', 'T' ) ";

		String sqlKaedahBayaranSewa = "INSERT INTO `kew_resit_kaedah_bayaran` (`id`, `id_bayaran_resit`, `amaun_bayaran`, `id_mod_bayaran` ) "
				+ " VALUES "
				+ " ('"+idResitKaedahBayaranSewa+"', '"+idResitSewa+"', '"+r.amaunTotalSewaRpWithoutDeposit()+"', 'T' ) ";
	
		Db database = new Db();
	    try{
	        Statement stmt = database.getStatement();
	        stmt.executeUpdate(sqlSewa);
	        stmt.executeUpdate(sqlKaedahBayaranSewa);
	    }catch(SQLException ex){
	    	System.out.println(":: ERROR createResitSenaraiInvoisAndUpdateLejar (resit sewa & kaedah bayaran sewa) : "+ex.getMessage());
	    }finally { if ( database != null ) database.close(); }
	    
	    
	    String idResitDeposit = "";
	    String noResitDeposit = "";
	    if(flag.equalsIgnoreCase("ONLINE")){
	    	
		    /**RESIT DEPOSIT & KAEDAH BAYARAN*/
		    idResitDeposit = UID.getUID();
		    String idResitKaedahBayaranDeposit = UID.getUID();
		    noResitDeposit = generateReceiptNoWalkin(r,noPusatTerima,noKodJuruwang,idJuruwang);
		    String sqlDeposit = "INSERT INTO `kew_bayaran_resit` (`id`, `id_pembayar`, `no_resit`, `tarikh_bayaran`, `flag_jenis_bayaran`,  `id_juruwang`, "
					+ " `tarikh_daftar`, `id_pendaftar`, `jumlah_amaun_bayaran`, `kod_juruwang`, `flag_jenis_resit`, `id_permohonan`, `flag_void` ) "
					+ " VALUES "
					+ " ('"+idResitDeposit+"', '"+r.getPemohon().getId()+"', '"+noResitDeposit+"', '"+tarikhBayaran+"', 'KAUNTER', '" + idJuruwang + "', "
					+ " now(), '"+loginId+"', "+r.amaunDeposit()+", '"+noKodJuruwang+"', '1', '"+r.getId()+"', 'T' ) ";
		    
		    String sqlKaedahBayaranDeposit = "INSERT INTO `kew_resit_kaedah_bayaran` (`id`, `id_bayaran_resit`, `amaun_bayaran`, `id_mod_bayaran` ) "
					+ " VALUES "
					+ " ('"+idResitKaedahBayaranDeposit+"', '"+idResitDeposit+"', '"+r.amaunDeposit()+"', 'T' ) ";
		    
			Db databasedep = new Db();
		    try{
		        Statement stmt = databasedep.getStatement();
		        stmt.executeUpdate(sqlDeposit);
		        stmt.executeUpdate(sqlKaedahBayaranDeposit);
		    }catch(SQLException ex){
		    	System.out.println(":: ERROR createResitSenaraiInvoisAndUpdateLejar (resit deposit & kaedah bayaran sewa) : "+ex.getMessage());
		    }finally { if ( databasedep != null ) databasedep.close(); }
	    
	    }else{
	    	idResitDeposit = idResitSewa;
	    	noResitDeposit = noResitSewa;
	    }
	    
	    /**UPDATE RPP PERMOHONAN*/
	    String sqlrp = "UPDATE rpp_permohonan SET status_bayaran = 'Y' , tarikh_bayaran = '"+tarikhBayaran+"' , id_resit_sewa = '"+idResitSewa+"' , id_resit_deposit = '"+idResitDeposit+"' WHERE id = '"+r.getId()+"' ";
		Db dbs = new Db();
	    try{
	        Statement stmt = dbs.getStatement();
	        stmt.executeUpdate(sqlrp);
	    }catch(SQLException ex){
	    	System.out.println(":: ERROR createResitSenaraiInvoisAndUpdateLejar (Update permohonan) : "+ex.getMessage());
	    }finally { if ( dbs != null ) dbs.close(); }

	    
	    /**UPDATE INVOIS DAN CREATE RESITSENARAIINVOIS */
		List<RppAkaun> lejar = mp.list("select x from RppAkaun x where x.permohonan.id = '"+r.getId()+"' ");
		for(int i=0;i<lejar.size();i++){
			
			RppAkaun lj = lejar.get(i);
			
			if(lj.getKodHasil().getId().equalsIgnoreCase("72311")){
				
				//Deposit
				KewDeposit dep = (KewDeposit) mp.get("select x from KewDeposit x where x.idLejar = '"+lj.getId()+"' ");
				
				//update invois
				String sqlDep = "UPDATE kew_deposit SET flag_bayar = 'Y' , tarikh_bayaran = '"+tarikhBayaran+"', id_pendeposit = '"+r.getPemohon().getId()+"' WHERE id = '"+dep.getId()+"' ";
				 
				//create resitsenaraiinois
				String idResitSenaraiInvoisDep = UID.getUID();
				String sqlRsinv = "INSERT INTO `kew_resit_senarai_invois` (`id`, `id_deposit`, `id_bayaran_resit`, `flag_jenis_resit` ) "
						+ " VALUES "
						+ " ('"+idResitSenaraiInvoisDep+"', '"+dep.getId()+"', '"+idResitDeposit+"', 'DEPOSIT' ) ";
				
				//update rppakaun
				String sqlAkaunDep = "UPDATE rpp_akaun SET no_resit = '"+noResitDeposit+"' , tarikh_transaksi = '"+tarikhBayaran+"' , "
						+ " flag_bayar = 'Y', id_kemaskini = '"+loginId+"' , kredit = "+lj.getDebit()+", tarikh_kemaskini = now() WHERE id = '"+lj.getId()+"' ";
				
				Db dblj = new Db();
		        try{
		            Statement stmt = dblj.getStatement();
		            stmt.executeUpdate(sqlDep);
		            stmt.executeUpdate(sqlRsinv);
		            stmt.executeUpdate(sqlAkaunDep);
		        }catch(SQLException ex){
		        	System.out.println(":: ERROR update invois,deposit (deposit) & create resitsenaraiinvois : "+ex.getMessage());
		        }finally { if ( dblj != null ) dblj.close(); }
				
			}else{
				//invois
				KewInvois inv = (KewInvois) mp.get("select x from KewInvois x where x.idLejar = '"+lj.getId()+"' ");
				
				//update invois
				String sqlInvois = "UPDATE kew_invois SET kredit = "+inv.getDebit()+" , flag_bayar = 'Y', id_pembayar = '"+r.getPemohon().getId()+"' WHERE id = '"+inv.getId()+"' ";
				 
				//create resitsenaraiinois
				String idResitSenaraiInvois = UID.getUID();
				String sqlRsinv = "INSERT INTO `kew_resit_senarai_invois` (`id`, `id_invois`, `id_bayaran_resit`, `flag_jenis_resit` ) "
						+ " VALUES "
						+ " ('"+idResitSenaraiInvois+"', '"+inv.getId()+"', '"+idResitSewa+"', 'INVOIS' ) ";
				
				//update rppakaun
				String sqlAkaunSewa = "UPDATE rpp_akaun SET no_resit = '"+noResitSewa+"' , tarikh_transaksi = '"+tarikhBayaran+"' , "
						+ " flag_bayar = 'Y', id_kemaskini = '"+loginId+"' , kredit = "+lj.getDebit()+", tarikh_kemaskini = now() WHERE id = '"+lj.getId()+"' ";
				
				
				Db dblj = new Db();
		        try{
		            Statement stmt = dblj.getStatement();
		            stmt.executeUpdate(sqlInvois);
		            stmt.executeUpdate(sqlRsinv);
		            stmt.executeUpdate(sqlAkaunSewa);
		        }catch(SQLException ex){
		        	System.out.println(":: ERROR update invois,deposit (sewa) & create resitsenaraiinvois : "+ex.getMessage());
		        }finally { if ( dblj != null ) dblj.close(); }				
			}
			
		}
		
	}
	
	public static String generateReceiptNoWalkin(RppPermohonan r,String noPusatTerima,String noKodJuruwang,String idJuruwang) throws Exception{
		
		String receiptNo = "";
		
		Integer today = Integer.parseInt(Util.getCurrentDate("dd"));
		Integer month = Integer.parseInt(Util.getCurrentDate("MM"));
		Integer year = Integer.parseInt(Util.getCurrentDate("yyyy"));
		
		String date = Util.getCurrentDate("ddMMyyyy");
		int runningNo = 0;		
		
		Db dtbs = null;
		String sql = "";
		try {
			dtbs = new Db();
			sql = "select * from kew_seq_resit where day = "+today+" and month = "+month+" and year = "+year+" and id_kod_juruwang = '"+idJuruwang+"' ";
			
			ResultSet rs = dtbs.getStatement().executeQuery(sql);
			String subsql = "";
			if (rs.next()){
				//UPDATE
				String id = rs.getString("id");
				runningNo = rs.getInt("bil")+1;
				subsql = "UPDATE kew_seq_resit SET bil = "+runningNo+" WHERE id = '"+id+"' ";
			}else{
				//INSERT
				String id = UID.getUID();
				subsql = " INSERT INTO `kew_seq_resit` (`id`, `day`, `month`, `year`, `bil`, `id_kod_juruwang`) "+
						 " VALUES "+
						 " ('"+id+"', "+today+", "+month+", "+year+", 1, '"+idJuruwang+"') ";
				runningNo = 1;
			}
			
			Db dtbs2 = new Db();
			try{
	            Statement stmt = dtbs2.getStatement();
	            stmt.executeUpdate(subsql);
	        }catch(SQLException exsub){
	        	System.out.println(":: ERROR SUB Generate no resit : "+exsub.getMessage());
	        } finally { if ( dtbs2 != null ) dtbs2.close(); }
			
		} catch(SQLException ex){
			System.out.println(":: ERROR MAIN Generate no resit : "+ex.getMessage());
		}finally { if ( dtbs != null ) dtbs.close(); }
		
		
		//format : DD.MM.YYYY.<2 digit pusat penerimaan>.<kod juruwang>.<turutan nombor(3 digit)>.
		String seq = String.format("%05d",runningNo);
		receiptNo = date+""+noPusatTerima+""+noKodJuruwang+""+seq;
	
		return receiptNo;
	}
	
	
	/**
	 * EMAIL TO PEMOHON
	 * EMAIL NOTIFIKASI PERMOHONAN LONDON DILULUSKAN / TIDAK / TEMPAHAN DIBATALKAN
	 * */
	public static void emailtoPemohon(RppRekodTempahanLondon r,String flag) {
		if(!r.getPemohon().getId().equalsIgnoreCase("faizal") && ResourceBundle.getBundle("dbconnection").getString("SERVER_DEFINITION").equals("LIVE")){
			System.out.println("Send email to pemohon london.");
			String emelto = r.getPemohon().getEmel();
			if( !emelto.equalsIgnoreCase("") && emelto != null ){
				String emelcc = "rpp@bph.gov.my";
				RppMailer.get().notifikasiStatusPermohonanLondon(emelto,emelcc,r,flag);
			}
		}
	}
	
	/**
	 * EMAIL TO PMO
	 * EMAIL NOTIFIKASI PERMOHONAN BARU
	 * */
	public static void emailtoPmo(RppRekodTempahanLondon r) {
		if(!r.getPemohon().getId().equalsIgnoreCase("faizal") && ResourceBundle.getBundle("dbconnection").getString("SERVER_DEFINITION").equals("LIVE")){
			System.out.println("Send email to pmo.");
			String emelto = "siti.junaidah870@gmail.com";
			if( !emelto.equalsIgnoreCase("") && emelto != null ){
				String emelcc = "rpp@bph.gov.my";
				RppMailer.get().notifikasiPermohonanBaru(emelto,emelcc,r);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static void deletePermohonan(MyPersistence mp, RppPermohonan r){
		List<RppAkaun> lk = getListTempahanDanBayaran(mp,r);
		if(lk.size() > 0){
			for(int i=0;i<lk.size();i++){
				RppAkaun lj = lk.get(i);
	
				KewInvois inv = (KewInvois) mp.get("select x from KewInvois x where x.idLejar = '"+lj.getId()+"' ");
				if(inv!=null){ mp.remove(inv); }
				
				KewDeposit dep = (KewDeposit) mp.get("select x from KewDeposit x where x.idLejar = '"+lj.getId()+"' ");
				if(dep!=null){ mp.remove(dep); }
				
				if(lj!=null){ mp.remove(lj); }
			}
		}

		List<RppJadualTempahan> jt = mp.list("select x from RppJadualTempahan x where x.permohonan.id = '"+r.getId()+"' ");
		if(jt.size() > 0){
			for(int i=0;i<jt.size();i++){
				mp.remove(jt.get(i));
			}
		}
		
		RppPengurusanBilik b = (RppPengurusanBilik) mp.get("select x from RppPengurusanBilik x where x.permohonan.id = '"+r.getId()+"' ");
		if(b!=null){
			mp.remove(b);
		}
		
		mp.remove(r);
	}
	
	
	public static List<String> resultSearchSelenggara (MyPersistence mp, Map<String, Object> params){
		
		ArrayList<String> list = new ArrayList<String>();
		Db db1 = null;
		
		String idrp = (String) params.get("idrp");
		String idjenisUnit = (String) params.get("idjenisUnit");
		String idUnit = (String) params.get("idUnit");
		
		try {
			db1 = new Db();
			
			String sql = "select a.id from rpp_selenggara a, rpp_selenggara_unit_lokasi b "+
						" where a.id = b.id_selenggara ";
			
			if(idrp != null && !idrp.equalsIgnoreCase("")){
				sql += " and b.id_peranginan = '"+idrp+"' ";
			}
				
			if(idjenisUnit != null && !idjenisUnit.equalsIgnoreCase("")){
				sql += " and b.id_unit in (select j.id from rpp_unit j where j.id_jenis_unit = '"+idjenisUnit+"' ) ";
			}
			
			if(idUnit != null && !idUnit.equalsIgnoreCase("")){
				sql += " and b.id_unit = '"+idUnit+"' ";
			}
					
			ResultSet rs = db1.getStatement().executeQuery(sql);			
			while (rs.next()) {
				String id = rs.getString("id");
				if(!list.contains(id)){
					list.add(id);
				}
			}	
			
		}catch(Exception e){
			System.out.println("Error resultSearchSelenggara : "+e.getMessage());
		}finally { 
			if ( db1 != null ) db1.close();
		}
		return list;
	}
	
	public static void createPengurusanBilik(RppPermohonan r) throws DbException{
		
		String id = UID.getUID();
		
		String sql = "INSERT INTO `rpp_pengurusan_bilik` (`id`, `id_permohonan`, `catatan_masuk`, `tarikh_daftar_masuk`, `masa_daftar_jam`, `masa_daftar_minit`, `masa_daftar_ampm` ) "
				+ " VALUES "
				+ " ('"+id+"', '"+r.getId()+"', 'PERMOHONAN SECARA WALKIN', now(), 2, 0, 'PM' ) "; 
		
		Db database = new Db();
		try{
		    Statement stmt = database.getStatement();
		    stmt.executeUpdate(sql);
		}catch(SQLException ex){
			System.out.println(":: ERROR createPengurusanBilik : "+ex.getMessage());
		}finally { if ( database != null ) database.close(); }
		
	}	
	
	@SuppressWarnings("unchecked")
	public static void batalPermohonan(MyPersistence mp, RppPermohonan r) throws DbException{
		
		String catatan = "";
		int bil = getDayBetweenPayment(r);
		if(bil < 7){
			catatan = "PEMBAYARAN TIDAK DIBUAT DALAM MASA 48 JAM DARI TARIKH PERMOHONAN.";
		}else{
			catatan = "PEMBAYARAN TIDAK DIBUAT DALAM MASA 7 HARI DARI TARIKH PERMOHONAN.";
		}
		
		/**Update main table*/
		r.setStatus((Status) mp.find(Status.class, "1435093978588")); //status batal
		r.setTarikhPembatalan(new Date());
		r.setPemohonBatal((Users) mp.find(Users.class, "faizal")); // admin
		r.setCatatanPembatalan(catatan);
		
		/**Remove locked unit*/
		deleteChildTempahan(mp,r.getId());
		
		/**Update RppAkaun*/
		List<RppAkaun> lk = mp.list("select x from RppAkaun x where x.permohonan.id = '"+r.getId()+"' ");
		
		if(lk != null){
			for(int i=0;i<lk.size();i++){
				RppAkaun lj = lk.get(i);
	
				if(lj.getKodHasil().getId().equalsIgnoreCase("72311")){
					//deposit
					KewDeposit dep = (KewDeposit) mp.get("select x from KewDeposit x where x.idLejar = '"+lj.getId()+"' ");
					if(dep!=null){
						mp.remove(dep);
					}					
				}else{
					//invois
					KewInvois inv = (KewInvois) mp.get("select x from KewInvois x where x.idLejar = '"+lj.getId()+"' ");
					if(inv!=null){
						mp.remove(inv);
					}
				}
				
				if(lj!=null){
					lj.setAmaunVoid(lj.getDebit());
					lj.setFlagVoid("Y");
					lj.setTarikhVoid(new Date());
				}
			}
		}
	}
	
	public static int getDayBetweenPayment(RppPermohonan r){
		int days = 0;
		if(r.getTarikhAkhirBayaran() != null && r.getTarikhPermohonan() != null){
			Calendar dateMohon = Calendar.getInstance();
			dateMohon.setTime(r.getTarikhPermohonan());
			dateMohon.set(Calendar.HOUR_OF_DAY, 0);
			dateMohon.set(Calendar.MINUTE, 0);
			dateMohon.set(Calendar.SECOND, 0);
			dateMohon.set(Calendar.MILLISECOND, 0);
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(r.getTarikhAkhirBayaran());
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			
			//days = (int)( (cal.getTime() - currentDate.getTime()) / (1000 * 60 * 60 * 24) );
			days = Util.daysBetween(dateMohon.getTime(),cal.getTime());
		}
		return days;
	}

	public static int getPaymentBalanceDay(RppPermohonan r){
		int days = 0;
		if(r.getTarikhAkhirBayaran() != null){
			Calendar currentDate = Calendar.getInstance();
			currentDate.setTime(new Date());
			currentDate.set(Calendar.HOUR_OF_DAY, 0);
			currentDate.set(Calendar.MINUTE, 0);
			currentDate.set(Calendar.SECOND, 0);
			currentDate.set(Calendar.MILLISECOND, 0);
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(r.getTarikhAkhirBayaran());
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			
			//days = (int)( (cal.getTime() - currentDate.getTime()) / (1000 * 60 * 60 * 24) );
			days = Util.daysBetween(currentDate.getTime(),cal.getTime());
		}
		return days;
	}
	
	public static RppNotifikasi saveUpdateObjectNotifikasi(MyPersistence mp, RppNotifikasi n, String idrekod, String flagAktif, String moduleClass, String flagAktiviti) {
        
		if(n == null){ n = new RppNotifikasi(); }
		
        n.setIdRekod(idrekod);
        n.setFlagAktiviti(flagAktiviti);
        n.setFlagAktif(flagAktif);
        n.setModuleClass(moduleClass);
        n.setTarikhAktiviti(new Date());
        mp.persist(n);
        return n;
	}
	
	/***
	 * 
	 * aktiviti : TEMPAHAN_KELOMPOK_BARU, TEMPAHAN_PREMIER_BARU, TUNTUTAN_DEPOSIT, TEMPAHAN_LONDON_BARU
	 */
	public static void saveNotifikasi(MyPersistence mp, List<Role> ur,String idrekod, String flagAktif, String moduleClass, String flagAktiviti) {
		RppNotifikasi n = saveUpdateObjectNotifikasi(mp,null,idrekod,flagAktif,moduleClass,flagAktiviti);
		if(n!=null){
			for(int i=0;i<ur.size();i++){
				Role r = ur.get(i);
				RppUserRoleNotifikasi rn = new RppUserRoleNotifikasi();
				rn.setNotifikasi(n);
				rn.setRole(r);
				rn.setFlagPenerima("KUMPULAN");
				mp.persist(rn);
			}
			//emel
		}
	}
	
	public static void readNotification(MyPersistence mp, String idrekod, String userRole, String userId, String flagAktiviti) throws Exception {
		RppNotifikasi obj = null;
		Db dtbs = null;
		try {
			dtbs = new Db();
			String sql = "select x.id "+ 
						" from rpp_notifikasi x, rpp_user_role_notifikasi y "+
						" where x.id = y.id_notifikasi "+
						" and x.flag_aktiviti = '"+flagAktiviti+"' "+ 
						" and x.flag_aktif = 'Y' "+
						" and y.id_role = '"+userRole+"' "+
						" and x.id_rekod = '"+idrekod+"' ";
			ResultSet rs = dtbs.getStatement().executeQuery(sql);
			while (rs.next()){
				String id = rs.getString("id");
				obj = (RppNotifikasi) mp.find(RppNotifikasi.class,id);
			}
		} catch(SQLException ex){
			System.out.println("Error readNotification : "+ex.getMessage());
		}finally { if ( dtbs != null ) dtbs.close(); }	
		
		if(obj != null){
			RppTransaksiNotifikasi tn = new RppTransaksiNotifikasi();
			tn.setNotifikasi(obj);
			tn.setTarikhBuka(new Date());
			tn.setUser((Users) mp.find(Users.class, userId));
			mp.persist(tn);
			
			obj.setFlagAktif("T");
		}
		
	}
	
	
	
	
	//TEMPORARY OBJECT
	public static class SummaryRp {
			
		public String peranginanId;
		public String namaPeranginan;
		
		public SummaryRp() { }

		public String getPeranginanId() {
			return peranginanId;
		}

		public void setPeranginanId(String peranginanId) {
			this.peranginanId = peranginanId;
		}

		public String getNamaPeranginan() {
			return namaPeranginan;
		}

		public void setNamaPeranginan(String namaPeranginan) {
			this.namaPeranginan = namaPeranginan;
		}

	}
		
	
	
}












