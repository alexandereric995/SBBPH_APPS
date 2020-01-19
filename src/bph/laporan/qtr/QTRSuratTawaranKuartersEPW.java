package bph.laporan.qtr;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class QTRSuratTawaranKuartersEPW extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(QTRSuratTawaranKuartersEPW.class);
	public QTRSuratTawaranKuartersEPW() {
		
		
		super.setFolderName("kuarters");
		super.setReportName("QTRSuratTawaranKuartersEPW");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
