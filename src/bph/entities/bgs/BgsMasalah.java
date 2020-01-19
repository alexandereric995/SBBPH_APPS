package bph.entities.bgs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lebah.template.UID;

@Entity
@Table(name = "bgs_masalah")
public class BgsMasalah {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_permohonan")
	private BgsPermohonan permohonan;
	
	@Column(name = "masalah")
	private String masalah;
	
	public BgsMasalah() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public BgsPermohonan getPermohonan() {
		return permohonan;
	}

	public void setPermohonan(BgsPermohonan permohonan) {
		this.permohonan = permohonan;
	}

	public String getMasalah() {
		return masalah;
	}

	public void setMasalah(String masalah) {
		this.masalah = masalah;
	}

	public void setId(String id) {
		this.id = id;
	}
}