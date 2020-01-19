package bph.entities.pro;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lebah.template.UID;

@Entity
@Table(name = "pro_kategori_teknikal")
public class ProKategoriTeknikal {

	@Id
	@Column(name = "id")
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "id_aduan")
	private ProAduan aduan;
	
	@ManyToOne
	@JoinColumn(name = "id_ruj_keterangan_teknikal")
	private ProKeteranganTeknikal idKeteranganTeknikal;
	
	@Column(name = "kategori")
	private String kategori;
	
	public ProKategoriTeknikal() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ProAduan getAduan() {
		return aduan;
	}

	public void setAduan(ProAduan aduan) {
		this.aduan = aduan;
	}

	public ProKeteranganTeknikal getIdKeteranganTeknikal() {
		return idKeteranganTeknikal;
	}

	public void setIdKeteranganTeknikal(ProKeteranganTeknikal idKeteranganTeknikal) {
		this.idKeteranganTeknikal = idKeteranganTeknikal;
	}

	public String getKategori() {
		return kategori;
	}

	public void setKategori(String kategori) {
		this.kategori = kategori;
	}

}
