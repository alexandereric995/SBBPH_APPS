package bph.entities.qtr;

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
import portal.module.entity.Users;
import bph.entities.kod.GelaranDalamSurat;
import bph.entities.kod.JenisPenolakan;
import bph.entities.kod.SebabPenolakan;

@Entity
@Table(name = "kua_tawaran")
public class KuaTawaran {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_agihan")
	private KuaAgihan agihan;

	@ManyToOne
	@JoinColumn(name = "id_pegawai")
	private Users pegawai;

	@ManyToOne
	@JoinColumn(name = "id_jenis_penolakan")
	private JenisPenolakan jenisPenolakan;

	@ManyToOne
	@JoinColumn(name = "id_sebab_penolakan")
	private SebabPenolakan sebabPenolakan;

	@Column(name = "tarikh_masuk")
	@Temporal(TemporalType.TIMESTAMP)
	private Date tarikhMasuk;

	@Column(name = "tarikh_kemaskini")
	@Temporal(TemporalType.TIMESTAMP)
	private Date tarikhKemaskini;

	// @ManyToOne
	// @JoinColumn(name = "nama_petugas")
	// private Users petugas;

	@Column(name = "tarikh_surat")
	@Temporal(TemporalType.DATE)
	private Date tarikhSurat;

	@Column(name = "no_fail")
	private String noFail;

	@Column(name = "status_tawaran")
	private String statusTawaran;

	@Column(name = "cetak_surat")
	private String cetakSurat;

	@Column(name = "tarikh_surat_sebenar")
	@Temporal(TemporalType.DATE)
	private Date tarikhSuratSebenar;

	@Column(name = "tarikh_surat_diterima")
	@Temporal(TemporalType.DATE)
	private Date tarikhSuratDiterima;

	@Column(name = "catatan")
	private String catatan;

	@ManyToOne
	@JoinColumn(name = "title_dalam_surat")
	private GelaranDalamSurat titleDalamSurat;

	@Column(name = "kepada")
	private String kepada;

	@Column(name = "bil")
	private String bil;

	public int getFlagSelesai() {
		return flagSelesai;
	}

	public void setFlagSelesai(int flagSelesai) {
		this.flagSelesai = flagSelesai;
	}

	@Column(name = "generate_email")
	private String generateEmail;

	@Column(name = "flag_selesai")
	private int flagSelesai;

	@Column(name = "flag_hantar")
	private int flagHantar;

	public int getFlagHantar() {
		return flagHantar;
	}

	public void setFlagHantar(int flagHantar) {
		this.flagHantar = flagHantar;
	}

	@Column(name = "flag_semakan_pelulus")
	private int flagSemakanPelulus;

	@Column(name = "nama_kementerian")
	private String namaKementerian;

	public int getFlagSemakanPelulus() {
		return flagSemakanPelulus;
	}

	public void setFlagSemakanPelulus(int flagSemakanPelulus) {
		this.flagSemakanPelulus = flagSemakanPelulus;
	}

	@Column(name = "nama_jabatan")
	private String namaJabatan;

	@Column(name = "nama_bahagian")
	private String namaBahagian;

	@Column(name = "nama_alamat1")
	private String namaAlamat1;

	@Column(name = "nama_alamat2")
	private String namaAlamat2;

	@Column(name = "nama_alamat3")
	private String namaAlamat3;

	@Column(name = "nama_poskod")
	private String namaPoskod;

	@Column(name = "nama_negeri")
	private String namaNegeri;

	public String getNamaKementerian() {
		return namaKementerian;
	}

	public void setNamaKementerian(String namaKementerian) {
		this.namaKementerian = namaKementerian;
	}

	public String getNamaJabatan() {
		return namaJabatan;
	}

	public void setNamaJabatan(String namaJabatan) {
		this.namaJabatan = namaJabatan;
	}

	public String getNamaBahagian() {
		return namaBahagian;
	}

	public void setNamaBahagian(String namaBahagian) {
		this.namaBahagian = namaBahagian;
	}

	public String getNamaAlamat1() {
		return namaAlamat1;
	}

	public void setNamaAlamat1(String namaAlamat1) {
		this.namaAlamat1 = namaAlamat1;
	}

	public String getNamaAlamat2() {
		return namaAlamat2;
	}

	public void setNamaAlamat2(String namaAlamat2) {
		this.namaAlamat2 = namaAlamat2;
	}

	public String getNamaAlamat3() {
		return namaAlamat3;
	}

	public void setNamaAlamat3(String namaAlamat3) {
		this.namaAlamat3 = namaAlamat3;
	}

	public String getNamaPoskod() {
		return namaPoskod;
	}

	public void setNamaPoskod(String namaPoskod) {
		this.namaPoskod = namaPoskod;
	}

	public String getNamaNegeri() {
		return namaNegeri;
	}

	public void setNamaNegeri(String namaNegeri) {
		this.namaNegeri = namaNegeri;
	}

	public String getNamaBandar() {
		return namaBandar;
	}

	public void setNamaBandar(String namaBandar) {
		this.namaBandar = namaBandar;
	}

	@Column(name = "nama_bandar")
	private String namaBandar;
	@ManyToOne
	@JoinColumn(name = "id_masuk")
	private Users idMasuk;

	@ManyToOne
	@JoinColumn(name = "id_kemaskini")
	private Users idKemaskini;

	public KuaTawaran() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public KuaAgihan getAgihan() {
		return agihan;
	}

	public void setAgihan(KuaAgihan agihan) {
		this.agihan = agihan;
	}

	public JenisPenolakan getJenisPenolakan() {
		return jenisPenolakan;
	}

	public void setJenisPenolakan(JenisPenolakan jenisPenolakan) {
		this.jenisPenolakan = jenisPenolakan;
	}

	public SebabPenolakan getSebabPenolakan() {
		return sebabPenolakan;
	}

	public void setSebabPenolakan(SebabPenolakan sebabPenolakan) {
		this.sebabPenolakan = sebabPenolakan;
	}

	public Date getTarikhMasuk() {
		return tarikhMasuk;
	}

	public void setTarikhMasuk(Date tarikhMasuk) {
		this.tarikhMasuk = tarikhMasuk;
	}

	public Date getTarikhKemaskini() {
		return tarikhKemaskini;
	}

	public void setTarikhKemaskini(Date tarikhKemaskini) {
		this.tarikhKemaskini = tarikhKemaskini;
	}

	// public Users getPetugas() {
	// return petugas;
	// }
	//
	// public void setPetugas(Users petugas) {
	// this.petugas = petugas;
	// }

	public Date getTarikhSurat() {
		return tarikhSurat;
	}

	public void setTarikhSurat(Date tarikhSurat) {
		this.tarikhSurat = tarikhSurat;
	}

	public String getNoFail() {
		return noFail;
	}

	public void setNoFail(String noFail) {
		this.noFail = noFail;
	}

	public String getStatusTawaran() {
		return statusTawaran;
	}

	public void setStatusTawaran(String statusTawaran) {
		this.statusTawaran = statusTawaran;
	}

	public String getCetakSurat() {
		return cetakSurat;
	}

	public void setCetakSurat(String cetakSurat) {
		this.cetakSurat = cetakSurat;
	}

	public Date getTarikhSuratSebenar() {
		return tarikhSuratSebenar;
	}

	public void setTarikhSuratSebenar(Date tarikhSuratSebenar) {
		this.tarikhSuratSebenar = tarikhSuratSebenar;
	}

	public Date getTarikhSuratDiterima() {
		return tarikhSuratDiterima;
	}

	public void setTarikhSuratDiterima(Date tarikhSuratDiterima) {
		this.tarikhSuratDiterima = tarikhSuratDiterima;
	}

	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}

	public GelaranDalamSurat getTitleDalamSurat() {
		return titleDalamSurat;
	}

	public void setTitleDalamSurat(GelaranDalamSurat titleDalamSurat) {
		this.titleDalamSurat = titleDalamSurat;
	}

	public String getKepada() {
		return kepada;
	}

	public void setKepada(String kepada) {
		this.kepada = kepada;
	}

	public String getBil() {
		return bil;
	}

	public void setBil(String bil) {
		this.bil = bil;
	}

	public String getGenerateEmail() {
		return generateEmail;
	}

	public void setGenerateEmail(String generateEmail) {
		this.generateEmail = generateEmail;
	}

	public String getDescGenEmail() {
		String gE = "";

		if ("Y".equals(getGenerateEmail())) {
			gE = "Ya";
		} else {
			gE = "Tidak";
		}

		return gE;
	}

	public void setPegawai(Users pegawai) {
		this.pegawai = pegawai;
	}

	public Users getPegawai() {
		return pegawai;
	}

	@Column(name = "status_dalaman")
	private String statusDalaman;

	public String getStatusDalaman() {
		return statusDalaman;
	}

	public void setStatusDalaman(String statusDalaman) {
		this.statusDalaman = statusDalaman;
	}

	public Users getIdMasuk() {
		return idMasuk;
	}

	public void setIdMasuk(Users idMasuk) {
		this.idMasuk = idMasuk;
	}

	public Users getIdKemaskini() {
		return idKemaskini;
	}

	public void setIdKemaskini(Users idKemaskini) {
		this.idKemaskini = idKemaskini;
	}

}
