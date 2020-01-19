package bph.modules.kewangan.deposit.lejar.master;


import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lebah.db.Db;
import lebah.portal.AjaxBasedModule;
import lebah.template.DbPersistence;

import org.apache.log4j.Logger;

import bph.entities.kew.KewTuntutanDeposit;
import bph.entities.kewangan.KewDeposit;
import bph.utils.DataUtil;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class MasterLejarRK extends AjaxBasedModule {

	private static final long serialVersionUID = 1L;
	private static String path = "bph/modules/kewangan/deposit/lejar/master/ruangKomersil/";
	static Logger myLogger = Logger.getLogger(MasterLejarRK.class);
	protected DbPersistence db;
	private DataUtil dataUtil;
	private MyPersistence mp;
	
	@Override
	public String doTemplate2() throws Exception {

		db = new DbPersistence();
		mp = new MyPersistence();
		dataUtil = DataUtil.getInstance(db);
		String userIdLogin = (String) request.getSession().getAttribute("_portal_login");
		String vm = "";
		String pageIndex = "main.vm";
		String command = getParam("command");
		context.put("util", new Util());
		
		clearcontext();
		
		List<KewDeposit> listdep = null;
		List<Object> listdep1 = null;
		
		if(command.equalsIgnoreCase("xxx")){
			//vm = path + "/belumWarta.vm";
		}
		
		String tarikhBayar = getParam("tarikhBayar");
		
		/**Get List Pendeposit*/
		listdep = dbListPendeposit();
		
		/**Get List Pendeposit1*/
		listdep1 = dbListPendeposit1(tarikhBayar);
		
		context.put("listdep", listdep1);
		
		if (command.equalsIgnoreCase("carianPendeposit")) {
			try {
				mp = new MyPersistence();
				listdep1 = dbListPendeposit1(tarikhBayar);
				context.put("listdep", listdep1);
			} finally {
				if (mp != null) { mp.close(); }
			}
			vm = path + "/index.vm";

		}
		
		if(vm==""){vm = path+pageIndex;}
		context.put("templateDir",path);
		return vm;
	}
	
	private void clearcontext(){
		context.remove("");
	}

	
	/**
	 * Get senarai pendeposit
	 * */
	private List<KewDeposit> dbListPendeposit() {
		
		ArrayList<KewDeposit> list = new ArrayList<KewDeposit>();
		Db db1 = null;
		
		try {
			db1 = new Db();
			String sql = "select a.id from kew_deposit a where a.id_kod_hasil = '79503' "+
						 " and a.id_jenis_bayaran = '04' "+
						 " and ifnull(a.flag_warta,'T') <> 'Y'";
						 //" and ifnull(a.flag_pulang_deposit,'T') = 'T' ";
			
			ResultSet rs = db1.getStatement().executeQuery(sql);			
			while (rs.next()) {
				KewDeposit kd = db.find(KewDeposit.class, rs.getString("id"));
				list.add(kd);
			}	
			
		}catch(Exception e){
			myLogger.error("error dbListPendeposit : "+e.getMessage());
			e.printStackTrace();
		}finally { 
			if ( db1 != null ) db1.close();
		}
		return list;
	}
	
	private List<Object> dbListPendeposit1(String tarikhBayar) throws Exception {
		
		ArrayList<Object> list = new ArrayList<Object>();
		Db db1 = null;
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat nfBayaran = new SimpleDateFormat("yyyy-MM-dd"); 
	    Date startDate = null;
	    String newTarikhBayaran ="";

	    if(tarikhBayar!=""){
			startDate = df.parse(tarikhBayar);
			newTarikhBayaran = nfBayaran.format(startDate);
		}
		try {
			db1 = new Db();
			String sql = " select a.id,'' as id_tuntutan, a.tarikh_bayaran, '1' as flag from kew_deposit a" +
//						" join kew_resit_senarai_invois b on a.id = b.id_deposit" +
//						" join kew_bayaran_resit c on b.id_bayaran_resit = c.id" +
						" where a.id_kod_hasil = '79503'" +
						" and a.id_jenis_bayaran = '04'" +
						" and ifnull(a.flag_warta,'T') <> 'Y'" +
						" and ifnull(a.flag_bayar,'T') = 'Y'";
//						" and ifnull(c.flag_void,'T') = 'T'" +
						
						if(!("").equalsIgnoreCase(tarikhBayar) || tarikhBayar.length() > 0){
							sql = sql +" and a.tarikh_bayaran <= '" + newTarikhBayaran + "'";
						}
					sql = sql + " union" +
						" select '' as id, b.id as id_tuntutan, b.tarikh_baucer as tarikh_bayaran, '2' as flag from kew_deposit a" +
						" join kew_tuntutan_deposit b on a.id_tuntutan_deposit = b.id and a.id = b.id_kew_deposit" +
						" where a.id_kod_hasil = '79503'" +
						" and a.id_jenis_bayaran = '04'" +
						" and ifnull(a.flag_warta,'T') <> 'Y'" +
						" and ifnull(a.flag_bayar,'T') = 'Y'" +
						" and ifnull(a.flag_pulang_deposit,'T') = 'Y'";
					
						if(!("").equalsIgnoreCase(tarikhBayar) || tarikhBayar.length() > 0){
							sql = sql +" and (a.tarikh_bayaran <= '" + newTarikhBayaran + "'" +
								" or b.tarikh_baucer <= '" + newTarikhBayaran + "')";
						}
						
					sql = sql + " order by tarikh_bayaran asc";
			ResultSet rs = db1.getStatement().executeQuery(sql);			
			while (rs.next()) {
				
				if("1".equals(rs.getString("flag"))){
					KewDeposit kd = (KewDeposit) mp.find(KewDeposit.class, rs.getString("id"));
				list.add(kd);
				}else if("2".equals(rs.getString("flag"))){
					KewTuntutanDeposit tp = (KewTuntutanDeposit) mp.find(KewTuntutanDeposit.class, rs.getString("id_tuntutan"));
					if(tp != null){
						list.add(tp);
					}	
				}
			}	
			
		}catch(Exception e){
			myLogger.error("error dbListPendeposit : "+e.getMessage());
			e.printStackTrace();
		}finally { 
			if ( db1 != null ) db1.close();
		}
		return list;
	}
}
