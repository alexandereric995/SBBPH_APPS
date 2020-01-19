package bph.scheduler.utiliti;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import bph.entities.utiliti.UtilPermohonan;
import bph.mail.mailer.UtilitiMailer;
import db.persistence.MyPersistence;

public class NotifikasiPembatalanPermohonanDewanBelumBayarJob implements Job {

	static Logger myLogger = Logger.getLogger("NotifikasiPembatalanPermohonanDewanBelumBayarJob");
	private MyPersistence mp;

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		myLogger.info("Executing NotifikasiPembatalanPermohonanDewanBelumBayarJob on : " + new Date());
		System.out.println("Executing NotifikasiPembatalanPermohonanDewanBelumBayarJob on : " + new Date());
		
		try {
			mp = new MyPersistence();

			List<UtilPermohonan> listPermohonan = mp.list("select x from UtilPermohonan x where x.statusBayaran = 'T' and x.statusPermohonan in ('Y') and x.gelanggang is null");
			for (UtilPermohonan permohonan : listPermohonan) {
				if (permohonan.getTarikhKelulusan() != null) {
					Calendar calCurrent = new GregorianCalendar();
					calCurrent.setTime(new Date());
					Calendar calKelulusan = new GregorianCalendar();
					calKelulusan.setTime(permohonan.getTarikhKelulusan());
					calKelulusan.add(Calendar.DATE, 14);
					if (calCurrent.after(calKelulusan)) {
						emailtoGuest(permohonan);;
					}
				}				
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		myLogger.info("Finish NotifikasiPembatalanPermohonanDewanBelumBayarJob on : " + new Date());
		System.out.println("Finish NotifikasiPembatalanPermohonanDewanBelumBayarJob on : " + new Date());
	}

	/**
	 * EMAIL TO GUEST - EMAIL PEMBERITAHUAN TUNTUTAN BAYARAN
	 * */
	public static void emailtoGuest(UtilPermohonan r) {
		if (r.getPemohon() != null) {
			if ((!r.getPemohon().getId().equalsIgnoreCase("faizal") && !r.getPemohon().getId().equalsIgnoreCase("anon"))) {
				if (r.getPemohon().getEmel() != null) {
					if (!r.getPemohon().getEmel().equalsIgnoreCase("")) {
						UtilitiMailer.get().notifikasiPembayaranTempahanDewan(r.getPemohon().getEmel(), r);
					}
				}
			}
		} else {
			myLogger.info("NotifikasiPembatalanPermohonanDewanBelumBayarJob PEMOHON IS NULL : ID_PERMOHONAN = " + r.getId());
		}
	}// close emailtoGuest

}
