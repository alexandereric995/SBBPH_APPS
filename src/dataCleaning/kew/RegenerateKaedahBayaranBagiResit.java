/**
 * 
 */
package dataCleaning.kew;

import java.util.Date;
import java.util.List;

import lebah.db.Db;
import lebah.template.DbPersistence;
import bph.entities.kewangan.KewBayaranResit;
import bph.entities.kewangan.KewResitKaedahBayaran;
import bph.entities.kod.CaraBayar;

/**
 * @author Mohd Faizal
 * 
 */
public class RegenerateKaedahBayaranBagiResit {

	private static DbPersistence db;
	private static Db db2;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("START JOB ON : " + new Date());
		regenerateKaedahBayaranBagiResit();
		System.out.println("FINISH JOB ON : " + new Date());
	}

	private static void regenerateKaedahBayaranBagiResit() {
		try {
			db = new DbPersistence();
			List<KewBayaranResit> list = db.list("select x from KewBayaranResit x where x.id not in (select y.resit.id from KewResitKaedahBayaran y) and x.flagJenisBayaran = 'ONLINE' and x.flagVoid = 'T' and x.noResit not in ('26082015S00009', '26082015S00010') order by x.tarikhBayaran desc");
			System.out.println(list.size());
			db.begin();
			for (int i = 0; i < list.size(); i++) {
				KewBayaranResit resit = list.get(i);
				System.out.println(resit.getNoResit() + " " + resit.getTarikhBayaran() + " " + resit.getFlagJenisBayaran());
				KewResitKaedahBayaran kb = new KewResitKaedahBayaran();
				kb.setResit(resit);
				kb.setAmaunBayaran(resit.getJumlahAmaunBayaran());		
				kb.setModBayaran((CaraBayar) db.find(CaraBayar.class, "FPX"));
				db.persist(kb);
			}
			db.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}		
	}	
}
