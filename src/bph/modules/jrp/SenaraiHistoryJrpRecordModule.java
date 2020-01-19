package bph.modules.jrp;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import portal.module.entity.Users;
import bph.entities.jrp.JrpDokumen;
import bph.entities.jrp.JrpKakitangan;
import bph.entities.jrp.JrpKeluasanRuang;
import bph.entities.jrp.JrpKertasPertimbangan;
import bph.entities.jrp.JrpMesyuarat;
import bph.entities.jrp.JrpPermohonan;
import bph.entities.jrp.JrpPermohonanLokasi;
import bph.entities.jrp.JrpUlasanTeknikal;
import bph.entities.kod.Agensi;
import bph.entities.kod.Bandar;
import bph.entities.kod.JenisDokumen;
import bph.entities.kod.JenisPermohonanJRP;
import bph.entities.kod.Status;
import bph.utils.DataUtil;
import bph.utils.Util;

public class SenaraiHistoryJrpRecordModule extends LebahRecordTemplateModule<JrpPermohonan> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private DataUtil dataUtil;

	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public void afterSave(JrpPermohonan permohonan) {
		//GENERATE NO PERMOHONAN
		if (permohonan.getNoPermohonan() == null)
			permohonan.setNoPermohonan(permohonan.getId());
		db.begin();		
		try {
			db.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<Agensi> listAgensi = dataUtil.getListAgensi(permohonan.getAgensi().getKementerian().getId());
		context.put("selectAgensi", listAgensi);
		List<Bandar> listBandar = dataUtil.getListBandar(permohonan.getBandar().getNegeri().getId());
		context.put("selectBandarPemohon", listBandar);
		context.put("selectedTab", "1");
		context.put("selectedSubTab", "1");
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void begin() {
		
		context.remove("permohonanLanjutan");
		dataUtil = DataUtil.getInstance(db);
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		userName = (String) request.getSession().getAttribute("_portal_username");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		context.put("userRole", userRole);
		
		Users users = db.find(Users.class, userId);
		context.put("users", users);
		
		//LIST DROPDOWN
		context.put("selectKementerian", dataUtil.getListKementerian());
		context.put("selectNegeri", dataUtil.getListNegeri());
		context.put("selectJenisPermohonanJRP", dataUtil.getListJenisPermohonanBaruJRP());
		context.put("selectPermohonanJRP", dataUtil.getListJenisPermohonanJRP());
		context.put("selectJenisPermohonanLanjutanJRP", dataUtil.getListJenisPermohonanLanjutanJRP());
				
		defaultButtonOption();
		addfilter();
		/*context.put("util", new Util());
		context.put("path", getPath());
		context.put("command", command);*/
		context.put("baseDir", getBaseDir());
//		context.put("currentDate", currentDate);
		
//		ArrayList<String> list = checkTarikhTamat();
//		checkTarikhTamat();
		
//		context.put("status", list);
		
		try {
			context.put("tabIdAndClassName", getTabIdAndClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	private void defaultButtonOption() {
		
		/*if (!"(JRP) Pemohon".equals(userRole)){
			this.setReadonly(true);
		}
		
		this.setDisableSaveAddNewButton(true);
		if (!"add_new_record".equals(command)){
			this.setDisableBackButton(true);
			this.setDisableDefaultButton(true);
		}	*/
		this.setReadonly(true);
	}
	
	private void addfilter() {
		Users users = db.find(Users.class, userId);
		
		this.addFilter("flagAktif = 'Y'");

//		if ("(JRP) Pemohon".equals(userRole)){
//			this.addFilter("agensi.id = '" + users.getAgensi().getId() + "'");
//		}
//		if ("(JRP) JawatanKuasa Teknikal".equals(userRole)){
//			this.addFilter("status.id = '1424860634475'"); //ULASAN JRP
//			this.addFilter("id NOT IN (SELECT y.jrpPermohonan.id FROM JrpUlasanTeknikal y WHERE y.jrpPermohonan.id = x.id AND y.flagHantar = 'Y' AND y.agensi.id = '" + users.getAgensi().getId() + "')"); //ULASAN JRP
//		}
//		if ("(JRP) Penyedia".equals(userRole)){
//			this.addFilter("status.id IN ('1424860634481', '1424860634490', '1424860634499', '1424860634493')"); //PENYEDIAAN KERTAS PERTIMBANGAN || MESYUARAT || LULUS BERSYARAT || LULUS 
//			this.addFilter("urusetia.id = '" + users.getId() + "'"); //URUSETIA
//		}
//		if ("(JRP) Penyemak".equals(userRole)){
//			this.addFilter("status.id IN ('1424860634478', '1424860634484')"); //PERMOHONAN LENGKAP || SEMAKAN KERTAS PERTIMBANGAN
//		}
//		if ("(JRP) Pelulus".equals(userRole)){
//			this.addFilter("status.id = '1424860634487'"); //PENGESAHAN KERTAS PERTIMBANGAN
//		}
//		
		this.setOrderBy("tarikhTerima");
		this.setOrderType("asc");
	}
	
	@Override
	public boolean delete(JrpPermohonan permohonan) throws Exception {
		// TODO Auto-generated method stub
		
		if("1424860634472".equals(permohonan.getStatus().getId()))
			return true;
		else
			return false;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/jrp/historyJrp";
	}

	public String getBaseDir() {
		return "bph/modules/jrp";
	}
	
	@Override
	public Class<JrpPermohonan> getPersistenceClass() {
		// TODO Auto-generated method stub
		return JrpPermohonan.class;
	}

	@Override
	public void getRelatedData(JrpPermohonan permohonan) {
		List<Agensi> listAgensi = dataUtil.getListAgensi(permohonan.getAgensi().getKementerian().getId());
		context.put("selectAgensi", listAgensi);
		List<Bandar> listBandar = dataUtil.getListBandar(permohonan.getBandar().getNegeri().getId());
		context.put("selectBandarPemohon", listBandar);
		context.put("selectedTab", "1");
		context.put("selectedSubTab", "1");
	}

	@Override
	public void save(JrpPermohonan permohonan) throws Exception {
				
		Users users = db.find(Users.class, userId);
		
		// TODO Auto-generated method stub		
		permohonan.setTarikhSurat(getDate("tarikhSurat"));
		if (!"(JRP) Pemohon".equals(userRole))
			permohonan.setTarikhTerima(getDate("tarikhTerima"));
		permohonan.setJenisPermohonanJrp(db.find(JenisPermohonanJRP.class, get("idJenisPermohonanJRP")));
		permohonan.setAlasanPermohonan(get("alasanPermohonan"));
		permohonan.setTujuanPermohonan(get("tujuanPermohonan"));
		
		if ("(JRP) Pemohon".equals(userRole)) {
			permohonan.setAgensi(db.find(Agensi.class, users.getAgensi().getId()));
		} else {
			permohonan.setAgensi(db.find(Agensi.class, get("idAgensi")));
		}		
		permohonan.setAlamat1(get("alamat1"));
		permohonan.setAlamat2(get("alamat2"));
		permohonan.setAlamat3(get("alamat3"));
		permohonan.setPoskod(get("poskod"));
		permohonan.setBandar(db.find(Bandar.class, get("idBandarPemohon")));
		permohonan.setNamaPegawai1(get("namaPegawai1"));
		permohonan.setNamaPegawai2(get("namaPegawai2"));
		permohonan.setNoTelefonPegawai1(get("noTelefonPegawai1"));
		permohonan.setNoTelefonPegawai2(get("noTelefonPegawai2"));
		permohonan.setEmelPegawai1(get("emelPegawai1"));
		permohonan.setEmelPegawai2(get("emelPegawai2"));
		permohonan.setNoFaks(get("noFaks"));
		permohonan.setFlagAktif("Y");
		
		permohonan.setStatus(db.find(Status.class, "1424860634472")); //PERMOHONAN BARU RUANG PEJABAT
		permohonan.setIdMasuk(db.find(Users.class, userId));
		permohonan.setTarikhMasuk(new Date());		
		
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Map<String, Object> m = new HashMap<String, Object>();

		m.put("noPermohonan", getParam("findNoPermohonan"));
		m.put("bandar.negeri.id", getParam("findNegeri"));
		m.put("bandar.id", getParam("findBandar"));
		m.put("agensi.kementerian.id", getParam("findKementerian"));
		m.put("agensi.id", getParam("findAgensi"));
		m.put("jenisPermohonanJrp.id", getParam("findJenisPermohonanJRP"));
		
		return m;
	}
	
	/** START MAKLUMAT PERMOHONAN
	 * 
	 * @return
	 * @throws Exception
	 */
	@Command("getMaklumatPermohonan")
	public String getMaklumatPermohonan() throws Exception {
		
		JrpPermohonan permohonan = db.find(JrpPermohonan.class, get("idPermohonan"));
		context.put("r", permohonan);
		
		context.put("selectedTab", "1");
		context.put("selectedSubTab", "1");

		return getPath() + "/entry_page.vm";
//		return getPath() + "/maklumatPermohonan/start.vm";
	}
	
	@Command("getMaklumatRuangSediaAda")
	public String getMaklumatRuangSediaAda() throws Exception {

		String idPermohonan = get("idPermohonan");
		context.remove("rekod");
        JrpPermohonanLokasi rekod = (JrpPermohonanLokasi) db.get("select x from JrpPermohonanLokasi x where x.flagLokasi = 'S' and x.permohonan.id = '" + idPermohonan + "'");
        context.put("rekod", rekod);
        
        if (rekod != null) {
        	List<Bandar> listBandar = dataUtil.getListBandar(rekod.getBandar().getNegeri().getId());
    		context.put("selectBandar", listBandar);
        }
        context.put("selectedTab", "1");
		context.put("selectedSubTab", "2");

		return getPath() + "/entry_page.vm";
//		return getPath() + "/maklumatPermohonan/start.vm";
	}
	
	@Command("getMaklumatRuangBaru")
	public String getMaklumatRuangBaru() throws Exception {
		
		String idPermohonan = getParam("idPermohonan");
		
		context.remove("rekod");
        JrpPermohonanLokasi rekod = (JrpPermohonanLokasi) db.get("select x from JrpPermohonanLokasi x where x.flagLokasi = 'B' and x.permohonan.id = '" + idPermohonan + "'");
        context.put("rekod", rekod);
        
        if (rekod != null) {
        	List<Bandar> listBandar = dataUtil.getListBandar(rekod.getBandar().getNegeri().getId());
    		context.put("selectBandar", listBandar);
        }        

        context.put("selectedTab", "1");
		context.put("selectedSubTab", "3");

		return getPath() + "/entry_page.vm";
//		return getPath() + "/maklumatPermohonan/start.vm";
	}
	
	@Command("getPerakuanKSU")
	public String getPerakuanKSU() throws Exception {

		context.put("selectedTab", "1");
		context.put("selectedSubTab", "4");

		return getPath() + "/entry_page.vm";
//		return getPath() + "/maklumatPermohonan/start.vm";
	}
	
	@Command("getMaklumatKakitangan")
	public String getMaklumatKakitangan() throws Exception {
		
		List<JrpKakitangan> listMaklumatKakitangan = db.list("SELECT x FROM JrpKakitangan x WHERE x.permohonan.id = '" + get("idPermohonan") +"' order by x.turutan");
		context.put("listMaklumatKakitangan", listMaklumatKakitangan);

		context.put("selectedTab", "1");
		context.put("selectedSubTab", "5");
		
		return getPath() + "/entry_page.vm";
		
//		return getPath() + "/maklumatPermohonan/start.vm";
	}
	
	@Command("getMaklumatKeluasanRuang")
	public String getMaklumatKeluasanRuang() throws Exception {
		
		List<JrpKeluasanRuang> listKeluasanRuang = db.list("SELECT x FROM JrpKeluasanRuang x WHERE x.permohonan.id = '" + get("idPermohonan") +"'");
		context.put("listKeluasanRuang", listKeluasanRuang);

		context.put("selectedTab", "1");
		context.put("selectedSubTab", "6");

		return getPath() + "/entry_page.vm";
//		return getPath() + "/maklumatPermohonan/start.vm";
	}
	
	@Command("getAgihanTugas")
	public String getAgihanTugas() throws Exception {

		List<Users> listPenyediaJRP = dataUtil.getListPenyediaJRP();
		context.put("selectPenyediaJRP", listPenyediaJRP);
		
		context.put("selectedTab", "6");

		return getPath() + "/entry_page.vm";
//		return getPath() + "/agihanTugas/start.vm";
	}

	@Command("doSaveMaklumatPermohonan")
	public String doSaveMaklumatPermohonan() throws Exception {
		String statusInfo = "";
		
		Users users = db.find(Users.class, userId);
		JrpPermohonan permohonan = db.find(JrpPermohonan.class, getParam("idPermohonan"));
		
		db.begin();			
		permohonan.setTarikhSurat(getDate("tarikhSurat"));
		if (!"(JRP) Pemohon".equals(userRole)){
			permohonan.setTarikhTerima(getDate("tarikhTerima"));
		}
		if(get("idJenisPermohonanJRP").equals("JRP1") || get("idJenisPermohonanJRP").equals("JRP2")){
			permohonan.setJenisPermohonanJrp(db.find(JenisPermohonanJRP.class, get("idJenisPermohonanJRP")));
		}
		else{
			permohonan.setJenisPermohonanJrp(db.find(JenisPermohonanJRP.class, get("idJenisPermohonanLanjutanJRP")));
		}
		permohonan.setAlasanPermohonan(get("alasanPermohonan"));
		permohonan.setTujuanPermohonan(get("tujuanPermohonan"));
		
		if ("(JRP) Pemohon".equals(userRole)) {
			permohonan.setAgensi(db.find(Agensi.class, users.getAgensi().getId()));
		} else {
			permohonan.setAgensi(db.find(Agensi.class, get("idAgensi")));
		}
		permohonan.setAlamat1(get("alamat1"));
		permohonan.setAlamat2(get("alamat2"));
		permohonan.setAlamat3(get("alamat3"));
		permohonan.setPoskod(get("poskod"));
		permohonan.setBandar(db.find(Bandar.class, get("idBandarPemohon")));
		permohonan.setNamaPegawai1(get("namaPegawai1"));
		permohonan.setNamaPegawai2(get("namaPegawai2"));
		permohonan.setNoTelefonPegawai1(get("noTelefonPegawai1"));
		permohonan.setNoTelefonPegawai2(get("noTelefonPegawai2"));
		permohonan.setEmelPegawai1(get("emelPegawai1"));
		permohonan.setEmelPegawai2(get("emelPegawai2"));
		permohonan.setNoFaks(get("noFaks"));
		
		permohonan.setIdKemaskini(db.find(Users.class, userId));
		permohonan.setTarikhKemaskini(new Date());

		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			statusInfo = "error";
		}
		
		context.put("selectedTab", "1");
		context.put("selectedSubTab", "1");
		context.put("statusInfo", statusInfo);
		
		return getPath() + "/entry_page.vm";
	}
	
	/** START MAKLUMAT PERMOHONAN LANJUTAN
	 * 
	 * @return
	 * @throws Exception
	 */
	@Command("doDaftarLanjutan")
	public String doDaftarLanjutan() throws Exception {

		
		return getPath() + "/daftarLanjutan/start.vm";
	}
	
	@Command("doSaveMaklumatPermohonanLanjutan")
	public String doSaveMaklumatPermohonanLanjutan() throws Exception {
		String statusInfo = "";
		
		Users users = db.find(Users.class, userId);
		JrpPermohonan permohonan = db.find(JrpPermohonan.class, getParam("idPermohonan"));
		JrpPermohonan permohonanLanjutan = new JrpPermohonan();
		
		db.begin();
		
		//START COPY PERMOHONAN OLD TO PERMOHONAN LANJUTAN
		permohonanLanjutan.setTarikhSurat(getDate("tarikhSurat"));
		permohonanLanjutan.setJenisPermohonanJrp(db.find(JenisPermohonanJRP.class, get("idJenisPermohonanLanjutanJRP")));
		permohonanLanjutan.setAlasanPermohonan(get("alasanPermohonan"));
		permohonanLanjutan.setTujuanPermohonan(get("tujuanPermohonan"));
		
		if ("(JRP) Pemohon".equals(userRole)) {
			permohonanLanjutan.setAgensi(db.find(Agensi.class, users.getAgensi().getId()));
		} else {
			permohonanLanjutan.setAgensi(db.find(Agensi.class, permohonan.getAgensi().getId()));
		}
		permohonanLanjutan.setNoPermohonan(permohonan.getNoPermohonan()); //SHARE NO PERMOHONAN
		permohonanLanjutan.setAlamat1(permohonan.getAlamat1().equals(null) ? "" : permohonan.getAlamat1());
		permohonanLanjutan.setAlamat2(permohonan.getAlamat2().equals(null) ? "" : permohonan.getAlamat2());
		permohonanLanjutan.setAlamat3(permohonan.getAlamat3().equals(null) ? "" : permohonan.getAlamat3());
		permohonanLanjutan.setPoskod(permohonan.getPoskod().equals(null) ? "" : permohonan.getPoskod());
		permohonanLanjutan.setBandar(db.find(Bandar.class, permohonan.getBandar().getId()));
		permohonanLanjutan.setNamaPegawai1(permohonan.getNamaPegawai1().equals(null) ? "" : permohonan.getNamaPegawai1());
		permohonanLanjutan.setNamaPegawai2(permohonan.getNamaPegawai2().equals(null) ? "" : permohonan.getNamaPegawai2());
		permohonanLanjutan.setNoTelefonPegawai1(permohonan.getNoTelefonPegawai1().equals(null) ? "" : permohonan.getNoTelefonPegawai1());
		permohonanLanjutan.setNoTelefonPegawai2(permohonan.getNoTelefonPegawai2().equals(null) ? "" : permohonan.getNoTelefonPegawai2());
		permohonanLanjutan.setEmelPegawai1(permohonan.getEmelPegawai1().equals(null) ? "" : permohonan.getEmelPegawai1());
		permohonanLanjutan.setEmelPegawai2(permohonan.getEmelPegawai2().equals(null) ? "" : permohonan.getEmelPegawai2());
		permohonanLanjutan.setNoFaks(permohonan.getNoFaks().equals(null) ? "" : permohonan.getNoFaks());
		permohonanLanjutan.setFlagAktif("Y");
		
		permohonanLanjutan.setStatus(db.find(Status.class, "1424860634472")); //PERMOHONAN BARU LANJUTAN
		permohonanLanjutan.setIdMasuk(db.find(Users.class, userId));
		permohonanLanjutan.setTarikhMasuk(new Date());
		
		permohonan.setFlagAktif("T"); //set permohonan lama T
		
		db.persist(permohonanLanjutan);
		//END COPY PERMOHONAN OLD TO PERMOHONAN LANJUTAN
		
		// START COPY RUANG BARU OLD TO RUANG SEDIA ADA NEW
		JrpPermohonanLokasi ruangSediaAdaOld = (JrpPermohonanLokasi) db.get("Select x from JrpPermohonanLokasi x where x.permohonan.id = '" + getParam("idPermohonan") + "' and x.flagLokasi = 'B'");
		
		JrpPermohonanLokasi ruangSediaAdaLanjutan = null;	
		if(ruangSediaAdaOld != null){
		ruangSediaAdaLanjutan = new JrpPermohonanLokasi();
		ruangSediaAdaLanjutan.setPermohonan(db.find(JrpPermohonan.class, permohonanLanjutan.getId())); //set id permohonan lanjutan
		ruangSediaAdaLanjutan.setFlagLokasi("S"); //save permohonan Baru menjadi permohonan sediada
		ruangSediaAdaLanjutan.setCawangan(ruangSediaAdaOld.getCawangan().equals(null) ? "" : ruangSediaAdaOld.getCawangan());
		ruangSediaAdaLanjutan.setJenisBangunan(ruangSediaAdaOld.getJenisBangunan().equals(null) ? "" : ruangSediaAdaOld.getJenisBangunan());
		ruangSediaAdaLanjutan.setNamaBangunan(ruangSediaAdaOld.getNamaBangunan().equals(null) ? "" : ruangSediaAdaOld.getNamaBangunan());
		ruangSediaAdaLanjutan.setAlamat1(ruangSediaAdaOld.getAlamat1().equals(null) ? "" : ruangSediaAdaOld.getAlamat1());
		ruangSediaAdaLanjutan.setAlamat2(ruangSediaAdaOld.getAlamat2().equals(null) ? "" : ruangSediaAdaOld.getAlamat2());
		ruangSediaAdaLanjutan.setAlamat3(ruangSediaAdaOld.getAlamat3().equals(null) ? "" : ruangSediaAdaOld.getAlamat3());
		ruangSediaAdaLanjutan.setPoskod(ruangSediaAdaOld.getPoskod().equals(null) ? "" : ruangSediaAdaOld.getPoskod());
		ruangSediaAdaLanjutan.setBandar(db.find(Bandar.class, ruangSediaAdaOld.getBandar().getId().equals(null) ? "" : ruangSediaAdaOld.getBandar().getId()));
		ruangSediaAdaLanjutan.setNamaPemilikPremis(ruangSediaAdaOld.getNamaPemilikPremis().equals(null) ? "" : ruangSediaAdaOld.getNamaPemilikPremis());
		ruangSediaAdaLanjutan.setSewaSebulan(ruangSediaAdaOld.getSewaSebulan().equals(null) ? new Double(0.00) : ruangSediaAdaOld.getSewaSebulan());
		ruangSediaAdaLanjutan.setSewaMp(ruangSediaAdaOld.getSewaMp().equals(null) ? new Double(0.00) : ruangSediaAdaOld.getSewaMp());
		ruangSediaAdaLanjutan.setSewaKp(ruangSediaAdaOld.getSewaKp().equals(null) ? new Double(0.00) : ruangSediaAdaOld.getSewaKp());
		ruangSediaAdaLanjutan.setKadarGst(ruangSediaAdaOld.getKadarGst().equals(null) ? new Double(0.00) : ruangSediaAdaOld.getKadarGst());
		ruangSediaAdaLanjutan.setTotalSewa(ruangSediaAdaOld.getTotalSewa().equals(null) ? new Double(0.00) : ruangSediaAdaOld.getTotalSewa());
		db.persist(ruangSediaAdaLanjutan);
		
		//END COPY RUANG BARU OLD TO RUANG SEDIA ADA NEW 
		
		if(permohonanLanjutan.getJenisPermohonanJrp().getId().equals("JRP7")){
			
		// START COPY RUANG SEDIA ADA NEW TO RUANG BARU NEW
		JrpPermohonanLokasi ruangLanjutan = new JrpPermohonanLokasi();
		ruangLanjutan.setPermohonan(db.find(JrpPermohonan.class, permohonanLanjutan.getId())); //set id permohonan lanjutan
		ruangLanjutan.setFlagLokasi("B");
		ruangLanjutan.setCawangan(ruangSediaAdaLanjutan.getCawangan().equals(null) ? "" : ruangSediaAdaLanjutan.getCawangan());
		ruangLanjutan.setJenisBangunan(ruangSediaAdaLanjutan.getJenisBangunan().equals(null) ? "" : ruangSediaAdaLanjutan.getJenisBangunan());
		ruangLanjutan.setNamaBangunan(ruangSediaAdaLanjutan.getNamaBangunan().equals(null) ? "" : ruangSediaAdaLanjutan.getNamaBangunan());
		ruangLanjutan.setAlamat1(ruangSediaAdaLanjutan.getAlamat1().equals(null) ? "" : ruangSediaAdaLanjutan.getAlamat1());
		ruangLanjutan.setAlamat2(ruangSediaAdaLanjutan.getAlamat2().equals(null) ? "" : ruangSediaAdaLanjutan.getAlamat2());
		ruangLanjutan.setAlamat3(ruangSediaAdaLanjutan.getAlamat3().equals(null) ? "" : ruangSediaAdaLanjutan.getAlamat3());
		ruangLanjutan.setPoskod(ruangSediaAdaLanjutan.getPoskod().equals(null) ? "" : ruangSediaAdaLanjutan.getPoskod());
		ruangLanjutan.setBandar(db.find(Bandar.class, ruangSediaAdaLanjutan.getBandar().getId().equals(null) ? "" : ruangSediaAdaLanjutan.getBandar().getId()));
		ruangLanjutan.setNamaPemilikPremis(ruangSediaAdaLanjutan.getNamaPemilikPremis().equals(null) ? "" : ruangSediaAdaLanjutan.getNamaPemilikPremis());
		ruangLanjutan.setSewaSebulan(ruangSediaAdaLanjutan.getSewaSebulan().equals(null) ? new Double(0.00) : ruangSediaAdaLanjutan.getSewaSebulan());
		ruangLanjutan.setSewaMp(ruangSediaAdaLanjutan.getSewaMp().equals(null) ? new Double(0.00) : ruangSediaAdaLanjutan.getSewaMp());
		ruangLanjutan.setSewaKp(ruangSediaAdaLanjutan.getSewaKp().equals(null) ? new Double(0.00) : ruangSediaAdaLanjutan.getSewaKp());
		ruangLanjutan.setKadarGst(ruangSediaAdaLanjutan.getKadarGst().equals(null) ? new Double(0.00) : ruangSediaAdaLanjutan.getKadarGst());
		ruangLanjutan.setTotalSewa(ruangSediaAdaLanjutan.getTotalSewa().equals(null) ? new Double(0.00) : ruangSediaAdaLanjutan.getTotalSewa());
		db.persist(ruangLanjutan);
		}
		//END COPY RUANG SEDIA ADA NEW TO RUANG BARU NEW
		}
		
		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			statusInfo = "error";
		}
		
		permohonanLanjutan = db.find(JrpPermohonan.class, permohonanLanjutan.getId());
		context.put("permohonanLanjutan", permohonanLanjutan);
		
		
		if(ruangSediaAdaOld != null){
		ruangSediaAdaLanjutan = db.find(JrpPermohonanLokasi.class, ruangSediaAdaLanjutan.getId());
		context.put("ruangSediaAdaLanjutan", ruangSediaAdaLanjutan);
		}
//	
//		context.put("selectedTab", "1");
//		context.put("selectedSubTab", "1");
		context.put("statusInfo", statusInfo);
		
		return getPath() + "/entry_page.vm";
		
//		return getMaklumatPermohonan();
	}
	/** END MAKLUMAT PERMOHONAN LANJUTAN */
	
	@Command("doSaveMaklumatRuangSediaAda")
	public String doSaveMaklumatRuangSediaAda() throws Exception {
		String idPermohonan = getParam("idPermohonan");
		String statusInfo = "";
		String jenisBangunan = "";
		JrpPermohonanLokasi rekod = db.find(JrpPermohonanLokasi.class, getParam("idPermohonanLokasiSediaAda"));
		Boolean addRekod = false;
		
		if(rekod == null){
			addRekod = true;
			rekod = new JrpPermohonanLokasi();
		}
		
		db.begin();
		rekod.setPermohonan(db.find(JrpPermohonan.class, idPermohonan));
		rekod.setFlagLokasi("S");
		rekod.setCawangan(get("cawanganSediaAda"));
		if(!"RUMAH KEDAI".equals(getParam("jenisBangunanSediaAda")) && !"KEDIAMAN".equals(getParam("jenisBangunanSediaAda")) && !"".equals(getParam("jenisBangunanSediaAda")))
			jenisBangunan = get("jenisBangunan2");
		else
			jenisBangunan = get("jenisBangunanSediaAda");		
		rekod.setJenisBangunan(jenisBangunan);		
		rekod.setNamaBangunan(get("namaBangunanSediaAda"));		
		rekod.setAlamat1(get("alamat1SediaAda"));
		rekod.setAlamat2(get("alamat2SediaAda"));
		rekod.setAlamat3(get("alamat3SediaAda"));
		rekod.setPoskod(get("poskodSediaAda"));
		rekod.setBandar(db.find(Bandar.class, get("idBandarSediaAda")));
		rekod.setNamaPemilikPremis(get("namaPemilikPremisSediaAda"));
		rekod.setSewaSebulan(Util.getDouble(Util.RemoveComma(get("sewaSebulanSediaAda"))));
		rekod.setSewaMp(Util.getDouble(Util.RemoveComma(get("sewaMpSediaAda"))));
		rekod.setSewaKp(Util.getDouble(Util.RemoveComma(get("sewaKpSediaAda"))));
		rekod.setKadarGst(Util.getDouble(Util.RemoveComma(get("kadarGst"))));
		rekod.setTotalSewa(Util.getDouble(Util.RemoveComma(get("totalSewa"))));
		if ( addRekod ) db.persist(rekod);
		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			statusInfo = "error";
		}
		
		context.remove("rekod");
        rekod = (JrpPermohonanLokasi) db.get("select x from JrpPermohonanLokasi x where x.flagLokasi = 'S' and x.permohonan.id = '" + idPermohonan + "'");
        context.put("rekod", rekod);
		
		context.put("selectedSubTab", "2");
		context.put("statusInfo", statusInfo);
		context.put("selectedTab", "1");
		
		return getPath() + "/entry_page.vm";
//		return getPath() + "/maklumatPermohonan/start.vm";
	}
	
	@Command("selectJenisBangunanSediaAda")
	public String selectJenisBangunanSediaAda() throws Exception {

		String jenisBangunan = "";
		if (getParam("jenisBangunanSediaAda").trim().length() > 0) 
		
			jenisBangunan = get("jenisBangunanSediaAda");

		context.put("idJenisBangunan", jenisBangunan);
		if(jenisBangunan.equals("RUMAH KEDAI") || jenisBangunan.equals("KEDIAMAN") || jenisBangunan.equals("") ){
			return getPath() + path + "/maklumatPermohonan/skrinKosong.vm";
		}else{
			return getPath() + path + "/maklumatPermohonan/selectJenisBangunanSediaAda.vm";
		}
	}
	
	@Command("doSaveMaklumatRuangBaru")
	public String doSaveMaklumatRuangBaru() throws Exception {
		String idPermohonan = getParam("idPermohonan");
		String statusInfo = "";
		String jenisBangunan = "";
		JrpPermohonanLokasi rekod = db.find(JrpPermohonanLokasi.class, getParam("idPermohonanLokasiBaru"));
		Boolean addRekod = false;
		
		if(rekod == null){
			addRekod = true;
			rekod = new JrpPermohonanLokasi();
		}
		
		db.begin();
		rekod.setPermohonan(db.find(JrpPermohonan.class, idPermohonan));
		rekod.setFlagLokasi("B");
		rekod.setCawangan(get("cawanganBaru"));
		if(!"RUMAH KEDAI".equals(getParam("jenisBangunanBaru")) && !"KEDIAMAN".equals(getParam("jenisBangunanBaru")) && !"".equals(getParam("jenisBangunanBaru")))
			jenisBangunan = get("jenisBangunan2");
		else
			jenisBangunan = get("jenisBangunanBaru");		
		rekod.setJenisBangunan(jenisBangunan);		
		rekod.setNamaBangunan(get("namaBangunanBaru"));		
		rekod.setAlamat1(get("alamat1Baru"));
		rekod.setAlamat2(get("alamat2Baru"));
		rekod.setAlamat3(get("alamat3Baru"));
		rekod.setPoskod(get("poskodBaru"));
		rekod.setBandar(db.find(Bandar.class, get("idBandarBaru")));
		rekod.setNamaPemilikPremis(get("namaPemilikPremisBaru"));
		rekod.setSewaSebulan(Util.getDouble(Util.RemoveComma(get("sewaSebulanBaru"))));
		rekod.setSewaMp(Util.getDouble(Util.RemoveComma(get("sewaMpBaru"))));
		rekod.setSewaKp(Util.getDouble(Util.RemoveComma(get("sewaKpSediaAda"))));
		rekod.setKadarGst(Util.getDouble(Util.RemoveComma(get("kadarGst"))));
		rekod.setTotalSewa(Util.getDouble(Util.RemoveComma(get("totalSewa"))));
		if ( addRekod ) db.persist(rekod);
		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			statusInfo = "error";
		}
		
		context.remove("rekod");
        rekod = (JrpPermohonanLokasi) db.get("select x from JrpPermohonanLokasi x where x.flagLokasi = 'B' and x.permohonan.id = '" + idPermohonan + "'");
        context.put("rekod", rekod);
        
		context.put("selectedSubTab", "3");
		context.put("statusInfo", statusInfo);
		context.put("selectedTab", "1");
		
		return getPath() + "/entry_page.vm";
//		return getPath() + "/maklumatPermohonan/start.vm";
	}
	
	@Command("selectJenisBangunanBaru")
	public String selectJenisBangunanBaru() throws Exception {

		String jenisBangunan = "";
		if (getParam("jenisBangunanBaru").trim().length() > 0) 
		
			jenisBangunan = get("jenisBangunanBaru");

		context.put("idJenisBangunan", jenisBangunan);
		if(jenisBangunan.equals("RUMAH KEDAI") || jenisBangunan.equals("KEDIAMAN") || jenisBangunan.equals("") ){
			return getPath() + path + "/maklumatPermohonan/skrinKosong.vm";
		}else{
			return getPath() + path + "/maklumatPermohonan/selectJenisBangunanBaru.vm";
		}
	}
	
	@Command("doSavePerakuanKSU")
	public String doSavePerakuanKSU() throws Exception {
		String statusInfo = "";

		JrpPermohonan permohonan = db.find(JrpPermohonan.class, getParam("idPermohonan"));
		
		db.begin();
		permohonan.setFlagPerakuanPeruntukan(get("flagPerakuanPeruntukan"));
		
		permohonan.setIdKemaskini(db.find(Users.class, userId));
		permohonan.setTarikhKemaskini(new Date());

		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			statusInfo = "error";
		}
		
		context.put("selectedSubTab", "4");
		context.put("statusInfo", statusInfo);
		context.put("selectedTab", "1");
		
		return getPath() + "/entry_page.vm";
//		return getPath() + "/maklumatPermohonan/start.vm";
	}
	
	@Command("addMaklumatKakitangan")
	public String addMaklumatKakitangan() {
		context.put("rekod", "");
		return getPath() + "/maklumatPermohonan/popupMaklumatKakitangan.vm";
	}
	
	@Command("editMaklumatKakitangan")
	public String editMaklumatKakitangan() {
		context.put("rekod", "");
		JrpKakitangan maklumatKakitangan = db.find(JrpKakitangan.class, get("idMaklumatKakitangan"));

		if(maklumatKakitangan != null){
			context.put("rekod", maklumatKakitangan);
		}
		
		return getPath()  + "/maklumatPermohonan/popupMaklumatKakitangan.vm";
	}
	
	@Command("saveMaklumatKakitangan")
	public String saveMaklumatKakitangan() throws ParseException {
		String statusInfo = "";
		
		JrpKakitangan maklumatKakitangan = db.find(JrpKakitangan.class, get("idMaklumatKakitangan"));
		Boolean addMaklumatKakitangan = false;
		
		if(maklumatKakitangan == null){
			addMaklumatKakitangan = true;
			maklumatKakitangan = new JrpKakitangan();
		}
		
		maklumatKakitangan.setJawatan(get("jawatan"));
		maklumatKakitangan.setGred(get("gred"));
		maklumatKakitangan.setBilanganSediaAda(get("bilanganSediaAda"));
		maklumatKakitangan.setLuasSediaAda(get("luasSediaAda"));
		maklumatKakitangan.setBilanganBaru(get("bilanganBaru"));
		maklumatKakitangan.setLuasBaru(get("luasBaru"));		
		maklumatKakitangan.setPermohonan(db.find(JrpPermohonan.class, get("idPermohonan")));
		
		db.begin();
		if ( addMaklumatKakitangan ) db.persist(maklumatKakitangan);
		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			statusInfo = "error";
		}
		
		context.put("selectedSubTab", "5");
		context.put("statusInfo", statusInfo);
		context.put("selectedTab", "1");
		
		return getPath() + "/entry_page.vm";
//		return getPath() + "/maklumatPermohonan/start.vm";
	}
	
	@Command("saveMaklumatKakitangan1")
	public String saveMaklumatKakitangan1() throws ParseException {
		String statusInfo = "";
		
		JrpKakitangan maklumatKakitangan = db.find(JrpKakitangan.class, get("idMaklumatKakitangan"));
		Boolean addMaklumatKakitangan = false;
		
		if(maklumatKakitangan == null){
			addMaklumatKakitangan = true;
			maklumatKakitangan = new JrpKakitangan();
		}
		
		maklumatKakitangan.setJawatan(get("jawatan1"));
		maklumatKakitangan.setGred(get("gred1"));
		maklumatKakitangan.setBilanganSediaAda(get("bilanganSediaAda1"));
		maklumatKakitangan.setLuasSediaAda(get("luasSediaAda1"));
		maklumatKakitangan.setBilanganBaru(get("bilanganBaru1"));
		maklumatKakitangan.setLuasBaru(get("luasBaru1"));		
		maklumatKakitangan.setPermohonan(db.find(JrpPermohonan.class, get("idPermohonan")));
		
		db.begin();
		if ( addMaklumatKakitangan ) db.persist(maklumatKakitangan);
		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			statusInfo = "error";
		}
		
		context.put("selectedSubTab", "5");
		context.put("statusInfo", statusInfo);
		context.put("selectedTab", "1");
		
		return getPath() + "/entry_page.vm";
//		return getPath() + "/maklumatPermohonan/start.vm";
	}
	
	@SuppressWarnings("unchecked")
	@Command("removeMaklumatKakitangan")
	public String removeMaklumatKakitangan() {
		String statusInfo = "";
		
		JrpKakitangan maklumatKakitangan = db.find(JrpKakitangan.class, get("idMaklumatKakitangan"));

		db.begin();
		db.remove(maklumatKakitangan);
		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			statusInfo = "error";
		}
		
		List<JrpKakitangan> listMaklumatKakitangan = db.list("SELECT x FROM JrpKakitangan x WHERE x.permohonan.id = '" + get("idPermohonan") +"' order by x.turutan");
		context.put("listMaklumatKakitangan", listMaklumatKakitangan);
		
		context.put("selectedSubTab", "5");
		context.put("statusInfo", statusInfo);
		context.put("selectedTab", "1");
		
		return getPath() + "/entry_page.vm";
//		return getPath() + "/maklumatPermohonan/start.vm";
	}
	
	@Command("saveSequence")
	public String saveSequence() throws ParseException {
		JrpKakitangan maklumatKakitangan = db.find(JrpKakitangan.class, getParam("idKakitangan"));
		db.begin();
		maklumatKakitangan.setTurutan(getParamAsInteger("turutan" + getParam("idKakitangan")));
		try {
			db.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
		
		List<JrpKakitangan> listMaklumatKakitangan = db.list("SELECT x FROM JrpKakitangan x WHERE x.permohonan.id = '" + get("idPermohonan") +"' order by x.turutan");
		context.put("listMaklumatKakitangan", listMaklumatKakitangan);
		
		context.put("saveAdd", getParam("saveAdd"));
		context.put("selectedSubTab", "5");
		context.put("selectedTab", "1");
		
		return getPath() + "/entry_page.vm";
	}
	
	@Command("addKeluasanRuang")
	public String addKeluasanRuang() {
		context.put("rekod", "");
		return getPath() + "/maklumatPermohonan/popupMaklumatKeluasanRuang.vm";
	}
	
	@Command("editKeluasanRuang")
	public String editKeluasanRuang() {
		context.put("rekod", "");
		JrpKeluasanRuang KeluasanRuang = db.find(JrpKeluasanRuang.class, get("idKeluasanRuang"));

		if(KeluasanRuang != null){
			context.put("rekod", KeluasanRuang);
		}
		return getPath()  + "/maklumatPermohonan/popupMaklumatKeluasanRuang.vm";
	}
	
	@Command("saveKeluasanRuang")
	public String saveKeluasanRuang() throws ParseException {
		String statusInfo = "";
		
		JrpKeluasanRuang keluasanRuang = db.find(JrpKeluasanRuang.class, get("idKeluasanRuang"));
		Boolean addKeluasanRuang = false;
		
		if(keluasanRuang == null){
			addKeluasanRuang = true;
			keluasanRuang = new JrpKeluasanRuang();
		}
		
		keluasanRuang.setPerkara(get("perkara"));
		keluasanRuang.setLuasSediaAda(get("luasSediaAda"));
		keluasanRuang.setLuasBaru(get("luasBaru"));		
		keluasanRuang.setPermohonan(db.find(JrpPermohonan.class, get("idPermohonan")));
		
		db.begin();
		if ( addKeluasanRuang ) db.persist(keluasanRuang);
		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			statusInfo = "error";
		}
		
		context.put("selectedSubTab", "6");
		context.put("statusInfo", statusInfo);
		context.put("selectedTab", "1");
		
		return getPath() + "/entry_page.vm";
//		return getPath() + "/maklumatPermohonan/start.vm";
	}
	
	@Command("saveKeluasanRuang1")
	public String saveKeluasanRuang1() throws ParseException {
		String statusInfo = "";
		
		JrpKeluasanRuang keluasanRuang = db.find(JrpKeluasanRuang.class, get("idKeluasanRuang"));
		Boolean addKeluasanRuang = false;
		
		if(keluasanRuang == null){
			addKeluasanRuang = true;
			keluasanRuang = new JrpKeluasanRuang();
		}
		
		keluasanRuang.setPerkara(get("perkara1"));
		keluasanRuang.setLuasSediaAda(get("luasSediaAda1"));
		keluasanRuang.setLuasBaru(get("luasBaru1"));		
		keluasanRuang.setPermohonan(db.find(JrpPermohonan.class, get("idPermohonan")));
		
		db.begin();
		if ( addKeluasanRuang ) db.persist(keluasanRuang);
		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			statusInfo = "error";
		}
		
		context.put("selectedSubTab", "6");
		context.put("statusInfo", statusInfo);
		context.put("selectedTab", "1");
		
		return getPath() + "/entry_page.vm";
//		return getPath() + "/maklumatPermohonan/start.vm";
	}
	
	@SuppressWarnings("unchecked")
	@Command("removeKeluasanRuang")
	public String removeKeluasanRuang() {
		String statusInfo = "";
		
		JrpKeluasanRuang KeluasanRuang = db.find(JrpKeluasanRuang.class, get("idKeluasanRuang"));

		db.begin();
		db.remove(KeluasanRuang);
		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			statusInfo = "error";
		}
		
		List<JrpKeluasanRuang> listKeluasanRuang = db.list("SELECT x FROM JrpKeluasanRuang x WHERE x.permohonan.id = '" + get("idPermohonan") +"'");
		context.put("listKeluasanRuang", listKeluasanRuang);
		
		context.put("selectedSubTab", "6");
		context.put("statusInfo", statusInfo);
		context.put("selectedTab", "1");
		
		return getPath() + "/entry_page.vm";
//		return getPath() + "/maklumatPermohonan/start.vm";
	}
	
	/** START MAKLUMAT DOKUMEN SOKONGAN
	 * 
	 * @return
	 * @throws Exception
	 */
	@Command("getMaklumatDokumenSokongan")
	public String getMaklumatDokumenSokongan() throws Exception {

		JrpPermohonan jrpPermohonan = db.find(JrpPermohonan.class, get("idPermohonan"));
		context.put("r", jrpPermohonan);
		
		List<JrpDokumen> listDokumen = db.list("SELECT x FROM JrpDokumen x WHERE x.permohonan.id = '" + get("idPermohonan") +"'");
		context.put("listDokumen", listDokumen);
		
		List<JenisDokumen> listJenisDokumen = dataUtil.getListJenisDokumenPermohonanJRP();
		context.put("selectJenisDokumen", listJenisDokumen);
		
//		return getPath() + "/dokumenSokongan/start.vm";
		context.put("selectedTab", "2");
		return getPath() + "/entry_page.vm";
	}
	
	@SuppressWarnings("rawtypes")
	@Command("uploadDoc")
	public String uploadPhoto() throws Exception {
		
		JrpPermohonan jrpPermohonan = db.find(JrpPermohonan.class, get("idPermohonan"));
		context.put("r", jrpPermohonan);
		
		String idPermohonan = get("idPermohonan");
		String tajuk = get("tajuk");
		String idJenisDokumen = get("idJenisDokumen");
		String keterangan = get("keterangan");

		String uploadDir = "jrp/permohonanRuangPejabat/dokumenSokongan/";
		
		File dir = new File(ResourceBundle.getBundle("dbconnection").getString("folder") + uploadDir);
		if ( !dir.exists() ) dir.mkdir();
		
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		List items = upload.parseRequest(request);
		Iterator itr = items.iterator();
		List<FileItem> files = new ArrayList<FileItem>();
		while (itr.hasNext()) {
			FileItem item = (FileItem)itr.next();
			if ((!(item.isFormField())) && (item.getName() != null) && (!("".equals(item.getName())))) {
				files.add(item);
			}
		}

		for ( FileItem item : files ) {
			String avatarName = "";
			String fileName = item.getName();
			String imgName = uploadDir + fileName;
			
			imgName = imgName.replaceAll(" ", "_");
			item.write(new File(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName));

			String mimetype= item.getContentType();
	        String type = mimetype.split("/")[0];
	        if(type.equals("image"))
	        {
	        	avatarName = imgName.substring(0, imgName.lastIndexOf(".")) + "_dev" + imgName.substring(imgName.lastIndexOf("."));
				avatarName = avatarName.replaceAll(" ", "_");
	        	lebah.repository.Thumbnail.create(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName, ResourceBundle.getBundle("dbconnection").getString("folder") + imgName, 600, 560, 100);
				lebah.repository.Thumbnail.create(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName, ResourceBundle.getBundle("dbconnection").getString("folder") + avatarName, 150, 90, 100);
	        }
			
			if(!imgName.equals("")) {
				simpanDokumen(idPermohonan, imgName, avatarName, tajuk, idJenisDokumen, keterangan);
			}
		}
//		context.put("selectedTab", "2");
//		return getPath() + "/entry_page.vm";
		return getPath() + "/dokumenSokongan/uploadDoc.vm";
	}
	
	public void simpanDokumen(String idPermohonan, String imgName, String avatarName, String tajuk, String idJenisDokumen, String keterangan) throws Exception {
		JrpDokumen a = new JrpDokumen();
	
		db.begin();
		a.setPermohonan(db.find(JrpPermohonan.class, idPermohonan));
		a.setPhotofilename(imgName);
		a.setThumbfilename(avatarName);	
		a.setTajuk(tajuk);
		a.setJenisDokumen(db.find(JenisDokumen.class, idJenisDokumen));
		a.setKeterangan(keterangan);
		
		db.persist(a);
		db.commit();
	}
	
	@Command("deleteDokumen")
	public String deleteDokumen() throws Exception {
		String dokumenId = get("dokumenId");
		
		JrpDokumen a = db.find(JrpDokumen.class, dokumenId);
		
		if(a != null) {		
			db.begin();
			db.remove(a);
			db.commit();		
		}
		
		List<JrpDokumen> listDokumen = db.list("SELECT x FROM JrpDokumen x WHERE x.permohonan.id = '" + get("idPermohonan") +"'");
		context.put("listDokumen", listDokumen);
		context.put("selectedTab", "2");
		return getPath() + "/entry_page.vm";
//		return getPath() + "/dokumenSokongan/listDokumen.vm";	
	}
	@Command("refreshList")
	public String refreshList() throws Exception {

		JrpPermohonan jrpPermohonan = db.find(JrpPermohonan.class, get("idPermohonan"));
		context.put("r", jrpPermohonan);
		
		List<JrpDokumen> listDokumen = db.list("SELECT x FROM JrpDokumen x WHERE x.permohonan.id = '" + get("idPermohonan") +"'");
		context.put("listDokumen", listDokumen);
		context.put("selectedTab", "2");
		return getPath() + "/entry_page.vm";
//		return getPath() + "/dokumenSokongan/listDokumen.vm";
	}
	
	@Command("doHantarUlasanJRP")
	public String doHantarUlasanJRP() throws Exception {
		String statusInfo = "";
		Users users = db.find(Users.class, userId);
		JrpPermohonan permohonan = db.find(JrpPermohonan.class, getParam("idPermohonan"));
		
		db.begin();			
		permohonan.setStatus(db.find(Status.class, "1424860634475")); //ULASAN JRP
		permohonan.setIdKemaskini(db.find(Users.class, userId));
		permohonan.setTarikhKemaskini(new Date());
		permohonan.setTarikhHantarUlasan(new Date());

		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			statusInfo = "error";
		}
		context.put("selectedTab", "1");
		context.put("selectedSubTab", "1");
		context.put("statusInfo", statusInfo);
		return getPath() + "/entry_page.vm";
//		return getPath() + "/maklumatPermohonan/start.vm";
	}
	
	@Command("doHantarPermohonan")
	public String doHantarPermohonan() throws Exception {
		String statusInfo = "";
		Users users = db.find(Users.class, userId);
		JrpPermohonan permohonan = db.find(JrpPermohonan.class, getParam("idPermohonan"));
		
		db.begin();	
		permohonan.setTarikhTerima(new Date());
		permohonan.setStatus(db.find(Status.class, "1424860634478")); //PERMOHONAN LENGKAP
		permohonan.setIdKemaskini(db.find(Users.class, userId));
		permohonan.setTarikhKemaskini(new Date());

		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			statusInfo = "error";
		}
		context.put("selectedTab", "1");
		context.put("selectedSubTab", "1");
		context.put("statusInfo", statusInfo);
		return getPath() + "/entry_page.vm";
//		return getPath() + "/maklumatPermohonan/start.vm";
	}
	
	@Command("doAgihTugasan")
	public String doAgihTugasan() throws Exception {
		String statusInfo = "";
		Users users = db.find(Users.class, userId);
		JrpPermohonan permohonan = db.find(JrpPermohonan.class, getParam("idPermohonan"));
		
		db.begin();	
		permohonan.setTarikhAgihan(new Date());
		permohonan.setUrusetia(db.find(Users.class, getParam("idPenyediaJRP")));
		permohonan.setCatatanAgihan(get("catatanAgihan"));
		permohonan.setStatus(db.find(Status.class, "1424860634481")); //PENYEDIAAN KERTAS PERTIMBANGAN
		permohonan.setIdKemaskini(db.find(Users.class, userId));
		permohonan.setTarikhKemaskini(new Date());

		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			statusInfo = "error";
		}
		context.put("selectedTab", "1");
		context.put("selectedSubTab", "1");
		context.put("statusInfo", statusInfo);
		return getPath() + "/entry_page.vm";
//		return getPath() + "/maklumatPermohonan/start.vm";
	}

	/** START MAKLUMAT ULASAN
	 * 
	 * @return
	 * @throws Exception
	 */
	@Command("getMaklumatUlasan")
	public String getMaklumatUlasan() throws Exception {
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		String idPermohonan = getParam("idPermohonan");
		Users users = db.find(Users.class, userId);
		
		context.remove("ut");
		
		String jpph = "1306", kpkk = "1262", jbpm = "2005", epu = "1216", mof = "1301", jkptg = "1804";
		JrpUlasanTeknikal jrpUlasanTeknikal = new JrpUlasanTeknikal();
		String subtab = "";
		if(userRole.equalsIgnoreCase("(JRP) JawatanKuasa Teknikal")){
			String idUserAgensi = users.getAgensi().getId();
			if(idUserAgensi.equalsIgnoreCase(jpph)){
				
				jrpUlasanTeknikal = getObjJrpUlasanTeknikal(jpph,idPermohonan);
				if(jrpUlasanTeknikal != null)
					context.put("idFlagKeputusan", jrpUlasanTeknikal.getFlagKeputusan());
				subtab = "1";							
			}else if(idUserAgensi.equalsIgnoreCase(kpkk)){ 
				jrpUlasanTeknikal = getObjJrpUlasanTeknikal(kpkk,idPermohonan);
				subtab = "2";
			}else if(idUserAgensi.equalsIgnoreCase(jbpm)){ 
				jrpUlasanTeknikal = getObjJrpUlasanTeknikal(jbpm,idPermohonan);
				subtab = "3";
			}else if(idUserAgensi.equalsIgnoreCase(epu)){ 
				jrpUlasanTeknikal = getObjJrpUlasanTeknikal(epu,idPermohonan);
				subtab = "4";
			}else if(idUserAgensi.equalsIgnoreCase(mof)){ 
				jrpUlasanTeknikal = getObjJrpUlasanTeknikal(mof,idPermohonan);
				subtab = "5";
			}else if(idUserAgensi.equalsIgnoreCase(jkptg)){ 
				jrpUlasanTeknikal = getObjJrpUlasanTeknikal(jkptg,idPermohonan);
				subtab = "6";
			}else{
				jrpUlasanTeknikal = getObjJrpUlasanTeknikal(jpph,idPermohonan);
				if(jrpUlasanTeknikal != null)
					context.put("idFlagKeputusan", jrpUlasanTeknikal.getFlagKeputusan());
				subtab = "1";
			}
		}else{
			jrpUlasanTeknikal = getObjJrpUlasanTeknikal(jpph,idPermohonan);
			if(jrpUlasanTeknikal != null)
				context.put("idFlagKeputusan", jrpUlasanTeknikal.getFlagKeputusan());
			subtab = "1";
		}
			
		context.put("idFlagKeputusan", jrpUlasanTeknikal.getFlagKeputusan());
		context.put("ut", jrpUlasanTeknikal);
		context.put("selectedTab", "3");
		context.put("selectedSubTab", subtab);

		return getPath() + "/entry_page.vm";
//		return getPath() + "/ulasanTeknikal/start.vm";
	}
	
	/** START KELULUSAN PERMOHONAN
	 * 
	 * @return
	 * @throws Exception
	 */
	@Command("getKelulusanPermohonan")
	public String getKelulusanPermohonan() throws Exception {
		
		String idPermohonan = getParam("idPermohonan");
		
		context.remove("kp");
		JrpKertasPertimbangan jrpKertasPertimbangan = getObjJrpKertasPertimbangan(idPermohonan);
		
		context.put("kp", jrpKertasPertimbangan);
		context.put("selectedTab", "7");
		context.put("selectedSubTab", "1");
		return getPath() + "/entry_page.vm";
//		return getPath() + "/kelulusanPermohonan/start.vm";
	}
	
	/** START KEPUTUSAN MESYUARAT
	 * 
	 * @return
	 * @throws Exception
	 */
	@Command("getKeputusanMesyuarat")
	public String getKeputusanMesyuarat() throws Exception {
		
		String idPermohonan = getParam("idPermohonan");
		
		context.remove("km");
		JrpMesyuarat jrpMesyuarat = getObjJrpMesyuarat(idPermohonan);
		
		context.put("km", jrpMesyuarat);
		context.put("selectedTab", "7");
		context.put("selectedSubTab", "2");
		return getPath() + "/entry_page.vm";
//		return getPath() + "/kelulusanPermohonan/mesyuarat/start.vm";
	}
	
	
	@Command("getMaklumatKpkk")
	public String getMaklumatKpkk() throws Exception {
		String idPermohonan = getParam("idPermohonan");
		String idKpkk = "1262";
		
		context.remove("ut");
		JrpUlasanTeknikal jrpUlasanTeknikal = getObjJrpUlasanTeknikal(idKpkk,idPermohonan);
		
		context.put("ut", jrpUlasanTeknikal);
		context.put("selectedTab", "3");
		context.put("selectedSubTab", "2");
		return getPath() + "/entry_page.vm";
//		return getPath() + "/ulasanTeknikal/kpkk.vm";
	}
	
	@Command("getMaklumatJbpm")
	public String getMaklumatJbpm() throws Exception {
		String idPermohonan = getParam("idPermohonan");
		String idJbpm = "2005";
		
		context.remove("ut");
		JrpUlasanTeknikal jrpUlasanTeknikal = getObjJrpUlasanTeknikal(idJbpm,idPermohonan);
		
		context.put("ut", jrpUlasanTeknikal);
		context.put("selectedTab", "3");
		context.put("selectedSubTab", "3");
		return getPath() + "/entry_page.vm";
//		return getPath() + "/ulasanTeknikal/jbpm.vm";
	}
	
	@Command("getMaklumatUpe")
	public String getMaklumatUpe() throws Exception {
		String idPermohonan = getParam("idPermohonan");
		String idUpe = "1216";
		
		context.remove("ut");
		JrpUlasanTeknikal jrpUlasanTeknikal = getObjJrpUlasanTeknikal(idUpe,idPermohonan);
		
		context.put("ut", jrpUlasanTeknikal);
		context.put("selectedTab", "3");
		context.put("selectedSubTab", "4");
		return getPath() + "/entry_page.vm";
//		return getPath() + "/ulasanTeknikal/upe.vm";
	}
	
	@Command("getMaklumatMof")
	public String getMaklumatMof() throws Exception {
		String idPermohonan = getParam("idPermohonan");
		String idMof = "1301";
		
		context.remove("ut");
		JrpUlasanTeknikal jrpUlasanTeknikal = getObjJrpUlasanTeknikal(idMof,idPermohonan);
		
		context.put("ut", jrpUlasanTeknikal);
		context.put("selectedTab", "3");
		context.put("selectedSubTab", "5");
		return getPath() + "/entry_page.vm";
//		return getPath() + "/ulasanTeknikal/mof.vm";
	}
	
	@Command("getMaklumatJkptg")
	public String getMaklumatJkptg() throws Exception {
		String idPermohonan = getParam("idPermohonan");
		String idJkptg = "1804";
		
		context.remove("ut");
		JrpUlasanTeknikal jrpUlasanTeknikal = getObjJrpUlasanTeknikal(idJkptg,idPermohonan);
		
		context.put("ut", jrpUlasanTeknikal);
		context.put("selectedTab", "3");
		context.put("selectedSubTab", "6");
		return getPath() + "/entry_page.vm";
//		return getPath() + "/ulasanTeknikal/jkptg.vm";
	}
	
	@Command("saveUlasanKpkk")
	public String saveUlasanKpkk() throws Exception {
		
		String statusInfo = "";
		String idPermohonan = getParam("idPermohonan");
		String idKpkk = "1262";
		
		Date tarikhLawatan = getDate("tarikhLawatan");
		String namaPegawaiPeriksa = getParam("namaPegawaiPeriksa");
		String cawangan = getParam("cawangan");
		String syor = getParam("syor");
		String namaPegawai = getParam("namaPegawai");
		String jawatanPegawai = getParam("jawatanPegawai");
		String flagKeputusan = getParam("flagKeputusan");
		
		JrpUlasanTeknikal ul = null;
		JrpUlasanTeknikal jrpUlasanTeknikal = getObjJrpUlasanTeknikal(idKpkk,idPermohonan);
		JrpPermohonan jrpPermohonan = db.find(JrpPermohonan.class, idPermohonan);
		Agensi agensi = db.find(Agensi.class, idKpkk);
		
		if(jrpUlasanTeknikal != null){
			ul = jrpUlasanTeknikal;
		}else{
			ul = new JrpUlasanTeknikal();
			ul.setTarikhUlasan(new Date());
		}
		
		db.begin();
		ul.setJrpPermohonan(jrpPermohonan);
		ul.setAgensi(agensi);
		ul.setTarikhLawatan(tarikhLawatan);
		ul.setCawangan(cawangan);
		ul.setNamaPegawai(namaPegawai);
		ul.setJawatanPegawai(jawatanPegawai);
		ul.setFlagKeputusan(flagKeputusan);
		ul.setNamaPegawaiPeriksa(namaPegawaiPeriksa);
		ul.setSyor(syor);
		db.persist(ul);

		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			statusInfo = "error";
		}
		context.put("selectedTab", "3");
		context.put("selectedSubTab", "2");
		context.put("statusInfo", statusInfo);
		
		return getPath() + "/ulasanTeknikal/status.vm";
	}
	
	@Command("saveUlasanJbpm")
	public String saveUlasanJbpm() throws Exception {
		
		String statusInfo = "";
		String idPermohonan = getParam("idPermohonan");
		String idJbpm = "2005";
		
		Date tarikhLawatan = getDate("tarikhLawatan");
		String namaPegawaiPeriksa = getParam("namaPegawaiPeriksa");
		String cawangan = getParam("cawangan");
		String syor = getParam("syor");
		String namaPegawai = getParam("namaPegawai");
		String jawatanPegawai = getParam("jawatanPegawai");
		Date tarikhUlasan = getDate("tarikhUlasan");
		String flagKeputusan = getParam("flagKeputusan");
		
		JrpUlasanTeknikal ul = null;
		JrpUlasanTeknikal jrpUlasanTeknikal = getObjJrpUlasanTeknikal(idJbpm,idPermohonan);
		JrpPermohonan jrpPermohonan = db.find(JrpPermohonan.class, idPermohonan);
		Agensi agensi = db.find(Agensi.class, idJbpm);
		
		if(jrpUlasanTeknikal != null){
			ul = jrpUlasanTeknikal;
		}else{
			ul = new JrpUlasanTeknikal();
			ul.setTarikhUlasan(new Date());
		}
		
		db.begin();
		ul.setJrpPermohonan(jrpPermohonan);
		ul.setAgensi(agensi);
		ul.setTarikhLawatan(tarikhLawatan);
		ul.setCawangan(cawangan);
		ul.setNamaPegawai(namaPegawai);
		ul.setJawatanPegawai(jawatanPegawai);
		ul.setFlagKeputusan(flagKeputusan);
		ul.setNamaPegawaiPeriksa(namaPegawaiPeriksa);
		ul.setSyor(syor);
		db.persist(ul);

		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			statusInfo = "error";
		}
		context.put("selectedTab", "3");
		context.put("selectedSubTab", "3");
		context.put("statusInfo", statusInfo);
		
		return getPath() + "/ulasanTeknikal/status.vm";
	}
	
	@Command("saveUlasanUpe")
	public String saveUlasanUpe() throws Exception {
		
		String statusInfo = "";
		String idPermohonan = getParam("idPermohonan");
		String idUpe = "1216";
		
//		Date tarikhUlasan = getDate("tarikhUlasan");
		String ulasan = getParam("ulasan");
		String flagKeputusan = getParam("flagKeputusan");
		String namaPegawai = getParam("namaPegawai");
		String jawatanPegawai = getParam("jawatanPegawai");
		
		JrpUlasanTeknikal ul = null;
		JrpUlasanTeknikal jrpUlasanTeknikal = getObjJrpUlasanTeknikal(idUpe,idPermohonan);
		JrpPermohonan jrpPermohonan = db.find(JrpPermohonan.class, idPermohonan);
		Agensi agensi = db.find(Agensi.class, idUpe);
		
		if(jrpUlasanTeknikal != null){
			ul = jrpUlasanTeknikal;
		}else{
			ul = new JrpUlasanTeknikal();
			ul.setTarikhUlasan(new Date());
		}
		
		db.begin();
		ul.setJrpPermohonan(jrpPermohonan);
		ul.setAgensi(agensi);
//		ul.setTarikhUlasan(tarikhUlasan);
		ul.setUlasan(ulasan);
		ul.setFlagKeputusan(flagKeputusan);
		ul.setNamaPegawai(namaPegawai);
		ul.setJawatanPegawai(jawatanPegawai);
		db.persist(ul);

		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			statusInfo = "error";
		}
		context.put("selectedTab", "3");
		context.put("selectedSubTab", "4");
		context.put("statusInfo", statusInfo);
		
		return getPath() + "/ulasanTeknikal/status.vm";
	}
	
	
	@Command("saveUlasanMof")
	public String saveUlasanMof() throws Exception {
		
		String statusInfo = "";
		String idPermohonan = getParam("idPermohonan");
		String idMof = "1301";
		
//		Date tarikhUlasan = getDate("tarikhUlasan");
		String ulasan = getParam("ulasan");
		String flagKeputusan = getParam("flagKeputusan");
		String namaPegawai = getParam("namaPegawai");
		String jawatanPegawai = getParam("jawatanPegawai");
		
		JrpUlasanTeknikal ul = null;
		JrpUlasanTeknikal jrpUlasanTeknikal = getObjJrpUlasanTeknikal(idMof,idPermohonan);
		JrpPermohonan jrpPermohonan = db.find(JrpPermohonan.class, idPermohonan);
		Agensi agensi = db.find(Agensi.class, idMof);
		
		if(jrpUlasanTeknikal != null){
			ul = jrpUlasanTeknikal;
		}else{
			ul = new JrpUlasanTeknikal();
			ul.setTarikhUlasan(new Date());
		}
		
		db.begin();
		ul.setJrpPermohonan(jrpPermohonan);
		ul.setAgensi(agensi);
		ul.setUlasan(ulasan);
		ul.setFlagKeputusan(flagKeputusan);
		ul.setNamaPegawai(namaPegawai);
		ul.setJawatanPegawai(jawatanPegawai);
		db.persist(ul);

		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			statusInfo = "error";
		}
		context.put("selectedTab", "3");
		context.put("selectedSubTab", "5");
		context.put("statusInfo", statusInfo);
		
		return getPath() + "/ulasanTeknikal/status.vm";
	}
	
	@Command("saveUlasanJkptg")
	public String saveUlasanJkptg() throws Exception {
		
		String statusInfo = "";
		String idPermohonan = getParam("idPermohonan");
		String idJkptg = "1804";
		
//		Date tarikhUlasan = getDate("tarikhUlasan");
		String ulasan = getParam("ulasan");
		String flagKeputusan = getParam("flagKeputusan");
		String namaPegawai = getParam("namaPegawai");
		String jawatanPegawai = getParam("jawatanPegawai");
		
		JrpUlasanTeknikal ul = null;
		JrpUlasanTeknikal jrpUlasanTeknikal = getObjJrpUlasanTeknikal(idJkptg,idPermohonan);
		JrpPermohonan jrpPermohonan = db.find(JrpPermohonan.class, idPermohonan);
		Agensi agensi = db.find(Agensi.class, idJkptg);
		
		if(jrpUlasanTeknikal != null){
			ul = jrpUlasanTeknikal;
		}else{
			ul = new JrpUlasanTeknikal();
			ul.setTarikhUlasan(new Date());
		}
		
		db.begin();
		ul.setJrpPermohonan(jrpPermohonan);
		ul.setAgensi(agensi);
//		ul.setTarikhUlasan(tarikhUlasan);
		ul.setUlasan(ulasan);
		ul.setFlagKeputusan(flagKeputusan);
		ul.setNamaPegawai(namaPegawai);
		ul.setJawatanPegawai(jawatanPegawai);
		db.persist(ul);

		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			statusInfo = "error";
		}
		context.put("selectedTab", "3");
		context.put("selectedSubTab", "5");
		context.put("statusInfo", statusInfo);
		
		return getPath() + "/ulasanTeknikal/status.vm";
	}
	
	@Command("selectFlagGst")
	public String selectFlagGst() throws Exception {

		String flagGst = "T";
		if (get("flagGst").trim().length() > 0) 
		
		flagGst = get("flagGst");
		context.put("idGst", flagGst);
	
		
		return getPath() + path + "/ulasanTeknikal/selectGst1.vm";
	}
	
	@Command("selectFlagKeputusan")
	public String selectFlagKeputusan() throws Exception {

		String flagKeputusan = "T";
		if (get("flagKeputusan").trim().length() > 0) 
		
		flagKeputusan = get("flagKeputusan");
		context.put("idFlagKeputusan", flagKeputusan);
		
		return getPath() + path + "/ulasanTeknikal/selectNilaiTinggi.vm";
	}
	
	@Command("saveUlasanJpph")
	public String saveUlasanJpph() throws Exception {
		
		String statusInfo = "";
		String idPermohonan = getParam("idPermohonan");
		String idJPPH = "1306";
		
		String cawangan = getParam("cawangan");
		String flagKeputusan = getParam("flagKeputusan");
		Double nilaianMp = 0.00;
		String luasMp = "";
		Double nilaianKp = 0.00;
		String luasKp = "";
		
		String flagGst = "";
		Double nilaianJpphMp = 0.00;
		String luasJpphMp = "";
		Double nilaianJpphKp = 0.00;
		String luasJpphKp = "";
		Double nilaianGstMp = 0.00;
		Double nilaianGstKp = 0.00;
		
		nilaianMp = Double.valueOf(Util.RemoveComma(getParam("nilaianMp")));
		luasMp = getParam("luasMp");
		nilaianKp = Double.valueOf(Util.RemoveComma(getParam("nilaianKp")));
		luasKp = getParam("luasKp");
		flagGst = getParam("flagGst");
		
		if("TS".equals(flagKeputusan)){
			nilaianJpphMp = Double.valueOf(Util.RemoveComma(getParam("nilaianJpphMp")));
			luasJpphMp = getParam("luasJpphMp");
			nilaianJpphKp = Double.valueOf(Util.RemoveComma(getParam("nilaianJpphKp")));
			luasJpphKp = getParam("luasJpphKp");	
		}
		
		if("Y".equalsIgnoreCase(flagGst)){
			nilaianGstMp = Double.valueOf(Util.RemoveComma(getParam("nilaianGstMp")));
			nilaianGstKp = Double.valueOf(Util.RemoveComma(getParam("nilaianGstKp")));
		}
		
		Double perakuanSewaBulanan = Double.valueOf(Util.RemoveComma(getParam("perakuanSewaBulanan")));
		Integer tempohSewa = getParamAsInteger("tempohSewa");
		String syaratSewa1 = getParam("syaratSewa1");
		String syaratSewa2 = getParam("syaratSewa2");
		String syaratSewa3 = getParam("syaratSewa3");
		String namaPegawai = getParam("namaPegawai");
		String jawatanPegawai = getParam("jawatanPegawai");
		String ulasan = getParam("ulasan");
		
		JrpUlasanTeknikal ul = null;
		JrpUlasanTeknikal jrpUlasanTeknikal = getObjJrpUlasanTeknikal(idJPPH,idPermohonan);
		JrpPermohonan jrpPermohonan = db.find(JrpPermohonan.class, idPermohonan);
		Agensi agensi = db.find(Agensi.class, idJPPH);
		
		if(jrpUlasanTeknikal != null){
			ul = jrpUlasanTeknikal;
		}else{
			ul = new JrpUlasanTeknikal();
			ul.setTarikhUlasan(new Date());
		}
		
		db.begin();
		ul.setJrpPermohonan(jrpPermohonan);
		ul.setAgensi(agensi);
		ul.setCawangan(cawangan);
		ul.setFlagKeputusan(flagKeputusan);
		ul.setNamaPegawai(namaPegawai);
		ul.setJawatanPegawai(jawatanPegawai);
		ul.setNilaianMp(nilaianMp);
		ul.setLuasKp(luasKp);
		ul.setLuasMp(luasMp);
		ul.setNilaianKp(nilaianKp);	
		ul.setPerakuanSewaBulanan(perakuanSewaBulanan);
		ul.setTempohSewa(tempohSewa);
		ul.setSyaratSewa1(syaratSewa1);
		ul.setSyaratSewa2(syaratSewa2);
		ul.setSyaratSewa3(syaratSewa3);
		ul.setUlasan(ulasan);
		ul.setNilaianJpphMp(nilaianJpphMp);
		ul.setLuasJpphMp(luasJpphMp);
		ul.setNilaianJpphKp(nilaianJpphKp);
		ul.setLuasJpphKp(luasJpphKp);
		ul.setFlagGst(flagGst);
		ul.setNilaianGstMp(nilaianGstMp);
		ul.setNilaianGstKp(nilaianGstKp);
		db.persist(ul);

		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			statusInfo = "error";
		}
		context.put("selectedTab", "3");
		context.put("selectedSubTab", "1");
		context.put("statusInfo", statusInfo);
		
		return getPath() + "/ulasanTeknikal/status.vm";
	}
	
	@Command("saveMesyuarat")
	public String saveMesyuarat() throws Exception {
		
		String statusInfo = "";
		String idPermohonan = getParam("idPermohonan");
	    
		Date tarikhMesyuarat = getDate("tarikhMesyuarat");
		Date tarikhSah = getDate("tarikhSah");
		//String bilMesyuarat = getParam("bilMesyuarat");
		String ulasan = getParam("ulasan");
		String urusetiaPenyedia = getParam("urusetiaPenyedia");
		String urusetiaPengesah = getParam("urusetiaPengesah");
		String flagKeputusan = getParam("flagKeputusan");
		
		
		JrpMesyuarat ul = null;
		JrpMesyuarat jrpMesyuarat = getObjJrpMesyuarat(idPermohonan);
		JrpPermohonan jrpPermohonan = db.find(JrpPermohonan.class, idPermohonan);
		
		JrpKertasPertimbangan jrpKertasPertimbangan = getObjJrpKertasPertimbangan(idPermohonan);
		String bilMesy = "";
		if(jrpKertasPertimbangan!=null){
			bilMesy = jrpKertasPertimbangan.getBilMesyuarat().toString();
		}
		
		if(jrpMesyuarat != null){
			ul = jrpMesyuarat;
		}else{
			ul = new JrpMesyuarat();
		}
		
		db.begin();
		ul.setJrpPermohonan(jrpPermohonan);
		ul.setTarikhMesyuarat(tarikhMesyuarat);
		ul.setTarikhSah(tarikhSah);
		ul.setBilMesyuarat(bilMesy);
		ul.setFlagKeputusan(flagKeputusan);
		ul.setUlasan(ulasan);
		ul.setUrusetiaPenyedia(db.find(Users.class,urusetiaPenyedia));
		ul.setUrusetiaPengesah(db.find(Users.class,urusetiaPengesah));		
		db.persist(ul);
		
		if(flagKeputusan.equalsIgnoreCase("L")){ 
			jrpPermohonan.setStatus(db.find(Status.class, "1424860634493")); //LULUS
		}else if(flagKeputusan.equalsIgnoreCase("TL")){ 
			jrpPermohonan.setStatus(db.find(Status.class, "1424860634496")); //TOLAK
		} else if(flagKeputusan.equalsIgnoreCase("TG")){ 
			jrpPermohonan.setStatus(db.find(Status.class, "1424924558493")); //TANGGUH
		} else if(flagKeputusan.equalsIgnoreCase("LB")){ 
			jrpPermohonan.setStatus(db.find(Status.class, "1424860634499")); //LULUS BERSYARAT
		}
		jrpPermohonan.setIdKemaskini(db.find(Users.class, userId));
		jrpPermohonan.setTarikhKemaskini(new Date());
		
		try {
			db.commit();			
			statusInfo = "success";
		} catch (Exception e) {
			statusInfo = "error";
		}
		context.put("selectedTab", "7");
		context.put("selectedSubTab", "2");
		context.put("statusInfo", statusInfo);
		
		return getPath() + "/kelulusanPermohonan/status.vm";
	}
	
//	@Command("saveKertasPertimbangan")
//	public String saveKertasPertimbangan() throws Exception {
//		
//		String statusInfo = "";
//		String idPermohonan = getParam("idPermohonan");
//	    
//	    String bilMesyuarat = getParam("bilMesyuarat");
//	    String noDaftar = getParam("noDaftar");
//	    Date tarikhLengkapPermohonan = getDate("tarikhLengkapPermohonan");
//	    String catatan = getParam("catatan");
//	    String disediakanOleh = getParam("disediakanOleh");
//	    String disemakOleh = getParam("disemakOleh");
//	    String disahkanOleh = getParam("disahkanOleh");
//	    String flagSyorBersyarat = getParam("flagSyorBersyarat");
//	    String ulasanPenolongUrusetia = getParam("ulasanPenolongUrusetia");
//	    String ulasanKetuaUrusetia = getParam("ulasanKetuaUrusetia");
//	    Date tarikhSemakan = getDate("tarikhSemakan");
//	    Date tarikhPengesahan = getDate("tarikhPengesahan");
//	    
//		JrpKertasPertimbangan kp = null;
//		JrpKertasPertimbangan jrpKertasPertimbangan = getObjJrpKertasPertimbangan(idPermohonan);
//		JrpPermohonan jrpPermohonan = db.find(JrpPermohonan.class, idPermohonan);
//		
//		if(jrpKertasPertimbangan != null){
//			kp = jrpKertasPertimbangan;
//		}else{
//			kp = new JrpKertasPertimbangan();
//		}
//		
//		db.begin();
//		kp.setJrpPermohonan(jrpPermohonan);
//		kp.setBilMesyuarat(bilMesyuarat);
//		kp.setNoDaftar(noDaftar);
//		kp.setTarikhLengkapPermohonan(tarikhLengkapPermohonan);
//		kp.setCatatan(catatan);
//		kp.setDisediakanOleh(disediakanOleh);
//		kp.setDisemakOleh(disemakOleh);
//		kp.setDisahkanOleh(disahkanOleh);
//		kp.setFlagSyorBersyarat(flagSyorBersyarat);
//		kp.setUlasanPenolongUrusetia(ulasanPenolongUrusetia);
//		kp.setUlasanKetuaUrusetia(ulasanKetuaUrusetia);
//		kp.setTarikhSemakan(tarikhSemakan);
//		kp.setTarikhPengesahan(tarikhPengesahan);
//		db.persist(kp);
//
//		try {
//			db.commit();
//			statusInfo = "success";
//		} catch (Exception e) {
//			statusInfo = "error";
//		}
//		
//		context.put("selectedSubTab", "1");
//		context.put("statusInfo", statusInfo);
//		
//		return getPath() + "/kelulusanPermohonan/status.vm";
//	}
	
	@Command("saveKertasPertimbanganUrusetia")
	public String saveKertasPertimbanganUrusetia() throws Exception {
		
		String statusInfo = "";
		String idPermohonan = getParam("idPermohonan");
	    
	    String bilMesyuarat = getParam("bilMesyuarat");
	    String noDaftar = getParam("noDaftar");
//	    Date tarikhLengkapPermohonan = getDate("tarikhLengkapPermohonan");
	    String catatan = getParam("catatan");
	    String disediakanOleh = getParam("disediakanOleh");
	    String flagSyorBersyarat = getParam("flagSyorBersyarat");
	    
		JrpKertasPertimbangan kp = null;
		JrpKertasPertimbangan jrpKertasPertimbangan = getObjJrpKertasPertimbangan(idPermohonan);
		JrpPermohonan jrpPermohonan = db.find(JrpPermohonan.class, idPermohonan);
		if(jrpKertasPertimbangan != null){
			kp = jrpKertasPertimbangan;
		}else{
			kp = new JrpKertasPertimbangan();
			kp.setTarikhLengkapPermohonan(jrpPermohonan.getTarikhTerima());
		}
		
		db.begin();
		kp.setJrpPermohonan(jrpPermohonan);
		kp.setBilMesyuarat(bilMesyuarat);
		kp.setNoDaftar(noDaftar);
		kp.setCatatan(catatan);
		kp.setDisediakanOleh(db.find(Users.class,disediakanOleh));
		kp.setFlagSyorBersyarat(flagSyorBersyarat);
		db.persist(kp);

		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			statusInfo = "error";
		}
		context.put("selectedTab", "7");
		context.put("selectedSubTab", "1");
		context.put("statusInfo", statusInfo);
		
		return getPath() + "/kelulusanPermohonan/status.vm";
	}
	
	
	@Command("saveKertasPertimbanganPenolongUrusetia")
	public String saveKertasPertimbanganPenolongUrusetia() throws Exception {
		
		String statusInfo = "";
		String idKertasPertimbangan = getParam("idKertasPertimbangan");
	    
	    String disemakOleh = getParam("disemakOleh");
	    String ulasanPenolongUrusetia = getParam("ulasanPenolongUrusetia");
//	    Date tarikhSemakan = getDate("tarikhSemakan");
	    
		JrpKertasPertimbangan kp = db.find(JrpKertasPertimbangan.class, idKertasPertimbangan);
		
		db.begin();
		kp.setDisemakOleh(db.find(Users.class,disemakOleh));
		kp.setUlasanPenolongUrusetia(ulasanPenolongUrusetia);
		if(kp.getTarikhSemakan() == null){
		kp.setTarikhSemakan(new Date());
		}
		db.persist(kp);

		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			statusInfo = "error";
		}
		context.put("selectedTab", "7");
		context.put("selectedSubTab", "1");
		context.put("statusInfo", statusInfo);
		
		return getPath() + "/kelulusanPermohonan/status.vm";
	}
	
	@Command("saveKertasPertimbanganKetuaUrusetia")
	public String saveKertasPertimbanganKetuaUrusetia() throws Exception {
		
		String statusInfo = "";
		String idKertasPertimbangan = getParam("idKertasPertimbangan");
	    
	    String disahkanOleh = getParam("disahkanOleh");
	    String ulasanKetuaUrusetia = getParam("ulasanKetuaUrusetia");
//	    Date tarikhPengesahan = getDate("tarikhPengesahan");
	    
		JrpKertasPertimbangan kp = db.find(JrpKertasPertimbangan.class, idKertasPertimbangan);
		JrpPermohonan p = db.find(JrpPermohonan.class, kp.getJrpPermohonan().getId());
		Status s = db.find(Status.class, "1424860634490"); //mesyuarat
		
		db.begin();
		kp.setDisahkanOleh(db.find(Users.class,disahkanOleh));
		kp.setUlasanKetuaUrusetia(ulasanKetuaUrusetia);
		if(kp.getTarikhPengesahan() == null){
			kp.setTarikhPengesahan(new Date());
			}
		db.persist(kp);

		try {
			db.commit();
			statusInfo = "success";
			
			db.begin();
			p.setStatus(s);
			db.persist(p);
			db.commit();
			
		} catch (Exception e) {
			statusInfo = "error";
		}
		context.put("selectedTab", "7");
		context.put("selectedSubTab", "1");
		context.put("statusInfo", statusInfo);
		
		return getPath() + "/kelulusanPermohonan/status.vm";
	}
	
	@Command("hantarSemakanKp")
	public String hantarSemakanKp() throws Exception {
		
		String statusInfo = "";
		String idPermohonan = getParam("idPermohonan");
	    
		JrpPermohonan p = db.find(JrpPermohonan.class, idPermohonan);
		Status s = db.find(Status.class, "1424860634484"); //mesyuarat
		
		db.begin();
		p.setStatus(s);
		db.persist(p);

		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			statusInfo = "error";
		}
		context.put("selectedTab", "7");
		context.put("selectedSubTab", "1");
		context.put("statusInfo", statusInfo);
		
		return getPath() + "/kelulusanPermohonan/status.vm";
	}
	
	@Command("hantarPengesahanKp")
	public String hantarPengesahanKp() throws Exception {
		
		String statusInfo = "";
		String idPermohonan = getParam("idPermohonan");
	    
		JrpPermohonan p = db.find(JrpPermohonan.class, idPermohonan);
		Status s = db.find(Status.class, "1424860634487"); //mesyuarat
		
		db.begin();
		p.setStatus(s);
		db.persist(p);

		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			statusInfo = "error";
		}
		context.put("selectedTab", "7");
		context.put("selectedSubTab", "1");
		context.put("statusInfo", statusInfo);
		
		return getPath() + "/kelulusanPermohonan/status.vm";
	}
	
	public JrpUlasanTeknikal getObjJrpUlasanTeknikal(String idAgensi, String idPermohonan) throws Exception {
		
		@SuppressWarnings("unchecked")
		List<JrpUlasanTeknikal>listJrpUlasanTeknikal = db.list("SELECT x FROM JrpUlasanTeknikal x WHERE x.agensi.id ='"+idAgensi+"' AND x.jrpPermohonan.id = '"+idPermohonan+"' ");
		JrpUlasanTeknikal jrpUlasanTeknikal = db.find(JrpUlasanTeknikal.class, (!listJrpUlasanTeknikal.isEmpty()?listJrpUlasanTeknikal.get(0).getId():""));
		
		return jrpUlasanTeknikal;
		
	}
	
	public JrpMesyuarat getObjJrpMesyuarat(String idPermohonan) throws Exception {
		
		@SuppressWarnings("unchecked")
		List<JrpMesyuarat>listJrpMesyuarat = db.list("SELECT x FROM JrpMesyuarat x WHERE x.jrpPermohonan.id = '"+idPermohonan+"' ");
		JrpMesyuarat jrpMesyuarat = db.find(JrpMesyuarat.class, (!listJrpMesyuarat.isEmpty()?listJrpMesyuarat.get(0).getId():""));
		
		return jrpMesyuarat;
	}
	
	public JrpKertasPertimbangan getObjJrpKertasPertimbangan(String idPermohonan) throws Exception {
		
		@SuppressWarnings("unchecked")
		List<JrpKertasPertimbangan>listJrpKertasPertimbangan = db.list("SELECT x FROM JrpKertasPertimbangan x WHERE x.jrpPermohonan.id = '"+idPermohonan+"' ");
		JrpKertasPertimbangan jrpKertasPertimbangan = db.find(JrpKertasPertimbangan.class, (!listJrpKertasPertimbangan.isEmpty()?listJrpKertasPertimbangan.get(0).getId():""));
		
		return jrpKertasPertimbangan;
	}
	
	/** DROP DOWN
	 * 
	 * @return
	 * @throws Exception
	 */
	@Command("selectAgensi")
	public String selectAgensi() throws Exception {
		String idKementerian = "0";
		if (get("idKementerian").trim().length() > 0)
			idKementerian = get("idKementerian");
		
		List<Agensi> list = dataUtil.getListAgensi(idKementerian);
		context.put("selectAgensi", list);

		return getPath() + "/selectAgensi.vm";
	}
	
	@Command("selectBandarPemohon")
	public String selectBandarPemohon() throws Exception {
		String idNegeri = "0";
		if (get("idNegeriPemohon").trim().length() > 0)
			idNegeri = get("idNegeriPemohon");
		
		List<Bandar> list = dataUtil.getListBandar(idNegeri);
		context.put("selectBandarPemohon", list);

		return getPath() + "/selectBandarPemohon.vm";
	}
	
	@Command("selectBandarSediaAda")
	public String selectBandarSediaAda() throws Exception {
		String idNegeri = "0";
		if (get("idNegeriSediaAda").trim().length() > 0)
			idNegeri = get("idNegeriSediaAda");
		
		List<Bandar> list = dataUtil.getListBandar(idNegeri);
		context.put("selectBandar", list);

		return getPath() + "/maklumatPermohonan/selectBandarSediaAda.vm";
	}
	
	@Command("selectBandarBaru")
	public String selectBandarBaru() throws Exception {
		String idNegeri = "0";
		if (get("idNegeriBaru").trim().length() > 0)
			idNegeri = get("idNegeriBaru");
		
		List<Bandar> list = dataUtil.getListBandar(idNegeri);
		context.put("selectBandar", list);

		return getPath() + "/maklumatPermohonan/selectBandarBaru.vm";
	}
	
	@Command("findBandar")
	public String findBandar() throws Exception {
		String idNegeri = "0";
		if (get("findNegeri").trim().length() > 0)
			idNegeri = get("findNegeri");
		
		List<Bandar> list = dataUtil.getListBandar(idNegeri);
		context.put("selectBandar", list);

		return getPath() + "/findBandar.vm";
	}
	
	@Command("findAgensi")
	public String findAgensi() throws Exception {
		String idKementerian = "0";
		if (get("findKementerian").trim().length() > 0)
			idKementerian = get("findKementerian");
		
		List<Agensi> list = dataUtil.getListAgensi(idKementerian);
		context.put("selectAgensi", list);

		return getPath() + "/findAgensi.vm";
	}

	/** TAB
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getTabIdAndClassName() throws Exception{
		String portal_role  = (String)request.getSession().getAttribute("_portal_role");
		String tabIdAndClassName = "";
		try {
			tabIdAndClassName = lebah.util.Util.getTabID("Permohonan", portal_role)+"?_portal_module=bph.modules.jrp.SenaraiPermohonanJrpRecordModule";;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tabIdAndClassName;
	}
	
	
	/** START MAKLUMAT PERJANJIAN
	 * 
	 * @return
	 * @throws Exception
	 */
	@Command("getMaklumatPerjanjian")
	public String getMaklumatPerjanjian() throws Exception {
		
		String idPermohonan = getParam("idPermohonan");
		JrpPermohonan jrpPermohonan = db.find(JrpPermohonan.class,idPermohonan);
		context.put("r", jrpPermohonan);
		
		context.put("selectedTab", "8");
		return getPath() + "/entry_page.vm";
//		return getPath() + "/maklumatPerjanjian/start.vm";
	}
	
	@Command("savePerjanjian")
	public String savePerjanjian() throws Exception {
		
		String statusInfo = "";
		String idPermohonan = getParam("idPermohonan");
	    
	    String noSiriPerjanjian = getParam("noSiriPerjanjian");
	    Date tarikhMulaPerjanjian = getDate("tarikhMulaPerjanjian");
	    Date tarikhTamatPerjanjian = getDate("tarikhTamatPerjanjian");
	    Double kadarSewa = Util.getDouble(Util.RemoveComma(get("kadarSewa")));
	    Date tarikhMulaDuduk = getDate("tarikhMulaDuduk");
	    
		JrpPermohonan p = db.find(JrpPermohonan.class, idPermohonan);
		
		db.begin();
		p.setNoSiriPerjanjian(noSiriPerjanjian);
		p.setTarikhMulaPerjanjian(tarikhMulaPerjanjian);
		p.setTarikhTamatPerjanjian(tarikhTamatPerjanjian);
		p.setKadarSewa(kadarSewa);
		p.setTarikhMulaDudukJrp(tarikhMulaDuduk);
		db.persist(p);

		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			statusInfo = "error";
		}
		context.put("selectedTab", "8");
		context.put("statusInfo", statusInfo);
		
		return getPath() + "/maklumatPerjanjian/status.vm";
	}
	
	/** START SEMAKAN ULASAN
	 * 
	 * @return
	 * @throws Exception
	 */
	@Command("getSemakanUlasan")
	public String getSemakanUlasan() throws Exception {
		
		String idPermohonan = getParam("idPermohonan");
		List <JrpUlasanTeknikal> listSemakanUlasan = db.list("Select x from  JrpUlasanTeknikal x where x.jrpPermohonan.id='" + idPermohonan + "'");
		context.put("listSemakanUlasan", listSemakanUlasan);
		
		context.put("selectedTab", "4");
		return getPath() + "/entry_page.vm";
//		return getPath() + "/semakanUlasan/start.vm";
	}
	
	@Command("hantarUlasanKeAgensi")
	public String hantarUlasanKeAgensi() throws Exception {
		
		String statusInfo = "";
		String idUlasanTeknikal = getParam("idUlasanTeknikal");
	    
		JrpUlasanTeknikal p = db.find(JrpUlasanTeknikal.class, idUlasanTeknikal);
		
		db.begin();
		p.setFlagHantar("Y");
		db.persist(p);

		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			statusInfo = "error";
		}
		context.put("selectedTab", "4");
		context.put("statusInfo", statusInfo);
		
		return getPath() + "/ulasanTeknikal/status.vm";
	}
	
	/** START BATAL PERMOHONAN
	 * 
	 * @return
	 * @throws Exception
	 */
	
	@Command("getMaklumatBatalPermohonan")
	public String getMaklumatBatalPermohonan() throws Exception {
		
		String idPermohonan = getParam("idPermohonan");
		JrpPermohonan jrpPermohonan = db.find(JrpPermohonan.class,idPermohonan);
		context.put("bp", jrpPermohonan);
		
		context.put("selectedTab", "5");
		return getPath() + "/entry_page.vm";
//		return getPath() + "/batalPermohonan/start.vm";
	}
	
	@Command("batalPermohonan")
	public String batalPermohonan() throws Exception {
		
		String statusInfo = "";
		String idPermohonan = getParam("idPermohonan");
		JrpPermohonan permohonan = (JrpPermohonan) db.get("Select x from  JrpPermohonan x where x.id='" + idPermohonan + "'");
		context.put("bp", permohonan);
		context.put("classView", "");
		
		context.put("selectedTab", "5");
		return getPath() + "/batalPermohonan/form.vm";
	}
	
	@Command("simpanBatalPermohonan")
	public String simpanBatalPermohonan() throws Exception {
		
		String statusInfo = "";
		String idPermohonan = getParam("idPermohonan");
		JrpPermohonan permohonan = (JrpPermohonan) db.get("Select x from  JrpPermohonan x where x.id='" + idPermohonan + "'");
		
		db.begin();
		permohonan.setStatus(db.find(Status.class,"1424860634502")); //BATAL
		permohonan.setTarikhBatal(getDate("tarikhBatalPermohonan"));
		permohonan.setCatatanBatal(getParam("catatanBatal"));
		db.persist(permohonan);

		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			statusInfo = "error";
		}
		context.put("selectedTab", "5");
		context.put("statusInfo", statusInfo);
		
		return getPath() + "/batalPermohonan/status.vm";
	}
}
