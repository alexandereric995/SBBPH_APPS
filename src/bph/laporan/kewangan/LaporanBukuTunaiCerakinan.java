package bph.laporan.kewangan;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class LaporanBukuTunaiCerakinan extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(LaporanBukuTunaiCerakinan.class);
	public LaporanBukuTunaiCerakinan() {
		
		super.setReportName("BukuTunaiCerakinan");
		super.setFolderName("kewangan");
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
