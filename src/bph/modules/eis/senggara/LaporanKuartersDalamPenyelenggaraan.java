/**
 * 
 */
package bph.modules.eis.senggara;

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
public class LaporanKuartersDalamPenyelenggaraan extends LebahModule {

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
		context.put("util", new Util());
		context.put("selectedTab", 0);
		context.put("path", getPath());
		List<RppPeranginan> listJenisPeranginan = dataUtil.getListPeranginanRpp();
		context.put("listJenisPeranginan", listJenisPeranginan);
		context.put("selectJenisRPP", dataUtil.getListPeranginanRpp());
		context.put("selectedTab", 0);
		context.put("selectLokasiKuarters", dataUtil.getListLokasiPermohonan());
		context.put("laporan", "jumlah_penyelenggaraan");
		context.put("path", getPath());
		
		return getPath() + "/start.vm";
	}

	private String getTajukLaporan() {
		String tajukLaporan = "Jumlah Kuarters Dalam Penyelenggaraan Mengikut Lokasi";
		context.put("tajukLaporan", tajukLaporan);
		return tajukLaporan;
	}

	private String getXAxisName() {
		return "Bulan";
	}

	private String getYAxisName() {
		return "Jumlah";
	}

//	private String getSQL() {
//		String sql = "";
//
//		sql = "SELECT ruj_bulan.keterangan AS x, count(permohonan.id) AS y"
//				+ " FROM    ruj_bulan"
//				+ " LEFT JOIN"
//				+ " (SELECT rpp_permohonan.id AS id,"
//				+ " rpp_permohonan.id_peranginan AS id_peranginan,"
//				+ " month(rpp_permohonan.tarikh_permohonan) AS bulan"
//				+ " FROM rpp_permohonan"
//				+ " WHERE     year(rpp_permohonan.tarikh_permohonan) ="
////				+ " year(current_date)"
//				+ " 2016"
//				+ " AND rpp_permohonan.jenis_pemohon = 'INDIVIDU'"
//				+ " AND rpp_permohonan.id_status in ('1425259713418','1435093978588')) permohonan"
//				+ " ON permohonan.bulan = ruj_bulan.id WHERE permohonan.id is NOT NULL";
//		//parameter
//		if (!"".equals(getParam("findJenisRPP"))) {
//			sql = sql + " AND id_peranginan = '" + getParam("findJenisRPP") + "'";
//		}
//		
//		sql = sql + " GROUP BY ruj_bulan.id" + " ORDER BY ruj_bulan.id + 0";
//		System.out.println(sql);
//		return sql;
//
//	}
	
	private String getSQL() {
		String sql = "";

		sql = "SELECT id_kelas_kuarters AS x, count(id) AS y"
				+ " FROM kua_kuarters"
				+ " WHERE id_kelas_kuarters IS NOT NULL AND id_kelas_kuarters != '' and id_status_kuarters='02'";
		//parameter
				if (!"".equals(getParam("findLokasiKuarters"))) {
					sql = sql + " AND kua_kuarters.id_lokasi_kuarters = '" + getParam("findLokasiKuarters") + "'";
				}
				
				sql = sql +" GROUP BY id_kelas_kuarters ORDER BY id_kelas_kuarters ASC";
				 
		return sql;
	}

//	private String getPath() {
//		return "bph/modules/eis/rpp/senaraiPermohonanRPPBatalMengikutBulan";
//	}
	
	private String getPath() {
		return "bph/modules/eis/senggara/laporanSenggara";
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
