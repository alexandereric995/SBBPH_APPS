package bph.laporan.eis;

import lebah.portal.action.Command;
import lebah.portal.action.LebahModule;

public class SenaraiLaporanEISGelanggangRecordModule extends LebahModule {

	private static final long serialVersionUID = 1L;
	
	@Override
	public String start() {
		context.put("path", getPath());
		return getPath() + "/start.vm";
	}

	private String getPath() {
		return "bph/laporan/eis/gelanggang";
	}

	@Command("paparLaporan")
	public String paparLaporan() throws Exception {
		String idLaporan = getParam("idLaporan");
		context.put("path", getPath());
		
		if (idLaporan.equals("1")) {
			return getPath() + "/laporan1.vm";
		} else if (idLaporan.equals("2")) {
			return getPath() + "/laporan2.vm";
		} else{
			return "";
		}
	}
}
