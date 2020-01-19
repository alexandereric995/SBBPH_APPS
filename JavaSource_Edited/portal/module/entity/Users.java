package portal.module.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lebah.template.DbPersistence;
import bph.entities.kod.Agama;
import bph.entities.kod.Agensi;
import bph.entities.kod.BadanBerkanun;
import bph.entities.kod.Bandar;
import bph.entities.kod.Bangsa;
import bph.entities.kod.Bank;
import bph.entities.kod.Etnik;
import bph.entities.kod.Gelaran;
import bph.entities.kod.GredPerkhidmatan;
import bph.entities.kod.Jantina;
import bph.entities.kod.Jawatan;
import bph.entities.kod.JenisPengenalan;
import bph.entities.kod.JenisPerkhidmatan;
import bph.entities.kod.KategoriPengguna;
import bph.entities.kod.KelasPerkhidmatan;
import bph.entities.kod.Seksyen;
import bph.entities.kod.StatusPerkahwinan;

@Entity
@Table(name = "users")
public class Users {

	@Id
	@Column(name = "user_login")
	private String id;

	@Column(name = "user_password")
	private String userPassword;

	@ManyToOne
	@JoinColumn(name = "gelaran")
	private Gelaran gelaran;

	@Column(name = "user_name")
	private String userName;

	@ManyToOne
	@JoinColumn(name = "user_role")
	private Role role;

	@ManyToOne
	@JoinColumn(name = "user_css")
	private CSS css;

	@Column(name = "user_login_alt")
	private String userLoginAlt;

	@Column(name = "user_address")
	private String userAddress;

	@Column(name = "user_address2")
	private String userAddress2;

	@Column(name = "user_address3")
	private String userAddress3;

	@Column(name = "user_postcode")
	private String userPostcode;

	@ManyToOne
	@JoinColumn(name = "user_bandar")
	private Bandar userBandar;

	@Column(name = "user_ip_address")
	private String userIPAddress;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_registered")
	private Date dateRegistered;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_login_date")
	private Date lastLoginDate;

	@Column(name = "profile_photo")
	private String profilePhoto;

	@Column(name = "avatar")
	private String avatar;

	@ManyToOne
	@JoinColumn(name = "jenis_pengenalan")
	private JenisPengenalan jenisPengenalan;

	@Column(name = "no_kp")
	private String noKP;

	@Column(name = "no_kp_2")
	private String noKP2;

	@Column(name = "no_telefon")
	private String noTelefon;

	@Column(name = "no_telefon_bimbit")
	private String noTelefonBimbit;

	@Column(name = "no_telefon_pejabat")
	private String noTelefonPejabat;

	@Column(name = "no_faks")
	private String noFaks;

	@Column(name = "emel")
	private String emel;

	@ManyToOne
	@JoinColumn(name = "id_seksyen")
	private Seksyen seksyen;

	@ManyToOne
	@JoinColumn(name = "id_jawatan")
	private Jawatan jawatan;

	@Column(name = "keterangan_jawatan")
	private String keteranganJawatan;

	@ManyToOne
	@JoinColumn(name = "id_kelas_perkhidmatan")
	private KelasPerkhidmatan kelasPerkhidmatan;

	@OneToOne
	@JoinColumn(name = "id_gred_perkhidmatan")
	private GredPerkhidmatan gredPerkhidmatan;

	@OneToOne
	@JoinColumn(name = "id_jenis_perkhidmatan")
	private JenisPerkhidmatan jenisPerkhidmatan;

	@ManyToOne
	@JoinColumn(name = "id_agensi")
	private Agensi agensi;

	@Column(name = "bahagian")
	private String bahagian;

	@ManyToOne
	@JoinColumn(name = "id_badan_berkanun")
	private BadanBerkanun badanBerkanun;

	@ManyToOne
	@JoinColumn(name = "jantina")
	private Jantina jantina;

	@ManyToOne
	@JoinColumn(name = "bangsa")
	private Bangsa bangsa;

	@ManyToOne
	@JoinColumn(name = "etnik")
	private Etnik etnik;

	@ManyToOne
	@JoinColumn(name = "agama")
	private Agama agama;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_lahir")
	private Date tarikhLahir;

	@ManyToOne
	@JoinColumn(name = "status_perkahwinan")
	private StatusPerkahwinan statusPerkahwinan;

	@Column(name = "status_perkahwinan", updatable = false, insertable = false)
	private String idStatusPerkahwinan;

	@Column(name = "alamat_1")
	private String alamat1;

	@Column(name = "alamat_2")
	private String alamat2;

	@Column(name = "alamat_3")
	private String alamat3;

	@Column(name = "poskod")
	private String poskod;

	@ManyToOne
	@JoinColumn(name = "id_bandar")
	private Bandar bandar;

	@Column(name = "no_akaun_bank")
	private String noAkaunBank;

	@ManyToOne
	@JoinColumn(name = "id_bank")
	private Bank bank;

	@Column(name = "nama_bank")
	private String namaBank;

	@Column(name = "cawangan_bank")
	private String cawanganBank;

	@Column(name = "alamat_bank")
	private String alamatBank;

	@Column(name = "dokumen_bank")
	private String dokumenBank;

	@Column(name = "flag_sah_maklumat_bank")
	private String flagSahMaklumatBank;

	@ManyToOne
	@JoinColumn(name = "jenis_pengguna")
	private KategoriPengguna jenisPengguna;

	@Column(name = "flag_aktif")
	private String flagAktif;

	@Column(name = "flag_anak")
	private String flagAnak;

	@Column(name = "bil_anak")
	private int bilAnak;

	@Column(name = "flag_hq")
	private String flagHq;

	@Column(name = "flag_semakan_jpn")
	private String flagSemakanJPN;

	@Column(name = "flag_semakan_hrmis")
	private String flagSemakanHRMIS;

	@Column(name = "flag_semakan_pesara")
	private String flagSemakanPESARA;

	@Column(name = "dokumen_sokongan")
	private String dokumenSokongan;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_pengesahan")
	private Date tarikhPengesahan;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_luput_pengesahan")
	private Date tarikhLuputPengesahan;

	@ManyToOne
	@JoinColumn(name = "id_pengesah")
	private Users pengesah;

	@Column(name = "catatan_pengesahan")
	private String catatanPengesahan;

	@Column(name = "nota_pengesahan")
	private String notaPengesahan;

	@Column(name = "flag_menunggu_pengesahan")
	private String flagMenungguPengesahan;

	@Column(name = "flag_urusan_pemohon")
	private int flagUrusanPemohon;

	@Column(name = "flag_daftar_sbbph")
	private String flagDaftarSBBPH;

	@Column(name = "rpp_catatan_pengguna")
	private String rppCatatanPengguna;

	@Column(name = "flag_daftar_manual")
	private String flagDaftarManual;

	@Column(name = "flag_open_gelanggang")
	private String flagGelanggang;

	@ManyToOne
	@JoinColumn(name = "id_masuk")
	private Users idMasuk;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_masuk")
	private Date tarikhMasuk;

	@ManyToOne
	@JoinColumn(name = "id_kemaskini")
	private Users idKemaskini;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_kemaskini")
	private Date tarikhKemaskini;

	public Users() {
		setDateRegistered(new Date());
	}

	public String getFlagUrusanPemohonDesc() {
		String flagUrusanPemohonDesc = "";

		if (getFlagUrusanPemohon() == 1)
			flagUrusanPemohonDesc = "Urusan Kuarters";
		else if (getFlagUrusanPemohon() == 2)
			flagUrusanPemohonDesc = "Urusan Rumah Peranginan";
		else {
			flagUrusanPemohonDesc = "Urusan Kuarters dan Rumah Peranginan";
		}

		return flagUrusanPemohonDesc;
	}

	public UsersSpouse rekodSpouse() {
		DbPersistence db = new DbPersistence();
		UsersSpouse obj = new UsersSpouse();
		obj = (UsersSpouse) db
				.get("select x from UsersSpouse x where x.users.id = '"
						+ this.id + "' ");
		return obj;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserPassword() {
		return this.userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public Gelaran getGelaran() {
		return this.gelaran;
	}

	public void setGelaran(Gelaran gelaran) {
		this.gelaran = gelaran;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Role getRole() {
		return this.role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public CSS getCss() {
		return this.css;
	}

	public void setCss(CSS css) {
		this.css = css;
	}

	public String getUserLoginAlt() {
		return this.userLoginAlt;
	}

	public void setUserLoginAlt(String userLoginAlt) {
		this.userLoginAlt = userLoginAlt;
	}

	public String getUserAddress() {
		return this.userAddress;
	}

	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}

	public String getUserAddress2() {
		return this.userAddress2;
	}

	public void setUserAddress2(String userAddress2) {
		this.userAddress2 = userAddress2;
	}

	public String getUserAddress3() {
		return this.userAddress3;
	}

	public void setUserAddress3(String userAddress3) {
		this.userAddress3 = userAddress3;
	}

	public String getUserPostcode() {
		return this.userPostcode;
	}

	public void setUserPostcode(String userPostcode) {
		this.userPostcode = userPostcode;
	}

	public Bandar getUserBandar() {
		return this.userBandar;
	}

	public void setUserBandar(Bandar userBandar) {
		this.userBandar = userBandar;
	}

	public String getUserIPAddress() {
		return this.userIPAddress;
	}

	public void setUserIPAddress(String userIPAddress) {
		this.userIPAddress = userIPAddress;
	}

	public Date getDateRegistered() {
		return this.dateRegistered;
	}

	public void setDateRegistered(Date dateRegistered) {
		this.dateRegistered = dateRegistered;
	}

	public Date getLastLoginDate() {
		return this.lastLoginDate;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public String getProfilePhoto() {
		return this.profilePhoto;
	}

	public void setProfilePhoto(String profilePhoto) {
		this.profilePhoto = profilePhoto;
	}

	public String getAvatar() {
		return this.avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public JenisPengenalan getJenisPengenalan() {
		return this.jenisPengenalan;
	}

	public void setJenisPengenalan(JenisPengenalan jenisPengenalan) {
		this.jenisPengenalan = jenisPengenalan;
	}

	public String getNoKP() {
		return this.noKP;
	}

	public void setNoKP(String noKP) {
		this.noKP = noKP;
	}

	public String getNoKP2() {
		return this.noKP2;
	}

	public void setNoKP2(String noKP2) {
		this.noKP2 = noKP2;
	}

	public String getNoTelefon() {
		return this.noTelefon;
	}

	public void setNoTelefon(String noTelefon) {
		this.noTelefon = noTelefon;
	}

	public String getNoTelefonBimbit() {
		return this.noTelefonBimbit;
	}

	public void setNoTelefonBimbit(String noTelefonBimbit) {
		this.noTelefonBimbit = noTelefonBimbit;
	}

	public String getNoTelefonPejabat() {
		return this.noTelefonPejabat;
	}

	public void setNoTelefonPejabat(String noTelefonPejabat) {
		this.noTelefonPejabat = noTelefonPejabat;
	}

	public String getNoFaks() {
		return this.noFaks;
	}

	public void setNoFaks(String noFaks) {
		this.noFaks = noFaks;
	}

	public String getEmel() {
		return this.emel;
	}

	public void setEmel(String emel) {
		this.emel = emel;
	}

	public Seksyen getSeksyen() {
		return this.seksyen;
	}

	public void setSeksyen(Seksyen seksyen) {
		this.seksyen = seksyen;
	}

	public Jawatan getJawatan() {
		return this.jawatan;
	}

	public void setJawatan(Jawatan jawatan) {
		this.jawatan = jawatan;
	}

	public String getKeteranganJawatan() {
		return this.keteranganJawatan;
	}

	public void setKeteranganJawatan(String keteranganJawatan) {
		this.keteranganJawatan = keteranganJawatan;
	}

	public KelasPerkhidmatan getKelasPerkhidmatan() {
		return this.kelasPerkhidmatan;
	}

	public void setKelasPerkhidmatan(KelasPerkhidmatan kelasPerkhidmatan) {
		this.kelasPerkhidmatan = kelasPerkhidmatan;
	}

	public GredPerkhidmatan getGredPerkhidmatan() {
		return this.gredPerkhidmatan;
	}

	public void setGredPerkhidmatan(GredPerkhidmatan gredPerkhidmatan) {
		this.gredPerkhidmatan = gredPerkhidmatan;
	}

	public JenisPerkhidmatan getJenisPerkhidmatan() {
		return this.jenisPerkhidmatan;
	}

	public void setJenisPerkhidmatan(JenisPerkhidmatan jenisPerkhidmatan) {
		this.jenisPerkhidmatan = jenisPerkhidmatan;
	}

	public Agensi getAgensi() {
		return this.agensi;
	}

	public void setAgensi(Agensi agensi) {
		this.agensi = agensi;
	}

	public String getBahagian() {
		return this.bahagian;
	}

	public void setBahagian(String bahagian) {
		this.bahagian = bahagian;
	}

	public BadanBerkanun getBadanBerkanun() {
		return this.badanBerkanun;
	}

	public void setBadanBerkanun(BadanBerkanun badanBerkanun) {
		this.badanBerkanun = badanBerkanun;
	}

	public Jantina getJantina() {
		return this.jantina;
	}

	public void setJantina(Jantina jantina) {
		this.jantina = jantina;
	}

	public Bangsa getBangsa() {
		return this.bangsa;
	}

	public void setBangsa(Bangsa bangsa) {
		this.bangsa = bangsa;
	}

	public Etnik getEtnik() {
		return this.etnik;
	}

	public void setEtnik(Etnik etnik) {
		this.etnik = etnik;
	}

	public Agama getAgama() {
		return this.agama;
	}

	public void setAgama(Agama agama) {
		this.agama = agama;
	}

	public Date getTarikhLahir() {
		return this.tarikhLahir;
	}

	public void setTarikhLahir(Date tarikhLahir) {
		this.tarikhLahir = tarikhLahir;
	}

	public StatusPerkahwinan getStatusPerkahwinan() {
		return this.statusPerkahwinan;
	}

	public void setStatusPerkahwinan(StatusPerkahwinan statusPerkahwinan) {
		this.statusPerkahwinan = statusPerkahwinan;
	}

	public String getIdStatusPerkahwinan() {
		return this.idStatusPerkahwinan;
	}

	public void setIdStatusPerkahwinan(String idStatusPerkahwinan) {
		this.idStatusPerkahwinan = idStatusPerkahwinan;
	}

	public String getAlamat1() {
		return this.alamat1;
	}

	public void setAlamat1(String alamat1) {
		this.alamat1 = alamat1;
	}

	public String getAlamat2() {
		return this.alamat2;
	}

	public void setAlamat2(String alamat2) {
		this.alamat2 = alamat2;
	}

	public String getAlamat3() {
		return this.alamat3;
	}

	public void setAlamat3(String alamat3) {
		this.alamat3 = alamat3;
	}

	public String getPoskod() {
		return this.poskod;
	}

	public void setPoskod(String poskod) {
		this.poskod = poskod;
	}

	public Bandar getBandar() {
		return this.bandar;
	}

	public void setBandar(Bandar bandar) {
		this.bandar = bandar;
	}

	public String getNoAkaunBank() {
		return this.noAkaunBank;
	}

	public void setNoAkaunBank(String noAkaunBank) {
		this.noAkaunBank = noAkaunBank;
	}

	public Bank getBank() {
		return this.bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	public String getNamaBank() {
		return this.namaBank;
	}

	public void setNamaBank(String namaBank) {
		this.namaBank = namaBank;
	}

	public String getCawanganBank() {
		return this.cawanganBank;
	}

	public void setCawanganBank(String cawanganBank) {
		this.cawanganBank = cawanganBank;
	}

	public String getAlamatBank() {
		return this.alamatBank;
	}

	public void setAlamatBank(String alamatBank) {
		this.alamatBank = alamatBank;
	}

	public String getDokumenBank() {
		return this.dokumenBank;
	}

	public void setDokumenBank(String dokumenBank) {
		this.dokumenBank = dokumenBank;
	}

	public String getFlagSahMaklumatBank() {
		return this.flagSahMaklumatBank;
	}

	public void setFlagSahMaklumatBank(String flagSahMaklumatBank) {
		this.flagSahMaklumatBank = flagSahMaklumatBank;
	}

	public KategoriPengguna getJenisPengguna() {
		return this.jenisPengguna;
	}

	public void setJenisPengguna(KategoriPengguna jenisPengguna) {
		this.jenisPengguna = jenisPengguna;
	}

	public String getFlagAktif() {
		return this.flagAktif;
	}

	public void setFlagAktif(String flagAktif) {
		this.flagAktif = flagAktif;
	}

	public String getFlagAnak() {
		return this.flagAnak;
	}

	public void setFlagAnak(String flagAnak) {
		this.flagAnak = flagAnak;
	}

	public int getBilAnak() {
		return this.bilAnak;
	}

	public void setBilAnak(int bilAnak) {
		this.bilAnak = bilAnak;
	}

	public String getFlagHq() {
		return this.flagHq;
	}

	public void setFlagHq(String flagHq) {
		this.flagHq = flagHq;
	}

	public String getFlagSemakanJPN() {
		return this.flagSemakanJPN;
	}

	public void setFlagSemakanJPN(String flagSemakanJPN) {
		this.flagSemakanJPN = flagSemakanJPN;
	}

	public String getFlagSemakanHRMIS() {
		return this.flagSemakanHRMIS;
	}

	public void setFlagSemakanHRMIS(String flagSemakanHRMIS) {
		this.flagSemakanHRMIS = flagSemakanHRMIS;
	}

	public String getFlagSemakanPESARA() {
		return this.flagSemakanPESARA;
	}

	public void setFlagSemakanPESARA(String flagSemakanPESARA) {
		this.flagSemakanPESARA = flagSemakanPESARA;
	}

	public String getDokumenSokongan() {
		return this.dokumenSokongan;
	}

	public void setDokumenSokongan(String dokumenSokongan) {
		this.dokumenSokongan = dokumenSokongan;
	}

	public Date getTarikhPengesahan() {
		return this.tarikhPengesahan;
	}

	public void setTarikhPengesahan(Date tarikhPengesahan) {
		this.tarikhPengesahan = tarikhPengesahan;
	}

	public Date getTarikhLuputPengesahan() {
		return this.tarikhLuputPengesahan;
	}

	public void setTarikhLuputPengesahan(Date tarikhLuputPengesahan) {
		this.tarikhLuputPengesahan = tarikhLuputPengesahan;
	}

	public Users getPengesah() {
		return this.pengesah;
	}

	public void setPengesah(Users pengesah) {
		this.pengesah = pengesah;
	}

	public String getCatatanPengesahan() {
		return this.catatanPengesahan;
	}

	public void setCatatanPengesahan(String catatanPengesahan) {
		this.catatanPengesahan = catatanPengesahan;
	}

	public String getNotaPengesahan() {
		return this.notaPengesahan;
	}

	public void setNotaPengesahan(String notaPengesahan) {
		this.notaPengesahan = notaPengesahan;
	}

	public String getFlagMenungguPengesahan() {
		return this.flagMenungguPengesahan;
	}

	public void setFlagMenungguPengesahan(String flagMenungguPengesahan) {
		this.flagMenungguPengesahan = flagMenungguPengesahan;
	}

	public int getFlagUrusanPemohon() {
		return this.flagUrusanPemohon;
	}

	public void setFlagUrusanPemohon(int flagUrusanPemohon) {
		this.flagUrusanPemohon = flagUrusanPemohon;
	}

	public String getFlagDaftarSBBPH() {
		return this.flagDaftarSBBPH;
	}

	public void setFlagDaftarSBBPH(String flagDaftarSBBPH) {
		this.flagDaftarSBBPH = flagDaftarSBBPH;
	}

	public String getRppCatatanPengguna() {
		return this.rppCatatanPengguna;
	}

	public void setRppCatatanPengguna(String rppCatatanPengguna) {
		this.rppCatatanPengguna = rppCatatanPengguna;
	}

	public String getFlagDaftarManual() {
		return this.flagDaftarManual;
	}

	public void setFlagDaftarManual(String flagDaftarManual) {
		this.flagDaftarManual = flagDaftarManual;
	}

	public String getFlagGelanggang() {
		return this.flagGelanggang;
	}

	public void setFlagGelanggang(String flagGelanggang) {
		this.flagGelanggang = flagGelanggang;
	}

	public Users getIdMasuk() {
		return idMasuk;
	}

	public void setIdMasuk(Users idMasuk) {
		this.idMasuk = idMasuk;
	}

	public Date getTarikhMasuk() {
		return tarikhMasuk;
	}

	public void setTarikhMasuk(Date tarikhMasuk) {
		this.tarikhMasuk = tarikhMasuk;
	}

	public Users getIdKemaskini() {
		return idKemaskini;
	}

	public void setIdKemaskini(Users idKemaskini) {
		this.idKemaskini = idKemaskini;
	}

	public Date getTarikhKemaskini() {
		return tarikhKemaskini;
	}

	public void setTarikhKemaskini(Date tarikhKemaskini) {
		this.tarikhKemaskini = tarikhKemaskini;
	}
}