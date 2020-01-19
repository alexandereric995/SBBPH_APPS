//Author : zulfazdliabuas@gmail.com Data 2015-2017

package bph.modules.utk;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import portal.module.Util;
import bph.entities.kod.Bandar;
import bph.entities.utk.UtkAbt;
import bph.entities.utk.UtkNotis;
import bph.entities.utk.UtkRayuan;
import bph.utils.DataUtil;

public class AbtRecordModule extends LebahRecordTemplateModule<UtkAbt>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Util util = new Util();
	private DataUtil dataUtil;
	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public void afterSave(UtkAbt r) {
		// TODO Auto-generated method stub
		
		context.put("selectedTab", "1");
		
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
		
	} 	

	@Override
	public void begin() {
		// TODO Auto-generated method stub
		
		this.setDisableSaveAddNewButton(true);
//		this.setReadonly(true);
		this.setDisableAddNewRecordButton(true);
		
		if (!"add_new_record".equals(command)) {
			this.setDisableBackButton(true);
			this.setDisableDefaultButton(true);
		}
		
		context.remove("selectNegeri");
		dataUtil = DataUtil.getInstance(db);
		
		context.put("selectNegeri", dataUtil.getListNegeri());
		context.put ("path",getPath());
		context.put ("util",util);
		
		addFilter();
	}
	
	public void addFilter(){

	}

	@Override
	public boolean delete(UtkAbt r) throws Exception {
		// TODO Auto-generated method stub
		
		return true;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/utk/abt";
	}

	@Override
	public Class<UtkAbt> getPersistenceClass() {
		// TODO Auto-generated method stub
		return UtkAbt.class;
	}

	@Override
	public void getRelatedData(UtkAbt r) {
		// TODO Auto-generated method stub
		context.put("selectedTab", "1");
		
	}

	@Override
	public void save(UtkAbt r) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		// TODO Auto-generated method stub

		String noPenghuni = get("noPenghuni");
		String noKp = get("noKp");
		String namaPenghuni = get("namaPenghuni");
		String noUnit = get("noUnit");
		
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("id", noPenghuni);
		map.put("penghuni.pemohon.noKP", noKp);
		map.put("penghuni.pemohon.userName", namaPenghuni);
		map.put("penghuni.kuarters.noUnit", noUnit);
		
		return map;
	}
	
	/** START SENARAI TAB **/
	
	@SuppressWarnings("unchecked")
	@Command("getMaklumatAbt")
	public String getMaklumatAbt() {

		context.put("selectedTab", "1");

		return getPath() + "/entry_page.vm";
	}
	
	@Command("getNotis")
	public String getNotis() {
		context.remove("idJenisNotis");
		context.remove("flagPeringatan");
		List<UtkNotis> notisList = db.list("Select x from UtkNotis x where x.abt.id='" + get("idAbt") + "'") ;
		
		context.put("listNotis", notisList);
		context.put("selectedTab", "2");

		return getPath() + "/entry_page.vm";
	}
	
	@Command("getRayuan")
	public String getRayuan() {

		List<UtkRayuan> rayuanList = db.list("Select x from UtkRayuan x where x.abt.id='" + get("idAbt") + "'") ;
		
		context.put("listRayuan", rayuanList);
		context.put("selectedTab", "3");

		return getPath() + "/entry_page.vm";
	}
	
	/** END SENARAI TAB **/
	
	
	/** START MAKLUMAT ABT **/
	@Command("funcSimpanAtauKemaskiniAbt")
	public String funcSimpanAtauKemaskiniAbt(){
		String statusInfo="";
			
		UtkAbt rekod = db.find(UtkAbt.class, get("idAbt"));
		Boolean simpanMaklumatABT = false;
		
		if(rekod == null){
			simpanMaklumatABT = true;
			rekod = new UtkAbt();
		}
//		rekod.setPenghuni(db.find(KuaPenghuni.class, get("idPenghuni")));
//		rekod.setAmaun(Double.valueOf(Util.RemoveComma(get("amaun"))));
		rekod.setJumlahTunggakan(Double.valueOf(Util.RemoveComma(get("jumlahTunggakan"))));
		rekod.setJumlahBayaran(Double.valueOf(Util.RemoveComma(get("jumlahBayaran"))));
		rekod.setFlagBayaran(getParam("flagBayaran")); // BELUM BAYAR = 1 TELAH BAYAR = 2 TUNGGAKAN = 3
		rekod.setCatatan(getParam("catatan"));
		
		db.begin();
		if ( simpanMaklumatABT ){
			db.persist(rekod);
		}
		
		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			statusInfo = "error";
		}
		
		context.put("r", rekod);
		return getMaklumatAbt();
	}
	/** END MAKLUMAT ABT **/
	
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
		String jenisNotis = "";
		
		UtkNotis n = db.find(UtkNotis.class, get("idNotis"));
		
		Boolean addMaklumatNotis = false;
		
		if(n == null){
			addMaklumatNotis = true;
			n = new UtkNotis();
		}
		
		n.setNoSiri(get("noSiri"));
		n.setAbt(db.find(UtkAbt.class, get("idAbt")));
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
		
		if ( addMaklumatNotis ){
			if("1".equals(n.getFlagJenisNotis()))
				jenisNotis = "PENGELUARAN NOTIS PERINGATAN";
			else if("2".equals(n.getFlagJenisNotis()))
				jenisNotis = "PENGELUARAN NOTIS PENGOSONGAN";
			else if("3".equals(n.getFlagJenisNotis()))
				jenisNotis = "PENGELUARAN NOTIS BAYARAN SEWA";
			
			
			if("1".equals(n.getFlagJenisNotis())){
				if("1".equals(n.getFlagPeringatan())){
					updateStatusAbt(jenisNotis + " PERTAMA");
				}else{
					updateStatusAbt(jenisNotis + " KE-" + n.getFlagPeringatan());
				}
			}else{
				updateStatusAbt(jenisNotis);
			}
		}
		
		context.put("statusInfo", statusInfo);
		context.put("selectedTab", "2");
		
		return getNotis();
	}
	
	@Command("removeNotis")
	public String removeNotis() {
		String statusInfo = "";
		
		UtkNotis maklumatNotis = db.find(UtkNotis.class, get("idNotis"));
		
		db.begin();
		db.remove(maklumatNotis);
		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			statusInfo = "error";
		}
		
		List<UtkNotis> notisList = db.list("Select x from UtkNotis x where x.abt.id='" + get("idAbt") + "'") ;
			
		context.put("listNotis", notisList);
		context.put("selectedTab", "2");
		
		return getPath() + "/entry_page.vm";
	}
		
	/** END MAKLUMAT NOTIS**/
	

	/** START MAKLUMAT RAYUAN**/
	
	@Command("addRayuan")
	public String addRayuan() {
		
		context.remove("rekod");
		context.remove("idJenisRayuan");
		context.remove("flagBilRayuan");
		
		context.put("selectedTab", "3");
		return getPath() + "/rayuan/popupMaklumatRayuan.vm";
	}
	
	@Command("editRayuan")
	public String editRayuan() {
		
		context.remove("rekod");
		
		UtkRayuan rekod = (UtkRayuan) db.get("Select x from UtkRayuan x where x.id='" + get("idRayuan") + "'") ;
		
		context.put("idJenisRayuan", rekod.getFlagJenisRayuan());
		context.put("flagBilRayuan", rekod.getFlagRayuan());
		context.put("rekod", rekod);
		
		context.put("selectedTab", "3");
		return getPath() + "/rayuan/popupMaklumatRayuan.vm";
	}
	
	@Command("saveRayuan")
	public String saveRayuan() throws ParseException {
		String statusInfo = "";
		String keputusanSub = "";
		
		UtkRayuan n = db.find(UtkRayuan.class, get("idRayuan"));
		
		Boolean addMaklumatRayuan = false;
		Boolean addMaklumatKelulusan = false;
		
		if(n == null){
			addMaklumatRayuan = true;
			n = new UtkRayuan();
			if(!"".equals(get("idKelulusan"))){
				addMaklumatKelulusan = true;
			}
		}
		else{
			if((n.getFlagKelulusanSub() == null || "".equals(n.getFlagKelulusanSub()))){
				if(!"".equals(get("idKelulusan")))
					addMaklumatKelulusan = true;
			}
		}
		
		n.setNoRayuan(get("noRayuan"));
		n.setAbt(db.find(UtkAbt.class, get("idAbt")));
		n.setFlagJenisRayuan(get("idJenisRayuan"));
		n.setFlagRayuan(get("idBilanganRayuan"));
		n.setTarikhRayuan(getDate("tarikhRayuan"));
		n.setCatatanRayuan(get("catatanRayuan"));
		n.setTarikhKelulusan(getDate("tarikhKelulusan"));
		n.setFlagKelulusanSub(get("idKelulusan"));
		n.setCatatanKelulusan(get("catatanKelulusan"));
		
		db.begin();
		if ( addMaklumatRayuan ) db.persist(n);
		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			statusInfo = "error";
		}
		
		if ( addMaklumatRayuan ){	
			if("1".equals(n.getFlagRayuan())){
				updateStatusAbt("RAYUAN PERTAMA DALAM PROSES");
			}else{
				updateStatusAbt("RAYUAN KE-" + n.getFlagRayuan() + " DALAM PROSES");
			}
		}
		
		if( addMaklumatKelulusan){
			
			if("L".equals(n.getFlagKelulusanSub()))
				keputusanSub = "LULUS";
			else if("T".equals(n.getFlagKelulusanSub()))
				keputusanSub = "TOLAK";
			
			if("1".equals(n.getFlagRayuan())){
				updateStatusAbt("RAYUAN PERTAMA " + keputusanSub);
			}else{
				updateStatusAbt("RAYUAN KE-" + n.getFlagRayuan() + " " + keputusanSub);
			}
			
		}
		
		context.put("statusInfo", statusInfo);
		context.put("selectedTab", "3");
		
		return getRayuan();
	}
	
	@Command("removeRayuan")
	public String removeRayuan() {
		String statusInfo = "";
		
		UtkRayuan maklumatRayuan = db.find(UtkRayuan.class, get("idRayuan"));
		
		db.begin();
		db.remove(maklumatRayuan);
		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			statusInfo = "error";
		}
		
		List<UtkRayuan> rayuanList = db.list("Select x from UtkRayuan x where x.abt.id='" + get("idAbt") + "'") ;
			
		context.put("listRayuan", rayuanList);
		context.put("selectedTab", "3");
		
		return getPath() + "/entry_page.vm";
	}
		
	/** END MAKLUMAT RAYUAN**/
	
	/** START UPDATE STATUS **/
	
	public void updateStatusAbt(String status){
		String statusInfo = "";
		UtkAbt k = db.find(UtkAbt.class, get("idAbt"));
		
		k.setStatus(status);
		
		db.begin();
		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			statusInfo = "error";
		}
	}
	
	/** END UPDATE STATUS **/
	
	/** START DROP DOWN **/
	
	@Command("findBandar")
	public String findBandar() throws Exception {
		String idNegeri = "0";
		if (get("findNegeri").trim().length() > 0)
			idNegeri = get("findNegeri");
		
		List<Bandar> list = dataUtil.getListBandar(idNegeri);
		context.put("selectBandar", list);

		return getPath() + "/findBandar.vm";
	}
	
	@Command("selectFlagPeringatan")
	public String selectFlagPeringatan() throws Exception {
		String idJenisNotis = "0";
		int flagPeringatan = 1;
		
		if (get("idJenisNotis").trim().length() > 0)
			idJenisNotis = get("idJenisNotis");

		String maxFlagPeringatan = (String) db.get("Select max(x.flagPeringatan) from UtkNotis x where x.abt.id='" + get("idAbt") + "'");

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
	
	@Command("selectFlagRayuan")
	public String selectFlagRayuan() throws Exception {
		String idJenisRayuan = "0";
		int flagRayuan = 0;
		
		if (get("idJenisRayuan").trim().length() > 0)
			idJenisRayuan = get("idJenisRayuan");

		String maxFlagRayuan = (String) db.get("Select max(x.flagRayuan) from UtkRayuan x where x.abt.id='" + get("idAbt") + "' and x.flagJenisRayuan='"+ get("idJenisRayuan") +"'");

			if(maxFlagRayuan != null){
				System.out.println("dalam mex");
				flagRayuan = Integer.parseInt(maxFlagRayuan);
				if(flagRayuan >= 1){
					flagRayuan = flagRayuan + 1;
				}
			}else{
				System.out.println("luarr");
				flagRayuan = 1;
			}
			
		context.put("idJenisRayuan", idJenisRayuan);
		context.put("flagBilRayuan", flagRayuan);
		return getPath() + "/rayuan/selectBilanganRayuan.vm";
	}
	
	/** END DROP DOWN **/

}

