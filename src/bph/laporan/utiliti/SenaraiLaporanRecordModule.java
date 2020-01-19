package bph.laporan.utiliti;

import java.util.List;

import lebah.portal.action.Command;
import lebah.portal.action.LebahModule;
import lebah.template.DbPersistence;
import bph.entities.kod.KodPetugas;
import bph.entities.utiliti.UtilGelanggang;
import bph.utils.DataUtil;
import bph.utils.Util;

public class SenaraiLaporanRecordModule extends LebahModule{

	private DbPersistence db = new DbPersistence();
	private DataUtil dataUtil = DataUtil.getInstance(db);


	private static final long serialVersionUID = 1L;

	@Override
	public String start() {
		context.put("path", getPath());
		
		
		return getPath() + "/start.vm";
	}
	
	private String getPath() {
		return "bph/laporan/utiliti";
	}
	
	
	@Command("paparLaporan")
	public String paparLaporan() throws Exception {
		
		String idLaporan = getParam("idLaporan");
		
		context.put("path", getPath());
		
		if(idLaporan.equals("1")){
	        return getPath() + "/laporanSenaraiPenggunaanDewan.vm";
		} 
		if(idLaporan.equals("2")){
			return getPath() + "/laporanSenaraiStatusPermohonan.vm";
		}
		if(idLaporan.equals("3")){
			return getPath() + "/laporanStatistikPenggunaanDewan.vm";
		}
		if(idLaporan.equals("4")){
			return getPath() + "/laporanStatistikPermohonan.vm";
		}
		if(idLaporan.equals("5")){
			//roszaineza add untuk cawangan dewan gelanggang 17/7/2017
			String idCawangan="";
			String userId = (String) request.getSession().getAttribute("_portal_login");
			String userRole = (String) request.getSession().getAttribute("_portal_role");
			if ("(UTILITI) Penyedia Cawangan".equalsIgnoreCase(userRole)|| "(UTILITI) Pelulus Cawangan".equalsIgnoreCase(userRole)) {
				KodPetugas petugas = (KodPetugas) db.get("select x from KodPetugas x where x.petugas.id = '" + userId + "'");
				if (petugas != null) {
					idCawangan=petugas.getCawangan().getId();
				}
			}
			dataUtil = DataUtil.getInstance(db);
			if ("(UTILITI) Penyedia Cawangan".equalsIgnoreCase(userRole)|| "(UTILITI) Pelulus Cawangan".equalsIgnoreCase(userRole)) {
				context.put("selectDewan", dataUtil.getListDewanSahaja(idCawangan));
			}else{
				context.put("selectDewan", dataUtil.getListDewanSahaja(null));
			}
			return getPath() + "/laporanSenaraiTempahanGelanggang.vm";
		}
		if(idLaporan.equals("6")){
			return getPath() + "/laporanStatistikKutipanBayaran.vm";
		}
		if(idLaporan.equals("7")){
			return getPath() + "/laporanStatistikKutipanBayaran.vm";
		}
		if(idLaporan.equals("8")){
			String today = Util.getCurrentDate("dd-MM-yyyy");
			//roszaineza add untuk cawangan dewan gelanggang 17/7/2017
			String idCawangan="";
			String userId = (String) request.getSession().getAttribute("_portal_login");
			String userRole = (String) request.getSession().getAttribute("_portal_role");
			if ("(UTILITI) Penyedia Cawangan".equalsIgnoreCase(userRole)|| "(UTILITI) Pelulus Cawangan".equalsIgnoreCase(userRole)) {
				KodPetugas petugas = (KodPetugas) db.get("select x from KodPetugas x where x.petugas.id = '" + userId + "'");
				if (petugas != null) {
					idCawangan=petugas.getCawangan().getId();
				}
			}
			dataUtil = DataUtil.getInstance(db);
			if ("(UTILITI) Penyedia Cawangan".equalsIgnoreCase(userRole)|| "(UTILITI) Pelulus Cawangan".equalsIgnoreCase(userRole)) {
				context.put("selectDewan", dataUtil.getListDewanSahaja(idCawangan));
			}else{
				context.put("selectDewan", dataUtil.getListDewanSahaja(null));
			}
			context.put("today",today);
			return getPath() + "/laporanSenaraiButiranTempahanGelanggang.vm";
		}
		else{
						
			return "";
		}
	}	
	
	@Command("selectGelanggang")
	public String selectGelanggang() throws Exception {

		String idDewan = "";
		if (getParam("idDewan").trim().length() > 0)
			idDewan = getParam("idDewan");
		List<UtilGelanggang> list = dataUtil.getListGelanggang(idDewan);
		context.put("selectGelanggang", list);
		return getPath() + "/selectGelanggang.vm";
	}
}
