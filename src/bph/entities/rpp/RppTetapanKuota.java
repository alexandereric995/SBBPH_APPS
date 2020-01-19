package bph.entities.rpp;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lebah.template.UID;
import bph.entities.kod.JenisUnitRPP;

@Entity
@Table(name="rpp_tetapan_kuota")
public class RppTetapanKuota {

	@Id
	@Column(name = "id")
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "id_jenis_unit")
	private JenisUnitRPP jenisUnitRpp;	
	
	@Column(name = "hari")
	private Integer hari;
	
	@Column(name = "kuota")
	private Integer kuota;
	  
	
	public RppTetapanKuota() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public JenisUnitRPP getJenisUnitRpp() {
		return jenisUnitRpp;
	}

	public void setJenisUnitRpp(JenisUnitRPP jenisUnitRpp) {
		this.jenisUnitRpp = jenisUnitRpp;
	}

	public Integer getHari() {
		return hari;
	}

	public void setHari(Integer hari) {
		this.hari = hari;
	}

	public Integer getKuota() {
		return kuota;
	}

	public void setKuota(Integer kuota) {
		this.kuota = kuota;
	}

}
