/**
 * 
 */
package bph.modules.eis.kewangan;

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

import org.apache.log4j.Logger;

import bph.modules.eis.EisLaporan;
import bph.utils.DataUtil;
import bph.utils.Util;

public class LaporanPembayaranDepositMengikutBulan extends LebahModule {

	/**
	 * 
	 */
	static Logger myLogger = Logger.getLogger(LaporanPembayaranDepositMengikutBulan.class);
	private static final long serialVersionUID = 1L;
	
	private DataUtil dataUtil;

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
		
		if ("kewangan1".equals(jenisLaporan)) {
			return getSQL_kewangan1(dateStart,dateEnd);
		} else if ("kewangan2".equals(jenisLaporan)) {
			return getSQL_kewangan2(dateStart,dateEnd);
		} else {
			
		}
		
		return "";
	}
	
	public String getSQL_kewangan1(String dateStart,String dateEnd) {
		StringBuffer sb = new StringBuffer();
		
		sb.append("SELECT E.keterangan,E.kod as x, SUM(C.debit) as y FROM kew_resit_senarai_invois A ");
		sb.append("JOIN kew_bayaran_resit B ON A.id_bayaran_resit = B.id ");
		sb.append("JOIN kew_invois C ON A.id_invois = C.id ");
		sb.append("JOIN ruj_kod_hasil E ON C.id_kod_hasil = E.id ");
		if (dateStart != "" && dateEnd != "") {
			//sb.append("WHERE DATE_FORMAT(B.tarikh_bayaran,'%d-%m-%Y') BETWEEN '"+dateStart+"' AND '"+dateEnd+"' ");
			sb.append("WHERE B.tarikh_bayaran BETWEEN str_to_date('"+dateStart+"','%d-%m-%Y') AND str_to_date('"+dateEnd+"','%d-%m-%Y')");
		}
		sb.append("GROUP BY E.kod, E.keterangan ");
		sb.append("ORDER BY y desc");
		System.out.println("SQL getXML=>"+sb.toString());
		return sb.toString();
	}
	
	public String getSQL_kewangan2(String dateStart,String dateEnd) {
		StringBuffer sb = new StringBuffer();
		
		sb.append("select b.nama_peranginan as keterangan,b.nama_peranginan as x,sum(C.jumlah_amaun_bayaran) as y ");
		sb.append("from rpp_permohonan A inner join rpp_peranginan B on a.id_peranginan=b.id  ");
		sb.append("inner join kew_bayaran_resit C on C.id = A.id_resit_deposit ");
		sb.append("WHERE id_resit_deposit IS NOT NULL GROUP BY x ");
		
		return sb.toString();
	}

	private String getPath() {
		return "bph/modules/eis/kewangan/laporanPembayaranDepositMengikutBulan";
	}

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
				laporan.setKeterangan(rs.getString("keterangan"));
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
		System.out.println("selectedTab:"+selectedTab);
		context.put("selectedTab",selectedTab);
		context.put("path", getPath());
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
	
	public void handleRequestParam() {
		
		String jenisLaporan = getParam("findJenisLaporan");
		context.put("jenisLaporan",jenisLaporan);
		if ("kewangan1".equals(jenisLaporan)) {
			//LAPORAN KUTIPAN HARIAN BPH KESELURUHAN
			this.tajukLaporan = "LAPORAN KUTIPAN HARIAN BPH KESELURUHAN";
			this.XAxisName = "KOD HASIL";
			this.YAxisName = "JUMLAH";
		} else {
			this.tajukLaporan = "LAPORAN KUTIPAN";
			this.XAxisName = "KETERANGAN";
			this.YAxisName = "JUMLAH";
		}
		
		eisParam = new HashMap<String, Object>();
		eisParam.put("jenisLaporan",jenisLaporan);
		eisParam.put("dateStart",getParam("dateStart"));
		eisParam.put("dateEnd",getParam("dateEnd"));
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
