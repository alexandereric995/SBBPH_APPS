package bph.laporan.rpp;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class LaporanDaftarMasukdanKeluar extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(LaporanDaftarMasukdanKeluar.class);
	public LaporanDaftarMasukdanKeluar() {
		
		
		super.setFolderName("rpp");
		super.setReportName("LaporanDaftarMasukdanKeluar");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
