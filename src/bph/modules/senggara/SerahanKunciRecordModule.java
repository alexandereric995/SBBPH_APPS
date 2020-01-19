package bph.modules.senggara;

import portal.module.entity.Users;

public class SerahanKunciRecordModule extends RekodKunciRecordModule {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doOverideFilterRecord() {
		clearFilter();
		Users users = db.find(Users.class, userId);
		
		this.addFilter("status.id in ('144160488701579')"); //SERAHAN KUNCI
		
		this.setOrderBy("tarikhTerimaLaporan");
		this.setOrderType("desc");
		
		boolean showTerimaanKunci = false;
		context.put("showTerimaanKunci", showTerimaanKunci);	
		boolean showSerahanKunci = false;
		context.put("showSerahanKunci", showSerahanKunci);		
		boolean showTugasan = false;
		context.put("showTugasan", showTugasan);
		boolean showStatus = false;
		context.put("showStatus", showStatus);
		
		context.put("flagSkrin", "SerahanKunciRecordModule");
	}
}
