package bph.entities.utk;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lebah.template.UID;
import bph.entities.kod.JenisDokumen;

@Entity
@Table(name = "utk_dokumen")
public class UtkDokumen {

	@Id
	@Column(name = "id")
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "id_operasi")
	private UtkOperasi operasi;
	
	@ManyToOne
	@JoinColumn(name = "id_kesalahan")
	private UtkKesalahan kesalahan;
	
	@Column(name = "photofilename")
	private String photofilename;

	@Column(name = "thumbfilename")
	private String thumbfilename;
	
	@Column(name = "tajuk")
	private String tajuk;

	@ManyToOne
	@JoinColumn(name = "id_jenis_dokumen")
	private JenisDokumen jenisDokumen;

	@Column(name = "keterangan")
	private String keterangan;

	public UtkDokumen() {
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

	public UtkKesalahan getKesalahan() {
		return kesalahan;
	}

	public void setKesalahan(UtkKesalahan kesalahan) {
		this.kesalahan = kesalahan;
	}

	public UtkOperasi getOperasi() {
		return operasi;
	}

	public void setOperasi(UtkOperasi operasi) {
		this.operasi = operasi;
	}
}
