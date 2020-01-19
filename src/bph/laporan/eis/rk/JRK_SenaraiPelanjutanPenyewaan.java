package bph.laporan.eis.rk;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class JRK_SenaraiPelanjutanPenyewaan extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(JRK_SenaraiPelanjutanPenyewaan.class);
	
	public JRK_SenaraiPelanjutanPenyewaan() {
	
		super.setFolderName("eis"); // nama folder java
		super.setReportName("JRK_SenaraiPelanjutanPenyewaan"); // nama file jasper
		
	}
		
	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
