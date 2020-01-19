package bph.laporan.jrp;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class LaporanSenaraiPemohon extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(LaporanSenaraiPemohon.class);
	public LaporanSenaraiPemohon() {
		
		
		super.setFolderName("jrp");
		super.setReportName("LaporanSenaraiPemohonPenyewa");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
