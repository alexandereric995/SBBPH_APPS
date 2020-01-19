package bph.laporan.kewangan;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class LaporanSenaraiKutipanHarian extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(LaporanSenaraiKutipanHarian.class);
	public LaporanSenaraiKutipanHarian() {
		
		
		super.setFolderName("kewangan");
		super.setReportName("LaporanSenaraiKutipanHarian");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
		
		// SAMPLE UNTUK REPORT EXCEL
//		HttpSession session = request.getSession();
//		if (session.getAttribute("rFormat").equals("EXCEL")) {
//			super.setReportName("LaporanSenaraiKutipanHarianExcel");
//		}
	}
	
}
