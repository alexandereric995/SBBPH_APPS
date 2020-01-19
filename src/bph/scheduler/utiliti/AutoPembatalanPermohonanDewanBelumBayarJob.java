package bph.scheduler.utiliti;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import portal.module.entity.Users;
import bph.entities.kewangan.KewInvois;
import bph.entities.utiliti.UtilAkaun;
import bph.entities.utiliti.UtilJadualTempahan;
import bph.entities.utiliti.UtilPermohonan;
import bph.mail.mailer.UtilitiMailer;
import db.persistence.MyPersistence;

public class AutoPembatalanPermohonanDewanBelumBayarJob implements Job {

	static Logger myLogger = Logger.getLogger("AutoPembatalanPermohonanDewanBelumBayarJob");
	private MyPersistence mp;

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		myLogger.info("Executing AutoPembatalanPermohonanDewanBelumBayarJob on : " + new Date());
		System.out.println("Executing AutoPembatalanPermohonanDewanBelumBayarJob on : " + new Date());
		
		try {
			mp = new MyPersistence();

			List<UtilPermohonan> listPermohonan = mp.list("select x from UtilPermohonan x where x.statusBayaran = 'T' and x.statusPermohonan in ('R', 'Y') and x.gelanggang is null");
			for (UtilPermohonan permohonan : listPermohonan) {
				Calendar calCurrent = new GregorianCalendar();
				calCurrent.setTime(new Date());								
				
				//BATAL PERMOHONAN BELUM LULUS YANG MELEBIHI 14 HARI DARI TARIKH PERMOHONAN
				if (permohonan.getStatusPermohonan().equals("R")) {
					Calendar calPermohonan = new GregorianCalendar();
					calPermohonan.setTime(permohonan.getTarikhPermohonan());
					calPermohonan.add(Calendar.DATE, 14);
					if (calCurrent.after(calPermohonan)) {
						mp.begin();
						batalPermohonan(mp, permohonan, permohonan.getStatusPermohonan());
						mp.commit();
						emailtoGuestTiadaKelulusan(permohonan);
					}
				}
				
				//BATAL PERMOHONAN TELAH LULUS DAN BELUM BUAT BAYARAN YANG MELEBIHI 14 HARI DARI TARIKH KELULUSAN
				if (permohonan.getStatusPermohonan().equals("Y")) {
					if (permohonan.getTarikhKelulusan() != null) {
						Calendar calKelulusan = new GregorianCalendar();
						calKelulusan.setTime(permohonan.getTarikhKelulusan());
						calKelulusan.add(Calendar.DATE, 14);
						if (calCurrent.after(calKelulusan)) {
							mp.begin();
							batalPermohonan(mp, permohonan, permohonan.getStatusPermohonan());
							mp.commit();
							emailtoGuestTiadaBayaran(permohonan);
						}
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
		myLogger.info("Finish AutoPembatalanPermohonanDewanBelumBayarJob on : " + new Date());
		System.out.println("Finish AutoPembatalanPermohonanDewanBelumBayarJob on : " + new Date());
	}
	
	private void batalPermohonan(MyPersistence mp, UtilPermohonan permohonan, String statusPermohonan) {
		try {
			UtilAkaun akaun = (UtilAkaun) mp.get("select x from UtilAkaun x where x.permohonan.id = '" + permohonan.getId() + "'");
			KewInvois invois = (KewInvois) mp.get("select x from KewInvois x where x.noInvois = '" + permohonan.getId() + "'");
			List<UtilJadualTempahan> listJadualTempahan = mp.list("select x from UtilJadualTempahan x where x.permohonan.id = '" + permohonan.getId() + "'");
			for (UtilJadualTempahan jadualTempahan : listJadualTempahan) {
				mp.remove(jadualTempahan);
			}
			if(invois != null) {
				mp.remove(invois);
			}
			if(akaun != null) {
				mp.remove(akaun);
			}
			permohonan.setStatusPermohonan("B");
			permohonan.setPemohonBatal((Users) mp.find(Users.class, "faizal"));
			permohonan.setTarikhBatal(new Date());
			permohonan.setMasaBatal(new Date());
			if ("R".equals(statusPermohonan)) {
				permohonan.setSebabBatal("TIADA KELULUSAN DALAM TEMPOH 14 HARI DARIPADA TARIKH PERMOHONAN DIBUAT.");
			}
			if ("Y".equals(statusPermohonan)) {
				permohonan.setSebabBatal("PEMBAYARAN TIDAK DILAKUKAN SELEPAS 7 HARI DARIPADA TARIKH KELULUSAN PERMOHONAN.");
			}			
		} catch (Exception ex) {
			ex.printStackTrace();
		}		
	}

	/**
	 * EMAIL TO GUEST - EMAIL PEMBERITAHUAN PERMOHONAN DIBATALKAN
	 * */
	public static void emailtoGuestTiadaKelulusan(UtilPermohonan r) {
		if (r.getPemohon() != null) {
			if ((!r.getPemohon().getId().equalsIgnoreCase("faizal") && !r.getPemohon().getId().equalsIgnoreCase("anon"))) {
				if (r.getPemohon().getEmel() != null) {
					if (!r.getPemohon().getEmel().equalsIgnoreCase("")) {
						UtilitiMailer.get().notifikasiPembatalanTempahanDewanTiadaKelulusan(r.getPemohon().getEmel(), r);
					}
				}
			}
		} else {
			myLogger.info("AutoPembatalanPermohonanDewanBelumBayarJob PEMOHON IS NULL : ID_PERMOHONAN = " + r.getId());
		}
	}
	
	public static void emailtoGuestTiadaBayaran(UtilPermohonan r) {
		if (r.getPemohon() != null) {
			if ((!r.getPemohon().getId().equalsIgnoreCase("faizal") && !r.getPemohon().getId().equalsIgnoreCase("anon"))) {
				if (r.getPemohon().getEmel() != null) {
					if (!r.getPemohon().getEmel().equalsIgnoreCase("")) {
						UtilitiMailer.get().notifikasiPembatalanTempahanDewan(r.getPemohon().getEmel(), r);
					}
				}
			}
		} else {
			myLogger.info("AutoPembatalanPermohonanDewanBelumBayarJob PEMOHON IS NULL : ID_PERMOHONAN = " + r.getId());
		}
	}

}
