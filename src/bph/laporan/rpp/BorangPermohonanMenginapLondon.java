package bph.laporan.rpp;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class BorangPermohonanMenginapLondon extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(BorangPermohonanMenginapLondon.class);
	public BorangPermohonanMenginapLondon() {
		
		
		super.setFolderName("rpp");
		super.setReportName("BorangPermohonanMenginapLondon");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
