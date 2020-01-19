package bph.entities.kontrak;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lebah.template.UID;
import bph.entities.kod.Seksyen;

@Entity
@Table(name = "kontrak_perjanjian")
public class KontrakPerjanjian {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_seksyen")
	private Seksyen seksyen;
	
	@Column(name = "no_perjanjian")
	private String noPerjanjian;
	
	@Column(name = "perkara")
	private String perkara;
	
	@Column(name = "tarikh_mula")
	@Temporal(TemporalType.DATE)
	private Date tarikhMula;
	
	@Column(name = "tempoh")
	private int tempoh;
	
	@Column(name = "tarikh_tamat")
	@Temporal(TemporalType.DATE)
	private Date tarikhTamat;	
	
	@Column(name = "status")
	private String status;
		
	public KontrakPerjanjian(){
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Seksyen getSeksyen() {
		return seksyen;
	}

	public void setSeksyen(Seksyen seksyen) {
		this.seksyen = seksyen;
	}

	public String getNoPerjanjian() {
		return noPerjanjian;
	}

	public void setNoPerjanjian(String noPerjanjian) {
		this.noPerjanjian = noPerjanjian;
	}

	public String getPerkara() {
		return perkara;
	}

	public void setPerkara(String perkara) {
		this.perkara = perkara;
	}

	public Date getTarikhMula() {
		return tarikhMula;
	}

	public void setTarikhMula(Date tarikhMula) {
		this.tarikhMula = tarikhMula;
	}

	public int getTempoh() {
		return tempoh;
	}

	public void setTempoh(int tempoh) {
		this.tempoh = tempoh;
	}

	public Date getTarikhTamat() {
		return tarikhTamat;
	}

	public void setTarikhTamat(Date tarikhTamat) {
		this.tarikhTamat = tarikhTamat;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}	
}














