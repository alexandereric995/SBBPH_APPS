/**
 * 
 */
package bph.entities.pembangunan;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lebah.template.UID;
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

	public DevRuang() {
		setId(UID.getUID());
	}
	
//	public String getKeteranganFlagFungsiRuang() {
//		String keteranganFlagFungsiRuang = "";
//
//		if (this.flagFungsiRuang != null && this.flagFungsiRuang.trim().length() > 0) {
//			if ("RP".equals(this.fungsiRuang)) {
//				keteranganFlagFungsiRuang = "RUANG PEJABAT";
//			}
//			if ("RK".equals(this.flagFungsiRuang)) {
//				keteranganFlagFungsiRuang = "RUANG KOMERSIL";
//			}
//			if ("KK".equals(this.flagFungsiRuang)) {
//				keteranganFlagFungsiRuang = "KUARTERS KEDIAMAN";
//			}
//		}
//		return keteranganFlagFungsiRuang;
//	}
	
	public String getLuasInKakiPersegi(){
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
}
