package bph.laporan.eis;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class LaporanKehadiranRppHadir extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(LaporanKehadiranRppHadir.class);
	public LaporanKehadiranRppHadir() {
		
		
		super.setFolderName("eis");
		super.setReportName("LAPORANKEHADIRANRPP_HADIR");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
