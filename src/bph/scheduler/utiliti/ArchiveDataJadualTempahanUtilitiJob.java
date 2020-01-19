package bph.scheduler.utiliti;

import java.sql.Connection;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import lebah.db.Db;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class ArchiveDataJadualTempahanUtilitiJob implements Job {
	
	static Logger myLogger = Logger.getLogger("ArchiveDataJadualTempahanUtilitiJob");

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		myLogger.info("Executing ArchiveDataJadualTempahanUtilitiJob on : " + new Date());
		System.out.println("Executing ArchiveDataJadualTempahanUtilitiJob on : " + new Date());
		
		String sql = "";	
		Db lebahDb = null;
		Connection conn = null;
		try {
			lebahDb = new Db();
			conn = lebahDb.getConnection();
			conn.setAutoCommit(false);
			Statement stmt = lebahDb.getStatement();	
			
			Calendar cal = GregorianCalendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.DATE, -20);
			int tarikh = cal.get(Calendar.DATE);
			int bulan = cal.get(Calendar.MONTH) + 1;
			int tahun = cal.get(Calendar.YEAR);
			String strDate = new DecimalFormat("00").format(tarikh) + "-" + new DecimalFormat("00").format(bulan) + "-" + new DecimalFormat("0000").format(tahun);
			
			sql = "INSERT INTO util_jadual_tempahan_archive"
					+ " SELECT * FROM util_jadual_tempahan WHERE tarikh_tempahan <= STR_TO_DATE('" + strDate + "', '%d-%m-%Y')";			
			stmt.executeUpdate(sql);
			
			sql = "DELETE FROM util_jadual_tempahan WHERE tarikh_tempahan <= STR_TO_DATE('" + strDate + "', '%d-%m-%Y')";			
			stmt.executeUpdate(sql);
			
			conn.commit();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (lebahDb != null) { lebahDb.close(); }
		}
		
		myLogger.info("Finish ArchiveDataJadualTempahanUtilitiJob on : " + new Date());
		System.out.println("Finish ArchiveDataJadualTempahanUtilitiJob on : " + new Date());
	}
}
