package bph.entities.pembangunan;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lebah.template.UID;

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

	public DevAras() {
		setId(UID.getUID());
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
}
