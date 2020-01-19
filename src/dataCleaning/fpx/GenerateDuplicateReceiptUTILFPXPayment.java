/**
 * 
 */
package dataCleaning.fpx;

import java.util.Calendar;

import lebah.template.DbPersistence;
import bph.entities.integrasi.FPXRecords;
import bph.entities.utiliti.UtilPermohonan;
import dataCleaning.kew.UtilCleanResit;

/**
 * @author Mohd Faizal
 * 
 */
public class GenerateDuplicateReceiptUTILFPXPayment {

	private static DbPersistence db;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("START");
		manageUtil();
		System.out.println("END");
	}

	private static void manageUtil() {
		String idPermohonan = "193574852133815";
		String fpxTxnId = "1910111633420830";
		
		try {
			db = new DbPersistence();
			UtilPermohonan permohonan = db.find(UtilPermohonan.class, idPermohonan);
			FPXRecords fpxRecords = db.find(FPXRecords.class, fpxTxnId);
			
			if (permohonan != null && fpxRecords != null) {
				generateReceipt(permohonan, fpxRecords, db);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}	
	
	private static void generateReceipt(UtilPermohonan permohonan, FPXRecords fpxRecords, DbPersistence db) throws Exception {
		db.begin();
		// REGENERATE MAKLUMAT BAYARAN
		String year = fpxRecords.getFpxTxnTime().substring(0, 4);
		String month = fpxRecords.getFpxTxnTime().substring(4, 6);
		String day = fpxRecords.getFpxTxnTime().substring(6, 8);
		Calendar tarikhBayaran = Calendar.getInstance();
		tarikhBayaran.set(Calendar.YEAR, Integer.valueOf(year));
		tarikhBayaran.set(Calendar.MONTH, Integer.valueOf(month) - 1);
		tarikhBayaran.set(Calendar.DATE, Integer.valueOf(day));
				
		UtilCleanResit.generateResitUtiliti(permohonan, tarikhBayaran.getTime(), fpxRecords, true, db);
			
		fpxRecords.setCreatedDate(tarikhBayaran.getTime());
		fpxRecords.setFlagModul("UTIL");
		fpxRecords.setFlagManagePayment("Y");
		
		db.commit();
	}
}
