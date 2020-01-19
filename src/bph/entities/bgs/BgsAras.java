package bph.entities.bgs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lebah.template.UID;
import bph.entities.pembangunan.DevAras;

@Entity
@Table(name = "bgs_aras")
public class BgsAras {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_aras")
	private DevAras aras;

	public BgsAras() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DevAras getAras() {
		return aras;
	}

	public void setAras(DevAras aras) {
		this.aras = aras;
	}
}
