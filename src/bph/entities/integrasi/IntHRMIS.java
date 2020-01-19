package bph.entities.integrasi;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lebah.template.UID;


@Entity
@Table(name = "int_hrmis")
public class IntHRMIS {
	
	@Id
	@Column(name = "id")
	private String id;	
	
	@Column(name = "tarikh_hantar")
	@Temporal(TemporalType.TIMESTAMP)
	private Date tarikhHantar;
	
	@Column(name = "no_pengenalan")
	private String noPengenalan;
	
	@Column(name = "tarikh_terima")
	@Temporal(TemporalType.TIMESTAMP)
	private Date tarikhTerima;
	
	@Column(name = "nama")
	private String nama;
	
	@Column(name = "gelaran")
	private String gelaran;
	
	@Column(name = "kod_gelaran")
	private String kodGelaran;
	
	@Column(name = "tarikh_lahir")
	@Temporal(TemporalType.DATE)
	private Date tarikhLahir;
	
	@Column(name = "jantina")
	private String jantina;	
	
	@Column(name = "kod_jantina")
	private String kodJantina;
	
	@Column(name = "bangsa")
	private String bangsa;
	
	@Column(name = "kod_bangsa")
	private String kodBangsa;
	
	@Column(name = "etnik")
	private String etnik;
	
	@Column(name = "kod_etnik")
	private String kodEtnik;
	
	@Column(name = "agama")
	private String agama;
	
	@Column(name = "kod_agama")
	private String kodAgama;
	
	@Column(name = "status_perkahwinan")
	private String statusPerkahwinan;
	
	@Column(name = "kod_status_perkahwinan")
	private String kodStatusPerkahwinan;
	
	@Column(name = "no_telefon")
	private String noTelefon;

	@Column(name = "no_telefon_bimbit")
	private String noTelefonBimbit;
	
	@Column(name = "emel")
	private String emel;	
	
	@Column(name = "alamat_tetap1")
	private String alamatTetap1;
	
	@Column(name = "alamat_tetap2")
	private String alamatTetap2;
	
	@Column(name = "alamat_tetap3")
	private String alamatTetap3;
	
	@Column(name = "poskod_tetap")
	private String poskodTetap;	
	
	@Column(name = "bandar_tetap")
	private String bandarTetap;	
	
	@Column(name = "kod_bandar_tetap")
	private String kodBandarTetap;	
	
	@Column(name = "negeri_tetap")
	private String negeriTetap;
	
	@Column(name = "kod_negeri_tetap")
	private String kodNegeriTetap;
	
	@Column(name = "alamat_surat1")
	private String alamatSurat1;
	
	@Column(name = "alamat_surat2")
	private String alamatSurat2;
	
	@Column(name = "alamat_surat3")
	private String alamatSurat3;
	
	@Column(name = "poskod_surat")
	private String poskodSurat;	
	
	@Column(name = "bandar_surat")
	private String bandarSurat;	
	
	@Column(name = "kod_bandar_surat")
	private String kodBandarSurat;	
	
	@Column(name = "negeri_surat")
	private String negeriSurat;
	
	@Column(name = "kod_negeri_surat")
	private String kodNegeriSurat;		
	
	@Column(name = "kelas_perkhidmatan")
	private String kelasPerkhidmatan;
	
	@Column(name = "kod_kelas_perkhidmatan")
	private String kodKelasPerkhidmatan;
	
	@Column(name = "gred_perkhidmatan")
	private String gredPerkhidmatan;
	
	@Column(name = "kod_gred_perkhidmatan")
	private String kodGredPerkhidmatan;
	
	@Column(name = "tarikh_mula_sandang")
	@Temporal(TemporalType.DATE)
	private Date tarikhMulaSandang;	
	
	@Column(name = "tarikh_tamat_sandang")
	@Temporal(TemporalType.DATE)
	private Date tarikhTamatSandang;
	
	@Column(name = "umur_persaraan")
	private String umurPersaraan;
	
	@Column(name = "kementerian")
	private String kementerian;
	
	@Column(name = "kod_kementerian")
	private String kodKementerian;
	
	@Column(name = "agensi")
	private String agensi;
	
	@Column(name = "kod_agensi")
	private String kodAgensi;
	
	@Column(name = "jabatan")
	private String jabatan;
	
	@Column(name = "alamat_pejabat1")
	private String alamatPejabat1;
	
	@Column(name = "alamat_pejabat2")
	private String alamatPejabat2;
	
	@Column(name = "alamat_pejabat3")
	private String alamatPejabat3;
	
	@Column(name = "poskod_pejabat")
	private String poskodPejabat;	
	
	@Column(name = "bandar_pejabat")
	private String bandarPejabat;	
	
	@Column(name = "kod_bandar_pejabat")
	private String kodBandarPejabat;	
	
	@Column(name = "negeri_pejabat")
	private String negeriPejabat;
	
	@Column(name = "kod_negeri_pejabat")
	private String kodNegeriPejabat;	
	
	@Column(name = "no_telefon_pejabat")
	private String noTelefonPejabat;
	
	@Column(name = "status_lantikan")
	private String statusLantikan;
	
	@Column(name = "kod_status_lantikan")
	private String kodStatusLantikan;
	
	@Column(name = "no_pengenalan_pasangan")
	private String noPengenalanPasangan;
	
	@Column(name = "nama_pasangan")
	private String namaPasangan;
	
	@Column(name = "gelaran_pasangan")
	private String gelaranPasangan;
	
	@Column(name = "kod_gelaran_pasangan")
	private String kodGelaranPasangan;
	
	@Column(name = "no_telefon_pasangan")
	private String noTelefonPasangan;
	
	@Column(name = "jenis_badan_korporat")
	private String jenisBadanKorporat;
	
	@Column(name = "kod_jenis_badan_korporat")
	private String kodJenisBadanKorporat;
	
	@Column(name = "pekerjaan_pasangan")
	private String pekerjaanPasangan;
	
	@Column(name = "majikan_pasangan")
	private String majikanPasangan;
	
	@Column(name = "hubungan_pasangan")
	private String hubunganPasangan;
	
	@Column(name = "kod_hubungan_pasangan")
	private String kodHubunganPasangan;
	
	@Column(name = "flag_penjawat_awam")
	private String flagPenjawatAwam;
	
	@Column(name = "catatan")
	private String catatan;

	public IntHRMIS() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getTarikhHantar() {
		return tarikhHantar;
	}

	public void setTarikhHantar(Date tarikhHantar) {
		this.tarikhHantar = tarikhHantar;
	}

	public String getNoPengenalan() {
		return noPengenalan;
	}

	public void setNoPengenalan(String noPengenalan) {
		this.noPengenalan = noPengenalan;
	}

	public Date getTarikhTerima() {
		return tarikhTerima;
	}

	public void setTarikhTerima(Date tarikhTerima) {
		this.tarikhTerima = tarikhTerima;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getGelaran() {
		return gelaran;
	}

	public void setGelaran(String gelaran) {
		this.gelaran = gelaran;
	}

	public String getKodGelaran() {
		return kodGelaran;
	}

	public void setKodGelaran(String kodGelaran) {
		this.kodGelaran = kodGelaran;
	}

	public Date getTarikhLahir() {
		return tarikhLahir;
	}

	public void setTarikhLahir(Date tarikhLahir) {
		this.tarikhLahir = tarikhLahir;
	}

	public String getJantina() {
		return jantina;
	}

	public void setJantina(String jantina) {
		this.jantina = jantina;
	}

	public String getKodJantina() {
		return kodJantina;
	}

	public void setKodJantina(String kodJantina) {
		this.kodJantina = kodJantina;
	}

	public String getBangsa() {
		return bangsa;
	}

	public void setBangsa(String bangsa) {
		this.bangsa = bangsa;
	}

	public String getKodBangsa() {
		return kodBangsa;
	}

	public void setKodBangsa(String kodBangsa) {
		this.kodBangsa = kodBangsa;
	}

	public String getEtnik() {
		return etnik;
	}

	public void setEtnik(String etnik) {
		this.etnik = etnik;
	}

	public String getKodEtnik() {
		return kodEtnik;
	}

	public void setKodEtnik(String kodEtnik) {
		this.kodEtnik = kodEtnik;
	}

	public String getAgama() {
		return agama;
	}

	public void setAgama(String agama) {
		this.agama = agama;
	}

	public String getKodAgama() {
		return kodAgama;
	}

	public void setKodAgama(String kodAgama) {
		this.kodAgama = kodAgama;
	}

	public String getStatusPerkahwinan() {
		return statusPerkahwinan;
	}

	public void setStatusPerkahwinan(String statusPerkahwinan) {
		this.statusPerkahwinan = statusPerkahwinan;
	}

	public String getKodStatusPerkahwinan() {
		return kodStatusPerkahwinan;
	}

	public void setKodStatusPerkahwinan(String kodStatusPerkahwinan) {
		this.kodStatusPerkahwinan = kodStatusPerkahwinan;
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

	public String getEmel() {
		return emel;
	}

	public void setEmel(String emel) {
		this.emel = emel;
	}

	public String getAlamatTetap1() {
		return alamatTetap1;
	}

	public void setAlamatTetap1(String alamatTetap1) {
		this.alamatTetap1 = alamatTetap1;
	}

	public String getAlamatTetap2() {
		return alamatTetap2;
	}

	public void setAlamatTetap2(String alamatTetap2) {
		this.alamatTetap2 = alamatTetap2;
	}

	public String getAlamatTetap3() {
		return alamatTetap3;
	}

	public void setAlamatTetap3(String alamatTetap3) {
		this.alamatTetap3 = alamatTetap3;
	}

	public String getPoskodTetap() {
		return poskodTetap;
	}

	public void setPoskodTetap(String poskodTetap) {
		this.poskodTetap = poskodTetap;
	}

	public String getBandarTetap() {
		return bandarTetap;
	}

	public void setBandarTetap(String bandarTetap) {
		this.bandarTetap = bandarTetap;
	}

	public String getKodBandarTetap() {
		return kodBandarTetap;
	}

	public void setKodBandarTetap(String kodBandarTetap) {
		this.kodBandarTetap = kodBandarTetap;
	}

	public String getNegeriTetap() {
		return negeriTetap;
	}

	public void setNegeriTetap(String negeriTetap) {
		this.negeriTetap = negeriTetap;
	}

	public String getKodNegeriTetap() {
		return kodNegeriTetap;
	}

	public void setKodNegeriTetap(String kodNegeriTetap) {
		this.kodNegeriTetap = kodNegeriTetap;
	}

	public String getAlamatSurat1() {
		return alamatSurat1;
	}

	public void setAlamatSurat1(String alamatSurat1) {
		this.alamatSurat1 = alamatSurat1;
	}

	public String getAlamatSurat2() {
		return alamatSurat2;
	}

	public void setAlamatSurat2(String alamatSurat2) {
		this.alamatSurat2 = alamatSurat2;
	}

	public String getAlamatSurat3() {
		return alamatSurat3;
	}

	public void setAlamatSurat3(String alamatSurat3) {
		this.alamatSurat3 = alamatSurat3;
	}

	public String getPoskodSurat() {
		return poskodSurat;
	}

	public void setPoskodSurat(String poskodSurat) {
		this.poskodSurat = poskodSurat;
	}

	public String getBandarSurat() {
		return bandarSurat;
	}

	public void setBandarSurat(String bandarSurat) {
		this.bandarSurat = bandarSurat;
	}

	public String getKodBandarSurat() {
		return kodBandarSurat;
	}

	public void setKodBandarSurat(String kodBandarSurat) {
		this.kodBandarSurat = kodBandarSurat;
	}

	public String getNegeriSurat() {
		return negeriSurat;
	}

	public void setNegeriSurat(String negeriSurat) {
		this.negeriSurat = negeriSurat;
	}

	public String getKodNegeriSurat() {
		return kodNegeriSurat;
	}

	public void setKodNegeriSurat(String kodNegeriSurat) {
		this.kodNegeriSurat = kodNegeriSurat;
	}

	public String getKelasPerkhidmatan() {
		return kelasPerkhidmatan;
	}

	public void setKelasPerkhidmatan(String kelasPerkhidmatan) {
		this.kelasPerkhidmatan = kelasPerkhidmatan;
	}

	public String getKodKelasPerkhidmatan() {
		return kodKelasPerkhidmatan;
	}

	public void setKodKelasPerkhidmatan(String kodKelasPerkhidmatan) {
		this.kodKelasPerkhidmatan = kodKelasPerkhidmatan;
	}

	public String getGredPerkhidmatan() {
		return gredPerkhidmatan;
	}

	public void setGredPerkhidmatan(String gredPerkhidmatan) {
		this.gredPerkhidmatan = gredPerkhidmatan;
	}

	public String getKodGredPerkhidmatan() {
		return kodGredPerkhidmatan;
	}

	public void setKodGredPerkhidmatan(String kodGredPerkhidmatan) {
		this.kodGredPerkhidmatan = kodGredPerkhidmatan;
	}

	public Date getTarikhMulaSandang() {
		return tarikhMulaSandang;
	}

	public void setTarikhMulaSandang(Date tarikhMulaSandang) {
		this.tarikhMulaSandang = tarikhMulaSandang;
	}

	public Date getTarikhTamatSandang() {
		return tarikhTamatSandang;
	}

	public void setTarikhTamatSandang(Date tarikhTamatSandang) {
		this.tarikhTamatSandang = tarikhTamatSandang;
	}

	public String getUmurPersaraan() {
		return umurPersaraan;
	}

	public void setUmurPersaraan(String umurPersaraan) {
		this.umurPersaraan = umurPersaraan;
	}

	public String getKementerian() {
		return kementerian;
	}

	public void setKementerian(String kementerian) {
		this.kementerian = kementerian;
	}

	public String getKodKementerian() {
		return kodKementerian;
	}

	public void setKodKementerian(String kodKementerian) {
		this.kodKementerian = kodKementerian;
	}

	public String getAgensi() {
		return agensi;
	}

	public void setAgensi(String agensi) {
		this.agensi = agensi;
	}

	public String getKodAgensi() {
		return kodAgensi;
	}

	public void setKodAgensi(String kodAgensi) {
		this.kodAgensi = kodAgensi;
	}

	public String getJabatan() {
		return jabatan;
	}

	public void setJabatan(String jabatan) {
		this.jabatan = jabatan;
	}

	public String getAlamatPejabat1() {
		return alamatPejabat1;
	}

	public void setAlamatPejabat1(String alamatPejabat1) {
		this.alamatPejabat1 = alamatPejabat1;
	}

	public String getAlamatPejabat2() {
		return alamatPejabat2;
	}

	public void setAlamatPejabat2(String alamatPejabat2) {
		this.alamatPejabat2 = alamatPejabat2;
	}

	public String getAlamatPejabat3() {
		return alamatPejabat3;
	}

	public void setAlamatPejabat3(String alamatPejabat3) {
		this.alamatPejabat3 = alamatPejabat3;
	}

	public String getPoskodPejabat() {
		return poskodPejabat;
	}

	public void setPoskodPejabat(String poskodPejabat) {
		this.poskodPejabat = poskodPejabat;
	}

	public String getBandarPejabat() {
		return bandarPejabat;
	}

	public void setBandarPejabat(String bandarPejabat) {
		this.bandarPejabat = bandarPejabat;
	}

	public String getKodBandarPejabat() {
		return kodBandarPejabat;
	}

	public void setKodBandarPejabat(String kodBandarPejabat) {
		this.kodBandarPejabat = kodBandarPejabat;
	}

	public String getNegeriPejabat() {
		return negeriPejabat;
	}

	public void setNegeriPejabat(String negeriPejabat) {
		this.negeriPejabat = negeriPejabat;
	}

	public String getKodNegeriPejabat() {
		return kodNegeriPejabat;
	}

	public void setKodNegeriPejabat(String kodNegeriPejabat) {
		this.kodNegeriPejabat = kodNegeriPejabat;
	}

	public String getNoTelefonPejabat() {
		return noTelefonPejabat;
	}

	public void setNoTelefonPejabat(String noTelefonPejabat) {
		this.noTelefonPejabat = noTelefonPejabat;
	}

	public String getStatusLantikan() {
		return statusLantikan;
	}

	public void setStatusLantikan(String statusLantikan) {
		this.statusLantikan = statusLantikan;
	}

	public String getKodStatusLantikan() {
		return kodStatusLantikan;
	}

	public void setKodStatusLantikan(String kodStatusLantikan) {
		this.kodStatusLantikan = kodStatusLantikan;
	}

	public String getNoPengenalanPasangan() {
		return noPengenalanPasangan;
	}

	public void setNoPengenalanPasangan(String noPengenalanPasangan) {
		this.noPengenalanPasangan = noPengenalanPasangan;
	}

	public String getNamaPasangan() {
		return namaPasangan;
	}

	public void setNamaPasangan(String namaPasangan) {
		this.namaPasangan = namaPasangan;
	}

	public String getGelaranPasangan() {
		return gelaranPasangan;
	}

	public void setGelaranPasangan(String gelaranPasangan) {
		this.gelaranPasangan = gelaranPasangan;
	}

	public String getKodGelaranPasangan() {
		return kodGelaranPasangan;
	}

	public void setKodGelaranPasangan(String kodGelaranPasangan) {
		this.kodGelaranPasangan = kodGelaranPasangan;
	}

	public String getNoTelefonPasangan() {
		return noTelefonPasangan;
	}

	public void setNoTelefonPasangan(String noTelefonPasangan) {
		this.noTelefonPasangan = noTelefonPasangan;
	}

	public String getJenisBadanKorporat() {
		return jenisBadanKorporat;
	}

	public void setJenisBadanKorporat(String jenisBadanKorporat) {
		this.jenisBadanKorporat = jenisBadanKorporat;
	}

	public String getKodJenisBadanKorporat() {
		return kodJenisBadanKorporat;
	}

	public void setKodJenisBadanKorporat(String kodJenisBadanKorporat) {
		this.kodJenisBadanKorporat = kodJenisBadanKorporat;
	}

	public String getPekerjaanPasangan() {
		return pekerjaanPasangan;
	}

	public void setPekerjaanPasangan(String pekerjaanPasangan) {
		this.pekerjaanPasangan = pekerjaanPasangan;
	}

	public String getMajikanPasangan() {
		return majikanPasangan;
	}

	public void setMajikanPasangan(String majikanPasangan) {
		this.majikanPasangan = majikanPasangan;
	}

	public String getHubunganPasangan() {
		return hubunganPasangan;
	}

	public void setHubunganPasangan(String hubunganPasangan) {
		this.hubunganPasangan = hubunganPasangan;
	}

	public String getKodHubunganPasangan() {
		return kodHubunganPasangan;
	}

	public void setKodHubunganPasangan(String kodHubunganPasangan) {
		this.kodHubunganPasangan = kodHubunganPasangan;
	}

	public String getFlagPenjawatAwam() {
		return flagPenjawatAwam;
	}

	public void setFlagPenjawatAwam(String flagPenjawatAwam) {
		this.flagPenjawatAwam = flagPenjawatAwam;
	}

	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}
}
