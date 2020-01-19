package bph.laporan.pembangunan;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class LaporanDinamik extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(LaporanDinamik.class);
	public LaporanDinamik() {
		
		
		super.setFolderName("pembangunan");
		super.setReportName("LaporanDinamik");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
