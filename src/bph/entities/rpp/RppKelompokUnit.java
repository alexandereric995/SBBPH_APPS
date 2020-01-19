package bph.entities.rpp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lebah.template.UID;
import bph.entities.kod.JenisUnitRPP;

@Entity
@Table(name = "rpp_kelompok_unit")
public class RppKelompokUnit {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_permohonan")
	private RppPermohonan permohonan;
	
	@ManyToOne
	@JoinColumn(name = "id_jenis_unit_rpp")
	private JenisUnitRPP jenisUnitRpp;
	
	@Column(name = "bil_unit")
	private Integer bilUnit;
	
	@Column(name = "bil_unit_lulus")
	private Integer bilUnitLulus;
	
	
	public RppKelompokUnit() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public RppPermohonan getPermohonan() {
		return permohonan;
	}

	public void setPermohonan(RppPermohonan permohonan) {
		this.permohonan = permohonan;
	}

	public JenisUnitRPP getJenisUnitRpp() {
		return jenisUnitRpp;
	}

	public void setJenisUnitRpp(JenisUnitRPP jenisUnitRpp) {
		this.jenisUnitRpp = jenisUnitRpp;
	}

	public Integer getBilUnit() {
		return bilUnit;
	}

	public void setBilUnit(Integer bilUnit) {
		this.bilUnit = bilUnit;
	}

	public Integer getBilUnitLulus() {
		return bilUnitLulus;
	}

	public void setBilUnitLulus(Integer bilUnitLulus) {
		this.bilUnitLulus = bilUnitLulus;
	}

}
