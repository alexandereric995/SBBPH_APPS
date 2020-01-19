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

import lebah.template.UID;
import bph.entities.kod.Agama;
import bph.entities.kod.Agensi;
import bph.entities.kod.BadanBerkanun;
import bph.entities.kod.Bandar;
import bph.entities.kod.Bangsa;
import bph.entities.kod.Etnik;
import bph.entities.kod.Gelaran;
import bph.entities.kod.GredPerkhidmatan;
import bph.entities.kod.Jantina;
import bph.entities.kod.Jawatan;
import bph.entities.kod.JenisPengenalan;
import bph.entities.kod.JenisPerkhidmatan;
import bph.entities.kod.KategoriPengguna;
import bph.entities.kod.KelasPerkhidmatan;
import bph.entities.kod.LokasiPermohonan;
import bph.entities.kod.Seksyen;
import bph.entities.kod.Status;
import bph.entities.kod.StatusPekerjaan;
import bph.entities.kod.StatusPerkahwinan;
import bph.entities.kod.StatusPerkhidmatan;

@Entity
@Table(name="users_activity")
public class UsersActivity {
	
	//1
	@Id
	@Column(name="id")
	private String id;
	
	//2
	@Column(name="user_login")
	private String userLogin;
	
	//3
	@ManyToOne
	@JoinColumn(name="gelaran")
	private Gelaran gelaran;	
	
	//4
	@Column(name="user_name")
	private String userName;
	
	//5
	@Column(name="user_address")
	private String userAddress;
	
	//6
	@Column(name="user_address2")
	private String userAddress2;
	
	//7
	@Column(name="user_address3")
	private String userAddress3;
	
	//8
	@Column(name="user_postcode")
	private String userPostcode;
	
	//9
	@ManyToOne
	@JoinColumn(name="user_bandar")
	private Bandar userBandar;
	
	//10
	@ManyToOne
	@JoinColumn(name="jenis_pengenalan")
	private JenisPengenalan jenisPengenalan;
	
	//11
	@Column(name = "no_kp")
	private String noKP;

	//12
	@Column(name = "no_kp_2")
	private String noKP2;
	
	//13
	@Column(name = "no_telefon")
	private String noTelefon;	

	//14
	@Column(name = "no_telefon_bimbit")
	private String noTelefonBimbit;
	
	//15
	@Column(name = "no_telefon_pejabat")
	private String noTelefonPejabat;

	//16
	@Column(name = "no_faks")
	private String noFaks;

	//17
	@Column(name = "emel")
	private String emel;
	
	//18
	@ManyToOne
	@JoinColumn(name="id_seksyen")
	private Seksyen seksyen;	
	
	//19
	@ManyToOne
	@JoinColumn(name="id_jawatan")
	private Jawatan jawatan;
	
	//20
	@Column(name = "keterangan_jawatan")
	private String keteranganJawatan;
	
	//21
	@ManyToOne
	@JoinColumn(name="id_kelas_perkhidmatan")
	private KelasPerkhidmatan kelasPerkhidmatan;
	
	//22
	@OneToOne
	@JoinColumn(name = "id_gred_perkhidmatan")
	private GredPerkhidmatan gredPerkhidmatan;

	//23
	@OneToOne
	@JoinColumn(name="id_jenis_perkhidmatan")
	private JenisPerkhidmatan jenisPerkhidmatan;
	
	//24
	@ManyToOne
	@JoinColumn(name="id_agensi")
	private Agensi agensi;
	
	//25
	@Column(name = "bahagian")
	private String bahagian;
	
	//26
	@ManyToOne
	@JoinColumn(name="id_badan_berkanun")
	private BadanBerkanun badanBerkanun;
	
	//27
	@ManyToOne
	@JoinColumn(name="jantina")
	private Jantina jantina;
	
	//28
	@ManyToOne
	@JoinColumn(name="bangsa")
	private Bangsa bangsa;
	
	//29
	@ManyToOne
	@JoinColumn(name="etnik")
	private Etnik etnik;
	
	//30
	@ManyToOne
	@JoinColumn(name="agama")
	private Agama agama;
	
	//31
	@Temporal(TemporalType.DATE) 
	@Column(name="tarikh_lahir")
	private Date tarikhLahir;
	
	//32
	@ManyToOne
	@JoinColumn(name="status_perkahwinan")
	private StatusPerkahwinan statusPerkahwinan;

	//34
	@Column(name = "alamat_1")
	private String alamat1;
	
	//35
	@Column(name = "alamat_2")
	private String alamat2;
	
	//36
	@Column(name = "alamat_3")
	private String alamat3;
	
	//37
	@Column(name = "poskod")
	private String poskod;
	
	//38
	@ManyToOne
	@JoinColumn(name="id_bandar")
	private Bandar bandar;
	
	//39
	@ManyToOne
	@JoinColumn(name="jenis_pengguna")
	private KategoriPengguna jenisPengguna;
	
	//40
	@Column(name="flag_aktif")
	private String flagAktif;
	
	//41
	@Column(name="flag_anak")
	private String flagAnak;
	
	//42
	@Column(name="bil_anak")
	private int bilAnak;
		
	//users job-------------------------------------------------------------------------------------

	//43
	@ManyToOne
	@JoinColumn(name = "uj_id_jawatan")
	private Jawatan ujJawatan;
	
	//44
	@ManyToOne
	@JoinColumn(name = "uj_id_gred_jawatan")
	private GredPerkhidmatan ujGredJawatan;
	
	//46
	@ManyToOne
	@JoinColumn(name = "uj_id_jenis_perkhidmatan")
	private JenisPerkhidmatan ujJenisPerkhidmatan;
	
	//47
	@ManyToOne
	@JoinColumn(name = "uj_id_kelas_perkhidmatan")
	private KelasPerkhidmatan ujKelasPerkhidmatan;
	
	//48
	@ManyToOne
	@JoinColumn(name = "uj_id_status_perkhidmatan")
	private StatusPerkhidmatan ujStatusPerkhidmatan;
	
	//49
	@ManyToOne
	@JoinColumn(name = "uj_id_agensi")
	private Agensi ujAgensi;
	
	//50
	@Column(name = "uj_bahagian")
	private String ujBahagian;
	
	//51
	@ManyToOne
	@JoinColumn(name = "uj_id_badan_berkanun")
	private BadanBerkanun ujBadanBerkanun;
	
	//52
	@Column(name = "uj_tarikh_lantikan")
	@Temporal(TemporalType.DATE)
	private Date ujTarikhLantikan;
	
	//53
	@Column(name = "uj_tarikh_tamat")
	@Temporal(TemporalType.DATE)
	private Date ujTarikhTamat;
	
	//54
	@Column(name = "uj_no_gaji")
	private String ujNoGaji;
	
	//55
	@Column(name = "uj_gaji_pokok")
	private Double ujGajiPokok;
	
	//56
	@Column(name = "uj_alamat_1")
	private String ujAlamat1;
	
	//57
	@Column(name = "uj_alamat_2")
	private String ujAlamat2;
	
	//58
	@Column(name = "uj_alamat_3")
	private String ujAlamat3;
	
	//59
	@Column(name = "uj_poskod")
	private String ujPoskod;
	
	//60
	@ManyToOne
	@JoinColumn(name = "uj_id_bandar")
	private Bandar ujBandar;	
	
	//61
	@Column(name = "uj_no_tel_pejabat")
	private String ujNoTelPejabat;
	
	//62
	@Column(name = "uj_no_faks")
	private String ujNoFaks;
	
	//63
	@Column(name = "uj_emel")
	private String ujEmel;
	
	//64
	@Column(name = "uj_flag_itp")
	private int ujFlagITP;
	
	//65
	@Column(name = "uj_flag_epw")
	private int ujFlagEPW;	

	//66
	@Column(name = "uj_flag_cola")
	private int ujFlagCola;
	
	//67
	@Column(name = "uj_tarikh_bersara")
	@Temporal(TemporalType.DATE)
	private Date ujTarikhBersara;
	
	
	//users spouse-------------------------------------------------------------------------------
	
	//68
	@Column(name = "us_nama_pasangan")
	private String usNamaPasangan;
	
	//69
	@ManyToOne
	@JoinColumn(name="us_jenis_pengenalan")
	private JenisPengenalan usJenisPengenalan;
	
	//70
	@Column(name = "us_no_kp_pasangan")
	private String usNoKPPasangan;
	
	//71
	@ManyToOne
	@JoinColumn(name="us_status_pekerjaan_pasangan")
	private StatusPekerjaan usStatusPekerjaanPasangan;
	
	//72
	@Column(name = "us_jenis_pekerjaan")
	private String usJenisPekerjaan;
	
	//73
	@Column(name = "us_gaji_pasangan")
	private Double usGajiPasangan;
	
	//74
	@Column(name = "us_nama_syarikat")
	private String usNamaSyarikat;
	
	//75
	@Column(name = "us_alamat_pejabat_1")
	private String usAlamatPejabat1;
	
	//76
	@Column(name = "us_alamat_pejabat_2")
	private String usAlamatPejabat2;
	
	//77
	@Column(name = "us_alamat_pejabat_3")
	private String usAlamatPejabat3;
	
	//78
	@Column(name = "us_poskod_pejabat")
	private String usPoskodPejabat;
	
	//79
	@ManyToOne
	@JoinColumn(name="us_bandar_pejabat")
	private Bandar usBandarPejabat;
	
	//80
	@Column(name = "us_no_tel_pejabat")
	private String usNoTelPejabat;
	
	//81
	@Column(name = "us_no_faks_pejabat")
	private String usNoFaksPejabat;
	
	//82
	@Column(name = "us_no_tel_bimbit")
	private String usNoTelBimbit;

	//83
	@ManyToOne
	@JoinColumn(name = "us_id_jawatan")
	private Jawatan usJawatan;
	
	//84
	@ManyToOne
	@JoinColumn(name = "us_id_gred_jawatan")
	private GredPerkhidmatan usGredJawatan;
	
	//85
	@ManyToOne
	@JoinColumn(name = "us_id_kelas_perkhidmatan")
	private KelasPerkhidmatan usKelasPerkhidmatan;
	
	//86
	@ManyToOne
	@JoinColumn(name = "us_id_agensi")
	private Agensi usAgensi;
	
	//87
	@ManyToOne
	@JoinColumn(name = "us_id_badan_berkanun")
	private BadanBerkanun usBadanBerkanun;
	
	@Column(name = "reason")
	private String reason;
	
	@Column(name = "message")
	private String message;
	
	
	//88
	@Column(name = "tarikh_kemaskini")
	@Temporal(TemporalType.TIMESTAMP)
	private Date tarikhKemaskini;
	
	//89
	@Column(name = "turutan")
	private int turutan;
	
	@Column(name = "flag_downgrade")
	private String flagDowngrade;
	
	@Column(name = "flag_tuntutan")
	private String flagTuntutan;
	
	//88
	@Column(name = "tarikh_permohonan")
	@Temporal(TemporalType.TIMESTAMP)
	private Date tarikhPermohonan;
	
	@JoinColumn (name = "id_lokasi_permohonan")
	private LokasiPermohonan lokasiPermohonan;
	
	@JoinColumn (name = "status")
	private Status status;
	
	public Users getKemaskiniOleh() {
		return kemaskiniOleh;
	}

	public void setKemaskiniOleh(Users kemaskiniOleh) {
		this.kemaskiniOleh = kemaskiniOleh;
	}

	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}

	@JoinColumn (name = "kemaskini_oleh")
	private Users kemaskiniOleh;
	
	@Column(name="catatan")
	private String catatan;
	
	public String getFlagDowngrade() {
		return flagDowngrade;
	}

	public void setFlagDowngrade(String flagDowngrade) {
		this.flagDowngrade = flagDowngrade;
	}

	public String getFlagTuntutan() {
		return flagTuntutan;
	}

	public void setFlagTuntutan(String flagTuntutan) {
		this.flagTuntutan = flagTuntutan;
	}

	public Date getTarikhPermohonan() {
		return tarikhPermohonan;
	}

	public void setTarikhPermohonan(Date tarikhPermohonan) {
		this.tarikhPermohonan = tarikhPermohonan;
	}

	public UsersActivity() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserLogin() {
		return userLogin;
	}

	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}

	public Gelaran getGelaran() {
		return gelaran;
	}

	public void setGelaran(Gelaran gelaran) {
		this.gelaran = gelaran;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserAddress() {
		return userAddress;
	}

	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}

	public String getUserAddress2() {
		return userAddress2;
	}

	public void setUserAddress2(String userAddress2) {
		this.userAddress2 = userAddress2;
	}

	public String getUserAddress3() {
		return userAddress3;
	}

	public void setUserAddress3(String userAddress3) {
		this.userAddress3 = userAddress3;
	}

	public String getUserPostcode() {
		return userPostcode;
	}

	public void setUserPostcode(String userPostcode) {
		this.userPostcode = userPostcode;
	}

	public Bandar getUserBandar() {
		return userBandar;
	}

	public void setUserBandar(Bandar userBandar) {
		this.userBandar = userBandar;
	}

	public JenisPengenalan getJenisPengenalan() {
		return jenisPengenalan;
	}

	public void setJenisPengenalan(JenisPengenalan jenisPengenalan) {
		this.jenisPengenalan = jenisPengenalan;
	}

	public String getNoKP() {
		return noKP;
	}

	public void setNoKP(String noKP) {
		this.noKP = noKP;
	}

	public String getNoKP2() {
		return noKP2;
	}

	public void setNoKP2(String noKP2) {
		this.noKP2 = noKP2;
	}

	public String getNoTelefon() {
		return noTelefon;
	}

	public void setNoTelefon(String noTelefon) {
		this.noTelefon = noTelefon;
	}

	public String getNoTelefonBimbit() {
		return noTelefonBimbit;
	}

	public void setNoTelefonBimbit(String noTelefonBimbit) {
		this.noTelefonBimbit = noTelefonBimbit;
	}

	public String getNoTelefonPejabat() {
		return noTelefonPejabat;
	}

	public void setNoTelefonPejabat(String noTelefonPejabat) {
		this.noTelefonPejabat = noTelefonPejabat;
	}

	public String getNoFaks() {
		return noFaks;
	}

	public void setNoFaks(String noFaks) {
		this.noFaks = noFaks;
	}

	public String getEmel() {
		return emel;
	}

	public void setEmel(String emel) {
		this.emel = emel;
	}

	public Seksyen getSeksyen() {
		return seksyen;
	}

	public void setSeksyen(Seksyen seksyen) {
		this.seksyen = seksyen;
	}

	public Jawatan getJawatan() {
		return jawatan;
	}

	public void setJawatan(Jawatan jawatan) {
		this.jawatan = jawatan;
	}

	public String getKeteranganJawatan() {
		return keteranganJawatan;
	}

	public void setKeteranganJawatan(String keteranganJawatan) {
		this.keteranganJawatan = keteranganJawatan;
	}

	public KelasPerkhidmatan getKelasPerkhidmatan() {
		return kelasPerkhidmatan;
	}

	public void setKelasPerkhidmatan(KelasPerkhidmatan kelasPerkhidmatan) {
		this.kelasPerkhidmatan = kelasPerkhidmatan;
	}

	public GredPerkhidmatan getGredPerkhidmatan() {
		return gredPerkhidmatan;
	}

	public void setGredPerkhidmatan(GredPerkhidmatan gredPerkhidmatan) {
		this.gredPerkhidmatan = gredPerkhidmatan;
	}

	public JenisPerkhidmatan getJenisPerkhidmatan() {
		return jenisPerkhidmatan;
	}

	public void setJenisPerkhidmatan(JenisPerkhidmatan jenisPerkhidmatan) {
		this.jenisPerkhidmatan = jenisPerkhidmatan;
	}

	public Agensi getAgensi() {
		return agensi;
	}

	public void setAgensi(Agensi agensi) {
		this.agensi = agensi;
	}

	public String getBahagian() {
		return bahagian;
	}

	public void setBahagian(String bahagian) {
		this.bahagian = bahagian;
	}

	public BadanBerkanun getBadanBerkanun() {
		return badanBerkanun;
	}

	public void setBadanBerkanun(BadanBerkanun badanBerkanun) {
		this.badanBerkanun = badanBerkanun;
	}

	public Jantina getJantina() {
		return jantina;
	}

	public void setJantina(Jantina jantina) {
		this.jantina = jantina;
	}

	public Bangsa getBangsa() {
		return bangsa;
	}

	public void setBangsa(Bangsa bangsa) {
		this.bangsa = bangsa;
	}

	public Etnik getEtnik() {
		return etnik;
	}

	public void setEtnik(Etnik etnik) {
		this.etnik = etnik;
	}

	public Agama getAgama() {
		return agama;
	}

	public void setAgama(Agama agama) {
		this.agama = agama;
	}

	public Date getTarikhLahir() {
		return tarikhLahir;
	}

	public void setTarikhLahir(Date tarikhLahir) {
		this.tarikhLahir = tarikhLahir;
	}

	public String getAlamat1() {
		return alamat1;
	}

	public void setAlamat1(String alamat1) {
		this.alamat1 = alamat1;
	}

	public String getAlamat2() {
		return alamat2;
	}

	public void setAlamat2(String alamat2) {
		this.alamat2 = alamat2;
	}

	public String getAlamat3() {
		return alamat3;
	}

	public void setAlamat3(String alamat3) {
		this.alamat3 = alamat3;
	}

	public String getPoskod() {
		return poskod;
	}

	public void setPoskod(String poskod) {
		this.poskod = poskod;
	}

	public Bandar getBandar() {
		return bandar;
	}

	public void setBandar(Bandar bandar) {
		this.bandar = bandar;
	}

	public KategoriPengguna getJenisPengguna() {
		return jenisPengguna;
	}

	public void setJenisPengguna(KategoriPengguna jenisPengguna) {
		this.jenisPengguna = jenisPengguna;
	}

	public String getFlagAktif() {
		return flagAktif;
	}

	public void setFlagAktif(String flagAktif) {
		this.flagAktif = flagAktif;
	}

	public String getFlagAnak() {
		return flagAnak;
	}

	public void setFlagAnak(String flagAnak) {
		this.flagAnak = flagAnak;
	}

	public int getBilAnak() {
		return bilAnak;
	}

	public void setBilAnak(int bilAnak) {
		this.bilAnak = bilAnak;
	}

	public Jawatan getUjJawatan() {
		return ujJawatan;
	}

	public void setUjJawatan(Jawatan ujJawatan) {
		this.ujJawatan = ujJawatan;
	}

	public GredPerkhidmatan getUjGredJawatan() {
		return ujGredJawatan;
	}

	public void setUjGredJawatan(GredPerkhidmatan ujGredJawatan) {
		this.ujGredJawatan = ujGredJawatan;
	}

	public JenisPerkhidmatan getUjJenisPerkhidmatan() {
		return ujJenisPerkhidmatan;
	}

	public void setUjJenisPerkhidmatan(JenisPerkhidmatan ujJenisPerkhidmatan) {
		this.ujJenisPerkhidmatan = ujJenisPerkhidmatan;
	}

	public KelasPerkhidmatan getUjKelasPerkhidmatan() {
		return ujKelasPerkhidmatan;
	}

	public void setUjKelasPerkhidmatan(KelasPerkhidmatan ujKelasPerkhidmatan) {
		this.ujKelasPerkhidmatan = ujKelasPerkhidmatan;
	}

	public StatusPerkhidmatan getUjStatusPerkhidmatan() {
		return ujStatusPerkhidmatan;
	}

	public void setUjStatusPerkhidmatan(StatusPerkhidmatan ujStatusPerkhidmatan) {
		this.ujStatusPerkhidmatan = ujStatusPerkhidmatan;
	}

	public Agensi getUjAgensi() {
		return ujAgensi;
	}

	public void setUjAgensi(Agensi ujAgensi) {
		this.ujAgensi = ujAgensi;
	}

	public String getUjBahagian() {
		return ujBahagian;
	}

	public void setUjBahagian(String ujBahagian) {
		this.ujBahagian = ujBahagian;
	}

	public BadanBerkanun getUjBadanBerkanun() {
		return ujBadanBerkanun;
	}

	public void setUjBadanBerkanun(BadanBerkanun ujBadanBerkanun) {
		this.ujBadanBerkanun = ujBadanBerkanun;
	}

	public Date getUjTarikhLantikan() {
		return ujTarikhLantikan;
	}

	public void setUjTarikhLantikan(Date ujTarikhLantikan) {
		this.ujTarikhLantikan = ujTarikhLantikan;
	}

	public Date getUjTarikhTamat() {
		return ujTarikhTamat;
	}

	public void setUjTarikhTamat(Date ujTarikhTamat) {
		this.ujTarikhTamat = ujTarikhTamat;
	}

	public String getUjNoGaji() {
		return ujNoGaji;
	}

	public void setUjNoGaji(String ujNoGaji) {
		this.ujNoGaji = ujNoGaji;
	}

	public Double getUjGajiPokok() {
		return ujGajiPokok;
	}

	public void setUjGajiPokok(Double ujGajiPokok) {
		this.ujGajiPokok = ujGajiPokok;
	}

	public String getUjAlamat1() {
		return ujAlamat1;
	}

	public void setUjAlamat1(String ujAlamat1) {
		this.ujAlamat1 = ujAlamat1;
	}

	public String getUjAlamat2() {
		return ujAlamat2;
	}

	public void setUjAlamat2(String ujAlamat2) {
		this.ujAlamat2 = ujAlamat2;
	}

	public String getUjAlamat3() {
		return ujAlamat3;
	}

	public void setUjAlamat3(String ujAlamat3) {
		this.ujAlamat3 = ujAlamat3;
	}

	public String getUjPoskod() {
		return ujPoskod;
	}

	public void setUjPoskod(String ujPoskod) {
		this.ujPoskod = ujPoskod;
	}

	public Bandar getUjBandar() {
		return ujBandar;
	}

	public void setUjBandar(Bandar ujBandar) {
		this.ujBandar = ujBandar;
	}

	public String getUjNoTelPejabat() {
		return ujNoTelPejabat;
	}

	public void setUjNoTelPejabat(String ujNoTelPejabat) {
		this.ujNoTelPejabat = ujNoTelPejabat;
	}

	public String getUjNoFaks() {
		return ujNoFaks;
	}

	public void setUjNoFaks(String ujNoFaks) {
		this.ujNoFaks = ujNoFaks;
	}

	public String getUjEmel() {
		return ujEmel;
	}

	public void setUjEmel(String ujEmel) {
		this.ujEmel = ujEmel;
	}

	public int getUjFlagITP() {
		return ujFlagITP;
	}

	public void setUjFlagITP(int ujFlagITP) {
		this.ujFlagITP = ujFlagITP;
	}

	public int getUjFlagEPW() {
		return ujFlagEPW;
	}

	public void setUjFlagEPW(int ujFlagEPW) {
		this.ujFlagEPW = ujFlagEPW;
	}

	public int getUjFlagCola() {
		return ujFlagCola;
	}

	public void setUjFlagCola(int ujFlagCola) {
		this.ujFlagCola = ujFlagCola;
	}

	public Date getUjTarikhBersara() {
		return ujTarikhBersara;
	}

	public void setUjTarikhBersara(Date ujTarikhBersara) {
		this.ujTarikhBersara = ujTarikhBersara;
	}

	public String getUsNamaPasangan() {
		return usNamaPasangan;
	}

	public void setUsNamaPasangan(String usNamaPasangan) {
		this.usNamaPasangan = usNamaPasangan;
	}

	public JenisPengenalan getUsJenisPengenalan() {
		return usJenisPengenalan;
	}

	public void setUsJenisPengenalan(JenisPengenalan usJenisPengenalan) {
		this.usJenisPengenalan = usJenisPengenalan;
	}

	public String getUsNoKPPasangan() {
		return usNoKPPasangan;
	}

	public void setUsNoKPPasangan(String usNoKPPasangan) {
		this.usNoKPPasangan = usNoKPPasangan;
	}

	public StatusPekerjaan getUsStatusPekerjaanPasangan() {
		return usStatusPekerjaanPasangan;
	}

	public void setUsStatusPekerjaanPasangan(
			StatusPekerjaan usStatusPekerjaanPasangan) {
		this.usStatusPekerjaanPasangan = usStatusPekerjaanPasangan;
	}

	public String getUsJenisPekerjaan() {
		return usJenisPekerjaan;
	}

	public void setUsJenisPekerjaan(String usJenisPekerjaan) {
		this.usJenisPekerjaan = usJenisPekerjaan;
	}

	public Double getUsGajiPasangan() {
		return usGajiPasangan;
	}

	public void setUsGajiPasangan(Double usGajiPasangan) {
		this.usGajiPasangan = usGajiPasangan;
	}

	public String getUsNamaSyarikat() {
		return usNamaSyarikat;
	}

	public void setUsNamaSyarikat(String usNamaSyarikat) {
		this.usNamaSyarikat = usNamaSyarikat;
	}

	public String getUsAlamatPejabat1() {
		return usAlamatPejabat1;
	}

	public void setUsAlamatPejabat1(String usAlamatPejabat1) {
		this.usAlamatPejabat1 = usAlamatPejabat1;
	}

	public String getUsAlamatPejabat2() {
		return usAlamatPejabat2;
	}

	public void setUsAlamatPejabat2(String usAlamatPejabat2) {
		this.usAlamatPejabat2 = usAlamatPejabat2;
	}

	public String getUsAlamatPejabat3() {
		return usAlamatPejabat3;
	}

	public void setUsAlamatPejabat3(String usAlamatPejabat3) {
		this.usAlamatPejabat3 = usAlamatPejabat3;
	}

	public String getUsPoskodPejabat() {
		return usPoskodPejabat;
	}

	public void setUsPoskodPejabat(String usPoskodPejabat) {
		this.usPoskodPejabat = usPoskodPejabat;
	}

	public Bandar getUsBandarPejabat() {
		return usBandarPejabat;
	}

	public void setUsBandarPejabat(Bandar usBandarPejabat) {
		this.usBandarPejabat = usBandarPejabat;
	}

	public String getUsNoTelPejabat() {
		return usNoTelPejabat;
	}

	public void setUsNoTelPejabat(String usNoTelPejabat) {
		this.usNoTelPejabat = usNoTelPejabat;
	}

	public String getUsNoFaksPejabat() {
		return usNoFaksPejabat;
	}

	public void setUsNoFaksPejabat(String usNoFaksPejabat) {
		this.usNoFaksPejabat = usNoFaksPejabat;
	}

	public String getUsNoTelBimbit() {
		return usNoTelBimbit;
	}

	public void setUsNoTelBimbit(String usNoTelBimbit) {
		this.usNoTelBimbit = usNoTelBimbit;
	}

	public Jawatan getUsJawatan() {
		return usJawatan;
	}

	public void setUsJawatan(Jawatan usJawatan) {
		this.usJawatan = usJawatan;
	}

	public GredPerkhidmatan getUsGredJawatan() {
		return usGredJawatan;
	}

	public void setUsGredJawatan(GredPerkhidmatan usGredJawatan) {
		this.usGredJawatan = usGredJawatan;
	}

	public KelasPerkhidmatan getUsKelasPerkhidmatan() {
		return usKelasPerkhidmatan;
	}

	public void setUsKelasPerkhidmatan(KelasPerkhidmatan usKelasPerkhidmatan) {
		this.usKelasPerkhidmatan = usKelasPerkhidmatan;
	}

	public Agensi getUsAgensi() {
		return usAgensi;
	}

	public void setUsAgensi(Agensi usAgensi) {
		this.usAgensi = usAgensi;
	}

	public BadanBerkanun getUsBadanBerkanun() {
		return usBadanBerkanun;
	}

	public void setUsBadanBerkanun(BadanBerkanun usBadanBerkanun) {
		this.usBadanBerkanun = usBadanBerkanun;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getReason() {
		return reason;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public Date getTarikhKemaskini() {
		return tarikhKemaskini;
	}

	public void setTarikhKemaskini(Date tarikhKemaskini) {
		this.tarikhKemaskini = tarikhKemaskini;
	}

	public int getTurutan() {
		return turutan;
	}

	public void setTurutan(int turutan) {
		this.turutan = turutan;
	}

	public void setStatusPerkahwinan(StatusPerkahwinan statusPerkahwinan) {
		this.statusPerkahwinan = statusPerkahwinan;
	}

	public StatusPerkahwinan getStatusPerkahwinan() {
		return statusPerkahwinan;
	}

	public void setLokasiPermohonan(LokasiPermohonan lokasiPermohonan) {
		this.lokasiPermohonan = lokasiPermohonan;
	}

	public LokasiPermohonan getLokasiPermohonan() {
		return lokasiPermohonan;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Status getStatus() {
		return status;
	}
	
	
}
