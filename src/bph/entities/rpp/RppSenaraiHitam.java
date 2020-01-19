package bph.entities.rpp;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lebah.template.UID;
import portal.module.entity.Users;
import bph.entities.kod.Status;

@Entity
@Table(name = "rpp_senarai_hitam")
public class RppSenaraiHitam {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_permohonan")
	private RppPermohonan permohonan;
	
	@OneToOne
	@JoinColumn(name = "id_pemohon")
	private Users pemohon;

	@Column(name = "sebab")
	private String sebab;

	@Column(name = "tarikh_mula")
	@Temporal(TemporalType.DATE)
	private Date tarikhMula;
	
	@Column(name = "tarikh_tamat")
	@Temporal(TemporalType.DATE)
	private Date tarikhTamat;

	@Column(name = "flag_aktif")
	private String flagAktif;

	@Column(name = "catatan")
	private String catatan;

	@Column(name = "tarikh_permohonan")
	@Temporal(TemporalType.DATE)
	private Date tarikhPermohonan;
	
	@ManyToOne
	@JoinColumn(name = "id_status")
	private Status status;
	
	
	public RppSenaraiHitam() {
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

	public String getSebab() {
		return sebab;
	}

	public void setSebab(String sebab) {
		this.sebab = sebab;
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

	public String getFlagAktif() {
		return flagAktif;
	}

	public void setFlagAktif(String flagAktif) {
		this.flagAktif = flagAktif;
	}

	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}
	
	public Users getPemohon() {
		return pemohon;
	}

	public void setPemohon(Users pemohon) {
		this.pemohon = pemohon;
	}

	public Date getTarikhPermohonan() {
		return tarikhPermohonan;
	}

	public void setTarikhPermohonan(Date tarikhPermohonan) {
		this.tarikhPermohonan = tarikhPermohonan;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getKeteranganFlagAktif() {
		if (this.flagAktif.equals("Y")) {
			return "AKTIF";
		} else {
			return "TIDAK AKTIF";
		}
	}

}
