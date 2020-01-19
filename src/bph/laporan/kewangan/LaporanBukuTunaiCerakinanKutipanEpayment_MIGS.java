package bph.laporan.kewangan;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class LaporanBukuTunaiCerakinanKutipanEpayment_MIGS extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(LaporanBukuTunaiCerakinanKutipanEpayment_MIGS.class);
	public LaporanBukuTunaiCerakinanKutipanEpayment_MIGS() {
		
		
		super.setFolderName("kewangan");
		super.setReportName("LaporanBukuTunaiCerakinanKutipanEpayment_MIGS");
		
	}

	@Override
	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
