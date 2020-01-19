/**
 * 
 */
package dataCleaning.fpx;

import java.util.Date;
import java.util.List;

import lebah.db.Db;
import lebah.template.DbPersistence;
import bph.entities.integrasi.FPXRecords;
import bph.entities.rpp.RppPermohonan;
import bph.entities.rpp.RppRekodTempahanLondon;
import bph.entities.utiliti.UtilPermohonan;

/**
 * @author Mohd Faizal
 * 
 */
public class UpdateFlagManagePaymentFpxRecords {

	private static DbPersistence db;
	private static Db db2;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("START JOB ON : " + new Date());
		updateFlagManagePaymentFpxRecords();
		System.out.println("FINISH JOB ON : " + new Date());
	}
	
	private static void updateFlagManagePaymentFpxRecords() {
		try {
			db = new DbPersistence();
			List<FPXRecords> list = db.list("select x from FPXRecords x where x.debitAuthCode = '00' and x.flagManagePayment = 'T' order by x.id asc");
			System.out.println(list.size());
			db.begin();
			for (int i = 0; i < list.size(); i++) {
				FPXRecords fpx = list.get(i);
				
				//UTIL
				UtilPermohonan util = db.find(UtilPermohonan.class, fpx.getSellerOrderNo());
				if (util != null) {
					if (util.getStatusBayaran().equals("Y")) {
						fpx.setFlagModul("UTIL");
						fpx.setFlagManagePayment("Y");
					}					
				}
				
				//RPP_PERMOHONAN
				RppPermohonan ir = db.find(RppPermohonan.class, fpx.getSellerOrderNo());
				if (ir != null) {
					if (ir.getStatusBayaran().equals("Y")) {
						fpx.setFlagModul("IR");
						fpx.setFlagManagePayment("Y");
					}
				}
				
				//RPP_LONDON
				RppRekodTempahanLondon london = db.find(RppRekodTempahanLondon.class, fpx.getSellerOrderNo());
				if (london != null) {
					if (london.getFlagBayar().equals("Y")) {
						fpx.setFlagModul("LONDON");
						fpx.setFlagManagePayment("Y");
					}
				}
			}
			db.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}		
	}
}
