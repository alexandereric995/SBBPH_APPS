package bph.laporan.jrp;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class JRPBorangJRP extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(JRPBorangJRP.class);
	public JRPBorangJRP() {
		
		
		super.setFolderName("jrp");
		super.setReportName("JRPBorang1_arahanBorang");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
