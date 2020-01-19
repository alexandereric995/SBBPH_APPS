package bph.laporan.senggara;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class LaporanStatistikSumberAduan extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(LaporanStatistikSumberAduan.class);
	public LaporanStatistikSumberAduan() {
		
		
		super.setFolderName("senggara");
		super.setReportName("LaporanStatistikSumberAduan");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
