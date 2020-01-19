package bph.modules.senggara;

import portal.module.entity.Users;

public class PenyediaanIndenKerjaRecordModule extends RekodIndenKerjaRecordModule {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public void doOverideFilterRecord() {
		clearFilter();
		Users users = db.find(Users.class, userId);
		
		this.addFilter("status.id = '1426130691711'"); //PENYEDIAAN INDEN KERJA
		
		this.setOrderBy("noInden");
		this.setOrderType("desc");
		
		boolean showKontraktor = false;
		context.put("showKontraktor", showKontraktor);
		boolean showTugasan = false;
		context.put("showTugasan", showTugasan);
		boolean showStatus = false;
		context.put("showStatus", showStatus);
		
		this.setReadonly(false);
		this.setDisableSaveAddNewButton(true);
		this.setHideDeleteButton(true);
		if ("add_new_record".equals(command)){
			this.setDisableBackButton(false);
			this.setDisableDefaultButton(false);
		} else {
			this.setDisableBackButton(true);
			this.setDisableDefaultButton(true);
		}
		
		context.put("flagSkrin", "PenyediaanIndenKerjaRecordModule");
	}
}
