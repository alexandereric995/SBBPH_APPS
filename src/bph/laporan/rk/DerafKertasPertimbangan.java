package bph.laporan.rk;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.entities.rk.RkPermohonan;
import bph.laporan.ReportServlet;
import db.persistence.MyPersistence;

public class DerafKertasPertimbangan extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(DerafKertasPertimbangan.class);
	private MyPersistence mp;	
	
	public DerafKertasPertimbangan() {		
		
		super.setFolderName("rk");
		super.setReportName("KertasPertimbangan");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
		String jenisPermohonan = "";	
		String idPermohonan = (String) parameters.get("ID_PERMOHONAN");
		try {
			mp = new MyPersistence();
			RkPermohonan permohonan = (RkPermohonan) mp.find(RkPermohonan.class, idPermohonan);
			if (permohonan != null) {
				jenisPermohonan = permohonan.getJenisPermohonan();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		parameters.put("ID_PERMOHONAN", idPermohonan);
		parameters.put("BIL_MESYUARAT", "");
		parameters.put("JENIS_PERMOHONAN", jenisPermohonan);		
	}	
}
