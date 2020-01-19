package bph.laporan.bgs;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class LaporanTerimaanPermohonanRuangPejabat extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(LaporanTerimaanPermohonanRuangPejabat.class);
	public LaporanTerimaanPermohonanRuangPejabat() {
		
		
		super.setFolderName("bgs");
		super.setReportName("LaporanTerimaanPermohonanRuangPejabat");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
