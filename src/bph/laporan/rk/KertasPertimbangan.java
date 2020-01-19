package bph.laporan.rk;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.entities.rk.RkMesyuaratPermohonan;
import bph.laporan.ReportServlet;
import db.persistence.MyPersistence;

public class KertasPertimbangan extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(KertasPertimbangan.class);
	private MyPersistence mp;	
	
	public KertasPertimbangan() {		
		
		super.setFolderName("rk");
		super.setReportName("KertasPertimbangan");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
		String idPermohonan = "";
		String bilMesyuarat = "";
		String jenisPermohonan = "";		
		String idMesyuaratPermohonan = (String) parameters.get("ID_MESYUARAT_PERMOHONAN");
		try {
			mp = new MyPersistence();
			RkMesyuaratPermohonan mesyuaratPermohonan = (RkMesyuaratPermohonan) mp.find(RkMesyuaratPermohonan.class, idMesyuaratPermohonan);
			if (mesyuaratPermohonan != null) {
				if (mesyuaratPermohonan.getPermohonan() != null) {
					idPermohonan = mesyuaratPermohonan.getPermohonan().getId();
					jenisPermohonan = mesyuaratPermohonan.getPermohonan().getJenisPermohonan();					
				}
				if (mesyuaratPermohonan.getMesyuarat() != null) {
					bilMesyuarat = mesyuaratPermohonan.getMesyuarat().getBil();			
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		parameters.put("ID_PERMOHONAN", idPermohonan);
		parameters.put("JENIS_PERMOHONAN", jenisPermohonan);
		parameters.put("BIL_MESYUARAT", bilMesyuarat);
	}	
}
