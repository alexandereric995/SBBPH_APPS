package bph.modules.kewangan.terimaanHasil;

import bph.entities.kod.KodJuruwang;

public class PenyataPemungutCawangan extends PenyataPemungutRecordModule {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doOverideFilterRecord() {
		clearFilter();
		userId = (String) request.getSession().getAttribute("_portal_login");
		
		KodJuruwang juruwang = (KodJuruwang) db.get("select x from KodJuruwang x where x.juruwang.id = '" + userId + "' and x.flagAktif = 'Y'");
		if (juruwang != null) {
			this.addFilter("kodPusat = '" + juruwang.getKodPusatTerima() + "'");
		} else {
			this.addFilter("kodPusat = ''");
		}
		
		this.setOrderBy("tarikhPenyataPemungut");
		this.setOrderType("desc");
	}
}
