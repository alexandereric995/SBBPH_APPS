package bph.entities.rpp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lebah.template.UID;
import portal.module.entity.Users;

@Entity
@Table(name = "rpp_pemohon_vip")
public class RppPemohonVIP {

	@Id
	@Column(name = "id")
	private String id;

	@OneToOne
	@JoinColumn(name = "id_pemohon")
	private Users pemohon;
	
	@Column(name = "flag_vip")
	private String flagVip; 

	
	public RppPemohonVIP() {
		setId(UID.getUID());
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Users getPemohon() {
		return pemohon;
	}

	public void setPemohon(Users pemohon) {
		this.pemohon = pemohon;
	}

	public String getFlagVip() {
		return flagVip;
	}

	public void setFlagVip(String flagVip) {
		this.flagVip = flagVip;
	}

}
