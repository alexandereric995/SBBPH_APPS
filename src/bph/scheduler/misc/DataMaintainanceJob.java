package bph.scheduler.misc;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

import lebah.db.Db;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import portal.module.entity.Users;
import bph.entities.kewangan.KewBayaranResit;
import db.persistence.MyPersistence;

public class DataMaintainanceJob implements Job {
	
	static Logger myLogger = Logger.getLogger("DataMaintainanceJob");
	private MyPersistence mp;

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		myLogger.info("Executing DataMaintainanceJob on : " + new Date());
		System.out.println("Executing DataMaintainanceJob on : " + new Date());
		
		try {
			mp = new MyPersistence();
			mp.begin();
			
			//DOKUMEN SOKONGAN USERS
			clearDokumenSokonganUsers(mp);			
			
			//FLAG VOID RESIT
			setDefaultFlagVoidResit(mp);			
			
			mp.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		Db lebahDb = null;
		Connection conn = null;
		try {
			lebahDb = new Db();
			conn = lebahDb.getConnection();
			conn.setAutoCommit(false);
			
			//CLEAR ADMIN DATA
			clearAdminData(lebahDb);			
			
			conn.commit();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (lebahDb != null) { lebahDb.close(); }
		}
		
		myLogger.info("Finish DataMaintainanceJob on : " + new Date());
		System.out.println("Finish DataMaintainanceJob on : " + new Date());
	}

	private void clearAdminData(Db lebahDb) throws SQLException {
		String sql = "";
		Statement stmt = lebahDb.getStatement();	
		
		sql = "truncate user_module";			
		stmt.executeUpdate(sql);
		
		sql = "insert into user_module(tab_id, user_login, module_id, sequence, module_custom_title, column_number) VALUES"
				+ " ('1191904545724', 'admin', 'lebah_portal_RenameGroupModule', 5, 'module_rename_module_group', 0),"
				+ " ('1191904545724', 'admin', 'lebah_portal_PrepareUserModule', 2, 'module_roles_pages_template', 0)," 
				+ " ('1191904545724', 'admin', 'lebah_app_RoleModule', 1, 'module_roles_manager', 0)," 
				+ " ('1191904545724', 'admin', 'secondary_role', 3, 'module_user_secondary_roles', 0),"  
				+ " ('1191904545724', 'admin', 'lebah_app_UpdateUserProfileModule', 7, 'Tukar Kata Laluan', 0)," 
				+ " ('1227490920324', 'admin', 'mobile_helloworld', 1, 'Hello World', 0)," 
				+ " ('1227490920324', 'admin', 'lebah_app_ForumModule', 3, 'Discussion Forum', 1)," 
				+ " ('1365055354720', 'admin', 'lebah_util_FileManagerModule', 1, 'module_file_manager', 0),"  
				+ " ('1365055354720', 'admin', 'lebah.portal.AuditTrailRecordModule', 2, 'Audit Trail Record', 0),"  
				+ " ('1349792203061', 'admin', 'lebah_app_UpdateUserProfileModule', 2, 'module_tukar_kata_laluan', 0),"  
				+ " ('1191904545724', 'admin', 'portal.module.ModuleListRecordModule', 4, 'module_list_record', 0),"  
				+ " ('1191904545724', 'admin', 'portal.module.UsersListRecordModule', 6, 'Senarai Pengguna', 0),"  
				+ " ('1366075571122', 'anon', 'bph.portal.FrmPortalHome', 1, 'Portal Home', 0),"  
				+ " ('1416891521996', 'admin', 'lebah_app_UpdateUserProfileModule', 1, 'Tukar Kata Laluan', 0),"  
				+ " ('1416891521996', 'admin', 'portal.module.UserProfileModule', 2, 'Profil Pengguna', 0),"  
				+ " ('1416891521996', 'admin', 'eventCalendar', 3, 'Kalendar Aktiviti', 0),"  
				+ " ('1434425523992', 'admin', 'testing.ResetPassword', 1, 'Reset Kata Laluan', 0),"  
				+ " ('1434425523992', 'admin', 'testing.EmailTester', 2, 'Email Tester', 0),"  
				+ " ('1434425523992', 'admin', 'testing.PluginTutor', 3, 'Plugin Tutor', 0),"   
				+ " ('1434425523992', 'admin', 'testing.UpdateTransactionRPP', 4, 'Update Transaction RP', 0),"  
				+ " ('1434425523992', 'admin', 'testing.UpdateTransactionDewanGelanggang', 5, 'Update Transaction Dewan Gelanggang', 0),"  
				+ " ('1434007144755', 'admin', 'bph.dashboard.bil.FrmDashboard', 1, 'Dashboard', 0),"  
				+ " ('1428633155846', 'admin', 'bph.laporan.bil.SenaraiLaporanRecordModule', 1, 'Laporan Pengurusan Bil', 0)";			
		stmt.executeUpdate(sql);
		
		sql = "delete from role_module where user_role not in (select name from role)";
		stmt.executeUpdate(sql);
		
		sql = "delete from role_module where module_id not in (select module.module_id from module)";
		stmt.executeUpdate(sql);
		
		sql = "delete from tab_template where user_login not in (select name from role)";
		stmt.executeUpdate(sql);
		
		sql = "delete from user_module_template where user_login not in (select name from role)";
		stmt.executeUpdate(sql);
		
		sql = "delete from user_module_template where module_id not in (select module_id from module)";
		stmt.executeUpdate(sql);
		
		sql = "delete from user_role where role_id not in (select name from role)";
		stmt.executeUpdate(sql);
		
		sql = "delete from user_role where user_id not in (select user_login from users)";	
		stmt.executeUpdate(sql);		
	}

	private void clearDokumenSokonganUsers(MyPersistence mp) {
		List<Users> listUsers = mp.list("select x from Users x where x.dokumenSokongan = '' order by x.id asc");
		for (int i = 0; i < listUsers.size(); i++) {
			Users users = listUsers.get(i);
			users.setDokumenSokongan(null);
		}		
	}
	
	private void setDefaultFlagVoidResit(MyPersistence mp) {
		List<KewBayaranResit> listResit = mp.list("select x from KewBayaranResit x where (x.flagVoid = '' or x.flagVoid is null) order by x.id asc");
		for (int i = 0; i < listResit.size(); i++) {
			KewBayaranResit resit = listResit.get(i);
			resit.setFlagVoid("T");
		}
	}
}
