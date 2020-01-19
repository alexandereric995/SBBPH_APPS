package bph.laporan.rk;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class SuratSetujuTerimaTawaranBulanan extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(SuratSetujuTerimaTawaranBulanan.class);
	public SuratSetujuTerimaTawaranBulanan() {
		
		
		super.setFolderName("rk");
		super.setReportName("SuratSetujuTerimaTawaranBulanan");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
