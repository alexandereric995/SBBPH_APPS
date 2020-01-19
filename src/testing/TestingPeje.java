/**
 * 
 */
package testing;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

import lebah.db.Db;
import lebah.template.DbPersistence;
import bph.entities.kewangan.KewBayaranResit;
import bph.entities.kewangan.KewDeposit;
import bph.entities.kewangan.KewInvois;
import bph.entities.kewangan.KewResitSenaraiInvois;
import bph.entities.kewangan.KewSubsidiari;
import bph.entities.kewangan.KewSubsidiariAgihan;
import bph.entities.rpp.RppAkaun;
import bph.entities.rpp.RppPermohonan;
import bph.entities.rpp.RppPermohonanBayaranBalik;
import bph.entities.utiliti.UtilAkaun;

/**
 * @author Mohd Faizal
 * 
 */
public class TestingPeje {

	private static DbPersistence db;
	private static Db db2;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("START JOB ON : " + new Date());
		
		doJob();
		
		System.out.println("FINISH JOB ON : " + new Date());
	}

	private static void doJob() {
		Db lebahDb = null;
		try {
			db = new DbPersistence();
			lebahDb = new Db();
			Statement stmt = lebahDb.getStatement();	
			int z = 1;
			for (int i = 2014; i < 2020; i++) {
				for (int y = 1; y <= 12; y++) {
					db.begin();
					String sql = "SELECT id FROM kew_bayaran_resit WHERE id_pembayar != no_pengenalan_pembayar AND flag_jenis_bayaran != 'ONLINE'"
							+ " and YEAR(tarikh_resit) = '" + i + "' and MONTH(tarikh_resit) = '" + y + "'";
					ResultSet rs = stmt.executeQuery(sql);
					while (rs.next()) {
						KewBayaranResit resit = db.find(KewBayaranResit.class, rs.getString("id"));
						if (resit != null) {
							List<KewResitSenaraiInvois> listRSI = db.list("select x from KewResitSenaraiInvois x where x.resit.id = '" + resit.getId() + "'");
							if (listRSI.size() > 0) {
								for (KewResitSenaraiInvois rsi : listRSI) {
									if (rsi.getDeposit() != null) {
										KewDeposit deposit = rsi.getDeposit();
										if (deposit.getPendeposit() != null && !deposit.getPendeposit().getId().equalsIgnoreCase("FAIZAL")) {
											resit.setPembayar(deposit.getPendeposit());
										}
//										System.out.println("DEPOSIT : " + resit.getNoResit() + " - " + resit.getPembayar().getId() + " - " + deposit.getPendeposit().getId());
									}
									
									if (rsi.getInvois() != null) {
										KewInvois invois = rsi.getInvois();
										if (invois.getPembayar() != null && !invois.getPembayar().getId().equalsIgnoreCase("FAIZAL")) {
											resit.setPembayar(invois.getPembayar());
										}
//										System.out.println("INVOIS : " + resit.getNoResit() + " - " + resit.getPembayar().getId() + " - " + invois.getUserPendaftar().getId());
									}
								}
							}
							System.out.println(z);
							z++;
						}
					}
					db.commit();					
				}
			}			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (lebahDb != null) { lebahDb.close(); }
		}
	}
}
