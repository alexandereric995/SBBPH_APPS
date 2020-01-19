/**
 * 
 */
package dataCleaning.fpx;

import java.util.Calendar;

import lebah.template.DbPersistence;
import bph.entities.integrasi.FPXRecords;
import bph.entities.rpp.RppRekodTempahanLondon;
import dataCleaning.kew.UtilCleanResit;

/**
 * @author Mohd Faizal
 * 
 */
public class GenerateDuplicateReceiptLondonFPXPayment {

	private static DbPersistence db;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("START");
		manageRPPLondon();
		System.out.println("END");
	}

	private static void manageRPPLondon() {
		String idPermohonan = "18641780305555832";
		String fpxTxnId = "1803231633070662";
		
		try {
			db = new DbPersistence();
			RppRekodTempahanLondon permohonan = db.find(RppRekodTempahanLondon.class, idPermohonan);
			FPXRecords fpxRecords = db.find(FPXRecords.class, fpxTxnId);
			
			if (permohonan != null && fpxRecords != null) {
				generateReceipt(permohonan, fpxRecords, false, db);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}	
	
	private static void generateReceipt(RppRekodTempahanLondon permohonan, FPXRecords fpxRecords, boolean flagCombineResit, DbPersistence db) throws Exception {
		db.begin();
		// REGENERATE MAKLUMAT BAYARAN
		String year = fpxRecords.getFpxTxnTime().substring(0, 4);
		String month = fpxRecords.getFpxTxnTime().substring(4, 6);
		String day = fpxRecords.getFpxTxnTime().substring(6, 8);
		Calendar tarikhBayaran = Calendar.getInstance();
		tarikhBayaran.set(Calendar.YEAR, Integer.valueOf(year));
		tarikhBayaran.set(Calendar.MONTH, Integer.valueOf(month) - 1);
		tarikhBayaran.set(Calendar.DATE, Integer.valueOf(day));
				
		UtilCleanResit.generateResitLondon(permohonan, tarikhBayaran.getTime(), fpxRecords, true, db);
			
		fpxRecords.setCreatedDate(tarikhBayaran.getTime());
		fpxRecords.setFlagModul("LONDON");
		fpxRecords.setFlagManagePayment("Y");
		
		db.commit();
	}
}
