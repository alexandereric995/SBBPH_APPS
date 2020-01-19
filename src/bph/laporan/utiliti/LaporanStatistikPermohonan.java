package bph.laporan.utiliti;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class LaporanStatistikPermohonan extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(LaporanStatistikPermohonan.class);
	public LaporanStatistikPermohonan() {
		
		
		super.setFolderName("utiliti");
		super.setReportName("LaporanStatistikPermohonan");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
