package bph.laporan.kewangan;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class LaporanBukuTunaiCerakinanKutipanEpayment_FPX extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(LaporanBukuTunaiCerakinanKutipanEpayment_FPX.class);
	public LaporanBukuTunaiCerakinanKutipanEpayment_FPX() {
		
		
		super.setFolderName("kewangan");
		super.setReportName("LaporanBukuTunaiCerakinanKutipanEpayment_FPX");
		
	}

	@Override
	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
