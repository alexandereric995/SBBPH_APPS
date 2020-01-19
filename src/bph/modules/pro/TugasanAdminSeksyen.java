package bph.modules.pro;

import portal.module.entity.Users;

public class TugasanAdminSeksyen extends SenaraiAduanRecordModule {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public void doOverideFilterRecord() {
		clearFilter();
		Users users = db.find(Users.class, userId);
		
		this.addFilter("seksyen.id = '" + users.getSeksyen().getId() + "'");
		
		this.setReadonly(true);
		
		context.put("flagPenyemak", true);
		
	}

}
