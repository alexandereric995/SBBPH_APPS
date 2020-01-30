package bph.modules.utiliti;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import bph.entities.kewangan.KewResitKaedahBayaran;
import bph.entities.kewangan.KewResitSenaraiInvois;
import bph.entities.kod.Bandar;
import bph.entities.kod.CaraBayar;
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
import bph.utils.UtilKewangan;
import db.persistence.MyPersistence;

public class SenaraiPermohonanGelanggangRecordModule extends LebahRecordTemplateModule<UtilPermohonan> {
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
		createRecordBayaran(r);
		try {
			insertJadualTempahan(r.getDewan().getId(), r.getGelanggang()
					.getId(), r.getTarikhMula(), r.getMasaMula(), r
					.getMasaTamat(), "B", r.getId());
			insertTempahanRelated(r.getDewan().getId(), r.getTarikhMula(), r
					.getMasaMula(), r.getMasaTamat(), "C", r.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		context.put("selectedTab", "2");
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
		
		@SuppressWarnings("unused")
		HttpSession session = request.getSession();
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
			context.put("selectDewan", dataUtil.getListDewanGelanggang(idCawangan));
		}else{
			context.put("selectDewan", dataUtil.getListDewanGelanggangPenyedia());	
		}
		context.put("selectNegeri", dataUtil.getListNegeri());
		context.put("userRole", userRole);
		paintJadualMula();
	}

	private void addfilter() {
		if (userRole.equalsIgnoreCase("(UTILITI) Pelulus")) {
			this.addFilter("gelanggang is not null");
			this.addFilter("statusAktif = '1' ");
			this.addFilter("statusPermohonan = 'R' ");// perlu kelulusan
			setOrderBy("tarikhMula ASC");
		} else {
			// COMMENT BY PEJE - DATA X KELUAR KENE CEK BALIK FILTER NIH
			this.addFilter("tarikhMula >= '"+ Util.getDateTime(new Date(), "yyyy-MM-dd") + "'");
			this.addFilter("gelanggang is not null");
			this.addFilter("statusAktif = '1' ");
			setOrderBy("tarikhMula ASC");
		
			// this.addFilter("statusBayaran='Y'");
			// this.addFilterOr("statusPermohonan='R'");
		}
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
	public void deleteTemporary() throws Exception {
		try {
			mp = new MyPersistence();
			mp.begin();
			UtilPermohonan permohonanVolatile = null;
			List<UtilPermohonan> listPermohonan = mp
					.list("select x from UtilPermohonan x where x.statusBayaran='T' and x.gelanggang is not null and x.statusPermohonan<>'R'");
			for (int x = 0; x < listPermohonan.size(); x++) {
				permohonanVolatile = listPermohonan.get(x);
				if (permohonanVolatile != null) {
					UtilJadualTempahan jadualTempahan = null;
					List<UtilJadualTempahan> listTempahan = mp
							.list("select x from UtilJadualTempahan x where x.permohonan.id = '"
									+ permohonanVolatile.getId() + "'");
					for (int y = 0; y < listTempahan.size(); y++) {
						jadualTempahan = listTempahan.get(y);
						if (jadualTempahan != null) {
							mp.remove(jadualTempahan);
						}
					}
					mp.remove(permohonanVolatile);
				}
			}
			mp.commit();
		} catch (Exception e) {
			System.out.println("Error deleteTemporary : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean delete(UtilPermohonan r) throws Exception {	
		try {
			mp = new MyPersistence();
			UtilJadualTempahan jadualTempahan = (UtilJadualTempahan) mp
					.get("select x from UtilJadualTempahan x where x.permohonan.id = '"
							+ r.getId() + "'");
			KewInvois invois = (KewInvois) mp
					.get("select x from KewInvois x where x.noInvois = '"
							+ r.getId() + "'");
			if (r.getStatusBayaran().equals("Y")) {
				return false;
			} else {
				mp.begin();
				if(jadualTempahan!=null)
					mp.remove(jadualTempahan);
				if(invois!=null)
					mp.remove(invois);
				mp.commit();
				context.put("error_flag","record_delete_success" );
			}
		} catch (Exception e) {
			System.out.println("error delete : "
					+ e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return true;
	}

	@SuppressWarnings("unchecked")
	public void deleteBooking(UtilPermohonan r) {
		UtilJadualTempahan jadualTempahan = null;
		List<UtilJadualTempahan> list = mp
				.list("select x from UtilJadualTempahan x where x.permohonan.id = '"
						+ r.getId() + "'");

		for (int y = 0; y < list.size(); y++) {
			jadualTempahan = list.get(y);
			mp.remove(jadualTempahan);
		}
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/utiliti/senaraiPermohonanGelanggang";
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
		try {
			mp = new MyPersistence();

			tempahan = (UtilPermohonan) mp
					.find(UtilPermohonan.class, r.getId());
			context.put("tempahan", tempahan);
			String statusBayar = null;
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
		// TODO Auto-generated method stub
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
								resit.setIdPermohonan(r.getId());
								r.setResitSewa(resit);
								mp.begin();
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
		try {
			mp = new MyPersistence();
			r.setDewan((UtilDewan) mp.find(UtilDewan.class, get("idDewan")));
			r.setGelanggang((UtilGelanggang) mp.find(UtilGelanggang.class,
					get("idGelanggang")));
			r.setTarikhMula(getDate("tarikhTempahan"));
			r.setTarikhTamat(getDate("tarikhTempahan"));
			r.setMasaMula(getParamAsInteger("masaMula"));
			r.setMasaTamat(getParamAsInteger("masaTamat"));
			r.setTujuan(get("tujuan"));
			r.setJenisPermohonan("WALKIN");
			r.setStatusBayaran("T");
			r.setStatusAktif("1");
			r.setStatusPermohonan("Y");
			r.setTarikhPermohonan(new Date());
			kadarSewa(r);

		} catch (Exception e) {
			System.out.println("Error save : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
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
		double sewaSiang = r.getGelanggang().getKadarSewa();// kadar sewa siang
		double sewaMalam = r.getGelanggang().getKadarSewaAwam();// kadar sewa
		// malam
		double kadarBayarSiang = hoursiang * sewaSiang;
		double kadarBayarMalam = hourmalam * sewaMalam;
		double jumlahSewa = kadarBayarSiang + kadarBayarMalam;
		r.setAmaun(jumlahSewa);
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String findNoPengenalan = get("find_noPengenalan");
		String findNoTempahan = get("find_noTempahan");
		String findGelanggang = get("find_gelanggang");
		String findLokasi = get("find_lokasi");
		map.put("pemohon.noKP", findNoPengenalan);
		map.put("id", findNoTempahan);
		map.put("gelanggang.nama", findGelanggang);
		map.put("gelanggang.lokasi", findLokasi);
		map.put("tarikhMula", getDate("findTarikhMula"));
		return map;
	}

	@Command("findGelanggang")
	public String findGelanggang() throws Exception {

		String findDewan = "";
		if (get("findDewan").trim().length() > 0)
			findDewan = get("findDewan");
		List<UtilGelanggang> list = dataUtil.getListGelanggang(findDewan);
		context.put("findGelanggang", list);
		return getPath() + "/findGelanggang.vm";
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
		try {
			mp = new MyPersistence();
			context.put("noPendaftaran", "");
			context.put("pemohon", null);
			String noPendaftaran = "";
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

	@Command("getBayaran")
	public String getBayaran() {
		try {
			mp = new MyPersistence();
			UtilPermohonan permohonan = (UtilPermohonan) mp.find(
					UtilPermohonan.class, get("idTempahan"));
			context.put("d", permohonan);
			context.put("listUtilPermohonan", getListPermohonan(permohonan));
			context.put("selectedTab", "3");
		} catch (Exception e) {
			System.out.println("Error getBayaran : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
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
			System.out.println("Error editMaklumatPeralatan : "
					+ e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		return listUtilPermohonanDewan;
	}

	/** END TAB **/

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

	// private List<UtilGelanggang> getListGelanggang(String idGelanggang) {
	// List<UtilGelanggang> list = null;
	// try {
	// mp = new MyPersistence();
	// list = mp
	// .list("select x from UtilGelanggang x order by x.nama asc");
	// } catch (Exception e) {
	// System.out.println("Error editMaklumatPeralatan : "
	// + e.getMessage());
	// } finally {
	// if (mp != null) { mp.close(); }
	// }
	// return list;
	// }

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
		try {
			mp = new MyPersistence();
			UtilPermohonan tempahan = null;
			tempahan = (UtilPermohonan) mp.find(UtilPermohonan.class,
					get("idTempahan"));
			tempahan.setDewan((UtilDewan) mp.find(UtilDewan.class,
					get("idDewan")));
			tempahan.setGelanggang((UtilGelanggang) mp.find(
					UtilGelanggang.class, get("idGelanggang")));
			tempahan.setTarikhMula(getDate("tarikhTempahan"));
			tempahan.setTarikhTamat(getDate("tarikhTempahan"));
			tempahan.setMasaMula(getParamAsInteger("masaMula"));
			tempahan.setMasaTamat(getParamAsInteger("masaTamat"));
			tempahan.setTujuan(get("tujuan"));
			kadarSewa(tempahan);
			// kemaskiniRecordBayaran(tempahan);
			mp.begin();
//			mp.persist(tempahan);
			mp.commit();
			updateJadualTempahan(tempahan.getDewan().getId(), tempahan
					.getGelanggang().getId(), tempahan.getTarikhMula(),
					tempahan.getMasaMula(), tempahan.getMasaTamat(), "B",
					tempahan.getId());
		} catch (Exception e) {
			System.out.println("Error savePermohonan : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		getMaklumatTempahan();
		return getPath() + "/maklumatTempahan.vm";
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
		UtilPermohonan tempahan = null;
		try {
			mp = new MyPersistence();
			tempahan = (UtilPermohonan) mp.find(UtilPermohonan.class,
					get("idTempahan"));
			tempahan.setStatusPermohonan("Y");
			tempahan.setAmaun(0d);
			tempahan.setStatusBayaran("Y");
			mp.begin();
			mp.persist(tempahan);
			mp.commit();
			updateJadualTempahan(tempahan.getDewan().getId(), tempahan
					.getGelanggang().getId(), tempahan.getTarikhMula(),
					tempahan.getMasaMula(), tempahan.getMasaTamat(), "B",
					tempahan.getId());
		} catch (Exception e) {
			System.out.println("Error lulusPermohonan : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/bayaran/start.vm";
	}

	@Command("tolakPermohonan")
	public String tolakPermohonan() throws Exception {
		try {
			mp = new MyPersistence();
			UtilPermohonan tempahan = null;
			tempahan = (UtilPermohonan) mp.find(UtilPermohonan.class,
					get("idTempahan"));
			tempahan.setStatusPermohonan("N");
			mp.begin();
			mp.persist(tempahan);
			deleteBooking(tempahan);
			mp.commit();
		} catch (Exception e) {
			System.out.println("Error tolakPermohonan : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/bayaran/start.vm";
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
			Users pemohon = (Users) mp.find(Users.class,
					getParam("noPendaftaran"));

			UtilPermohonan tempahan = null;
			tempahan = (UtilPermohonan) mp.find(UtilPermohonan.class,
					get("idTempahan"));
			tempahan.setPemohon(pemohon);

			UtilAkaun ua = (UtilAkaun) mp
					.get("select ua from UtilAkaun ua where ua.permohonan.id = '"
							+ get("idTempahan") + "' ");
			KewInvois inv = (KewInvois) mp
					.get("select x from KewInvois x where x.idLejar = '"
							+ ua.getId() + "' ");
			if (inv != null) {
				inv.setPembayar(pemohon);
			}

			mp.begin();
			mp.commit();
			getMaklumatPemohon();
			context.put("selectedTab", "3");
		} catch (Exception e) {
			System.out.println("Error saveDetailPemohon : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/maklumatPemohon.vm";
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

	/** END DROP DOWN **/

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
			String fpx_productDesc = "Pembayaran Tempahan Gelanggang";
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
			context.put("itemDesc", "Pembayaran Tempahan Gelanggang");
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

	@SuppressWarnings("unchecked")
	@Command("getDokumenSokongan")
	public String getDokumenSokongan() {
		try {
			mp = new MyPersistence();
			List<JenisDokumen> list = dataUtil.getListJenisDokumen();
			context.put("selectJenisDokumen", list);
			UtilPermohonan tempahan = (UtilPermohonan) mp.find(
					UtilPermohonan.class, get("idTempahan"));
			context.put("r", tempahan);
			List<UtilDokumen> listDokumen = mp
					.list("SELECT x FROM UtilDokumen x WHERE x.tempahan.id = '"
							+ tempahan.getId() + "'");
			context.put("listDokumen", listDokumen);
			context.put("selectedTab", "4");
		} catch (Exception e) {
			System.out.println("Error getDokumenSokongan : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/entry_page.vm";
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

	private void updateJadualTempahan(String idDewan, String idGelanggang,
			Date tarikhTempahan, int masaMula, int masaTamat, String idStatus,
			String idTempahan) throws Exception {
		try {
			mp = new MyPersistence();
			mp.begin();
			UtilJadualTempahan jadualTempahan = (UtilJadualTempahan) mp
					.get("select x from UtilJadualTempahan x where x.permohonan.id = '"
							+ idTempahan + "'");
			UtilDewan dewan = (UtilDewan) mp.find(UtilDewan.class, idDewan);
			UtilGelanggang gelanggang = (UtilGelanggang) mp.find(
					UtilGelanggang.class, idGelanggang);
			jadualTempahan.setDewan(dewan);
			jadualTempahan.setGelanggang(gelanggang);
			jadualTempahan.setTarikhTempahan(tarikhTempahan);
			jadualTempahan.setMasaMula(masaMula);
			jadualTempahan.setMasaTamat(masaTamat);
			mp.commit();
			paintJadual();
		} catch (Exception e) {
			System.out.println("Error editMaklumatPeralatan : "
					+ e.getMessage());
		}
	}

	private void insertJadualTempahan(String idDewan, String idGelanggang,
			Date tarikhTempahan, int masaMula, int masaTamat, String idStatus,
			String idTempahan) throws Exception {
		try {
			mp = new MyPersistence();
			mp.begin();
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
			mp.commit();
		} catch (Exception e) {
			System.out
					.println("Error insertJadualTempahan : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
	}

	private void insertTempahanRelated(String idDewan, Date tarikhTempahan,
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
			tempJadualTempahan.setStatus("C");
			UtilPermohonan permohonan = (UtilPermohonan) mp.find(
					UtilPermohonan.class, idTempahan);
			tempJadualTempahan.setPermohonan(permohonan);
			mp.persist(tempJadualTempahan);
			mp.commit();
		} catch (Exception e) {
			System.out.println("Error editMaklumatPeralatan : "
					+ e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
	}

	@SuppressWarnings("unchecked")
	public String paintJadualRelated(UtilPermohonan tempahan) {
		String bgcolour = "#008800";
		Date tarikhTempahan = tempahan.getTarikhMula();
		String idGelanggang = tempahan.getGelanggang().getId();
		if (tarikhTempahan != null && idGelanggang != null) {
			Hashtable h = new Hashtable();
			h.put("idAset", idGelanggang);
			h.put("date", tarikhTempahan);
			try {
				mp = new MyPersistence();

				List<UtilJadualTempahan> list = mp
						.list(
								"select t from UtilJadualTempahan t where t.gelanggang.id = :idAset and t.tarikhTempahan = :date",
								h);
				UtilJadualTempahan tempJadualTempahan = null;

				// PAINT JADUAL TO GREEN
				int hari=tarikhTempahan.getDay();
				if(hari==0 || hari == 6)//weekend
				{
					for (int x = 7; x < 23; x++) {
						bgcolour = "#008800";
						context.put("hour" + (x + 1), bgcolour);
					}
				}else{
					for (int x = 16; x < 23; x++) {
						bgcolour = "#008800";
						context.put("hour" + (x + 1), bgcolour);
					}
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
				System.out.println("Error paintJadualRelated : " + e.getMessage());
			}
		}		
		return getPath() + "/jadualTempahan.vm";
	}

	@SuppressWarnings("unchecked")
	@Command("paintJadual")
	public String paintJadual() {
		try {
			mp = new MyPersistence();
			String bgcolour = "#008800";
			Date tarikhTempahan = getDate("tarikhTempahan");
			String idGelanggang = getParam("idGelanggang");
			if (tarikhTempahan != null && idGelanggang != null) {
				UtilGelanggang gelanggang = (UtilGelanggang) mp.find(UtilGelanggang.class, idGelanggang);
				
				Hashtable h = new Hashtable();
				h.put("idAset", idGelanggang);
				h.put("date", tarikhTempahan);
				@SuppressWarnings("deprecation")
				
				int hari=tarikhTempahan.getDay();
				List<UtilJadualTempahan> list = mp
						.list(
								"select t from UtilJadualTempahan t where t.gelanggang.id = :idAset and t.tarikhTempahan = :date",
								h);
				UtilJadualTempahan tempJadualTempahan = null;

				// PAINT JADUAL TO GREEN
				if(hari==0 || hari == 6)//weekend
				{
					for (int x = 7; x < 23; x++) {
						bgcolour = "#008800";
						context.put("hour" + (x + 1), bgcolour);
					}
				}else{
					//08082018 - CHANGES BY PEJE - UNTUK CAWANGAN WEEKDAYS JUGA BUKA PAGI KE PETANG
					if (gelanggang != null && gelanggang.getDewan() != null) {
						if (gelanggang.getDewan().getKodCawangan() != null) {
							for (int x = 7; x < 23; x++) {
								bgcolour = "#008800";
								context.put("hour" + (x + 1), bgcolour);
							}
						} else {
							for (int x = 16; x < 23; x++) {
								bgcolour = "#008800";
								context.put("hour" + (x + 1), bgcolour);
							}
						}
					} else {
						for (int x = 16; x < 23; x++) {
							bgcolour = "#008800";
							context.put("hour" + (x + 1), bgcolour);
						}
					}					
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
			}
			
		} catch (Exception e) {
			e.printStackTrace();
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

	public void createRecordBayaran(UtilPermohonan r) {

		userId = (String) request.getSession().getAttribute("_portal_login");
		try {
			mp = new MyPersistence();
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
			mp.begin();
			mp.commit();
			createInvoisInFinance(mn, user);
		} catch (Exception e) {
			System.out.println("Error createRecordBayaran : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}

	}

	public void createInvoisInFinance(UtilAkaun ak, Users user) {
		// push to invois kewangan
		KewInvois inv = new KewInvois();
		inv.setFlagBayar("T");
		inv.setDebit(ak.getDebit());
		// inv.setKredit(ak.getDebit());
		inv.setFlagBayaran("SEWA");
		inv.setFlagQueue("T");
		inv.setIdLejar(ak.getId());
		try {
			mp = new MyPersistence();
			inv.setJenisBayaran((KewJenisBayaran) mp.find(
					KewJenisBayaran.class, "03")); // 03 - UTILITI
			inv.setKeteranganBayaran(ak.getKeterangan().toUpperCase());
			inv.setKodHasil(ak.getKodHasil());
			inv.setNoInvois(ak.getNoInvois());
			// inv.setNoRujukan(ak.getPermohonan().getNoTempahan().toUpperCase());
			inv.setNoRujukan(ak.getPermohonan().getIdTempahan());
			// inv.setPembayar(ak.getPermohonan().getPemohon());
			inv.setTarikhInvois(ak.getTarikhInvois());
			inv.setUserPendaftar(user);
			inv.setTarikhDaftar(new Date());
			inv.setUserKemaskini(user);
			inv.setTarikhKemaskini(new Date());
			inv.setTarikhDari(ak.getPermohonan().getTarikhMula());
			inv.setTarikhHingga(ak.getPermohonan().getTarikhTamat());
			mp.persist(inv);
			mp.begin();
			mp.commit();
		} catch (Exception e) {
			System.out.println("Error createInvoisInFinance : "
					+ e.getMessage());
		}
	}

	@Command("bayarTempahan")
	public void updateBayarTempahan() {
		try {
			mp = new MyPersistence();
			mp.begin();
			String idTempahan = getParam("idTempahan");// local
			UtilPermohonan r = (UtilPermohonan) mp.find(UtilPermohonan.class,
					idTempahan);// local

			// CREATE RESIT
			KewBayaranResit resit = new KewBayaranResit();
			Users pembayar = r.getPemohon();
			if (pembayar != null) {
				resit.setPembayar(pembayar);
				resit.setNoPengenalanPembayar(pembayar.getId());
				resit.setNamaPembayar(pembayar.getUserName());				
				resit.setAlamatPembayar(UtilKewangan.getAlamatPembayar(pembayar));
			}
			resit.setNoResit(UtilKewangan.generateReceiptNo(mp, (Users) mp
					.find(Users.class, userId)));
			resit.setTarikhBayaran(new Date());
			resit.setTarikhResit(new Date());
			resit.setMasaResit(new Date());
			resit.setFlagJenisBayaran("ONLINE");
			resit.setTarikhDaftar(new Date());
			resit.setUserPendaftar((Users) mp.find(Users.class, userId));
			resit.setJumlahAmaunBayaran(r.getAmaun());
			resit.setFlagJenisResit("2"); // INVOIS
			mp.persist(resit);

			// CREATE KAEDAH BAYARAN
			KewResitKaedahBayaran kb = new KewResitKaedahBayaran();
			kb.setResit(resit);
			kb.setAmaunBayaran(resit.getJumlahAmaunBayaran());
			kb.setModBayaran((CaraBayar) mp.find(CaraBayar.class, "T"));
			mp.persist(kb);

			// update lejar
			// UtilAkaun mn = mp.find(UtilAkaun.class, r.getId());
			UtilAkaun mn = (UtilAkaun) mp
					.get("select x from UtilAkaun x where x.permohonan.id = '"
							+ r.getIdTempahan() + "' ");
			mn.setFlagBayar("Y");
			mn.setIdKemaskini((Users) mp.find(Users.class, userId));
			mn.setKredit(mn.getDebit());
			mn.setNoResit(resit.getNoResit());
			mn.setTarikhKemaskini(new Date());
			mn.setTarikhResit(resit.getTarikhResit());
			mn.setTarikhTransaksi(resit.getTarikhBayaran());

			if (mn.getKodHasil().getId().equalsIgnoreCase("74299")) {
				KewInvois inv = (KewInvois) mp
						.get("select x from KewInvois x where x.idLejar = '"
								+ mn.getId() + "' ");
				KewResitSenaraiInvois rsi = new KewResitSenaraiInvois();
				rsi.setInvois(inv);
				rsi.setResit(resit);
				rsi.setFlagJenisResit("INVOIS");
				mp.persist(rsi);
			}

			// UPDATE MAIN TABLE
			r.setStatusBayaran("Y");
			r.setStatusBayaran("Y");
			r.setTarikhBayaran(new Date());
			r.setResitSewa(resit);
			
			try {
				mp.commit();
			} catch (Exception e) {
				System.out.println("error " + e.getMessage());
				e.printStackTrace();
			}
		} catch (Exception e) {
			System.out.println("Error editMaklumatPeralatan : "
					+ e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
	}

}
