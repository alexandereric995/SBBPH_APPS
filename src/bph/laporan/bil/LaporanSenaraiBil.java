package bph.laporan.bil;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class LaporanSenaraiBil extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(LaporanSenaraiBil.class);
	public LaporanSenaraiBil() {
		
		
		super.setFolderName("bil");
		super.setReportName("LaporanSenaraiBil");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
