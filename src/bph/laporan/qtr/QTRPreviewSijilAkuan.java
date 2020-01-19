package bph.laporan.qtr;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;
import db.persistence.MyPersistence;

public class QTRPreviewSijilAkuan extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(QTRPreviewSijilAkuan.class);
	private MyPersistence mp;
	public QTRPreviewSijilAkuan() {
		
		
		super.setFolderName("kuarters");
		super.setReportName("SijilAkuanPreview");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
}
