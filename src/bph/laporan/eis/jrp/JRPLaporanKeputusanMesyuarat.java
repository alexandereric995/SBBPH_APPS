package bph.laporan.eis.jrp;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class JRPLaporanKeputusanMesyuarat extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(JRPLaporanKeputusanMesyuarat.class);
	
	public JRPLaporanKeputusanMesyuarat() {
	
		super.setFolderName("eis"); // nama folder java
		super.setReportName("B-LaporanKeputusanMesyuarat"); // nama file jasper
		
	}
		
	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
