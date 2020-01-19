package bph.modules.qtr;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lebah.db.Db;
import lebah.db.DbException;
import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorDateBetween;
import lebah.template.UID;

import org.apache.log4j.Logger;

import portal.module.entity.Users;
import portal.module.entity.UsersJob;
import bph.entities.kod.Agensi;
import bph.entities.kod.Bandar;
import bph.entities.kod.GelaranDalamSurat;
import bph.entities.kod.JenisPenolakan;
import bph.entities.kod.KodHasil;
import bph.entities.kod.SebabPenolakan;
import bph.entities.kod.Status;
import bph.entities.qtr.KuaAgihan;
import bph.entities.qtr.KuaAkaun;
import bph.entities.qtr.KuaKuarters;
import bph.entities.qtr.KuaPermohonan;
import bph.entities.qtr.KuaTawaran;
import bph.utils.DataUtil;
import db.persistence.MyPersistence;

public class FrmKuaTawaranRecord extends LebahRecordTemplateModule<KuaTawaran> {

	private static final long serialVersionUID = 8517309942213366361L;
	static Logger myLogger = Logger.getLogger("bph/modules/qtr/FrmKuaTawaranRecord");
	private DataUtil dataUtil;
	private MyPersistence mp;

	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		return String.class;
	}

	@Override
	public Class<KuaTawaran> getPersistenceClass() {
		return KuaTawaran.class;
	}

	@Override
	public String getPath() {
		return "bph/modules/qtr/tawaran";
	}

	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);
		setDisableAddNewRecordButton(true);
		setDisableDefaultButton(true);
		setDisableBackButton(true);
		setDisableUpperBackButton(true);
		setReadonly(true);
		userId = (String) request.getSession().getAttribute("_portal_login");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		if (userRole.equalsIgnoreCase("(QTR) Penyedia")) {
			addFilter("agihan.petugas.id = '" + userId + "'");
		}
		context.put("selectJenisPenolakan", dataUtil.getListJenisPenolakan());
		context.put("selectSebabPenolakan", dataUtil.getListSebabPenolakan());
		context.put("selectGelaranDalamSurat",dataUtil.getListGelaranDalamSurat());
		context.put("selectNegeri", dataUtil.getListNegeri());
		context.put("selectKementerian", dataUtil.getListKementerian());
		context.put("path", getPath());
	}

	@Override
	public void save(KuaTawaran r) throws Exception {

	}

	@Override
	public boolean delete(KuaTawaran r) throws Exception {
		return true;
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		HashMap<String, Object> r = new HashMap<String, Object>();
		r.put("agihan.dateAgih",new OperatorDateBetween(getDate("findTarikhAgihan"), getDate("findTarikhAgihan")));
		r.put("agihan.permohonan.noPermohonan", getParam("findNoPermohonan"));
		r.put("agihan.pemohon.userName", getParam("findNamaPemohon"));
		r.put("agihan.pemohon.noKP", getParam("findNoKPPemohon"));
		return r;
	}

	@Override
	public void beforeSave() {

	}

	@Override
	public void afterSave(KuaTawaran r) {

	}

	@SuppressWarnings("unchecked")
	@Override
	public void getRelatedData(KuaTawaran r) {
		try {
			mp = new MyPersistence();
			UsersJob uj = (UsersJob) mp.get("SELECT uj FROM UsersJob uj WHERE uj.users.id = '" + r.getAgihan().getPemohon().getId() + "'");
			KuaTawaran tawaran = (KuaTawaran) mp.find(KuaTawaran.class, r.getId());
			userId = (String) request.getSession().getAttribute("_portal_login");
			userRole= (String) request.getSession().getAttribute("_portal_role");	
			context.put("uj", uj);
			context.put("offer", tawaran);
			context.put("r", tawaran);
			context.put("flaghantar", tawaran.getFlagHantar());
			context.put("currentRole", userRole);
			List<Users> users = mp.list("SELECT u FROM Users u WHERE u.id IN (" + getPelulus()+ ") OR u.role.description = '(QTR) Pelulus' ORDER BY u.userName ASC");
			context.put("selectPegawai", users);
		} catch (Exception e) {
			System.out.println("Error getRelatedData : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
	}

	@Command("getSuratTawaran")
	public String getSuratTawaran() {
		try {
			mp = new MyPersistence();
			KuaTawaran tawaran = (KuaTawaran) mp.find(KuaTawaran.class, getParam("idTawaran"));
			KuaPermohonan kp = (KuaPermohonan) mp.find(KuaPermohonan.class,tawaran.getAgihan().getPermohonan().getId());	
			UsersJob uj = (UsersJob) mp.get("select uj from UsersJob uj where uj.users.id = '" + kp.getPemohon().getId() + "'");
			GelaranDalamSurat gds = (GelaranDalamSurat) mp.find(GelaranDalamSurat.class,getParam("idGelaranDalamSurat"));
			Date d = getDate("tarikhSurat");
			if (d == null){
				d = new Date();
			}
			if (tawaran.getTarikhSurat() == null){
				tawaran.setTarikhSurat(new Date());
			}
			
			if (uj == null) {
				uj = new UsersJob();
			}
			
			Agensi agensi = (Agensi) mp.find(Agensi.class,getParam("idJabatan"));
			uj.setAgensi(agensi);
			uj.setBahagian(getParam("bahagian"));
			uj.setAlamat1(getParam("alamatPejabat1"));
			uj.setAlamat2(getParam("alamatPejabat2"));
			uj.setAlamat3(getParam("alamatPejabat3"));
			uj.setPoskod(getParam("alamatPoskod"));
			Bandar bandar = (Bandar) mp.find(Bandar.class,getParam("idBandar"));
			uj.setBandar(bandar);
			
			tawaran.setTarikhSuratSebenar(getDate("tarikhSurat"));
			tawaran.setTitleDalamSurat(gds);
			tawaran.setNoFail(getParam("noFail"));
			kp.setNoFail(getParam("noFail"));
			tawaran.setBil(getParam("bil"));
			tawaran.setCetakSurat("ya");
			tawaran.setKepada(getParam("kepadaSiapa"));
			tawaran.setGenerateEmail("Y");
			tawaran.setPegawai((Users) mp.find(Users.class, getParam("idPegawai")));
			tawaran.setNamaKementerian(getParam("kementerian"));
			tawaran.setNamaJabatan(getParam("jabatan"));
			tawaran.setNamaBahagian(getParam("bahagian"));
			tawaran.setNamaAlamat1(getParam("alamatPejabat1"));
			tawaran.setNamaAlamat2(getParam("alamatPejabat2"));
			tawaran.setNamaAlamat3(getParam("alamatPejabat3"));
			tawaran.setNamaPoskod(getParam("alamatPoskod"));
			tawaran.setNamaNegeri(getParam("alamatNegeri"));
			tawaran.setNamaBandar(getParam("alamatBandar"));
			tawaran.setFlagHantar(1);
			
			mp.begin();
			mp.commit();
		} catch (Exception e) {
			System.out.println("Error getSuratTawaran : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("idTawaran", getParam("idTawaran"));
		return getPath() + "/sub_page/suratTawaran.vm";
	}
			
	@SuppressWarnings("unchecked")
	@Command("simpanPermohonan")
	public String simpanPermohonan() {
		KuaTawaran tawaran = null;
		String idStatus = "";
		String alamatKuarters = "";
		String idStatusAgihan = "";
		boolean newDeposit = false;
		KuaAgihan ka = null;
		KuaKuarters kk = null;
		KuaAkaun kuaAkaun = null;
		String kelasDowngrade = "";
		String kelasTawaran="";
		try {
			mp = new MyPersistence();
			tawaran = (KuaTawaran) mp.find(KuaTawaran.class, getParam("idTawaran"));
			ka = (KuaAgihan) mp.find(KuaAgihan.class, tawaran.getAgihan().getId());
			kk = (KuaKuarters) mp.find(KuaKuarters.class, ka.getKuarters().getId());
			tawaran.setTarikhSuratDiterima(getDate("tarikhSuratDiterima"));
			tawaran.setBil(getParam("bil"));
			tawaran.setTarikhSuratSebenar(getDate("tarikhSurat"));
			tawaran.setCatatan(getParam("catatan"));
			tawaran.setTarikhKemaskini(new Date());
			tawaran.setStatusTawaran(getParam("statusTawaran"));
			if (getParam("statusTawaran").equalsIgnoreCase("01")){ // "01" TERIMA
				tawaran.setJenisPenolakan(null);
				tawaran.setSebabPenolakan(null);
			}
			else {// "02" BATAL
				
				if (getParam("idJenisPenolakan").equalsIgnoreCase("1")) { // PENOLAKAN BERSYARAT
					tawaran.setJenisPenolakan((JenisPenolakan) mp.find(JenisPenolakan.class, getParam("idJenisPenolakan")));
					tawaran.setSebabPenolakan((SebabPenolakan) mp.find(SebabPenolakan.class, getParam("idSebabPenolakan")));
					tawaran.setFlagSemakanPelulus(1);
				} else {// // PENOLAKAN BIASA		
					tawaran.setJenisPenolakan((JenisPenolakan) mp.find(JenisPenolakan.class, getParam("idJenisPenolakan")));
					tawaran.setSebabPenolakan(null);
					//tawaran.setFlagSemakanPelulus(1);
				}
				kk.setFlagAgihan(0);
			}
			tawaran.setFlagSelesai(1);
			mp.begin();
			mp.commit();
		} catch (Exception e) {
			System.out.println("Error simpanPermohonan : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}

		if(tawaran.getStatusTawaran().equalsIgnoreCase("01")){
			try {
				mp = new MyPersistence();
				tawaran = (KuaTawaran) mp.find(KuaTawaran.class, getParam("idTawaran"));
				if (tawaran.getAgihan() != null)
					ka = tawaran.getAgihan();
				if (ka.getKuarters() != null)
					kk = ka.getKuarters();
				KuaPermohonan kp = (KuaPermohonan) mp.find(KuaPermohonan.class,tawaran.getAgihan().getPermohonan().getId());	
				if(kp != null){
					List<KuaAgihan> listAgihan = mp.list("SELECT u FROM KuaAgihan u WHERE u.permohonan.id = '"+kp.getId()+"'");
						if(listAgihan.size()>1)
						{
							for (int f = 0; f < listAgihan.size(); f++){
								KuaAgihan a = (KuaAgihan)mp.find(KuaAgihan.class, listAgihan.get(f).getId());
								kelasTawaran=a.getKelasKuarters();
								UsersJob pekerjaan = (UsersJob) mp.get("SELECT uj FROM UsersJob uj WHERE uj.users.id = '"+ kp.getPemohon().getId() + "'");
								if (pekerjaan != null) {
									String gredKelasKuarters = "";
									if (pekerjaan.getGredJawatan() != null) {
										gredKelasKuarters = pekerjaan.getGredJawatan().getKelasKuarters();
									}
									kelasDowngrade = dataUtil.getKelasDowngrade(gredKelasKuarters);
								}
								if(kelasTawaran.equalsIgnoreCase(kelasDowngrade)){
									mp.remove(a);
								}
							}	
						}
				}	
				KuaAkaun deposit = (KuaAkaun) mp.get("SELECT d FROM KuaAkaun d WHERE d.kodHasil.id = '72310' AND d.pembayar.id = '"+ kp.getPemohon().getId() + "'");
				idStatusAgihan = "1435817077476"; // tawaran
				idStatus = "1435817077476";// tawaran (baru tambah)
				if (getParam("idJenisPenolakan").equalsIgnoreCase("1")) {// penolakan bersyarat
					idStatusAgihan = "1431405647231"; // penolakan bersyarat
					idStatus = "1431327994521";// tolak tawaran
				}			
				if (getParam("idJenisPenolakan").equalsIgnoreCase("2")|| getParam("idJenisPenolakan").equalsIgnoreCase("3")) {
					idStatusAgihan = "1431327994521"; // tolak tawaran
					idStatus = "1431327994521";// tolak tawaran
				}
				if (deposit != null){
					kuaAkaun = (KuaAkaun) mp.find(KuaAkaun.class, deposit.getId());
				}
				
				if (kuaAkaun == null) {
					kuaAkaun = new KuaAkaun();
					newDeposit = true;
				}
				kuaAkaun.setPermohonan(kp);
				kuaAkaun.setKodHasil((KodHasil) mp.find(KodHasil.class, "72310"));
				kuaAkaun.setNoInvois(getLongTransactionNo());
				kuaAkaun.setTarikhInvois(new Date());
				
				/**
				 *  14/11/2019 Edited by @author Mohd Faizal, kemaskini maklumat alamat kuarters
				 */
				//set alamat/kuarters
				if (kk.getNoUnit() != null && kk.getNoUnit().trim().length() > 0) {
					alamatKuarters = kk.getNoUnit().trim();
				}
				if (kk.getAlamat1() != null && kk.getAlamat1().trim().length() > 0) {
					alamatKuarters = alamatKuarters + ", " + kk.getAlamat1().trim();
				}
				if (kk.getAlamat2() != null && kk.getAlamat2().trim().length() > 0) {
					alamatKuarters = alamatKuarters + ", " + kk.getAlamat2().trim();
				}
				if (kk.getAlamat3() != null && kk.getAlamat3().trim().length() > 0) {
					alamatKuarters = alamatKuarters + ", " + kk.getAlamat3().trim();
				}
				if (kk.getFasa() != null && kk.getFasa().getKeterangan().trim().length() > 0) {
					alamatKuarters = alamatKuarters + kk.getFasa().getKeterangan().trim() + " ";
				}
				if (kk.getKawasan() != null && kk.getKawasan().trim().length() > 0) {
					alamatKuarters = alamatKuarters + ", " + kk.getKawasan().trim();
				}
				if (kk.getPoskod() != null && kk.getPoskod().trim().length() > 0) {
					alamatKuarters = alamatKuarters + ", " + kk.getPoskod().trim();
					if (kk.getBandar() != null && kk.getBandar().getKeterangan().trim().length() > 0) {
						alamatKuarters = alamatKuarters + " " + kk.getBandar().getKeterangan().trim();
					}
				} else {
					alamatKuarters = alamatKuarters + ", " + kk.getBandar().getKeterangan().trim();
				}		
				if (kk.getBandar() != null && kk.getBandar().getNegeri() != null && kk.getBandar().getNegeri().getKeterangan().trim().length() > 0) {
					alamatKuarters = alamatKuarters + ", " + kk.getBandar().getNegeri().getKeterangan().trim();
				}
				if (kk.getKelas() != null && kk.getKelas().getId() != null) {
					alamatKuarters = alamatKuarters + " (KELAS " + kk.getKelas().getId() + ")";
				}				
				kuaAkaun.setKeterangan("DEPOSIT KUARTERS ( " + alamatKuarters + " )");
				
				if (tawaran.getStatusTawaran().equalsIgnoreCase("02")) {
					kk.setFlagAgihan(0);// tolak
				}
				kuaAkaun.setAmaunBayaranSeunit(kk.getDeposit());
				kuaAkaun.setDebit(kk.getDeposit());
				kuaAkaun.setBilanganUnit(1);
				kuaAkaun.setFlagBayar("T");
				kuaAkaun.setIdMasuk((Users) mp.find(Users.class, tawaran.getAgihan().getPemohon().getId()));
				kuaAkaun.setPembayar((Users) mp.find(Users.class, tawaran.getAgihan().getPemohon().getId()));
				if (kuaAkaun.getTarikhMasuk() == null){
					kuaAkaun.setTarikhMasuk(new Date());
				}
				ka.setStatus((Status) mp.find(Status.class, idStatusAgihan));
//				if (idStatus.equalsIgnoreCase("1431327994521")) 
//				{
//					ka.setKuarters(null);
//				}
				kp.setStatus((Status) mp.find(Status.class, idStatus));
				if (newDeposit == true)
					mp.persist(kuaAkaun);
				mp.begin();
				mp.commit();
				createDepositInFinance(kuaAkaun);
				context.put("simpanPermohonan", "success");
			} catch (Exception e) {
				System.out.println("Error simpanPermohonan : " + e.getMessage());
			} finally {
				if (mp != null) { mp.close(); }
			}
		}else{
			try {
				mp = new MyPersistence();
				KuaPermohonan kp = (KuaPermohonan) mp.find(KuaPermohonan.class,tawaran.getAgihan().getPermohonan().getId());
				idStatus="1431327994521";// tolak tawaran
				kp.setStatus((Status) mp.find(Status.class, idStatus));
				mp.begin();
				mp.commit();
			} catch (Exception e) {
				System.out.println("Error simpanPermohonan : " + e.getMessage());
			} finally {
				if (mp != null) { mp.close(); }
			}
		}
		
		try {
			mp = new MyPersistence();
			tawaran = (KuaTawaran) mp.find(KuaTawaran.class, getParam("idTawaran"));
			context.put("r", tawaran);
		} catch (Exception e) {
			System.out.println("Error simpanPermohonan : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}	
		return getPath() + "/entry_page.vm";
	}

//	public void createInvoisInFinance(KuaAkaun ak) {
//		try {
//			KewInvois inv = new KewInvois();
//			inv.setFlagBayar("T");
//			inv.setDebit(ak.getDebit());
//			inv.setFlagBayaran("DEPOSIT");
//			inv.setFlagQueue("T");
//			inv.setIdLejar(ak.getId());
//			inv.setJenisBayaran((KewJenisBayaran) mp.find(KewJenisBayaran.class, "01")); 
//			inv.setKeteranganBayaran(ak.getKeterangan().toUpperCase());
//			inv.setKodHasil(ak.getKodHasil());
//			inv.setNoInvois(ak.getNoInvois());
//			inv.setNoRujukan(ak.getPermohonan().getId());
//			inv.setPembayar(ak.getPermohonan().getPemohon());
//			inv.setTarikhInvois(ak.getTarikhInvois());
//			inv.setUserPendaftar(ak.getPembayar());
//			inv.setTarikhDaftar(new Date());
//			inv.setUserKemaskini(ak.getPembayar());
//			inv.setTarikhKemaskini(new Date());
//			inv.setTarikhDari(ak.getPermohonan().getTarikhPermohonan());
//			inv.setTarikhHingga(ak.getPermohonan().getTarikhKemaskini());
//			mp.begin();
//			mp.persist(inv);
//			mp.commit();
//			KuaAkaun akaun = ak;
//			createDepositInFinance(akaun);
//		} catch (Exception e) {
//			System.out.println("Error createInvoisInFinance : " + e.getMessage());
//		}
//	}

	public static void createDepositInFinance(KuaAkaun ak) throws Exception {

		String id = UID.getUID();
		// String kodHasil = "72311"; // DEPOSIT
		String kodHasil = "72310"; // DEPOSIT
		String lejarId = ak.getId();
		String pemohonId = ak.getPermohonan().getPemohon().getId();
		String keterangan = ak.getKeterangan().toUpperCase();
		Double amaunDeposit = ak.getDebit() != null ? ak.getDebit() : 0d;
		String noInvois = ak.getNoInvois();

		String sql = " INSERT INTO `kew_deposit` (`id`, `id_kod_hasil`, `id_lejar`, `id_jenis_bayaran`, `id_pendeposit`, "
				+ " `keterangan_deposit`, `tarikh_deposit`, `flag_pulang_deposit`, "
				+ " `jumlah_deposit`, `baki_deposit`," + " `flag_warta`, `flag_bayar`, `flag_queue`, `no_invois`) "
				+ " VALUES " + " ('" + id + "', '" + kodHasil + "', '" + lejarId + "', '01', '" + pemohonId + "', "
				+ " '" + keterangan + "', now(), 'T', " + amaunDeposit + ", 0.00, 'T', 'T', 'T', '" + noInvois + "') ";

		Db database = new Db();
		try {
			Statement stmt = database.getStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException ex) {
			System.out.println(":: ERROR createDepositInFinance : " + ex.getMessage());
		} finally {
			if (database != null)
				database.close();
		}
	}

	@Command("batalPermohonan")
	public String batalPermohonan() {
		KuaKuarters kuarters = null;
		try {
			mp = new MyPersistence();
			KuaTawaran tawaran = (KuaTawaran) mp.find(KuaTawaran.class, getParam("idTawaran"));
			context.put("idTawaran", tawaran.getId());
			KuaAgihan agihan = (KuaAgihan) mp.find(KuaAgihan.class, tawaran.getAgihan().getId());
			if (agihan.getKuarters() != null)
				kuarters = (KuaKuarters) mp.find(KuaKuarters.class, agihan.getKuarters().getId());
			KuaPermohonan permohonan = (KuaPermohonan) mp.find(KuaPermohonan.class, agihan.getPermohonan().getId());
			if (agihan.getKuarters() != null)
				kuarters.setFlagAgihan(0);
			agihan.setStatus((Status) mp.find(Status.class, "1431327994524"));
//			agihan.setKuarters(null);
			permohonan.setStatus((Status) mp.find(Status.class, "1431327994524"));
			mp.begin();
			mp.remove(tawaran);
			mp.commit();
		} catch (Exception e) {
			System.out.println("Error batalPermohonan : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/entry_page.vm";
	}
	
	public String getPelulus() throws SQLException {
		Db db = null;
		String sql = "";
		String ptName = "";
		try {
			db = new Db();
			sql = "SELECT user_id, role_id FROM user_role WHERE role_id = '(QTR) Pelulus'";
			ResultSet rs = db.getStatement().executeQuery(sql);
			while (rs.next()) {
				if (ptName == "") {
					ptName = "'" + rs.getString("user_id") + "'";
				} else {
					ptName = ptName + ", '" + rs.getString("user_id") + "'";
				}
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
		return ptName;
	}
	
	@Command("simpanPenolakanBersyarat")
	public String simpanPenolakanBersyarat() {
		KuaTawaran tawaran = null;
		KuaKuarters kuarters = null;
		KuaAgihan agihan=null;
		try {
			mp = new MyPersistence();
			tawaran = (KuaTawaran) mp.find(KuaTawaran.class, getParam("idTawaran"));
			agihan = (KuaAgihan) mp.find(KuaAgihan.class, tawaran.getAgihan().getId());
			kuarters = (KuaKuarters) mp.find(KuaKuarters.class, agihan.getKuarters().getId());
			tawaran.setTarikhSuratDiterima(getDate("tarikhSuratDiterima"));
			tawaran.setBil(getParam("bil"));
			tawaran.setTarikhSuratSebenar(getDate("tarikhSurat"));
			tawaran.setCatatan(getParam("catatan"));
			tawaran.setTarikhKemaskini(new Date());
			tawaran.setStatusTawaran(getParam("statusTawaran"));
			tawaran.setFlagSemakanPelulus(1);
			if (getParam("statusTawaran").equalsIgnoreCase("01"))
			{
				tawaran.setJenisPenolakan(null);
				tawaran.setSebabPenolakan(null);
			}
			else 
			{
				/*PENOLAKAN BERSYARAT*/
				if (getParam("idJenisPenolakan").equalsIgnoreCase("1")) {
					tawaran.setJenisPenolakan(
							(JenisPenolakan) mp.find(JenisPenolakan.class, getParam("idJenisPenolakan")));
					tawaran.setSebabPenolakan(
							(SebabPenolakan) mp.find(SebabPenolakan.class, getParam("idSebabPenolakan")));
				} else {
					/*PENOLAKAN TANPA SYARAT / PENOLAKAN DOWNGRADE*/
					tawaran.setJenisPenolakan(
							(JenisPenolakan) mp.find(JenisPenolakan.class, getParam("idJenisPenolakan")));
					tawaran.setSebabPenolakan(null);
				}
				kuarters.setFlagAgihan(0);
			}
			mp.begin();
			mp.commit();
		} catch (Exception e) {
			System.out.println("Error simpanPenolakanBersyarat : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return getPath() + "/entry_page.vm";
	}
	
	@Command("pengesahanTerimaTawaran")
	public String pengesahanTerimaTawaran() {
		KuaTawaran tawaran = null;
		KuaAgihan agihan=null;
		KuaKuarters kuarters=null;
		try {
			mp = new MyPersistence();
			tawaran = (KuaTawaran) mp.find(KuaTawaran.class, getParam("idTawaran"));
			tawaran.setFlagSemakanPelulus(2);
			agihan=(KuaAgihan) mp.find(KuaAgihan.class, tawaran.getAgihan().getId());
			kuarters = (KuaKuarters) mp.find(KuaKuarters.class, agihan.getKuarters().getId());
			agihan.setFlagMenungguBersyarat(1);
			agihan.setStatus((Status) mp.find(Status.class, "1431405647231"));
			kuarters.setFlagAgihan(0);
			//agihan.setKuarters(null);
			mp.begin();
			mp.commit();
		} catch (Exception e) {
			System.out.println("Error pengesahanTerimaTawaran : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/entry_page.vm";
	}
	
	@Command("pengesahanBatalTawaran")
	public String pengesahanBatalTawaran() {
		KuaTawaran tawaran = null;
		KuaAgihan agihan=null;
		KuaKuarters kuarters=null;
		try {
			mp = new MyPersistence();
			mp.begin();
			tawaran = (KuaTawaran) mp.find(KuaTawaran.class, getParam("idTawaran"));
			tawaran.setFlagSemakanPelulus(2);
			agihan=(KuaAgihan) mp.find(KuaAgihan.class, tawaran.getAgihan().getId());
			kuarters = (KuaKuarters) mp.find(KuaKuarters.class, agihan.getKuarters().getId());
			//agihan.setFlagMenungguBersyarat(1);
			agihan.setStatus((Status) mp.find(Status.class, "1431405647231"));
			kuarters.setFlagAgihan(0);
			//agihan.setKuarters(null);			
			mp.commit();
		} catch (Exception e) {
			System.out.println("Error pengesahanTerimaTawaran : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/entry_page.vm";
	}
	
	@Command("selectJabatan")
	public String selectJabatan() throws Exception {
		dataUtil = DataUtil.getInstance(db);
		String idKementerian = "0";

		if (get("idKementerian").trim().length() > 0) {
			idKementerian = get("idKementerian");
		}

		context.put("selectJabatan", dataUtil.getListAgensi(idKementerian));
		return getPath() + "/select/selectJabatan.vm";
	}
	
	@Command("selectBandar")
	public String selectBandar() throws Exception {
		dataUtil = DataUtil.getInstance(db);
		try {
			mp = new MyPersistence();
			String idNegeri = "0";
			if (getParam("idNegeri").trim().length() > 0)
				idNegeri = getParam("idNegeri");
			context.put("selectBandar", dataUtil.getListBandar(idNegeri));
		} catch (Exception e) {
			System.out.println("Error selectBandar : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/select/selectBandar.vm";
	}
}
