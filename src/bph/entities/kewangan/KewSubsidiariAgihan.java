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
	private Users penyelia;
	/** atau penyemak */

	@ManyToOne
	@JoinColumn(name = "id_penyedia")
	private Users penyedia;

	@Column(name = "catatan_penyelia")
	private String catatanPenyelia;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_agihan")
	private Date tarikhAgihan;

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

	public KewSubsidiariAgihan() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());
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
