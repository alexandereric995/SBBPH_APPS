package bph.laporan.kontrak;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class LaporanDaftarBon extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(LaporanDaftarBon.class);
	public LaporanDaftarBon() {
		
		
		super.setFolderName("kontrak");
		super.setReportName("LaporanDaftarBon");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
