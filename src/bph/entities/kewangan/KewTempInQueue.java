package bph.entities.kewangan;

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
@Table(name = "kew_temp_in_queue")
public class KewTempInQueue {

	@Id
	@Column(name = "id")
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "id_invois")
	private KewInvois invois;
	
	@ManyToOne
	@JoinColumn(name = "id_deposit")
	private KewDeposit deposit;
	
	@ManyToOne
	@JoinColumn(name = "id_pembayar")
	private Users pembayar;
	
	@ManyToOne
	@JoinColumn(name = "id_pembayar_lain")
	private PembayarLain pembayarLain;
	
	@Column(name = "amaun_bayaran")
	private Double amaunBayaran = 0D;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_dari")
	private Date tarikhDari;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_hingga")
	private Date tarikhHingga;
	
	public KewTempInQueue() {
		setId(UID.getUID());
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Users getPembayar() {
		return pembayar;
	}

	public void setPembayar(Users pembayar) {
		this.pembayar = pembayar;
	}

	public PembayarLain getPembayarLain() {
		return pembayarLain;
	}

	public void setPembayarLain(PembayarLain pembayarLain) {
		this.pembayarLain = pembayarLain;
	}

	public Double getAmaunBayaran() {
		return amaunBayaran;
	}

	public void setAmaunBayaran(Double amaunBayaran) {
		this.amaunBayaran = amaunBayaran;
	}

	public Date getTarikhDari() {
		return tarikhDari;
	}

	public void setTarikhDari(Date tarikhDari) {
		this.tarikhDari = tarikhDari;
	}

	public Date getTarikhHingga() {
		return tarikhHingga;
	}

	public void setTarikhHingga(Date tarikhHingga) {
		this.tarikhHingga = tarikhHingga;
	}
}
