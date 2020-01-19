package bph.laporan.kewangan;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class KEWPenyataPemungut extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(KEWPenyataPemungut.class);
	public KEWPenyataPemungut() {
		
		
		super.setFolderName("kewangan");
		super.setReportName("KEWPenyataPemungut");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
