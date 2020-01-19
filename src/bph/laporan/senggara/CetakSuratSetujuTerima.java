package bph.laporan.senggara;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.entities.senggara.MtnIndenKerja;
import bph.laporan.ReportServlet;
import bph.utils.NumberToWords;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class CetakSuratSetujuTerima extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(CetakSuratSetujuTerima.class);
	public CetakSuratSetujuTerima() {
		
		
		super.setFolderName("senggara");
		super.setReportName("SuratSetujuTerima");

	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
		
		String jumlahIndenKerja = "";
		String idIndenKerja = (String) parameters.get("idIndenKerja");		
		MyPersistence mp = new MyPersistence();
		try {
			MtnIndenKerja indenKerja = (MtnIndenKerja) mp.find(MtnIndenKerja.class, idIndenKerja);
			if (indenKerja != null) {
				// ADD EXTRA PARAMETER
				jumlahIndenKerja = "RINGGIT MALAYSIA:" 
						+ NumberToWords.convertNumberToWords(indenKerja.getJumlah().toString()).toUpperCase()
						+ " (RM" + Util.formatDecimal(indenKerja.getJumlah()) + ")"; 
				parameters.put("jumlahIndenKerja", jumlahIndenKerja);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}		
	}	
}
