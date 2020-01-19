package bph.laporan.penguatkuasa;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class LaporanSenaraiPenghuniYangMelanggarSyarat extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(LaporanSenaraiPenghuniYangMelanggarSyarat.class);
	public LaporanSenaraiPenghuniYangMelanggarSyarat() {
		
		
		super.setFolderName("penguatkuasa");
		super.setReportName("LaporanSenaraiPenghuniYangMelanggarSyarat");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}

