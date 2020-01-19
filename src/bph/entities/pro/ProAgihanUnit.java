package bph.entities.pro;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lebah.template.UID;
import bph.entities.kod.Seksyen;

@Entity
@Table(name = "pro_agihan_unit")
public class ProAgihanUnit {
	
	@Id
	@Column(name = "id")
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "id_aduan")
	private ProAduan aduan;
	
	@ManyToOne
	@JoinColumn(name = "id_seksyen")
	private Seksyen seksyen;
	
	@ManyToOne
	@JoinColumn(name = "id_senarai_unit")
	private ProSenaraiUnit idSenaraiUnit;
	
	public ProAgihanUnit() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ProAduan getAduan() {
		return aduan;
	}

	public void setAduan(ProAduan aduan) {
		this.aduan = aduan;
	}

	public Seksyen getSeksyen() {
		return seksyen;
	}

	public void setSeksyen(Seksyen seksyen) {
		this.seksyen = seksyen;
	}

	public ProSenaraiUnit getIdSenaraiUnit() {
		return idSenaraiUnit;
	}

	public void setIdSenaraiUnit(ProSenaraiUnit idSenaraiUnit) {
		this.idSenaraiUnit = idSenaraiUnit;
	}

	
}
