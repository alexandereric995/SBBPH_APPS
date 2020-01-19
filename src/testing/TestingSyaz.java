package testing;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.List;

import lebah.db.Db;
import lebah.template.DbPersistence;
import bph.entities.rpp.RppPeranginan;
import bph.entities.rpp.RppPermohonan;

public class TestingSyaz {
	
	private static DbPersistence db;

	public static void main(String[] args) {
		try {
			runscriptStatistikIR();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	@SuppressWarnings("unchecked")
	private static void runscriptStatistikIR() throws IOException {		
		System.out.println("START runscript");

		String jenisPemohon = "('INDIVIDU','KELOMPOK')";
		
		String idperanginan = "1439720205023";
		String tarikhPermohonan1 = "2016-04-01";
		String tarikhPermohonan2 = "2016-05-01";
		
		Db dtbs = null;
		
		try {
			dtbs = new Db();
			db = new DbPersistence();
			
			RppPeranginan r = db.find(RppPeranginan.class, idperanginan);
			
			List<RppPermohonan> lista = db.list("select x from RppPermohonan x where x.tarikhPermohonan >= '"+tarikhPermohonan1+"' "
					+ "and x.tarikhPermohonan < '"+tarikhPermohonan2+"' and x.rppPeranginan.id = '"+idperanginan+"' and x.jenisPemohon in "+jenisPemohon+" ");
			
			List<RppPermohonan> listb = db.list("select x from RppPermohonan x where x.tarikhPermohonan >= '"+tarikhPermohonan1+"' "
					+ "and x.tarikhPermohonan < '"+tarikhPermohonan2+"' and x.flagWaktuPuncak = 'Y' and x.rppPeranginan.id = '"+idperanginan+"' and x.jenisPemohon in "+jenisPemohon+" ");
			
			List<RppPermohonan> listc = db.list("select x from RppPermohonan x where x.tarikhPermohonan >= '"+tarikhPermohonan1+"' "
					+ "and x.tarikhPermohonan < '"+tarikhPermohonan2+"' and x.flagWaktuPuncak = 'T' and x.rppPeranginan.id = '"+idperanginan+"' and x.jenisPemohon in "+jenisPemohon+" ");
			
			//bil permohonan batal semua
			List<RppPermohonan> listd = db.list("select x from RppPermohonan x where x.tarikhPermohonan >= '"+tarikhPermohonan1+"' "
					+ "and x.tarikhPermohonan < '"+tarikhPermohonan2+"' and x.rppPeranginan.id = '"+idperanginan+"' and x.jenisPemohon in "+jenisPemohon+" and x.pemohonBatal is not null ");
			
			//bil permohonan batal semua (wp)
			List<RppPermohonan> listd2 = db.list("select x from RppPermohonan x where x.tarikhPermohonan >= '"+tarikhPermohonan1+"' "
					+ "and x.tarikhPermohonan < '"+tarikhPermohonan2+"' and x.flagWaktuPuncak = 'Y' and x.rppPeranginan.id = '"+idperanginan+"' and x.jenisPemohon in "+jenisPemohon+" and x.pemohonBatal is not null ");
			
			//bil permohonan batal semua (wlp)
			List<RppPermohonan> listd3 = db.list("select x from RppPermohonan x where x.tarikhPermohonan >= '"+tarikhPermohonan1+"' "
					+ "and x.tarikhPermohonan < '"+tarikhPermohonan2+"' and x.flagWaktuPuncak = 'T' and x.rppPeranginan.id = '"+idperanginan+"' and x.jenisPemohon in "+jenisPemohon+" and x.pemohonBatal is not null ");
			
			//bil permohonan batal sistem
			List<RppPermohonan> liste = db.list("select x from RppPermohonan x where x.tarikhPermohonan >= '"+tarikhPermohonan1+"' "
					+ "and x.tarikhPermohonan < '"+tarikhPermohonan2+"' and x.rppPeranginan.id = '"+idperanginan+"' and x.jenisPemohon in "+jenisPemohon+" and x.pemohonBatal is not null and x.pemohonBatal.id = 'faizal' ");
			
			//bil permohonan batal sistem
			List<RppPermohonan> liste2 = db.list("select x from RppPermohonan x where x.tarikhPermohonan >= '"+tarikhPermohonan1+"' "
					+ "and x.tarikhPermohonan < '"+tarikhPermohonan2+"' and x.flagWaktuPuncak = 'Y' and x.rppPeranginan.id = '"+idperanginan+"' and x.jenisPemohon in "+jenisPemohon+" and x.pemohonBatal is not null and x.pemohonBatal.id = 'faizal' ");
			
			//bil permohonan batal sistem
			List<RppPermohonan> liste3 = db.list("select x from RppPermohonan x where x.tarikhPermohonan >= '"+tarikhPermohonan1+"' "
					+ "and x.tarikhPermohonan < '"+tarikhPermohonan2+"' and x.flagWaktuPuncak = 'T' and x.rppPeranginan.id = '"+idperanginan+"' and x.jenisPemohon in "+jenisPemohon+" and x.pemohonBatal is not null and x.pemohonBatal.id = 'faizal' ");
			
			//TOTAL HARGA KESELURUHAN
			String sqlA = "select sum(rp.debit)as total_harga "+
					" from rpp_permohonan a, rpp_peranginan b, users d, ruj_status e, rpp_akaun rp "+ 
					" where a.id_peranginan = b.id "+
					//" and a.id_jenis_unit_rpp = c.id "+
					" and a.id_pemohon = d.user_login "+
					" and a.id_status = e.id "+
					" and rp.id_permohonan = a.id "+
					" and a.tarikh_permohonan >= '"+tarikhPermohonan1+"' "+
					" and a.tarikh_permohonan < '"+tarikhPermohonan2+"' "+
					" and a.id_peranginan = '"+idperanginan+"' "+
					" and a.jenis_pemohon in "+jenisPemohon+" "+
					" and rp.id_kod_hasil = '74299' ";
			ResultSet rs = dtbs.getStatement().executeQuery(sqlA);
			Double kadar = 0d;
			while (rs.next()){
				kadar += rs.getDouble("total_harga");
			}
			
			//TOTAL HARGA KESELURUHAN (WP)
			String sqlB = "select sum(rp.debit)as total_harga "+
					" from rpp_permohonan a, rpp_peranginan b, users d, ruj_status e, rpp_akaun rp "+ 
					" where a.id_peranginan = b.id "+
					//" and a.id_jenis_unit_rpp = c.id "+
					" and a.id_pemohon = d.user_login "+
					" and a.id_status = e.id "+
					" and rp.id_permohonan = a.id "+
					" and a.tarikh_permohonan >= '"+tarikhPermohonan1+"' "+
					" and a.tarikh_permohonan < '"+tarikhPermohonan2+"' "+
					" and a.id_peranginan = '"+idperanginan+"' "+
					" and a.jenis_pemohon in "+jenisPemohon+" "+
					" and rp.id_kod_hasil = '74299' "+
					" and a.flag_waktu_puncak = 'Y' ";
			ResultSet rsB = dtbs.getStatement().executeQuery(sqlB);
			Double kadarB = 0d;
			while (rsB.next()){
				kadarB += rsB.getDouble("total_harga");
			}
			
			
			//TOTAL HARGA KESELURUHAN (WLP)
			String sqlC = "select sum(rp.debit)as total_harga "+
					" from rpp_permohonan a, rpp_peranginan b, users d, ruj_status e, rpp_akaun rp "+ 
					" where a.id_peranginan = b.id "+
					//" and a.id_jenis_unit_rpp = c.id "+
					" and a.id_pemohon = d.user_login "+
					" and a.id_status = e.id "+
					" and rp.id_permohonan = a.id "+
					" and a.tarikh_permohonan >= '"+tarikhPermohonan1+"' "+
					" and a.tarikh_permohonan < '"+tarikhPermohonan2+"' "+
					" and a.id_peranginan = '"+idperanginan+"' "+
					" and a.jenis_pemohon in "+jenisPemohon+" "+
					" and rp.id_kod_hasil = '74299' "+
					" and a.flag_waktu_puncak = 'T' ";
			ResultSet rsC = dtbs.getStatement().executeQuery(sqlC);
			Double kadarC = 0d;
			while (rsC.next()){
				kadarC += rsC.getDouble("total_harga");
			}
			
			
			//////////////
			
			
			//TOTAL HARGA KESELURUHAN (SELESAI)
			String sqlD = "select sum(rp.debit)as total_harga "+
					" from rpp_permohonan a, rpp_peranginan b, users d, ruj_status e, rpp_akaun rp "+ 
					" where a.id_peranginan = b.id "+
					//" and a.id_jenis_unit_rpp = c.id "+
					" and a.id_pemohon = d.user_login "+
					" and a.id_status = e.id "+
					" and rp.id_permohonan = a.id "+
					" and a.tarikh_permohonan >= '"+tarikhPermohonan1+"' "+
					" and a.tarikh_permohonan < '"+tarikhPermohonan2+"' "+
					" and a.id_peranginan = '"+idperanginan+"' "+
					" and a.jenis_pemohon in "+jenisPemohon+" "+
					" and rp.id_kod_hasil = '74299' "+
/**TODO*/			//" and a.id_status = '1435512646303' ";
					" and a.status_bayaran = 'Y' ";
			ResultSet rsD = dtbs.getStatement().executeQuery(sqlD);
			Double kadarD = 0d;
			while (rsD.next()){
				kadarD += rsD.getDouble("total_harga");
			}
			
			//TOTAL HARGA KESELURUHAN WALKIN
			String sqlDw = "select sum(rp.debit)as total_harga "+
					" from rpp_permohonan a, rpp_peranginan b, users d, ruj_status e, rpp_akaun rp "+ 
					" where a.id_peranginan = b.id "+
					//" and a.id_jenis_unit_rpp = c.id "+
					" and a.id_pemohon = d.user_login "+
					" and a.id_status = e.id "+
					" and rp.id_permohonan = a.id "+
					" and a.tarikh_permohonan >= '"+tarikhPermohonan1+"' "+
					" and a.tarikh_permohonan < '"+tarikhPermohonan2+"' "+
					" and a.id_peranginan = '"+idperanginan+"' "+
					" and a.jenis_pemohon in "+jenisPemohon+" "+
					" and rp.id_kod_hasil = '74299' "+
/**TODO*/			//" and a.id_status = '1435512646303' ";
					" and a.status_bayaran = 'Y' "+
					" and a.jenis_permohonan = 'WALKIN' ";
			ResultSet rsDw = dtbs.getStatement().executeQuery(sqlDw);
			Double kadarDw = 0d;
			while (rsDw.next()){
				kadarDw += rsDw.getDouble("total_harga");
			}
			
			//TOTAL HARGA KESELURUHAN (WP) (SELESAI)
			String sqlE = "select sum(rp.debit)as total_harga "+
					" from rpp_permohonan a, rpp_peranginan b, users d, ruj_status e, rpp_akaun rp "+ 
					" where a.id_peranginan = b.id "+
					//" and a.id_jenis_unit_rpp = c.id "+
					" and a.id_pemohon = d.user_login "+
					" and a.id_status = e.id "+
					" and rp.id_permohonan = a.id "+
					" and a.tarikh_permohonan >= '"+tarikhPermohonan1+"' "+
					" and a.tarikh_permohonan < '"+tarikhPermohonan2+"' "+
					" and a.id_peranginan = '"+idperanginan+"' "+
					" and a.jenis_pemohon in "+jenisPemohon+" "+
					" and rp.id_kod_hasil = '74299' "+
					" and a.flag_waktu_puncak = 'Y' "+
/**TODO*/			//" and a.id_status = '1435512646303' ";
					" and a.status_bayaran = 'Y' ";
			ResultSet rsE = dtbs.getStatement().executeQuery(sqlE);
			Double kadarE = 0d;
			while (rsE.next()){
				kadarE += rsE.getDouble("total_harga");
			}
			
			//TOTAL HARGA KESELURUHAN (WP) WALKIN
			String sqlEw = "select sum(rp.debit)as total_harga "+
					" from rpp_permohonan a, rpp_peranginan b, users d, ruj_status e, rpp_akaun rp "+ 
					" where a.id_peranginan = b.id "+
					//" and a.id_jenis_unit_rpp = c.id "+
					" and a.id_pemohon = d.user_login "+
					" and a.id_status = e.id "+
					" and rp.id_permohonan = a.id "+
					" and a.tarikh_permohonan >= '"+tarikhPermohonan1+"' "+
					" and a.tarikh_permohonan < '"+tarikhPermohonan2+"' "+
					" and a.id_peranginan = '"+idperanginan+"' "+
					" and a.jenis_pemohon in "+jenisPemohon+" "+
					" and rp.id_kod_hasil = '74299' "+
					" and a.flag_waktu_puncak = 'Y' "+
/**TODO*/			//" and a.id_status = '1435512646303' ";
					" and a.status_bayaran = 'Y' "+
					" and a.jenis_permohonan = 'WALKIN' ";
			ResultSet rsEw = dtbs.getStatement().executeQuery(sqlEw);
			Double kadarEw = 0d;
			while (rsEw.next()){
				kadarEw += rsEw.getDouble("total_harga");
			}
			
			
			//TOTAL HARGA KESELURUHAN (WLP) SELESAI
			String sqlF = "select sum(rp.debit)as total_harga "+
					" from rpp_permohonan a, rpp_peranginan b, users d, ruj_status e, rpp_akaun rp "+ 
					" where a.id_peranginan = b.id "+
					//" and a.id_jenis_unit_rpp = c.id "+
					" and a.id_pemohon = d.user_login "+
					" and a.id_status = e.id "+
					" and rp.id_permohonan = a.id "+
					" and a.tarikh_permohonan >= '"+tarikhPermohonan1+"' "+
					" and a.tarikh_permohonan < '"+tarikhPermohonan2+"' "+
					" and a.id_peranginan = '"+idperanginan+"' "+
					" and a.jenis_pemohon in "+jenisPemohon+" "+
					" and rp.id_kod_hasil = '74299' "+
					" and a.flag_waktu_puncak = 'T' "+
/**TODO*/			//" and a.id_status = '1435512646303' ";
					" and a.status_bayaran = 'Y' ";
			ResultSet rsF = dtbs.getStatement().executeQuery(sqlF);
			Double kadarF = 0d;
			while (rsF.next()){
				kadarF += rsF.getDouble("total_harga");
			}
			
			
			//TOTAL HARGA KESELURUHAN (WLP) WALKIN
			String sqlFw = "select sum(rp.debit)as total_harga "+
					" from rpp_permohonan a, rpp_peranginan b, users d, ruj_status e, rpp_akaun rp "+ 
					" where a.id_peranginan = b.id "+
					//" and a.id_jenis_unit_rpp = c.id "+
					" and a.id_pemohon = d.user_login "+
					" and a.id_status = e.id "+
					" and rp.id_permohonan = a.id "+
					" and a.tarikh_permohonan >= '"+tarikhPermohonan1+"' "+
					" and a.tarikh_permohonan < '"+tarikhPermohonan2+"' "+
					" and a.id_peranginan = '"+idperanginan+"' "+
					" and a.jenis_pemohon in "+jenisPemohon+" "+
					" and rp.id_kod_hasil = '74299' "+
					" and a.flag_waktu_puncak = 'T' "+
/**TODO*/			//" and a.id_status = '1435512646303' ";
					" and a.status_bayaran = 'Y' "+
					" and a.jenis_permohonan = 'WALKIN' ";
			ResultSet rsFw = dtbs.getStatement().executeQuery(sqlFw);
			Double kadarFw = 0d;
			while (rsFw.next()){
				kadarFw += rsFw.getDouble("total_harga");
			}
			
			
			///////////
			
			
			//TOTAL BIL HARI DITEMPAH
			String sqlG = "select sum((select DATEDIFF(a1.tarikh_keluar_rpp,a1.tarikh_masuk_rpp) from rpp_permohonan a1 where a1.id = a.id))as bil_malam_keseluruhan, "+
					" sum((select DATEDIFF(a1.tarikh_keluar_rpp,a1.tarikh_masuk_rpp) from rpp_permohonan a1 where a1.id = a.id and a1.flag_waktu_puncak = 'Y'))as bil_malam_keseluruhan_wp, "+
					" sum((select DATEDIFF(a1.tarikh_keluar_rpp,a1.tarikh_masuk_rpp) from rpp_permohonan a1 where a1.id = a.id and a1.flag_waktu_puncak = 'T'))as bil_malam_keseluruhan_wlp, "+
//					" sum((select DATEDIFF(a1.tarikh_keluar_rpp,a1.tarikh_masuk_rpp) from rpp_permohonan a1 where a1.id = a.id and a1.id_status = '1435512646303'))as bil_malam_keseluruhan_selesai, "+
//					" sum((select DATEDIFF(a1.tarikh_keluar_rpp,a1.tarikh_masuk_rpp) from rpp_permohonan a1 where a1.id = a.id and a1.flag_waktu_puncak = 'Y' and a1.id_status = '1435512646303'))as bil_malam_keseluruhan_wp_selesai, "+
//					" sum((select DATEDIFF(a1.tarikh_keluar_rpp,a1.tarikh_masuk_rpp) from rpp_permohonan a1 where a1.id = a.id and a1.flag_waktu_puncak = 'T' and a1.id_status = '1435512646303'))as bil_malam_keseluruhan_wlp_selesai "+
					
/**TODO*/
					" sum((select DATEDIFF(a1.tarikh_keluar_rpp,a1.tarikh_masuk_rpp) from rpp_permohonan a1 where a1.id = a.id and a1.status_bayaran = 'Y' ))as bil_malam_keseluruhan_selesai, "+
					" sum((select DATEDIFF(a1.tarikh_keluar_rpp,a1.tarikh_masuk_rpp) from rpp_permohonan a1 where a1.id = a.id and a1.flag_waktu_puncak = 'Y' and a1.status_bayaran = 'Y' ))as bil_malam_keseluruhan_wp_selesai, "+
					" sum((select DATEDIFF(a1.tarikh_keluar_rpp,a1.tarikh_masuk_rpp) from rpp_permohonan a1 where a1.id = a.id and a1.flag_waktu_puncak = 'T' and a1.status_bayaran = 'Y' ))as bil_malam_keseluruhan_wlp_selesai, "+
					
					" sum((select DATEDIFF(a1.tarikh_keluar_rpp,a1.tarikh_masuk_rpp) from rpp_permohonan a1 where a1.id = a.id and a1.jenis_permohonan = 'WALKIN'))as bil_malam_keseluruhan_walkin, "+
					" sum((select DATEDIFF(a1.tarikh_keluar_rpp,a1.tarikh_masuk_rpp) from rpp_permohonan a1 where a1.id = a.id and a1.flag_waktu_puncak = 'Y' and a1.jenis_permohonan = 'WALKIN'))as bil_malam_keseluruhan_wp_walkin, "+
					" sum((select DATEDIFF(a1.tarikh_keluar_rpp,a1.tarikh_masuk_rpp) from rpp_permohonan a1 where a1.id = a.id and a1.flag_waktu_puncak = 'T' and a1.jenis_permohonan = 'WALKIN'))as bil_malam_keseluruhan_wlp_walkin "+

					" from rpp_permohonan a, rpp_peranginan b, users d, ruj_status e "+ 
					" where a.id_peranginan = b.id "+
					//" and a.id_jenis_unit_rpp = c.id "+
					" and a.id_pemohon = d.user_login "+
					" and a.id_status = e.id "+
					" and a.jenis_pemohon in "+jenisPemohon+" "+
					" and a.tarikh_permohonan >= '"+tarikhPermohonan1+"' "+
					" and a.tarikh_permohonan < '"+tarikhPermohonan2+"' "+
					" and a.id_peranginan = '"+idperanginan+"' ";
			ResultSet rsG = dtbs.getStatement().executeQuery(sqlG);
			int bilMalamAll = 0;
			int bilMalamAllSelesai = 0;
			int bilMalamWP = 0;
			int bilMalamWLP = 0;
			int bilMalamWPSelesai = 0;
			int bilMalamWLPSelesai = 0;
			int bilMalamAllWalkin = 0;
			int bilMalamWPWalkin = 0;
			int bilMalamWLPWalkin = 0;
			while (rsG.next()){
				bilMalamAll += rsG.getInt("bil_malam_keseluruhan");
				bilMalamAllSelesai += rsG.getInt("bil_malam_keseluruhan_selesai");
				bilMalamWP += rsG.getInt("bil_malam_keseluruhan_wp");
				bilMalamWLP += rsG.getInt("bil_malam_keseluruhan_wlp");
				bilMalamWPSelesai += rsG.getInt("bil_malam_keseluruhan_wp_selesai");
				bilMalamWLPSelesai += rsG.getInt("bil_malam_keseluruhan_wlp_selesai");
				
				bilMalamAllWalkin += rsG.getInt("bil_malam_keseluruhan_walkin");
				bilMalamWPWalkin += rsG.getInt("bil_malam_keseluruhan_wp_walkin");
				bilMalamWLPWalkin += rsG.getInt("bil_malam_keseluruhan_wlp_walkin");
			}
			
			
			//TOTAL BIL HARI DITEMPAH BY JENIS UNIT
			String sqlH = "select c.keterangan, sum((select DATEDIFF(a1.tarikh_keluar_rpp,a1.tarikh_masuk_rpp) from rpp_permohonan a1 where a1.id = a.id))as bil_malam_keseluruhan, "+
					" sum((select DATEDIFF(a1.tarikh_keluar_rpp,a1.tarikh_masuk_rpp) from rpp_permohonan a1 where a1.id = a.id and a1.flag_waktu_puncak = 'Y'))as bil_malam_keseluruhan_wp, "+
					" sum((select DATEDIFF(a1.tarikh_keluar_rpp,a1.tarikh_masuk_rpp) from rpp_permohonan a1 where a1.id = a.id and a1.flag_waktu_puncak = 'T'))as bil_malam_keseluruhan_wlp, "+
//					" sum((select DATEDIFF(a1.tarikh_keluar_rpp,a1.tarikh_masuk_rpp) from rpp_permohonan a1 where a1.id = a.id and a1.id_status = '1435512646303'))as bil_malam_keseluruhan_selesai, "+
//					" sum((select DATEDIFF(a1.tarikh_keluar_rpp,a1.tarikh_masuk_rpp) from rpp_permohonan a1 where a1.id = a.id and a1.flag_waktu_puncak = 'Y' and a1.id_status = '1435512646303'))as bil_malam_keseluruhan_wp_selesai, "+
//					" sum((select DATEDIFF(a1.tarikh_keluar_rpp,a1.tarikh_masuk_rpp) from rpp_permohonan a1 where a1.id = a.id and a1.flag_waktu_puncak = 'T' and a1.id_status = '1435512646303'))as bil_malam_keseluruhan_wlp_selesai "+
/**TODO*/
					" sum((select DATEDIFF(a1.tarikh_keluar_rpp,a1.tarikh_masuk_rpp) from rpp_permohonan a1 where a1.id = a.id and a1.status_bayaran = 'Y' ))as bil_malam_keseluruhan_selesai, "+
					" sum((select DATEDIFF(a1.tarikh_keluar_rpp,a1.tarikh_masuk_rpp) from rpp_permohonan a1 where a1.id = a.id and a1.flag_waktu_puncak = 'Y' and a1.status_bayaran = 'Y' ))as bil_malam_keseluruhan_wp_selesai, "+
					" sum((select DATEDIFF(a1.tarikh_keluar_rpp,a1.tarikh_masuk_rpp) from rpp_permohonan a1 where a1.id = a.id and a1.flag_waktu_puncak = 'T' and a1.status_bayaran = 'Y' ))as bil_malam_keseluruhan_wlp_selesai "+
					
					" from rpp_permohonan a, rpp_peranginan b, ruj_jenis_unit_rpp c, users d, ruj_status e "+ 
					" where a.id_peranginan = b.id "+
					" and a.id_jenis_unit_rpp = c.id "+
					" and a.id_pemohon = d.user_login "+
					" and a.id_status = e.id "+
					" and a.tarikh_permohonan >= '"+tarikhPermohonan1+"' "+
					" and a.tarikh_permohonan < '"+tarikhPermohonan2+"' "+
					" and a.id_peranginan = '"+idperanginan+"' "+
					" and a.jenis_pemohon in "+jenisPemohon+" "+
					" group by c.keterangan ";
			
			
			//TOTAL HARGA KESELURUHAN (SELESAI) BY JENIS UNIT
			String sqlI = "select c.keterangan,"+
					" sum((select rp1.debit from rpp_akaun rp1 where rp1.id = rp.id))as total_harga, "+
					" sum((select rp1.debit from rpp_akaun rp1, rpp_permohonan a1 where rp1.id = rp.id and rp1.id_permohonan = a1.id and a1.flag_waktu_puncak = 'Y'))as total_harga_wp, "+
					" sum((select rp1.debit from rpp_akaun rp1, rpp_permohonan a1 where rp1.id = rp.id and rp1.id_permohonan = a1.id and a1.flag_waktu_puncak = 'T'))as total_harga_wlp "+
					" from rpp_permohonan a, rpp_peranginan b, ruj_jenis_unit_rpp c, users d, ruj_status e, rpp_akaun rp "+ 
					" where a.id_peranginan = b.id "+
					" and a.id_jenis_unit_rpp = c.id "+
					" and a.id_pemohon = d.user_login "+
					" and a.id_status = e.id "+
					" and rp.id_permohonan = a.id "+
					" and a.tarikh_permohonan >= '"+tarikhPermohonan1+"' "+
					" and a.tarikh_permohonan < '"+tarikhPermohonan2+"' "+
					" and a.id_peranginan = '"+idperanginan+"' "+
					" and a.jenis_pemohon in "+jenisPemohon+" "+
					" and rp.id_kod_hasil = '74299' "+
//					" and a.id_status = '1435512646303' "+
/**TODO**/			
					" and a.status_bayaran = 'Y' "+
					" group by c.keterangan ";
			
			
			
			//TOTAL BIL HARI DITEMPAH BY JENIS UNIT (WALKIN)
			String sqlHw = "select c.keterangan, sum((select DATEDIFF(a1.tarikh_keluar_rpp,a1.tarikh_masuk_rpp) from rpp_permohonan a1 where a1.id = a.id))as bil_malam_keseluruhan, "+
					" sum((select DATEDIFF(a1.tarikh_keluar_rpp,a1.tarikh_masuk_rpp) from rpp_permohonan a1 where a1.id = a.id and a1.flag_waktu_puncak = 'Y'))as bil_malam_keseluruhan_wp, "+
					" sum((select DATEDIFF(a1.tarikh_keluar_rpp,a1.tarikh_masuk_rpp) from rpp_permohonan a1 where a1.id = a.id and a1.flag_waktu_puncak = 'T'))as bil_malam_keseluruhan_wlp, "+
					" sum((select DATEDIFF(a1.tarikh_keluar_rpp,a1.tarikh_masuk_rpp) from rpp_permohonan a1 where a1.id = a.id and a1.status_bayaran = 'Y' ))as bil_malam_keseluruhan_selesai, "+
					" sum((select DATEDIFF(a1.tarikh_keluar_rpp,a1.tarikh_masuk_rpp) from rpp_permohonan a1 where a1.id = a.id and a1.flag_waktu_puncak = 'Y' and a1.status_bayaran = 'Y' ))as bil_malam_keseluruhan_wp_selesai, "+
					" sum((select DATEDIFF(a1.tarikh_keluar_rpp,a1.tarikh_masuk_rpp) from rpp_permohonan a1 where a1.id = a.id and a1.flag_waktu_puncak = 'T' and a1.status_bayaran = 'Y' ))as bil_malam_keseluruhan_wlp_selesai "+
					
					" from rpp_permohonan a, rpp_peranginan b, ruj_jenis_unit_rpp c, users d, ruj_status e "+ 
					" where a.id_peranginan = b.id "+
					" and a.id_jenis_unit_rpp = c.id "+
					" and a.id_pemohon = d.user_login "+
					" and a.id_status = e.id "+
					" and a.tarikh_permohonan >= '"+tarikhPermohonan1+"' "+
					" and a.tarikh_permohonan < '"+tarikhPermohonan2+"' "+
					" and a.id_peranginan = '"+idperanginan+"' "+
					" and a.jenis_pemohon in "+jenisPemohon+" "+
					" and a.jenis_permohonan = 'WALKIN' "+
					" group by c.keterangan ";
			
			
			//TOTAL HARGA KESELURUHAN (SELESAI) BY JENIS UNIT (WALKIN)
			String sqlIw = "select c.keterangan,"+
					" sum((select rp1.debit from rpp_akaun rp1 where rp1.id = rp.id))as total_harga, "+
					" sum((select rp1.debit from rpp_akaun rp1, rpp_permohonan a1 where rp1.id = rp.id and rp1.id_permohonan = a1.id and a1.flag_waktu_puncak = 'Y'))as total_harga_wp, "+
					" sum((select rp1.debit from rpp_akaun rp1, rpp_permohonan a1 where rp1.id = rp.id and rp1.id_permohonan = a1.id and a1.flag_waktu_puncak = 'T'))as total_harga_wlp "+
					" from rpp_permohonan a, rpp_peranginan b, ruj_jenis_unit_rpp c, users d, ruj_status e, rpp_akaun rp "+ 
					" where a.id_peranginan = b.id "+
					" and a.id_jenis_unit_rpp = c.id "+
					" and a.id_pemohon = d.user_login "+
					" and a.id_status = e.id "+
					" and rp.id_permohonan = a.id "+
					" and a.tarikh_permohonan >= '"+tarikhPermohonan1+"' "+
					" and a.tarikh_permohonan < '"+tarikhPermohonan2+"' "+
					" and a.id_peranginan = '"+idperanginan+"' "+
					" and a.jenis_pemohon in "+jenisPemohon+" "+
					" and rp.id_kod_hasil = '74299' "+
					" and a.status_bayaran = 'Y' "+
					" and a.jenis_permohonan = 'WALKIN' "+
					" group by c.keterangan ";
			
			
			//BIL BATAL BY JENIS UNIT - SISTEM
			String sqlJ = "select c.keterangan, count(a.id_pemohon_batal)as bil, "+
					" sum((select count(a1.id_pemohon_batal) from rpp_permohonan a1 where a.id = a1.id and a1.flag_waktu_puncak = 'Y'))as bil_batal_wp, "+
					" sum((select count(a1.id_pemohon_batal) from rpp_permohonan a1 where a.id = a1.id and a1.flag_waktu_puncak = 'T'))as bil_batal_wlp "+
					" from rpp_permohonan a, rpp_peranginan b, ruj_jenis_unit_rpp c, users d, ruj_status e "+
					" where a.id_peranginan = b.id "+
					" and a.id_jenis_unit_rpp = c.id "+
					" and a.id_pemohon = d.user_login "+
					" and a.id_status = e.id "+
					" and a.tarikh_permohonan >= '"+tarikhPermohonan1+"' "+
					" and a.tarikh_permohonan < '"+tarikhPermohonan2+"' "+
					" and a.id_peranginan = '"+idperanginan+"' "+
					" and a.jenis_pemohon in "+jenisPemohon+" "+
					" and a.id_pemohon_batal is not null "+
					" and a.id_pemohon_batal = 'faizal' "+
					" group by c.keterangan ";
			
			String sqlK = "select c.keterangan, count(a.id_pemohon_batal)as bil, "+
					" sum((select count(a1.id_pemohon_batal) from rpp_permohonan a1 where a.id = a1.id and a1.flag_waktu_puncak = 'Y'))as bil_batal_wp, "+
					" sum((select count(a1.id_pemohon_batal) from rpp_permohonan a1 where a.id = a1.id and a1.flag_waktu_puncak = 'T'))as bil_batal_wlp "+
					" from rpp_permohonan a, rpp_peranginan b, ruj_jenis_unit_rpp c, users d, ruj_status e "+
					" where a.id_peranginan = b.id "+
					" and a.id_jenis_unit_rpp = c.id "+
					" and a.id_pemohon = d.user_login "+
					" and a.id_status = e.id "+
					" and a.tarikh_permohonan >= '"+tarikhPermohonan1+"' "+
					" and a.tarikh_permohonan < '"+tarikhPermohonan2+"' "+
					" and a.id_peranginan = '"+idperanginan+"' "+
					" and a.jenis_pemohon in "+jenisPemohon+" "+
					" and a.id_pemohon_batal is not null "+
					" and a.id_pemohon_batal <> 'faizal' "+
					" group by c.keterangan ";
			
			System.out.println("___________________________");
			System.out.println("Nama Peranginan : "+r.getNamaPeranginan().toUpperCase());
			System.out.println("Tarikh mula : "+tarikhPermohonan1+ "  Sehingga : "+tarikhPermohonan2);
			
			System.out.println("___________________________");
			System.out.println("Bil Permohonan 1 bulan : "+lista.size());
			System.out.println("Bil Permohonan 1 bulan (WP) : "+listb.size());
			System.out.println("Bil Permohonan 1 bulan (WLP) : "+listc.size());
			
			System.out.println("___________________________");
			System.out.println("Total harga Keseluruhan : "+kadar);
			System.out.println("Total harga Keseluruhan (WP) : "+kadarB);
			System.out.println("Total harga Keseluruhan (WLP) : "+kadarC);
			
			System.out.println("___________________________");
			System.out.println("Total bil malam keseluruhan : "+bilMalamAll);
			System.out.println("Total bil malam keseluruhan (WP) : "+bilMalamWP);
			System.out.println("Total bil malam keseluruhan (WLP) : "+bilMalamWLP);
			
			System.out.println("___________________________");
			System.out.println("Total bil malam SELESAI : "+bilMalamAllSelesai);
			System.out.println("Total bil malam SELESAI (WP) : "+bilMalamWPSelesai);
			System.out.println("Total bil malam SELESAI (WLP) : "+bilMalamWLPSelesai);
			
			System.out.println("___________________________");
			System.out.println("Total harga SELESAI : "+kadarD);
			System.out.println("Total harga SELESAI (WP) : "+kadarE);
			System.out.println("Total harga SELESAI (WLP) : "+kadarF);
			
			System.out.println("___________________________");
			System.out.println("Total bil malam WALKIN : "+bilMalamAllWalkin);
			System.out.println("Total bil malam WALKIN (WP) : "+bilMalamWPWalkin);
			System.out.println("Total bil malam WALKIN (WLP) : "+bilMalamWLPWalkin);
			
			System.out.println("___________________________");
			System.out.println("Total harga WALKIN : "+kadarDw);
			System.out.println("Total harga WALKIN (WP) : "+kadarEw);
			System.out.println("Total harga WALKIN (WLP) : "+kadarFw);
			
			System.out.println("___________________________");
			
			ResultSet rsH = dtbs.getStatement().executeQuery(sqlH);
			while (rsH.next()){
				System.out.println("Jenis unit : "+rsH.getString("keterangan") + 
						//" Bil Malam : "+rsH.getString("bil_malam_keseluruhan")+ 
						//" Bil Malam (WP) : "+rsH.getString("bil_malam_keseluruhan_wp")+ " Bil Malam (WLP) : "+rsH.getString("bil_malam_keseluruhan_wlp")+ 
						" Bil Malam Selesai : "+rsH.getInt("bil_malam_keseluruhan_selesai")+ 
						" | Bil Malam Selesai(WP) : "+ rsH.getInt("bil_malam_keseluruhan_wp_selesai")+ " | Bil Malam Selesai (WLP) : "+rsH.getInt("bil_malam_keseluruhan_wlp_selesai") );
			}
			
			System.out.println("___________________________");
			
			ResultSet rsI = dtbs.getStatement().executeQuery(sqlI);
			while (rsI.next()){
				System.out.println("Jenis unit : "+rsI.getString("keterangan") + 
						" Total Income Selesai : "+rsI.getDouble("total_harga")+ 
						" | Total Income Selesai (WP) : "+ rsI.getDouble("total_harga_wp")+ " | Total Income Selesai (WLP) : "+rsI.getDouble("total_harga_wlp") );
			}
			
			System.out.println("___________________________");
			
			ResultSet rsHw = dtbs.getStatement().executeQuery(sqlHw);
			while (rsHw.next()){
				System.out.println("Jenis unit : "+rsHw.getString("keterangan") + 
						" Bil Malam Walkin : "+rsHw.getInt("bil_malam_keseluruhan_selesai")+ 
						" | Bil Malam Walkin(WP) : "+ rsHw.getInt("bil_malam_keseluruhan_wp_selesai")+ " | Bil Malam Walkin (WLP) : "+rsHw.getInt("bil_malam_keseluruhan_wlp_selesai") );
			}
			
			System.out.println("___________________________");
			
			ResultSet rsIw = dtbs.getStatement().executeQuery(sqlIw);
			while (rsIw.next()){
				System.out.println("Jenis unit : "+rsIw.getString("keterangan") + 
						" Total Income Walkin : "+rsIw.getDouble("total_harga")+ 
						" | Total Income Walkin (WP) : "+ rsIw.getDouble("total_harga_wp")+ " | Total Income Walkin (WLP) : "+rsIw.getDouble("total_harga_wlp") );
			}
			
			
			System.out.println("___________________________");
			System.out.println("Bil permohonan batal keseluruhan : "+listd.size());
			System.out.println("Bil permohonan batal keseluruhan (WP) : "+listd2.size());
			System.out.println("Bil permohonan batal keseluruhan (WLP) : "+listd3.size());
			System.out.println("Bil permohonan batal individu : "+(listd.size() - liste.size()));
			System.out.println("Bil permohonan batal individu (WP) : "+(listd2.size() - liste2.size()));
			System.out.println("Bil permohonan batal individu (WLP) : "+(listd3.size() - liste3.size()));
			System.out.println("Bil permohonan batal sistem : "+liste.size());
			System.out.println("Bil permohonan batal sistem (WP) : "+liste2.size());
			System.out.println("Bil permohonan batal sistem (WLP) : "+liste3.size());
			
			
			System.out.println("___________________________");
			
			ResultSet rsK = dtbs.getStatement().executeQuery(sqlK);
			while (rsK.next()){
				System.out.println("Jenis unit : "+rsK.getString("keterangan") + " Total Batal Individu : "+rsK.getString("bil") +
						" | WP : "+rsK.getString("bil_batal_wp") + " | WLP : "+rsK.getString("bil_batal_wlp") );
			}
			
			System.out.println("___________________________");
			
			ResultSet rsJ = dtbs.getStatement().executeQuery(sqlJ);
			while (rsJ.next()){
				System.out.println("Jenis unit : "+rsJ.getString("keterangan") + " Total Batal Sistem : "+rsJ.getString("bil") +
						" | WP : "+rsJ.getString("bil_batal_wp") + " | WLP : "+rsJ.getString("bil_batal_wlp") );
			}
			
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally { if ( dtbs != null ) dtbs.close(); }
		
		System.out.println("___________________________");
		System.out.println("END runscript");
	}
}
