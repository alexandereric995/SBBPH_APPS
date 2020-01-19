/**

 */

package bph.laporan.pembangunan;

import java.util.List;

import lebah.portal.action.Command;
import lebah.portal.action.LebahModule;
import lebah.template.DbPersistence;
import bph.entities.kod.Agensi;
import bph.entities.kod.Daerah;
import bph.entities.kod.Mukim;
import bph.utils.DataUtil;

public class SenaraiLaporanDinamikTanah extends LebahModule {

	private static final long serialVersionUID = 1L;
	private DbPersistence db = new DbPersistence();
	private DataUtil dataUtil = DataUtil.getInstance(db);

	@Override
	public String start() {
		
		context.put("selectJenisHakmilik", dataUtil.getListJenisHakMilik());
		context.put("selectJenisLot",dataUtil.getListLot());
		context.put("selectNegeri", dataUtil.getListNegeri());
		context.put("selectKategoriTanah", dataUtil.getListKategoriTanah());
		
		
		context.put("path", getPath());
		return getPath() + "/start.vm";
	}
	
	private String getPath() {
		return "bph/laporan/pembangunan/dinamik/tanah";
	}
	
	/** START DROP DOWN LIST **/
	@Command("findDaerah")
	public String findDaerah() throws Exception {	
		
		String idNegeri = "0";
		if (getParam("findNegeri").trim().length() > 0)
			idNegeri = getParam("findNegeri");
		List<Daerah> list = dataUtil.getListDaerah(idNegeri);
		context.put("selectDaerah", list);
		
		return getPath() + "/findDaerah.vm";
	}
	
	@Command("findMukim")
	public String findMukim() throws Exception {
		
		String idDaerah = "0";
		if (getParam("findDaerah").trim().length() > 0)
			idDaerah = getParam("findDaerah");
	
		List<Mukim> list = dataUtil.getListMukim(idDaerah);
		context.put("selectMukim", list);

		return getPath() + "/findMukim.vm";
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
	/** END DROP DOWN LIST **/
}
