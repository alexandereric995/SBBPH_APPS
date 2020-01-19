package bph.modules.rk;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.ServletContext;

import lebah.db.Db;
import lebah.entity.UserRole;
import portal.module.entity.CSS;
import portal.module.entity.Role;
import portal.module.entity.Users;
import bph.entities.kew.KewJenisBayaran;
import bph.entities.kewangan.KewDeposit;
import bph.entities.kewangan.KewInvois;
import bph.entities.kod.JenisPengenalan;
import bph.entities.kod.KategoriPengguna;
import bph.entities.kod.KodHasil;
import bph.entities.rk.RkAkaun;
import bph.entities.rk.RkFail;
import bph.entities.rk.RkIndividu;
import bph.entities.rk.RkInvois;
import bph.entities.rk.RkPerjanjian;
import bph.entities.rk.RkRuangKomersil;
import bph.entities.rk.RkSST;
import bph.entities.rk.RkSyarikat;
import bph.mail.mailer.RkMailer;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class UtilRk {
	
	private static String kodHasilDeposit = "79503"; // DEPOSIT PELBAGAI
	private static String kodHasilSewa = "74202"; // SEWA BANGUNAN PEJABAT LUAR WILAYAH	
	private static String kodHasilIWK = "81199"; // BAYARAN-BAYARAN BALIK YANG LAIN (UTILITI/IWK)
	
	private static String kodJenisBayaranDeposit = "04"; // DEPOSIT RUANG KOMERSIL - KEW_DEPOSIT
	private static String kodJenisBayaranSewa = "04"; // SEWA RUANG KOMERSIL - KEW_INVOIS
	private static String kodJenisBayaranIWK = "04"; // BAYARAN IWK RUANG KOMERSIL (PUKAL) - KEW_INVOIS
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	/* PUBLIC */
	public static void daftarUsersBagiPenyewaRK(RkFail fail, MyPersistence mp) {		
		try {	
			mp.begin();
			if (fail.getPemohon() != null) {
				if (fail.getPemohon().getIndividu() != null) {
					daftarPemohon(fail.getPemohon().getIndividu(), mp);
				}
				if (fail.getPemohon().getSyarikat() != null) {
					daftarSyarikat(fail.getPemohon().getSyarikat(), mp);
				}
				mp.commit();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void kemaskiniRekodPerjanjianDanAkaun(RkFail fail, boolean sendEmel, boolean regenerateInvois, MyPersistence mp, ServletContext context) {		
		try {		
			updateStatusPerjanjian(fail);			
			updateFlagAktifPerjanjianSemasa(fail);
			updateStatusSewaRuang(fail, mp);
			janaAkaunKeseluruhanFail(fail, mp);			
			updateStatusTunggakanSewa(fail, mp);			
			janaAkaunIWKKeseluruhanFail(fail, mp);
			updateStatusTunggakanIWK(fail, mp);		
			generateInvoisBulanan(fail, sendEmel, regenerateInvois, mp, context);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void generateInvoisBulanan(RkFail fail, boolean sendEmel, boolean regenerateInvois, MyPersistence mp, ServletContext context) {
		try {		
			janaInvoisBulanan(fail, sendEmel, regenerateInvois, mp, context);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void semakTarikhLuputPerjanjian(RkFail fail, MyPersistence mp) {		
		try {		
			updateStatusPerjanjian(fail);			
			updateFlagAktifPerjanjianSemasa(fail);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void updateStatusSewaRuangKomersil(MyPersistence mp) {
		try {
			mp.begin();			
			
			List<RkRuangKomersil> listRuang = mp.list("select x from RkRuangKomersil x order by x.id asc");
			for (RkRuangKomersil ruang : listRuang) {
				boolean statusSewa = false;
				List<RkFail> listFail = mp.list("select x from RkFail x where x.ruang.id = '" + ruang.getId() + "'");
				for (RkFail fail : listFail) {
					RkPerjanjian perjanjian = fail.getPerjanjianSemasa();
					if (perjanjian != null) {
						if (perjanjian.getIdStatusPerjanjian().equals("1")){
							statusSewa = true;
							break;
						}
					}
				}
				
				if (statusSewa) {
					ruang.setFlagSewa("Y");
				} else {
					ruang.setFlagSewa("T");
				}
			}
			
			mp.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void updateStatusTunggakanPerjanjian(RkFail fail, MyPersistence mp) {		
		try {		
			updateStatusTunggakanSewa(fail, mp);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/* PRIVATE */
	private static void daftarPemohon(RkIndividu individu, MyPersistence mp) {
		Users user = null;
		try {
			user = (Users) mp.find(Users.class, individu.getId());
			if (user == null) {
				user = new Users();
				user.setId(individu.getId());
				user.setUserName(individu.getNama());
				user.setRole((Role) mp.find(Role.class, "(AWAM) Pengguna Awam"));
				user.setCss((CSS) mp.find(CSS.class, "BPH-Anon"));
				user.setUserLoginAlt("-");
				user.setUserAddress(individu.getAlamat1());
				user.setUserAddress2(individu.getAlamat2());
				user.setUserAddress3(individu.getAlamat3());
				user.setUserPostcode(individu.getPoskod());
				user.setUserBandar(individu.getBandar());
				user.setDateRegistered(new Date());
				user.setJenisPengenalan((JenisPengenalan) mp.find(JenisPengenalan.class, "B"));
				user.setNoKP(individu.getId());
				user.setNoTelefon(individu.getNoTelefon());
				user.setNoTelefonBimbit(individu.getNoTelefonBimbit());
				user.setNoTelefonPejabat(individu.getNoTelefon());
				user.setNoFaks(individu.getNoFaks());
				user.setEmel(individu.getEmel());				
				user.setAlamat1(individu.getAlamat1());
				user.setAlamat2(individu.getAlamat2());
				user.setAlamat3(individu.getAlamat3());
				user.setPoskod(individu.getPoskod());
				user.setBandar(individu.getBandar());				
				
				user.setJenisPengguna((KategoriPengguna) mp.find(KategoriPengguna.class, "06"));
				user.setFlagSahMaklumatBank("T");
				user.setFlagAktif("T");
				user.setFlagSemakanJPN("T");
				user.setFlagSemakanHRMIS("T");
				user.setFlagSemakanPESARA("T");
				user.setFlagDaftarSBBPH("T");
				user.setFlagDaftarManual("T");
				user.setFlagMenungguPengesahan("T");
				mp.persist(user);
				
			} else {
				// USER TELAH BERDAFTAR DENGAN SBBPH
				if ("Y".equals(user.getFlagDaftarSBBPH())) {					
					if (!user.getRole().getName().equals("(AWAM) Pengguna Awam")) {
						UserRole userRole = (UserRole) mp.get("select x from UserRole x where x.userId = '" + individu.getId() + "' and x.roleId = '(AWAM) Pengguna Awam'");
						if (userRole == null) {
							userRole = new UserRole();
							userRole.setUserId(individu.getId());
							userRole.setRoleId("(AWAM) Pengguna Awam");
							mp.persist(userRole);		
						}
					}										
				} else {
					// USER TELAH WUJUD (MIGRATION) TETAPI TIDAK BERDAFTAR DENGAN SBBPH
					user.setId(individu.getId());
					user.setUserName(individu.getNama());
					user.setRole((Role) mp.find(Role.class, "(AWAM) Pengguna Awam"));
					user.setCss((CSS) mp.find(CSS.class, "BPH-Anon"));
					user.setUserLoginAlt("-");
					user.setUserAddress(individu.getAlamat1());
					user.setUserAddress2(individu.getAlamat2());
					user.setUserAddress3(individu.getAlamat3());
					user.setUserPostcode(individu.getPoskod());
					user.setUserBandar(individu.getBandar());
					user.setDateRegistered(new Date());
					user.setJenisPengenalan((JenisPengenalan) mp.find(JenisPengenalan.class, "B"));
					user.setNoKP(individu.getId());
					user.setNoTelefon(individu.getNoTelefon());
					user.setNoTelefonBimbit(individu.getNoTelefonBimbit());
					user.setNoTelefonPejabat(individu.getNoTelefon());
					user.setNoFaks(individu.getNoFaks());
					user.setEmel(individu.getEmel());				
					user.setAlamat1(individu.getAlamat1());
					user.setAlamat2(individu.getAlamat2());
					user.setAlamat3(individu.getAlamat3());
					user.setPoskod(individu.getPoskod());
					user.setBandar(individu.getBandar());				
					
					user.setJenisPengguna((KategoriPengguna) mp.find(KategoriPengguna.class, "06"));
					user.setFlagSahMaklumatBank("T");
					user.setFlagAktif("T");
					user.setFlagSemakanJPN("T");
					user.setFlagSemakanHRMIS("T");
					user.setFlagSemakanPESARA("T");
					user.setFlagDaftarSBBPH("T");
					user.setFlagDaftarManual("T");
					user.setFlagMenungguPengesahan("T");
				}				
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private static void daftarSyarikat(RkSyarikat syarikat, MyPersistence mp) {
		Users user = null;
		try {
			user = (Users) mp.find(Users.class, syarikat.getId());
			if (user == null) {
				user = new Users();
				user.setId(syarikat.getId());
				user.setUserName(syarikat.getNama());
				user.setRole((Role) mp.find(Role.class, "(AWAM) Pengguna Awam"));
				user.setCss((CSS) mp.find(CSS.class, "BPH-Anon"));
				user.setUserLoginAlt("-");
				user.setUserAddress(syarikat.getAlamat1());
				user.setUserAddress2(syarikat.getAlamat2());
				user.setUserAddress3(syarikat.getAlamat3());
				user.setUserPostcode(syarikat.getPoskod());
				user.setUserBandar(syarikat.getBandar());
				user.setDateRegistered(new Date());
				user.setJenisPengenalan((JenisPengenalan) mp.find(JenisPengenalan.class, "S"));
				user.setNoKP(syarikat.getId());			
				user.setAlamat1(syarikat.getAlamat1());
				user.setAlamat2(syarikat.getAlamat2());
				user.setAlamat3(syarikat.getAlamat3());
				user.setPoskod(syarikat.getPoskod());
				user.setBandar(syarikat.getBandar());				
				
				user.setJenisPengguna((KategoriPengguna) mp.find(KategoriPengguna.class, "06"));
				user.setFlagSahMaklumatBank("T");
				user.setFlagAktif("T");
				user.setFlagSemakanJPN("T");
				user.setFlagSemakanHRMIS("T");
				user.setFlagSemakanPESARA("T");
				user.setFlagDaftarSBBPH("T");
				user.setFlagDaftarManual("T");
				user.setFlagMenungguPengesahan("T");
				mp.persist(user);
				
			} else {
				// USER TELAH BERDAFTAR DENGAN SBBPH
				if ("Y".equals(user.getFlagDaftarSBBPH())) {					
					if (!user.getRole().getName().equals("(AWAM) Pengguna Awam")) {
						UserRole userRole = (UserRole) mp.get("select x from UserRole x where x.userId = '" + syarikat.getId() + "' and x.roleId = '(AWAM) Pengguna Awam'");
						if (userRole == null) {
							userRole = new UserRole();
							userRole.setUserId(syarikat.getId());
							userRole.setRoleId("(AWAM) Pengguna Awam");
							mp.persist(userRole);		
						}
					}									
				} else {
					// USER TELAH WUJUD (MIGRATION) TETAPI TIDAK BERDAFTAR DENGAN SBBPH
					user.setId(syarikat.getId());
					user.setUserName(syarikat.getNama());
					user.setRole((Role) mp.find(Role.class, "(AWAM) Pengguna Awam"));
					user.setCss((CSS) mp.find(CSS.class, "BPH-Anon"));
					user.setUserLoginAlt("-");
					user.setUserAddress(syarikat.getAlamat1());
					user.setUserAddress2(syarikat.getAlamat2());
					user.setUserAddress3(syarikat.getAlamat3());
					user.setUserPostcode(syarikat.getPoskod());
					user.setUserBandar(syarikat.getBandar());
					user.setDateRegistered(new Date());
					user.setJenisPengenalan((JenisPengenalan) mp.find(JenisPengenalan.class, "S"));
					user.setNoKP(syarikat.getId());			
					user.setAlamat1(syarikat.getAlamat1());
					user.setAlamat2(syarikat.getAlamat2());
					user.setAlamat3(syarikat.getAlamat3());
					user.setPoskod(syarikat.getPoskod());
					user.setBandar(syarikat.getBandar());				
					
					user.setJenisPengguna((KategoriPengguna) mp.find(KategoriPengguna.class, "06"));
					user.setFlagSahMaklumatBank("T");
					user.setFlagAktif("T");
					user.setFlagSemakanJPN("T");
					user.setFlagSemakanHRMIS("T");
					user.setFlagSemakanPESARA("T");
					user.setFlagDaftarSBBPH("T");
					user.setFlagDaftarManual("T");
					user.setFlagMenungguPengesahan("T");
				}				
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
	
	private static void updateStatusSewaRuang(RkFail fail, MyPersistence mp) {
		try {
			RkRuangKomersil ruang = fail.getRuang();
			RkPerjanjian perjanjianSemasa = fail.getPerjanjianSemasa();			
			if (perjanjianSemasa != null) {
				if ("1".equals(perjanjianSemasa.getIdStatusPerjanjian())) {
					mp.begin();
					ruang.setFlagSewa("Y");
					ruang.setFlagAktif("Y");
					mp.commit();
				} else {
					mp.begin();
					ruang.setFlagSewa("T");
					mp.commit();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}	
	}
	
	private static void janaAkaunKeseluruhanFail(RkFail fail, MyPersistence mp) {
		Db lebahDb = null;
		Connection conn = null;
			
		try {
			lebahDb = new Db();
			conn = lebahDb.getConnection();
			conn.setAutoCommit(false);			
			
			//DELETE ALL CAJ UNTUK FAIL
			List<RkAkaun> listAkaun = mp.list("select x from RkAkaun x where x.fail.id = '" + fail.getId() + "' and x.kodHasil.id = '" + kodHasilSewa + "' and x.idJenisTransaksi = '1'");
			mp.begin();
			for (RkAkaun akaun : listAkaun) {
				mp.remove(akaun);
			}
			mp.commit();			
			
			List<RkPerjanjian> listPerjanjian = mp.list("SELECT x FROM RkPerjanjian x WHERE x.flagAktif = 'Y' AND x.idJenisPerjanjian in ('1', '2') AND x.fail.id = '" + fail.getId() + "'");
			for (RkPerjanjian perjanjian : listPerjanjian) {
				mp.begin();
				if (perjanjian.getTarikhMula() != null && perjanjian.getTarikhTamat() != null) {
					generateAkaunForPerjanjian(perjanjian, mp, lebahDb, conn);
				}				
				mp.commit();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (lebahDb != null) { lebahDb.close(); }
		}
 	}
	
	private static void janaAkaunIWKKeseluruhanFail(RkFail fail, MyPersistence mp) {
		Db lebahDb = null;
		Connection conn = null;
			
		try {
			lebahDb = new Db();
			conn = lebahDb.getConnection();
			conn.setAutoCommit(false);			
			
			//DELETE ALL CAJ IWK UNTUK FAIL
			List<RkAkaun> listAkaun = mp.list("select x from RkAkaun x where x.fail.id = '" + fail.getId() + "' and x.kodHasil.id = '" + kodHasilIWK + "' and x.idJenisTransaksi = '1'");
			mp.begin();
			for (RkAkaun akaun : listAkaun) {
				mp.remove(akaun);
			}
			mp.commit();			
			
			List<RkPerjanjian> listPerjanjian = mp.list("SELECT x FROM RkPerjanjian x WHERE x.flagAktif = 'Y' AND x.idJenisPerjanjian in ('1', '2') AND x.flagCajIWK = 'Y' AND x.fail.id = '" + fail.getId() + "'");
			for (RkPerjanjian perjanjian : listPerjanjian) {
				mp.begin();
				if (perjanjian.getTarikhMula() != null && perjanjian.getTarikhTamat() != null) {
					generateAkaunIWKForPerjanjian(perjanjian, mp, lebahDb, conn);
				}
				mp.commit();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (lebahDb != null) { lebahDb.close(); }
		}
 	}

	private static void generateAkaunForPerjanjian(RkPerjanjian perjanjian, MyPersistence mp, Db lebahDb, Connection conn) {
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
						akaun.setKodHasil((KodHasil) mp.find(KodHasil.class, kodHasilSewa));
						akaun.setIdJenisTransaksi("1"); //CAJ
						akaun.setDebit(getKadarSewa(perjanjian, calInvois, lebahDb));
						akaun.setKeterangan(getKeteranganSewaBulanan(calInvois));
						akaun.setInvois(getInvoisBulanan(perjanjian, calInvois, mp));
						mp.persist(akaun);		
					}
				} else {
					if (calTransaksi.getTime().before(calCurrent.getTime()) || calTransaksi.getTime().equals(calCurrent.getTime())) {
						RkAkaun akaun = new RkAkaun();
						akaun.setFail(perjanjian.getFail());
						akaun.setTarikhInvois(calInvois.getTime());
						akaun.setTarikhTransaksi(calMulaPerjanjian.getTime());
						akaun.setKodHasil((KodHasil) mp.find(KodHasil.class, kodHasilSewa));
						akaun.setIdJenisTransaksi("1"); //CAJ
						akaun.setDebit(getKadarSewa(perjanjian, calInvois, lebahDb));
						akaun.setKeterangan(getKeteranganSewaBulanan(calInvois));
						akaun.setInvois(getInvoisBulanan(perjanjian, calInvois, mp));
						mp.persist(akaun);	
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
	
	private static void generateAkaunIWKForPerjanjian(RkPerjanjian perjanjian, MyPersistence mp, Db lebahDb, Connection conn) {
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
						akaun.setKodHasil((KodHasil) mp.find(KodHasil.class, kodHasilIWK));
						akaun.setIdJenisTransaksi("1"); //CAJ
						akaun.setDebit(getKadarBayaranIWK(perjanjian, calInvois, lebahDb));
						akaun.setKeterangan(getKeteranganCajIWKBulanan(calInvois));
						akaun.setInvois(getInvoisIWKBulanan(perjanjian, calInvois, mp));
						mp.persist(akaun);		
					}
				} else {
					if (calTransaksi.getTime().before(calCurrent.getTime()) || calTransaksi.getTime().equals(calCurrent.getTime())) {
						RkAkaun akaun = new RkAkaun();
						akaun.setFail(perjanjian.getFail());
						akaun.setTarikhInvois(calInvois.getTime());
						akaun.setTarikhTransaksi(calMulaPerjanjian.getTime());
						akaun.setKodHasil((KodHasil) mp.find(KodHasil.class, kodHasilIWK));
						akaun.setIdJenisTransaksi("1"); //CAJ
						akaun.setDebit(getKadarBayaranIWK(perjanjian, calInvois, lebahDb));
						akaun.setKeterangan(getKeteranganCajIWKBulanan(calInvois));
						akaun.setInvois(getInvoisIWKBulanan(perjanjian, calInvois, mp));
						mp.persist(akaun);
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
	
	private static RkInvois getInvoisBulanan(RkPerjanjian perjanjian, GregorianCalendar calInvois, MyPersistence mp) {
		RkInvois invois = null;
		try {
			String noInvois = perjanjian.getFail().getRuang().getIdRuang() + "-" + new DecimalFormat("00").format(calInvois.get(Calendar.MONTH) + 1) + new DecimalFormat("0000").format(calInvois.get(Calendar.YEAR));
			invois = (RkInvois) mp.get("select x from RkInvois x where x.noInvois = '" + noInvois + "' and x.fail.id = '" + perjanjian.getFail().getId() + "' order by x.tarikhMasuk asc");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return invois;
	}
	
	private static RkInvois getInvoisIWKBulanan(RkPerjanjian perjanjian, GregorianCalendar calInvois, MyPersistence mp) {
		RkInvois invois = null;
		try {
			String noInvois = perjanjian.getFail().getRuang().getIdRuang() + "-" + new DecimalFormat("00").format(calInvois.get(Calendar.MONTH) + 1) + new DecimalFormat("0000").format(calInvois.get(Calendar.YEAR)) + " (IWK)";
			invois = (RkInvois) mp.get("select x from RkInvois x where x.noInvois = '" + noInvois + "' and x.fail.id = '" + perjanjian.getFail().getId() + "' order by x.tarikhMasuk asc");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return invois;
	}

	private static void updateStatusTunggakanSewa(RkFail fail, MyPersistence mp) {
		double kadarSewaSemasa = 0D;
		double baki = 0D;
		int abt = 0;
		boolean invoisSemasaTertunggak = false;
		
		Db lebahDb = null;
		try {
			lebahDb = new Db();
			Statement stmt = lebahDb.getStatement();
			
			mp.begin();
			GregorianCalendar calCurrent = (GregorianCalendar) GregorianCalendar.getInstance();
			calCurrent.setTime(new Date());
			GregorianCalendar calTarikhInvois = (GregorianCalendar) GregorianCalendar.getInstance();
			GregorianCalendar calTarikhAkhirInvois = (GregorianCalendar) GregorianCalendar.getInstance();
			
			RkAkaun akaun = (RkAkaun) mp.get("select x from RkAkaun x where x.idJenisTransaksi = '1' and x.kodHasil.id = '" + kodHasilSewa + "' and x.fail.id = '" + fail.getId() + "' order by x.tarikhInvois desc");
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
			mp.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (lebahDb != null) { lebahDb.close(); }
		}	
	}
	
	private static void updateStatusTunggakanIWK(RkFail fail, MyPersistence mp) {
		double kadarBayaranIWKSemasa = 0D;
		double baki = 0D;
		int abt = 0;
		boolean invoisSemasaTertunggak = false;
		
		Db lebahDb = null;
		try {
			lebahDb = new Db();
			Statement stmt = lebahDb.getStatement();
			
			mp.begin();
			GregorianCalendar calCurrent = (GregorianCalendar) GregorianCalendar.getInstance();
			calCurrent.setTime(new Date());
			GregorianCalendar calTarikhInvois = (GregorianCalendar) GregorianCalendar.getInstance();
			GregorianCalendar calTarikhAkhirInvois = (GregorianCalendar) GregorianCalendar.getInstance();
			
			RkAkaun akaun = (RkAkaun) mp.get("select x from RkAkaun x where x.idJenisTransaksi = '1' and x.kodHasil.id = '" + kodHasilIWK + "' and x.fail.id = '" + fail.getId() + "' order by x.tarikhInvois desc");
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
			mp.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (lebahDb != null) { lebahDb.close(); }
		}
	}
	
	private static void janaInvoisBulanan(RkFail fail, boolean sendEmel, boolean regenerateInvois, MyPersistence mp, ServletContext context) {
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
				RkPerjanjian perjanjianSemasa = (RkPerjanjian) mp.get("select x from RkPerjanjian x where x.flagAktif = 'Y'"
						+ " and x.flagPerjanjianSemasa = 'Y' and x.fail.id = '" + fail.getId() + "'");
				
				if (perjanjianSemasa != null) {	
					GregorianCalendar calMulaPerjanjian = (GregorianCalendar) GregorianCalendar.getInstance();
					calMulaPerjanjian.setTime(perjanjianSemasa.getTarikhMula());
					calInvoisBulanSemasa.setTime(perjanjianSemasa.getTarikhMula());

					//INVOIS SEWA
					RkAkaun akaunSewa = (RkAkaun) mp.get("select x from RkAkaun x where x.idJenisTransaksi = '1' and x.kodHasil.id = '" + kodHasilSewa + "' and x.fail.id = '" + fail.getId() + "' order by x.tarikhInvois desc");
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
					RkAkaun akaunIWK = (RkAkaun) mp.get("select x from RkAkaun x where x.idJenisTransaksi = '1' and x.kodHasil.id = '" + kodHasilIWK + "' and x.fail.id = '" + fail.getId() + "' order by x.tarikhInvois desc");
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
								createInvoisRK(fail, perjanjianSemasa, kadarSewaBulanan, tunggakan, abt, calInvoisBulanSemasa, calTarikhAkhirInvois, sendEmel, regenerateInvois, mp, stmt, context);
							} else {
								removeKewInvois(fail, kodHasilSewa, kodJenisBayaranSewa, mp);
							}
							
							//SEMAK AKAUN IWK ADA BAKI TUNGGAKAN ATAU TIDAK
							if (fail.getFlagTunggakanIWK().equals("Y")) {
								kadarIWKBulanan = 0D;
								tunggakanIWK = fail.getNilaiTunggakanIWK();
								abtIWK = fail.getAbtIWK();
								createInvoisIWKRK(fail, perjanjianSemasa, kadarIWKBulanan, tunggakanIWK, abtIWK, calInvoisBulanSemasa, calTarikhAkhirInvois, sendEmel, regenerateInvois, mp, stmt, context);
							} else {
								removeKewInvois(fail, kodHasilIWK, kodJenisBayaranIWK, mp);
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
							
							createInvoisRK(fail, perjanjianSemasa, kadarSewaBulanan, tunggakan, abt, calInvoisBulanSemasa, calTarikhAkhirInvois, sendEmel, regenerateInvois, mp, stmt, context);
							if (perjanjianSemasa.getFlagCajIWK().equals("Y")) {
								createInvoisIWKRK(fail, perjanjianSemasa, kadarIWKBulanan, tunggakanIWK, abtIWK, calInvoisBulanSemasa, calTarikhAkhirInvois, sendEmel, regenerateInvois, mp, stmt, context);
							} else {
								removeKewInvois(fail, kodHasilIWK, kodJenisBayaranIWK, mp);
							}
						}						
					} else {
						// PERJANJIAN TELAH TAMAT						
						//SEMAK AKAUN SEWA ADA BAKI TUNGGAKAN ATAU TIDAK
						if (fail.getFlagTunggakan().equals("Y")) {
							kadarSewaBulanan = 0D;
							tunggakan = fail.getNilaiTunggakan();
							abt = fail.getAbt();
							createInvoisRK(fail, perjanjianSemasa, kadarSewaBulanan, tunggakan, abt, calInvoisBulanSemasa, calTarikhAkhirInvois, sendEmel, regenerateInvois, mp, stmt, context);
						} else {
							removeKewInvois(fail, kodHasilSewa, kodJenisBayaranSewa, mp);
						}
						
						//SEMAK AKAUN IWK ADA BAKI TUNGGAKAN ATAU TIDAK
						if (fail.getFlagTunggakanIWK().equals("Y")) {
							kadarIWKBulanan = 0D;
							tunggakanIWK = fail.getNilaiTunggakanIWK();
							abtIWK = fail.getAbtIWK();
							createInvoisIWKRK(fail, perjanjianSemasa, kadarIWKBulanan, tunggakanIWK, abtIWK, calInvoisBulanSemasa, calTarikhAkhirInvois, sendEmel, regenerateInvois, mp, stmt, context);
						} else {
							removeKewInvois(fail, kodHasilIWK, kodJenisBayaranIWK, mp);
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
			GregorianCalendar calTarikhAkhirInvois, boolean sendEmel, boolean regenerateInvois, MyPersistence mp, Statement stmt, 
			ServletContext context) {
		
		RkInvois invoisRk = null;
		RkInvois newInvoisRk = null;
		try {			
			
			String noInvois = "";
			if (fail != null) {
				mp.begin();
				
				if (fail.getRuang() != null) {
					noInvois = fail.getRuang().getIdRuang() + "-" + new DecimalFormat("00").format(calInvoisBulanSemasa.get(Calendar.MONTH) + 1) + new DecimalFormat("0000").format(calInvoisBulanSemasa.get(Calendar.YEAR));
				}
				
				invoisRk = (RkInvois) mp.get("select x from RkInvois x where x.flagAktif = 'Y' and x.kodHasil.id = '" + kodHasilSewa + "' and x.noInvois = '" + noInvois + "' and x.fail.id = '" + fail.getId() + "'");
				
				//INVOIS RK
				if (invoisRk == null) {
					invoisRk = new RkInvois();
					invoisRk.setFail(fail);
					invoisRk.setTarikhInvois(calInvoisBulanSemasa.getTime());
					invoisRk.setTarikhMula(new Date());
					invoisRk.setTarikhAkhir(calTarikhAkhirInvois.getTime());
					invoisRk.setKodHasil((KodHasil) mp.find(KodHasil.class, kodHasilSewa));
					invoisRk.setNoInvois(noInvois);					
					String keteranganInvoisBulanan = "BAYARAN SEWA BAGI RUANG" + getKeteranganInvoisBulanan(fail);
					invoisRk.setKeterangan(keteranganInvoisBulanan);					
					invoisRk.setAmaunSemasa(kadarSewaBulanan);
					invoisRk.setAmaunTunggakan(tunggakan);
					invoisRk.setAbt(abt);							
					mp.persist(invoisRk);
					
					regenerateInvois = false;
				} else {
					if (regenerateInvois) {
						List<RkInvois> listInvois = mp.list("select x from RkInvois x where x.flagAktif = 'Y' and x.kodHasil.id = '" + kodHasilSewa + "' and x.noInvois = '" + noInvois + "' and x.fail.id = '" + fail.getId() + "'");
						for (RkInvois invois : listInvois) {
							invois.setFlagAktif("T");
						}
								
						//GENERATE NEW INVOIS
						newInvoisRk = new RkInvois();
						newInvoisRk.setFail(fail);
						newInvoisRk.setTarikhInvois(calInvoisBulanSemasa.getTime());
						newInvoisRk.setTarikhMula(new Date());
						newInvoisRk.setTarikhAkhir(calTarikhAkhirInvois.getTime());
						newInvoisRk.setKodHasil((KodHasil) mp.find(KodHasil.class, kodHasilSewa));
						newInvoisRk.setNoInvois(noInvois);
						String keteranganInvoisBulanan = "BAYARAN SEWA BAGI RUANG" + getKeteranganInvoisBulanan(fail);
						newInvoisRk.setKeterangan(keteranganInvoisBulanan);		
						newInvoisRk.setAmaunSemasa(kadarSewaBulanan);
						newInvoisRk.setAmaunTunggakan(tunggakan);
						newInvoisRk.setAbt(abt);	
						mp.persist(newInvoisRk);
					}
				}
				
				String sql = "SELECT id FROM rk_akaun WHERE flag_aktif = 'Y' AND id_kod_hasil = '" + kodHasilSewa + "'"
						+ " AND DATE_FORMAT(STR_TO_DATE('" + sdf.format(calInvoisBulanSemasa.getTime()) + "', '%d/%m/%Y'), '%d/%m/%Y') = DATE_FORMAT(tarikh_invois, '%d/%m/%Y')"
						+ " AND id_fail = '" + fail.getId() + "'";
				ResultSet rs = stmt.executeQuery(sql);	
				if (rs.next()) {
					RkAkaun akaun = (RkAkaun) mp.find(RkAkaun.class, rs.getString("id"));
					if (akaun != null) {
						if (!regenerateInvois) {
							akaun.setInvois(invoisRk);
						}						
					}
				}				
				
				mp.commit();
				
				if (regenerateInvois) {
					pushDataToKewInvois(newInvoisRk, kodHasilSewa, kodJenisBayaranSewa, mp);
				} else {
					pushDataToKewInvois(invoisRk, kodHasilSewa, kodJenisBayaranSewa, mp);
				}				
				
				if (sendEmel) {
					if (regenerateInvois) {
						janaEmelInvois(newInvoisRk, stmt, context); //GENERATE EMEL
					} else {
						janaEmelInvois(invoisRk, stmt, context); //GENERATE EMEL
					}					
				}				
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}	 	
	}
	
	private static void createInvoisIWKRK(RkFail fail, RkPerjanjian perjanjianSemasa, double kadarIWKBulanan, double tunggakanIWK, int abtIWK, 
			GregorianCalendar calInvoisBulanSemasa, GregorianCalendar calTarikhAkhirInvois, boolean sendEmel, boolean regenerateInvois, 
			MyPersistence mp, Statement stmt, ServletContext context) {
		
		RkInvois invoisRk = null;
		RkInvois newInvoisRk = null;
		try {			
			
			String noInvois = "";
			if (fail != null) {
				mp.begin();
				
				if (fail.getRuang() != null) {
					noInvois = fail.getRuang().getIdRuang() + "-" + new DecimalFormat("00").format(calInvoisBulanSemasa.get(Calendar.MONTH) + 1) + new DecimalFormat("0000").format(calInvoisBulanSemasa.get(Calendar.YEAR)) + " (IWK)";
				}
				
				invoisRk = (RkInvois) mp.get("select x from RkInvois x where x.flagAktif = 'Y' and x.kodHasil.id = '" + kodHasilIWK + "' and x.noInvois = '" + noInvois + "' and x.fail.id = '" + fail.getId() + "'");
				
				//INVOIS RK
				if (invoisRk == null) {
					invoisRk = new RkInvois();
					invoisRk.setFail(fail);
					invoisRk.setTarikhInvois(calInvoisBulanSemasa.getTime());
					invoisRk.setTarikhMula(new Date());
					invoisRk.setTarikhAkhir(calTarikhAkhirInvois.getTime());
					invoisRk.setKodHasil((KodHasil) mp.find(KodHasil.class, kodHasilIWK));
					invoisRk.setNoInvois(noInvois);					
					String keteranganInvoisBulanan = "BAYARAN CAJ IWK BAGI RUANG" + getKeteranganInvoisBulanan(fail);
					invoisRk.setKeterangan(keteranganInvoisBulanan);					
					invoisRk.setAmaunSemasa(kadarIWKBulanan);
					invoisRk.setAmaunTunggakan(tunggakanIWK);
					invoisRk.setAbt(abtIWK);							
					mp.persist(invoisRk);
					
					regenerateInvois = false;
				} else {
					if (regenerateInvois) {
						List<RkInvois> listInvois = mp.list("select x from RkInvois x where x.flagAktif = 'Y' and x.kodHasil.id = '" + kodHasilIWK + "' and x.noInvois = '" + noInvois + "' and x.fail.id = '" + fail.getId() + "'");
						for (RkInvois invois : listInvois) {
							invois.setFlagAktif("T");
						}
						
						//GENERATE NEW INVOIS
						newInvoisRk = new RkInvois();
						newInvoisRk.setFail(fail);
						newInvoisRk.setTarikhInvois(calInvoisBulanSemasa.getTime());
						newInvoisRk.setTarikhMula(new Date());
						newInvoisRk.setTarikhAkhir(calTarikhAkhirInvois.getTime());
						newInvoisRk.setKodHasil((KodHasil) mp.find(KodHasil.class, kodHasilIWK));
						newInvoisRk.setNoInvois(noInvois);
						String keteranganInvoisBulanan = "BAYARAN CAJ IWK BAGI RUANG" + getKeteranganInvoisBulanan(fail);
						newInvoisRk.setKeterangan(keteranganInvoisBulanan);					
						newInvoisRk.setAmaunSemasa(kadarIWKBulanan);
						newInvoisRk.setAmaunTunggakan(tunggakanIWK);
						newInvoisRk.setAbt(abtIWK);					
						mp.persist(newInvoisRk);
					}
				}
				
				String sql = "SELECT id FROM rk_akaun WHERE flag_aktif = 'Y' AND id_kod_hasil = '" + kodHasilIWK + "'"
						+ " AND DATE_FORMAT(STR_TO_DATE('" + sdf.format(calInvoisBulanSemasa.getTime()) + "', '%d/%m/%Y'), '%d/%m/%Y') = DATE_FORMAT(tarikh_invois, '%d/%m/%Y')"
						+ " AND id_fail = '" + fail.getId() + "'";
				ResultSet rs = stmt.executeQuery(sql);	
				if (rs.next()) {
					RkAkaun akaun = (RkAkaun) mp.find(RkAkaun.class, rs.getString("id"));
					if (akaun != null) {
						if (!regenerateInvois) {
							akaun.setInvois(invoisRk);
						}						
					}
				}				
				
				mp.commit();
				
				if (regenerateInvois) {
					pushDataToKewInvois(newInvoisRk, kodHasilIWK, kodJenisBayaranIWK, mp);
				} else {
					pushDataToKewInvois(invoisRk, kodHasilIWK, kodJenisBayaranIWK, mp);
				}				
				
				if (sendEmel) {
					if (regenerateInvois) {
						janaEmelInvois(newInvoisRk, stmt, context); //GENERATE EMEL
					} else {
						janaEmelInvois(invoisRk, stmt, context); //GENERATE EMEL
					}					
				}				
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}	 	
	}
	
	private static void removeKewInvois(RkFail fail, String kodHasil, String kodJenisBayaran, MyPersistence mp) {
		try {
			KewInvois invois = (KewInvois) mp.get("select x from KewInvois x where x.kodHasil.id = '" + kodHasil + "'"
					+ " and x.idLejar = '" + fail.getId() + "' and x.flagBayar = 'T' and x.jenisBayaran.id = '" + kodJenisBayaran + "'");
			if (invois != null) {
				mp.begin();
				mp.remove(invois);
				mp.commit();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
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
	
	public static void janaEmelInvois(RkInvois invoisRk, Statement stmt, ServletContext context) {
		try {
			String sql = "SELECT rk_invois.id as id_invois, rk_syarikat.id as id_syarikat, rk_syarikat.nama as nama_syarikat,"
					+ " rk_individu.emel as emel_pemohon, UPPER(rk_fail.no_fail) as no_fail,"
					+ " rk_invois.no_invois,"
					+ " CONCAT(DATE_FORMAT(rk_invois.tarikh_mula, '%d'), ' '," 
					+ " CASE" 
					+ " WHEN DATE_FORMAT(rk_invois.tarikh_mula, '%m') = 01 THEN 'Januari'"
					+ " WHEN DATE_FORMAT(rk_invois.tarikh_mula, '%m') = 02 THEN 'Februari'"
					+ " WHEN DATE_FORMAT(rk_invois.tarikh_mula, '%m') = 03 THEN 'Mac'"
					+ " WHEN DATE_FORMAT(rk_invois.tarikh_mula, '%m') = 04 THEN 'April'"
					+ " WHEN DATE_FORMAT(rk_invois.tarikh_mula, '%m') = 05 THEN 'Mei'"
					+ " WHEN DATE_FORMAT(rk_invois.tarikh_mula, '%m') = 06 THEN 'Jun'"
					+ " WHEN DATE_FORMAT(rk_invois.tarikh_mula, '%m') = 07 THEN 'Julai'"
					+ " WHEN DATE_FORMAT(rk_invois.tarikh_mula, '%m') = 08 THEN 'Ogos'"
					+ " WHEN DATE_FORMAT(rk_invois.tarikh_mula, '%m') = 09 THEN 'September'"
					+ " WHEN DATE_FORMAT(rk_invois.tarikh_mula, '%m') = 10 THEN 'Oktober'"
					+ " WHEN DATE_FORMAT(rk_invois.tarikh_mula, '%m') = 11 THEN 'November'"
					+ " WHEN DATE_FORMAT(rk_invois.tarikh_mula, '%m') = 12 THEN 'Disember'"
					+ " END,"
					+ " ' ', DATE_FORMAT(rk_invois.tarikh_mula, '%Y')) as tarikh_mula,"
					+ " CONCAT(DATE_FORMAT(rk_invois.tarikh_akhir, '%d'), ' ', "
					+ " CASE "
					+ " WHEN DATE_FORMAT(rk_invois.tarikh_akhir, '%m') = 01 THEN 'Januari'"
					+ " WHEN DATE_FORMAT(rk_invois.tarikh_akhir, '%m') = 02 THEN 'Februari'"
					+ " WHEN DATE_FORMAT(rk_invois.tarikh_akhir, '%m') = 03 THEN 'Mac'"
					+ " WHEN DATE_FORMAT(rk_invois.tarikh_akhir, '%m') = 04 THEN 'April'"
					+ " WHEN DATE_FORMAT(rk_invois.tarikh_akhir, '%m') = 05 THEN 'Mei'"
					+ " WHEN DATE_FORMAT(rk_invois.tarikh_akhir, '%m') = 06 THEN 'Jun'"
					+ " WHEN DATE_FORMAT(rk_invois.tarikh_akhir, '%m') = 07 THEN 'Julai'"
					+ " WHEN DATE_FORMAT(rk_invois.tarikh_akhir, '%m') = 08 THEN 'Ogos'"
					+ " WHEN DATE_FORMAT(rk_invois.tarikh_akhir, '%m') = 09 THEN 'September'"
					+ " WHEN DATE_FORMAT(rk_invois.tarikh_akhir, '%m') = 10 THEN 'Oktober'"
					+ " WHEN DATE_FORMAT(rk_invois.tarikh_akhir, '%m') = 11 THEN 'November'"
					+ " WHEN DATE_FORMAT(rk_invois.tarikh_akhir, '%m') = 12 THEN 'Disember'"
					+ " END,"
					+ " ' ', DATE_FORMAT(rk_invois.tarikh_akhir, '%Y')) as tarikh_akhir_bayaran,"
					+ " rk_invois.keterangan AS keterangan_bayaran, rk_invois.amaun_semasa, rk_invois.amaun_tunggakan,"
					+ " CONCAT(CASE "
					+ " WHEN DATE_FORMAT(rk_invois.tarikh_invois, '%m') = 01 THEN 'Januari'"
					+ " WHEN DATE_FORMAT(rk_invois.tarikh_invois, '%m') = 02 THEN 'Februari'"
					+ " WHEN DATE_FORMAT(rk_invois.tarikh_invois, '%m') = 03 THEN 'Mac'"
					+ " WHEN DATE_FORMAT(rk_invois.tarikh_invois, '%m') = 04 THEN 'April'"
					+ " WHEN DATE_FORMAT(rk_invois.tarikh_invois, '%m') = 05 THEN 'Mei'"
					+ " WHEN DATE_FORMAT(rk_invois.tarikh_invois, '%m') = 06 THEN 'Jun'"
					+ " WHEN DATE_FORMAT(rk_invois.tarikh_invois, '%m') = 07 THEN 'Julai'"
					+ " WHEN DATE_FORMAT(rk_invois.tarikh_invois, '%m') = 08 THEN 'Ogos'"
					+ " WHEN DATE_FORMAT(rk_invois.tarikh_invois, '%m') = 09 THEN 'September'"
					+ " WHEN DATE_FORMAT(rk_invois.tarikh_invois, '%m') = 10 THEN 'Oktober'"
					+ " WHEN DATE_FORMAT(rk_invois.tarikh_invois, '%m') = 11 THEN 'November'"
					+ " WHEN DATE_FORMAT(rk_invois.tarikh_invois, '%m') = 12 THEN 'Disember'"
					+ " END,"
					+ " ' ', DATE_FORMAT(rk_invois.tarikh_invois, '%Y')) as bulan_semasa,"
					+ " CONCAT(CASE "
					+ " WHEN DATE_FORMAT(DATE_ADD(tarikh_invois, INTERVAL -rk_invois.abt MONTH), '%m') = 01 THEN 'Januari'"
					+ " WHEN DATE_FORMAT(DATE_ADD(tarikh_invois, INTERVAL -rk_invois.abt MONTH), '%m') = 02 THEN 'Februari'"
					+ " WHEN DATE_FORMAT(DATE_ADD(tarikh_invois, INTERVAL -rk_invois.abt MONTH), '%m') = 03 THEN 'Mac'"
					+ " WHEN DATE_FORMAT(DATE_ADD(tarikh_invois, INTERVAL -rk_invois.abt MONTH), '%m') = 04 THEN 'April'"
					+ " WHEN DATE_FORMAT(DATE_ADD(tarikh_invois, INTERVAL -rk_invois.abt MONTH), '%m') = 05 THEN 'Mei'"
					+ " WHEN DATE_FORMAT(DATE_ADD(tarikh_invois, INTERVAL -rk_invois.abt MONTH), '%m') = 06 THEN 'Jun'"
					+ " WHEN DATE_FORMAT(DATE_ADD(tarikh_invois, INTERVAL -rk_invois.abt MONTH), '%m') = 07 THEN 'Julai'"
					+ " WHEN DATE_FORMAT(DATE_ADD(tarikh_invois, INTERVAL -rk_invois.abt MONTH), '%m') = 08 THEN 'Ogos'"
					+ " WHEN DATE_FORMAT(DATE_ADD(tarikh_invois, INTERVAL -rk_invois.abt MONTH), '%m') = 09 THEN 'September'"
					+ " WHEN DATE_FORMAT(DATE_ADD(tarikh_invois, INTERVAL -rk_invois.abt MONTH), '%m') = 10 THEN 'Oktober'"
					+ " WHEN DATE_FORMAT(DATE_ADD(tarikh_invois, INTERVAL -rk_invois.abt MONTH), '%m') = 11 THEN 'November'"
					+ " WHEN DATE_FORMAT(DATE_ADD(tarikh_invois, INTERVAL -rk_invois.abt MONTH), '%m') = 12 THEN 'Disember'"
					+ " END,"
					+ " ' ', DATE_FORMAT(DATE_ADD(tarikh_invois, INTERVAL -rk_invois.abt MONTH), '%Y')) as bulan_tunggakan_dari,"
					+ " CONCAT(CASE "
					+ " WHEN DATE_FORMAT(DATE_ADD(tarikh_invois, INTERVAL -1 MONTH), '%m') = 01 THEN 'Januari'"
					+ " WHEN DATE_FORMAT(DATE_ADD(tarikh_invois, INTERVAL -1 MONTH), '%m') = 02 THEN 'Februari'"
					+ " WHEN DATE_FORMAT(DATE_ADD(tarikh_invois, INTERVAL -1 MONTH), '%m') = 03 THEN 'Mac'"
					+ " WHEN DATE_FORMAT(DATE_ADD(tarikh_invois, INTERVAL -1 MONTH), '%m') = 04 THEN 'April'"
					+ " WHEN DATE_FORMAT(DATE_ADD(tarikh_invois, INTERVAL -1 MONTH), '%m') = 05 THEN 'Mei'"
					+ " WHEN DATE_FORMAT(DATE_ADD(tarikh_invois, INTERVAL -1 MONTH), '%m') = 06 THEN 'Jun'"
					+ " WHEN DATE_FORMAT(DATE_ADD(tarikh_invois, INTERVAL -1 MONTH), '%m') = 07 THEN 'Julai'"
					+ " WHEN DATE_FORMAT(DATE_ADD(tarikh_invois, INTERVAL -1 MONTH), '%m') = 08 THEN 'Ogos'"
					+ " WHEN DATE_FORMAT(DATE_ADD(tarikh_invois, INTERVAL -1 MONTH), '%m') = 09 THEN 'September'"
					+ " WHEN DATE_FORMAT(DATE_ADD(tarikh_invois, INTERVAL -1 MONTH), '%m') = 10 THEN 'Oktober'"
					+ " WHEN DATE_FORMAT(DATE_ADD(tarikh_invois, INTERVAL -1 MONTH), '%m') = 11 THEN 'November'"
					+ " WHEN DATE_FORMAT(DATE_ADD(tarikh_invois, INTERVAL -1 MONTH), '%m') = 12 THEN 'Disember'"
					+ " END,"
					+ " ' ', DATE_FORMAT(DATE_ADD(tarikh_invois, INTERVAL -1 MONTH), '%Y')) as bulan_tunggakan_hingga,"
					+ " rk_invois.abt"
					+ " FROM rk_invois,"
					+ " rk_fail, rk_pemohon, rk_individu, rk_syarikat, ruj_bandar, ruj_negeri"
					+ " WHERE     rk_invois.id_fail = rk_fail.id"
					+ " AND rk_fail.id_pemohon = rk_pemohon.id"
					+ " AND rk_pemohon.id_individu = rk_individu.id"
					+ " AND rk_pemohon.id_syarikat = rk_syarikat.id"
					+ " AND rk_syarikat.id_bandar = ruj_bandar.id"
					+ " AND ruj_bandar.id_negeri = ruj_negeri.id"
					+ " AND rk_invois.id = '" + invoisRk.getId() + "'";
			ResultSet rsInvois = stmt.executeQuery(sql);	
			if (rsInvois.next()) {
				String idInvois = rsInvois.getString("id_invois");
				String idSyarikat = rsInvois.getString("id_syarikat");
				String namaSyarikat = rsInvois.getString("nama_syarikat");
				String emel = rsInvois.getString("emel_pemohon");
				String keteranganBayaran = rsInvois.getString("keterangan_bayaran");
				String noFail = rsInvois.getString("no_fail");
				String noInvois = rsInvois.getString("no_invois");
				String tarikhInvois = rsInvois.getString("tarikh_mula");				
				String tarikhAkhirBayaran = rsInvois.getString("tarikh_akhir_bayaran");
				double sewaSemasa = rsInvois.getDouble("amaun_semasa");
				double tunggakanSewa = rsInvois.getDouble("amaun_tunggakan");
				double jumlahKenaBayar = sewaSemasa + tunggakanSewa;
				String bulanSemasa = rsInvois.getString("bulan_semasa");
				String bulanTunggakanDari = rsInvois.getString("bulan_tunggakan_dari");
				String bulanTunggakanHingga = rsInvois.getString("bulan_tunggakan_hingga");
				int abt = rsInvois.getInt("abt");
				
				if (emel != null && emel != "")  {
					RkMailer.get().emelNotisTuntutanBayaran(idInvois, idSyarikat, namaSyarikat, emel, keteranganBayaran, noFail, noInvois,
							tarikhInvois, tarikhAkhirBayaran, sewaSemasa, tunggakanSewa, jumlahKenaBayar, bulanSemasa, bulanTunggakanDari,
							bulanTunggakanHingga, abt, context);
				}				
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}	

	private static void pushDataToKewInvois(RkInvois invoisRk, String kodHasil, String kodJenisBayaran, MyPersistence mp) {
		boolean addKewInvois = false;
		KewInvois invoisKewAktif = null;
		try {	
			mp.begin();
			invoisKewAktif = (KewInvois) mp.get("select x from KewInvois x where x.kodHasil.id = '" + kodHasil + "' and x.idLejar = '" + invoisRk.getFail().getId() + "' and x.flagBayar = 'T'");
			if (invoisKewAktif == null) {
				invoisKewAktif = new KewInvois();
				addKewInvois = true;				
			}
			invoisKewAktif.setKodHasil((KodHasil) mp.find(KodHasil.class, kodHasil));
			invoisKewAktif.setFlagBayaran("SEWA");
			invoisKewAktif.setNoInvois(invoisRk.getNoInvois());	
			invoisKewAktif.setTarikhInvois(invoisRk.getTarikhMula());
			invoisKewAktif.setNoRujukan(invoisRk.getFail().getNoFail());
			invoisKewAktif.setIdLejar(invoisRk.getFail().getId());
			invoisKewAktif.setPembayar(getPembayar(invoisRk.getFail(), mp));		
			invoisKewAktif.setJenisBayaran((KewJenisBayaran) mp.find(KewJenisBayaran.class, kodJenisBayaran));				
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
				mp.persist(invoisKewAktif);
			}
			mp.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}		
	}	

	private static Users getPembayar(RkFail fail, MyPersistence mp) {
		Users pembayar = null;
		try {
			if (fail.getPemohon() != null) {
				if (fail.getPemohon().getSyarikat() != null) {
					pembayar = (Users) mp.find(Users.class, fail.getPemohon().getSyarikat().getId());
				} else if (fail.getPemohon().getIndividu() != null) {
					pembayar = (Users) mp.find(Users.class, fail.getPemohon().getIndividu().getId());
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

	public static void generateDepositPermohonan(RkSST sst, String userId, MyPersistence mp) {
		try {
			mp.begin();
			RkInvois invois = new RkInvois();
			invois.setFail(sst.getPermohonan().getFail());
			invois.setPermohonan(sst.getPermohonan());
			invois.setTarikhInvois(sst.getTarikhMulaSST());
			invois.setTarikhMula(sst.getTarikhMulaSST());
			invois.setTarikhAkhir(sst.getTarikhTamatSST());
			invois.setKodHasil((KodHasil) mp.find(KodHasil.class, kodHasilDeposit));	
			invois.setNoInvois(sst.getNoRujukanSST());
			String keteranganInvoisDeposit = "BAYARAN DEPOSIT BAGI RUANG" + getKeteranganInvoisBulanan(sst.getPermohonan().getFail());
			invois.setKeterangan(keteranganInvoisDeposit);
			invois.setAmaunSemasa(sst.getDeposit());
			invois.setDaftarOleh((Users) mp.find(Users.class, userId));
			mp.persist(invois);
			
			KewDeposit deposit = new KewDeposit();
			deposit.setKodHasil(invois.getKodHasil());
			deposit.setJenisBayaran((KewJenisBayaran) mp.find(KewJenisBayaran.class, kodJenisBayaranDeposit));
			deposit.setIdLejar(sst.getPermohonan().getId());
			deposit.setNoInvois(sst.getNoRujukanSST());
			deposit.setTarikhDari(sst.getTarikhMula());
			deposit.setTarikhHingga(sst.getTarikhTamat());
			deposit.setKeteranganDeposit(invois.getKeterangan());
			deposit.setPendeposit(getPembayar(sst.getPermohonan().getFail(), mp));
			deposit.setJumlahDeposit(sst.getDeposit());
			deposit.setTarikhDeposit(sst.getTarikhMulaSST());
			deposit.setDaftarOleh((Users) mp.find(Users.class, userId));
			mp.persist(deposit);
			mp.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
