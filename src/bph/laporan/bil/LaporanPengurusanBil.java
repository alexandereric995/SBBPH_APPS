package bph.laporan.bil;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class LaporanPengurusanBil extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(LaporanPengurusanBil.class);
	public LaporanPengurusanBil() {
		
		
		super.setFolderName("bil");
		super.setReportName("LaporanPengurusanBil");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
