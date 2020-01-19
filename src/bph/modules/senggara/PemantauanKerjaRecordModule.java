package bph.modules.senggara;

import portal.module.entity.Users;

public class PemantauanKerjaRecordModule extends RekodIndenKerjaRecordModule {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doOverideFilterRecord() {
		clearFilter();
		Users users = db.find(Users.class, userId);
		
		this.addFilter("tugasan.pegawaiTugasan.id = '" + users.getId() + "'");
		this.addFilter("status.id = '17859229601095'"); //PEMANTAUAN KERJA
		
		this.setOrderBy("noInden");
		this.setOrderType("desc");
		
		boolean showKontraktor = true;
		context.put("showKontraktor", showKontraktor);
		boolean showTugasan = false;
		context.put("showTugasan", showTugasan);
		boolean showStatus = false;
		context.put("showStatus", showStatus);
		
		context.put("flagSkrin", "PemantauanKerjaRecordModule");
	}
}
