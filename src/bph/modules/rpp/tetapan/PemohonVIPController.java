package bph.modules.rpp.tetapan;

import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import lebah.db.Db;
import lebah.portal.AjaxBasedModule;
import lebah.template.DbPersistence;

import org.apache.log4j.Logger;

import portal.module.entity.Users;
import bph.entities.rpp.RppPemohonVIP;
import bph.utils.Util;

public class PemohonVIPController extends AjaxBasedModule {
	
	private static final long serialVersionUID = 1L;
	private static String path = "bph/modules/rpp/tetapan/pemohonVIP/";
	static Logger myLogger = Logger.getLogger(PemohonVIPController.class);
	protected DbPersistence db;
	@SuppressWarnings({ "rawtypes" })
	@Override
	public String doTemplate2() throws Exception {
		myLogger.info(".:PemohonVIPController:.");
		db = new DbPersistence();
		
		String vm = "";
		String pageIndex = "index.vm";
		String command = getParam("command");
		myLogger.info("command : "+command);
		
		List list = listPemohonDanStatusVIP();
		List<RppPemohonVIP> listVip = listRegisteredVip(db);
		
		if(command.equalsIgnoreCase("saveRecord")){
			try{
				boolean addVIP = false;
				Users pengguna = db.find(Users.class, getParam("idpengguna"));
				RppPemohonVIP vip = (RppPemohonVIP) db.get("select x from RppPemohonVIP x where x.pemohon.id = '"+pengguna.getId()+"' ");
				if(vip==null){
					vip = new RppPemohonVIP();
					addVIP = true;
				}
				
				db.begin();
				vip.setFlagVip("Y");
				vip.setPemohon(pengguna);
				if (addVIP) {
					db.persist(vip);
				}				
				db.commit();
				context.put("error_flag","" );
				list = listPemohonDanStatusVIP();
				listVip = listRegisteredVip(db);
				
			}catch(Exception ex){
				System.out.println("error saveRecord : "+ex.getMessage());
			}
			vm = path + "/main.vm";
		}
		
		else if(command.equalsIgnoreCase("deleteRecord")){
			System.out.println("deleteRecord...");
			try{
				
				RppPemohonVIP vip = db.find(RppPemohonVIP.class, getParam("idvip"));
				db.begin();
				db.remove(vip);
				db.commit();
				context.put("error_flag","record_delete_success" );
				list = listPemohonDanStatusVIP();
				listVip = listRegisteredVip(db);
				
			}catch(Exception ex){
				System.out.println("error deleteRecord : "+ex.getMessage());
			}
			vm = path + "/main.vm";
		}
		
		context.put("listPemohonDanStatusVIP", list);
		context.put("listVip", listVip);
		context.put("util", new Util());
		if(vm==""){vm = path+pageIndex;}
		context.put("templateDir",path);
		return vm;
	}
	
	@SuppressWarnings("unchecked")
	public List<RppPemohonVIP> listRegisteredVip(DbPersistence db){
		List<RppPemohonVIP> listVip = db.list("select x from RppPemohonVIP x");
		return listVip;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List listPemohonDanStatusVIP(){
		
		Vector list = new Vector();
		Hashtable h = null;
		Db db1 = null;
		
		try {
			db1 = new Db();
			String sql = " select distinct u.user_login, u.user_name, u.no_kp, ra.keterangan as agensi, km.keterangan as kementerian, "+ 
					" kp.keterangan as kelas_perkhidmatan, gp.keterangan as gred_perkhidmatan, vip.flag_vip "+
					" FROM users u"+
					" LEFT JOIN user_role ur ON u.user_login = ur.user_id "+
					" LEFT JOIN ruj_agensi ra ON u.id_agensi = ra.id "+
					" LEFT JOIN ruj_kementerian km ON ra.id_kementerian = km.id "+
					" LEFT JOIN ruj_kelas_perkhidmatan kp ON u.id_kelas_perkhidmatan = kp.id "+
					" LEFT JOIN ruj_gred_perkhidmatan gp ON u.id_gred_perkhidmatan = gp.id "+
					" LEFT JOIN rpp_pemohon_vip vip ON u.user_login = vip.id_pemohon "+
					" WHERE CAST(u.id_gred_perkhidmatan AS UNSIGNED) >= '55' AND CAST(u.id_gred_perkhidmatan AS UNSIGNED) <= '63' "+
			
					//Exclude pemohon vip
					" AND u.user_login not in (select id_pemohon from rpp_pemohon_vip) ";
					
			sql += " order by u.user_name asc ";
			
			ResultSet rs = db1.getStatement().executeQuery(sql);			
			while (rs.next()) {
				h = new Hashtable();
				h.put("userLogin", rs.getString("user_login")== null?"":rs.getString("user_login"));
				h.put("userName", rs.getString("user_name")== null?"":rs.getString("user_name"));
				h.put("noKp", rs.getString("no_kp")== null?"":rs.getString("no_kp"));
				h.put("agensi", rs.getString("agensi")== null?"":rs.getString("agensi"));
				h.put("kementerian", rs.getString("kementerian")== null?"":rs.getString("kementerian"));
				h.put("kelasPerkhidmatan", rs.getString("kelas_perkhidmatan")== null?"":rs.getString("kelas_perkhidmatan"));
				h.put("gredPerkhidmatan", rs.getString("gred_perkhidmatan")== null?"":rs.getString("gred_perkhidmatan"));
				h.put("flagVip", rs.getString("flag_vip")== null?"":rs.getString("flag_vip"));
				list.addElement(h);
			}	
			
		}catch(Exception e){
			System.out.println("error listPemohonDanStatusVIP() : "+e.getMessage());
		}finally { 
			if ( db1 != null ) db1.close();
		}
		
		return list;
	}
	
}



