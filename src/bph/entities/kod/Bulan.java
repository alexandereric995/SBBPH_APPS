package bph.entities.kod;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ruj_bulan")
public class Bulan {

	@Id
	@Column(name = "id")
	private String id;
	
	@Column(name = "kod_bulan")
	private Integer kodBulan;
	
	@Column(name = "abbrev")
	private String abbrev;

	@Column(name = "keterangan")
	private String keterangan;

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

	public String getAbbrev() {
		return abbrev;
	}

	public void setAbbrev(String abbrev) {
		this.abbrev = abbrev;
	}

	public Integer getKodBulan() {
		return kodBulan;
	}

	public void setKodBulan(Integer kodBulan) {
		this.kodBulan = kodBulan;
	}
	
}
