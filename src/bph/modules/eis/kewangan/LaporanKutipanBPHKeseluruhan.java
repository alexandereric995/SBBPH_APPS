/**
 * 
 */
package bph.modules.eis.kewangan;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import lebah.db.Db;
import lebah.portal.action.Command;
import lebah.portal.action.LebahModule;
import lebah.template.DbPersistence;
import bph.modules.eis.EisLaporan;
import bph.utils.DataUtil;
import bph.utils.Util;

/**
 * @author rz
 * 
 */
public class LaporanKutipanBPHKeseluruhan extends LebahModule {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private DataUtil dataUtil;

	@Override
	public String start() {
		// TODO Auto-generated method stub
		DbPersistence db = new DbPersistence();
		dataUtil = DataUtil.getInstance(db);
		context.put("xml", generateXML());
		context.put("selectBulan", dataUtil.getListBulan());
		context.put("selectJenisHakmilik", dataUtil.getListJenisHakMilik());
		context.put("selectJenisLot", dataUtil.getListLot());
		context.put("senaraiNegeri", dataUtil.getListNegeri());
		context.put("util", new Util());
		context.put("selectedTab", 0);
		context.put("path", getPath());
		return getPath() + "/start.vm";
	}

	private String getTajukLaporan() {
		String tajukLaporan = "Laporan Kutipan Tahunan BPH Mengikut Bulan";
		context.put("tajukLaporan", tajukLaporan);
		return tajukLaporan;
	}

	private String getXAxisName() {
		return "Bulan";
	}

	private String getYAxisName() {
		return "Jumlah Kutipan";
	}

//	private String getSQL() {
//		String tahun = getParam("yearStart");
//		StringBuffer sb = new StringBuffer();
//		sb.append("select sum(kew_bayaran_resit.jumlah_amaun_bayaran) as y,");
//		sb.append("month(kew_bayaran_resit.tarikh_resit)as x from kew_bayaran_resit where flag_void = 'T' ");
//		
//		if (!"".equalsIgnoreCase(tahun)){
//			sb.append("and year(kew_bayaran_resit.tarikh_resit) = '"+tahun+"' ");
//		}else{
//			sb.append("and year(kew_bayaran_resit.tarikh_resit) = '2016' ");	
//		}
//		
//		sb.append("group by month(kew_bayaran_resit.tarikh_resit)");
//		return sb.toString();
//	}

	private String getSQL() {
	String tahun = getParam("yearStart");
	StringBuffer sb = new StringBuffer();
	sb.append("select rujBulan.keterangan AS x, sum(bayaran.jumlah) as y FROM ruj_bulan rujBulan ");
	sb.append("LEFT OUTER JOIN ");
	sb.append("(select kew_bayaran_resit.id as id,kew_bayaran_resit.jumlah_amaun_bayaran as jumlah, ");
	sb.append("month(kew_bayaran_resit.tarikh_resit)as bulan from kew_bayaran_resit where flag_void = 'T' ");
	
	if (!"".equalsIgnoreCase(tahun)){
		sb.append("and year(kew_bayaran_resit.tarikh_resit) = '"+tahun+"') ");
	}else{
		sb.append("and year(kew_bayaran_resit.tarikh_resit) = '2016') ");	
	}
	sb.append("bayaran ON bayaran.bulan=rujBulan.id ");
	sb.append("group by rujBulan.id ORDER BY rujBulan.id + 0");
	return sb.toString();
}

	private String getPath() {
		return "bph/modules/eis/kewangan/laporanKutipanBPHKeseluruhan";
	}

//	public String generateXML() {
//
//		String xml = "<chart caption='" + getTajukLaporan()
//				+ "' subcaption='' xAxisName='" + getXAxisName()
//				+ "' yAxisName='" + getYAxisName()
//				+ "' numberPrefix='' showLegend='1'>";
//		
//		Db db = null;
//		try {
//			db = new Db();
//			Connection conn = db.getConnection();
//			Statement stat = conn.createStatement();
//			List<Object> listLaporan = new ArrayList<Object>();
//			ResultSet rs = stat.executeQuery(getSQL());
//			while (rs.next()) {				
//				xml = xml + "<set label = '" + rs.getString("x")
//						+ "' value = '" + rs.getString("y") + "' />";
//				
//				//LAPORAN STATISTIK
//				EisLaporan laporan = new EisLaporan();
//				laporan.setX(rs.getString("x"));
//				laporan.setY(rs.getString("y"));
//				if(rs.getString("x").equalsIgnoreCase("1")){
//					laporan.setKeterangan("Januari");
//				}else if(rs.getString("x").equalsIgnoreCase("2")){
//					laporan.setKeterangan("Februari");
//				}else if(rs.getString("x").equalsIgnoreCase("3")){
//					laporan.setKeterangan("Mac");
//				}else if(rs.getString("x").equalsIgnoreCase("4")){
//					laporan.setKeterangan("April");
//				}else if(rs.getString("x").equalsIgnoreCase("5")){
//					laporan.setKeterangan("Mei");
//				}else if(rs.getString("x").equalsIgnoreCase("6")){
//					laporan.setKeterangan("Jun");
//				}else if(rs.getString("x").equalsIgnoreCase("7")){
//					laporan.setKeterangan("Julai");
//				}else if(rs.getString("x").equalsIgnoreCase("8")){
//					laporan.setKeterangan("Ogos");
//				}else if(rs.getString("x").equalsIgnoreCase("9")){
//					laporan.setKeterangan("September");
//				}else if(rs.getString("x").equalsIgnoreCase("10")){
//					laporan.setKeterangan("October");
//				}else if(rs.getString("x").equalsIgnoreCase("11")){
//					laporan.setKeterangan("November");
//				}else if(rs.getString("x").equalsIgnoreCase("12")){
//					laporan.setKeterangan("December");
//				}
//				//laporan.setKeterangan(rs.getString("keterangan"));
//				//laporan.setAbbrev(rs.getString("x"));
//				listLaporan.add(laporan);
//			}
//			xml = xml + "</chart>";
//			context.put("listLaporan", listLaporan);
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			if (db != null)
//				db.close();
//		}
//		return xml;
//	}
	
	public String generateXML() {

		String xml = "<chart caption='" + getTajukLaporan()
				+ "' subcaption='' xAxisName='" + getXAxisName()
				+ "' yAxisName='" + getYAxisName()
				+ "' numberPrefix='' showLegend='1'>";

		Db db = null;
		try {
			db = new Db();
			Connection conn = db.getConnection();
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery(getSQL());
			List<Object> listLaporan = new ArrayList<Object>();
			while (rs.next()) {
				xml = xml + "<set label = '" + rs.getString("x")
						+ "' value = '" + rs.getString("y") + "' />";
				
				//LAPORAN STATISTIK
				EisLaporan laporan = new EisLaporan();
				laporan.setX(rs.getString("x"));
				laporan.setY(rs.getString("y"));
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
		return xml;
	}
	/** START SENARAI TAB **/
	@Command("janaLaporan")
	public String janaLaporan() {
		context.put("xml", generateXML());
		context.put("selectedTab", getParam("selectedTab"));
		context.put("path", getPath());
		return getPath() + "/senaraiTab.vm";
	}
	
	@Command("getStatistik")
	public String getStatistik() {
		context.put("xml", generateXML());
		//context.put("util", new Util());
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
