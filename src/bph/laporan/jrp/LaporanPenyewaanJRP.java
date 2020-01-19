package bph.laporan.jrp;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class LaporanPenyewaanJRP extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(LaporanPenyewaanJRP.class);
	public LaporanPenyewaanJRP() {
		
		
		super.setFolderName("jrp");
		super.setReportName("LaporanPenyewaanJRP");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
