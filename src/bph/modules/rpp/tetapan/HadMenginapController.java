package bph.modules.rpp.tetapan;

import lebah.portal.AjaxBasedModule;
import lebah.template.DbPersistence;

import org.apache.log4j.Logger;

import bph.entities.rpp.RppTetapanHadMenginap;
import bph.utils.Util;

public class HadMenginapController extends AjaxBasedModule {
	
	private static final long serialVersionUID = 1L;
	private static String path = "bph/modules/rpp/tetapan/hadMenginap/";
	static Logger myLogger = Logger.getLogger(HadMenginapController.class);
	protected DbPersistence db;
	
	@Override
	public String doTemplate2() throws Exception {
		myLogger.info(".:HadMenginapController:.");

		db = new DbPersistence();
		String vm = "";
		String pageIndex = "index.vm";
		String command = getParam("command");
		myLogger.info("command : "+command);
		
		RppTetapanHadMenginap obj = (RppTetapanHadMenginap) db.get("select x from RppTetapanHadMenginap x");

		if(command.equalsIgnoreCase("saveRecord")){
			if(obj==null){obj = new RppTetapanHadMenginap();}

			db.begin();
			obj.setBilHari(getParamAsInteger("bilHari"));
			obj.setCatatan(getParam("catatan"));
			db.persist(obj);

			try{
				db.commit();
				obj = (RppTetapanHadMenginap) db.get("select x from RppTetapanHadMenginap x");
			}catch(Exception ex){
				System.out.println("error saving : "+ex.getMessage());
			}
		}
		
		context.put("r", obj);
		context.put("util", new Util());
		if(vm==""){vm = path+pageIndex;}
		context.put("templateDir",path);
		return vm;
	}
	
}



