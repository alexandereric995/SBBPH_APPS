package bph.modules.utiliti;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import lebah.portal.action.Command;
import lebah.template.DbPersistence;
import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorEqualTo;
import lebah.util.PasswordService;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import portal.module.entity.CSS;
import portal.module.entity.Role;
import portal.module.entity.Users;
import bph.entities.kod.Bandar;
import bph.entities.kod.JenisDokumen;
import bph.entities.kod.KodPetugas;
import bph.entities.utiliti.UtilDewan;
import bph.entities.utiliti.UtilDokumen;
import bph.entities.utiliti.UtilGelanggang;
import bph.entities.utiliti.UtilJadualTempahan;
import bph.entities.utiliti.UtilPermohonan;
import bph.utils.DataUtil;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class SenaraiPindaDewanRecordModule extends
		LebahRecordTemplateModule<UtilPermohonan> {

	private static final long serialVersionUID = 1L;

	private DbPersistence db = new DbPersistence();
	private DataUtil dataUtil = DataUtil.getInstance(db);
	private MyPersistence mp;

	@SuppressWarnings("unchecked")
	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public void afterSave(UtilPermohonan r) {
		try {
			insertJadualTempahan(r.getDewan().getId(), r.getTarikhMula(), r
					.getMasaMula(), r.getMasaTamat(), "B", r.getId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		context.put("selectedTab", "1");
		paintJadualRelated(r);
	}

	@Override
	public void beforeSave() {

	}

//	private void getDataPemohon(String userId) {
//		Users users = db.find(Users.class, userId);
//		context.put("users", users);
//	}

	@Override
	public void begin() {
		String idCawangan="";
		userId = (String) request.getSession().getAttribute("_portal_login");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		if ("(UTILITI) Penyedia Cawangan".equalsIgnoreCase(userRole)|| "(UTILITI) Pelulus Cawangan".equalsIgnoreCase(userRole)) {
			KodPetugas petugas = (KodPetugas) db.get("select x from KodPetugas x where x.petugas.id = '" + userId + "'");
			if (petugas != null) {
				idCawangan=petugas.getCawangan().getId();
				this.addFilter("dewan.kodCawangan.id= '" + idCawangan + "'");
			} else {
				this.addFilter("dewan is null");
			}
		}
		
		this.setDisableBackButton(true);
		this.setDisableDefaultButton(true);
		addfilter();
		dataUtil = DataUtil.getInstance(db);
		context.put("util", new Util());
		context.put("path", getPath());
		context.put("command", command);
		context.put("uploadDir", ResourceBundle.getBundle("dbconnection").getString("folder"));
		context.remove("pemohon");
		if ("(UTILITI) Penyedia Cawangan".equalsIgnoreCase(userRole)|| "(UTILITI) Pelulus Cawangan".equalsIgnoreCase(userRole)) {
			context.put("selectDewan", dataUtil.getListDewanSahaja(idCawangan));
		}else{
			context.put("selectDewan", dataUtil.getListDewanSahaja());	
		}
		context.put("selectNegeri", dataUtil.getListNegeri());
		paintJadualMula();
		// context.put("addNewRecordButtonDisabled", false);
		this.setDisableAddNewRecordButton(true);
	}

	private void addfilter() {
		this.addFilter("gelanggang is null");
		this.addFilter("statusAktif = '1' "); // 1= aktif , 0 = tak aktif
		this.addFilter("statusBayaran = 'Y' "); // Y=dah bayar, T=belum bayar
		this.addFilter("tarikhMula >= '"+ Util.getDateTime(new Date(), "yyyy-MM-dd") + "'");
		setOrderBy("tarikhMula ASC");
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/utiliti/senaraiBatalPermohonanDewan";
	}

	@Override
	public Class<UtilPermohonan> getPersistenceClass() {
		// TODO Auto-generated method stub
		return UtilPermohonan.class;
	}

	@Override
	public void getRelatedData(UtilPermohonan r) {
		context.put("selectedTab", "1");
		UtilPermohonan tempahan = null;
		tempahan = db.find(UtilPermohonan.class, r.getId());
		context.put("tempahan", tempahan);
		paintJadualRelated(tempahan);

		// kira jumlah jam
	//	int mula = r.getMasaMula();
	//	int tamat = r.getMasaTamat();
	//	int jum = tamat - mula;
	//	double jumlahjam = (double) jum;
	}

	@Override
	public boolean delete(UtilPermohonan r) throws Exception {
		UtilPermohonan logTempahan = null;
		logTempahan = new UtilPermohonan();
		logTempahan.setIdTempahan(r.getId());
		logTempahan.setDewan(db.find(UtilDewan.class, r.getDewan().getId()));
		logTempahan.setPemohon(db.find(Users.class, r.getPemohon().getId()));
		logTempahan.setTarikhMula(r.getTarikhMula());

		logTempahan.setFlagAwam(r.getFlagAwam());
		logTempahan.setFlagSwasta(r.getFlagSwasta());

		logTempahan.setMasaMula(r.getMasaMula());
		logTempahan.setMasaTamat(r.getMasaTamat());
		logTempahan.setTujuan(r.getTujuan());
		logTempahan.setAmaun(r.getAmaun());
		logTempahan.setStatusBayaran(r.getStatusBayaran());
		logTempahan.setJenisPermohonan(r.getJenisPermohonan());
		logTempahan.setStatusPermohonan("D");
		logTempahan.setStatusAktif("0");
		db.persist(logTempahan);

		UtilJadualTempahan jadualTempahan = (UtilJadualTempahan) db
				.get("select x from UtilJadualTempahan x where x.permohonan.id = '"
						+ r.getId() + "'");
		if (jadualTempahan != null) {
			db.remove(jadualTempahan);
		}

		return true;

	}

	@Override
	public void save(UtilPermohonan r) throws Exception {
		// userId = (String) request.getSession().getAttribute("_portal_login");
		// userRole = (String)
		// request.getSession().getAttribute("_portal_role");
		String idPemohon = "";
		String flagAwam = get("flagAwam");
		String flagSwasta = get("flagSwasta");

		idPemohon = get("noPendaftaran");
		r.setDewan(db.find(UtilDewan.class, get("idDewan")));
		r.setTarikhMula(getDate("tarikhTempahan"));
		r.setPemohon(db.find(Users.class, idPemohon));
		// r.setTarikhTamat(getDate("tarikhTempahanTamat"));
		if (flagAwam.equals("Y")) {
			r.setFlagAwam(flagAwam);
		} else {
			r.setFlagAwam("N");
		}
		if (flagSwasta.equals("Y")) {
			r.setFlagSwasta(flagSwasta);
		} else {
			r.setFlagSwasta("N");
		}

		if (flagAwam.equals("Y")) {
			r.setMasaMula(9);
			r.setMasaTamat(23);
		} else {
			r.setMasaMula(getParamAsInteger("masaMula"));
			r.setMasaTamat(getParamAsInteger("masaTamat"));
		}
		r.setTujuan(get("tujuan"));
		r.setJenisPermohonan("WALKIN");
		r.setStatusBayaran("T");
		kadarSewa(r);
	}

	public void kadarSewa(UtilPermohonan r) throws Exception {
		if (r.getFlagAwam().equals("Y"))// if kakitangan awam and penghuni
		{
			double sewa = r.getDewan().getKadarSewaAwam();
			Date tarikhMula = r.getTarikhMula();
			// Date tarikhTamat=r.getTarikhTamat();
			Date tarikhTamat = r.getTarikhMula();
			long diff = tarikhTamat.getTime() - tarikhMula.getTime();
			long jumlahHari = (diff / (24 * 60 * 60 * 1000)) + 1;
			double jumlahSewa = jumlahHari * sewa;
			r.setAmaun(jumlahSewa);
		} else // if swasta/lain-lain
		{
			double sewa = r.getDewan().getKadarSewa();
			int mula = r.getMasaMula();
			int tamat = r.getMasaTamat();
			int jum = tamat - mula;
			double jumlahjam = (double) jum;
			double jumlahSewa = jumlahjam * sewa;
			r.setAmaun(jumlahSewa);
		}
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String findNoMykad = get("find_noMykad");
		String findNoTempahan = get("find_noTempahan");
		String findDewan = get("find_dewan");
		String findNegeri = get("findNegeri");
		String findBandar = get("findBandar");

		map.put("pemohon.id", findNoMykad);
		map.put("id", findNoTempahan);
		map.put("dewan.nama", findDewan);
		map.put("dewan.bandar.negeri.id", new OperatorEqualTo(findNegeri));
		map.put("dewan.bandar.id", new OperatorEqualTo(findBandar));

		return map;
	}

	@Command("getMaklumatPemohonBerdaftar")
	public String getMaklumatPemohonBerdaftar() throws Exception {
		String noPendaftaran = get("noPendaftaran").trim();
		Users pemohon = db.find(Users.class, noPendaftaran);
		if (pemohon != null) {
			context.put("pemohon", pemohon);
		} else {
			context.put("pemohon", "");
		}
		context.put("noPendaftaran", noPendaftaran);
		context.put("selectedTab", "2");
		return getPath() + "/entry_page.vm";
	}

	@Command("selectBandar")
	public String selectBandar() throws Exception {

		String idNegeri = "0";
		if (get("idNegeri").trim().length() > 0)
			idNegeri = get("idNegeri");
		List<Bandar> list = dataUtil.getListBandar(idNegeri);
		context.put("selectBandar", list);

		return getPath() + "/selectBandar.vm";
	}

	@SuppressWarnings("unchecked")
	private List<UtilGelanggang> getListGelanggang(String idGelanggang) {
		List<UtilGelanggang> list = db
				.list("select x from UtilGelanggang x order by x.nama asc");
		return list;
	}

	@Command("selectGelanggang")
	public String selectGelanggang() throws Exception {

		String idGelanggang = "";
		if (get("idGelanggang").trim().length() > 0)
			idGelanggang = get("idGelanggang");
		List<UtilGelanggang> list = getListGelanggang(idGelanggang);
		context.put("selectGelanggang", list);
		return getPath() + "/selectGelanggang.vm";
	}

	@Command("savePermohonan")
	public String savePermohonan() throws Exception {

		UtilPermohonan tempahan = null;
		tempahan = db.find(UtilPermohonan.class, get("idTempahan"));
		int jumlahjam = tempahan.getMasaTamat() - tempahan.getMasaMula();

		// insert log rekod baru
		UtilPermohonan logTempahan = null;
		logTempahan = new UtilPermohonan();
		logTempahan.setIdTempahan(get("idTempahan"));
		logTempahan.setDewan(db.find(UtilDewan.class, tempahan.getDewan()
				.getId()));
		logTempahan.setPemohon(db.find(Users.class, tempahan.getPemohon()
				.getId()));
		logTempahan.setTarikhMula(tempahan.getTarikhMula());

		logTempahan.setFlagAwam(tempahan.getFlagAwam());
		logTempahan.setFlagSwasta(tempahan.getFlagSwasta());
		logTempahan.setMasaMula(tempahan.getMasaMula());
		logTempahan.setMasaTamat(tempahan.getMasaTamat());
		logTempahan.setTujuan(tempahan.getTujuan());
		logTempahan.setAmaun(tempahan.getAmaun());
		logTempahan.setStatusBayaran(tempahan.getStatusBayaran());
		logTempahan.setJenisPermohonan(tempahan.getJenisPermohonan());
		logTempahan.setStatusPermohonan("P");
		logTempahan.setStatusAktif("0");

		// update rekod asal
		String flagAwam = get("flagAwam");
		String flagSwasta = get("flagSwasta");

		String idTempahan = tempahan.getId();
		tempahan.setIdTempahan(idTempahan);
		tempahan.setDewan(db.find(UtilDewan.class, get("idDewan")));
		tempahan.setTarikhMula(getDate("tarikhTempahan"));
		tempahan.setTarikhTamat(getDate("tarikhTempahanTamat"));
		if (flagAwam.equals("Y")) {
			tempahan.setFlagAwam(flagAwam);
		} else {
			tempahan.setFlagAwam("N");
		}
		if (flagSwasta.equals("Y")) {
			tempahan.setFlagSwasta(flagSwasta);
		} else {
			tempahan.setFlagSwasta("N");
		}
		int jumlahjambaru = getParamAsInteger("masaTamat")
				- getParamAsInteger("masaMula");
		if (jumlahjambaru == jumlahjam) {
			tempahan.setMasaMula(getParamAsInteger("masaMula"));
			tempahan.setMasaTamat(getParamAsInteger("masaTamat"));
		}
		// tempahan.setMasaMula(getParamAsInteger("masaMula"));
		// tempahan.setMasaTamat(getParamAsInteger("masaTamat"));
		tempahan.setTujuan(get("tujuan"));
		kadarSewa(tempahan);

		db.begin();
		db.persist(logTempahan);
		try {
			db.commit();
			updateJadualTempahan(tempahan.getDewan().getId(), tempahan
					.getTarikhMula(), tempahan.getMasaMula(), tempahan
					.getMasaTamat(), "B", tempahan.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return getPath() + "/maklumatTempahan.vm";
	}

	@Command("saveDetailPemohon")
	public String saveDetailPemohon() throws Exception {

		boolean addPemohon = false;
		Users pemohon = db.find(Users.class, getParam("noPendaftaran"));
		if (pemohon == null) {
			addPemohon = true;
			pemohon = new Users();
			String username = getParam("nama");
			Role role = db.find(Role.class, "(AWAM) Penjawat Awam");
			CSS css = db.find(CSS.class, "BPH-Anon.css");
			db.begin();
			pemohon.setId(getParam("noPendaftaran"));
			if (getParam("noPendaftaran") != null
					&& getParam("noPendaftaran").trim().length() > 0)
				pemohon.setUserPassword(PasswordService
						.encrypt(getParam("noPendaftaran")));
			pemohon.setUserName(username);
			pemohon.setRole(role);
			pemohon.setCss(css);
			pemohon.setAlamat1(getParam("alamat1"));
			pemohon.setAlamat2(getParam("alamat2"));
			pemohon.setAlamat3(getParam("alamat3"));
			pemohon.setNoTelefon(getParam("noTel"));
			pemohon.setNoTelefonBimbit(getParam("noTelBimbit"));
			pemohon.setNoFaks(getParam("noFax"));
			pemohon.setEmel(getParam("emel"));
			pemohon.setPoskod(getParam("poskod"));
			pemohon.setBandar(db.find(Bandar.class, getParam("idBandar")));
		}

		db.begin();
		if (addPemohon) {
			db.persist(pemohon);
		}

		UtilPermohonan tempahan = null;
		tempahan = db.find(UtilPermohonan.class, get("idTempahan"));
		tempahan.setPemohon(db.find(Users.class, get("noPendaftaran")));

		try {
			db.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getPath() + "/maklumatPemohon.vm";
	}

	@Command("findBandar")
	public String findBandar() throws Exception {

		String findNegeri = "0";
		if (get("findNegeri").trim().length() > 0)
			findNegeri = get("findNegeri");
		List<Bandar> list = dataUtil.getListBandar(findNegeri);
		context.put("findBandar", list);

		return getPath() + "/findBandar.vm";
	}

	/** START TAB **/
	@Command("getMaklumatTempahan")
	public String getMaklumatTempahan() {

		UtilPermohonan permohonan = db.find(UtilPermohonan.class,
				get("idTempahan"));
		context.put("r", permohonan);
		paintJadualRelated(permohonan);
		context.put("selectedTab", "1");
		return getPath() + "/entry_page.vm";
	}

	@Command("getMaklumatPemohon")
	public String getMaklumatPemohon() {
		context.put("noPendaftaran", "");
		context.put("pemohon", null);
		String noPendaftaran = "";
		UtilPermohonan permohonan = db.find(UtilPermohonan.class,
				get("idTempahan"));
		context.put("r", permohonan);
		if (permohonan != null) {
			try {
				noPendaftaran = permohonan.getPemohon().getId();
			} catch (Exception e) {
				noPendaftaran = "";// kes tak isi maklumat pemohon
			}

			Users pemohon = db.find(Users.class, noPendaftaran);
			if (pemohon != null) {
				context.put("pemohon", pemohon);
			} else {
				context.put("pemohon", "");
			}
		}
		context.put("noPendaftaran", noPendaftaran);
		context.put("selectedTab", "2");
		return getPath() + "/entry_page.vm";
	}

	@SuppressWarnings("unchecked")
	private List<UtilPermohonan> getListPermohonan(UtilPermohonan r) {
		List<UtilPermohonan> listUtilPermohonanDewan = db
				.list("select x from UtilPermohonan x where x.id = '"
						+ r.getId() + "' order by x.id asc");
		return listUtilPermohonanDewan;
	}

	@Command("getBayaran")
	public String getBayaran() {
		UtilPermohonan r = db
				.find(UtilPermohonan.class, getParam("idTempahan"));
		context.put("r", r);
		context.put("listUtilPermohonan", getListPermohonan(r));

		context.put("selectedTab", "3");
		return getPath() + "/entry_page.vm";
	}

	@SuppressWarnings("unchecked")
	@Command("getDokumenSokongan")
	public String getDokumenSokongan() {

		List<JenisDokumen> list = dataUtil.getListJenisDokumen();
		context.put("selectJenisDokumen", list);

		UtilPermohonan tempahan = db.find(UtilPermohonan.class,
				get("idTempahan"));
		context.put("r", tempahan);
		List<UtilDokumen> listDokumen = db
				.list("SELECT x FROM UtilDokumen x WHERE x.tempahan.id = '"
						+ tempahan.getId() + "'");
		context.put("listDokumen", listDokumen);

		context.put("selectedTab", "4");
		return getPath() + "/entry_page.vm";
	}

	@Command("bayarTempahan")
	public String bayarTempahan() throws Exception {
		String idTempahan = getParam("idTempahan");
		UtilPermohonan r = db.find(UtilPermohonan.class, idTempahan);
		context.put("r", r);

		db.begin();
		r.setStatusBayaran("Y");

		// update table bayaran di kewangan
		// KewBayaran bayaran = (KewBayaran)
		// db.get("select b from KewBayaran b where b.rppPermohonan.id = '" +
		// idRppPermohonan + "'");
		// if (bayaran != null){bayaran.setFlagBayaran("TB");} //TELAH BAYAR

		db.commit();
		return getPath() + "/bayaran/statusButton.vm";
	}

	/** START DOKUMEN SOKONGAN **/
	@SuppressWarnings("unchecked")
	@Command("uploadDoc")
	public String uploadPhoto() throws Exception {
		String idTempahan = get("idTempahan");
		String tajukDokumen = get("tajukDokumen");
		String idJenisDokumen = get("idJenisDokumen");
		String keteranganDokumen = get("keteranganDokumen");
		UtilDokumen dokumen = new UtilDokumen();
		String uploadDir = ResourceBundle.getBundle("dbconnection").getString(
				"folder")
				+ "utiliti/permohonan/dokumenSokongan/";
		File dir = new File(ResourceBundle.getBundle("dbconnection").getString(
				"folder")
				+ uploadDir);
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
			String imgName = uploadDir + idTempahan + "_" + dokumen.getId()
					+ fileName.substring(fileName.lastIndexOf("."));

			imgName = imgName.replaceAll(" ", "_");
			item.write(new File(ResourceBundle.getBundle("dbconnection")
					.getString("folder")
					+ imgName));

			String mimetype = item.getContentType();
			String type = mimetype.split("/")[0];
			if (type.equals("image")) {
				avatarName = imgName.substring(0, imgName.lastIndexOf("."))
						+ "_avatar"
						+ imgName.substring(imgName.lastIndexOf("."));
				avatarName = avatarName.replaceAll(" ", "_");
				lebah.repository.Thumbnail.create(ResourceBundle.getBundle(
						"dbconnection").getString("folder")
						+ imgName, ResourceBundle.getBundle("dbconnection")
						.getString("folder")
						+ imgName, 600, 560, 100);
				lebah.repository.Thumbnail.create(ResourceBundle.getBundle(
						"dbconnection").getString("folder")
						+ imgName, ResourceBundle.getBundle("dbconnection")
						.getString("folder")
						+ avatarName, 150, 90, 100);
			}

			if (!imgName.equals("")) {
				simpanDokumen(idTempahan, imgName, avatarName, tajukDokumen,
						idJenisDokumen, keteranganDokumen, dokumen);
			}
		}

		return getPath() + "/dokumenSokongan/uploadDoc.vm";
	}

	public void simpanDokumen(String idTempahan, String imgName,
			String avatarName, String tajukDokumen, String idJenisDokumen,
			String keteranganDokumen, UtilDokumen dokumen) throws Exception {

		UtilPermohonan permohonan = db.find(UtilPermohonan.class, idTempahan);
		dokumen.setTempahan(permohonan);
		dokumen.setPhotofilename(imgName);
		dokumen.setThumbfilename(avatarName);
		dokumen.setTajuk(tajukDokumen);
		dokumen.setJenisDokumen(db.find(JenisDokumen.class, idJenisDokumen));
		dokumen.setKeterangan(keteranganDokumen);

		db.begin();
		db.persist(dokumen);
		db.commit();
	}

	@Command("deleteDokumen")
	public String deleteDokumen() throws Exception {
		String idDokumen = get("idDokumen");
		UtilDokumen dokumen = db.find(UtilDokumen.class, idDokumen);

		if (dokumen != null) {
			Util.deleteFile(dokumen.getPhotofilename());
			Util.deleteFile(dokumen.getThumbfilename());
			db.begin();
			db.remove(dokumen);
			db.commit();
		}

		return getDokumenSokongan();
	}

	@Command("refreshList")
	public String refreshList() throws Exception {

		return getDokumenSokongan();
	}

	/** END DOKUMEN SOKONGAN **/

	/** START JADUAL TEMPAHAN **/

	private void updateJadualTempahan(String idDewan, Date tarikhTempahan,
			int masaMula, int masaTamat, String idStatus, String idTempahan)
			throws Exception {
		db.begin();
		UtilJadualTempahan jadualTempahan = (UtilJadualTempahan) db
				.get("select x from UtilJadualTempahan x where x.permohonan.id = '"
						+ idTempahan + "'");
		UtilDewan dewan = db.find(UtilDewan.class, idDewan);
		jadualTempahan.setDewan(dewan);
		jadualTempahan.setTarikhTempahan(tarikhTempahan);
		jadualTempahan.setMasaMula(masaMula);
		jadualTempahan.setMasaTamat(masaTamat);
		db.commit();
		paintJadual();
	}

	private void insertJadualTempahan(String idDewan, Date tarikhTempahan,
			int masaMula, int masaTamat, String idStatus, String idTempahan)
			throws Exception {
		db.begin();
		UtilDewan dewan = db.find(UtilDewan.class, idDewan);
		UtilJadualTempahan tempJadualTempahan = new UtilJadualTempahan();
		tempJadualTempahan.setDewan(dewan);
		tempJadualTempahan.setTarikhTempahan(tarikhTempahan);
		tempJadualTempahan.setMasaMula(masaMula);
		tempJadualTempahan.setMasaTamat(masaTamat);
		tempJadualTempahan.setStatus(idStatus);
		UtilPermohonan permohonan = db.find(UtilPermohonan.class, idTempahan);
		tempJadualTempahan.setPermohonan(permohonan);
		db.persist(tempJadualTempahan);
		db.commit();
	}

	@SuppressWarnings("unchecked")
	public String paintJadualRelated(UtilPermohonan tempahan) {
		String bgcolour = "#008800";
		Date tarikhTempahan = tempahan.getTarikhMula();
		String idDewan = tempahan.getDewan().getId();
		Hashtable h = new Hashtable();
		h.put("idAset", idDewan);
		h.put("date", tarikhTempahan);

		// List<TempJadualTempahan> list =
		// db.list("select t from TempJadualTempahan t where t.tempAsetPBT.id = :idAset and t.tarikhTempahan = :date",
		// h);
		List<UtilJadualTempahan> list = db
				.list(
						"select t from UtilJadualTempahan t where t.dewan.id = :idAset and t.tarikhTempahan = :date and t.gelanggang is null",
						h);
		UtilJadualTempahan tempJadualTempahan = null;

		// PAINT JADUAL TO GREEN
		for (int x = 7; x < 23; x++) {
			bgcolour = "#008800";
			context.put("hour" + (x + 1), bgcolour);
		}

		// PAINT JADUAL BASED ON STATUS
		for (int y = 0; y < list.size(); y++) {
			tempJadualTempahan = list.get(y);
			if ("C".equals(tempJadualTempahan.getStatus())) {
				bgcolour = "#999999";
			} else if ("B".equals(tempJadualTempahan.getStatus())) {
				bgcolour = "#bb0000";
			} else {
				bgcolour = "#008800";
			}

			// highlight masa tempahan yang sedang aktif
			if (tempJadualTempahan.getMasaMula() == tempahan.getMasaMula()) {
				bgcolour = "#e67400";
			} else if (tempJadualTempahan.getMasaTamat() == tempahan
					.getMasaTamat()) {
				bgcolour = "#e67400";
			}

			int start = tempJadualTempahan.getMasaMula();
			int stop = tempJadualTempahan.getMasaTamat();
			int range = stop - start;
			for (int k = 1; k <= range; k++) {
				context.put("hour" + start, bgcolour);
				start = start + 1;
			}
		}

		return getPath() + "/jadualTempahan.vm";
	}

	@SuppressWarnings("unchecked")
	@Command("paintJadual")
	public String paintJadual() {
		String bgcolour = "#008800";
		Date tarikhTempahan = getDate("tarikhTempahan");
		String idDewan = get("idDewan");
		Hashtable h = new Hashtable();
		h.put("idAset", idDewan);
		h.put("date", tarikhTempahan);

		// List<TempJadualTempahan> list =
		// db.list("select t from TempJadualTempahan t where t.tempAsetPBT.id = :idAset and t.tarikhTempahan = :date",
		// h);
		List<UtilJadualTempahan> list = db
				.list(
						"select t from UtilJadualTempahan t where t.dewan.id = :idAset and t.tarikhTempahan = :date and t.gelanggang is null",
						h);
		UtilJadualTempahan tempJadualTempahan = null;

		// PAINT JADUAL TO GREEN
		for (int x = 7; x < 23; x++) {
			bgcolour = "#008800";
			context.put("hour" + (x + 1), bgcolour);
		}

		// PAINT JADUAL BASED ON STATUS
		for (int y = 0; y < list.size(); y++) {
			tempJadualTempahan = list.get(y);
			if ("C".equals(tempJadualTempahan.getStatus())) {
				bgcolour = "#999999";
			} else if ("B".equals(tempJadualTempahan.getStatus())) {
				bgcolour = "#bb0000";
			} else {
				bgcolour = "#008800";
			}

			int start = tempJadualTempahan.getMasaMula();
			int stop = tempJadualTempahan.getMasaTamat();
			int range = stop - start;
			for (int k = 1; k <= range; k++) {
				context.put("hour" + start, bgcolour);
				start = start + 1;
			}
		}
		return getPath() + "/jadualTempahan.vm";
	}

	// papar default jadual
	private void paintJadualMula() {
		String bgcolour = "";
		for (int x = 7; x < 23; x++) {
			bgcolour = "#999999";
			context.put("hour" + (x + 1), bgcolour);
		}
	}
	/** END JADUAL TEMPAHAN **/
}
