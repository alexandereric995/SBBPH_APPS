package bph.entities.kod;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lebah.template.UID;

@Entity
@Table(name="ruj_aktiviti_rpp")
public class AktivitiRpp {

	@Id
	@Column(name = "id")
	private String id;
	
	@Column(name = "keterangan")
	private String keterangan;
	
	
	public AktivitiRpp() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}
	
}
