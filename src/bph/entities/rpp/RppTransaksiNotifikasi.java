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
@Table(name = "rpp_transaksi_notifikasi")
public class RppTransaksiNotifikasi {

	@Id
	@Column(name = "id")
	private String id;
	  
	@ManyToOne
	@JoinColumn(name = "id_notifikasi")
	private RppNotifikasi notifikasi;

	@ManyToOne
	@JoinColumn(name = "id_user")
	private Users user;
	
	@Column(name = "tarikh_buka")
	@Temporal(TemporalType.TIMESTAMP)
	private Date tarikhBuka;
	
	
	public RppTransaksiNotifikasi() {
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

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public Date getTarikhBuka() {
		return tarikhBuka;
	}

	public void setTarikhBuka(Date tarikhBuka) {
		this.tarikhBuka = tarikhBuka;
	}

}
