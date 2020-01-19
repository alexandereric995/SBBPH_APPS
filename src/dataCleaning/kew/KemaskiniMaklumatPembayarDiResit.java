/**
 * 
 */
package dataCleaning.kew;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

import lebah.db.Db;
import lebah.template.DbPersistence;
import bph.entities.kewangan.KewBayaranResit;
import bph.entities.kewangan.KewResitSenaraiInvois;
import bph.utils.UtilKewangan;

/**
 * @author Mohd Faizal
 * 
 */
public class KemaskiniMaklumatPembayarDiResit {

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
		Connection conn = null;
		String sql = "";
		try {
			db2 = new Db();
			conn = db2.getConnection();
			conn.setAutoCommit(false);
			Statement stm = db2.getStatement();
			db = new DbPersistence();
			db.begin();
			
			List<KewBayaranResit> listResit = db.list("select x from KewBayaranResit x where x.id is not null and x.noPengenalanPembayar is null");
			int i = 0;
			for (KewBayaranResit resit : listResit) {
				System.out.println(i++ + " : " + listResit.size() + " - " + resit.getNoResit());
				if (resit.getPembayar() != null) {
					if (resit.getNoPengenalanPembayar() == null)
						resit.setNoPengenalanPembayar(resit.getPembayar().getId());
					if (resit.getNamaPembayar() == null)
						resit.setNamaPembayar(resit.getPembayar().getUserName());
					if (resit.getAlamatPembayar() == null)
						resit.setAlamatPembayar(UtilKewangan.getAlamatPembayar(resit.getPembayar()));
				} else {
					KewResitSenaraiInvois rsiI = (KewResitSenaraiInvois) db.get("select x from KewResitSenaraiInvois x where x.resit.id = '" + resit.getId() + "' and x.invois.id is not null");
					if (rsiI != null) {
						if (rsiI.getInvois().getPembayarLain() != null) {
							if (resit.getNoPengenalanPembayar() == null)
								resit.setNoPengenalanPembayar(rsiI.getInvois().getPembayarLain().getId());
							if (resit.getNamaPembayar() == null)
								resit.setNamaPembayar(rsiI.getInvois().getPembayarLain().getNama());
							if (resit.getAlamatPembayar() == null)
								resit.setAlamatPembayar(UtilKewangan.getAlamatPembayarLain(rsiI.getInvois().getPembayarLain()));
						}
					} else {
						KewResitSenaraiInvois rsiD = (KewResitSenaraiInvois) db.get("select x from KewResitSenaraiInvois x where x.resit.id = '" + resit.getId() + "' and x.deposit.id is not null");
						if (rsiD != null) {
							if (resit.getNoPengenalanPembayar() == null)
								resit.setNoPengenalanPembayar(rsiD.getDeposit().getPendeposit().getId());
							if (resit.getNamaPembayar() == null)
								resit.setNamaPembayar(rsiD.getDeposit().getPendeposit().getUserName());
							if (resit.getAlamatPembayar() == null)
								resit.setAlamatPembayar(UtilKewangan.getAlamatPembayar(rsiD.getDeposit().getPendeposit()));
						}
					}
				}
			}
			
			db.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}		
	}
}
