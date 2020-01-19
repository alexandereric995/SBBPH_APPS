/**
 * 
 */
package bph.modules.eis.pembangunan;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import lebah.db.Db;
import lebah.portal.action.Command;
import lebah.portal.action.LebahModule;
import lebah.template.DbPersistence;
import bph.entities.kod.Daerah;
import bph.modules.eis.EisLaporan;
import bph.utils.DataUtil;
import bph.utils.Util;

/**
 * @author mohd faizal
 * 
 */
public class SenaraiTanahKosongBelumDimajukan extends LebahModule {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DbPersistence db = new DbPersistence();
	private DataUtil dataUtil = DataUtil.getInstance(db);

	@Override
	public String start() {
		// TODO Auto-generated method stub
		DbPersistence db = new DbPersistence();
		dataUtil = DataUtil.getInstance(db);
		context.put("xml", generateXML());
		context.put("selectNegeri", dataUtil.getListNegeri());
//		context.put("selectDaerah", dataUtil.getListDaerah());
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
		String tajukLaporan = "Senarai Tanah Kosong Belum Dimajukan";
		context.put("tajukLaporan", tajukLaporan);
		return tajukLaporan;
	}

	private String getXAxisName() {
		return "Negeri";
	}

	private String getYAxisName() {
		return "Bilangan";
	}

	private String getSQL() {
		String sql = "";

		sql = "SELECT ruj_negeri.keterangan AS namaNegeri, ruj_negeri.abbrev AS x, count(hakmilik.id_hakmilik) AS y"
				+ " FROM ruj_negeri"
				+ " LEFT JOIN"
				+ " (SELECT dev_hakmilik.id AS id_hakmilik, ruj_daerah.id_negeri AS id_negeri"
				+ " FROM dev_hakmilik LEFT JOIN ruj_mukim ON dev_hakmilik.id_mukim = ruj_mukim.id"
				+ " LEFT JOIN ruj_daerah ON ruj_mukim.id_daerah = ruj_daerah.id"
				+ " WHERE dev_hakmilik.id IS NOT NULL";
				
				//PARAMETER
				if (!"".equals(getParam("findJenisHakmilik"))) {
					sql = sql + " AND dev_hakmilik.id_jenishakmilik = '" + getParam("findJenisHakmilik") + "'";
				}
				if (!"".equals(getParam("findJenisLot"))) {
					sql = sql + " AND dev_hakmilik.id_lot = '" + getParam("findJenisLot") + "'";
				}
				if (!"".equals(getParam("findStatusDaftar"))) {
					sql = sql + " AND dev_hakmilik.status_daftar = '" + getParam("findStatusDaftar") + "'";
				}
				
				sql = sql + " ) hakmilik"
				+ " ON hakmilik.id_negeri = ruj_negeri.id"
				+ " GROUP BY ruj_negeri.id"
				+ " ORDER BY abbrev ASC";
		return sql;
	}

	private String getPath() {
		return "bph/modules/eis/pembangunan/senaraiTanahKosongBelumDimajukan";
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
				laporan.setNamaNegeri(rs.getString("namaNegeri"));
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
	
	/** START DROP DOWN LIST **/
	@Command("findDaerah")
	public String findDaerah() throws Exception {	
		
		String idNegeri = "0";
		if (getParam("findNegeri").trim().length() > 0)
			idNegeri = getParam("findNegeri");
		List<Daerah> list = dataUtil.getListDaerah(idNegeri);
		context.put("selectDaerah", list);
		
		return getPath() + "/findDaerah.vm";
	}
}
