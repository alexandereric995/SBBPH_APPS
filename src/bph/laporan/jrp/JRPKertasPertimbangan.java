package bph.laporan.jrp;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class JRPKertasPertimbangan extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(JRPKertasPertimbangan.class);
	public JRPKertasPertimbangan() {
		
		
		super.setFolderName("jrp");
		super.setReportName("JRPKertasPertimbangan");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
		/*
		String jenis = "";

		if (parameters.get("jenis") != null){
			jenis = (String)parameters.get("jenis");
		}
		
		if(jenis.equals("JRP7")){
			
		}else{
			System.out.println("dalam dah");
			super.setReportName("JRPKertasPertimbangan2");
		}*/
		
	}
	
}
