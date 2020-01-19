package bph.laporan.pro;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class LaporanStatistikTerimaanAduanTahunanMengikutSeksyen extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(LaporanStatistikTerimaanAduanTahunanMengikutSeksyen.class);
	public LaporanStatistikTerimaanAduanTahunanMengikutSeksyen() {
		
		
		super.setFolderName("pro");
		super.setReportName("LaporanStatistikTerimaanAduanTahunanMengikutSeksyen");
		
	}

	@Override
	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
