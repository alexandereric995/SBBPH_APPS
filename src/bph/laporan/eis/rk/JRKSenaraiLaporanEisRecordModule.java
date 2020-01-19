package bph.laporan.eis.rk;

import lebah.portal.action.Command;
import lebah.portal.action.LebahModule;
import lebah.template.DbPersistence;
import bph.utils.DataUtil;

public class JRKSenaraiLaporanEisRecordModule extends LebahModule{

	private static final long serialVersionUID = 1L;
	private DbPersistence db = new DbPersistence();
	private DataUtil dataUtil = DataUtil.getInstance(db);
		
	@Override
	public String start() {
		// TODO Auto-generated method stub
		
		context.put("path", getPath());
		
		return getPath() + "/start.vm";
	}

	private String getPath() {
		// TODO Auto-generated method stub
		return "bph/laporan/eis/rk";
	}
	
	@Command("paparLaporan")
	public String paparLaporan() throws Exception {
		
		String idLaporan = getParam("idLaporan");
		System.out.println("paparLaporan ==== " + idLaporan);
		
		context.put("selectNegeri", dataUtil.getListNegeri());
		context.put("selectBandar", dataUtil.getListBandar());
		context.put("selectSeksyen", dataUtil.getListSeksyen());
		context.put("selectJenisKegunaanRuang", dataUtil.getListJenisKegunaanRuang());
		context.put("path", getPath());
		
		if (idLaporan.equals("1")) {
			return getPath() + "/JRK_SenaraiPelanjutanPenyewaan.vm";
		}else if (idLaporan.equals("2")) {
			return getPath() + "/JRK_SenaraiRuangKomersil.vm";
		} else{
			return "";
		}
	}
		

}
