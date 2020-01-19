/**
 * 
 */
package bph.modules.eis.kuarters;

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
 * @author rzai
 * 
 */
public class LaporanPermohonanKuartersMengikutBulanTahun extends LebahModule {

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
		return getPath() + "/start.vm";
	}

	private String getTajukLaporan() {
		String tajukLaporan = "Laporan Permohonan Kuarters Mengikut Bulan Dan Tahun";
		context.put("tajukLaporan", tajukLaporan);
		return tajukLaporan;
	}

	private String getXAxisName() {
		return "Bulan";
	}

	private String getYAxisName() {
		return "Jumlah";
	}
	
	private String getSQL() {
	String tahun = getParam("yearStart");
	StringBuffer sb = new StringBuffer();
	sb.append("SELECT rujBulan.keterangan AS x, count(permohonan.id) as y FROM ruj_bulan rujBulan ");
	sb.append("LEFT JOIN ");
	sb.append("(select kua_permohonan.id as id,month(kua_permohonan.tarikh_permohonan)as bulan from kua_permohonan ");
	sb.append("where no_permohonan is not null ");
	
	if (!"".equalsIgnoreCase(tahun)){
		sb.append("and year(kua_permohonan.tarikh_permohonan) = '"+tahun+"') ");
	}else{
		sb.append("and year(kua_permohonan.tarikh_permohonan) = '2016') ");	
	}
	
	sb.append("permohonan ON permohonan.bulan=rujBulan.id ");
	sb.append("GROUP BY rujBulan.id ORDER BY rujBulan.id + 0");
	return sb.toString();
}
	
//	private String getSQL() {
//		String sql = "";
//
//		
//		sql = "SELECT (CASE"
//		      +" WHEN kekosongan = 0 THEN 'KOSONG'"
//		      +" WHEN kekosongan = 1 THEN 'BERPENGHUNI'"
//		      +" ELSE ''"
//		      +" END)"
//		      +" AS x,"
//		      +" count(id) AS y"
//		      +" FROM kua_kuarters WHERE kua_kuarters.id IS NOT NULL";
//
//		//PARAMETER
//		if (!"".equals(getParam("findKelasKuarters"))) {
//			sql = sql + " AND kua_kuarters.id_kelas_kuarters = '" + getParam("findKelasKuarters") + "'";
//		}
//		if (!"".equals(getParam("findLokasiKuarters"))) {
//			sql = sql + " AND kua_kuarters.id_lokasi_kuarters = '" + getParam("findLokasiKuarters") + "'";
//		}
//		
//			sql = sql +" GROUP BY kekosongan";
//		 
//			return sql;
//	}

	private String getPath() {
		return "bph/modules/eis/kuarters/laporanPermohonanBaruKuarters";
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
			List<Object> listLaporan = new ArrayList<Object>();
			ResultSet rs = stat.executeQuery(getSQL());
			while (rs.next()) {
				xml = xml + "<set label = '" + rs.getString("x")
						+ "' value = '" + rs.getString("y") + "' />";
				//LAPORAN STATISTIK
				EisLaporan laporan = new EisLaporan();
				laporan.setX(rs.getString("x"));
				laporan.setY(rs.getString("y"));
				//laporan.setNamaNegeri(rs.getString("namaNegeri"));
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
