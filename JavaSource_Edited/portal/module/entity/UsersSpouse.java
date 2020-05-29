package portal.module.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lebah.template.UID;
import bph.entities.kod.Agensi;
import bph.entities.kod.BadanBerkanun;
import bph.entities.kod.Bandar;
import bph.entities.kod.GredPerkhidmatan;
import bph.entities.kod.Jawatan;
import bph.entities.kod.JenisPengenalan;
import bph.entities.kod.KelasPerkhidmatan;
import bph.entities.kod.StatusPekerjaan;

@Entity
@Table(name = "users_spouse")
public class UsersSpouse {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "user_login")
	private Users users;

	@Column(name = "nama_pasangan")
	private String namaPasangan;

	@ManyToOne
	@JoinColumn(name = "jenis_pengenalan")
	private JenisPengenalan jenisPengenalan;

	@Column(name = "no_kp_pasangan")
	private String noKPPasangan;

	@ManyToOne
	@JoinColumn(name = "status_pekerjaan_pasangan")
	private StatusPekerjaan statusPekerjaanPasangan;

	@Column(name = "jenis_pekerjaan")
	private String jenisPekerjaan;

	@Column(name = "gaji_pasangan")
	private Double gajiPasangan;

	@Column(name = "nama_syarikat")
	private String namaSyarikat;

	@Column(name = "alamat_pejabat_1")
	private String alamatPejabat1;

	@Column(name = "alamat_pejabat_2")
	private String alamatPejabat2;

	@Column(name = "alamat_pejabat_3")
	private String alamatPejabat3;

	@Column(name = "poskod_pejabat")
	private String poskodPejabat;

	@ManyToOne
	@JoinColumn(name = "bandar_pejabat")
	private Bandar bandarPejabat;

	@Column(name = "no_tel_pejabat")
	private String noTelPejabat;

	@Column(name = "no_faks_pejabat")
	private String noFaksPejabat;

	@Column(name = "no_tel_bimbit")
	private String noTelBimbit;

	@ManyToOne
	@JoinColumn(name = "id_jawatan")
	private Jawatan jawatan;

	@ManyToOne
	@JoinColumn(name = "id_gred_jawatan")
	private GredPerkhidmatan gredJawatan;

	@ManyToOne
	@JoinColumn(name = "id_kelas_perkhidmatan")
	private KelasPerkhidmatan kelasPerkhidmatan;

	@ManyToOne
	@JoinColumn(name = "id_agensi")
	private Agensi agensi;

	@ManyToOne
	@JoinColumn(name = "id_badan_berkanun")
	private BadanBerkanun badanBerkanun;

	public UsersSpouse() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	public String getNamaPasangan() {
		return namaPasangan;
	}

	public void setNamaPasangan(String namaPasangan) {
		this.namaPasangan = namaPasangan;
	}

	public JenisPengenalan getJenisPengenalan() {
		return jenisPengenalan;
	}

	public void setJenisPengenalan(JenisPengenalan jenisPengenalan) {
		this.jenisPengenalan = jenisPengenalan;
	}

	public String getNoKPPasangan() {
		return noKPPasangan;
	}

	public void setNoKPPasangan(String noKPPasangan) {
		this.noKPPasangan = noKPPasangan;
	}

	public StatusPekerjaan getStatusPekerjaanPasangan() {
		return statusPekerjaanPasangan;
	}

	public void setStatusPekerjaanPasangan(
			StatusPekerjaan statusPekerjaanPasangan) {
		this.statusPekerjaanPasangan = statusPekerjaanPasangan;
	}

	public String getJenisPekerjaan() {
		return jenisPekerjaan;
	}

	public void setJenisPekerjaan(String jenisPekerjaan) {
		this.jenisPekerjaan = jenisPekerjaan;
	}

	public Double getGajiPasangan() {
		return gajiPasangan;
	}

	public void setGajiPasangan(Double gajiPasangan) {
		this.gajiPasangan = gajiPasangan;
	}

	public String getNamaSyarikat() {
		return namaSyarikat;
	}

	public void setNamaSyarikat(String namaSyarikat) {
		this.namaSyarikat = namaSyarikat;
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

	public Bandar getBandarPejabat() {
		return bandarPejabat;
	}

	public void setBandarPejabat(Bandar bandarPejabat) {
		this.bandarPejabat = bandarPejabat;
	}

	public String getNoTelPejabat() {
		return noTelPejabat;
	}

	public void setNoTelPejabat(String noTelPejabat) {
		this.noTelPejabat = noTelPejabat;
	}

	public String getNoFaksPejabat() {
		return noFaksPejabat;
	}

	public void setNoFaksPejabat(String noFaksPejabat) {
		this.noFaksPejabat = noFaksPejabat;
	}

	public String getNoTelBimbit() {
		return noTelBimbit;
	}

	public void setNoTelBimbit(String noTelBimbit) {
		this.noTelBimbit = noTelBimbit;
	}

	public Jawatan getJawatan() {
		return jawatan;
	}

	public void setJawatan(Jawatan jawatan) {
		this.jawatan = jawatan;
	}

	public GredPerkhidmatan getGredJawatan() {
		return gredJawatan;
	}

	public void setGredJawatan(GredPerkhidmatan gredJawatan) {
		this.gredJawatan = gredJawatan;
	}

	public KelasPerkhidmatan getKelasPerkhidmatan() {
		return kelasPerkhidmatan;
	}

	public void setKelasPerkhidmatan(KelasPerkhidmatan kelasPerkhidmatan) {
		this.kelasPerkhidmatan = kelasPerkhidmatan;
	}

	public Agensi getAgensi() {
		return agensi;
	}

	public void setAgensi(Agensi agensi) {
		this.agensi = agensi;
	}

	public BadanBerkanun getBadanBerkanun() {
		return badanBerkanun;
	}

	public void setBadanBerkanun(BadanBerkanun badanBerkanun) {
		this.badanBerkanun = badanBerkanun;
	}

}