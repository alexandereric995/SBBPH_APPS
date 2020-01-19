package bph.laporan.pembangunan;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class LaporanTanah extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(LaporanTanah.class);
	public LaporanTanah() {
		
		
		super.setFolderName("pembangunan");
		super.setReportName("LaporanTanah");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
