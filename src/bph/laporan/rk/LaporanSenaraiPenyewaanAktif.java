package bph.laporan.rk;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class LaporanSenaraiPenyewaanAktif extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(LaporanSenaraiPenyewaanAktif.class);
	public LaporanSenaraiPenyewaanAktif() {
		
		
		super.setFolderName("rk");
		super.setReportName("LaporanSenaraiPenyewaanAktif");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
