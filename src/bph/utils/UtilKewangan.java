package bph.utils;

import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import lebah.db.Db;
import lebah.template.DbPersistence;
import portal.module.entity.Users;
import bph.entities.kew.KewTuntutanDeposit;
import bph.entities.kewangan.KewBayaranResit;
import bph.entities.kewangan.KewDeposit;
import bph.entities.kewangan.KewInvois;
import bph.entities.kewangan.KewTempInQueue;
import bph.entities.kewangan.PembayarLain;
import bph.entities.kewangan.SeqNoResit;
import bph.entities.kod.KodJuruwang;
import bph.entities.kod.Status;
import bph.entities.qtr.KuaAkaun;
import bph.entities.qtr.KuaPenghuni;
import bph.entities.rk.RkAkaun;
import bph.entities.rk.RkFail;
import bph.entities.rk.RkPermohonan;
import bph.entities.rpp.RppAkaun;
import bph.entities.rpp.RppPermohonan;
import bph.entities.rpp.RppRekodTempahanLondon;
import bph.entities.utiliti.UtilAkaun;
import bph.entities.utiliti.UtilPermohonan;
import db.persistence.MyPersistence;

public class UtilKewangan {

	private static String idJenisBayaranDepositKuarters = "01";
	private static String idJenisBayaranDepositRPP = "02";
	private static String idJenisBayaranDepositRuangKomersil = "04";
	
	private static String idJenisBayaranSewaKuarters = "01";
	private static String idJenisBayaranSewaRPP = "02";
	private static String idJenisBayaranSewaGelanggang = "03";
	private static String idJenisBayaranSewaRuangKomersil = "04";
	private static String idJenisBayaranSewaRPLondon = "12";

	/** START EDIT BY PEJE **/
	public synchronized static String generateReceiptNoOnline(MyPersistence mp, Date tarikhBayaran) {

		String receiptNo = "";
		Integer today = Integer.parseInt(getCurrentDate(tarikhBayaran, "dd"));
		Integer month = Integer.parseInt(getCurrentDate(tarikhBayaran, "MM"));
		Integer year = Integer.parseInt(getCurrentDate(tarikhBayaran, "yyyy"));

		String defaultNoKodJuruwang = "09";// online
		String date = getCurrentDate(tarikhBayaran, "ddMMyyyy");
		int runningNo = 0;
		SeqNoResit sq = (SeqNoResit) mp.get("select x from SeqNoResit x where x.day = '" + today + "' and x.month = '" + month + "' and x.year = '"
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
		// format : DD.MM.YYYY.<2 digit pusat penerimaan>.<kod
		// juruwang>.<turutan nombor(3 digit)>.
		String seq = String.format("%05d", runningNo);
		receiptNo = date + "" + defaultNoKodJuruwang + seq;

		return receiptNo;
	}

	public static String getCurrentDate(Date date, String format) {
		String afterFormat = "";
		if (date != null) {
			if (date.toString().length() > 0) {
				afterFormat = new java.text.SimpleDateFormat(format).format(date);
			} else {
				afterFormat = "";
			}
		}
		return afterFormat;
	}
	/** END EDIT BY PEJE **/

	public Status getStatus(String turutan, String seksyen, MyPersistence mp) throws Exception {
		@SuppressWarnings("unchecked")
		List<Status> listStatus = mp.list("SELECT x FROM Status x WHERE x.turutan = '" + turutan + "' AND x.seksyen.id = '" + seksyen + "'");
		Status status = (Status) mp.find(Status.class, (!listStatus.isEmpty() ? listStatus.get(0).getId() : ""));
		return status;
	}

	public static String generateReceiptNo(MyPersistence mp, Users juruwang) throws ParseException {

		String receiptNo = "NORESIT";
		KodJuruwang kodj = (KodJuruwang) mp.get("select x from KodJuruwang x where x.juruwang.id = '" + juruwang.getId() + "' and x.flagAktif = 'Y'");

		Integer today = Integer.parseInt(Util.getCurrentDate("dd"));
		Integer month = Integer.parseInt(Util.getCurrentDate("MM"));
		Integer year = Integer.parseInt(Util.getCurrentDate("yyyy"));

		String defaultNoPusatTerima = "";
		String defaultNoKodJuruwang = "09";
		String noPusatTerima = kodj != null ? kodj.getKodPusatTerima() : defaultNoPusatTerima;
		String noKodJuruwang = kodj != null ? kodj.getKodJuruwang() : defaultNoKodJuruwang;
		String date = Util.getCurrentDate("ddMMyyyy");
		int runningNo = 0;

		SeqNoResit sq = null;
		if (kodj != null) {
			sq = (SeqNoResit) mp.get("select x from SeqNoResit x where x.day = '" + today + "' and x.month = '" + month + "' and x.year = '"
							+ year + "' and x.kodJuruwang.id = '" + kodj.getId() + "'");
		} else {
			sq = (SeqNoResit) mp.get("select x from SeqNoResit x where x.day = '" + today + "' and x.month = '" + month + "' and x.year = '"
							+ year + "' and x.kodJuruwang is null ");
		}

		/** running number by daily */
		if (sq == null) {
			sq = new SeqNoResit();
			sq.setDay(today);
			sq.setMonth(month);
			sq.setYear(year);
			sq.setKodJuruwang(kodj);
			sq.setBil(1);
			runningNo = 1;
			mp.persist(sq);
			mp.flush();

		} else {
			mp.pesismisticLock(sq);
			runningNo = sq.getBil() + 1;
			sq.setBil(runningNo);
			sq = (SeqNoResit) mp.merge(sq);
		}

		// format : DD.MM.YYYY.<2 digit pusat_penerimaan>.<kod_juruwang>.<turutan nombor(5 digit)>.
		String seq = "";
		if ("09".equals(noKodJuruwang)) { //ONLINE
			seq = String.format("%05d", runningNo);
		} else {
			//ADD BY PEJE - 15102018 - USING NEW RECEIPT TEMPLATE
			Date currDate = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			String startDateInString = "15/10/2018";
			Date startDate = sdf.parse(startDateInString);
			if (currDate.before(startDate)) {
				seq = String.format("%05d", runningNo); // OLD FORMAT
			} else {
				seq = String.format("%04d", runningNo); // NEW FORMAT
			}
		}
		receiptNo = date + "" + noPusatTerima + "" + noKodJuruwang + "" + seq;

		return receiptNo;
	}

	// AZAM ADD
	public static void updateInvoisLejarModul(MyPersistence mp, KewInvois inv, KewBayaranResit resit, KewTempInQueue q, String userIdLogin) {

		String idModul = inv.getJenisBayaran() != null ? inv.getJenisBayaran().getId() : null;
		Users userLogin = (Users) mp.find(Users.class, userIdLogin);
		Double amaunBayaran = 0D;

		// find and update by modul
		if (idModul.equalsIgnoreCase(idJenisBayaranSewaKuarters)) { // KUARTERS

			KuaAkaun ak = (KuaAkaun) mp.find(KuaAkaun.class, inv.getIdLejar());
			ak.setFlagBayar("Y");
			ak.setIdKemaskini(userLogin);
			ak.setKredit(ak.getDebit());
			ak.setNoResit(resit.getNoResit());
			ak.setTarikhKemaskini(new Date());
			ak.setTarikhResit(resit.getTarikhResit());
			ak.setTarikhTransaksi(resit.getTarikhBayaran());

		} else if (idModul.equalsIgnoreCase(idJenisBayaranSewaRPP) || idModul.equalsIgnoreCase(idJenisBayaranSewaRPLondon)) { // IR

			RppAkaun ak = (RppAkaun) mp.find(RppAkaun.class, inv.getIdLejar());
			ak.setFlagBayar("Y");
			ak.setIdKemaskini(userLogin);
			ak.setKredit(ak.getDebit());
			ak.setNoResit(resit.getNoResit());
			ak.setTarikhKemaskini(new Date());
			ak.setTarikhResit(resit.getTarikhResit());
			ak.setTarikhTransaksi(resit.getTarikhBayaran());

			if (ak != null) {
				if (idModul.equalsIgnoreCase(idJenisBayaranSewaRPP)) {
					RppPermohonan r = ak.getPermohonan();
					r.setStatusBayaran("Y");
					r.setTarikhBayaran(resit.getTarikhBayaran());
					r.setStatus((Status) mp.find(Status.class, "1425259713415")); // Tempahan lulus
					r.setResitSewa(resit); /** EDIT 21102015 */
				} else if (idModul.equalsIgnoreCase(idJenisBayaranSewaRPLondon)) {
					RppRekodTempahanLondon rk = ak.getRekodTempahanLondon();
					rk.setKredit(rk.getDebit());
					rk.setFlagBayar("Y");
					rk.setTarikhTransaksi(resit.getTarikhBayaran());
					rk.setResitSewa(resit); /** EDIT 21102015 */
				}
			}

		} else if (idModul.equalsIgnoreCase(idJenisBayaranSewaGelanggang)) { // UTIL (DEWAN GELANGGANG)

			UtilAkaun ak = (UtilAkaun) mp.find(UtilAkaun.class, inv.getIdLejar());
			if (ak != null) {
				ak.setFlagBayar("Y");
				ak.setIdKemaskini(userLogin);
				ak.setKredit(ak.getDebit());
				ak.setNoResit(resit.getNoResit());
				ak.setTarikhKemaskini(new Date());
				ak.setTarikhResit(resit.getTarikhResit());
				ak.setTarikhTransaksi(resit.getTarikhBayaran());

				UtilPermohonan permohonan = (UtilPermohonan) mp.find(UtilPermohonan.class, ak.getPermohonan().getId());

				if (permohonan != null) {
					permohonan.setStatusBayaran("Y");
				}
			}

		} else if (idModul.equalsIgnoreCase(idJenisBayaranSewaRuangKomersil)) { // RK
			RkFail fail = (RkFail) mp.find(RkFail.class, inv.getIdLejar());
			if (fail != null) {
				RkAkaun akaun = new RkAkaun();
				akaun.setFail(fail);
				akaun.setTarikhTransaksi(resit.getTarikhResit());
				akaun.setKodHasil(inv.getKodHasil()); // KOD HASIL BAGI INVOIS RK
				akaun.setIdJenisTransaksi("2"); // BAYARAN
				if (q != null) {
					amaunBayaran = q.getAmaunBayaran();
				}
				akaun.setKredit(amaunBayaran);
				akaun.setDebit(0D);
				akaun.setKeterangan(inv.getKeteranganBayaran());
				akaun.setResit(resit);
				akaun.setKewInvois(inv);
				akaun.setDaftarOleh(userLogin);
				mp.persist(akaun);
			}	
		} 
	}

	/****
	 * DEPOSIT
	 * @param db
	 * @param dep
	 * @param resit
	 * @param q 
	 * @param userIdLogin
	 */
	public static void updateDepositLejarModul(MyPersistence mp, KewDeposit dep, KewBayaranResit resit, KewTempInQueue q, String userIdLogin) {

		String idModul = dep.getJenisBayaran() != null ? dep.getJenisBayaran().getId() : null;
		Users userLogin = (Users) mp.find(Users.class, userIdLogin);
		Double amaunBayaran = 0D;

		// find and update by modul
		if (idModul.equalsIgnoreCase(idJenisBayaranDepositKuarters)) { // KUARTERS

			KuaAkaun ak = (KuaAkaun) mp.find(KuaAkaun.class, dep.getIdLejar());
			ak.setFlagBayar("Y");
			ak.setIdKemaskini(userLogin);
			ak.setKredit(ak.getDebit());
			ak.setNoResit(resit.getNoResit());
			ak.setTarikhKemaskini(new Date());
			ak.setTarikhResit(resit.getTarikhResit());
			ak.setTarikhTransaksi(resit.getTarikhBayaran());

		} else if (idModul.equalsIgnoreCase(idJenisBayaranDepositRPP)) { // IR

			RppAkaun ak = (RppAkaun) mp.find(RppAkaun.class, dep.getIdLejar());
			ak.setFlagBayar("Y");
			ak.setIdKemaskini(userLogin);
			ak.setKredit(ak.getDebit());
			ak.setNoResit(resit.getNoResit());
			ak.setTarikhKemaskini(new Date());
			ak.setTarikhResit(resit.getTarikhResit());
			ak.setTarikhTransaksi(resit.getTarikhBayaran());
			ak.getPermohonan().setResitDeposit(resit); // EDIT 21102015

		} else if (idModul.equalsIgnoreCase(idJenisBayaranDepositRuangKomersil)) { // RK
			RkPermohonan permohonan = (RkPermohonan) mp.find(RkPermohonan.class, dep.getIdLejar());
			if (permohonan != null) {
				RkAkaun akaun = new RkAkaun();
				akaun.setFail(permohonan.getFail());
				akaun.setPermohonan(permohonan);
				akaun.setTarikhTransaksi(resit.getTarikhResit());
				akaun.setKodHasil(dep.getKodHasil());
				akaun.setIdJenisTransaksi("2"); // BAYARAN
				if (q != null) {
					amaunBayaran = q.getAmaunBayaran();
				}
				akaun.setDebit(amaunBayaran);
				akaun.setKredit(0D);
				akaun.setKeterangan(dep.getKeteranganDeposit());
				akaun.setResit(resit);
				akaun.setDaftarOleh(userLogin);
				mp.persist(akaun);
			}			
		}
	}

	public static List<KewTuntutanDeposit> listTuntutanDeposit(DbPersistence db, String userRole, Map<String, Object> params) {

		ArrayList<KewTuntutanDeposit> list = new ArrayList<KewTuntutanDeposit>();
		Db db1 = null;

		String findPemohon = (String) params.get("findPemohon");
		String findNoKp = (String) params.get("findNoKp");
		String findNoTempahan = (String) params.get("findNoTempahan");
		String findPeranginan = (String) params.get("findPeranginan");
		String findTarikhDari = (String) params.get("findTarikhMohonDeposit");
		String findTarikhHingga = (String) params.get("findTarikhMohonDeposit2");
		String findStatus = (String) params.get("findStatus");

		try {
			db1 = new Db();

			String sql = "select d.id from rpp_permohonan a, rpp_akaun b, kew_deposit c, kew_tuntutan_deposit d, users e "
					+ " where a.id = b.id_permohonan "
					+ " and b.id = c.id_lejar "
					+ " and c.id = d.id_kew_deposit "
					+ " and d.id_jenis_tuntutan = '02' "
					+ " and a.id_pemohon = e.user_login ";

			if (findNoTempahan != null && !findNoTempahan.equalsIgnoreCase("")) {
				sql += " and upper(a.no_tempahan) like '%" + findNoTempahan.trim().toUpperCase() + "%' ";
			}

			if (findPeranginan != null && !findPeranginan.equalsIgnoreCase("")) {
				sql += " and a.id_peranginan = '" + findPeranginan + "' ";
			}

			if (findPemohon != null && !findPemohon.equalsIgnoreCase("")) {
				sql += " and upper(e.user_name) like '%" + findPemohon.trim().toUpperCase() + "%' ";
			}

			if (findNoKp != null && !findNoKp.equalsIgnoreCase("")) {
				sql += " and upper(e.no_kp) like '%" + findNoKp.trim().toUpperCase() + "%' ";
			}
			
			if (findTarikhDari != null && !findTarikhDari.equalsIgnoreCase("") && findTarikhHingga != null && !findTarikhHingga.equalsIgnoreCase("")) {
				sql += " and d.tarikh_permohonan between '" + findTarikhDari + "' and '" + findTarikhHingga + "' ";
			} else if (findTarikhDari != null && !findTarikhDari.equalsIgnoreCase("") && findTarikhHingga == null) {
				sql += " and d.tarikh_permohonan = '" + findTarikhDari + "' ";
			}

			if (findStatus != null && !findStatus.equalsIgnoreCase("")) {
				sql += " and d.id_status = '" + findStatus + "' ";
			}
			
			ResultSet rs = db1.getStatement().executeQuery(sql);
			while (rs.next()) {
				KewTuntutanDeposit rp = db.find(KewTuntutanDeposit.class, rs.getString("id"));
				list.add(rp);
			}
		} catch (Exception e) {
			System.out.println("error listTuntutanDeposit() : " + e.getMessage());
		} finally {
			if (db1 != null)
				db1.close();
		}
		return list;
	}

	public static List<String> listPembayar(DbPersistence db, String kodHasil) {

		ArrayList<String> list = new ArrayList<String>();
		Db db1 = null;
		try {
			db1 = new Db();

			String sql = "select a.id from kew_bayaran_resit a, kew_resit_senarai_invois b, kew_invois c"
					+ " where a.id = b.id_bayaran_resit and b.id_invois = c.id";

			if (kodHasil != null && !kodHasil.equalsIgnoreCase("")) {
				sql += " and c.id_kod_hasil = '" + kodHasil + "' ";
			}

			sql += " union"
					+ " select a.id from kew_bayaran_resit a, kew_resit_senarai_invois b, kew_deposit c"
					+ " where a.id = b.id_bayaran_resit and b.id_deposit = c.id";

			if (kodHasil != null && !kodHasil.equalsIgnoreCase("")) {
				sql += " and c.id_kod_hasil = '" + kodHasil + "' ";
			}

			ResultSet rs = db1.getStatement().executeQuery(sql);
			while (rs.next()) {
				String id = rs.getString("id");
				list.add(id);
			}
		} catch (Exception e) {
			System.out.println("error listPembayar() : " + e.getMessage());
		} finally {
			if (db1 != null)
				db1.close();
		}
		return list;
	}

	public static List<String> listPembayarLama(DbPersistence db,
			Map<String, Object> params) throws Exception {

		ArrayList<String> list = new ArrayList<String>();
		Db db1 = null;

		String findNoKp = (String) params.get("findNoKp");
		String findNamaPembayar = (String) params.get("findNamaPembayar");
		String findTarikhResit = (String) params.get("tarikhResit");
		String findTarikhResitHingga = (String) params.get("tarikhResitHingga");

		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat nfResit = new SimpleDateFormat("yyyy-MM-dd");
		Date resitMula = null;
		Date resitHingga = null;
		String newTarikhResitMula = "";
		String newTarikhResitHingga = "";

		if (findTarikhResit != "") {
			resitMula = df.parse(findTarikhResit);
			newTarikhResitMula = nfResit.format(resitMula);
		}

		if (findTarikhResit != "") {
			resitHingga = df.parse(findTarikhResitHingga);
			newTarikhResitHingga = nfResit.format(resitHingga);
		}
		try {
			db1 = new Db();

			String sql = "select a.id_bayaranlain as id from kew_bayaranlain_temp_detail a where a.id is not null";

			if (findNoKp != null && !findNoKp.equalsIgnoreCase("")) {
				sql += " and upper(a.no_kp) like '%" + findNoKp.trim().toUpperCase() + "%' ";
			}

			if (findNamaPembayar != null && !findNamaPembayar.equalsIgnoreCase("")) {
				sql += " and upper(a.nama) like '%" + findNamaPembayar.trim().toUpperCase() + "%' ";
			}

			if (findTarikhResit != null && findTarikhResitHingga != null) {
				sql += " and a.tarikh_resit between '" + newTarikhResitMula + "' and '" + newTarikhResitHingga + "'";
			}

			ResultSet rs = db1.getStatement().executeQuery(sql);
			while (rs.next()) {
				String id = rs.getString("id");
				list.add(id);
			}
		} catch (Exception e) {
			System.out.println("error listPembayarLama() : " + e.getMessage());
		} finally {
			if (db1 != null)
				db1.close();
		}
		return list;
	}

	public static List<String> listPemohonKelompok(DbPersistence db, Map<String, Object> params) {

		ArrayList<String> list = new ArrayList<String>();
		Db db1 = null;

		String findNoInvois = (String) params.get("findNoInvois");

		try {
			db1 = new Db();

			String sql = "select a.id_lejar from kew_invois a where a.id is not null";

			if (findNoInvois != null && !findNoInvois.equalsIgnoreCase("")) {
				sql += " and a.no_invois like '%" + findNoInvois.trim().toUpperCase() + "%' ";
			}

			String sqlmain = "select a.id_permohonan as id from rpp_akaun a where a.id is not null and id in(" + sql + ")";

			ResultSet rs = db1.getStatement().executeQuery(sqlmain);
			while (rs.next()) {
				String id = rs.getString("id");
				list.add(id);
			}
		} catch (Exception e) {
			System.out.println("error listPemohonKelompok() : " + e.getMessage());
		} finally {
			if (db1 != null)
				db1.close();
		}
		return list;
	}

	/**
	 * Find maklumat penghuni yang telah keluar rumah
	 * */
	public KuaPenghuni getKuaPenghuni(String idPemohon) {
		DbPersistence db = new DbPersistence();
		KuaPenghuni penghuni = null;
		Db db1 = null;
		try {
			db1 = new Db();
			String sql = "select id from kua_penghuni "
					+ " where tarikh_keluar_kuarters is not null "
					+ " and id_pemohon = '" + idPemohon + "' ";
			ResultSet rs = db1.getStatement().executeQuery(sql);
			if (rs.next()) {
				String id = rs.getString("id");
				penghuni = db.find(KuaPenghuni.class, id);
			}
		} catch (Exception e) {
			System.out.println("error getKuaPenghuni() : " + e.getMessage());
		} finally {
			if (db1 != null)
				db1.close();
		}
		return penghuni;
	}
	
	public static String getAlamatPembayar(Users pembayar) {
		String alamatPembayar = "";
		
		if (pembayar.getAlamat1() != null && !pembayar.getAlamat1().trim().equals("")) {
			alamatPembayar = pembayar.getAlamat1();
		}
		if (pembayar.getAlamat2() != null && !pembayar.getAlamat2().trim().equals("")) {
			if (alamatPembayar.equals("")) {
				alamatPembayar = pembayar.getAlamat2();
			} else {
				alamatPembayar = alamatPembayar + " " + pembayar.getAlamat2();
			}			
		}
		if (pembayar.getAlamat3() != null && !pembayar.getAlamat3().trim().equals("")) {
			if (alamatPembayar.equals("")) {
				alamatPembayar = pembayar.getAlamat3();
			} else {
				alamatPembayar = alamatPembayar + " " + pembayar.getAlamat3();
			}			
		}
		if (pembayar.getPoskod() != null && !pembayar.getPoskod().trim().equals("")) {
			if (alamatPembayar.equals("")) {
				alamatPembayar = pembayar.getPoskod();
			} else {
				alamatPembayar = alamatPembayar + ", " + pembayar.getPoskod();
			}			
		}
		if (pembayar.getBandar() != null) {
			if (alamatPembayar.equals("")) {
				alamatPembayar = pembayar.getBandar().getKeterangan();
			} else {
				alamatPembayar = alamatPembayar + " " + pembayar.getBandar().getKeterangan();
			}
			
			if (pembayar.getBandar().getNegeri() != null) {
				if (alamatPembayar.equals("")) {
					alamatPembayar = pembayar.getBandar().getNegeri().getKeterangan();
				} else {
					alamatPembayar = alamatPembayar + ", " + pembayar.getBandar().getNegeri().getKeterangan();
				}			
			}
		}
		
		return alamatPembayar;
	}
	
	public static String getAlamatPembayarLain(PembayarLain pembayar) {
		String alamatPembayar = "";
		
		if (pembayar.getAlamat1() != null && !pembayar.getAlamat1().trim().equals("")) {
			alamatPembayar = pembayar.getAlamat1();
		}
		if (pembayar.getAlamat2() != null && !pembayar.getAlamat2().trim().equals("")) {
			if (alamatPembayar.equals("")) {
				alamatPembayar = pembayar.getAlamat2();
			} else {
				alamatPembayar = alamatPembayar + " " + pembayar.getAlamat2();
			}			
		}
		if (pembayar.getAlamat3() != null && !pembayar.getAlamat3().trim().equals("")) {
			if (alamatPembayar.equals("")) {
				alamatPembayar = pembayar.getAlamat3();
			} else {
				alamatPembayar = alamatPembayar + " " + pembayar.getAlamat3();
			}			
		}
		if (pembayar.getPoskod() != null && !pembayar.getPoskod().trim().equals("")) {
			if (alamatPembayar.equals("")) {
				alamatPembayar = pembayar.getPoskod();
			} else {
				alamatPembayar = alamatPembayar + ", " + pembayar.getPoskod();
			}			
		}
		if (pembayar.getBandar() != null) {
			if (alamatPembayar.equals("")) {
				alamatPembayar = pembayar.getBandar().getKeterangan();
			} else {
				alamatPembayar = alamatPembayar + " " + pembayar.getBandar().getKeterangan();
			}
			
			if (pembayar.getBandar().getNegeri() != null) {
				if (alamatPembayar.equals("")) {
					alamatPembayar = pembayar.getBandar().getNegeri().getKeterangan();
				} else {
					alamatPembayar = alamatPembayar + ", " + pembayar.getBandar().getNegeri().getKeterangan();
				}			
			}
		}
		
		return alamatPembayar;
	}
}
