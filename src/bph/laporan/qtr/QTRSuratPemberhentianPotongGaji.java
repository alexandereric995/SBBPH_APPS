package bph.laporan.qtr;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class QTRSuratPemberhentianPotongGaji extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(QTRSuratPemberhentianPotongGaji.class);
	public QTRSuratPemberhentianPotongGaji() {
		
		
		super.setFolderName("kuarters");
		super.setReportName("QTRSuratPemberhentianPotongGaji");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
