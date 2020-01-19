/**
* AUTHOR : zufazdliabuas@gmail.com
* Date : 14/6/2017
*/
package bph.entities.portal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lebah.template.UID;

@Entity
@Table(name = "cms_akrab_gambar")
public class CmsAkrabGambar {

	@Id
	@Column(name = "id")
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "id_akrab")
	private CmsAkrab akrab;
	
	@ManyToOne
	@JoinColumn(name = "id_aktiviti")
	private CmsAkrabAktiviti aktiviti;
	
	@Column(name = "file_name")
	private String fileName;
	
	@Column(name = "avatar_name")
	private String avatarName;

	public CmsAkrabGambar() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public CmsAkrabAktiviti getAktiviti() {
		return aktiviti;
	}

	public void setAktiviti(CmsAkrabAktiviti aktiviti) {
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

	public CmsAkrab getAkrab() {
		return akrab;
	}

	public void setAkrab(CmsAkrab akrab) {
		this.akrab = akrab;
	}

}
