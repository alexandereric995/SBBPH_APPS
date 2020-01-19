package bph.laporan.eis;

import lebah.portal.action.Command;
import lebah.portal.action.LebahModule;

public class SenaraiLaporanEISRecordModule extends LebahModule {

	private static final long serialVersionUID = 1L;
	
	@Override
	public String start() {
		context.put("path", getPath());
		return getPath() + "/start.vm";
	}

	private String getPath() {
		return "bph/laporan/eis/kewangan";
	}

//	@Command("paparLaporan")
//	public String paparLaporan() throws Exception {
//		String idLaporan = getParam("idLaporan");
//		context.put("path", getPath());
//		
//		if (idLaporan.equals("1")) {
//			return getPath() + "/laporanKutipanBPHKeseluruhan.vm";
//		} else if (idLaporan.equals("2")) {
//			return getPath() + "/laporanKutipanBPHKeseluruhanRPP.vm";
//		} else if (idLaporan.equals("3")) {
//			return getPath() + "/laporanKutipanBulananKuarters.vm";
//		} else if (idLaporan.equals("4")) {
//			return getPath() + "/laporanKutipanBulananNaziran.vm";
//		} else if (idLaporan.equals("5")) {
//			return getPath() + "/laporanKutipanBulananRuangKerajaan.vm";
//		} else if (idLaporan.equals("6")) {
//			return getPath() + "/laporanKutipanBulananRumahPeranginan.vm";
//		} else if (idLaporan.equals("17")) {
//			return "bph/laporan/eis/rpp/laporanKutipanHasilRPP.vm";
//		} else{
//			return "";
//		}
//	}
	
	@Command("paparLaporan")
	public String paparLaporan() throws Exception {
		String idLaporan = getParam("idLaporan");
		System.out.println("idLaporan ====== : " + idLaporan);
		context.put("idLaporan", idLaporan);
		context.put("path", getPath());
		
		if (idLaporan.equals("1")) {
			return getPath() + "/laporanKutipanBPHKeseluruhan/start.vm";
		} else if (idLaporan.equals("2")) {
			return getPath() + "/laporanKutipanBPHKeseluruhanRPP/start.vm";
		} else if (idLaporan.equals("3")) {
			return getPath() + "/laporanKutipanBulananKuarters/start.vm";
		} else if (idLaporan.equals("4")) {
			return getPath() + "/laporanKutipanBulananNaziran/start.vm";
		} else if (idLaporan.equals("5")) {
			return getPath() + "/laporanKutipanBulananRuangKerajaan/start.vm";
		} else if (idLaporan.equals("6")) {
			return getPath() + "/laporanKutipanBulananRumahPeranginan/start.vm";
		} else if (idLaporan.equals("17")) {
			return getPath() + "/laporanKutipanHasilRPP/start.vm";
		} else{
			return "";
		}
	}
	
	
	
	@Command("pilihLaporan")
	public String pilihLaporan() {
		
		String strJenisLaporan = getParam("findJenisLaporan");
		System.out.println("strJenisLaporan ====== : " + strJenisLaporan);
		context.put("jenisLaporan", strJenisLaporan);
		
		String idLaporan = getParam("idLaporan");
		System.out.println("idLaporan ====== : " + idLaporan);
		
//		if (strJenisLaporan.equals("1")){
//			return getPath() + "/laporanKutipanBPHKeseluruhan/kriteriaTahun.vm";
//		} else if (strJenisLaporan.equals("2")) {
//			return getPath() + "/laporanKutipanBPHKeseluruhan/kriteriaBulan.vm";
//		}else{
//			return "";
//		}
		
		if (idLaporan.equals("1")) {
			if (strJenisLaporan.equals("1")){
				return getPath() + "/laporanKutipanBPHKeseluruhan/kriteriaTahun.vm";
			} else if (strJenisLaporan.equals("2")) {
				return getPath() + "/laporanKutipanBPHKeseluruhan/kriteriaBulan.vm";
			}else{
				return "";
			}
		} else if (idLaporan.equals("2")) {
			if (strJenisLaporan.equals("1")){
				return getPath() + "/laporanKutipanBPHKeseluruhanRPP/kriteriaTahun.vm";
			} else if (strJenisLaporan.equals("2")) {
				return getPath() + "/laporanKutipanBPHKeseluruhanRPP/kriteriaBulan.vm";
			}else{
				return "";
			}
		} else if (idLaporan.equals("3")) {
			if (strJenisLaporan.equals("1")){
				return getPath() + "/laporanKutipanBulananKuarters/kriteriaTahun.vm";
			} else if (strJenisLaporan.equals("2")) {
				return getPath() + "/laporanKutipanBulananKuarters/kriteriaBulan.vm";
			}else{
				return "";
			}
		} else if (idLaporan.equals("4")) {
			if (strJenisLaporan.equals("1")){
				return getPath() + "/laporanKutipanBulananNaziran/kriteriaTahun.vm";
			} else if (strJenisLaporan.equals("2")) {
				return getPath() + "/laporanKutipanBulananNaziran/kriteriaBulan.vm";
			}else{
				return "";
			}
		} else if (idLaporan.equals("5")) {
			if (strJenisLaporan.equals("1")){
				return getPath() + "/laporanKutipanBulananRuangKerajaan/kriteriaTahun.vm";
			} else if (strJenisLaporan.equals("2")) {
				return getPath() + "/laporanKutipanBulananRuangKerajaan/kriteriaBulan.vm";
			}else{
				return "";
			}
		} else if (idLaporan.equals("6")) {
			if (strJenisLaporan.equals("1")){
				return getPath() + "/laporanKutipanBulananRumahPeranginan/kriteriaTahun.vm";
			} else if (strJenisLaporan.equals("2")) {
				return getPath() + "/laporanKutipanBulananRumahPeranginan/kriteriaBulan.vm";
			}else{
				return "";
			}
		} else if (idLaporan.equals("17")) {
			if (strJenisLaporan.equals("1")){
				return getPath() + "/laporanKutipanHasilRPP/kriteriaTahun.vm";
			} else if (strJenisLaporan.equals("2")) {
				return getPath() + "/laporanKutipanHasilRPP/kriteriaBulan.vm";
			}else{
				return "";
			}
		} else{
			return "";
		}
	}
	
}
