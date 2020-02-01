package bph.modules.utiliti;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpSession;

import lebah.portal.action.Command;
import lebah.template.DbPersistence;
import portal.module.entity.Users;
import bph.entities.integrasi.FPXRecords;
import bph.entities.integrasi.FPXRecordsRequest;
import bph.entities.kew.KewJenisBayaran;
import bph.entities.kewangan.KewBayaranResit;
import bph.entities.kewangan.KewInvois;
import bph.entities.kewangan.KewResitSenaraiInvois;
import bph.entities.kod.Bandar;
import bph.entities.kod.KodHasil;
import bph.entities.utiliti.UtilAkaun;
import bph.entities.utiliti.UtilDewan;
import bph.entities.utiliti.UtilGelanggang;
import bph.entities.utiliti.UtilJadualTempahan;
import bph.entities.utiliti.UtilPermohonan;
import bph.integrasi.fpx.FPXPkiImplementation;
import bph.integrasi.fpx.FPXUtil;
import bph.utils.DataUtil;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class SenaraiPermohonanGelanggangRecordModuleAwam extends
		SenaraiPermohonanGelanggangRecordModule {
	private static final long serialVersionUID = 1L;
	private DbPersistence db = new DbPersistence();
	private DataUtil dataUtil = DataUtil.getInstance(db);
	private MyPersistence mp;
	
	private String fpxSellerExId = ResourceBundle.getBundle("dbconnection").getString("FPX_SELLER_EX_ID");
	private String fpxSellerId = ResourceBundle.getBundle("dbconnection").getString("FPX_SELLER_ID");
	private String keyPath = ResourceBundle.getBundle("dbconnection").getString("FPX_KEY_PATH");

	@SuppressWarnings("rawtypes")
	public Class getIdType() {
		return String.class;
	}

	@Override
	public void begin() {
		HttpSession session = request.getSession();
		userRole = (String) session.getAttribute("_portal_role");
		userId = (String) session.getAttribute("_portal_login");
		this.setDisableDefaultButton(true);
		setRecordOnly(true);
		this.setDisableUpperBackButton(true);
		this.setDisableBackButton(true);
		this.setDisableKosongkanUpperButton(true);
		this.setHideDeleteButton(true);
		String idPemohon = "";
		idPemohon = userId;
		this.setDisableSaveAddNewButton(true);
		if (!"add_new_record".equals(command)) {
			this.setDisableBackButton(true);
			this.setDisableDefaultButton(true);
		}
		dataUtil = DataUtil.getInstance(db);
		context.put("util", new Util());
		context.put("path", getPath());
		context.put("command", command);
		context.put("uploadDir", ResourceBundle.getBundle("dbconnection")
				.getString("folder"));
		context.put("dataUtil", dataUtil);
		context.put("util", new Util());
		context.put("path", getPath());
		context.put("command", command);

		this.addFilter("gelanggang is not null");
		this.addFilter("pemohon.id ='" + idPemohon + "'");
		setOrderBy("tarikhMula ASC");
		this.addFilter("tarikhMula >= '"
				+ Util.getDateTime(new Date(), "yyyy-MM-dd") + "'");
		context.put("selectDewan", dataUtil.getListDewanGelanggang());
		context.put("selectNegeri", dataUtil.getListNegeri());
		paintJadualMula();

		userId = (String) request.getSession().getAttribute("_portal_login");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		context.put("jadualtempahan", "default");
		context.put("userId", userId);
	}

	@Override
	public void beforeSave() {

	}

	@Override
	public void save(UtilPermohonan r) throws Exception {

	}

	@Override
	public void afterSave(UtilPermohonan r) {

	}

	private void insertTempahanRelated(String idDewan, Date tarikhTempahan,
			int masaMula, int masaTamat, String idStatus, String idTempahan,
			MyPersistence mp) throws Exception {
		try {
			UtilDewan dewan = (UtilDewan) mp.find(UtilDewan.class, idDewan);
			UtilJadualTempahan tempJadualTempahan = new UtilJadualTempahan();
			tempJadualTempahan.setDewan(dewan);
			tempJadualTempahan.setTarikhTempahan(tarikhTempahan);
			tempJadualTempahan.setMasaMula(masaMula);
			tempJadualTempahan.setMasaTamat(masaTamat);
			tempJadualTempahan.setStatus("C");
			UtilPermohonan permohonan = (UtilPermohonan) mp.find(
					UtilPermohonan.class, idTempahan);
			tempJadualTempahan.setPermohonan(permohonan);
			mp.persist(tempJadualTempahan);
		} catch (Exception e) {
			System.out.println("Error editMaklumatPeralatan : "
					+ e.getMessage());
		}
	}

	private void insertJadualTempahan(String idDewan, String idGelanggang,
			Date tarikhTempahan, int masaMula, int masaTamat, String idStatus,
			String idTempahan, MyPersistence mp) throws Exception {
		try {
			UtilDewan dewan = (UtilDewan) mp.find(UtilDewan.class, idDewan);
			UtilGelanggang gelanggang = (UtilGelanggang) mp.find(
					UtilGelanggang.class, idGelanggang);
			UtilJadualTempahan tempJadualTempahan = new UtilJadualTempahan();
			tempJadualTempahan.setDewan(dewan);
			tempJadualTempahan.setGelanggang(gelanggang);
			tempJadualTempahan.setTarikhTempahan(tarikhTempahan);
			tempJadualTempahan.setMasaMula(masaMula);
			tempJadualTempahan.setMasaTamat(masaTamat);
			tempJadualTempahan.setStatus(idStatus);
			UtilPermohonan permohonan = (UtilPermohonan) mp.find(
					UtilPermohonan.class, idTempahan);
			tempJadualTempahan.setPermohonan(permohonan);
			mp.persist(tempJadualTempahan);
		} catch (Exception e) {
			System.out
					.println("Error insertJadualTempahan : " + e.getMessage());
		}
	}

	// papar default jadual
	private void paintJadualMula() {
		String bgcolour = "";
		for (int x = 7; x < 23; x++) {
			bgcolour = "#999999";
			context.put("hour" + (x + 1), bgcolour);
		}
		context.put("jadualtempahan", "default");
	}

	@Override
	public boolean delete(UtilPermohonan r) throws Exception {
		boolean allowDelete = false;
		try {
			mp = new MyPersistence();		

			if (!r.getStatusBayaran().equals("Y")) {
				Boolean belumBayar = reCheckPaymentStatus(mp, r.getId());
				if (belumBayar) {
					mp.begin();
					UtilAkaun akaun = (UtilAkaun) mp
							.get("select x from UtilAkaun x where x.permohonan.id = '"
									+ r.getId() + "'");
					KewInvois invois = (KewInvois) mp
							.get("select x from KewInvois x where x.noInvois = '"
									+ r.getId() + "'");
					List<UtilJadualTempahan> listJadualTempahan = mp
							.list("select x from UtilJadualTempahan x where x.permohonan.id = '"
									+ r.getId() + "'");
					mp.remove(invois);
					mp.remove(akaun);
					for (int x = 0; x < listJadualTempahan.size(); x++) {
						UtilJadualTempahan jadualTempahan = listJadualTempahan
								.get(x);
						if (jadualTempahan != null) {
							mp.remove(jadualTempahan);
						}
					}
					r.setStatusPermohonan("B");
					r.setPemohonBatal((Users) mp.find(Users.class, userId));
					r.setTarikhBatal(new Date());
					r.setMasaBatal(new Date());
					mp.commit();
				}
			}
		} catch (Exception e) {
			System.out.println("Error delete : " + e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return allowDelete;
	}

	@Override
	public String getPath() {
		return "bph/modules/utiliti/senaraiPermohonanGelanggangAwam";
	}

	@Override
	public Class<UtilPermohonan> getPersistenceClass() {
		return UtilPermohonan.class;
	}

	@Override
	public void getRelatedData(UtilPermohonan r) {
		context.put("selectedTab", "1");
		String idDewan = "";
		try {
			mp = new MyPersistence();
			if (r.getDewan().getId() != null
					&& r.getDewan().getId().trim().length() > 0)
				idDewan = r.getDewan().getId();
			List<UtilGelanggang> list = dataUtil.getListGelanggang(idDewan);
			context.put("selectGelanggang", list);
			UtilPermohonan tempahan = null;
			tempahan = (UtilPermohonan) mp
					.find(UtilPermohonan.class, r.getId());
			if (tempahan.getStatusBayaran().equalsIgnoreCase("Y")) {
				context.put("save", "N");
			} else {
				context.put("save", "");
			}
			context.put("tempahan", tempahan);
			String statusBayar = null;
			statusBayar = tempahan.getStatusBayaran();
			context.put("statusBayar", statusBayar);
			context.put("d", tempahan);
			// check tarikh if hari sama block payment
			context.put("blockPayment", false);
			Date tarikhTempahan = r.getTarikhMula();
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			Date today = new Date();
			Date todayWithZeroTime = formatter.parse(formatter.format(today));
			if (tarikhTempahan.compareTo(todayWithZeroTime) == 0) {
				context.put("blockPayment", true);
			}
			paintJadualRelated(tempahan);
			semakStatusResit(r);
		} catch (Exception e) {
			System.out.println("Error getRelatedData : " + e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
	}

	private void semakStatusResit(UtilPermohonan r) {
		try {
			mp = new MyPersistence();
			if (r.getStatusBayaran().equals("Y") && r.getResitSewa() == null) {
				UtilAkaun akaun = (UtilAkaun) mp
						.get("select x from UtilAkaun x where x.permohonan.id = '"
								+ r.getId() + "'");
				if (akaun != null) {
					KewInvois invois = (KewInvois) mp
							.get("select x from KewInvois x where x.idLejar = '"
									+ akaun.getId() + "'");
					if (invois != null) {
						KewResitSenaraiInvois senaraiInvois = (KewResitSenaraiInvois) mp
								.get("select x from KewResitSenaraiInvois x where x.invois.id = '"
										+ invois.getId() + "'");
						if (senaraiInvois != null) {
							KewBayaranResit resit = senaraiInvois.getResit();
							if (resit != null) {
								mp.begin();
								resit.setIdPermohonan(r.getId());
								r.setResitSewa(resit);
								mp.commit();
							}
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Error semakStatusResit : " + e.getMessage());
		}
	}

	public void kadarSewa(UtilPermohonan r) throws Exception {
		int mula = r.getMasaMula();
		int tamat = r.getMasaTamat();

		String mulasiang = r.getGelanggang().getWaktuBukaSiang();
		String tamatsiang = r.getGelanggang().getWaktuTutupSiang();
		String mulamalam = r.getGelanggang().getWaktuBukaMalam();
		String tamatmalam = r.getGelanggang().getWaktuTutupMalam();

		int waktuMulaSiang = Integer.parseInt(mulasiang);
		int waktuTamatSiang = Integer.parseInt(tamatsiang);
		int waktuMulaMalam = Integer.parseInt(mulamalam);
		int waktuTamatMalam = Integer.parseInt(tamatmalam);

		int hoursiang = 0;
		int hourmalam = 0;

		for (int x = mula; x < tamat; x++) {
			if (waktuMulaSiang == x || x < waktuTamatSiang) {
				hoursiang++;
			} else if (waktuMulaMalam == x || x < waktuTamatMalam) {
				hourmalam++;
			} else {
				hoursiang++;// default for error
			}
		}

		double sewaSiang = 0;
		double sewaMalam = 0;
			sewaSiang = r.getGelanggang().getKadarSewa();// kadar sewa siang
			sewaMalam = r.getGelanggang().getKadarSewaAwam();// kadar sewa
		double kadarBayarSiang = hoursiang * sewaSiang;
		double kadarBayarMalam = hourmalam * sewaMalam;
		double jumlahSewa = kadarBayarSiang + kadarBayarMalam;
		r.setMasaMula(mula);
		r.setMasaTamat(tamat);
		r.setAmaun(jumlahSewa);
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String findDewan = get("find_dewan");
		String findGelanggang = get("find_gelanggang");
		String findNegeri = get("find_negeri");
		String findBandar = get("find_bandar");

		map.put("dewan.nama", findDewan);
		map.put("gelanggang.nama", findGelanggang);
		map.put("dewan.bandar.negeri.keterangan", findNegeri);
		map.put("dewan.bandar.keterangan", findBandar);

		return map;
	}

	@Command("getMaklumatTempahan")
	public String getMaklumatTempahan() {
		try {
			mp = new MyPersistence();
			UtilPermohonan permohonan = (UtilPermohonan) mp.find(
					UtilPermohonan.class, get("idTempahan"));
			context.put("r", permohonan);
			paintJadualRelated(permohonan);
			context.put("selectedTab", "1");
		} catch (Exception e) {
			System.out.println("Error getMaklumatTempahan : " + e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return getPath() + "/entry_page.vm";
	}

	@Command("getBayaran")
	public String getBayaran() {
		try {
			mp = new MyPersistence();
			mp.begin();
			UtilPermohonan permohonan = (UtilPermohonan) mp.find(
					UtilPermohonan.class, get("idTempahan"));
			context.put("d", permohonan);
			context.put("listUtilPermohonan", getListPermohonan(permohonan));
			context.put("selectedTab", "2");
			userId = (String) request.getSession().getAttribute("_portal_login");
			context.put("userId", userId);
		} catch (Exception e) {
			System.out.println("Error getBayaran : " + e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return getPath() + "/entry_page.vm";
	}

	@SuppressWarnings("unchecked")
	private List<UtilPermohonan> getListPermohonan(UtilPermohonan r) {
		List<UtilPermohonan> listUtilPermohonanDewan = null;
		try {
			mp = new MyPersistence();
			listUtilPermohonanDewan = mp
					.list("select x from UtilPermohonan x where x.id = '"
							+ r.getId() + "' order by x.id asc");
		} catch (Exception e) {
			System.out.println("Error getListPermohonan : " + e.getMessage());
		}
		return listUtilPermohonanDewan;
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

	@Command("selectGelanggang")
	public String selectGelanggang() throws Exception {

		String idDewan = "";
		if (get("idDewan").trim().length() > 0)
			idDewan = get("idDewan");
		List<UtilGelanggang> list = dataUtil.getListGelanggang(idDewan);
		context.put("selectGelanggang", list);
		return getPath() + "/selectGelanggang.vm";
	}

	@Command("lompatBayaran")
	public String lompatBayaran() throws Exception {

		context.put("selectedTab", "2");
		return getPath() + "/entry_page.vm";
	}

	public void removeTempahan(UtilPermohonan r) {
		try {
			mp = new MyPersistence();
			UtilAkaun mn = (UtilAkaun) mp
					.get("select x from UtilAkaun x where x.permohonan.id = '"
							+ r.getIdTempahan() + "' ");
			KewInvois inv = (KewInvois) mp
					.get("select x from KewInvois x where x.idLejar = '"
							+ mn.getId() + "' ");
			mp.begin();
			mp.remove(inv);
			mp.remove(mn);
			mp.remove(r);
			mp.commit();
		} catch (Exception e) {
			System.out.println("Error removeTempahan : " + e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
	}

	public String semakTempahanBertindih() {
		String overlap = "false";
		try {
			mp = new MyPersistence();
			String idDewan = get("idDewan");
			String idGelanggang = get("idGelanggang");
			Date tarikhMula = getDate("tarikhTempahan");
			Date tarikhTamat = getDate("tarikhTempahan");
			Integer masaMula = getParamAsInteger("masaMula");
			Integer masaTamat = getParamAsInteger("masaTamat");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			UtilPermohonan permohonan = (UtilPermohonan) mp
					.get("SELECT x FROM UtilPermohonan x WHERE x.dewan.id='"
							+ idDewan + "' and x.gelanggang.id='"
							+ idGelanggang + "' and x.tarikhMula='"
							+ sdf.format(tarikhMula) + "' and x.tarikhTamat='"
							+ sdf.format(tarikhTamat) + "' and x.masaMula='"
							+ masaMula + "' and x.masaTamat='" + masaTamat
							+ "' and x.statusPermohonan='Y'");
			if (permohonan != null) {
				overlap = "true";
				context.put("overlap", overlap);
			} else {
				context.put("overlap", overlap);
			}
		} catch (Exception e) {
			System.out.println("Error semakanOverlap : " + e.getMessage());
		}
		return overlap;
	}

	@Command("simpanPermohonan")
	public String simpanPermohonan() {
		Boolean disableTempahan = checkBetween();
		if(!disableTempahan){
			String bertindih = semakTempahanBertindih();
			UtilPermohonan tempahan = new UtilPermohonan();
			if (bertindih.equalsIgnoreCase("false")) {
				try {
					mp = new MyPersistence();
					mp.begin();
					userId = (String) request.getSession().getAttribute(
							"_portal_login");
					userRole = (String) request.getSession().getAttribute(
							"_portal_role");
					String idPemohon = "";
					idPemohon = userId;
					tempahan.setDewan((UtilDewan) mp.find(UtilDewan.class,
							get("idDewan")));
					tempahan.setGelanggang((UtilGelanggang) mp.find(
							UtilGelanggang.class, get("idGelanggang")));
					tempahan.setTarikhMula(getDate("tarikhTempahan"));
					tempahan.setTarikhTamat(getDate("tarikhTempahan"));
					tempahan.setPemohon((Users) mp.find(Users.class, idPemohon));
					tempahan.setMasaMula(getParamAsInteger("masaMula"));
					tempahan.setMasaTamat(getParamAsInteger("masaTamat"));
					tempahan.setTujuan("BADMINTON");
					tempahan.setJenisPermohonan("ONLINE");
					tempahan.setStatusBayaran("T");
					tempahan.setStatusAktif("1");
					tempahan.setStatusPermohonan("Y");
					tempahan.setTarikhPermohonan(new Date());
					mp.persist(tempahan);
					mp.flush();
					kadarSewa(tempahan);
					createRecordBayaran(tempahan, mp);
					if (tempahan.getStatusBayaran().equalsIgnoreCase("Y")) {
						context.put("save", "N");
					} else {
						context.put("save", "");
					}
					context.put("tempahan", tempahan);
					insertJadualTempahan(tempahan.getDewan().getId(), tempahan
							.getGelanggang().getId(), tempahan.getTarikhMula(),
							tempahan.getMasaMula(), tempahan.getMasaTamat(), "B",
							tempahan.getId(), mp);
					insertTempahanRelated(tempahan.getDewan().getId(),
							tempahan.getTarikhMula(), tempahan.getMasaMula(),
							tempahan.getMasaTamat(), "C", tempahan.getId(), mp);
					context.put("selectedTab", "1");
					context.put("r", tempahan);
					mp.commit();
					paintJadualRelated(tempahan);
				} catch (Exception e) {
					System.out
							.println("Error simpanPermohonan : " + e.getMessage());
				} finally {
					if (mp != null) {
						mp.close();
					}
				}
				getRelatedData(tempahan);
				context.put("d", tempahan);
				context.put("listUtilPermohonan", getListPermohonan(tempahan));
				context.put("selectedTab", "2");
				return getPath() + "/entry_page.vm";
			} else {
				return getPath() + "/notis.vm";
			}
		} else {
			return getPath() + "/notisRamadhan.vm";
		}
	}
	
	 public boolean checkBetween() {
		 SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		 Date startDate = null;
		try {
			startDate = sdf.parse("05/15/2018");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 Date endDate = null;
		try {
			endDate = sdf.parse("07/01/2018");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		 
		 Date dateToCheck=getDate("tarikhTempahan");
		 return dateToCheck.compareTo(startDate) >= 0 && dateToCheck.compareTo(endDate) <=0;
	}

	public void createRecordBayaran(UtilPermohonan r, MyPersistence mp) {

		userId = (String) request.getSession().getAttribute("_portal_login");
		try {
			Users user = (Users) mp.find(Users.class, userId);
			String catatan = null;
			Double kadarSewa = r.getAmaun();
			String noTempahan = r.getId();
			// create main ledger (tempahan)
			UtilAkaun mn = new UtilAkaun();
			mn.setPermohonan(r);
			mn.setAmaunBayaranSeunit(kadarSewa);
			mn.setBilanganUnit(1);
			mn.setDebit(r.getAmaun());
			mn.setKredit(0d);
			mn.setFlagBayar("T");
			mn.setFlagVoid("T");
			mn.setKeterangan("BAYARAN BAGI SEWAAN "
					+ r.getGelanggang().getNama());
			mn.setCatatan(catatan);
			mn.setKodHasil((KodHasil) mp.find(KodHasil.class, "74299")); // BAYARAN-BAYARAN SEWA YANG LAIN
			mn.setNoInvois(noTempahan); // TEMPORARY SET TO ID PERMOHONAN
			mn.setTarikhInvois(new Date());
			mn.setIdMasuk(user);
			mn.setIdKemaskini(user);
			mn.setTarikhMasuk(new Date());
			mn.setTarikhKemaskini(new Date());
			mp.persist(mn);
			createInvoisInFinance(mn, user, mp);
		} catch (Exception e) {
			System.out.println("Error createRecordBayaran : " + e.getMessage());
		}
	}

	public void createInvoisInFinance(UtilAkaun ak, Users user, MyPersistence mp) {
		// push to invois kewangan
		KewInvois inv = new KewInvois();
		inv.setFlagBayar("T");
		inv.setDebit(ak.getDebit());
		inv.setFlagBayaran("SEWA");
		inv.setFlagQueue("T");
		inv.setIdLejar(ak.getId());
		try {
			inv.setJenisBayaran((KewJenisBayaran) mp.find(
					KewJenisBayaran.class, "03")); // 03 - UTILITI
			inv.setKeteranganBayaran(ak.getKeterangan().toUpperCase());
			inv.setKodHasil(ak.getKodHasil());
			inv.setNoInvois(ak.getNoInvois());
			// inv.setNoRujukan(ak.getPermohonan().getNoTempahan().toUpperCase());
			inv.setNoRujukan(ak.getPermohonan().getIdTempahan());
			inv.setPembayar(ak.getPermohonan().getPemohon());
			inv.setTarikhInvois(ak.getTarikhInvois());
			inv.setUserPendaftar(user);
			inv.setTarikhDaftar(new Date());
			inv.setUserKemaskini(user);
			inv.setTarikhKemaskini(new Date());
			inv.setTarikhDari(ak.getPermohonan().getTarikhMula());
			inv.setTarikhHingga(ak.getPermohonan().getTarikhTamat());
			mp.persist(inv);
		} catch (Exception e) {
			System.out.println("Error createInvoisInFinance : "
					+ e.getMessage());
		}
	}

	@Command("batalTempahan")
	public String batalTempahan() throws Exception {
		UtilPermohonan tempahan = null;
		try {
			mp = new MyPersistence();
			tempahan = (UtilPermohonan) mp.find(UtilPermohonan.class,
					get("idTempahan"));
			
			if (!tempahan.getStatusBayaran().equals("Y")) {
				Boolean belumBayar = reCheckPaymentStatus(mp, tempahan.getId());
				if (belumBayar) {
					mp.begin();
					
					UtilAkaun akaun = (UtilAkaun) mp
							.get("select x from UtilAkaun x where x.permohonan.id = '"
									+ tempahan.getId() + "'");
					KewInvois invois = (KewInvois) mp
							.get("select x from KewInvois x where x.noInvois = '"
									+ tempahan.getId() + "'");
					List<UtilJadualTempahan> listJadualTempahan = mp
							.list("select x from UtilJadualTempahan x where x.permohonan.id = '"
									+ tempahan.getId() + "'");
					mp.remove(invois);
					mp.remove(akaun);
					for (int x = 0; x < listJadualTempahan.size(); x++) {
						UtilJadualTempahan jadualTempahan = listJadualTempahan
								.get(x);
						if (jadualTempahan != null) {
							mp.remove(jadualTempahan);
						}
					}
					tempahan.setStatusPermohonan("B");
					tempahan.setPemohonBatal((Users) mp.find(Users.class, userId));
					tempahan.setTarikhBatal(new Date());
					tempahan.setMasaBatal(new Date());
					mp.commit();
				}
			}
		} catch (Exception e) {
			System.out.println("Error batalTempahan : " + e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		context.put("r", tempahan);
		return getPath() + "/entry_page.vm";
	}
	
	/** START PEMBAYARAN FPX **/
	@Command("paparPilihan")
	public String paparPilihan() throws Exception {
		String vm = "";
		String idTempahan = getParam("idTempahan");
		context.remove("recheckPaymentMsg");
		try {
			mp = new MyPersistence();
			Boolean belumBayar = reCheckPaymentStatus(mp, idTempahan);

			if (belumBayar) {
				UtilPermohonan r = (UtilPermohonan) mp.find(
						UtilPermohonan.class, idTempahan);
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
				String fpx_sellerExId = fpxSellerExId;
				String fpx_sellerExOrderNo = new SimpleDateFormat(
						"yyyyMMddHHmmss").format(new Date());
				String fpx_sellerTxnTime = new SimpleDateFormat(
						"yyyyMMddHHmmss").format(new Date());
				String fpx_sellerOrderNo = r.getIdTempahan();
				String fpx_sellerId = fpxSellerId;
				String fpx_sellerBankCode = "01";
				String fpx_txnCurrency = "MYR";

				String fpx_txnAmount = Double.toString(r.getAmaun());
				String fpx_buyerEmail = "";
				String fpx_buyerName = "";
				String fpx_buyerBankId = "";
				String fpx_buyerBankBranch = "";
				String fpx_buyerAccNo = "";
				String fpx_buyerId = "";
				String fpx_makerName = "";
				String fpx_buyerIban = "";
				String fpx_productDesc = "Pembayaran Tempahan Gelanggang";
				String fpx_version = "5.0";

				fpx_checkSum = fpx_buyerAccNo + "|" + fpx_buyerBankBranch + "|"
						+ fpx_buyerBankId + "|" + fpx_buyerEmail + "|"
						+ fpx_buyerIban + "|" + fpx_buyerId + "|"
						+ fpx_buyerName + "|";
				fpx_checkSum += fpx_makerName + "|" + fpx_msgToken + "|"
						+ fpx_msgType + "|" + fpx_productDesc + "|"
						+ fpx_sellerBankCode + "|" + fpx_sellerExId + "|";
				fpx_checkSum += fpx_sellerExOrderNo + "|" + fpx_sellerId + "|"
						+ fpx_sellerOrderNo + "|" + fpx_sellerTxnTime + "|"
						+ fpx_txnAmount + "|" + fpx_txnCurrency + "|"
						+ fpx_version;

				final_checkSum = FPXPkiImplementation.signData(
						keyPath, fpx_checkSum,
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

				HttpSession session = request.getSession();
				session.setAttribute("sesIdPermohonan", r.getId());
				session.setAttribute("sesModul", "UTIL");
				session.setAttribute("sesRole", (String) request.getSession()
						.getAttribute("_portal_role"));
				userRole = (String) request.getSession().getAttribute(
						"_portal_role");
				if ("(AWAM) Penjawat Awam".equalsIgnoreCase(userRole)) {
					session.setAttribute("returnlink", "../sbbphv2/c/1431069110780");
				} else {
					session.setAttribute("returnlink", "../sbbphv2/c/1438069927208");
				}

				// AZAM ADD - 14/1/2016
				FPXUtil fpxUtil = new FPXUtil(session);
				fpxUtil.addUpdatePayment_Transaction((String) request
						.getSession().getAttribute("_portal_login"),
						(String) session.getAttribute("sesIdPermohonan"),
						fpx_txnAmount, (String) session
								.getAttribute("sesModul"));

				fpxUtil.registerFPXRequest(fpx_sellerId, fpx_sellerExId,
						fpx_sellerOrderNo, fpx_sellerExOrderNo, fpx_txnAmount,
						fpx_productDesc, "UTIL", mp);
				vm = "bph/modules/fpx/pilihan.vm";
			} else {
				String msg = "Rekod transaksi pembayaran sedang diproses.Sila semak semula dalam tempoh 30 minit untuk status pembayaran dikemaskini.<br>Harap maklum.";
				context.put("recheckPaymentMsg", msg);
				vm = "bph/modules/fpx/pending.vm";
			}
		} catch (Exception e) {
			System.out.println("Error paparPilihan FPX Gelanggang : "
					+ e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}

		return vm;
	}
	
	@Command("paparPilihan1")
	public String paparPilihan1() throws Exception {
		String vm = "";
		String idTempahan = getParam("idTempahan");
		context.remove("recheckPaymentMsg");
		try {
			mp = new MyPersistence();
			Boolean belumBayar = reCheckPaymentStatus(mp, idTempahan);

			if (belumBayar) {
				UtilPermohonan r = (UtilPermohonan) mp.find(
						UtilPermohonan.class, idTempahan);
				String serverName = request.getServerName();
				String contextPath = request.getContextPath();
				int serverPort = request.getServerPort();
				String server = serverPort != 80 ? serverName + ":"
						+ serverPort : serverName;
				String image_url = "http://" + server + contextPath;
				context.put("imageUrl", image_url);
				
				context.put("listBankFPX", dataUtil.getListBankFPX());
				vm = "bph/modules/fpx/pilihan1.vm";
			} else {
				String msg = "Rekod transaksi pembayaran sedang diproses. Sila semak semula dalam tempoh 30 minit untuk status pembayaran dikemaskini.<br>Harap maklum.";
				context.put("recheckPaymentMsg", msg);
				vm = "bph/modules/fpx/pending.vm";
			}
		} catch (Exception e) {
			System.out.println("Error paparPilihan FPX Gelanggang : " + e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return vm;
	}
	
	@Command("doChangeBankFPX")
	public String doChangeBankFPX() throws Exception {
		try {
			mp = new MyPersistence();
			UtilPermohonan r = (UtilPermohonan) mp.find(
					UtilPermohonan.class, get("idTempahan"));
			String fpx_checkSum = "";
			String final_checkSum = "";
			String fpx_msgType = "AR";
			String fpx_msgToken = "01";
			String fpx_sellerExId = fpxSellerExId;
			String fpx_sellerExOrderNo = new SimpleDateFormat(
					"yyyyMMddHHmmss").format(new Date());
			String fpx_sellerTxnTime = new SimpleDateFormat(
					"yyyyMMddHHmmss").format(new Date());
			String fpx_sellerOrderNo = r.getIdTempahan();
			String fpx_sellerId = fpxSellerId;
			String fpx_sellerBankCode = "01";
			String fpx_txnCurrency = "MYR";

			String fpx_txnAmount = Double.toString(r.getAmaun());
			String fpx_buyerEmail = "";
			if (r != null && r.getPemohon() != null && r.getPemohon().getEmel() != null)
				fpx_buyerEmail = r.getPemohon().getEmel();
			String fpx_buyerName = "";
			if (r != null && r.getPemohon() != null && r.getPemohon().getUserName() != null)
				fpx_buyerName = r.getPemohon().getUserName();
			String fpx_buyerBankId = getParam("idBankFPX");
			String fpx_buyerBankBranch = "";
			String fpx_buyerAccNo = "";
			String fpx_buyerId = "";
			String fpx_makerName = "";
			String fpx_buyerIban = "";
			String fpx_productDesc = "Pembayaran Tempahan Gelanggang";
			String fpx_version = "7.0";

			fpx_checkSum = fpx_buyerAccNo + "|" + fpx_buyerBankBranch + "|"
					+ fpx_buyerBankId + "|" + fpx_buyerEmail + "|"
					+ fpx_buyerIban + "|" + fpx_buyerId + "|"
					+ fpx_buyerName + "|";
			fpx_checkSum += fpx_makerName + "|" + fpx_msgToken + "|"
					+ fpx_msgType + "|" + fpx_productDesc + "|"
					+ fpx_sellerBankCode + "|" + fpx_sellerExId + "|";
			fpx_checkSum += fpx_sellerExOrderNo + "|" + fpx_sellerId + "|"
					+ fpx_sellerOrderNo + "|" + fpx_sellerTxnTime + "|"
					+ fpx_txnAmount + "|" + fpx_txnCurrency + "|"
					+ fpx_version;

			final_checkSum = FPXPkiImplementation.signData(
					keyPath, fpx_checkSum,
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
			
			context.put("idBankFPX", getParam("idBankFPX"));		
			
			HttpSession session = request.getSession();
			session.setAttribute("sesIdPermohonan", r.getId());
			session.setAttribute("sesModul", "UTIL");
			session.setAttribute("sesRole", (String) request.getSession()
					.getAttribute("_portal_role"));
			userRole = (String) request.getSession().getAttribute(
					"_portal_role");
			if ("(AWAM) Penjawat Awam".equalsIgnoreCase(userRole)) {
				session.setAttribute("returnlink", "../sbbphv2/c/1431069110780");
			} else {
				session.setAttribute("returnlink", "../sbbphv2/c/1438069927208");
			}

			// AZAM ADD - 14/1/2016
			FPXUtil fpxUtil = new FPXUtil(session);
			fpxUtil.addUpdatePayment_Transaction((String) request
					.getSession().getAttribute("_portal_login"),
					(String) session.getAttribute("sesIdPermohonan"),
					fpx_txnAmount, (String) session
							.getAttribute("sesModul"));

			fpxUtil.registerFPXRequest(fpx_sellerId, fpx_sellerExId,
					fpx_sellerOrderNo, fpx_sellerExOrderNo, fpx_txnAmount,
					fpx_productDesc, "UTIL", mp);
		} catch (Exception e) {
			System.out.println("Error doChangeBankFPX FPX Gelanggang : " + e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return "bph/modules/fpx/pilihan1.vm";
	}

	private Boolean reCheckPaymentStatus(MyPersistence mp, String idPermohonan) {
		boolean bool = true;

		try {
			// REQUERY BASED ON NO RESPONSE DURING TRANSACTION.
			requeryNoResponseTransaction(mp, idPermohonan);

			// REQUERY BASED ON DEBITAUTHCODE = 09 (TRANSACTION PENDING).
			requeryPendingTransaction(mp, idPermohonan);

			// QUERY DATA FPXRECORDS - CHECK PAYMENT SUCCESS(00) / PENDING(09)
			FPXRecords fpxRecords = (FPXRecords) mp
					.get("select x from FPXRecords x where x.debitAuthCode in ('00', '09') and x.sellerOrderNo = '"
							+ idPermohonan + "' order by x.id asc");
			if (fpxRecords != null) {
				bool = false;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return bool;
	}

	private void requeryNoResponseTransaction(MyPersistence mp,
			String idPermohonan) {
		FPXUtil fpxUtil = new FPXUtil();
		try {
			int loopRequery = 0;
			while (loopRequery < 3) {
				List<FPXRecordsRequest> listFPXRecordRequest = mp
						.list("select x from FPXRecordsRequest x where (x.fpxTxnId is null or x.fpxTxnId = '') and x.sellerOrderNo = '"
								+ idPermohonan + "' order by x.id asc");
				if (listFPXRecordRequest.size() > 0) {
					for (FPXRecordsRequest fpxRecordRequest : listFPXRecordRequest) {
						FPXRecords fpxMyClear = fpxUtil.reQueryFPX(
								fpxRecordRequest.getSellerOrderNo(),
								fpxRecordRequest.getSellerExOrderNo(),
								fpxRecordRequest.getTxnAmount());
						mp.begin();
						if (fpxMyClear != null) {
							boolean addRecord = false;
							FPXRecords fpxRecords = (FPXRecords) mp.find(
									FPXRecords.class, fpxMyClear.getId());
							if (fpxRecords == null) {
								fpxRecords = new FPXRecords();
								fpxRecords.setId(fpxMyClear.getId());
								addRecord = true;
							}
							fpxRecords.setBuyerBankBranch(fpxMyClear
									.getBuyerBankBranch());
							fpxRecords.setBuyerBankId(fpxMyClear
									.getBuyerBankId());
							fpxRecords.setBuyerIban(fpxMyClear.getBuyerIban());
							fpxRecords.setBuyerId(fpxMyClear.getBuyerId());
							fpxRecords.setBuyerName(fpxMyClear.getBuyerName());
							fpxRecords.setCreditAuthCode(fpxMyClear
									.getCreditAuthCode());
							fpxRecords.setCreditAuthNo(fpxMyClear
									.getCreditAuthNo());
							fpxRecords.setDebitAuthCode(fpxMyClear
									.getDebitAuthCode());
							fpxRecords.setDebitAuthNo(fpxMyClear
									.getDebitAuthNo());
							fpxRecords
									.setFpxTxnTime(fpxMyClear.getFpxTxnTime());
							fpxRecords.setMakerName(fpxMyClear.getMakerName());
							fpxRecords.setMsgToken(fpxMyClear.getMsgToken());
							fpxRecords.setMsgType(fpxMyClear.getMsgType());
							fpxRecords
									.setSellerExId(fpxMyClear.getSellerExId());
							fpxRecords.setSellerExOrderNo(fpxMyClear
									.getSellerExOrderNo());
							fpxRecords.setSellerId(fpxMyClear.getSellerId());
							fpxRecords.setSellerOrderNo(fpxMyClear
									.getSellerOrderNo());
							fpxRecords.setSellerTxnTime(fpxMyClear
									.getSellerTxnTime());
							fpxRecords.setTxnAmount(fpxMyClear.getTxnAmount());
							fpxRecords.setTxnCurrency(fpxMyClear
									.getTxnCurrency());
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
				List<FPXRecords> listFPXRecords = mp
						.list("select x from FPXRecords x where x.debitAuthCode = '09' and x.sellerOrderNo = '"
								+ idPermohonan + "' order by x.id asc");
				if (listFPXRecords.size() > 0) {
					for (FPXRecords fpxRecords : listFPXRecords) {
						FPXRecords fpxMyClear = fpxUtil.reQueryFPX(
								fpxRecords.getSellerOrderNo(),
								fpxRecords.getSellerExOrderNo(),
								fpxRecords.getTxnAmount());
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
	/** END PEMBAYARAN FPX **/
}
