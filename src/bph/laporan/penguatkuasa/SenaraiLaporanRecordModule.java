package bph.laporan.penguatkuasa;

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
		return "bph/laporan/penguatkuasa";
	}
	
	@Command("paparLaporan")
	public String paparLaporan() throws Exception {
		
		String idLaporan = getParam("idLaporan");
		context.put("path", getPath());
		
		if(idLaporan.equals("1")){
			context.put("selectJenisPelanggaran", dataUtil.getListJenisPelanggaranSyaratUtk());
			return getPath() + "/laporanSenaraiPenghuniYangMelanggarSyarat.vm";
		}
		if(idLaporan.equals("2")){
			context.put("selectJenisPelanggaran", dataUtil.getListJenisPelanggaranSyaratUtk());
			return getPath() + "/laporanSenaraiPenghuniYangHilangKelayakan.vm";
		}
		if(idLaporan.equals("3")){
			context.put("selectJenisPelanggaran", dataUtil.getListJenisPelanggaranSyaratUtk());
			return getPath() + "/laporanSenaraiPenghuniYangDikenakanClamping.vm";
		}		
		else{
			return "";
		}
	}
}