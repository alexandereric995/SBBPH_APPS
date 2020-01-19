package bph.laporan.qtr;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class CetakSenaraiMenungguBersyarat extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(CetakSenaraiMenungguBersyarat.class);
	public CetakSenaraiMenungguBersyarat() {
		
		
		super.setFolderName("kuarters");
		super.setReportName("CetakSenaraiMenungguBersyarat");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
