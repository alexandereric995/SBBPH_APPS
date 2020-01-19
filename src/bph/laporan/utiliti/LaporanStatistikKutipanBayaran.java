package bph.laporan.utiliti;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class LaporanStatistikKutipanBayaran extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(LaporanStatistikKutipanBayaran.class);
	public LaporanStatistikKutipanBayaran() {
		
		
		super.setFolderName("utiliti");
		super.setReportName("LaporanStatistikKutipanBayaran");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
