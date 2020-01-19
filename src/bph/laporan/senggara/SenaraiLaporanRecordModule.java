package bph.laporan.senggara;

import java.util.List;

import lebah.portal.action.Command;
import lebah.portal.action.LebahModule;
import lebah.template.DbPersistence;
import bph.entities.kod.Daerah;
import bph.entities.kod.KelasKuarters;
import bph.entities.kod.LokasiKuarters;
import bph.entities.kod.Negeri;
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
	//private DataUtil dateUtil;

	
	private String getPath() {
		return "bph/laporan/senggara";
	}
	
	@Command("paparLaporan")
	public String paparLaporan() throws Exception {
		context.remove("selectNegeri");		
		
		dataUtil = DataUtil.getInstance(db);		
		
		List<Negeri> negeriList = dataUtil.getListNegeri();	
		
		context.put("selectNegeri", negeriList);	
		
		String idLaporan = getParam("idLaporan");	
		
		context.put("path", getPath());
		
		if(idLaporan.equals("1")){
			return getPath() + "/laporanSenaraiDaftarKontraktor.vm";
		}
		else
		if(idLaporan.equals("2")){
			return getPath() + "/laporanSenaraiHitamKontraktor.vm";
		}
		else
		if(idLaporan.equals("3")){
			List<KelasKuarters> KelasPeranginanList = dataUtil.getListKelasPeranginan();
			context.put("selectKelasKuarters", KelasPeranginanList);
			
			List<LokasiKuarters> LokasiKuartersList = dataUtil.getListLokasiKuarters();
			context.put("selectLokasiKuarters", LokasiKuartersList);
			return getPath() + "/laporanSenaraiAduanSenggara.vm";
		}
		else
		if(idLaporan.equals("4")){
			return getPath() + "/laporanStatistikRekodKuartersSenggara.vm";
		}
		else
		if(idLaporan.equals("5")){
			return getPath() + "/laporanStatistikSumberAduan.vm";
		}
		else
			if(idLaporan.equals("6")){
				return getPath() + "/laporanSenaraiKuartersSenggara.vm";
			}
		return "";
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
	
	
}
