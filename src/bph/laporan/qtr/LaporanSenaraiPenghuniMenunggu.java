package bph.laporan.qtr;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class LaporanSenaraiPenghuniMenunggu extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(LaporanSenaraiPenghuniMenunggu.class);
	public LaporanSenaraiPenghuniMenunggu() {
		
		
		super.setFolderName("kuarters");
		super.setReportName("LaporanSenaraiMenungguKuarters");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
