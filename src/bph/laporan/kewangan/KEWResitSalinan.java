package bph.laporan.kewangan;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.entities.kewangan.KewBayaranResit;
import bph.laporan.ReportServlet;
import bph.utils.NumberToWords;
import db.persistence.MyPersistence;

public class KEWResitSalinan extends ReportServlet{
	
	private MyPersistence mp;	
	static Logger myLog = Logger.getLogger(KEWResitSalinan.class);
	
	public KEWResitSalinan() {		

		super.setFolderName("kewangan");
		super.setReportName("KEWResitSalinan");
	}

	@Override
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {	
		
		String jumlahDalamPerkataan = "";
		try {	
			mp = new MyPersistence();			
			KewBayaranResit resit = (KewBayaranResit) mp.find(KewBayaranResit.class, parameters.get("idResit"));
			if (resit != null) {
				//ADD BY PEJE - 15102018 - USING NEW RECEIPT TEMPLATE
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				String startDateInString = "15/10/2018";
				Date startDate = sdf.parse(startDateInString);
				
				if (resit.getTarikhBayaran() != null) {
					if (resit.getTarikhBayaran().after(startDate) || resit.getTarikhBayaran().equals(startDate)) {
						//NEW TEMPLATE
						super.setReportName("KEWResitSalinanV2");
					}
				}	
				jumlahDalamPerkataan = NumberToWords.convertNumberToWords(String.valueOf(resit.getJumlahAmaunBayaran()));
				if (jumlahDalamPerkataan != "") {
					jumlahDalamPerkataan = jumlahDalamPerkataan + " SAHAJA";
				}
				
				mp.begin();
				if (resit.getBilCetakResit() != null) {
					resit.setBilCetakResit(resit.getBilCetakResit() + 1);
					resit.setTarikhCetakResit(new Date());
				} else {
					resit.setBilCetakResit(1);
					resit.setTarikhCetakResit(new Date());
				}
				mp.commit();
			}			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		parameters.put("jumlahDalamPerkataan", jumlahDalamPerkataan.toUpperCase());
	}	
}
