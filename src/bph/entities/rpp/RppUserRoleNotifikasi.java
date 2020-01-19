package bph.entities.rpp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lebah.template.UID;
import portal.module.entity.Role;
import portal.module.entity.Users;

@Entity
@Table(name = "rpp_user_role_notifikasi")
public class RppUserRoleNotifikasi {

	@Id
	@Column(name = "id")
	private String id;
	  
	@ManyToOne
	@JoinColumn(name = "id_notifikasi")
	private RppNotifikasi notifikasi;

	@ManyToOne
	@JoinColumn(name = "id_role")
	private Role role;
	
	@ManyToOne
	@JoinColumn(name = "id_user")
	private Users user;
	
	@Column(name = "flag_penerima")
	private String flagPenerima; //INDIVIDU / KUMPULAN
	
	
	public RppUserRoleNotifikasi() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public RppNotifikasi getNotifikasi() {
		return notifikasi;
	}

	public void setNotifikasi(RppNotifikasi notifikasi) {
		this.notifikasi = notifikasi;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public String getFlagPenerima() {
		return flagPenerima;
	}

	public void setFlagPenerima(String flagPenerima) {
		this.flagPenerima = flagPenerima;
	}

}
