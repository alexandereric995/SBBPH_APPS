package bph.scheduler.portal;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import bph.entities.portal.CmsInformasi;
import bph.entities.portal.CmsSubInformasi;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class InformasiPortalJob implements Job {
	
	static Logger myLogger = Logger.getLogger("InformasiPortalJob");
	private MyPersistence mp;

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		myLogger.info("Executing InformasiPortalJob on : " + new Date());
		System.out.println("Executing InformasiPortalJob on : " + new Date());
		
		try {
			mp = new MyPersistence();
			mp.begin();
			//SUBINFORMASI
			List<CmsSubInformasi> listSubInformasi = mp.list("select x from CmsSubInformasi x where x.flagAktif = 'Y' order by x.id asc");
			for (int i = 0; i < listSubInformasi.size(); i++) {
				CmsSubInformasi subInformasi = listSubInformasi.get(i);
				if (subInformasi != null) {
					
					//NOTIFIKASI FLAG BARU
					if (subInformasi.getTarikhKemaskini() != null) {
						Calendar currentDate = Calendar.getInstance();
						currentDate.setTime(new Date());
						Calendar cal = Calendar.getInstance();
						cal.setTime(subInformasi.getTarikhKemaskini());
						if (Util.daysBetween(cal.getTime(),currentDate.getTime()) > 30) {
							subInformasi.setFlagBaru("T");
						}
					} else if (subInformasi.getTarikhMasuk() != null) {
						Calendar currentDate = Calendar.getInstance();
						currentDate.setTime(new Date());
						Calendar cal = Calendar.getInstance();
						cal.setTime(subInformasi.getTarikhMasuk());
						if (Util.daysBetween(cal.getTime(),currentDate.getTime()) > 30) {
							subInformasi.setFlagBaru("T");
						}
					}
					
					//SEMAKAN TARIKH LUPUT
					if (subInformasi.getTarikhLuput() != null) {
						Calendar currentDate = Calendar.getInstance();
						currentDate.setTime(new Date());
						Calendar tarikhLuput = Calendar.getInstance();
						tarikhLuput.setTime(subInformasi.getTarikhLuput());
						tarikhLuput.add(Calendar.DATE, 1);
						if (currentDate.getTime().after(tarikhLuput.getTime())) {
							subInformasi.setFlagAktif("T");
						}
					}
				}
			}
			mp.commit();
			
			mp.begin();
			//INFORMASI - NOTIFIKASI FLAG BARU
			List<CmsInformasi> listInformasi = mp.list("select x from CmsInformasi x where x.flagAktif = 'Y' order by x.id asc");
			for (int i = 0; i < listInformasi.size(); i++) {
				CmsInformasi informasi = listInformasi.get(i);
				if (informasi != null) {
					if (informasi.getTarikhKemaskini() != null) {
						Calendar currentDate = Calendar.getInstance();
						currentDate.setTime(new Date());
						Calendar cal = Calendar.getInstance();
						cal.setTime(informasi.getTarikhKemaskini());
						if (Util.daysBetween(cal.getTime(),currentDate.getTime()) > 30) {
							informasi.setFlagBaru("T");
						}
						listSubInformasi = mp.list("select x from CmsSubInformasi x where x.flagAktif = 'Y' and x.informasi.id = '" + informasi.getId() + "'");
						if (listSubInformasi.size() == 0) {
							informasi.setFlagBaru("T");
						}
					} else if (informasi.getTarikhMasuk() != null) {
						Calendar currentDate = Calendar.getInstance();
						currentDate.setTime(new Date());
						Calendar cal = Calendar.getInstance();
						cal.setTime(informasi.getTarikhMasuk());
						if (Util.daysBetween(cal.getTime(),currentDate.getTime()) > 30) {
							informasi.setFlagBaru("T");
						}
						listSubInformasi = mp.list("select x from CmsSubInformasi x where x.flagAktif = 'Y' and x.informasi.id = '" + informasi.getId() + "'");
						if (listSubInformasi.size() == 0) {
							informasi.setFlagBaru("T");
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
		myLogger.info("Finish InformasiPortalJob on : " + new Date());
		System.out.println("Finish InformasiPortalJob on : " + new Date());
	}
}
