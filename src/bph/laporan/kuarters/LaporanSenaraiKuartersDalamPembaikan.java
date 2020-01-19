package bph.laporan.kuarters;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class LaporanSenaraiKuartersDalamPembaikan extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(LaporanSenaraiKuartersDalamPembaikan.class);
	public LaporanSenaraiKuartersDalamPembaikan() {
		
		
		super.setFolderName("kuarters");
		super.setReportName("LaporanSenaraiKuartersDalamPembaikan");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
