package bph.scheduler.rpp;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

import lebah.db.Db;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import bph.entities.kewangan.KewDeposit;
import bph.entities.kewangan.KewInvois;
import bph.entities.rpp.RppAkaun;
import bph.entities.rpp.RppJadualTempahan;
import bph.entities.rpp.RppPermohonan;
import db.persistence.MyPersistence;

public class DataMaintainanceRPPJob implements Job {
	
	static Logger myLogger = Logger.getLogger("DataMaintainanceRPPJob");
	private MyPersistence mp;
	private Db lebahDB;

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		myLogger.info("Executing DataMaintainanceRPPJob on : " + new Date());
		System.out.println("Executing DataMaintainanceRPPJob on : " + new Date());
		
		try {
			mp = new MyPersistence();
			
			//DELETE ALL PERMOHONAN RPP TIDAK LENGKAP
			deletePermohonanTidakLengkap(mp);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		myLogger.info("Finish DataMaintainanceRPPJob on : " + new Date());
		System.out.println("Finish DataMaintainanceRPPJob on : " + new Date());
	}

	private void deletePermohonanTidakLengkap(MyPersistence mp) {
		try {
			lebahDB = new Db();
			Statement stmt = lebahDB.getStatement();
			String sql = "SELECT id FROM rpp_permohonan WHERE(id_pemohon IS NULL OR id_pemohon = 'anon') AND jenis_pemohon = 'INDIVIDU'"
					+ " UNION"
					+ " SELECT id FROM rpp_permohonan WHERE id_peranginan IS NULL";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				RppPermohonan permohonan = (RppPermohonan) mp.find(RppPermohonan.class, rs.getString("id"));
				if (permohonan != null) {
					mp.begin();
					//JADUAL TEMPAHAN
					List<RppJadualTempahan> listJadualTempahan = mp.list("select x from RppJadualTempahan x where x.permohonan.id = '" + permohonan.getId() + "'");
					for (RppJadualTempahan jadualTempahan : listJadualTempahan) {
						mp.remove(jadualTempahan);
					}
					
					//RPP AKAUN
					List<RppAkaun> listAkaun = mp.list("select x from RppAkaun x where x.permohonan.id = '" + permohonan.getId() + "'");
					for (RppAkaun akaun : listAkaun) {
						//KEW INVOIS
						KewInvois invois = (KewInvois) mp.get("select x from KewInvois x where x.idLejar = '" + akaun.getId() + "'");
						if (invois != null) {
							mp.remove(invois);
						}
						
						//KEW DEPOSIT
						KewDeposit deposit = (KewDeposit) mp.get("select x from KewDeposit x where x.idLejar = '" + akaun.getId() + "'");
						if (deposit != null) {
							mp.remove(deposit);
						}
						mp.remove(akaun);
					}
					
					mp.remove(permohonan);
					mp.commit();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (lebahDB != null)
				lebahDB.close();
		}
	}
}
