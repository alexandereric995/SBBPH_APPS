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
@Table(name = "rpp_jadual_tempahan")
public class RppJadualTempahan {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_unit")
	private RppUnit unit;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_mula")
	private Date tarikhMula;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_tamat")
	private Date tarikhTamat;

	@Column(name = "status")
	private String status;

	@ManyToOne
	@JoinColumn(name = "id_permohonan")
	private RppPermohonan permohonan;

	@Column(name = "flag_status_tempahan")
	private String flagStatusTempahan;

	@ManyToOne
	@JoinColumn(name = "id_kelompok_unit")
	private RppKelompokUnit kelompokUnit;

	@ManyToOne
	@JoinColumn(name = "id_unit_confirm")
	private RppUnit unitConfirm;

	@Column(name = "catatan")
	private String catatan;

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

	public RppJadualTempahan() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public RppUnit getUnit() {
		return unit;
	}

	public void setUnit(RppUnit unit) {
		this.unit = unit;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public RppPermohonan getPermohonan() {
		return permohonan;
	}

	public void setPermohonan(RppPermohonan permohonan) {
		this.permohonan = permohonan;
	}

	public String getFlagStatusTempahan() {
		return flagStatusTempahan;
	}

	public void setFlagStatusTempahan(String flagStatusTempahan) {
		this.flagStatusTempahan = flagStatusTempahan;
	}

	public RppKelompokUnit getKelompokUnit() {
		return kelompokUnit;
	}

	public void setKelompokUnit(RppKelompokUnit kelompokUnit) {
		this.kelompokUnit = kelompokUnit;
	}

	public RppUnit getUnitConfirm() {
		return unitConfirm;
	}

	public void setUnitConfirm(RppUnit unitConfirm) {
		this.unitConfirm = unitConfirm;
	}

	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
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
