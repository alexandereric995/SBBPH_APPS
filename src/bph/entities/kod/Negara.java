package bph.entities.kod;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ruj_negara")
public class Negara {

	@Id
	@Column(name="id")
	private String id;
	
	@Column(name="keterangan")
	private String keterangan;

	@Column(name="kod_1")
	private String kod1;
	
	@Column(name="kod_2")
	private String kod2;

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

	public String getKod1() {
		return kod1;
	}

	public void setKod1(String kod1) {
		this.kod1 = kod1;
	}

	public String getKod2() {
		return kod2;
	}

	public void setKod2(String kod2) {
		this.kod2 = kod2;
	}
	
}