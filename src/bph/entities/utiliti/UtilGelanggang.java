package bph.entities.utiliti;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lebah.template.UID;
import bph.entities.kod.Bandar;

@Entity
@Table(name = "util_gelanggang")
public class UtilGelanggang {

	@Id
	@Column(name = "id")
	private String id;
	
	@Column(name = "nama")
	private String nama;
	
	@Column(name = "alamat1")
	private String alamat1;
	
	@Column(name = "alamat2")
	private String alamat2;
	
	@Column(name = "alamat3")
	private String alamat3;
	
	@Column(name = "poskod")
	private String poskod;
	
	@Column(name = "lokasi")
	private String lokasi;
	
	@ManyToOne
	@JoinColumn(name = "bandar")
	private Bandar bandar;

	@ManyToOne
	@JoinColumn(name = "id_dewan")
	private UtilDewan dewan;
	
	@Column(name = "catatan")
	private String catatan;
	
	@Column(name = "kadar_sewa_siang")
	private double kadarSewa;
	
	@Column(name = "kadar_sewa_malam")
	private double kadarSewaAwam;
	
	@Column(name = "waktu_buka")
	private String waktuBuka;
	
	@Column(name = "waktu_tutup")
	private String waktuTutup;
	
	@Column(name = "waktu_buka_siang")
	private String waktuBukaSiang;
	
	@Column(name = "waktu_tutup_siang")
	private String waktuTutupSiang;
	
	@Column(name = "status")
	private String status;

	@Column(name = "waktu_buka_malam")
	private String waktuBukaMalam;
	
	@Column(name = "waktu_tutup_malam")
	private String waktuTutupMalam;
	
	public UtilGelanggang() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
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

	public String getLokasi() {
		return lokasi;
	}

	public void setLokasi(String lokasi) {
		this.lokasi = lokasi;
	}

	public Bandar getBandar() {
		return bandar;
	}

	public void setBandar(Bandar bandar) {
		this.bandar = bandar;
	}

	public UtilDewan getDewan() {
		return dewan;
	}

	public void setDewan(UtilDewan dewan) {
		this.dewan = dewan;
	}

	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}

	public double getKadarSewa() {
		return kadarSewa;
	}

	public void setKadarSewa(double kadarSewa) {
		this.kadarSewa = kadarSewa;
	}

	public double getKadarSewaAwam() {
		return kadarSewaAwam;
	}

	public void setKadarSewaAwam(double kadarSewaAwam) {
		this.kadarSewaAwam = kadarSewaAwam;
	}

	public String getWaktuBuka() {
		return waktuBuka;
	}

	public void setWaktuBuka(String waktuBuka) {
		this.waktuBuka = waktuBuka;
	}

	public String getWaktuTutup() {
		return waktuTutup;
	}

	public void setWaktuTutup(String waktuTutup) {
		this.waktuTutup = waktuTutup;
	}

	public String getWaktuBukaSiang() {
		return waktuBukaSiang;
	}

	public void setWaktuBukaSiang(String waktuBukaSiang) {
		this.waktuBukaSiang = waktuBukaSiang;
	}

	public String getWaktuTutupSiang() {
		return waktuTutupSiang;
	}

	public void setWaktuTutupSiang(String waktuTutupSiang) {
		this.waktuTutupSiang = waktuTutupSiang;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getWaktuBukaMalam() {
		return waktuBukaMalam;
	}

	public void setWaktuBukaMalam(String waktuBukaMalam) {
		this.waktuBukaMalam = waktuBukaMalam;
	}

	public String getWaktuTutupMalam() {
		return waktuTutupMalam;
	}

	public void setWaktuTutupMalam(String waktuTutupMalam) {
		this.waktuTutupMalam = waktuTutupMalam;
	}
}