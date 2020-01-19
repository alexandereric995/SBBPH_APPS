package bph.entities.kewangan;

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

@Entity
@Table(name = "kew_subsidiari_agihan")
public class KewSubsidiariAgihan {

	@Id
	@Column(name = "id")
	private String id;
	
	@OneToOne
	@JoinColumn(name = "id_subsidiari")
	private KewSubsidiari subsidiari;
	
	@ManyToOne
	@JoinColumn(name = "id_penyelia")
	private Users penyelia; /** atau penyemak */
	
	@ManyToOne
	@JoinColumn(name = "id_penyedia")
	private Users penyedia;
	
	@Column(name = "catatan_penyelia")
	private String catatanPenyelia;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_agihan")
	private Date tarikhAgihan;
	
	
	public KewSubsidiariAgihan() {
		setId(UID.getUID());
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Users getPenyelia() {
		return penyelia;
	}

	public void setPenyelia(Users penyelia) {
		this.penyelia = penyelia;
	}

	public Users getPenyedia() {
		return penyedia;
	}

	public void setPenyedia(Users penyedia) {
		this.penyedia = penyedia;
	}

	public String getCatatanPenyelia() {
		return catatanPenyelia;
	}

	public void setCatatanPenyelia(String catatanPenyelia) {
		this.catatanPenyelia = catatanPenyelia;
	}

	public Date getTarikhAgihan() {
		return tarikhAgihan;
	}

	public void setTarikhAgihan(Date tarikhAgihan) {
		this.tarikhAgihan = tarikhAgihan;
	}

	public KewSubsidiari getSubsidiari() {
		return subsidiari;
	}

	public void setSubsidiari(KewSubsidiari subsidiari) {
		this.subsidiari = subsidiari;
	}
	
}
