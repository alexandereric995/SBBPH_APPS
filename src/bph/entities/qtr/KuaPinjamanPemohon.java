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
import bph.entities.kod.Bandar;

@Entity
@Table(name = "kua_pinjaman_pemohon")
public class KuaPinjamanPemohon {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_pemohon")
	private Users users;

	@Column(name = "pinjaman_perumahan")
	private int pinjamanPerumahan;

	@Column(name = "status_pembinaan")
	private int statusPembinaan;

	@Column(name = "tarikh_jangka_siap")
	@Temporal(TemporalType.DATE)
	private Date tarikhJangkaSiap;

	@Column(name = "alamat1")
	private String alamat1;

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

	@Column(name = "alamat2")
	private String alamat2;

	@Column(name = "alamat3")
	private String alamat3;

	@Column(name = "poskod_pinjaman")
	private String poskodPinjaman;

	@ManyToOne
	@JoinColumn(name = "id_bandar")
	private Bandar bandar;

	@Column(name = "jenis_perumahan")
	private String jenisPerumahan;

	@Column(name = "pembiayaan")
	private String pembiayaan;

	@Column(name = "pembelian")
	private String pembelian;

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

	public KuaPinjamanPemohon() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());
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

	public int getPinjamanPerumahan() {
		return pinjamanPerumahan;
	}

	public void setPinjamanPerumahan(int pinjamanPerumahan) {
		this.pinjamanPerumahan = pinjamanPerumahan;
	}

	public int getStatusPembinaan() {
		return statusPembinaan;
	}

	public void setStatusPembinaan(int statusPembinaan) {
		this.statusPembinaan = statusPembinaan;
	}

	public Date getTarikhJangkaSiap() {
		return tarikhJangkaSiap;
	}

	public void setTarikhJangkaSiap(Date tarikhJangkaSiap) {
		this.tarikhJangkaSiap = tarikhJangkaSiap;
	}

	public String getPoskodPinjaman() {
		return poskodPinjaman;
	}

	public void setPoskodPinjaman(String poskodPinjaman) {
		this.poskodPinjaman = poskodPinjaman;
	}

	public Bandar getBandar() {
		return bandar;
	}

	public void setBandar(Bandar bandar) {
		this.bandar = bandar;
	}

	public String getJenisPerumahan() {
		return jenisPerumahan;
	}

	public void setJenisPerumahan(String jenisPerumahan) {
		this.jenisPerumahan = jenisPerumahan;
	}

	public String getPembiayaan() {
		return pembiayaan;
	}

	public void setPembiayaan(String pembiayaan) {
		this.pembiayaan = pembiayaan;
	}

	public String getPembelian() {
		return pembelian;
	}

	public void setPembelian(String pembelian) {
		this.pembelian = pembelian;
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
