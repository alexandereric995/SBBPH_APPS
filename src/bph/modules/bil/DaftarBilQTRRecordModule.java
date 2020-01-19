package bph.modules.bil;

import portal.module.entity.Users;
import bph.entities.kod.Seksyen;
import db.persistence.MyPersistence;

public class DaftarBilQTRRecordModule extends DaftarBilRecordModule {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MyPersistence mp;
	
	public void doOverideFilterRecord() {
		
		try {			
			mp = new MyPersistence();
			
			clearFilter();
			Users users = (Users) mp.find(Users.class, userId);
			this.addFilter("seksyen.id = '02'");
			
			Seksyen seksyen = (Seksyen) mp.get("Select x from Seksyen x where x.id='02'");
			context.put("seksyen", seksyen);
			context.put("users", users);
			
		} catch (Exception ex) {
			System.out.println("ERROR INSERT DOKUMEN : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}

		
	}
		
}
