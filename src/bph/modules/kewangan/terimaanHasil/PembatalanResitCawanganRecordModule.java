package bph.modules.kewangan.terimaanHasil;

import java.util.List;

import bph.entities.kod.KodJuruwang;
import db.persistence.MyPersistence;

public class PembatalanResitCawanganRecordModule extends PembatalanResitRecordModule {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MyPersistence mp;

	public void doOverideFilterRecord() {
		clearFilter();
		userId = (String) request.getSession().getAttribute("_portal_login");
		List<KodJuruwang> listKodJuruwang = getListPusatTerimaReportByIndividu(userId);
		String pusatTerima = "";
		pusatTerima = getListPusatTerimaByCawangan(listKodJuruwang);	

		this.addFilter("kodPusatTerima in (" + pusatTerima + ")");
		this.addFilter("noResit is not null and COALESCE(x.flagVoid,'T') = 'T' ");
		this.setOrderBy("tarikhBayaran");
		this.setOrderType("desc");		
	}	

	private List<KodJuruwang> getListPusatTerimaReportByIndividu(String userId) {
		List<KodJuruwang> list = null;
		try {
			mp = new MyPersistence();
			list = mp.list("select x from KodJuruwang x where x.juruwang.id = '" + userId + "' and x.jawatan = 'PENYELIA' and x.flagAktif = 'Y' order by x.kodPusatTerima asc");
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null)
				mp.close();
		}
		return list;
	}
	
	private String getListPusatTerimaByCawangan(List<KodJuruwang> listKodJuruwang) {
		String kodPusatTerima = null;
		for (int i = 0; i < listKodJuruwang.size(); i++) {
			KodJuruwang kodJuruwang = listKodJuruwang.get(i);
			if (kodJuruwang.getKodPusatTerima() != null) {
				if (kodJuruwang.getKodPusatTerima().trim().length() > 0) {
					if (kodPusatTerima == null) {
						kodPusatTerima = "'" + kodJuruwang.getKodPusatTerima() + "'";
					} else {
						kodPusatTerima = kodPusatTerima + ", '" + kodJuruwang.getKodPusatTerima() + "'";
					}
				}
			}
		}
		return kodPusatTerima;
	}
}
