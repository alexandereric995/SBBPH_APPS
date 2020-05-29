// Author : zulfazdliabuas@gmail.com Data 2015-2017

package bph.modules.utk;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import portal.module.Util;
import portal.module.entity.Users;
import portal.module.entity.UsersJob;
import bph.entities.kod.Bandar;
import bph.entities.kod.SebabHilangKelayakanUtk;
import bph.entities.qtr.KuaKuarters;
import bph.entities.qtr.KuaPenghuni;
import bph.entities.utk.UtkHilangKelayakan;
import bph.entities.utk.UtkKesalahan;
import bph.entities.utk.UtkLanjutanHK;
import bph.entities.utk.UtkNotis;
import bph.entities.utk.UtkRayuan;
import bph.utils.DataUtil;

public class HilangKelayakanBersaraRecordModule extends LebahRecordTemplateModule<UtkHilangKelayakan>{

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
	public void afterSave(UtkHilangKelayakan r) {
		// TODO Auto-generated method stub
		
		KuaPenghuni penghuni = (KuaPenghuni) db.get("Select x from KuaPenghuni x where x.pemohon.noKP='"+ r.getPenghuni().getPemohon().getNoKP() + "'");		
		context.put("penghuni", penghuni);
		
		UsersJob pekerjaan = (UsersJob) db.get("Select x from UsersJob x where x.users.id='" + r.getPenghuni().getPemohon().getId() + "'");
		context.put("pekerjaan", pekerjaan);
		
		KuaKuarters kuarters = (KuaKuarters) db.get("Select x from KuaKuarters x where x.id='" + r.getPenghuni().getKuarters().getId() + "'");
		context.put("kuarters", kuarters);
		
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
		
		if (!"add_new_record".equals(command)) {
			this.setDisableBackButton(true);
			this.setDisableDefaultButton(true);
		}
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		userName = (String) request.getSession().getAttribute("_portal_username");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		context.put("userRole", userRole);
		
		context.remove("penghuni");
		context.remove("pekerjaan");
		context.remove("kuarters");
		
		context.remove("selectNegeri");
		dataUtil = DataUtil.getInstance(db);
		
		List<SebabHilangKelayakanUtk> sebabHilangKelayakanUtkList = dataUtil.getListSebabHilangKelayakanUtk();
		context.put("selectSebabHk", sebabHilangKelayakanUtkList);
		
		context.put("selectNegeri", dataUtil.getListNegeri());
		context.put ("path",getPath());
		context.put ("util",util);
		
		addFilter();
	}
	
	public void addFilter(){

	}

	@Override
	public boolean delete(UtkHilangKelayakan r) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("ceking ========= " + get("idHilangKelayakan"));
		List<UtkNotis> listNotis = db.list("select x from UtkNotis x where x.hilangKelayakan.id='" + r.getId() +"'");
		List<UtkRayuan> listRayuan = db.list("select x from UtkRayuan x where x.hilangKelayakan.id='" + r.getId() +"'");
		
		db.begin();
		for (UtkNotis c : listNotis)
			db.remove(c);
		for(UtkRayuan d : listRayuan)
			db.remove(d);
		
		try {
			db.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
		
		return true;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/utk/hilangKelayakanBersara";
	}

	@Override
	public Class<UtkHilangKelayakan> getPersistenceClass() {
		// TODO Auto-generated method stub
		return UtkHilangKelayakan.class;
	}

	@Override
	public void getRelatedData(UtkHilangKelayakan r) {
		// TODO Auto-generated method stub
		context.remove("penghuni");
		context.remove("pekerjaan");
		context.remove("kuarters");
		
		KuaPenghuni penghuni = (KuaPenghuni) db.get("Select x from KuaPenghuni x where x.pemohon.noKP='"+ r.getPenghuni().getPemohon().getNoKP() + "'");		
		context.put("penghuni", penghuni);
		
		UsersJob pekerjaan = (UsersJob) db.get("Select x from UsersJob x where x.users.id='" + r.getPenghuni().getPemohon().getId() + "'");
		context.put("pekerjaan", pekerjaan);
		
		KuaKuarters kuarters = (KuaKuarters) db.get("Select x from KuaKuarters x where x.id='" + r.getPenghuni().getKuarters().getId() + "'");
		context.put("kuarters", kuarters);
		
		context.put("selectedTab", "1");
		
	}

	@Override
	public void save(UtkHilangKelayakan r) throws Exception {
		// TODO Auto-generated method stub
		userId = (String) request.getSession().getAttribute("_portal_login");
		
		//START FUNCTION SAVE HILANG KELAYAKAN UNTUK FIRST DATA
		String idPenghuni = (String) db.get("Select x.id from KuaPenghuni x where x.pemohon.noKP='" + get("noKadPengenalan") + "'");
		r.setPenghuni(db.find(KuaPenghuni.class, idPenghuni));
//		r.setFlagSebab(db.find(SebabHilangKelayakanUtk.class, "1")); //BERSARA
		r.setFlagSebab(db.find(SebabHilangKelayakanUtk.class, get("idSebabHk")));
		r.setTarikh(getDate("tarikh"));
		r.setCatatan(get("catatan"));
		r.setStatusPenghuni(get("statusPenghuni"));
		r.setCola(getParam("cola"));
		r.setIwk(getParam("iwk"));
		r.setKadarBiasa(getParam("kadarBiasa"));
		r.setKadarPasaran(getParam("kadarPasaran"));
		r.setSlipGaji(getParam("slipGaji"));
		r.setSurathilangKelayakan(getParam("surathilangKelayakan"));
		r.setTarikhHilangKelayakan(getDate("tarikhHilangKelayakan"));
		r.setTarikhMulaBiasa(getDate("tarikhMulaBiasa"));
		r.setTarikhMulaCola(getDate("tarikhMulaCola"));
		r.setTarikhMulaIwk(getDate("tarikhMulaIwk"));
		r.setTarikhMulaPasaran(getDate("tarikhMulaPasaran"));
		r.setTarikhTamatBiasa(getDate("tarikhTamatBiasa"));
		r.setTarikhTamatCola(getDate("tarikhTamatCola"));
		r.setTarikhTamatHilangKelayakan(getDate("tarikhTamatHilangKelayakan"));
		r.setTarikhTamatIwk(getDate("tarikhTamatIwk"));
		r.setTarikhTamatPasaran(getDate("tarikhTamatPasaran"));
		r.setSurathilangKelayakan(getParam("suratHk"));
		r.setSlipGaji(getParam("slipGaji"));
		r.setDaftarOleh(db.find(Users.class, userId));
		r.setTarikhMasuk(new Date());		
	
	}
	
	//START FUNCTION SAVE KEMASKINI HILANG KELAYAKAN
	@Command("saveMaklumatBersara")
	public String saveMaklumatBersara(){
		userId = (String) request.getSession().getAttribute("_portal_login");
		String statusInfo = "";
		
		UtkHilangKelayakan hk = db.find(UtkHilangKelayakan.class, get("idHilangKelayakan"));
		Boolean addMaklumatHk = false;
		
		if(hk == null){
			addMaklumatHk = true;
			hk = new UtkHilangKelayakan();
		}
		System.out.println("PRINT ========= " + get("noKad"));
		String idPenghuni = (String) db.get("Select x.id from KuaPenghuni x where x.pemohon.noKP='" + get("noKadPengenalan") + "'");
		hk.setPenghuni(db.find(KuaPenghuni.class, idPenghuni));
//		hk.setFlagSebab(db.find(SebabHilangKelayakanUtk.class, "1")); //BERSARA
		hk.setFlagSebab(db.find(SebabHilangKelayakanUtk.class, get("idSebabHk")));
		hk.setTarikh(getDate("tarikh"));
		hk.setCatatan(get("catatan"));
		hk.setStatusPenghuni(get("statusPenghuni"));
		hk.setCola(getParam("cola"));
		hk.setIwk(getParam("iwk"));
		hk.setKadarBiasa(getParam("kadarBiasa"));
		hk.setKadarPasaran(getParam("kadarPasaran"));
		hk.setSlipGaji(getParam("slipGaji"));
		hk.setSurathilangKelayakan(getParam("surathilangKelayakan"));
		hk.setTarikhHilangKelayakan(getDate("tarikhHilangKelayakan"));
		hk.setTarikhMulaBiasa(getDate("tarikhMulaBiasa"));
		hk.setTarikhMulaCola(getDate("tarikhMulaCola"));
		hk.setTarikhMulaIwk(getDate("tarikhMulaIwk"));
		hk.setTarikhMulaPasaran(getDate("tarikhMulaPasaran"));
		hk.setTarikhTamatBiasa(getDate("tarikhTamatBiasa"));
		hk.setTarikhTamatCola(getDate("tarikhTamatCola"));
		hk.setTarikhTamatHilangKelayakan(getDate("tarikhTamatHilangKelayakan"));
		hk.setTarikhTamatIwk(getDate("tarikhTamatIwk"));
		hk.setTarikhTamatPasaran(getDate("tarikhTamatPasaran"));
		hk.setSurathilangKelayakan(getParam("suratHk"));
		hk.setSlipGaji(getParam("slipGaji"));
		
		hk.setKemaskiniOleh(db.find(Users.class, userId));
		hk.setTarikhKemaskini(new Date());
		
		db.begin();
		if ( addMaklumatHk ) db.persist(hk);
		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			statusInfo = "error";
		}
		
		context.put("statusInfo", statusInfo);
		context.put("selectedTab", "1");
		
		return getMaklumatPenghuni();
//		return getNotis();
	}
	//END FUNCTION SAVE MAKLUMAT HILANG KELAYAKAN
	
	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		// TODO Auto-generated method stub

		String noPenghuni = get("noPenghuni");
		String noKp = get("noKp");
		String namaPenghuni = get("namaPenghuni");
		String noUnit = get("noUnit");
		
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("penghuni.id", noPenghuni);
		map.put("penghuni.pemohon.id", noKp);
		map.put("penghuni.pemohon.userName", namaPenghuni);
		map.put("penghuni.kuarters.noUnit", noUnit);
		
		return map;
	}
	
	/** START SENARAI TAB **/
	
	@SuppressWarnings("unchecked")
	@Command("getMaklumatPenghuni")
	public String getMaklumatPenghuni() {

		context.remove("penghuni");
		context.remove("pekerjaan");
		context.remove("kuarters");

		UtkHilangKelayakan hilangkelayakan = (UtkHilangKelayakan) db.get("Select x from UtkHilangKelayakan x where x.id='" + get("idHilangKelayakan") + "'");
		
		KuaPenghuni penghuni = (KuaPenghuni) db.get("Select x from KuaPenghuni x where x.id='"+ hilangkelayakan.getPenghuni().getId() + "'");
		context.put("penghuni", penghuni);
		
		if(penghuni != null){
		UsersJob pekerjaan = (UsersJob) db.get("Select x from UsersJob x where x.users.id='" + penghuni.getPemohon().getNoKP() + "'");
		context.put("pekerjaan", pekerjaan);
		
		KuaKuarters kuarters = (KuaKuarters) db.get("Select x from KuaKuarters x where x.id='" + penghuni.getKuarters().getId() + "'");
		context.put("kuarters", kuarters);
		}
		context.put("selectedTab", "1");

		return getPath() + "/entry_page.vm";
	}
	
	@Command("getNotis")
	public String getNotis() {
		context.remove("idJenisNotis");
		context.remove("flagPeringatan");
		List<UtkNotis> notisList = db.list("Select x from UtkNotis x where x.hilangKelayakan.id='" + get("idHilangKelayakan") + "'") ;
		
		context.put("listNotis", notisList);
		context.put("selectedTab", "2");

		return getPath() + "/entry_page.vm";
	}
	
	
	
	@Command("getRayuan")
	public String getRayuan() {

		List<UtkRayuan> rayuanList = db.list("Select x from UtkRayuan x where x.hilangKelayakan.id='" + get("idHilangKelayakan") + "'") ;
		
		context.put("listRayuan", rayuanList);
		context.put("selectedTab", "3");

		return getPath() + "/entry_page.vm";
	}
	
	@Command("getPerlanjutan")
	public String getPerlanjutan() {
		
		List<UtkLanjutanHK> notisList = db.list("Select x from UtkLanjutanHK x where x.hilangKelayakan.id='" + get("idHilangKelayakan") + "'") ;
		
		context.put("listNotis", notisList);
		context.put("selectedTab", "4");

		return getPath() + "/entry_page.vm";
	}
	
	/** END SENARAI TAB **/
	
	/** START CARIAN PENGHUNI **/
	
	@Command("cariPenghuni")
	public String cariPenghuni() {
		
		String noKp = get("kadPengenalan");
		
		List<KuaPenghuni> penghuniList = searchListPenghuni(noKp);
		System.out.println("ceking ====== " + penghuniList.size());
		context.put("penghuniList", penghuniList);
		
		return getPath() + "/popupCarianPenghuni.vm";
	}
	
		private List<KuaPenghuni> searchListPenghuni(String noKp) {
		
		List<KuaPenghuni> list = new ArrayList<KuaPenghuni>();

			String sql = "select x from KuaPenghuni x";
			
			if(!noKp.equalsIgnoreCase("")){
				sql = sql + " where x.pemohon.noKP like '%" + noKp + "%'";
			}
			
			System.out.println("ceking sql =========== " + sql);
			 list = db.list(sql);

		return list;
	}
		
	@Command("savePilihanPenghuni")
	public String savePilihanPenghuni() throws Exception {
		
		context.remove("penghuni");
		context.remove("pekerjaan");
		context.remove("kuarters");
		
		String penghuniId = getParam("radPenghuni");
		System.out.println("ceking radio =======" + getParam("radPenghuni"));
		
		KuaPenghuni penghuni = (KuaPenghuni) db.get("Select x from KuaPenghuni x where x.id='"+ penghuniId + "'");
		context.put("penghuni", penghuni);
		
		if(penghuni != null){
		UsersJob pekerjaan = (UsersJob) db.get("Select x from UsersJob x where x.users.id='" + penghuni.getPemohon().getNoKP() + "'");
		context.put("pekerjaan", pekerjaan);
		
		KuaKuarters kuarters = (KuaKuarters) db.get("Select x from KuaKuarters x where x.id='" + penghuni.getKuarters().getId() + "'");
		context.put("kuarters", kuarters);
		}
		
		context.put("selectedTab", "1");
		return getPath() + "/maklumatPenghuni.vm";
//		return getPath() + "/maklumatHilangKelayakan.vm";
	}
	
	/** END CARIAN PENGHUNI **/
	
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
		
		return getMaklumatPenghuni();
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
		n.setHilangKelayakan(db.find(UtkHilangKelayakan.class, get("idHilangKelayakan")));
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
					updateStatusHilangKelayakan(jenisNotis + " PERTAMA");
				}else{
					updateStatusHilangKelayakan(jenisNotis + " KE-" + n.getFlagPeringatan());
				}
			}else{
				updateStatusHilangKelayakan(jenisNotis);
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
		
		List<UtkNotis> notisList = db.list("Select x from UtkNotis x where x.hilangKelayakan.id='" + get("idHilangKelayakan") + "'") ;
			
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
		UtkLanjutanHK rekodLanjutan = (UtkLanjutanHK) db.get("Select x from UtkLanjutanHK x where x.rayuan.id='" + get("idRayuan") + "'") ;
		
		context.put("idJenisRayuan", rekod.getFlagJenisRayuan());
		context.put("flagBilRayuan", rekod.getFlagRayuan());
		context.put("rekod", rekod);
		context.put("rekodLanjutan", rekodLanjutan);
		context.put("selectedTab", "3");
		return getPath() + "/rayuan/popupMaklumatRayuan.vm";
	}
	
	@Command("saveRayuan")
	public String saveRayuan() throws ParseException {
		String statusInfo = "";
		String keputusanSub = "";
		
		UtkRayuan n = db.find(UtkRayuan.class, get("idRayuan"));
		UtkLanjutanHK m = (UtkLanjutanHK) db.get("Select x from UtkLanjutanHK x where x.rayuan.id='" + get("idRayuan") + "'") ;
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
		n.setHilangKelayakan(db.find(UtkHilangKelayakan.class, get("idHilangKelayakan")));
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
				updateStatusHilangKelayakan("RAYUAN PERTAMA DALAM PROSES");
			}else{
				updateStatusHilangKelayakan("RAYUAN KE-" + n.getFlagRayuan() + " DALAM PROSES");
			}
		}
		
		if( addMaklumatKelulusan){
			
			if("L".equals(n.getFlagKelulusanSub()))
				keputusanSub = "LULUS";
			else if("T".equals(n.getFlagKelulusanSub()))
				keputusanSub = "TOLAK";
			
			if("1".equals(n.getFlagRayuan())){
				updateStatusHilangKelayakan("RAYUAN PERTAMA " + keputusanSub);
			}else{
				updateStatusHilangKelayakan("RAYUAN KE-" + n.getFlagRayuan() + " " + keputusanSub);
			}
			
			UtkLanjutanHK lanjutan = new UtkLanjutanHK();
			lanjutan.setRayuan(db.find(UtkRayuan.class,n.getId()));
			lanjutan.setHilangKelayakan(db.find(UtkHilangKelayakan.class,get("idHilangKelayakan")));
			lanjutan.setTarikhKelulusan(n.getTarikhKelulusan());
			lanjutan.setTarikhMula(getDate("tarikhMula"));
			lanjutan.setTarikhTamat(getDate("tarikhTamat"));
			lanjutan.setCatatan(get("catatanKelulusan"));
			db.begin();
			db.persist(lanjutan);
			try {
				db.commit();
			} catch (Exception e) {
				
			}
		}else{
			m.setTarikhKelulusan(n.getTarikhKelulusan());
			m.setTarikhMula(getDate("tarikhMula"));
			m.setTarikhTamat(getDate("tarikhTamat"));
			m.setCatatan(get("catatanKelulusan"));
			db.begin();
			try {
				db.commit();
			} catch (Exception e) {
				
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
		
		List<UtkRayuan> rayuanList = db.list("Select x from UtkRayuan x where x.hilangKelayakan.id='" + get("idHilangKelayakan") + "'") ;
			
		context.put("listRayuan", rayuanList);
		context.put("selectedTab", "3");
		
		return getPath() + "/entry_page.vm";
	}
		
	/** END MAKLUMAT RAYUAN**/
	
	
	/************************************************************ START MAKLUMAT PERLANJUTAN ************************************************************/
	@Command("addPerlanjutan")
	public String addPerlanjutan() {
		context.remove("rekod");
		context.put("selectedTab", "4");
		return getPath() + "/perlanjutan/popupMaklumatPerlanjutan.vm";
	}
	
	@Command("editPerlanjutan")
	public String editPerlanjutan() {
		context.remove("rekod");
		UtkLanjutanHK rekod = (UtkLanjutanHK) db.get("Select x from UtkLanjutanHK x where x.id='" + get("idLanjutan") + "'") ;
		context.put("rekod", rekod);
		context.put("selectedTab", "4");
		return getPath() + "/perlanjutan/popupMaklumatPerlanjutan.vm";
	}
	
	@Command("savePerlanjutan")
	public String savePerlanjutan() throws ParseException {
		String statusInfo = "";
		
		UtkLanjutanHK n = db.find(UtkLanjutanHK.class, get("idLanjutan"));
		
		Boolean addMaklumatPerlanjutan = false;
		
		if(n == null){
			addMaklumatPerlanjutan = true;
			n = new UtkLanjutanHK();
		}
		n.setTarikhMula(getDate("tarikhMula"));
		n.setTarikhTamat(getDate("tarikhTamat"));
		n.setCatatan(get("catatan"));
		
		db.begin();
		if ( addMaklumatPerlanjutan ) db.persist(n);
		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			statusInfo = "error";
		}
		context.put("statusInfo", statusInfo);
		context.put("selectedTab", "4");
		
		return getNotis();
	}
	
	@Command("removePerlanjutan")
	public String removePerlanjutan() {
		String statusInfo = "";
		UtkLanjutanHK maklumatPerlanjutan = db.find(UtkLanjutanHK.class, get("idLanjutan"));
		db.begin();
		db.remove(maklumatPerlanjutan);
		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			statusInfo = "error";
		}
		
		List<UtkLanjutanHK> perlanjutanList = db.list("Select x from UtkLanjutanHK x where x.hilangKelayakan.id='" + get("idHilangKelayakan") + "'") ;	
		context.put("listNotis", perlanjutanList);
		context.put("selectedTab", "4");
		
		return getPath() + "/entry_page.vm";
	}		
	/************************************************************ END MAKLUMAT PERLANJUTAN ************************************************************/

	
	/** START UPDATE STATUS **/
	
	public void updateStatusHilangKelayakan(String status){
		String statusInfo = "";
		UtkHilangKelayakan k = db.find(UtkHilangKelayakan.class, get("idHilangKelayakan"));
		
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

		String maxFlagPeringatan = (String) db.get("Select max(x.flagPeringatan) from UtkNotis x where x.hilangKelayakan.id='" + get("idHilangKelayakan") + "'");

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

		String maxFlagRayuan = (String) db.get("Select max(x.flagRayuan) from UtkRayuan x where x.hilangKelayakan.id='" + get("idHilangKelayakan") + "' and x.flagJenisRayuan='"+ get("idJenisRayuan") +"'");

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

