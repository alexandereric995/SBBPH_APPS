package bph.laporan.aduanInternal;

import lebah.portal.action.Command;
import lebah.portal.action.LebahModule;
import lebah.template.DbPersistence;
import bph.utils.DataUtil;

public class SenaraiLaporanRecordModules extends LebahModule{

	private static final long serialVersionUID = 1L;
	private DbPersistence db = new DbPersistence();
	private DataUtil dataUtil = DataUtil.getInstance(db);

	@Override
	public String start() {
		context.put("path", getPath());
		return getPath() + "/start.vm";
	}
	
	private String getPath() {
		return "bph/laporan/aduanInternal";
	}
	
	@Command("paparLaporan")
	public String paparLaporan() throws Exception {
		
		String idLaporan = getParam("idLaporan");
		context.put("path", getPath());
	
		if(idLaporan.equals("1")){
			return getPath() + "/laporanSenaraiAduanInternal.vm";
		}

		else{
			return "";
		}
	}
	
}
