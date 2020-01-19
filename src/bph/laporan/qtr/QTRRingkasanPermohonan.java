package bph.laporan.qtr;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.entities.qtr.KuaPermohonan;
import bph.laporan.ReportServlet;
import db.persistence.MyPersistence;

public class QTRRingkasanPermohonan extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(QTRRingkasanPermohonan.class);
	private MyPersistence mp;
	public QTRRingkasanPermohonan() {
		
		
		super.setFolderName("kuarters");
		super.setReportName("QTRRingkasanPermohonan");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
		
		String idPermohonan = "";
		String catatan="";
		if (parameters.get("idPermohonan") != null){
			
			idPermohonan = (String)parameters.get("idPermohonan");
		}
		try {
			mp = new MyPersistence();
			KuaPermohonan kp = (KuaPermohonan) mp.find(KuaPermohonan.class, idPermohonan);
			if(kp!=null){
				catatan=kp.getCatatan();
				catatan=catatan.replace("&#13;&#10;", "\n");
			}
		} catch (Exception e) {
			System.out.println("Error simpanCatatan : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		parameters.put("idPermohonan",idPermohonan);	
		parameters.put("catatan",catatan);
	}
	
}
