package bph.entities.kewangan;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lebah.template.UID;

@Entity
@Table(name = "kew_resit_senarai_invois")
public class KewResitSenaraiInvois {

	@Id
	@Column(name = "id")
	private String id;
	
	@ManyToOne(cascade={CascadeType.PERSIST},fetch=FetchType.LAZY)
	@JoinColumn(name = "id_bayaran_resit")
	private KewBayaranResit resit;
	
	@OneToOne
	@JoinColumn(name = "id_invois")
	private KewInvois invois;
	
	@OneToOne
	@JoinColumn(name = "id_deposit")
	private KewDeposit deposit;
	
	@Column(name = "flag_jenis_resit")
	private String flagJenisResit = "INVOIS"; // INVOIS / DEPOSIT - DEFAULT INVOIS
	
	
	public KewResitSenaraiInvois() {
		setId(UID.getUID());
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
	
}
