package bph.laporan.rpp;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class LaporanSenaraiHitamPemohon extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(LaporanSenaraiHitamPemohon.class);
	public LaporanSenaraiHitamPemohon() {
		
		
		super.setFolderName("rpp");
		super.setReportName("LaporanSenaraiHitamPemohon");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
