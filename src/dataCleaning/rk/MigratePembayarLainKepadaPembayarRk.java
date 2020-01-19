/**
 * 
 */
package dataCleaning.rk;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

import lebah.db.Db;
import lebah.template.DbPersistence;
import portal.module.entity.Users;
import bph.entities.kewangan.KewBayaranResit;
import bph.entities.kewangan.KewInvois;
import bph.entities.kewangan.KewResitSenaraiInvois;
import bph.entities.rk.RkAkaun;

/**
 * @author Mohd Faizal
 * 
 */
public class MigratePembayarLainKepadaPembayarRk {

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
