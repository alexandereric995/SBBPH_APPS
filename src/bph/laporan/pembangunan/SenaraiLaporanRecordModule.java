/**

 */

package bph.laporan.pembangunan;

import java.util.List;

import lebah.portal.action.Command;
import lebah.portal.action.LebahModule;
import lebah.template.DbPersistence;
import bph.entities.kod.Daerah;
import bph.entities.kod.Mukim;
import bph.entities.kod.Negeri;
import bph.utils.DataUtil;

public class SenaraiLaporanRecordModule extends LebahModule {

	private static final long serialVersionUID = 1L;
	private DbPersistence db = new DbPersistence();
	private DataUtil dataUtil = DataUtil.getInstance(db);

	@Override
	public String start() {
		context.put("path", getPath());
		return getPath() + "/start.vm";
	}
	
	private String getPath() {
		return "bph/laporan/pembangunan/statistik";
	}
	
	
	@Command("paparLaporan")
	public String paparLaporan() throws Exception {
		context.remove("selectNegeri");
		
		String idLaporan = getParam("idLaporan");
		
		context.put("path", getPath());
		

		dataUtil = DataUtil.getInstance(db);
		
		List<Negeri> negeriList = dataUtil.getListNegeri();
		context.put("selectNegeri", negeriList);
		
		if(idLaporan.equals("1")){
			return getPath() + "/laporanSenaraiHakmilikMengikutTarikhDaftar.vm";
		}
		else if(idLaporan.equals("2")){	
			return getPath() + "/laporanSenaraiHakmilik.vm";
		}
		else if(idLaporan.equals("3")){	
			return getPath() + "/maklumatDaftarAsetKhusus.vm";
		}
		else if(idLaporan.equals("5")){	
			return getPath() + "/maklumatDaftarPremisAset.vm";
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
