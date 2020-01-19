package bph.entities.qtr;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lebah.template.UID;

@Entity
@Table(name = "kua_kelainan_upaya")
public class KuaKelainanUpaya {

	@Id
	@Column(name = "id")
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "id_permohonan")
	private KuaPermohonan permohonan;
	
	@Column(name = "noMyKad")
	private String noMyKad;
	
	@Column(name = "noKad")
	private String noKad;
	
	@Column(name = "hubungan")
	private String hubungan;
	
	@Column(name = "imgName")
	private String imgName;
	
	@Column(name = "avatarName")
	private String avatarName;
	
	public KuaKelainanUpaya() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public KuaPermohonan getPermohonan() {
		return permohonan;
	}

	public void setPermohonan(KuaPermohonan permohonan) {
		this.permohonan = permohonan;
	}

	public String getNoMyKad() {
		return noMyKad;
	}

	public void setNoMyKad(String noMyKad) {
		this.noMyKad = noMyKad;
	}

	public String getNoKad() {
		return noKad;
	}

	public void setNoKad(String noKad) {
		this.noKad = noKad;
	}

	public String getHubungan() {
		return hubungan;
	}

	public void setHubungan(String hubungan) {
		this.hubungan = hubungan;
	}

	public String getImgName() {
		return imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
	}

	public String getAvatarName() {
		return avatarName;
	}

	public void setAvatarName(String avatarName) {
		this.avatarName = avatarName;
	}

}
