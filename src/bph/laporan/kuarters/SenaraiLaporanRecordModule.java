package bph.laporan.kuarters;

import java.util.List;

import lebah.portal.action.Command;
import lebah.portal.action.LebahModule;
import lebah.template.DbPersistence;
import bph.entities.kod.Daerah;
import bph.entities.kod.LokasiPermohonan;
import bph.entities.kod.Mukim;
import bph.utils.DataUtil;

public class SenaraiLaporanRecordModule extends LebahModule{

	private static final long serialVersionUID = 1L;
	private DbPersistence db = new DbPersistence();
	private DataUtil dataUtil = DataUtil.getInstance(db);

	@Override
	public String start() {
		context.put("path", getPath());
		return getPath() + "/start.vm";
	}
	
	private String getPath() {
		return "bph/laporan/kuarters";
	}
	
	
	@Command("paparLaporan")
	public String paparLaporan() throws Exception {
		
		context.remove("lokasiPermohonan");
		
		dataUtil = DataUtil.getInstance(db);		
		String idLaporan = getParam("idLaporan");		
			
		context.put("path", getPath());
		
		if(idLaporan.equals("1")){
			List<LokasiPermohonan> lokasiPermohonanList = dataUtil.getListLokasiPermohonan();
			context.put("selectlokasiPermohonan", lokasiPermohonanList);
			return getPath() + "/laporanSenaraiKuartersKosong.vm";
		}
		else if(idLaporan.equals("2")){			
			
			return getPath() + "/laporanSenaraiKuartersDalamPembaikan.vm";
		} 
		else if(idLaporan.equals("3")){			
			
			return getPath() + "/laporanSenaraiKuartersSiapPembaikan.vm";
		}
		else if(idLaporan.equals("4")){			
			
			return getPath() + "/laporanSenaraiPermohonanKuarters.vm";
		}
		else if(idLaporan.equals("5")){			
			
			return getPath() + "/laporanSenaraiMenungguKuarters.vm";
		}
		else if(idLaporan.equals("6")){			
			
			return getPath() + "/laporanSenaraiPenghuniKuarters.vm";
		}
		else if(idLaporan.equals("7")){			
			
			return getPath() + "/laporanSenaraieTemujanji.vm";
		}
		else{
			
			
			return "";
		}
	}
	
	@Command("selectDaerah")
	public String selectDaerah() throws Exception {
		
		String idNegeri = "0";

		if (getParam("idNegeri").trim().length() > 0) 
			idNegeri = getParam("idNegeri");

		List<Daerah> DaerahList = dataUtil.getListDaerah(idNegeri);
		context.put("selectDaerah", DaerahList);
		
		return getPath() + "/selectDaerah.vm";
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
