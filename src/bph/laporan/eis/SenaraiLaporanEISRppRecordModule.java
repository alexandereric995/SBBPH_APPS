package bph.laporan.eis;

import lebah.portal.action.Command;
import lebah.portal.action.LebahModule;

public class SenaraiLaporanEISRppRecordModule extends LebahModule {

	private static final long serialVersionUID = 1L;
	
	@Override
	public String start() {
		context.put("path", getPath());
		return getPath() + "/start.vm";
	}

	private String getPath() {
		return "bph/laporan/eis/rpp";
	}

	@Command("paparLaporan")
	public String paparLaporan() throws Exception {
		String idLaporan = getParam("idLaporan");
		context.put("path", getPath());
		
		if (idLaporan.equals("1")) {
			return getPath() + "/laporanPembayaranRppBelumBayar.vm";
		} else if (idLaporan.equals("2")) {
			return getPath() + "/laporanPembayaranRppSudahBayar.vm";
		} else if (idLaporan.equals("3")) {
			return getPath() + "/laporanPembayaranRppBatal.vm";
		} else if (idLaporan.equals("4")) {
			return getPath() + "/laporanKehadiranRppHadir.vm";
		} else if (idLaporan.equals("5")) {
			return getPath() + "/laporanKehadiranRppTidakHadir.vm";
		} else if (idLaporan.equals("6")) {
			return getPath() + "/laporanKehadiranRppBatal.vm";	
		} else if (idLaporan.equals("7")) {
			return getPath() + "/laporanTempahanRppOnline.vm";
		} else if (idLaporan.equals("8")) {
			return getPath() + "/laporanTempahanRppWalkin.vm";
		} else if (idLaporan.equals("9")) {
			return getPath() + "/laporanTempahanRppKelompok.vm";
		} else if (idLaporan.equals("10")) {
			return getPath() + "/laporanPenginapRppPenjawatAwam.vm";
		} else if (idLaporan.equals("11")) {
			return getPath() + "/laporanPenginapRppBadanBerkanun.vm";	
		} else if (idLaporan.equals("12")) {
			return getPath() + "/laporanPenginapRppBadanBeruniform.vm";
		} else if (idLaporan.equals("13")) {
			return getPath() + "/laporanPenginapRppPesara.vm";
		} else if (idLaporan.equals("14")) {
			return getPath() + "/laporanPenginapRppProfesional.vm";
		} else if (idLaporan.equals("15")) {
			return getPath() + "/laporanKadarPenginapanRppWaktuPuncak.vm";
		} else if (idLaporan.equals("16")) {
			return getPath() + "/laporanKadarPenginapanRppWaktuLuarPuncak.vm";
		} else if (idLaporan.equals("17")) {
			return getPath() + "/laporanKutipanHasilRPP.vm";
		} else{
			return "";
		}
	}
}
