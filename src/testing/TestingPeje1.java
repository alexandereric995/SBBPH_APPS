/**
 * 
 */
package testing;

import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import lebah.db.Db;
import lebah.template.DbPersistence;
import bph.entities.qtr.KuaPenghuni;
import bph.entities.qtr.KuaPermohonan;

/**
 * @author Mohd Faizal
 * 
 */
public class TestingPeje1 {

	private static DbPersistence db;
	private static Db db2;
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("START JOB ON : " + new Date());
		
		doJob();
		
		System.out.println("FINISH JOB ON : " + new Date());
	}

	private static void doJob() {
		int count = 0;
		Db lebahDb = null;
		try {
			db = new DbPersistence();
			lebahDb = new Db();
			Statement stmt = lebahDb.getStatement();	

//			db.begin();
			List<KuaPenghuniMigrate> listPenghuniMigrate = db.list("select x from KuaPenghuniMigrate x where x.flagMigrate = 'S'");
			for (KuaPenghuniMigrate penghuniMigrate : listPenghuniMigrate) {
				KuaPenghuni penghuni = (KuaPenghuni) db.get("select x from KuaPenghuni x where"
						+ " x.pemohon.id = '" + penghuniMigrate.getNoPengenalan() + "' and x.kuarters.id = '" + penghuniMigrate.getIdKuarters() + "'");
				if (penghuni == null) {					
					List<KuaPermohonan> listPermohonan = db.list("select x from KuaPermohonan x where x.pemohon.id = '" + penghuniMigrate.getNoPengenalan() + "'");
					if (listPermohonan.size() > 1) {
						count++;
						for (KuaPermohonan permohonan : listPermohonan) {
							System.out.println(permohonan.getId() + " : " + permohonan.getPemohon().getId() + " : " + permohonan.getStatus().getKeterangan());
						}
//						KuaPermohonan permohonan = listPermohonan.get(0);
//						if (permohonan != null) {
//							penghuni = new KuaPenghuni();
//							penghuni.setPermohonan(permohonan);
//							penghuni.setPemohon(permohonan.getPemohon());
//							penghuni.setKuarters(db.find(KuaKuarters.class, penghuniMigrate.getIdKuarters()));
//							if (penghuniMigrate.getTarikhMasuk() != null)
//								penghuni.setTarikhMasukKuarters(sdf.parse(penghuniMigrate.getTarikhMasuk()));
//							if (penghuniMigrate.getTarikhKeluar() != null)
//								penghuni.setTarikhKeluarKuarters(sdf.parse(penghuniMigrate.getTarikhKeluar()));
//							penghuni.setNoRujukanKuartersSyspintar(penghuniMigrate.getNoRujukanKuarters());
//							db.persist(penghuni);
//							
//							permohonan.setStatus(db.find(Status.class, "1423101446117"));
//							
//							penghuniMigrate.setFlagMigrate("Y");
//							penghuniMigrate.setMsg(null);
//						}
					}
				}
			}
//			db.commit();
			System.out.println(count);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (lebahDb != null) { lebahDb.close(); }
		}
	}
}
