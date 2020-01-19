package bph.dashboard.kontrak;

import java.util.List;

import lebah.portal.action.LebahModule;
import bph.entities.kontrak.KontrakKontrak;
import db.persistence.MyPersistence;


public class FrmDashboard extends LebahModule {

	private static final long serialVersionUID = 1L;
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
		return "bph/dashboard/kontrak";
	}
	
	//------------ START KODING UNTUK NOTAFICATION PADA DASHBOARD ------------
	private void process() {
		try {
			mp = new MyPersistence();
			
			getBilDaftarKontrak(mp);
			getBilPengesahanKontrak(mp);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
	}
	
	private void getBilDaftarKontrak(MyPersistence mp) {
		List<KontrakKontrak> list = mp.list("select x from KontrakKontrak x where x.status in ('01')");
//		System.out.println(list.size());

		if (list.size() > 0) {
			context.put("bilDaftarKontrak", list.size());
		} else {
			context.remove("bilDaftarKontrak");
		}
	}
	
	private void getBilPengesahanKontrak(MyPersistence mp) {
		List<KontrakKontrak> list = mp.list("select x from KontrakKontrak x where x.status in ('02')");
//		System.out.println(list.size());
		
		if (list.size() > 0) {
			context.put("bilPengesahanKontrak", list.size());
		} else {
			context.remove("bilPengesahanKontrak");
		}
	}
	//---------- END KODING UNTUK NOTAFICATION PADA DASHBOARD ----------
}
