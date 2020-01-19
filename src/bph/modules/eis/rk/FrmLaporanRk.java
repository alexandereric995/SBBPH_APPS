/**
 * 
 */
package bph.modules.eis.rk;

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

import bph.modules.eis.EisLaporan;
import bph.utils.DataUtil;
import bph.utils.Util;

public class FrmLaporanRk extends LebahModule {

	/**
	 * 
	 */
	static Logger myLogger = Logger.getLogger(FrmLaporanRk.class);
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

	private String getPath() {
		return "bph/modules/eis/rk/eisLaporanRk";
		
	}
	
	private String getTajukLaporan() {
		context.put("tajukLaporan", this.tajukLaporan);
		return tajukLaporan;
	}

	private String getXAxisName() {
		return this.XAxisName;
	}

	private String getYAxisName() {
		return this.YAxisName;
	}

	private String getSQL() {
		
		String jenisLaporan = (String)eisParam.get("jenisLaporan");
		String dateStart = (String)eisParam.get("dateStart");
		String dateEnd = (String)eisParam.get("dateEnd");
		
		if ("laporan1".equals(jenisLaporan)) {
			return getSQL_laporan1(dateStart,dateEnd);
		}else if ("laporan2".equals(jenisLaporan)) {
			return getSQL_laporan2(dateStart,dateEnd);
		} else {
			//
		}
		
		return "";
	}
	
	public String getSQL_laporan1(String dateStart,String dateEnd) {
		StringBuffer sb = new StringBuffer();
	
		sb.append("select month(rk_permohonan.tarikh_permohonan) AS X, ");
		sb.append("count(rk_permohonan.id) AS Y ");
		sb.append("from rk_permohonan ");
		if (dateStart != "" && dateEnd != "") {
			sb.append(" WHERE tarikh_permohonan BETWEEN str_to_date('"+dateStart+"','%Y') AND str_to_date('"+dateEnd+"','%Y')");
		}
		sb.append("group by month(rk_permohonan.tarikh_permohonan), year(rk_permohonan.tarikh_permohonan)");

		System.out.println("SQL getXML=> " + sb.toString());
		return sb.toString();
	}
	
	public String getSQL_laporan2(String dateStart,String dateEnd) {
		StringBuffer sb = new StringBuffer();
	
		sb.append("select b.keterangan AS X,  ");
		sb.append("count(a.id) AS Y  ");
		sb.append("from rk_permohonan a  ");
		sb.append("join ruj_status b on a.id_status = b.id  ");
		if (dateStart != "" && dateEnd != "") {
			sb.append(" AND a.tarikh_permohonan BETWEEN str_to_date('"+dateStart+"','%m-%Y') AND str_to_date('"+dateEnd+"','%m-%Y')");
		}
		sb.append("group by a.id_status ");

		System.out.println("SQL getXML=> " + sb.toString());
		return sb.toString();
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
	
	
	
	/****************************** START SENARAI TAB ******************************/
	
	//---------- Start - ini adalah dropdown - Jenis Laporan EIS ----------/
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
		
		return getPath() + "/kriteriaParameter.vm";
	}
	//---------- End - ini adalah dropdown - Jenis Laporan EIS ----------/
	
	
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
		context.put("path", getPath());
		return getPath() + "/senaraiTab.vm";
	}
	
	public void handleRequestParam() {
		
		eisParam = new HashMap<String, Object>();
		
		String jenisLaporan = getParam("findJenisLaporan");
		String idPeranginan = getParam("idPeranginan");
		
		context.put("jenisLaporan",jenisLaporan);
		if ("laporan1".equals(jenisLaporan)) {
			this.tajukLaporan = "KEPUTUSAN";
			this.XAxisName = "TAJUK";
			this.YAxisName = "JUMLAH";
		}else if ("laporan2".equals(jenisLaporan)) {
			this.tajukLaporan = "KEPUTUSAN";
			this.XAxisName = "TAJUK";
			this.YAxisName = "JUMLAH";
		}
		
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
	/****************************** END SENARAI TAB ******************************/
}
