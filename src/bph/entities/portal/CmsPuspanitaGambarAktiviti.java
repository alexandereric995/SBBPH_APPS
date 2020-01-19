package bph.entities.portal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lebah.template.UID;

@Entity
@Table(name = "cms_puspanita_gambar_aktiviti")
public class CmsPuspanitaGambarAktiviti {

	@Id
	@Column(name = "id")
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "id_aktiviti")
	private CmsPuspanitaAktiviti aktiviti;
	
	@Column(name = "file_name")
	private String fileName;
	
	@Column(name = "avatar_name")
	private String avatarName;

	public CmsPuspanitaGambarAktiviti() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public CmsPuspanitaAktiviti getAktiviti() {
		return aktiviti;
	}

	public void setAktiviti(CmsPuspanitaAktiviti aktiviti) {
		this.aktiviti = aktiviti;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getAvatarName() {
		return avatarName;
	}

	public void setAvatarName(String avatarName) {
		this.avatarName = avatarName;
	}
}
