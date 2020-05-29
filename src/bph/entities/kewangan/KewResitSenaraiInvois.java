package bph.entities.kewangan;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lebah.template.UID;
import portal.module.entity.Users;

@Entity
@Table(name = "kew_resit_senarai_invois")
public class KewResitSenaraiInvois {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne(cascade = { CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "id_bayaran_resit")
	private KewBayaranResit resit;

	@OneToOne
	@JoinColumn(name = "id_invois")
	private KewInvois invois;

	@OneToOne
	@JoinColumn(name = "id_deposit")
	private KewDeposit deposit;

	@Column(name = "flag_jenis_resit")
	private String flagJenisResit = "INVOIS"; // INVOIS / DEPOSIT - DEFAULT
												// INVOIS

	@ManyToOne
	@JoinColumn(name = "id_masuk")
	private Users idMasuk;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_masuk")
	private Date tarikhMasuk;

	@ManyToOne
	@JoinColumn(name = "id_kemaskini")
	private Users idKemaskini;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_kemaskini")
	private Date tarikhKemaskini;

	public KewResitSenaraiInvois() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public KewBayaranResit getResit() {
		return resit;
	}

	public void setResit(KewBayaranResit resit) {
		this.resit = resit;
	}

	public KewInvois getInvois() {
		return invois;
	}

	public void setInvois(KewInvois invois) {
		this.invois = invois;
	}

	public KewDeposit getDeposit() {
		return deposit;
	}

	public void setDeposit(KewDeposit deposit) {
		this.deposit = deposit;
	}

	public String getFlagJenisResit() {
		return flagJenisResit;
	}

	public void setFlagJenisResit(String flagJenisResit) {
		this.flagJenisResit = flagJenisResit;
	}

	public Users getIdMasuk() {
		return idMasuk;
	}

	public void setIdMasuk(Users idMasuk) {
		this.idMasuk = idMasuk;
	}

	public Date getTarikhMasuk() {
		return tarikhMasuk;
	}

	public void setTarikhMasuk(Date tarikhMasuk) {
		this.tarikhMasuk = tarikhMasuk;
	}

	public Users getIdKemaskini() {
		return idKemaskini;
	}

	public void setIdKemaskini(Users idKemaskini) {
		this.idKemaskini = idKemaskini;
	}

	public Date getTarikhKemaskini() {
		return tarikhKemaskini;
	}

	public void setTarikhKemaskini(Date tarikhKemaskini) {
		this.tarikhKemaskini = tarikhKemaskini;
	}

}
