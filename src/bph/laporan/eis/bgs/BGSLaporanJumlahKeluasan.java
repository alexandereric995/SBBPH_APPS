package bph.laporan.eis.bgs;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class BGSLaporanJumlahKeluasan extends ReportServlet{

	static Logger myLog = Logger.getLogger(BGSLaporanJumlahKeluasan.class);
	
	public BGSLaporanJumlahKeluasan() {
	
		super.setFolderName("eis"); // nama folder java
		super.setReportName("LaporanJumlahKeluasanMengikutAgensiNegeri"); // nama file jasper
		
	}
	
	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
