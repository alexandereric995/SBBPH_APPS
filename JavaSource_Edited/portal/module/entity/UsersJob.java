package portal.module.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lebah.template.UID;
import bph.entities.kod.Agensi;
import bph.entities.kod.BadanBerkanun;
import bph.entities.kod.Bandar;
import bph.entities.kod.GredPerkhidmatan;
import bph.entities.kod.Jawatan;
import bph.entities.kod.JenisPerkhidmatan;
import bph.entities.kod.KelasPerkhidmatan;
import bph.entities.kod.StatusPerkhidmatan;

@Entity
@Table(name = "users_job")
public class UsersJob {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "user_login")
	private Users users;

	@ManyToOne
	@JoinColumn(name = "id_jawatan")
	private Jawatan jawatan;

	@ManyToOne
	@JoinColumn(name = "id_gred_jawatan")
	private GredPerkhidmatan gredJawatan;

	// @Column(name = "id_gred_jawatan", updatable = false, insertable = false)
	// private String idGredJawatan;

	@ManyToOne
	@JoinColumn(name = "id_jenis_perkhidmatan")
	private JenisPerkhidmatan jenisPerkhidmatan;

	@ManyToOne
	@JoinColumn(name = "id_kelas_perkhidmatan")
	private KelasPerkhidmatan kelasPerkhidmatan;

	@ManyToOne
	@JoinColumn(name = "id_status_perkhidmatan")
	private StatusPerkhidmatan statusPerkhidmatan;

	@ManyToOne
	@JoinColumn(name = "id_agensi")
	private Agensi agensi;

	@Column(name = "bahagian")
	private String bahagian;

	@ManyToOne
	@JoinColumn(name = "id_badan_berkanun")
	private BadanBerkanun badanBerkanun;

	@Column(name = "tarikh_lantikan")
	@Temporal(TemporalType.DATE)
	private Date tarikhLantikan;

	@Column(name = "tarikh_tamat")
	@Temporal(TemporalType.DATE)
	private Date tarikhTamat;

	@Column(name = "no_gaji")
	private String noGaji;

	@Column(name = "gaji_pokok")
	private Double gajiPokok;

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

	@Column(name = "no_tel_pejabat")
	private String noTelPejabat;

	@Column(name = "no_faks")
	private String noFaks;

	@Column(name = "emel")
	private String emel;

	@Column(name = "flag_itp")
	private int flagITP;

	@Column(name = "flag_epw")
	private int flagEPW;

	@Column(name = "flag_cola")
	private int flagCola;

	@Column(name = "tarikh_bersara")
	@Temporal(TemporalType.DATE)
	private Date tarikhBersara;

	public UsersJob() {
		setId(UID.getUID());
	}

	public String getDescCola() {
		String x = "";

		if (getFlagCola() == 0) {
			x = "Tidak";
		} else {
			x = "Ya";
		}
		return x;
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

	// public String getIdGredJawatan() {
	// return idGredJawatan;
	// }
	//
	// public void setIdGredJawatan(String idGredJawatan) {
	// this.idGredJawatan = idGredJawatan;
	// }

	public JenisPerkhidmatan getJenisPerkhidmatan() {
		return jenisPerkhidmatan;
	}

	public void setJenisPerkhidmatan(JenisPerkhidmatan jenisPerkhidmatan) {
		this.jenisPerkhidmatan = jenisPerkhidmatan;
	}

	public KelasPerkhidmatan getKelasPerkhidmatan() {
		return kelasPerkhidmatan;
	}

	public void setKelasPerkhidmatan(KelasPerkhidmatan kelasPerkhidmatan) {
		this.kelasPerkhidmatan = kelasPerkhidmatan;
	}

	public StatusPerkhidmatan getStatusPerkhidmatan() {
		return statusPerkhidmatan;
	}

	public void setStatusPerkhidmatan(StatusPerkhidmatan statusPerkhidmatan) {
		this.statusPerkhidmatan = statusPerkhidmatan;
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

	public Date getTarikhLantikan() {
		return tarikhLantikan;
	}

	public void setTarikhLantikan(Date tarikhLantikan) {
		this.tarikhLantikan = tarikhLantikan;
	}

	public Date getTarikhTamat() {
		return tarikhTamat;
	}

	public void setTarikhTamat(Date tarikhTamat) {
		this.tarikhTamat = tarikhTamat;
	}

	public String getNoGaji() {
		return noGaji;
	}

	public void setNoGaji(String noGaji) {
		this.noGaji = noGaji;
	}

	public Double getGajiPokok() {
		return gajiPokok;
	}

	public void setGajiPokok(Double gajiPokok) {
		this.gajiPokok = gajiPokok;
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

	public String getNoTelPejabat() {
		return noTelPejabat;
	}

	public void setNoTelPejabat(String noTelPejabat) {
		this.noTelPejabat = noTelPejabat;
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

	public int getFlagITP() {
		return flagITP;
	}

	public void setFlagITP(int flagITP) {
		this.flagITP = flagITP;
	}

	public int getFlagEPW() {
		return flagEPW;
	}

	public void setFlagEPW(int flagEPW) {
		this.flagEPW = flagEPW;
	}

	public int getFlagCola() {
		return flagCola;
	}

	public void setFlagCola(int flagCola) {
		this.flagCola = flagCola;
	}

	public Date getTarikhBersara() {
		return tarikhBersara;
	}

	public void setTarikhBersara(Date tarikhBersara) {
		this.tarikhBersara = tarikhBersara;
	}
}