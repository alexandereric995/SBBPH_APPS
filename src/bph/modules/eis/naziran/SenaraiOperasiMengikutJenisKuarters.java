/**
 * 
 */
package bph.modules.eis.naziran;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import lebah.db.Db;
import lebah.portal.action.Command;
import lebah.portal.action.LebahModule;
import lebah.template.DbPersistence;
import bph.entities.kod.JenisKuartersUtk;
import bph.entities.kod.JenisOperasiUtk;
import bph.entities.kod.KawasanUtk;
import bph.entities.kod.ZonUtk;
import bph.modules.eis.EisLaporan;
import bph.utils.DataUtil;
import bph.utils.Util;

/**
 * @author rzai
 * @author zul a.k.a matjuju 2016 
 */
public class SenaraiOperasiMengikutJenisKuarters extends LebahModule {	
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
		context.put("selectBulan",dataUtil.getListBulan());
		context.put("xml", generateXML());
		context.put("util", new Util());
		context.put("selectedTab", 0);
		
		context.put("laporan", "jenis_kuarters");
		
		List<JenisKuartersUtk> listJenisKuarters = dataUtil.getListJenisKuartersUtk();
		context.put("listJenisKuarters", listJenisKuarters);
		List<JenisOperasiUtk> listJenisOperasi = dataUtil.getListJenisOperasiUtk();
		context.put("jenisOperasi", listJenisOperasi);
		List<KawasanUtk> listKawasan = dataUtil.getListKawasanUtk();
		context.put("kawasan", listKawasan);
		List<ZonUtk> listZon = dataUtil.getListZonUtk();
		context.put("zon", listZon);
		
		context.put("path", getPath());
		return getPath() + "/start.vm";
	}

	private String getTajukLaporan() {
		String tajukLaporan = "Bilangan Operasi Mengikut Jenis Kuarters";
		context.put("tajukLaporan", tajukLaporan);
		return tajukLaporan;
	}

	private String getXAxisName() {
		return "Jenis Kuarters";
	}

	private String getYAxisName() {
		return "Bilangan Operasi";
	}

	private String getSQL() {
		String sql = "";

		sql = "SELECT ruj_jenis_kuarters_utk.keterangan AS x,"
				+ " count(operasi.id_operasi) AS y"
				+ " FROM    ruj_jenis_kuarters_utk" + " LEFT JOIN"
				+ " (SELECT utk_operasi.id AS id_operasi,"
				+ " utk_operasi.id_jenis_kuarters AS id_jenis_kuarters"
				+ " FROM utk_operasi) operasi"
				+ " ON operasi.id_jenis_kuarters = ruj_jenis_kuarters_utk.id"
				+ " GROUP BY ruj_jenis_kuarters_utk.id"
				+ " ORDER BY ruj_jenis_kuarters_utk.keterangan ASC";
		System.out.println(sql);
		return sql;

	}

	private String getPath() {
		return "bph/modules/eis/naziran/laporanKeseluruhanNaziran";
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
