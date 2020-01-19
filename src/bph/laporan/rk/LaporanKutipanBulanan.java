package bph.laporan.rk;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class LaporanKutipanBulanan extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(LaporanKutipanBulanan.class);
	public LaporanKutipanBulanan() {		
		
		super.setFolderName("rk");
		super.setReportName("LaporanKutipanBulanan");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}	
}
