package bph.laporan.senggara;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.entities.senggara.MtnIndenKerja;
import bph.entities.senggara.MtnJKH;
import bph.laporan.ReportServlet;
import bph.utils.NumberToWords;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class CetakIndenKerja extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(CetakIndenKerja.class);
	public CetakIndenKerja() {
		
		
		super.setFolderName("senggara");
		super.setReportName("BorangIndenKerja");

	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
		
		String alamatKuarters = "";
		String jumlahIndenKerja = "";
		String idIndenKerja = (String) parameters.get("idIndenKerja");		
		MyPersistence mp = new MyPersistence();
		try {
			MtnIndenKerja indenKerja = (MtnIndenKerja) mp.find(MtnIndenKerja.class, idIndenKerja);
			if (indenKerja != null) {
				// ADD EXTRA PARAMETER
				List<MtnJKH> listJKH = mp.list("select x from MtnJKH x where x.indenKerja.id = '" + indenKerja.getId() + "'");
				for (MtnJKH jkh : listJKH) {
					if ("".equals(alamatKuarters)) {
						alamatKuarters = jkh.getNoJKH();
					} else {
						alamatKuarters = alamatKuarters + ", " + jkh.getNoJKH();
					}
				}
				parameters.put("alamatKuarters", alamatKuarters);
				
				jumlahIndenKerja = "Ringgit: " 
						+ NumberToWords.convertNumberToWords(indenKerja.getJumlah().toString()) + " sahaja"
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
