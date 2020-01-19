package bph.laporan.pembangunan;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class MaklumatDaftarAsetKhususBinaanLuar extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(MaklumatDaftarAsetKhususBinaanLuar.class);
	public MaklumatDaftarAsetKhususBinaanLuar() {
		
		
		super.setFolderName("pembangunan");
		super.setReportName("MaklumatDaftarAsetKhususBinaanLuar");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
