/* Author :
 * zulfazdliabuas@gmail.com Data 2015-2017
 */

package bph.modules.utk;

import bph.entities.utk.UtkAkaun;
import bph.entities.utk.UtkKesalahan;

public class SenaraiHitamPenghuni extends PelanggaranSyaratRecordModule {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doOverideFilterRecord() {
		clearFilter();
		
		this.addFilter("statusPenghuni='3'");
		
	}
	
	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/utk/senaraiHitamPenghuni";
	}

	@Override
	public void getRelatedData(UtkKesalahan r) {
		// TODO Auto-generated method stub

		UtkAkaun ak = (UtkAkaun) db.get("Select x from UtkAkaun x where x.kesalahan.id='" + r.getId() +"'");
		context.put("ak", ak);
		context.put("selectedTab", "1");
		
	}
		
}
