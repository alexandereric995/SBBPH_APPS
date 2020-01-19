/**
 * 
 */
package dataCleaning.kew;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

import db.persistence.MyPersistence;

import lebah.db.Db;
import lebah.template.DbPersistence;
import bph.entities.kewangan.KewBayaranResit;
import bph.entities.kewangan.KewDeposit;
import bph.entities.kewangan.KewInvois;
import bph.entities.kewangan.KewResitSenaraiInvois;
import bph.entities.kewangan.KewSubsidiari;
import bph.entities.kewangan.KewSubsidiariAgihan;
import bph.entities.kewangan.SeqNoResit;
import bph.entities.rpp.RppAkaun;
import bph.entities.rpp.RppPermohonan;
import bph.entities.rpp.RppPermohonanBayaranBalik;
import bph.entities.utiliti.UtilAkaun;

/**
 * @author Mohd Faizal
 * 
 */
public class CleanDuplicateResit {

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
			
//			String sql = "SELECT no_resit FROM kew_bayaran_resit WHERE kod_juruwang = '09' AND flag_jenis_bayaran = 'ONLINE' AND no_resit LIKE '05112019%' GROUP BY no_resit HAVING COUNT(no_resit) > 1 ORDER BY tarikh_resit DESC";
			String sql = "SELECT no_resit FROM kew_bayaran_resit WHERE kod_juruwang = '09' AND flag_jenis_bayaran = 'ONLINE' AND no_resit LIKE '19112019%' ORDER BY id_transaksi_bank ASC";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				List<KewBayaranResit> listResit = db.list("select x from KewBayaranResit x where x.juruwangKod = '09' and x.noResit = '" + rs.getString("no_resit") + "'");
				int i = 0;
				for (KewBayaranResit resit : listResit) {					
//					if (i != 0) {
						db.begin();
						String noResit = generateReceiptNoOnline(db, resit.getTarikhBayaran());
						System.out.println(noResit);
						List<KewResitSenaraiInvois> listRsi = db.list("select x from KewResitSenaraiInvois x where x.resit.id = '" + resit.getId() + "'");
						for (KewResitSenaraiInvois rsi : listRsi) {
							KewInvois invois = rsi.getInvois();
							if (invois != null) {
								RppAkaun rppAkaun = (RppAkaun) db.get("select x from RppAkaun x where x.id = '" + invois.getIdLejar() + "'");
								if (rppAkaun != null) {
									rppAkaun.setNoResit(noResit);
								}
								UtilAkaun utilAkaun = (UtilAkaun) db.get("select x from UtilAkaun x where x.id = '" + invois.getIdLejar() + "'");
								if (utilAkaun != null) {
									utilAkaun.setNoResit(noResit);
								}
							}
							
							KewDeposit deposit = rsi.getDeposit();
							if (deposit != null) {
								deposit.setNoResit(noResit);
								RppAkaun rppAkaun = (RppAkaun) db.get("select x from RppAkaun x where x.id = '" + deposit.getIdLejar() + "'");
								if (rppAkaun != null) {
									rppAkaun.setNoResit(noResit);
								}
							}
						}
//						resit.setNoResitLama(resit.getNoResit());
						resit.setNoResit(noResit);
						db.commit();
//					}
//					i++;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (lebahDb != null) { lebahDb.close(); }
		}
	}
	

	public synchronized static String generateReceiptNoOnline(DbPersistence db, Date tarikhBayaran) {

		String receiptNo = "";
		Integer today = Integer.parseInt(getCurrentDate(tarikhBayaran, "dd"));
		Integer month = Integer.parseInt(getCurrentDate(tarikhBayaran, "MM"));
		Integer year = Integer.parseInt(getCurrentDate(tarikhBayaran, "yyyy"));

		String defaultNoKodJuruwang = "09";// online
		String date = getCurrentDate(tarikhBayaran, "ddMMyyyy");
		int runningNo = 0;
		SeqNoResit sq = (SeqNoResit) db.get("select x from SeqNoResit x where x.day = '" + today + "' and x.month = '" + month + "' and x.year = '"
						+ year + "' and x.kodJuruwang is null");

		if (sq != null) {
			db.pesismisticLock(sq);
			runningNo = sq.getBil() + 1;
			sq.setBil(runningNo);
			sq = (SeqNoResit) db.merge(sq);
		} else {
			runningNo = 1;
			sq = new SeqNoResit();
			sq.setDay(today);
			sq.setMonth(month);
			sq.setYear(year);
			sq.setBil(1);
			db.persist(sq);
			db.flush();
		}
		// format : DD.MM.YYYY.<2 digit pusat penerimaan>.<kod
		// juruwang>.<turutan nombor(3 digit)>.
		String seq = String.format("%05d", runningNo);
		receiptNo = date + "" + defaultNoKodJuruwang + seq;

		return receiptNo;
	}

	public static String getCurrentDate(Date date, String format) {
		String afterFormat = "";
		if (date != null) {
			if (date.toString().length() > 0) {
				afterFormat = new java.text.SimpleDateFormat(format).format(date);
			} else {
				afterFormat = "";
			}
		}
		return afterFormat;
	}
}
