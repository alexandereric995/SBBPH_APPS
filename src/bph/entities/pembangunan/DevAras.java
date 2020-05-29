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
@Table(name = "dev_aras")
public class DevAras {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_bangunan")
	private DevBangunan bangunan;

	@Column(name = "kod_dak")
	private String kodDAK;

	@Column(name = "id_jenis_aras")
	private String jenisAras;

	@Column(name = "nama_aras")
	private String namaAras;

	@Column(name = "luas_aras")
	private double luasAras;

	@Column(name = "bil_ruang")
	private int bilRuang;

	@Column(name = "flag_aktif")
	private String flagAktif;

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

	public DevAras() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DevBangunan getBangunan() {
		return bangunan;
	}

	public void setBangunan(DevBangunan bangunan) {
		this.bangunan = bangunan;
	}

	public String getKodDAK() {
		return kodDAK;
	}

	public void setKodDAK(String kodDAK) {
		this.kodDAK = kodDAK;
	}

	public String getJenisAras() {
		return jenisAras;
	}

	public void setJenisAras(String jenisAras) {
		this.jenisAras = jenisAras;
	}

	public String getNamaAras() {
		return namaAras;
	}

	public void setNamaAras(String namaAras) {
		this.namaAras = namaAras;
	}

	public double getLuasAras() {
		return luasAras;
	}

	public void setLuasAras(double luasAras) {
		this.luasAras = luasAras;
	}

	public int getBilRuang() {
		return bilRuang;
	}

	public void setBilRuang(int bilRuang) {
		this.bilRuang = bilRuang;
	}

	public String getFlagAktif() {
		return flagAktif;
	}

	public void setFlagAktif(String flagAktif) {
		this.flagAktif = flagAktif;
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
