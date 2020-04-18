package bph.scheduler.rk;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import bph.entities.rk.RkPerjanjian;
import bph.mail.mailer.RkMailer;
import db.persistence.MyPersistence;

public class MailSuratPengecualianSewaJob implements Job {

	static Logger myLogger = Logger.getLogger("MailSuratPengecualianSewaJob");
	private MyPersistence mp;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		myLogger.info("Executing MailSuratPengecualianSewaJob on : " + new Date());
		System.out.println("Executing MailSuratPengecualianSewaJob on : " + new Date());
		
		try {
			mp = new MyPersistence();
			List<RkPerjanjian> listPerjanjian = mp
					.list("select x from RkPerjanjian x where x.catatan like 'PENGECUALIAN SELAMA 6 BULAN DISEBABKAN OLEH PERINTAH KAWALAN PERGERAKAN SUSULAN PENULARAN WABAK COVID-19' and x.fail.ruang.seksyen.id = '06'");
			for (RkPerjanjian perjanjian : listPerjanjian) {
				RkMailer.get().emelSuratPengecualianBayaranSewa(perjanjian.getFail().getId(), perjanjian.getFail().getPemohon().getIndividu().getEmel(), null);
			}		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		myLogger.info("Finish MailSuratPengecualianSewaJob on : " + new Date());
		System.out.println("Finish MailSuratPengecualianSewaJob on : " + new Date());
	}
}
