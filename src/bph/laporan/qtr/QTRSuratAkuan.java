package bph.laporan.qtr;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class QTRSuratAkuan extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(QTRSuratAkuan.class);
	public QTRSuratAkuan() {
		
		
		super.setFolderName("kuarters");
		super.setReportName("QTRSuratAkuan");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
