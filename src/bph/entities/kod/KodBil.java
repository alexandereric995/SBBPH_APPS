package bph.entities.kod;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lebah.template.UID;



@Entity
@Table(name = "ruj_kod_bil")
public class KodBil {

	@Id
	@Column(name = "id")
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "id_kategori")
	private KategoriBil kategori;
	
	@Column(name = "kod")
	private String kod;
	
	@Column(name = "keterangan")
	private String keterangan;
	
	@Column(name = "catatan")
	private String catatan;
	
	public KodBil() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public KategoriBil getKategori() {
		return kategori;
	}

	public void setKategori(KategoriBil kategori) {
		this.kategori = kategori;
	}

	public String getKod() {
		return kod;
	}

	public void setKod(String kod) {
		this.kod = kod;
	}

	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}

	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}

	

	
}
