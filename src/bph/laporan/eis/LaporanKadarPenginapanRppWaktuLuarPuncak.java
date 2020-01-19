package bph.laporan.eis;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class LaporanKadarPenginapanRppWaktuLuarPuncak extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(LaporanKadarPenginapanRppWaktuLuarPuncak.class);
	public LaporanKadarPenginapanRppWaktuLuarPuncak() {
		
		
		super.setFolderName("eis");
		super.setReportName("LAPORANKADARPENGINAPANRPP_WAKTULUARPUNCAK");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
