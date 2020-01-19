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
	
	public KuaPinjamanPemohon() {
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

}
