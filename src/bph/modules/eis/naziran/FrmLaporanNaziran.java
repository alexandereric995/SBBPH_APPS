/**
 * 
 */
package bph.modules.eis.naziran;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lebah.db.Db;
import lebah.portal.action.Command;
import lebah.portal.action.LebahModule;
import lebah.template.DbPersistence;

import org.apache.log4j.Logger;

import bph.entities.kod.JenisKuartersUtk;
import bph.entities.kod.JenisOperasiUtk;
import bph.entities.kod.KawasanUtk;
import bph.entities.kod.ZonUtk;
import bph.modules.eis.EisLaporan;
import bph.utils.DataUtil;
import bph.utils.Util;

public class FrmLaporanNaziran extends LebahModule {

	/**
	 * 
	 */
	static Logger myLogger = Logger.getLogger(FrmLaporanNaziran.class);
	private static final long serialVersionUID = 1L;
	
	//private DataUtil dataUtil;
	private DbPersistence db = new DbPersistence();
	private DataUtil dataUtil = DataUtil.getInstance(db);

	String tajukLaporan;
	String XAxisName;
	String YAxisName;
	Map<String,Object> eisParam;
	
	@Override
	public String start() {
		context.put("jenisLaporan","");
		context.put("path", getPath());
		return getPath() + "/start.vm";
	}

	private String getTajukLaporan() {
		//String tajukLaporan = "Jumlah Hakmilik Mengikut Negeri";
		context.put("tajukLaporan", this.tajukLaporan);
		return tajukLaporan;
	}

	private String getXAxisName() {
		return this.XAxisName;
	}

	private String getYAxisName() {
		return this.YAxisName;
		//return "Jumlah Hakmilik";
	}

	private String getSQL() {
		
		String jenisLaporan = (String)eisParam.get("jenisLaporan");
		String dateStart = (String)eisParam.get("dateStart");
		String dateEnd = (String)eisParam.get("dateEnd");
		
		if ("ringanBerat".equals(jenisLaporan)) {
			return getSQL_ringanBerat(dateStart,dateEnd);
		} else if ("beratIkutKesalahan".equals(jenisLaporan)) {
//			String idPeranginan = (String)eisParam.get("idPeranginan");
//			return getSQL_beratJenisKesalahan(dateStart,dateEnd,idPeranginan);
			return getSQL_beratJenisKesalahan(dateStart,dateEnd);
		} else if ("ringanIkutKesalahan".equals(jenisLaporan)) {
			return getSQL_ringanJenisKesalahan(dateStart,dateEnd);
		} else if ("operasi".equals(jenisLaporan)) {
			String idJenisKuarters= (String)eisParam.get("idJenisKuarters");
			String jenisOperasi = (String)eisParam.get("jenisOperasi");
			String kawasan = (String)eisParam.get("kawasan");
			String zon = (String)eisParam.get("idZon");
			System.out.println("PRINT STING idJenisKuarters === ::: " + idJenisKuarters + " STING jenisOperasi ==:: "  + jenisOperasi + " STING kawasan ==:: " + kawasan );
			return getSQL_operasi(dateStart,dateEnd,idJenisKuarters,jenisOperasi,kawasan,zon);
		} else {
			//
		}
		
		return "";
	}
	
	public String getSQL_ringanBerat(String dateStart,String dateEnd) {
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("SELECT 'KESALAHAN BERAT' as x, count(utk_kesalahan.id) as y");
		sb.append(" from utk_kesalahan, ruj_jenis_pelanggaran_syarat_utk where utk_kesalahan.id = ruj_jenis_pelanggaran_syarat_utk.id and ruj_jenis_pelanggaran_syarat_utk.flag_kes = '2'");
		sb.append(" union");
		sb.append("	select 'KESALAHAN RINGAN' as x, count(utk_kesalahan.id) as y");
		sb.append(" from utk_kesalahan, ruj_jenis_pelanggaran_syarat_utk where utk_kesalahan.id = ruj_jenis_pelanggaran_syarat_utk.id and ruj_jenis_pelanggaran_syarat_utk.flag_kes = '1'");
		if (dateStart != "" && dateEnd != "") {
			sb.append(" AND tarikh BETWEEN str_to_date('"+dateStart+"','%d-%m-%Y') AND str_to_date('"+dateEnd+"','%d-%m-%Y')");
		}
//		sb.append("GROUP BY E.kod, E.keterangan ");
//		sb.append("ORDER BY y desc");
	
		System.out.println("SQL getXML=>"+sb.toString());
		return sb.toString();
	}
	
	public String getSQL_beratJenisKesalahan(String dateStart,String dateEnd) {
		
		System.out.println("dateStart ==== :::" + dateStart);
		System.out.println("dateEnd ==== :::" + dateEnd);
		StringBuffer sb = new StringBuffer();
		
		sb.append("SELECT a.keterangan AS x,");
		sb.append(" count(b.id) AS y");
		sb.append(" from ruj_jenis_pelanggaran_syarat_utk a");
		sb.append(" LEFT OUTER JOIN utk_kesalahan b");
		sb.append(" ON a.id = b.id_jenis_pelanggaran_syarat");
		sb.append(" WHERE a.flag_kes  = '2'");
		
////		if (dateStart != "" && dateEnd != "") {
//		if (dateStart.equals(null) == false && dateEnd.equals(null) == false) {
//			sb.append(" AND b.tarikh BETWEEN str_to_date('"+dateStart+"','%d-%m-%Y') AND str_to_date('"+dateEnd+"','%d-%m-%Y')");
//		}
//		if (Integer.parseInt(idPeranginan) > 0) {
//			sb.append("AND B.id_peranginan='"+idPeranginan+"' ");
//		}
		sb.append(" GROUP BY a.id");
		sb.append(" ORDER BY a.keterangan ASC;");
		
		System.out.println("PRINT =====::" + sb.toString());
		return sb.toString();
	}
	
	public String getSQL_ringanJenisKesalahan(String dateStart,String dateEnd) {
		System.out.println("dateStart ==== :::" + dateStart);
		System.out.println("dateEnd ==== :::" + dateEnd);

		StringBuffer sb = new StringBuffer();
		
		sb.append("SELECT ruj_jenis_pelanggaran_syarat_utk.keterangan AS x,");
		sb.append(" count(kesalahan.id) AS y");
		sb.append(" FROM ruj_jenis_pelanggaran_syarat_utk");
		sb.append(" LEFT JOIN");
		sb.append(" (SELECT utk_kesalahan.id AS id,");
		sb.append(" utk_kesalahan.id AS id_kesalahan");
		sb.append(" FROM utk_kesalahan) kesalahan");
		sb.append(" ON kesalahan.id = ruj_jenis_pelanggaran_syarat_utk.id");
		sb.append(" WHERE ruj_jenis_pelanggaran_syarat_utk.flag_kes = '1'");
		
//		if (dateStart != "" && dateEnd != "") { // Disable sementara sebab x jadi
//			sb.append(" AND utk_kesalahan.tarikh BETWEEN str_to_date('"+dateStart+"','%d-%m-%Y') AND str_to_date('"+dateEnd+"','%d-%m-%Y')");
//		}
		sb.append(" GROUP BY ruj_jenis_pelanggaran_syarat_utk.id");
		sb.append(" ORDER BY ruj_jenis_pelanggaran_syarat_utk.keterangan ASC");
		System.out.println(sb.toString());
		return sb.toString();

	}
	
	public String getSQL_operasi(String dateStart,String dateEnd, String jenisKuarters, String jenisOperasi, String kawasan, String zon) {
		System.out.println("PRINT ID jenis Kuarters === ::: " + jenisKuarters);
		System.out.println("PRINT ID jenis Operasi === ::: " + jenisOperasi);
		System.out.println("PRINT ID kawasan === ::: " + kawasan);
		System.out.println("PRINT ID zon === ::: " + zon);
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("select 'OPERASI BERJADUAL' as x,");
		sb.append(" count(id) as y");
		sb.append(" from utk_operasi");
		sb.append(" where flag_operasi = '1'");
		sb.append(" union");
		sb.append(" select 'OPERASI TIDAK BERJADUAL' as x,");
		sb.append(" count(id) as y");
		sb.append(" from utk_operasi");
		sb.append(" where flag_operasi = '2'");
		
		 //Disable sementara sebab tak masuk ke sini	
//		if (dateStart != "" && dateEnd != "") {
//			sb.append("AND A.tarikh_bayaran BETWEEN str_to_date('"+dateStart+"','%m-%Y') AND str_to_date('"+dateEnd+"','%m-%Y')");
//		}
		
		//x jadi lagi ni akan disambung debug
		if ( jenisKuarters.equals("A") || jenisKuarters.equals("T")) {
//		if ( Integer.parseInt(jenisKuarters) > 0) {
			sb.append(" AND id_jenis_kuartes='"+jenisKuarters+"' ");
		}
		
		if (jenisOperasi != null) {
			sb.append(" AND id_jenis_operasi='"+jenisOperasi+"' ");
		}
		
		if (kawasan != null) {
			sb.append(" AND id_kawasan='"+kawasan+"' ");
		}
		
		if(zon != null){
			sb.append(" AND id_zon='"+zon+"'");
		}
		
//		sb.append("group by x");
		System.out.println("PRINT SQL =====::" + sb.toString());
		System.out.println(sb.toString());
		return sb.toString();
	}

	private String getPath() {
		return "bph/modules/eis/naziran/eisLaporan";
		
	}
	
	/****** START - HANTAR DATA UNTUK GENERATE GRAF ******/
	public String generateXML() {
		handleRequestParam();
		String xml = "<chart caption='" + getTajukLaporan()
				+ "' subcaption='' xAxisName='" + getXAxisName()
				+ "' yAxisName='" + getYAxisName()
				+ "' numberPrefix='' showLegend='1'>";
		
		Db db = null;
		try {
			db = new Db();
			Connection conn = db.getConnection();
			Statement stat = conn.createStatement();
			List<Object> listLaporan = new ArrayList<Object>();
			ResultSet rs = stat.executeQuery(getSQL());
			while (rs.next()) {				
				xml = xml + "<set label = '" + rs.getString("x")
						+ "' value = '" + rs.getString("y") + "' />";
				
				//LAPORAN STATISTIK
				EisLaporan laporan = new EisLaporan();
				laporan.setX(rs.getString("x"));
				laporan.setY(rs.getString("y"));
//				laporan.setKeterangan(rs.getString("keterangan"));
				laporan.setAbbrev(rs.getString("x"));
				listLaporan.add(laporan);
			}
			xml = xml + "</chart>";
			context.put("listLaporan", listLaporan);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (db != null)
				db.close();
		}
		System.out.println("XML return=>"+xml);
		return xml;
	}
	/****** END - HANTAR DATA UNTUK GENERATE GRAF ******/
	
	
	/** START SENARAI TAB **/
	
	@Command("pilihLaporan")
	public String pilihLaporan() {
		String jenisLaporan = getParam("findJenisLaporan");
		context.put("jenisLaporan",jenisLaporan);
		//SET SELECTED TAB KEPADA STATISTIC
		String selectedTab = getParam("selectedTab");
		if ("".equals(selectedTab)) {
			selectedTab = "0";
		}
		context.put("selectedTab",selectedTab);
		context.put("path", getPath());

		List<JenisKuartersUtk> listJenisKuarters = dataUtil.getListJenisKuartersUtk();
		context.put("listJenisKuarters", listJenisKuarters);
		System.out.println("PRINT listJenisKuarters === :::" + listJenisKuarters.size());
		
		List<JenisOperasiUtk> listJenisOperasi = dataUtil.getListJenisOperasiUtk();
		context.put("jenisOperasi", listJenisOperasi);
		
		List<KawasanUtk> listKawasan = dataUtil.getListKawasanUtk();
		context.put("kawasan", listKawasan);
		
		List<ZonUtk> listZon = dataUtil.getListZonUtk();
		context.put("zon", listZon);
		
		return getPath() + "/kriteriaParameter.vm";
	}
	
	@Command("janaLaporan")
	public String janaLaporan() {
		context.put("util", new Util());
		getStatistik();
		//SET SELECTED TAB KEPADA STATISTIC
		String selectedTab = getParam("selectedTab");
		if ("".equals(selectedTab)) {
			selectedTab = "0";
		}
		
		context.put("selectedTab",selectedTab);
		//context.put("selectedTab", getParam("selectedTab"));
		context.put("path", getPath());
		return getPath() + "/senaraiTab.vm";
	}
	
	/******** BUAT TAJUK *********/
	public void handleRequestParam() {
		
		eisParam = new HashMap<String, Object>();
		
		String jenisLaporan = getParam("findJenisLaporan");
		String idPeranginan = getParam("idPeranginan");
		String idJenisKuarters = getParam("idJenisKuarters");
		String idJenisOperasi = getParam("idJenisOperasi");
		String idKawasan = getParam("idKawasan");

		context.put("jenisLaporan",jenisLaporan);
		if ("ringanBerat".equals(jenisLaporan)) {
			//LAPORAN KUTIPAN HARIAN BPH KESELURUHAN
			this.tajukLaporan = "LAPORAN KESALAHAN BERAT DAN RINGAN";
			this.XAxisName = "KESALAHAN";
			this.YAxisName = "JUMLAH";
		} else if ("beratIkutKesalahan".equals(jenisLaporan)) {
			this.tajukLaporan = "SENARAI KESALAHAN BERAT MENGIKUT JENIS KESALAHAN";
			this.XAxisName = "KESALAHAN";
			this.YAxisName = "JUMLAH";
//			eisParam.put("idPeranginan", idPeranginan);
		}else {
			this.tajukLaporan = "LAPORAN KUTIPAN";
			this.XAxisName = "KETERANGAN";
			this.YAxisName = "JUMLAH";
		}
		
		eisParam.put("jenisLaporan",jenisLaporan);
		eisParam.put("dateStart",getParam("dateStart"));
		eisParam.put("dateEnd",getParam("dateEnd"));
		eisParam.put("idJenisKuarters", idJenisKuarters);
		eisParam.put("idJenisOperasi", idJenisOperasi);
		eisParam.put("kawasan", idKawasan);
	}
	
	@Command("getStatistik")
	public String getStatistik() {
		context.put("xml", generateXML());
		context.put("util", new Util());
		context.put("selectedTab", 0);
		context.put("path", getPath());
		return getPath() + "/senaraiTab.vm";
	}
	
	@Command("getBar")
	public String getBar() {
		context.put("xml", generateXML());	
		context.put("selectedTab", 1);
		context.put("path", getPath());
		return getPath() + "/senaraiTab.vm";
	}
	
	@Command("getDoughnut")
	public String getDoughnut() {

		context.put("xml", generateXML());
		context.put("selectedTab", 2);
		context.put("path", getPath());
		return getPath() + "/senaraiTab.vm";
	}
	
	@Command("getLine")
	public String getLine() {

		context.put("xml", generateXML());
		
		context.put("selectedTab", 3);
		context.put("path", getPath());
		return getPath() + "/senaraiTab.vm";
	}
	
	@Command("getPie")
	public String getPie() {
		context.put("xml", generateXML());
		context.put("selectedTab", 4);
		context.put("path", getPath());
		return getPath() + "/senaraiTab.vm";
	}
	/** END SENARAI TAB **/
}
