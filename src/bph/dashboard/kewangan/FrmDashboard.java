package bph.dashboard.kewangan;

import java.util.List;

import lebah.portal.action.LebahModule;
import lebah.template.DbPersistence;
import bph.entities.bil.BayaranBil;
import bph.utils.DataUtil;
import db.persistence.MyPersistence;

public class FrmDashboard extends LebahModule{

	private static final long serialVersionUID = 1L;
	private DbPersistence db = new DbPersistence();
	private DataUtil dataUtil = DataUtil.getInstance(db);
	private MyPersistence mp;

	@Override
	public String start() {
		
		String portal_role  = (String)request.getSession().getAttribute("_portal_role");
		context.put("portal_role", portal_role);
		
		lebah.util.Util lebahUtil = new lebah.util.Util();
		context.put("lebahUtil", lebahUtil);
		
		process();
		
		context.put("path", getPath());
		return getPath() + "/start.vm";
	}
	
	private String getPath() {
		return "bph/dashboard/kewangan";
	}
	
	//------------ START KODING UNTUK NOTAFICATION PADA DASHBOARD ------------
	private void process() {
		try {
			mp = new MyPersistence();
			
			getRekodBilTertunggak(mp);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
	}
	
	private void getRekodBilTertunggak(MyPersistence mp) {

		try {
			mp = new MyPersistence();
			List<BayaranBil> list = mp.list("select x from BayaranBil x where x.statusBayaran in ('T')");
			if (list.size() > 0) {
				context.put("rekodBilTertunggak", list.size());
			} else {
				context.remove("rekodBilTertunggak");
			}
						
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
	}	
	//---------- END KODING UNTUK NOTAFICATION PADA DASHBOARD ----------
	
}
	
