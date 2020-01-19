package bph.laporan.rpp;

import java.util.List;

import lebah.portal.action.Command;
import lebah.portal.action.LebahModule;
import lebah.template.DbPersistence;
import portal.module.entity.Users;
import bph.entities.kod.GredJawatan;
import bph.entities.kod.JenisBangunan;
import bph.entities.kod.JenisUnitRPP;
import bph.entities.rpp.RppPeranginan;
import bph.utils.DataUtil;
import db.persistence.MyPersistence;

public class SenaraiLaporanRecordModule extends LebahModule{

	private static final long serialVersionUID = 1L;
	private DbPersistence db = new DbPersistence();
	private DataUtil dataUtil = DataUtil.getInstance(db);
	private MyPersistence mp;

	@Override
	public String start() {
		String userId = (String) request.getSession().getAttribute("_portal_login");
		context.put("userId", userId);
		
		try {
		mp = new MyPersistence();
		
		Users users = (Users) mp.find(Users.class, userId);
		context.put("users", users);
		
		} catch (Exception ex) {
			System.out.println("ERROR start : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("path", getPath());
		return getPath() + "/start.vm";
	}
	
	private String getPath() {
		return "bph/laporan/rpp";
	}
	
	@Command("paparLaporan")
	public String paparLaporan() throws Exception {
		
		String idLaporan = getParam("idLaporan");
		context.put("path", getPath());
		
		if(idLaporan.equals("1")){
			return getPath() + "/laporanKeseluruhanPenyalahgunaanKemudahan.vm";
		}
		else if(idLaporan.equals("2")){	
			//2. Laporan Keseluruhan Kerosakan
			
			context.remove("findJenisPeranginan");
			List<JenisBangunan> jenisPeranginan = dataUtil.getListJenisPeranginan();
			context.put("selectJenisPeranginan", jenisPeranginan);
			
			List<GredJawatan> gredJawatanList = dataUtil.getListGredJawatan();
			context.put("selectGredJawatan", gredJawatanList);
			
			return getPath() + "/laporanKeseluruhanKerosakan.vm";
		}  
		else if(idLaporan.equals("3")){
			
			mp = new MyPersistence();
			context.put("selectGredPerkhidmatan", dataUtil.getListGredPerkhidmatan());
			context.put("selectKelasPerkhidmatan", dataUtil.getListKelasPerkhidmatan());
			
			return getPath() + "/laporanSenaraiHitamPemohon.vm";
		}
		else if(idLaporan.equals("4")){
			
			context.remove("findJenisPeranginan");
			List<JenisBangunan> jenisPeranginan = dataUtil.getListJenisPeranginan();
			context.put("selectJenisPeranginan", jenisPeranginan);
			
			return getPath() + "/laporanDaftarPemohonDanPermohonan.vm";
		}
		else if(idLaporan.equals("5")){
			context.remove("findJenisPeranginan");
			List<JenisBangunan> jenisPeranginan = dataUtil.getListJenisPeranginan();
			context.put("selectJenisPeranginan", jenisPeranginan);
			
			List<RppPeranginan> PeranginanRppList = dataUtil.getListPeranginanRpp();
			context.put("selectRppPeranginan", PeranginanRppList);
			return getPath() + "/laporanMaklumatPenghuniKeluar.vm";
		}
		else if(idLaporan.equals("8")){
			//1. Laporan Kadar Pengunaan dan Jumlah Bayaran Penginapan
			
			context.put("listStatusPermohonan", dataUtil.getListStatusPermohonanLaporanPenggunaan());
			
			List<JenisBangunan> findJenisPeranginan = dataUtil.getListJenisPeranginan();
			context.put("selectJenisPeranginan", findJenisPeranginan);
			
			List<RppPeranginan> PeranginanRppList = dataUtil.getListPeranginanRpp();
			context.put("selectRppPeranginan", PeranginanRppList);
			
			List<GredJawatan> gredJawatanList = dataUtil.getListGredJawatan();
			context.put("selectGredJawatan", gredJawatanList);
			
			return getPath() + "/laporanKadarPengunaan.vm";
		}
		else if(idLaporan.equals("9")){
			return getPath() + "/laporanBayaranPenginapan.vm";
		}
		else if(idLaporan.equals("10")){
			List<RppPeranginan> PeranginanRppList = dataUtil.getListPeranginanRpp();
			context.put("selectRppPeranginan", PeranginanRppList);				
			return getPath() + "/laporanJumlahPermohonan.vm";
		}
		else if(idLaporan.equals("11")){
			return getPath() + "/laporanJumlahPermohonanWalk-In.vm";
		}
		else if(idLaporan.equals("12")){
			List<RppPeranginan> PeranginanRppList = dataUtil.getListPeranginanRpp();
			context.put("selectRppPeranginan", PeranginanRppList);
			return getPath() + "/laporanBilanganPenghuni.vm";
		}
		else if(idLaporan.equals("13")){
			context.remove("findJenisPeranginan");
			List<JenisBangunan> jenisPeranginan = dataUtil.getListJenisPeranginan();
			context.put("selectJenisPeranginan", jenisPeranginan);			
			return getPath() + "/LaporanSenaraiDaftarMasukRumahPeranginan.vm";
		}
		else if(idLaporan.equals("14")){
			List<JenisBangunan> jenisPeranginan = dataUtil.getListJenisPeranginan();
			context.put("selectJenisPeranginan", jenisPeranginan);
			
			List<RppPeranginan> PeranginanRppList = dataUtil.getListPeranginanRpp();
			context.put("selectRppPeranginan", PeranginanRppList);
			return getPath() + "/LaporanSenaraiDaftarKeluarRumahPeranginan.vm";
		}
		else if(idLaporan.equals("15")){
			return getPath() + "/laporanSemakanOverBookBilik.vm";
		}
		else if(idLaporan.equals("16")){
			List<JenisBangunan> jenisPeranginan = dataUtil.getListJenisPeranginanLondon();
			context.put("selectJenisPeranginan", jenisPeranginan);
			context.put("listStatusPermohonan", dataUtil.getListStatusPermohonanLaporanPenggunaan());
			context.put("selectGredPerkhidmatan", dataUtil.getListGredPerkhidmatan());
			return getPath() + "/laporanKadarPenggunaanLondon.vm";
		}
			
		else if(idLaporan.equals("17")){
			//1. Laporan Kadar Pengunaan dan Jumlah Bayaran Penginapan
			
			context.put("listStatusPermohonan", dataUtil.getListStatusPermohonanLaporanPenggunaan());
			
			List<JenisBangunan> findJenisPeranginan = dataUtil.getListJenisPeranginan();
			context.put("selectJenisPeranginan", findJenisPeranginan);
			
			List<RppPeranginan> PeranginanRppList = dataUtil.getListPeranginanRpp();
			context.put("selectRppPeranginan", PeranginanRppList);
			
			List<GredJawatan> gredJawatanList = dataUtil.getListGredJawatan();
			context.put("selectGredJawatan", gredJawatanList);
			
			return getPath() + "/laporanKadarPengunaanKemudahan.vm";
		}
		
		else{
			return "";
		}
	}
	
	@Command("findPeranginan")
	public String findPeranginan() throws Exception {
		
		String jenisPeranginan = "0";
		if (getParam("idJenisPeranginan").trim().length() > 0) 		
		jenisPeranginan = getParam("idJenisPeranginan");
		
		List<RppPeranginan> rppList = dataUtil.getListPeranginanRpp(jenisPeranginan);
		context.put("findRppPeranginan", rppList);
	
		return getPath() + "/findPeranginan.vm";
	}
	
	@Command("findPeranginanLondon")
	public String findPeranginanLondon() throws Exception {
		
		String jenisPeranginan = "0";
		if (getParam("findJenisPeranginan").trim().length() > 0) 		
		jenisPeranginan = getParam("findJenisPeranginan");
		
		List<RppPeranginan> rppList = dataUtil.getListPeranginanRppLondon(jenisPeranginan);
		context.put("findRppPeranginan", rppList);
	
		return getPath() + "/findPeranginanLondon.vm";
	}
	
	@Command("selectJenisUnitRpp")
	public String selectJenisUnitRpp() throws Exception {
		
		System.out.println("dalam method =========" + getParam("idPeranginan"));
		String idPeranginan = "0";

		if (getParam("idPeranginan").trim().length() > 0) 		
		idPeranginan = getParam("idPeranginan");
		
		List<JenisUnitRPP> JenisUnitRPPList = dataUtil.getListAllJenisUnitByPeranginan(idPeranginan);
		context.put("selectJenisUnitRPP", JenisUnitRPPList);
	
		return getPath() + "/selectJenisUnitRPP.vm";
	}
	
}
