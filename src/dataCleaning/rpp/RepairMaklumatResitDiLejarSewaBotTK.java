/**
 * 
 */
package dataCleaning.rpp;

import java.util.Date;
import java.util.List;

import lebah.template.DbPersistence;
import bph.entities.rpp.RppAkaun;

/**
 * @author Mohd Faizal
 *
 */
public class RepairMaklumatResitDiLejarSewaBotTK {
	
	private static DbPersistence db;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("START JOB : " + new Date());
		try {
			db = new DbPersistence();
			db.begin();
			doJob(db);
			db.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		System.out.println("END JOB : " + new Date());
	}	
	
	private static void doJob(DbPersistence db) {
		List<RppAkaun> list = db.list("select x from RppAkaun x where x.permohonan.statusBayaran = 'Y' and x.kodHasil.id = '74304' order by x.permohonan.id asc");
		for (int i = 0; i < list.size(); i++) {
			RppAkaun akaun = list.get(i);
			if (akaun.getPermohonan() != null) {
				if (akaun.getPermohonan().getResitSewa() != null) {
					if (akaun.getPermohonan().getResitSewa().getNoResit() != null) {
						akaun.setNoResit(akaun.getPermohonan().getResitSewa().getNoResit());
					}
				}
			}				
		}
	}
}
