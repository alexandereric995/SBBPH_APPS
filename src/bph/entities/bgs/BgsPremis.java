package bph.entities.bgs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lebah.template.UID;
import bph.entities.pembangunan.DevPremis;

@Entity
@Table(name = "bgs_premis")
public class BgsPremis {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_premis")
	private DevPremis premis;
	
	@Column(name = "no_fail")
	private String noFail;
	
	@Column(name = "nama_konsesi")
	private String namaKonsesi;
	
	@Column(name = "nama_pegawai")
	private String namaPegawai;

	@Column(name = "no_telefon")
	private String noTelefon;
	
	@Column(name = "no_faks")
	private String noFaks;
	
	@Column(name = "emel")
	private String emel;
	
	@Column(name = "catatan")
	private String catatan;

	public BgsPremis() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DevPremis getPremis() {
		return premis;
	}

	public void setPremis(DevPremis premis) {
		this.premis = premis;
	}

	public String getNoFail() {
		return noFail;
	}

	public void setNoFail(String noFail) {
		this.noFail = noFail;
	}

	public String getNamaKonsesi() {
		return namaKonsesi;
	}

	public void setNamaKonsesi(String namaKonsesi) {
		this.namaKonsesi = namaKonsesi;
	}

	public String getNamaPegawai() {
		return namaPegawai;
	}

	public void setNamaPegawai(String namaPegawai) {
		this.namaPegawai = namaPegawai;
	}

	public String getNoTelefon() {
		return noTelefon;
	}

	public void setNoTelefon(String noTelefon) {
		this.noTelefon = noTelefon;
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
