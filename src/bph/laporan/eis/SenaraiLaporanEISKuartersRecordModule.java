package bph.laporan.eis;

import lebah.portal.action.Command;
import lebah.portal.action.LebahModule;

public class SenaraiLaporanEISKuartersRecordModule extends LebahModule {

	private static final long serialVersionUID = 1L;
	
	@Override
	public String start() {
		context.put("path", getPath());
		return getPath() + "/start.vm";
	}

	private String getPath() {
		return "bph/laporan/eis/kuarters";
	}

	@Command("paparLaporan")
	public String paparLaporan() throws Exception {
		String idLaporan = getParam("idLaporan");
		context.put("path", getPath());
		
		if (idLaporan.equals("1")) {
			return getPath() + "/laporan1.vm";
		} else if (idLaporan.equals("2")) {
			return getPath() + "/laporan2.vm";
		} else if (idLaporan.equals("3")) {
			return getPath() + "/laporan3.vm";
		} else if (idLaporan.equals("4")) {
			return getPath() + "/laporan4.vm";
		} else if (idLaporan.equals("5")) {
			return getPath() + "/laporan5.vm";
		} else{
			return "";
		}
	}
}
