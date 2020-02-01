package bph.modules.rpp;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpSession;

import lebah.db.Db;
import lebah.portal.action.Command;
import lebah.template.DbPersistence;
import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorDateBetween;
import lebah.template.OperatorEqualTo;
import lebah.template.UID;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import portal.module.entity.Role;
import portal.module.entity.Users;
import bph.entities.integrasi.FPXRecords;
import bph.entities.integrasi.FPXRecordsRequest;
import bph.entities.kewangan.KewDeposit;
import bph.entities.kewangan.KewInvois;
import bph.entities.kod.Bank;
import bph.entities.kod.JenisUnitRPP;
import bph.entities.kod.Status;
import bph.entities.rpp.RppAkaun;
import bph.entities.rpp.RppJadualTempahan;
import bph.entities.rpp.RppPeranginan;
import bph.entities.rpp.RppPermohonan;
import bph.entities.rpp.RppSelenggara;
import bph.entities.rpp.RppTermaSyaratPeranginan;
import bph.entities.rpp.RppTetapanBukaTempahan;
import bph.entities.rpp.RppTetapanCajTambahan;
import bph.entities.rpp.RppTetapanKuota;
import bph.integrasi.fpx.FPXPkiImplementation;
import bph.integrasi.fpx.FPXUtil;
import bph.mail.mailer.RppMailer;
import bph.utils.DataUtil;
import bph.utils.Util;
import bph.utils.UtilRpp;
import bph.utils.UtilRpp.SummaryRp;
import db.persistence.MyPersistence;

public class TempahanRPRecordModule extends
		LebahRecordTemplateModule<RppPermohonan> {

	private static final long serialVersionUID = 1L;
	private DbPersistence db = new DbPersistence();
	private DataUtil dataUtil;
	private MyPersistence mp;

	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		return String.class;
	}

	@Override
	public String getPath() {
		return "bph/modules/rpp/tempahanPermohonan";
	}

	@Override
	public Class<RppPermohonan> getPersistenceClass() {
		return RppPermohonan.class;
	}

	@Override
	public void begin() {

		dataUtil = DataUtil.getInstance(db);

		userId = (String) request.getSession().getAttribute("_portal_login");
		userRole = (String) request.getSession().getAttribute("_portal_role");

		try {

			mp = new MyPersistence();

			Users objUser = (Users) mp.find(Users.class, userId);
			context.put("objUser", objUser);

			clearContext();
			if (userRole.equalsIgnoreCase("(AWAM) Penjawat Awam")
					|| userRole.equalsIgnoreCase("(AWAM) Pesara")
					|| userRole.equalsIgnoreCase("(AWAM) Badan Berkanun")
					|| userRole.equalsIgnoreCase("(AWAM) Pegawai Tadbir")
					|| userRole
							.equalsIgnoreCase("(AWAM) Pesara Polis / Tentera")
					|| userRole.equalsIgnoreCase("(AWAM) Polis / Tentera")) {
				checkingTarikhBukaTempahanOnline(mp);
				checkingBlacklist(mp);
				// REMOVE...checkingHadPermohonan();
				checkingMaklumatBank(objUser);
				senaraiRPbyGred(objUser);
				checkFlagAktif(mp, userId);
			} else if (userRole.equalsIgnoreCase("(RPP) Penyelia")) { // ADD BY
																		// PEJE
																		// TO
																		// CATER
																		// SEMAKAN
																		// KEKOSONGAN
																		// UNTUK
																		// PENYELIA
				senaraiRPbyPenyelia(objUser);
				context.put("flagPenggunaAktif", true);
			} else {
				senaraiRPbyGred(objUser);
				context.put("flagPenggunaAktif", true);
			}

			defaultButtonOption(userRole);
			filtering(mp);
			tambahanDewasa(mp);
			hadTempahanMenginap();
			listPeranginanDanJenisUnit(userRole);
			tambahanKanakKanak(mp);
			reportFFiltering();

			context.put("qListGred", dataUtil.getListGredPerkhidmatan());
			context.put("listStatus", dataUtil.getListStatusPermohonan());

			/** Untuk tempahan RT */
			context.put("listSebabMohonRT", dataUtil.getListRppSebabMohonRT());

			context.put("qListBulan", dataUtil.getListBulan());
			context.put("userRole", userRole);
			context.put("controllerName", this.moduleId);
			context.put("listBank", dataUtil.getListBank());

			context.put("path", getPath());
			context.put("util", new Util());
			context.put("userRole", userRole);
			context.put("command", command);
			context.put("uploadDir", ResourceBundle.getBundle("dbconnection")
					.getString("folder"));

			redirectSkrinTempahan();

		} catch (Exception e) {
			System.out.println("Error begin : " + e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}

	}

	public void checkFlagAktif(MyPersistence mp, String userId) {
		boolean flagPenggunaAktif = false;
		flagPenggunaAktif = UtilRpp.checkingFlagPenggunaAktif(mp, userId);
		context.put("flagPenggunaAktif", flagPenggunaAktif);
	}

	public void senaraiRPbyGred(Users user) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("searching", false);
		List<SummaryRp> list = null;
		try {
			list = getSenaraiRPbyGred(user, false, map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		context.put("listRpByGred", list);
	}

	/**
	 * ADD BY PEJE TO CATER SEMAKAN KEKOSONGAN UNTUK PENYELIA
	 * 
	 * @param user
	 */
	public void senaraiRPbyPenyelia(Users user) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("searching", false);
		List<SummaryRp> list = null;
		try {
			list = getSenaraiRPbyPenyelia(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		context.put("listRpByGred", list);
	}

	private void defaultButtonOption(String userRole) {
		if (userRole.equalsIgnoreCase("(AWAM) Penjawat Awam")
				|| userRole.equalsIgnoreCase("(AWAM) Pesara")
				|| userRole.equalsIgnoreCase("(AWAM) Badan Berkanun")
				|| userRole.equalsIgnoreCase("(AWAM) Pegawai Tadbir")
				|| userRole.equalsIgnoreCase("(AWAM) Pesara Polis / Tentera")
				|| userRole.equalsIgnoreCase("(AWAM) Polis / Tentera")) {
			this.setDisableAddNewRecordButton(false);
		} else {
			this.setDisableAddNewRecordButton(true);
		}

		this.setDisableDefaultButton(true);
		this.setDisableBackButton(true);
		this.setDisableKosongkanUpperButton(true);
		this.setDisableSaveAddNewButton(true);
		this.setDisabledInfoNextTab(true);
		this.setDisableUpperBackButton(true);
		this.setHideDeleteButton(true);
	}

	@Command("checkingMaklumatBank")
	public void checkingMaklumatBank(Users objUser) {
		context.put("accountInfoExist",
				UtilRpp.checkingAccountInfoExist(objUser));
	}

	@Command("listPeranginanDanJenisUnit")
	public void listPeranginanDanJenisUnit(String userRole) {
		if (userRole.equalsIgnoreCase("(RPP) Penyelia")) {
			context.put("listPeranginan",
					dataUtil.getListPeranginanExceptRppLondon()); // carian
		} else {
			context.put("listPeranginan",
					dataUtil.getListPeranginanByGred(null)); // carian
			// context.put("qListPeranginan",dataUtil.getListPeranginanByGred(""));
			// context.put("qListJenisUnit",dataUtil.getListJenisUnitByGred(""));
		}
	}

	/**
	 * 
	 * CHECKING TOTAL BILANGAN PERMOHONAN TAHUN SEMASA
	 * 
	 */
	@Command("checkingHadPermohonan")
	public void checkingHadPermohonan(MyPersistence mp, Date tarikhMasukRpp) {
		context.put("blockBooking",
				UtilRpp.getTotalBilPrmhnByYear(mp, userId, tarikhMasukRpp));
	}

	/**
	 * 
	 * CHECKING BLACKLIST
	 * 
	 */
	@Command("checkingBlacklist")
	public void checkingBlacklist(MyPersistence mp) {
		context.put("blacklisted", UtilRpp.checkingBlacklist(mp, userId));
	}

	/**
	 * 
	 * CHECKING TARIKH TEMPAHAN ONLINE DIBUKA CHECKING UNTUK TEMPAHAN INDIVIDU
	 * SAHAJA
	 * 
	 */
	@Command("checkingTarikhBukaTempahanOnline")
	public void checkingTarikhBukaTempahanOnline(MyPersistence mp) {
		boolean checkingTarikhBukaOnline = false;
		try {
			checkingTarikhBukaOnline = UtilRpp
					.checkingTarikhTempahanOnlineDibuka();
		} catch (Exception e1) {
			System.out.println("Error checkingTarikhBukaOnline : "
					+ e1.getMessage());
			e1.printStackTrace();
		}
		if (!checkingTarikhBukaOnline) {
			context.put(
					"listBukaTempahan",
					mp.list("select x from RppTetapanBukaTempahan x order by x.tarikhBukaTempahan asc"));
		}
		context.put("bukaUntukDitempah", checkingTarikhBukaOnline);
	}

	@Command("filterCarianRpp")
	public String filterCarianRpp() throws Exception {
		String carianJenisPeranginan = getParam("carianJenisPeranginan");
		context.put("listPeranginan",
				dataUtil.getListPeranginanRpp(carianJenisPeranginan));
		return getPath() + "/divCarianPeranginan.vm";
	}

	/**
	 * BILANGAN HAD TEMPOH MENGINAP DEFAULT 7 HARI
	 * */
	public void hadTempahanMenginap() {
		// RppTetapanHadMenginap objHadMenginap = (RppTetapanHadMenginap)
		// db.get("select x from RppTetapanHadMenginap x");
		// int defaultHad = 7;
		// int hadtempoh =
		// objHadMenginap!=null?objHadMenginap.getBilHari():defaultHad;
		context.put("hadTempohMenginap", 7);
	}

	public void tambahanDewasa(MyPersistence mp) {
		RppTetapanCajTambahan caj = (RppTetapanCajTambahan) mp.find(
				RppTetapanCajTambahan.class, "1432867359415");
		Double tmbh = caj != null ? caj.getCajBayaran() : 0d;
		context.put("extbedcharge", Util.formatDecimal(tmbh)); // tambahan
																// dewasa
	}

	public void tambahanKanakKanak(MyPersistence mp) {
		RppTetapanCajTambahan caj = (RppTetapanCajTambahan) mp.find(
				RppTetapanCajTambahan.class, "1436755298337");
		Double tmbh = caj != null ? caj.getCajBayaran() : 0d;
		context.put("extChargeKid", Util.formatDecimal(tmbh)); // tambahan kanak
																// kanak
	}

	public void redirectSkrinTempahan() {
		context.put("redirectSkrinTempahan", "false");
	}

	private void clearContext() {
		context.remove("redirectSkrinTempahan");
		context.remove("r");
		context.put("bukaUntukDitempah", true);
		context.remove("blockBooking");
		context.remove("blacklisted");
		context.remove("bookingFail");
		context.remove("listBukaTempahan");
		context.put("accountInfoExist", true);
		context.remove("flagPenggunaAktif");
		context.remove("rekodPenginap");
		context.put("skrinBayaranPenyelia", true);
	}

	public void filtering(MyPersistence mp) {

		userId = (String) request.getSession().getAttribute("_portal_login");
		userRole = (String) request.getSession().getAttribute("_portal_role");

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.YEAR, -2);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DATE, 1);
		
		this.addFilter("tarikhPermohonan >= '2016-01-01'");
		
		if (userRole.equalsIgnoreCase("(RPP) Penyelia")) {

			
			/**
			 * paparan permohonan baru & tunggu kelulusan sub & xlulus sub &
			 * lulus sub & Permohonan Diluluskan
			 */
			this.addFilter("status.id in ('1425259713412','1430809277096','1430809277102','1425259713415')");
			this.addFilter("jenisPemohon = 'INDIVIDU' ");

		} else {

			/**
			 * paparan permohonan baru & tunggu kelulusan sub & lulus sub &
			 * Permohonan Diluluskan
			 */
			this.addFilter("status.id in ('1425259713412','1430809277096','1430809277102','1425259713415')");
			this.addFilter("jenisPemohon = 'INDIVIDU' ");
			this.addFilter("rppPeranginan.jenisPeranginan.id in ('RP','RPP') ");

		}

		if (userRole.equalsIgnoreCase("(AWAM) Penjawat Awam")
				|| userRole.equalsIgnoreCase("(AWAM) Pesara")
				|| userRole.equalsIgnoreCase("(AWAM) Badan Berkanun")
				|| userRole.equalsIgnoreCase("(AWAM) Pegawai Tadbir")
				|| userRole.equalsIgnoreCase("(AWAM) Pesara Polis / Tentera")
				|| userRole.equalsIgnoreCase("(AWAM) Polis / Tentera")) {
			this.addFilter("pemohon.id = '" + userId + "'");
		} else if (userRole.equalsIgnoreCase("(RPP) Penyelia")) {
			String listPeranginan = UtilRpp.multipleListSeliaan(mp, userId);
			if (listPeranginan.length() == 0) {
				this.addFilter("rppPeranginan.id = ''");
			} else {
				this.addFilter("rppPeranginan.id in (" + listPeranginan + ")");
			}
			this.setOrderBy("rppPeranginan.namaPeranginan asc");
		}
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pemohon.noKP", getParam("findNoKp"));
		map.put("pemohon.userName", getParam("findUserName"));
		map.put("rppPeranginan.id", new OperatorEqualTo(getParam("findRpp")));
		map.put("status.id", new OperatorEqualTo(getParam("findStatus")));
		map.put("statusBayaran", getParam("findStatusBayaran"));
		map.put("tarikhMasukRpp", new OperatorDateBetween(
				getDate("findTarikhMasukRpp"), getDate("findTarikhKeluarRpp")));
		map.put("tarikhKeluarRpp", new OperatorDateBetween(
				getDate("findTarikhMasukRpp"), getDate("findTarikhKeluarRpp")));

		// TEMPORARY - REQUEST BY EN WAN NAK CEK PERMOHONAN BY NO_TEMPAHAN
		map.put("noTempahan", getParam("findNoTempahan"));

		return map;
	}

	@Override
	public boolean delete(RppPermohonan r) throws Exception {
		return false;
	}

	@Override
	public void getRelatedData(RppPermohonan r) {
		HttpSession session = request.getSession();
		session.removeAttribute("sesIdPermohonan");
		session.removeAttribute("sesModul");
		session.removeAttribute("sesReturnFlag");
		context.remove("blockBooking");
		context.remove("blacklisted");
		context.remove("bookingFailed");

		/**
		 * CHECKING KALAU DATA TEMPAHAN TIDAK LENGKAP. (TIADA RPP AKAUN)
		 * */
		try {
			mp = new MyPersistence();
			RppPermohonan rr = (RppPermohonan) mp.find(RppPermohonan.class,
					r.getId());
			String kelulusanHq = r.getRppPeranginan().getFlagKelulusanSub() != null ? r
					.getRppPeranginan().getFlagKelulusanSub() : "";
			List<RppAkaun> listak = UtilRpp.getListTempahanDanBayaran(mp, r);
			if (!kelulusanHq.equalsIgnoreCase("Y")) {
				checkingTidakLengkap(mp, listak, r);
			} else {
				context.put("bookingFailed", "T");
			}

			int balanceDay = UtilRpp.getPaymentBalanceDay(r);

			context.put("balanceDay", balanceDay);
			context.put("listTempahanDanBayaran", listak);
			context.put("r", rr);
			context.put("selectedTab", "1");
			/** Penyelia shj yg pakai */

		} catch (Exception e) {
			System.out.println("Error getRelatedData : " + e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
	}

	@Override
	public void beforeSave() { /* NOT USE */
	}

	@SuppressWarnings("unchecked")
	@Command("saveBooking")
	public String saveBooking() throws Exception {

		String userIdMohon = getUserIdMohon();

		System.out.println("saveBooking...");
		// String pemohonId = (String)
		// request.getSession().getAttribute("_portal_login");

		String permohonanId = UID.getUID();
		boolean success = saveMainRecordInSql(permohonanId, userIdMohon);

		// AFTERSAVE
		String vm = "";
		RppPermohonan r = null;

		if (success == true) {

			try {

				mp = new MyPersistence();

				// Users user = (Users) mp.find(Users.class, userIdMohon);

				String bookingFailed = "T";

				r = (RppPermohonan) mp.find(RppPermohonan.class, permohonanId);

				List<RppAkaun> listak = null;
				String kelulusanHq = r.getRppPeranginan().getFlagKelulusanSub() != null ? r
						.getRppPeranginan().getFlagKelulusanSub() : "";
				if (!kelulusanHq.equalsIgnoreCase("Y")) {
					UtilRpp.saveTarikhAkhirBayaranInSql(r);
					/** UPDATE TARIKH AKHIR BAYARAN */
					UtilRpp.daftarChalet(mp, r);
					/** CREATE TABLE JADUAL TEMPAHAN DAN TABLE TEMPAHAN CHALET */
					UtilRpp.createRecordBayaran(mp, userIdMohon, r);
					/** CREATE RPPAKAUN & INVOIS & DEPOSIT */

					listak = UtilRpp.getListTempahanDanBayaran(mp, r);

					/**
					 * CHECKING KALAU DATA TEMPAHAN TIDAK LENGKAP. (TIADA RPP
					 * AKAUN) KECUALI PREMIER *
					 */
					List<RppJadualTempahan> listJadual = mp
							.list("select x from RppJadualTempahan x where x.permohonan.id = '"
									+ r.getId() + "' ");
					if (listak.size() == 0 || listJadual.size() == 0) {
						System.out.println(":: BOOKING FAILED : 1."
								+ listak.size() + " 2." + listJadual.size());
						bookingFailed = "Y";
						UtilRpp.deleteAndUpdateStatusGagal(mp, r, listak); // DB.BEGIN,
																			// DB.REMOVE,
																			// DB.COMMIT
					} else {
						bookingFailed = "T";
						emailtoOperator(r, userId);
						emailtoPemohon(r, userId);
					}
				} else {
					/** Notifikasi kepada HQ kelulusan */
					mp.begin();
					List<Role> roles = mp
							.list("select x from Role x where x.name in ('(RPP) Pelulus') ");
					UtilRpp.saveNotifikasi(mp, roles, r.getId(), "Y",
							getClass().getName(), "TEMPAHAN_PREMIER_BARU");
					mp.commit();
				}

				r = (RppPermohonan) mp.find(RppPermohonan.class, permohonanId);

				int balanceDay = UtilRpp.getPaymentBalanceDay(r);
				context.put("balanceDay", balanceDay);
				context.put("bookingFailed", bookingFailed);
				context.put("listTempahanDanBayaran", listak);
				context.put("r", r);

			} finally {
				if (mp != null) {
					mp.close();
				}
			}

			vm = templateDir + "/entry_fields.vm";

		} else {
			System.out.println("FAILED!");
			context.put("bookingFail", true);
			vm = templateDir + "/entry_fields.vm";
		}

		return vm;
	}

	@SuppressWarnings("rawtypes")
	public boolean saveMainRecordInSql(String permohonanId, String userIdMohon)
			throws Exception {

		String idrp = getParam("idrp");
		String jenisPemohon = "INDIVIDU";
		String statusBayaran = "T";
		String jenisPermohonan = "ONLINE";
		String idJenisUnit = getParam("idJenisUnit");
		Integer bilDewasa = getParamAsInteger("bilDewasa");
		Integer bilUnit = getParamAsInteger("bilUnit");
		Integer bilKanakKanak = getParamAsInteger("bilKanakKanak");
		Date tarikhMasukRpp = getDate("tarikhMasukRpp");
		Date tarikhKeluarRpp = getDate("tarikhKeluarRpp");

		String sebabMohonRT = getParam("sebabMohonRT") != "" ? getParam("sebabMohonRT")
				: null;

		String dtIn = new SimpleDateFormat("yyyy-MM-dd").format(tarikhMasukRpp);
		String dtOut = new SimpleDateFormat("yyyy-MM-dd")
				.format(tarikhKeluarRpp);

		List listrp = UtilRpp.hashtableDetailRumahPeranginan(idrp);
		String flagKelulusanSub = "";
		String kodLokasi = "";
		if (listrp.size() > 0) {
			Hashtable dh = (Hashtable) listrp.get(0);
			flagKelulusanSub = (String) dh.get("flagKelulusanSub");
			kodLokasi = (String) dh.get("kodLokasi");
		}

		String status = "";
		if (!flagKelulusanSub.equalsIgnoreCase("Y")) {
			status = "1425259713412"; // Permohonan Baru
		} else {
			status = "1430809277096"; // Dalam Proses
		}

		/** RPP PD DAN LANGKAWI TAKDE TAMBAHAN KANAK2 DAN DEWASA */
		int tambahanDewasa = 0;
		int bilTambahanKanakKanak = 0;
		if (!idrp.equalsIgnoreCase("3") && !idrp.equalsIgnoreCase("14")) {
			try {
				tambahanDewasa = getParamAsInteger("bilTambahanDewasa");
			} catch (NumberFormatException ex) { // handle your exception
				tambahanDewasa = 0;
			}
			try {
				bilTambahanKanakKanak = getParamAsInteger("bilTambahanKanakKanak");
			} catch (NumberFormatException ex) { // handle your exception
				bilTambahanKanakKanak = 0;
			}
		}

		String waktupuncak = "T";
		if (UtilRpp.checkWaktuPuncak(dtIn)) {
			waktupuncak = "Y";
		}

		String generatedNoTempahan = UtilRpp
				.generateNoTempahanIndividuSQL(kodLokasi, null);

		String sql = " INSERT INTO `rpp_permohonan` (`id`, `id_peranginan`, `jenis_pemohon`, `status_bayaran`, `jenis_permohonan`, `id_pemohon`, `no_tempahan`, "
				+ " `id_status`, `id_masuk`, `tarikh_masuk`, `tarikh_permohonan`, `id_jenis_unit_rpp`, `bil_dewasa`, `bil_tambahan_dewasa`, "
				+ " `bil_kanak_kanak`, `bil_unit`, `tarikh_masuk_rpp`, `tarikh_keluar_rpp`, `flag_waktu_puncak`, "
				+ " `bil_tambahan_kanak_kanak`, `id_sebab_mohon_rt` ) "
				+ " VALUES " + " ('"
				+ permohonanId
				+ "', '"
				+ idrp
				+ "', '"
				+ jenisPemohon
				+ "', '"
				+ statusBayaran
				+ "', '"
				+ jenisPermohonan
				+ "', '"
				+ userIdMohon
				+ "', '"
				+ generatedNoTempahan
				+ "', "
				+ " '"
				+ status
				+ "', '"
				+ userIdMohon
				+ "', now() , now(), '"
				+ idJenisUnit
				+ "', "
				+ bilDewasa
				+ ", "
				+ tambahanDewasa
				+ ", "
				+ " "
				+ bilKanakKanak
				+ ", "
				+ bilUnit
				+ ", '"
				+ dtIn
				+ "', '"
				+ dtOut
				+ "', '"
				+ waktupuncak
				+ "', "
				+ " '"
				+ bilTambahanKanakKanak + "', '" + sebabMohonRT + "' ) ";

		boolean success = true;

		Db database = new Db();
		try {
			Statement stmt = database.getStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException ex) {
			success = false;
			System.out.println(":: ERROR saveMainRecordInSql : "
					+ ex.getMessage());
		} finally {
			if (database != null)
				database.close();
		}

		return success;
	}

	@Override
	public void save(RppPermohonan r) throws Exception {

	}

	@Override
	public void afterSave(RppPermohonan r) {

	}

	/**
	 * EMAIL TO PD DAN LANGKAWI EMAIL NOTIFIKASI PERMOHONAN BARU
	 * */
	public void emailtoOperator(RppPermohonan r, String userId) {
		String emelto = "";

		if (!r.getPemohon().getId().equalsIgnoreCase("faizal")
				&& ResourceBundle.getBundle("dbconnection")
						.getString("SERVER_DEFINITION").equals("LIVE")) {
			System.out.println("Send email to operator.");
			if (r.getRppPeranginan().getId().equalsIgnoreCase("3")) {
				// Langkawi
				emelto = "reservation@lgkhvl.com";
			} else if (r.getRppPeranginan().getId().equalsIgnoreCase("14")) {
				// PD
				emelto = "resvn.puteribeachresort@gmail.com";
			}

			if (!emelto.equalsIgnoreCase("")) {
				String emelcc = "rpp@bph.gov.my";
				RppMailer.get().notifikasiPermohonanBaruOperator(emelto,
						emelcc, r);
			}
		}
	}

	/**
	 * EMAIL TO GUEST EMAIL NOTIFIKASI PERMOHONAN BARU DAN PEMBAYARAN YG PERLU
	 * DIBUAT
	 * */
	public void emailtoPemohon(RppPermohonan r, String userId) {
		if ((!r.getPemohon().getId().equalsIgnoreCase("faizal") && !r
				.getPemohon().getId().equalsIgnoreCase("anon"))
				&& ResourceBundle.getBundle("dbconnection")
						.getString("SERVER_DEFINITION").equals("LIVE")) {
			String emelto = r.getPemohon().getEmel();
			if (!emelto.equalsIgnoreCase("") && emelto != null) {
				RppMailer.get().notifikasiTempahanBaru(emelto, r);
			}
		}
	}

	@Command("carianKekosongan")
	public String carianKekosongan() throws Exception {

		userRole = (String) request.getSession().getAttribute("_portal_role");
		userId = (String) request.getSession().getAttribute("_portal_login");
		Users user = null;

		try {
			mp = new MyPersistence();

			if (userRole.equalsIgnoreCase("(AWAM) Penjawat Awam")
					|| userRole.equalsIgnoreCase("(AWAM) Pesara")
					|| userRole.equalsIgnoreCase("(AWAM) Badan Berkanun")
					|| userRole.equalsIgnoreCase("(AWAM) Pegawai Tadbir")
					|| userRole
							.equalsIgnoreCase("(AWAM) Pesara Polis / Tentera")
					|| userRole.equalsIgnoreCase("(AWAM) Polis / Tentera")) {

				user = (Users) mp.find(Users.class, userId);
			}

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("gredId", getParam("qGred"));
			map.put("peranginanId", getParam("qPeranginan"));
			map.put("searching", true);
			List<SummaryRp> list = getSenaraiRPbyGred(user, false, map);
			context.put("listRpByGred", list);

		} catch (Exception ex) {
			System.out.println("ERROR carianKekosongan : " + ex.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}

		return getPath() + "/carianKekosongan/start.vm";

	}

	@Command("paparSemuaKekosongan")
	public String paparSemuaKekosongan() throws Exception {

		String userIdMohon = getUserIdMohon();

		try {
			mp = new MyPersistence();

			Users user = (Users) mp.find(Users.class, userIdMohon);

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("searching", false);
			map.put("gredId", getParam("qGred"));
			List<SummaryRp> list = getSenaraiRPbyGred(user, false, map);
			context.put("listRpByGred", list);

		} catch (Exception ex) {
			System.out.println("ERROR paparSemuaKekosongan : "
					+ ex.getMessage() + " session : "
					+ request.getSession().getId() + " role : " + userRole);
		} finally {
			if (mp != null) {
				mp.close();
			}
		}

		return getPath() + "/carianKekosongan/start.vm";
	}

	@Command("paparJenisUnitByRP")
	public String paparJenisUnitByRP() throws Exception {

		String idrp = getParam("idrp");
		String gredId = getParam("qGred");

		String userIdMohon = getUserIdMohon();

		try {
			mp = new MyPersistence();

			Users user = (Users) mp.find(Users.class, userIdMohon);

			RppPeranginan rp = (RppPeranginan) mp.find(RppPeranginan.class,
					idrp);
			List<JenisUnitRPP> listJenisUnit = getListJenisUnitByPeranginanAndJawatan(
					mp, idrp, user, gredId);

			context.put("rp", rp);
			context.put("listJenisUnit", listJenisUnit);

		} catch (Exception ex) {
			System.out.println("ERROR paparJenisUnitByRP : " + ex.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}

		return getPath() + "/carianKekosongan/senaraiJenisRP.vm";
	}

	@Command("openPopupDateRange")
	public String openPopupDateRange() throws Exception {

		String idJenisUnit = getParam("idju");
		String idrp = getParam("idrp");

		try {
			mp = new MyPersistence();

			RppPeranginan rp = (RppPeranginan) mp.find(RppPeranginan.class,
					idrp);
			JenisUnitRPP jenisUnit = (JenisUnitRPP) mp.find(JenisUnitRPP.class,
					idJenisUnit);

			context.put("idJenisUnit", idJenisUnit);
			context.put("idrp", idrp);
			context.put("rp", rp);
			context.put("jenisUnit", jenisUnit);

			RppTetapanBukaTempahan tetapanBookingDate = (RppTetapanBukaTempahan) mp
					.get("select x from RppTetapanBukaTempahan x where x.flagAktif = 'Y' ");
			String dtto = Util.getDateTime(
					tetapanBookingDate.getTarikhMenginapHingga(), "dd-MM-yyyy");

			Date dateToday = new Date();
			String dtfrom = Util.getDateTime(dateToday, "dd-MM-yyyy");

			String rangeDirection = "['" + dtfrom + "','" + dtto + "']";
			context.put("rangeDirection", rangeDirection);

		} catch (Exception e) {
			System.out.println("Error openPopupDateRange : " + e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return getPath() + "/popup/selectDateRange.vm";
	}

	/**
	 * CHECKING BY AVAILABILITY AND SELENGGARA CHECKING BY WAKTU PUNCAK CHECKING
	 * ON SAME DATE
	 * */
	@Command("paparMaklumatPermohonan")
	public String paparMaklumatPermohonan() throws Exception {

		String idJenisUnit = getParam("idju");
		String idrp = getParam("idrp");
		String userIdMohon = getUserIdMohon();

		try {

			mp = new MyPersistence();

			Users user = (Users) mp.find(Users.class, userIdMohon);

			RppPeranginan rp = (RppPeranginan) mp.find(RppPeranginan.class,
					idrp);
			JenisUnitRPP jenisUnit = (JenisUnitRPP) mp.find(JenisUnitRPP.class,
					idJenisUnit);

			/** Get tarikh buka tempahan equal "Y" **/
			RppTetapanBukaTempahan tetapanBookingDate = (RppTetapanBukaTempahan) mp
					.get("select x from RppTetapanBukaTempahan x where x.flagAktif = 'Y' ");
			if (tetapanBookingDate == null) {
				context.put(
						"listBukaTempahan",
						mp.list("select x from RppTetapanBukaTempahan x order by x.tarikhBukaTempahan asc"));
				context.put("bukaUntukDitempah", false);
				return getPath() + "/notis.vm";
				/** STOP OPERATION RETURN NOTICE. **/
			}

			/** Loop tarikh buka tempahan from to. **/
			Calendar calendar = new GregorianCalendar();
			List<Object> listDisabledDate = new ArrayList<Object>();

			/** INPUT DARI POPUP SELECT DATE RANGE */
			Date dfotest = getDate("tarikhDari"); // Hardcode
													// Util.convertStrToDate("16-03-2016",
													// "dd-MM-yyyy");
			Date dtotest = getDate("tarikhHingga"); // Hardcode
													// Util.convertStrToDate("30-03-2016",
													// "dd-MM-yyyy");

			context.put("selectedTarikhDari",
					Util.getDateTime(dfotest, "dd/MM/yyyy"));
			context.put("selectedTarikhHingga",
					Util.getDateTime(dtotest, "dd/MM/yyyy"));

			Date dateToday = new Date();

			dateToday = dfotest;

			// calendar.setTime(tetapanBookingDate.getTarikhMenginapDari());
			// calendar.setTime(tetapanBookingDate.getTarikhBukaTempahan());
			int defaultKuota = 0;
			int checkingBilAvailableUnit = 0;
			calendar.setTime(dateToday);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);

			// while(calendar.getTime().before(tetapanBookingDate.getTarikhMenginapHingga())
			// ||
			// calendar.getTime().equals(tetapanBookingDate.getTarikhMenginapHingga())){
			while (calendar.getTime().before(dtotest)
					|| calendar.getTime().equals(dtotest)) {

				int countcheck = 0;
				Date result = calendar.getTime();
				calendar.add(Calendar.DATE, 1);
				String strdate = Util.getDateTime(result, "yyyy-MM-dd");

				/***
				 * CHECKING AVAILABILITY DAN SELENGGARA
				 * */
				int bilAvailableUnit = UtilRpp.getBilanganUnitAvailable(mp,
						idJenisUnit, strdate);
				if (bilAvailableUnit == 0) {
					// System.out.println("1.selenggara/availability "+Util.getDateTime(result,"dd MM yyyy"));
					// listDisabledDate.add("'"+Util.getDateTime(result,"dd MM yyyy")+"'");
					countcheck = countcheck + 1;
				}

				/***
				 * CHECKING WAKTU PUNCAK
				 * */
				boolean available = true;
				boolean checkWaktuPuncak = UtilRpp.checkWaktuPuncak(strdate);
				// Check/query balik date boleh diapply by gred jawatan pemohon.
				if (checkWaktuPuncak) {
					String gredJawatanPemohon = user.getGredPerkhidmatan() != null ? user
							.getGredPerkhidmatan().getId() : "";
					available = UtilRpp.queryAvailabilityGredWaktuPuncak(
							gredJawatanPemohon, idJenisUnit);
					// Kalau available false, add to disabled
					if (!available) {
						// System.out.println("waktu puncak "+Util.getDateTime(result,"dd MM yyyy"));
						countcheck = countcheck + 1;
					}
				}

				/***
				 * CHECKING JIKA ADA TARIKH MOHON YANG SAMA
				 * */
				if (userIdMohon == null || userIdMohon.equalsIgnoreCase("")) {
					System.out
							.println(":::::::: NO USER ID FOUND FOR CHECKING SAME DATE :::::::::");
				}
				if (strdate == null || strdate.equalsIgnoreCase("")) {
					System.out
							.println(":::::::: NO STRDATE (UTIL) FOUND FOR CHECKING SAME DATE :::::::::");
				}
				boolean bookingOnSameDate = UtilRpp.getCheckingSameDateBooking(
						mp, userIdMohon, strdate);
				if (bookingOnSameDate) {
					// System.out.println("same date "+Util.getDateTime(result,"dd MM yyyy"));
					countcheck = countcheck + 1;
				}

				/**
				 * CHECKING KUOTA untuk PD dan Langkawi sahaja.
				 * 
				 * */
				Date currentDate = new Date();
				if (idrp.equalsIgnoreCase("14") || idrp.equalsIgnoreCase("3")) {

					Integer dayLeft = (int) ((result.getTime() - currentDate
							.getTime()) / (1000 * 60 * 60 * 24));
					int quotaUse = UtilRpp.getBilanganApplyWithinQuota(
							idJenisUnit, strdate);
					RppTetapanKuota kuota = (RppTetapanKuota) mp
							.get("select x from RppTetapanKuota x where "
									+ dayLeft
									+ " <= x.hari and x.jenisUnitRpp.id = '"
									+ idJenisUnit + "' order by x.hari asc ");

					if (kuota != null) {
						defaultKuota = kuota.getKuota();
					}

					// if(bilAvailableUnit <= 0 || ((defaultKuota - quotaUse) <=
					// 0 )){
					// countcheck = countcheck +1;
					// //System.out.println("quota pd/lw "+Util.getDateTime(result,"dd MM yyyy"));
					// //listDisabledDate.add("'"+Util.getDateTime(result,"dd MM yyyy")+"'");
					// }

					if (bilAvailableUnit <= 0) {
						countcheck = countcheck + 1;
					} else {
						if (kuota != null && ((defaultKuota - quotaUse) <= 0)) {
							countcheck = countcheck + 1;
						}
					}

				}// close filter langkawi/pd

				if (countcheck > 0) {
					// System.out.println("same date "+Util.getDateTime(result,"dd MM yyyy"));
					listDisabledDate.add("'"
							+ Util.getDateTime(result, "dd MM yyyy") + "'");
				} else {
					checkingBilAvailableUnit++;
				}

			}

			/** TIADA UNIT AVAILABLE */
			if (checkingBilAvailableUnit == 0) {
				context.put("unitNotAvailable", true);
				context.put("rp", rp);
				return getPath() + "/notis.vm";
				/** STOP OPERATION RETURN NOTICE. **/
			}

			String dtfrom = Util.getDateTime(
					tetapanBookingDate.getTarikhBukaTempahan(), "dd-MM-yyyy");

			String today = Util.getDateTime(new Date(), "dd-MM-yyyy");

			// if(tetapanBookingDate.getTarikhMenginapDari().before(new
			// Date())){
			if (tetapanBookingDate.getTarikhBukaTempahan().before(new Date())) {
				// Date tempdate = Util.convertStrToDate("03/01/2016",
				// "dd/MM/yyyy");
				// if(new Date().before(tempdate)){
				// String strtempdate = Util.getDateTime(tempdate,
				// "dd-MM-yyyy");
				// dtfrom = strtempdate;
				// }else{
				dtfrom = today;
				// }
			}

			String dtto = Util.getDateTime(dtotest, "dd-MM-yyyy");
			dtfrom = Util.getDateTime(dateToday, "dd-MM-yyyy");

			// String dtto =
			// Util.getDateTime(tetapanBookingDate.getTarikhMenginapHingga(),
			// "dd-MM-yyyy");

			Date maxhingga = tetapanBookingDate.getTarikhMenginapHingga();
			if (dtotest.after(maxhingga)) {
				String strMaxhingga = Util.getDateTime(maxhingga, "dd-MM-yyyy");
				dtto = strMaxhingga;
			}

			// Put tarikh buka tempahan di kalendar (direction:[from,to])
			String calDirection = "['" + dtfrom + "','" + dtto + "']";

			// Add disabled date (lepas validate setiap date)
			context.put("listDisabledDate", listDisabledDate != null
					&& !listDisabledDate.isEmpty() ? listDisabledDate : false);
			context.put("calDirection", calDirection);
			context.put("rp", rp);
			context.put("jenisUnit", jenisUnit);

		} catch (Exception e) {
			System.out.println("Error paparMaklumatPermohonan : "
					+ e.getMessage());
			e.printStackTrace();
		} finally {
			if (mp != null) {
				mp.close();
			}
		}

		return getPath() + "/carianKekosongan/maklumatPermohonan.vm";

	}// close paparMaklumatPermohonan

	@Command("openPopupTempahan")
	public String openPopupTempahan() throws Exception {

		String userIdMohon = getUserIdMohon();

		try {
			mp = new MyPersistence();
			String idJenisUnit = getParam("idJenisUnit");
			RppPeranginan rp = (RppPeranginan) mp.find(RppPeranginan.class,
					getParam("idrp"));
			JenisUnitRPP jenisUnit = (JenisUnitRPP) mp.find(JenisUnitRPP.class,
					idJenisUnit);
			List<RppTermaSyaratPeranginan> listTermaSyaratPeranginan = getListTermaSyaratPeranginan(
					mp, rp);

			Date tarikhMasukRpp = getDate("tarikhMasukRpp");

			checkingHadPermohonan(mp, tarikhMasukRpp);

			boolean bookingOnSameDate = UtilRpp
					.getCheckingSameDateBooking(mp, userIdMohon,
							Util.getDateTime(tarikhMasukRpp, "yyyy-MM-dd"));
			context.put("bookingOnSameDate", bookingOnSameDate);

			/** DOUBLE CHECK */
			/** Validate kekosongan by bilangan unit */
			int bilAvailableUnit = UtilRpp
					.getBilanganUnitAvailable(mp, idJenisUnit,
							Util.getDateTime(tarikhMasukRpp, "yyyy-MM-dd"));
			context.put("bilAvailableUnit", bilAvailableUnit);

			context.put("rp", rp);
			context.put("jenisUnit", jenisUnit);
			context.put("listTermaSyaratPeranginan", listTermaSyaratPeranginan);
		} catch (Exception ex) {
			System.out.println("ERROR openPopupTempahan : " + ex.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}

		return getPath() + "/popup/termaDanTempah.vm";
	}

	@SuppressWarnings("unchecked")
	public List<RppTermaSyaratPeranginan> getListTermaSyaratPeranginan(
			MyPersistence mp, RppPeranginan r) {
		List<RppTermaSyaratPeranginan> listTermaSyaratPeranginan = mp
				.list("select x from RppTermaSyaratPeranginan x where x.rppPeranginan.id = '"
						+ r.getId() + "' ");
		return listTermaSyaratPeranginan;
	}

	@SuppressWarnings("unchecked")
	@Command("batalTempahan")
	public String batalTempahan() throws Exception {

		try {

			mp = new MyPersistence();

			userId = (String) request.getSession()
					.getAttribute("_portal_login");

			String idPermohonan = getParam("idPermohonan");
			Users user = (Users) mp.find(Users.class, userId);
			RppPermohonan p = (RppPermohonan) mp.find(RppPermohonan.class,
					idPermohonan);
			String statusDibatalPemohon = "1435093978588";

			mp.begin();

			p.setTarikhPembatalan(new Date());
			p.setCatatanPembatalan("PEMBATALAN TEMPAHAN");
			p.setPemohonBatal(user);
			p.setStatus((Status) mp.find(Status.class, statusDibatalPemohon));

			UtilRpp.deleteChildTempahan(mp, idPermohonan);

			List<RppAkaun> lk = mp
					.list("select x from RppAkaun x where x.permohonan.id = '"
							+ idPermohonan + "' ");

			if (lk != null) {
				for (int i = 0; i < lk.size(); i++) {
					RppAkaun lj = lk.get(i);

					if (lj.getKodHasil().getId().equalsIgnoreCase("72311")) {
						// deposit
						KewDeposit dep = (KewDeposit) mp
								.get("select x from KewDeposit x where x.idLejar = '"
										+ lj.getId() + "' ");
						if (dep != null) {
							mp.remove(dep);
						}
					} else {
						// invois
						KewInvois inv = (KewInvois) mp
								.get("select x from KewInvois x where x.idLejar = '"
										+ lj.getId() + "' ");
						if (inv != null) {
							mp.remove(inv);
						}
					}

					if (lj != null) {
						lj.setAmaunVoid(lj.getDebit());
						lj.setFlagVoid("Y");
						lj.setTarikhVoid(new Date());
					}
				}
			}

			mp.commit();
			RppMailer.get().notifikasiPembatalanTempahanOlehPemohon(p.getPemohon().getEmel(), p);
		} catch (Exception ex) {
			System.out.println("ERROR batalTempahan : " + ex.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}

		return listPage();

	}

	/**
	 * Get list rpp Filter by gred jawatan
	 * */
	public List<SummaryRp> getSenaraiRPbyGred(Users user, boolean isRT,
			Map<String, Object> paramsMap) throws Exception {
		List<SummaryRp> list = UtilRpp.senaraiRPbyGred(user, isRT, paramsMap);
		return list;
	}

	/**
	 * Get list jenis unit by peranginan Filter by gred jawatan
	 * */
	public List<JenisUnitRPP> getListJenisUnitByPeranginanAndJawatan(
			MyPersistence mp, String idrp, Users user, String gredId)
			throws Exception {
		List<JenisUnitRPP> list = UtilRpp
				.senaraiJenisUnitByPeranginanAndJawatan(mp, idrp, user, gredId);
		return list;
	}

	/**
	 * ADD BY PEJE TO CATER SEMAKAN KEKOSONGAN UNTUK PENYELIA Get list jenis
	 * unit by peranginan Filter by penyelia
	 */
	public List<SummaryRp> getSenaraiRPbyPenyelia(Users user) throws Exception {
		List<SummaryRp> list = UtilRpp.senaraiRPbyPenyelia(user);
		return list;
	}

	@Command("kemaskiniBank")
	public String kemaskiniBank() throws Exception {

		try {
			mp = new MyPersistence();

			RppPermohonan r = (RppPermohonan) mp.find(RppPermohonan.class,
					getParam("idPermohonan"));
			Users user = (Users) mp.find(Users.class, getParam("idUser"));

			mp.begin();
			user.setNoAkaunBank(getParam("noAkaunBank"));
			user.setBank((Bank) mp.find(Bank.class, getParam("bank")));
			user.setFlagSahMaklumatBank(getParam("cbSyarat"));
			mp.commit();

			context.put("r", r);
			context.put("accountInfoExist",
					UtilRpp.checkingAccountInfoExist(user));

		} catch (Exception ex) {
			System.out.println("ERROR kemaskiniBank : " + ex.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return templateDir + "/entry_fields.vm";
	}

	@SuppressWarnings("rawtypes")
	@Command("uploadDoc")
	public String uploadDoc() throws Exception {

		try {
			mp = new MyPersistence();

			String idPermohonan = getParam("idPermohonan");
			RppPermohonan permohonan = (RppPermohonan) mp.find(
					RppPermohonan.class, idPermohonan);
			String uploadDir = "rpp/resitPembayaran/";
			File dir = new File(ResourceBundle.getBundle("dbconnection")
					.getString("folder") + uploadDir);
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
				String imgName = uploadDir + idPermohonan
						+ fileName.substring(fileName.lastIndexOf("."));

				imgName = imgName.replaceAll(" ", "_");
				item.write(new File(ResourceBundle.getBundle("dbconnection")
						.getString("folder") + imgName));

				if (!imgName.equals("")) {
					mp.begin();
					permohonan.setPhotofilename(imgName);
					permohonan.setThumbfilename(avatarName);
					mp.commit();
				}
			}

			context.put("r", permohonan);

		} catch (Exception ex) {
			System.out.println("ERROR uploadDoc : " + ex.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return getPath() + "/uploadDoc.vm";
	}

	@Command("deleteResit")
	public String deleteResit() throws Exception {

		String idPermohonan = getParam("idPermohonan");

		try {
			mp = new MyPersistence();
			RppPermohonan r = (RppPermohonan) mp.find(RppPermohonan.class,
					idPermohonan);

			mp.begin();
			r.setPhotofilename(null);
			r.setThumbfilename(null);
			mp.commit();

		} catch (Exception ex) {
			System.out.println("ERROR deleteResit : " + ex.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}

		return refreshList();
	}

	@Command("refreshList")
	public String refreshList() throws Exception {
		String idPermohonan = getParam("idPermohonan");

		try {
			mp = new MyPersistence();

			RppPermohonan r = (RppPermohonan) mp.find(RppPermohonan.class,
					idPermohonan);
			context.remove("blockBooking");
			context.remove("blacklisted");
			context.remove("bookingFail");
			context.put("listTempahanDanBayaran",
					UtilRpp.getListTempahanDanBayaran(mp, r));

			/**
			 * CHECKING KALAU DATA TEMPAHAN TIDAK LENGKAP. (TIADA RPP AKAUN)
			 * */
			List<RppAkaun> listak = UtilRpp.getListTempahanDanBayaran(mp, r);
			checkingTidakLengkap(mp, listak, r);

			context.put("r", r);

		} catch (Exception ex) {
			System.out.println("ERROR refreshList : " + ex.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return templateDir + "/entry_fields.vm";
	}

	@Command("hantarBuktiPembayaran")
	public String hantarBuktiPembayaran() throws Exception {

		String idPermohonan = getParam("idPermohonan");
		userId = (String) request.getSession().getAttribute("_portal_login");

		try {
			mp = new MyPersistence();

			RppPermohonan r = (RppPermohonan) mp.find(RppPermohonan.class,
					idPermohonan);

			mp.begin();
			r.setStatusBayaran("Y");
			r.setTarikhBayaran(new Date());
			r.setStatus((Status) mp.find(Status.class, "1425259713415")); // terus
																			// selesai
																			// tukar
																			// ke
																			// permohonan
																			// lulus
			r.setCatatanPulangTempahan(null);

			// update lejar
			List<RppAkaun> listAkaun = UtilRpp.getListTempahanDanBayaran(mp, r);
			for (int i = 0; i < listAkaun.size(); i++) {
				listAkaun.get(i).setKredit(listAkaun.get(i).getDebit());
				listAkaun.get(i).setTarikhTransaksi(new Date());
				listAkaun.get(i).setFlagBayar("Y");
			}

			mp.commit();

			// EMEL TO PD DAN LANGKAWI
			// EMEL NOTIFIKASI BUKTI BAYAR
			String emelto = "";

			if (!r.getPemohon().getId().equalsIgnoreCase("faizal")
					&& ResourceBundle.getBundle("dbconnection")
							.getString("SERVER_DEFINITION").equals("LIVE")) {
				System.out.println("Send email to operator.");
				if (r.getRppPeranginan().getId().equalsIgnoreCase("3")) {
					// Langkawi
					emelto = "reservation@lgkhvl.com";
				} else if (r.getRppPeranginan().getId().equalsIgnoreCase("14")) {
					// PD
					emelto = "resvn.puteribeachresort@gmail.com";
				}

				if (!emelto.equalsIgnoreCase("")) {
					String emelcc = "rpp@bph.gov.my";
					RppMailer.get().notifikasiOperatorUploadSlipBayaran(emelto,
							emelcc, r);
				}
			}

		} catch (Exception ex) {
			System.out.println("ERROR hantarBuktiPembayaran : "
					+ ex.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}

		return refreshList();
	}

	/**
	 * ONLINE PAYMENT
	 * 
	 * */
	@Command("paparPilihan")
	public String paparPilihan() throws Exception {
		String vm = "";
		String idPermohonan = getParam("idPermohonan");
		context.remove("recheckPaymentMsg");

		try {
			mp = new MyPersistence();
			RppPermohonan r = (RppPermohonan) mp.find(RppPermohonan.class,
					idPermohonan);

			if (r != null) {
				Boolean belumBayar = reCheckPaymentStatus(mp, r.getId());

				if (belumBayar) {
					String serverName = request.getServerName();
					String contextPath = request.getContextPath();
					int serverPort = request.getServerPort();
					String server = serverPort != 80 ? serverName + ":"
							+ serverPort : serverName;
					String image_url = "http://" + server + contextPath;
					context.put("imageUrl", image_url);

					String fpx_checkSum = "";
					String final_checkSum = "";
					String fpx_msgType = "AR";
					String fpx_msgToken = "01";
					String fpx_sellerExId = "EX00000345";
					String fpx_sellerExOrderNo = new SimpleDateFormat(
							"yyyyMMddHHmmss").format(new Date());
					String fpx_sellerTxnTime = new SimpleDateFormat(
							"yyyyMMddHHmmss").format(new Date());
					String fpx_sellerOrderNo = r.getId();
					String fpx_sellerId = "SE00000392";
					String fpx_sellerBankCode = "01";
					String fpx_txnCurrency = "MYR";

					String fpx_txnAmount = Double.toString(r
							.amaunTotalSewaRpWithDeposit());
					String fpx_buyerEmail = "";
					String fpx_buyerName = "";
					String fpx_buyerBankId = "";
					String fpx_buyerBankBranch = "";
					String fpx_buyerAccNo = "";
					String fpx_buyerId = "";
					String fpx_makerName = "";
					String fpx_buyerIban = "";
					String fpx_productDesc = "Pembayaran Tempahan RP";
					String fpx_version = "5.0";

					fpx_checkSum = fpx_buyerAccNo + "|" + fpx_buyerBankBranch
							+ "|" + fpx_buyerBankId + "|" + fpx_buyerEmail
							+ "|" + fpx_buyerIban + "|" + fpx_buyerId + "|"
							+ fpx_buyerName + "|";
					fpx_checkSum += fpx_makerName + "|" + fpx_msgToken + "|"
							+ fpx_msgType + "|" + fpx_productDesc + "|"
							+ fpx_sellerBankCode + "|" + fpx_sellerExId + "|";
					fpx_checkSum += fpx_sellerExOrderNo + "|" + fpx_sellerId
							+ "|" + fpx_sellerOrderNo + "|" + fpx_sellerTxnTime
							+ "|" + fpx_txnAmount + "|" + fpx_txnCurrency + "|"
							+ fpx_version;

					final_checkSum = FPXPkiImplementation.signData(
							"D:\\SMIExchange\\bph.gov.my.key", fpx_checkSum,
							"SHA1withRSA");

					context.put("fpx_msgType", fpx_msgType);
					context.put("fpx_msgToken", fpx_msgToken);
					context.put("fpx_sellerExId", fpx_sellerExId);
					context.put("fpx_sellerExOrderNo", fpx_sellerExOrderNo);
					context.put("fpx_sellerTxnTime", fpx_sellerTxnTime);
					context.put("fpx_sellerOrderNo", fpx_sellerOrderNo);
					context.put("fpx_sellerId", fpx_sellerId);
					context.put("fpx_sellerBankCode", fpx_sellerBankCode);
					context.put("fpx_txnCurrency", fpx_txnCurrency);
					context.put("fpx_txnAmount", fpx_txnAmount);
					context.put("fpx_buyerEmail", fpx_buyerEmail);
					context.put("fpx_buyerName", fpx_buyerName);
					context.put("fpx_buyerBankId", fpx_buyerBankId);
					context.put("fpx_buyerBankBranch", fpx_buyerBankBranch);
					context.put("fpx_buyerAccNo", fpx_buyerAccNo);
					context.put("fpx_buyerId", fpx_buyerId);
					context.put("fpx_makerName", fpx_makerName);
					context.put("fpx_buyerIban", fpx_buyerIban);
					context.put("fpx_productDesc", fpx_productDesc);
					context.put("fpx_version", fpx_version);
					context.put("fpx_checkSum", final_checkSum);

					// MIGS
					context.put("URL", "https://migs.mastercard.com.au/vpcpay");
					context.put("accessCode", "95541A4A");
					context.put("noRujukan", r.getId());
					context.put("mercID", "10701400013");
					context.put("itemDesc", "PEMBAYARAN SEWA DEPOSIT RP");// max
																			// 34
																			// char
																			// only
					Double jum = r.amaunTotalSewaRpWithDeposit() * 100;
					String jumlah = jum.toString();

					String[] amaun = jumlah.split("\\.");
					String amaun1 = amaun[0].toString();
					context.put("amaun", amaun1);
					context.put("hashCode", "C4342B9D0330CDD09FB7337EAF95FDA8");

					HttpSession session = request.getSession();
					session.setAttribute("sesIdPermohonan", r.getId());
					session.setAttribute("sesModul", "IR");
					session.setAttribute("sesRole", (String) request
							.getSession().getAttribute("_portal_role"));
					session.setAttribute("returnlink", "../sbbphv2/c/1425001361831");

					// AZAM ADD - 14/1/2016
					FPXUtil fpxUtil = new FPXUtil(session);
					fpxUtil.addUpdatePayment_Transaction((String) request
							.getSession().getAttribute("_portal_login"),
							(String) session.getAttribute("sesIdPermohonan"),
							fpx_txnAmount, (String) session
									.getAttribute("sesModul"));

					fpxUtil.registerFPXRequest(fpx_sellerId, fpx_sellerExId,
							fpx_sellerOrderNo, fpx_sellerExOrderNo,
							fpx_txnAmount, fpx_productDesc, "IR", mp);

					vm = "bph/modules/fpx/pilihan.vm";
				} else {
					String msg = "Rekod transaksi pembayaran sedang diproses.Sila semak semula dalam tempoh 30 minit untuk status pembayaran dikemaskini.<br>Harap maklum.";
					context.put("recheckPaymentMsg", msg);
					vm = "bph/modules/fpx/pending.vm";
				}
			}

		} catch (Exception ex) {
			System.out
					.println("Error paparPilihan FPX RP : " + ex.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}

		return vm;
	}

	@Command("checkingSelectedDate")
	public String checkingSelectedDate() throws Exception {

		String validSelectedDate = "true";
		String listDisabledDate = getParam("listDisabledDate");

		if (!listDisabledDate.equalsIgnoreCase("false")) {

			String[] loopDate = listDisabledDate.split(",");
			Date datefrom = getDate("datefrom");
			Date dateto = getDate("dateto");

			int count = 0;
			if (loopDate.length > 0) {
				for (int i = 0; i < loopDate.length; i++) {
					String strdate = loopDate[i].trim();
					DateFormat format = new SimpleDateFormat("dd MM yyyy",
							Locale.ENGLISH);
					Date datex = format.parse(strdate);

					if (datex.after(datefrom) && datex.before(dateto)) {
						count = count + 1;
					}
				}
			}

			if (count > 0) {
				validSelectedDate = "false";
			}
		}

		context.put("validSelectedDate", validSelectedDate);
		return getPath() + "/errorSelectDate.vm";
	}

	/**
	 * Get max bil unit yang boleh ditempah
	 * */
	@Command("checkingBilUnit")
	public String checkingBilUnit() throws Exception {

		String dtIn = new SimpleDateFormat("yyyy-MM-dd")
				.format(getDate("datefrom"));
		String dtOut = new SimpleDateFormat("yyyy-MM-dd")
				.format(getDate("dateto"));
		String idJenisUnit = getParam("idJenisUnit");
		Date dateToday = new Date();

		try {
			mp = new MyPersistence();

			JenisUnitRPP jenisUnit = (JenisUnitRPP) mp.find(JenisUnitRPP.class,
					idJenisUnit);
			int bilAvailableUnit = UtilRpp.getBilanganUnitAvailableByRange(mp,
					idJenisUnit, dtIn, dtOut);
			int hadMenginap = jenisUnit.getHadKuantiti();
			int defaultKuota = 0;

			if (hadMenginap > 1) {
				if (bilAvailableUnit <= hadMenginap) {
					hadMenginap = bilAvailableUnit;
				}
			}

			/** Langkawi dan PD sahaja */
			if (jenisUnit.getPeranginan().getId().equalsIgnoreCase("14")
					|| jenisUnit.getPeranginan().getId().equalsIgnoreCase("3")) {

				Integer dayLeft = (int) ((getDate("datefrom").getTime() - dateToday
						.getTime()) / (1000 * 60 * 60 * 24));
				int quotaUse = UtilRpp.getBilanganApplyWithinQuota(idJenisUnit,
						dtIn);
				RppTetapanKuota kuota = (RppTetapanKuota) mp
						.get("select x from RppTetapanKuota x where " + dayLeft
								+ " <= x.hari and x.jenisUnitRpp.id = '"
								+ idJenisUnit + "' order by x.hari asc ");

				int leftQuota = 0;

				if (kuota != null) {
					defaultKuota = kuota.getKuota();
					leftQuota = (defaultKuota - quotaUse);
					if (leftQuota <= 1) {
						hadMenginap = leftQuota;
					}
				}

			}

			/** ELAKKAN HAD JADI 0 */
			if (hadMenginap == 0) {
				hadMenginap = 1;
			}

			context.put("hadUnit", hadMenginap);

		} catch (Exception ex) {
			System.out.println("ERROR checkingBilUnit : " + ex.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return getPath() + "/carianKekosongan/bilUnit.vm";
	}

	@Command("plusNextDay")
	public String plusNextDay() throws Exception {

		String userIdMohon = getUserIdMohon();

		try {
			mp = new MyPersistence();

			RppTetapanBukaTempahan tetapanBookingDate = (RppTetapanBukaTempahan) mp
					.get("select x from RppTetapanBukaTempahan x where x.flagAktif = 'Y' ");

			Users user = (Users) mp.find(Users.class, userIdMohon);

			List<Object> listEnableDate = new ArrayList<Object>();

			Date datefrom = getDate("datefrom");
			String strDateIn = Util.getDateTime(datefrom, "yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			cal.setTime(datefrom);

			Date dateto = tetapanBookingDate.getTarikhMenginapHingga();
			Calendar cal2 = Calendar.getInstance();
			cal2.setTime(dateto);
			cal2.add(Calendar.DATE, 1); // 1 day after tarikh tutup
			String dtto = Util.getDateTime(cal2.getTime(), "dd-MM-yyyy");

			listEnableDate.add("'" + Util.getDateTime(datefrom, "dd MM yyyy")
					+ "'"); // add date from (elakkan infinity loop kalau next
							// calendar xde date)

			String idJenisUnit = getParam("idJenisUnit");
			JenisUnitRPP jenisUnit = (JenisUnitRPP) mp.find(JenisUnitRPP.class,
					idJenisUnit);
			if (jenisUnit != null) {
				String idrp = jenisUnit.getPeranginan().getId();
				String jenisPeranginan = jenisUnit.getPeranginan()
						.getJenisPeranginan().getId();
				int maxDayBooked = 0;
				if (jenisPeranginan.equalsIgnoreCase("RT")) {
					maxDayBooked = 14;
				} else {
					maxDayBooked = 7;
				}

				int defaultKuota = 0;
				Date currentDate = new Date();
				boolean startDate = true;
				boolean stopLoopingDate = false;
				for (int i = 0; i <= maxDayBooked; i++) {
					if (!stopLoopingDate) {
						Date dateLooping = cal.getTime();
						cal.add(Calendar.DATE, 1);
						String strDateOut = Util.getDateTime(dateLooping,
								"yyyy-MM-dd");

						// checking with datein dateout and id jenis unit.
						// checking dngan availability, selenggara, waktu
						// puncak, same date
						int bilAvailableUnit = UtilRpp
								.getBilanganUnitAvailableByRange(mp,
										idJenisUnit, strDateIn, strDateOut);

						if (bilAvailableUnit > 0) {

							// CHECK QUOTA PD/LGWI
							if (idrp.equalsIgnoreCase("14")
									|| idrp.equalsIgnoreCase("3")) {
								Integer dayLeft = (int) ((dateLooping.getTime() - currentDate
										.getTime()) / (1000 * 60 * 60 * 24));
								int quotaUse = UtilRpp
										.getBilanganApplyWithinQuota(
												idJenisUnit, strDateOut);
								RppTetapanKuota kuota = (RppTetapanKuota) mp
										.get("select x from RppTetapanKuota x where "
												+ dayLeft
												+ " <= x.hari and x.jenisUnitRpp.id = '"
												+ idJenisUnit
												+ "' order by x.hari asc ");

								if (kuota != null) {
									defaultKuota = kuota.getKuota();
								}

								if (kuota != null
										&& ((defaultKuota - quotaUse) <= 0)) {
									listEnableDate.add("'"
											+ Util.getDateTime(dateLooping,
													"dd MM yyyy") + "'");
									break;
								}
							}

							boolean bookingOnSameDate = UtilRpp
									.getCheckingSameDateBooking(mp,
											userIdMohon, strDateOut);
							if (!bookingOnSameDate) {
								// TAKDE BOOKING HARI YG SAMA
								boolean available = true;
								boolean checkWaktuPuncak = UtilRpp
										.checkWaktuPuncak(strDateOut);

								if (checkWaktuPuncak) {

									String gredJawatanPemohon = user
											.getGredPerkhidmatan() != null ? user
											.getGredPerkhidmatan().getId() : "";
									available = UtilRpp
											.queryAvailabilityGredWaktuPuncak(
													gredJawatanPemohon,
													idJenisUnit);
									if (available) {
										listEnableDate.add("'"
												+ Util.getDateTime(dateLooping,
														"dd MM yyyy") + "'");
									} else {
										// tak boleh melompat waktu puncak. just
										// tambah next day.
										listEnableDate.add("'"
												+ Util.getDateTime(dateLooping,
														"dd MM yyyy") + "'");
										break;
									}

								} else {
									listEnableDate.add("'"
											+ Util.getDateTime(dateLooping,
													"dd MM yyyy") + "'");
								}
							} else {
								// USER ADA CHECKIN PADA TEMPAHAN LAIN
								// CHECK SAMA ADA DATELOOPING ADALAH SAMA DENGAN
								// START DATE
								if (!startDate) {
									listEnableDate.add("'"
											+ Util.getDateTime(dateLooping,
													"dd MM yyyy") + "'");
								}
							}
						}// close bilAvailableUnit
					}
					startDate = false;
				}
			}

			String dtfrom = Util.getDateTime(datefrom, "dd-MM-yyyy");

			String cal2Direction = "['" + dtfrom + "','" + dtto + "']";
			context.put("cal2Direction", cal2Direction);

			// elakkan return 1 je
			if (listEnableDate.size() == 1) {
				Calendar cal3 = Calendar.getInstance();
				cal3.setTime(datefrom);
				cal3.add(Calendar.DATE, 1); // 1 day after tarikh tutup
				Date nextDay = cal3.getTime();
				listEnableDate.add("'"
						+ Util.getDateTime(nextDay, "dd MM yyyy") + "'");
			}

			context.put("listEnableDate", listEnableDate != null
					&& !listEnableDate.isEmpty() ? listEnableDate : false);

			// RppTetapanBukaTempahan tetapanBookingDate =
			// (RppTetapanBukaTempahan)mp.get("select x from RppTetapanBukaTempahan x where x.flagAktif = 'Y' ");
			//
			// List<Object> listDisabledDate = new ArrayList<Object>();
			// List<Object> previousListDisabledDate = new ArrayList<Object>();

			// Date datefrom = getDate("datefrom");
			// Calendar cal = Calendar.getInstance();
			// cal.setTime(datefrom);
			// cal.add(Calendar.DATE, 1);
			// Date nextDay = cal.getTime();
			//
			// String strDateFrom = Util.getDateTime(nextDay,"dd MM yyyy");
			//
			// String strListDisabledDate = getParam("listDisabledDate");
			// if(!strListDisabledDate.equalsIgnoreCase("false")){
			// String[] loopDate = strListDisabledDate.split(",");
			// if (loopDate.length > 0) {
			// for (int i = 0; i < loopDate.length; i++) {
			// String strdate = loopDate[i].trim();
			// previousListDisabledDate.add("'"+strdate+"'");
			// if(!strdate.equalsIgnoreCase(strDateFrom)){
			// listDisabledDate.add("'"+strdate+"'");
			// }
			//
			// }
			// }
			// }

			// String dtfrom = Util.getDateTime(nextDay, "dd-MM-yyyy");
			// String dtto =
			// Util.getDateTime(tetapanBookingDate.getTarikhMenginapHingga(),
			// "dd-MM-yyyy");
			//
			// if(tetapanBookingDate.getTarikhMenginapHingga().before(nextDay)){
			// dtto = dtfrom;
			// }
			//
			// String calDirection = "['"+dtfrom+"','"+dtto+"']";
			// context.put("calDirection",calDirection);
			//
			// context.put("previousListDisabledDate",previousListDisabledDate!=null&&!previousListDisabledDate.isEmpty()?previousListDisabledDate:false);
			// context.put("listDisabledDate",listDisabledDate!=null&&!listDisabledDate.isEmpty()?listDisabledDate:false);

		} catch (Exception e) {
			System.out.println("Error plusNextDay : " + e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}

		return getPath() + "/carianKekosongan/secondCalendar.vm";
	}

	public void reportFFiltering() {
		context.put("jenisReport", "RPP");
	}

	@Command("saveCatatanBookingHq")
	public String saveCatatanBookingHq() throws Exception {

		try {
			mp = new MyPersistence();

			String idpermohonan = getParam("idPermohonan");
			RppPermohonan r = (RppPermohonan) mp.find(RppPermohonan.class,
					idpermohonan);
			mp.begin();
			r.setCatatanBookingHq(getParam("catatanBookingHq"));
			mp.commit();
			context.put("r", r);

		} catch (Exception ex) {
			System.out.println("ERROR saveCatatanBookingHq : "
					+ ex.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return getPath() + "/catatanBookingHq.vm";
	}

	@SuppressWarnings("unchecked")
	public void checkingTidakLengkap(MyPersistence mp, List<RppAkaun> listak,
			RppPermohonan r) {
		List<RppJadualTempahan> listJadual = mp
				.list("select x from RppJadualTempahan x where x.permohonan.id = '"
						+ r.getId() + "' ");
		if (listak.size() == 0 || listJadual.size() == 0) {
			context.put("bookingFailed", "Y");
		} else {
			context.put("bookingFailed", "T");
		}
	}

	@Command("callPopupSenaraiPenginap")
	public String callPopupSenaraiPenginap() throws Exception {
		try {
			mp = new MyPersistence();
			String carianPenginap = getParam("carianPenginap");
			List<Users> listNama = searchUsers(mp, carianPenginap);
			context.put("listNama", listNama);
		} catch (Exception ex) {
			System.out.println("ERROR callPopupSenaraiPenginap : "
					+ ex.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return getPath() + "/daftarkanTempahan/popupCarianPenginap.vm";
	}

	private List<Users> searchUsers(MyPersistence mp, String param) {
		ArrayList<Users> list = new ArrayList<Users>();
		Db db1 = null;
		try {
			db1 = new Db();
			String sql = "SELECT DISTINCT a.user_login FROM users a "
					+ " WHERE (upper(user_name) LIKE upper('%" + param
					+ "%') OR upper(no_kp) LIKE upper('%" + param + "%')) ";

			ResultSet rs = db1.getStatement().executeQuery(sql);
			while (rs.next()) {
				Users us = (Users) mp.find(Users.class,
						rs.getString("user_login"));
				list.add(us);
			}
		} catch (Exception e) {
			System.out.println("error searchUsers : " + e.getMessage());
			e.printStackTrace();
		} finally {
			if (db1 != null)
				db1.close();
		}
		return list;
	}

	@Command("savePilihanPenginap")
	public String savePilihanPenginap() throws Exception {
		try {
			mp = new MyPersistence();
			String guestId = getParam("radNama");
			Users rekodPenginap = (Users) mp.find(Users.class, guestId);
			context.put("rekodPenginap", rekodPenginap);
		} catch (Exception ex) {
			System.out
					.println("ERROR savePilihanPenginap : " + ex.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return getPath() + "/daftarkanTempahan/maklumatPenginap.vm";
	}

	@Command("filterRppByPenginap")
	public String filterRppByPenginap() {
		try {
			mp = new MyPersistence();
			String guestId = getParam("radNama");
			Users rekodPenginap = (Users) mp.find(Users.class, guestId);
			context.put("rekodPenginap", rekodPenginap);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("searching", false);
			List<SummaryRp> list = null;
			list = getSenaraiRPbyGred(rekodPenginap, false, map);
			context.put("listRpByGred", list);
		} catch (Exception ex) {
			System.out
					.println("ERROR filterRppByPenginap : " + ex.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return getPath() + "/carianKekosongan/start.vm";
	}

	@Command("calculateHad")
	public String calculateHad() {
		try {
			mp = new MyPersistence();
			int bilUnit = getParamAsInteger("bilUnit");
			String idJenisUnit = getParam("idJenisUnit");
			JenisUnitRPP jenisUnitRpp = (JenisUnitRPP) mp.find(
					JenisUnitRPP.class, idJenisUnit);
			if (jenisUnitRpp != null) {
				int defaultHadDewasa = 0;
				int defaultHadKanakKanak = 0;
				if (bilUnit > 0) {
					defaultHadDewasa = (jenisUnitRpp.getHadDewasa() * bilUnit);
					defaultHadKanakKanak = (jenisUnitRpp.getHadKanakKanak() * bilUnit);
				} else {
					defaultHadDewasa = jenisUnitRpp.getHadDewasa();
					defaultHadKanakKanak = jenisUnitRpp.getHadKanakKanak();
				}
				context.put("defaultHadDewasa", defaultHadDewasa);
				context.put("defaultHadKanakKanak", defaultHadKanakKanak);
			}
		} catch (Exception ex) {
			System.out.println("ERROR calculateHad : " + ex.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return getPath() + "/carianKekosongan/divRefreshHad.vm";
	}

	public String getUserIdMohon() {
		String idPemohon = (String) request.getSession().getAttribute(
				"_portal_login");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		if (userRole != null) {
			if (userRole.equalsIgnoreCase("(RPP) Penyedia")
					|| userRole.equalsIgnoreCase("(RPP) Penyemak")
					|| userRole.equalsIgnoreCase("(RPP) Pelulus")
					|| userRole.equalsIgnoreCase("(RPP) Penyelia")) {
				String daftarkanPenginapId = getParam("penginap");
				if (daftarkanPenginapId != null
						&& !daftarkanPenginapId.equalsIgnoreCase("")) {
					idPemohon = daftarkanPenginapId;
				}
			}
		}

		return idPemohon;
	}

	@SuppressWarnings("unchecked")
	@Command("openPopupSenaraiSelenggara")
	public String openPopupSenaraiSelenggara() throws Exception {

		String today = Util.getCurrentDate("yyyy-MM-dd");

		try {
			mp = new MyPersistence();

			JenisUnitRPP jenisUnit = (JenisUnitRPP) mp.find(JenisUnitRPP.class,
					getParam("idJenisUnit"));

			List<RppSelenggara> list = null;
			if (jenisUnit != null) {
				list = mp
						.list("select x from RppSelenggara x where x.id in "
								+ " (select y.rppSelenggara.id from RppSelenggaraUnitLokasi y where y.rppPeranginan.id = '"
								+ jenisUnit.getPeranginan().getId() + "' ) "
								+ " and x.tarikhMula >= '" + today + "' "
								+ " order by x.tarikhMula asc"); // tarikh sysdate dan kedepan dan by rpp
			}

			context.put("listSelenggara", list);
			context.put("jenisUnit", jenisUnit);

		} catch (Exception ex) {
			System.out.println("ERROR openPopupSenaraiSelenggara : "
					+ ex.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return getPath() + "/popup/senaraiSelenggara.vm";
	}

	/**
	 * MAKLUMAT BAYARAN DISPLAY UNTUK PENYELIA SHJ
	 * */
	@Command("maklumatBayaran")
	public String maklumatBayaran() {

		userRole = (String) request.getSession().getAttribute("_portal_role");

		try {
			mp = new MyPersistence();

			String idPermohonan = getParam("idPermohonan");
			RppPermohonan r = (RppPermohonan) mp.find(RppPermohonan.class,
					idPermohonan);

			if (!userRole.equalsIgnoreCase("(RPP) Penyelia")) {
				context.put("skrinBayaranPenyelia", false);
				context.put("r", r);
				return getPath() + "/notis.vm";
				/** STOP OPERATION RETURN NOTICE. **/
			}

			context.put("r", r);
			context.put("selectedTab", "2");
			context.put("listTempahanDanBayaran",
					UtilRpp.getListTempahanDanBayaran(mp, r));
			context.put("enabledEditDate", true);

		} catch (Exception e) {
			System.out.println("Error maklumatBayaran : " + e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return getPath() + "/maklumatBayaran.vm";
	}

	@Command("pilihCaraBayaran")
	public String pilihCaraBayaran() {
		try {
			mp = new MyPersistence();

			String flagJenisBayaran = getParam("flagJenisBayaran");
			context.put("flagJenisBayaran", flagJenisBayaran);

		} catch (Exception e) {
			System.out.println("Error pilihCaraBayaran : " + e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return getPath() + "/noResit.vm";
	}

	@Command("simpanBayaran")
	public String simpanBayaran() {
		try {
			mp = new MyPersistence();

			String idPermohonan = getParam("idPermohonan");
			RppPermohonan r = (RppPermohonan) mp.find(RppPermohonan.class,
					idPermohonan);

			String flagJenisBayaran = getParam("flagJenisBayaran");
			String noResitSewaDeposit = getParam("noResitSewaDeposit");
			Date tarikhBayaran = getDate("tarikhBayaran");

			mp.begin();

			UtilRpp.createPengurusanBilik(r);
			UtilRpp.createWalkinResitSenaraiInvoisAndUpdateLejar(mp, r, userId,
					flagJenisBayaran, noResitSewaDeposit, tarikhBayaran);

			r.setStatus((Status) mp.find(Status.class, "1425259713421")); // daftar
																			// masuk

			mp.commit();

			context.put("r", r);
			context.put("flagJenisBayaran", flagJenisBayaran);

		} catch (Exception e) {
			System.out.println("Error simpanBayaran : " + e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return maklumatBayaran();
	}

	@Command("getRangeTarikhHingga")
	public String getRangeTarikhHingga() throws Exception {

		Date dateRangeFrom = getDate("dateRangeFrom");

		try {
			mp = new MyPersistence();

			RppTetapanBukaTempahan tetapanBookingDate = (RppTetapanBukaTempahan) mp
					.get("select x from RppTetapanBukaTempahan x where x.flagAktif = 'Y' ");
			String dtto = Util.getDateTime(
					tetapanBookingDate.getTarikhMenginapHingga(), "dd-MM-yyyy");

			String dtfrom = Util.getDateTime(dateRangeFrom, "dd-MM-yyyy");

			Calendar cal = Calendar.getInstance();
			cal.setTime(dateRangeFrom);
			cal.add(Calendar.DATE, 14); // open next 30days
			String strDateRangeTo = Util.getDateTime(cal.getTime(),
					"dd-MM-yyyy");

			if (cal.getTime().before(
					tetapanBookingDate.getTarikhMenginapHingga())) {
				dtto = strDateRangeTo;
			}

			String rangeDirection = "['" + dtfrom + "','" + dtto + "']";
			context.put("rangeDirection", rangeDirection);

		} catch (Exception e) {
			System.out
					.println("Error getRangeTarikhHingga : " + e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return getPath() + "/popup/divRangeTarikhHingga.vm";
	}

	// ADD BY PEJE - TEKAN BUTANG BAYAR, CHECK DULU SAMAADA PAYMENT DAH DIBUAT ATAU BELUM
	private Boolean reCheckPaymentStatus(MyPersistence mp, String idPermohonan) {
		boolean bool = true;
		
		try {
			//REQUERY BASED ON NO RESPONSE DURING TRANSACTION.
			requeryNoResponseTransaction(mp, idPermohonan);
			
			//REQUERY BASED ON DEBITAUTHCODE = 09 (TRANSACTION PENDING).
			requeryPendingTransaction(mp, idPermohonan);
			
			//QUERY DATA FPXRECORDS - CHECK PAYMENT SUCCESS(00) / PENDING(09)
			FPXRecords fpxRecords = (FPXRecords) mp.get("select x from FPXRecords x where x.debitAuthCode in ('00', '09') and x.sellerOrderNo = '" + idPermohonan + "' order by x.id asc");
			if (fpxRecords != null) {
				bool = false;
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return bool;
	}

	private void requeryNoResponseTransaction(MyPersistence mp, String idPermohonan) {
		FPXUtil fpxUtil = new FPXUtil();
		try {
			int loopRequery = 0;			
			while (loopRequery < 3) {
				List<FPXRecordsRequest> listFPXRecordRequest = mp.list("select x from FPXRecordsRequest x where (x.fpxTxnId is null or x.fpxTxnId = '') and x.sellerOrderNo = '" + idPermohonan + "' order by x.id asc");
				if (listFPXRecordRequest.size() > 0) {
					for (FPXRecordsRequest fpxRecordRequest : listFPXRecordRequest) {
						FPXRecords fpxMyClear = fpxUtil.reQueryFPX(fpxRecordRequest.getSellerOrderNo(), fpxRecordRequest.getSellerExOrderNo(), fpxRecordRequest.getTxnAmount());
						mp.begin();
						if (fpxMyClear != null) {	
							boolean addRecord = false;
							FPXRecords fpxRecords = (FPXRecords) mp.find(FPXRecords.class, fpxMyClear.getId());
							if (fpxRecords == null) {
								fpxRecords = new FPXRecords();
								fpxRecords.setId(fpxMyClear.getId());
								addRecord = true;
							}
							fpxRecords.setBuyerBankBranch(fpxMyClear.getBuyerBankBranch());
							fpxRecords.setBuyerBankId(fpxMyClear.getBuyerBankId());
							fpxRecords.setBuyerIban(fpxMyClear.getBuyerIban());
							fpxRecords.setBuyerId(fpxMyClear.getBuyerId());
							fpxRecords.setBuyerName(fpxMyClear.getBuyerName());
							fpxRecords.setCreditAuthCode(fpxMyClear.getCreditAuthCode());
							fpxRecords.setCreditAuthNo(fpxMyClear.getCreditAuthNo());
							fpxRecords.setDebitAuthCode(fpxMyClear.getDebitAuthCode());
							fpxRecords.setDebitAuthNo(fpxMyClear.getDebitAuthNo());
							fpxRecords.setFpxTxnTime(fpxMyClear.getFpxTxnTime());
							fpxRecords.setMakerName(fpxMyClear.getMakerName());
							fpxRecords.setMsgToken(fpxMyClear.getMsgToken());
							fpxRecords.setMsgType(fpxMyClear.getMsgType());
							fpxRecords.setSellerExId(fpxMyClear.getSellerExId());
							fpxRecords.setSellerExOrderNo(fpxMyClear.getSellerExOrderNo());
							fpxRecords.setSellerId(fpxMyClear.getSellerId());
							fpxRecords.setSellerOrderNo(fpxMyClear.getSellerOrderNo());
							fpxRecords.setSellerTxnTime(fpxMyClear.getSellerTxnTime());
							fpxRecords.setTxnAmount(fpxMyClear.getTxnAmount());
							fpxRecords.setTxnCurrency(fpxMyClear.getTxnCurrency());
							if (addRecord) {
								mp.persist(fpxRecords);							
							}
							fpxRecordRequest.setFpxTxnId(fpxMyClear.getId());
							fpxRecordRequest.setRespondDate(new Date());
						} else {
							if (fpxUtil.isSuccessRequery()) {
								int daysBetween = Util.daysBetween(fpxRecordRequest.getRequestDate(), new Date());
								if (daysBetween >= 2) {
									mp.remove(fpxRecordRequest);
								}								
							}
						}
						mp.commit();
					}
					loopRequery++;
				} else {
					break;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}		
	}
	
	private void requeryPendingTransaction(MyPersistence mp, String idPermohonan) {
		FPXUtil fpxUtil = new FPXUtil();
		try {
			int loopRequery = 0;			
			while (loopRequery < 3) {
				List<FPXRecords> listFPXRecords = mp.list("select x from FPXRecords x where x.debitAuthCode = '09' and x.sellerOrderNo = '" + idPermohonan + "' order by x.id asc");
				if (listFPXRecords.size() > 0) {
					for (FPXRecords fpxRecords : listFPXRecords) {
						FPXRecords fpxMyClear = fpxUtil.reQueryFPX(fpxRecords.getSellerOrderNo(), fpxRecords.getSellerExOrderNo(), fpxRecords.getTxnAmount());
						mp.begin();
						if (fpxMyClear != null) {	
							boolean addRecord = false;
							FPXRecords newFpxRecords= (FPXRecords) mp.find(FPXRecords.class, fpxMyClear.getId());
							if (newFpxRecords == null) {
								newFpxRecords = new FPXRecords();
								newFpxRecords.setId(fpxMyClear.getId());
								addRecord = true;
							}
							newFpxRecords.setBuyerBankBranch(fpxMyClear.getBuyerBankBranch());
							newFpxRecords.setBuyerBankId(fpxMyClear.getBuyerBankId());
							newFpxRecords.setBuyerIban(fpxMyClear.getBuyerIban());
							newFpxRecords.setBuyerId(fpxMyClear.getBuyerId());
							newFpxRecords.setBuyerName(fpxMyClear.getBuyerName());
							newFpxRecords.setCreditAuthCode(fpxMyClear.getCreditAuthCode());
							newFpxRecords.setCreditAuthNo(fpxMyClear.getCreditAuthNo());
							newFpxRecords.setDebitAuthCode(fpxMyClear.getDebitAuthCode());
							newFpxRecords.setDebitAuthNo(fpxMyClear.getDebitAuthNo());
							newFpxRecords.setFpxTxnTime(fpxMyClear.getFpxTxnTime());
							newFpxRecords.setMakerName(fpxMyClear.getMakerName());
							newFpxRecords.setMsgToken(fpxMyClear.getMsgToken());
							newFpxRecords.setMsgType(fpxMyClear.getMsgType());
							newFpxRecords.setSellerExId(fpxMyClear.getSellerExId());
							newFpxRecords.setSellerExOrderNo(fpxMyClear.getSellerExOrderNo());
							newFpxRecords.setSellerId(fpxMyClear.getSellerId());
							newFpxRecords.setSellerOrderNo(fpxMyClear.getSellerOrderNo());
							newFpxRecords.setSellerTxnTime(fpxMyClear.getSellerTxnTime());
							newFpxRecords.setTxnAmount(fpxMyClear.getTxnAmount());
							newFpxRecords.setTxnCurrency(fpxMyClear.getTxnCurrency());
							if (addRecord) {
								mp.persist(newFpxRecords);	
								mp.remove(fpxRecords);
							}
						} else {
							if (fpxUtil.isSuccessRequery()) {
								mp.remove(fpxRecords);
							}
						}
						mp.commit();
					}
					loopRequery++;
				} else {
					break;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}		
	}
}
