package bph.laporan.jrp;

import java.util.List;

import lebah.portal.action.Command;
import lebah.portal.action.LebahModule;
import lebah.template.DbPersistence;
import bph.entities.kod.Agensi;
import bph.entities.kod.Bandar;
import bph.entities.kod.JenisPermohonanJRP;
import bph.entities.kod.Kementerian;
import bph.entities.kod.Negeri;
import bph.entities.kod.Status;
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
		return "bph/laporan/jrp";
	}
	
	
	@Command("paparLaporan")
	public String paparLaporan() throws Exception {
		
		context.remove("jenisLaporan");
		context.remove("selectKementerian");
		context.remove("selectNegeri");
		
		String idLaporan = getParam("idLaporan");
		
		context.put("path", getPath());
		

		dataUtil = DataUtil.getInstance(db);
		
		List<Negeri> negeriList = dataUtil.getListNegeri();
		List<Kementerian> kementerianList = dataUtil.getListKementerian();
		List<JenisPermohonanJRP> jenisPermohonanJRP = dataUtil.getListJenisPermohonanJRP();
		List<Status> statusPermohonan = dataUtil.getListStatusJawatankuasaRuangPejabat();
		
		context.put("selectNegeri", negeriList);
		context.put("selectKementerian", kementerianList);
		context.put("selectJenisPermohonanJRP", jenisPermohonanJRP);
		context.put("jenisLaporan", idLaporan);
		context.put("selectStatusPermohonan", statusPermohonan);
		
		
		if(idLaporan.equals("sewaRuang")){
			return getPath() + "/laporanPenyewaanJRP.vm";
		}
		else if(idLaporan.equals("maklumatRuang")){						
			return getPath() + "/laporanMaklumatRuang.vm";
		} 
		else if(idLaporan.equals("keputusanMesyuarat")){					
			return getPath() + "/laporanKeputusanMensyuaratJRP.vm";
		} 
		else if(idLaporan.equals("senaraiPemohon")){					
			return getPath() + "/laporanSenaraiPemohon.vm";
		}
		else if(idLaporan.equals("maklumatPemohon")){		
			return getPath() + "/laporanMaklumatPemohon.vm";
		}
		else if(idLaporan.equals("statistikKelulusan")){					
			return getPath() + "/laporanStatistikKelulusanMesyuarat.vm";
		}
		else if(idLaporan.equals("statistikMensyuarat")){					
			return getPath() + "/laporanStatistikKeputusanMesyuarat.vm";
		}else{			
			return "";
		}
	}
	
	@Command("findBandar")
	public String findBandar() throws Exception {
		String idNegeri = "0";
		if (getParam("findNegeri").trim().length() > 0)
			idNegeri = getParam("findNegeri");
		
		List<Bandar> list = dataUtil.getListBandar(idNegeri);
		context.put("selectBandar", list);

		return getPath() + "/findBandar.vm";
	}
	
	@Command("findAgensi")
	public String findAgensi() throws Exception {
		String idKementerian = "0";
		if (getParam("findKementerian").trim().length() > 0)
			idKementerian = getParam("findKementerian");
		
		List<Agensi> list = dataUtil.getListAgensi(idKementerian);
		context.put("selectAgensi", list);

		return getPath() + "/findAgensi.vm";
	}
}
