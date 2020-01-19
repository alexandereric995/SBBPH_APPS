package bph.laporan;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import lebah.db.Db;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.util.JRLoader;

public class RTFConverter {

	public static void main (String args[]) {
		Db db = null;
		Connection conn = null;
		try {
		conn = new Db().getConnection();
		RTFConverter r = new RTFConverter();
		Map parameters = new HashMap();
		parameters.put("idfail", "53533");
		
		String folder = "D:\\eclipse-Version\\MyWorkspace\\eTapp_22122009\\reports\\example\\";
		String file = "KulitFail";
		JasperReport jasperReport = r.getCompiledReport(folder + file );
		JasperFillManager.fillReportToFile(jasperReport,folder + file + ".jrprint", parameters,conn);
		JasperExportManager.exportReportToPdfFile(folder + file + ".jrprint");
		System.out.println( "PDF file created using .jrprint Jasper file");
		
		//create a RTF file
		File sourceFile = new File(folder + file + ".jrprint");
		JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);
		File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".rtf");
		JRRtfExporter pdfExporter = new JRRtfExporter();
		pdfExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		pdfExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
		pdfExporter.exportReport();
		System.out.println( "RTF file created using .jrprint Jasper file");
		
		//create an Excel file
		destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".xls");
		
		JRXlsExporter xlsExporter = new JRXlsExporter();
					
		xlsExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		xlsExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
		xlsExporter.setParameter(JRXlsAbstractExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
		
		xlsExporter.exportReport();
		
		System.out.println( "Excel file created using .jrprint Jasper file");
		
		} catch (Exception e) { e.printStackTrace();}
		finally {
	    	try {
	    		if (conn != null) conn.close();
	    		if (db != null) db.close();
	    	}catch (SQLException xx) {}
		}
	}
	
	public RTFConverter() {
		
	}
	
	public void doConvert() {
		File sourceFile = new File("C:/JasperReports/contacts.jrprint");
	}
	
	private JasperReport getCompiledReport(String filename)
	throws JRException {
		File reportFile;
		reportFile = new File(filename+".jasper");
		if (!reportFile.exists()) {
			JasperCompileManager.compileReportToFile(filename+ ".jrxml");
		}
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reportFile.getPath());

		return jasperReport;
}
	
}
