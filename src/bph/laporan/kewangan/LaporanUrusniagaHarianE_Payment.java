package bph.laporan.kewangan;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class LaporanUrusniagaHarianE_Payment extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(LaporanUrusniagaHarianE_Payment.class);
	public LaporanUrusniagaHarianE_Payment() {
		
		
		super.setFolderName("kewangan");
		super.setReportName("LaporanUrusniagaHarianE_Payment");
		
	}

	@Override
	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
