package bph.laporan.rpp;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class RPPBorangPermohonanMenginapLondon extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(RPPBorangPermohonanMenginapLondon.class);
	public RPPBorangPermohonanMenginapLondon() {
		
		
		super.setFolderName("rpp");
		super.setReportName("BorangPermohonanMenginapLondon");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
