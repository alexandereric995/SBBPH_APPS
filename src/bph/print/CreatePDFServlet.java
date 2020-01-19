package bph.print;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lebah.servlets.IServlet;

import org.apache.log4j.Logger;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.DocumentException;

public class CreatePDFServlet implements IServlet {
	static Logger myLogger = Logger.getLogger("CreatePDFServlet");
	
	public void doService(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		try {
	    	String pdfName = request.getParameter("pdfName");
	    	myLogger.debug("pdfName=" + pdfName);
	    	//String pathUrl = request.getParameter("pathUrl");
	    	
	    	String uri = request.getRequestURI();
	    	String queryString = request.getQueryString();
	    	String pathUrl = queryString.substring("pathUrl=".length());

	    	
	    	if ( pdfName == null || "".equals(pdfName)) pdfName = "noname";
	    	if ( pathUrl != null && !"".equals(pathUrl)) {
	    	
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "attachment; filename=\"" + pdfName + ".pdf\"");
	
				myLogger.debug("REQ URL (1) :: " + request.getSession().getAttribute("_portal_reqUrl"));
				myLogger.debug("PATH INFO (1) :: " + request.getSession().getAttribute("_portal_pathInfo"));
				
				String serverName = request.getServerName();
				int serverPort = request.getServerPort();
		        String server = serverPort != 80 ? serverName + ":" + serverPort : serverName;
		        String serverUrl = "http://" + server;
				String s1 = uri.substring(1);
				String appname = s1.substring(0, s1.indexOf("/"));
				
		    	String url = serverUrl + "/" + appname + "/" + pathUrl;
				myLogger.debug("" + url);
			    URL u = new URL(url);
			    HttpURLConnection uc = (HttpURLConnection) u.openConnection();
			    
			    //AZAM CHANGE
			    //create a temp file
	    		File temp = File.createTempFile(lebah.db.UniqueID.getUID(), ".html"); 
	    		//System.out.println("Temp file : " + temp.getAbsolutePath());
	    		
			    //String filename = "c:/temp/" + lebah.db.UniqueID.getUID() + ".html";
	    		
			    
			    Tidy tidy = new Tidy();
				BufferedInputStream in = new BufferedInputStream(uc.getInputStream());
				BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(temp));
				tidy.parse(in, out);
				in.close();
				out.close();
				
				ServletOutputStream os = response.getOutputStream();
				ITextRenderer renderer = new ITextRenderer();
				//renderer.setDocument(url);
				//ORI
				/*
				File file = new File(filename);
				renderer.setDocument(file);
				*/
				renderer.setDocument(temp);
				renderer.layout();
				renderer.createPDF(os);     
				
				
				//TEST RTF
				
				
				os.flush();
				os.close();
				
				//file.delete();
				temp.delete();
				
	    	}
		} catch (DocumentException e) {
			myLogger.error(e.getMessage());
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
	}
	

}
