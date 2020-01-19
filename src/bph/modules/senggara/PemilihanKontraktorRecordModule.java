package bph.modules.senggara;

import portal.module.entity.Users;


public class PemilihanKontraktorRecordModule extends RekodIndenKerjaRecordModule {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public void doOverideFilterRecord() {
		clearFilter();
		Users users = db.find(Users.class, userId);
		
		this.addFilter("status.id = '17859229601035'"); //PEMILIHAN KONTRAKTOR
		this.setOrderBy("noInden");
		this.setOrderType("desc");
		
		boolean showKontraktor = false;
		context.put("showKontraktor", showKontraktor);
		boolean showTugasan = false;
		context.put("showTugasan", showTugasan);
		boolean showStatus = false;
		context.put("showStatus", showStatus);
				
		context.put("flagSkrin", "PemilihanKontraktorRecordModule");
	}
}
