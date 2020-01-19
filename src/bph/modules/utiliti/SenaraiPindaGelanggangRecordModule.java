package bph.modules.utiliti;

import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import lebah.portal.action.Command;
import lebah.template.DbPersistence;
import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorEqualTo;
import lebah.util.PasswordService;
import portal.module.entity.CSS;
import portal.module.entity.Role;
import portal.module.entity.Users;
import bph.entities.kod.Bandar;
import bph.entities.kod.KodPetugas;
import bph.entities.utiliti.UtilDewan;
import bph.entities.utiliti.UtilGelanggang;
import bph.entities.utiliti.UtilJadualTempahan;
import bph.entities.utiliti.UtilPermohonan;
import bph.utils.DataUtil;
import bph.utils.Util;

public class SenaraiPindaGelanggangRecordModule extends
		LebahRecordTemplateModule<UtilPermohonan> {

	private static final long serialVersionUID = 1L;

	private DbPersistence db = new DbPersistence();
	private DataUtil dataUtil = DataUtil.getInstance(db);

	@SuppressWarnings("unchecked")
	@Override
	public Class getIdType() {
		return String.class;
	}

	@Override
	public void afterSave(UtilPermohonan r) {
		try {
			insertJadualTempahan(r.getDewan().getId(), r.getGelanggang()
					.getId(), r.getTarikhMula(), r.getMasaMula(), r
					.getMasaTamat(), "B", r.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		context.put("selectedTab", "1");
		paintJadualRelated(r);
	}

	@Override
	public void beforeSave() {

	}

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
		// defaultButtonOption();
		// this.setReadonly(true);
		this.setDisableBackButton(true);
		this.setDisableDefaultButton(true);
		addfilter();

		dataUtil = DataUtil.getInstance(db);
		context.put("util", new Util());
		context.put("path", getPath());
		context.put("command", command);
		context.put("uploadDir", ResourceBundle.getBundle("dbconnection")
				.getString("folder"));
		context.remove("pemohon");
		if ("(UTILITI) Penyedia Cawangan".equalsIgnoreCase(userRole)|| "(UTILITI) Pelulus Cawangan".equalsIgnoreCase(userRole)) {
			context.put("selectDewan", dataUtil.getListDewanSahaja(idCawangan));
		}else{
			context.put("selectDewan", dataUtil.getListDewanSahaja());	
		}
		context.put("selectNegeri", dataUtil.getListNegeri());
		paintJadualMula();
		// context.put("addNewRecordButton", false);
		this.setDisableAddNewRecordButton(true);
	}

	private void addfilter() {
		this.addFilter("gelanggang is not null");
		this.addFilter("statusAktif = '1' "); // 1= aktif , 0 = tak aktif
		this.addFilter("statusBayaran = 'Y' "); // Y=dah bayar, T=belum bayar
		this.addFilter("tarikhMula >= '"
				+ Util.getDateTime(new Date(), "yyyy-MM-dd") + "'");
		setOrderBy("tarikhMula ASC");

	}

	@SuppressWarnings("unused")
	private void defaultButtonOption() {
		this.setDisableSaveAddNewButton(true);
		if (!"add_new_record".equals(command)) {
			this.setDisableBackButton(true);
			this.setDisableDefaultButton(true);
		} else {
			this.setDisableBackButton(false);
			this.setDisableDefaultButton(false);
		}
	}

	@Override
	public boolean delete(UtilPermohonan r) throws Exception {
		UtilPermohonan logTempahan = null;
		logTempahan = new UtilPermohonan();
		logTempahan.setIdTempahan(r.getId());
		logTempahan.setDewan(db.find(UtilDewan.class, r.getDewan().getId()));
		logTempahan.setGelanggang(db.find(UtilGelanggang.class, r
				.getGelanggang().getId()));
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
		db.remove(jadualTempahan);
		return true;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/utiliti/senaraiBatalPermohonanGelanggang";
	}

	@Override
	public Class<UtilPermohonan> getPersistenceClass() {
		// TODO Auto-generated method stub
		return UtilPermohonan.class;
	}

	@Override
	public void getRelatedData(UtilPermohonan r) {
		context.put("selectedTab", "1");
		String idDewan = "";
		if (r.getDewan().getId() != null
				&& r.getDewan().getId().trim().length() > 0)
			idDewan = r.getDewan().getId();
		List<UtilGelanggang> list = dataUtil.getListGelanggang(idDewan);
		context.put("selectGelanggang", list);
		UtilPermohonan tempahan = null;
		tempahan = db.find(UtilPermohonan.class, r.getId());
		context.put("tempahan", tempahan);
		paintJadualRelated(tempahan);
	}

	@Override
	public void save(UtilPermohonan r) throws Exception {

		r.setDewan(db.find(UtilDewan.class, get("idDewan")));
		r.setGelanggang(db.find(UtilGelanggang.class, get("idGelanggang")));
		r.setTarikhMula(getDate("tarikhTempahan"));
		r.setTarikhTamat(getDate("tarikhTempahan"));
		r.setMasaMula(getParamAsInteger("masaMula"));
		r.setMasaTamat(getParamAsInteger("masaTamat"));
		r.setTujuan(get("tujuan"));
		r.setJenisPermohonan("WALKIN");
		r.setStatusBayaran("T");
		kadarSewa(r);
	}

	public void kadarSewa(UtilPermohonan r) throws Exception {
		double sewa = r.getGelanggang().getKadarSewa();
		int mula = r.getMasaMula();
		int tamat = r.getMasaTamat();
		int jum = tamat - mula;
		double jumlahjam = (double) jum;
		double jumlahSewa = jumlahjam * sewa;
		r.setAmaun(jumlahSewa);
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

	@Command("getBayaran")
	public String getBayaran() {
		UtilPermohonan permohonan = db.find(UtilPermohonan.class,
				get("idTempahan"));
		context.put("d", permohonan);
		context.put("listUtilPermohonan", getListPermohonan(permohonan));
		context.put("selectedTab", "3");
		return getPath() + "/entry_page.vm";
	}

	@SuppressWarnings("unchecked")
	private List<UtilPermohonan> getListPermohonan(UtilPermohonan r) {
		List<UtilPermohonan> listUtilPermohonanDewan = db
				.list("select x from UtilPermohonan x where x.id = '"
						+ r.getId() + "' order by x.id asc");
		return listUtilPermohonanDewan;
	}

	/** END TAB **/

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

//	@SuppressWarnings("unchecked")
//	private List<UtilGelanggang> getListGelanggang(String idGelanggang) {
//		List<UtilGelanggang> list = db
//				.list("select x from UtilGelanggang x order by x.nama asc");
//		return list;
//	}

	@Command("selectGelanggang")
	public String selectGelanggang() throws Exception {

		String idDewan = "";
		if (get("idDewan").trim().length() > 0)
			idDewan = get("idDewan");
		List<UtilGelanggang> list = dataUtil.getListGelanggang(idDewan);
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
		logTempahan.setGelanggang(db.find(UtilGelanggang.class, tempahan
				.getGelanggang().getId()));
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
		tempahan.setGelanggang(db.find(UtilGelanggang.class,
				get("idGelanggang")));
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

		tempahan.setTujuan(get("tujuan"));
		kadarSewa(tempahan);

		db.begin();
		db.persist(logTempahan);
		try {
			db.commit();
			updateJadualTempahan(tempahan.getDewan().getId(), tempahan
					.getGelanggang().getId(), tempahan.getTarikhMula(),
					tempahan.getMasaMula(), tempahan.getMasaTamat(), "B",
					tempahan.getId());
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
		// return getMaklumatPemohon();
		return getPath() + "/maklumatPemohon.vm";
	}

	@Command("bayarTempahan")
	public String bayarTempahan() throws Exception {
		String idTempahan = getParam("idTempahan");
		UtilPermohonan r = db.find(UtilPermohonan.class, idTempahan);
		context.put("r", r);

		db.begin();
		r.setStatusBayaran("Y");
		db.commit();
		return getPath() + "/bayaran/statusButton.vm";
	}

	/** START DROP DOWN **/
	@Command("selectBandar")
	public String selectBandar() throws Exception {

		String idNegeri = "0";
		if (get("idNegeri").trim().length() > 0)
			idNegeri = get("idNegeri");
		List<Bandar> list = dataUtil.getListBandar(idNegeri);
		context.put("selectBandar", list);

		return getPath() + "/selectBandar.vm";
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

	/** END DROP DOWN **/

	/** START JADUAL TEMPAHAN **/

	private void updateJadualTempahan(String idDewan, String idGelanggang,
			Date tarikhTempahan, int masaMula, int masaTamat, String idStatus,
			String idTempahan) throws Exception {
		db.begin();
		UtilJadualTempahan jadualTempahan = (UtilJadualTempahan) db
				.get("select x from UtilJadualTempahan x where x.permohonan.id = '"
						+ idTempahan + "'");
		UtilDewan dewan = db.find(UtilDewan.class, idDewan);
		UtilGelanggang gelanggang = db.find(UtilGelanggang.class, idGelanggang);
		jadualTempahan.setDewan(dewan);
		jadualTempahan.setGelanggang(gelanggang);
		jadualTempahan.setTarikhTempahan(tarikhTempahan);
		jadualTempahan.setMasaMula(masaMula);
		jadualTempahan.setMasaTamat(masaTamat);
		db.commit();
		paintJadual();
	}

	private void insertJadualTempahan(String idDewan, String idGelanggang,
			Date tarikhTempahan, int masaMula, int masaTamat, String idStatus,
			String idTempahan) throws Exception {
		db.begin();
		UtilDewan dewan = db.find(UtilDewan.class, idDewan);
		UtilGelanggang gelanggang = db.find(UtilGelanggang.class, idGelanggang);
		UtilJadualTempahan tempJadualTempahan = new UtilJadualTempahan();
		tempJadualTempahan.setDewan(dewan);
		tempJadualTempahan.setGelanggang(gelanggang);
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
		// Date tarikhTempahan = getDate("tarikhTempahan");
		// String idGelanggang=get("idGelanggang");
		Date tarikhTempahan = tempahan.getTarikhMula();
		String idGelanggang = tempahan.getGelanggang().getId();
		Hashtable h = new Hashtable();
		h.put("idAset", idGelanggang);
		h.put("date", tarikhTempahan);

		// List<TempJadualTempahan> list =
		// db.list("select t from TempJadualTempahan t where t.tempAsetPBT.id = :idAset and t.tarikhTempahan = :date",
		// h);
		List<UtilJadualTempahan> list = db
				.list(
						"select t from UtilJadualTempahan t where t.gelanggang.id = :idAset and t.tarikhTempahan = :date",
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
		String idGelanggang = get("idGelanggang");
		Hashtable h = new Hashtable();
		h.put("idAset", idGelanggang);
		h.put("date", tarikhTempahan);

		// List<TempJadualTempahan> list =
		// db.list("select t from TempJadualTempahan t where t.tempAsetPBT.id = :idAset and t.tarikhTempahan = :date",
		// h);
		List<UtilJadualTempahan> list = db
				.list(
						"select t from UtilJadualTempahan t where t.gelanggang.id = :idAset and t.tarikhTempahan = :date",
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
