package bph.entities.pembangunan;

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
@Table(name = "dev_eot")
public class DevEOT {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_cadangan")
	private DevCadangan cadangan;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_mohon")
	private Date tarikhMohon;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_eot_mohon")
	private Date tarikhEOTMohon;

	@Column(name = "tempoh_eot_mohon")
	private String tempohEOTMohon;

	@Column(name = "catatan_mohon")
	private String catatanMohon;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_eot_lulus")
	private Date tarikhEOTLulus;

	@Column(name = "tempoh_eot_lulus")
	private String tempohEOTLulus;

	@Column(name = "catatan_lulus")
	private String catatanLulus;

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

	public DevEOT() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DevCadangan getCadangan() {
		return cadangan;
	}

	public void setCadangan(DevCadangan cadangan) {
		this.cadangan = cadangan;
	}

	public Date getTarikhMohon() {
		return tarikhMohon;
	}

	public void setTarikhMohon(Date tarikhMohon) {
		this.tarikhMohon = tarikhMohon;
	}

	public Date getTarikhEOTMohon() {
		return tarikhEOTMohon;
	}

	public void setTarikhEOTMohon(Date tarikhEOTMohon) {
		this.tarikhEOTMohon = tarikhEOTMohon;
	}

	public String getTempohEOTMohon() {
		return tempohEOTMohon;
	}

	public void setTempohEOTMohon(String tempohEOTMohon) {
		this.tempohEOTMohon = tempohEOTMohon;
	}

	public String getCatatanMohon() {
		return catatanMohon;
	}

	public void setCatatanMohon(String catatanMohon) {
		this.catatanMohon = catatanMohon;
	}

	public Date getTarikhEOTLulus() {
		return tarikhEOTLulus;
	}

	public void setTarikhEOTLulus(Date tarikhEOTLulus) {
		this.tarikhEOTLulus = tarikhEOTLulus;
	}

	public String getTempohEOTLulus() {
		return tempohEOTLulus;
	}

	public void setTempohEOTLulus(String tempohEOTLulus) {
		this.tempohEOTLulus = tempohEOTLulus;
	}

	public String getCatatanLulus() {
		return catatanLulus;
	}

	public void setCatatanLulus(String catatanLulus) {
		this.catatanLulus = catatanLulus;
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
