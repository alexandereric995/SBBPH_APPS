package bph.mail;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bph.laporan.ReportServlet;

public class GetAttachment extends ReportServlet {	

	public byte[] getReportBytes(String namaFolder, String namaFail, ServletContext context, Map parameters)
			throws Exception {
		    return attachmentInbytes(namaFolder, namaFail, context, parameters);		
	}

	@Override
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
		// TODO Auto-generated method stub		
	}	
}
