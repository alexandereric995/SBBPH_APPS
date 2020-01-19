package bph.laporan.rpp;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class LaporanMaklumatPenghuniKeluar extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(LaporanMaklumatPenghuniKeluar.class);
	public LaporanMaklumatPenghuniKeluar() {
		
		
		super.setFolderName("rpp");
		super.setReportName("LaporanMaklumatPenghuniKeluar");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
