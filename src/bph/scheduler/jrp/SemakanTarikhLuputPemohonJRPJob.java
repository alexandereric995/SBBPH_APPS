package bph.scheduler.jrp;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lebah.entity.UserRole;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import portal.module.entity.Users;
import bph.entities.bgs.BgsPemohon;
import db.persistence.MyPersistence;

public class SemakanTarikhLuputPemohonJRPJob implements Job {
	
	static Logger myLogger = Logger.getLogger("SemakanTarikhLuputPemohonJRPJob");
	private MyPersistence mp;

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		myLogger.info("Executing SemakanTarikhLuputPemohonJRPJob on : " + new Date());
		System.out.println("Executing SemakanTarikhLuputPemohonJRPJob on : " + new Date());
		
		try {
			mp = new MyPersistence();
			
			List<BgsPemohon> listPemohon = mp.list("select x from BgsPemohon x where x.flagAktif = 'Y' order by x.tarikhDaftar asc");
			for (int i = 0; i < listPemohon.size(); i++) {
				BgsPemohon pemohon = listPemohon.get(i);
				if (pemohon != null) {
					if (pemohon.getTarikhLuput() != null) {
						Calendar tarikhLuput = Calendar.getInstance();
						tarikhLuput.setTime(pemohon.getTarikhLuput());
						tarikhLuput.add(Calendar.DATE, 1);
						Calendar today = Calendar.getInstance();
						today.setTime(new Date());
						if (today.after(tarikhLuput)) {
							deactivateUser(pemohon, mp);
						}
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		myLogger.info("Finish SemakanTarikhLuputPemohonJRPJob on : " + new Date());
		System.out.println("Finish SemakanTarikhLuputPemohonJRPJob on : " + new Date());
	}

	private void deactivateUser(BgsPemohon pemohon, MyPersistence mp) {
		try {
			mp.begin();
			pemohon.setFlagAktif("T");			
			Users user = (Users) mp.find(Users.class, pemohon.getId());
			if (user != null) {
				//DELETE SECONDARY ROLE
				UserRole userRole = (UserRole) mp.get("select x from UserRole x where x.userId = '" + pemohon.getId() + "' and x.roleId = '(JRP) Pemohon'");
				if (userRole != null) {
					mp.remove(userRole);
				} else {
					// DELETE MAIN USER
					if ("(JRP) Pemohon".equals(user.getRole().getName())) {
						mp.remove(user);
					}
				}
			}				
			mp.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
