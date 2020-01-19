package bph.laporan.pembangunan;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class MaklumatTanah extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(MaklumatTanah.class);
	public MaklumatTanah() {
		
		
		super.setFolderName("pembangunan");
		super.setReportName("MaklumatTanah");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
