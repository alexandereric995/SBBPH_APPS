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
import bph.entities.kew.KewJenisBayaran;
import bph.entities.kod.Status;

@Entity
@Table(name = "kew_subsidiari")
public class KewSubsidiari {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "id_fail")
	private String idFail; // id kuarters / permohonan ir

	@ManyToOne
	@JoinColumn(name = "id_jenis_subsidiari")
	private KewJenisBayaran jenisSubsidiari;
	/** 01=kua / 02=ir / dll */

	@ManyToOne
	@JoinColumn(name = "id_pemohon")
	private Users pemohon;

	@ManyToOne
	@JoinColumn(name = "id_status")
	private Status status;

	@OneToOne
	@JoinColumn(name = "id_agihan")
	private KewSubsidiariAgihan agihan;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_permohonan")
	private Date tarikhPermohonan;

	@Column(name = "justifikasi_pemohon")
	private String justifikasiPemohon;

	@Column(name = "no_baucer_bayaran")
	private String noBaucerBayaran;

	/** Senarai semak */
	@Column(name = "flag_sijil_akuan_masuk")
	private String flagSijilAkuanMasuk;

	@Column(name = "flag_sijil_akuan_keluar")
	private String flagSijilAkuanKeluar;

	@Column(name = "flag_resit_bayaran")
	private String flagResitBayaran;

	@Column(name = "flag_surat_sokongan")
	private String flagSuratSokongan;

	@Column(name = "flag_salinan_akaun_bank")
	private String flagSalinanAkaunBank;

	@Column(name = "flag_surat_tawaran")
	private String flagSuratTawaran;
	/** Senarai semak */

	@Column(name = "no_eft_bayaran")
	private String noEFT;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_baucer_bayaran")
	private Date tarikhBaucer;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_eft_bayaran")
	private Date tarikhEFT;

	@ManyToOne
	@JoinColumn(name = "id_daftar")
	private Users daftarOleh;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_masuk")
	private Date tarikhMasuk;

	@ManyToOne
	@JoinColumn(name = "id_kemaskini")
	private Users kemaskiniOleh;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_kemaskini")
	private Date tarikhKemaskini;

	public KewSubsidiari() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdFail() {
		return idFail;
	}

	public void setIdFail(String idFail) {
		this.idFail = idFail;
	}

	public KewJenisBayaran getJenisSubsidiari() {
		return jenisSubsidiari;
	}

	public void setJenisSubsidiari(KewJenisBayaran jenisSubsidiari) {
		this.jenisSubsidiari = jenisSubsidiari;
	}

	public Users getPemohon() {
		return pemohon;
	}

	public void setPemohon(Users pemohon) {
		this.pemohon = pemohon;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public KewSubsidiariAgihan getAgihan() {
		return agihan;
	}

	public void setAgihan(KewSubsidiariAgihan agihan) {
		this.agihan = agihan;
	}

	public Date getTarikhPermohonan() {
		return tarikhPermohonan;
	}

	public void setTarikhPermohonan(Date tarikhPermohonan) {
		this.tarikhPermohonan = tarikhPermohonan;
	}

	public String getJustifikasiPemohon() {
		return justifikasiPemohon;
	}

	public void setJustifikasiPemohon(String justifikasiPemohon) {
		this.justifikasiPemohon = justifikasiPemohon;
	}

	public String getNoBaucerBayaran() {
		return noBaucerBayaran;
	}

	public void setNoBaucerBayaran(String noBaucerBayaran) {
		this.noBaucerBayaran = noBaucerBayaran;
	}

	public String getFlagSijilAkuanMasuk() {
		return flagSijilAkuanMasuk;
	}

	public void setFlagSijilAkuanMasuk(String flagSijilAkuanMasuk) {
		this.flagSijilAkuanMasuk = flagSijilAkuanMasuk;
	}

	public String getFlagSijilAkuanKeluar() {
		return flagSijilAkuanKeluar;
	}

	public void setFlagSijilAkuanKeluar(String flagSijilAkuanKeluar) {
		this.flagSijilAkuanKeluar = flagSijilAkuanKeluar;
	}

	public String getFlagResitBayaran() {
		return flagResitBayaran;
	}

	public void setFlagResitBayaran(String flagResitBayaran) {
		this.flagResitBayaran = flagResitBayaran;
	}

	public String getFlagSuratSokongan() {
		return flagSuratSokongan;
	}

	public void setFlagSuratSokongan(String flagSuratSokongan) {
		this.flagSuratSokongan = flagSuratSokongan;
	}

	public String getFlagSalinanAkaunBank() {
		return flagSalinanAkaunBank;
	}

	public void setFlagSalinanAkaunBank(String flagSalinanAkaunBank) {
		this.flagSalinanAkaunBank = flagSalinanAkaunBank;
	}

	public String getFlagSuratTawaran() {
		return flagSuratTawaran;
	}

	public void setFlagSuratTawaran(String flagSuratTawaran) {
		this.flagSuratTawaran = flagSuratTawaran;
	}

	public String getNoEFT() {
		return noEFT;
	}

	public void setNoEFT(String noEFT) {
		this.noEFT = noEFT;
	}

	public Date getTarikhBaucer() {
		return tarikhBaucer;
	}

	public void setTarikhBaucer(Date tarikhBaucer) {
		this.tarikhBaucer = tarikhBaucer;
	}

	public Date getTarikhEFT() {
		return tarikhEFT;
	}

	public void setTarikhEFT(Date tarikhEFT) {
		this.tarikhEFT = tarikhEFT;
	}

	public Users getDaftarOleh() {
		return daftarOleh;
	}

	public void setDaftarOleh(Users daftarOleh) {
		this.daftarOleh = daftarOleh;
	}

	public Date getTarikhMasuk() {
		return tarikhMasuk;
	}

	public void setTarikhMasuk(Date tarikhMasuk) {
		this.tarikhMasuk = tarikhMasuk;
	}

	public Users getKemaskiniOleh() {
		return kemaskiniOleh;
	}

	public void setKemaskiniOleh(Users kemaskiniOleh) {
		this.kemaskiniOleh = kemaskiniOleh;
	}

	public Date getTarikhKemaskini() {
		return tarikhKemaskini;
	}

	public void setTarikhKemaskini(Date tarikhKemaskini) {
		this.tarikhKemaskini = tarikhKemaskini;
	}

}
