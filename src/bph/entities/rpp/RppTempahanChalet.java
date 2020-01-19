package bph.entities.rpp;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lebah.template.UID;

@Entity
@Table(name="rpp_tempahan_chalet")
public class RppTempahanChalet {

	@Id
	@Column(name = "id")
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "id_permohonan")
	private RppPermohonan rppPermohonan;
	
	@OneToOne
	@JoinColumn(name = "id_unit")
	private RppUnit rppUnit;
	
	
	public RppTempahanChalet() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public RppPermohonan getRppPermohonan() {
		return rppPermohonan;
	}

	public void setRppPermohonan(RppPermohonan rppPermohonan) {
		this.rppPermohonan = rppPermohonan;
	}

	public RppUnit getRppUnit() {
		return rppUnit;
	}

	public void setRppUnit(RppUnit rppUnit) {
		this.rppUnit = rppUnit;
	}

}
