package bph.laporan.qtr;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class QTRSuratTawaranKuartersBadanBerkanun extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(QTRSuratTawaranKuartersBadanBerkanun.class);
	public QTRSuratTawaranKuartersBadanBerkanun() {
		
		
		super.setFolderName("kuarters");
		super.setReportName("QTRSuratTawaranKuartersBadanBerkanun");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
