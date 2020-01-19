package bph.laporan.pro;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class CetakMaklumbalasUnitProPengguna extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(CetakMaklumbalasUnitProPengguna.class);
	public CetakMaklumbalasUnitProPengguna() {
		
		
		super.setFolderName("pro");
		super.setReportName("CetakMaklumbalasUnitProPengguna");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
		
		String idAduan = (String) parameters.get("idAduan");
		parameters.put("idAduan", idAduan);
		myLog.info("idAduan="+idAduan);
	}
	
}
