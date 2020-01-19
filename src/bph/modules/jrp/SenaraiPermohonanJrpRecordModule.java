package bph.modules.jrp;

import java.io.File;
import java.text.DecimalFormat;
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
import bph.entities.jrp.JrpKuiri;
import bph.entities.jrp.JrpMesyuarat;
import bph.entities.jrp.JrpPermohonan;
import bph.entities.jrp.JrpPermohonanLokasi;
import bph.entities.jrp.JrpSeqPermohonan;
import bph.entities.jrp.JrpUlasanTeknikal;
import bph.entities.kod.Agensi;
import bph.entities.kod.Bandar;
import bph.entities.kod.Daerah;
import bph.entities.kod.JenisDokumen;
import bph.entities.kod.JenisPermohonanJRP;
import bph.entities.kod.Status;
import bph.mail.mailer.JrpMailer;
import bph.utils.DataUtil;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class SenaraiPermohonanJrpRecordModule extends LebahRecordTemplateModule<JrpPermohonan> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MyPersistence mp;
	private DataUtil dataUtil;

	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public void afterSave(JrpPermohonan r) {
		
		//GENERATE NO PERMOHONAN
		if (r.getNoPermohonan() == null){
			db.begin();

			String year = Util.getDateTime(new Date(), "yyyy");
			JrpSeqPermohonan seq = (JrpSeqPermohonan) db.get("select x from JrpSeqPermohonan x where x.tahun = '" + year
								+ "' and x.kementerian.id = '" + r.getAgensi().getKementerian().getId()
								+ "' and x.agensi.id = '" + r.getAgensi().getId()
								+ "' and x.negeri.id = '" + r.getIdMasuk().getBandar().getNegeri().getId() + "'");
			if(seq == null){
				seq = new JrpSeqPermohonan();
				seq.setTahun(Integer.parseInt(year));
				seq.setKementerian(r.getAgensi().getKementerian());
				seq.setAgensi(r.getAgensi());
				seq.setNegeri(r.getIdMasuk().getBandar().getNegeri());
				seq.setBil(1);
				db.persist(seq);
			} else {
				int next = seq.getBil() + 1;
				seq.setBil(next);
			}
			
			String formatserial = new DecimalFormat("00").format(seq.getBil());
			String noFail = year + "/" + r.getAgensi().getKementerian().getId() + "/" + r.getAgensi().getId() + "/" + r.getIdMasuk().getBandar().getNegeri().getId() + "/" 
							+ formatserial; 
			
			r.setNoPermohonan(noFail);
			
			try {
				db.commit();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		List<Agensi> listAgensi = dataUtil.getListAgensi(r.getAgensi().getKementerian().getId());
		context.put("selectAgensi", listAgensi);
		List<Bandar> listBandar = dataUtil.getListBandar(r.getBandar().getNegeri().getId());
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
		Users users=null;
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		userName = (String) request.getSession().getAttribute("_portal_username");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		context.put("userRole", userRole);
		context.put("dateSurat",new Date());
		
		try {
			mp = new MyPersistence();
			users = (Users) mp.find(Users.class, userId);
			context.put("users", users);
			
			//LIST DROPDOWN
			context.put("selectKementerian", dataUtil.getListKementerian());
			context.put("selectNegeri", dataUtil.getListNegeri());
			context.put("selectDaerah", dataUtil.getListDaerah());
			if(("add_new_record").equals(command)){
				context.put("selectNegeri", users.getBandar().getNegeri());
				context.put("selectBandarPemohon", dataUtil.getListBandar(users.getBandar().getNegeri().getId()));
			}
			context.put("selectJenisPermohonanJRP", dataUtil.getListJenisPermohonanBaruJRP());
			context.put("selectPermohonanJRP", dataUtil.getListJenisPermohonanJRP());
			context.put("selectJenisPermohonanLanjutanJRP", dataUtil.getListJenisPermohonanLanjutanJRP());
			context.put("selectJenisPermohonanTambahanJRP", dataUtil.getListJenisPermohonanTambahanJRP());
			context.put("selectJenisPermohonanPindahJRP", dataUtil.getListJenisPermohonanPindahJRP());
			context.put("selectStatus", dataUtil.getListStatusJawatankuasaRuangPejabat());
			JrpPermohonan permohonan = db.find(JrpPermohonan.class, get("idPermohonan"));
			defaultButtonOption(users);
			addfilter();
			/*context.put("util", new Util());
			context.put("path", getPath());
			context.put("command", command);*/
			context.put("baseDir", getBaseDir());
	//		context.put("currentDate", currentDate);		
	//		ArrayList<String> list = checkTarikhTamat();
	//		checkTarikhTamat();		
	//		context.put("status", list);
			context.put("tabIdAndClassName", getTabIdAndClassName());
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
	}
		
	
	private void defaultButtonOption(Users users) {
		if (!"(JRP) Pemohon".equals(userRole) || ("(JRP) Pemohon".equals(userRole) && "Y".equals(users.getFlagHq()))){
			this.setReadonly(true);
		}
		
		this.setDisableSaveAddNewButton(true);
		if (!"add_new_record".equals(command)){
			this.setDisableBackButton(true);
			this.setDisableDefaultButton(true);
		}		
	}
	
	private void addfilter() {
		Users users = db.find(Users.class, userId);

		if ("(JRP) Pemohon".equals(userRole) && "T".equals(users.getFlagHq())){
			this.addFilter("agensi.id = '" + users.getAgensi().getId() + "'");
			this.addFilter("bandar.negeri.id = '" + users.getBandar().getNegeri().getId() + "'");
		}
		
		if ("(JRP) Pemohon".equals(userRole) && "Y".equals(users.getFlagHq())){
			this.addFilter("agensi.id = '" + users.getAgensi().getId() + "'");
			this.addFilter("idStatus not in ('1424860634472','1424860634475')");
		}
		
		/*if ("(JRP) JawatanKuasa Teknikal".equals(userRole)){
			this.addFilter("status.id = '1424860634475'"); //ULASAN JRP
			this.addFilter("id NOT IN (SELECT y.jrpPermohonan.id FROM JrpUlasanTeknikal y WHERE y.jrpPermohonan.id = x.id AND y.flagHantar = 'Y' AND y.agensi.id = '" + users.getAgensi().getId() + "')"); //ULASAN JRP
		}*/
		
		/*if ("(JRP) Penyedia".equals(userRole)){
//			this.addFilter("id is not null and ((x.idStatus IN ('1436921583144','1424860634481', '1424860634490', '1424860634499', '1424860634493') and x.idUrusetia = '" + users.getId() + "') or x.idStatus = '1424860634478')"); // PERMOHONAN LENGKAP || PENYEDIAAN KERTAS PERTIMBANGAN || MESYUARAT || LULUS BERSYARAT || LULUS 
			this.addFilter("id is not null and x.idStatus IN ('1436921583144','1424860634481', '1424860634490', '1424860634499', '1424860634493','1424860634478')"); // PERMOHONAN LENGKAP || PENYEDIAAN KERTAS PERTIMBANGAN || MESYUARAT || LULUS BERSYARAT || LULUS
		}
		if ("(JRP) Penyemak".equals(userRole)){
			this.addFilter("idStatus = '1424860634484'"); //SEMAKAN KERTAS PERTIMBANGAN
		}
		if ("(JRP) Pelulus".equals(userRole)){
			this.addFilter("idStatus in ('1424860634487','1438356583116')"); //PENGESAHAN KERTAS PERTIMBANGAN
		}*/
		
		if ("(JRP) Penyedia".equals(userRole) || "(JRP) Penyemak".equals(userRole) || "(JRP) Pelulus".equals(userRole)){
			this.addFilter("id is not null and x.idStatus in ('1438356583123','1424860634487','1438356583116','1424860634484','1436921583144','1424860634481', '1424860634490', '1424860634499', '1424860634493','1424860634478','471300128898077','1438356583122')"); // PERMOHONAN LENGKAP || PENYEDIAAN KERTAS PERTIMBANGAN || MESYUARAT || LULUS BERSYARAT || LULUS
		}
		this.addFilter("flagAktif = 'Y'");
		if("(JRP) Pemohon".equals(userRole) && "Y".equals(users.getFlagHq())){
			this.setOrderBy("tarikhHantarUlasan");
		}else if("(JRP) Pemohon".equals(userRole) && "T".equals(users.getFlagHq())){
			this.setOrderBy("tarikhMasuk");
		}else{
			this.setOrderBy("tarikhTerima");
		}
		this.setOrderType("desc");
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
		return "bph/modules/jrp/permohonanJrp";
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
		context.put("selectBandar", listBandar);
		
		context.put("selectedTab", "1");
		context.put("selectedSubTab", "1");
		context.put("selectNegeri", permohonan.getPemohon().getBandar().getNegeri());
		String status = permohonan.getStatus().getId();
		
		if("1424860634478".equals(status)){
			permohonan.setUrusetia(db.find(Users.class, userId));
		}
	}

	@Override
	public void save(JrpPermohonan permohonan) throws Exception {
				
		Users users = db.find(Users.class, userId);
		String tujuan = get("tujuanPermohonan");

		// TODO Auto-generated method stub		
		permohonan.setTarikhSurat(getDate("tarikhSurat"));
		if (!"(JRP) Pemohon".equals(userRole))
			permohonan.setTarikhTerima(getDate("tarikhTerima"));
		permohonan.setJenisPermohonanJrp(db.find(JenisPermohonanJRP.class, get("idJenisPermohonanJRP")));
		permohonan.setAlasanPermohonan(get("alasanPermohonan"));
		
		if(("LAIN - LAIN").equalsIgnoreCase(tujuan)){
			permohonan.setTujuanPermohonan(get("tujuanPermohonan2"));
		}else{
			permohonan.setTujuanPermohonan(tujuan);
		}
		
		if ("(JRP) Pemohon".equals(userRole)) {
			permohonan.setAgensi(db.find(Agensi.class, users.getAgensi().getId()));
		} else {
			permohonan.setAgensi(db.find(Agensi.class, get("idAgensi")));
		}
		permohonan.setPemohon(db.find(Users.class, userId));
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
		m.put("status.id", getParam("findStatusPermohonan"));
		
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
		context.put("selectNegeri", permohonan.getPemohon().getBandar().getNegeri());
		return getPath() + "/entry_page.vm";
//		return getPath() + "/maklumatPermohonan/start.vm";
	}
	
	@Command("getMaklumatRuangSediaAda")
	public String getMaklumatRuangSediaAda() throws Exception {

		String idPermohonan = get("idPermohonan");
		context.remove("rekod");
        JrpPermohonanLokasi rekod = (JrpPermohonanLokasi) db.get("select x from JrpPermohonanLokasi x where x.flagLokasi = 'S' and x.permohonan.id = '" + idPermohonan + "'");
        context.put("rekod", rekod);
        JrpPermohonan permohonan = db.find(JrpPermohonan.class, get("idPermohonan"));
        
        if (rekod != null) {
        	List<Bandar> listBandar = dataUtil.getListBandar(rekod.getBandar().getNegeri().getId());
        	List<Daerah> listDaerah = dataUtil.getListDaerah(rekod.getBandar().getNegeri().getId());
    		context.put("selectBandar", listBandar);
    		context.put("selectDaerah", listDaerah);
        }else{
        	List<Bandar> listBandar = dataUtil.getListBandar(permohonan.getPemohon().getBandar().getNegeri().getId());
        	List<Daerah> listDaerah = dataUtil.getListDaerah(permohonan.getPemohon().getBandar().getNegeri().getId());
        	context.put("selectBandar", listBandar);
    		context.put("selectDaerah", listDaerah);
        }
        context.put("selectNegeri", permohonan.getPemohon().getBandar().getNegeri());
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
        JrpPermohonan permohonan = db.find(JrpPermohonan.class, get("idPermohonan"));
        if (rekod != null) {
        	List<Bandar> listBandar = dataUtil.getListBandar(rekod.getBandar().getNegeri().getId());
        	List<Daerah> listDaerah = dataUtil.getListDaerah(rekod.getBandar().getNegeri().getId());
        	context.put("selectBandar", listBandar);
        	context.put("selectDaerah", listDaerah);
        }else{
        	List<Bandar> listBandar = dataUtil.getListBandar(permohonan.getPemohon().getBandar().getNegeri().getId());
        	List<Daerah> listDaerah = dataUtil.getListDaerah(permohonan.getPemohon().getBandar().getNegeri().getId());
        	context.put("selectBandar", listBandar);
        	context.put("selectDaerah", listDaerah);
        }
        context.put("selectNegeri", permohonan.getPemohon().getBandar().getNegeri());
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
	
	/** END MAKLUMAT PERMOHONAN **/
	
	/** START MAKLUMAT PERMOHONAN LANJUTAN
	 * 
	 * @return
	 * @throws Exception
	 */
	@Command("doDaftarLanjutan")
	public String doDaftarLanjutan() throws Exception {

		String jenisPermohonan = getParam("jenisPermohonan");
		if("3".equals(jenisPermohonan)){
			context.put("selectJenisPermohonanLanjutanJRP", dataUtil.getListJenisPermohonanLanjutanJRP());
		}else if("1".equals(jenisPermohonan)){
			context.put("selectJenisPermohonanLanjutanJRP", dataUtil.getListJenisPermohonanTambahanJRP());
		}else if("2".equals(jenisPermohonan)){
			context.put("selectJenisPermohonanLanjutanJRP", dataUtil.getListJenisPermohonanPindahJRP());
		}
		
		context.put("jenis", jenisPermohonan);
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
		permohonanLanjutan.setPemohon(permohonan.getPemohon());
		
		permohonanLanjutan.setStatus(db.find(Status.class, "1424860634472")); //PERMOHONAN BARU LANJUTAN
		permohonanLanjutan.setIdMasuk(db.find(Users.class, userId));
		permohonanLanjutan.setTarikhMasuk(new Date());
		
		permohonan.setFlagAktif("T"); //set permohonan lama T
		
		db.persist(permohonanLanjutan);
		//END COPY PERMOHONAN OLD TO PERMOHONAN LANJUTAN
		
		// START COPY RUANG BARU OLD TO RUANG SEDIA ADA NEW
		JrpPermohonanLokasi ruangSediaAdaOld = (JrpPermohonanLokasi) db.get("Select x from JrpPermohonanLokasi x where x.permohonan.id = '" + getParam("idPermohonan") + "' and x.flagLokasi = 'B'");
		String cawanganMemerlukanRuang = (String) db.get("Select x.cawangan from JrpPermohonanLokasi x where x.permohonan.id = '" + getParam("idPermohonan") + "' and x.flagLokasi = 'S'");
		
		JrpPermohonanLokasi ruangSediaAdaLanjutan = null;	
		if(ruangSediaAdaOld != null){
		ruangSediaAdaLanjutan = new JrpPermohonanLokasi();
		ruangSediaAdaLanjutan.setPermohonan(db.find(JrpPermohonan.class, permohonanLanjutan.getId())); //set id permohonan lanjutan
		ruangSediaAdaLanjutan.setFlagLokasi("S"); //save permohonan Baru menjadi permohonan sediada
		ruangSediaAdaLanjutan.setCawangan(cawanganMemerlukanRuang.equals(null) ? "" : cawanganMemerlukanRuang);
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
		if(!"BANGUNAN PEJABAT".equals(getParam("jenisBangunanSediaAda")) && !"RUMAH KEDAI".equals(getParam("jenisBangunanSediaAda")) && !"KEDIAMAN".equals(getParam("jenisBangunanSediaAda")) && !"".equals(getParam("jenisBangunanSediaAda")))
			jenisBangunan = get("jenisBangunan2");
		else
			jenisBangunan = get("jenisBangunanSediaAda");		
		rekod.setJenisBangunan(jenisBangunan);		
		rekod.setNamaBangunan(get("namaBangunanSediaAda"));		
		rekod.setAlamat1(get("alamat1SediaAda"));
		rekod.setAlamat2(get("alamat2SediaAda"));
		rekod.setAlamat3(get("alamat3SediaAda"));
		rekod.setPoskod(get("poskodSediaAda"));
		rekod.setDaerah(db.find(Daerah.class, get("idDaerahSediaAda")));
		rekod.setBandar(db.find(Bandar.class, get("idBandarSediaAda")));
		rekod.setNamaPemilikPremis(get("namaPemilikPremisSediaAda"));
		rekod.setSewaSebulan(Util.getDouble(Util.RemoveComma(get("sewaSebulanSediaAda"))));
		rekod.setSewaMp(Util.getDouble(Util.RemoveComma(get("sewaMpSediaAda"))));
		rekod.setSewaKp(Util.getDouble(Util.RemoveComma(get("sewaKpSediaAda"))));
		rekod.setKadarGst(Util.getDouble(Util.RemoveComma(get("kadarGst"))));
		rekod.setTotalSewa(Util.getDouble(Util.RemoveComma(get("totalSewa"))));
		if ( addRekod ) db.persist(rekod);
		db.flush();
		totalSewa(rekod);
		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			statusInfo = "error";
		}
		
		context.remove("rekod");
        rekod = (JrpPermohonanLokasi) db.get("select x from JrpPermohonanLokasi x where x.flagLokasi = 'S' and x.permohonan.id = '" + idPermohonan + "'");
        context.put("selectNegeri", rekod.getBandar().getNegeri());
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
		System.out.println("ceking bangunan ======== " + jenisBangunan);
		context.put("idJenisBangunan", jenisBangunan);
		if(jenisBangunan.equals("BANGUNAN PEJABAT") || jenisBangunan.equals("RUMAH KEDAI") || jenisBangunan.equals("KEDIAMAN") || jenisBangunan.equals("") ){
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
//		rekod.setCawangan(get("cawanganBaru"));
		if(!"BANGUNAN PEJABAT".equals(getParam("jenisBangunanBaru")) && !"RUMAH KEDAI".equals(getParam("jenisBangunanBaru")) && !"KEDIAMAN".equals(getParam("jenisBangunanBaru")) && !"".equals(getParam("jenisBangunanBaru")))
			jenisBangunan = get("jenisBangunan2");
		else
			jenisBangunan = get("jenisBangunanBaru");		
		rekod.setJenisBangunan(jenisBangunan);		
		rekod.setNamaBangunan(get("namaBangunanBaru"));		
		rekod.setAlamat1(get("alamat1Baru"));
		rekod.setAlamat2(get("alamat2Baru"));
		rekod.setAlamat3(get("alamat3Baru"));
		rekod.setPoskod(get("poskodBaru"));
		rekod.setDaerah(db.find(Daerah.class, get("idDaerahBaru")));
		rekod.setBandar(db.find(Bandar.class, get("idBandarBaru")));
		rekod.setNamaPemilikPremis(get("namaPemilikPremisBaru"));
		rekod.setSewaSebulan(Util.getDouble(Util.RemoveComma(get("sewaSebulanBaru"))));
		rekod.setSewaMp(Util.getDouble(Util.RemoveComma(get("sewaMpBaru"))));
		rekod.setTempohSewaMula(getDate("tempohSewaMula"));
		rekod.setTempohSewaTamat(getDate("tempohSewaTamat"));
		rekod.setSewaKp(Util.getDouble(Util.RemoveComma(get("sewaKpBaru"))));
		rekod.setKadarGst(Util.getDouble(Util.RemoveComma(get("kadarGst"))));
		rekod.setTotalSewa(Util.getDouble(Util.RemoveComma(get("totalSewa"))));
		if ( addRekod ) db.persist(rekod);
		db.flush();
		totalSewa(rekod);
		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			statusInfo = "error";
		}
		
		context.remove("rekod");
        rekod = (JrpPermohonanLokasi) db.get("select x from JrpPermohonanLokasi x where x.flagLokasi = 'B' and x.permohonan.id = '" + idPermohonan + "'");
        context.put("rekod", rekod);
        JrpPermohonan permohonan = db.find(JrpPermohonan.class, idPermohonan);
        context.put("selectNegeri", permohonan.getPemohon().getBandar().getNegeri());
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
		if(jenisBangunan.equals("BANGUNAN PEJABAT") || jenisBangunan.equals("RUMAH KEDAI") || jenisBangunan.equals("KEDIAMAN") || jenisBangunan.equals("") ){
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
		permohonan.setNamaKSU(get("namaKSU"));
		permohonan.setJawatanKSU(get("jawatanKSU"));
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
		Boolean addMaklumatRuang = false;
		
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
		
		//rozai tambah 18/8/2016(ORIGINAL)
//		if ( addMaklumatKakitangan ){
//			addMaklumatRuang = true;
//			JrpKeluasanRuang maklumatRuang=null;
//			maklumatRuang = new JrpKeluasanRuang();		
//			maklumatRuang.setPermohonan(db.find(JrpPermohonan.class, get("idPermohonan")));
//			maklumatRuang.setPerkara(get("jawatan")+" ("+ get("gred")+ ")");
//			maklumatRuang.setLuasSediaAda(get("luasSediaAda"));
//			maklumatRuang.setLuasBaru(get("luasBaru"));
//			if ( addMaklumatRuang ) db.persist(maklumatRuang);
//		}
		
		////rozai EDIT 06/03/2018(IMPROVISED)
		if ( addMaklumatKakitangan ){
			addMaklumatRuang = true;
			JrpKeluasanRuang maklumatRuang=null;
			List<JrpKakitangan> listMaklumatKakitangan = db.list("SELECT x FROM JrpKakitangan x WHERE x.permohonan.id = '" + get("idPermohonan") +"' order by x.turutan");
			double luasSediaAda=0;
			double luasBaru=0;
			for(int i=0; i<listMaklumatKakitangan.size(); i++)
			{
				JrpKakitangan kakitangan = listMaklumatKakitangan.get(i); 
				luasSediaAda=luasSediaAda + Double.valueOf(kakitangan.getLuasSediaAda());
				luasBaru=luasBaru + Double.valueOf(kakitangan.getLuasBaru());
			}
			maklumatRuang = new JrpKeluasanRuang();		
			maklumatRuang.setPermohonan(db.find(JrpPermohonan.class, get("idPermohonan")));
			JrpKeluasanRuang maklumatRuangAwal = (JrpKeluasanRuang) db.get("select x from JrpKeluasanRuang x where x.permohonan.id = '" + get("idPermohonan") + "' and x.perkara='RUANG KAKITANGAN'");
			DecimalFormat formatter = new DecimalFormat("#0.00");
			if(maklumatRuangAwal!=null){
				maklumatRuangAwal.setPerkara("RUANG KAKITANGAN");
				maklumatRuangAwal.setLuasSediaAda(formatter.format(luasSediaAda));
				maklumatRuangAwal.setLuasBaru(formatter.format(luasBaru));
			}else{
				maklumatRuang.setPerkara("RUANG KAKITANGAN");
				maklumatRuang.setLuasSediaAda(formatter.format(luasSediaAda));
				maklumatRuang.setLuasBaru(formatter.format(luasBaru));
				if ( addMaklumatRuang ) db.persist(maklumatRuang);	
			}
		}
		
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
		Boolean addMaklumatRuang = false;
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
		
		//rozai improvise 20/2/2018
		if ( addMaklumatKakitangan ){
			addMaklumatRuang = true;
			JrpKeluasanRuang maklumatRuang=null;
			List<JrpKakitangan> listMaklumatKakitangan = db.list("SELECT x FROM JrpKakitangan x WHERE x.permohonan.id = '" + get("idPermohonan") +"' order by x.turutan");
			double luasSediaAda=0;
			double luasBaru=0;
			for(int i=0; i<listMaklumatKakitangan.size(); i++)
			{
				JrpKakitangan kakitangan = listMaklumatKakitangan.get(i); 
				luasSediaAda=luasSediaAda + Double.valueOf(kakitangan.getLuasSediaAda());
				luasBaru=luasBaru + Double.valueOf(kakitangan.getLuasBaru());
			}
			maklumatRuang = new JrpKeluasanRuang();		
			maklumatRuang.setPermohonan(db.find(JrpPermohonan.class, get("idPermohonan")));
			JrpKeluasanRuang maklumatRuangAwal = (JrpKeluasanRuang) db.get("select x from JrpKeluasanRuang x where x.permohonan.id = '" + get("idPermohonan") + "' and x.perkara='RUANG KAKITANGAN'");
			DecimalFormat formatter = new DecimalFormat("#0.00");
			if(maklumatRuangAwal!=null){
				maklumatRuangAwal.setPerkara("RUANG KAKITANGAN");
				maklumatRuangAwal.setLuasSediaAda(formatter.format(luasSediaAda));
				maklumatRuangAwal.setLuasBaru(formatter.format(luasBaru));
			}else{
				maklumatRuang.setPerkara("RUANG KAKITANGAN");
				maklumatRuang.setLuasSediaAda(formatter.format(luasSediaAda));
				maklumatRuang.setLuasBaru(formatter.format(luasBaru));
				if ( addMaklumatRuang ) db.persist(maklumatRuang);	
			}
		}
		
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
		String perkara=get("perkara");
		if(!perkara.equalsIgnoreCase("RUANG KAKITANGAN")){
			keluasanRuang.setPerkara(get("perkara"));
			keluasanRuang.setLuasSediaAda(Util.RemoveComma(getParam("luasSediaAda")));
			keluasanRuang.setLuasBaru(Util.RemoveComma(getParam("luasBaru")));		
			keluasanRuang.setPermohonan(db.find(JrpPermohonan.class, get("idPermohonan")));
		}
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
		Date tarikhMuatNaik = new Date();

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
				simpanDokumen(idPermohonan, imgName, avatarName, tajuk, idJenisDokumen, keterangan, tarikhMuatNaik);
			}
		}
//		context.put("selectedTab", "2");
//		return getPath() + "/entry_page.vm";
		return getPath() + "/dokumenSokongan/uploadDoc.vm";
	}
	
	public void simpanDokumen(String idPermohonan, String imgName, String avatarName, String tajuk, String idJenisDokumen, String keterangan, Date tarikhMuatnaik) throws Exception {
		JrpDokumen a = new JrpDokumen();
	
		db.begin();
		a.setPermohonan(db.find(JrpPermohonan.class, idPermohonan));
		a.setPhotofilename(imgName);
		a.setThumbfilename(avatarName);	
		a.setTajuk(tajuk);
		a.setJenisDokumen(db.find(JenisDokumen.class, idJenisDokumen));
		a.setKeterangan(keterangan);
		a.setTarikhMuatnaik(tarikhMuatnaik);
		
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
		
		List<JenisDokumen> listJenisDokumen = dataUtil.getListJenisDokumenPermohonanJRP();
		context.put("selectJenisDokumen", listJenisDokumen);
		
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
		permohonan.setAgensiHq(db.find(Users.class, userId));
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
	
	@Command("doHantarMaklumbalas")
	public String doHantarMaklumbalas() throws Exception {
		String statusInfo = "";
		Users users = db.find(Users.class, userId);
		JrpPermohonan permohonan = db.find(JrpPermohonan.class, getParam("idPermohonan"));
		JrpKuiri kuiri = db.find(JrpKuiri.class, get("idKuiri"));
		
		db.begin();	
		
		kuiri.setFlagHantarMaklumbalas("Y");
		
		permohonan.setStatus(db.find(Status.class, "1424860634478")); //PERMOHONAN LENGKAP
		permohonan.setIdKemaskini(db.find(Users.class, userId));
		permohonan.setAgensiHq(db.find(Users.class, userId));
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
	
	@Command("doHantarMaklumbalasToPenyemak")
	public String doHantarMaklumbalasToPenyemak() throws Exception {
		String statusInfo = "";
		Users users = db.find(Users.class, userId);
		JrpPermohonan permohonan = db.find(JrpPermohonan.class, getParam("idPermohonan"));
		JrpKuiri kuiri = db.find(JrpKuiri.class, get("idKuiri"));
		
		db.begin();	
		
		kuiri.setFlagHantarMaklumbalas("Y");
		
//		permohonan.setTarikhTerima(new Date());
		permohonan.setStatus(db.find(Status.class, "1424860634484")); //BELUM DISEMAK
		permohonan.setIdKemaskini(db.find(Users.class, userId));
		permohonan.setAgensiHq(db.find(Users.class, userId));
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
	
	@Command("doHantarPermohonanKeHq")
	public String doHantarPermohonanKeHq() throws Exception {
		String statusInfo = "";
		Users users = db.find(Users.class, userId);
		JrpPermohonan permohonan = db.find(JrpPermohonan.class, getParam("idPermohonan"));
		
		db.begin();	
		permohonan.setTarikhHantarUlasan(new Date());
		permohonan.setStatus(db.find(Status.class, "1436841294664")); //ULASAN JRP HQ
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
					context.put("idFlagKeputusan", jrpUlasanTeknikal.getFlagNilaian());
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
					context.put("idFlagKeputusan", jrpUlasanTeknikal.getFlagNilaian());
				subtab = "1";
			}
		}else{
			jrpUlasanTeknikal = getObjJrpUlasanTeknikal(jpph,idPermohonan);
			if(jrpUlasanTeknikal != null)
				context.put("idFlagKeputusan", jrpUlasanTeknikal.getFlagNilaian());
			subtab = "1";
		}
		
		
		context.put("ut", jrpUlasanTeknikal);
		context.put("selectedTab", "3");
		context.put("selectedSubTab", subtab);

		return getPath() + "/entry_page.vm";
//		return getPath() + "/ulasanTeknikal/start.vm";
	}
	
	/** START MAKLUMAT JPPH**/
	
	@Command("addJpph")
	public String addJpph() {
		
		context.remove("ut");
		
		context.put("selectedTab", "3");
		return getPath() + "/ulasanTeknikal/jpph/jpph.vm";
	}
	
	@Command("editJpph")
	public String editJpph() {
		
		context.remove("ut");
		
		JrpUlasanTeknikal ut = (JrpUlasanTeknikal) db.get("SELECT x FROM JrpUlasanTeknikal x WHERE x.id ='" + getParam("idJpph") +"'");
		context.put("idFlagKeputusan", ut.getFlagNilaian());
		context.put("ut", ut);
		context.put("selectedTab", "3");
		context.put("selectedSubTab", "1");
		return getPath() + "/ulasanTeknikal/jpph/jpph.vm";
	}
	
	@Command("removeJpph")
	public String removeJpph() throws Exception {
		String statusInfo = "";
		
		JrpUlasanTeknikal ut = (JrpUlasanTeknikal) db.get("SELECT x FROM JrpUlasanTeknikal x WHERE x.id ='" + getParam("idJpph") +"'");

		db.begin();
		db.remove(ut);
		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			statusInfo = "error";
		}
		
		return getSenaraiJpph();
	}
		
	/** END MAKLUMAT JPPH**/
	
	/** START MAKLUMAT KPKK**/
	
	@Command("addKpkk")
	public String addKpkk() {
		
		context.remove("ut");
		
		context.put("selectedTab", "3");
		context.put("selectedSubTab", "2");
		return getPath() + "/ulasanTeknikal/kpkk/kpkk.vm";
	}
	
	@Command("editKpkk")
	public String editKpkk() {
		
		context.remove("ut");
		
		JrpUlasanTeknikal ut = (JrpUlasanTeknikal) db.get("SELECT x FROM JrpUlasanTeknikal x WHERE x.id ='" + getParam("idKpkk") +"'");
		
		context.put("ut", ut);
		context.put("selectedTab", "3");
		context.put("selectedSubTab", "2");
		return getPath() + "/ulasanTeknikal/kpkk/kpkk.vm";
	}
	
	@Command("removeKpkk")
	public String removeKpkk() throws Exception {
		String statusInfo = "";
		
		JrpUlasanTeknikal ut = (JrpUlasanTeknikal) db.get("SELECT x FROM JrpUlasanTeknikal x WHERE x.id ='" + getParam("idKpkk") +"'");

		db.begin();
		db.remove(ut);
		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			statusInfo = "error";
		}
		
		return getSenaraiKpkk();
	}
		
	/** END MAKLUMAT KPKK**/
	
	/** START MAKLUMAT JBPM**/
	
	@Command("addJbpm")
	public String addJbpm() {
		
		context.remove("ut");
		
		context.put("selectedTab", "3");
		context.put("selectedSubTab", "3");
		return getPath() + "/ulasanTeknikal/jbpm/jbpm.vm";
	}
	
	@Command("editJbpm")
	public String editJbpm() {
		
		context.remove("ut");
		
		JrpUlasanTeknikal ut = (JrpUlasanTeknikal) db.get("SELECT x FROM JrpUlasanTeknikal x WHERE x.id ='" + getParam("idJbpm") +"'");
		
		context.put("ut", ut);
		context.put("selectedTab", "3");
		context.put("selectedSubTab", "3");
		return getPath() + "/ulasanTeknikal/jbpm/jbpm.vm";
	}
	
	@Command("removeJbpm")
	public String removeJbpm() throws Exception {
		String statusInfo = "";
		
		JrpUlasanTeknikal ut = (JrpUlasanTeknikal) db.get("SELECT x FROM JrpUlasanTeknikal x WHERE x.id ='" + getParam("idJbpm") +"'");

		db.begin();
		db.remove(ut);
		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			statusInfo = "error";
		}
		
		return getSenaraiJbpm();
	}
		
	/** END MAKLUMAT JBPM**/
	
	/** START MAKLUMAT JKPTG**/
	
	@Command("addJkptg")
	public String addJkptg() {
		
		context.remove("ut");
		
		context.put("selectedTab", "3");
		context.put("selectedSubTab", "6");
		return getPath() + "/ulasanTeknikal/jkptg/jkptg.vm";
	}
	
	@Command("editJkptg")
	public String editJkptg() {
		
		context.remove("ut");
		
		JrpUlasanTeknikal ut = (JrpUlasanTeknikal) db.get("SELECT x FROM JrpUlasanTeknikal x WHERE x.id ='" + getParam("idJkptg") +"'");
		
		context.put("ut", ut);
		context.put("selectedTab", "3");
		context.put("selectedSubTab", "6");
		return getPath() + "/ulasanTeknikal/jkptg/jkptg.vm";
	}
	
	@Command("removeJkptg")
	public String removeJkptg() throws Exception {
		String statusInfo = "";
		
		JrpUlasanTeknikal ut = (JrpUlasanTeknikal) db.get("SELECT x FROM JrpUlasanTeknikal x WHERE x.id ='" + getParam("idJkptg") +"'");

		db.begin();
		db.remove(ut);
		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			statusInfo = "error";
		}
		
		return getSenaraiJkptg();
	}
		
	/** END MAKLUMAT JKPTG**/
	
	/** START MAKLUMAT UPE**/
	
	@Command("addUpe")
	public String addUpe() {
		
		context.remove("ut");
		
		context.put("selectedTab", "3");
		context.put("selectedSubTab", "4");
		return getPath() + "/ulasanTeknikal/upe/upe.vm";
	}
	
	@Command("editUpe")
	public String editUpe() {
		
		context.remove("ut");
		
		JrpUlasanTeknikal ut = (JrpUlasanTeknikal) db.get("SELECT x FROM JrpUlasanTeknikal x WHERE x.id ='" + getParam("idUpe") +"'");
		
		context.put("ut", ut);
		context.put("selectedTab", "3");
		context.put("selectedSubTab", "4");
		return getPath() + "/ulasanTeknikal/upe/upe.vm";
	}
	
	@Command("removeUpe")
	public String removeUpe() throws Exception {
		String statusInfo = "";
		
		JrpUlasanTeknikal ut = (JrpUlasanTeknikal) db.get("SELECT x FROM JrpUlasanTeknikal x WHERE x.id ='" + getParam("idUpe") +"'");

		db.begin();
		db.remove(ut);
		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			statusInfo = "error";
		}
		
		return getSenaraiUpe();
	}
		
	/** END MAKLUMAT UPE**/
	
	/** START MAKLUMAT MOF**/
	
	@Command("addMof")
	public String addMof() {
		
		context.remove("ut");
		
		context.put("selectedTab", "3");
		context.put("selectedSubTab", "5");
		return getPath() + "/ulasanTeknikal/mof/mof.vm";
	}
	
	@Command("editMof")
	public String editMof() {
		
		context.remove("ut");
		
		JrpUlasanTeknikal ut = (JrpUlasanTeknikal) db.get("SELECT x FROM JrpUlasanTeknikal x WHERE x.id ='" + getParam("idMof") +"'");
		
		context.put("ut", ut);
		context.put("selectedTab", "3");
		context.put("selectedSubTab", "5");
		return getPath() + "/ulasanTeknikal/mof/mof.vm";
	}
	
	@Command("removeMof")
	public String removeMof() throws Exception {
		String statusInfo = "";
		
		JrpUlasanTeknikal ut = (JrpUlasanTeknikal) db.get("SELECT x FROM JrpUlasanTeknikal x WHERE x.id ='" + getParam("idMof") +"'");

		db.begin();
		db.remove(ut);
		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			statusInfo = "error";
		}
		
		return getSenaraiMof();
	}
		
	/** END MAKLUMAT MOF**/
	
	/** START KUIRI **/
	
	@Command("getKuiri")
	public String getKuiri() throws Exception {
		context.remove("kp");
		
		String idPermohonan = getParam("idPermohonan");
		Date tarikhUlasan = new Date();
		Date tarikhPengesahan = null;
		List<JrpKuiri> kuiriList = null;
		if("(JRP) Pemohon".equals(userRole)){

			kuiriList = db.list("Select x from JrpKuiri x where x.permohonan.id='"+ idPermohonan + "' and x.flagHantar ='Y' order by x.tarikhUlasan desc");
		}else{

			kuiriList = db.list("Select x from JrpKuiri x where x.permohonan.id='"+ idPermohonan + "' order by x.tarikhUlasan desc");
		}

		context.put("kuiriList", kuiriList);
		context.put("selectedTab", "9");
		context.put("tarikhUlasan", tarikhUlasan);
//		context.put("selectedSubTab", "3");
		return getPath() + "/entry_page.vm";
//		return getPath() + "/kelulusanPermohonan/start.vm";
	}
	
	@Command("addKuiri")
	public String addKuiri() {
		
		context.remove("kuiri");
		
		context.put("selectedTab", "7");
		context.put("selectedSubTab", "3");
		return getPath() + "/kelulusanPermohonan/kuiri/popupKuiri.vm";
	}
	
	@Command("saveKuiri")
	public String saveKuiri() throws Exception {
		
		String statusInfo = "";
		String idPermohonan = getParam("idPermohonan");
		
		
		Date tarikhUlasan = getDate("tarikhUlasan");
		String ulasan = get("ulasan");
		
		JrpKuiri kuiri = null;
		
		JrpKuiri jrpKuiri = db.find(JrpKuiri.class, get("idKuiri"));
		
		if(jrpKuiri != null){
			kuiri = jrpKuiri;
		}else{
			kuiri = new JrpKuiri();
		}
		
		db.begin();
		kuiri.setPermohonan(db.find(JrpPermohonan.class, idPermohonan));
		kuiri.setTarikhUlasan(getDate("tarikhUlasan"));
		kuiri.setUlasan(get("ulasan"));
		kuiri.setFlagBukaUlasan("T");
		kuiri.setFlagHantar("T"); // BELUM HANTAR
		db.persist(kuiri);

		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			statusInfo = "error";
		}
		
		jrpKuiri = db.find(JrpKuiri.class, kuiri.getId());
		
		
		context.put("kuiri", jrpKuiri);
		context.put("selectedTab", "7");
		context.put("selectedSubTab", "3");
		context.put("statusInfo", statusInfo);
		
		return getPath() + "/kelulusanPermohonan/kuiri/popupKuiri.vm";
	}
	
	@Command("saveMaklumbalas")
	public String saveMaklumbalas() throws Exception {
		
		String statusInfo = "";
		String idPermohonan = getParam("idPermohonan");
		
		
		Date tarikhMaklumbalas = getDate("tarikhMaklumbalas");
		String maklumbalas = get("maklumbalas");
		
		JrpKuiri mb = db.find(JrpKuiri.class, get("idKuiri"));
		
		db.begin();
		mb.setTarikhMaklumbalas(tarikhMaklumbalas);
		mb.setMaklumbalas(maklumbalas);
		mb.setFlagBukaMaklumbalas("T");
		mb.setFlagHantarMaklumbalas("T"); // BELUM HANTAR
		db.persist(mb);

		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			statusInfo = "error";
		}
		
		mb = db.find(JrpKuiri.class, get("idKuiri"));
		
		
		context.put("kuiri", mb);
		context.put("selectedTab", "7");
		context.put("selectedSubTab", "3");
		context.put("statusInfo", statusInfo);
		
		return getPath() + "/kelulusanPermohonan/kuiri/popupKuiri.vm";
	}
	
	@Command("editKuiri")
	public String editKuiri() {
		context.put("kuiri", "");
		String statusInfo = "";
		Date tarikhMb = new Date();
		JrpKuiri jrpKuiri = db.find(JrpKuiri.class, get("idKuiri"));
		
		db.begin();
		
		if(("(JRP) Pemohon").equalsIgnoreCase(userRole)){
			jrpKuiri.setFlagBukaUlasan("Y");
		}else{
			jrpKuiri.setFlagBukaMaklumbalas("Y");
		}

		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			statusInfo = "error";
		}
		
		if(jrpKuiri != null){
			context.put("kuiri", jrpKuiri);
		}
		
		context.put("selectedTab", "7");
		context.put("selectedSubTab", "3");
		context.put("tarikhMb", tarikhMb);
		return getPath() + "/kelulusanPermohonan/kuiri/popupKuiri.vm";
	}
	
	@Command("removeKuiri")
	public String removeKuiri() throws Exception {
		String statusInfo = "";
		
		JrpKuiri jrpKuiri = db.find(JrpKuiri.class, getParam("idKuiri"));

		db.begin();
		db.remove(jrpKuiri);
		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			statusInfo = "error";
		}
		
		return getKuiri();
	}
	
	@Command("hantarKuiri")
	public String hantarKuiri() throws Exception {
		
		String statusInfo = "";
		String idKuiri = getParam("idKuiri");
	    
		JrpKuiri jrpKuiri = db.find(JrpKuiri.class, idKuiri);
		
		db.begin();
		jrpKuiri.setFlagHantar("Y");
		jrpKuiri.setFlagInternal("T");
		db.persist(jrpKuiri);
		
		JrpPermohonan permohonan = db.find(JrpPermohonan.class, jrpKuiri.getPermohonan().getId());

		
		if(permohonan.getAgensiHq().getEmel() != null){
			permohonan.setStatus(db.find(Status.class, "1438356583122")); //PINDAAN MAKLUMAT PERMOHONAN
			JrpMailer.get().hantarKuiri(permohonan.getAgensiHq().getEmel(), jrpKuiri.getUlasan(), permohonan.getUrusetia().getUserName(), permohonan.getUrusetia().getNoTelefon());
		}
		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			statusInfo = "error";
		}
		context.put("selectedTab", "7");
		context.put("selectedSubTab", "3");
		context.put("statusInfo", statusInfo);
		
		return getPath() + "/kelulusanPermohonan/kuiri/popupKuiri.vm";
	}
	
	@Command("hantarKuiriInternal")
	public String hantarKuiriInternal() throws Exception {
		
		String statusInfo = "";
		String idKuiri = getParam("idKuiri");
	    
		JrpKuiri jrpKuiri = db.find(JrpKuiri.class, idKuiri);
		
		db.begin();
		jrpKuiri.setFlagHantar("Y");
		jrpKuiri.setFlagInternal("Y");
		db.persist(jrpKuiri);
		
		JrpPermohonan permohonan = db.find(JrpPermohonan.class, jrpKuiri.getPermohonan().getId());

		
		if(permohonan.getAgensiHq().getEmel() != null){
			permohonan.setStatus(db.find(Status.class, "1438356583123")); //KUIRI DARI PENYEMAK
			JrpMailer.get().hantarKuiri(permohonan.getAgensiHq().getEmel(), jrpKuiri.getUlasan(), permohonan.getUrusetia().getUserName(), permohonan.getUrusetia().getNoTelefon());
		}
		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			statusInfo = "error";
		}
		context.put("selectedTab", "7");
		context.put("selectedSubTab", "3");
		context.put("statusInfo", statusInfo);
		
		return getPath() + "/kelulusanPermohonan/kuiri/popupKuiri.vm";
	}
	
	/** END KUIRI **/
	
	/** START KELULUSAN PERMOHONAN
	 * 
	 * @return
	 * @throws Exception
	 */
	
	@Command("addKelulusanPermohonan")
	public String addKelulusanPermohonan() {
		
		context.remove("kp");
		
		context.put("selectedTab", "7");
		context.put("selectedSubTab", "1");
		return getPath() + "/kelulusanPermohonan/kertasPertimbangan/form.vm";
	}
	
	@Command("getKelulusanPermohonan")
	public String getKelulusanPermohonan() throws Exception {
		context.remove("kp");
		
		String idPermohonan = getParam("idPermohonan");
		Date tarikhSemakan = null;
		Date tarikhPengesahan = null;
//		JrpKertasPertimbangan jrpKertasPertimbangan = getObjJrpKertasPertimbangan(idPermohonan);
		JrpKertasPertimbangan jrpKertasPertimbangan = db.find(JrpKertasPertimbangan.class, getParam("idKertasPertimbangan"));
		
			if(jrpKertasPertimbangan != null){
				tarikhSemakan = jrpKertasPertimbangan.getTarikhSemakan();
				tarikhPengesahan = jrpKertasPertimbangan.getTarikhPengesahan();
				if(tarikhSemakan == null){
					tarikhSemakan = new Date();
				}else{
					tarikhSemakan = jrpKertasPertimbangan.getTarikhSemakan();
				}
				
				if(tarikhPengesahan == null){
					tarikhPengesahan = new Date();
				}else{
					tarikhPengesahan = jrpKertasPertimbangan.getTarikhPengesahan();
				}
			}
		
		context.put("tarikhSemakan", tarikhSemakan);
		context.put("tarikhPengesahan", tarikhPengesahan);
		context.put("kp", jrpKertasPertimbangan);
		context.put("selectedTab", "7");
		context.put("selectedSubTab", "1");
		return getPath() + "/kelulusanPermohonan/kertasPertimbangan/form.vm";
//		return getPath() + "/entry_page.vm";
	}
	
	@Command("getSenaraiKelulusanPermohonan")
	public String getSenaraiKelulusanPermohonan() throws Exception {
		context.remove("kp");
		
		String idPermohonan = get("idPermohonan");
		List<JrpKertasPertimbangan> kpList = null;
		
		if("(JRP) Penyedia".equals(userRole)){
			kpList = db.list("Select x from JrpKertasPertimbangan x where x.jrpPermohonan.id='"+ idPermohonan + "' order by x.disediakanOleh desc");
		}else{
			kpList = db.list("Select x from JrpKertasPertimbangan x where x.jrpPermohonan.id='"+ idPermohonan + "' and (x.flagPindaan = 'Y' or x.flagAktif = 'Y') order by x.disediakanOleh desc");
		}

		context.put("kpList", kpList);
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
		Date tarikhPengesahan = null;
		
		context.remove("km");
		JrpMesyuarat jrpMesyuarat = getObjJrpMesyuarat(idPermohonan);
		
		if(tarikhPengesahan == null){
			tarikhPengesahan = new Date();
		}else{
			tarikhPengesahan = jrpMesyuarat.getTarikhSah();
		}
		
		context.put("tarikhPengesahan", tarikhPengesahan);
		context.put("km", jrpMesyuarat);
		context.put("selectedTab", "7");
		context.put("selectedSubTab", "2");
		return getPath() + "/entry_page.vm";
//		return getPath() + "/kelulusanPermohonan/mesyuarat/start.vm";
	}
	
	/** START SENARAI ULASAN TEKNIKAL **/
	@Command("getSenaraiJpph")
	public String getSenaraiJpph() throws Exception {
		String idPermohonan = getParam("idPermohonan");
		String idJpph = "1306";
		int countUt = 0;
		JrpPermohonan permohonan = db.find(JrpPermohonan.class, get("idPermohonan"));
		context.put("r", permohonan);
		
		context.remove("ut");
		List<JrpUlasanTeknikal> jrpUlasanTeknikal = getJrpUlasanTeknikal(idJpph,idPermohonan);
		for(JrpUlasanTeknikal ut : jrpUlasanTeknikal){
			countUt++;
		}
		context.put("countJpph", countUt);
		context.put("listJpph", jrpUlasanTeknikal);
		context.put("selectedTab", "3");
		context.put("selectedSubTab", "1");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getSenaraiKpkk")
	public String getSenaraiKpkk() throws Exception {
		String idPermohonan = getParam("idPermohonan");
		String idKpkk = "1262";
		
		JrpPermohonan permohonan = db.find(JrpPermohonan.class, get("idPermohonan"));
		context.put("r", permohonan);
		
		context.remove("ut");
		List<JrpUlasanTeknikal> jrpUlasanTeknikal = getJrpUlasanTeknikal(idKpkk,idPermohonan);
		
		context.put("listKpkk", jrpUlasanTeknikal);
		context.put("selectedTab", "3");
		context.put("selectedSubTab", "2");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getSenaraiJbpm")
	public String getSenaraiJbpm() throws Exception {
		String idPermohonan = getParam("idPermohonan");
		String idJbpm = "2005";
		
		JrpPermohonan permohonan = db.find(JrpPermohonan.class, get("idPermohonan"));
		context.put("r", permohonan);
		
		context.remove("ut");
		List<JrpUlasanTeknikal> jrpUlasanTeknikal = getJrpUlasanTeknikal(idJbpm,idPermohonan);
		
		context.put("listJbpm", jrpUlasanTeknikal);
		context.put("selectedTab", "3");
		context.put("selectedSubTab", "3");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getSenaraiJkptg")
	public String getSenaraiJkptg() throws Exception {
		String idPermohonan = getParam("idPermohonan");
		String idJkptg = "1804";
		
		JrpPermohonan permohonan = db.find(JrpPermohonan.class, get("idPermohonan"));
		context.put("r", permohonan);
		
		context.remove("ut");
		List<JrpUlasanTeknikal> jrpUlasanTeknikal = getJrpUlasanTeknikal(idJkptg,idPermohonan);
		
		context.put("listJkptg", jrpUlasanTeknikal);
		context.put("selectedTab", "3");
		context.put("selectedSubTab", "6");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getSenaraiUpe")
	public String getSenaraiUpe() throws Exception {
		String idPermohonan = getParam("idPermohonan");
		String idUpe = "1216";
		
		JrpPermohonan permohonan = db.find(JrpPermohonan.class, get("idPermohonan"));
		context.put("r", permohonan);
		
		context.remove("ut");
		List<JrpUlasanTeknikal> jrpUlasanTeknikal = getJrpUlasanTeknikal(idUpe,idPermohonan);
		
		context.put("listUpe", jrpUlasanTeknikal);
		context.put("selectedTab", "3");
		context.put("selectedSubTab", "4");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getSenaraiMof")
	public String getSenaraiMof() throws Exception {
		String idPermohonan = getParam("idPermohonan");
		String idMof = "1301";
		
		JrpPermohonan permohonan = db.find(JrpPermohonan.class, get("idPermohonan"));
		context.put("r", permohonan);
		
		context.remove("ut");
		List<JrpUlasanTeknikal> jrpUlasanTeknikal = getJrpUlasanTeknikal(idMof,idPermohonan);
		
		context.put("listMof", jrpUlasanTeknikal);
		context.put("selectedTab", "3");
		context.put("selectedSubTab", "5");
		return getPath() + "/entry_page.vm";
	}
	
	/** END SENARAI ULASAN TEKNIKAL **/
	
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
	
	/** START SIMPAN ULASAN **/
	
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
		
		JrpUlasanTeknikal jrpUlasanTeknikal = db.find(JrpUlasanTeknikal.class, get("idUlasan"));
		JrpPermohonan jrpPermohonan = db.find(JrpPermohonan.class, idPermohonan);
		Agensi agensi = db.find(Agensi.class, idKpkk);
		
		if(jrpUlasanTeknikal != null){
			ul = jrpUlasanTeknikal;
		}else{
			ul = new JrpUlasanTeknikal();
			List<JrpUlasanTeknikal> jrpUlasanTeknikalList = getJrpUlasanTeknikal(idKpkk,idPermohonan);
			for(JrpUlasanTeknikal ut : jrpUlasanTeknikalList){
				ut.setFlagAktif("T");
			}
			ul.setFlagAktif("Y");
			Boolean status = checkStatusUlasan(idPermohonan);
			if(status){
				JrpPermohonan p = db.find(JrpPermohonan.class, idPermohonan);
				p.setStatus(db.find(Status.class, "1424860634475"));
			}
			
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
		ul.setTarikhHantarUlasan(getDate("tarikhHantarUlasan"));
		ul.setTarikhUlasan(getDate("tarikhUlasan"));
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
//		Date tarikhUlasan = getDate("tarikhUlasan");
		String flagKeputusan = getParam("flagKeputusan");
		
		JrpUlasanTeknikal ul = null;
		
		JrpUlasanTeknikal jrpUlasanTeknikal = db.find(JrpUlasanTeknikal.class, get("idUlasan"));
		JrpPermohonan jrpPermohonan = db.find(JrpPermohonan.class, idPermohonan);
		Agensi agensi = db.find(Agensi.class, idJbpm);
		
		if(jrpUlasanTeknikal != null){
			ul = jrpUlasanTeknikal;
		}else{
			ul = new JrpUlasanTeknikal();
			List<JrpUlasanTeknikal> jrpUlasanTeknikalList = getJrpUlasanTeknikal(idJbpm,idPermohonan);
			for(JrpUlasanTeknikal ut : jrpUlasanTeknikalList){
				ut.setFlagAktif("T");
			}
			ul.setFlagAktif("Y");
			Boolean status = checkStatusUlasan(idPermohonan);
			if(status){
				JrpPermohonan p = db.find(JrpPermohonan.class, idPermohonan);
				p.setStatus(db.find(Status.class, "1424860634475"));
			}
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
		ul.setTarikhHantarUlasan(getDate("tarikhHantarUlasan"));
		ul.setTarikhUlasan(getDate("tarikhUlasan"));

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
		
		JrpUlasanTeknikal jrpUlasanTeknikal = db.find(JrpUlasanTeknikal.class, get("idUlasan"));
		JrpPermohonan jrpPermohonan = db.find(JrpPermohonan.class, idPermohonan);
		Agensi agensi = db.find(Agensi.class, idUpe);
		
		if("1436841294664".equals(jrpPermohonan.getStatus())){
			JrpPermohonan p = db.find(JrpPermohonan.class, idPermohonan);
			p.setAgensiHq(db.find(Users.class, userId));
		}
		
		if(jrpUlasanTeknikal != null){
			ul = jrpUlasanTeknikal;
		}else{
			ul = new JrpUlasanTeknikal();
			List<JrpUlasanTeknikal> jrpUlasanTeknikalList = getJrpUlasanTeknikal(idUpe,idPermohonan);
			for(JrpUlasanTeknikal ut : jrpUlasanTeknikalList){
				ut.setFlagAktif("T");
			}
			ul.setFlagAktif("Y");
			Boolean status = checkStatusUlasan(idPermohonan);
			if(status){
				JrpPermohonan p = db.find(JrpPermohonan.class, idPermohonan);
				p.setStatus(db.find(Status.class, "1424860634475"));
				
			}
		}
		
		db.begin();
		ul.setJrpPermohonan(jrpPermohonan);
		ul.setAgensi(agensi);
//		ul.setTarikhUlasan(tarikhUlasan);
		ul.setUlasan(ulasan);
		ul.setFlagKeputusan(flagKeputusan);
		ul.setNamaPegawai(namaPegawai);
		ul.setJawatanPegawai(jawatanPegawai);
		ul.setTarikhHantarUlasan(getDate("tarikhHantarUlasan"));
		ul.setTarikhUlasan(getDate("tarikhUlasan"));

		db.persist(ul);

		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			statusInfo = "error";
		}
		/*context.put("selectedTab", "3");
		context.put("selectedSubTab", "4");*/
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

		JrpUlasanTeknikal jrpUlasanTeknikal = db.find(JrpUlasanTeknikal.class, get("idUlasan"));
		JrpPermohonan jrpPermohonan = db.find(JrpPermohonan.class, idPermohonan);
		Agensi agensi = db.find(Agensi.class, idMof);
		
		if("1436841294664".equals(jrpPermohonan.getStatus())){
			JrpPermohonan p = db.find(JrpPermohonan.class, idPermohonan);
			p.setAgensiHq(db.find(Users.class, userId));
		}
		
		if(jrpUlasanTeknikal != null){
			ul = jrpUlasanTeknikal;
		}else{
			ul = new JrpUlasanTeknikal();
			List<JrpUlasanTeknikal> jrpUlasanTeknikalList = getJrpUlasanTeknikal(idMof,idPermohonan);
			for(JrpUlasanTeknikal ut : jrpUlasanTeknikalList){
				ut.setFlagAktif("T");
			}
			ul.setFlagAktif("Y");
			Boolean status = checkStatusUlasan(idPermohonan);
			if(status){
				JrpPermohonan p = db.find(JrpPermohonan.class, idPermohonan);
				p.setStatus(db.find(Status.class, "1424860634475"));
			}
		}
		
		db.begin();
		ul.setJrpPermohonan(jrpPermohonan);
		ul.setAgensi(agensi);
		ul.setUlasan(ulasan);
		ul.setFlagKeputusan(flagKeputusan);
		ul.setNamaPegawai(namaPegawai);
		ul.setJawatanPegawai(jawatanPegawai);
		ul.setTarikhHantarUlasan(getDate("tarikhHantarUlasan"));
		ul.setTarikhUlasan(getDate("tarikhUlasan"));

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
		
		JrpUlasanTeknikal jrpUlasanTeknikal = db.find(JrpUlasanTeknikal.class, get("idUlasan"));
		JrpPermohonan jrpPermohonan = db.find(JrpPermohonan.class, idPermohonan);
		Agensi agensi = db.find(Agensi.class, idJkptg);
		
		if(jrpUlasanTeknikal != null){
			ul = jrpUlasanTeknikal;
		}else{
			ul = new JrpUlasanTeknikal();
			List<JrpUlasanTeknikal> jrpUlasanTeknikalList = getJrpUlasanTeknikal(idJkptg,idPermohonan);
			for(JrpUlasanTeknikal ut : jrpUlasanTeknikalList){
				ut.setFlagAktif("T");
			}
			ul.setFlagAktif("Y");
			Boolean status = checkStatusUlasan(idPermohonan);
			if(status){
				JrpPermohonan p = db.find(JrpPermohonan.class, idPermohonan);
				p.setStatus(db.find(Status.class, "1424860634475"));
			}
		}
		
		db.begin();
		ul.setJrpPermohonan(jrpPermohonan);
		ul.setAgensi(agensi);
//		ul.setTarikhUlasan(tarikhUlasan);
		ul.setUlasan(ulasan);
		ul.setFlagKeputusan(flagKeputusan);
		ul.setNamaPegawai(namaPegawai);
		ul.setJawatanPegawai(jawatanPegawai);
		ul.setTarikhHantarUlasan(getDate("tarikhHantarUlasan"));
		ul.setTarikhUlasan(getDate("tarikhUlasan"));

		db.persist(ul);

		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			statusInfo = "error";
		}
		context.put("selectedTab", "3");
		context.put("selectedSubTab", "6");
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
	
	@Command("selectNilaian")
	public String selectNilaian() throws Exception {

		String flagK = "";
		if (get("flagK").trim().length() > 0) 
		
		flagK = get("flagK");
		context.put("idSokong", flagK);
		
		return getPath() + path + "/ulasanTeknikal/jpph/nilaianJpph.vm";
	}
	
	@Command("saveUlasanJpph")
	public String saveUlasanJpph() throws Exception {
		
		String statusInfo = "";
		String idPermohonan = getParam("idPermohonan");
		String idJPPH = "1306";
		
		String cawangan = getParam("cawangan");
		String flagK = getParam("flagK");
		String flagKeputusan = getParam("flagKeputusan");
		
		Double nilaianMp = 0.00;
		String luasMp = "0.00";
		Double nilaianKp = 0.00;
		String luasKp = "0.00";
		
		String flagGst = "";
		Double nilaianJpphMp = 0.00;
		String luasJpphMp = "0.00";
		Double nilaianJpphKp = 0.00;
		String luasJpphKp = "0.00";
		Double nilaianGstMp = 0.00;
		Double nilaianGstKp = 0.00;
		Integer tempohSewa = 0;
		String syaratSewa1 = "";	
		String syaratSewa2 = "";	
		String syaratSewa3 = "";
		String syaratSewa4 = "";
		String syaratSewa5 = "";
		String syaratSewa6 = "";	
		String syaratSewa7 = "";	
		String syaratSewa8 = "";
		String syaratSewa9 = "";
		String syaratSewa10 = "";
		String syaratSewa11 = "";
		String namaPegawai = "";
		String jawatanPegawai = "";
		String ulasan = "";
		Double perakuanSewaBulanan = 0.00; 
		
		namaPegawai = getParam("namaPegawai");
		jawatanPegawai = getParam("jawatanPegawai");
		ulasan = getParam("ulasan");
		
		if(!"TS".equals(flagK)){
		nilaianMp = Double.valueOf(Util.RemoveComma(getParam("nilaianMp")));
		luasMp = getParam("luasMp");
		nilaianKp = Double.valueOf(Util.RemoveComma(getParam("nilaianKp")));
		luasKp = getParam("luasKp");
		
		perakuanSewaBulanan = Double.valueOf(Util.RemoveComma(getParam("perakuanSewaBulanan")));
		tempohSewa = getParamAsInteger("tempohSewa");
		syaratSewa1 = getParam("syaratSewa1");
		syaratSewa2 = getParam("syaratSewa2");
		syaratSewa3 = getParam("syaratSewa3");
		syaratSewa4 = getParam("syaratSewa4");
		syaratSewa5 = getParam("syaratSewa5");
		syaratSewa6 = getParam("syaratSewa6");
		syaratSewa7 = getParam("syaratSewa7");
		syaratSewa8 = getParam("syaratSewa8");
		syaratSewa9 = getParam("syaratSewa9");
		syaratSewa10 = getParam("syaratSewa10");
		syaratSewa11 = getParam("syaratSewa11");
		}
		
		if("TS".equals(flagKeputusan)){
			flagGst = getParam("flagGst");	
			nilaianJpphMp = Double.valueOf(Util.RemoveComma(getParam("nilaianJpphMp")));
			luasJpphMp = getParam("luasJpphMp");
			nilaianJpphKp = Double.valueOf(Util.RemoveComma(getParam("nilaianJpphKp")));
			luasJpphKp = getParam("luasJpphKp");
			
			
		}
		
		if("Y".equalsIgnoreCase(flagGst)){
			nilaianGstMp = Double.valueOf(Util.RemoveComma(getParam("nilaianGstMp")));
			nilaianGstKp = Double.valueOf(Util.RemoveComma(getParam("nilaianGstKp")));
		}
		
		
		Date tarikhUlasan = getDate("tarikhUlasan");
		Date tarikhHantarUlasan = getDate("tarikhHantarUlasan");
		
		JrpUlasanTeknikal ul = null;
		
		JrpUlasanTeknikal jrpUlasanTeknikal = db.find(JrpUlasanTeknikal.class, get("idUlasan"));
		JrpPermohonan jrpPermohonan = db.find(JrpPermohonan.class, idPermohonan);
		Agensi agensi = db.find(Agensi.class, idJPPH);
		
		if("1436841294664".equals(jrpPermohonan.getStatus())){
			JrpPermohonan p = db.find(JrpPermohonan.class, idPermohonan);
			p.setAgensiHq(db.find(Users.class, userId));
		}
		
		if(jrpUlasanTeknikal != null){
			ul = jrpUlasanTeknikal;
		}else{
			ul = new JrpUlasanTeknikal();
			List<JrpUlasanTeknikal> jrpUlasanTeknikalList = getJrpUlasanTeknikal(idJPPH,idPermohonan);
			for(JrpUlasanTeknikal ut : jrpUlasanTeknikalList){
				ut.setFlagAktif("T");
			}
			ul.setFlagAktif("Y");
			Boolean status = checkStatusUlasan(idPermohonan);
			if(status){
				JrpPermohonan p = db.find(JrpPermohonan.class, idPermohonan);
				p.setStatus(db.find(Status.class, "1424860634475"));
			}
		}
		
		db.begin();
		
		if(!"TS".equals(flagK)){
			ul.setFlagNilaian(flagKeputusan);
		}
		ul.setJrpPermohonan(jrpPermohonan);
		ul.setAgensi(agensi);
		ul.setCawangan(cawangan);
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
		ul.setSyaratSewa4(syaratSewa4);
		ul.setSyaratSewa5(syaratSewa5);
		ul.setSyaratSewa6(syaratSewa6);
		ul.setSyaratSewa7(syaratSewa7);
		ul.setSyaratSewa8(syaratSewa8);
		ul.setSyaratSewa9(syaratSewa9);
		ul.setSyaratSewa10(syaratSewa10);
		ul.setSyaratSewa11(syaratSewa11);
		ul.setUlasan(ulasan);
		ul.setFlagKeputusan(flagK);
		ul.setFlagGst(flagGst);
		ul.setNilaianJpphMp(nilaianJpphMp);
		ul.setLuasJpphMp(luasJpphMp);
		ul.setNilaianJpphKp(nilaianJpphKp);
		ul.setLuasJpphKp(luasJpphKp);
		ul.setNilaianGstMp(nilaianGstMp);
		ul.setNilaianGstKp(nilaianGstKp);
		ul.setTarikhUlasan(tarikhUlasan);
		ul.setTarikhHantarUlasan(tarikhHantarUlasan);
		ul.setFlagJenisSewa(getParam("flagjenisSewa"));
		
		System.out.println("ceking flag ========= " +getParam("flagjenisSewa"));

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
	
	public Boolean checkStatusUlasan(String idPermohonan){
		Boolean status = false;
		
		JrpUlasanTeknikal ut = (JrpUlasanTeknikal) db.get("Select x from JrpUlasanTeknikal x where x.jrpPermohonan.id ='" + idPermohonan + "'"); //" and x.flagPindaan ='Y'");
		if(ut == null){
			status = true;
		}
		
		return status;
	}
	/** END SIMPAN ULASAN **/
	
	@Command("saveMesyuarat")
	public String saveMesyuarat() throws Exception {
		
		String statusInfo = "";
		String idPermohonan = getParam("idPermohonan");
	    
		Date tarikhMesyuarat = getDate("tarikhMesyuarat");
		String ulasan = getParam("ulasan");
		String urusetiaPenyedia = getParam("urusetiaPenyedia");
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
		ul.setBilMesyuarat(bilMesy);
		ul.setFlagKeputusan(flagKeputusan);
		ul.setUlasan(ulasan);
		ul.setUrusetiaPenyedia(db.find(Users.class,urusetiaPenyedia));
		db.persist(ul);
		
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
	
	@Command("hantarPengesahanMesyuarat")
	public String hantarPengesahanMesyuarat() throws Exception {
		
		String statusInfo = "";
		String idPermohonan = getParam("idPermohonan");
	    
		JrpPermohonan p = db.find(JrpPermohonan.class, idPermohonan);
		Status s = db.find(Status.class, "1438356583116"); // pengesahan mesyuarat
		
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
		context.put("selectedSubTab", "2");
		context.put("statusInfo", statusInfo);
		
		return getPath() + "/kelulusanPermohonan/status.vm";
	}
	
	@Command("savePengesahanMesyuarat")
	public String savePengesahanMesyuarat() throws Exception {
		
		String statusInfo = "";
		String idPermohonan = getParam("idPermohonan");
		Date tarikhSah = getDate("tarikhSah");
		String urusetiaPengesah = getParam("urusetiaPengesah");
		String flagKeputusan = getParam("flagKeputusan");
		
		JrpMesyuarat ul = null;
		JrpMesyuarat jrpMesyuarat = getObjJrpMesyuarat(idPermohonan);
		JrpPermohonan jrpPermohonan = db.find(JrpPermohonan.class, idPermohonan);
		
		if(jrpMesyuarat != null){
			ul = jrpMesyuarat;
		}else{
			ul = new JrpMesyuarat();
		}
		
		db.begin();
		ul.setTarikhSah(tarikhSah);
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
		
		jrpMesyuarat.setUlasanUrusetiaPengesah(get("ulasanUrusetiaPengesah"));
		
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
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		Users users = db.find(Users.class, userId);
		
		String statusInfo = "";
		String idPermohonan = getParam("idPermohonan");
	    
	    String bilMesyuarat = getParam("bilMesyuarat");
	    String noDaftar = getParam("noDaftar");
//	    Date tarikhLengkapPermohonan = getDate("tarikhLengkapPermohonan");
	    String catatan = getParam("catatan");
	    String ulasanUrusetia = getParam("ulasanUrusetia");
	    String disediakanOleh = getParam("disediakanOleh");
	    String flagSyorBersyarat = getParam("flagSyorBersyarat");
	    
		JrpKertasPertimbangan kp = null;
//		JrpKertasPertimbangan jrpKertasPertimbangan = getObjJrpKertasPertimbangan(idPermohonan);
		
		
		JrpKertasPertimbangan jrpKertasPertimbangan = db.find(JrpKertasPertimbangan.class, get("idKertasPertimbangan"));
		JrpPermohonan jrpPermohonan = db.find(JrpPermohonan.class, idPermohonan);
		if(jrpKertasPertimbangan != null){
			kp = jrpKertasPertimbangan;
			kp.setIdKemaskini(db.find(Users.class, userId));
			kp.setTarikhKemaskini(new Date());
		}else{
			kp = new JrpKertasPertimbangan();
			kp.setTarikhLengkapPermohonan(jrpPermohonan.getTarikhTerima());
			kp.setIdMasuk(db.find(Users.class, disediakanOleh));
			kp.setTarikhMasuk(new Date());
			
			List<JrpKertasPertimbangan> kpList = db.list("Select x from JrpKertasPertimbangan x where x.jrpPermohonan.id='" + idPermohonan + "'");
			for(JrpKertasPertimbangan k : kpList){
				k.setFlagAktif("T");
			}
			kp.setFlagAktif("Y");
			
			jrpPermohonan.setUrusetia(db.find(Users.class, disediakanOleh));
			if(!"1436921583144".equals(jrpPermohonan.getStatus())){
				jrpPermohonan.setStatus(db.find(Status.class,"1424860634481")); //PENYEDIAAN KERTAS PERTIMBANGAN
			}
		}
		
		db.begin();
		kp.setJrpPermohonan(jrpPermohonan);
		kp.setBilMesyuarat(bilMesyuarat);
		kp.setNoDaftar(noDaftar);
		kp.setCatatan(catatan);
		kp.setDisediakanOleh(db.find(Users.class, disediakanOleh));
		kp.setFlagSyorBersyarat(flagSyorBersyarat);
		kp.setUlasanUrusetia(ulasanUrusetia);
		db.persist(kp);

		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			statusInfo = "error";
		}
		
		jrpKertasPertimbangan = db.find(JrpKertasPertimbangan.class, kp.getId());
		
		context.put("kp", jrpKertasPertimbangan);
		context.put("selectedTab", "7");
		context.put("selectedSubTab", "1");
		context.put("statusInfo", statusInfo);
		
		return getPath() + "/kelulusanPermohonan/kertasPertimbangan/form.vm";
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
		kp.setDisemakOleh(db.find(Users.class, disemakOleh));
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
		
		return getPath() + "/kelulusanPermohonan/kertasPertimbangan/form.vm";
	}
	
	@Command("saveKertasPertimbanganKetuaUrusetia")
	public String saveKertasPertimbanganKetuaUrusetia() throws Exception {
		
		String statusInfo = "";
		String idKertasPertimbangan = getParam("idKertasPertimbangan");
	    
	    String disahkanOleh = getParam("disahkanOleh");
	    String ulasanKetuaUrusetia = getParam("ulasanKetuaUrusetia");
//	    Date tarikhPengesahan = getDate("tarikhPengesahan");
	    
		JrpKertasPertimbangan kp = db.find(JrpKertasPertimbangan.class, idKertasPertimbangan);
//		JrpPermohonan p = db.find(JrpPermohonan.class, kp.getJrpPermohonan().getId());
//		Status s = db.find(Status.class, "1424860634490"); //mesyuarat
		
		db.begin();
		kp.setDisahkanOleh(db.find(Users.class, disahkanOleh));
		kp.setUlasanKetuaUrusetia(ulasanKetuaUrusetia);
		if(kp.getTarikhPengesahan() == null){
			kp.setTarikhPengesahan(new Date());
			}
		db.persist(kp);

		try {
			db.commit();
			statusInfo = "success";
			
//			db.begin();
//			p.setStatus(s);
//			db.persist(p);
//			db.commit();
			
		} catch (Exception e) {
			statusInfo = "error";
		}
		context.put("selectedTab", "7");
		context.put("selectedSubTab", "1");
		context.put("statusInfo", statusInfo);
		
		return getPath() + "/kelulusanPermohonan/kertasPertimbangan/form.vm";
//		return getPath() + "/kelulusanPermohonan/status.vm";
	}
	
	@Command("hantarSemakanKp")
	public String hantarSemakanKp() throws Exception {
		
		String statusInfo = "";
		String idPermohonan = getParam("idPermohonan");
	    
		JrpPermohonan p = db.find(JrpPermohonan.class, idPermohonan);
		Status s = db.find(Status.class, "1424860634484"); //semakan kertas pertimbangan
		
		
		db.begin();
		p.setStatus(s);
		db.persist(p);
//		db.persist(kp);

		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			statusInfo = "error";
		}
		context.put("selectedTab", "7");
		context.put("selectedSubTab", "1");
		context.put("statusInfo", statusInfo);
		
//		return getPath() + "/kelulusanPermohonan/status.vm";
		return getPath() + "/kelulusanPermohonan/kertasPertimbangan/form.vm";
	}
	
	@Command("hantarPengesahanKp")
	public String hantarPengesahanKp() throws Exception {
		
		String statusInfo = "";
		String idPermohonan = getParam("idPermohonan");
		String idKertasPertimbangan = getParam("idKertasPertimbangan");
	    
		JrpPermohonan p = db.find(JrpPermohonan.class, idPermohonan);
		Status s = db.find(Status.class, "1424860634487"); //pengesahan kertas pertimbangan
		
		JrpKertasPertimbangan kp = db.find(JrpKertasPertimbangan.class, idKertasPertimbangan);
		
		db.begin();
		p.setStatus(s);
		kp.setFlagPindaan("T"); //tidak dipinda
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
		
//		return getPath() + "/kelulusanPermohonan/status.vm";
		return getPath() + "/kelulusanPermohonan/kertasPertimbangan/form.vm";
	}
	
	@Command("hantarPindaanKp")
	public String hantarPindaanKp() throws Exception {
		
		String statusInfo = "";
		String idPermohonan = getParam("idPermohonan");
		String idKertasPertimbangan = getParam("idKertasPertimbangan");
	    
		JrpPermohonan p = db.find(JrpPermohonan.class, idPermohonan);
		Status s = null;
		
		if(("1424860634484").equalsIgnoreCase(p.getStatus().getId())){ // DARI PENYEMAK
			s = db.find(Status.class, "1436921583144"); //PINDAAN DARI PENYEMAK
		}else if(("1424860634487").equalsIgnoreCase(p.getStatus().getId())){ // DARI PELULUS
			s = db.find(Status.class, "471300128898077"); //PINDAAN DARI PELULUS
		}
		
		JrpKertasPertimbangan kp = db.find(JrpKertasPertimbangan.class, idKertasPertimbangan);
		
		db.begin();
		p.setStatus(s);
		kp.setFlagPindaan("Y"); //dipinda
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
		
//		return getPath() + "/kelulusanPermohonan/status.vm";
		return getPath() + "/kelulusanPermohonan/kertasPertimbangan/form.vm";
	}
	
	@Command("hantarMesyuarat")
	public String hantarMesyuarat() throws Exception {
		
		String statusInfo = "";
		String idPermohonan = getParam("idPermohonan");
	    
		JrpPermohonan p = db.find(JrpPermohonan.class, idPermohonan);
		Status s = db.find(Status.class, "1424860634490"); //mesyuarat
		
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
		
//		return getPath() + "/kelulusanPermohonan/status.vm";
		return getPath() + "/kelulusanPermohonan/kertasPertimbangan/form.vm";
	}
	
	public JrpUlasanTeknikal getObjJrpUlasanTeknikal(String idAgensi, String idPermohonan) throws Exception {
		
		@SuppressWarnings("unchecked")
		List<JrpUlasanTeknikal>listJrpUlasanTeknikal = db.list("SELECT x FROM JrpUlasanTeknikal x WHERE x.agensi.id ='"+idAgensi+"' AND x.jrpPermohonan.id = '"+idPermohonan+"' ");
		JrpUlasanTeknikal jrpUlasanTeknikal = db.find(JrpUlasanTeknikal.class, (!listJrpUlasanTeknikal.isEmpty()?listJrpUlasanTeknikal.get(0).getId():""));
		
		return jrpUlasanTeknikal;
		
	}
	
	public List<JrpUlasanTeknikal> getJrpUlasanTeknikal(String idAgensi, String idPermohonan) throws Exception {
		
		@SuppressWarnings("unchecked")
		List<JrpUlasanTeknikal>listJrpUlasanTeknikal = db.list("SELECT x FROM JrpUlasanTeknikal x WHERE x.agensi.id ='"+idAgensi+"' AND x.jrpPermohonan.id = '"+idPermohonan+"' ");
//		JrpUlasanTeknikal jrpUlasanTeknikal = db.find(JrpUlasanTeknikal.class, (!listJrpUlasanTeknikal.isEmpty()?listJrpUlasanTeknikal.get(0).getId():""));
		
		return listJrpUlasanTeknikal;
		
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
		List <JrpUlasanTeknikal> listSemakanUlasan = db.list("Select x from JrpUlasanTeknikal x where x.jrpPermohonan.id='" + idPermohonan + "' and x.flagAktif = 'Y'");
		context.put("listSemakanUlasan", listSemakanUlasan);
		
		// CHECK JUMLAH RAYUAN JPPH
		Long countJpph =  (Long) db.get("Select count(x.id) from JrpUlasanTeknikal x where x.jrpPermohonan.id='" + idPermohonan + "' and x.agensi.id ='1306'");

		if(countJpph != 0){
			context.put("countJpph", countJpph);
		}
		
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
	
	/** START SURAT JPPH **/
	@Command("uploadSuratJpph")
	public String uploadSuratJpph() throws Exception {
		String idUlasan = get("idUlasan");
		String uploadDir = "jrp/permohonanRuangPejabat/surat/";
		File dir = new File(ResourceBundle.getBundle("dbconnection").getString("folder") + uploadDir);
		if (!dir.exists())
			dir.mkdir();

		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		List items = upload.parseRequest(request);
		Iterator itr = items.iterator();
		List<FileItem> files = new ArrayList<FileItem>();
		while (itr.hasNext()) {
			FileItem item = (FileItem) itr.next();
			if ((!(item.isFormField())) && (item.getName() != null)
					&& (!("".equals(item.getName())))) {
				files.add(item);
			}
		}

		for (FileItem item : files) {
			String avatarName = "";
			String fileName = item.getName();
			//String imgName = uploadDir + fileName;
			JrpUlasanTeknikal ut = db.find(JrpUlasanTeknikal.class, get("idUlasan"));
			if(ut!=null)
			{
				if (ut.getSuratFileName() != null && ut.getSuratFileName().length() > 0) {
					Util.deleteFile(ut.getSuratFileName());
					Util.deleteFile(ut.getSuratThumbFile());
				}
			}
			
			String imgName = uploadDir + idUlasan + fileName.substring(fileName.lastIndexOf("."));

			imgName = imgName.replaceAll(" ", "_");
			item.write(new File(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName));

			String mimetype = item.getContentType();
			String type = mimetype.split("/")[0];
			if (type.equals("image")) {
				avatarName = imgName.substring(0, imgName.lastIndexOf("."))
						+ "_avatar" + imgName.substring(imgName.lastIndexOf("."));
				avatarName = avatarName.replaceAll(" ", "_");
				lebah.repository.Thumbnail.create(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName, ResourceBundle.getBundle("dbconnection").getString("folder") + imgName, 600, 560,
						100);
				lebah.repository.Thumbnail.create(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName, ResourceBundle.getBundle("dbconnection").getString("folder") + avatarName, 150, 90,
						100);
			}

			if (!imgName.equals("")) {
				simpanSurat(idUlasan, imgName, avatarName);
			}
		}

		JrpUlasanTeknikal ut = db.find(JrpUlasanTeknikal.class, get("idUlasan"));	
		context.put("ut", ut);
		context.put("selectedTab", "3");
		context.put("selectedSubTab", "1");
		return getPath() + "/ulasanTeknikal/jpph/uploadSurat.vm";
	}
	
	@Command("doHapusSurat")
	public String doHapusSurat() throws Exception {
		JrpUlasanTeknikal ut = db.find(JrpUlasanTeknikal.class, get("idUlasan"));
		if (ut.getSuratFileName() != null && ut.getSuratFileName().length() > 0) {
			Util.deleteFile(ut.getSuratFileName());
			Util.deleteFile(ut.getSuratThumbFile());
		}
		ut.setSuratFileName("");
		ut.setSuratThumbFile("");

		db.begin();
		try {
			db.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}

		return getSenaraiJpph();
	}
	/** END SURAT JPPH **/
	
	/** START SURAT KPKK **/
	@Command("uploadSuratKpkk")
	public String uploadSuratKpkk() throws Exception {
		String idUlasan = get("idUlasan");
		String uploadDir = "jrp/permohonanRuangPejabat/surat/";
		File dir = new File(ResourceBundle.getBundle("dbconnection").getString("folder") + uploadDir);
		if (!dir.exists())
			dir.mkdir();

		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		List items = upload.parseRequest(request);
		Iterator itr = items.iterator();
		List<FileItem> files = new ArrayList<FileItem>();
		while (itr.hasNext()) {
			FileItem item = (FileItem) itr.next();
			if ((!(item.isFormField())) && (item.getName() != null)
					&& (!("".equals(item.getName())))) {
				files.add(item);
			}
		}

		for (FileItem item : files) {
			String avatarName = "";
			String fileName = item.getName();
			//String imgName = uploadDir + fileName;
			JrpUlasanTeknikal ut = db.find(JrpUlasanTeknikal.class, get("idUlasan"));
			if (ut.getSuratFileName() != null && ut.getSuratFileName().length() > 0) {
				Util.deleteFile(ut.getSuratFileName());
				Util.deleteFile(ut.getSuratThumbFile());
			}
			String imgName = uploadDir + idUlasan + fileName.substring(fileName.lastIndexOf("."));

			imgName = imgName.replaceAll(" ", "_");
			item.write(new File(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName));

			String mimetype = item.getContentType();
			String type = mimetype.split("/")[0];
			if (type.equals("image")) {
				avatarName = imgName.substring(0, imgName.lastIndexOf("."))
						+ "_avatar" + imgName.substring(imgName.lastIndexOf("."));
				avatarName = avatarName.replaceAll(" ", "_");
				lebah.repository.Thumbnail.create(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName, ResourceBundle.getBundle("dbconnection").getString("folder") + imgName, 600, 560,
						100);
				lebah.repository.Thumbnail.create(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName, ResourceBundle.getBundle("dbconnection").getString("folder") + avatarName, 150, 90,
						100);
			}

			if (!imgName.equals("")) {
				simpanSurat(idUlasan, imgName, avatarName);
			}
		}

		JrpUlasanTeknikal ut = db.find(JrpUlasanTeknikal.class, get("idUlasan"));	
		context.put("ut", ut);
		context.put("selectedTab", "3");
		context.put("selectedSubTab", "2");
		return getPath() + "/ulasanTeknikal/kpkk/uploadSurat.vm";
	}
	
	@Command("doHapusSuratKpkk")
	public String doHapusSuratKpkk() throws Exception {
		JrpUlasanTeknikal ut = db.find(JrpUlasanTeknikal.class, get("idUlasan"));
		if (ut.getSuratFileName() != null && ut.getSuratFileName().length() > 0) {
			Util.deleteFile(ut.getSuratFileName());
			Util.deleteFile(ut.getSuratThumbFile());
		}
		ut.setSuratFileName("");
		ut.setSuratThumbFile("");

		db.begin();
		try {
			db.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}

		return getSenaraiKpkk();
	}
	/** END SURAT KPKK **/
	
	/** START SURAT JBPM **/
	
	@Command("uploadSuratJbpm")
	public String uploadSuratJbpm() throws Exception {
		String idUlasan = get("idUlasan");
		String uploadDir = "jrp/permohonanRuangPejabat/surat/";
		File dir = new File(ResourceBundle.getBundle("dbconnection").getString("folder") + uploadDir);
		if (!dir.exists())
			dir.mkdir();

		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		List items = upload.parseRequest(request);
		Iterator itr = items.iterator();
		List<FileItem> files = new ArrayList<FileItem>();
		while (itr.hasNext()) {
			FileItem item = (FileItem) itr.next();
			if ((!(item.isFormField())) && (item.getName() != null)
					&& (!("".equals(item.getName())))) {
				files.add(item);
			}
		}

		for (FileItem item : files) {
			String avatarName = "";
			String fileName = item.getName();
			//String imgName = uploadDir + fileName;
			JrpUlasanTeknikal ut = db.find(JrpUlasanTeknikal.class, get("idUlasan"));
			if (ut.getSuratFileName() != null && ut.getSuratFileName().length() > 0) {
				Util.deleteFile(ut.getSuratFileName());
				Util.deleteFile(ut.getSuratThumbFile());
			}
			String imgName = uploadDir + idUlasan + fileName.substring(fileName.lastIndexOf("."));

			imgName = imgName.replaceAll(" ", "_");
			item.write(new File(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName));

			String mimetype = item.getContentType();
			String type = mimetype.split("/")[0];
			if (type.equals("image")) {
				avatarName = imgName.substring(0, imgName.lastIndexOf("."))
						+ "_avatar" + imgName.substring(imgName.lastIndexOf("."));
				avatarName = avatarName.replaceAll(" ", "_");
				lebah.repository.Thumbnail.create(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName, ResourceBundle.getBundle("dbconnection").getString("folder") + imgName, 600, 560,
						100);
				lebah.repository.Thumbnail.create(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName, ResourceBundle.getBundle("dbconnection").getString("folder") + avatarName, 150, 90,
						100);
			}

			if (!imgName.equals("")) {
				simpanSurat(idUlasan, imgName, avatarName);
			}
		}

		JrpUlasanTeknikal ut = db.find(JrpUlasanTeknikal.class, get("idUlasan"));	
		context.put("ut", ut);
		context.put("selectedTab", "3");
		context.put("selectedSubTab", "3");
		return getPath() + "/ulasanTeknikal/jbpm/uploadSurat.vm";
	}
	
	@Command("doHapusSuratJbpm")
	public String doHapusSuratJbpm() throws Exception {
		JrpUlasanTeknikal ut = db.find(JrpUlasanTeknikal.class, get("idUlasan"));
		if (ut.getSuratFileName() != null && ut.getSuratFileName().length() > 0) {
			Util.deleteFile(ut.getSuratFileName());
			Util.deleteFile(ut.getSuratThumbFile());
		}
		ut.setSuratFileName("");
		ut.setSuratThumbFile("");

		db.begin();
		try {
			db.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}

		return getSenaraiJbpm();
	}
	
	/** END SURAT JBPM **/
	
	/** START SURAT JKPTG **/
	
	@Command("uploadSuratJkptg")
	public String uploadSuratJkptg() throws Exception {
		String idUlasan = get("idUlasan");
		String uploadDir = "jrp/permohonanRuangPejabat/surat/";
		File dir = new File(ResourceBundle.getBundle("dbconnection").getString("folder") + uploadDir);
		if (!dir.exists())
			dir.mkdir();

		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		List items = upload.parseRequest(request);
		Iterator itr = items.iterator();
		List<FileItem> files = new ArrayList<FileItem>();
		while (itr.hasNext()) {
			FileItem item = (FileItem) itr.next();
			if ((!(item.isFormField())) && (item.getName() != null)
					&& (!("".equals(item.getName())))) {
				files.add(item);
			}
		}

		for (FileItem item : files) {
			String avatarName = "";
			String fileName = item.getName();
			//String imgName = uploadDir + fileName;
			JrpUlasanTeknikal ut = db.find(JrpUlasanTeknikal.class, get("idUlasan"));
			if (ut.getSuratFileName() != null && ut.getSuratFileName().length() > 0) {
				Util.deleteFile(ut.getSuratFileName());
				Util.deleteFile(ut.getSuratThumbFile());
			}
			String imgName = uploadDir + idUlasan + fileName.substring(fileName.lastIndexOf("."));

			imgName = imgName.replaceAll(" ", "_");
			item.write(new File(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName));

			String mimetype = item.getContentType();
			String type = mimetype.split("/")[0];
			if (type.equals("image")) {
				avatarName = imgName.substring(0, imgName.lastIndexOf("."))
						+ "_avatar" + imgName.substring(imgName.lastIndexOf("."));
				avatarName = avatarName.replaceAll(" ", "_");
				lebah.repository.Thumbnail.create(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName, ResourceBundle.getBundle("dbconnection").getString("folder") + imgName, 600, 560,
						100);
				lebah.repository.Thumbnail.create(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName, ResourceBundle.getBundle("dbconnection").getString("folder") + avatarName, 150, 90,
						100);
			}

			if (!imgName.equals("")) {
				simpanSurat(idUlasan, imgName, avatarName);
			}
		}

		JrpUlasanTeknikal ut = db.find(JrpUlasanTeknikal.class, get("idUlasan"));	
		context.put("ut", ut);
		context.put("selectedTab", "3");
		context.put("selectedSubTab", "6");
		return getPath() + "/ulasanTeknikal/jkptg/uploadSurat.vm";
	}
	
	@Command("doHapusSuratJkptg")
	public String doHapusSuratJkptg() throws Exception {
		JrpUlasanTeknikal ut = db.find(JrpUlasanTeknikal.class, get("idUlasan"));
		if (ut.getSuratFileName() != null && ut.getSuratFileName().length() > 0) {
			Util.deleteFile(ut.getSuratFileName());
			Util.deleteFile(ut.getSuratThumbFile());
		}
		ut.setSuratFileName("");
		ut.setSuratThumbFile("");

		db.begin();
		try {
			db.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}

		return getSenaraiJkptg();
	}
	
	/** END SURAT JKPTG **/
	
	/** START SURAT EPU **/
	
	@Command("uploadSuratUpe")
	public String uploadSuratUpe() throws Exception {
		String idUlasan = get("idUlasan");
		String uploadDir = "jrp/permohonanRuangPejabat/surat/";
		File dir = new File(ResourceBundle.getBundle("dbconnection").getString("folder") + uploadDir);
		if (!dir.exists())
			dir.mkdir();

		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		List items = upload.parseRequest(request);
		Iterator itr = items.iterator();
		List<FileItem> files = new ArrayList<FileItem>();
		while (itr.hasNext()) {
			FileItem item = (FileItem) itr.next();
			if ((!(item.isFormField())) && (item.getName() != null)
					&& (!("".equals(item.getName())))) {
				files.add(item);
			}
		}

		for (FileItem item : files) {
			String avatarName = "";
			String fileName = item.getName();
			//String imgName = uploadDir + fileName;
			JrpUlasanTeknikal ut = db.find(JrpUlasanTeknikal.class, get("idUlasan"));
			if (ut.getSuratFileName() != null && ut.getSuratFileName().length() > 0) {
				Util.deleteFile(ut.getSuratFileName());
				Util.deleteFile(ut.getSuratThumbFile());
			}
			String imgName = uploadDir + idUlasan + fileName.substring(fileName.lastIndexOf("."));

			imgName = imgName.replaceAll(" ", "_");
			item.write(new File(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName));

			String mimetype = item.getContentType();
			String type = mimetype.split("/")[0];
			if (type.equals("image")) {
				avatarName = imgName.substring(0, imgName.lastIndexOf("."))
						+ "_avatar" + imgName.substring(imgName.lastIndexOf("."));
				avatarName = avatarName.replaceAll(" ", "_");
				lebah.repository.Thumbnail.create(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName, ResourceBundle.getBundle("dbconnection").getString("folder") + imgName, 600, 560,
						100);
				lebah.repository.Thumbnail.create(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName, ResourceBundle.getBundle("dbconnection").getString("folder") + avatarName, 150, 90,
						100);
			}

			if (!imgName.equals("")) {
				simpanSurat(idUlasan, imgName, avatarName);
			}
		}

		JrpUlasanTeknikal ut = db.find(JrpUlasanTeknikal.class, get("idUlasan"));	
		context.put("ut", ut);
		context.put("selectedTab", "3");
		context.put("selectedSubTab", "4");
		return getPath() + "/ulasanTeknikal/upe/uploadSurat.vm";
	}
	
	@Command("doHapusSuratUpe")
	public String doHapusSuratUpe() throws Exception {
		JrpUlasanTeknikal ut = db.find(JrpUlasanTeknikal.class, get("idUlasan"));
		if (ut.getSuratFileName() != null && ut.getSuratFileName().length() > 0) {
			Util.deleteFile(ut.getSuratFileName());
			Util.deleteFile(ut.getSuratThumbFile());
		}
		ut.setSuratFileName("");
		ut.setSuratThumbFile("");

		db.begin();
		try {
			db.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}

		return getSenaraiUpe();
	}
	
	/** END SURAT EPU **/
	
	/** START SURAT MOF **/
	
	@Command("uploadSuratMof")
	public String uploadSuratMof() throws Exception {
		String idUlasan = get("idUlasan");
		String uploadDir = "jrp/permohonanRuangPejabat/surat/";
		File dir = new File(ResourceBundle.getBundle("dbconnection").getString("folder") + uploadDir);
		if (!dir.exists())
			dir.mkdir();

		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		List items = upload.parseRequest(request);
		Iterator itr = items.iterator();
		List<FileItem> files = new ArrayList<FileItem>();
		while (itr.hasNext()) {
			FileItem item = (FileItem) itr.next();
			if ((!(item.isFormField())) && (item.getName() != null)
					&& (!("".equals(item.getName())))) {
				files.add(item);
			}
		}

		for (FileItem item : files) {
			String avatarName = "";
			String fileName = item.getName();
			//String imgName = uploadDir + fileName;
			JrpUlasanTeknikal ut = db.find(JrpUlasanTeknikal.class, get("idUlasan"));
			if (ut.getSuratFileName() != null && ut.getSuratFileName().length() > 0) {
				Util.deleteFile(ut.getSuratFileName());
				Util.deleteFile(ut.getSuratThumbFile());
			}
			String imgName = uploadDir + idUlasan + fileName.substring(fileName.lastIndexOf("."));

			imgName = imgName.replaceAll(" ", "_");
			item.write(new File(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName));

			String mimetype = item.getContentType();
			String type = mimetype.split("/")[0];
			if (type.equals("image")) {
				avatarName = imgName.substring(0, imgName.lastIndexOf("."))
						+ "_avatar" + imgName.substring(imgName.lastIndexOf("."));
				avatarName = avatarName.replaceAll(" ", "_");
				lebah.repository.Thumbnail.create(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName, ResourceBundle.getBundle("dbconnection").getString("folder") + imgName, 600, 560,
						100);
				lebah.repository.Thumbnail.create(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName, ResourceBundle.getBundle("dbconnection").getString("folder") + avatarName, 150, 90,
						100);
			}

			if (!imgName.equals("")) {
				simpanSurat(idUlasan, imgName, avatarName);
			}
		}

		JrpUlasanTeknikal ut = db.find(JrpUlasanTeknikal.class, get("idUlasan"));	
		context.put("ut", ut);
		context.put("selectedTab", "3");
		context.put("selectedSubTab", "5");
		return getPath() + "/ulasanTeknikal/mof/uploadSurat.vm";
	}
	
	@Command("doHapusSuratMof")
	public String doHapusSuratMof() throws Exception {
		JrpUlasanTeknikal ut = db.find(JrpUlasanTeknikal.class, get("idUlasan"));
		if (ut.getSuratFileName() != null && ut.getSuratFileName().length() > 0) {
			Util.deleteFile(ut.getSuratFileName());
			Util.deleteFile(ut.getSuratThumbFile());
		}
		ut.setSuratFileName("");
		ut.setSuratThumbFile("");

		db.begin();
		try {
			db.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}

		return getSenaraiMof();
	}
	
	/** END SURAT MOF **/
	
	/** START SIMPAN SURAT **/
	
	public void simpanSurat(String idUlasanTeknikal, String imgName, String avatarName) throws Exception {
		
		JrpUlasanTeknikal ut = db.find(JrpUlasanTeknikal.class, idUlasanTeknikal);
		ut.setSuratFileName(imgName);
		ut.setSuratThumbFile(avatarName);

		db.begin();
		try {
			db.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
	}
	
	/** END SIMPAN SURAT **/
	
	public void totalSewa(JrpPermohonanLokasi r) throws Exception {
		double sewa = r.getSewaSebulan();
		double gst = r.getKadarGst();
		double totalGst=sewa*(gst*0.01);
		double totalSewa= sewa + totalGst;
		r.setTotalSewa(totalSewa);
	}
}
