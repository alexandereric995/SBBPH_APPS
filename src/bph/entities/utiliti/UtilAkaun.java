package bph.entities.utiliti;

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
import bph.entities.kod.KodHasil;

@Entity
@Table(name = "util_akaun")
public class UtilAkaun {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_permohonan")
	private UtilPermohonan permohonan;

	@ManyToOne
	@JoinColumn(name = "id_kod_hasil")
	private KodHasil kodHasil; // KOD HASIL KEWANGAN

	@Column(name = "no_invois")
	private String noInvois;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_invois")
	private Date tarikhInvois; // TARIKH BILA TEMPAHAN DIBUAT

	@Column(name = "keterangan")
	private String keterangan; // KETERANGAN BAYARAN

	@Column(name = "catatan")
	private String catatan;

	@Column(name = "amaun_bayaran_seunit")
	private Double amaunBayaranSeunit;

	@Column(name = "bilangan_unit")
	private Integer bilanganUnit;

	@Column(name = "debit")
	private Double debit;

	@Column(name = "kredit")
	private Double kredit;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_transaksi")
	private Date tarikhTransaksi; // TARIKH BAYARAN DIBUAT

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_void")
	private Date tarikhVoid; // TARIKH BATAL BAYARAN

	@Column(name = "flag_void")
	private String flagVoid; // Y / T

	@Column(name = "amaun_void")
	private Double amaunVoid;

	@Column(name = "flag_bayar")
	private String flagBayar; // Y = TELAH BAYAR / T = BELUM

	@Column(name = "no_lo_tempahan")
	private String noLoTempahan;

	@Column(name = "no_resit")
	private String noResit;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_resit")
	private Date tarikhResit;

	@ManyToOne
	@JoinColumn(name = "id_masuk")
	private Users idMasuk;

	@ManyToOne
	@JoinColumn(name = "id_kemaskini")
	private Users idKemaskini;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_masuk")
	private Date tarikhMasuk;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_kemaskini")
	private Date tarikhKemaskini;

	@Column(name = "id_pembayar")
	private String idPembayar;

	public UtilAkaun() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public UtilPermohonan getPermohonan() {
		return permohonan;
	}

	public void setPermohonan(UtilPermohonan permohonan) {
		this.permohonan = permohonan;
	}

	public KodHasil getKodHasil() {
		return kodHasil;
	}

	public void setKodHasil(KodHasil kodHasil) {
		this.kodHasil = kodHasil;
	}

	public String getNoInvois() {
		return noInvois;
	}

	public void setNoInvois(String noInvois) {
		this.noInvois = noInvois;
	}

	public Date getTarikhInvois() {
		return tarikhInvois;
	}

	public void setTarikhInvois(Date tarikhInvois) {
		this.tarikhInvois = tarikhInvois;
	}

	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}

	public Double getDebit() {
		return debit;
	}

	public void setDebit(Double debit) {
		this.debit = debit;
	}

	public Double getKredit() {
		return kredit;
	}

	public void setKredit(Double kredit) {
		this.kredit = kredit;
	}

	public Date getTarikhTransaksi() {
		return tarikhTransaksi;
	}

	public void setTarikhTransaksi(Date tarikhTransaksi) {
		this.tarikhTransaksi = tarikhTransaksi;
	}

	public String getFlagVoid() {
		return flagVoid;
	}

	public void setFlagVoid(String flagVoid) {
		this.flagVoid = flagVoid;
	}

	public Double getAmaunVoid() {
		return amaunVoid;
	}

	public void setAmaunVoid(Double amaunVoid) {
		this.amaunVoid = amaunVoid;
	}

	public String getFlagBayar() {
		return flagBayar;
	}

	public void setFlagBayar(String flagBayar) {
		this.flagBayar = flagBayar;
	}

	public String getNoLoTempahan() {
		return noLoTempahan;
	}

	public void setNoLoTempahan(String noLoTempahan) {
		this.noLoTempahan = noLoTempahan;
	}

	public String getNoResit() {
		return noResit;
	}

	public void setNoResit(String noResit) {
		this.noResit = noResit;
	}

	public Date getTarikhResit() {
		return tarikhResit;
	}

	public void setTarikhResit(Date tarikhResit) {
		this.tarikhResit = tarikhResit;
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

	public Double getAmaunBayaranSeunit() {
		return amaunBayaranSeunit;
	}

	public void setAmaunBayaranSeunit(Double amaunBayaranSeunit) {
		this.amaunBayaranSeunit = amaunBayaranSeunit;
	}

	public Integer getBilanganUnit() {
		return bilanganUnit;
	}

	public void setBilanganUnit(Integer bilanganUnit) {
		this.bilanganUnit = bilanganUnit;
	}

	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}

	public Date getTarikhVoid() {
		return tarikhVoid;
	}

	public void setTarikhVoid(Date tarikhVoid) {
		this.tarikhVoid = tarikhVoid;
	}

	public Double bakiBayaran() {
		Double baki = 0d;
		if (this.debit != null && this.kredit != null) {
			baki = this.debit - this.kredit;
		}
		return baki;
	}

	public void setIdPembayar(String idPembayar) {
		this.idPembayar = idPembayar;
	}

	public String getIdPembayar() {
		return idPembayar;
	}

}
