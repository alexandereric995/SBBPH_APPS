package bph.laporan.eis.bgs;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class BGSLaporanStatusPermohonan extends ReportServlet{

	static Logger myLog = Logger.getLogger(BGSLaporanStatusPermohonan.class);
	
	public BGSLaporanStatusPermohonan() {
	
		super.setFolderName("eis"); // nama folder java
		super.setReportName("LaporanStatusPermohonanMengikutBulan"); // nama file jasper
		
	}
	
	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
