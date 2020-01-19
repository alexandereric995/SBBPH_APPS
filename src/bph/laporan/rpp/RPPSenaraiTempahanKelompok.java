package bph.laporan.rpp;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class RPPSenaraiTempahanKelompok extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(RPPSenaraiTempahanKelompok.class);
	public RPPSenaraiTempahanKelompok() {
		
		
		super.setFolderName("rpp");
		super.setReportName("RPPSenaraiTempahanKelompok");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
