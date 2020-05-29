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
import bph.entities.kod.Agensi;
import bph.entities.kod.Bandar;
import bph.entities.kod.Mukim;
import bph.entities.kod.Zon;

@Entity
@Table(name = "dev_premis")
public class DevPremis {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_zon")
	private Zon zon;

	@Column(name = "kod_dpa")
	private String kodDPA;

	@Column(name = "id_pemilik")
	private String pemilikPremis;

	@ManyToOne
	@JoinColumn(name = "id_mukim")
	private Mukim mukim;

	@ManyToOne
	@JoinColumn(name = "id_agensi")
	private Agensi agensi;

	@Column(name = "id_kategori_premis")
	private String kategoriPremis;

	@Column(name = "id_subkategori_premis")
	private String subkategoriPremis;

	@Column(name = "nama_premis")
	private String namaPremis;

	@Column(name = "alamat1")
	private String alamat1;

	@Column(name = "alamat2")
	private String alamat2;

	@Column(name = "alamat3")
	private String alamat3;

	@Column(name = "poskod")
	private int poskod;

	@ManyToOne
	@JoinColumn(name = "id_bandar")
	private Bandar bandar;

	@Column(name = "luas_premis")
	private double luasPpremis;

	@Column(name = "luas_bangunan")
	private double luasBangunan;

	@Column(name = "luas_binaan_luar")
	private double luasBinaanLuar;

	@Column(name = "kos_asal")
	private double kosAsal;

	@Column(name = "kos_tambahan")
	private double kosTambahan;

	@Column(name = "jumlah_kos")
	private double jumlahKos;

	@Column(name = "tahun_siap_bina")
	private int tahunSiapBina;

	@Column(name = "catatan")
	private String catatan;

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

	public DevPremis() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Zon getZon() {
		return zon;
	}

	public void setZon(Zon zon) {
		this.zon = zon;
	}

	public String getKodDPA() {
		return kodDPA;
	}

	public void setKodDPA(String kodDPA) {
		this.kodDPA = kodDPA;
	}

	public String getPemilikPremis() {
		return pemilikPremis;
	}

	public void setPemilikPremis(String pemilikPremis) {
		this.pemilikPremis = pemilikPremis;
	}

	public Mukim getMukim() {
		return mukim;
	}

	public void setMukim(Mukim mukim) {
		this.mukim = mukim;
	}

	public Agensi getAgensi() {
		return agensi;
	}

	public void setAgensi(Agensi agensi) {
		this.agensi = agensi;
	}

	public String getKategoriPremis() {
		return kategoriPremis;
	}

	public void setKategoriPremis(String kategoriPremis) {
		this.kategoriPremis = kategoriPremis;
	}

	public String getSubkategoriPremis() {
		return subkategoriPremis;
	}

	public void setSubkategoriPremis(String subkategoriPremis) {
		this.subkategoriPremis = subkategoriPremis;
	}

	public String getNamaPremis() {
		return namaPremis;
	}

	public void setNamaPremis(String namaPremis) {
		this.namaPremis = namaPremis;
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

	public int getPoskod() {
		return poskod;
	}

	public void setPoskod(int poskod) {
		this.poskod = poskod;
	}

	public Bandar getBandar() {
		return bandar;
	}

	public void setBandar(Bandar bandar) {
		this.bandar = bandar;
	}

	public double getLuasPpremis() {
		return luasPpremis;
	}

	public void setLuasPpremis(double luasPpremis) {
		this.luasPpremis = luasPpremis;
	}

	public double getLuasBangunan() {
		return luasBangunan;
	}

	public void setLuasBangunan(double luasBangunan) {
		this.luasBangunan = luasBangunan;
	}

	public double getLuasBinaanLuar() {
		return luasBinaanLuar;
	}

	public void setLuasBinaanLuar(double luasBinaanLuar) {
		this.luasBinaanLuar = luasBinaanLuar;
	}

	public double getKosAsal() {
		return kosAsal;
	}

	public void setKosAsal(double kosAsal) {
		this.kosAsal = kosAsal;
	}

	public double getKosTambahan() {
		return kosTambahan;
	}

	public void setKosTambahan(double kosTambahan) {
		this.kosTambahan = kosTambahan;
	}

	public double getJumlahKos() {
		return jumlahKos;
	}

	public void setJumlahKos(double jumlahKos) {
		this.jumlahKos = jumlahKos;
	}

	public int getTahunSiapBina() {
		return tahunSiapBina;
	}

	public void setTahunSiapBina(int tahunSiapBina) {
		this.tahunSiapBina = tahunSiapBina;
	}

	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
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
