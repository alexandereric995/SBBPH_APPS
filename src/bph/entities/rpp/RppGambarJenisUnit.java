package bph.entities.rpp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lebah.template.UID;
import bph.entities.kod.JenisDokumen;
import bph.entities.kod.JenisUnitRPP;

@Entity
@Table(name = "rpp_gambar_jenis_unit")
public class RppGambarJenisUnit {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "tajuk")
	private String tajuk;
	
	@Column(name = "photofilename")
	private String photofilename;

	@Column(name = "tempId")
	private String tempId;

	@Column(name = "thumbfilename")
	private String thumbfilename;

	@ManyToOne
	@JoinColumn(name = "id_jenis_unit_rpp")
	private JenisUnitRPP jenisUnit;
	
	@ManyToOne
	@JoinColumn(name = "id_jenis_dokumen")
	private JenisDokumen jenisDokumen;

	@Column(name = "keterangan")
	private String keterangan;
	
	public RppGambarJenisUnit() {
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

	public JenisUnitRPP getJenisUnit() {
		return jenisUnit;
	}

	public void setJenisUnit(JenisUnitRPP jenisUnit) {
		this.jenisUnit = jenisUnit;
	}
	
}
