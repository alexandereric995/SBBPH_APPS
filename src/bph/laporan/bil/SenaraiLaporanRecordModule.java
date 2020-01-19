package bph.laporan.bil;

import java.util.List;

import lebah.portal.action.Command;
import lebah.portal.action.LebahModule;
import lebah.template.DbPersistence;
import bph.entities.kod.KodBil;
import bph.entities.kod.Mukim;
import bph.entities.kod.Seksyen;
import bph.utils.DataUtil;

public class SenaraiLaporanRecordModule extends LebahModule{

	private static final long serialVersionUID = 1L;
	private DbPersistence db = new DbPersistence();
	private DataUtil dataUtil = DataUtil.getInstance(db);

	@Override
	public String start() {
		context.put("path", getPath());
		
		//context.put("SelectKodBil", dataUtil.getListKodBilByKategoriBil1());
		
		return getPath() + "/start.vm";
	}
	
	private String getPath() {
		return "bph/laporan/bil";
	}
	
	
	@Command("paparLaporan")
	public String paparLaporan() throws Exception {
		
		String idLaporan = getParam("idLaporan");
		
		context.put("path", getPath());
		
		if(idLaporan.equals("1")){
			
			List<KodBil> KodBilList = dataUtil.getListKodBilByKategoriBil();
			context.put("SelectKodBil", KodBilList);
			
			List<Seksyen> findSeksyen = dataUtil.getListSeksyen();
			context.put("selectSeksyen", findSeksyen);
									
			return getPath() + "/laporanPengurusanBil.vm";
		} 
		
		else if(idLaporan.equals("2")){
			
			List<KodBil> KodBilList = dataUtil.getListKodBilByKategoriBil();
			context.put("SelectKodBil", KodBilList);
			
			List<Seksyen> findSeksyen = dataUtil.getListSeksyen();
			context.put("selectSeksyen", findSeksyen);
			
			return getPath() + "/laporanSenaraiBil.vm";
		}
		
		else{
						
			return "";
		}
	}
	
	@Command("SelectKodBil")
	public String SelectKodBil() throws Exception {
		
		String idKategoriBil = "0";

		if (getParam("idKategoriBil").trim().length() > 0) 
			idKategoriBil = getParam("idKategoriBil");

		List<KodBil> KodBilList = dataUtil.getListKodBilByKategoriBil();
		System.out.println("kod billl " + KodBilList.size());
		context.put("SelectKodBil", KodBilList);
		
		return getPath() + "/SelectKodBil.vm";
	}
	
	@Command("selectMukim")
	public String simpan() throws Exception {
		
		String idDaerah = "0";

		if (getParam("idDaerah").trim().length() > 0) 
			idDaerah = getParam("idDaerah");
		
		List<Mukim> MukimList = dataUtil.getListMukim(idDaerah);
		context.put("selectMukim", MukimList);
	
		return getPath() + "/selectMukim.vm";
	}
	
}
