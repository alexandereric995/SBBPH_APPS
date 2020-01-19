package dataCleaning.util;

import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import lebah.db.Db;
import lebah.template.DbPersistence;

import bph.entities.utiliti.UtilDewan;
import bph.entities.utiliti.UtilGelanggang;
import bph.entities.utiliti.UtilJadualTempahan;

public class PenutupanKeseluruhanDewanDanGelanggang {
	
	private static DbPersistence db;
	private static Db db2;

	/**
	 * @param args
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws ParseException {
		System.out.println("START JOB ON : " + new Date());		
		String tarikhMula = "04-05-2019";		
		String tarikhTamat = "17-06-2019";
		int masaMula = 9;
		int masaTamat = 23;
		doJob(tarikhMula, tarikhTamat, masaMula, masaTamat);		
		System.out.println("FINISH JOB ON : " + new Date());
	}

	private static void doJob(String tarikhMula, String tarikhTamat, int masaMula, int masaTamat) {
		Db lebahDb = null;
		try {
			db = new DbPersistence();
			lebahDb = new Db();
			Statement stmt = lebahDb.getStatement();
			
			Calendar dateMula = Calendar.getInstance();
			dateMula.setTime(new SimpleDateFormat("dd-MM-yyyy").parse(tarikhMula));
			Calendar dateTamat = Calendar.getInstance();
			dateTamat.setTime(new SimpleDateFormat("dd-MM-yyyy").parse(tarikhTamat));
			dateTamat.add(Calendar.DATE, 1);
			
			db.begin();
			while (dateMula.before(dateTamat)) {
				List<UtilDewan> listDewan = db.list("select x from UtilDewan x order by x.id asc");
				for (UtilDewan dewan : listDewan) {
					
					Hashtable h = new Hashtable();
					h.put("idDewan", dewan.getId());
					h.put("date", dateMula.getTime());
					h.put("masa", masaMula);
					List<UtilJadualTempahan> list = db.list(
							"select t from UtilJadualTempahan t where t.dewan.id = :idDewan and t.tarikhTempahan = :date and t.masaMula=:masa",
							h);
					UtilJadualTempahan tempJadualTempahan = null;
					for (int x = 0; x < list.size(); x++) {
						tempJadualTempahan = list.get(x);
						if ("C".equals(tempJadualTempahan.getStatus())) {
							db.remove(tempJadualTempahan);
						}
					}
					
					UtilJadualTempahan jadual = new UtilJadualTempahan();
					jadual.setDewan(dewan);
					jadual.setTarikhTempahan(dateMula.getTime());
					jadual.setMasaMula(masaMula);
					jadual.setMasaTamat(masaTamat);
					jadual.setStatus("C");
					db.persist(jadual);
					
					insertTempahanRelated(jadual.getDewan().getId(), dateMula.getTime(), jadual.getMasaMula(),
							jadual.getMasaTamat(), "C", jadual.getId());
				}				
				dateMula.add(Calendar.DATE, 1);
			}
			db.commit();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (lebahDb != null) { lebahDb.close(); }
		}		
	}
	
	private static void insertTempahanRelated(String idDewan, Date tarikhTempahan, int masaMula, int masaTamat,
			String idStatus, String idTempahan) throws Exception {
		UtilDewan dewan = db.find(UtilDewan.class, idDewan);
		Hashtable h = new Hashtable();
		h.put("idDewan", idDewan);
		List<UtilGelanggang> list = db.list("select t from UtilGelanggang t where t.dewan.id = :idDewan", h);
		UtilGelanggang tempGelanggang = null;
		for (int y = 0; y < list.size(); y++) {
			tempGelanggang = list.get(y);
			UtilJadualTempahan tempJadualTempahan = new UtilJadualTempahan();
			tempJadualTempahan.setDewan(dewan);
			UtilGelanggang gelanggang = db.find(UtilGelanggang.class, tempGelanggang.getId());
			tempJadualTempahan.setGelanggang(gelanggang);
			tempJadualTempahan.setTarikhTempahan(tarikhTempahan);
			tempJadualTempahan.setMasaMula(masaMula);
			tempJadualTempahan.setMasaTamat(masaTamat);
			tempJadualTempahan.setStatus("C");
			db.persist(tempJadualTempahan);
		}
	}
}
