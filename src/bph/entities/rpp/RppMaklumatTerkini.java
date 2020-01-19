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
@Table(name = "rpp_maklumat_terkini")
public class RppMaklumatTerkini {

	@Id
	@Column(name = "id")
	private String id;
	
	@Column(name = "catatan")
	private String catatan;
	
	@Column(name = "tarikh_catatan")
	@Temporal(TemporalType.TIMESTAMP)
	private Date tarikhCatatan;

	@ManyToOne
	@JoinColumn(name = "id_pendaftar")
	private Users pendaftar;

	
	public RppMaklumatTerkini() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}

	public Users getPendaftar() {
		return pendaftar;
	}

	public void setPendaftar(Users pendaftar) {
		this.pendaftar = pendaftar;
	}

	public Date getTarikhCatatan() {
		return tarikhCatatan;
	}

	public void setTarikhCatatan(Date tarikhCatatan) {
		this.tarikhCatatan = tarikhCatatan;
	}
	
}
