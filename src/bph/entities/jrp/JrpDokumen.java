package bph.entities.jrp;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lebah.template.UID;
import bph.entities.kod.JenisDokumen;

@Entity
@Table(name = "jrp_dokumen")
public class JrpDokumen {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "photofilename")
	private String photofilename;

	@Column(name = "tempId")
	private String tempId;

	@Column(name = "thumbfilename")
	private String thumbfilename;

	@ManyToOne
	@JoinColumn(name = "id_permohonan")
	private JrpPermohonan permohonan;

	@ManyToOne
	@JoinColumn(name = "id_ulasan_teknikal")
	private JrpUlasanTeknikal ulasanTeknikal;
	
	@Column(name = "tajuk")
	private String tajuk;

	@ManyToOne
	@JoinColumn(name = "id_jenis_dokumen")
	private JenisDokumen jenisDokumen;

	@Column(name = "keterangan")
	private String keterangan;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_muatnaik")
	private Date tarikhMuatnaik;

	public JrpDokumen() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPhotofilename() {
		return photofilename;
	}

	public void setPhotofilename(String photofilename) {
		this.photofilename = photofilename;
	}

	public String getTempId() {
		return tempId;
	}

	public void setTempId(String tempId) {
		this.tempId = tempId;
	}

	public String getThumbfilename() {
		return thumbfilename;
	}

	public void setThumbfilename(String thumbfilename) {
		this.thumbfilename = thumbfilename;
	}

	public JrpPermohonan getPermohonan() {
		return permohonan;
	}

	public void setPermohonan(JrpPermohonan permohonan) {
		this.permohonan = permohonan;
	}

	public JrpUlasanTeknikal getUlasanTeknikal() {
		return ulasanTeknikal;
	}

	public void setUlasanTeknikal(JrpUlasanTeknikal ulasanTeknikal) {
		this.ulasanTeknikal = ulasanTeknikal;
	}

	public String getTajuk() {
		return tajuk;
	}

	public void setTajuk(String tajuk) {
		this.tajuk = tajuk;
	}

	public JenisDokumen getJenisDokumen() {
		return jenisDokumen;
	}

	public void setJenisDokumen(JenisDokumen jenisDokumen) {
		this.jenisDokumen = jenisDokumen;
	}

	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}

	public Date getTarikhMuatnaik() {
		return tarikhMuatnaik;
	}

	public void setTarikhMuatnaik(Date tarikhMuatnaik) {
		this.tarikhMuatnaik = tarikhMuatnaik;
	}
}
