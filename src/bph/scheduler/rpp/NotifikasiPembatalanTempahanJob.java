package bph.scheduler.rpp;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import bph.entities.rpp.RppPermohonan;
import bph.mail.mailer.RppMailer;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class NotifikasiPembatalanTempahanJob implements Job {
	
	static Logger myLogger = Logger.getLogger("NotifikasiPembatalanTempahanJob");
	private MyPersistence mp;

	@SuppressWarnings("unchecked")
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		myLogger.info("Executing NotifikasiPembatalanTempahanJob on : " + new Date());
		System.out.println("Executing NotifikasiPembatalanTempahanJob on : " + new Date());
		
		int count = 0;
		try {
			mp = new MyPersistence();			
			
			String hql = " select x from RppPermohonan x where x.jenisPemohon = 'INDIVIDU' and x.jenisPermohonan = 'ONLINE' and x.rppPeranginan.jenisPeranginan.id <> 'RP' "+
					 " and x.statusBayaran = 'T' and x.status.id = '1425259713412' and x.pemohon.id not in ('anon','faizal') and x.tarikhAkhirBayaran is not null ";
			List<RppPermohonan> list = mp.list(hql);
			
			for(int i=0;i < list.size();i++){
			
				RppPermohonan r = list.get(i);
			
				Calendar currentDate = Calendar.getInstance();
				currentDate.setTime(new Date());
				currentDate.set(Calendar.HOUR_OF_DAY, 0);
				currentDate.set(Calendar.MINUTE, 0);
				currentDate.set(Calendar.SECOND, 0);
				currentDate.set(Calendar.MILLISECOND, 0);
				Calendar cal = Calendar.getInstance();
				cal.setTime(r.getTarikhAkhirBayaran());
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				
				if (Util.daysBetween(currentDate.getTime(),cal.getTime()) <= 3) {
					emailtoGuest(r);
					count++;
				}
			
			}
			
		} catch (Exception e) {
			System.out.println("Error NotifikasiPembatalanTempahanJob : "+e.getMessage());
			e.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		myLogger.info("Finish NotifikasiPembatalanTempahanJob on : " + new Date() +"  Total emel send : "+count);
		System.out.println("Finish NotifikasiPembatalanTempahanJob on : " + new Date() +"  Total emel send : "+count);
	}
	
	
	/**
	 * EMAIL TO GUEST
	 * EMAIL PERINGATAN BUAT PEMBAYARAN
	 * */
	public static void emailtoGuest(RppPermohonan r) {
		if (r.getPemohon() != null) {
			if((!r.getPemohon().getId().equalsIgnoreCase("faizal") && !r.getPemohon().getId().equalsIgnoreCase("anon")) && ResourceBundle.getBundle("dbconnection").getString("SERVER_DEFINITION").equals("LIVE")){
				if (r.getPemohon().getEmel() != null) {
					if( r.getPemohon().getEmel() != null ){
						if (!r.getPemohon().getEmel().equalsIgnoreCase("")) {
							RppMailer.get().notifikasiPembayaranTempahan(r.getPemohon().getEmel(),r);
						}						
					}
				}
			}
		} else {
			System.out.println("NotifikasiPembatalanTempahanJob PEMOHON IS NULL : ID_PERMOHONAN = " + r.getId());
		}	
	}//close emailtoGuest
	
}
