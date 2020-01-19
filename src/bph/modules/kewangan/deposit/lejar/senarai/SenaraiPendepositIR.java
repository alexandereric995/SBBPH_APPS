package bph.modules.kewangan.deposit.lejar.senarai;


import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lebah.db.Db;
import lebah.portal.AjaxBasedModule;
import lebah.template.DbPersistence;

import org.apache.log4j.Logger;

import bph.entities.kewangan.KewDeposit;
import bph.utils.DataUtil;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class SenaraiPendepositIR extends AjaxBasedModule {

	private static final long serialVersionUID = 1L;
	private static String path = "bph/modules/kewangan/deposit/lejar/senarai/rumahPeranginan/";
	static Logger myLogger = Logger.getLogger(SenaraiPendepositIR.class);
	protected DbPersistence db;
	private DataUtil dataUtil;
	private MyPersistence mp;
	@Override
	public String doTemplate2() throws Exception {

		mp = new MyPersistence();
		db = new DbPersistence();
		dataUtil = DataUtil.getInstance(db);
		String userIdLogin = (String) request.getSession().getAttribute("_portal_login");
		String vm = "";
		String pageIndex = "main.vm";
		String command = getParam("command");
		context.put("util", new Util());
		
		clearcontext();
		
		List<KewDeposit> listdep = null;
		
		if(command.equalsIgnoreCase("xxx")){
			//vm = path + "/belumWarta.vm";
		}
		
		String tarikhDari = getParam("tarikhDari");
		String tarikhBayar = getParam("tarikhBayar");
		
		/**Get List Pendeposit*/
		listdep = dbListPendeposit(tarikhBayar);
		context.put("listdep", listdep);
		for (int i=0; i < listdep.size(); i++)
		{
			listdep.get(i).getNoResit();
		}
		context.put("listdep", listdep);
		
		if (command.equalsIgnoreCase("carianPendeposit")) {
			try {
				mp = new MyPersistence();
				if(tarikhDari !=null && tarikhBayar != null){
					listdep = dbListPendepositBetween(tarikhDari,tarikhBayar);
				}else{
					listdep = dbListPendeposit(tarikhBayar);
				}
				context.put("listdep", listdep);
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
	 * @throws Exception 
	 * */
	private List<KewDeposit> dbListPendeposit(String tarikhBayar) throws Exception {
		
		ArrayList<KewDeposit> list = new ArrayList<KewDeposit>();
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
			String sql = "select a.id from kew_deposit a where a.id_kod_hasil = '72311' "+
					 	 " and a.id_jenis_bayaran = '02' "+
					 	 " and ifnull(a.flag_warta,'T') <> 'Y'  "+
					 	 " and ifnull(a.flag_pulang_deposit,'T') = 'T' and ifnull(a.flag_bayar,'T') = 'Y'";
						
						if(!("").equalsIgnoreCase(tarikhBayar) || tarikhBayar.length() > 0){
							sql = sql +" and a.tarikh_bayaran <= '" + newTarikhBayaran + "'";
						}
						sql = sql +" order by a.tarikh_bayaran desc LIMIT 100";
						
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
	
	private List<KewDeposit> dbListPendepositBetween(String tarikhMula,String tarikhTamat) throws Exception {
		
		ArrayList<KewDeposit> list = new ArrayList<KewDeposit>();
		Db db1 = null;
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat nfBayaran = new SimpleDateFormat("yyyy-MM-dd"); 
		Date startDate = null;
	    Date endDate = null;
	    String firstTarikhBayaran ="";
	    String secondTarikhBayaran ="";
 
	    if(tarikhTamat!=""){
	    	startDate = df.parse(tarikhMula);
			endDate = df.parse(tarikhTamat);
			firstTarikhBayaran = nfBayaran.format(startDate);
			secondTarikhBayaran = nfBayaran.format(endDate);
		}
	    
		try {
			db1 = new Db();
			String sql = "select a.id from kew_deposit a where a.id_kod_hasil = '72311' "+
					 	 " and a.id_jenis_bayaran = '02' "+
					 	 " and ifnull(a.flag_warta,'T') <> 'Y'  "+
					 	 " and ifnull(a.flag_pulang_deposit,'T') = 'T' and ifnull(a.flag_bayar,'T') = 'Y'";
						
						if(!("").equalsIgnoreCase(tarikhTamat) || tarikhTamat.length() > 0){
							sql = sql +" and a.tarikh_bayaran >= '" + firstTarikhBayaran + "' and a.tarikh_bayaran <= '" + secondTarikhBayaran +"'";
						}
						sql = sql +" order by a.tarikh_bayaran desc LIMIT 100";
						
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
	
}
