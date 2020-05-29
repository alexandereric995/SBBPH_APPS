package bph.entities.rpp;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lebah.template.UID;
import portal.module.entity.Users;
import bph.entities.kod.Status;

@Entity
@Table(name = "rpp_selenggara_unit_lokasi")
public class RppSelenggaraUnitLokasi {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "catatan")
	private String catatan;

	@ManyToOne
	@JoinColumn(name = "id_selenggara")
	private RppSelenggara rppSelenggara;

	@ManyToOne
	@JoinColumn(name = "id_peranginan")
	private RppPeranginan rppPeranginan;

	@ManyToOne
	@JoinColumn(name = "id_unit")
	private RppUnit rppUnit;

	@OneToOne
	@JoinColumn(name = "id_status")
	private Status status;

	@ManyToOne
	@JoinColumn(name = "id_masuk")
	private Users idMasuk;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_masuk")
	private Date tarikhMasuk;

	@ManyToOne
	@JoinColumn(name = "id_kemaskini")
	private Users idKemaskini;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_kemaskini")
	private Date tarikhKemaskini;

	public RppSelenggaraUnitLokasi() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());
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

	public RppSelenggara getRppSelenggara() {
		return rppSelenggara;
	}

	public void setRppSelenggara(RppSelenggara rppSelenggara) {
		this.rppSelenggara = rppSelenggara;
	}

	public RppUnit getRppUnit() {
		return rppUnit;
	}

	public void setRppUnit(RppUnit rppUnit) {
		this.rppUnit = rppUnit;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public RppPeranginan getRppPeranginan() {
		return rppPeranginan;
	}

	public void setRppPeranginan(RppPeranginan rppPeranginan) {
		this.rppPeranginan = rppPeranginan;
	}

	public Users getIdMasuk() {
		return idMasuk;
	}

	public void setIdMasuk(Users idMasuk) {
		this.idMasuk = idMasuk;
	}

	public Date getTarikhMasuk() {
		return tarikhMasuk;
	}

	public void setTarikhMasuk(Date tarikhMasuk) {
		this.tarikhMasuk = tarikhMasuk;
	}

	public Users getIdKemaskini() {
		return idKemaskini;
	}

	public void setIdKemaskini(Users idKemaskini) {
		this.idKemaskini = idKemaskini;
	}

	public Date getTarikhKemaskini() {
		return tarikhKemaskini;
	}

	public void setTarikhKemaskini(Date tarikhKemaskini) {
		this.tarikhKemaskini = tarikhKemaskini;
	}

}
