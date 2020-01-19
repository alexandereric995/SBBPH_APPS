package bph.modules.pembangunan;

import java.util.List;

import org.apache.log4j.Logger;

import bph.entities.pembangunan.DevLogSemakan;


public class SenaraiSemakanMaklumatRecordModule extends SenaraiHakmilikRecordModule {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static Logger myLogger = Logger.getLogger("SenaraiSemakanMaklumatRecordModule");
	
	public void doOverideFilterRecord() {
		clearFilter();
		String idHakmilik = "";
		List<DevLogSemakan> listLogSemakan = db.list("select x from DevLogSemakan x where x.flagAktif = 'Y' and x.semakan.status = 'B' and x.pegawai.id = '" + userId + "'");
		for(DevLogSemakan logSemakan : listLogSemakan) {
			if ("".equals(idHakmilik)) {
				idHakmilik = logSemakan.getSemakan().getHakmilik().getId();
			} else {
				idHakmilik = idHakmilik + ", " + logSemakan.getSemakan().getHakmilik().getId();
			}
		}
		if ("".equals(idHakmilik)) {
			idHakmilik = "''";
		}
		this.addFilter("id in (" + idHakmilik + ")");		
		this.setReadonly(true);
		context.remove("allowUpdate");
	}
}