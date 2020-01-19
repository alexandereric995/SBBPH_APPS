package bph.laporan.pro;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class LaporanStatistikTerimaanAduanBulananMengikutSeksyen extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(LaporanStatistikTerimaanAduanBulananMengikutSeksyen.class);
	public LaporanStatistikTerimaanAduanBulananMengikutSeksyen() {
		
		
		super.setFolderName("pro");
		super.setReportName("LaporanStatistikTerimaanAduanBulananMengikutSeksyen");
		
	}

	@Override
	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
