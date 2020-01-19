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
import bph.entities.kod.TermaSyaratRpp;

@Entity
@Table(name = "rpp_terma_syarat_peranginan")
public class RppTermaSyaratPeranginan {

	@Id
	@Column(name = "id")
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "id_terma_syarat_rpp")
	private TermaSyaratRpp termaSyaratRpp;
	
	@ManyToOne
	@JoinColumn(name = "id_peranginan")
	private RppPeranginan rppPeranginan;
	
	@Column(name = "flag_mandatori")
	private String flagMandatori; // Y / T . default is Y
	
	@OneToOne
	@JoinColumn(name = "id_masuk")
	private Users idMasuk;
	
	@OneToOne
	@JoinColumn(name = "id_kemaskini")
	private Users idKemaskini;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_masuk")
	private Date tarikhMasuk;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_kemaskini")
	private Date tarikhKemaskini;
	
	
	public RppTermaSyaratPeranginan() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public TermaSyaratRpp getTermaSyaratRpp() {
		return termaSyaratRpp;
	}

	public void setTermaSyaratRpp(TermaSyaratRpp termaSyaratRpp) {
		this.termaSyaratRpp = termaSyaratRpp;
	}

	public RppPeranginan getRppPeranginan() {
		return rppPeranginan;
	}

	public void setRppPeranginan(RppPeranginan rppPeranginan) {
		this.rppPeranginan = rppPeranginan;
	}

	public String getFlagMandatori() {
		return flagMandatori;
	}

	public void setFlagMandatori(String flagMandatori) {
		this.flagMandatori = flagMandatori;
	}

	public Users getIdMasuk() {
		return idMasuk;
	}

	public void setIdMasuk(Users idMasuk) {
		this.idMasuk = idMasuk;
	}

	public Users getIdKemaskini() {
		return idKemaskini;
	}

	public void setIdKemaskini(Users idKemaskini) {
		this.idKemaskini = idKemaskini;
	}

	public Date getTarikhMasuk() {
		return tarikhMasuk;
	}

	public void setTarikhMasuk(Date tarikhMasuk) {
		this.tarikhMasuk = tarikhMasuk;
	}

	public Date getTarikhKemaskini() {
		return tarikhKemaskini;
	}

	public void setTarikhKemaskini(Date tarikhKemaskini) {
		this.tarikhKemaskini = tarikhKemaskini;
	}

}
