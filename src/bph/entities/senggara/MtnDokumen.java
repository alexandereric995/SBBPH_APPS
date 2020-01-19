package bph.entities.senggara;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lebah.template.UID;
import bph.entities.kod.JenisDokumen;

@Entity
@Table(name = "mtn_dokumen")
public class MtnDokumen {

	@Id
	@Column(name = "id")
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "no_pendaftaran")
	private MtnKontraktor kontraktor;

	@ManyToOne
	@JoinColumn(name = "id_jenis_dokumen")
	private JenisDokumen jenisDokumen;

	@Column(name = "keterangan")
	private String keterangan;

	@Column(name = "photofilename")
	private String photofilename;

	@Column(name = "tajuk")
	private String tajuk;

	@Column(name = "thumbfilename")
	private String thumbfilename;

	public MtnDokumen() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getTajuk() {
		return tajuk;
	}

	public void setTajuk(String tajuk) {
		this.tajuk = tajuk;
	}

	public String getThumbfilename() {
		return thumbfilename;
	}

	public void setThumbfilename(String thumbfilename) {
		this.thumbfilename = thumbfilename;
	}

	public MtnKontraktor getKontraktor() {
		return kontraktor;
	}

	public void setKontraktor(MtnKontraktor kontraktor) {
		this.kontraktor = kontraktor;
	}

	public JenisDokumen getJenisDokumen() {
		return jenisDokumen;
	}

	public void setJenisDokumen(JenisDokumen jenisDokumen) {
		this.jenisDokumen = jenisDokumen;
	}
}
