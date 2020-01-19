package bph.laporan.qtr;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class QTRSuratTawaranKuartersTukarNamaEPW extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(QTRSuratTawaranKuartersTukarNamaEPW.class);
	public QTRSuratTawaranKuartersTukarNamaEPW() {
		
		
		super.setFolderName("kuarters");
		super.setReportName("QTRSuratTawaranKuartersTukarNamaEPW");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
