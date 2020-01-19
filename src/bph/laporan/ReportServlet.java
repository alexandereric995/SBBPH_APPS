package bph.laporan;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lebah.db.Db;
import lebah.servlets.IServlet2;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRTextExporter;
import net.sf.jasperreports.engine.export.JRTextExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.log4j.Logger;

public abstract class ReportServlet implements IServlet2 {

	private String reportFileName;
	private String folderName;
	private Map parameters;
	
	static Logger myLogger = Logger.getLogger(ReportServlet.class);
	
	public abstract void doProcessing(HttpServletRequest request, HttpServletResponse response, 
			ServletContext context,Map parameters) throws Exception;
	
	@Override
	public void doService(HttpServletRequest request, HttpServletResponse response,
			ServletContext context) throws IOException, ServletException {
		// TODO Auto-generated method stub
		Map parameters = new HashMap();
		if (this.parameters != null) parameters = this.parameters;
	    parameters.put("BaseDir",context.getRealPath("/img/"));
		parameters.put("ReportDir",context.getRealPath("/reports/"));
		String name="";
		String value="";
		Enumeration allparam = request.getParameterNames();
		for (; allparam.hasMoreElements(); ) {
	        name = (String)allparam.nextElement();
	        value = request.getParameter(name);
	        parameters.put(name,value);
		}
		this.parameters = parameters;		
		
		Connection conn = null;
		String reportType;
		HttpSession session = request.getSession();
		try {
			
			doProcessing(request,response,context,parameters);
			
			String reportFileName = this.reportFileName;
			String folderName = this.folderName;
			
			//AZAM - GET DB FROM REPORTING DB
			conn = new Db("dbconnectionDB2").getConnection();
			
			//AZAM ADD ON 10/12/2015
			reportType = (String)session.getAttribute("rFormat");
			
			//conn = new Db().getConnection();
			JasperReport jasperReport = getCompiledReport(reportFileName,folderName,context);
			System.out.println(reportType);
			if ("EXCEL".equals(reportType)) {
				createExcelReport(response, jasperReport,parameters,conn);
			} else if ("RTF".equals(reportType)) {
				//rtfConverter(response);
				createRTFReport(response, parameters, jasperReport,conn,request);
			} else {
				createPDFReport(response, jasperReport, parameters, conn);
			} 
			
//			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,null);
//			createPDFReport(response, jasperReport, parameters, conn);
//			createHTMLReport(jasperPrint, request, response);
//			createTEXTReport(request,response, parameters, jasperReport, "test123");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void createPDFReport(HttpServletResponse response,
			JasperReport jasperReport, Map parameters, Connection conn) throws JRException, IOException {
		// TODO Auto-generated method stub
		
		myLogger.debug("Con:"+conn);
		myLogger.debug("jasper:"+jasperReport);
		
		byte[] bytes = null;
		bytes = JasperRunManager.runReportToPdf(jasperReport,parameters,conn);
		
		if ((bytes != null) && (bytes.length > 0))
		{
			response.setContentType("application/pdf");
			response.setContentLength(bytes.length);
			ServletOutputStream ouputStream = response.getOutputStream();
			ouputStream.write(bytes, 0, bytes.length);
			ouputStream.flush();
			ouputStream.close();
		}
		else emptyResponse(response);
	}

	  private void createHTMLReport(JasperPrint jasperPrint,HttpServletRequest request,HttpServletResponse response)
				throws IOException, JRException {
		  		HttpSession session = request.getSession();
				Map imagesMap = new HashMap();
				request.getSession().setAttribute("IMAGES_MAP", imagesMap);
				response.setContentType("application/html");
				response.setHeader("Content-Disposition", "inline; filename=\"report.html\"");

				JRHtmlExporter exporter = new JRHtmlExporter();
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				exporter.setParameter(JRExporterParameter.OUTPUT_WRITER,response.getWriter());
				exporter.setParameter(JRHtmlExporterParameter.IMAGES_MAP, imagesMap);
				exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI,"/"+session.getAttribute("_portal_appname")+"/servlets/image?image=");
				exporter.exportReport();
	  }
	  
	  //AZAM ADD ON 10/12/2015
	  private void createExcelReport(HttpServletResponse response,JasperReport jasperReport,Map parameters,
			  Connection conn)
		throws JRException,SQLException, IOException {

		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
		OutputStream ouputStream = response.getOutputStream();
		net.sf.jasperreports.engine.export.JExcelApiExporter exporter = null;
		try
		{
			exporter = new net.sf.jasperreports.engine.export.JExcelApiExporter();
			response.setContentType("application/xls");
			response.setHeader("Content-Disposition", "inline; filename=\"SBBPH_.xls\"");
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, ouputStream);
			exporter.exportReport();
		}
		catch (JRException e)
		{
			e.printStackTrace();
		}
		ouputStream.flush();  
		ouputStream.close();
	}
	  
	  private void createRTFReport(HttpServletResponse response,Map parameters,
	          JasperReport jasperReport,Connection conn,HttpServletRequest request)
	  throws JRException,SQLException, IOException, ServletException {

		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

	  	response.setContentType("application/rtf");
	  	response.setHeader("Content-Disposition", "inline; filename=\"file.rtf\""); 
	  	
	  	JRRtfExporter docxExporter=new JRRtfExporter();  
	    docxExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);  
	    
		ServletOutputStream servletOutputStream=response.getOutputStream();  
		docxExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);  

		try
		   {
			docxExporter.exportReport();
		   }
		   catch (JRException e)
		   {
		       throw new ServletException(e);
		   }
		   finally
		   {
		       if (servletOutputStream != null)
		       {
		           try
		           {
		        	   servletOutputStream.close();
		           }
		           catch (IOException ex)
		           {
		           }
		       }
		   } 
	  }
	  
	  private void createTEXTReport(HttpServletRequest request,
			  HttpServletResponse response,Map parameters,
			  JasperReport jasperReport,String title)
		throws JRException, IOException {
		  
		  
		  JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters);
		  JRTextExporter exporterTxt = new JRTextExporter();
		  exporterTxt.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);

		  exporterTxt.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, request.getRealPath("/reports") + "/" +title+".txt");
		  exporterTxt.setParameter(JRTextExporterParameter.CHARACTER_WIDTH, new Integer(7));

		  exporterTxt.setParameter(JRTextExporterParameter.CHARACTER_HEIGHT, new Integer(11));

		  exporterTxt.exportReport();

		  File f=new File(request.getRealPath("/reports") + "/" + title+".txt");
		  FileInputStream fin = new FileInputStream(f);
		  ServletOutputStream outStream = response.getOutputStream();
		  // SET THE MIME TYPE.
		  response.setContentType("application/text");
		  // set content dispostion to attachment in with file name.
		  // case the open/save dialog needs to appear.
		  response.setHeader("Content-Disposition", "attachment;filename=JPN_"+title+".txt");

		  byte[] buffer = new byte[1024];
		  int n = 0;
		  while ((n = fin.read(buffer)) != -1) {
		  outStream.write(buffer, 0, n);
		  System.out.println(buffer);
		  }

		  outStream.flush();
		  fin.close();
		  outStream.close();
	}  
	  
	private JasperReport getCompiledReport(String fileName,String folderName, ServletContext context)
	throws JRException {
		File reportFile;
		String path;
		
		if (folderName != null) {
			path = "/reports/" + folderName + "/" + fileName ;
		} else {
			path = "/reports/" + fileName ;
		}
		if (context != null) {
			reportFile = new File(context.getRealPath(path+".jasper"));
			
			if (!reportFile.exists()) {
				JasperCompileManager.compileReportToFile(context.getRealPath(path+ ".jrxml"));
			}
			
			File jxml = new File(context.getRealPath(path+ ".jrxml"));
		
			if (jxml.lastModified() > reportFile.lastModified()) {
				JasperCompileManager.compileReportToFile(context.getRealPath(path+ ".jrxml"));
			}
		} else {
			String dir = ResourceBundle.getBundle("dbconnection").getString("CONTEXT_PATH");
			reportFile = new File( dir + path + ".jasper");
			
			if (!reportFile.exists()) {
				JasperCompileManager.compileReportToFile( dir + path + ".jrxml" );
			}
			
			File jxml = new File( dir + path + ".jrxml" );
		
			if (jxml.lastModified() > reportFile.lastModified()) {
				JasperCompileManager.compileReportToFile( dir + path + ".jrxml" );
			}
		}
		
		
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reportFile.getPath());
		
		return jasperReport;
	}
	
	private void emptyResponse(HttpServletResponse response) throws IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<head>");
		out.println("<title>Empty response.</title>");
		out.println("</head>");
		out.println("<body bgcolor=\"white\">");
		out.println("<span class=\"bold\">Empty response.</span>");
		out.println("</body>");
		out.println("</html>");
	}
	  
	public void setReportName(String s) {
		this.reportFileName = s;
	}
	  
	public void setParameters(Map params) {
		this.parameters = params;
	}
	
	public String getFolderName() {
		return folderName;
	}
	
	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}
	
	public byte[] attachmentInbytes(String folderName, String reportFileName, ServletContext context, Map parameters) throws IOException {
		
		Connection conn = null;
		byte[] bytes = null;
		try {
			conn = new Db().getConnection();
			JasperReport jasperReport = getCompiledReport(reportFileName, folderName, context);
			bytes = JasperRunManager.runReportToPdf(jasperReport, parameters, conn);
		} catch (Exception e) {

		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException xx) { }
		}
		return bytes;
	}
}
