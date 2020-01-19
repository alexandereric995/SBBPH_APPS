/**
 * 
 */
package bph.modules.eis.rpp;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import lebah.db.Db;
import lebah.portal.action.Command;
import lebah.portal.action.LebahModule;
import lebah.template.DbPersistence;
import bph.entities.rpp.RppPeranginan;
import bph.modules.eis.EisLaporan;
import bph.utils.DataUtil;
import bph.utils.Util;

/**
 * @author rzai
 * 
 */
public class SenaraiPermohonanRPPLulusMengikutBulan extends
		LebahModule {

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
		context.put("selectJenisRPP", dataUtil.getListPeranginanRpp());
		List<RppPeranginan> listJenisPeranginan = dataUtil.getListPeranginanRpp();
		context.put("listJenisPeranginan", listJenisPeranginan);
		context.put("util", new Util());
		context.put("selectedTab", 0);
		context.put("path", getPath());
		return getPath() + "/start.vm";
	}

	private String getTajukLaporan() {
		String tajukLaporan = "Bilangan Permohonan Rumah Peranginan Yang Lulus Mengikut Bulan (Tahun Semasa)";
		context.put("tajukLaporan", tajukLaporan);
		return tajukLaporan;
	}

	private String getXAxisName() {
		return "Bulan";
	}

	private String getYAxisName() {
		return "Bilangan permohonan";
	}

//	private String getSQL() {
//		String sql = "";
//
//		sql = "SELECT ruj_bulan.keterangan AS x, count(permohonan.id) AS y"
//				+ " FROM ruj_bulan" + " LEFT JOIN"
//				+ " (SELECT rpp_permohonan.id AS id,"
//				+ " rpp_permohonan.id_peranginan AS id_peranginan,"
//				+ " month(rpp_permohonan.tarikh_permohonan) AS bulan"
//				+ " FROM rpp_permohonan"
//				+ " WHERE     year(rpp_permohonan.tarikh_permohonan) ="
////				+ " year(current_date)"
//				+ "2016"
//				+ " AND rpp_permohonan.jenis_pemohon = 'INDIVIDU'"
//				+ " AND rpp_permohonan.id_status = '1425259713415'"
//				+ " AND rpp_permohonan.status_bayaran = 'Y') permohonan"
//				+ " ON permohonan.bulan = ruj_bulan.id WHERE permohonan.id is NOT NULL";
//				//parameter
//				if (!"".equals(getParam("findJenisRPP"))) {
//					sql = sql + " AND id_peranginan = '" + getParam("findJenisRPP") + "'";
//				}
//				sql = sql + " GROUP BY ruj_bulan.id" + " ORDER BY ruj_bulan.id + 0";
//		System.out.println(sql);
//		return sql;
//
//	}

private String getSQL() {
		
		String tahun = getParam("yearStart");
		String idStatusBayaran = getParam("idStatusBayaran");
		String idRPP = getParam("idPeranginan");
		StringBuffer sb = new StringBuffer();
		
		sb.append("select month(rpp_permohonan.tarikh_masuk_rpp) as x, sum(rpp_akaun.kredit) as y" );
		sb.append(" from rpp_akaun join rpp_permohonan on rpp_akaun.id_permohonan = rpp_permohonan.id join rpp_peranginan on rpp_permohonan.id_peranginan = rpp_peranginan.id");
		sb.append(" where year(rpp_permohonan.tarikh_masuk_rpp) is not null");
		sb.append(" and rpp_permohonan.id_status = '1425259713415'");
		if (!"".equalsIgnoreCase(tahun)){
			sb.append(" and year(rpp_permohonan.tarikh_masuk_rpp) = '"+tahun+"' ");
		}else{
			sb.append(" and year(rpp_permohonan.tarikh_masuk_rpp) = '2017' ");	
		}
		
		if (!"0".equalsIgnoreCase(idRPP)){
			sb.append(" and rpp_permohonan.id_peranginan = '"+idRPP+"'");
		}else{
	
		}
		sb.append(" group by month(rpp_permohonan.tarikh_masuk_rpp)");
		
	return sb.toString();
	}

	private String getPath() {
		return "bph/modules/eis/rpp/laporanKutipanRPPMengikutCawangan";
	}

	public String generateXML() {
		context.put("laporan", "lulus");
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
				if(rs.getString("x").equalsIgnoreCase("1")){
					laporan.setKeterangan("Januari");
				}else if(rs.getString("x").equalsIgnoreCase("2")){
					laporan.setKeterangan("Februari");
				}else if(rs.getString("x").equalsIgnoreCase("3")){
					laporan.setKeterangan("Mac");
				}else if(rs.getString("x").equalsIgnoreCase("4")){
					laporan.setKeterangan("April");
				}else if(rs.getString("x").equalsIgnoreCase("5")){
					laporan.setKeterangan("Mei");
				}else if(rs.getString("x").equalsIgnoreCase("6")){
					laporan.setKeterangan("Jun");
				}else if(rs.getString("x").equalsIgnoreCase("7")){
					laporan.setKeterangan("Julai");
				}else if(rs.getString("x").equalsIgnoreCase("8")){
					laporan.setKeterangan("Ogos");
				}else if(rs.getString("x").equalsIgnoreCase("9")){
					laporan.setKeterangan("September");
				}else if(rs.getString("x").equalsIgnoreCase("10")){
					laporan.setKeterangan("October");
				}else if(rs.getString("x").equalsIgnoreCase("11")){
					laporan.setKeterangan("November");
				}else if(rs.getString("x").equalsIgnoreCase("12")){
					laporan.setKeterangan("December");
				}
				//laporan.setKeterangan(rs.getString("keterangan"));
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
		return xml;
	}

	/** START SENARAI TAB **/
	@Command("getStatistik")
	public String getStatistik() 
	{
		context.put("xml", generateXML());
		context.put("util", new Util());
		context.put("selectedTab", 0);
		context.put("path", getPath());
		return getPath() + "/senaraiTab.vm";
	}
	@Command("janaLaporan")
	public String janaLaporan() {

		context.put("xml", generateXML());

		context.put("selectedTab", getParam("selectedTab"));
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
