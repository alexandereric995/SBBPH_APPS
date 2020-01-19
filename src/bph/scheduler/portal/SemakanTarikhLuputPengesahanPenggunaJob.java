package bph.scheduler.portal;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lebah.util.Util;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import portal.module.entity.Users;
import bph.mail.mailer.DaftarAkaunBaruMailer;
import db.persistence.MyPersistence;

public class SemakanTarikhLuputPengesahanPenggunaJob implements Job {
	
	static Logger myLogger = Logger.getLogger("SemakanTarikhLuputPengesahanPenggunaJob");
	private MyPersistence mp;

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		myLogger.info("Executing SemakanTarikhLuputPengesahanPenggunaJob on : " + new Date());
		System.out.println("Executing SemakanTarikhLuputPengesahanPenggunaJob on : " + new Date());
		
		try {
			mp = new MyPersistence();
			
			List<Users> listUsers = mp.list("select x from Users x where x.role.name in ('(AWAM) Badan Berkanun', '(AWAM) Polis / Tentera') and x.flagDaftarSBBPH = 'Y' and x.flagAktif = 'Y' order by x.dateRegistered desc");
			for (int i = 0; i < listUsers.size(); i++) {
				Users users = listUsers.get(i);
				if (users != null) {
					if (users.getTarikhLuputPengesahan() != null) {
						Calendar tarikhLuput = Calendar.getInstance();
						tarikhLuput.setTime(users.getTarikhLuputPengesahan());
						tarikhLuput.add(Calendar.DATE, 1);
						Calendar today = Calendar.getInstance();
						today.setTime(new Date());
						if (today.getTime().after(tarikhLuput.getTime())) {
							deactivateUser(users, mp);
						}
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		myLogger.info("Finish SemakanTarikhLuputPengesahanPenggunaJob on : " + new Date());
		System.out.println("Finish SemakanTarikhLuputPengesahanPenggunaJob on : " + new Date());
	}

	private void deactivateUser(Users users, MyPersistence mp) {
		try {
			mp.begin();
			users.setFlagAktif("T");
			users.setFlagMenungguPengesahan("T");
			users.setCatatanPengesahan("TEMPOH SAH AKAUN PENGGUNA TELAH LUPUT PADA : " + Util.getDateTime(users.getTarikhLuputPengesahan(),"dd-MM-yyyy"));
			mp.commit();
			// GENERATE EMEL
			DaftarAkaunBaruMailer.get().tempohPengesahanLuput(users.getEmel(), users);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
