package bph.entities.pembangunan;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lebah.template.UID;
import bph.entities.kod.JenisDokumen;

@Entity
@Table(name = "dev_dokumen")
public class DevDokumen {

	@Id
	@Column(name = "id")
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "id_hakmilik")
	private DevHakmilik hakmilik;
	
	@ManyToOne
	@JoinColumn(name = "id_premis")
	private DevPremis premis;
	
	@ManyToOne
	@JoinColumn(name = "id_penguatkuasaan")
	private DevPenguatkuasaan penguatkuasaan;
	
	@ManyToOne
	@JoinColumn(name = "id_jenis_dokumen")
	private JenisDokumen jenisDokumen;
	
	@Column(name = "tajuk")
	private String tajuk;
	
	@Column(name = "keterangan")
	private String keterangan;

	@Column(name = "photofilename")
	private String photofilename;

	@Column(name = "thumbfilename")
	private String thumbfilename;
	

	public DevDokumen() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DevHakmilik getHakmilik() {
		return hakmilik;
	}

	public void setHakmilik(DevHakmilik hakmilik) {
		this.hakmilik = hakmilik;
	}

	public DevPremis getPremis() {
		return premis;
	}

	public void setPremis(DevPremis premis) {
		this.premis = premis;
	}

	public JenisDokumen getJenisDokumen() {
		return jenisDokumen;
	}

	public void setJenisDokumen(JenisDokumen jenisDokumen) {
		this.jenisDokumen = jenisDokumen;
	}

	public String getTajuk() {
		return tajuk;
	}

	public void setTajuk(String tajuk) {
		this.tajuk = tajuk;
	}

	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}

	public String getPhotofilename() {
		return photofilename;
	}

	public void setPhotofilename(String photofilename) {
		this.photofilename = photofilename;
	}

	public String getThumbfilename() {
		return thumbfilename;
	}

	public void setThumbfilename(String thumbfilename) {
		this.thumbfilename = thumbfilename;
	}

	public DevPenguatkuasaan getPenguatkuasaan() {
		return penguatkuasaan;
	}

	public void setPenguatkuasaan(DevPenguatkuasaan penguatkuasaan) {
		this.penguatkuasaan = penguatkuasaan;
	}
}
