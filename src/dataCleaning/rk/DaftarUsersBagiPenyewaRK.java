/**
 * 
 */
package dataCleaning.rk;

import java.util.Date;
import java.util.List;

import lebah.entity.UserRole;
import lebah.template.DbPersistence;
import portal.module.entity.CSS;
import portal.module.entity.Role;
import portal.module.entity.Users;
import bph.entities.kod.JenisPengenalan;
import bph.entities.kod.KategoriPengguna;
import bph.entities.rk.RkFail;
import bph.entities.rk.RkIndividu;
import bph.entities.rk.RkSyarikat;

/**
 * @author Mohd Faizal
 *
 */
public class DaftarUsersBagiPenyewaRK {
	
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
		List<RkFail> listFail = db.list("select x from RkFail x where x.flagAktifPerjanjian = 'Y' order by x.id asc");
		for (int i = 0; i < listFail.size(); i++) {
			RkFail fail = listFail.get(i);
			if (fail != null) {
				if (fail.getPemohon() != null) {
					if (fail.getPemohon().getIndividu() != null) {
						daftarPemohon(fail.getPemohon().getIndividu(), db);
					}
					if (fail.getPemohon().getSyarikat() != null) {
						daftarSyarikat(fail.getPemohon().getSyarikat(), db);
					}
				}
			}
		}
	}

	private static void daftarPemohon(RkIndividu individu, DbPersistence mp) {
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
	
	private static void daftarSyarikat(RkSyarikat syarikat, DbPersistence mp) {
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
}
