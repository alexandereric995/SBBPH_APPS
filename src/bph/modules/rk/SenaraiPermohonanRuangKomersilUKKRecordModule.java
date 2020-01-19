package bph.modules.rk;

import portal.module.entity.Users;

public class SenaraiPermohonanRuangKomersilUKKRecordModule extends SenaraiPermohonanRuangKomersilRecordModule {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public void doOverideFilterRecord() {
		clearFilter();
		Users users = db.find(Users.class, userId);
		
		this.addFilter("fail.ruang.seksyen.id = '07'");
		
		this.setOrderBy("tarikhPermohonan");
		this.setOrderType("desc");			
		
		boolean showSeksyen = false;
		context.put("showSeksyen", showSeksyen);
		context.put("idSeksyen", "07");
	}
}
