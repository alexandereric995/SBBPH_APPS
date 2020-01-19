package bph.entities.kew;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lebah.template.UID;
import portal.module.entity.Users;
import bph.entities.kod.KodJenisPerolehan;
import bph.entities.kod.Status;

/**
 * @author muhdsyazreen
 * @description : - Perolehan (daftar/semak/sah)
 */

@Entity
@Table(name = "kew_perolehan")
public class KewPerolehan {

	@Id
	@Column(name = "id")
	private String id;

	@OneToOne
	@JoinColumn(name = "id_jenis_perolehan")
	private KodJenisPerolehan kodJenisPerolehan;

	@Column(name = "justifikasi")
	private String justifikasi;

	@Column(name = "tajuk")
	private String tajuk;

	@Column(name = "unit")
	private String unit;

	@Column(name = "catatan_sokongan")
	private String catatanSokongan;

	@Column(name = "catatan_lulus")
	private String catatanLulus;

	@Column(name = "flag_sokong")
	private String flagSokong;

	@Column(name = "flag_lulus")
	private String flagLulus;

	@OneToOne
	@JoinColumn(name = "id_status")
	private Status status;

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

	public KewPerolehan() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getJustifikasi() {
		return justifikasi;
	}

	public void setJustifikasi(String justifikasi) {
		this.justifikasi = justifikasi;
	}

	public String getTajuk() {
		return tajuk;
	}

	public void setTajuk(String tajuk) {
		this.tajuk = tajuk;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
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

	public String getCatatanSokongan() {
		return catatanSokongan;
	}

	public void setCatatanSokongan(String catatanSokongan) {
		this.catatanSokongan = catatanSokongan;
	}

	public String getCatatanLulus() {
		return catatanLulus;
	}

	public void setCatatanLulus(String catatanLulus) {
		this.catatanLulus = catatanLulus;
	}

	public String getFlagSokong() {
		return flagSokong;
	}

	public void setFlagSokong(String flagSokong) {
		this.flagSokong = flagSokong;
	}

	public String getFlagLulus() {
		return flagLulus;
	}

	public void setFlagLulus(String flagLulus) {
		this.flagLulus = flagLulus;
	}

	public KodJenisPerolehan getKodJenisPerolehan() {
		return kodJenisPerolehan;
	}

	public void setKodJenisPerolehan(KodJenisPerolehan kodJenisPerolehan) {
		this.kodJenisPerolehan = kodJenisPerolehan;
	}

}
