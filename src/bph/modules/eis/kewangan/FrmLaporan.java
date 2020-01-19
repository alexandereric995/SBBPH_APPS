/**
 * 
 */
package bph.modules.eis.kewangan;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lebah.db.Db;
import lebah.portal.action.Command;
import lebah.portal.action.LebahModule;
import lebah.template.DbPersistence;

import org.apache.log4j.Logger;

import bph.entities.rpp.RppPeranginan;
import bph.modules.eis.EisLaporan;
import bph.utils.DataUtil;
import bph.utils.Util;

public class FrmLaporan extends LebahModule {

	/**
	 * 
	 */
	static Logger myLogger = Logger.getLogger(FrmLaporan.class);
	private static final long serialVersionUID = 1L;
	
	//private DataUtil dataUtil;
	private DbPersistence db = new DbPersistence();
	private DataUtil dataUtil = DataUtil.getInstance(db);

	String tajukLaporan;
	String XAxisName;
	String YAxisName;
	Map<String,Object> eisParam;
	
	@Override
	public String start() {
		context.put("jenisLaporan","");
		context.put("path", getPath());
		return getPath() + "/start.vm";
	}

	private String getTajukLaporan() {
		//String tajukLaporan = "Jumlah Hakmilik Mengikut Negeri";
		context.put("tajukLaporan", this.tajukLaporan);
		return tajukLaporan;
	}

	private String getXAxisName() {
		return this.XAxisName;
	}

	private String getYAxisName() {
		return this.YAxisName;
		//return "Jumlah Hakmilik";
	}

	private String getSQL() {
		
		String jenisLaporan = (String)eisParam.get("jenisLaporan");
		String dateStart = (String)eisParam.get("dateStart");
		String dateEnd = (String)eisParam.get("dateEnd");
				
		if ("kewangan1".equals(jenisLaporan)) {
			return getSQL_kewangan1(dateStart,dateEnd);
		} else if ("kewangan2".equals(jenisLaporan)) {
			String idPeranginan = (String)eisParam.get("idPeranginan");
			return getSQL_kewangan2(dateStart,dateEnd,idPeranginan);
		} else if ("kewangan3".equals(jenisLaporan)) {
			return getSQL_kewangan3(dateStart,dateEnd);
		} else if ("kewangan4".equals(jenisLaporan)) {
			return getSQL_kewangan4(dateStart,dateEnd);
		} else {
			//
		}
		return "";
	}
	
	//LAPORAN KUTIPAN HARIAN BPH KESELURUHAN
	public String getSQL_kewangan1(String dateStart,String dateEnd) {		
		String tahun = getParam("yearStart");
		StringBuffer sb = new StringBuffer();
		sb.append("select sum(kew_bayaran_resit.jumlah_amaun_bayaran) as y,");
		sb.append("month(kew_bayaran_resit.tarikh_resit)as x from kew_bayaran_resit where flag_void = 'T' ");
		if (!"".equalsIgnoreCase(tahun)){
			sb.append("and year(kew_bayaran_resit.tarikh_resit) = '"+tahun+"' ");
		}else{
			//DEFAULT TAHUN SEMASA
			sb.append("and year(kew_bayaran_resit.tarikh_resit) = '2016' ");	
		}
		sb.append("group by month(kew_bayaran_resit.tarikh_resit)");
		return sb.toString();
	}

	//LAPORAN KUTIPAN HARIAN RPP MENGIKUT CAWANGAN
	public String getSQL_kewangan2(String dateStart,String dateEnd,String idPeranginan) {
		String tahun = getParam("yearStart");
		String idRPP = getParam("idPeranginan");
		StringBuffer sb = new StringBuffer();
		sb.append("select month(kew_bayaran_resit.tarikh_bayaran) as x, ");
		sb.append("sum(kew_bayaran_resit.jumlah_amaun_bayaran) as y from kew_bayaran_resit join rpp_permohonan on kew_bayaran_resit.id_permohonan = rpp_permohonan.id join rpp_peranginan on rpp_permohonan.id_peranginan = rpp_peranginan.id ");
		sb.append("where kew_bayaran_resit.flag_void = 'T' and kew_bayaran_resit.tarikh_bayaran is not null ");
		if (!"".equalsIgnoreCase(tahun)){
			sb.append("and year(kew_bayaran_resit.tarikh_resit) = '"+tahun+"' ");
		}else{
			//DEFAULT TAHUN SEMASA
			sb.append("and year(kew_bayaran_resit.tarikh_resit) = '2016' ");	
		}
		if (!"0".equalsIgnoreCase(idRPP)){
			sb.append("and rpp_permohonan.id_peranginan = '"+idRPP+"' ");
		}else{
			
		}
		sb.append("group by  month(kew_bayaran_resit.tarikh_bayaran)");
		return sb.toString();
	}
	
	//LAPORAN KUTIPAN SEWAAN RUANG KOMERSIL BULANAN
	public String getSQL_kewangan3(String dateStart,String dateEnd) {
		StringBuffer sb = new StringBuffer();
		String tahun = getParam("yearStart");
		sb.append("select month(kew_bayaran_resit.tarikh_bayaran) as x, ");
		sb.append("SUM(kew_bayaran_resit.jumlah_amaun_bayaran)  as y ");
		sb.append("from kew_bayaran_resit join kew_resit_senarai_invois on kew_bayaran_resit.id = kew_resit_senarai_invois.id_bayaran_resit ");
		sb.append("where kew_resit_senarai_invois.flag_jenis_resit = 'DEPOSIT' and kew_bayaran_resit.tarikh_bayaran is not null ");
		if (!"".equalsIgnoreCase(tahun)){
			sb.append("and year(kew_bayaran_resit.tarikh_resit) = '"+tahun+"' ");
		}else{
			//DEFAULT TAHUN SEMASA
			sb.append("and year(kew_bayaran_resit.tarikh_resit) = '2016' ");	
		}	
		sb.append("group by  month(kew_bayaran_resit.tarikh_bayaran)");
//		StringBuffer sb = new StringBuffer();
//		sb.append("select date_format(A.tarikh_bayaran,'%m-%Y') as keterangan,date_format(A.tarikh_bayaran,'%m-%Y') as x, ");
//		sb.append("SUM(A.jumlah_amaun_bayaran)  as y ");
//		sb.append("from kew_bayaran_resit A join kew_resit_senarai_invois B on A.id = B.id_bayaran_resit ");
//		sb.append("JOIN kew_invois C on B.id_invois = C.id ");
//		sb.append("where C.id_kod_hasil = '92499' ");
//		sb.append("AND A.tarikh_bayaran BETWEEN str_to_date('08-2016','%m-%Y') AND str_to_date('12-2016','%m-%Y') ");
//		sb.append("group by x");
////		System.out.println(sb.toString());
		return sb.toString();
	}
	
	//LAPORAN PEMBAYARAN DEPOSIT BULANAN
	public String getSQL_kewangan4(String dateStart,String dateEnd) {
		StringBuffer sb = new StringBuffer();
		String tahun = getParam("yearStart");
		sb.append("select month(kew_bayaran_resit.tarikh_bayaran) as x, ");
		sb.append("SUM(kew_bayaran_resit.jumlah_amaun_bayaran)  as y ");
		sb.append("from kew_bayaran_resit join kew_resit_senarai_invois on kew_bayaran_resit.id = kew_resit_senarai_invois.id_bayaran_resit ");
		sb.append("where kew_resit_senarai_invois.flag_jenis_resit = 'DEPOSIT' and kew_bayaran_resit.tarikh_bayaran is not null ");
		if (!"".equalsIgnoreCase(tahun)){
			sb.append("and year(kew_bayaran_resit.tarikh_resit) = '"+tahun+"' ");
		}else{
			//DEFAULT TAHUN SEMASA
			sb.append("and year(kew_bayaran_resit.tarikh_resit) = '2016' ");	
		}	
		sb.append("group by  month(kew_bayaran_resit.tarikh_bayaran)");
		
//		StringBuffer sb = new StringBuffer();
//		sb.append("select date_format(A.tarikh_bayaran,'%m-%Y') as keterangan,date_format(A.tarikh_bayaran,'%m-%Y') as x,");
//		sb.append(" SUM(A.jumlah_amaun_bayaran)  as y ");
//		sb.append("from kew_bayaran_resit A join kew_resit_senarai_invois B on A.id = B.id_bayaran_resit ");
//		sb.append("where B.flag_jenis_resit = 'DEPOSIT'");
//		if (dateStart != "" && dateEnd != "") {
//			sb.append("AND A.tarikh_bayaran BETWEEN str_to_date('"+dateStart+"','%m-%Y') AND str_to_date('"+dateEnd+"','%m-%Y')");
//		}
//		sb.append("group by x");
////		System.out.println(sb.toString());
		return sb.toString();
	}

//	LAPORAN KUTIPAN BULANAN BPH KESELURUHAN
	
//	LAPORAN KUTIPAN BULANAN RPP/RUMAH TRANSIT 
	
//	LAPORAN KUTIPAN BULANAN RUANG KOMERSIAL 
	
//	LAPORAN KUTIPAN BULANAN DEPOSIT
	
//	LAPORAN KUTIPAN BULANAN BPH KESELURUHAN MENGIKUT JENIS KUTIPAN DI SEKSYEN PENGURUSAN RUMAH PERANGINAN

//	LAPORAN KUTIPAN BULANAN BPH KESELURUHAN MENGIKUT JENIS KUTIPAN DI SEKSYEN PENGURUSAN BANGUNAN KERAJAAN

//	LAPORAN KUTIPAN BULANAN BPH KESELURUHAN MENGIKUT JENIS KUTIPAN DI SEKSYEN PENGURUSAN KUARTERS

//	LAPORAN KUTIPAN BULANAN BPH KESELURUHAN MENGIKUT JENIS KUTIPAN DI UNIT NAZIRAN
	
	
	
	private String getPath() {
		return "bph/modules/eis/kewangan/laporan";
		
	}

	public String generateXML() {
		handleRequestParam();
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
					laporan.setKeterangan("Januari");
				}else if(rs.getString("x").equalsIgnoreCase("2")){
					laporan.setKeterangan("Februari");
				}else if(rs.getString("x").equalsIgnoreCase("3")){
					laporan.setKeterangan("Mac");
				}else if(rs.getString("x").equalsIgnoreCase("4")){
					laporan.setKeterangan("April");
				}else if(rs.getString("x").equalsIgnoreCase("5")){
					laporan.setKeterangan("Mei");
				}else if(rs.getString("x").equalsIgnoreCase("6")){
					laporan.setKeterangan("Jun");
				}else if(rs.getString("x").equalsIgnoreCase("7")){
					laporan.setKeterangan("Julai");
				}else if(rs.getString("x").equalsIgnoreCase("8")){
					laporan.setKeterangan("Ogos");
				}else if(rs.getString("x").equalsIgnoreCase("9")){
					laporan.setKeterangan("September");
				}else if(rs.getString("x").equalsIgnoreCase("10")){
					laporan.setKeterangan("October");
				}else if(rs.getString("x").equalsIgnoreCase("11")){
					laporan.setKeterangan("November");
				}else if(rs.getString("x").equalsIgnoreCase("12")){
					laporan.setKeterangan("December");
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
//		System.out.println("XML return=>"+xml);
		return xml;
	}
	
	/** START SENARAI TAB **/

	@Command("pilihLaporan")
	public String pilihLaporan() {
		String jenisLaporan = getParam("findJenisLaporan");
		context.put("jenisLaporan",jenisLaporan);
		//SET SELECTED TAB KEPADA STATISTIC
		String selectedTab = getParam("selectedTab");
		if ("".equals(selectedTab)) {
			selectedTab = "0";
		}
		context.put("selectedTab",selectedTab);
		context.put("path", getPath());
		
		List<RppPeranginan> listJenisPeranginan = dataUtil.getListPeranginanRpp();
		context.put("listJenisPeranginan", listJenisPeranginan);
		
		return getPath() + "/kriteriaParameter.vm";
	}
	
	@Command("janaLaporan")
	public String janaLaporan() {
		context.put("util", new Util());
		getStatistik();
		//SET SELECTED TAB KEPADA STATISTIC
		String selectedTab = getParam("selectedTab");
		if ("".equals(selectedTab)) {
			selectedTab = "0";
		}
		
		context.put("selectedTab",selectedTab);
		//context.put("selectedTab", getParam("selectedTab"));
		context.put("path", getPath());
		return getPath() + "/senaraiTab.vm";
	}
	
	public void handleRequestParam() {
		
		eisParam = new HashMap<String, Object>();
		
		String jenisLaporan = getParam("findJenisLaporan");
		String idPeranginan = getParam("idPeranginan");
		
		context.put("jenisLaporan",jenisLaporan);
		if ("kewangan1".equals(jenisLaporan)) {
			//LAPORAN KUTIPAN HARIAN BPH KESELURUHAN
			this.tajukLaporan = "LAPORAN KUTIPAN TAHUNAN BPH MENGIKUT BULAN";
			this.XAxisName = "BULAN";
			this.YAxisName = "JUMLAH";		
		} else if ("kewangan2".equals(jenisLaporan)) {
			this.tajukLaporan = "LAPORAN KUTIPAN TAHUNAN RPP MENGIKUT BULAN";
			this.XAxisName = "BULAN";
			this.YAxisName = "JUMLAH";
			eisParam.put("idPeranginan", idPeranginan);
		} else if ("kewangan3".equals(jenisLaporan)) {
			this.tajukLaporan = "LAPORAN KUTIPAN SEWAAN RUANG KOMERSIL MENGIKUT BULAN";
			this.XAxisName = "BULAN";
			this.YAxisName = "JUMLAH";
		} else if ("kewangan4".equals(jenisLaporan)) {
			this.tajukLaporan = "LAPORAN KUTIPAN PEMBAYARAN DEPOSIT TAHUNAN MENGIKUT BULAN";
			this.XAxisName = "BULAN";
			this.YAxisName = "JUMLAH";
		} else {
			this.tajukLaporan = "LAPORAN KUTIPAN";
			this.XAxisName = "KETERANGAN";
			this.YAxisName = "JUMLAH";
		}
		eisParam.put("jenisLaporan",jenisLaporan);
		eisParam.put("dateStart",getParam("dateStart"));
		eisParam.put("dateEnd",getParam("dateEnd"));
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
