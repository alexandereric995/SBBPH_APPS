package testing;

import java.util.Date;
import java.util.List;

import lebah.template.DbPersistence;
import portal.module.entity.Users;
import portal.module.entity.UsersSpouse;
import bph.entities.integrasi.IntHRMIS;
import bph.entities.kewangan.KewBayaranResit;
import bph.entities.kewangan.KewDeposit;
import bph.entities.kewangan.KewResitSenaraiInvois;
import bph.entities.kod.JenisPengenalan;

public class TestingEnWan {

	private static DbPersistence db;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("START JOB ON : " + new Date());
//		updateTarikhBayaranTuntutanDeposit();
		kemaskiniMaklumatPasangan();
		System.out.println("FINISH JOB ON : " + new Date());
	}
	
	private static void updateTarikhBayaranTuntutanDeposit() {
		try {
			db = new DbPersistence();
			db.begin();
			List<KewDeposit> listDeposit = db.list("select x from KewDeposit x where x.tarikhBayaran is null"
					+ " and x.tuntutanDeposit.noBaucerPulanganDeposit is not null and x.tuntutanDeposit.noEft is not null"
					+ " and x.tuntutanDeposit.tarikhEft is not null");
			System.out.println(listDeposit.size());
			for (int i = 0; i < listDeposit.size(); i++) {
				KewDeposit deposit = listDeposit.get(i);	
				KewBayaranResit resit = (KewBayaranResit) db.get("select x from KewBayaranResit x where x.noResit = '" + deposit.getNoResit() + "'");
				if (resit != null) {
					deposit.setTarikhBayaran(resit.getTarikhBayaran());
				} else {
					KewResitSenaraiInvois rsi = (KewResitSenaraiInvois) db.get("select x from KewResitSenaraiInvois x where x.deposit.id = '" + deposit.getId() +"'");
					if (rsi != null) {
						deposit.setTarikhBayaran(rsi.getResit().getTarikhBayaran());
					}
				}
			}
			db.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}				
	}
	
	private static void kemaskiniMaklumatPasangan() {
		try {
			db = new DbPersistence();
			db.begin();
			List<Users> list = db.list("select x from Users x where x.role.name = '(AWAM) Penjawat Awam' and x.flagSemakanHRMIS = 'Y' ");
			System.out.println(list.size());
			for (int i = 0; i < list.size(); i++) {
				Users users = list.get(i);
				UsersSpouse spouse = (UsersSpouse) db.get("select x from UsersSpouse x where x.users.id = '" + users.getId() + "'");
				if (spouse == null) {
					IntHRMIS hrmis = (IntHRMIS) db.get("select x from IntHRMIS x where x.noPengenalan = '" + users.getId() + "'");
					if (hrmis != null) {					
						// USER SPOUSE
						spouse = new UsersSpouse();
						spouse.setUsers(users);				
						spouse.setNamaPasangan(hrmis.getNamaPasangan());
						spouse.setJenisPengenalan((JenisPengenalan) db.find(JenisPengenalan.class, "B"));
						spouse.setNoKPPasangan(hrmis.getNoPengenalanPasangan());
						spouse.setNoTelBimbit(hrmis.getNoTelefonPasangan());
						spouse.setJenisPekerjaan(hrmis.getPekerjaanPasangan());
						spouse.setNamaSyarikat(hrmis.getMajikanPasangan());
						db.persist(spouse);
						System.out.println(i + " from " + list.size() + " : " + users.getId() + " - DONE");
					}
				}				
			}
			db.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}				
	}

}
