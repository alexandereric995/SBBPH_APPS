package bph.modules.rpp.tetapan;

import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import lebah.db.Db;
import lebah.portal.AjaxBasedModule;
import lebah.template.DbPersistence;

import org.apache.log4j.Logger;

import bph.entities.kod.GredPerkhidmatan;
import bph.entities.rpp.RppTempohBayaranByGred;
import bph.utils.Util;

public class TempohBayaranByGredController extends AjaxBasedModule {
	
	private static final long serialVersionUID = 1L;
	private static String path = "bph/modules/rpp/tetapan/tempohBayaranByGred/";
	static Logger myLogger = Logger.getLogger(TempohBayaranByGredController.class);
	protected DbPersistence db;
	
	@SuppressWarnings("rawtypes")
	@Override
	public String doTemplate2() throws Exception {
		myLogger.info(".:TempohBayaranByGredController:.");

		String vm = "";
		String pageIndex = "index.vm";
		String command = getParam("command");
		myLogger.info("command : "+command);
		
		List list = listTempohBayaranByGred();
		
		if(command.equalsIgnoreCase("saveRecord")){
			db = new DbPersistence();
			db.begin();
			for (int i = 0;i < list.size(); i++){
				Hashtable hs = (Hashtable)list.get(i);
				String hsid = (String)hs.get("id");
				
				int days = getParamAsInteger("tempohBayaran"+hsid);
				//if(days==0){days=7;}//set default
					
				RppTempohBayaranByGred tb = (RppTempohBayaranByGred) db.get("select x from RppTempohBayaranByGred x where x.gredPerkhidmatan.id = '"+hsid+"' ");
				GredPerkhidmatan gp = db.find(GredPerkhidmatan.class, hsid);
				if(tb==null){
					tb = new RppTempohBayaranByGred();
				}
				tb.setGredPerkhidmatan(gp);
				tb.setTempohBayaran(days);
				db.persist(tb);
			}
			try{
				db.commit();
				list = listTempohBayaranByGred();
			}catch(Exception ex){
				System.out.println("error saving : "+ex.getMessage());
			}
		}
		
		context.put("listTempohBayaranByGred", list);
		context.put("util", new Util());
		if(vm==""){vm = path+pageIndex;}
		context.put("templateDir",path);
		return vm;
	}
	
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List listTempohBayaranByGred(){
		
		Vector list = new Vector();
		Hashtable h = null;
		Db db1 = null;
		
		try {
			db1 = new Db();
			String sql = "select a.id, a.keterangan, b.tempoh_bayaran "+
						 " from ruj_gred_perkhidmatan a left join rpp_tempoh_bayaran_gred b on a.id = b.id_gred_perkhidmatan "+
						 " where a.flag_aktif = 'Y' ";
			
			ResultSet rs = db1.getStatement().executeQuery(sql);			
			while (rs.next()) {
				h = new Hashtable();
				h.put("id", rs.getString("id")== null?"":rs.getString("id"));
				h.put("keterangan", rs.getString("keterangan")== null?"":rs.getString("keterangan"));
				h.put("tempohBayaran", rs.getString("tempoh_bayaran")== null?"":rs.getString("tempoh_bayaran"));
				list.addElement(h);
			}	
			
		}catch(Exception e){
			System.out.println("error listTempohBayaranByGred() : "+e.getMessage());
		}finally { 
			if ( db1 != null ) db1.close();
		}
		
		return list;
	}
	
}



