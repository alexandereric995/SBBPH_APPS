package bph.entities.pro;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ruj_pro_keterangan_teknikal")
public class ProKeteranganTeknikal {
	
	@Id
	@Column(name = "id")
	private int id;
	
	@Column(name = "keterangan")
	private String keterangan;
			
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}	
}
