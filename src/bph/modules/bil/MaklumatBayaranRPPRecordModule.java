package bph.modules.bil;

import bph.entities.kod.Seksyen;
import db.persistence.MyPersistence;

public class MaklumatBayaranRPPRecordModule extends MaklumatBayaranRecordModule {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MyPersistence mp;
	
	public void doOverideFilterRecord() {
		
		try {			
			mp = new MyPersistence();
			
			clearFilter();
			
			if ("(RPP) Penyemak".equals(userRole)){
				this.addFilter("statusBil.id = '1433131904918'");
			}
			
			this.addFilter("pendaftaranBil.seksyen.id = '10'");
			
			Seksyen seksyen = (Seksyen) mp.get("Select x from Seksyen x where x.id='10'");
			context.put("seksyen", seksyen);

			
		} catch (Exception ex) {
			System.out.println("ERROR INSERT DOKUMEN : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}

	}
		
}
