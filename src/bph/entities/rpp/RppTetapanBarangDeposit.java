package bph.entities.rpp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lebah.template.UID;

@Entity
@Table(name="rpp_tetapan_barang_deposit")
public class RppTetapanBarangDeposit {

	@Id
	@Column(name = "id")
	private String id;
	
	@Column(name = "nama")
	private String nama;
	
	@Column(name = "harga")
	private Double harga;
	
	@Column(name = "keterangan")
	private String keterangan;
	
	@Column(name = "flag_kategori")
	private String flagKategori;
	
	public RppTetapanBarangDeposit() {
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

	public Double getHarga() {
		return harga;
	}

	public void setHarga(Double harga) {
		this.harga = harga;
	}

	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}

	public String getFlagKategori() {
		return flagKategori;
	}

	public void setFlagKategori(String flagKategori) {
		this.flagKategori = flagKategori;
	}
	
}
