package bph.scheduler.senggara;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import bph.entities.senggara.MtnKontraktorSenaraiHitam;
import db.persistence.MyPersistence;

public class SemakanStatusSenaraiHitamKontraktorJob implements Job {
	
	static Logger myLogger = Logger.getLogger("SemakanStatusSenaraiHitamKontraktorJob");
	private MyPersistence mp;

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		myLogger.info("Executing SemakanStatusSenaraiHitamKontraktorJob on : " + new Date());
		System.out.println("Executing SemakanStatusSenaraiHitamKontraktorJob on : " + new Date());
		
		try {
			mp = new MyPersistence();
			mp.begin();
			//SENARAI HITAM KONTRAKTOR
			List<MtnKontraktorSenaraiHitam> listSenaraiHitam = mp.list("select x from MtnKontraktorSenaraiHitam x where x.flagAktif = 'Y' order by x.id asc");
			for (int i = 0; i < listSenaraiHitam.size(); i++) {
				MtnKontraktorSenaraiHitam senaraiHitam = listSenaraiHitam.get(i);
				if (senaraiHitam != null) {					
					//SEMAKAN TARIKH LUPUT
					if (senaraiHitam.getTarikhTamat() != null) {
						Calendar currentDate = Calendar.getInstance();
						currentDate.setTime(new Date());
						Calendar tarikhLuput = Calendar.getInstance();
						tarikhLuput.setTime(senaraiHitam.getTarikhTamat());
						tarikhLuput.add(Calendar.DATE, 1);
						if (currentDate.getTime().after(tarikhLuput.getTime())) {							
							senaraiHitam.setTarikhBebas(new Date());
							senaraiHitam.setFlagAktif("T");
						}
					}
				}
			}
			mp.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		myLogger.info("Finish SemakanStatusSenaraiHitamKontraktorJob on : " + new Date());
		System.out.println("Finish SemakanStatusSenaraiHitamKontraktorJob on : " + new Date());
	}
}
