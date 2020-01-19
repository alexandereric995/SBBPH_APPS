package bph.entities.portal;

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
import portal.module.entity.Users;

@Entity
@Table(name = "cms_sub_galeri")
public class CmsSubGaleri {

	@Id
	@Column(name = "id")
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "id_galeri")
	private CmsGaleri galeri;
	
	@Column(name = "file_name")
	private String fileName;
	
	@Column(name = "thumb_file_name")
	private String thumbFileName;
	
	@ManyToOne
	@JoinColumn(name = "id_masuk")
	private Users daftarOleh;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_masuk")
	private Date tarikhMasuk;
	
	public CmsSubGaleri() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public CmsGaleri getGaleri() {
		return galeri;
	}

	public void setGaleri(CmsGaleri galeri) {
		this.galeri = galeri;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getThumbFileName() {
		return thumbFileName;
	}

	public void setThumbFileName(String thumbFileName) {
		this.thumbFileName = thumbFileName;
	}

	public Users getDaftarOleh() {
		return daftarOleh;
	}

	public void setDaftarOleh(Users daftarOleh) {
		this.daftarOleh = daftarOleh;
	}

	public Date getTarikhMasuk() {
		return tarikhMasuk;
	}

	public void setTarikhMasuk(Date tarikhMasuk) {
		this.tarikhMasuk = tarikhMasuk;
	}
}
