package bph.entities.utiliti;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lebah.template.UID;
import bph.entities.kod.JenisDokumen;

@Entity
@Table(name = "util_dokumen")
public class UtilDokumen {

	@Id
	@Column(name = "id")
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "id_tempahan")
	private UtilPermohonan tempahan;
	
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

	public UtilDokumen() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public void setTempahan(UtilPermohonan tempahan) {
		this.tempahan = tempahan;
	}

	public UtilPermohonan getTempahan() {
		return tempahan;
	}
}
