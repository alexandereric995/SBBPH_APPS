/**
 * 
 */
package dataCleaning.kew;

import java.util.Date;
import java.util.List;

import lebah.db.Db;
import lebah.template.DbPersistence;
import bph.entities.kewangan.KewInvois;
import bph.entities.kewangan.KewResitSenaraiInvois;
import bph.entities.rpp.RppAkaun;

/**
 * @author Mohd Faizal
 * 
 */
public class RegenerateResitBotTasikKenyir {

	private static DbPersistence db;
	private static Db db2;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("START JOB ON : " + new Date());
		regenerateResitBotTasikKenyir();
		System.out.println("FINISH JOB ON : " + new Date());
	}
	
	private static void regenerateResitBotTasikKenyir() {
		try {
			db = new DbPersistence();
			db.begin();
			List<RppAkaun> listAkaun = db.list("select x from RppAkaun x where x.permohonan.statusBayaran = 'Y' and x.kodHasil.id = '74304' order by x.id asc");
			for (int j = 0; j < listAkaun.size(); j++) {
				RppAkaun akaun = listAkaun.get(j);
				KewInvois invois = (KewInvois) db.get("select x from KewInvois x where x.idLejar = '" + akaun.getId() + "'");
				if (invois != null) {
					KewResitSenaraiInvois senaraiInvois = (KewResitSenaraiInvois) db.get("select x from KewResitSenaraiInvois x where x.invois.id = '" + invois.getId() + "'");
					if (senaraiInvois == null) {
						System.out.println(akaun.getPermohonan().getId());
						invois.setKredit(invois.getDebit());
						invois.setFlagBayar("Y");
						
						KewResitSenaraiInvois si = new KewResitSenaraiInvois();
						si.setResit(akaun.getPermohonan().getResitSewa());
						si.setInvois(invois);
						si.setFlagJenisResit("INVOIS");
						db.persist(si);
					}
				}					
			}
			db.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}		
	}	
}
