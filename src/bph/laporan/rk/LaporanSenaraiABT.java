package bph.laporan.rk;

import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lebah.db.Db;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class LaporanSenaraiABT extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(LaporanSenaraiABT.class);
	public LaporanSenaraiABT() {		
		
		super.setFolderName("rk");
		super.setReportName("LaporanSenaraiABT");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
		
		if (parameters.get("TARIKH") != null) {
			String tarikh = (String) parameters.get("TARIKH");
			String idSeksyen = (String) parameters.get("ID_SEKSYEN");
			
			Calendar calSemasa = new GregorianCalendar();
			Calendar calSebelum = new GregorianCalendar();
			
			calSemasa.set(Calendar.DATE, Integer.valueOf(tarikh.substring(0,2)));
			calSemasa.set(Calendar.MONTH, Integer.valueOf(tarikh.substring(3,5)) - 1);
			calSemasa.set(Calendar.YEAR, Integer.valueOf(tarikh.substring(6,10)));
			
			calSebelum.setTime(calSemasa.getTime());
			calSebelum.set(Calendar.DATE, 1);
			calSebelum.add(Calendar.DATE, -1);
			
			// SET ALL THE PARAMETERS
			String tarikhSemasa = new DecimalFormat("00").format(calSemasa.get(Calendar.DATE)) + "-" + new DecimalFormat("00").format(calSemasa.get(Calendar.MONTH) + 1) + "-" + new DecimalFormat("0000").format(calSemasa.get(Calendar.YEAR));
			String tarikhSebelum = new DecimalFormat("00").format(calSebelum.get(Calendar.DATE)) + "-" + new DecimalFormat("00").format(calSebelum.get(Calendar.MONTH) + 1) + "-" + new DecimalFormat("0000").format(calSebelum.get(Calendar.YEAR));
			parameters.put("TARIKH_SEMASA", tarikhSemasa);
			parameters.put("TARIKH_SEBELUM", tarikhSebelum);
			parameters.put("BULAN_SEMASA", getKeteranganBulan(calSemasa));
			parameters.put("BULAN_SEBELUM", getKeteranganBulan(calSebelum));
			
			Double bakiKASebelum = getBaki(tarikhSebelum, idSeksyen, true);
			Double bakiKASemasa = getBaki(tarikhSemasa, idSeksyen, true);
			Double bakiKTSebelum = getBaki(tarikhSebelum, idSeksyen, false);
			Double bakiKTSemasa = getBaki(tarikhSemasa, idSeksyen, false);
			parameters.put("BAKI_KA_SEBELUM", bakiKASebelum);
			parameters.put("BAKI_KA_SEMASA", bakiKASemasa);
			parameters.put("BAKI_KT_SEBELUM", bakiKTSebelum);
			parameters.put("BAKI_KT_SEMASA", bakiKTSemasa);
			
			Double jumlahBakiTunggakanSebelum = bakiKASebelum + bakiKTSebelum;
			Double jumlahBakiTunggakanSemasa = bakiKASemasa + bakiKTSemasa;
			Double bezaKA = bakiKASemasa - bakiKASebelum;
			Double bezaKT = bakiKTSemasa - bakiKTSebelum;
			Double jumlahBeza = bezaKA + bezaKT;
			parameters.put("JUMLAH_BAKI_TUNGGAKAN_SEBELUM", jumlahBakiTunggakanSebelum);
			parameters.put("JUMLAH_BAKI_TUNGGAKAN_SEMASA", jumlahBakiTunggakanSemasa);
			parameters.put("BEZA_KA", bezaKA);
			parameters.put("BEZA_KT", bezaKT);
			parameters.put("JUMLAH_BEZA", jumlahBeza);
			
			if (bakiKASebelum == 0D) bakiKASebelum = 1D;
			Double peratusanKA = bezaKA / bakiKASebelum;
			if (bakiKTSebelum == 0D) bakiKTSebelum = 1D;
			Double peratusanKT = bezaKT / bakiKTSebelum;
			if (jumlahBakiTunggakanSebelum == 0D) jumlahBakiTunggakanSebelum = 1D;
			Double peratusanJumlah = jumlahBeza / jumlahBakiTunggakanSebelum;
			parameters.put("PERATUSAN_KA", peratusanKA);
			parameters.put("PERATUSAN_KT", peratusanKT);
			parameters.put("PERATUSAN_JUMLAH", peratusanJumlah);
		}
	}
	
	private Double getBaki(String tarikh, String idSeksyen, boolean flagPerjanjianAktif) {
		Double totalDebit = 0D;
		Double totalPelarasanDebit = 0D;
		Double totalKredit = 0D;
		Double totalBaki = 0D;
		Db lebahDb = null;
		try {
			lebahDb = new Db();
			Statement stmt = lebahDb.getStatement();
			
			String sql = "SELECT (SELECT IFNULL(sum(IFNULL(debit, 0)), 0) AS total_debit"
					+ " FROM rk_akaun"
					+ " WHERE rk_akaun.id_jenis_transaksi = '1'"
					+ " AND rk_akaun.tarikh_invois <= STR_TO_DATE('" + tarikh + "', '%d-%m-%Y')"
					+ " AND rk_akaun.id_fail = rk_fail.id) as total_debit,"
					+ " (SELECT IFNULL(sum(IFNULL(debit, 0)), 0) AS total_pelarasan_debit"
					+ " FROM rk_akaun"
					+ " WHERE rk_akaun.id_jenis_transaksi = '3'"
					+ " AND rk_akaun.tarikh_transaksi <= STR_TO_DATE('" + tarikh + "', '%d-%m-%Y')"
					+ " AND rk_akaun.id_fail = rk_fail.id) as total_pelarasan_debit,"
					+ " (SELECT IFNULL(sum(IFNULL(kredit, 0)), 0) AS total_kredit"
					+ " FROM rk_akaun"
					+ " WHERE rk_akaun.id_jenis_transaksi in (2,4)"
					+ " AND rk_akaun.tarikh_transaksi <= STR_TO_DATE('" + tarikh + "', '%d-%m-%Y')"
					+ " AND rk_akaun.id_fail = rk_fail.id) as total_kredit"
					+ " FROM rk_perjanjian, rk_fail, rk_ruang_komersil"
					+ " WHERE rk_perjanjian.id_fail = rk_fail.id AND rk_fail.id_ruang = rk_ruang_komersil.id"
					+ " AND rk_perjanjian.flag_perjanjian_semasa = 'Y'";
			if (flagPerjanjianAktif) {
				sql = sql + "AND rk_perjanjian.id_status_perjanjian = '1'";
			} else {
				sql = sql + "AND rk_perjanjian.id_status_perjanjian != '1'";
			}	
			if (!"".equals(idSeksyen)) {
				sql = sql + " AND rk_ruang_komersil.id_seksyen = '" + idSeksyen + "'";
			}
			sql = sql + " AND rk_fail.flag_tunggakan = 'Y'";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				totalDebit = totalDebit + rs.getDouble("total_debit");
				totalPelarasanDebit = totalPelarasanDebit + rs.getDouble("total_pelarasan_debit");
				totalKredit = totalKredit + rs.getDouble("total_kredit");
			}
			totalBaki = totalDebit + totalPelarasanDebit - totalKredit;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (lebahDb != null) { lebahDb.close(); }
		}	
		return totalBaki;
	}

	private static String getKeteranganBulan(Calendar cal) {
		String keterangan = "";
		
		if (cal.get(Calendar.MONTH) == 0) {
			keterangan = "Januari";
		} else if (cal.get(Calendar.MONTH) == 1) {
			keterangan = "Februari";
		} else if (cal.get(Calendar.MONTH) == 2) {
			keterangan = "Mac";
		} else if (cal.get(Calendar.MONTH) == 3) {
			keterangan = "April";
		} else if (cal.get(Calendar.MONTH) == 4) {
			keterangan = "Mei";
		} else if (cal.get(Calendar.MONTH) == 5) {
			keterangan = "Jun";
		} else if (cal.get(Calendar.MONTH) == 6) {
			keterangan = "Julai";
		} else if (cal.get(Calendar.MONTH) == 7) {
			keterangan = "Ogos";
		} else if (cal.get(Calendar.MONTH) == 8) {
			keterangan = "September";
		} else if (cal.get(Calendar.MONTH) == 9) {
			keterangan = "Oktober";
		} else if (cal.get(Calendar.MONTH) == 10) {
			keterangan = "November";
		} else if (cal.get(Calendar.MONTH) == 11) {
			keterangan = "Disember";
		}
		keterangan = keterangan + " " + cal.get(Calendar.YEAR);
		return keterangan;
	}
	
}
