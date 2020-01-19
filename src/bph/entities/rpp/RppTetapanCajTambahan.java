package bph.entities.rpp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lebah.template.UID;

@Entity
@Table(name = "rpp_tetapan_caj_tambahan")
public class RppTetapanCajTambahan {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "keterangan")
	private String keterangan;
	
	@Column(name = "caj_bayaran")
	private Double cajBayaran; 

	
	public RppTetapanCajTambahan() {
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

	public Double getCajBayaran() {
		return cajBayaran;
	}

	public void setCajBayaran(Double cajBayaran) {
		this.cajBayaran = cajBayaran;
	}

}
