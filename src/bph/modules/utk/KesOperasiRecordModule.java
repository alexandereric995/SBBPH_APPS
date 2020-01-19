// Author : zulfazdliabuas@gmail.com Data 2015-2017

package bph.modules.utk;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import portal.module.Util;
import bph.entities.kod.Bandar;
import bph.entities.utk.UtkKesPeguam;
import bph.entities.utk.UtkKesalahan;
import bph.entities.utk.UtkNotis;
import bph.entities.utk.UtkRayuan;
import bph.utils.DataUtil;

public class KesOperasiRecordModule extends LebahRecordTemplateModule<UtkKesalahan>{

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
	public void afterSave(UtkKesalahan arg0) {
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
		this.setDisableAddNewRecordButton(true);
		this.setDisableDefaultButton(true);
		this.setDisableBackButton(true);
		this.setReadonly(true);
		
		context.remove("pekerjaan");
		context.remove("selectNegeri");
		
		dataUtil = DataUtil.getInstance(db);
		
		context.put("selectNegeri", dataUtil.getListNegeri());
		
		context.put ("path",getPath());
		context.put ("util",util);
		
		addFilter();
	}
	
	public void addFilter(){
		
		this.addFilter("operasi.jenisOperasi.id not in ('PS','CP','NK')"); // SELAIN PELANGGARAN SYARAT, CLAMPING, NAZIRAN KEBERSIHAN
	}

	@Override
	public boolean delete(UtkKesalahan arg0) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/utk/kesOperasi";
	}

	@Override
	public Class<UtkKesalahan> getPersistenceClass() {
		// TODO Auto-generated method stub
		return UtkKesalahan.class;
	}

	@Override
	public void getRelatedData(UtkKesalahan r) {
		// TODO Auto-generated method stub

		context.put("selectedTab", "1");
		
	}

	@Override
	public void save(UtkKesalahan r) throws Exception {
		// TODO Auto-generated method stub
//		r.setStatus(get("status"));
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		// TODO Auto-generated method stub

		String noPenghuni = get("noPenghuni");
		String noKp = get("noKp");
		String namaPenghuni = get("namaPenghuni");
		String noUnit = get("noUnit");
//		String findNegeri = get("findNegeri");
//		String findBandar = get("findBandar");

		
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("id", noPenghuni);
		map.put("pemohon.noKP", noKp);
		map.put("pemohon.userName", namaPenghuni);
		map.put("kuarters.noUnit", noUnit);
//		map.put("kuarters.bandar.negeri.id", findNegeri);
//		map.put("kuarters.bandar.id", findBandar);
		
		return map;
	}
	
	/** START SENARAI TAB **/
	
	@SuppressWarnings("unchecked")
	@Command("getPelanggaranSyarat")
	public String getPelanggaranSyarat() {

		context.put("selectedTab", "1");

		return getPath() + "/entry_page.vm";
	}
	
	@Command("getNotis")
	public String getNotis() {
		context.remove("idJenisNotis");
		context.remove("flagPeringatan");
		List<UtkNotis> notisList = db.list("Select x from UtkNotis x where x.kesalahan.id='" + get("idKesalahan") + "'") ;
		
		context.put("listNotis", notisList);
		context.put("selectedTab", "2");

		return getPath() + "/entry_page.vm";
	}
	
	@Command("getRayuan")
	public String getRayuan() {

		List<UtkRayuan> rayuanList = db.list("Select x from UtkRayuan x where x.kesalahan.id='" + get("idKesalahan") + "'") ;
		
		context.put("listRayuan", rayuanList);
		context.put("selectedTab", "3");

		return getPath() + "/entry_page.vm";
	}
	
	@Command("getKesPeguam")
	public String getKesPeguam() {

		UtkKesPeguam rekod = (UtkKesPeguam) db.get("Select x from UtkKesPeguam x where x.kesalahan.id='" + get("idKesalahan") + "'") ;
		
		context.put("rekod", rekod);
		context.put("selectedTab", "4");

		return getPath() + "/entry_page.vm";
	}
	
	/** END SENARAI TAB **/
	
	/** START UPDATE STATUS PENGHUNI **/
	
	@Command("saveStatusPenghuni")
	public String saveStatusPenghuni(){
		String statusInfo = "";
		UtkKesalahan k = db.find(UtkKesalahan.class, get("idKesalahan"));
		
		k.setStatusPenghuni(get("statusPenghuni"));
		
		db.begin();
		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			statusInfo = "error";
		}
		
		return getPelanggaranSyarat();
	}
	
	/** END UPDATE STATUS PENGHUNI **/
	
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
		n.setKesalahan(db.find(UtkKesalahan.class,get("idKesalahan")));
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
					updateStatusKesalahan(jenisNotis + " PERTAMA");
				}else{
					updateStatusKesalahan(jenisNotis + " KE-" + n.getFlagPeringatan());
				}
			}else{
				updateStatusKesalahan(jenisNotis);
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
			if(n.getFlagKelulusanSub() == null || "".equals(n.getFlagKelulusanSub())){
				if(!"".equals(get("idKelulusan")))
					addMaklumatKelulusan = true;
			}
		}
		
		n.setNoRayuan(get("noRayuan"));
		n.setKesalahan(db.find(UtkKesalahan.class,get("idKesalahan")));
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
		System.out.println("cekkkkkkkkkkkk =============" + addMaklumatKelulusan);
		
		if ( addMaklumatRayuan ){	
			if("1".equals(n.getFlagRayuan())){
				updateStatusKesalahan("RAYUAN PERTAMA DALAM PROSES");
			}else{
				updateStatusKesalahan("RAYUAN KE-" + n.getFlagRayuan() + " DALAM PROSES");
			}
		}
		
		if(addMaklumatKelulusan){
			
			if("L".equals(n.getFlagKelulusanSub()))
				keputusanSub = "LULUS";
			else if("T".equals(n.getFlagKelulusanSub()))
				keputusanSub = "TOLAK";
			
			if("1".equals(n.getFlagRayuan())){
				updateStatusKesalahan("RAYUAN PERTAMA " + keputusanSub);
			}else{
				updateStatusKesalahan("RAYUAN KE-" + n.getFlagRayuan() + " " + keputusanSub);
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
		
		List<UtkRayuan> rayuanList = db.list("Select x from UtkRayuan x where x.kesalahan.id='" + get("idKesalahan") + "'") ;
			
		context.put("listRayuan", rayuanList);
		context.put("selectedTab", "3");
		
		return getPath() + "/entry_page.vm";
	}
		
	/** END MAKLUMAT RAYUAN**/
	
	/** START MAKLUMAT KES PEGUAM **/
	
	@Command("saveKesPeguam")
	public String saveKesPeguam() throws ParseException {
		String statusInfo = "";
		String keputusan = "";
		
		UtkKesPeguam kp = db.find(UtkKesPeguam.class, get("idKesPeguam"));
		Boolean addMaklumatKesPeguam = false;
		
		if(kp == null){
			addMaklumatKesPeguam = true;
			kp = new UtkKesPeguam();
		}
		
		kp.setKesalahan(db.find(UtkKesalahan.class,get("idKesalahan")));
		kp.setTarikhKeputusan(getDate("tarikhKeputusan"));
		kp.setFlagKeputusan(get("flagKeputusan"));
		kp.setCatatan(get("catatan"));
		
		db.begin();
		if ( addMaklumatKesPeguam ) db.persist(kp);
		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			statusInfo = "error";
		}
		
		if ( addMaklumatKesPeguam ){
			if("1".equals(get("flagKeputusan")))
				keputusan = "BERSALAH";
			else if("2".equals(get("flagKeputusan")))
				keputusan = "TIDAK BERSALAH";

			updateStatusKesalahan("KEPUTUSAN KES PEGUAM " + keputusan);
		}
		
		context.put("statusInfo", statusInfo);
		context.put("selectedTab", "4");
		
		return getKesPeguam();
	}
	
	/** END MAKLUMAT KES PEGUAM **/
	
	/** START UPDATE STATUS **/
	
	public void updateStatusKesalahan(String status){
		String statusInfo = "";
		UtkKesalahan k = db.find(UtkKesalahan.class, get("idKesalahan"));
		
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
	
	@Command("selectFlagRayuan")
	public String selectFlagRayuan() throws Exception {
		String idJenisRayuan = "0";
		int flagRayuan = 0;
		
		if (get("idJenisRayuan").trim().length() > 0)
			idJenisRayuan = get("idJenisRayuan");

		String maxFlagRayuan = (String) db.get("Select max(x.flagRayuan) from UtkRayuan x where x.kesalahan.id='" + get("idKesalahan") + "' and x.flagJenisRayuan='"+ get("idJenisRayuan") +"'");

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

