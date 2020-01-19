/***/

package bph.laporan.eis.bgs;

import lebah.portal.action.Command;
import lebah.portal.action.LebahModule;
import lebah.template.DbPersistence;
import bph.utils.DataUtil;

public class BGSSenaraiLaporanEisRecordModule extends LebahModule {

	private static final long serialVersionUID = 1L;
	private DbPersistence db = new DbPersistence();
	private DataUtil dataUtil = DataUtil.getInstance(db);

	@Override
	public String start() {
				
		context.put("path", getPath());
		return getPath() + "/start.vm";
	}
	
	private String getPath() {
		return "bph/laporan/eis/bgs";
	}
	
	@Command("paparLaporan")
	public String paparLaporan() throws Exception {
		
		String idLaporan = getParam("idLaporan");
		System.out.println("paparLaporan ==== " + idLaporan);
		context.put("path", getPath());
		context.put("selectNegeri", dataUtil.getListNegeri());
		
		context.put("selectAgensi", dataUtil.getListAgensi());
		if (idLaporan.equals("1")) {
			return getPath() + "/BGSLaporanStatusPermohonan.vm";
		} else if (idLaporan.equals("2")) {
			return getPath() + "/BGSLaporanJumlahKeluasan.vm";
		} else{
			return "";
		}
	}
	
	
//	/** START DROP DOWN LIST **/
//	@Command("findDaerah")
//	public String findDaerah() throws Exception {	
//		
//		String idNegeri = "0";
//		if (getParam("findNegeri").trim().length() > 0)
//			idNegeri = getParam("findNegeri");
//		List<Daerah> list = dataUtil.getListDaerah(idNegeri);
//		context.put("selectDaerah", list);
//		
//		return getPath() + "/findDaerah.vm";
//	}
//	
//	@Command("findMukim")
//	public String findMukim() throws Exception {
//		
//		String idDaerah = "0";
//		if (getParam("findDaerah").trim().length() > 0)
//			idDaerah = getParam("findDaerah");
//	
//		List<Mukim> list = dataUtil.getListMukim(idDaerah);
//		context.put("selectMukim", list);
//
//		return getPath() + "/findMukim.vm";
//	}
	
//	@Command("findAgensi")
//	public String findAgensi() throws Exception {
//		
////		String idKementerian = "0";
////		if (getParam("findKementerian").trim().length() > 0){
////			idKementerian = getParam("findKementerian");
////		}
//		
//		List<Agensi> list = dataUtil.getListAgensi();
//		context.put("selectAgensi", list);
//
//		return getPath() + "/findAgensi.vm";
//	}
	/** END DROP DOWN LIST **/
}
