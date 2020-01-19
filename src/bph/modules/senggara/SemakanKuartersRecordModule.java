package bph.modules.senggara;

import portal.module.entity.Users;

public class SemakanKuartersRecordModule extends RekodKunciRecordModule {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doOverideFilterRecord() {
		clearFilter();
		Users users = db.find(Users.class, userId);
		
		this.addFilter("tugasan.pegawaiTugasan.id = '" + users.getId() + "'");
		this.addFilter("status.id = '762326425830'"); //SEMAKAN KUARTERS
		
		this.setOrderBy("tarikhTerimaLaporan");
		this.setOrderType("desc");
		
		boolean showTerimaanKunci = true;
		context.put("showTerimaanKunci", showTerimaanKunci);	
		boolean showSerahanKunci = false;
		context.put("showSerahanKunci", showSerahanKunci);		
		boolean showTugasan = false;
		context.put("showTugasan", showTugasan);
		boolean showStatus = false;
		context.put("showStatus", showStatus);
		
		context.put("flagSkrin", "SemakanKuartersRecordModule");
	}
}
