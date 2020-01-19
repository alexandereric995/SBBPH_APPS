package bph.scheduler.kew;

import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import db.persistence.MyPersistence;

public class ResetFlagQueueJob implements Job {
	
	static Logger myLogger = Logger.getLogger("ResetFlagQueueJob");
	private MyPersistence mp;

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		myLogger.info("Executing ResetFlagQueueJob on : " + new Date());
		System.out.println("Executing ResetFlagQueueJob on : " + new Date());
		
		try {
			mp = new MyPersistence();
			mp.begin();
			
			//KEW_INVOIS
			String sql = "update KewInvois x set x.flagQueue = 'T'";
			mp.executeUpdate(sql);
			
			//KEW_INVOIS
			sql = "delete from KewInvois x where x.flagBayar = 'T' and x.jenisBayaran.id = '08'";
			mp.executeUpdate(sql);
			
			//KEW_DEPOSIT
			sql = "update KewDeposit x set x.flagQueue = 'T'";
			mp.executeUpdate(sql);	
			
			//KEW_TEMP_BAYAR
			sql = "delete from KewTempBayar x";
			mp.executeUpdate(sql);		
			
			//KEW_TEMP_IN_QUEUE
			sql = "delete from KewTempInQueue x";
			mp.executeUpdate(sql);	
			
			mp.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		myLogger.info("Finish ResetFlagQueueJob on : " + new Date());
		System.out.println("Finish ResetFlagQueueJob on : " + new Date());
	}
}
