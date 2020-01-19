package bph.entities.rpp;

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
@Table(name = "rpp_catatan_pengguna")
public class RppCatatanPengguna {

	@Id
	@Column(name = "id")
	private String id;
	  
	@ManyToOne
	@JoinColumn(name = "id_pengguna")
	private Users pengguna;

	@Column(name = "catatan")
	private String catatan;
	
	@Column(name = "tarikh_daftar")
	@Temporal(TemporalType.TIMESTAMP)
	private Date tarikhDaftar;
	
	@Column(name = "tarikh_kemaskini")
	@Temporal(TemporalType.TIMESTAMP)
	private Date tarikhKemaskini;
	
	@ManyToOne
	@JoinColumn(name = "id_user_daftar")
	private Users userDaftar;
	
	@ManyToOne
	@JoinColumn(name = "id_user_kemaskini")
	private Users userKemaskini;
	
	
	
	public RppCatatanPengguna() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Users getPengguna() {
		return pengguna;
	}

	public void setPengguna(Users pengguna) {
		this.pengguna = pengguna;
	}

	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}

	public Date getTarikhDaftar() {
		return tarikhDaftar;
	}

	public void setTarikhDaftar(Date tarikhDaftar) {
		this.tarikhDaftar = tarikhDaftar;
	}

	public Date getTarikhKemaskini() {
		return tarikhKemaskini;
	}

	public void setTarikhKemaskini(Date tarikhKemaskini) {
		this.tarikhKemaskini = tarikhKemaskini;
	}

	public Users getUserDaftar() {
		return userDaftar;
	}

	public void setUserDaftar(Users userDaftar) {
		this.userDaftar = userDaftar;
	}

	public Users getUserKemaskini() {
		return userKemaskini;
	}

	public void setUserKemaskini(Users userKemaskini) {
		this.userKemaskini = userKemaskini;
	}

}
