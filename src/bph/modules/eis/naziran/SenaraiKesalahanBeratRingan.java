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
 * 
 */
public class SenaraiKesalahanBeratRingan extends LebahModule {
	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;
	@Override
	public String start() {
		DbPersistence db = new DbPersistence();
		dataUtil = DataUtil.getInstance(db);		
		context.put("selectBulan",dataUtil.getListBulan());
		context.put("xml", generateXML());
		context.put("util", new Util());
		context.put("selectedTab", 0);
		
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
		String tajukLaporan = "Bilangan Kesalahan Berat/Ringan";
		context.put("tajukLaporan", tajukLaporan);
		return tajukLaporan;
	}

	private String getXAxisName() {
		return "Jenis Kesalahan";
	}

	private String getYAxisName() {
		return "Jumlah Kesalahan";
	}

	private String getSQL() {
		String sql = "";

		sql = "select 'KESALAHAN BERAT' as x, count(utk_kesalahan.id) as y"
				+ " from utk_kesalahan, ruj_jenis_pelanggaran_syarat_utk where utk_kesalahan.id = ruj_jenis_pelanggaran_syarat_utk.id and ruj_jenis_pelanggaran_syarat_utk.flag_kes = '2'"
				+ " union"
				+ " select 'KESALAHAN RINGAN' as x, count(utk_kesalahan.id) as y"
				+ " from utk_kesalahan, ruj_jenis_pelanggaran_syarat_utk where utk_kesalahan.id = ruj_jenis_pelanggaran_syarat_utk.id and ruj_jenis_pelanggaran_syarat_utk.flag_kes = '1'";
		System.out.println("PRINT ==== 1 ::: " + sql);
		//PARAMETER
		if (!"".equals(getParam("findBulan"))) {
			sql = sql + " AND utk_kesalahan.tarikh = '" + getParam("findBulan") + "'";
		}
		System.out.println("PRINT ==== 2 ::: " + sql);
		
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
