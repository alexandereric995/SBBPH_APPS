package bph.laporan.eis.pembangunan;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class TNH_LaporanDinamik extends ReportServlet{

	static Logger myLog = Logger.getLogger(TNH_LaporanDinamik.class);
	
	public TNH_LaporanDinamik() {
	
		super.setFolderName("eis"); // nama folder java
		super.setReportName("TNH_LaporanDinamik"); // nama file jasper
		
	}
	
	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
