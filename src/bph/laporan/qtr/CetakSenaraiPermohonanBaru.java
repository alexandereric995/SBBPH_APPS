package bph.laporan.qtr;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class CetakSenaraiPermohonanBaru extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(CetakSenaraiPermohonanBaru.class);
	public CetakSenaraiPermohonanBaru() {
		
		
		super.setFolderName("kuarters");
		super.setReportName("CetakSenaraiPermohonanBaru");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
