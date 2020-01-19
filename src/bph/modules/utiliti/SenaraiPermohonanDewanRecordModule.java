package bph.modules.utiliti;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpSession;

import lebah.portal.action.Command;
import lebah.template.DbPersistence;
import lebah.template.LebahRecordTemplateModule;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import portal.module.entity.Users;
import bph.entities.kew.KewJenisBayaran;
import bph.entities.kewangan.KewBayaranResit;
import bph.entities.kewangan.KewInvois;
import bph.entities.kewangan.KewResitSenaraiInvois;
import bph.entities.kod.Bandar;
import bph.entities.kod.JenisDokumen;
import bph.entities.kod.KodHasil;
import bph.entities.kod.KodPetugas;
import bph.entities.utiliti.UtilAkaun;
import bph.entities.utiliti.UtilDewan;
import bph.entities.utiliti.UtilDokumen;
import bph.entities.utiliti.UtilGelanggang;
import bph.entities.utiliti.UtilJadualTempahan;
import bph.entities.utiliti.UtilPermohonan;
import bph.integrasi.fpx.FPXPkiImplementation;
import bph.utils.DataUtil;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class SenaraiPermohonanDewanRecordModule extends
		LebahRecordTemplateModule<UtilPermohonan> {

	private static final long serialVersionUID = 1L;
	private DbPersistence db = new DbPersistence();
	private DataUtil dataUtil = DataUtil.getInstance(db);
	private MyPersistence mp;

	@SuppressWarnings("unchecked")
	@Override
	public Class getIdType() {
		return String.class;
	}

	@Override
	public void afterSave(UtilPermohonan r) {
		try {			
			String tarikhMula = getParam("tarikhTempahan");
			String tarikhTamat = getParam("tarikhTempahanTamat");
			String idDewan = getParam("idDewan");
			Calendar dateMula = Calendar.getInstance();

			dateMula.setTime(new SimpleDateFormat("dd-MM-yyyy")
					.parse(tarikhMula));

			// dateMula.add(Calendar.DATE, 1);

			Calendar dateTamat = Calendar.getInstance();

			dateTamat.setTime(new SimpleDateFormat("dd-MM-yyyy")
					.parse(tarikhTamat));

			dateTamat.add(Calendar.DATE, 1);
			@SuppressWarnings("unused")
			int masaMula = getParamAsInteger("masaMula");
			@SuppressWarnings("unused")
			int masaTamat = getParamAsInteger("masaTamat");
			while (dateMula.before(dateTamat)) {
				// insertJadualTempahan(idDewan, dateMula.getTime(),
				// r.getMasaMula(), r.getMasaTamat(), "B", r.getId());
				// insertTempahanRelated(idDewan, dateMula.getTime(),
				// r.getMasaMula(), r.getMasaTamat(), "C", r.getId());
				// dateMula.add(Calendar.DATE, 1);
				insertJadualTempahan(idDewan, dateMula.getTime(), r
						.getMasaMula(), r.getMasaTamat(), "B", r
						.getIdTempahan());
				insertTempahanRelated(idDewan, dateMula.getTime(), r
						.getMasaMula(), r.getMasaTamat(), "C", r
						.getIdTempahan());
				dateMula.add(Calendar.DATE, 1);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		context.put("selectedTab", "2");
		paintJadualRelated(r);
	}

	@SuppressWarnings("unused")
	private void removeJadualTempahan(UtilJadualTempahan tempJadualTempahan)
			throws Exception {
		mp.begin();
		mp.remove(tempJadualTempahan);
		mp.commit();
	}

	@Override
	public void beforeSave() {

	}

	@SuppressWarnings("unused")
	private void getDataPemohon(String userId) {
		try {
			mp = new MyPersistence();
			Users users = (Users) mp.find(Users.class, userId);
			context.put("users", users);
		} catch (Exception e) {
			System.out.println("Error getDataPemohon : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
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
		
		context.put("util", new Util());
		context.put("path", getPath());
		context.put("command", command);
		context.put("uploadDir", ResourceBundle.getBundle("dbconnection")
				.getString("folder"));
		userRole = (String) request.getSession().getAttribute("_portal_role");
		defaultButtonOption();
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
		context.put("tarikh", new Date());
		context.put("userRole", userRole);
		paintJadualMula();
		context.put("statusBayar", "T");
	}

	// private void fpxSuccess(UtilPermohonan r) {
	// System.out.println("r " + r);
	// }

	private void addfilter() {
		if (userRole.equalsIgnoreCase("(UTILITI) Pelulus")) {
			this.addFilter("statusPermohonan in ('R', 'Y', 'YY') ");// perlu kelulusan			
		}
		this.addFilter("gelanggang is null");
		this.addFilter("statusAktif = '1' ");
		this.addFilter("tarikhMula >= '"+ Util.getDateTime(new Date(), "yyyy-MM-dd") + "'");
		setOrderBy("tarikhMula ASC");
	}

	private void defaultButtonOption() {

		userRole = (String) request.getSession().getAttribute("_portal_role");
		this.setDisableSaveAddNewButton(true);
		if (userRole.equalsIgnoreCase("(UTILITI) Pelulus")) {
			this.setDisableAddNewRecordButton(true);
		} else {
			this.setDisableAddNewRecordButton(false);
		}
		if (!"add_new_record".equals(command)) {
			this.setDisableBackButton(true);
			this.setDisableDefaultButton(true);
		} else {
			this.setDisableBackButton(false);
			this.setDisableDefaultButton(false);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean delete(UtilPermohonan r) throws Exception {
		UtilJadualTempahan jadualTempahan = null;
		try {
			mp = new MyPersistence();
			mp.begin();
			List<UtilJadualTempahan> list = mp
					.list("select x from UtilJadualTempahan x where x.permohonan.id = '"
							+ r.getId() + "'");
			if (r.getStatusBayaran().equals("Y")) {
				return false;
			} else {
				for (int y = 0; y < list.size(); y++) {
					jadualTempahan = list.get(y);
					if (jadualTempahan != null) {
						mp.remove(jadualTempahan);
					}
				}
				UtilAkaun mn = (UtilAkaun) mp
						.get("select x from UtilAkaun x where x.permohonan.id = '"
								+ r.getId() + "' ");
				if (mn != null) {
					mp.remove(mn);
				}
				mp.commit();
			}
		} catch (Exception e) {
			System.out.println("Error delete : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public void deleteBooking(UtilPermohonan r) {
		UtilJadualTempahan jadualTempahan = null;
		try {
			mp = new MyPersistence();
			mp.begin();
			List<UtilJadualTempahan> list = mp
					.list("select x from UtilJadualTempahan x where x.permohonan.id = '"
							+ r.getId() + "'");
			for (int y = 0; y < list.size(); y++) {
				jadualTempahan = list.get(y);
				mp.remove(jadualTempahan);
			}
			mp.commit();
		} catch (Exception e) {
			System.out.println("Error deleteBooking : " + e.getMessage());
		} 
	}

	@Override
	public String getPath() {

		return "bph/modules/utiliti/senaraiPermohonanDewan";
	}

	@Override
	public Class<UtilPermohonan> getPersistenceClass() {

		return UtilPermohonan.class;
	}

	@Override
	public void getRelatedData(UtilPermohonan r) {
		try {
			mp = new MyPersistence();
			context.put("selectedTab", "1");
			UtilPermohonan tempahan = null;
			tempahan = (UtilPermohonan) mp
					.find(UtilPermohonan.class, r.getId());
			context.put("tempahan", tempahan);
			String idPemohon = null;
			String statusBayar = null;
			try {
				idPemohon = tempahan.getPemohon().getId();
			} catch (Exception e) {
				idPemohon = null;
			}

			if (idPemohon != null) {
				Users pemohon = (Users) mp.find(Users.class, idPemohon);
				if (pemohon != null) {
					context.put("pemohon", pemohon);
				} else {
					context.put("pemohon", "");
				}
			}
			statusBayar = tempahan.getStatusBayaran();
			context.put("statusBayar", statusBayar);
			paintJadualRelated(tempahan);
			semakStatusResit(r);
		} catch (Exception e) {
			System.out.println("Error getRelatedData : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
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

	@Override
	public void save(UtilPermohonan r) throws Exception {
		// userId = (String) request.getSession().getAttribute("_portal_login");
		// userRole = (String)
		// request.getSession().getAttribute("_portal_role");
		String idPemohon = "";
		String flagAwam = get("flagAwam");
		String flagSwasta = get("flagSwasta");
		try {
			mp = new MyPersistence();
			idPemohon = get("noPendaftaran");
			r.setDewan((UtilDewan) mp.find(UtilDewan.class, get("idDewan")));
			r.setTarikhMula(getDate("tarikhTempahan"));
			r.setTarikhTamat(getDate("tarikhTempahanTamat"));
			r.setPemohon((Users) mp.find(Users.class, idPemohon));
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
			r.setStatusPermohonan("R");
			//r.setStatusPermohonan("Y");
			r.setStatusAktif("1");
			r.setTarikhPermohonan(new Date());
			kadarSewa(r);
		} catch (Exception e) {
			System.out.println("Error save : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
	}

	public void kadarSewa(UtilPermohonan r) throws Exception {
		if (r.getFlagAwam().equals("Y"))// if kakitangan awam, penghuni dan
		// majlis kenduri
		{
			double sewa = r.getDewan().getKadarSewaAwam();
			Date tarikhMula = r.getTarikhMula();
			Date tarikhTamat = r.getTarikhTamat();
			long diff = tarikhTamat.getTime() - tarikhMula.getTime();
			long jumlahHari = (diff / (24 * 60 * 60 * 1000)) + 1;
			double jumlahSewa = jumlahHari * sewa;
			r.setAmaun(jumlahSewa);
		} else // if swasta/lain-lain
		{
			double sewa = r.getDewan().getKadarSewa();
			Date tarikhMula = getDate("tarikhTempahan");
			Date tarikhTamat = getDate("tarikhTempahanTamat");
			;
			long diff = tarikhTamat.getTime() - tarikhMula.getTime();
			long jumlahHari = (diff / (24 * 60 * 60 * 1000)) + 1;
			int mula = r.getMasaMula();
			int tamat = r.getMasaTamat();
			int jum = tamat - mula;
			double jumlahjam = (double) jum;
//			double jumlahSewa = jumlahHari * jumlahjam * sewa;
			/** 08082018 CHANGES BY PEJE - TUKAR KADAR SEWA BAGI CAWANGAN / SURAT JKPTG **/
			double jumlahSewa = jumlahHari * sewa;
			r.setAmaun(jumlahSewa);
		}
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String findNoPengenalan = get("find_noPengenalan");
		String findNoTempahan = get("find_noTempahan");
		String findDewan = get("find_dewan");
		// String findNegeri = get("findNegeri");
		// String findBandar = get("findBandar");

		map.put("pemohon.noKP", findNoPengenalan);
		map.put("id", findNoTempahan);
		map.put("dewan.nama", findDewan);
		// map.put("dewan.bandar.negeri.id", new OperatorEqualTo(findNegeri));
		// map.put("dewan.bandar.id", new OperatorEqualTo(findBandar));
		return map;
	}

	@Command("getMaklumatPemohonBerdaftar")
	public String getMaklumatPemohonBerdaftar() throws Exception {
		String noPendaftaran = get("noPendaftaran").trim();
		try {
			mp = new MyPersistence();
			Users pemohon = (Users) mp.find(Users.class, noPendaftaran);
			if (pemohon != null) {
				context.put("pemohon", pemohon);
			} else {
				context.put("pemohon", "");
			}
			context.put("noPendaftaran", noPendaftaran);
			context.put("selectedTab", "2");
		} catch (Exception e) {
			System.out.println("Error getMaklumatPemohonBerdaftar : "
					+ e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
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
		List<UtilGelanggang> list = null;
		try {
			mp = new MyPersistence();
			list = mp
					.list("select x from UtilGelanggang x order by x.nama asc");
		} catch (Exception e) {
			System.out.println("Error getMaklumatPemohonBerdaftar : "
					+ e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
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
		String flagAwam = get("flagAwam");
		String flagSwasta = get("flagSwasta");
		UtilPermohonan tempahan = null;
		try {
			mp = new MyPersistence();
			tempahan = (UtilPermohonan) mp.find(UtilPermohonan.class,
					get("idTempahan"));
			tempahan.setDewan((UtilDewan) mp.find(UtilDewan.class,
					get("idDewan")));
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
			tempahan.setMasaMula(getParamAsInteger("masaMula"));
			tempahan.setMasaTamat(getParamAsInteger("masaTamat"));
			tempahan.setTujuan(get("tujuan"));
			kadarSewa(tempahan);
			kemaskiniRecordBayaran(tempahan);
//			mp.persist(tempahan);
			mp.begin();
			mp.commit();
//			updateJadualTempahan(tempahan.getDewan().getId(), tempahan
//					.getTarikhMula(), tempahan.getMasaMula(), tempahan
//					.getMasaTamat(), "B", tempahan.getId());
		} catch (Exception e) {
			System.out.println("Error savePermohonan : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("r", tempahan);
		return getPath() + "/entry_page.vm";
	}

	@Command("permohonanPengecualian")
	public String permohonanPengecualian() throws Exception {
		UtilPermohonan tempahan = null;
		try {
			mp = new MyPersistence();
			tempahan = (UtilPermohonan) mp.find(UtilPermohonan.class,
					get("idTempahan"));
			tempahan.setStatusPermohonan("R");// pengecualian bayaran
			mp.begin();
			mp.persist(tempahan);
			mp.commit();
		} catch (Exception e) {
			System.out.println("Error permohonanPengecualian : "
					+ e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/bayaran/statusButton.vm";

	}

	@Command("lulusPermohonan")
	public String lulusPermohonan() throws Exception {
		userId = (String) request.getSession().getAttribute("_portal_login");
		try {
			mp = new MyPersistence();
			UtilPermohonan tempahan = null;
			tempahan = (UtilPermohonan) mp.find(UtilPermohonan.class,get("idTempahan"));
			if (tempahan != null) {
				mp.begin();
				tempahan.setStatusPermohonan("Y");
				tempahan.setPelulus((Users) mp.find(Users.class, userId));
				tempahan.setTarikhKelulusan(new Date());
				createRecordBayaran(tempahan, mp);
				mp.commit();
				
				updateJadualTempahan(tempahan.getDewan().getId(), tempahan
						.getTarikhMula(), tempahan.getMasaMula(), tempahan
						.getMasaTamat(), "B", tempahan.getId());
			}
			

		} catch (Exception e) {
			System.out.println("Error lulusPermohonan : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		return getMaklumatTempahan();
	}
	
	@Command("lulusPermohonanTanpaBayaran")
	public String lulusPermohonanTanpaBayaran() throws Exception {
		try {
			mp = new MyPersistence();
			UtilPermohonan tempahan = null;
			tempahan = (UtilPermohonan) mp.find(UtilPermohonan.class,get("idTempahan"));
			if (tempahan != null) {
				mp.begin();
				tempahan.setStatusPermohonan("YY");
				tempahan.setPelulus((Users) mp.find(Users.class, userId));
				tempahan.setTarikhKelulusan(new Date());
				tempahan.setAmaun(0D);
				tempahan.setStatusBayaran("YY");
				mp.commit();
				
				updateJadualTempahan(tempahan.getDewan().getId(), tempahan
						.getTarikhMula(), tempahan.getMasaMula(), tempahan
						.getMasaTamat(), "B", tempahan.getId());
			}
			

		} catch (Exception e) {
			System.out.println("Error lulusPermohonanTanpaBayaran : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		return getMaklumatTempahan();
	}

	@Command("tolakPermohonan")
	public String tolakPermohonan() throws Exception {
		try {
			mp = new MyPersistence();
			UtilPermohonan tempahan = (UtilPermohonan) mp.find(UtilPermohonan.class,
					get("idTempahan"));
			if (tempahan != null) {
				mp.begin();
				tempahan.setStatusPermohonan("N");
				tempahan.setPelulus((Users) mp.find(Users.class, userId));
				tempahan.setTarikhKelulusan(new Date());
				mp.commit();
				deleteBooking(tempahan);
			}			
		} catch (Exception e) {
			System.out.println("Error tolakPermohonan : " + e.getMessage());
		} finally {
			
			if (mp != null) { mp.close(); }
		}
		return getMaklumatTempahan();
	}

	@Command("deletePermohonan")
	public void deletePermohonan() throws Exception {
		try {
			mp = new MyPersistence();
			UtilJadualTempahan jadualTempahan = (UtilJadualTempahan) mp
					.get("select x from UtilJadualTempahan x where x.permohonan.id = '"
							+ get("idTempahan") + "'");
			mp.remove(jadualTempahan);
			UtilPermohonan permohonan = (UtilPermohonan) mp.find(
					UtilPermohonan.class, get("idTempahan"));
			mp.remove(permohonan);
		} catch (Exception e) {
			System.out.println("Error deletePermohonan : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
	}

	@Command("saveDetailPemohon")
	public String saveDetailPemohon() throws Exception {
		try {
			mp = new MyPersistence();
			mp.begin();
			Users pemohon = (Users) mp.find(Users.class,
					getParam("noPendaftaran"));

			UtilPermohonan tempahan = null;
			tempahan = (UtilPermohonan) mp.find(UtilPermohonan.class,
					get("idTempahan"));
			tempahan.setPemohon(pemohon);
			
			UtilAkaun ua = (UtilAkaun) mp
					.get("select ua from UtilAkaun ua where ua.permohonan.id = '"
							+ get("idTempahan") + "' ");
			if (ua != null) {
				KewInvois inv = (KewInvois) mp
						.get("select x from KewInvois x where x.idLejar = '"
								+ ua.getId() + "' ");
				if (inv != null) {
					inv.setPembayar(pemohon);
				}
			}		
						
			mp.commit();
			getMaklumatPemohon();

		} catch (Exception e) {
			System.out.println("Error saveDetailPemohon : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
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
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/entry_page.vm";
	}

	@Command("getMaklumatPemohon")
	public String getMaklumatPemohon() {
		String noPendaftaran = "";
		try {
			mp = new MyPersistence();
			UtilPermohonan permohonan = (UtilPermohonan) mp.find(
					UtilPermohonan.class, get("idTempahan"));
			context.put("r", permohonan);
			if (permohonan != null) {
				try {
					noPendaftaran = permohonan.getPemohon().getId();
				} catch (Exception e) {
					noPendaftaran = "";// kes tak isi maklumat pemohon
				}
				Users pemohon = (Users) mp.find(Users.class, noPendaftaran);
				if (pemohon != null) {
					context.put("pemohon", pemohon);
				} else {
					context.put("pemohon", "");
				}
			}
			context.put("noPendaftaran", noPendaftaran);
			context.put("selectedTab", "2");

		} catch (Exception e) {
			System.out.println("Error editMaklumatPeralatan : "
					+ e.getMessage());
		}
		return getPath() + "/entry_page.vm";
	}

	@SuppressWarnings("unchecked")
	private List<UtilPermohonan> getListPermohonan(UtilPermohonan r) {
		List<UtilPermohonan> listUtilPermohonanDewan = mp
				.list("select x from UtilPermohonan x where x.id = '"
						+ r.getId() + "' order by x.id asc");
		return listUtilPermohonanDewan;
	}

	@Command("getBayaran")
	public String getBayaran() {
		this.setDisableBackButton(true);
		try {
			mp = new MyPersistence();
			UtilPermohonan r = (UtilPermohonan) mp.find(UtilPermohonan.class,
					getParam("idTempahan"));
			context.put("r", r);
			context.put("listUtilPermohonan", getListPermohonan(r));
			userRole = (String) request.getSession().getAttribute(
					"_portal_role");
			context.put("userRole", userRole);
			context.put("selectedTab", "3");
		} catch (Exception e) {
			System.out.println("Error getBayaran : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/entry_page.vm";
	}

	@SuppressWarnings("unchecked")
	@Command("getDokumenSokongan")
	public String getDokumenSokongan() {
		List<JenisDokumen> list = dataUtil.getListJenisDokumen();
		context.put("selectJenisDokumen", list);
		try {
			mp = new MyPersistence();
			UtilPermohonan tempahan = (UtilPermohonan) mp.find(
					UtilPermohonan.class, get("idTempahan"));
			context.put("r", tempahan);
			List<UtilDokumen> listDokumen = mp
					.list("SELECT x FROM UtilDokumen x WHERE x.tempahan.id = '"
							+ tempahan.getId() + "'");
			context.put("listDokumen", listDokumen);
			context.put("selectedTab", "4");
		} catch (Exception e) {
			System.out.println("Error editMaklumatPeralatan : "
					+ e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/entry_page.vm";
	}

	// @Command("bayarTempahan")
	// public String bayarTempahan() throws Exception {
	// userId = (String) request.getSession().getAttribute("_portal_login");
	// Users user = mp.find(Users.class, userId);
	// String idTempahan = getParam("idTempahan");
	// UtilPermohonan r = mp.find(UtilPermohonan.class, idTempahan);
	// context.put("r", r);
	//
	// Users pemohon = r.getPemohon();
	// if (pemohon == null) {
	// context.put("statusPemohon", "");
	// return getPath() + "/bayaran/statusButton.vm";
	// }
	// context.put("statusPemohon", "OK");
	// mp.begin();
	// r.setStatusBayaran("Y");
	// Double kadarSewa = 0d;
	//
	// if (r.getFlagAwam() == "Y") {
	// kadarSewa = r.getDewan().getKadarSewaAwam();
	// } else {
	// kadarSewa = r.getDewan().getKadarSewa();
	// }
	//
	// Double debitUtil = r.getAmaun();
	//
	// // push dalam ledger Util ()
	// UtilAkaun mn = new UtilAkaun();
	// mn.setPermohonan(r);
	// mn.setAmaunBayaranSeunit(kadarSewa);
	// mn.setDebit(debitUtil);
	// mn.setKredit(0d);
	// mn.setFlagBayar("T");
	// mn.setFlagVoid("T");
	// mn.setKeterangan("SEWA DEWAN");
	// mn.setKodHasil(mp.find(KodHasil.class, "74299")); // BAYARAN-BAYARAN
	// // LAIN
	// mn.setNoInvois(r.getId()); // TEMPORARY SET TO ID PERMOHONAN
	// mn.setTarikhInvois(new Date());
	// mn.setIdMasuk(user);
	// mn.setTarikhMasuk(new Date());
	// mp.persist(mn);
	// mp.commit();
	//
	// mp.begin();
	// // push masuk Kewangan
	// KewInvois inv = new KewInvois();
	// inv.setDebit(debitUtil);
	// inv.setFlagBayaran("SEWA"); // SEWA / DEPOSIT
	// inv.setFlagQueue("T");
	// inv.setIdLejar(r.getId());
	// inv.setJenisBayaran(mp.find(KewJenisBayaran.class, "03")); // 03 � UTIL
	// inv.setKodHasil(mn.getKodHasil());
	// inv.setNoInvois(mn.getNoInvois());
	// inv.setNoRujukan(mn.getPermohonan().getIdTempahan().toUpperCase()); //
	// no.fail/tempahan/no.permohonan/dll
	// inv.setPembayar(mn.getPermohonan().getPemohon());
	// inv.setTarikhInvois(mn.getTarikhInvois());
	// inv.setUserPendaftar(user); // user login
	// inv.setTarikhDaftar(new Date());
	// mp.persist(inv);
	// mp.commit();
	// return getPath() + "/bayaran/statusButton.vm";
	// }

	/** START PEMBAYARAN FPX **/

	@Command("bayarTempahanFPX")
	public String bayarTempahanFPX() throws Exception {
		return getPath() + "/fpx/Main.vm";
	}

	@Command("paparPilihan")
	public String paparPilihan() throws Exception {
		try {
			mp = new MyPersistence();
			String idTempahan = getParam("idTempahan");
			UtilPermohonan r = (UtilPermohonan) mp.find(UtilPermohonan.class,
					idTempahan);
			String serverName = request.getServerName();
			String contextPath = request.getContextPath();
			int serverPort = request.getServerPort();
			String server = serverPort != 80 ? serverName + ":" + serverPort
					: serverName;
			String image_url = "http://" + server + contextPath;
			context.put("imageUrl", image_url);

			String fpx_checkSum = "";
			String final_checkSum = "";
			String fpx_msgType = "AR";
			String fpx_msgToken = "01";
			// String fpx_sellerExId = "EX00000902";
			String fpx_sellerExId = "EX00000345";
			String fpx_sellerExOrderNo = new SimpleDateFormat("yyyyMMddHHmmss")
					.format(new Date());
			String fpx_sellerTxnTime = new SimpleDateFormat("yyyyMMddHHmmss")
					.format(new Date());
			String fpx_sellerOrderNo = new SimpleDateFormat("yyyyMMddHHmmss")
					.format(new Date());
			// String fpx_sellerId = "SE00001209";
			String fpx_sellerId = "SE00000392";
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
			// String fpx_productDesc="product description";
			String fpx_productDesc = "Pembayaran Tempahan Dewan";
			String fpx_version = "5.0";

			fpx_checkSum = fpx_buyerAccNo + "|" + fpx_buyerBankBranch + "|"
					+ fpx_buyerBankId + "|" + fpx_buyerEmail + "|"
					+ fpx_buyerIban + "|" + fpx_buyerId + "|" + fpx_buyerName
					+ "|";
			fpx_checkSum += fpx_makerName + "|" + fpx_msgToken + "|"
					+ fpx_msgType + "|" + fpx_productDesc + "|"
					+ fpx_sellerBankCode + "|" + fpx_sellerExId + "|";
			fpx_checkSum += fpx_sellerExOrderNo + "|" + fpx_sellerId + "|"
					+ fpx_sellerOrderNo + "|" + fpx_sellerTxnTime + "|"
					+ fpx_txnAmount + "|" + fpx_txnCurrency + "|" + fpx_version;

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
			// context.put("mercID", "TEST10701400013");
			context.put("mercID", "10701400013");
			context.put("itemDesc", "Pembayaran Tempahan Dewan");
			Double jum = r.getAmaun() * 100;
			String jumlah = jum.toString();

			String[] amaun = jumlah.split("\\.");
			String amaun1 = amaun[0].toString();
			context.put("amaun", amaun1);
			context.put("hashCode", "C4342B9D0330CDD09FB7337EAF95FDA8");

			HttpSession session = request.getSession();
			session.setAttribute("sesIdPermohonan", r.getId());
			session.setAttribute("sesModul", "UTIL");
			session.setAttribute("sesRole", (String) request.getSession()
					.getAttribute("_portal_role"));
		} catch (Exception e) {
			System.out.println("Error paparPilihan : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		return "bph/modules/fpx/pilihan.vm";
	}

	/** END PEMBAYARAN FPX **/

	/** START DOKUMEN SOKONGAN **/
	@SuppressWarnings("unchecked")
	@Command("uploadDoc")
	public String uploadPhoto() throws Exception {
		String idTempahan = get("idTempahan");
		String tajukDokumen = get("tajukDokumen");
		String idJenisDokumen = get("idJenisDokumen");
		String keteranganDokumen = get("keteranganDokumen");
		UtilDokumen dokumen = new UtilDokumen();
		// String uploadDir =
		// ResourceBundle.getBundle("dbconnection").getString("folder") +
		// "utiliti/permohonan/dokumenSokongan/";
		String uploadDir = "utiliti/permohonan/dokumenSokongan/";
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
		try {
			mp = new MyPersistence();

			UtilPermohonan permohonan = (UtilPermohonan) mp.find(
					UtilPermohonan.class, idTempahan);
			dokumen.setTempahan(permohonan);
			dokumen.setPhotofilename(imgName);
			dokumen.setThumbfilename(avatarName);
			dokumen.setTajuk(tajukDokumen);
			dokumen.setJenisDokumen((JenisDokumen) mp.find(JenisDokumen.class,
					idJenisDokumen));
			dokumen.setKeterangan(keteranganDokumen);

			mp.begin();
			mp.persist(dokumen);
			mp.commit();
		} catch (Exception e) {
			System.out.println("Error simpanDokumen : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
	}

	@Command("deleteDokumen")
	public String deleteDokumen() throws Exception {
		String idDokumen = get("idDokumen");
		try {
			mp = new MyPersistence();

			UtilDokumen dokumen = (UtilDokumen) mp.find(UtilDokumen.class,
					idDokumen);

			if (dokumen != null) {
				Util.deleteFile(dokumen.getPhotofilename());
				Util.deleteFile(dokumen.getThumbfilename());
				mp.begin();
				mp.remove(dokumen);
				mp.commit();
			}
		} catch (Exception e) {
			System.out.println("Error deleteDokumen : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
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
		try {
			mp = new MyPersistence();
			mp.begin();
			UtilJadualTempahan jadualTempahan = (UtilJadualTempahan) mp
					.get("select x from UtilJadualTempahan x where x.permohonan.id = '"
							+ idTempahan + "'");
			UtilDewan dewan = (UtilDewan) mp.find(UtilDewan.class, idDewan);
			jadualTempahan.setDewan(dewan);
			jadualTempahan.setTarikhTempahan(tarikhTempahan);
			jadualTempahan.setMasaMula(masaMula);
			jadualTempahan.setMasaTamat(masaTamat);
			mp.commit();
			paintJadual();
		} catch (Exception e) {
			System.out
					.println("Error updateJadualTempahan : " + e.getMessage());
		}
	}

	private void insertJadualTempahan(String idDewan, Date tarikhTempahan,
			int masaMula, int masaTamat, String idStatus, String idTempahan)
			throws Exception {
		try {
			mp = new MyPersistence();
			mp.begin();
			UtilDewan dewan = (UtilDewan) mp.find(UtilDewan.class, idDewan);
			UtilJadualTempahan tempJadualTempahan = new UtilJadualTempahan();
			tempJadualTempahan.setDewan(dewan);
			tempJadualTempahan.setTarikhTempahan(tarikhTempahan);
			tempJadualTempahan.setMasaMula(masaMula);
			tempJadualTempahan.setMasaTamat(masaTamat);
			tempJadualTempahan.setStatus(idStatus);
			UtilPermohonan permohonan = (UtilPermohonan) mp
					.get("select x from UtilPermohonan x where x.idTempahan = '"
							+ idTempahan + "' ");
			tempJadualTempahan.setPermohonan(permohonan);
			mp.persist(tempJadualTempahan);
			mp.commit();
		} catch (Exception e) {
			System.out
					.println("Error insertJadualTempahan : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
	}

	@SuppressWarnings("unchecked")
	private void insertTempahanRelated(String idDewan, Date tarikhTempahan,
			int masaMula, int masaTamat, String idStatus, String idTempahan)
			throws Exception {
		// mp.begin();
		try {
			mp = new MyPersistence();
			mp.begin();
			UtilDewan dewan = (UtilDewan) mp.find(UtilDewan.class, idDewan);
			Hashtable h = new Hashtable();
			h.put("idDewan", idDewan);
			List<UtilGelanggang> list = mp
					.list(
							"select t from UtilGelanggang t where t.dewan.id = :idDewan",
							h);
			UtilGelanggang tempGelanggang = null;
			for (int y = 0; y < list.size(); y++) {
				tempGelanggang = list.get(y);
				UtilJadualTempahan tempJadualTempahan = new UtilJadualTempahan();
				tempJadualTempahan.setDewan(dewan);
				UtilGelanggang gelanggang = (UtilGelanggang) mp.find(
						UtilGelanggang.class, tempGelanggang.getId());
				tempJadualTempahan.setGelanggang(gelanggang);
				tempJadualTempahan.setTarikhTempahan(tarikhTempahan);
				tempJadualTempahan.setMasaMula(masaMula);
				tempJadualTempahan.setMasaTamat(masaTamat);
				tempJadualTempahan.setStatus("C");
				// UtilPermohonan permohonan =
				// mp.find(UtilPermohonan.class,idTempahan);
				UtilPermohonan permohonan = (UtilPermohonan) mp
						.get("select x from UtilPermohonan x where x.idTempahan = '"
								+ idTempahan + "' ");

				tempJadualTempahan.setPermohonan(permohonan);
				mp.persist(tempJadualTempahan);
			}
			mp.commit();

		} catch (Exception e) {
			System.out.println("Error insertTempahanRelated : "
					+ e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
	}

	@SuppressWarnings("unchecked")
	public String paintJadualRelated(UtilPermohonan tempahan) {
		String bgcolour = "#008800";
		Date tarikhTempahan = tempahan.getTarikhMula();
		String idDewan = tempahan.getDewan().getId();
		Hashtable h = new Hashtable();
		h.put("idAset", idDewan);
		h.put("date", tarikhTempahan);

		try {
			mp = new MyPersistence();

			// List<TempJadualTempahan> list =
			// mp.list("select t from TempJadualTempahan t where t.tempAsetPBT.id = :idAset and t.tarikhTempahan = :date",
			// h);

			List<UtilJadualTempahan> list = mp
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

		} catch (Exception e) {
			System.out.println("Error editMaklumatPeralatan : "
					+ e.getMessage());
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
		try {
			mp = new MyPersistence();

			List<UtilJadualTempahan> list = mp
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
		} catch (Exception e) {
			System.out.println("Error paintJadual : " + e.getMessage());
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

	public void createRecordBayaran(UtilPermohonan r, MyPersistence mp) {
		userId = (String) request.getSession().getAttribute("_portal_login");
		try {
			Users user = (Users) mp.find(Users.class, userId);
			String catatan = null;
			Double kadarSewa = r.getAmaun();
			// create main ledger (tempahan)
			UtilAkaun mn = new UtilAkaun();
			mn.setPermohonan(r);
			mn.setAmaunBayaranSeunit(kadarSewa);
			mn.setBilanganUnit(1);
			mn.setDebit(r.getAmaun());
			mn.setKredit(0d);
			mn.setFlagBayar("T");
			mn.setFlagVoid("T");
			mn.setKeterangan("BAYARAN BAGI SEWAAN " + r.getDewan().getNama());
			mn.setCatatan(catatan);
			mn.setKodHasil((KodHasil) mp.find(KodHasil.class, "74299")); // BAYARAN-BAYARAN
			// SEWA
			// YANG
			// LAIN
			mn.setNoInvois(r.getIdTempahan()); // TEMPORARY SET TO ID TEMPAHAN//
			// PERMOHONAN
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
		
		try {
			// push to invois kewangan
			KewInvois inv = new KewInvois();
			inv.setFlagBayar("T");
			inv.setDebit(ak.getDebit());
			// inv.setKredit(ak.getDebit());
			inv.setFlagBayaran("SEWA");
			inv.setFlagQueue("T");
			inv.setIdLejar(ak.getId());
			inv.setJenisBayaran((KewJenisBayaran) mp.find(
					KewJenisBayaran.class, "03")); // 03 - UTILITI
			inv.setKeteranganBayaran(ak.getKeterangan().toUpperCase());
			inv.setKodHasil(ak.getKodHasil());
			inv.setNoInvois(ak.getNoInvois());
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

	public void kemaskiniRecordBayaran(UtilPermohonan r) {
		userId = (String) request.getSession().getAttribute("_portal_login");
		Users user = (Users) mp.find(Users.class, userId);
		Double kadarSewa = r.getAmaun();

		try {
			UtilAkaun mn = (UtilAkaun) mp
					.get("select x from UtilAkaun x where x.permohonan.id = '"
							+ r.getIdTempahan() + "' ");
			mn.setAmaunBayaranSeunit(kadarSewa);
			mn.setIdKemaskini(user);
			mn.setTarikhKemaskini(new Date());
		} catch (Exception e) {
			System.out.println("Error kemaskiniRecordBayaran : "
					+ e.getMessage());
		}
	}
}
