package bph.laporan.rpp;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.entities.kod.JenisUnitRPP;
import bph.laporan.ReportServlet;
import db.persistence.MyPersistence;

public class LaporanKadarPengunaan extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(LaporanKadarPengunaan.class);
	private MyPersistence mp;	
	
	public LaporanKadarPengunaan() {
		
		
		super.setFolderName("rpp");
		super.setReportName("LaporanKadarPengunaan");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
		
		String namaJenisPeranginan = "";
		String namaPeranginan = "";
		String namaJenisUnit = "";
		String idJenisUnit = (String) parameters.get("idJenisUnit");
		try {
			mp = new MyPersistence();
			
			JenisUnitRPP jenisUnit = (JenisUnitRPP) mp.find(JenisUnitRPP.class, idJenisUnit);
			if (jenisUnit != null) {
				namaJenisUnit = jenisUnit.getKeterangan();
				
				if (jenisUnit.getPeranginan() != null) {
					namaPeranginan = jenisUnit.getPeranginan().getNamaPeranginan();
					
					if (jenisUnit.getPeranginan().getJenisPeranginan() != null) {
						namaJenisPeranginan = jenisUnit.getPeranginan().getJenisPeranginan().getKeterangan();
					}
				}
			}
			
 		} catch (Exception ex) {
 			ex.printStackTrace();
 		} finally {
 			if (mp != null) { mp.close(); }
 		}
		
		parameters.put("namaJenisPeranginan", namaJenisPeranginan);
		parameters.put("namaPeranginan", namaPeranginan);
		parameters.put("namaJenisUnit", namaJenisUnit);
	}	
}
