/* Author :
 * zulfazdliabuas@gmail.com Data 2015-2017
 */

package bph.modules.utk;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import bph.entities.kod.LokasiPermohonan;
import bph.entities.kod.Negeri;
import bph.entities.utk.UtkNaziranKebersihan;
import bph.entities.utk.UtkNotis;
import bph.utils.DataUtil;
import bph.utils.Util;

public class SyarikatPembersihanRecordModule extends LebahRecordTemplateModule<UtkNaziranKebersihan>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;

	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public void afterSave(UtkNaziranKebersihan r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void begin() {
		// TODO Auto-generated method stub
		
		dataUtil = DataUtil.getInstance(db);		

		context.remove("selectKawasan");
		context.remove("selectNegeri");
		context.remove("selectDaerah");
		
		List<LokasiPermohonan> kawasanList = dataUtil.getListLokasiPermohonan();
		List<Negeri> negeriList = dataUtil.getListNegeri();
		
		context.put("selectKawasan", kawasanList);
		context.put("selectNegeri", negeriList);
		
		context.put("util", new Util());
		context.put("path", getPath());
		context.put("command", command);
		
		this.setReadonly(true);
		this.setDisableBackButton(true);
	}

	@Override
	public boolean delete(UtkNaziranKebersihan r) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/utk/syarikatPembersihan";
	}

	@Override
	public Class<UtkNaziranKebersihan> getPersistenceClass() {
		// TODO Auto-generated method stub
		return UtkNaziranKebersihan.class;
	}

	@Override
	public void getRelatedData(UtkNaziranKebersihan r) {
		// TODO Auto-generated method stub
		context.put("selectedTab", "1");
	}

	@Override
	public void save(UtkNaziranKebersihan r) throws Exception {

	
	}

	@Override	
	public Map<String, Object> searchCriteria() throws Exception {
	
		String findNoPendaftaran = get("findNoPendaftaran");
		String findNamaKontraktor = get("findNamaKontraktor");
		String findNamaPemilik = get("findNamaPemilik");
		String findKawasan = get("findKawasan");
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("kontraktor.id", findNoPendaftaran);
		map.put("kontraktor.namaKontraktor", findNamaKontraktor);
		map.put("kontraktor.namaPemilik", findNamaPemilik);
		map.put("kontraktor.kawasan.id", findKawasan);
		
		return map;

	}

	/** START SENARAI TAB **/
	
	@SuppressWarnings("unchecked")
	@Command("getKebersihan")
	public String getKebersihan() {

		context.put("selectedTab", "1");

		return getPath() + "/entry_page.vm";
	}
	
	@Command("getNotis")
	public String getNotis() {
		context.remove("idJenisNotis");
		context.remove("flagPeringatan");
		List<UtkNotis> notisList = db.list("Select x from UtkNotis x where x.naziranKebersihan.id='" + get("idPembersihan") + "'") ;
		
		context.put("listNotis", notisList);
		context.put("selectedTab", "2");

		return getPath() + "/entry_page.vm";
	}
	
	/** END SENARAI TAB **/
	
	/** START MAKLUMAT NAZIRAN PEMBERSIHAN**/
	
	@Command("saveKebersihan")
	public String saveKebersihan() {
		String statusInfo="";
		UtkNaziranKebersihan kebersihan = db.find(UtkNaziranKebersihan.class, get("idPembersihan"));

		kebersihan.setStatus(get("statusKerja"));
		
		db.begin();
		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			statusInfo = "error";
		}
        
		context.put("selectedTab", "1");
		return getPath() + "/entry_page.vm";
	}
	
	/** END MAKLUMAT NAZIRAN PEMBERSIHAN**/
	
	/** START MAKLUMAT NOTIS**/
	
	@Command("addNotis")
	public String addNotis() {
		
		context.remove("rekod");
		
		context.put("selectedTab", "2");
		return getPath() + "/notis/popupMaklumatNotis.vm";
	}
	
	@Command("editNotis")
	public String editNotis() {
		int flagPeringatan = 1; 
		context.remove("rekod");
		
		UtkNotis rekod = (UtkNotis) db.get("Select x from UtkNotis x where x.id='" + get("idNotis") + "'") ;
		
		context.put("idJenisNotis", rekod.getFlagJenisNotis());
		context.put("flagPeringatan", rekod.getFlagPeringatan());
		context.put("rekod", rekod);
		
		context.put("selectedTab", "2");
		
		return getPath() + "/notis/popupMaklumatNotis.vm";
	}
	
	@Command("saveNotis")
	public String saveNotis() throws ParseException {
		String statusInfo = "";
		
		UtkNotis n = db.find(UtkNotis.class, get("idNotis"));
		Boolean addMaklumatNotis = false;
		
		if(n == null){
			addMaklumatNotis = true;
			n = new UtkNotis();
		}
		
		n.setNoSiri(get("noSiri"));
		n.setNaziranKebersihan(db.find(UtkNaziranKebersihan.class,get("idPembersihan")));
		n.setFlagJenisNotis(get("idJenisNotis"));
		if(get("idJenisNotis").equals("1"))
			n.setFlagPeringatan(get("idJenisPeringatan"));
		n.setTarikhHantar(getDate("tarikhHantar"));
		n.setCatatan(get("catatan"));
		
		db.begin();
		if ( addMaklumatNotis ) db.persist(n);
		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			statusInfo = "error";
		}
		
		context.put("statusInfo", statusInfo);
		context.put("selectedTab", "2");
		
		return getNotis();
	}
	
	@Command("removeNotis")
	public String removeNotis() {
		String statusInfo = "";
		
		UtkNotis maklumatNotis = db.find(UtkNotis.class, get("idNotis"));
		String maxFlagPeringatan = (String) db.get("Select max(x.flagPeringatan) from UtkNotis x where x.kesalahan.id='" + get("idKesalahan") + "'");
		
		db.begin();
		db.remove(maklumatNotis);
		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			statusInfo = "error";
		}
		
		List<UtkNotis> notisList = db.list("Select x from UtkNotis x where x.kesalahan.id='" + get("idKesalahan") + "'") ;
			
		context.put("listNotis", notisList);
		context.put("selectedTab", "2");
		
		return getPath() + "/entry_page.vm";
	}
		
	/** END MAKLUMAT NOTIS**/
	
	/** START DROP DOWN */
	
	@Command("selectFlagPeringatan")
	public String selectFlagPeringatan() throws Exception {
		String idJenisNotis = "0";
		int flagPeringatan = 1;
		
		if (get("idJenisNotis").trim().length() > 0)
			idJenisNotis = get("idJenisNotis");

		String maxFlagPeringatan = (String) db.get("Select max(x.flagPeringatan) from UtkNotis x where x.kesalahan.id='" + get("idKesalahan") + "'");

		if(maxFlagPeringatan != null){
			if(idJenisNotis.equals("1")){
				flagPeringatan = Integer.parseInt(maxFlagPeringatan);
				if(flagPeringatan >= 1){
					flagPeringatan = flagPeringatan + 1;
				}
			}
		}
			
		context.put("idJenisNotis", idJenisNotis);
		context.put("flagPeringatan", flagPeringatan);
		return getPath() + "/notis/selectPeringatan.vm";
	}
	
	/** END DROP DOWN **/
}
