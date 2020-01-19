package bph.laporan.jrp;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class LaporanMaklumatRuang extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(LaporanMaklumatRuang.class);
	public LaporanMaklumatRuang() {
		
		
		super.setFolderName("jrp");
		super.setReportName("LaporanMaklumatRuang");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
