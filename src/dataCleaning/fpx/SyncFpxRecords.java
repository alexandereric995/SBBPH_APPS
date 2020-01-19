/**
 * 
 */
package dataCleaning.fpx;

import java.io.IOException;
import java.util.List;

import lebah.template.DbPersistence;
import bph.entities.integrasi.FPXRecords;
import bph.entities.integrasi.IntFPX;

/**
 * @author Mohd Faizal
 *
 */
public class SyncFpxRecords {
	
	private static DbPersistence db;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
		
			updateFpxRecords();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	private static void updateFpxRecords() throws IOException {
        System.out.println("START updateFpxRecords");

        try {
            db = new DbPersistence();
            db.begin();
            List<IntFPX> listRecords = db.list("select x from IntFPX x order by x.id asc");
            for (int i = 0; i < listRecords.size(); i++) {
            	IntFPX records = db.find(IntFPX.class, listRecords.get(i).getId());
                if (records != null) {
                    FPXRecords fpxRecords = db.find(FPXRecords.class, records.getFpxTxnId());
                    if (fpxRecords == null) {
                        fpxRecords = new FPXRecords();
                        fpxRecords.setId(records.getFpxTxnId());
                        fpxRecords.setCreditAuthCode("00");
                        fpxRecords.setCreditAuthNo(records.getCreditAuthNo());
                        fpxRecords.setDebitAuthCode("00");
                        fpxRecords.setDebitAuthNo(records.getDebitAuthNo());
                        fpxRecords.setFpxTxnTime(records.getFpxTxnTime());
                        fpxRecords.setSellerExId("EX00000345");
                        fpxRecords.setSellerExOrderNo(records.getSellerExOrderNo());
                        fpxRecords.setSellerId("SE00000392");
                        fpxRecords.setSellerOrderNo(records.getSellerOrderNo());
                        fpxRecords.setSellerTxnTime(records.getSellerTxnTime());
                        fpxRecords.setTxnAmount(records.getTxnAmount()); 
                        fpxRecords.setFlagManagePayment("T");
                        db.persist(fpxRecords);

                        records.setFlagSync("Y");
                        records.setMsg("SUCCESS");
                    } else {
                    	records.setFlagSync("T");
                    	records.setMsg("EXIST");
                    }
                }
            }
            db.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("END updateFpxRecords");
    }
}
