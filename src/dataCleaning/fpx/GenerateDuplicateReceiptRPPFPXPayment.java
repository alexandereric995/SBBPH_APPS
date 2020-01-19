/**
 * 
 */
package dataCleaning.fpx;

import java.util.Calendar;

import lebah.template.DbPersistence;
import bph.entities.integrasi.FPXRecords;
import bph.entities.rpp.RppPermohonan;
import dataCleaning.kew.UtilCleanResit;

/**
 * @author Mohd Faizal
 * 
 */
public class GenerateDuplicateReceiptRPPFPXPayment {

	private static DbPersistence db;
 
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("START");
		manageRPP();
		System.out.println("END");
	}

	private static void manageRPP() {
		String idPermohonan = "61123843236363";
		String fpxTxnId = "1911082057570089";
		
		try {
			db = new DbPersistence();
			RppPermohonan permohonan = db.find(RppPermohonan.class, idPermohonan);
			FPXRecords fpxRecords = db.find(FPXRecords.class, fpxTxnId);
			
			if (permohonan != null && fpxRecords != null) {
				generateReceipt(permohonan, fpxRecords, false, db);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}	
	
	private static void generateReceipt(RppPermohonan permohonan, FPXRecords fpxRecords, boolean flagCombineResit, DbPersistence db) throws Exception {
		db.begin();
		// REGENERATE MAKLUMAT BAYARAN
		String year = fpxRecords.getFpxTxnTime().substring(0, 4);
		String month = fpxRecords.getFpxTxnTime().substring(4, 6);
		String day = fpxRecords.getFpxTxnTime().substring(6, 8);
		Calendar tarikhBayaran = Calendar.getInstance();
		tarikhBayaran.set(Calendar.YEAR, Integer.valueOf(year));
		tarikhBayaran.set(Calendar.MONTH, Integer.valueOf(month) - 1);
		tarikhBayaran.set(Calendar.DATE, Integer.valueOf(day));
				
		UtilCleanResit.generateResitRPP(permohonan, tarikhBayaran.getTime(), flagCombineResit, fpxRecords, true, db);
			
		fpxRecords.setCreatedDate(tarikhBayaran.getTime());
		fpxRecords.setFlagModul("IR");
		fpxRecords.setFlagManagePayment("Y");
		
		db.commit();
	}
}
