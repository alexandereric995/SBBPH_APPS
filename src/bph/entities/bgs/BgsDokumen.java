package bph.entities.bgs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lebah.template.UID;
import bph.entities.kod.JenisDokumen;

@Entity
@Table(name = "bgs_dokumen")
public class BgsDokumen {

	@Id
	@Column(name = "id")
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "id_permohonan")
	private BgsPermohonan permohonan;

	@ManyToOne
	@JoinColumn(name = "id_permohonan_ubahsuai")
	private BgsPermohonanUbahSuai permohonanUbahsuai;
	
	@ManyToOne
	@JoinColumn(name = "id_aras")
	private BgsAras aras;
	
	@ManyToOne
	@JoinColumn(name = "id_jenis_dokumen")
	private JenisDokumen jenisDokumen;

	@Column(name = "photofilename")
	private String photofilename;

	@Column(name = "thumbfilename")
	private String thumbfilename;	
	
	@Column(name = "tajuk")
	private String tajuk;	

	@Column(name = "keterangan")
	private String keterangan;

	public BgsDokumen() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BgsPermohonan getPermohonan() {
		return permohonan;
	}

	public void setPermohonan(BgsPermohonan permohonan) {
		this.permohonan = permohonan;
	}

	public BgsPermohonanUbahSuai getPermohonanUbahsuai() {
		return permohonanUbahsuai;
	}

	public void setPermohonanUbahsuai(BgsPermohonanUbahSuai permohonanUbahsuai) {
		this.permohonanUbahsuai = permohonanUbahsuai;
	}

	public BgsAras getAras() {
		return aras;
	}

	public void setAras(BgsAras aras) {
		this.aras = aras;
	}

	public JenisDokumen getJenisDokumen() {
		return jenisDokumen;
	}

	public void setJenisDokumen(JenisDokumen jenisDokumen) {
		this.jenisDokumen = jenisDokumen;
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
}
