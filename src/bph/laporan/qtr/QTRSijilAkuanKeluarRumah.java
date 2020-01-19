package bph.laporan.qtr;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class QTRSijilAkuanKeluarRumah extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(QTRSijilAkuanKeluarRumah.class);
	public QTRSijilAkuanKeluarRumah() {
		
		
		super.setFolderName("kuarters");
		super.setReportName("QTRSijilAkuanKeluarRumah");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
