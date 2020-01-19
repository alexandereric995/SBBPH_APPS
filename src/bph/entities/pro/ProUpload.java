package bph.entities.pro;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lebah.template.UID;


@Entity
@Table(name = "pro_aduan")
public class ProUpload {

	@Id
	@Column(name = "id")
	private String id;
	
	@Column(name = "photo_file_name")
	private String photoFileName;
	
	@Column(name = "thumb_file_name")
	private String thumbFileName;
	
	@Column(name = "sequence")
	private int sequence;
	
	@Column(name = "flag_aktif")
	private int flagAktif;
	
	
	@Column(name = "url_onclick")
	private String urlOnclick;

	public ProUpload() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPhotoFileName() {
		return photoFileName;
	}

	public void setPhotoFileName(String photoFileName) {
		this.photoFileName = photoFileName;
	}

	public String getThumbFileName() {
		return thumbFileName;
	}

	public void setThumbFileName(String thumbFileName) {
		this.thumbFileName = thumbFileName;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public int getFlagAktif() {
		return flagAktif;
	}

	public void setFlagAktif(int flagAktif) {
		this.flagAktif = flagAktif;
	}

	public String getUrlOnclick() {
		return urlOnclick;
	}

	public void setUrlOnclick(String urlOnclick) {
		this.urlOnclick = urlOnclick;
	}

}
