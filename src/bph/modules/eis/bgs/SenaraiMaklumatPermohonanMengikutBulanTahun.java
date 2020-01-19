/**
 * 
 */
package bph.modules.eis.bgs;

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
 * Laporan senarai maklumat pemohon/penyewa baru / tambah ruang / pindah / pelanjutan / pelanjutan lewat  J/K Ruang Pejabat mengikut bulan / tahun.

 */
public class SenaraiMaklumatPermohonanMengikutBulanTahun extends LebahModule {

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
		context.put("laporan", "maklumat_permohonan");
		return getPath() + "/start.vm";
	}

	private String getTajukLaporan() {
		String tajukLaporan = "Senarai Maklumat Permohonan JRP Mengikut Status / Bulan / Tahun";
		context.put("tajukLaporan", tajukLaporan);
		return tajukLaporan;
	}

	private String getXAxisName() {
		return "Status / Bulan / Tahun";
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

	private String getPath() {
		return "bph/modules/eis/bgs/laporanKeseluruhanBangunanGunaSama";
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
				if(rs.getString("x").equalsIgnoreCase("1")){
					laporan.setKeterangan("JANUARI");
				}else if(rs.getString("x").equalsIgnoreCase("2")){
					laporan.setKeterangan("FEBRUARI");
				}else if(rs.getString("x").equalsIgnoreCase("3")){
					laporan.setKeterangan("MAC");
				}else if(rs.getString("x").equalsIgnoreCase("4")){
					laporan.setKeterangan("APRIL");
				}else if(rs.getString("x").equalsIgnoreCase("5")){
					laporan.setKeterangan("MEI");
				}else if(rs.getString("x").equalsIgnoreCase("6")){
					laporan.setKeterangan("JUN");
				}else if(rs.getString("x").equalsIgnoreCase("7")){
					laporan.setKeterangan("JULAI");
				}else if(rs.getString("x").equalsIgnoreCase("8")){
					laporan.setKeterangan("OGOS");
				}else if(rs.getString("x").equalsIgnoreCase("9")){
					laporan.setKeterangan("SEPTEMBER");
				}else if(rs.getString("x").equalsIgnoreCase("10")){
					laporan.setKeterangan("OKTOBER");
				}else if(rs.getString("x").equalsIgnoreCase("11")){
					laporan.setKeterangan("NOVEMBER");
				}else if(rs.getString("x").equalsIgnoreCase("12")){
					laporan.setKeterangan("DISEMBER");
				}
				//laporan.setKeterangan(rs.getString("keterangan"));
				laporan.setAbbrev(rs.getString("x"));
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
