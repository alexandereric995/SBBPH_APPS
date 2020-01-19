package bph.laporan.bgs;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class BGSSuratLulusPermohonanRuangPejabat extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(BGSSuratLulusPermohonanRuangPejabat.class);
	public BGSSuratLulusPermohonanRuangPejabat() {
		
		
		super.setFolderName("bgs");
		super.setReportName("BGSSuratLulusPermohonanRuangPejabat");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
