package bph.laporan.bgs;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class BGSSuratLulusPermohonanUbahsuaiRuangPejabat extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(BGSSuratLulusPermohonanUbahsuaiRuangPejabat.class);
	public BGSSuratLulusPermohonanUbahsuaiRuangPejabat() {
		
		
		super.setFolderName("bgs");
		super.setReportName("BGSSuratLulusPermohonanUbahsuaiRuangPejabat");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
