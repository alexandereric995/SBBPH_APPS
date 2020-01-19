package bph.entities.kewangan;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lebah.template.UID;

@Entity
@Table(name="kew_deposit_warta")
public class KewDepositWarta {

	@Id
	@Column(name = "id")
	private String id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_warta", nullable = false)
	private KewWarta warta;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_deposit", nullable = false)
	private KewDeposit deposit;
	
	
	public KewDepositWarta() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public KewWarta getWarta() {
		return warta;
	}

	public void setWarta(KewWarta warta) {
		this.warta = warta;
	}

	public KewDeposit getDeposit() {
		return deposit;
	}

	public void setDeposit(KewDeposit deposit) {
		this.deposit = deposit;
	}
	
}
