package bph.entities.pro;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import bph.entities.kod.Seksyen;

@Entity
@Table(name = "ruj_pro_senarai_unit")
public class ProSenaraiUnit {
	
	@Id
	@Column(name = "id")
	private int id;
	
	@Column(name = "keterangan")
	private String keterangan;
	
	@ManyToOne
	@JoinColumn(name = "id_seksyen")
	private Seksyen seksyen;
			
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

	public Seksyen getSeksyen() {
		return seksyen;
	}

	public void setSeksyen(Seksyen seksyen) {
		this.seksyen = seksyen;
	}
	
}
