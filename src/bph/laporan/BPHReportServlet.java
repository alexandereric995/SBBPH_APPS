package bph.laporan;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lebah.db.Db;
import lebah.db.SQLRenderer;
import lebah.servlets.IServlet2;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRResultSetDataSource;
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
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.log4j.Logger;

public abstract class BPHReportServlet implements IServlet2 {
	
  static Logger myLogger = Logger.getLogger(BPHReportServlet.class);
	
  private String reportFileName;
  private Map parameters;
  private byte[] bytes;
  private String SQL = null;
  private String folderName;
  private boolean setMaklumatPegawai;
  private boolean setMaklumatPermohonan;
  private boolean setMaklumatPemohon;
  private boolean setMaklumatSimati;
  private boolean setMaklumatPerbicaraan;
  private boolean setMaklumatHTA;
  private boolean setVersion;
  private boolean generateTextFile;

  private String doSaveVersion;
  private String borang;
  private String idfail;
  private String nofail;
  private String idmasuk;
  private String errorMsg;
  
  public String getErrorMsg() {
	  return errorMsg;
  }

  public void setErrorMsg(String errorMsg) {
	  this.errorMsg = errorMsg;
  }



private Vector beanMaklumatPegawai = null;
  private Vector beanMaklumatPermohonan = null;
  private Vector beanMaklumatPemohon = null;
  private Vector beanMaklumatSimati = null;
  private Vector beanMaklumatPerbicaraan = null;
  private Vector beanMaklumatHTA = null;
  

public abstract void doProcessing(HttpServletRequest request, HttpServletResponse response, 
		ServletContext context,Map parameters) throws Exception;

@Override
public void doService(HttpServletRequest request, HttpServletResponse response, ServletContext context)
    throws IOException, ServletException
  {
	
	//Security Check
	HttpSession session = request.getSession();
	/*String user_id = (String)session.getAttribute("_ekptg_user_id");
	this.idmasuk = user_id;
	if (user_id == null) {
		usernotvalid(response);
	} else if (this.errorMsg != null) {
		showErrors(response);
	}
	else {*/
	
		//
		
		String contextPath = request.getContextPath();

	    Map parameters = new HashMap();
	    if (this.parameters != null) parameters = this.parameters;
		//parameters.put("BaseDir",new File(context.getRealPath("/img/")));   
	    parameters.put("BaseDir",context.getRealPath("/img/"));
		//Report folder
		parameters.put("ReportDir",context.getRealPath("/reports/"));
		System.out.println("ReportDir : "+context.getRealPath("/reports/"));
		//Get all parameters from query String
		String name="";
		String value="";
		Enumeration allparam = request.getParameterNames();
		for (; allparam.hasMoreElements(); ) {
	        // Get the name of the request parameter
	        name = (String)allparam.nextElement();
	        // Get the value of the request parameter
	        value = request.getParameter(name);
	        //System.out.println(name +"="+value);
	        parameters.put(name,value);
		}
		//Get all paramaters to global
		this.parameters = parameters;
		try {
			doProcessing(request,response,context,parameters);
			
			if (this.errorMsg != null) {
				showErrors(response);
				return;
			}
			
			//
			String reportFileName = this.reportFileName;
			String folderName = this.folderName;
		    if (reportFileName == null || "".equals(reportFileName)) {
		    	fileNameEmpty(response);
		    	return;
		    }
		    //Versioning
		    myLogger.debug("this.setVersion:"+this.setVersion);
		    myLogger.debug("this.doSaveVersion:"+this.doSaveVersion);
		    
		    /*
		    if (this.setVersion) {
				if ("yes".equals(this.doSaveVersion)) {
					doSaveVersioning(request,response,context,request.getQueryString());
					return;
				} else if ("no".equals(this.doSaveVersion)) {
					//Ok-now we just print the report
				} else if ("view".equals(this.doSaveVersion)) { 
					viewPDF(request,response,(String)parameters.get("idborang"));
					return;
				} 
				else {	
					askForVersion(request,response);
					return;
				}
		    	
		    }*/

			//Additional
		    if (this.setMaklumatPegawai) 		doPegawai(request,response,context,parameters);
			if (this.setMaklumatPermohonan) 	doPermohonan(request,response,context,parameters);
			if (this.setMaklumatPemohon) 		doPemohon(request,response,context,parameters);
			if (this.setMaklumatSimati) 		doSimati(request,response,context,parameters);
			if (this.setMaklumatPerbicaraan) 	doPerbicaraan(request,response,context,parameters);
			if (this.setMaklumatHTA) 			doHTA(request,response,context,parameters);
			
			//
			myLogger.info("checking...");
			myLogger.debug(parameters.get("bilDokumen"));
			if ( "".equals(parameters.get("bilDokumen")) ) {
				parameters.put("bilDokumen","1");
			}
			
		} catch (Exception e) {
			//e.printStackTrace();
	    	doException(response,contextPath,e);
	    	return;
		}
		
		
		
		byte[] bytes = null;
	    Db db = null;
	    Statement statement = null;
	    ResultSet resultSet = null;
	    JRResultSetDataSource resultSetDataSource = null;
	    Connection conn = null;
	    String reportType;
	    try {
	    	conn = new Db().getConnection();
	     	//String reportType = request.getParameter("reportType");
	     	reportType = (String)session.getAttribute("rFormat");
			JasperReport jasperReport = getCompiledReport(reportFileName,folderName,context);
			//Automatically set when no data section
			//jasperReport.setWhenNoDataType(jasperReport.WHEN_NO_DATA_TYPE_NO_DATA_SECTION);
	    	if (this.SQL != null) {
	    		statement = conn.createStatement();
	    		resultSet = statement.executeQuery(SQL);
	    		resultSetDataSource = new JRResultSetDataSource(resultSet);
	    	}
	    	

	    	if (isGenerateTextFile()) {
	    		createTEXTReport(request,response, parameters, jasperReport,conn,(String)parameters.get("id_simati"));
	    		return;
	    	}
			if ("HTML".equals(reportType)) {
				JasperPrint jasperPrint = null;
				if (resultSetDataSource != null) {
					jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, resultSetDataSource);
				} else {
					jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
				}
				createHTMLReport(jasperPrint, request, response);
			} else if ("RTF".equals(reportType)) {
				//rtfConverter(response);
				createRTFReport(response, parameters, jasperReport,conn,request);
			} else if ("EXCEL".equals(reportType)) {
				//rtfConverter(response);
				createExcelReport(response, parameters, jasperReport,conn);
			}
			// Assume it is a PDF report
			// else if ("PDF".equals(reportType) || "".equals(reportType)) 
			else 
			{
				if (resultSetDataSource != null) {
					createPDFReport(response, parameters, jasperReport,resultSetDataSource);
				}else {
					createPDFReport(response, parameters, jasperReport,conn);
				}
			}
	     } catch (Exception e) {
	    	doException(response,contextPath,e);
	    	//return;
	    } finally {
	    	try {
	    		if (resultSet != null) resultSet.close();
	    		if (statement != null) statement.close();
	    		if (conn != null) conn.close();
	    		if (db != null) db.close();
	    	}catch (SQLException xx) {}
	    	
	    }
	}
 // }
  
  public void setReportName(String s) {
	  this.reportFileName = s;
  }
  
  public String getFolderName() {
	return folderName;
  }

  public void setFolderName(String folderName) {
	this.folderName = folderName;
  }

  public void setParameters(Map params) {
	  this.parameters = params;
  }

  public void setSQL(String sql) {
	  this.SQL = sql;
  }
  
  public void flagMaklumatPegawai(boolean flagPegawai) {
	  this.setMaklumatPegawai= flagPegawai;
  }
  
  public void flagMaklumatPermohonan(boolean flagPermohonan) {
	  this.setMaklumatPermohonan = flagPermohonan;
  }
  
  public void flagMaklumatPemohon(boolean flagPemohon){
	  this.setMaklumatPemohon = flagPemohon;
  }
  
  public void flagMaklumatSimati(boolean flagSimati){
	  this.setMaklumatSimati = flagSimati;
  }
  
  public void flagMaklumatPerbicaraan(boolean flagPerbicaraan){
	  this.setMaklumatPerbicaraan = flagPerbicaraan;
  }
  
  public void flagMaklumatHTA(boolean flagHTA){
	  this.setMaklumatHTA = flagHTA;
  }
  
  public boolean isGenerateTextFile() {
		return generateTextFile;
	}

	public void setGenerateTextFile(boolean generateTextFile) {
		this.generateTextFile = generateTextFile;
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
	  



  private void createExcelReport(HttpServletResponse response,Map parameters,
		  JasperReport jasperReport,Connection conn)
	throws JRException,SQLException, IOException {

	JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

	OutputStream ouputStream = response.getOutputStream();
	JRXlsExporter exporter = null;
	response.setContentType("application/xls");
	response.setHeader("Content-Disposition", "inline; filename=\"eTapp.xls\"");
	exporter = new JRXlsExporter();
	exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
	exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, ouputStream);
	try
	{
		exporter.exportReport();
	}
	catch (JRException e)
	{
		e.printStackTrace();
	}
	ouputStream.flush();  
	ouputStream.close();
}
  
  private void createTEXTReport(HttpServletRequest request,
		  HttpServletResponse response,Map parameters,
		  JasperReport jasperReport,Connection conn,String title)
	throws JRException,SQLException, IOException {
	  
	  
	  JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
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
  
  
  private void createPDFReport(HttpServletResponse response,Map parameters,JasperReport jasperReport,Connection conn)
			throws JRException,SQLException, IOException {
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

  private void createPDFReport(HttpServletResponse response,Map parameters,JasperReport jasperReport,JRResultSetDataSource ds)
	throws JRException,SQLException, IOException {
	byte[] bytes = null;
	bytes = JasperRunManager.runReportToPdf(jasperReport,parameters,ds);
	
	if ((bytes != null) && (bytes.length > 0))
	{
		response.setContentType("application/pdf");
		response.setContentLength(bytes.length);
		response.setHeader("Content-Disposition", "attachment; filename=eTapp.pdf");
		ServletOutputStream ouputStream = response.getOutputStream();
		ouputStream.write(bytes, 0, bytes.length);
		ouputStream.flush();
		ouputStream.close();
	}
	else emptyResponse(response);

}
  
  private void usernotvalid(HttpServletResponse response) throws IOException {
	    response.setContentType("text/html");
	    PrintWriter out = response.getWriter();
	    out.println("<html>");
	    out.println("<head>");
	    out.println("<title>Ralat</title>");
	    out.println("</head>");
	    out.println("<body bgcolor=\"white\">");
	    out.println("<span class=\"bold\">Maaf, sessi anda telah tamat.</span>");
	    out.println("</body>");
	    out.println("</html>");
	  }
  
  
 
  
  
  private void  rtfConverter(HttpServletResponse response) throws IOException {
	    response.setContentType("text/html");
	    PrintWriter out = response.getWriter();
	    out.println("<html>");
	    out.println("<head>");
	    out.println("<title>RTF Converter</title>");
	    out.println("</head>");
	    out.println("<body bgcolor=\"white\">");
	    out.println("<span class=\"bold\">RTF Converter In progress....</span>");
	    out.println("</body>");
	    out.println("</html>");
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

  private void fileNameEmpty(HttpServletResponse response) throws IOException {
	    response.setContentType("text/html");
	    PrintWriter out = response.getWriter();
	    out.println("<html>");
        out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"../default.css\" />");
        out.println("<div class=\"error\">");
        out.println("File Name Empty.");
        out.println("</div>");
	    out.println("</html>");
}
  
  private void showErrors(HttpServletResponse response) throws IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<center>"+this.getErrorMsg()+"</center>");
		out.println("</html>");
}
  
  
  private void doException(HttpServletResponse response,String contextPath, Exception e)
    throws IOException
  {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\""+contextPath+"/default.css\" />");
    out.println("<div class=\"error\">");
    out.println("JasperReports encountered this error :<br><br>");
    out.println("<pre>");
    //e.printStackTrace(out);
    out.println(e.getMessage());
    out.println("</pre>");
    out.println("</div>");
    out.close();
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
		reportFile = new File(context.getRealPath(path+".jasper"));
		
	// If compiled file is not found, then
	// compile XML template
	if (!reportFile.exists()) {
		System.out.println("::!:::::::::::::::::");
		System.out.println("realpath "+context.getRealPath(path+ ".jrxml"));
		JasperCompileManager.compileReportToFile(context.getRealPath(path+ ".jrxml"));
	}
	//azam add on 18 Nov,2009
	// check if .jrxml > .jasper then compile again
	File jxml = new File(context.getRealPath(path+ ".jrxml"));
	if (jxml.lastModified() > reportFile.lastModified()) {
		System.out.println("::?:::::::::::::::::");
		System.out.println("realpath "+context.getRealPath(path+ ".jrxml"));
		JasperCompileManager.compileReportToFile(context.getRealPath(path+ ".jrxml"));
	}
	
	// Since it's in development, just ignore this part first, everytime we run then
	// we should always recompile // it will be a litte bit slow in terms of performance
	//JasperCompileManager.compileReportToFile(context.getRealPath(path+ ".jrxml"));
	//System.out.println("reportFile.getPath():"+reportFile.getPath());	
	JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reportFile.getPath());

	return jasperReport;
}
	
	/////////////////
	
	public void setIdPermohonan(String idfail,Map parameters) {
		Db db = null;
		String sql = "";
		try {
			db = new Db();
			sql = "Select id_permohonan from tblppkpermohonan where id_fail='"+idfail+"'";
			Statement stmt = db.getStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				parameters.put("idpermohonan",rs.getString("id_permohonan"));
			} else {
				parameters.put("idpermohonan","0");//this cannot be true.
			}

		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (db!=null) db.close();
		}
	}
	
	public void doPegawai(HttpServletRequest request, HttpServletResponse response, ServletContext context, Map parameters) throws Exception{
		
		Vector listPegawai = null;
		try {
				listPegawai = new Vector();
				setMaklumatPegawai(parameters);
				listPegawai = getBeanMaklumatPegawai();
				if (listPegawai.size() != 0){
					Hashtable h = (Hashtable) listPegawai.get(0);
					parameters.put("namaPegawai",h.get("nama").toString());
					parameters.put("jawatan",h.get("jawatan").toString());
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doPermohonan(HttpServletRequest request, HttpServletResponse response, ServletContext context, Map parameters) throws Exception{
		
		Vector listPermohonan = null;
		
		try {

				listPermohonan = new Vector();
				setMaklumatPermohonan(parameters);
				listPermohonan = getBeanMaklumatPermohonan();
				
				if (listPermohonan.size() != 0){
					Hashtable h = (Hashtable) listPermohonan.get(0);
					
					parameters.put("idPermohonan",h.get("idPermohonan").toString());
					parameters.put("idNegeriMhn",h.get("idNegeri").toString());
					parameters.put("idDaerahMhn",h.get("idDaerah").toString());
					parameters.put("jumHTATarikhMohon",h.get("jumHTATkhMhn").toString());
					parameters.put("jumHTATarikhMati",h.get("jumHTATkhMati").toString());
					parameters.put("jumHATarikhMohon",h.get("jumHATkhMhn").toString());
					parameters.put("jumHATarikhMati",h.get("jumHATkhMati").toString());
					parameters.put("jumHartaTarikhMohon",h.get("jumHartaTkhMhn").toString());
					parameters.put("jumHartaTarikhMati",h.get("jumHartaTkhMati").toString());
					parameters.put("tarikhMohon",h.get("tarikhMhn").toString());
					parameters.put("NoFail",h.get("noFail").toString());
					parameters.put("seksyen",h.get("seksyen").toString());
				
				}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doPemohon(HttpServletRequest request, HttpServletResponse response, ServletContext context, Map parameters) throws Exception{
		Vector listPemohon = null;
		
		try {
				listPemohon = new Vector();
				setMaklumatPemohon(parameters);
				listPemohon = getBeanMaklumatPemohon();
				
				if (listPemohon.size() != 0){
					Hashtable h = (Hashtable) listPemohon.get(0);
					parameters.put("idPermohonan",h.get("idPermohonan").toString());
					parameters.put("namaPemohon",h.get("namaPemohon").toString());
					parameters.put("singleLineAlamatPemohon",h.get("singleLineAlamatPemohon").toString());
					parameters.put("newLineAlamatPemohon",h.get("newLineAlamatPemohon").toString());
					parameters.put("no_kp_pemohon",h.get("no_kp_pemohon").toString());
					parameters.put("namaNegeriPemohon",h.get("namaNegeriPemohon").toString());
					parameters.put("namaBandarPemohon",h.get("namaBandarPemohon").toString());
					parameters.put("poskodPemohon",h.get("poskodPemohon").toString());
					parameters.put("noTel",h.get("noTel").toString());
				}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void doSimati(HttpServletRequest request, HttpServletResponse response, ServletContext context, Map parameters) throws Exception{
		Vector listSimati= null;
		
		try {

				listSimati = new Vector();
				setMaklumatSimati(parameters);
				listSimati = getBeanMaklumatSimati();
				
				if (listSimati.size() != 0){
					Hashtable h = (Hashtable) listSimati.get(0);
					parameters.put("idPermohonan",h.get("idPermohonan").toString());
					parameters.put("namaSimati",h.get("namaSimati").toString());
					parameters.put("singleLineAlamatSimati",h.get("singleLineAlamatSimati").toString());
					parameters.put("newLineAlamatSimati",h.get("newLineAlamatSimati").toString());
					parameters.put("no_kp_simati",h.get("no_kp_simati").toString());
					parameters.put("namaNegeriSimati",h.get("namaNegeriSimati").toString());
					parameters.put("namaBandarSimati",h.get("namaBandarSimati").toString());
					parameters.put("poskodSimati",h.get("poskodSimati").toString());
					parameters.put("noSijilMati",h.get("noSijilMati").toString());
					parameters.put("tarikhMati",h.get("tarikhMati").toString());
					parameters.put("waktuKematian",h.get("waktuKematian").toString());
					parameters.put("buktiKematian",h.get("buktiKematian").toString());
					
				}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void doPerbicaraan(HttpServletRequest request, HttpServletResponse response, ServletContext context, Map parameters) throws Exception{
		
		Vector listPerbicaraan = null;
		
		try {
				listPerbicaraan = new Vector();	
				setMaklumatPerbicaraan(parameters);
				listPerbicaraan = getBeanMaklumatPerbicaraan();
				
				if (listPerbicaraan.size() != 0){
					Hashtable h = (Hashtable) listPerbicaraan.get(0);
					parameters.put("idPermohonan",h.get("idPermohonan").toString());
					parameters.put("pegPengendali",h.get("pegPengendali").toString());
					parameters.put("singleLineAlamat",h.get("singleLineAlamat").toString());
					parameters.put("newLineAlamat",h.get("newLineAlamat").toString());
					parameters.put("namaNegeri",h.get("namaNegeri").toString());
					parameters.put("bandar",h.get("bandar").toString());
					parameters.put("poskod",h.get("poskod").toString());
					parameters.put("masaBicara",h.get("masaBicara").toString());

				}

			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void doHTA(HttpServletRequest request, HttpServletResponse response, ServletContext context, Map parameters) throws Exception{
		
		Vector listHTA = null;
		
		try {
				listHTA = new Vector();
				setMaklumatHTA(parameters);
				listHTA = getBeanMaklumatHTA();
				
				if (listHTA.size() != 0){
					Hashtable h = (Hashtable) listHTA.get(0);
					parameters.put("idnegerijaagan",h.get("idnegerijaagan").toString());
					parameters.put("iddaerahjagaan",h.get("iddaerahjagaan").toString());
					
				}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	////////////
	private void setMaklumatPegawai(Map parameters) throws Exception {
		Db db = null;
		String sql = "";
		
		try {
			db = new Db();
			beanMaklumatPegawai = new Vector();
			Statement stmt = db.getStatement();
			SQLRenderer r = new SQLRenderer();

			r.add("INITCAP(NAMA_PEGAWAI)AS NAMA_PEGAWAI");
			r.add("INITCAP(JAWATAN) AS JAWATAN");
			
			r.add("ID_UNITPSK", parameters.get("idPegawai").toString());

			sql = r.getSQLSelect("TBLPPKRUJUNIT");
			ResultSet rs = stmt.executeQuery(sql);

			Hashtable h;
			int bil = 1;
			Integer count = 0;

			while (rs.next()) {
				h = new Hashtable();
				
				if (parameters.get("flagReport").toString().equals("S")){
					h.put("nama", rs.getString("NAMA_PEGAWAI").toUpperCase());
					h.put("jawatan",rs.getString("JAWATAN"));
				} else {
					h.put("nama", rs.getString("NAMA_PEGAWAI").toUpperCase());
					h.put("jawatan",rs.getString("JAWATAN").toUpperCase());
				}
				
				beanMaklumatPegawai.addElement(h);
				bil++;
				count++;
			}
			
		} finally {
			if (db != null)
				db.close();
		}
	}
		
	private void setMaklumatPermohonan(Map parameters) throws Exception {
		Db db = null;
		String sql = "";
		
		System.out.println("No fail = " + parameters.get("NoFail"));
		try {
			db = new Db();
			beanMaklumatPermohonan = new Vector();
			Statement stmt = db.getStatement();
			SQLRenderer r = new SQLRenderer();

			r.add("A.ID_PERMOHONAN");
			r.add("INITCAP(C.NAMA_NEGERI) AS NAMA_NEGERI");
			r.add("INITCAP(D.NAMA_DAERAH) AS NAMA_DAERAH");
			r.add("A.JUMLAH_HTA_TARIKHMOHON");
			r.add("A.JUMLAH_HTA_TARIKHMATI");
			r.add("A.JUMLAH_HA_TARIKHMOHON");
			r.add("A.JUMLAH_HA_TARIKHMATI");
			r.add("A.JUMLAH_HARTA_TARIKHMOHON");
			r.add("A.JUMLAH_HARTA_TARIKHMATI");
			r.add("A.TARIKH_MOHON");
			r.add("B.NO_FAIL");
			r.add("A.SEKSYEN");
			

			r.add("B.ID_FAIL",r.unquote("A.ID_FAIL"));
			r.add("C.ID_NEGERI",r.unquote("A.ID_NEGERIMHN"));
			r.add("D.ID_DAERAH",r.unquote("A.ID_DAERAHMHN"));
			
			r.add("B.NO_FAIL", parameters.get("NoFail").toString());

			sql = r.getSQLSelect("TBLPPKPERMOHONAN A, TBLPFDFAIL B, TBLRUJNEGERI C,TBLRUJDAERAH D");
			ResultSet rs = stmt.executeQuery(sql);

			Hashtable h;
			int bil = 1;
			Integer count = 0;

			while (rs.next()) {
				h = new Hashtable();
				
				h.put("idPermohonan", rs.getString("ID_PERMOHONAN"));
				h.put("idNegeri",rs.getString("NAMA_NEGERI"));
				h.put("idDaerah",rs.getString("NAMA_DAERAH"));
				h.put("jumHTATkhMhn",rs.getString("JUMLAH_HTA_TARIKHMOHON")==null?"":rs.getString("JUMLAH_HTA_TARIKHMOHON"));
				h.put("jumHTATkhMati",rs.getString("JUMLAH_HTA_TARIKHMATI")==null?"":rs.getString("JUMLAH_HTA_TARIKHMATI"));
				h.put("jumHATkhMhn",rs.getString("JUMLAH_HA_TARIKHMOHON")==null?"":rs.getString("JUMLAH_HA_TARIKHMOHON"));
				h.put("jumHATkhMati", rs.getString("JUMLAH_HA_TARIKHMATI")==null?"":rs.getString("JUMLAH_HA_TARIKHMATI"));
				h.put("jumHartaTkhMhn",rs.getString("JUMLAH_HARTA_TARIKHMOHON")==null?"":rs.getString("JUMLAH_HARTA_TARIKHMOHON"));
				h.put("jumHartaTkhMati",rs.getString("JUMLAH_HARTA_TARIKHMATI")==null?"":rs.getString("JUMLAH_HARTA_TARIKHMATI"));
				h.put("tarikhMhn", rs.getString("TARIKH_MOHON"));
				h.put("noFail", rs.getString("NO_FAIL"));
				h.put("seksyen", rs.getString("SEKSYEN"));
				beanMaklumatPermohonan.addElement(h);
				bil++;
				count++;
			}
			
		} finally {
			if (db != null)
				db.close();
		}
	}
	
	private void setMaklumatPemohon(Map parameters) throws Exception {
		Db db = null;
		String sql = "";

		try {
			db = new Db();
			beanMaklumatPemohon = new Vector();
			Statement stmt = db.getStatement();
			SQLRenderer r = new SQLRenderer();

			r.add("B.ID_PERMOHONAN");
			r.add("A.NAMA_PEMOHON");
			r.add("A.ALAMAT1_SURAT");
			r.add("A.ALAMAT2_SURAT");
			r.add("A.ALAMAT3_SURAT");
			r.add("C.NAMA_NEGERI");
			r.add("D.KETERANGAN AS NAMA_BANDAR");
			r.add("A.POSKOD_SURAT");
			r.add("A.NO_TEL_SURAT");
			
			r.add("E.ID_FAIL",r.unquote("B.ID_FAIL"));
			r.add("B.ID_PEMOHON",r.unquote("A.ID_PEMOHON"));
			r.add("C.ID_NEGERI(+)",r.unquote("A.ID_NEGERISURAT"));
			r.add("D.ID_BANDAR(+)",r.unquote("A.ID_BANDARSURAT"));
			
			r.add("E.NO_FAIL", parameters.get("NoFail").toString());

			sql = r.getSQLSelect("TBLPPKPEMOHON A, TBLPPKPERMOHONAN B, TBLRUJNEGERI C,TBLRUJBANDAR D, TBLPFDFAIL E");
			myLogger.info(sql);
			ResultSet rs = stmt.executeQuery(sql);
			
			//System.out.println("sql pemohon == " + sql);
			
			Hashtable h;
			int bil = 1;
			Integer count = 0;
			String alamat1 = "";
			String alamat2 = "";
			String alamat3 = "";
			String poskod = "";
			String bandar = "";
			String negeri = "";
			while (rs.next()) {
				h = new Hashtable();
				
				alamat1 = rs.getString("ALAMAT1_SURAT")==null?"":rs.getString("ALAMAT1_SURAT");
				alamat2 = rs.getString("ALAMAT2_SURAT")== null?"":rs.getString("ALAMAT2_SURAT");
				alamat3 = rs.getString("ALAMAT3_SURAT")== null?"":rs.getString("ALAMAT3_SURAT");
				poskod = rs.getString("POSKOD_SURAT")==null?"":rs.getString("POSKOD_SURAT");
				bandar = rs.getString("NAMA_BANDAR")==null?"":rs.getString("NAMA_BANDAR").toUpperCase();
				negeri = rs.getString("NAMA_NEGERI")==null?"":rs.getString("NAMA_NEGERI");
				
				h.put("idPermohonan", rs.getString("ID_PERMOHONAN"));
				h.put("namaPemohon", rs.getString("NAMA_PEMOHON").toUpperCase());
				
				if (alamat3 != ""){
					h.put("singleLineAlamatPemohon",alamat1 + alamat2 + alamat3);
					h.put("newLineAlamatPemohon",alamat1 + "\n\n" + alamat2 + "\n\n" + alamat3 + "\n\n" + poskod + " " + bandar + "\n\n" + negeri);
				}
				else{
					h.put("singleLineAlamatPemohon",alamat1 + alamat2);
					h.put("newLineAlamatPemohon",alamat1 + "\n\n"  + alamat2 + "\n\n" + poskod + " " + bandar + "\n\n" + negeri);
				}
				h.put("no_kp_pemohon", noKpPemohon(rs.getString("ID_PERMOHONAN").toString()));
				h.put("namaNegeriPemohon",rs.getString("NAMA_NEGERI")==null?"":rs.getString("NAMA_NEGERI"));
				h.put("namaBandarPemohon",rs.getString("NAMA_BANDAR")==null?"":rs.getString("NAMA_BANDAR").toUpperCase());
				h.put("poskodPemohon",rs.getString("POSKOD_SURAT")==null?"":rs.getString("POSKOD_SURAT"));
				h.put("noTel", rs.getString("NO_TEL_SURAT")==null?"":rs.getString("NO_TEL_SURAT"));
				beanMaklumatPemohon.addElement(h);
				bil++;
				count++;
			}
			
		} finally {
			if (db != null)
				db.close();
		}
	}
	
	private void setMaklumatSimati(Map parameters) throws Exception {
		Db db = null;
		
		String sql = "";
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		try {
			db = new Db();
			beanMaklumatSimati = new Vector();
			Statement stmt = db.getStatement();
			SQLRenderer r = new SQLRenderer();

			r.add("B.ID_PERMOHONAN");
			r.add("A.NAMA_SIMATI");
			r.add("A.NO_SIJIL_MATI");
			r.add("A.TARIKH_MATI");
			r.add("A.WAKTU_KEMATIAN");
			r.add("G.KETERANGAN");
			r.add("A.ALAMAT_1");
			r.add("A.ALAMAT_2");
			r.add("A.ALAMAT_3");
			r.add("C.NAMA_NEGERI");
			r.add("D.KETERANGAN AS NAMA_BANDAR");
			r.add("A.POSKOD");
			
			
			r.add("E.ID_FAIL",r.unquote("B.ID_FAIL"));
			r.add("B.ID_PERMOHONAN",r.unquote("F.ID_PERMOHONAN"));
			r.add("A.ID_SIMATI",r.unquote("F.ID_SIMATI"));
			r.add("C.ID_NEGERI(+)",r.unquote("A.ID_NEGERI"));
			r.add("D.ID_BANDAR(+)",r.unquote("A.ID_BANDAR"));
			r.add("G.ID_BUKTIMATI",r.unquote("A.ID_BUKTIMATI"));
			
			r.add("E.NO_FAIL", parameters.get("NoFail").toString());

			sql = r.getSQLSelect("TBLPPKSIMATI A, TBLPPKPERMOHONAN B, TBLRUJNEGERI C,TBLRUJBANDAR D, TBLPFDFAIL E, TBLPPKPERMOHONANSIMATI F, TBLPPKRUJBUKTIMATI G");
			myLogger.debug(sql);
			ResultSet rs = stmt.executeQuery(sql);
			
			Hashtable h;
			int bil = 1;
			Integer count = 0;
			
			String alamat1 = "";
			String alamat2 = "";
			String alamat3 = "";
			
			while (rs.next()) {
				h = new Hashtable();
				
				alamat1 = rs.getString("ALAMAT_1")==null?"":rs.getString("ALAMAT_1").toUpperCase();
				alamat2 = rs.getString("ALAMAT_2")== null?"":rs.getString("ALAMAT_2").toUpperCase();
				alamat3 = rs.getString("ALAMAT_3")== null?"":rs.getString("ALAMAT_3").toUpperCase();
			
				
				h.put("idPermohonan", rs.getString("ID_PERMOHONAN"));
				h.put("namaSimati", rs.getString("NAMA_SIMATI").toUpperCase());
				h.put("singleLineAlamatSimati",alamat1 + alamat2 + "," + alamat3);
				h.put("newLineAlamatSimati",alamat1 + "\n" + alamat2 + "\n" + alamat3);
				h.put("no_kp_simati", noKpSimati(rs.getString("ID_PERMOHONAN")));
				h.put("namaNegeriSimati",rs.getString("NAMA_NEGERI")==null?"":rs.getString("NAMA_NEGERI").toUpperCase());
				h.put("namaBandarSimati",rs.getString("NAMA_BANDAR")==null?"":rs.getString("NAMA_BANDAR").toUpperCase());
				h.put("poskodSimati",rs.getString("POSKOD")==null?"":rs.getString("POSKOD"));
				h.put("noSijilMati", rs.getString("NO_SIJIL_MATI"));
				h.put("tarikhMati", rs.getString("TARIKH_MATI")==null?"":sdf.format(rs.getDate("TARIKH_MATI")));
				h.put("waktuKematian", rs.getString("WAKTU_KEMATIAN")==null?"":rs.getString("WAKTU_KEMATIAN"));
				h.put("buktiKematian", rs.getString("KETERANGAN"));
				beanMaklumatSimati.addElement(h);
				bil++;
				count++;
			}
			
		} finally {
			if (db != null)
				db.close();
		}
	}
	
	private void setMaklumatPerbicaraan(Map parameters) throws Exception {
		Db db = null;
		String sql = "";

		try {
			db = new Db();
			beanMaklumatPerbicaraan = new Vector();
			Statement stmt = db.getStatement();
			SQLRenderer r = new SQLRenderer();

			r.add("E.ID_PERMOHONAN");
			r.add("A.PEG_PENGENDALI");
			r.add("INITCAP(A.ALAMAT_BICARA1) AS ALAMAT_BICARA1");
			r.add("INITCAP(A.ALAMAT_BICARA2) AS ALAMAT_BICARA2");
			r.add("INITCAP(A.ALAMAT_BICARA3) AS ALAMAT_BICARA3");
			r.add("INITCAP(C.NAMA_NEGERI) AS NAMA_NEGERI");
			r.add("A.BANDAR");
			r.add("A.POSKOD");

			r.add("D.ID_FAIL",r.unquote("B.ID_FAIL"));
			r.add("B.ID_PERMOHONAN",r.unquote("E.ID_PERMOHONAN"));
			r.add("E.ID_KEPUTUSANPERMOHONAN",r.unquote("A.ID_KEPUTUSANPERMOHONAN"));
			r.add("C.ID_NEGERI",r.unquote("A.ID_NEGERIBICARA"));
			
			r.add("D.NO_FAIL", parameters.get("NoFail").toString());

			sql = r.getSQLSelect("TBLPPKPERBICARAAN A, TBLPPKPERMOHONAN B, TBLRUJNEGERI C,TBLPFDFAIL D, TBLPPKKEPUTUSANPERMOHONAN E");
			ResultSet rs = stmt.executeQuery(sql);

			Hashtable h;
			int bil = 1;
			Integer count = 0;

			while (rs.next()) {
				h = new Hashtable();
				
				h.put("idPermohonan", rs.getString("ID_PERMOHONAN"));
				h.put("pegPengendali", rs.getString("PEG_PENGENDALI"));
				h.put("singleLineAlamat",rs.getString("ALAMAT_BICARA1") + "," + rs.getString("ALAMAT_BICARA2") + "," + rs.getString("ALAMAT_BICARA3"));
				h.put("newLineAlamat",rs.getString("ALAMAT_BICARA1") + "\n" + rs.getString("ALAMAT_BICARA2") + "\n" + rs.getString("ALAMAT_BICARA3"));
				h.put("namaNegeri",rs.getString("NAMA_NEGERI"));
				h.put("bandar",rs.getString("BANDAR"));
				h.put("poskod",rs.getString("POSKOD"));
				h.put("masaBicara", masaBicara(rs.getString("ID_PERMOHONAN")));
				beanMaklumatPerbicaraan.addElement(h);
				bil++;
				count++;
			}
			
		} finally {
			if (db != null)
				db.close();
		}
	}
	
	private void setMaklumatHTA(Map parameters) throws Exception {
		Db db = null;
		String sql = "";

		try {
			db = new Db();
			beanMaklumatHTA = new Vector();
			sql = "SELECT" + 
				  " J.ID_NEGERI AS idnegerijaagan , J.ID_DAERAH as iddaerahjagaan" + 
				  " FROM TBLPFDFAIL A, TBLPPKPERMOHONAN B, TBLPPKPEMOHON P,TBLRUJBANDAR PPBANDAR, TBLRUJNEGERI PPNEGERI,TBLPPKPERMOHONANSIMATI C," +  
				  " TBLPPKSIMATI E, TBLRUJPEJABATURUSAN F, TBLRUJPEJABAT G, TBLRUJNEGERI H, TBLPPKHTA I, TBLRUJPEJABATURUSAN J" +
  
				  " WHERE A.ID_FAIL=B.ID_FAIL" +
				  " AND   B.ID_PERMOHONAN=C.ID_PERMOHONAN" +
				  " AND   C.ID_SIMATI= E.ID_SIMATI" +
				  " AND I.ID_NEGERI  = F.ID_NEGERIURUS" +
				  " AND I.ID_DAERAH = F.ID_DAERAHURUS" +
				  " AND F.ID_PEJABATJKPTG = G.ID_PEJABAT" +
				  " AND G.ID_JENISPEJABAT = F.ID_JENISPEJABAT" +
				  " AND F.ID_JENISPEJABAT = 3" +
				  " AND P.ID_PERMOHONAN= B.ID_PERMOHONAN" +
				  " AND g.id_seksyen = 2" +
				  " AND G.ID_DAERAH = F.ID_DAERAH" +
				  " AND PPBANDAR.ID_BANDAR = P.ID_BANDARSURAT" +
				  " AND PPNEGERI.ID_NEGERI = P.ID_NEGERISURAT" +
				  " AND G.ID_NEGERI  =  H.ID_NEGERI" +
				  " AND G.ID_PEJABAT = F.ID_PEJABATJKPTG" +
				  " AND C.ID_PERMOHONANSIMATI  = I.ID_PERMOHONANSIMATI" +
				  " AND I.ID_NEGERI  = J.ID_NEGERIURUS" +
				  " AND I.ID_DAERAH = J.ID_DAERAHURUS" +
				  " AND J.ID_JENISPEJABAT <> 3" +
				  " AND E.ID_SIMATI = AAA.ID_SIMATI" +
				  " AND A.NO_FAIL = " + parameters.get("NoFail").toString() +
				  " AND I.ID_HTA = " + parameters.get("idhta").toString() ;
	
			Statement stmt = db.getStatement();
			ResultSet rs = stmt.executeQuery(sql);

			Hashtable h;
			int bil = 1;
			Integer count = 0;

			while (rs.next()) {
				h = new Hashtable();
				
				h.put("idnegerijaagan", rs.getString("idnegerijaagan"));
				h.put("iddaerahjagaan", rs.getString("iddaerahjagaan"));
				beanMaklumatHTA.addElement(h);
				bil++;
				count++;
			}
			
		} finally {
			if (db != null)
				db.close();
		}
	}
	private String noKpPemohon (String idPermohonan) throws Exception{
		
		Db db = null;
		String sql = "";
		
		try{
			
		db = new Db();
		
		sql = "SELECT" +
			  " CASE" + 
			  " WHEN length(BBB.NO_KP2)<12 THEN  ''||rtrim(BBB.NO_KP2)||''" +
			  " WHEN length(rtrim(BBB.NO_KP2))=12 then substr(BBB.NO_KP2,1,6) || '-' || substr(BBB.NO_KP2,7,2) || '-' || substr(BBB.NO_KP2,9,4)" + 
			  " ELSE substr(BBB.NO_KP2,1,6) || '-' || substr(BBB.NO_KP2,7,2) || '-' || substr(BBB.NO_KP2,9,4)  ||'  ('||TRIM(substr(BBB.NO_KP2,13,length(BBB.NO_KP2)))||')'" +
			  " END  AS no_kp_pemohon" +
			  " FROM" +
			  " (SELECT" +     
			  " CASE" +
			  " WHEN TBLPPKPEMOHON.NO_KP_BARU IS NULL AND TBLPPKPEMOHON.NO_KP_LAMA IS NOT NULL THEN  TBLPPKPEMOHON.NO_KP_LAMA" +
			  " WHEN TBLPPKPEMOHON.NO_KP_BARU IS NULL AND TBLPPKPEMOHON.NO_KP_LAMA IS NULL THEN  TBLPPKPEMOHON.NO_KP_LAIN" +
			  " WHEN TBLPPKPEMOHON.NO_KP_BARU IS NULL AND TBLPPKPEMOHON.NO_KP_LAIN IS NULL THEN  TBLPPKPEMOHON.NO_KP_LAMA" + 
			  " ELSE TBLPPKPEMOHON.NO_KP_BARU" +
			  " END || '' ||" +     
			  " CASE" + 
			  " WHEN TBLPPKPEMOHON.NO_KP_BARU IS NOT NULL AND TBLPPKPEMOHON.NO_KP_LAMA IS NOT NULL THEN  TBLPPKPEMOHON.NO_KP_LAMA" +
			  " END || '' ||" +     
			  " CASE" + 
			  " WHEN TBLPPKPEMOHON.NO_KP_BARU IS  NULL AND TBLPPKPEMOHON.NO_KP_LAMA IS NOT NULL THEN  TBLPPKPEMOHON.NO_KP_LAIN" +
			  " END AS NO_KP2 , ID_PEMOHON, ID_PERMOHONAN" +      
			  " FROM TBLPPKPEMOHON ) BBB,TBLPPKPEMOHON A" +
			  " WHERE A.ID_PEMOHON = BBB.ID_PEMOHON" +
			  " AND BBB.ID_PERMOHONAN = " + idPermohonan;
		
			Statement stmt = db.getStatement();
			ResultSet rs = stmt.executeQuery(sql);
			
			if (rs.next()){
				return rs.getString("no_kp_pemohon").toString().toUpperCase();
			} else {
				return "";
			}
		
		}finally {
			if (db != null)
				db.close();
		}

		
	}
	
	private String noKpSimati (String idPermohonan) throws Exception{
		
		Db db = null;
		String sql = "";
		
		try{
			
		db = new Db();
		
		sql = "SELECT" +
			  " CASE" + 
			  " WHEN length(AAA.NO_KP1)<12 THEN  ''||rtrim(AAA.NO_KP1)||''" +
			  " WHEN length(rtrim(AAA.NO_KP1))=12 then substr(AAA.NO_KP1,1,6) || '-' || substr(AAA.NO_KP1,7,2) || '-' || substr(AAA.NO_KP1,9,4)" + 
			  " ELSE substr(AAA.NO_KP1,1,6) || '-' || substr(AAA.NO_KP1,7,2) || '-' || substr(AAA.NO_KP1,9,4)  ||'  ('||TRIM(substr(AAA.NO_KP1,13,length(AAA.NO_KP1)))||')'" +
			  " END  AS no_kp_simati" +
			  " FROM" +
			  " (SELECT" +     
			  " CASE" +
			  " WHEN TBLPPKSIMATI.NO_KP_BARU IS NULL AND TBLPPKSIMATI.NO_KP_LAMA IS NOT NULL THEN  TBLPPKSIMATI.NO_KP_LAMA" +
			  " WHEN TBLPPKSIMATI.NO_KP_BARU IS NULL AND TBLPPKSIMATI.NO_KP_LAMA IS NULL THEN  TBLPPKSIMATI.NO_KP_LAIN" +
			  " WHEN TBLPPKSIMATI.NO_KP_BARU IS NULL AND TBLPPKSIMATI.NO_KP_LAIN IS NULL THEN  TBLPPKSIMATI.NO_KP_LAMA" + 
			  " ELSE TBLPPKSIMATI.NO_KP_BARU" +
			  " END || '' ||" +     
			  " CASE" + 
			  " WHEN TBLPPKSIMATI.NO_KP_BARU IS NOT NULL AND TBLPPKSIMATI.NO_KP_LAMA IS NOT NULL THEN  TBLPPKSIMATI.NO_KP_LAMA" +
			  " END || '' ||" +     
			  " CASE" + 
			  " WHEN TBLPPKSIMATI.NO_KP_BARU IS  NULL AND TBLPPKSIMATI.NO_KP_LAMA IS NOT NULL THEN  TBLPPKSIMATI.NO_KP_LAIN" +
			  " END AS NO_KP1 , ID_SIMATI" +      
			  " FROM TBLPPKSIMATI ) AAA, TBLPPKPERMOHONAN CCC, TBLPPKPERMOHONANSIMATI DDD" +
			  " WHERE CCC.ID_PERMOHONAN = " + idPermohonan +
			  " AND CCC.ID_PERMOHONAN = DDD.ID_PERMOHONAN" +
			  " AND AAA.ID_SIMATI = DDD.ID_SIMATI";
		
			Statement stmt = db.getStatement();
			ResultSet rs = stmt.executeQuery(sql);
			
			if (rs.next()){
				return rs.getString("no_kp_simati").toString().toUpperCase();
			} else {
				return "";
			}
		
		}finally {
			if (db != null)
				db.close();
		}

		
	}
	
	private String masaBicara (String idPermohonan) throws Exception{
		
		Db db = null;
		String sql = "";
		
		try{
			
		db = new Db();
		
		sql = "SELECT" +
			  " DECODE(substr(A.MASA_BICARA,1,2),1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8,9,9,10,10,11,11,12,12," +
			  " 13,1,14,2,15,3,16,4,17,5,18,6,19,7,20,8,21,9,22,10,23,11,24,12)"+ 
			  " || ':'" + 
			  " || substr(A.masa_bicara,3,4)" + 
			  " || ' '"+ 
			  " || case when A.MASA_BICARA < 1200 then 'Pagi'" + 
			  " when A.MASA_BICARA between 1200 and 1259 then 'Tengahari'" + 
			  " when A.MASA_BICARA between 1300 and 1859 then 'Petang'" + 
			  " else 'Malam'" +
			  " end  as MASA_BICARA" +
			  " FROM TBLPPKPERBICARAAN A, TBLPPKKEPUTUSANPERMOHONAN B, TBLPPKPERMOHONAN C" +
			  " WHERE A.ID_KEPUTUSANPERMOHONAN = B.ID_KEPUTUSANPERMOHONAN" +
			  " AND B.ID_PERMOHONAN = C.ID_PERMOHONAN" +
			  " AND B.ID_PERMOHONAN = " + idPermohonan;

		
			Statement stmt = db.getStatement();
			ResultSet rs = stmt.executeQuery(sql);
			
			if (rs.next()){
				return rs.getString("MASA_BICARA").toString().toUpperCase();
			} else {
				return "";
			}
		
		}finally {
			if (db != null)
				db.close();
		}

		
	}
	
	private Vector getBeanMaklumatPegawai() {
		return beanMaklumatPegawai;
	}
	
	public Vector getBeanMaklumatPermohonan() {
		return beanMaklumatPermohonan;
	}
	
	public Vector getBeanMaklumatPemohon() {
		return beanMaklumatPemohon;
	}
	
	public Vector getBeanMaklumatSimati() {
		return beanMaklumatSimati;
	}
	
	public Vector getBeanMaklumatPerbicaraan() {
		return beanMaklumatPerbicaraan;
	}
	
	public Vector getBeanMaklumatHTA() {
		return beanMaklumatHTA;
	}
	
	//Versioning Control
	public void doVersioning(String borang,String idfail,String nofail,String doSaveVersion) {
		this.setVersion = true;
		this.borang = borang;
		this.idfail = idfail;
		this.nofail = nofail;
		this.doSaveVersion = doSaveVersion;
	}
	
	public void askForVersion(HttpServletRequest request,HttpServletResponse response) 
	throws IOException,Exception {
	    response.setContentType("text/html");
	    PrintWriter out = response.getWriter();
	    out.println("<html>");
	    out.println("<head>");
	    out.println("<title>Versioning Control</title>");
	    out.println("</head>");
	    out.println("<body bgcolor=\"white\">");
	    out.println("<center>");
	    out.println("No Fail:"+this.nofail+"<br><br>");
	    out.println("<hr>");
	    String qS = request.getQueryString();
	    out.println("<a href="+request.getRequestURL()+"?idfail="+this.idfail+"&flagVersion=yes&"+qS+">Simpan History</a> |");
	    out.println("<a href="+request.getRequestURL()+"?idfail="+this.idfail+"&flagVersion=no&"+qS+">Cetak</a><br>");
	    if (hasVersionHistory(this.idfail,this.borang)) {
		    doListing(request,response,this.idfail,this.borang);
	    }

//	    out.println("<input onClick=\" "+request.getRequestURL()+"?idfail="+this.idfail+"&flagVersion=yes \" type=button value=Simpan History>");
//	    out.println("<input type=button value=Cetak></br>");
	    out.println("</center>");
	    out.println("</body>");
	    out.println("</html>");
	}
	
	public boolean hasVersionHistory(String idfail,String borang) throws Exception  {
		boolean output=false;
		String sql = "";
		Db db = null;
		try {
			db = new Db(); 
			sql = "Select count(*) as total from TBLPPKBORANG_HISTORY where id_fail ='"+idfail+"' and nama_borang='"+borang+"'";
			//myLogger.info(sql);
			ResultSet rs = db.getStatement().executeQuery(sql); 
			if (rs.next()){	
				if (rs.getInt("total") > 0) {
					output = true;
				}
			}
		} catch (Exception e) {
			throw new Exception ("error checking versioning :"+e.getMessage());
		}finally {
			if (db != null) db.close();
		}
		return output;		
	}
	
	public void doListing(HttpServletRequest request,HttpServletResponse response,
			String idfail,String  borang) throws IOException,Exception {
		PrintWriter out = response.getWriter();
	    out.println("<hr>");
	    out.println("Senarai </br>");
	    
		String sql = "";
		Db db = null;
		try {
			db = new Db(); 
			sql = "Select id_borang,tarikh_masuk from TBLPPKBORANG_HISTORY where id_fail ='"+idfail+"' and nama_borang='"+borang+"' order by tarikh_masuk desc";
			myLogger.info(sql);
			ResultSet rs = db.getStatement().executeQuery(sql); 
			int x=1;
			 out.println("<table width='50%'   align='center'>");
			 out.println("  <tr>");
				out.println(" <td width='2%' align='center' >Bil</td>");
				out.println(" <td width='1%' ></td>");
				out.println(" <td width='37%' align='left' >ID CETAKAN</a></td>");
				out.println(" <td width='60%' align='left'>Tarikh / Waktu Cetak</td>");
				out.println("  </tr>");
			 
			
			
			while (rs.next()){
				
				out.println("  <tr>");
				out.println(" <td width='2%' >"+x+"</td>");
				out.println(" <td width='1%' >:</td>");
				out.println(" <td width='30%' > <a href="+request.getRequestURL()+"?idfail="+idfail+"&flagVersion=view&idborang="+rs.getString("id_borang")+">"+rs.getString("id_borang")+"</a></td>");
				out.println(" <td width='67%' >"+(rs.getString("tarikh_masuk") == null ? "": rs.getString("tarikh_masuk"))+"</td>");
				out.println("  </tr>");
				
				
				//out.println(x+":"+rs.getString("id_borang")+"<br>");
				//out.println(x+" : <a href="+request.getRequestURL()+"?idfail="+idfail+"&flagVersion=view&idborang="+rs.getString("id_borang")+">"+rs.getString("id_borang")+"</a> <br>");
				x++;
			}
		} catch (Exception e) {
			throw new Exception ("error getting listing:"+e.getMessage());
		}finally {
			if (db != null) db.close();
		}
	    
	}
	
		
}
