package bph.dashboard.rk;

import java.util.List;

import lebah.portal.action.LebahModule;
import bph.entities.rk.RkFail;
import bph.entities.rk.RkPermohonan;
import bph.entities.rk.RkRuangKomersil;
import db.persistence.MyPersistence;

public class FrmDashboard extends LebahModule {

	private static final long serialVersionUID = 1L;
	private MyPersistence mp;

	private String getPath() {
		return "bph/dashboard/rk";
	}

	@Override
	public String start() {

		String portal_role = (String) request.getSession().getAttribute("_portal_role");
		context.put("portal_role", portal_role);

		process(portal_role);

		lebah.util.Util lebahUtil = new lebah.util.Util();
		context.put("lebahUtil", lebahUtil);
		context.put("path", getPath());
		return getPath() + "/start.vm";
	}

	private void process(String role) {
		try {
			mp = new MyPersistence();

			getBilRuangKomersil(role, mp);
			getBilPermohonanPenyewaan(role, mp);
			getBilAkaunPenyewa(role, mp);		

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
	}
	
	private void getBilRuangKomersil(String role, MyPersistence mp) {
		List<RkRuangKomersil> list = null;
		
		if ("(RK) Penyedia".equals(role)) {
			list = mp.list("select x from RkRuangKomersil x where x.seksyen.id = '07'");
		} else {
			list = mp.list("select x from RkRuangKomersil x");
		}
		if (list.size() > 0) {
			context.put("bilRuangKomersil", list.size());
		} else {
			context.remove("bilRuangKomersil");
		}
	}	
	
	private void getBilPermohonanPenyewaan(String role, MyPersistence mp) {
		List<RkPermohonan> list = null;
		
		if ("(RK) Penyedia".equals(role)) {
			list = mp.list("select x from RkPermohonan x where x.fail.ruang.seksyen.id = '07'");
		} else {
			list = mp.list("select x from RkPermohonan x");
		}
		if (list.size() > 0) {
			context.put("bilPermohonanPenyewaan", list.size());
		} else {
			context.remove("bilPermohonanPenyewaan");
		}
	}	
	
	private void getBilAkaunPenyewa(String role, MyPersistence mp) {
		List<RkFail> list = null;
		
		if ("(RK) Penyedia".equals(role)) {
			list = mp.list("select x from RkFail x where x.flagAktifPerjanjian = 'Y' and x.ruang.seksyen.id = '07'");
		} else {
			list = mp.list("select x from RkFail x where x.flagAktifPerjanjian = 'Y'");
		}
		
		if (list.size() > 0) {
			context.put("bilAkaunPenyewa", list.size());
		} else {
			context.remove("bilAkaunPenyewa");
		}
	}
	
	
}
