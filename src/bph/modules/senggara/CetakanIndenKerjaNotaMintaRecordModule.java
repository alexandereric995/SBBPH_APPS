package bph.modules.senggara;

import portal.module.entity.Users;

public class CetakanIndenKerjaNotaMintaRecordModule extends RekodIndenKerjaRecordModule {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public void doOverideFilterRecord() {
		clearFilter();
		Users users = db.find(Users.class, userId);
		
		this.addFilter("status.id = '17859229601059'"); //CETAKAN INDEN KERJA / NOTA MINTA
		
		this.setOrderBy("noInden");
		this.setOrderType("desc");
		
		boolean showKontraktor = true;
		context.put("showKontraktor", showKontraktor);
		boolean showTugasan = false;
		context.put("showTugasan", showTugasan);
		boolean showStatus = false;
		context.put("showStatus", showStatus);
		
		context.put("flagSkrin", "CetakanIndenKerjaNotaMintaRecordModule");
	}
}
