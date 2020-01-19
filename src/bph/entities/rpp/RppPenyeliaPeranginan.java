package bph.entities.rpp;


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
import portal.module.entity.Users;

@Entity
@Table(name="rpp_penyelia_peranginan")
public class RppPenyeliaPeranginan {

	@Id
	@Column(name = "id")
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "id_peranginan")
	private RppPeranginan peranginan;
	
	@ManyToOne
	@JoinColumn(name = "id_penyelia")
	private Users penyelia;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_mula_khidmat")
	private Date tarikhMulaKhidmat;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_tamat_khidmat")
	private Date tarikhTamatKhidmat;
	
	@Column(name = "no_telefon")
	private String noTelefon;
	
	@Column(name = "emel")
	private String emel;
	
	@Column(name = "catatan")
	private String catatan;
	
	@Column(name = "status_perkhidmatan")
	private String statusPerkhidmatan;
	
	@Column(name = "flag_operator")
	private String flagOperator;
	
	public RppPenyeliaPeranginan() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public RppPeranginan getPeranginan() {
		return peranginan;
	}

	public void setPeranginan(RppPeranginan peranginan) {
		this.peranginan = peranginan;
	}

	public Users getPenyelia() {
		return penyelia;
	}

	public void setPenyelia(Users penyelia) {
		this.penyelia = penyelia;
	}

	public Date getTarikhMulaKhidmat() {
		return tarikhMulaKhidmat;
	}

	public void setTarikhMulaKhidmat(Date tarikhMulaKhidmat) {
		this.tarikhMulaKhidmat = tarikhMulaKhidmat;
	}

	public Date getTarikhTamatKhidmat() {
		return tarikhTamatKhidmat;
	}

	public void setTarikhTamatKhidmat(Date tarikhTamatKhidmat) {
		this.tarikhTamatKhidmat = tarikhTamatKhidmat;
	}

	public String getNoTelefon() {
		return noTelefon;
	}

	public void setNoTelefon(String noTelefon) {
		this.noTelefon = noTelefon;
	}

	public String getEmel() {
		return emel;
	}

	public void setEmel(String emel) {
		this.emel = emel;
	}

	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}

	public String getStatusPerkhidmatan() {
		return statusPerkhidmatan;
	}

	public void setStatusPerkhidmatan(String statusPerkhidmatan) {
		this.statusPerkhidmatan = statusPerkhidmatan;
	}

	public String getFlagOperator() {
		return flagOperator;
	}

	public void setFlagOperator(String flagOperator) {
		this.flagOperator = flagOperator;
	}
}
