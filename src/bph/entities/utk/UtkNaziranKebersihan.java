package bph.entities.utk;

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
import bph.entities.senggara.MtnKontraktor;

@Entity
@Table(name = "utk_naziran_kebersihan")
public class UtkNaziranKebersihan {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_operasi")
	private UtkOperasi operasi;
	
	@ManyToOne
	@JoinColumn(name = "id_kontraktor")
	private MtnKontraktor kontraktor;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_mula")
	private Date tarikhMula;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_tamat")
	private Date tarikhTamat;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "catatan")
	private String catatan;

	public UtkNaziranKebersihan() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public UtkOperasi getOperasi() {
		return operasi;
	}

	public void setOperasi(UtkOperasi operasi) {
		this.operasi = operasi;
	}

	public MtnKontraktor getKontraktor() {
		return kontraktor;
	}

	public void setKontraktor(MtnKontraktor kontraktor) {
		this.kontraktor = kontraktor;
	}

	public Date getTarikhMula() {
		return tarikhMula;
	}

	public void setTarikhMula(Date tarikhMula) {
		this.tarikhMula = tarikhMula;
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

	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}
}
