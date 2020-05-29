package bph.entities.integrasi;

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

@Entity
@Table(name = "int_jpn")
public class IntJPN {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "kod_agensi")
	private String kodAgensi;

	@Column(name = "kod_cawangan")
	private String kodCawangan;

	@Column(name = "login_pengguna")
	private String loginPengguna;

	@Column(name = "kod_transaksi")
	private String kodTransaksi;

	@Column(name = "tarikh_hantar")
	@Temporal(TemporalType.TIMESTAMP)
	private Date tarikhHantar;

	@Column(name = "no_pengenalan")
	private String noPengenalan;

	@Column(name = "flag_hantar")
	private String flagHantar;

	@Column(name = "tarikh_terima")
	@Temporal(TemporalType.TIMESTAMP)
	private Date tarikhTerima;

	@Column(name = "flag_jawapan")
	private String flagJawapan;

	@Column(name = "kod_mesej")
	private String kodMesej;

	@Column(name = "mesej")
	private String mesej;

	@Column(name = "nama")
	private String nama;

	@Column(name = "tarikh_lahir")
	@Temporal(TemporalType.DATE)
	private Date tarikhLahir;

	@Column(name = "kod_jantina")
	private String kodJantina;

	@Column(name = "kod_bangsa")
	private String kodBangsa;

	@Column(name = "kod_agama")
	private String kodAgama;

	@Column(name = "alamat1")
	private String alamat1;

	@Column(name = "alamat2")
	private String alamat2;

	@Column(name = "alamat3")
	private String alamat3;

	@Column(name = "poskod")
	private String poskod;

	@Column(name = "kod_bandar")
	private String kodBandar;

	@Column(name = "kod_negeri")
	private String kodNegeri;

	@Column(name = "tarikh_mati")
	@Temporal(TemporalType.DATE)
	private Date tarikhMati;

	@Column(name = "no_pengenalan_lama")
	private String noPengenalanLama;

	@Column(name = "taraf_penduduk")
	private String tarafPenduduk;

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

	public IntJPN() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());
	}

	public String getKeteranganTarafPenduduk() {
		String keterangan = "";
		if (this.tarafPenduduk != null) {
			if ("B".equals(this.tarafPenduduk)) {
				keterangan = "WARGANEGARA";
			} else if ("C".equals(this.tarafPenduduk)) {
				keterangan = "WARGANEGARA (AMJ)";
			} else if ("H".equals(this.tarafPenduduk)) {
				keterangan = "PEMASTAUTIN SEMENTARA";
			} else if ("M".equals(this.tarafPenduduk)) {
				keterangan = "PEMASTAUTIN TETAP";
			} else if ("P".equals(this.tarafPenduduk)) {
				keterangan = "PEMASTAUTIN TETAP (AMJ)";
			} else if ("Q".equals(this.tarafPenduduk)) {
				keterangan = "BELUM DITENTUKAN";
			} else if ("X".equals(this.tarafPenduduk)) {
				keterangan = "BUKAN WARGANEGARA";
			}
		}
		return keterangan;
	}

	public String getKeteranganFlagJawapan() {
		String keterangan = "";
		if (this.flagJawapan != null) {
			if ("0".equals(this.flagJawapan)) {
				keterangan = "RALAT";
			} else if ("1".equals(this.flagJawapan)) {
				keterangan = "BERJAYA";
			} else if ("2".equals(this.flagJawapan)) {
				keterangan = "BERJAYA DENGAN PERINGATAN";
			}
		}
		return keterangan;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKodAgensi() {
		return kodAgensi;
	}

	public void setKodAgensi(String kodAgensi) {
		this.kodAgensi = kodAgensi;
	}

	public String getKodCawangan() {
		return kodCawangan;
	}

	public void setKodCawangan(String kodCawangan) {
		this.kodCawangan = kodCawangan;
	}

	public String getLoginPengguna() {
		return loginPengguna;
	}

	public void setLoginPengguna(String loginPengguna) {
		this.loginPengguna = loginPengguna;
	}

	public String getKodTransaksi() {
		return kodTransaksi;
	}

	public void setKodTransaksi(String kodTransaksi) {
		this.kodTransaksi = kodTransaksi;
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

	public String getFlagHantar() {
		return flagHantar;
	}

	public void setFlagHantar(String flagHantar) {
		this.flagHantar = flagHantar;
	}

	public Date getTarikhTerima() {
		return tarikhTerima;
	}

	public void setTarikhTerima(Date tarikhTerima) {
		this.tarikhTerima = tarikhTerima;
	}

	public String getFlagJawapan() {
		return flagJawapan;
	}

	public void setFlagJawapan(String flagJawapan) {
		this.flagJawapan = flagJawapan;
	}

	public String getKodMesej() {
		return kodMesej;
	}

	public void setKodMesej(String kodMesej) {
		this.kodMesej = kodMesej;
	}

	public String getMesej() {
		return mesej;
	}

	public void setMesej(String mesej) {
		this.mesej = mesej;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public Date getTarikhLahir() {
		return tarikhLahir;
	}

	public void setTarikhLahir(Date tarikhLahir) {
		this.tarikhLahir = tarikhLahir;
	}

	public String getKodJantina() {
		return kodJantina;
	}

	public void setKodJantina(String kodJantina) {
		this.kodJantina = kodJantina;
	}

	public String getKodBangsa() {
		return kodBangsa;
	}

	public void setKodBangsa(String kodBangsa) {
		this.kodBangsa = kodBangsa;
	}

	public String getKodAgama() {
		return kodAgama;
	}

	public void setKodAgama(String kodAgama) {
		this.kodAgama = kodAgama;
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

	public String getKodBandar() {
		return kodBandar;
	}

	public void setKodBandar(String kodBandar) {
		this.kodBandar = kodBandar;
	}

	public String getKodNegeri() {
		return kodNegeri;
	}

	public void setKodNegeri(String kodNegeri) {
		this.kodNegeri = kodNegeri;
	}

	public Date getTarikhMati() {
		return tarikhMati;
	}

	public void setTarikhMati(Date tarikhMati) {
		this.tarikhMati = tarikhMati;
	}

	public String getNoPengenalanLama() {
		return noPengenalanLama;
	}

	public void setNoPengenalanLama(String noPengenalanLama) {
		this.noPengenalanLama = noPengenalanLama;
	}

	public String getTarafPenduduk() {
		return tarafPenduduk;
	}

	public void setTarafPenduduk(String tarafPenduduk) {
		this.tarafPenduduk = tarafPenduduk;
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
