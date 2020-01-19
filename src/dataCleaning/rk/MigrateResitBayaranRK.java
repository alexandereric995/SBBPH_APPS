/**
 * 
 */
package dataCleaning.rk;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import lebah.db.Db;
import lebah.template.DbPersistence;
import portal.module.Util;
import portal.module.entity.Users;
import bph.entities.kew.KewJenisBayaran;
import bph.entities.kewangan.KewBayaranResit;
import bph.entities.kewangan.KewInvois;
import bph.entities.kewangan.KewResitSenaraiInvois;
import bph.entities.kod.KodHasil;
import bph.entities.rk.RkAkaun;
import bph.entities.rk.RkAkaunMigrate;
import bph.entities.rk.RkFail;
import bph.entities.rk.RkInvois;
import bph.entities.rk.RkPerjanjian;
import bph.entities.rk.RkRuangKomersil;

/**
 * @author Mohd Faizal
 *
 */
public class MigrateResitBayaranRK {
	
	private static String kodHasilDeposit = "79503"; // DEPOSIT PELBAGAI
	private static String kodHasilSewa = "74202"; // SEWA BANGUNAN PEJABAT LUAR WILAYAH	
	private static String kodHasilIWK = "81199"; // BAYARAN-BAYARAN BALIK YANG LAIN (UTILITI/IWK)
	
	private static String kodJenisBayaranDeposit = "04"; // DEPOSIT RUANG KOMERSIL - KEW_DEPOSIT
	private static String kodJenisBayaranSewa = "04"; // SEWA RUANG KOMERSIL - KEW_INVOIS
	private static String kodJenisBayaranIWK = "04"; // BAYARAN IWK RUANG KOMERSIL (PUKAL) - KEW_INVOIS
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	
	private static DbPersistence db;
	private static Db db2;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("START JOB : " + new Date());
		try {
			db = new DbPersistence();
			db.begin();
			String idFail = doJob(db);
			db.commit();

			if (!"".equals(idFail)) {
				updateFail(idFail, true, db); // SET REGENERATE INVOIS TO TRUE				
				migratePembayarLainKepadaPembayarRK();
			}			
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		System.out.println("END JOB : " + new Date());
	}

	private static String doJob(DbPersistence db) {
		String idFail = "";
		List<RkAkaunMigrate> listMigrate = db.list("select x from RkAkaunMigrate x where x.noFail is not null and x.flagMigrate = 'T' order by x.id asc");
		for (int i = 0; i < listMigrate.size(); i++) {
			RkAkaunMigrate akaunMigrate = listMigrate.get(i);
			RkFail fail = (RkFail) db.get("select x from RkFail x where x.noFail = '" + akaunMigrate.getNoFail() + "'");
			if (fail != null) {
				KewBayaranResit resit = (KewBayaranResit) db.get("select x from KewBayaranResit x where x.noResit = '" + akaunMigrate.getNoResit() + "'");
				if (resit != null) {
					if (resitExistInLejar(fail, resit, akaunMigrate, db)) {
						akaunMigrate.setMsg("RESIT EXIST IN LEDGER");
					} else {
						RkAkaun akaun = new RkAkaun();
						akaun.setFail(fail);
						akaun.setTarikhTransaksi(resit.getTarikhBayaran());
						akaun.setKodHasil((KodHasil) db.find(KodHasil.class, akaunMigrate.getIdKodHasil()));					
						akaun.setIdJenisTransaksi("2"); //BAYARAN					
						if (akaunMigrate.getIdKodHasil().equals(kodHasilDeposit)) {
							String keterangan = "BAYARAN DEPOSIT BAGI" + getKeteranganInvoisBulanan(fail);
							akaun.setKeterangan(keterangan);
							akaun.setDebit(Double.valueOf(akaunMigrate.getAmaun()));						
						} else if (akaunMigrate.getIdKodHasil().equals(kodHasilIWK)) {
							String keterangan = "BAYARAN CAJ IWK BAGI" + getKeteranganInvoisBulanan(fail);
							akaun.setKeterangan(keterangan);
							akaun.setKredit(Double.valueOf(Util.RemoveComma(akaunMigrate.getAmaun())));
						} else {
							String keterangan = "BAYARAN SEWA BAGI" + getKeteranganInvoisBulanan(fail);
							akaun.setKeterangan(keterangan);
							akaun.setKredit(Double.valueOf(Util.RemoveComma(akaunMigrate.getAmaun())));
						}
						akaun.setResit(resit);
						db.persist(akaun);	
						akaunMigrate.setFlagMigrate("Y");
						akaunMigrate.setMsg(null);
					}					
				} else {
					if (pelarasanExistInLejar(fail, akaunMigrate.getNoResit(), akaunMigrate, db)) {
						akaunMigrate.setMsg("PELARASAN EXIST IN LEDGER");
					} else {
						RkAkaun akaun = new RkAkaun();
						akaun.setFail(fail);
						Calendar calTarikhBayaran = new GregorianCalendar();
						if (akaunMigrate.getTarikhBayaran() != null) {
							calTarikhBayaran.set(Calendar.DATE, Integer.valueOf(akaunMigrate.getTarikhBayaran().substring(0, 2)));
							calTarikhBayaran.set(Calendar.MONTH, Integer.valueOf(akaunMigrate.getTarikhBayaran().substring(3, 5)) - 1);
							calTarikhBayaran.set(Calendar.YEAR, Integer.valueOf(akaunMigrate.getTarikhBayaran().substring(6, akaunMigrate.getTarikhBayaran().length())));
							akaun.setTarikhTransaksi(calTarikhBayaran.getTime());
						}	
						akaun.setKodHasil((KodHasil) db.find(KodHasil.class, akaunMigrate.getIdKodHasil()));					
						akaun.setIdJenisTransaksi("4"); //PELARASAN KREDIT	
						if (akaunMigrate.getIdKodHasil().equals(kodHasilDeposit)) {
							String keterangan = "BAYARAN DEPOSIT BAGI" + getKeteranganInvoisBulanan(fail);
							akaun.setKeterangan(keterangan);
							akaun.setDebit(Double.valueOf(Util.RemoveComma(akaunMigrate.getAmaun())));					
						} else if (akaunMigrate.getIdKodHasil().equals(kodHasilIWK)) {
							String keterangan = "BAYARAN CAJ IWK BAGI" + getKeteranganInvoisBulanan(fail);
							akaun.setKeterangan(keterangan);
							akaun.setKredit(Double.valueOf(Util.RemoveComma(akaunMigrate.getAmaun())));
						} else {
							String keterangan = "BAYARAN SEWA BAGI" + getKeteranganInvoisBulanan(fail);
							akaun.setKeterangan(keterangan);
							akaun.setKredit(Double.valueOf(Util.RemoveComma(akaunMigrate.getAmaun())));
						}
						
						akaun.setNoRujukanPelarasan(akaunMigrate.getNoResit());
						db.persist(akaun);	
						akaunMigrate.setFlagMigrate("Y");
						akaunMigrate.setMsg(null);
					}
				}
				
				if (idFail.equals("")) {
					idFail = fail.getId();
				} else {
					idFail = idFail + ", " + fail.getId();
				}
			} else {
				akaunMigrate.setMsg("FILE NOT FOUND");
			}			
		}
		return idFail;
	}

	private static boolean resitExistInLejar(RkFail fail, KewBayaranResit resit, RkAkaunMigrate akaunMigrate, DbPersistence db) {
		boolean bool = false;
		try {
			RkAkaun akaun = null;
			akaun = (RkAkaun) db.get("select x from RkAkaun x where x.fail.id = '" + fail.getId() + "' and x.resit.id = '" + resit.getId() + "' and x.kodHasil.id = '" + akaunMigrate.getIdKodHasil() + "'");			
			if (akaun != null) {
				bool = true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return bool;
	}
	
	private static boolean pelarasanExistInLejar(RkFail fail, String noRujukan, RkAkaunMigrate akaunMigrate, DbPersistence db) {
		boolean bool = false;
		try {
			RkAkaun akaun = null;
			akaun = (RkAkaun) db.get("select x from RkAkaun x where x.fail.id = '" + fail.getId() + "' and x.noRujukanPelarasan = '" + noRujukan + "' and x.kodHasil.id = '" + akaunMigrate.getIdKodHasil() + "'");		
			if (akaun != null) {
				bool = true;
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return bool;
	}
	
	private static void updateFail(String idFail, boolean regenerateInvois, DbPersistence db) {
		try {
			List<RkFail> listFail = db.list("select x from RkFail x where x.id in (" + idFail + ")");
			for (RkFail fail : listFail) {
				updateStatusPerjanjian(fail);			
				updateFlagAktifPerjanjianSemasa(fail);
				updateStatusSewaRuang(fail, db);
				janaAkaunKeseluruhanFail(fail, db);			
				updateStatusTunggakanSewa(fail, db);			
				janaAkaunIWKKeseluruhanFail(fail, db);
				updateStatusTunggakanIWK(fail, db);		
				generateInvoisBulanan(fail, regenerateInvois, db);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private static void updateStatusPerjanjian(RkFail fail) {
		Db lebahDb = null;
		Connection conn = null;
		try {
			lebahDb = new Db();
			conn = lebahDb.getConnection();
			conn.setAutoCommit(false);
			Statement stmt = lebahDb.getStatement();
			
			String sql = "update rk_perjanjian set id_status_perjanjian = '1' where flag_aktif = 'Y' and tarikh_tamat >= NOW() and id_fail = '" + fail.getId() + "'";
			stmt.executeUpdate(sql);
			
			sql = "update rk_perjanjian set id_status_perjanjian = '2' where flag_aktif = 'Y' and tarikh_tamat < NOW() and id_fail = '" + fail.getId() + "'";
			stmt.executeUpdate(sql);
			conn.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (lebahDb != null) { lebahDb.close(); }
		}	
	}
	
	private static void updateFlagAktifPerjanjianSemasa(RkFail fail) {
		Db lebahDb = null;
		Connection conn = null;
		String sql = "";
		String idPerjanjian = "";
			
		try {
			lebahDb = new Db();
			conn = lebahDb.getConnection();
			conn.setAutoCommit(false);
			Statement stmt = lebahDb.getStatement();			
			
			//SET ALL FLAG PERJANJIAN SEMASA AS TIDAK AKTIF
			sql = "UPDATE rk_perjanjian SET flag_perjanjian_semasa = 'T' WHERE flag_aktif = 'Y'"
					+ " AND id_fail = '" + fail.getId() + "'";
			stmt.executeUpdate(sql);
			
			//SEARCH PERJANJIAN YANG MASIH AKTIF PADA SYSDATE
			sql = "SELECT id FROM rk_perjanjian WHERE NOW() BETWEEN tarikh_mula AND tarikh_tamat AND flag_aktif = 'Y'"
					+ " AND id_jenis_perjanjian IN ('1', '2') AND id_fail = '" + fail.getId() + "'";
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				idPerjanjian = rs.getString("id");
			} else {
				//SEARCH PERJANJIAN TERAKHIR YANG TELAH TAMAT PADA SYSDATE
				sql = "SELECT * FROM rk_perjanjian WHERE tarikh_tamat < NOW() AND flag_aktif = 'Y' AND id_jenis_perjanjian IN ('1', '2')"
						+ " AND id_fail = '" + fail.getId() + "' ORDER BY tarikh_tamat DESC";
				ResultSet rsPerjanjianTamat = stmt.executeQuery(sql);
				if (rsPerjanjianTamat.next()) {
					idPerjanjian = rsPerjanjianTamat.getString("id");
				} else {
					//SEARCH PERJANJIAN YANG BELUM KUATKUASA PADA SYSDATE
					sql = "SELECT * FROM rk_perjanjian WHERE tarikh_mula > NOW() AND flag_aktif = 'Y' AND id_jenis_perjanjian IN ('1', '2')"
							+ " AND id_fail = '" + fail.getId() + "' ORDER BY tarikh_mula ASC";
					ResultSet rsPerjanjianBaru = stmt.executeQuery(sql);
					if (rsPerjanjianBaru.next()) {
						idPerjanjian = rsPerjanjianBaru.getString("id");
					}
				}
			}
			sql = "UPDATE rk_perjanjian SET flag_perjanjian_semasa = 'Y' WHERE id = '" + idPerjanjian + "'";
			stmt.executeUpdate(sql);
			conn.commit();			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (lebahDb != null) { lebahDb.close(); }
		}
 	}
	
	private static void updateStatusSewaRuang(RkFail fail, DbPersistence db) {
		try {
			RkRuangKomersil ruang = fail.getRuang();
			RkPerjanjian perjanjianSemasa = fail.getPerjanjianSemasa();			
			if (perjanjianSemasa != null) {
				if ("1".equals(perjanjianSemasa.getIdStatusPerjanjian())) {
					db.begin();
					ruang.setFlagSewa("Y");
					ruang.setFlagAktif("Y");
					db.commit();
				} else {
					db.begin();
					ruang.setFlagSewa("T");
					db.commit();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}	
	}
	
	private static void janaAkaunKeseluruhanFail(RkFail fail, DbPersistence db) {
		Db lebahDb = null;
		Connection conn = null;
			
		try {
			lebahDb = new Db();
			conn = lebahDb.getConnection();
			conn.setAutoCommit(false);			
			
			//DELETE ALL CAJ UNTUK FAIL
			List<RkAkaun> listAkaun = db.list("select x from RkAkaun x where x.fail.id = '" + fail.getId() + "' and x.kodHasil.id = '" + kodHasilSewa + "' and x.idJenisTransaksi = '1'");
			db.begin();
			for (RkAkaun akaun : listAkaun) {
				db.remove(akaun);
			}
			db.commit();			
			
			List<RkPerjanjian> listPerjanjian = db.list("SELECT x FROM RkPerjanjian x WHERE x.flagAktif = 'Y' AND x.idJenisPerjanjian in ('1', '2') AND x.fail.id = '" + fail.getId() + "'");
			for (RkPerjanjian perjanjian : listPerjanjian) {
				db.begin();
				if (perjanjian.getTarikhMula() != null && perjanjian.getTarikhTamat() != null) {
					generateAkaunForPerjanjian(perjanjian, db, lebahDb, conn);
				}				
				db.commit();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (lebahDb != null) { lebahDb.close(); }
		}
 	}
	
	private static void janaAkaunIWKKeseluruhanFail(RkFail fail, DbPersistence db) {
		Db lebahDb = null;
		Connection conn = null;
			
		try {
			lebahDb = new Db();
			conn = lebahDb.getConnection();
			conn.setAutoCommit(false);			
			
			//DELETE ALL CAJ IWK UNTUK FAIL
			List<RkAkaun> listAkaun = db.list("select x from RkAkaun x where x.fail.id = '" + fail.getId() + "' and x.kodHasil.id = '" + kodHasilIWK + "' and x.idJenisTransaksi = '1'");
			db.begin();
			for (RkAkaun akaun : listAkaun) {
				db.remove(akaun);
			}
			db.commit();			
			
			List<RkPerjanjian> listPerjanjian = db.list("SELECT x FROM RkPerjanjian x WHERE x.flagAktif = 'Y' AND x.idJenisPerjanjian in ('1', '2') AND x.flagCajIWK = 'Y' AND x.fail.id = '" + fail.getId() + "'");
			for (RkPerjanjian perjanjian : listPerjanjian) {
				db.begin();
				if (perjanjian.getTarikhMula() != null && perjanjian.getTarikhTamat() != null) {
					generateAkaunIWKForPerjanjian(perjanjian, db, lebahDb, conn);
				}
				db.commit();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (lebahDb != null) { lebahDb.close(); }
		}
 	}
	
	private static void generateAkaunForPerjanjian(RkPerjanjian perjanjian, DbPersistence db, Db lebahDb, Connection conn) {
		try {
			Statement stmt = lebahDb.getStatement();
			
			GregorianCalendar calCurrent = (GregorianCalendar) GregorianCalendar.getInstance();
			GregorianCalendar calMulaPerjanjian = (GregorianCalendar) GregorianCalendar.getInstance();
			GregorianCalendar calTamatPerjanjian = (GregorianCalendar) GregorianCalendar.getInstance();
			GregorianCalendar calInvois = (GregorianCalendar) GregorianCalendar.getInstance();
			GregorianCalendar calTransaksi = (GregorianCalendar) GregorianCalendar.getInstance();			

			calCurrent.setTime(new Date());
			calMulaPerjanjian.setTime(perjanjian.getTarikhMula());
			calTamatPerjanjian.setTime(perjanjian.getTarikhTamat());
			calInvois.setTime(calMulaPerjanjian.getTime());			

			String sql = "DELETE FROM rk_akaun WHERE tarikh_transaksi BETWEEN STR_TO_DATE('" + perjanjian.getTarikhMula() + "', '%d/%m/%Y')"
					+ " AND STR_TO_DATE('" + perjanjian.getTarikhTamat() + "', '%d/%m/%Y')"
					+ " AND id_fail = '" + perjanjian.getFail().getId() + "'"
					+ " AND id_kod_hasil = '" + kodHasilSewa + "' AND id_jenis_transaksi = '1'";
			stmt.executeUpdate(sql);
			conn.commit();
			
			while (calInvois.getTime().before(calTamatPerjanjian.getTime()) || calInvois.getTime().equals(calTamatPerjanjian.getTime())) {
				calTransaksi.setTime(calInvois.getTime());
				calTransaksi.add(Calendar.MONTH, -1);
				calTransaksi.set(Calendar.DATE, 20);
				
				if (calTransaksi.getTime().after(calMulaPerjanjian.getTime()) || calTransaksi.getTime().equals(calMulaPerjanjian.getTime())) {
					if (calTransaksi.getTime().before(calCurrent.getTime()) || calTransaksi.getTime().equals(calCurrent.getTime())) {
						RkAkaun akaun = new RkAkaun();
						akaun.setFail(perjanjian.getFail());
						akaun.setTarikhInvois(calInvois.getTime());
						akaun.setTarikhTransaksi(calTransaksi.getTime());
						akaun.setKodHasil((KodHasil) db.find(KodHasil.class, kodHasilSewa));
						akaun.setIdJenisTransaksi("1"); //CAJ
						akaun.setDebit(getKadarSewa(perjanjian, calInvois, lebahDb));
						akaun.setKeterangan(getKeteranganSewaBulanan(calInvois));
						akaun.setInvois(getInvoisBulanan(perjanjian, calInvois, db));
						db.persist(akaun);		
					}
				} else {
					if (calTransaksi.getTime().before(calCurrent.getTime()) || calTransaksi.getTime().equals(calCurrent.getTime())) {
						RkAkaun akaun = new RkAkaun();
						akaun.setFail(perjanjian.getFail());
						akaun.setTarikhInvois(calInvois.getTime());
						akaun.setTarikhTransaksi(calMulaPerjanjian.getTime());
						akaun.setKodHasil((KodHasil) db.find(KodHasil.class, kodHasilSewa));
						akaun.setIdJenisTransaksi("1"); //CAJ
						akaun.setDebit(getKadarSewa(perjanjian, calInvois, lebahDb));
						akaun.setKeterangan(getKeteranganSewaBulanan(calInvois));
						akaun.setInvois(getInvoisBulanan(perjanjian, calInvois, db));
						db.persist(akaun);	
					}						
				}
				
				if ("H".equals(perjanjian.getIdJenisSewa())) {
					calInvois.add(Calendar.DATE, 1);
				} else if ("B".equals(perjanjian.getIdJenisSewa())) {					
					//HUJUNG BULAN
					if (calMulaPerjanjian.get(Calendar.DATE) == 31) {
						calInvois.set(Calendar.DATE, 1);
						calInvois.add(Calendar.MONTH, 2);
						calInvois.add(Calendar.DATE, -1);
					} else if (calMulaPerjanjian.get(Calendar.DATE) == 30) {
						if (calInvois.get(Calendar.MONTH) == 0) {
							calInvois.set(Calendar.DATE, 1);
							calInvois.set(Calendar.MONTH, 2);
							calInvois.add(Calendar.DATE, -1);
						} else {
							calInvois.add(Calendar.MONTH, 1);
							calInvois.set(Calendar.DATE, 30);
						}
					} else if (calMulaPerjanjian.get(Calendar.DATE) == 29) {
						if (calInvois.get(Calendar.MONTH) == 0) {
							calInvois.set(Calendar.DATE, 1);
							calInvois.set(Calendar.MONTH, 2);
							calInvois.add(Calendar.DATE, -1);
						} else {
							calInvois.add(Calendar.MONTH, 1);
							calInvois.set(Calendar.DATE, 29);
						}
					} else {
						calInvois.add(Calendar.MONTH, 1);
						calInvois.set(Calendar.DATE, calMulaPerjanjian.get(Calendar.DATE));
					}
				} else if ("T".equals(perjanjian.getIdJenisSewa())) {
					calInvois.add(Calendar.YEAR, 1);
					if (calMulaPerjanjian.isLeapYear(calMulaPerjanjian.get(Calendar.YEAR)) && calMulaPerjanjian.get(Calendar.DATE) == 29 
							&& calMulaPerjanjian.get(Calendar.MONTH) == 1) {
						if (!calInvois.isLeapYear(calInvois.get(Calendar.YEAR))) {
							calInvois.set(Calendar.DATE, 28);
						}
					}
				} else {
					break;
				}
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private static void generateAkaunIWKForPerjanjian(RkPerjanjian perjanjian, DbPersistence db, Db lebahDb, Connection conn) {
		try {
			Statement stmt = lebahDb.getStatement();
			
			GregorianCalendar calCurrent = (GregorianCalendar) GregorianCalendar.getInstance();
			GregorianCalendar calMulaPerjanjian = (GregorianCalendar) GregorianCalendar.getInstance();
			GregorianCalendar calTamatPerjanjian = (GregorianCalendar) GregorianCalendar.getInstance();
			GregorianCalendar calInvois = (GregorianCalendar) GregorianCalendar.getInstance();
			GregorianCalendar calTransaksi = (GregorianCalendar) GregorianCalendar.getInstance();			

			calCurrent.setTime(new Date());
			calMulaPerjanjian.setTime(perjanjian.getTarikhMula());
			calTamatPerjanjian.setTime(perjanjian.getTarikhTamat());
			calInvois.setTime(calMulaPerjanjian.getTime());			
			
			String sql = "DELETE FROM rk_akaun WHERE tarikh_transaksi BETWEEN STR_TO_DATE('" + perjanjian.getTarikhMula() + "', '%d/%m/%Y')"
					+ " AND STR_TO_DATE('" + perjanjian.getTarikhTamat() + "', '%d/%m/%Y')"
					+ " AND id_fail = '" + perjanjian.getFail().getId() + "'"
					+ " AND id_kod_hasil = '" + kodHasilIWK + "' AND id_jenis_transaksi = '1'";
			stmt.executeUpdate(sql);
			conn.commit();
			
			while (calInvois.getTime().before(calTamatPerjanjian.getTime()) || calInvois.getTime().equals(calTamatPerjanjian.getTime())) {
				calTransaksi.setTime(calInvois.getTime());
				calTransaksi.add(Calendar.MONTH, -1);
				calTransaksi.set(Calendar.DATE, 20);
				
				if (calTransaksi.getTime().after(calMulaPerjanjian.getTime()) || calTransaksi.getTime().equals(calMulaPerjanjian.getTime())) {
					if (calTransaksi.getTime().before(calCurrent.getTime()) || calTransaksi.getTime().equals(calCurrent.getTime())) {
						RkAkaun akaun = new RkAkaun();
						akaun.setFail(perjanjian.getFail());
						akaun.setTarikhInvois(calInvois.getTime());
						akaun.setTarikhTransaksi(calTransaksi.getTime());
						akaun.setKodHasil((KodHasil) db.find(KodHasil.class, kodHasilIWK));
						akaun.setIdJenisTransaksi("1"); //CAJ
						akaun.setDebit(getKadarBayaranIWK(perjanjian, calInvois, lebahDb));
						akaun.setKeterangan(getKeteranganCajIWKBulanan(calInvois));
						akaun.setInvois(getInvoisIWKBulanan(perjanjian, calInvois, db));
						db.persist(akaun);		
					}
				} else {
					if (calTransaksi.getTime().before(calCurrent.getTime()) || calTransaksi.getTime().equals(calCurrent.getTime())) {
						RkAkaun akaun = new RkAkaun();
						akaun.setFail(perjanjian.getFail());
						akaun.setTarikhInvois(calInvois.getTime());
						akaun.setTarikhTransaksi(calMulaPerjanjian.getTime());
						akaun.setKodHasil((KodHasil) db.find(KodHasil.class, kodHasilIWK));
						akaun.setIdJenisTransaksi("1"); //CAJ
						akaun.setDebit(getKadarBayaranIWK(perjanjian, calInvois, lebahDb));
						akaun.setKeterangan(getKeteranganCajIWKBulanan(calInvois));
						akaun.setInvois(getInvoisIWKBulanan(perjanjian, calInvois, db));
						db.persist(akaun);
					}							
				}
				
				if ("H".equals(perjanjian.getIdJenisSewa())) {
					calInvois.add(Calendar.DATE, 1);
				} else if ("B".equals(perjanjian.getIdJenisSewa())) {					
					//HUJUNG BULAN
					if (calMulaPerjanjian.get(Calendar.DATE) == 31) {
						calInvois.set(Calendar.DATE, 1);
						calInvois.add(Calendar.MONTH, 2);
						calInvois.add(Calendar.DATE, -1);
					} else if (calMulaPerjanjian.get(Calendar.DATE) == 30) {
						if (calInvois.get(Calendar.MONTH) == 0) {
							calInvois.set(Calendar.DATE, 1);
							calInvois.set(Calendar.MONTH, 2);
							calInvois.add(Calendar.DATE, -1);
						} else {
							calInvois.add(Calendar.MONTH, 1);
							calInvois.set(Calendar.DATE, 30);
						}
					} else if (calMulaPerjanjian.get(Calendar.DATE) == 29) {
						if (calInvois.get(Calendar.MONTH) == 0) {
							calInvois.set(Calendar.DATE, 1);
							calInvois.set(Calendar.MONTH, 2);
							calInvois.add(Calendar.DATE, -1);
						} else {
							calInvois.add(Calendar.MONTH, 1);
							calInvois.set(Calendar.DATE, 29);
						}
					} else {
						calInvois.add(Calendar.MONTH, 1);
						calInvois.set(Calendar.DATE, calMulaPerjanjian.get(Calendar.DATE));
					}
				} else if ("T".equals(perjanjian.getIdJenisSewa())) {
					calInvois.add(Calendar.YEAR, 1);
					if (calMulaPerjanjian.isLeapYear(calMulaPerjanjian.get(Calendar.YEAR)) && calMulaPerjanjian.get(Calendar.DATE) == 29 
							&& calMulaPerjanjian.get(Calendar.MONTH) == 1) {
						if (!calInvois.isLeapYear(calInvois.get(Calendar.YEAR))) {
							calInvois.set(Calendar.DATE, 28);
						}
					}
				} else {
					break;
				}
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private static void updateStatusTunggakanSewa(RkFail fail, DbPersistence db) {
		double kadarSewaSemasa = 0D;
		double baki = 0D;
		int abt = 0;
		boolean invoisSemasaTertunggak = false;
		
		Db lebahDb = null;
		try {
			lebahDb = new Db();
			Statement stmt = lebahDb.getStatement();
			
			db.begin();
			GregorianCalendar calCurrent = (GregorianCalendar) GregorianCalendar.getInstance();
			calCurrent.setTime(new Date());
			GregorianCalendar calTarikhInvois = (GregorianCalendar) GregorianCalendar.getInstance();
			GregorianCalendar calTarikhAkhirInvois = (GregorianCalendar) GregorianCalendar.getInstance();
			
			RkAkaun akaun = (RkAkaun) db.get("select x from RkAkaun x where x.idJenisTransaksi = '1' and x.kodHasil.id = '" + kodHasilSewa + "' and x.fail.id = '" + fail.getId() + "' order by x.tarikhInvois desc");
			if (akaun != null) {
				kadarSewaSemasa = akaun.getDebit();			
				calTarikhInvois.setTime(akaun.getTarikhTransaksi());
				calTarikhAkhirInvois.setTime(calTarikhInvois.getTime());
				calTarikhAkhirInvois.add(Calendar.MONTH, 1);
				calTarikhAkhirInvois.set(Calendar.DATE, 10);
			}
			
			if (calCurrent.after(calTarikhAkhirInvois)) {
				invoisSemasaTertunggak = true;
			}
			
			//PERJANJIAN DAH TAMAT
			if (fail.getPerjanjianSemasa() != null) {
				if (fail.getPerjanjianSemasa().getIdStatusPerjanjian() != null) {
					if (!"1".equals(fail.getPerjanjianSemasa().getIdStatusPerjanjian())) {
						kadarSewaSemasa = 0D;
						invoisSemasaTertunggak = true;
					}
				}
			}					
			
			String sql = "select IFNULL(SUM(IFNULL(debit, 0)), 0) - IFNULL(SUM(IFNULL(kredit, 0)), 0) as baki from rk_akaun where flag_aktif = 'Y' and id_kod_hasil = '" + kodHasilSewa + "' and id_fail = '" + fail.getId() + "'";
			ResultSet rsBaki = stmt.executeQuery(sql);					
			if (rsBaki.next()) {
				baki = rsBaki.getDouble("baki");				
			}
			
			if (!invoisSemasaTertunggak) {
				baki = baki - kadarSewaSemasa;
			}
			if (baki > 0D) {
				sql = "select IFNULL(debit, 0) as debit from rk_akaun where flag_aktif = 'Y' and id_kod_hasil = '" + kodHasilSewa + "' and id_jenis_transaksi = '1' and id_fail = '" + fail.getId() + "' order by tarikh_transaksi desc";
				
				ResultSet rsABT = stmt.executeQuery(sql);
				double tunggakan = baki;
				boolean countABT = false;
				while (rsABT.next()) {
					if (invoisSemasaTertunggak) {
						countABT = true;
					}
					if (countABT) {
						if (tunggakan > 0D && rsABT.getDouble("debit") > 0) {
							abt++;
							tunggakan = tunggakan - rsABT.getDouble("debit");
						} else {
							break;
						}
					}
					countABT = true;
				}
				fail.setFlagTunggakan("Y");
				fail.setNilaiTunggakan(baki);	
				fail.setAbt(abt);
			} else {
				fail.setFlagTunggakan("T");
				fail.setNilaiTunggakan(baki);
				fail.setAbt(abt);
			}
			db.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (lebahDb != null) { lebahDb.close(); }
		}	
	}
	
	private static void updateStatusTunggakanIWK(RkFail fail, DbPersistence db) {
		double kadarBayaranIWKSemasa = 0D;
		double baki = 0D;
		int abt = 0;
		boolean invoisSemasaTertunggak = false;
		
		Db lebahDb = null;
		try {
			lebahDb = new Db();
			Statement stmt = lebahDb.getStatement();
			
			db.begin();
			GregorianCalendar calCurrent = (GregorianCalendar) GregorianCalendar.getInstance();
			calCurrent.setTime(new Date());
			GregorianCalendar calTarikhInvois = (GregorianCalendar) GregorianCalendar.getInstance();
			GregorianCalendar calTarikhAkhirInvois = (GregorianCalendar) GregorianCalendar.getInstance();
			
			RkAkaun akaun = (RkAkaun) db.get("select x from RkAkaun x where x.idJenisTransaksi = '1' and x.kodHasil.id = '" + kodHasilIWK + "' and x.fail.id = '" + fail.getId() + "' order by x.tarikhInvois desc");
			if (akaun != null) {
				kadarBayaranIWKSemasa = akaun.getDebit();			
				calTarikhInvois.setTime(akaun.getTarikhTransaksi());
				calTarikhAkhirInvois.setTime(calTarikhInvois.getTime());
				calTarikhAkhirInvois.add(Calendar.MONTH, 1);
				calTarikhAkhirInvois.set(Calendar.DATE, 10);
			}
			
			if (calCurrent.after(calTarikhAkhirInvois)) {
				invoisSemasaTertunggak = true;
			}
			
			//PERJANJIAN DAH TAMAT
			if (fail.getPerjanjianSemasa() != null) {
				if (fail.getPerjanjianSemasa().getIdStatusPerjanjian() != null) {
					if (!"1".equals(fail.getPerjanjianSemasa().getIdStatusPerjanjian())) {
						kadarBayaranIWKSemasa = 0D;
						invoisSemasaTertunggak = true;
					}
				}
			}
			
			String sql = "select IFNULL(SUM(IFNULL(debit, 0)), 0) - IFNULL(SUM(IFNULL(kredit, 0)), 0) as baki from rk_akaun where flag_aktif = 'Y' and id_kod_hasil = '" + kodHasilIWK + "' and id_fail = '" + fail.getId() + "'";
			ResultSet rsBaki = stmt.executeQuery(sql);					
			if (rsBaki.next()) {
				baki = rsBaki.getDouble("baki");	
			}
			
			if (!invoisSemasaTertunggak) {
				baki = baki - kadarBayaranIWKSemasa;
			}
			if (baki > 0D) {
				sql = "select IFNULL(debit, 0) as debit from rk_akaun where flag_aktif = 'Y' and id_kod_hasil = '" + kodHasilIWK + "' and id_jenis_transaksi = '1' and id_fail = '" + fail.getId() + "' order by tarikh_transaksi desc";
				
				ResultSet rsABT = stmt.executeQuery(sql);
				double tunggakan = baki;
				boolean countABT = false;
				while (rsABT.next()) {
					if (invoisSemasaTertunggak) {
						countABT = true;
					}
					if (countABT) {
						if (tunggakan > 0D && rsABT.getDouble("debit") > 0) {
							abt++;
							tunggakan = tunggakan - rsABT.getDouble("debit");
						} else {
							break;
						}
					}					
				}
				fail.setFlagTunggakanIWK("Y");
				fail.setNilaiTunggakanIWK(baki);	
				fail.setAbtIWK(abt);
			} else {
				fail.setFlagTunggakanIWK("T");
				fail.setNilaiTunggakanIWK(baki);
				fail.setAbtIWK(abt);
			}
			db.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (lebahDb != null) { lebahDb.close(); }
		}
	}
	
	private static RkInvois getInvoisBulanan(RkPerjanjian perjanjian, GregorianCalendar calInvois, DbPersistence db) {
		RkInvois invois = null;
		try {
			String noInvois = perjanjian.getFail().getRuang().getIdRuang() + "-" + new DecimalFormat("00").format(calInvois.get(Calendar.MONTH) + 1) + new DecimalFormat("0000").format(calInvois.get(Calendar.YEAR));
			invois = (RkInvois) db.get("select x from RkInvois x where x.noInvois = '" + noInvois + "' and x.fail.id = '" + perjanjian.getFail().getId() + "' order by x.tarikhMasuk asc");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return invois;
	}
	
	private static RkInvois getInvoisIWKBulanan(RkPerjanjian perjanjian, GregorianCalendar calInvois, DbPersistence db) {
		RkInvois invois = null;
		try {
			String noInvois = perjanjian.getFail().getRuang().getIdRuang() + "-" + new DecimalFormat("00").format(calInvois.get(Calendar.MONTH) + 1) + new DecimalFormat("0000").format(calInvois.get(Calendar.YEAR)) + " (IWK)";
			invois = (RkInvois) db.get("select x from RkInvois x where x.noInvois = '" + noInvois + "' and x.fail.id = '" + perjanjian.getFail().getId() + "' order by x.tarikhMasuk asc");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return invois;
	}
	
	private static double getKadarSewa(RkPerjanjian perjanjian, GregorianCalendar calInvois, Db lebahDb) {
		double kadarSewa = 0D;
		try {

			Statement stmt = lebahDb.getStatement();			
			String sql = "SELECT kadar_sewa FROM rk_perjanjian WHERE flag_aktif = 'Y' AND id_jenis_perjanjian IN ('3', '4')"
					+ " AND STR_TO_DATE('" + sdf.format(calInvois.getTime()) + "', '%d/%m/%Y') BETWEEN tarikh_mula AND tarikh_tamat"
					+ " AND id_fail = '" + perjanjian.getFail().getId() + "' ORDER BY tarikh_mula DESC";

			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				kadarSewa = rs.getDouble("kadar_sewa");
			} else {
				kadarSewa = perjanjian.getKadarSewa();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return kadarSewa;
	}
	
	private static double getKadarBayaranIWK(RkPerjanjian perjanjian, GregorianCalendar calInvois, Db lebahDb) {
		double kadarBayaranIWK = 0D;
		try {

			Statement stmt = lebahDb.getStatement();			
			String sql = "SELECT kadar_bayaran_iwk FROM rk_perjanjian WHERE flag_aktif = 'Y' AND id_jenis_perjanjian IN ('5', '6')"
					+ " AND STR_TO_DATE('" + sdf.format(calInvois.getTime()) + "', '%d/%m/%Y') BETWEEN tarikh_mula AND tarikh_tamat"
					+ " AND id_fail = '" + perjanjian.getFail().getId() + "' ORDER BY tarikh_mula DESC";

			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				kadarBayaranIWK = rs.getDouble("kadar_bayaran_iwk");
			} else {
				kadarBayaranIWK = perjanjian.getKadarBayaranIWK();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return kadarBayaranIWK;
	}
	
	private static String getKeteranganSewaBulanan(GregorianCalendar calMula) {
		String keterangan = "SEWA BAGI BULAN";
		
		if (calMula.get(Calendar.MONTH) == 0) {
			keterangan = keterangan + " JANUARI";
		} else if (calMula.get(Calendar.MONTH) == 1) {
			keterangan = keterangan + " FEBRUARI";
		} else if (calMula.get(Calendar.MONTH) == 2) {
			keterangan = keterangan + " MAC";
		} else if (calMula.get(Calendar.MONTH) == 3) {
			keterangan = keterangan + " APRIL";
		} else if (calMula.get(Calendar.MONTH) == 4) {
			keterangan = keterangan + " MEI";
		} else if (calMula.get(Calendar.MONTH) == 5) {
			keterangan = keterangan + " JUN";
		} else if (calMula.get(Calendar.MONTH) == 6) {
			keterangan = keterangan + " JULAI";
		} else if (calMula.get(Calendar.MONTH) == 7) {
			keterangan = keterangan + " OGOS";
		} else if (calMula.get(Calendar.MONTH) == 8) {
			keterangan = keterangan + " SEPTEMBER";
		} else if (calMula.get(Calendar.MONTH) == 9) {
			keterangan = keterangan + " OKTOBER";
		} else if (calMula.get(Calendar.MONTH) == 10) {
			keterangan = keterangan + " NOVEMBER";
		} else if (calMula.get(Calendar.MONTH) == 11) {
			keterangan = keterangan + " DISEMBER";
		}
		keterangan = keterangan + " " + calMula.get(Calendar.YEAR);
		return keterangan;
	}
	
	private static String getKeteranganCajIWKBulanan(GregorianCalendar calMula) {
		String keterangan = "CAJ IWK BAGI BULAN";
		
		if (calMula.get(Calendar.MONTH) == 0) {
			keterangan = keterangan + " JANUARI";
		} else if (calMula.get(Calendar.MONTH) == 1) {
			keterangan = keterangan + " FEBRUARI";
		} else if (calMula.get(Calendar.MONTH) == 2) {
			keterangan = keterangan + " MAC";
		} else if (calMula.get(Calendar.MONTH) == 3) {
			keterangan = keterangan + " APRIL";
		} else if (calMula.get(Calendar.MONTH) == 4) {
			keterangan = keterangan + " MEI";
		} else if (calMula.get(Calendar.MONTH) == 5) {
			keterangan = keterangan + " JUN";
		} else if (calMula.get(Calendar.MONTH) == 6) {
			keterangan = keterangan + " JULAI";
		} else if (calMula.get(Calendar.MONTH) == 7) {
			keterangan = keterangan + " OGOS";
		} else if (calMula.get(Calendar.MONTH) == 8) {
			keterangan = keterangan + " SEPTEMBER";
		} else if (calMula.get(Calendar.MONTH) == 9) {
			keterangan = keterangan + " OKTOBER";
		} else if (calMula.get(Calendar.MONTH) == 10) {
			keterangan = keterangan + " NOVEMBER";
		} else if (calMula.get(Calendar.MONTH) == 11) {
			keterangan = keterangan + " DISEMBER";
		}
		keterangan = keterangan + " " + calMula.get(Calendar.YEAR);
		return keterangan;
	}
	
	private static String getKeteranganInvoisBulanan(RkFail fail) {
		String keteranganBayaranInvois = "";
		if (fail.getRuang() != null) {
			if (fail.getRuang().getNamaRuang() != null) {
				keteranganBayaranInvois = keteranganBayaranInvois + " " + fail.getRuang().getNamaRuang();
			}
			keteranganBayaranInvois = keteranganBayaranInvois + " DI ";
			if (fail.getRuang().getLokasi() != null && !"".equals(fail.getRuang().getLokasi())) {
				keteranganBayaranInvois = keteranganBayaranInvois + fail.getRuang().getLokasi();
			}
			if (fail.getRuang().getAlamat1() != null && !"".equals(fail.getRuang().getAlamat1())) {
				keteranganBayaranInvois = keteranganBayaranInvois + ", " + Util.RemoveComma(fail.getRuang().getAlamat1());
			}
			if (fail.getRuang().getAlamat2() != null && !"".equals(fail.getRuang().getAlamat2())) {
				keteranganBayaranInvois = keteranganBayaranInvois + ", " + Util.RemoveComma(fail.getRuang().getAlamat2());
			}
			if (fail.getRuang().getAlamat3() != null && !"".equals(fail.getRuang().getAlamat3())) {
				keteranganBayaranInvois = keteranganBayaranInvois + ", " + Util.RemoveComma(fail.getRuang().getAlamat3());
			}
			if (fail.getRuang().getBandar() != null && !"".equals(fail.getRuang().getBandar())) {
				keteranganBayaranInvois = keteranganBayaranInvois + ", " + fail.getRuang().getBandar().getKeterangan();
				if (fail.getRuang().getBandar().getNegeri() != null && !"".equals(fail.getRuang().getBandar().getNegeri())) {
					keteranganBayaranInvois = keteranganBayaranInvois + " " + fail.getRuang().getBandar().getNegeri().getKeterangan();
				}
			}				
		}
		return keteranganBayaranInvois;
	}
	
	private static void generateInvoisBulanan(RkFail fail, boolean regenerateInvois, DbPersistence db) {
		Db lebahDb = null;
		double kadarSewaBulanan = 0D;
		double tunggakan = 0D;
		int abt = 0;
		
		double kadarIWKBulanan = 0D;
		double tunggakanIWK = 0D;
		int abtIWK = 0;
		
		try {
			lebahDb = new Db();
			Statement stmt = lebahDb.getStatement();
			
			GregorianCalendar calCurrent = (GregorianCalendar) GregorianCalendar.getInstance();
			calCurrent.setTime(new Date());

			GregorianCalendar calInvoisAkaunSewa = (GregorianCalendar) GregorianCalendar.getInstance();
			GregorianCalendar calInvoisBulanSemasa = (GregorianCalendar) GregorianCalendar.getInstance();
			GregorianCalendar calTarikhAkhirInvois = (GregorianCalendar) GregorianCalendar.getInstance();
			
			if (fail.getFlagAktifPerjanjian().equals("Y")) {
				//PERJANJIAN SEMASA
				RkPerjanjian perjanjianSemasa = (RkPerjanjian) db.get("select x from RkPerjanjian x where x.flagAktif = 'Y'"
						+ " and x.flagPerjanjianSemasa = 'Y' and x.fail.id = '" + fail.getId() + "'");
				
				if (perjanjianSemasa != null) {	
					GregorianCalendar calMulaPerjanjian = (GregorianCalendar) GregorianCalendar.getInstance();
					calMulaPerjanjian.setTime(perjanjianSemasa.getTarikhMula());
					calInvoisBulanSemasa.setTime(perjanjianSemasa.getTarikhMula());

					//INVOIS SEWA
					RkAkaun akaunSewa = (RkAkaun) db.get("select x from RkAkaun x where x.idJenisTransaksi = '1' and x.kodHasil.id = '" + kodHasilSewa + "' and x.fail.id = '" + fail.getId() + "' order by x.tarikhInvois desc");
					if (akaunSewa != null) {								
						kadarSewaBulanan = akaunSewa.getDebit();	
						tunggakan = fail.getNilaiTunggakan();
						abt = fail.getAbt();
						
						calInvoisAkaunSewa.setTime(akaunSewa.getTarikhInvois());
						
						calInvoisBulanSemasa.set(Calendar.DATE, 1);
						calInvoisBulanSemasa.set(Calendar.MONTH, calCurrent.get(Calendar.MONTH));
						calInvoisBulanSemasa.set(Calendar.YEAR, calCurrent.get(Calendar.YEAR));
						if (calCurrent.get(Calendar.DATE) >= 20) {
							calInvoisBulanSemasa.add(Calendar.MONTH, 1);
							calTarikhAkhirInvois.add(Calendar.MONTH, 1);
						}
						
						//HUJUNG BULAN
						if (calMulaPerjanjian.get(Calendar.DATE) == 31) {
							calInvoisBulanSemasa.add(Calendar.MONTH, 1);
							calInvoisBulanSemasa.add(Calendar.DATE, -1);
						} else if (calMulaPerjanjian.get(Calendar.DATE) == 30) {
							if (calInvoisBulanSemasa.get(Calendar.MONTH) == 1) {
								calInvoisBulanSemasa.set(Calendar.MONTH, 1);
								calInvoisBulanSemasa.add(Calendar.DATE, -1);
							} else {
								calInvoisBulanSemasa.set(Calendar.DATE, 30);
							}
						} else if (calMulaPerjanjian.get(Calendar.DATE) == 29) {
							if (calInvoisBulanSemasa.get(Calendar.MONTH) == 1) {
								calInvoisBulanSemasa.set(Calendar.MONTH, 1);
								calInvoisBulanSemasa.add(Calendar.DATE, -1);
							} else {
								calInvoisBulanSemasa.set(Calendar.DATE, 29);
							}
						} else {
							calInvoisBulanSemasa.set(Calendar.DATE, calMulaPerjanjian.get(Calendar.DATE));
						}
						
						calTarikhAkhirInvois.setTime(calInvoisBulanSemasa.getTime());
						calTarikhAkhirInvois.set(Calendar.DATE, 10);
					}
					
					//INVOIS IWK
					RkAkaun akaunIWK = (RkAkaun) db.get("select x from RkAkaun x where x.idJenisTransaksi = '1' and x.kodHasil.id = '" + kodHasilIWK + "' and x.fail.id = '" + fail.getId() + "' order by x.tarikhInvois desc");
					if (akaunIWK != null) {										
						kadarIWKBulanan = akaunIWK.getDebit();
						tunggakanIWK = fail.getNilaiTunggakanIWK();
						abtIWK = fail.getAbtIWK();
					}
					
					if ("1".equals(perjanjianSemasa.getIdStatusPerjanjian())) { 
						// PERJANJIAN MASIH AKTIF
						
						if (calInvoisBulanSemasa.getTime().after(calInvoisAkaunSewa.getTime())) {
							//PERJANJIAN MASIH AKTIF TETAPI AKAN TAMAT
							//SEMAK AKAUN SEWA ADA BAKI TUNGGAKAN ATAU TIDAK
							if (fail.getFlagTunggakan().equals("Y")) {
								kadarSewaBulanan = 0D;
								tunggakan = fail.getNilaiTunggakan();
								abt = fail.getAbt();
								createInvoisRK(fail, perjanjianSemasa, kadarSewaBulanan, tunggakan, abt, calInvoisBulanSemasa, calTarikhAkhirInvois, regenerateInvois, db, stmt);
							} else {
								removeKewInvois(fail, kodHasilSewa, kodJenisBayaranSewa, db);
							}
							
							//SEMAK AKAUN IWK ADA BAKI TUNGGAKAN ATAU TIDAK
							if (fail.getFlagTunggakanIWK().equals("Y")) {
								kadarIWKBulanan = 0D;
								tunggakanIWK = fail.getNilaiTunggakanIWK();
								abtIWK = fail.getAbtIWK();
								createInvoisIWKRK(fail, perjanjianSemasa, kadarIWKBulanan, tunggakanIWK, abtIWK, calInvoisBulanSemasa, calTarikhAkhirInvois, regenerateInvois, db, stmt);
							} else {
								removeKewInvois(fail, kodHasilIWK, kodJenisBayaranIWK, db);
							}
						} else {
							if (calCurrent.after(calTarikhAkhirInvois)) {
								tunggakan = fail.getNilaiTunggakan() - kadarSewaBulanan;
								abt = fail.getAbt() - 1;
								if (abt < 0)
									abt = 0;
								
								tunggakanIWK = fail.getNilaiTunggakanIWK() - kadarIWKBulanan;
								abtIWK = fail.getAbtIWK() - 1;
								if (abtIWK < 0)
									abtIWK = 0;
							}
							
							createInvoisRK(fail, perjanjianSemasa, kadarSewaBulanan, tunggakan, abt, calInvoisBulanSemasa, calTarikhAkhirInvois, regenerateInvois, db, stmt);
							if (perjanjianSemasa.getFlagCajIWK().equals("Y")) {
								createInvoisIWKRK(fail, perjanjianSemasa, kadarIWKBulanan, tunggakanIWK, abtIWK, calInvoisBulanSemasa, calTarikhAkhirInvois, regenerateInvois, db, stmt);
							} else {
								removeKewInvois(fail, kodHasilIWK, kodJenisBayaranIWK, db);
							}
						}						
					} else {
						// PERJANJIAN TELAH TAMAT						
						//SEMAK AKAUN SEWA ADA BAKI TUNGGAKAN ATAU TIDAK
						if (fail.getFlagTunggakan().equals("Y")) {
							kadarSewaBulanan = 0D;
							tunggakan = fail.getNilaiTunggakan();
							abt = fail.getAbt();
							createInvoisRK(fail, perjanjianSemasa, kadarSewaBulanan, tunggakan, abt, calInvoisBulanSemasa, calTarikhAkhirInvois, regenerateInvois, db, stmt);
						} else {
							removeKewInvois(fail, kodHasilSewa, kodJenisBayaranSewa, db);
						}
						
						//SEMAK AKAUN IWK ADA BAKI TUNGGAKAN ATAU TIDAK
						if (fail.getFlagTunggakanIWK().equals("Y")) {
							kadarIWKBulanan = 0D;
							tunggakanIWK = fail.getNilaiTunggakanIWK();
							abtIWK = fail.getAbtIWK();
							createInvoisIWKRK(fail, perjanjianSemasa, kadarIWKBulanan, tunggakanIWK, abtIWK, calInvoisBulanSemasa, calTarikhAkhirInvois, regenerateInvois, db, stmt);
						} else {
							removeKewInvois(fail, kodHasilIWK, kodJenisBayaranIWK, db);
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (lebahDb != null) { lebahDb.close(); }
		}
	}	
	
	private static void createInvoisRK(RkFail fail,
			RkPerjanjian perjanjianSemasa, double kadarSewaBulanan,
			double tunggakan, int abt, GregorianCalendar calInvoisBulanSemasa,
			GregorianCalendar calTarikhAkhirInvois, boolean regenerateInvois, DbPersistence db, Statement stmt) {
		
		RkInvois invoisRk = null;
		RkInvois newInvoisRk = null;
		try {			
			
			String noInvois = "";
			if (fail != null) {
				db.begin();
				
				if (fail.getRuang() != null) {
					noInvois = fail.getRuang().getIdRuang() + "-" + new DecimalFormat("00").format(calInvoisBulanSemasa.get(Calendar.MONTH) + 1) + new DecimalFormat("0000").format(calInvoisBulanSemasa.get(Calendar.YEAR));
				}
				
				invoisRk = (RkInvois) db.get("select x from RkInvois x where x.flagAktif = 'Y' and x.kodHasil.id = '" + kodHasilSewa + "' and x.noInvois = '" + noInvois + "' and x.fail.id = '" + fail.getId() + "'");
				
				//INVOIS RK
				if (invoisRk == null) {
					invoisRk = new RkInvois();
					invoisRk.setFail(fail);
					invoisRk.setTarikhInvois(calInvoisBulanSemasa.getTime());
					invoisRk.setTarikhMula(new Date());
					invoisRk.setTarikhAkhir(calTarikhAkhirInvois.getTime());
					invoisRk.setKodHasil((KodHasil) db.find(KodHasil.class, kodHasilSewa));
					invoisRk.setNoInvois(noInvois);					
					String keteranganInvoisBulanan = "BAYARAN SEWA BAGI RUANG" + getKeteranganInvoisBulanan(fail);
					invoisRk.setKeterangan(keteranganInvoisBulanan);					
					invoisRk.setAmaunSemasa(kadarSewaBulanan);
					invoisRk.setAmaunTunggakan(tunggakan);
					invoisRk.setAbt(abt);							
					db.persist(invoisRk);
					
					regenerateInvois = false;
				} else {
					if (regenerateInvois) {
						List<RkInvois> listInvois = db.list("select x from RkInvois x where x.flagAktif = 'Y' and x.kodHasil.id = '" + kodHasilSewa + "' and x.noInvois = '" + noInvois + "' and x.fail.id = '" + fail.getId() + "'");
						for (RkInvois invois : listInvois) {
							invois.setFlagAktif("T");
						}
								
						//GENERATE NEW INVOIS
						newInvoisRk = new RkInvois();
						newInvoisRk.setFail(fail);
						newInvoisRk.setTarikhInvois(calInvoisBulanSemasa.getTime());
						newInvoisRk.setTarikhMula(new Date());
						newInvoisRk.setTarikhAkhir(calTarikhAkhirInvois.getTime());
						newInvoisRk.setKodHasil((KodHasil) db.find(KodHasil.class, kodHasilSewa));
						newInvoisRk.setNoInvois(noInvois);
						String keteranganInvoisBulanan = "BAYARAN SEWA BAGI RUANG" + getKeteranganInvoisBulanan(fail);
						newInvoisRk.setKeterangan(keteranganInvoisBulanan);		
						newInvoisRk.setAmaunSemasa(kadarSewaBulanan);
						newInvoisRk.setAmaunTunggakan(tunggakan);
						newInvoisRk.setAbt(abt);	
						db.persist(newInvoisRk);
					}
				}
				
				String sql = "SELECT id FROM rk_akaun WHERE flag_aktif = 'Y' AND id_kod_hasil = '" + kodHasilSewa + "'"
						+ " AND DATE_FORMAT(STR_TO_DATE('" + sdf.format(calInvoisBulanSemasa.getTime()) + "', '%d/%m/%Y'), '%d/%m/%Y') = DATE_FORMAT(tarikh_invois, '%d/%m/%Y')"
						+ " AND id_fail = '" + fail.getId() + "'";
				ResultSet rs = stmt.executeQuery(sql);	
				if (rs.next()) {
					RkAkaun akaun = (RkAkaun) db.find(RkAkaun.class, rs.getString("id"));
					if (akaun != null) {
						if (!regenerateInvois) {
							akaun.setInvois(invoisRk);
						}						
					}
				}				
				
				db.commit();
				
				if (regenerateInvois) {
					pushDataToKewInvois(newInvoisRk, kodHasilSewa, kodJenisBayaranSewa, db);
				} else {
					pushDataToKewInvois(invoisRk, kodHasilSewa, kodJenisBayaranSewa, db);
				}								
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}	 	
	}
	
	private static void createInvoisIWKRK(RkFail fail, RkPerjanjian perjanjianSemasa, double kadarIWKBulanan, double tunggakanIWK, int abtIWK, 
			GregorianCalendar calInvoisBulanSemasa, GregorianCalendar calTarikhAkhirInvois, boolean regenerateInvois, 
			DbPersistence db, Statement stmt) {
		
		RkInvois invoisRk = null;
		RkInvois newInvoisRk = null;
		try {			
			
			String noInvois = "";
			if (fail != null) {
				db.begin();
				
				if (fail.getRuang() != null) {
					noInvois = fail.getRuang().getIdRuang() + "-" + new DecimalFormat("00").format(calInvoisBulanSemasa.get(Calendar.MONTH) + 1) + new DecimalFormat("0000").format(calInvoisBulanSemasa.get(Calendar.YEAR)) + " (IWK)";
				}
				
				invoisRk = (RkInvois) db.get("select x from RkInvois x where x.flagAktif = 'Y' and x.kodHasil.id = '" + kodHasilIWK + "' and x.noInvois = '" + noInvois + "' and x.fail.id = '" + fail.getId() + "'");
				
				//INVOIS RK
				if (invoisRk == null) {
					invoisRk = new RkInvois();
					invoisRk.setFail(fail);
					invoisRk.setTarikhInvois(calInvoisBulanSemasa.getTime());
					invoisRk.setTarikhMula(new Date());
					invoisRk.setTarikhAkhir(calTarikhAkhirInvois.getTime());
					invoisRk.setKodHasil((KodHasil) db.find(KodHasil.class, kodHasilIWK));
					invoisRk.setNoInvois(noInvois);					
					String keteranganInvoisBulanan = "BAYARAN CAJ IWK BAGI RUANG" + getKeteranganInvoisBulanan(fail);
					invoisRk.setKeterangan(keteranganInvoisBulanan);					
					invoisRk.setAmaunSemasa(kadarIWKBulanan);
					invoisRk.setAmaunTunggakan(tunggakanIWK);
					invoisRk.setAbt(abtIWK);							
					db.persist(invoisRk);
					
					regenerateInvois = false;
				} else {
					if (regenerateInvois) {
						List<RkInvois> listInvois = db.list("select x from RkInvois x where x.flagAktif = 'Y' and x.kodHasil.id = '" + kodHasilIWK + "' and x.noInvois = '" + noInvois + "' and x.fail.id = '" + fail.getId() + "'");
						for (RkInvois invois : listInvois) {
							invois.setFlagAktif("T");
						}
						
						//GENERATE NEW INVOIS
						newInvoisRk = new RkInvois();
						newInvoisRk.setFail(fail);
						newInvoisRk.setTarikhInvois(calInvoisBulanSemasa.getTime());
						newInvoisRk.setTarikhMula(new Date());
						newInvoisRk.setTarikhAkhir(calTarikhAkhirInvois.getTime());
						newInvoisRk.setKodHasil((KodHasil) db.find(KodHasil.class, kodHasilIWK));
						newInvoisRk.setNoInvois(noInvois);
						String keteranganInvoisBulanan = "BAYARAN CAJ IWK BAGI RUANG" + getKeteranganInvoisBulanan(fail);
						newInvoisRk.setKeterangan(keteranganInvoisBulanan);					
						newInvoisRk.setAmaunSemasa(kadarIWKBulanan);
						newInvoisRk.setAmaunTunggakan(tunggakanIWK);
						newInvoisRk.setAbt(abtIWK);					
						db.persist(newInvoisRk);
					}
				}
				
				String sql = "SELECT id FROM rk_akaun WHERE flag_aktif = 'Y' AND id_kod_hasil = '" + kodHasilIWK + "'"
						+ " AND DATE_FORMAT(STR_TO_DATE('" + sdf.format(calInvoisBulanSemasa.getTime()) + "', '%d/%m/%Y'), '%d/%m/%Y') = DATE_FORMAT(tarikh_invois, '%d/%m/%Y')"
						+ " AND id_fail = '" + fail.getId() + "'";
				ResultSet rs = stmt.executeQuery(sql);	
				if (rs.next()) {
					RkAkaun akaun = (RkAkaun) db.find(RkAkaun.class, rs.getString("id"));
					if (akaun != null) {
						if (!regenerateInvois) {
							akaun.setInvois(invoisRk);
						}						
					}
				}				
				
				db.commit();
				
				if (regenerateInvois) {
					pushDataToKewInvois(newInvoisRk, kodHasilIWK, kodJenisBayaranIWK, db);
				} else {
					pushDataToKewInvois(invoisRk, kodHasilIWK, kodJenisBayaranIWK, db);
				}							
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}	 	
	}
	
	private static void pushDataToKewInvois(RkInvois invoisRk, String kodHasil, String kodJenisBayaran, DbPersistence db) {
		boolean addKewInvois = false;
		KewInvois invoisKewAktif = null;
		try {	
			db.begin();
			invoisKewAktif = (KewInvois) db.get("select x from KewInvois x where x.kodHasil.id = '" + kodHasil + "' and x.idLejar = '" + invoisRk.getFail().getId() + "' and x.flagBayar = 'T'");
			if (invoisKewAktif == null) {
				invoisKewAktif = new KewInvois();
				addKewInvois = true;				
			}
			invoisKewAktif.setKodHasil((KodHasil) db.find(KodHasil.class, kodHasil));
			invoisKewAktif.setFlagBayaran("SEWA");
			invoisKewAktif.setNoInvois(invoisRk.getNoInvois());	
			invoisKewAktif.setTarikhInvois(invoisRk.getTarikhMula());
			invoisKewAktif.setNoRujukan(invoisRk.getFail().getNoFail());
			invoisKewAktif.setIdLejar(invoisRk.getFail().getId());
			invoisKewAktif.setPembayar(getPembayar(invoisRk.getFail(), db));		
			invoisKewAktif.setJenisBayaran((KewJenisBayaran) db.find(KewJenisBayaran.class, kodJenisBayaran));				
			invoisKewAktif.setKeteranganBayaran(invoisRk.getKeterangan());			
			Double totalPerluBayar = getLatestTotalPerluBayar(invoisRk.getFail(), kodHasil);
			invoisKewAktif.setDebit(totalPerluBayar);
			invoisKewAktif.setKredit(0D);
			invoisKewAktif.setAmaunPelarasan(0D);
			invoisKewAktif.setFlagBayar("T");
			invoisKewAktif.setFlagQueue("T");
			invoisKewAktif.setTarikhDaftar(new Date());
			GregorianCalendar calInvois = (GregorianCalendar) GregorianCalendar.getInstance();
			calInvois.setTime(invoisRk.getTarikhInvois());
			if (kodHasil.equals(kodHasilSewa))
				invoisKewAktif.setCatatanInvois(getKeteranganSewaBulanan(calInvois));
			if (kodHasil.equals(kodHasilIWK))
				invoisKewAktif.setCatatanInvois(getKeteranganCajIWKBulanan(calInvois));
			
			if (addKewInvois) {
				db.persist(invoisKewAktif);
			}
			db.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}		
	}	
	
	private static Users getPembayar(RkFail fail, DbPersistence db) {
		Users pembayar = null;
		try {
			if (fail.getPemohon() != null) {
				if (fail.getPemohon().getSyarikat() != null) {
					pembayar = (Users) db.find(Users.class, fail.getPemohon().getSyarikat().getId());
				} else if (fail.getPemohon().getIndividu() != null) {
					pembayar = (Users) db.find(Users.class, fail.getPemohon().getIndividu().getId());
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return pembayar;
	}
	
	private static Double getLatestTotalPerluBayar(RkFail fail, String kodHasil) {
		double total = 0D;
		double totalDebit = 0D;
		double totalKredit = 0D;
		Db lebahDb = null;
		try {
			lebahDb = new Db();
			Statement stmt = lebahDb.getStatement();
			
			String sql = "select IFNULL(SUM(IFNULL(debit, 0)),0) as total from rk_akaun where id_kod_hasil = '" + kodHasil + "' and flag_aktif = 'Y' and id_fail = '" + fail.getId() + "'";
			ResultSet rsDebit = stmt.executeQuery(sql);					
			if (rsDebit.next()) {
				totalDebit = rsDebit.getDouble("total");				
			}
			
			sql = "select IFNULL(SUM(IFNULL(kredit, 0)),0) as total from rk_akaun where id_kod_hasil = '" + kodHasil + "' and flag_aktif = 'Y' and id_fail = '" + fail.getId() + "'";
			ResultSet rsKredit = stmt.executeQuery(sql);					
			if (rsKredit.next()) {
				totalKredit = rsKredit.getDouble("total");				
			}
			
			total = totalDebit - totalKredit;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (lebahDb != null) { lebahDb.close(); }
		}
		return total;
	}
	
	private static void removeKewInvois(RkFail fail, String kodHasil, String kodJenisBayaran, DbPersistence db) {
		try {
			KewInvois invois = (KewInvois) db.get("select x from KewInvois x where x.kodHasil.id = '" + kodHasil + "'"
					+ " and x.idLejar = '" + fail.getId() + "' and x.flagBayar = 'T' and x.jenisBayaran.id = '" + kodJenisBayaran + "'");
			if (invois != null) {
				db.begin();
				db.remove(invois);
				db.commit();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private static void migratePembayarLainKepadaPembayarRK() {
		Connection conn = null;
		String sql = "";
		try {
			db2 = new Db();
			conn = db2.getConnection();
			conn.setAutoCommit(false);
			Statement stm = db2.getStatement();
			db = new DbPersistence();
			db.begin();
			int i = 0;
			sql = "select distinct(id_resit) as id from rk_akaun where id_resit is not null";
			ResultSet rs = stm.executeQuery(sql);
			while (rs.next()) {
				KewBayaranResit resit = db.find(KewBayaranResit.class, rs.getString("id"));
				if (resit != null) {
					if (resit.getPembayar() == null) {
						RkAkaun akaun = (RkAkaun) db.get("select x from RkAkaun x where x.resit.id = '" + resit.getId() + "'");
						if (akaun != null) {
							Users pembayar = db.find(Users.class, akaun.getFail().getPemohon().getSyarikat().getId());
							if (pembayar != null) {
								resit.setPembayar(pembayar);
								
								List<KewResitSenaraiInvois> listRSI = db.list("select x from KewResitSenaraiInvois x where x.resit.id = '" + resit.getId() + "'");
								for (KewResitSenaraiInvois rsi : listRSI) {
									if (rsi.getInvois() != null) {
										KewInvois invois = rsi.getInvois();
										if (invois.getPembayar() == null) {										
											invois.setPembayar(pembayar);
											invois.setFlagJenisPembayarLain(null);
											invois.setPembayarLain(null);		
										}								
									}
								}
							}						
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
