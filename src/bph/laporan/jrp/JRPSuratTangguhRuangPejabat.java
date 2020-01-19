package bph.laporan.jrp;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class JRPSuratTangguhRuangPejabat extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(JRPSuratTangguhRuangPejabat.class);
	public JRPSuratTangguhRuangPejabat() {
		
		
		super.setFolderName("jrp");
		super.setReportName("JRPSuratTangguhPermohonanRuangPejabat");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
