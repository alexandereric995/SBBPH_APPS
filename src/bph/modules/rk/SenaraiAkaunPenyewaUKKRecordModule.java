package bph.modules.rk;

import portal.module.entity.Users;

public class SenaraiAkaunPenyewaUKKRecordModule extends SenaraiAkaunPenyewaRecordModule {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doOverideFilterRecord() {
		clearFilter();
		Users users = db.find(Users.class, userId);
		
		this.addFilter("ruang.seksyen.id = '07'");
		this.addFilter("flagAktifPerjanjian = 'Y'");
		
		this.setOrderBy("noFail");
		this.setOrderType("asc");
	}
}
