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
import bph.modules.eis.EisLaporan;
import bph.utils.Util;

/**
 * @author rzai
 * 
 */
public class SenaraiRumahPeranginanMengikutKategori extends LebahModule {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String start() {
		// TODO Auto-generated method stub

		context.put("xml", generateXML());
		context.put("util", new Util());
		context.put("selectedTab", 0);
		context.put("path", getPath());
		return getPath() + "/start.vm";
	}

	private String getTajukLaporan() {
		String tajukLaporan = "Jumlah Rumah Peranginan Mengikut Kategori";
		context.put("tajukLaporan", tajukLaporan);
		return tajukLaporan;
	}

	private String getXAxisName() {
		return "Kategori";
	}

	private String getYAxisName() {
		return "Bilangan rumah";
	}

	private String getSQL() {
		String sql = "";

		sql = "SELECT ruj_jenis_bangunan.keterangan AS x,"
				+ " count(peranginan.id_peranginan) AS y"
				+ " FROM    ruj_jenis_bangunan" + " LEFT JOIN"
				+ " (SELECT rpp_peranginan.id AS id_peranginan,"
				+ " rpp_peranginan.id_jenis_peranginan AS jenis_peranginan"
				+ " FROM rpp_peranginan) peranginan"
				+ " ON peranginan.jenis_peranginan = ruj_jenis_bangunan.id"
				+ " WHERE ruj_jenis_bangunan.id IN ('RPP', 'RP', 'RT')"
				+ " GROUP BY ruj_jenis_bangunan.id"
				+ " ORDER BY ruj_jenis_bangunan.keterangan ASC";
		System.out.println(sql);
		return sql;

	}

	private String getPath() {
		return "bph/modules/eis/rpp/senaraiRumahPeranginanMengikutKategori";
	}

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
