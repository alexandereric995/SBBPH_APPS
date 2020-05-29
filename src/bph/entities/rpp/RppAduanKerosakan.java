package bph.entities.rpp;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lebah.template.UID;
import portal.module.entity.Users;
import bph.entities.kod.Status;

@Entity
@Table(name = "rpp_aduan_kerosakan")
public class RppAduanKerosakan {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_barang_deposit")
	private RppTetapanBarangDeposit barangDeposit;

	@ManyToOne
	@JoinColumn(name = "id_permohonan")
	private RppPermohonan permohonan;

	@ManyToOne
	@JoinColumn(name = "id_peranginan")
	private RppPeranginan peranginan;

	@Column(name = "keterangan")
	private String keterangan;

	@Column(name = "tarikh_aduan")
	@Temporal(TemporalType.DATE)
	private Date tarikhAduan;

	@Column(name = "kuantiti")
	private Integer kuantiti;

	@Column(name = "harga")
	private Double harga;

	@ManyToOne
	@JoinColumn(name = "id_pengadu")
	private Users pengadu;

	@ManyToOne
	@JoinColumn(name = "id_status")
	private Status status;

	@Column(name = "tarikh_terima_aduan")
	@Temporal(TemporalType.DATE)
	private Date tarikhTerimaAduan;

	@Column(name = "tarikh_aduan_selesai")
	@Temporal(TemporalType.DATE)
	private Date tarikhAduanSelesai;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "aduanKerosakan", fetch = FetchType.LAZY)
	private List<RppGambarAduan> listGambar;

	@Column(name = "ulasan_hq")
	private String ulasanHq;

	@ManyToOne
	@JoinColumn(name = "id_pengulas_hq")
	private Users pengulasHq;

	@Column(name = "tarikh_ulasan_hq")
	@Temporal(TemporalType.DATE)
	private Date tarikhUlasanHq;

	@Column(name = "flag_selesai")
	private String flagSelesai;

	@Column(name = "baucer_jurnal")
	private String baucerJurnal;

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

	public RppAduanKerosakan() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public RppPermohonan getPermohonan() {
		return permohonan;
	}

	public void setPermohonan(RppPermohonan permohonan) {
		this.permohonan = permohonan;
	}

	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}

	public Date getTarikhAduan() {
		return tarikhAduan;
	}

	public void setTarikhAduan(Date tarikhAduan) {
		this.tarikhAduan = tarikhAduan;
	}

	public Date getTarikhAduanSelesai() {
		return tarikhAduanSelesai;
	}

	public void setTarikhAduanSelesai(Date tarikhAduanSelesai) {
		this.tarikhAduanSelesai = tarikhAduanSelesai;
	}

	public List<RppGambarAduan> getListGambar() {
		return listGambar;
	}

	public void setListGambar(List<RppGambarAduan> listGambar) {
		this.listGambar = listGambar;
	}

	public String getUlasanHq() {
		return ulasanHq;
	}

	public void setUlasanHq(String ulasanHq) {
		this.ulasanHq = ulasanHq;
	}

	public Date getTarikhUlasanHq() {
		return tarikhUlasanHq;
	}

	public void setTarikhUlasanHq(Date tarikhUlasanHq) {
		this.tarikhUlasanHq = tarikhUlasanHq;
	}

	public String getFlagSelesai() {
		return flagSelesai;
	}

	public void setFlagSelesai(String flagSelesai) {
		this.flagSelesai = flagSelesai;
	}

	public RppTetapanBarangDeposit getBarangDeposit() {
		return barangDeposit;
	}

	public void setBarangDeposit(RppTetapanBarangDeposit barangDeposit) {
		this.barangDeposit = barangDeposit;
	}

	public RppPeranginan getPeranginan() {
		return peranginan;
	}

	public void setPeranginan(RppPeranginan peranginan) {
		this.peranginan = peranginan;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Date getTarikhTerimaAduan() {
		return tarikhTerimaAduan;
	}

	public void setTarikhTerimaAduan(Date tarikhTerimaAduan) {
		this.tarikhTerimaAduan = tarikhTerimaAduan;
	}

	public Users getPengulasHq() {
		return pengulasHq;
	}

	public void setPengulasHq(Users pengulasHq) {
		this.pengulasHq = pengulasHq;
	}

	public Users getPengadu() {
		return pengadu;
	}

	public void setPengadu(Users pengadu) {
		this.pengadu = pengadu;
	}

	public Integer getKuantiti() {
		return kuantiti;
	}

	public void setKuantiti(Integer kuantiti) {
		this.kuantiti = kuantiti;
	}

	public Double getHarga() {
		return harga;
	}

	public void setHarga(Double harga) {
		this.harga = harga;
	}

	public String getBaucerJurnal() {
		return baucerJurnal;
	}

	public void setBaucerJurnal(String baucerJurnal) {
		this.baucerJurnal = baucerJurnal;
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
