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
 * @author MATJUJU
 * 
 */
public class SenaraiKuartersPermohonanBaru extends LebahModule {

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
		
		context.put("selectKelasKuarters", dataUtil.getListKelasPeranginan());
		context.put("selectLokasiKuarters", dataUtil.getListLokasiPermohonan());
		
		context.put("util", new Util());
		context.put("selectedTab", 0);
		context.put("path", getPath());
		return getPath() + "/start.vm";
	}

	private String getTajukLaporan() {
		String tajukLaporan = "Bilangan Kuarters Dalam Penyelenggaraan";
		context.put("tajukLaporan", tajukLaporan);
		return tajukLaporan;
	}

	private String getXAxisName() {
		return "Status Kuarters";
	}

	private String getYAxisName() {
		return "Jumlah Kuarters";
	}

	private String getSQL() {
		
		String sql = "";
		sql = "SELECT (CASE"
		      +" WHEN status = 1419483289678 THEN 'PERMOHONAN BARU'"
		      +" WHEN status = 1419601227595 THEN 'PERMOHONAN DILULUSKAN'"
		      +" WHEN status = 1419601227598 THEN 'PERMOHONAN DITOLAK'"
		      +" ELSE ''"
		      +" END)"
		      +" AS x,"
		      +" count(status) AS y"
		      +" FROM kua_permohonan WHERE kua_permohonan.status in('1419483289678','1419601227595','1419601227598')";
		
		//PARAMETER
//		if (!"".equals(getParam("findKelasKuarters"))) {
//			sql = sql + " AND kua_kuarters.id_kelas_kuarters = '" + getParam("findKelasKuarters") + "'";
//		}

		if (!"".equals(getParam("findLokasiKuarters"))) {
			sql = sql + "AND kua_permohonan.id_lokasi_permohonan = '" + getParam("findLokasiKuarters") + "'";
		}
		
		sql = sql +" GROUP BY status";
//		System.out.println("PRINT ======= :" + sql);
		return sql;
	}

	private String getPath() {
		return "bph/modules/eis/kuarters/senaraiKuartersPermohonanBaru";
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
