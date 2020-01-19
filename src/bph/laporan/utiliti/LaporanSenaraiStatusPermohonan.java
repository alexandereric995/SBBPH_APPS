package bph.laporan.utiliti;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class LaporanSenaraiStatusPermohonan extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(LaporanSenaraiStatusPermohonan.class);
	public LaporanSenaraiStatusPermohonan() {
		
		
		super.setFolderName("utiliti");
		super.setReportName("LaporanSenaraiStatusPermohonan");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
