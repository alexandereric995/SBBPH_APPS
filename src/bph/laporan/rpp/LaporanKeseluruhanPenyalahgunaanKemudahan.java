package bph.laporan.rpp;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class LaporanKeseluruhanPenyalahgunaanKemudahan extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(LaporanKeseluruhanPenyalahgunaanKemudahan.class);
	public LaporanKeseluruhanPenyalahgunaanKemudahan() {
		
		
		super.setFolderName("rpp");
		super.setReportName("LaporanKeseluruhanPenyalahgunaanKemudahan");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
