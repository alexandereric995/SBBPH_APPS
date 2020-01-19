package bph.laporan.eis;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class LaporanKutipanBulananNaziran2 extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(LaporanKutipanBulananNaziran2.class);
	public LaporanKutipanBulananNaziran2() {
		
		
		super.setFolderName("eis");
		super.setReportName("LaporanKutipanBulananNaziran_2");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
