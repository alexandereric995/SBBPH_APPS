package bph.modules.kewangan.terimaanHasil;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;

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
import bph.entities.rk.RkFail;
import bph.entities.rk.RkIndividu;
import bph.entities.rk.RkInvois;
import bph.entities.rk.RkPemohon;
import bph.entities.rk.RkSyarikat;
import bph.modules.rk.UtilRk;
import bph.utils.DataUtil;
import bph.utils.Util;
import bph.utils.UtilKewangan;
import db.persistence.MyPersistence;

public class KaunterController extends AjaxBasedModule {

	private static final long serialVersionUID = 1L;
	private static String path = "bph/modules/kewangan/kaunter/";
	protected DbPersistence db;
	private MyPersistence mp;
	
	private DataUtil dataUtil;
	
	static Logger myLogger = Logger.getLogger(KaunterController.class);

	@SuppressWarnings("unchecked")
	@Override
	public String doTemplate2() throws Exception {
		
		db = new DbPersistence();
		dataUtil = DataUtil.getInstance(db);
		
		clearcontext();
		
		String userIdLogin = (String) request.getSession().getAttribute("_portal_login");
		String vm = "";
		String pageIndex = "index.vm";
		String command = getParam("command");
		context.put("util", new Util());
		context.put("templateDir", path);
		this.context.put("uploadDir", ResourceBundle.getBundle("dbconnection").getString("folder"));
		
		String carianPembayar = "";
		Users objuser = null;
		
		int sizeRecord = 0;
		List<Object> listInvoisAsal = null;
		List<KewTempInQueue> listInQueue = null;
		List<KewTempBayar> listTempBayar = null;

		String selectedTab = "1";
		String payerId = getParam("payerId");
		
		try {
			mp = new MyPersistence();

			KodJuruwang juruwang = (KodJuruwang) db.get("select x from KodJuruwang x where x.juruwang.id = '" + userIdLogin + "' and x.flagAktif = 'Y'");
			context.put("juruwang", juruwang);
			
			if (command.equalsIgnoreCase("callPopupCarianPembayar")) {
				carianPembayar = getParam("carianPembayar");
				List<Users> userList = searchUsers(mp, carianPembayar);
				context.put("userList", userList);
				
				vm = path + "popup/popupPembayar.vm";
				
			} else if (command.equalsIgnoreCase("savePilihanPembayar")) {
				
				String findPayerId = getParam("radPembayar");
				objuser = (Users) mp.find(Users.class, findPayerId);
				carianPembayar = objuser.getUserName().toUpperCase();

				sizeRecord = getSizeRecord(mp,findPayerId);
				listInvoisAsal = getListInvoisAsal(mp,findPayerId, "sewa");
				listInQueue = getListInvoisInQueue(mp,findPayerId);
				listTempBayar = getListTempBayaran(mp,findPayerId);
			
			} else if (command.equalsIgnoreCase("savePilihanBayar")) {
				
				objuser = (Users)mp.find(Users.class, payerId);
				String idPilihan = getParam("idPilihan");
				selectedTab = getParam("selectedTab");

				Users us = null;
				KewDeposit dp = null;
				KewInvois ki = null;
				
				if (selectedTab.equalsIgnoreCase("2")) {
					dp = (KewDeposit)mp.find(KewDeposit.class, idPilihan);
					if (dp !=null) {
						dp.setFlagQueue("Y");
						us = dp.getPendeposit();
					}
				} else {
					ki = (KewInvois)mp.find(KewInvois.class, idPilihan);
					if (ki != null) {
						ki.setFlagQueue("Y");
						us = ki.getPembayar();
					}
				}
				
				// create temp queue
				createTempQue(us, dp, ki, mp);
				
				// checking kalau bercampur jenis bayaran
				if (selectedTab.equalsIgnoreCase("2")) {
					listInvoisAsal = getListInvoisAsal(mp, payerId, "deposit");
				} else {
					listInvoisAsal = getListInvoisAsal(mp, payerId, "sewa");
				}
				
				vm = path + "senaraiBayaranAsal.vm";
			
			} else if (command.equalsIgnoreCase("saveAllPilihanBayar")) {
								
				objuser = (Users)mp.find(Users.class, payerId);
				listInvoisAsal = getListInvoisAsal(mp, payerId, "sewa");

				mp.begin();
				for (int i = 0; i < listInvoisAsal.size(); i++) {
					//create tempinque
					KewInvois inv = (KewInvois) listInvoisAsal.get(i);
					inv.setFlagQueue("Y");
					KewTempInQueue q = new KewTempInQueue();
					q.setInvois(inv);
					q.setPembayar(inv.getPembayar());
					q.setAmaunBayaran(inv.getDebit());
					q.setTarikhDari(inv.getTarikhDari());
					q.setTarikhHingga(inv.getTarikhHingga());
					mp.persist(q);
				}
				mp.commit();

				listInvoisAsal = getListInvoisAsal(mp, payerId, "sewa");
				vm = path + "senaraiBayaranAsal.vm";
				
			} else if (command.equalsIgnoreCase("savePilihanRemoveQueue")) {
				
				objuser = (Users)mp.find(Users.class, payerId);
				String idPilihan = getParam("idPilihan");
				KewTempInQueue q = (KewTempInQueue)mp.find(KewTempInQueue.class, idPilihan);

				//AZAM 
				mp.begin();
				if (q != null) {
					if (q.getDeposit() != null) {
						q.getDeposit().setFlagQueue("T");
					} else {
						q.getInvois().setFlagQueue("T");
					}
					mp.remove(q);
				}
				mp.commit();

				listInQueue = getListInvoisInQueue(mp, payerId);
				listTempBayar = getListTempBayaran(mp, payerId);
				
				vm = path + "senaraiBayaranPilihan.vm";
			
			} else if (command.equalsIgnoreCase("removeAllPilihan")) {
				
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
				
				vm = path + "/senaraiBayaranPilihan.vm";
			
			} else if (command.equalsIgnoreCase("refreshDivAsal")) {
				
				objuser = (Users)mp.find(Users.class, payerId);
				listInvoisAsal = getListInvoisAsal(mp,payerId, "sewa");
				
				vm = path + "senaraiBayaranAsal.vm";
			
			} else if (command.equalsIgnoreCase("refreshDivQueue")) {
				
				objuser = (Users)mp.find(Users.class, payerId);
				listInQueue = getListInvoisInQueue(mp, payerId);
				listTempBayar = getListTempBayaran(mp, payerId);
				
				vm = path + "senaraiBayaranPilihan.vm";
			
			} else if (command.equalsIgnoreCase("refreshMaklumatBayaran")) {
				
				objuser = (Users)mp.find(Users.class, payerId);
				listTempBayar = getListTempBayaran(mp, payerId);
				sizeRecord = getSizeRecord(mp,payerId);
				
				vm = path + "maklumatBayaran.vm";
			
			} else if (command.equalsIgnoreCase("tambahBayaran")) {
				
				objuser = (Users)mp.find(Users.class, payerId);
				simpanMaklumatBayaran(mp);
				listTempBayar = getListTempBayaran(mp, payerId);
				sizeRecord = getSizeRecord(mp,payerId);
				
				vm = path + "maklumatBayaran.vm";
			
			} else if (command.equalsIgnoreCase("hapusTempBayaran")) {
				
				objuser = (Users)mp.find(Users.class, payerId);
				hapusMaklumatBayaran(mp, getParam("idtempBayaran"));
				listTempBayar = getListTempBayaran(mp, payerId);
				sizeRecord = getSizeRecord(mp,payerId);
				
				vm = path + "maklumatBayaran.vm";
			
			} else if (command.equalsIgnoreCase("selesaiBayaran")) {
				
				objuser = (Users)mp.find(Users.class, payerId);
				carianPembayar = objuser.getUserName().toUpperCase();

				makePayment(mp,userIdLogin, payerId);
				context.put("errorMakePayment", "T");

				selectedTab = "1";
				sizeRecord = getSizeRecord(mp,payerId);
				listInvoisAsal = getListInvoisAsal(mp, payerId, "sewa"); 
				listInQueue = getListInvoisInQueue(mp, payerId);
				listTempBayar = getListTempBayaran(mp, payerId);
				
			} else if (command.equalsIgnoreCase("callPopupSenaraiResit")) {
				
				objuser =(Users) mp.find(Users.class, payerId);
				List<KewBayaranResit> listResit = null;
				if (objuser != null) {
					listResit = mp.list("select x from KewBayaranResit x where x.pembayar.id = '"+ objuser.getId() + "' order by x.tarikhBayaran desc");
				}
				context.put("listResit", listResit);
				
				vm = path + "popup/senaraiResit.vm";
			
			} else if (command.equalsIgnoreCase("callPopupBayaranLain")) {
				
				objuser = (Users)mp.find(Users.class, payerId);
				context.put("listKodHasil", dataUtil.getListKodHasil());
				
				vm = path + "popup/bayaranLain.vm";
			
			} else if (command.equalsIgnoreCase("saveBayaranLain")) {
				
				saveInvoisBayaranLain(mp,userIdLogin, payerId);
				listInQueue = getListInvoisInQueue(mp, payerId);
				listTempBayar = getListTempBayaran(mp, payerId);
				
				vm = path + "senaraiBayaranPilihan.vm";
			
			} else if (command.equalsIgnoreCase("skrinBayaranSewa")) {
				
				objuser = (Users)mp.find(Users.class, payerId);
				listInvoisAsal = getListInvoisAsal(mp, payerId, "sewa");
				selectedTab = "1";
				
				vm = path + "senaraiBayaranAsal.vm";
			
			} else if (command.equalsIgnoreCase("skrinBayaranDeposit")) {
				
				objuser = (Users)mp.find(Users.class, payerId);
				listInvoisAsal = getListInvoisAsal(mp, payerId, "deposit");
				selectedTab = "2";
				
				vm = path + "senaraiBayaranAsal.vm";
			
			} else if (command.equalsIgnoreCase("editMaklumatPayer")) {
				
				objuser = (Users)mp.find(Users.class, payerId);
				context.put("listBandar", dataUtil.getListBandar());
				context.put("isEditPayee", "Y");
				
				vm = path + "maklumatPembayar.vm";
			
			} else if (command.equalsIgnoreCase("showButtonSave")) {
				
				objuser = (Users)mp.find(Users.class, payerId);
				context.put("isEditPayee", "Y");
				
				vm = path + "buttonSimpanPayee.vm";
			
			} else if (command.equalsIgnoreCase("saveMaklumatPayee")) {
				
				objuser = (Users)mp.find(Users.class, payerId);
				context.remove("isEditPayee");
				saveMaklumatPayee(mp,objuser);
				
				vm = path + "maklumatPembayar.vm";
			
			} else if (command.equalsIgnoreCase("hideButtonSave")) {
				
				objuser = (Users)mp.find(Users.class, payerId);
				context.put("listBandar", dataUtil.getListBandar());
				context.remove("isEditPayee");
				
				vm = path + "buttonSimpanPayee.vm";
			
			} else if (command.equalsIgnoreCase("doCetakInvoisRK")) {
				
				String id = getParam("id");
				KewInvois invois = (KewInvois) mp.find(KewInvois.class, id);
				if (invois != null) {
					RkInvois invoisRK = (RkInvois) mp.get("select x from RkInvois x where x.flagAktif = 'Y' and x.noInvois = '" + invois.getNoInvois() + "' and x.fail.id = '" + invois.getIdLejar() + "'");
					if (invoisRK != null) {
						context.put("idInvois", invoisRK.getId());
					}
				}
				
				vm = path + "popup/cetakInvoisRK.vm";
			} else if (command.equalsIgnoreCase("doCetakInvoisIWKRK")) {
				
				String id = getParam("id");
				KewInvois invois = (KewInvois) mp.find(KewInvois.class, id);
				if (invois != null) {
					RkInvois invoisRK = (RkInvois) mp.get("select x from RkInvois x where x.noInvois = '" + invois.getNoInvois() + "' and x.fail.id = '" + invois.getIdLejar() + "'");
					if (invoisRK != null) {
						context.put("idInvois", invoisRK.getId());
					}
				}
				
				vm = path + "popup/cetakInvoisIWKRK.vm";
			} else if (command.equalsIgnoreCase("doChangeAmaunBayaran")) {
				
				listTempBayar = getListTempBayaran(mp, payerId);
				listInQueue = getListInvoisInQueue(mp, payerId);
				mp.begin();
				for (KewTempInQueue tempInQ : listInQueue) {
					String label = "amaunAkanBayar" + tempInQ.getId();
					tempInQ.setAmaunBayaran(Double.valueOf(Util.RemoveComma(getParam(label))));					
				}
				mp.commit();
				vm = path + "senaraiBayaranPilihan.vm";
				
			} else if (command.equalsIgnoreCase("doChangeTempoh")) {
				
				listTempBayar = getListTempBayaran(mp, payerId);
				listInQueue = getListInvoisInQueue(mp, payerId);
				mp.begin();
				for (KewTempInQueue tempInQ : listInQueue) {
					String labelTarikhDari = "tarikhDari" + tempInQ.getId();
					String labelTarikhHingga = "tarikhHingga" + tempInQ.getId();
					tempInQ.setTarikhDari(getDate(labelTarikhDari));	
					tempInQ.setTarikhHingga(getDate(labelTarikhHingga));
				}
				mp.commit();
				vm = path + "senaraiBayaranPilihan.vm";
			}
			
		} catch (Exception ex) {
			if ("selesaiBayaran".equals(command)) {
				context.put("errorMakePayment", "Y");
			}
			ex.printStackTrace();
		} finally {
			if (mp != null)
				mp.close();
		}
		
		// TAB
		context.put("selectedTab", selectedTab);

		/** MAKLUMAT PEMBAYAR */
		context.put("carianPembayar", carianPembayar);
		context.put("pembayar", objuser);
		
		/** SENARAI BIL/INVOIS */
		context.put("sizeRecord", sizeRecord);

		/** SENARAI BIL/INVOIS ASAL */
		context.put("listInvoisAsal", listInvoisAsal);

		/** SENARAI BIL/INVOIS IN QUEUE */
		context.put("listInQueue", listInQueue);

		/** SENARAI MAKLUMAT BAYARAN (table temp) */
		context.put("listTempBayar", listTempBayar);

		context.put("selectModBayaran", dataUtil.getCaraBayarKaunter());
		context.put("selectBank", dataUtil.getListBank());
		context.put("selectMesin", dataUtil.getListMesin());
		
		if ("".equals(vm)) {
			vm = path + "index.vm";
		}
		
		return vm;
	}

	private Date getDate(String strDate) throws ParseException {
		Date dt = null;
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		String tarikh = getParam(strDate);
		if(!tarikh.equalsIgnoreCase("") && tarikh.length() > 0) {
			dt = df.parse(tarikh);
		}
		return dt;
	}

	private void clearcontext() {
		context.remove("errorMakePayment");
		context.remove("isEditPayee");
		context.remove("carianPembayar");
		context.remove("pembayar");
		context.remove("listInvois");
		context.remove("listInvoisAsal");
		context.remove("listInQueue");
		context.remove("listTempBayar");
	}
	
	private List<Users> searchUsers(MyPersistence mp, String param) {
		ArrayList<Users> list = new ArrayList<Users>();
		Db dbCarianPembayar = null;
		try {
			dbCarianPembayar = new Db();

			String sql = "SELECT DISTINCT b.user_login FROM kew_invois a, users b"
					+ " WHERE a.id_pembayar = b.user_login"
					+ " AND (upper(b.user_name) LIKE upper('%" + param + "%')"
					+ " OR upper(b.no_kp) LIKE upper('%" + param + "%')"
					+ " OR upper(a.no_invois) LIKE upper('%" + param + "%'))"
					+ " union "
					+ " select user_login c from users c" 
					+ " WHERE (upper(c.user_name) LIKE upper('%" + param + "%')"
					+ " OR upper(c.no_kp) LIKE upper('%" + param + "%'))";
			ResultSet rs = dbCarianPembayar.getStatement().executeQuery(sql);
			while (rs.next()) {
				Users us = (Users)mp.find(Users.class, rs.getString("user_login"));
				if (us != null)
					list.add(us);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dbCarianPembayar != null)
				dbCarianPembayar.close();
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	private int getSizeRecord(MyPersistence mp, String payerId) throws Exception {

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
	private List<Object> getListInvoisAsal(MyPersistence mp,String payerId, String type)
			throws Exception {
		List<Object> list = new ArrayList<Object>();
		if (type.equalsIgnoreCase("sewa")) {
			list = mp
					.list("select x from KewInvois x where x.pembayar.id = '"
							+ payerId
							+ "' and COALESCE(x.flagQueue,'T') = 'T' and COALESCE(x.flagBayar,'T') = 'T'");
			
			// UNTUK RUANG KOMERSIL - LEJAR DIDAFTARKAN ATAS NAMA SYARIKAT, NAMUN CARIAN BERDASARKAN NAMA PEGAWAI BERTANGGUNGJAWAB
			RkPemohon pemohon = (RkPemohon) mp.get("select x from RkPemohon x where x.individu.id = '" + payerId + "' or x.syarikat.id = '" + payerId + "'");
			if (pemohon != null) {
				List<KewInvois> listInvois = mp.list("select x from KewInvois x where x.pembayar.id in ('" + pemohon.getIndividu().getId() + "', '" + pemohon.getSyarikat().getId() + "') and COALESCE(x.flagQueue,'T') = 'T' and COALESCE(x.flagBayar,'T') = 'T'");
				for(KewInvois invois: listInvois){
				    if(!list.contains(invois))
				    	list.add(invois);
				}
			}
		} else if (type.equalsIgnoreCase("deposit")) {
			list = mp
					.list("select x from KewDeposit x where x.pendeposit.id = '"
							+ payerId
							+ "' and COALESCE(x.flagQueue,'T') = 'T' and COALESCE(x.flagBayar,'T') = 'T'");
			
			// UNTUK RUANG KOMERSIL - LEJAR DIDAFTARKAN ATAS NAMA SYARIKAT, NAMUN CARIAN BERDASARKAN NAMA PEGAWAI BERTANGGUNGJAWAB
			RkPemohon pemohon = (RkPemohon) mp.get("select x from RkPemohon x where x.individu.id = '" + payerId + "' or x.syarikat.id = '" + payerId + "'");
			if (pemohon != null) {
				List<KewDeposit> listDeposit = mp.list("select x from KewDeposit x where x.pendeposit.id in ('" + pemohon.getIndividu().getId() + "', '" + pemohon.getSyarikat().getId() + "') and COALESCE(x.flagQueue,'T') = 'T' and COALESCE(x.flagBayar,'T') = 'T'");
				for(KewDeposit deposit: listDeposit){
				    if(!list.contains(deposit))
				    	list.add(deposit);
				}
			}
		}

		return list;
	}
	
	private List<KewTempInQueue> getListInvoisInQueue(MyPersistence mp,String payerId)
			throws Exception {
		List<KewTempInQueue> list = mp.list("select x from KewTempInQueue x where x.pembayar.id = '"+ payerId + "' ");
		
		// UNTUK RUANG KOMERSIL - LEJAR DIDAFTARKAN ATAS NAMA SYARIKAT, NAMUN CARIAN BERDASARKAN NAMA PEGAWAI BERTANGGUNGJAWAB
		RkPemohon pemohon = (RkPemohon) mp.get("select x from RkPemohon x where x.individu.id = '" + payerId + "' or x.syarikat.id = '" + payerId + "'");
		if (pemohon != null) {
			List<KewTempInQueue> listTempInQ = mp.list("select x from KewTempInQueue x where x.pembayar.id in ('" + pemohon.getIndividu().getId() + "', '" + pemohon.getSyarikat().getId() + "')");
			for(KewTempInQueue tempInQ: listTempInQ){
			    if(!list.contains(tempInQ))
			    	list.add(tempInQ);
			}
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	private List<KewTempBayar> getListTempBayaran(MyPersistence mp,String payerId)
			throws Exception {
		List<KewTempBayar> list = mp
				.list("select x from KewTempBayar x where x.pembayar.id = '"
						+ payerId + "' ");
		return list;
	}
	
	private void createTempQue(Users us, KewDeposit dp, KewInvois ki, MyPersistence mp) throws Exception {
		mp.begin();
		KewTempInQueue q = new KewTempInQueue();
		q.setDeposit(dp);
		q.setInvois(ki);
		q.setPembayar(us);
		if (dp != null) {
			q.setAmaunBayaran(dp.getJumlahDeposit());
			q.setTarikhDari(dp.getTarikhDari());
			q.setTarikhHingga(dp.getTarikhHingga());
		} else {
			q.setAmaunBayaran(ki.getDebit());
			q.setTarikhDari(ki.getTarikhDari());
			q.setTarikhHingga(ki.getTarikhHingga());
		}

		mp.persist(q);
		mp.commit();		
	}
	
	private void simpanMaklumatBayaran(MyPersistence mp) throws Exception {
		mp.begin();
		KewTempBayar temp = new KewTempBayar();

		temp.setPembayar((Users)mp.find(Users.class, getParam("payerId")));
		temp.setAmaunBayaran(Util.getDoubleRemoveComma(getParam("amaunBayaran")));
		temp.setBank((Bank)mp.find(Bank.class, getParam("bank")));
		temp.setModBayaran((CaraBayar)mp.find(CaraBayar.class, getParam("modBayaran")));
		temp.setNoRujukan(getParam("noRujukan"));
		temp.setTempat(getParam("tempat"));
		temp.setMesin((KodMesin)mp.find(KodMesin.class, getParam("mesin")));
		temp.setNoCek(getParam("noCek"));

		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		String tarikhCek = getParam("tarikhCek");
		if(!tarikhCek.equalsIgnoreCase("") && tarikhCek.length() > 0)
			temp.setTarikhCek(df.parse(tarikhCek));

		mp.persist(temp);
		mp.commit();
	}
	
	private void hapusMaklumatBayaran(MyPersistence mp,String idtempBayaran) throws Exception {
		mp.begin();
		KewTempBayar temp = (KewTempBayar)mp.find(KewTempBayar.class, idtempBayaran);
		mp.remove(temp);
		mp.commit();
	}

	@SuppressWarnings("null")
	private void makePayment(MyPersistence mp,String userIdLogin, String payerId) throws Exception {
		boolean flagDeposit = false;
		boolean flagInvois = false;
		
		mp.begin();

		KewBayaranResit resit = new KewBayaranResit();
		List<KewTempInQueue> listInQueue = getListInvoisInQueue(mp,payerId);
		List<KewTempBayar> listModPayment = getListTempBayaran(mp,payerId);
		
		//ADD BY PEJE - UNTUK DAPATKAN SENARAI FAIL RK YANG TERLIBAT DENGAN BAYARAN
		List<RkFail> listFailRuangKomersil = new ArrayList<>();
		
		Users juruwang = (Users)mp.find(Users.class, userIdLogin);
		KodJuruwang kodj = (KodJuruwang) mp.get("select x from KodJuruwang x where x.juruwang.id = '"+ juruwang.getId() + "' and x.flagAktif = 'Y'");
		
		Double sum = 0d;
		String kodMesin = null;
		for (int h = 0; h < listModPayment.size(); h++) {
			sum = sum + listModPayment.get(h).getAmaunBayaran();
			kodMesin = listModPayment.get(h).getMesin().getKodMesin();
		}
		
		String noResit = UtilKewangan.generateReceiptNo(mp,juruwang);
		Users pembayar = (Users)mp.find(Users.class, payerId);
		if (pembayar != null) {
			resit.setPembayar(pembayar);
			resit.setNoPengenalanPembayar(pembayar.getId());
			resit.setNamaPembayar(pembayar.getUserName());				
			resit.setAlamatPembayar(UtilKewangan.getAlamatPembayar(pembayar));
		}		
		resit.setNoResit(noResit);
		resit.setTarikhBayaran(new Date());
		resit.setTarikhResit(new Date());
		resit.setMasaResit(new Date());
		resit.setFlagJenisBayaran("KAUNTER");
		resit.setKodJuruwang(kodj); // BUAT ADMIN SCREEN TAMBAH USER
		resit.setKodPusatTerima(kodj.getKodPusatTerima());
		resit.setTarikhDaftar(new Date());
		resit.setUserPendaftar(juruwang);
		resit.setJumlahAmaunBayaran(sum);
		resit.setKodMesin(kodMesin); // nama pc
		if (kodj != null) {
			resit.setJuruwangKod(kodj.getKodJuruwang());
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
				mp.flush();
				flagDeposit = true;
			} else {
				// sewa/invois
				KewResitSenaraiInvois rsi = new KewResitSenaraiInvois();
				rsi.setInvois(q.getInvois());
				rsi.setResit(resit);
				rsi.setFlagJenisResit("INVOIS");
				mp.persist(rsi);
				mp.flush();
				flagInvois = true;
			}
		}
			
		//UPDATE FLAGJENISRESIT DI KEWBAYARANRESIT
		if (flagInvois && flagDeposit) {
			resit.setFlagJenisResit("3"); //DEPOSIT AND INVOIS
		} else if (flagInvois) {
			resit.setFlagJenisResit("2"); //INVOIS
		} else if (flagDeposit) {
			resit.setFlagJenisResit("1"); //DEPOSIT
		}

		// create record senarai kaedah bayaran dalam 1 resit
		for (int j = 0; j < listModPayment.size(); j++) {
			createRecordModBayaran(mp,listModPayment.get(j), resit);
			mp.remove((KewTempBayar)listModPayment.get(j));
		}
		
		for (int i = 0; i < listInQueue.size(); i++) {

			KewTempInQueue q = listInQueue.get(i);
			if (q.getDeposit() != null) {
				// update lejar modul berkaitan
				UtilKewangan.updateDepositLejarModul(mp,q.getDeposit(), resit, q, userIdLogin);
				// update kewdeposit
				updateDeposit(q.getDeposit(), resit, q, userIdLogin);				
			} else {
				// sewa/invois
				// update lejar modul berkaitan
				UtilKewangan.updateInvoisLejarModul(mp, q.getInvois(), resit, q, userIdLogin);
				// update kewinvois
				updateInvois(mp, q.getInvois(), q, userIdLogin);
				
				//ADD BY PEJE - UNTUK DAPATKAN SENARAI FAIL RK YANG TERLIBAT DENGAN BAYARAN
				if (q.getInvois().getIdLejar() != null) {
					RkFail fail = (RkFail) mp.find(RkFail.class, q.getInvois().getIdLejar());
					if (fail != null) {
						if(!listFailRuangKomersil.contains(fail)) {
							listFailRuangKomersil.add(fail);
						}
					}
				}				
			}

			mp.remove(listInQueue.get(i));
		}
		mp.commit();
		
		for (RkFail fail : listFailRuangKomersil) {
			ServletContext servletContext = getServletContext();
			UtilRk.kemaskiniRekodPerjanjianDanAkaun(fail, false, false, mp, servletContext);
		}		
	}
	
	private void createRecordModBayaran(MyPersistence mp,KewTempBayar temp, KewBayaranResit resit) throws Exception {
		KewResitKaedahBayaran rkb = new KewResitKaedahBayaran();
		rkb.setResit(resit);
		rkb.setAmaunBayaran(temp.getAmaunBayaran());
		rkb.setBank(temp.getBank());
		rkb.setModBayaran(temp.getModBayaran());
		rkb.setNoRujukan(temp.getNoRujukan());
		rkb.setTempat(temp.getTempat());
		rkb.setTarikhCek(temp.getTarikhCek());
		rkb.setNoCek(temp.getNoCek());
		mp.persist(rkb);
	}
	
	private void updateDeposit(KewDeposit dep, KewBayaranResit resit,
			KewTempInQueue q, String userIdLogin) {
		dep.setFlagBayar("Y");
		dep.setFlagQueue("T");
		dep.setBakiDeposit(q.getAmaunBayaran());
		dep.setNoResit(resit.getNoResit());
		dep.setTarikhBayaran(resit.getTarikhBayaran());
		dep.setTarikhResit(resit.getTarikhResit());
	}

	private void updateInvois(MyPersistence mp, KewInvois inv, KewTempInQueue q, String userIdLogin) {
		inv.setKredit(q.getAmaunBayaran());
		inv.setUserKemaskini((Users)mp.find(Users.class, userIdLogin));
		inv.setTarikhKemaskini(new Date());
		inv.setFlagBayar("Y");
		
		//TODO FOR RK - IF BAYARAN XPENUH - LATER BOLEH IMPLEMENT KAT MODUL LAIN
		if (inv.getJenisBayaran().getId().equals("04")) {
			inv.setTarikhDari(q.getTarikhDari());
			inv.setTarikhHingga(q.getTarikhHingga());
		}
	}
	
	private void saveInvoisBayaranLain(MyPersistence mp,String userIdLogin, String payerId)throws Exception {

		mp.begin();
		List<KewTempInQueue> listInQueue = null;
		String selectedTab = getParam("selectedTab");
		String idPilihan = getParam("idPilihan");

		KewInvois inv = new KewInvois();

		inv.setDebit(Util.getDoubleRemoveComma(getParam("amaunBayaranLain")));
		inv.setFlagBayaran("SEWA");
		inv.setFlagQueue("Y");
		inv.setJenisBayaran((KewJenisBayaran)mp.find(KewJenisBayaran.class, "08"));
		inv.setKeteranganBayaran(getParam("keteranganLain"));
		inv.setKodHasil((KodHasil)mp.find(KodHasil.class, getParam("kodHasil")));
		inv.setNoInvois(getParam("noInvoisLain"));

		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		String tarikhDari = getParam("tarikhDari");
		String tarikhHingga = getParam("tarikhHingga");

		if(!tarikhDari.equalsIgnoreCase("") && tarikhDari.length() > 0)
			inv.setTarikhDari(df.parse(tarikhDari));

		if(!tarikhHingga.equalsIgnoreCase("") && tarikhHingga.length() > 0)
			inv.setTarikhHingga(df.parse(tarikhHingga));

		inv.setNoRujukan(getParam("noRujukanLain"));
		inv.setPembayar((Users)mp.find(Users.class, payerId));
		inv.setUserPendaftar((Users)mp.find(Users.class, userIdLogin));
		mp.persist(inv);

		KewTempInQueue q = new KewTempInQueue();
		q.setInvois(inv);
		q.setPembayar((Users)mp.find(Users.class, payerId));
		q.setAmaunBayaran(inv.getDebit());
		mp.persist(q);
		mp.commit();
	}
	
	private void saveMaklumatPayee(MyPersistence mp,Users objUser) throws Exception {
		if (objUser != null) {
			mp.begin();
			objUser.setAlamat1(getParam("alamat1"));
			objUser.setAlamat2(getParam("alamat2"));
			objUser.setAlamat3(getParam("alamat3"));
			objUser.setPoskod(getParam("poskod"));
			objUser.setBandar((Bandar)mp.find(Bandar.class, getParam("bandar")));
			objUser.setNoTelefonBimbit(getParam("noTelefonBimbit"));
			
			//ADD BY PEJE - 12022018 - TO UDATE DETAIL PENYEWA DI RK
			updateMaklumatPenyewaRK(objUser, mp);
			
			mp.commit();
		}
	}

	private void updateMaklumatPenyewaRK(Users users, MyPersistence mp) {
		try {
			RkIndividu individu = (RkIndividu) mp.find(RkIndividu.class, users.getId());
			if (individu != null) {
				individu.setAlamat1(users.getAlamat1());
				individu.setAlamat2(users.getAlamat2());
				individu.setAlamat3(users.getAlamat3());
				individu.setPoskod(users.getPoskod());
				individu.setBandar(users.getBandar());
				individu.setNoTelefonBimbit(users.getNoTelefonBimbit());
			}
			
			RkSyarikat syarikat = (RkSyarikat) mp.find(RkSyarikat.class, users.getId());
			if (syarikat != null) {
				syarikat.setAlamat1(users.getAlamat1());
				syarikat.setAlamat2(users.getAlamat2());
				syarikat.setAlamat3(users.getAlamat3());
				syarikat.setPoskod(users.getPoskod());
				syarikat.setBandar(users.getBandar());
				syarikat.setNoTelefon(users.getNoTelefonBimbit());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}		
	}
}
