package bph.modules.senggara;

import portal.module.entity.Users;

public class PenyediaanJKHRecordModule extends RekodKunciRecordModule {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doOverideFilterRecord() {
		clearFilter();
		Users users = db.find(Users.class, userId);
		
		this.addFilter("status.id in ('1426130691702', '1427773516431')"); //PENYEDIAAN JKH || PINDAAN PENYEDIAAN JKH
		
		this.setOrderBy("tarikhTerimaLaporan");
		this.setOrderType("desc");
		
		boolean showTerimaanKunci = true;
		context.put("showTerimaanKunci", showTerimaanKunci);	
		boolean showSerahanKunci = false;
		context.put("showSerahanKunci", showSerahanKunci);		
		boolean showTugasan = false;
		context.put("showTugasan", showTugasan);
		boolean showStatus = true;
		context.put("showStatus", showStatus);
		
		context.put("flagSkrin", "PenyediaanJKHRecordModule");
	}
}
