package bph.modules.qtr;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import portal.module.entity.Users;
import portal.module.entity.UsersJob;
import portal.module.entity.UsersSpouse;
import bph.entities.kod.Agensi;
import bph.entities.kod.Bandar;
import bph.entities.kod.Bangsa;
import bph.entities.kod.Gelaran;
import bph.entities.kod.GredPerkhidmatan;
import bph.entities.kod.Jantina;
import bph.entities.kod.Jawatan;
import bph.entities.kod.JenisPengenalan;
import bph.entities.kod.JenisPerkhidmatan;
import bph.entities.kod.KelasPerkhidmatan;
import bph.entities.kod.StatusPekerjaan;
import bph.entities.kod.StatusPerkahwinan;
import bph.entities.kod.StatusPerkhidmatan;
import bph.entities.qtr.KuaPenghuni;
import bph.entities.qtr.KuaPermohonan;
import bph.entities.qtr.KuaPinjamanPemohon;
import bph.utils.DataUtil;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class FrmKuaPenghuniRecord extends
		LebahRecordTemplateModule<KuaPenghuni> {

	private static final long serialVersionUID = 8652059520249054563L;
	private DataUtil dataUtil;
	private MyPersistence mp;


	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		return String.class;
	}

	@Override
	public Class<KuaPenghuni> getPersistenceClass() {
		return KuaPenghuni.class;
	}

	@Override
	public String getPath() {
		return "bph/modules/qtr/penghuni";
	}

	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);
		setDisableKosongkanUpperButton(true);
		setDisableAddNewRecordButton(true);
		setHideDeleteButton(true);
		this.addFilter("tarikhKeluarKuarters IS NULL");
		context.put("selectGelaran", dataUtil.getListGelaran());
		context.put("selectJenisPengenalan", dataUtil.getListJenisPengenalan());
		context.put("selectAgama", dataUtil.getListAgama());
		context.put("selectJantina", dataUtil.getListJantina());
		context.put("selectBangsa", dataUtil.getListBangsa());
		context.put("selectEtnik", dataUtil.getListEtnik());
		context.put("selectStatusPerkahwinan", dataUtil
				.getListStatusPerkahwinan());
		context.put("selectNegeri", dataUtil.getListNegeri());
		context.put("selectNegeriSemasa", dataUtil.getListNegeri());
		context.put("findLokasiKuarters", dataUtil.getListLokasiKuarters());
		context.put("findJantina", dataUtil.getListJantina());
		context.put("findBangsa", dataUtil.getListBangsa());
		context.put("findStatusPerkahwinan", dataUtil
				.getListStatusPerkahwinan());
		context.put("path", getPath());
	}

	@Override
	public void save(KuaPenghuni r) throws Exception {
		r.setTarikhMulaPotongGaji(getDate("tarikhMulaPotongGaji"));
	}

	@Override
	public boolean delete(KuaPenghuni r) throws Exception {
		return false;
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		HashMap<String, Object> r = new HashMap<String, Object>();
		r.put("noFailLama", getParam("findNoFailLama"));
		r.put("pemohon.userName", getParam("findNamaPemohon"));
		r.put("permohonan.noPermohonan", get("findNoPermohonan"));
		r.put("pemohon.id", get("findNoKP"));
		return r;
	}

	@Override
	public void beforeSave() {

	}

	@Override
	public void afterSave(KuaPenghuni r) {

	}

	@Override
	public void getRelatedData(KuaPenghuni r) {
		context.put("now", new Date());
		try {
			mp = new MyPersistence();
			KuaPermohonan kp = (KuaPermohonan) mp
					.get("SELECT kp FROM KuaPermohonan kp WHERE kp.pemohon.id = '"
							+ r.getPemohon().getId()
							+ "' and kp.status.id in('1419601227601','1423101446117','1419601227595')");
			context.put("noDaftar", kp.getNoPermohonan());
			String tarikhPermohonan = Util.getDateTime(
					kp.getTarikhPermohonan(), "dd-MM-yyyy");
			context.put("tarikhPermohonan", tarikhPermohonan);
		} catch (Exception e) {
			System.out.println("Error getRelatedData : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
	}

	@Command("findJalan")
	public String findJalan() throws Exception {
		String idLokasi = "0";
		if (get("findLokasiKuarters").trim().length() > 0)
			idLokasi = get("findLokasiKuarters");
		context.put("findJalan", dataUtil.getListJalanKuarters(idLokasi));
		return getPath() + "/find/findJalan.vm";
	}

	@Command("selectBandar")
	public String selectBandar() throws Exception {
		String idNegeri = "0";
		if (get("idNegeriUser").trim().length() > 0)
			idNegeri = get("idNegeriUser");
		context.put("selectBandar", dataUtil.getListBandar(idNegeri));
		return getPath() + "/entry_sub/penghuni/select/selectBandar.vm";
	}

	@Command("selectBandarSemasa")
	public String selectBandarSemasa() throws Exception {
		String idNegeriSemasa = "0";
		if (get("idNegeriSemasa").trim().length() > 0)
			idNegeriSemasa = get("idNegeriSemasa");
		context.put("selectBandarSemasa", dataUtil
				.getListBandar(idNegeriSemasa));
		return getPath() + "/entry_sub/penghuni/select/selectBandarSemasa.vm";
	}

	@Command("selectBandarPekerjaan")
	public String selectBandarPekerjaan() throws Exception {
		String idNegeri = "0";
		if (get("idNegeriPekerjaan").trim().length() > 0)
			idNegeri = get("idNegeriPekerjaan");
		context.put("selectBandarPekerjaan", dataUtil.getListBandar(idNegeri));
		return getPath()
				+ "/entry_sub/penghuni/pekerjaan/selectBandarPekerjaan.vm";
	}

	@Command("selectBandarPasangan")
	public String selectBandarPasangan() throws Exception {
		String idNegeri = "0";
		if (get("idNegeriPasangan").trim().length() > 0)
			idNegeri = get("idNegeriPasangan");
		context.put("selectBandarPasangan", dataUtil.getListBandar(idNegeri));
		return getPath()
				+ "/entry_sub/penghuni/pasangan/selectBandarPasangan.vm";
	}

	@Command("selectBandarPinjaman")
	public String selectBandarPinjaman() throws Exception {
		String idNegeri = "0";
		if (get("idNegeriPinjaman").trim().length() > 0)
			idNegeri = get("idNegeriPinjaman");
		context.put("selectBandarPinjaman", dataUtil.getListBandar(idNegeri));
		return getPath()
				+ "/entry_sub/penghuni/pinjaman/selectBandarPinjaman.vm";
	}

	@Command("selectJabatan")
	public String selectJabatan() throws Exception {
		String idKementerian = "0";
		if (get("idKementerian").trim().length() > 0)
			idKementerian = get("idKementerian");
		context.put("selectJabatan", dataUtil.getListAgensi(idKementerian));
		return getPath() + "/entry_sub/penghuni/pekerjaan/selectJabatan.vm";
	}

	@Command("getPekerjaan")
	public String getPekerjaan() throws Exception {
		try {
			mp = new MyPersistence();
			context.put("selectKelasPerkhidmatan", dataUtil
					.getListKelasPerkhidmatan());
			context
					.put("selectGredJawatan", dataUtil
							.getListGredPerkhidmatan());
			context.put("selectJenisPerkhidmatan", dataUtil
					.getListJenisPerkhidmatan());
			context.put("selectStatusPerkhidmatan", dataUtil
					.getListStatusPerkhidmatan());
			context.put("selectKementerian", dataUtil.getListKementerian());
			context.put("selectJawatan", dataUtil.getListJawatan());
			context.put("selectNegeriPekerjaan", dataUtil.getListNegeri());
			context.put("activity", "pekerjaan");
			context.remove("uj");
			UsersJob usersJob = (UsersJob) mp
					.get("SELECT uj FROM UsersJob uj WHERE uj.users.id = '"
							+ getParam("idUsers") + "'");
			if (usersJob != null)
				context.put("uj", mp.find(UsersJob.class, usersJob.getId()));
		} catch (Exception e) {
			System.out.println("Error getPekerjaan : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/entry_sub/penghuni/pekerjaan.vm";
	}

	@Command("getPasangan")
	public String getPasangan() throws Exception {
		try {
			mp = new MyPersistence();
			context.put("selectJenisPengenalanPasangan", dataUtil
					.getListJenisPengenalan());
			context.put("selectStatusPekerjaanPasangan", dataUtil
					.getListStatusPekerjaan());
			context.put("selectNegeriPasangan", dataUtil.getListNegeri());
			context.put("activity", "pasangan");
			context.remove("us");
			UsersSpouse usersSpouse = (UsersSpouse) mp
					.get("SELECT us FROM UsersSpouse us WHERE us.users.id = '"
							+ getParam("idUsers") + "'");

			if (usersSpouse != null)
				context.put("us", mp.find(UsersSpouse.class, usersSpouse
						.getId()));
		} catch (Exception e) {
			System.out.println("Error getPasangan : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/entry_sub/penghuni/pasangan.vm";
	}

	@Command("getPinjaman")
	public String getPinjaman() throws Exception {
		try {
			mp = new MyPersistence();
			context.put("selectNegeriPinjaman", dataUtil.getListNegeri());
			context.put("activity", "pinjaman");
			context.remove("kpp");
			KuaPinjamanPemohon kpp = (KuaPinjamanPemohon) mp
					.get("SELECT kpp FROM KuaPinjamanPemohon kpp WHERE kpp.users.id = '"
							+ getParam("idUsers") + "'");
			if (kpp != null)
				context.put("kpp", mp.find(KuaPinjamanPemohon.class, kpp
						.getId()));
		} catch (Exception e) {
			System.out.println("Error getPinjaman : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/entry_sub/penghuni/pinjaman.vm";
	}

	@Command("tarikhJangkaSiap")
	public String tarikhJangkaSiap() {
		return getPath() + "/entry_sub/penghuni/pinjaman/tarikhJangkaSiap.vm";
	}

	@Command("simpanPeribadi")
	public String simpanPeribadi() {
		boolean success = false;
		try {
			mp = new MyPersistence();
			Gelaran gelaran = (Gelaran) mp.find(Gelaran.class,
					getParam("idGelaran"));
			JenisPengenalan jenisPengenalan = (JenisPengenalan) mp.find(
					JenisPengenalan.class, getParam("idJenisPengenalan"));
			Jantina jantina = (Jantina) mp.find(Jantina.class,
					getParam("idJantina"));
			Bangsa bangsa = (Bangsa) mp
					.find(Bangsa.class, getParam("idBangsa"));
			StatusPerkahwinan statusPerkahwinan = (StatusPerkahwinan) mp.find(
					StatusPerkahwinan.class, getParam("idStatusPerkahwinan"));
			Bandar bandar = (Bandar) mp.find(Bandar.class,
					getParam("idBandarUser"));
			Bandar bandarSemasa = (Bandar) mp.find(Bandar.class,
					getParam("idBandarSemasa"));
			Users users = (Users) mp.find(Users.class, getParam("idUsers"));
			users.setGelaran(gelaran);
			users.setUserName(getParam("userName"));
			users.setJenisPengenalan(jenisPengenalan);
			users.setNoKP(getParam("userNoKP"));
			users.setNoKP2(getParam("userNoKP2"));
			users.setTarikhLahir(getDate("userTarikhLahir"));
			users.setJantina(jantina);
			users.setBangsa(bangsa);
			users.setStatusPerkahwinan(statusPerkahwinan);
			users.setBilAnak(getParamAsInteger("userBilAnak"));
			users.setNoTelefon(getParam("userNoTel"));
			users.setNoTelefonBimbit(getParam("userNoTelBimbit"));
			users.setAlamat1(getParam("userAlamat1"));
			users.setAlamat2(getParam("userAlamat2"));
			users.setAlamat3(getParam("userAlamat3"));
			users.setPoskod(getParam("userPoskod"));
			users.setBandar(bandar);
			users.setUserAddress(getParam("userAlamatSemasa1"));
			users.setUserAddress2(getParam("userAlamatSemasa2"));
			users.setUserAddress3(getParam("userAlamatSemasa3"));
			users.setUserPostcode(getParam("userPoskodSemasa"));
			users.setUserBandar(bandarSemasa);

			mp.begin();
			mp.commit();
			success = true;
		} catch (Exception e) {
			System.out.println("Error simpanPeribadi : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}

		context.put("success", success);
		return getPath() + "/entry_sub/penghuni/result/simpanPenghuni.vm";
	}

	@Command("simpanPekerjaan")
	public String simpanPekerjaan() {
		boolean success = false;
		boolean newRecord = false;

		try {
			mp = new MyPersistence();
			Users users = (Users) mp.find(Users.class, getParam("idUsers"));
			KelasPerkhidmatan kelasPerkhidmatan = (KelasPerkhidmatan) mp.find(
					KelasPerkhidmatan.class, getParam("idKelasPerkhidmatan"));
			GredPerkhidmatan gredJawatan = (GredPerkhidmatan) mp.find(
					GredPerkhidmatan.class, getParam("idGredJawatan"));
			Jawatan jawatan = (Jawatan) mp.find(Jawatan.class,
					getParam("idJawatan"));
			JenisPerkhidmatan jenisPerkhidmatan = (JenisPerkhidmatan) mp.find(
					JenisPerkhidmatan.class, getParam("idJenisPerkhidmatan"));
			StatusPerkhidmatan statusPerkhidmatan = (StatusPerkhidmatan) mp
					.find(StatusPerkhidmatan.class,
							getParam("idStatusPerkhidmatan"));
			Agensi agensi = (Agensi) mp.find(Agensi.class,
					getParam("idJabatan"));
			Bandar bandarPekerjaan = (Bandar) mp.find(Bandar.class,
					getParam("idBandarPekerjaan"));
			UsersJob uj = null;
			UsersJob usersJob = (UsersJob) mp
					.get("SELECT uj FROM UsersJob uj WHERE uj.users.id = '"
							+ getParam("idUsers") + "'");
			if (usersJob != null)
				uj = (UsersJob) mp.find(UsersJob.class, usersJob.getId());
			if (uj == null) {
				newRecord = true;
				uj = new UsersJob();
			}
			uj.setUsers(users);
			uj.setKelasPerkhidmatan(kelasPerkhidmatan);
			uj.setGredJawatan(gredJawatan);
			uj.setJawatan(jawatan);
			uj.setTarikhLantikan(getDate("tarikhLantikan"));
			uj.setNoGaji(getParam("noGaji"));
			uj.setJenisPerkhidmatan(jenisPerkhidmatan);
			uj.setStatusPerkhidmatan(statusPerkhidmatan);
			uj.setTarikhTamat(getDate("tarikhTamat"));
			uj.setFlagITP(getParamAsInteger("valueFlagITP"));
			uj.setFlagEPW(getParamAsInteger("valueFlagEPW"));
			uj.setAgensi(agensi);
			uj.setBahagian(getParam("bahagian"));
			uj.setNoTelPejabat(getParam("noTelPejabat"));
			uj.setNoFaks(getParam("noFaks"));
			uj.setEmel(getParam("email"));
			uj.setBandar(bandarPekerjaan);
			mp.begin();
			if (newRecord == true)
				mp.persist(uj);
			mp.commit();
			success = true;
		} catch (Exception e) {
			System.out.println("Error simpanPekerjaan : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}

		context.put("success", success);
		return getPath() + "/entry_sub/penghuni/result/simpanPenghuni.vm";
	}

	@Command("simpanPasangan")
	public String simpanPasangan() {
		boolean success = false;
		boolean newRecord = false;
		try {
			mp = new MyPersistence();
			Users users = (Users) mp.find(Users.class, getParam("idUsers"));
			JenisPengenalan jenisPengenalan = (JenisPengenalan) mp.find(
					JenisPengenalan.class,
					getParam("idJenisPengenalanPasangan"));
			StatusPekerjaan statusPekerjaan = (StatusPekerjaan) mp.find(
					StatusPekerjaan.class,
					getParam("idStatusPekerjaanPasangan"));
			Bandar bandar = (Bandar) mp.find(Bandar.class,
					getParam("idBandarPasangan"));
			UsersSpouse us = null;
			UsersSpouse usersSpouse = (UsersSpouse) mp
					.get("SELECT us FROM UsersSpouse us WHERE us.users.id = '"
							+ getParam("idUsers") + "'");
			if (usersSpouse != null)
				us = (UsersSpouse) mp.find(UsersSpouse.class, usersSpouse
						.getId());
			if (us == null) {
				newRecord = true;
				us = new UsersSpouse();
			}
			us.setUsers(users);
			us.setNamaPasangan(getParam("namaPasangan"));
			us.setJenisPengenalan(jenisPengenalan);
			us.setNoKPPasangan(getParam("pasanganNoKP"));
			us.setStatusPekerjaanPasangan(statusPekerjaan);
			us.setJenisPekerjaan(getParam("pasanganJenisPekerjaan"));
			us.setGajiPasangan(Double.parseDouble((!getParam("pasanganGaji")
					.isEmpty() ? getParam("pasanganGaji") : "0.00").replaceAll(
					",", "")));
			us.setNamaSyarikat(getParam("pasanganSyarikat"));
			us.setAlamatPejabat1(getParam("pasanganAlamatKerja1"));
			us.setAlamatPejabat2(getParam("pasanganAlamatKerja2"));
			us.setAlamatPejabat3(getParam("pasanganAlamatKerja3"));
			us.setPoskodPejabat(getParam("pasanganPoskodKerja"));
			us.setBandarPejabat(bandar);
			us.setNoTelPejabat(getParam("pasanganNoTelKerja"));
			us.setNoFaksPejabat(getParam("pasanganNoFaksKerja"));
			us.setNoTelBimbit(getParam("pasanganNoTelBimbit"));
			mp.begin();
			if (newRecord == true)
				mp.persist(us);
			mp.commit();
			success = true;
		} catch (Exception e) {
			System.out.println("Error simpanPasangan : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}

		context.put("success", success);
		return getPath() + "/entry_sub/penghuni/result/simpanPenghuni.vm";
	}

	@Command("simpanPinjaman")
	public String simpanPinjaman() {
		boolean success = false;
		boolean newRecord = false;
		try {
			mp = new MyPersistence();
			Users users = (Users) mp.find(Users.class, getParam("idUsers"));
			Bandar bandar = (Bandar) mp.find(Bandar.class,
					getParam("idBandarPinjaman"));
			KuaPinjamanPemohon kpp = null;
			KuaPinjamanPemohon pinjamanPemohon = (KuaPinjamanPemohon) mp
					.get("SELECT p FROM KuaPinjamanPemohon p WHERE p.users.id = '"
							+ getParam("idUsers") + "'");

			if (pinjamanPemohon != null)
				kpp = (KuaPinjamanPemohon) mp.find(KuaPinjamanPemohon.class,
						pinjamanPemohon.getId());

			if (kpp == null) {
				newRecord = true;
				kpp = new KuaPinjamanPemohon();
			}

			kpp.setUsers(users);
			kpp
					.setPinjamanPerumahan(getParamAsInteger("valuePinjamanPerumahan"));
			kpp.setStatusPembinaan(getParamAsInteger("valueStatusRumah"));
			kpp.setPoskodPinjaman(getParam("poskodPinjaman"));
			kpp.setBandar(bandar);
			kpp.setJenisPerumahan(getParam("jenisPerumahan"));
			kpp.setPembiayaan(getParam("valuePembiayaan"));
			kpp.setPembelian(getParam("valuePembelian"));

			mp.begin();
			if (newRecord == true)
				mp.persist(kpp);
			mp.commit();
			success = true;
		} catch (Exception e) {
			System.out.println("Error simpanPinjaman : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("success", success);
		return getPath() + "/entry_sub/penghuni/result/simpanPenghuni.vm";
	}

	public String getMonth(String month) {
		String result = "";
		if ("Jan".equals(month)) {
			result = "01";
		} else if ("Feb".equals(month)) {
			result = "02";
		} else if ("Mar".equals(month)) {
			result = "03";
		} else if ("Apr".equals(month)) {
			result = "04";
		} else if ("May".equals(month)) {
			result = "05";
		} else if ("Jun".equals(month)) {
			result = "06";
		} else if ("Jul".equals(month)) {
			result = "07";
		} else if ("Aug".equals(month)) {
			result = "08";
		} else if ("Sep".equals(month)) {
			result = "09";
		} else if ("Oct".equals(month)) {
			result = "10";
		} else if ("Nov".equals(month)) {
			result = "11";
		} else if ("Dec".equals(month)) {
			result = "12";
		}
		return result;
	}

}
