package bph.laporan.kontrak;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class LaporanSenaraiKontrak extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(LaporanSenaraiKontrak.class);
	public LaporanSenaraiKontrak() {
		
		
		super.setFolderName("kontrak");
		super.setReportName("LaporanSenaraiKontrak");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
