package bph.laporan.rk;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class LaporanSenaraiPenyewaanAkanTamat extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(LaporanSenaraiPenyewaanAkanTamat.class);
	public LaporanSenaraiPenyewaanAkanTamat() {
		
		
		super.setFolderName("rk");
		super.setReportName("LaporanSenaraiPenyewaanAkanTamat");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
