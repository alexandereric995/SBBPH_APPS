package bph.laporan.pembangunan;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class LaporanSenaraiHakmilik extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(LaporanSenaraiHakmilik.class);
	public LaporanSenaraiHakmilik() {
		
		
		super.setFolderName("pembangunan");
		super.setReportName("LaporanSenaraiHakmilik");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
