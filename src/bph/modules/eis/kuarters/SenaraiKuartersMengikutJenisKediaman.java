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
import portal.module.Util;
import bph.modules.eis.EisLaporan;
import bph.utils.DataUtil;

/**
 * @author mohd faizal
 * 
 */
public class SenaraiKuartersMengikutJenisKediaman extends LebahModule {

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
		String tajukLaporan = "Bilangan Kuarters Mengikut Jenis Kediaman";
		context.put("tajukLaporan", tajukLaporan);
		return tajukLaporan;
	}

	private String getXAxisName() {
		return "Jenis Kediaman";
	}

	private String getYAxisName() {
		return "Bilangan Kuarters";
	}

	private String getSQL() {
		String sql = "";

		sql = "SELECT ruj_jenis_kediaman.keterangan AS x, count(kua_kuarters.id) as y"
				+ " FROM ruj_jenis_kediaman, kua_kuarters"
				+ " WHERE kua_kuarters.id_jenis_kediaman = ruj_jenis_kediaman.id";
		//PARAMETER
		if (!"".equals(getParam("findKelasKuarters"))) {
			sql = sql + " AND kua_kuarters.id_kelas_kuarters = '" + getParam("findKelasKuarters") + "'";
		}
		if (!"".equals(getParam("findLokasiKuarters"))) {
			sql = sql + " AND kua_kuarters.id_lokasi_kuarters = '" + getParam("findLokasiKuarters") + "'";
		}
		
			sql = sql + " GROUP BY ruj_jenis_kediaman.id ORDER BY ruj_jenis_kediaman.keterangan ASC";
		  
				
		return sql;
	}

	private String getPath() {
		return "bph/modules/eis/kuarters/senaraiKuartersMengikutJenisKediaman";
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
	public String getStatistik() 
	{
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
