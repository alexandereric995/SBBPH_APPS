/**
 * 
 */
package dataCleaning.kew;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import lebah.db.Db;
import lebah.template.DbPersistence;
import lebah.template.UID;
import bph.entities.kewangan.KewBayaranLainTemp;
import bph.entities.kewangan.KewBayaranLainTempDetail;

/**
 * @author Mohd Faizal
 * 
 */
public class MigrateResitSysPintarKeSBBPH {

	private static DbPersistence db;
	private static Db db2;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("START JOB ON : " + new Date());
		String noResit = "";
		doJob(noResit);
		System.out.println("FINISH JOB ON : " + new Date());
	}
	
	private static void doJob(String noResit) {
		Connection conn = null;
		String sql = "";
		try {
			db2 = new Db();
			conn = db2.getConnection();
			conn.setAutoCommit(false);
			Statement stm = db2.getStatement();
			db = new DbPersistence();
			db.begin();
			
			sql = "select * from syspintar.finance_receipt a, syspintar.finance_otherpayment b where a.ReceiptNo = b.ReceiptNo"
					+ " and a.ReceiptNo = '" + noResit + "'";
			ResultSet rs = stm.executeQuery(sql);
			int i = 0;
			while (rs.next()){
				i++;
				boolean addRecord = false;
				KewBayaranLainTemp kewTemp = (KewBayaranLainTemp) db.get("select x from KewBayaranLainTemp x where x.noResit = '" + rs.getString("ReceiptNo") + "'");
				if (kewTemp == null) {
					kewTemp = new KewBayaranLainTemp();
					kewTemp.setId(UID.getUID());
					addRecord = true;
				}				
				kewTemp.setNoResit(rs.getString("ReceiptNo"));
				kewTemp.setTarikhResit(rs.getDate("DateTime"));
				kewTemp.setNama(rs.getString("FullName"));
				kewTemp.setNoFail(rs.getString("PaymentOrderNo"));
				kewTemp.setJuruwang(rs.getString("CashierCode"));
				kewTemp.setCashierName(rs.getString("CashierName"));
				kewTemp.setMode("Counter");
				kewTemp.setModBayaran(rs.getString("PaymentMethod"));
				kewTemp.setNoDokumen(rs.getString("PaymentDocumentNo"));
				if (addRecord) {
					db.persist(kewTemp);
				}			
				
				if (kewTemp != null) {
					KewBayaranLainTempDetail kewTempDetail = new KewBayaranLainTempDetail();
					kewTempDetail.setId(UID.getUID());
					kewTempDetail.setBayaranLain(kewTemp);
					kewTempDetail.setKod(rs.getString("IncomeCode"));
					kewTempDetail.setKeterangan(rs.getString("Purpose"));
					kewTempDetail.setPerihal(rs.getString("Note"));
					kewTempDetail.setAmaun(rs.getDouble("Amount"));
					db.persist(kewTempDetail);
				}				
			}			
			db.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}		
	}
}
