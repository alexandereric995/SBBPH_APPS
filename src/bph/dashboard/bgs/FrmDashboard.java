package bph.dashboard.bgs;

import java.util.List;

import lebah.portal.action.LebahModule;
import bph.entities.bgs.BgsPermohonan;
import bph.entities.rk.RkFail;
import bph.entities.rk.RkRuangKomersil;
import db.persistence.MyPersistence;

public class FrmDashboard extends LebahModule{

	private static final long serialVersionUID = 1L;
	private MyPersistence mp;

	private String getPath() {
		return "bph/dashboard/bgs";
	}
	
	@Override
	public String start() {
		
		String portal_role  = (String)request.getSession().getAttribute("_portal_role");
		context.put("portal_role", portal_role);
		
		process();
		
		lebah.util.Util lebahUtil = new lebah.util.Util();
		context.put("lebahUtil", lebahUtil);
		context.put("path", getPath());
		return getPath() + "/start.vm";
	}

	private void process() {
		try {
			mp = new MyPersistence();
			
			getBilPermohonanRuangPejabat(mp);
			getBilSemakanKertasPertimbangan(mp);
			getBilPengesahanKertasPertimbangan(mp);
			
			getBilAkaunPenyewaRK(mp);
			getBilRuangKomersil(mp);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
	}

	private void getBilPermohonanRuangPejabat(MyPersistence mp) {
		List<BgsPermohonan> list = mp.list("select x from BgsPermohonan x where x.status.id in ('1423568441671','1423568441688','1423568441694','1423568441691')");
		if (list.size() > 0) {
			context.put("bilPermohonanRuangPejabat", list.size());
		} else {
			context.remove("bilPermohonanRuangPejabat");
		}
	}
	
	private void getBilSemakanKertasPertimbangan(MyPersistence mp) {
		List<BgsPermohonan> list = mp.list("select x from BgsPermohonan x where x.status.id in ('1423568441682')");
		if (list.size() > 0) {
			context.put("bilSemakanKertasPertimbangan", list.size());
		} else {
			context.remove("bilSemakanKertasPertimbangan");
		}
	}
	
	private void getBilPengesahanKertasPertimbangan(MyPersistence mp) {
		List<BgsPermohonan> list = mp.list("select x from BgsPermohonan x where x.status.id in ('1423822397722')");
		if (list.size() > 0) {
			context.put("bilPengesahanPertimbangan", list.size());
		} else {
			context.remove("bilPengesahanPertimbangan");
		}
	}
	
	private void getBilAkaunPenyewaRK(MyPersistence mp) {
		List<RkFail> list = mp.list("select x from RkFail x where x.ruang.seksyen.id = '06'");
		if (list.size() > 0) {
			context.put("bilAkaunPenyewaRK", list.size());
		} else {
			context.remove("bilAkaunPenyewaRK");
		}
	}
	
	private void getBilRuangKomersil(MyPersistence mp) {
		List<RkRuangKomersil> list = mp.list("select x from RkRuangKomersil x where x.seksyen.id = '06'");
		if (list.size() > 0) {
			context.put("bilRuangKomersil", list.size());
		} else {
			context.remove("bilRuangKomersil");
		}
	}
}
	
