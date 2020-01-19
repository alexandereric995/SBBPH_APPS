package bph.laporan.pro;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.entities.kod.JenisAduan;
import bph.laporan.ReportServlet;
import db.persistence.MyPersistence;

public class LaporanStatistikTerimaanAduan extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(LaporanStatistikTerimaanAduan.class);
	public LaporanStatistikTerimaanAduan() {
		
		
		super.setFolderName("pro");
		super.setReportName("LaporanStatistikTerimaanAduan");
		
	}

	@Override
	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
		
//		//ADD BY PEJE 15022016 - HANTAR CURRENT YEAR TO REPORT PARAM
//
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(new Date());
//		
//	
//		if (parameters.get("tahun").equals(cal.get(Calendar.YEAR))) {
//			parameters.put("limitBulan", cal.get(Calendar.MONTH) + 1);
//		} else {
//			parameters.put("limitBulan", "12");
//		}
		
		
		try {
			MyPersistence mp = new MyPersistence();
			JenisAduan jenisAduan = (JenisAduan) mp.find(JenisAduan.class, parameters.get("idJenisAduan"));
			if (jenisAduan != null) {
				parameters.put("jenisAduan", jenisAduan.getKeterangan());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			
		}
		
	}
	
}
