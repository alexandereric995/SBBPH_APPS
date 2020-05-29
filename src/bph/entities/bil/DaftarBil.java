package bph.entities.bil;

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
import bph.entities.kod.Bandar;
import bph.entities.kod.KodBil;
import bph.entities.kod.Seksyen;
import bph.entities.rpp.RppPeranginan;

@Entity
@Table(name = "bil_maklumat_bil")
public class DaftarBil {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_kod_bil")
	private KodBil jenisBil;

	@Column(name = "penerima_bayaran")
	private String penerimaBayaran;

	@Column(name = "no_akaun")
	private String noAkaun;

	@Column(name = "alamat1")
	private String alamat1;

	@Column(name = "alamat2")
	private String alamat2;

	@Column(name = "alamat3")
	private String alamat3;

	@Column(name = "poskod")
	private String poskod;

	@ManyToOne
	@JoinColumn(name = "bandar")
	private Bandar bandar;

	@Column(name = "catatan")
	private String catatan;

	@ManyToOne
	@JoinColumn(name = "id_seksyen")
	private Seksyen seksyen;

	// @Column(name="id_peranginan")
	// private String idPeranginan;
	@ManyToOne
	@JoinColumn(name = "id_peranginan")
	private RppPeranginan idPeranginan;

	@Column(name = "nama_pegawai")
	private String namaPegawai;

	@Column(name = "status")
	private String status;

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

	public DaftarBil() {
		// TODO Auto-generated constructor stub
		setId(UID.getUID());
		setTarikhMasuk(new Date());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public KodBil getJenisBil() {
		return jenisBil;
	}

	public void setJenisBil(KodBil jenisBil) {
		this.jenisBil = jenisBil;
	}

	public String getPenerimaBayaran() {
		return penerimaBayaran;
	}

	public void setPenerimaBayaran(String penerimaBayaran) {
		this.penerimaBayaran = penerimaBayaran;
	}

	public String getNoAkaun() {
		return noAkaun;
	}

	public void setNoAkaun(String noAkaun) {
		this.noAkaun = noAkaun;
	}

	public String getAlamat1() {
		return alamat1;
	}

	public void setAlamat1(String alamat1) {
		this.alamat1 = alamat1;
	}

	public String getAlamat2() {
		return alamat2;
	}

	public void setAlamat2(String alamat2) {
		this.alamat2 = alamat2;
	}

	public String getAlamat3() {
		return alamat3;
	}

	public void setAlamat3(String alamat3) {
		this.alamat3 = alamat3;
	}

	public String getPoskod() {
		return poskod;
	}

	public void setPoskod(String poskod) {
		this.poskod = poskod;
	}

	public Bandar getBandar() {
		return bandar;
	}

	public void setBandar(Bandar bandar) {
		this.bandar = bandar;
	}

	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}

	public Seksyen getSeksyen() {
		return seksyen;
	}

	public void setSeksyen(Seksyen seksyen) {
		this.seksyen = seksyen;
	}

	// public String getIdPeranginan() {
	// return idPeranginan;
	// }
	//
	// public void setIdPeranginan(String idPeranginan) {
	// this.idPeranginan = idPeranginan;
	// }

	public String getNamaPegawai() {
		return namaPegawai;
	}

	public RppPeranginan getIdPeranginan() {
		return idPeranginan;
	}

	public void setIdPeranginan(RppPeranginan idPeranginan) {
		this.idPeranginan = idPeranginan;
	}

	public void setNamaPegawai(String namaPegawai) {
		this.namaPegawai = namaPegawai;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
