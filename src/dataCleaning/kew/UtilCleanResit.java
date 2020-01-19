package dataCleaning.kew;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lebah.db.Db;
import lebah.template.DbPersistence;
import lebah.template.UID;

import org.apache.commons.lang.time.DateUtils;

import portal.module.entity.Users;
import bph.entities.integrasi.FPXRecords;
import bph.entities.kew.KewTuntutanDeposit;
import bph.entities.kewangan.KewBayaranResit;
import bph.entities.kewangan.KewDeposit;
import bph.entities.kewangan.KewInvois;
import bph.entities.kewangan.KewResitKaedahBayaran;
import bph.entities.kewangan.KewResitSenaraiInvois;
import bph.entities.kewangan.SeqNoResit;
import bph.entities.kod.CaraBayar;
import bph.entities.kod.JenisUnitRPP;
import bph.entities.rpp.RppAkaun;
import bph.entities.rpp.RppPermohonan;
import bph.entities.rpp.RppRekodTempahanLondon;
import bph.entities.rpp.RppTetapanCajTambahan;
import bph.entities.utiliti.UtilAkaun;
import bph.entities.utiliti.UtilPermohonan;
import bph.utils.UtilKewangan;

public class UtilCleanResit {

	public static void createRecordBayaran(DbPersistence mp, String pemohonId,
			RppPermohonan r) {
		int bilUnit = r.getKuantiti();

		int bilTambahanDewasa = 0;
		int bilTambahanKanakKanak = 0;
		if (!r.getRppPeranginan().getId().equalsIgnoreCase("3")
				&& !r.getRppPeranginan().getId().equalsIgnoreCase("14")) {
			bilTambahanDewasa = r.getBilTambahanDewasa() != null ? r
					.getBilTambahanDewasa() : 0;
			bilTambahanKanakKanak = r.getBilTambahanKanakKanak() != null ? r
					.getBilTambahanKanakKanak() : 0;
		}

		String dtIn = new SimpleDateFormat("yyyy-MM-dd").format(r
				.getTarikhMasukRpp());
		String dtOut = new SimpleDateFormat("yyyy-MM-dd").format(r
				.getTarikhKeluarRpp());
		Double kadarSewa = UtilCleanResit.kadarSewaBiasaAtauWaktuPuncak(
				r.getJenisUnitRpp(), dtIn, dtOut);

		/** TOTAL */
		Double debitRpp = (kadarSewa * bilUnit * r.getTotalBilMalam());

		try {

			/** LEJAR TEMPAHAN */
			saveLejarTempahanInSql(pemohonId, r, kadarSewa, dtIn, bilUnit,
					debitRpp);

			/** LANGKAWI DAN PD TAKPERLU BAYAR DEPOSIT **/
			if (!r.getRppPeranginan().getId().equalsIgnoreCase("3")
					&& !r.getRppPeranginan().getId().equalsIgnoreCase("14")) {
				saveLejarDepositInSql(mp, pemohonId, r);
			}

			/** LEJAR TAMBAHAN DEWASA */
			if (bilTambahanDewasa > 0) {
				saveLejarTambahanDewasaInSql(mp, pemohonId, r,
						bilTambahanDewasa);
			}

			/** LEJAR TAMBAHAN KANAK - KANAK */
			if (bilTambahanKanakKanak > 0) {
				saveLejarTambahanKanakKanakInSql(mp, pemohonId, r,
						bilTambahanKanakKanak);
			}

			/** LEJAR (SEWA BOT) JIKA DI TASIK KENYIR. */
			if (r.getRppPeranginan().getId().equalsIgnoreCase("4")) {
				saveLejarSewaBotInSql(pemohonId, r);
			}

		} catch (Exception e1) {
			System.out.println("::ERROR Save Lejar : " + e1.getMessage());
		}

		try {

			/** TIDAK MELIBATKAN EKSEKUTIF PD DAN LANGKAWI */
			if (!r.getRppPeranginan().getId().equalsIgnoreCase("3")
					&& !r.getRppPeranginan().getId().equalsIgnoreCase("14")) {
				saveInvoisDepositInSql(mp, pemohonId, r);
			}

		} catch (Exception e2) {
			System.out.println("::ERROR SAVE KEWINVOIS / KEWDEPOSIT : "
					+ e2.getMessage());
		}
	}

	public static Double kadarSewaBiasaAtauWaktuPuncak(JenisUnitRPP obj,
			String tarikhMasuk, String tarikhKeluar) {
		Double kadarsewa = 0d;
		boolean waktupuncak = false;

		try {
			waktupuncak = checkWaktuPuncak(tarikhMasuk);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}

		if (waktupuncak) {
			kadarsewa = obj.getKadarSewaWaktuPuncak();
		} else {
			kadarsewa = obj.getKadarSewa();
		}
		return kadarsewa;
	}

	public static void saveLejarTempahanInSql(String pemohonId,
			RppPermohonan r, Double kadarSewa, String dtIn, int bilUnit,
			Double debit) throws Exception {

		String id = UID.getUID();
		String kodHasil = "74299";
		String permohonanId = r.getId();
		String noTempahan = r.getNoTempahan();
		String keterangan = ("SEWA " + r.getTotalBilMalam() + " MALAM DI  "
				+ r.getJenisUnitRpp().getKeterangan() + ", " + r
				.getRppPeranginan().getNamaPeranginan().toUpperCase());
		String tarikhMohon = new SimpleDateFormat("yyyy-MM-dd").format(r
				.getTarikhPermohonan());

		String catatanWP = "";
		if (UtilCleanResit.checkWaktuPuncak(dtIn)) {
			catatanWP = "WAKTU PUNCAK";
		}

		String sql = "INSERT INTO `rpp_akaun` (`id`, `id_permohonan`, `id_kod_hasil`, `no_invois`, `tarikh_invois`, `keterangan`, "
				+ " `catatan`, `amaun_bayaran_seunit`, `bilangan_unit`, `debit`, `kredit`, `flag_void`, `flag_bayar`, "
				+ " `id_masuk`, `id_kemaskini`, `tarikh_masuk`, `tarikh_kemaskini` ) "
				+ " VALUES " + " ('"
				+ id
				+ "', '"
				+ permohonanId
				+ "', '"
				+ kodHasil
				+ "', '"
				+ noTempahan
				+ "', '"
				+ tarikhMohon
				+ "', '"
				+ keterangan
				+ "', "
				+ " '"
				+ catatanWP
				+ "', "
				+ kadarSewa
				+ ", "
				+ bilUnit
				+ ", "
				+ debit
				+ ", 0.00, 'T', 'T', "
				+ " '"
				+ pemohonId
				+ "', '"
				+ pemohonId
				+ "', '"
				+ tarikhMohon
				+ "', '"
				+ tarikhMohon + "') ";

		Db database = new Db();
		try {
			Statement stmt = database.getStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException ex) {
			System.out.println(":: ERROR saveLejarTempahanInSql : "
					+ ex.getMessage());
		} finally {
			if (database != null)
				database.close();
		}

	}

	/**
	 * CHECK BALIK DENGAN USER WAKTU PUNCAK MELIBATKAN WAKTU MASUK SAHAJA ATAU
	 * TIDAK
	 */
	public static boolean checkWaktuPuncak(String tarikhMasuk) {

		boolean waktupuncak = false;
		Db db1 = null;
		String sql = "";

		try {

			db1 = new Db();

			sql = "select * from ruj_cuti where '" + tarikhMasuk
					+ "' between tarikh_dari and tarikh_hingga ";

			ResultSet rs = db1.getStatement().executeQuery(sql);

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat fday = new SimpleDateFormat("EEEE");
			Date date = formatter.parse(tarikhMasuk);
			String hari = fday.format(date);

			if (rs.next() || hari.equalsIgnoreCase("saturday")
					|| hari.equalsIgnoreCase("sunday")) {
				waktupuncak = true;
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			if (db1 != null)
				db1.close();
		}

		return waktupuncak;
	}

	public static void saveLejarDepositInSql(DbPersistence mp,
			String pemohonId, RppPermohonan r) throws Exception {

		String id = UID.getUID();
		String kodHasil = "72311"; // KOD HASIL DEPOSIT
		String permohonanId = r.getId();
		String noTempahan = r.getNoTempahan();
		String keterangan = ("DEPOSIT SEWA "
				+ r.getJenisUnitRpp().getKeterangan() + ", " + r
				.getRppPeranginan().getNamaPeranginan().toUpperCase());
		String tarikhMohon = new SimpleDateFormat("yyyy-MM-dd").format(r
				.getTarikhPermohonan());

		RppTetapanCajTambahan objdep = (RppTetapanCajTambahan) mp.find(
				RppTetapanCajTambahan.class, "1432887883848");
		Double deposit = objdep != null ? objdep.getCajBayaran() : 0d;
		Double debitdep = (deposit * 1);

		String sql = "INSERT INTO `rpp_akaun` (`id`, `id_permohonan`, `id_kod_hasil`, `no_invois`, `tarikh_invois`, `keterangan`, "
				+ " `amaun_bayaran_seunit`, `bilangan_unit`, `debit`, `kredit`, `flag_void`, `flag_bayar`, "
				+ " `id_masuk`, `id_kemaskini`, `tarikh_masuk`, `tarikh_kemaskini` ) "
				+ " VALUES " + " ('"
				+ id
				+ "', '"
				+ permohonanId
				+ "', '"
				+ kodHasil
				+ "', '"
				+ noTempahan
				+ "', '"
				+ tarikhMohon
				+ "', '"
				+ keterangan
				+ "', "
				+ " "
				+ deposit
				+ ", 1, "
				+ debitdep
				+ ", 0.00, 'T', 'T', "
				+ " '"
				+ pemohonId
				+ "', '"
				+ pemohonId
				+ "', '" + tarikhMohon + "', '" + tarikhMohon + "') ";

		Db database = new Db();
		try {
			Statement stmt = database.getStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException ex) {
			System.out.println(":: ERROR saveLejarDepositInSql : "
					+ ex.getMessage());
		} finally {
			if (database != null)
				database.close();
		}

	}

	public static void saveLejarTambahanDewasaInSql(DbPersistence mp,
			String pemohonId, RppPermohonan r, int bilTambahanDewasa)
			throws Exception {

		String id = UID.getUID();
		String kodHasil = "74299";
		String permohonanId = r.getId();
		String noTempahan = r.getNoTempahan();
		String keterangan = "TAMBAHAN DEWASA";
		String tarikhMohon = new SimpleDateFormat("yyyy-MM-dd").format(r
				.getTarikhPermohonan());

		RppTetapanCajTambahan extbed = (RppTetapanCajTambahan) mp.find(
				RppTetapanCajTambahan.class, "1432867359415");
		Double extbedprice = extbed != null ? extbed.getCajBayaran() : 0d;
		Double akdebit = (extbedprice * bilTambahanDewasa); // total harga
															// seunit * bil
															// tambahan dewasa

		String sql = "INSERT INTO `rpp_akaun` (`id`, `id_permohonan`, `id_kod_hasil`, `no_invois`, `tarikh_invois`, `keterangan`, "
				+ " `amaun_bayaran_seunit`, `bilangan_unit`, `debit`, `kredit`, `flag_void`, `flag_bayar`, "
				+ " `id_masuk`, `id_kemaskini`, `tarikh_masuk`, `tarikh_kemaskini` ) "
				+ " VALUES " + " ('"
				+ id
				+ "', '"
				+ permohonanId
				+ "', '"
				+ kodHasil
				+ "', '"
				+ noTempahan
				+ "', '"
				+ tarikhMohon
				+ "', '"
				+ keterangan
				+ "', "
				+ " "
				+ extbedprice
				+ ", "
				+ bilTambahanDewasa
				+ ", "
				+ akdebit
				+ ", 0.00, 'T', 'T', "
				+ " '"
				+ pemohonId
				+ "', '"
				+ pemohonId
				+ "', '"
				+ tarikhMohon
				+ "', '"
				+ tarikhMohon
				+ "') ";

		Db database = new Db();
		try {
			Statement stmt = database.getStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException ex) {
			System.out.println(":: ERROR saveLejarTambahanDewasaInSql : "
					+ ex.getMessage());
		} finally {
			if (database != null)
				database.close();
		}

	}

	public static void saveLejarTambahanKanakKanakInSql(DbPersistence mp,
			String pemohonId, RppPermohonan r, int bilTambahanKanakKanak)
			throws Exception {

		String id = UID.getUID();
		String kodHasil = "74299";
		String permohonanId = r.getId();
		String noTempahan = r.getNoTempahan();
		String keterangan = "TAMBAHAN KANAK - KANAK";
		String tarikhMohon = new SimpleDateFormat("yyyy-MM-dd").format(r
				.getTarikhPermohonan());

		RppTetapanCajTambahan extbed = (RppTetapanCajTambahan) mp.find(
				RppTetapanCajTambahan.class, "1436755298337");
		Double extbedprice = extbed != null ? extbed.getCajBayaran() : 0d;
		Double akdebit = (extbedprice * bilTambahanKanakKanak); // total harga
																// seunit * bil
																// tambahan
																// kanak kanak

		String sql = "INSERT INTO `rpp_akaun` (`id`, `id_permohonan`, `id_kod_hasil`, `no_invois`, `tarikh_invois`, `keterangan`, "
				+ " `amaun_bayaran_seunit`, `bilangan_unit`, `debit`, `kredit`, `flag_void`, `flag_bayar`, "
				+ " `id_masuk`, `id_kemaskini`, `tarikh_masuk`, `tarikh_kemaskini` ) "
				+ " VALUES " + " ('"
				+ id
				+ "', '"
				+ permohonanId
				+ "', '"
				+ kodHasil
				+ "', '"
				+ noTempahan
				+ "', '"
				+ tarikhMohon
				+ "', '"
				+ keterangan
				+ "', "
				+ " "
				+ extbedprice
				+ ", "
				+ bilTambahanKanakKanak
				+ ", "
				+ akdebit
				+ ", 0.00, 'T', 'T', "
				+ " '"
				+ pemohonId
				+ "', '"
				+ pemohonId
				+ "', '"
				+ tarikhMohon
				+ "', '"
				+ tarikhMohon + "') ";

		Db database = new Db();
		try {
			Statement stmt = database.getStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException ex) {
			System.out.println(":: ERROR saveLejarTambahanKanakKanakInSql : "
					+ ex.getMessage());
		} finally {
			if (database != null)
				database.close();
		}

	}

	public static void saveLejarSewaBotInSql(String pemohonId, RppPermohonan r)
			throws Exception {

		String id = UID.getUID();
		String kodHasil = "74304";
		String permohonanId = r.getId();
		String noTempahan = r.getNoTempahan();
		String keterangan = "SEWA BOT PERGI DAN BALIK";
		String tarikhMohon = new SimpleDateFormat("yyyy-MM-dd").format(r
				.getTarikhPermohonan());

		int bilBotDewasa = ((r.getBilDewasa() != null ? r.getBilDewasa() : 0) + (r
				.getBilTambahanDewasa() != null ? r.getBilTambahanDewasa() : 0));
		int bilBotKanakKanak = ((r.getBilKanakKanak() != null ? r
				.getBilKanakKanak() : 0) + (r.getBilTambahanKanakKanak() != null ? r
				.getBilTambahanKanakKanak() : 0));
		int totalHead = (bilBotDewasa + bilBotKanakKanak);

		Double totalHargaDewasa = (bilBotDewasa * 10.00);
		Double totalHargaKanakKanak = (bilBotKanakKanak * 5.00);
		Double totalHargaSewaBot = (totalHargaDewasa + totalHargaKanakKanak);

		if (totalHead <= 3) {
			totalHargaSewaBot = 30.00;
		}

		String sql = "INSERT INTO `rpp_akaun` (`id`, `id_permohonan`, `id_kod_hasil`, `no_invois`, `tarikh_invois`, `keterangan`, "
				+ " `amaun_bayaran_seunit`, `bilangan_unit`, `debit`, `kredit`, `flag_void`, `flag_bayar`, "
				+ " `id_masuk`, `id_kemaskini`, `tarikh_masuk`, `tarikh_kemaskini` ) "
				+ " VALUES " + " ('"
				+ id
				+ "', '"
				+ permohonanId
				+ "', '"
				+ kodHasil
				+ "', '"
				+ noTempahan
				+ "', '"
				+ tarikhMohon
				+ "', '"
				+ keterangan
				+ "', "
				+ " 0.00, "
				+ totalHead
				+ ", "
				+ totalHargaSewaBot
				+ ", 0.00, 'T', 'T', "
				+ " '"
				+ pemohonId
				+ "', '"
				+ pemohonId
				+ "', '" + tarikhMohon + "', '" + tarikhMohon + "') ";

		Db database = new Db();
		try {
			Statement stmt = database.getStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException ex) {
			System.out.println(":: ERROR saveLejarSewaBotInSql : "
					+ ex.getMessage());
		} finally {
			if (database != null)
				database.close();
		}

	}

	@SuppressWarnings("unchecked")
	public static void saveInvoisDepositInSql(DbPersistence mp,
			String pemohonId, RppPermohonan r) throws Exception {

		List<RppAkaun> listAkaun = mp
				.list("select x from RppAkaun x where x.permohonan.id = '"
						+ r.getId() + "' ");
		for (int i = 0; i < listAkaun.size(); i++) {
			String kodHasil = listAkaun.get(i).getKodHasil() != null ? listAkaun
					.get(i).getKodHasil().getKod()
					: "";
			if (kodHasil.equalsIgnoreCase("72311")) { // deposit
				createDepositInFinance(listAkaun.get(i));
			} else {
				createInvoisInFinance(listAkaun.get(i), r);
			}
		}

	}

	public static void createDepositInFinance(RppAkaun ak) throws Exception {

		String id = UID.getUID();
		String kodHasil = "72311"; // DEPOSIT
		String lejarId = ak.getId();
		String pemohonId = ak.getPermohonan().getPemohon().getId();
		String keterangan = ak.getKeterangan().toUpperCase();
		Double amaunDeposit = ak.getDebit() != null ? ak.getDebit() : 0d;
		String dtIn = new SimpleDateFormat("yyyy-MM-dd").format(ak
				.getPermohonan().getTarikhMasukRpp());
		String dtOut = new SimpleDateFormat("yyyy-MM-dd").format(ak
				.getPermohonan().getTarikhKeluarRpp());
		String noInvois = ak.getNoInvois();
		String tarikhMohon = new SimpleDateFormat("yyyy-MM-dd").format(ak
				.getPermohonan().getTarikhPermohonan());

		String sql = " INSERT INTO `kew_deposit` (`id`, `id_kod_hasil`, `id_lejar`, `id_jenis_bayaran`, `id_pendeposit`, "
				+ " `keterangan_deposit`, `tarikh_deposit`, `flag_pulang_deposit`, "
				+ " `jumlah_deposit`, `baki_deposit`,"
				+ " `flag_warta`, `flag_bayar`, `flag_queue`, `tarikh_dari`, `tarikh_hingga`, `no_invois`) "
				+ " VALUES "
				+ " ('"
				+ id
				+ "', '"
				+ kodHasil
				+ "', '"
				+ lejarId
				+ "', '02', '"
				+ pemohonId
				+ "', "
				+ " '"
				+ keterangan
				+ "', '"
				+ tarikhMohon
				+ "', 'T', "
				+ amaunDeposit
				+ ", 0.00, 'T', 'T', 'T', '"
				+ dtIn
				+ "', '"
				+ dtOut
				+ "', '"
				+ noInvois + "') ";

		Db database = new Db();
		try {
			Statement stmt = database.getStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException ex) {
			System.out.println(":: ERROR createDepositInFinance : "
					+ ex.getMessage());
		} finally {
			if (database != null)
				database.close();
		}

	}

	public static void createInvoisInFinance(RppAkaun ak, RppPermohonan r)
			throws Exception {

		String id = UID.getUID();
		String noInvois = ak.getNoInvois();
		String lejarId = ak.getId();
		String pemohonId = ak.getPermohonan().getPemohon().getId();
		String keterangan = ak.getKeterangan().toUpperCase();
		Double debit = ak.getDebit();
		String dtIn = new SimpleDateFormat("yyyy-MM-dd").format(r
				.getTarikhMasukRpp());
		String dtOut = new SimpleDateFormat("yyyy-MM-dd").format(r
				.getTarikhKeluarRpp());
		String tarikhMohon = new SimpleDateFormat("yyyy-MM-dd").format(ak
				.getPermohonan().getTarikhPermohonan());

		String sql = " INSERT INTO `kew_invois` (`id`, `id_kod_hasil`, `flag_bayaran`, `no_invois`, `tarikh_invois`, `no_rujukan`, `id_lejar`, `id_pembayar`,"
				+ " `id_jenis_bayaran`, `keterangan_bayaran`, `debit`, `kredit`, `flag_bayar`, `flag_queue`, `id_pendaftar`, "
				+ " `tarikh_daftar`, `tarikh_dari`, `tarikh_hingga` ) "
				+ " VALUES " + " ('"
				+ id
				+ "', '"
				+ ak.getKodHasil().getId()
				+ "', 'SEWA', '"
				+ noInvois
				+ "', '"
				+ tarikhMohon
				+ "', '"
				+ noInvois
				+ "', '"
				+ lejarId
				+ "', '"
				+ pemohonId
				+ "', "
				+ " '02', '"
				+ keterangan
				+ "', "
				+ debit
				+ ", 0.00, 'T', 'T', '"
				+ pemohonId
				+ "', '"
				+ tarikhMohon
				+ "', '"
				+ dtIn + "', '" + dtOut + "') ";

		Db database = new Db();
		try {
			Statement stmt = database.getStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException ex) {
			System.out.println(":: ERROR createInvoisInFinance : "
					+ ex.getMessage());
		} finally {
			if (database != null)
				database.close();
		}

	}

	private synchronized static String generateReceiptNo(Date tarikhBayaran, DbPersistence mp) {

		String receiptNo = "";
		Calendar tarikh = Calendar.getInstance();
		tarikh.setTime(tarikhBayaran);
		
		Integer today = tarikh.get(Calendar.DATE);
		Integer month = tarikh.get(Calendar.MONTH) + 1;
		Integer year = tarikh.get(Calendar.YEAR);

		String date = new DecimalFormat("00").format(today) + new DecimalFormat("00").format(month) + new DecimalFormat("0000").format(year);
		String defaultNoKodJuruwang = "09";// online		
		int runningNo = 0;
		SeqNoResit sq = (SeqNoResit) mp
				.get("select x from SeqNoResit x where x.day = '" + today
						+ "' and x.month = '" + month + "' and x.year = '"
						+ year + "' and x.kodJuruwang is null");

		if (sq != null) {
			mp.pesismisticLock(sq);
			runningNo = sq.getBil() + 1;
			sq.setBil(runningNo);
			sq = (SeqNoResit) mp.merge(sq);
		} else {
			runningNo = 1;
			sq = new SeqNoResit();
			sq.setDay(today);
			sq.setMonth(month);
			sq.setYear(year);
			sq.setBil(1);
			mp.persist(sq);
			mp.flush();
		}
		// format : DD.MM.YYYY.<2 digit pusat penerimaan>.<kod_juruwang>.<turutan nombor(5 digit)>.
		String seq = new DecimalFormat("00000").format(runningNo);
		receiptNo = date + "" + defaultNoKodJuruwang + seq;

		return receiptNo;
	}

	public static void generateResitRPP(RppPermohonan r, Date tarikhBayaran, boolean flagCombineResit, FPXRecords fpxRecords, boolean duplicateResit, DbPersistence mp) {
		String statusPermohonanLulus = "1425259713415";
		KewBayaranResit rstSewa = null;
		/** RESIT SEWA * */
		Double kadarsewa = getAmaunTotalSewaRpWithoutDeposit(r, mp) != null ? getAmaunTotalSewaRpWithoutDeposit(r, mp) : 0d;
		Double kadardeposit = getAmaunDeposit(r, mp) != null ? getAmaunDeposit(r, mp) : 0d;
		Double totalAmaun = getAmaunTotalSewaRpWithDeposit(r, mp) != null ? getAmaunTotalSewaRpWithDeposit(r, mp) : 0d;
		if (duplicateResit) {
			rstSewa = new KewBayaranResit();
		} else {
			rstSewa = r.getResitSewa();
		}
		
		Users pembayar = r.getPemohon();
		if (pembayar != null) {
			rstSewa.setPembayar(pembayar);
			rstSewa.setNoPengenalanPembayar(pembayar.getId());
			rstSewa.setNamaPembayar(pembayar.getUserName());				
			rstSewa.setAlamatPembayar(UtilKewangan.getAlamatPembayar(pembayar));
		}
		if (duplicateResit) {
			if (rstSewa.getNoResitLama() == null){
				rstSewa.setNoResitLama(rstSewa.getNoResit());
			}
			rstSewa.setNoResit(generateReceiptNo(tarikhBayaran, mp));
		} else {
			if (isValidNoResit(rstSewa, tarikhBayaran)) {
				//DO NOTHING
			} else {
				if (rstSewa.getNoResitLama() == null){
					rstSewa.setNoResitLama(rstSewa.getNoResit());
				}
				rstSewa.setNoResit(generateReceiptNo(tarikhBayaran, mp));
			}
		}
		rstSewa.setTarikhBayaran(tarikhBayaran);
		rstSewa.setTarikhResit(tarikhBayaran);
		rstSewa.setFlagJenisBayaran("ONLINE");
		rstSewa.setTarikhDaftar(tarikhBayaran);
		rstSewa.setUserPendaftar(r.getPemohon());
		if (flagCombineResit) {
			rstSewa.setJumlahAmaunBayaran(totalAmaun);	
			rstSewa.setFlagJenisResit("3"); // INVOIS && DEPOSIT
		} else {
			rstSewa.setJumlahAmaunBayaran(kadarsewa);
			rstSewa.setFlagJenisResit("2"); // INVOIS
		}		
		rstSewa.setJuruwangKod("09");		
		rstSewa.setIdPermohonan(r.getId());
		rstSewa.setIdTransaksiBank(fpxRecords.getId());
		if (duplicateResit) {
			mp.persist(rstSewa);
		}

		KewResitKaedahBayaran kb = new KewResitKaedahBayaran();
		kb.setResit(rstSewa);
		if (flagCombineResit) {
			kb.setAmaunBayaran(totalAmaun);
		} else {
			kb.setAmaunBayaran(kadarsewa);
		}		
		kb.setModBayaran((CaraBayar) mp.find(CaraBayar.class, "FPX"));
		mp.persist(kb);

		KewBayaranResit rstDeposit = null;
		if (flagCombineResit) {
			rstDeposit = rstSewa;
		} else {
			/** RESIT DEPOSIT * */
			if (duplicateResit) {
				rstDeposit = new KewBayaranResit();
			} else {
				rstDeposit = r.getResitDeposit();
			}			
			if (pembayar != null) {
				rstDeposit.setPembayar(pembayar);
				rstDeposit.setNoPengenalanPembayar(pembayar.getId());
				rstDeposit.setNamaPembayar(pembayar.getUserName());				
				rstDeposit.setAlamatPembayar(UtilKewangan.getAlamatPembayar(pembayar));
			}
			if (duplicateResit) {
				if (rstDeposit.getNoResitLama() == null){
					rstDeposit.setNoResitLama(rstDeposit.getNoResit());
				}
				rstDeposit.setNoResit(generateReceiptNo(tarikhBayaran, mp));
			} else {
				if (isValidNoResit(rstDeposit, tarikhBayaran)) {
					//DO NOTHING
				} else {
					if (rstDeposit.getNoResitLama() == null){
						rstDeposit.setNoResitLama(rstDeposit.getNoResit());
					}
					rstDeposit.setNoResit(generateReceiptNo(tarikhBayaran, mp));
				}
			}
			
			rstDeposit.setTarikhBayaran(tarikhBayaran);
			rstDeposit.setTarikhResit(tarikhBayaran);
			rstDeposit.setFlagJenisBayaran("ONLINE");
			rstDeposit.setTarikhDaftar(tarikhBayaran);
			rstDeposit.setUserPendaftar(r.getPemohon());
			rstDeposit.setJumlahAmaunBayaran(kadardeposit);
			rstDeposit.setJuruwangKod("09");
			rstDeposit.setFlagJenisResit("1"); // DEPOSIT
			rstDeposit.setIdPermohonan(r.getId());
			rstDeposit.setIdTransaksiBank(fpxRecords.getId());
			if (duplicateResit) {
				mp.persist(rstDeposit);
			}
			
			KewResitKaedahBayaran kbdep = new KewResitKaedahBayaran();
			kbdep.setResit(rstDeposit);
			kbdep.setAmaunBayaran(kadardeposit);
			kbdep.setModBayaran((CaraBayar) mp.find(CaraBayar.class, "FPX"));
			mp.persist(kbdep);
		}				

		/** UPDATE RPPPERMOHONAN */
		r.setStatusBayaran("Y");
		r.setTarikhBayaran(tarikhBayaran);
//		r.setStatus((Status) mp.find(Status.class, statusPermohonanLulus));

		updateAndCreateMaklumatBayaran(r, rstSewa, rstDeposit, tarikhBayaran, duplicateResit, mp);
	}

	public static boolean isValidNoResit(KewBayaranResit resit, Date tarikhBayaran) {
		boolean bool = false;
		String day = resit.getNoResit().substring(0, 2);
		String month = resit.getNoResit().substring(2, 4);
		String year = resit.getNoResit().substring(4, 8);	
		Calendar tarikhResit = Calendar.getInstance();
		tarikhResit.set(Calendar.DATE, Integer.valueOf(day));
		tarikhResit.set(Calendar.MONTH, Integer.valueOf(month) - 1);
		tarikhResit.set(Calendar.YEAR, Integer.valueOf(year));

		if (DateUtils.isSameDay(tarikhBayaran, tarikhResit.getTime())) {
			if (resit.getNoResit().contains("P9999")) {
				bool = false;
			} else {
				bool = true;
			}
		}
		return bool;
	}

	public static void updateAndCreateMaklumatBayaran(RppPermohonan r,
			KewBayaranResit resitSewa, KewBayaranResit resitDeposit, Date tarikhBayaran,
			boolean duplicateResit, DbPersistence mp) {

		// update lejar, create record.
		List<RppAkaun> listAkaun = UtilCleanResit.getListTempahanDanBayaran(mp, r);
		for (int i = 0; i < listAkaun.size(); i++) {

			RppAkaun lj = listAkaun.get(i);
			if (!duplicateResit) {
				lj.setFlagBayar("Y");
				lj.setIdKemaskini(r.getPemohon());
				lj.setKredit(lj.getDebit());
				lj.setTarikhKemaskini(tarikhBayaran);
			}
			

			// create ref resit Invois/Deposit
			if (lj.getKodHasil().getId().equalsIgnoreCase("72311")) {
				// deposit
				KewDeposit dep = (KewDeposit) mp.get("select x from KewDeposit x where x.idLejar = '" + lj.getId() + "' ");
				if (dep != null) {
					KewResitSenaraiInvois rsi = (KewResitSenaraiInvois) mp.get("select x from KewResitSenaraiInvois x where x.deposit.id = '" + dep.getId() + "' ");
					if (rsi == null || duplicateResit) {
						rsi = new KewResitSenaraiInvois();
						rsi.setDeposit(dep);
						rsi.setResit(resitDeposit);
						rsi.setFlagJenisResit("DEPOSIT");
						mp.persist(rsi);
					}
				}

				if (!duplicateResit) {
					lj.setNoResit(resitDeposit.getNoResit());
					lj.setTarikhResit(resitDeposit.getTarikhResit());
					lj.setTarikhTransaksi(resitDeposit.getTarikhBayaran());
				}
				
			} else {
				// invois
				KewInvois inv = (KewInvois) mp.get("select x from KewInvois x where x.idLejar = '" + lj.getId() + "' ");
				if (inv != null) {
					KewResitSenaraiInvois rsi = (KewResitSenaraiInvois) mp
							.get("select x from KewResitSenaraiInvois x where x.invois.id = '"
									+ inv.getId() + "' ");
					if (rsi == null || duplicateResit) {
						rsi = new KewResitSenaraiInvois();
						rsi.setInvois(inv);
						rsi.setResit(resitSewa);
						rsi.setFlagJenisResit("INVOIS");
						mp.persist(rsi);
					}
				}
				
				if (!duplicateResit) {
					lj.setNoResit(resitSewa.getNoResit());
					lj.setTarikhResit(resitSewa.getTarikhResit());
					lj.setTarikhTransaksi(resitSewa.getTarikhBayaran());
				}				
			}

			if (!duplicateResit) {
				// update KewInvois / KewDeposit
				if (lj.getKodHasil().getId().equalsIgnoreCase("72311")) { // deposit
					updateDepositInFinance(lj, r.getPemohon(), mp);
				} else {
					updateInvoisInFinance(lj, r.getPemohon(), mp);					
				}
			}
		}
	}

	public static void updateInvoisInFinance(RppAkaun ak, Users pemohon,
			DbPersistence mp) {
		KewInvois inv = (KewInvois) mp
				.get("select x from KewInvois x where x.idLejar = '"
						+ ak.getId() + "' and x.jenisBayaran.id = '02' ");
		if (inv != null) {
			inv.setFlagBayar("Y");
			inv.setKredit(ak.getDebit());
			inv.setFlagQueue("Y");
		}
	}

	public static void updateDepositInFinance(RppAkaun ak, Users pemohon,
			DbPersistence mp) {
		KewDeposit dep = (KewDeposit) mp
				.get("select x from KewDeposit x where x.idLejar = '"
						+ ak.getId() + "' and x.jenisBayaran.id = '02' ");
		if (dep != null) {
			dep.setFlagBayar("Y");
			dep.setFlagQueue("T");
			dep.setNoResit(ak.getNoResit());
			dep.setTarikhBayaran(ak.getTarikhResit());
			dep.setBakiDeposit(ak.getDebit());
		}
	}

	@SuppressWarnings("unchecked")
	public static List<RppAkaun> getListTempahanDanBayaran(DbPersistence mp,
			RppPermohonan r) {
		List<RppAkaun> list = mp
				.list("select x from RppAkaun x where x.permohonan.id = '"
						+ r.getId() + "' ");
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public static Double getAmaunTotalSewaRpWithoutDeposit(RppPermohonan r, DbPersistence mp){
		Double amaun = 0d;
		try {
			List<RppAkaun> ak = null;
			ak = mp.list("select x from RppAkaun x where x.permohonan.id = '"+r.getId()+"' and x.kodHasil.id <> '72311' ");
				
			for(int i=0;i<ak.size();i++){
				amaun = amaun + ak.get(i).getDebit();
			}
		} catch (Exception e) {
			System.out.println("Error amaunTotalSewaRpWithoutDeposit : "+e.getMessage());
		}
		return amaun;
	}
	
	public static Double getAmaunDeposit(RppPermohonan r, DbPersistence mp){
		Double amaun = 0d;
		try {
			RppAkaun ak = null;
			ak = (RppAkaun) mp.get("select x from RppAkaun x where x.permohonan.id = '"+r.getId()+"' and x.kodHasil.id = '72311' ");
			if(ak != null){
				amaun = ak.getDebit();
			}
		} catch (Exception e) {
			System.out.println("Error amaunDeposit : "+e.getMessage());
		}
		return amaun;
	}
	
	public static Double getAmaunTotalSewaRpWithDeposit(RppPermohonan r, DbPersistence mp){
		Double amaun = 0d;
		try {
			List<RppAkaun> ak = null;
			ak = mp.list("select x from RppAkaun x where x.permohonan.id = '"+r.getId()+"' ");
				
			for(int i=0;i<ak.size();i++){
				amaun = amaun + ak.get(i).getDebit();
			}
		} catch (Exception e) {
			System.out.println("Error amaunTotalSewaRpWithDeposit : "+e.getMessage());
		}
		return amaun;
	}

	public static void generateResitLondon(RppRekodTempahanLondon permohonan, Date tarikhBayaran, FPXRecords fpxRecords, boolean duplicateResit, DbPersistence mp) {

		KewBayaranResit rstSewa = null;
		if (duplicateResit) {
			rstSewa = new KewBayaranResit();
		} else {
			rstSewa = permohonan.getResitSewa();
		}
		
		Users pembayar = permohonan.getPemohon();
		if (pembayar != null) {
			rstSewa.setPembayar(pembayar);
			rstSewa.setNoPengenalanPembayar(pembayar.getId());
			rstSewa.setNamaPembayar(pembayar.getUserName());				
			rstSewa.setAlamatPembayar(UtilKewangan.getAlamatPembayar(pembayar));
		}
		if (duplicateResit) {
			if (rstSewa.getNoResitLama() == null){
				rstSewa.setNoResitLama(rstSewa.getNoResit());
			}
			rstSewa.setNoResit(generateReceiptNo(tarikhBayaran, mp));
		} else {
			if (isValidNoResit(rstSewa, tarikhBayaran)) {
				//DO NOTHING
			} else {
				if (rstSewa.getNoResitLama() == null){
					rstSewa.setNoResitLama(rstSewa.getNoResit());
				}
				rstSewa.setNoResit(generateReceiptNo(tarikhBayaran, mp));
			}
		}
		rstSewa.setTarikhBayaran(tarikhBayaran);
		rstSewa.setTarikhResit(tarikhBayaran);
		rstSewa.setFlagJenisBayaran("ONLINE");
		rstSewa.setTarikhDaftar(tarikhBayaran);
		rstSewa.setUserPendaftar(permohonan.getPemohon());
		rstSewa.setJumlahAmaunBayaran(permohonan.getDebit());
		rstSewa.setJuruwangKod("09");
		rstSewa.setFlagJenisResit("2"); //INVOIS
		rstSewa.setIdPermohonan(permohonan.getId());
		rstSewa.setIdTransaksiBank(fpxRecords.getId());
		if (duplicateResit) {
			mp.persist(rstSewa);
		}
				
		KewResitKaedahBayaran kb = new KewResitKaedahBayaran();
		kb.setResit(rstSewa);
		kb.setAmaunBayaran(rstSewa.getJumlahAmaunBayaran());
		kb.setModBayaran((CaraBayar) mp.find(CaraBayar.class, "FPX"));
		mp.persist(kb);
				
		/** SAVE ID RESIT DALAM RPPPERMOHONAN * */
		permohonan.setResitSewa(rstSewa);
		permohonan.setFlagBayar("Y");
		permohonan.setTarikhTransaksi(tarikhBayaran);
		permohonan.setKredit(permohonan.getDebit());
				
		//update lejar
		List<RppAkaun> listAkaun = mp.list("select x from RppAkaun x where x.rekodTempahanLondon.id = '"+permohonan.getId()+"' ");
				
		for(int i=0;i<listAkaun.size();i++){
					
			RppAkaun lj = listAkaun.get(i);
			lj.setFlagBayar("Y");
			lj.setIdKemaskini(permohonan.getPemohon());
			lj.setKredit(lj.getDebit());
			lj.setTarikhKemaskini(tarikhBayaran);
			lj.setNoResit(rstSewa.getNoResit());
			lj.setTarikhResit(rstSewa.getTarikhResit());
			lj.setTarikhTransaksi(rstSewa.getTarikhBayaran());
					
			//invois
			KewInvois inv = (KewInvois) mp.get("select x from KewInvois x where x.idLejar = '"+lj.getId()+"' ");
			KewResitSenaraiInvois rsi = new KewResitSenaraiInvois();
			rsi.setInvois(inv);
			rsi.setResit(rstSewa);
			rsi.setFlagJenisResit("INVOIS");
			mp.persist(rsi);
					
			//update KewInvois / KewDeposit
			updateInvoisLondonInFinance(mp,lj,permohonan.getPemohon());		
		}		
	}
	
	public static void updateInvoisLondonInFinance(DbPersistence mp, RppAkaun ak,Users pemohon){
		KewInvois inv = (KewInvois) mp.get("select x from KewInvois x where x.idLejar = '"+ak.getId()+"' and x.jenisBayaran.id = '12' ");
		inv.setFlagBayar("Y");
		inv.setKredit(ak.getDebit());
		inv.setFlagQueue("Y");
	}

	public static void generateResitUtiliti(UtilPermohonan permohonan, Date tarikhBayaran, FPXRecords fpxRecords, boolean duplicateResit, DbPersistence mp) {
		KewBayaranResit resit = null;
		if (duplicateResit) {
			resit = new KewBayaranResit();
		} else {
			resit = permohonan.getResitSewa();
		}
		
		Users pembayar = permohonan.getPemohon();
		if (pembayar != null) {
			resit.setPembayar(pembayar);
			resit.setNoPengenalanPembayar(pembayar.getId());
			resit.setNamaPembayar(pembayar.getUserName());				
			resit.setAlamatPembayar(UtilKewangan.getAlamatPembayar(pembayar));
		}
		if (duplicateResit) {
			if (resit.getNoResitLama() == null){
				resit.setNoResitLama(resit.getNoResit());
			}
			resit.setNoResit(generateReceiptNo(tarikhBayaran, mp));
		} else {					
			if (isValidNoResit(resit, tarikhBayaran)) {
				//DO NOTHING
			} else {
				if (resit.getNoResitLama() == null){
					resit.setNoResitLama(resit.getNoResit());
				}
				resit.setNoResit(generateReceiptNo(tarikhBayaran, mp));
			}
		}
		resit.setTarikhBayaran(tarikhBayaran);
		resit.setTarikhResit(tarikhBayaran);
		resit.setFlagJenisBayaran("ONLINE");
		resit.setTarikhDaftar(tarikhBayaran);
		resit.setJuruwangKod("09");// ONLINE
		resit.setUserPendaftar(permohonan.getPemohon());
		resit.setJumlahAmaunBayaran(permohonan.getAmaun());
		resit.setFlagJenisResit("2"); //INVOIS
		resit.setIdPermohonan(permohonan.getId());
		resit.setIdTransaksiBank(fpxRecords.getId());
		if (duplicateResit)
			mp.persist(resit);

		// CREATE KAEDAH BAYARAN
		KewResitKaedahBayaran kb = new KewResitKaedahBayaran();
		kb.setResit(resit);
		kb.setAmaunBayaran(resit.getJumlahAmaunBayaran());
		kb.setModBayaran((CaraBayar) mp.find(CaraBayar.class, "FPX"));
		mp.persist(kb);
		
		// UPDATE LEJAR
		UtilAkaun mn = (UtilAkaun) mp.get("select x from UtilAkaun x where x.permohonan.id = '" + permohonan.getId() + "' ");
		if (mn != null) {
			if (!duplicateResit) {
				mn.setFlagBayar("Y");
				mn.setIdKemaskini(permohonan.getPemohon());
				mn.setKredit(mn.getDebit());
				mn.setNoResit(resit.getNoResit());
				mn.setTarikhKemaskini(tarikhBayaran);
				mn.setTarikhResit(resit.getTarikhResit());
				mn.setTarikhTransaksi(resit.getTarikhBayaran());
			}		

			if (mn.getKodHasil().getId().equalsIgnoreCase("74299")) {
				KewInvois inv = (KewInvois) mp.get("select x from KewInvois x where x.idLejar = '" + mn.getId() + "' ");
				if (!duplicateResit) {
					inv.setFlagBayar("Y");
					inv.setKredit(mn.getDebit());
					inv.setFlagQueue("Y");
				}
				
				KewResitSenaraiInvois rsi = new KewResitSenaraiInvois();
				rsi.setInvois(inv);
				rsi.setResit(resit);
				rsi.setFlagJenisResit("INVOIS");
				mp.persist(rsi);
			}
		}		
		
		// UPDATE MAIN TABLE
		if (!duplicateResit) {
			permohonan.setStatusBayaran("Y");
			permohonan.setTarikhBayaran(tarikhBayaran);
			permohonan.setResitSewa(resit);
		}
	}
	
	public static String getIdResitDepositRPP(String idPermohonan, DbPersistence db) {
		String idResit = null;

		RppAkaun oneAk = null;
		KewDeposit oneDep = null;
		KewResitSenaraiInvois rsi = null;
		oneAk = (RppAkaun) db.get("select x from RppAkaun x where x.permohonan.id = '" + idPermohonan + "' and x.kodHasil.id = '72311' ");
		
		if(oneAk != null){
			oneDep = (KewDeposit) db.get("select x from KewDeposit x where x.idLejar = '" + oneAk.getId() + "' ");
			if(oneDep != null){
				rsi = (KewResitSenaraiInvois) db.get("select x from KewResitSenaraiInvois x where x.deposit.id = '" + oneDep.getId() + "' ");
				if(rsi != null){
					if (rsi.getResit() != null)
						if (rsi.getResit().getIdPermohonan() == null || rsi.getResit().getIdPermohonan().equals("")){
							idResit = rsi.getResit().getId();
						}	
				}
			}
		}
		
		if (idResit == null) {
			if(oneDep != null){
				KewBayaranResit resit = (KewBayaranResit) db.get("select x from KewBayaranResit x where x.noResit = '" + oneDep.getNoResit() + "'");
				if (resit != null) {
					if (resit.getIdPermohonan() == null || resit.getIdPermohonan().equals("")){
						idResit = resit.getId();
					}
				}
			}			 
		}
		
		if (idResit == null) {
			if(oneAk != null){
				KewBayaranResit resit = (KewBayaranResit) db.get("select x from KewBayaranResit x where x.noResit = '" + oneAk.getNoResit() + "'");
				if (resit != null) {
					if (resit.getIdPermohonan() == null || resit.getIdPermohonan().equals("")){
						idResit = resit.getId();
					}
				}
			}
		}
		
		return idResit;
	}
	
	public static KewTuntutanDeposit getTuntutanDepositRPP(String idPermohonan, DbPersistence db) {
		KewTuntutanDeposit tuntutanDeposit = null;

		RppAkaun oneAk = null;
		KewDeposit oneDep = null;
		oneAk = (RppAkaun) db.get("select x from RppAkaun x where x.permohonan.id = '" + idPermohonan + "' and x.kodHasil.id = '72311' ");
		
		if(oneAk != null){
			oneDep = (KewDeposit) db.get("select x from KewDeposit x where x.idLejar = '" + oneAk.getId() + "' ");
			if(oneDep != null){
				if (oneDep.getTuntutanDeposit() != null) {
					tuntutanDeposit = oneDep.getTuntutanDeposit();
				}
			}
		}
		
		return tuntutanDeposit;
	}

	public static String getIdResitSewaRPP(String idPermohonan, DbPersistence db) {
		String idResit = null;
		
		RppAkaun oneAk = null;
		KewInvois oneInv = null;
		KewResitSenaraiInvois rsi = null;
		oneAk = (RppAkaun) db.get("select x from RppAkaun x where x.permohonan.id = '" + idPermohonan + "' and x.kodHasil.id = '74299' ");
		
		if(oneAk!=null){
			oneInv = (KewInvois) db.get("select x from KewInvois x where x.idLejar = '" + oneAk.getId() + "' ");
			if(oneInv != null){
				rsi = (KewResitSenaraiInvois) db.get("select x from KewResitSenaraiInvois x where x.invois.id = '" + oneInv.getId() + "' ");
				if(rsi != null){
					if (rsi.getResit() != null)
						if (rsi.getResit().getIdPermohonan() == null || rsi.getResit().getIdPermohonan().equals("")){
							idResit = rsi.getResit().getId();
						}
				}
			}
		}
		
		if (idResit == null) {
			if(oneAk != null){
				KewBayaranResit resit = (KewBayaranResit) db.get("select x from KewBayaranResit x where x.noResit = '" + oneAk.getNoResit() + "'");
				if (resit != null) {
					if (resit.getIdPermohonan() == null || resit.getIdPermohonan().equals("")){
						idResit = resit.getId();
					}
				}
			}
		}
	
		return idResit;
	}
	
	public static String getIdResitSewaLondon(String idPermohonan, DbPersistence db) {
		String idResit = null;
		
		RppAkaun oneAk = null;
		KewInvois oneInv = null;
		KewResitSenaraiInvois rsi = null;
		oneAk = (RppAkaun) db.get("select x from RppAkaun x where x.rekodTempahanLondon.id = '" + idPermohonan + "' and x.kodHasil.id = '74299' ");
		
		if(oneAk!=null){
			oneInv = (KewInvois) db.get("select x from KewInvois x where x.idLejar = '" + oneAk.getId() + "' ");
			if(oneInv != null){
				rsi = (KewResitSenaraiInvois) db.get("select x from KewResitSenaraiInvois x where x.invois.id = '" + oneInv.getId() + "' ");
				if(rsi != null){
					if (rsi.getResit() != null)
						if (rsi.getResit().getIdPermohonan() == null || rsi.getResit().getIdPermohonan().equals("")){
							idResit = rsi.getResit().getId();
						}						
				}
			}
		}
		
		if (idResit == null) {
			if(oneAk != null){
				KewBayaranResit resit = (KewBayaranResit) db.get("select x from KewBayaranResit x where x.noResit = '" + oneAk.getNoResit() + "'");
				if (resit != null) {
					if (resit.getIdPermohonan() == null || resit.getIdPermohonan().equals("")){
						idResit = resit.getId();
					}
				}
			}
		}
		
		return idResit;
	}
	
	public static String getIdResitSewaUtiliti(String idPermohonan, DbPersistence db) {
		String idResit = null;
		
		UtilAkaun oneAk = null;
		KewInvois oneInv = null;
		KewResitSenaraiInvois rsi = null;
		oneAk = (UtilAkaun) db.get("select x from UtilAkaun x where x.permohonan.id = '" + idPermohonan + "' and x.kodHasil.id = '74299' ");
		
		if(oneAk!=null){
			oneInv = (KewInvois) db.get("select x from KewInvois x where x.idLejar = '" + oneAk.getId() + "' ");
			if(oneInv != null){
				rsi = (KewResitSenaraiInvois) db.get("select x from KewResitSenaraiInvois x where x.invois.id = '" + oneInv.getId() + "' ");
				if(rsi != null){
					if (rsi.getResit() != null)
						if (rsi.getResit().getIdPermohonan() == null || rsi.getResit().getIdPermohonan().equals("")){
							idResit = rsi.getResit().getId();
						}						
				}
			}
		}
		
		if (idResit == null) {
			if(oneAk != null){
				KewBayaranResit resit = (KewBayaranResit) db.get("select x from KewBayaranResit x where x.noResit = '" + oneAk.getNoResit() + "'");
				if (resit != null) {
					if (resit.getIdPermohonan() == null || resit.getIdPermohonan().equals("")){
						idResit = resit.getId();
					}
				}
			}			 
		}
		
		return idResit;
	}
}
