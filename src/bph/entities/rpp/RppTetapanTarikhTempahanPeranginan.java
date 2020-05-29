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

@Entity
@Table(name = "rpp_tetapan_tarikh_tempahan_peranginan")
public class RppTetapanTarikhTempahanPeranginan {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_buka_tempahan")
	private RppTetapanBukaTempahan tetapanBukaTempahan;

	@ManyToOne
	@JoinColumn(name = "id_peranginan")
	private RppPeranginan peranginan;

	@OneToOne
	@JoinColumn(name = "id_masuk")
	private Users idMasuk;

	@OneToOne
	@JoinColumn(name = "id_kemaskini")
	private Users idKemaskini;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_masuk")
	private Date tarikhMasuk;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_kemaskini")
	private Date tarikhKemaskini;

	@Column(name = "flag_aktif")
	private String flagAktif;

	public RppTetapanTarikhTempahanPeranginan() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public RppTetapanBukaTempahan getTetapanBukaTempahan() {
		return tetapanBukaTempahan;
	}

	public void setTetapanBukaTempahan(
			RppTetapanBukaTempahan tetapanBukaTempahan) {
		this.tetapanBukaTempahan = tetapanBukaTempahan;
	}

	public RppPeranginan getPeranginan() {
		return peranginan;
	}

	public void setPeranginan(RppPeranginan peranginan) {
		this.peranginan = peranginan;
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

	public String getFlagAktif() {
		return flagAktif;
	}

	public void setFlagAktif(String flagAktif) {
		this.flagAktif = flagAktif;
	}

}
