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
@Table(name = "rpp_penggunaan_kemudahan")
public class RppPenggunaanKemudahan {

	@Id
	@Column(name = "id")
	private String id;

	@OneToOne
	@JoinColumn(name = "id_permohonan")
	private RppPermohonan permohonan;

	@OneToOne
	@JoinColumn(name = "id_pemohon")
	private Users pemohon;

	@OneToOne
	@JoinColumn(name = "id_kemudahan")
	private RppKemudahan kemudahan;

	@Column(name = "flag_jenis_pemohon")
	private String flagJenisPemohon; // MENGINAP / TIDAK MENGINAP

	@Column(name = "bilangan")
	private int bilangan;

	@Column(name = "jumlah_kadar_sewa")
	private Double jumlahKadarSewa;

	@Column(name = "catatan")
	private String catatan;

	@Column(name = "tarikh_mula_guna")
	@Temporal(TemporalType.DATE)
	private Date tarikhMulaGuna;

	@Column(name = "tarikh_tamat_guna")
	@Temporal(TemporalType.DATE)
	private Date tarikhTamatGuna;

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

	public RppPenggunaanKemudahan() {
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

	public Users getPemohon() {
		return pemohon;
	}

	public void setPemohon(Users pemohon) {
		this.pemohon = pemohon;
	}

	public RppKemudahan getKemudahan() {
		return kemudahan;
	}

	public void setKemudahan(RppKemudahan kemudahan) {
		this.kemudahan = kemudahan;
	}

	public String getFlagJenisPemohon() {
		return flagJenisPemohon;
	}

	public void setFlagJenisPemohon(String flagJenisPemohon) {
		this.flagJenisPemohon = flagJenisPemohon;
	}

	public int getBilangan() {
		return bilangan;
	}

	public void setBilangan(int bilangan) {
		this.bilangan = bilangan;
	}

	public Double getJumlahKadarSewa() {
		return jumlahKadarSewa;
	}

	public void setJumlahKadarSewa(Double jumlahKadarSewa) {
		this.jumlahKadarSewa = jumlahKadarSewa;
	}

	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}

	public Date getTarikhMulaGuna() {
		return tarikhMulaGuna;
	}

	public void setTarikhMulaGuna(Date tarikhMulaGuna) {
		this.tarikhMulaGuna = tarikhMulaGuna;
	}

	public Date getTarikhTamatGuna() {
		return tarikhTamatGuna;
	}

	public void setTarikhTamatGuna(Date tarikhTamatGuna) {
		this.tarikhTamatGuna = tarikhTamatGuna;
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
