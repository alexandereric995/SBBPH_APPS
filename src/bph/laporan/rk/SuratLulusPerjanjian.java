package bph.laporan.rk;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class SuratLulusPerjanjian extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(SuratLulusPerjanjian.class);
	public SuratLulusPerjanjian() {
		
		
		super.setFolderName("rk");
		super.setReportName("SuratLulusPerjanjian");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
