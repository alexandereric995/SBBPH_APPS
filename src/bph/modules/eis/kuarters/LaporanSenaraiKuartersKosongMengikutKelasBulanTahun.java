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
public class LaporanSenaraiKuartersKosongMengikutKelasBulanTahun extends LebahModule {

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
		String tajukLaporan = "Laporan Bilangan Kuarters Kosong Mengikut Bulan Dan Tahun";
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
	String kelas = getParam("findKelasKuarters");
	StringBuffer sb = new StringBuffer();
	sb.append("SELECT rujBulan.keterangan AS x, count(senggara.id) as y FROM ruj_bulan rujBulan ");
	sb.append("LEFT JOIN ");
	sb.append("(select senggara.id as id, month(senggara.tarikh_serah_kunci)as bulan, ");
	sb.append("senggara.id_kuarters as idKuarters from mtn_kuarters senggara ");
	sb.append("LEFT JOIN kua_kuarters kuarters ON senggara.id_kuarters=kuarters.id ");
	sb.append("where id_kuarters is not null ");
	
	if (!"".equalsIgnoreCase(kelas)){
		sb.append("and kuarters.id_kelas_kuarters = '"+kelas+"' ");
	}
	
	if (!"".equalsIgnoreCase(tahun)){
		sb.append("and year(senggara.tarikh_serah_kunci) = '"+tahun+"') ");
	}else{
		sb.append("and year(senggara.tarikh_serah_kunci) = '2016') ");	
	}
	
	sb.append("senggara ON senggara.bulan=rujBulan.id ");
	sb.append("GROUP BY rujBulan.id ORDER BY rujBulan.id + 0");
	return sb.toString();
}

//	SELECT rujBulan.keterangan AS x, count(senggara.id) as y FROM ruj_bulan rujBulan
//	LEFT JOIN 
//	(select senggara.id as id, month(senggara.tarikh_serah_kunci)as bulan, 
//	senggara.id_kuarters as idKuarters from mtn_kuarters senggara
//	LEFT JOIN kua_kuarters kuarters ON senggara.id_kuarters=kuarters.id
//	where id_kuarters is not null and kuarters.id_kelas_kuarters='A'
//	and year(senggara.tarikh_serah_kunci) = '2016') senggara
//	ON senggara.bulan=rujBulan.id
//	GROUP BY rujBulan.id ORDER BY rujBulan.id + 0
	
	private String getPath() {
		return "bph/modules/eis/kuarters/laporanSenaraiKuartersKosong";
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
