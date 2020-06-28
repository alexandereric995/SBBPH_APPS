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
		String noResit = "'31072008B00024','07082008B00009','08102008B00003','04112008C00015','10112008C00006','22122008B00013','22122008B00014','15012009B00002'," +
				"'08022009C00011','14032009C00007','25082015Y00052','21082015W00014','25082015W00005','25082015W00007','25082015Y00025','25082015Y00027','25082015Y00049'," +
				"'25082015Y00045','25082015Y00031','24082015W00018','20082015S00014','24082015W00014','24082015W00016','20082015S00017','20082015S00023','24082015W00007'," +
				"'25082015Y00053','25082015W00004','25082015W00006','25082015Y00026','25082015Y00028','25082015Y00050','25082015Y00046','25082015Y00032','24082015W00017'," +
				"'20082015S00013','24082015W00013','24082015W00015','20082015S00016','20082015S00022','24082015W00008'";
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
					+ " and a.ReceiptNo in (" + noResit + ")";
			System.out.println(sql);
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
				kewTemp.setNoKP(rs.getString("CustomerID"));
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
					kewTempDetail.setAmaun(rs.getDouble(21));
					db.persist(kewTempDetail);
				}				
			}			
			db.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}		
	}
}
