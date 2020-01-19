package bph.dashboard.naziran;

import java.util.List;

import lebah.portal.action.LebahModule;
import lebah.template.DbPersistence;
import bph.entities.qtr.KuaPenghuni;
import bph.entities.utk.UtkKesalahan;
import bph.entities.utk.UtkOperasi;
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
		return "bph/dashboard/naziran";
	}
	
	
	//------------ START KODING UNTUK NOTAFICATION PADA DASHBOARD ------------
	private void process() {
		try {
				mp = new MyPersistence();
			
			getSenaraiOperasiBerjadual(mp);
			getSenaraiOperasiTidakBerjadual(mp);
//			getSenaraiPenghuni(mp);
			getSenaraiPelanggaranSyarat(mp);
//			getSenaraiPengurusanPembersihan(mp);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
	}
	
	private void getSenaraiOperasiBerjadual(MyPersistence mp) {

		try {
			mp = new MyPersistence();
			List<UtkOperasi> list = mp.list("select x from UtkOperasi x where x.flagOperasi = '1'");
//					System.out.println("print ==== " + list.size());
			if (list.size() > 0) {
				context.put("senaraiOperasiBerjadual", list.size());
			} else {
				context.remove("senaraiOperasiBerjadual");
			}
						
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
	}
	
	private void getSenaraiOperasiTidakBerjadual(MyPersistence mp) {

		try {
			mp = new MyPersistence();
			List<UtkOperasi> list = mp.list("select x from UtkOperasi x where x.flagOperasi = '2'");
//					System.out.println("print ==== " + list.size());
			if (list.size() > 0) {
				context.put("senaraiOperasiTidakBerjadual", list.size());
			} else {
				context.remove("senaraiOperasiTidakBerjadual");
			}
						
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
	}
		

	private void getSenaraiPenghuni(MyPersistence mp) {

		try {
			mp = new MyPersistence();
			List<KuaPenghuni> list = mp.list("select x from KuaPenghuni x where x.tarikhKeluar is null");
//					System.out.println("print ==== " + list.size());
			if (list.size() > 0) {
				context.put("senaraiPenghuni", list.size());
			} else {
				context.remove("senaraiPenghuni");
			}
						
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
	}
	
	private void getSenaraiPelanggaranSyarat(MyPersistence mp) {
		// TODO Auto-generated method stub
		try {
			mp = new MyPersistence();
			List<UtkKesalahan> list = mp.list("select x from UtkKesalahan x where x.jenisOperasi.id = 'PS'");
//					System.out.println("print ==== " + list.size());
			if (list.size() > 0) {
				context.put("senaraiPelanggaranSyarat", list.size());
			} else {
				context.remove("senaraiPelanggaranSyarat");
			}
						
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
	}
	
//	private void getSenaraiPengurusanPembersihan(MyPersistence mp) {
//		// TODO Auto-generated method stub
//		try {
//			mp = new MyPersistence();
//			List<UtkNaziranKebersihan> list = mp.list("select x from UtkNaziranKebersihan x where x.id is no null");
//			if (list.size() > 0) {
//				context.put("senaraiPengurusanPembersihan", list.size());
//			} else {
//				context.remove("senaraiPengurusanPembersihan");
//			}
//						
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		} finally {
//			if (mp != null) { mp.close(); }
//		}
//	}
	
	
}
