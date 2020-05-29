package bph.entities.kew;

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
import bph.entities.kod.CaraBayar;

@Entity
@Table(name = "kew_penyata_pemungut")
public class KewPenyataPemungut {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "no_penyata_pemungut")
	private String noPenyataPemungut;

	@Column(name = "no_slip_bank")
	private String noSlipBank;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_penyata_pemungut")
	private Date tarikhPenyataPemungut;

	@Column(name = "no_akaun")
	private String noAkaun;

	@Column(name = "nama_akaun")
	private String namaAkaun;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_kutipan_dari")
	private Date tarikhKutipanDari;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_kutipan_hingga")
	private Date tarikhKutipanHingga;

	@Column(name = "kod_jabatan")
	private String kodJabatan;

	@Column(name = "kod_pusat")
	private String kodPusat;

	@Column(name = "jenis_kod")
	private String jenisKod = "140";

	@ManyToOne
	@JoinColumn(name = "id_kaedah_bayaran")
	private CaraBayar kaedahBayaran;

	@ManyToOne
	@JoinColumn(name = "id_penyedia")
	private Users penyedia; // Disediakan oleh

	@ManyToOne
	@JoinColumn(name = "perakuan_1")
	private Users perakuan1;

	@ManyToOne
	@JoinColumn(name = "perakuan_2")
	private Users perakuan2;

	@Column(name = "jumlah_amaun")
	private Double jumlahAmaun;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_perakuan_1")
	private Date tarikhPerakuan1;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_perakuan_2")
	private Date tarikhPerakuan2;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_disediakan")
	private Date tarikhDisediakan;

	@Column(name = "amaun_perkataan")
	private String amaunPerkataan;

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

	public KewPenyataPemungut() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNoPenyataPemungut() {
		return noPenyataPemungut;
	}

	public void setNoPenyataPemungut(String noPenyataPemungut) {
		this.noPenyataPemungut = noPenyataPemungut;
	}

	public String getNoSlipBank() {
		return noSlipBank;
	}

	public void setNoSlipBank(String noSlipBank) {
		this.noSlipBank = noSlipBank;
	}

	public Date getTarikhPenyataPemungut() {
		return tarikhPenyataPemungut;
	}

	public void setTarikhPenyataPemungut(Date tarikhPenyataPemungut) {
		this.tarikhPenyataPemungut = tarikhPenyataPemungut;
	}

	public String getNoAkaun() {
		return noAkaun;
	}

	public void setNoAkaun(String noAkaun) {
		this.noAkaun = noAkaun;
	}

	public String getNamaAkaun() {
		return namaAkaun;
	}

	public void setNamaAkaun(String namaAkaun) {
		this.namaAkaun = namaAkaun;
	}

	public String getKodJabatan() {
		return kodJabatan;
	}

	public void setKodJabatan(String kodJabatan) {
		this.kodJabatan = kodJabatan;
	}

	public String getJenisKod() {
		return jenisKod;
	}

	public void setJenisKod(String jenisKod) {
		this.jenisKod = jenisKod;
	}

	public CaraBayar getKaedahBayaran() {
		return kaedahBayaran;
	}

	public void setKaedahBayaran(CaraBayar kaedahBayaran) {
		this.kaedahBayaran = kaedahBayaran;
	}

	public Users getPenyedia() {
		return penyedia;
	}

	public void setPenyedia(Users penyedia) {
		this.penyedia = penyedia;
	}

	public Users getPerakuan1() {
		return perakuan1;
	}

	public void setPerakuan1(Users perakuan1) {
		this.perakuan1 = perakuan1;
	}

	public Users getPerakuan2() {
		return perakuan2;
	}

	public void setPerakuan2(Users perakuan2) {
		this.perakuan2 = perakuan2;
	}

	public Double getJumlahAmaun() {
		return jumlahAmaun;
	}

	public void setJumlahAmaun(Double jumlahAmaun) {
		this.jumlahAmaun = jumlahAmaun;
	}

	public Date getTarikhKutipanDari() {
		return tarikhKutipanDari;
	}

	public void setTarikhKutipanDari(Date tarikhKutipanDari) {
		this.tarikhKutipanDari = tarikhKutipanDari;
	}

	public Date getTarikhKutipanHingga() {
		return tarikhKutipanHingga;
	}

	public void setTarikhKutipanHingga(Date tarikhKutipanHingga) {
		this.tarikhKutipanHingga = tarikhKutipanHingga;
	}

	public String getKodPusat() {
		return kodPusat;
	}

	public void setKodPusat(String kodPusat) {
		this.kodPusat = kodPusat;
	}

	public Date getTarikhPerakuan1() {
		return tarikhPerakuan1;
	}

	public void setTarikhPerakuan1(Date tarikhPerakuan1) {
		this.tarikhPerakuan1 = tarikhPerakuan1;
	}

	public Date getTarikhPerakuan2() {
		return tarikhPerakuan2;
	}

	public void setTarikhPerakuan2(Date tarikhPerakuan2) {
		this.tarikhPerakuan2 = tarikhPerakuan2;
	}

	public Date getTarikhDisediakan() {
		return tarikhDisediakan;
	}

	public void setTarikhDisediakan(Date tarikhDisediakan) {
		this.tarikhDisediakan = tarikhDisediakan;
	}

	public String getAmaunPerkataan() {
		return amaunPerkataan;
	}

	public void setAmaunPerkataan(String amaunPerkataan) {
		this.amaunPerkataan = amaunPerkataan;
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
