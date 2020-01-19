package bph.laporan.jrp;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class JRPKertasPertimbangan2 extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(JRPKertasPertimbangan2.class);
	public JRPKertasPertimbangan2() {
		
		
		super.setFolderName("jrp");
		super.setReportName("JRPKertasPertimbangan2");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
		
		/*String jenis = "";

		if (parameters.get("jenis") != null){
			jenis = (String)parameters.get("jenis");
		}
		
		if(jenis.equals("JRP7")){
			super.setReportName("JRPKertasPertimbangan");
		}else{
			System.out.println("dalam dah");
			super.setReportName("JRPKertasPertimbangan2");
		}*/
		
	}
	
}
