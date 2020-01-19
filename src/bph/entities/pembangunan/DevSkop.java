package bph.entities.pembangunan;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lebah.template.UID;

@Entity
@Table(name = "dev_skop")
public class DevSkop {

	@Id
	@Column(name = "id")
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "id_cadangan")
	private DevCadangan cadangan;

	@Column(name = "item")
	private String item;

	public DevSkop() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public DevCadangan getCadangan() {
		return cadangan;
	}

	public void setCadangan(DevCadangan cadangan) {
		this.cadangan = cadangan;
	}
}
