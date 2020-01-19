package bph.laporan.jrp;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class LaporanMaklumatPemohon extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(LaporanMaklumatPemohon.class);
	public LaporanMaklumatPemohon() {
		
		
		super.setFolderName("jrp");
		super.setReportName("LaporanMaklumatPemohonPenyewa");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
