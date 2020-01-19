package bph.laporan.pro;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class LaporanSenaraiAduanPro extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(LaporanSenaraiAduanPro.class);
	public LaporanSenaraiAduanPro() {
		
		
		super.setFolderName("pro");
		super.setReportName("LaporanSenaraiAduanPro");
		
	}

	@Override
	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
