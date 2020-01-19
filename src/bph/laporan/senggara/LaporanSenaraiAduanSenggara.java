package bph.laporan.senggara;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class LaporanSenaraiAduanSenggara extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(LaporanSenaraiAduanSenggara.class);
	public LaporanSenaraiAduanSenggara() {
		
		
		super.setFolderName("senggara");
		super.setReportName("LaporanSenaraiAduanSenggara");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
