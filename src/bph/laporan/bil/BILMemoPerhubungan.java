package bph.laporan.bil;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class BILMemoPerhubungan extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(BILMemoPerhubungan.class);
	public BILMemoPerhubungan() {
		
		
		super.setFolderName("bil");
		super.setReportName("BILMemoPerhubungan");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
