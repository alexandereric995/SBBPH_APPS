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
@Table(name = "rpp_list_catatan")
public class RppListCatatan {

	@Id
	@Column(name = "id")
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "id_main_catatan_penyelia")
	private RppMainCatatanPenyelia mainCatatanPenyelia;
	
	@Column(name = "tarikh_catatan")
	@Temporal(TemporalType.TIMESTAMP)
	private Date tarikhCatatan;
	
	@Column(name = "catatan")
	private String catatan;
	
	@ManyToOne
	@JoinColumn(name = "id_pengguna")
	private Users pengguna;
	
	
	public RppListCatatan() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public RppMainCatatanPenyelia getMainCatatanPenyelia() {
		return mainCatatanPenyelia;
	}

	public void setMainCatatanPenyelia(RppMainCatatanPenyelia mainCatatanPenyelia) {
		this.mainCatatanPenyelia = mainCatatanPenyelia;
	}

	public Date getTarikhCatatan() {
		return tarikhCatatan;
	}

	public void setTarikhCatatan(Date tarikhCatatan) {
		this.tarikhCatatan = tarikhCatatan;
	}

	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}

	public Users getPengguna() {
		return pengguna;
	}

	public void setPengguna(Users pengguna) {
		this.pengguna = pengguna;
	}

}
