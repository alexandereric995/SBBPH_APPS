/**
 * 
 */
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
import bph.utils.Util;

/**
 * @author namira
 * 
 */
@Entity
@Table(name = "dev_ruang")
public class DevRuang {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_aras")
	private DevAras aras;

	@Column(name = "kod_dak")
	private String kodDAK;

	@Column(name = "nama_ruang")
	private String namaRuang;

	@Column(name = "luas_ruang")
	private double luasRuang;

	@Column(name = "tinggi_siling")
	private double tinggiSiling;

	@Column(name = "fungsi_ruang")
	private String fungsiRuang;

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

	public DevRuang() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());
	}

	// public String getKeteranganFlagFungsiRuang() {
	// String keteranganFlagFungsiRuang = "";
	//
	// if (this.flagFungsiRuang != null && this.flagFungsiRuang.trim().length()
	// > 0) {
	// if ("RP".equals(this.fungsiRuang)) {
	// keteranganFlagFungsiRuang = "RUANG PEJABAT";
	// }
	// if ("RK".equals(this.flagFungsiRuang)) {
	// keteranganFlagFungsiRuang = "RUANG KOMERSIL";
	// }
	// if ("KK".equals(this.flagFungsiRuang)) {
	// keteranganFlagFungsiRuang = "KUARTERS KEDIAMAN";
	// }
	// }
	// return keteranganFlagFungsiRuang;
	// }

	public String getLuasInKakiPersegi() {
		Double luasKakiPersegi = 0d;
		luasKakiPersegi = (this.luasRuang * 10.764);
		return Util.formatDecimal(luasKakiPersegi);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DevAras getAras() {
		return aras;
	}

	public void setAras(DevAras aras) {
		this.aras = aras;
	}

	public String getKodDAK() {
		return kodDAK;
	}

	public void setKodDAK(String kodDAK) {
		this.kodDAK = kodDAK;
	}

	public String getNamaRuang() {
		return namaRuang;
	}

	public void setNamaRuang(String namaRuang) {
		this.namaRuang = namaRuang;
	}

	public double getLuasRuang() {
		return luasRuang;
	}

	public void setLuasRuang(double luasRuang) {
		this.luasRuang = luasRuang;
	}

	public double getTinggiSiling() {
		return tinggiSiling;
	}

	public void setTinggiSiling(double tinggiSiling) {
		this.tinggiSiling = tinggiSiling;
	}

	public String getFungsiRuang() {
		return fungsiRuang;
	}

	public void setFungsiRuang(String fungsiRuang) {
		this.fungsiRuang = fungsiRuang;
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
