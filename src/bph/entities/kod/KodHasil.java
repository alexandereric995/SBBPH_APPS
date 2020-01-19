package bph.entities.kod;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author muhdsyazreen
 */

@Entity
@Table(name = "ruj_kod_hasil")
public class KodHasil {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "kod")
	private String kod;

	@Column(name = "keterangan")
	private String keterangan;

	@Column(name = "isObject")
	private Boolean isObject;

	@Column(name = "isTrust")
	private Boolean isTrust;

	@Column(name = "isB06")
	private Boolean isB06;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKod() {
		return kod;
	}

	public void setKod(String kod) {
		this.kod = kod;
	}

	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}

	public Boolean isObject() {
		return isObject;
	}

	public void setObject(Boolean isObject) {
		this.isObject = isObject;
	}

	public Boolean isTrust() {
		return isTrust;
	}

	public void setTrust(Boolean isTrust) {
		this.isTrust = isTrust;
	}

	public Boolean isB06() {
		return isB06;
	}

	public void setB06(Boolean isB06) {
		this.isB06 = isB06;
	}

}
