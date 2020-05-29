package dataCleaning.kuarters;

import java.io.IOException;
import java.util.List;

import lebah.template.DbPersistence;
import bph.entities.kod.Status;
import bph.entities.portal.Giliran;
import bph.entities.qtr.KuaAgihan;
import bph.entities.qtr.KuaKuarters;
import bph.entities.qtr.KuaPermohonan;

/**
 * @author Mohd Faizal
 *
 */
public class BatalTawaran {
	
	private static DbPersistence db;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			String[] ic = {	
							"781231025438",
							"791009065120",
							"741213035388",
							"821205055151",
							"840304036022"
						  };
			batalTawaran(ic);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	private static void batalTawaran(String[] ic) throws IOException {
        System.out.println("START batalTawaran");

        try {
            db = new DbPersistence();
            db.begin();
            for (int i = 0; i < ic.length; i++) {
            	List<KuaPermohonan> listPermohonan = db.list("select x from KuaPermohonan x where x.pemohon.id = '" + ic[i].trim() + "' order by x.id asc");
            	if (listPermohonan.size() == 1) {
            		KuaPermohonan permohonan = listPermohonan.get(0);
            		permohonan.setStatus(db.find(Status.class, "1431327994524"));
            		
            		KuaAgihan agihan = (KuaAgihan) db.get("select x from KuaAgihan x where x.permohonan.id = '" + permohonan.getId() + "'");
            		if (agihan != null) {
            			Giliran giliran = (Giliran) db.get("select x from Giliran x where x.noKP = '" + permohonan.getPemohon().getId() + "' and x.kelasKuarters = '" + agihan.getKelasKuarters() + "'");
            			if (giliran != null) {
            				db.remove(giliran);
            			}
            			
            			KuaKuarters kuarters = agihan.getKuarters();
            			if (kuarters != null) {
            				kuarters.setFlagAgihan(0);
            			}
            			db.remove(agihan);
            			
            			
            		}            		
            	} else if (listPermohonan.size() == 0) {
            		System.out.println("TIADA PERMOHONAN BAGI : " + ic[i].trim());
            	} else {
            		System.out.println("TERDAPAT " + listPermohonan.size() + " BILANGAN PERMOHONAN BAGI : " + ic[i].trim());
            	}
            }           
            db.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("END batalTawaran");
    }
}
