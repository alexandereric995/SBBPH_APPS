/**
 * 
 */
package bph.modules.eis.utiliti;

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
public class SenaraiPermohonanDewanMengikutJenisPermohonan extends LebahModule {

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
		context.put("selectDewan", dataUtil.getListDewan());
		
		context.put("xml", generateXML());

		context.put("util", new Util());
		context.put("selectedTab", 0);
		context.put("path", getPath());
		return getPath() + "/start.vm";
	}

	private String getTajukLaporan() {
		String tajukLaporan = "Bilangan Permohonan Dewan Mengikut Jenis Permohonan";
		context.put("tajukLaporan", tajukLaporan);
		return tajukLaporan;
	}

	private String getXAxisName() {
		return "Jenis Permohonan";
	}

	private String getYAxisName() {
		return "Jumlah Permohonan";
	}

	private String getSQL() {
		String sql = "";

//		sql = "select jenis_permohonan as x, count(id) as y FROM util_permohonan"
//				+ " where id_gelanggang is null"
//				+ " group by jenis_permohonan"
//				+ " order by id";
//		System.out.println(sql);
//		return sql;
		
		
		
		sql = "select 'WALK-IN' as x, count(id) as y"
		+" from util_permohonan where jenis_permohonan = 'WALKIN' and util_permohonan.id_gelanggang is null"
		+" union"
		+" select 'ONLINE' as x, count(id) as y"
		+" from util_permohonan where jenis_permohonan = 'ONLINE' and util_permohonan.id_gelanggang is null";
		System.out.println(sql);
		return sql;

	}

	private String getPath() {
		return "bph/modules/eis/utiliti/senaraiPermohonanDewanMengikutJenisPermohonan";
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
