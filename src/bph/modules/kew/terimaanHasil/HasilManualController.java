package bph.modules.kew.terimaanHasil;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lebah.db.Db;
import lebah.portal.AjaxBasedModule;
import lebah.template.DbPersistence;

import org.apache.log4j.Logger;

import portal.module.entity.Users;
import bph.entities.kew.KewJenisBayaran;
import bph.entities.kewangan.KewBayaranResit;
import bph.entities.kewangan.KewDeposit;
import bph.entities.kewangan.KewInvois;
import bph.entities.kewangan.KewResitKaedahBayaran;
import bph.entities.kewangan.KewResitSenaraiInvois;
import bph.entities.kewangan.KewTempBayar;
import bph.entities.kewangan.KewTempInQueue;
import bph.entities.kod.Bandar;
import bph.entities.kod.Bank;
import bph.entities.kod.CaraBayar;
import bph.entities.kod.KodHasil;
import bph.entities.kod.KodJuruwang;
import bph.entities.kod.KodMesin;
import bph.utils.DataUtil;
import bph.utils.Util;
import bph.utils.UtilKewangan;
import db.persistence.MyPersistence;

public class HasilManualController extends AjaxBasedModule {

	private static final long serialVersionUID = 1L;
	private static String path = "bph/modules/kewangan/terimaanManual/";
	static Logger myLogger = Logger.getLogger(HasilManualController.class);
	protected DbPersistence db;
	private MyPersistence mp;
	private DataUtil dataUtil;

	@SuppressWarnings("unchecked")
	@Override
	public String doTemplate2() throws Exception {

		db = new DbPersistence();
		dataUtil = DataUtil.getInstance(db);
		String userIdLogin = (String) request.getSession().getAttribute(
				"_portal_login");

		clearcontext();

		String vm = "";
		String pageIndex = "index.vm";
		String command = getParam("command");
		context.put("util", new Util());
		context.remove("isEditPayee");

		String carianPembayar = "";
		Users objuser = null;
		// List<KewInvois> listInvois = null;
		int sizeRecord = 0;
		List<Object> listInvoisAsal = null;
		List<KewTempInQueue> listInQueue = null;
		List<KewTempBayar> listTempBayar = null;

		String selectedTab = "1";
		String payerId = getParam("payerId");

		if (command.equalsIgnoreCase("callPopupCarianPembayar")) {

			try {
				mp = new MyPersistence();
				carianPembayar = getParam("carianPembayar");
				List<Users> userList = searchUsers(mp, carianPembayar);
				context.put("userList", userList);
			} finally {
				if (mp != null) { mp.close(); }
			}
			vm = path + "/popup/popupPembayar.vm";

		} else if (command.equalsIgnoreCase("savePilihanPembayar")) {

			try {
				mp = new MyPersistence();
				mp.begin();

				String findPayerId = getParam("radPembayar");
				objuser = (Users) mp.find(Users.class, findPayerId);
				carianPembayar = objuser.getUserName().toUpperCase();
				/** GET MAKLUMAT INVOIS BY PILIHAN USER */

				sizeRecord = getSizeRecord(mp, findPayerId);
				// listInvois = getListInvois(findPayerId);
				listInvoisAsal = getListInvoisAsal(mp, findPayerId, "sewa");
				listInQueue = getListInvoisInQueue(mp, findPayerId);
				listTempBayar = getListTempBayaran(mp, findPayerId);

			} finally {
				if (mp != null) { mp.close(); }
			}

		} else if (command.equalsIgnoreCase("savePilihanBayar")) {

			try {
				mp = new MyPersistence();
				mp.begin();

				objuser = (Users) mp.find(Users.class, payerId);
				String idPilihan = getParam("idPilihan");
				selectedTab = getParam("selectedTab");

				Users us = null;
				KewDeposit dp = null;
				KewInvois ki = null;

				if (selectedTab.equalsIgnoreCase("2")) {
					dp = (KewDeposit) mp.find(KewDeposit.class, idPilihan);
					dp.setFlagQueue("Y");
					us = dp.getPendeposit();
				} else {
					ki = (KewInvois) mp.find(KewInvois.class, idPilihan);
					ki.setFlagQueue("Y");
					us = ki.getPembayar();
				}

				// create temp queue
				KewTempInQueue q = new KewTempInQueue();

				q.setDeposit(dp);
				q.setInvois(ki);
				q.setPembayar(us);
				mp.persist(q);
				// checking kalau bercampur jenis bayaran

				mp.commit();

				if (selectedTab.equalsIgnoreCase("2")) {
					listInvoisAsal = getListInvoisAsal(mp, payerId, "deposit");
				} else {
					listInvoisAsal = getListInvoisAsal(mp, payerId, "sewa");
				}
			} finally {
				if (mp != null) { mp.close(); }
			}

			vm = path + "/senaraiBayaranAsal.vm";

		} else if (command.equalsIgnoreCase("saveAllPilihanBayar")) {

			try {
				mp = new MyPersistence();
				mp.begin();

				objuser = (Users) mp.find(Users.class, payerId);
				listInvoisAsal = getListInvoisAsal(mp, payerId, "sewa");

				for (int i = 0; i < listInvoisAsal.size(); i++) {
					KewInvois inv = (KewInvois) listInvoisAsal.get(i);
					inv.setFlagQueue("Y");
					KewTempInQueue q = new KewTempInQueue();
					q.setInvois(inv);
					q.setPembayar(inv.getPembayar());
					mp.persist(q);
				}
				mp.commit();

				listInvoisAsal = getListInvoisAsal(mp, payerId, "sewa");
			} finally {
				if (mp != null) { mp.close(); }
			}
			vm = path + "/senaraiBayaranAsal.vm";

		} else if (command.equalsIgnoreCase("savePilihanRemoveQueue")) {

			try {
				mp = new MyPersistence();
				mp.begin();
				objuser = (Users) mp.find(Users.class, payerId);
				String idPilihan = getParam("idPilihan");
				KewTempInQueue q = (KewTempInQueue) mp.find(
						KewTempInQueue.class, idPilihan);

				if (q.getDeposit() != null) {
					q.getDeposit().setFlagQueue("T");
				} else {
					q.getInvois().setFlagQueue("T");
				}
				mp.remove(q);
				mp.commit();

				listInQueue = getListInvoisInQueue(mp, payerId);
				listTempBayar = getListTempBayaran(mp, payerId);
			} finally {
				if (mp != null) { mp.close(); }
			}
			vm = path + "/senaraiBayaranPilihan.vm";

		} else if (command.equalsIgnoreCase("removeAllPilihan")) {

			try {
				mp = new MyPersistence();
				mp.begin();
				listInQueue = getListInvoisInQueue(mp, payerId);

				for (int i = 0; i < listInQueue.size(); i++) {
					if (listInQueue.get(i).getInvois() != null)
						listInQueue.get(i).getInvois().setFlagQueue("T");
					else if (listInQueue.get(i).getDeposit() != null)
						listInQueue.get(i).getDeposit().setFlagQueue("T");
					mp.remove(listInQueue.get(i));
				}
				mp.commit();
				listInQueue = getListInvoisInQueue(mp, payerId);
				listTempBayar = getListTempBayaran(mp, payerId);
			} finally {
				if (mp != null) { mp.close(); }
			}
			vm = path + "/senaraiBayaranPilihan.vm";

		} else if (command.equalsIgnoreCase("refreshDivAsal")) {
			try {
				mp = new MyPersistence();
				mp.begin();
				objuser = (Users) mp.find(Users.class, payerId);
				listInvoisAsal = getListInvoisAsal(mp, payerId, "sewa");
				selectedTab = "1";
				mp.commit();
			} finally {
				if (mp != null) { mp.close(); }
			}
			vm = path + "/senaraiBayaranAsal.vm";
		} else if (command.equalsIgnoreCase("refreshDivQueue")) {
			try {
				mp = new MyPersistence();
				objuser = (Users) mp.find(Users.class, payerId);
				listInQueue = getListInvoisInQueue(mp, payerId);
				listTempBayar = getListTempBayaran(mp, payerId);
			} finally {
				if (mp != null) { mp.close(); }
			}
			vm = path + "/senaraiBayaranPilihan.vm";

		} else if (command.equalsIgnoreCase("refreshMaklumatBayaran")) {
			try {
				mp = new MyPersistence();

				objuser = (Users) mp.find(Users.class, payerId);
				listTempBayar = getListTempBayaran(mp, payerId);
				// listInvois = getListInvois(payerId);
				sizeRecord = getSizeRecord(mp, payerId);

			} finally {
				if (mp != null) { mp.close(); }
			}
			vm = path + "/maklumatBayaran.vm";

		} else if (command.equalsIgnoreCase("tambahBayaran")) {
			try {
				mp = new MyPersistence();

				objuser = (Users) mp.find(Users.class, payerId);
				simpanMaklumatBayaran(mp);
				listTempBayar = getListTempBayaran(mp, payerId);
				// listInvois = getListInvois(payerId);
				sizeRecord = getSizeRecord(mp, payerId);

			} finally {
				if (mp != null) { mp.close(); }
			}
			vm = path + "/maklumatBayaran.vm";

		} else if (command.equalsIgnoreCase("hapusTempBayaran")) {
			try {
				mp = new MyPersistence();
				objuser = (Users) mp.find(Users.class, payerId);
				hapusMaklumatBayaran(mp, getParam("idtempBayaran"));
				listTempBayar = getListTempBayaran(mp, payerId);
				// listInvois = getListInvois(payerId);
				sizeRecord = getSizeRecord(mp, payerId);
			} finally {
				if (mp != null) { mp.close(); }
			}
			vm = path + "/maklumatBayaran.vm";

		} else if (command.equalsIgnoreCase("selesaiBayaran")) {
			try {
				mp = new MyPersistence();
				mp.begin();

				objuser = (Users) mp.find(Users.class, payerId);
				carianPembayar = objuser.getUserName().toUpperCase();

				makePayment(mp, userIdLogin, payerId);

				selectedTab = "1";
				// listInvois = getListInvois(payerId);
				sizeRecord = getSizeRecord(mp, payerId);
				listInvoisAsal = getListInvoisAsal(mp, payerId, "sewa");
				listInQueue = getListInvoisInQueue(mp, payerId);
				listTempBayar = getListTempBayaran(mp, payerId);
				mp.commit();
			} finally {
				if (mp != null) { mp.close(); }
			}

		} else if (command.equalsIgnoreCase("callPopupSenaraiResit")) {
			try {
				mp = new MyPersistence();
				objuser = (Users) mp.find(Users.class, payerId);
			} finally {
				if (mp != null) { mp.close(); }
			}
			vm = path + "/popup/senaraiResit.vm";

		} else if (command.equalsIgnoreCase("callPopupBayaranLain")) {
			try {
				mp = new MyPersistence();
				objuser = (Users) mp.find(Users.class, payerId);
				context.put("listKodHasil", dataUtil.getListKodHasil());
			} finally {
				if (mp != null) { mp.close(); }
			}
			vm = path + "/popup/bayaranLain.vm";

		} else if (command.equalsIgnoreCase("saveBayaranLain")) {
			try {
				mp = new MyPersistence();
				mp.begin();
				saveInvoisBayaranLain(mp, userIdLogin, payerId);
				listInQueue = getListInvoisInQueue(mp, payerId);
				listTempBayar = getListTempBayaran(mp, payerId);
				mp.commit();
			} finally {
				if (mp != null) { mp.close(); }
			}
			vm = path + "/senaraiBayaranPilihan.vm";

		} else if (command.equalsIgnoreCase("skrinBayaranSewa")) {
			try {
				mp = new MyPersistence();
				objuser = (Users) mp.find(Users.class, payerId);
				listInvoisAsal = getListInvoisAsal(mp, payerId, "sewa");
				selectedTab = "1";
			} finally {
				if (mp != null) { mp.close(); }
			}
			vm = path + "/senaraiBayaranAsal.vm";
		} else if (command.equalsIgnoreCase("skrinBayaranDeposit")) {
			try {
				mp = new MyPersistence();
				objuser = (Users) mp.find(Users.class, payerId);
				listInvoisAsal = getListInvoisAsal(mp, payerId, "deposit");
				selectedTab = "2";
			} finally {
				if (mp != null) { mp.close(); }
			}
			vm = path + "/senaraiBayaranAsal.vm";
		} else if (command.equalsIgnoreCase("editMaklumatPayer")) {
			try {
				mp = new MyPersistence();
				objuser = (Users) mp.find(Users.class, payerId);
				context.put("listBandar", dataUtil.getListBandar());
				context.put("isEditPayee", "Y");
			} finally {
				if (mp != null) { mp.close(); }
			}
			vm = path + "/maklumatPembayar.vm";
		} else if (command.equalsIgnoreCase("showButtonSave")) {
			try {
				mp = new MyPersistence();
				objuser = (Users) mp.find(Users.class, payerId);
				context.put("isEditPayee", "Y");
			} finally {
				if (mp != null) { mp.close(); }
			}
			vm = path + "/buttonSimpanPayee.vm";
		} else if (command.equalsIgnoreCase("saveMaklumatPayee")) {
			try {
				mp = new MyPersistence();
				objuser = (Users) mp.find(Users.class, payerId);
				context.remove("isEditPayee");
				saveMaklumatPayee(mp, objuser);
			} finally {
				if (mp != null) { mp.close(); }
			}
			vm = path + "/maklumatPembayar.vm";
		} else if (command.equalsIgnoreCase("hideButtonSave")) {
			try {
				mp = new MyPersistence();
				objuser = (Users) mp.find(Users.class, payerId);
				context.put("listBandar", dataUtil.getListBandar());
				context.remove("isEditPayee");
			} finally {
				if (mp != null) { mp.close(); }
			}
			vm = path + "/buttonSimpanPayee.vm";
		} else if (command.equalsIgnoreCase("doCheckJenisBayaran")) {
			String jenisBayaran = getParam("jenisBayaran");
			vm = path + "/maklumatResit.vm";
		}

		// TAB
		context.put("selectedTab", selectedTab);

		/** MAKLUMAT PEMBAYAR */
		context.put("carianPembayar", carianPembayar);
		context.put("pembayar", objuser);

		/** GET SENARAI RESIT */
		List<KewBayaranResit> listResit = null;
		if (objuser != null) {

			try {
				mp = new MyPersistence();
				listResit = mp
						.list("select x from KewBayaranResit x where x.pembayar.id = '"
								+ objuser.getId() + "' ");
			} finally {
				if (mp != null) { mp.close(); }
			}

		}
		context.put("listResit", listResit);

		/** SENARAI BIL/INVOIS */
		// context.put("listInvois", listInvois);
		context.put("sizeRecord", sizeRecord);

		/** SENARAI BIL/INVOIS ASAL */
		context.put("listInvoisAsal", listInvoisAsal);

		/** SENARAI BIL/INVOIS IN QUEUE */
		context.put("listInQueue", listInQueue);

		/** SENARAI MAKLUMAT BAYARAN (table temp) */
		context.put("listTempBayar", listTempBayar);

		context.put("selectModBayaran", dataUtil.getCaraBayar());
		context.put("selectBank", dataUtil.getListBank());
		context.put("selectMesin", dataUtil.getListMesin());
		context.put("listJuruwang", dataUtil.getListJuruwang());

		if (vm == "") {
			vm = path + pageIndex;
		}
		context.put("templateDir", path);
		return vm;

	}

	private void saveInvoisBayaranLain(MyPersistence mp, String userIdLogin,
			String payerId) throws Exception {

		List<KewTempInQueue> listInQueue = null;
		String selectedTab = getParam("selectedTab");
		String idPilihan = getParam("idPilihan");

		KewInvois inv = new KewInvois();

		inv.setDebit(Util.getDoubleRemoveComma(getParam("amaunBayaranLain")));
		inv.setFlagBayaran("SEWA");
		inv.setFlagQueue("Y");
		inv.setJenisBayaran((KewJenisBayaran) mp.find(KewJenisBayaran.class,
				"08"));
		inv.setKeteranganBayaran(getParam("keteranganLain"));
		inv.setKodHasil((KodHasil) mp
				.find(KodHasil.class, getParam("kodHasil")));
		inv.setNoInvois(getParam("noInvoisLain"));

		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		String tarikhDari = getParam("tarikhDari");
		String tarikhHingga = getParam("tarikhHingga");

		if (!tarikhDari.equalsIgnoreCase("") && tarikhDari.length() > 0)
			inv.setTarikhDari(df.parse(tarikhDari));

		if (!tarikhHingga.equalsIgnoreCase("") && tarikhHingga.length() > 0)
			inv.setTarikhHingga(df.parse(tarikhHingga));

		inv.setNoRujukan(getParam("noRujukanLain"));
		inv.setPembayar((Users) mp.find(Users.class, payerId));
		inv.setUserPendaftar((Users) mp.find(Users.class, userIdLogin));
		mp.persist(inv);

		KewTempInQueue q = new KewTempInQueue();
		q.setInvois(inv);
		q.setPembayar((Users) mp.find(Users.class, payerId));
		// q.setDeposit(deposit);
		mp.persist(q);

	}

	private void makePayment(MyPersistence mp, String userIdLogin,
			String payerId) throws Exception {
		boolean flagDeposit = false;
		boolean flagInvois = false;

		try {

			KewBayaranResit resit = new KewBayaranResit();
			List<KewTempInQueue> listInQueue = getListInvoisInQueue(mp, payerId);
			List<KewTempBayar> listModPayment = getListTempBayaran(mp, payerId);
			KodJuruwang kodj = null;
			String noResit = "";
			Users juruwang = (Users) mp.find(Users.class,
					getParam("findJuruwang"));
			DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			String tarikhBayaran = getParam("tarikhBayaran");
			if (juruwang != null) {
				kodj = (KodJuruwang) mp
						.get("select x from KodJuruwang x where x.juruwang.id = '"
								+ juruwang.getId() + "' and x.flagAktif = 'Y'");
			}

			if ("ONLINE".equalsIgnoreCase(getParam("jenisBayaran"))) {
				noResit = UtilKewangan.generateReceiptNoOnline(mp,
						df.parse(tarikhBayaran));
			} else {
				noResit = getParam("noResit");
			}

			Double sum = 0d;
			String kodMesin = null;
			for (int h = 0; h < listModPayment.size(); h++) {
				sum = sum + listModPayment.get(h).getAmaunBayaran();
				if (listModPayment.get(h).getMesin() != null)
					kodMesin = listModPayment.get(h).getMesin().getKodMesin();
			}

			Users pembayar = (Users)mp.find(Users.class, payerId);
			if (pembayar != null) {
				resit.setPembayar(pembayar);
				resit.setNoPengenalanPembayar(pembayar.getId());
				resit.setNamaPembayar(pembayar.getUserName());				
				resit.setAlamatPembayar(UtilKewangan.getAlamatPembayar(pembayar));
			}

			if (!tarikhBayaran.equalsIgnoreCase("") && tarikhBayaran.length() > 0) {
				resit.setTarikhResit(df.parse(tarikhBayaran));	
				resit.setMasaResit(df.parse(tarikhBayaran));
				resit.setTarikhBayaran(df.parse(tarikhBayaran));	
			}				
			resit.setFlagJenisBayaran(getParam("jenisBayaran"));
			resit.setNoResit(noResit);

			if (juruwang != null) {
				resit.setKodJuruwang(kodj); // BUAT ADMIN SCREEN TAMBAH USER	
				resit.setKodPusatTerima(kodj.getKodPusatTerima());
			}
			resit.setTarikhDaftar(new Date());
			resit.setUserPendaftar(juruwang);
			resit.setJumlahAmaunBayaran(sum);
			resit.setKodMesin(kodMesin); // nama pc
			if (kodj != null) {
				resit.setJuruwangKod(kodj.getKodJuruwang());
			} else if (getParam("jenisBayaran").equalsIgnoreCase("ONLINE")) {
				resit.setJuruwangKod("09");
			}
			mp.persist(resit);

			// create record senarai invois dalam 1 resit
			for (int i = 0; i < listInQueue.size(); i++) {

				KewTempInQueue q = listInQueue.get(i);

				if (q.getDeposit() != null) {
					// deposit
					KewResitSenaraiInvois rsi = new KewResitSenaraiInvois();
					rsi.setDeposit(q.getDeposit());
					rsi.setResit(resit);
					rsi.setFlagJenisResit("DEPOSIT");
					mp.persist(rsi);
					flagDeposit = true;
				} else {
					// sewa/invois

					KewResitSenaraiInvois rsi = new KewResitSenaraiInvois();
					rsi.setInvois(q.getInvois());
					rsi.setResit(resit);
					rsi.setFlagJenisResit("INVOIS");
					mp.persist(rsi);
					flagInvois = true;
				}

			}

			// UPDATE FLAGJENISRESIT DI KEWBAYARANRESIT
			if (flagInvois && flagDeposit) {
				resit.setFlagJenisResit("3"); // DEPOSIT AND INVOIS
			} else if (flagInvois) {
				resit.setFlagJenisResit("2"); // INVOIS
			} else if (flagDeposit) {
				resit.setFlagJenisResit("1"); // DEPOSIT
			}

			// create record senarai kaedah bayaran dalam 1 resit
			for (int j = 0; j < listModPayment.size(); j++) {
				createRecordModBayaran(mp, listModPayment.get(j), resit);
				mp.remove(listModPayment.get(j));
			}

			for (int i = 0; i < listInQueue.size(); i++) {

				KewTempInQueue q = listInQueue.get(i);
				if (q.getDeposit() != null) {
					// update lejar modul berkaitan
					UtilKewangan.updateDepositLejarModul(mp, q.getDeposit(),
							resit, q, userIdLogin);
					// update kewdeposit
					updateDeposit(q.getDeposit(), resit, userIdLogin);
				} else {
					// sewa/invois
					// update lejar modul berkaitan
					UtilKewangan.updateInvoisLejarModul(mp, q.getInvois(),
							resit, q, userIdLogin);
					// update kewinvois
					updateInvois(mp, q.getInvois(), userIdLogin);
				}

				mp.remove(listInQueue.get(i));
			}

		} catch (Exception e) {
			System.out.println("error makePayment : " + e.getMessage());
			e.printStackTrace();
		}
		// finally {
		// if (mp != null) { mp.close(); }
		// }

	}

	private void updateDeposit(KewDeposit dep, KewBayaranResit resit,
			String userIdLogin) {
		dep.setFlagBayar("Y");
		dep.setNoResit(resit.getNoResit());
		dep.setTarikhBayaran(resit.getTarikhBayaran());
		dep.setTarikhResit(resit.getTarikhResit());
	}

	private void updateInvois(MyPersistence mp, KewInvois inv,
			String userIdLogin) {
		inv.setKredit(inv.getDebit());
		inv.setUserKemaskini((Users) mp.find(Users.class, userIdLogin));
		inv.setTarikhKemaskini(new Date());
		inv.setFlagBayar("Y");
	}

	private void createRecordModBayaran(MyPersistence mp, KewTempBayar temp,
			KewBayaranResit resit) throws Exception {
		KewResitKaedahBayaran rkb = new KewResitKaedahBayaran();
		rkb.setResit(resit);
		rkb.setAmaunBayaran(temp.getAmaunBayaran());
		rkb.setBank(temp.getBank());
		rkb.setModBayaran(temp.getModBayaran());
		// rkb.setNoCek();
		// rkb.setNoLoTempahan();
		rkb.setNoRujukan(temp.getNoRujukan());
		rkb.setTempat(temp.getTempat());
		rkb.setTarikhCek(temp.getTarikhCek());
		rkb.setNoCek(temp.getNoCek());
		mp.persist(rkb);
	}

	private void simpanMaklumatBayaran(MyPersistence mp) throws Exception {
		mp.begin();
		KewTempBayar temp = new KewTempBayar();

		temp.setPembayar((Users) mp.find(Users.class, getParam("payerId")));
		temp.setAmaunBayaran(Util
				.getDoubleRemoveComma(getParam("amaunBayaran")));
		temp.setBank((Bank) mp.find(Bank.class, getParam("bank")));
		temp.setModBayaran((CaraBayar) mp.find(CaraBayar.class,
				getParam("modBayaran")));
		temp.setNoRujukan(getParam("noRujukan"));
		temp.setTempat(getParam("tempat"));
		temp.setMesin((KodMesin) mp.find(KodMesin.class, getParam("mesin")));
		temp.setNoCek(getParam("noCek"));

		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		String tarikhCek = getParam("tarikhCek");
		if (!tarikhCek.equalsIgnoreCase("") && tarikhCek.length() > 0)
			temp.setTarikhCek(df.parse(tarikhCek));

		mp.persist(temp);
		mp.commit();
	}

	private void hapusMaklumatBayaran(MyPersistence mp, String idtempBayaran) {

		try {
			mp.begin();
			KewTempBayar temp = (KewTempBayar) mp.find(KewTempBayar.class,
					idtempBayaran);
			mp.remove(temp);
			mp.commit();
		} catch (Exception e) {
			System.out
					.println("error hapusMaklumatBayaran : " + e.getMessage());
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private List<KewTempBayar> getListTempBayaran(MyPersistence mp,
			String payerId) throws Exception {
		List<KewTempBayar> list = mp
				.list("select x from KewTempBayar x where x.pembayar.id = '"
						+ payerId + "' ");
		return list;
	}

	@SuppressWarnings("unchecked")
	private List<KewInvois> getListInvois(MyPersistence mp, String payerId)
			throws Exception {
		List<KewInvois> list = mp
				.list("select x from KewInvois x where x.pembayar.id = '"
						+ payerId + "' and COALESCE(x.flagBayar,'T') <> 'Y' ");
		return list;
	}

	@SuppressWarnings("unchecked")
	private int getSizeRecord(MyPersistence mp, String payerId)
			throws Exception {

		int size = 0;

		List<KewInvois> list = mp
				.list("select x from KewInvois x where x.pembayar.id = '"
						+ payerId + "' and COALESCE(x.flagBayar,'T') <> 'Y' ");
		if (list != null) {
			size = size + 1;
		}

		List<KewDeposit> listd = mp
				.list("select x from KewDeposit x where x.pendeposit.id = '"
						+ payerId + "' and COALESCE(x.flagBayar,'T') <> 'Y' ");
		if (listd != null) {
			size = size + 1;
		}

		return size;
	}

	@SuppressWarnings("unchecked")
	private List<Object> getListInvoisAsal(MyPersistence mp, String payerId,
			String type) throws Exception {
		List<Object> list = new ArrayList<Object>();
		if (type.equalsIgnoreCase("sewa")) {
			list = mp
					.list("select x from KewInvois x where x.pembayar.id = '"
							+ payerId
							+ "' and COALESCE(x.flagQueue,'T') = 'T' and COALESCE(x.flagBayar,'T') <> 'Y' ");
		} else if (type.equalsIgnoreCase("deposit")) {
			list = mp
					.list("select x from KewDeposit x where x.pendeposit.id = '"
							+ payerId
							+ "' and COALESCE(x.flagQueue,'T') = 'T' and COALESCE(x.flagBayar,'T') = 'T' ");
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	private List<KewTempInQueue> getListInvoisInQueue(MyPersistence mp,
			String payerId) throws Exception {
		// List<KewInvois> list =
		// db.list("select x from KewInvois x where x.pembayar.id = '"+payerId+"' and COALESCE(x.flagQueue,'T') = 'Y' and COALESCE(x.flagBayar,'T') <> 'Y' ");
		List<KewTempInQueue> list = mp
				.list("select x from KewTempInQueue x where x.pembayar.id = '"
						+ payerId + "' ");
		return list;
	}

	private void clearcontext() {
		context.remove("carianPembayar");
		context.remove("pembayar");
		context.remove("listInvois");
		context.remove("listInvoisAsal");
		context.remove("listInQueue");
		context.remove("listTempBayar");
	}

	private void saveMaklumatPayee(MyPersistence mp, Users objUser) {
		if (objUser != null) {

			try {
				objUser.setAlamat1(getParam("alamat1"));
				objUser.setAlamat2(getParam("alamat2"));
				objUser.setAlamat3(getParam("alamat3"));
				objUser.setPoskod(getParam("poskod"));
				objUser.setBandar((Bandar) mp.find(Bandar.class,
						getParam("bandar")));
				objUser.setNoTelefonBimbit(getParam("noTelefonBimbit"));
				mp.persist(objUser);
			} catch (Exception e) {
				System.out.println("error saveMaklumatPayee : "
						+ e.getMessage());
				e.printStackTrace();
			}

		}
	}

	private List<Users> searchUsers(MyPersistence mp, String param) {
		ArrayList<Users> list = new ArrayList<Users>();
		Db db1 = null;
		try {
			db1 = new Db();

			String sql = "SELECT DISTINCT b.user_login FROM kew_invois a, users b "
					+ " WHERE a.id_pembayar = b.user_login "
					+ " AND (upper(b.user_name) LIKE upper('%"
					+ param
					+ "%') OR upper(b.no_kp) LIKE upper('%"
					+ param
					+ "%') OR upper(a.no_invois) LIKE upper('%"
					+ param
					+ "%')) "
					+ "union "
					+ "select user_login c from users c where "
					+ "(upper(c.user_name) LIKE upper('%"
					+ param
					+ "%') OR upper(c.no_kp) LIKE upper('%" + param + "%'))";

			// String sql =
			// "SELECT DISTINCT a.user_login FROM users a left join kew_invois b on a.user_login = b.id_pembayar "+
			// " WHERE (upper(user_name) LIKE upper('%"+param+"%') OR upper(no_kp) LIKE upper('%"+param+"%') OR upper(b.no_invois) LIKE upper('%"+param+"%')) ";

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

}
