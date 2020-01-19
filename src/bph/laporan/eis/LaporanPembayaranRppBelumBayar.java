package bph.laporan.eis;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class LaporanPembayaranRppBelumBayar extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(LaporanPembayaranRppBelumBayar.class);
	public LaporanPembayaranRppBelumBayar() {
		
		
		super.setFolderName("eis");
		super.setReportName("LAPORANPEMBAYARANRPP_BELUMBAYAR");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
