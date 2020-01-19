package bph.modules.qtr;

import java.util.List;

import lebah.portal.action.LebahModule;
import lebah.template.DbPersistence;

import org.apache.log4j.Logger;

import bph.entities.portal.Giliran;
import db.persistence.MyPersistence;

public class FrmKuaMenungguSubRecord extends LebahModule {
	private static final long serialVersionUID = 1779932252980602112L;
	static Logger myLogger = Logger.getLogger("bph/modules/qtr/FrmKuaMenungguSubRecord");
	
	@SuppressWarnings("unused")
	private DbPersistence db = new DbPersistence();
	private String userId = "";
	private MyPersistence mp;

	@Override
	public String start() {
		userId = (String) request.getSession().getAttribute("_portal_login");
		try {
			mp = new MyPersistence();
			getNoGiliran(mp);
		} catch (Exception e) {
			System.out.println("Error start : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		//return getPath() + "/start.vm";
		return getPath() + "/start_manual.vm";
	}

	public String getPath() {
		return "bph/modules/qtr/menunggu/sub_page";
	}

	@SuppressWarnings("unchecked")
//	***UNTUK DATA MENUNGGU SECARA MANUAL***
	public String getNoGiliran(MyPersistence mp) throws Exception {
		try {
			mp = new MyPersistence();
			userId = (String) request.getSession().getAttribute("_portal_login");
			List<Giliran> giliran = mp.list("SELECT x FROM Giliran x WHERE x.noKP = '" + userId+ "'");
			if (giliran.size() > 0) {
				if (giliran.get(0) != null) {
					if (giliran.get(0).getKelasKuarters().equalsIgnoreCase("A")){
						context.put("dontPapar", false);
					}else{
					context.put("dontPapar", false);
					}
					context.put("giliran", giliran);
					context.put("rekod", true);
				} else {
					context.put("rekod", false);
				}
			}	
		} catch (Exception e) {
			System.out.println("Error getNoGiliran : " + e.getMessage());
		}
		return getPath() + "/start_manual.vm";
	}
	
//	public String getNoGiliran(MyPersistence mp) throws Exception {
//		try {
//			userId = (String) request.getSession().getAttribute("_portal_login");
//			List<KuaAgihan> giliran = mp.list("SELECT x FROM KuaAgihan x WHERE x.pemohon.id = '" + userId+ "'");
//			if (giliran.size() > 0) {
//				for (KuaAgihan rekod : giliran) {
//					if (rekod.getKelasKuarters() != null) {
//						if (rekod.getKelasKuarters().equalsIgnoreCase("A")){
//							context.put("dontPapar", true);
//						}
//						context.put("giliran", giliran);
//						context.put("rekod", true);
//					} else {
//						context.put("rekod", false);
//					}
//				}
//			}
//		} catch (Exception e) {
//			System.out.println("Error getNoGiliran : " + e.getMessage());
//		}
//		return getPath() + "/start.vm";
//	}
}
