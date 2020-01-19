package bph.laporan.pembangunan;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class LaporanSenaraiHakmilikMengikutTarikhDaftar extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(LaporanSenaraiHakmilikMengikutTarikhDaftar.class);
	public LaporanSenaraiHakmilikMengikutTarikhDaftar() {
		
		
		super.setFolderName("pembangunan");
		super.setReportName("LaporanSenaraiHakmilikMengikutTarikhDaftar");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
