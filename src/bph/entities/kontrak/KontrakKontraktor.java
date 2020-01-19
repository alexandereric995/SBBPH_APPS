package bph.entities.kontrak;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import bph.entities.kod.Bandar;

@Entity
@Table(name = "kontrak_kontraktor")
public class KontrakKontraktor {

	@Id
	@Column(name = "no_pendaftaran")
	private String id;

	@Column(name = "nama_kontraktor")
	private String namaKontraktor;
	
	@Column(name = "kod_pembekal")
	private String kodPembekal;

	@Column(name = "nama_pemilik")
	private String namaPemilik;

	@Column(name = "alamat1")
	private String alamat1;

	@Column(name = "alamat2")
	private String alamat2;

	@Column(name = "alamat3")
	private String alamat3;

	@Column(name = "poskod")
	private int poskod;

	@ManyToOne
	@JoinColumn(name = "id_bandar")
	private Bandar bandar;

	@Column(name = "no_telefon")
	private String noTelefon;
	
	@Column(name = "no_telefon_bimbit")
	private String noTelefonBimbit;

	@Column(name = "no_faks")
	private String noFaks;
	
	@Column(name = "emel")
	private String emel;
	
	@Column(name = "catatan")
	private String catatan;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNamaKontraktor() {
		return namaKontraktor;
	}

	public void setNamaKontraktor(String namaKontraktor) {
		this.namaKontraktor = namaKontraktor;
	}

	public String getKodPembekal() {
		return kodPembekal;
	}

	public void setKodPembekal(String kodPembekal) {
		this.kodPembekal = kodPembekal;
	}

	public String getNamaPemilik() {
		return namaPemilik;
	}

	public void setNamaPemilik(String namaPemilik) {
		this.namaPemilik = namaPemilik;
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

	public int getPoskod() {
		return poskod;
	}

	public void setPoskod(int poskod) {
		this.poskod = poskod;
	}

	public Bandar getBandar() {
		return bandar;
	}

	public void setBandar(Bandar bandar) {
		this.bandar = bandar;
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

	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}
}
