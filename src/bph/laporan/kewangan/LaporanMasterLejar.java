package bph.laporan.kewangan;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class LaporanMasterLejar extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(LaporanMasterLejar.class);
	public LaporanMasterLejar() {
		
		super.setReportName("MasterLejar");
		super.setFolderName("kewangan");
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
//			parameters.remove("tarikhHingga");
			String tarikhHingga = (String) parameters.get("tarikhHingga");
			SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat nfHingga = new SimpleDateFormat("yyyy-MM-dd"); 
		    Date hinggaDate = null;
		    String newTarikhHingga ="";
	
		    if(tarikhHingga!="" || tarikhHingga.length() > 0){
		    	hinggaDate = df.parse(tarikhHingga);
				newTarikhHingga = nfHingga.format(hinggaDate);
			}
		    
		    parameters.put("tarikhHingga", newTarikhHingga);
	}
	
}
