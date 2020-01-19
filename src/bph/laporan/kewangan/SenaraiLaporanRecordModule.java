package bph.laporan.kewangan;

import java.util.List;

import lebah.portal.action.Command;
import lebah.portal.action.LebahModule;
import lebah.template.DbPersistence;
import bph.entities.kod.KodHasil;
import bph.entities.kod.KodJuruwang;
import bph.entities.kod.KodPusatTerima;
import bph.utils.DataUtil;

public class SenaraiLaporanRecordModule extends LebahModule {

	private static final long serialVersionUID = 1L;
	private DbPersistence db = new DbPersistence();
	private DataUtil dataUtil = DataUtil.getInstance(db);
	private String userRole = "";
	private String userId = "";
	
	@Override
	public String start() {
		userRole = (String) request.getSession().getAttribute("_portal_role");
		context.put("userRole", userRole);
		context.put("path", getPath());
		return getPath() + "/start.vm";
	}

	private String getPath() {
		return "bph/laporan/kewangan";
	}

	@Command("paparLaporan")
	public String paparLaporan() throws Exception {
		String kodPusatTerima = "";
		userRole = (String) request.getSession().getAttribute("_portal_role");
		userId = (String) request.getSession().getAttribute("_portal_login");
		KodJuruwang juruwang = (KodJuruwang) db.get("select x from KodJuruwang x where x.juruwang.id = '" + userId + "'");
		if (juruwang != null) {
			kodPusatTerima = juruwang.getKodPusatTerima();
		}
		String idLaporan = getParam("idLaporan");
		context.put("path", getPath());
		
		List<KodJuruwang> jurulist = null;
		List<KodPusatTerima> pusatTerimaList = null;
		
		List<KodJuruwang> ListKodJuruwang = null;	
		if ("(HASIL) Penyelia Cawangan".equals(userRole)) {
			ListKodJuruwang = dataUtil.getListPusatTerimaReportByIndividu(userId);
		} else {
			ListKodJuruwang = dataUtil.getListPusatTerimaReportByIndividuJuruwang(userId);	
		}
		
		if ("(HASIL) Penyelia Cawangan".equals(userRole) || "(HASIL) Juruwang Cawangan".equals(userRole)) {
			jurulist = dataUtil.getListJuruwangReportByCawangan(kodPusatTerima);
			pusatTerimaList = dataUtil.getListPusatTerimaReportByCawangan(ListKodJuruwang);	
		} else {
			jurulist = dataUtil.getListJuruwangReport();
			pusatTerimaList = dataUtil.getListPusatTerimaReport();
		}
		context.put("selectJuru", jurulist);
		context.put("selectPusatTerima", pusatTerimaList);
		List<KodHasil> listKodHasil = dataUtil.getListKodHasil();
		context.put("selectKodHasil", listKodHasil);

		if (idLaporan.equals("1")) {	
			return getPath() + "/laporanSenaraiKutipanHarian.vm";
		} else if (idLaporan.equals("2")) {
			return getPath() + "/laporanSenaraiKutipanHarianTerperinci.vm";
		} else if (idLaporan.equals("3")) {
			return getPath() + "/laporanSenaraiResit.vm";
		} else if (idLaporan.equals("4")) {
			return getPath() + "/laporanSenaraiResitBatal.vm";
		} else if (idLaporan.equals("5")) {
			return getPath() + "/laporanSerahanJuruwang.vm";
		} else if (idLaporan.equals("6")) {
			return getPath() + "/laporanBukuTunaiCerakinanBulanan.vm";
		} else if (idLaporan.equals("7")) {
			return getPath() + "/laporanBukuTunaiCerakinanHarian.vm";
		} else if (idLaporan.equals("8")) {
			return getPath() + "/laporanUrusniagaHarianE_Payment.vm";
		} else if (idLaporan.equals("11")) {
			return getPath() + "/laporanSemakanResitE_Payment.vm";
		} else if(idLaporan.equals("9")){
			return getPath() + "/laporanBukuTunaiCerakinanKutipanEpayment_MIGS.vm";
		} else if(idLaporan.equals("10")){
			return getPath() + "/LaporanBukuTunaiCerakinanKutipanEpayment_FPX.vm";
		} else if(idLaporan.equals("12")){			
			return getPath() + "/LaporanKutipanBerdasarkanKod.vm";
		} else {
			return "";
		}
	}
}
