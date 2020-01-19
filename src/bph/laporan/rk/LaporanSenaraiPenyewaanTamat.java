package bph.laporan.rk;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class LaporanSenaraiPenyewaanTamat extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(LaporanSenaraiPenyewaanTamat.class);
	public LaporanSenaraiPenyewaanTamat() {
		
		
		super.setFolderName("rk");
		super.setReportName("LaporanSenaraiPenyewaanTamat");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
