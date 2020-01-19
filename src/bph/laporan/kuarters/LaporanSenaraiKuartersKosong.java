package bph.laporan.kuarters;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class LaporanSenaraiKuartersKosong extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(LaporanSenaraiKuartersKosong.class);
	public LaporanSenaraiKuartersKosong() {
		
		
		super.setFolderName("kuarters");
		super.setReportName("LaporanSenaraiKuartersKosong");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
