package bph.laporan.penguatkuasa;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class LaporanSenaraiPenghuniYangHilangKelayakan extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(LaporanSenaraiPenghuniYangHilangKelayakan.class);
	public LaporanSenaraiPenghuniYangHilangKelayakan() {
		
		
		super.setFolderName("penguatkuasa");
		super.setReportName("LaporanSenaraiPenghuniYangHilangKelayakan");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
