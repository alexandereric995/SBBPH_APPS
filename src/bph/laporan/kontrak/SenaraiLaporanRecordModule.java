package bph.laporan.kontrak;

import java.util.List;

import lebah.portal.action.Command;
import lebah.portal.action.LebahModule;
import lebah.template.DbPersistence;
import bph.entities.kod.JenisKontrak;
import bph.entities.kod.LantikanKontrak;
import bph.entities.kod.Seksyen;
import bph.utils.DataUtil;
import db.persistence.MyPersistence;

public class SenaraiLaporanRecordModule extends LebahModule{

	private static final long serialVersionUID = 1L;
	private DbPersistence db = new DbPersistence();
	private DataUtil dataUtil = DataUtil.getInstance(db);
	private MyPersistence mp;

	@Override
	public String start() {
		context.put("path", getPath());
		
		return getPath() + "/start.vm";
	}
	
	private String getPath() {
		return "bph/laporan/kontrak";
	}
	
	
	@Command("paparLaporan")
	public String paparLaporan() throws Exception {
		
		String idLaporan = getParam("idLaporan");
		
		context.put("path", getPath());
				
		if(idLaporan.equals("1")){
			List<Seksyen> seksyen = dataUtil.getListSeksyen();
			context.put("selectSeksyen", seksyen);
			
			List<JenisKontrak> jeniskontrak = dataUtil.getListJenisKontrak();
			context.put("selectJenisKontrak", jeniskontrak);

			List<LantikanKontrak> Lantikankontrak = dataUtil.getListLantikanKontrak();
			context.put("selectLantikanKontrak", Lantikankontrak);
			
			return getPath() + "/laporanDaftarBon.vm";
		}
		else if(idLaporan.equals("2")){			
			List<Seksyen> seksyen = dataUtil.getListSeksyen();
			context.put("selectSeksyen", seksyen);
			
			List<JenisKontrak> jeniskontrak = dataUtil.getListJenisKontrak();
			context.put("selectJenisKontrak", jeniskontrak);
			return getPath() + "/laporanSenaraiKontrak.vm";
		} 
		else{
			return "";
		}
	}
		
}
