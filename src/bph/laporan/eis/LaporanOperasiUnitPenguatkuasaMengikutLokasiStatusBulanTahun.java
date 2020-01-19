package bph.laporan.eis;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class LaporanOperasiUnitPenguatkuasaMengikutLokasiStatusBulanTahun extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(LaporanOperasiUnitPenguatkuasaMengikutLokasiStatusBulanTahun.class);
	public LaporanOperasiUnitPenguatkuasaMengikutLokasiStatusBulanTahun() {
		
		
		super.setFolderName("eis");
		super.setReportName("LaporanOperasiUnitPenguatkuasaMengikutLokasiStatusBulanTahun");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
