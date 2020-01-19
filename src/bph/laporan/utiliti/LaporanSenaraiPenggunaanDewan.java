package bph.laporan.utiliti;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.entities.kod.KodPetugas;
import bph.laporan.ReportServlet;
import db.persistence.MyPersistence;

public class LaporanSenaraiPenggunaanDewan extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(LaporanSenaraiPenggunaanDewan.class);
	public LaporanSenaraiPenggunaanDewan() {
		
		
		super.setFolderName("utiliti");
		super.setReportName("LaporanSenaraiPenggunaanDewan");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
		
		String userId = (String) request.getSession().getAttribute("_portal_login");
		
		String senaraiCawangan = "";
		MyPersistence mp = null;
		try {
			mp = new MyPersistence();
			List<KodPetugas> listPetugas = mp.list("select x from KodPetugas x where x.petugas.id = '" + userId + "'");
			if (listPetugas.size() > 0) {
				for (KodPetugas petugas : listPetugas) {
					if (senaraiCawangan == "") {
						senaraiCawangan = petugas.getCawangan().getId();
					} else {
						senaraiCawangan = senaraiCawangan + ", " + petugas.getCawangan().getId();
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		parameters.put("senaraiCawangan", senaraiCawangan);
	}
	
}
