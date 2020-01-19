package bph.entities.rpp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lebah.template.UID;
import bph.entities.kod.Status;

@Entity
@Table(name="rpp_selenggara_unit_lokasi")
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
	
	
	public RppSelenggaraUnitLokasi() {
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

}
