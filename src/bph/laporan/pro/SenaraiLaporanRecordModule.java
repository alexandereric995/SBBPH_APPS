package bph.laporan.pro;

import lebah.portal.action.Command;
import lebah.portal.action.LebahModule;
import lebah.template.DbPersistence;
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
		return "bph/laporan/pro";
	}
	
	@Command("paparLaporan")
	public String paparLaporan() throws Exception {
		
		String idLaporan = getParam("idLaporan");
		context.put("path", getPath());
	
		if(idLaporan.equals("1")){
			context.put("selectSumberAduan", dataUtil.getListSumberAduan());
			context.put("selectJenisAduan", dataUtil.getListJenisAduan());
			context.put("selectUnit", dataUtil.getListProSenaraiUnit());
			return getPath() + "/laporanSenaraiAduanPro.vm";
		}
		if(idLaporan.equals("2")){
			context.put("selectSumberAduan", dataUtil.getListSumberAduan());
			context.put("selectJenisAduan", dataUtil.getListJenisAduan());
			context.put("selectUnit", dataUtil.getListProSenaraiUnit());
			return getPath() + "/laporanStatistikTerimaanAduan.vm";
		}
		if(idLaporan.equals("3")){
			context.put("selectSumberAduan", dataUtil.getListSumberAduan());
			context.put("selectJenisAduan", dataUtil.getListJenisAduan());
			context.put("selectUnit", dataUtil.getListProSenaraiUnit());
			return getPath() + "/laporanStatistikTerimaanAduanBulananMengikutSeksyen.vm";
		}
		if(idLaporan.equals("4")){
			context.put("selectSumberAduan", dataUtil.getListSumberAduan());
			context.put("selectJenisAduan", dataUtil.getListJenisAduan());
			context.put("selectUnit", dataUtil.getListProSenaraiUnit());
			return getPath() + "/laporanStatistikTerimaanAduanTahunanMengikutSeksyen.vm";
		}
		else{
			return "";
		}
	}
	
}
