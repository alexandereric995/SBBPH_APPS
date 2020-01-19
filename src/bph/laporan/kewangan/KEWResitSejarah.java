package bph.laporan.kewangan;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class KEWResitSejarah extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(KEWResitSejarah.class);
	public KEWResitSejarah() {
		

		super.setFolderName("kewangan");
		super.setReportName("KEWResitSejarah");
	}

	@Override
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
		
		
	}
	
}
