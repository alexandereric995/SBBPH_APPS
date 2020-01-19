package bph.laporan.rk;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.entities.rk.RkInvois;
import bph.laporan.ReportServlet;
import db.persistence.MyPersistence;

public class InvoisCajIWK extends ReportServlet {

	static Logger myLog = Logger.getLogger(InvoisCajIWK.class);
	private MyPersistence mp;

	public InvoisCajIWK() {

		super.setFolderName("rk");
		super.setReportName("InvoisCajIWK");
	}

	@Override
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
		
		try {
			mp = new MyPersistence();
			
			if (parameters.get("ID_INVOIS") != null) {
				RkInvois invois = (RkInvois) mp.find(RkInvois.class, parameters.get("ID_INVOIS"));
				if (invois != null) {
					mp.begin();
					invois.setBilCetakan(invois.getBilCetakan() + 1);
					mp.commit();
				}				
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
	}
}
