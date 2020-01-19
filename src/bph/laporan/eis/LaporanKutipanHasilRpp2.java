package bph.laporan.eis;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class LaporanKutipanHasilRpp2 extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(LaporanKutipanHasilRpp2.class);
	public LaporanKutipanHasilRpp2() {
		
		
		super.setFolderName("eis");
		super.setReportName("LAPORANKUTIPANHASILRPP_KUTIPANHASIL_2");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
