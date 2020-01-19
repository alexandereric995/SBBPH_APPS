/**
 * 
 */
package dataCleaning.kew;

import java.io.IOException;
import java.util.List;

import lebah.template.DbPersistence;
import bph.entities.kewangan.KewBayaranResit;
import bph.entities.rpp.RppPermohonan;
import bph.entities.rpp.RppRekodTempahanLondon;
import bph.entities.utiliti.UtilPermohonan;

/**
 * @author Mohd Faizal
 *
 */
public class SyncResitPermohonan {

	private static DbPersistence db;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			
			syncPermohonanResit();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void syncPermohonanResit() throws IOException {		
		System.out.println("START syncPermohonanResit");
		
		try {
			db = new DbPersistence();
			List<RppPermohonan> listPermohonanRPP = db.list("select x from RppPermohonan x where x.rppPeranginan.id not in ('14','3') and x.flagSyspintar = 'T' order by x.id asc");
			System.out.println(listPermohonanRPP.size());
			for (int i = 0; i < listPermohonanRPP.size(); i++) {
				RppPermohonan permohonan = listPermohonanRPP.get(i);
				System.out.println("RPP :" + i + " from " + listPermohonanRPP.size());
				if (permohonan.getResitDeposit() == null && permohonan.getResitSewa() == null) {
					String idResitDeposit = UtilCleanResit.getIdResitDepositRPP(permohonan.getId(), db);
					String idResitSewa = UtilCleanResit.getIdResitSewaRPP(permohonan.getId(), db);
					
					KewBayaranResit resitDeposit = null;
					KewBayaranResit resitSewa = null;
					if (idResitDeposit != null)
						resitDeposit = db.find(KewBayaranResit.class, idResitDeposit);
					if (idResitSewa != null)
						resitSewa = db.find(KewBayaranResit.class, idResitSewa);
					
					db.begin();
					permohonan.setResitDeposit(resitDeposit);
					permohonan.setResitSewa(resitSewa);
					if (idResitDeposit != null)
						resitDeposit.setIdPermohonan(permohonan.getId());
					if (idResitSewa != null)
						resitSewa.setIdPermohonan(permohonan.getId());							
					db.commit();
				}
			}
			
			List<RppRekodTempahanLondon> listPermohonanLondon = db.list("select x from RppRekodTempahanLondon x order by x.id asc");
			System.out.println(listPermohonanLondon.size());
			for (int i = 0; i < listPermohonanLondon.size(); i++) {
				System.out.println("LONDON :" + i + " from " + listPermohonanLondon.size());
				RppRekodTempahanLondon permohonan = listPermohonanLondon.get(i);
				if (permohonan.getResitSewa() == null) {
					String idResitSewa = UtilCleanResit.getIdResitSewaLondon(permohonan.getId(), db);
					
					if (idResitSewa != null) {
						KewBayaranResit resitSewa = db.find(KewBayaranResit.class, idResitSewa);
						
						db.begin();
						permohonan.setResitSewa(resitSewa);
						resitSewa.setIdPermohonan(permohonan.getId());										
						db.commit();
					}
				}
			}
			
			List<UtilPermohonan> listPermohonanUtiliti = db.list("select x from UtilPermohonan x where x.flagSyspintar = 'T' order by x.id asc");
			System.out.println(listPermohonanUtiliti.size());
			for (int i = 0; i < listPermohonanUtiliti.size(); i++) {
				System.out.println("UTIL :" + i + " from " + listPermohonanUtiliti.size());
				UtilPermohonan permohonan = listPermohonanUtiliti.get(i);
				if (permohonan.getResitSewa() == null) {
					String idResitSewa = UtilCleanResit.getIdResitSewaUtiliti(permohonan.getId(), db);
					
					if (idResitSewa != null) {
						KewBayaranResit resitSewa = db.find(KewBayaranResit.class, idResitSewa);
						
						db.begin();
						permohonan.setResitSewa(resitSewa);
						resitSewa.setIdPermohonan(permohonan.getId());										
						db.commit();
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		System.out.println("END syncPermohonanResit");
	}
}
