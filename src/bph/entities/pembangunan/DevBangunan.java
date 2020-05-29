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
@Table(name = "dev_bangunan")
public class DevBangunan {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_premis")
	private DevPremis premis;

	@Column(name = "kod_dpa")
	private String kodDPA;

	@Column(name = "kod_dak")
	private String kodDAK;

	@Column(name = "nama_bangunan")
	private String namaBangunan;

	@Column(name = "luas")
	private double luas;

	@Column(name = "nama_kontraktor")
	private String namaKontraktor;

	@Column(name = "bidang_kerja_kontraktor")
	private String bidangKerjaKontraktor;

	@Column(name = "nama_perunding")
	private String namaPerunding;

	@Column(name = "bidang_kerja_perunding")
	private String bidangKerjaPerunding;

	@Column(name = "tahun_siap_bina")
	private int tahunSiapBina;

	@Column(name = "no_pendaftaran")
	private String noPendaftaran;

	@Column(name = "kegunaan_bangunan")
	private String kegunaanBangunan;

	@Column(name = "kos_binaan")
	private double kosBinaan;

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

	public DevBangunan() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DevPremis getPremis() {
		return premis;
	}

	public void setPremis(DevPremis premis) {
		this.premis = premis;
	}

	public String getKodDPA() {
		return kodDPA;
	}

	public void setKodDPA(String kodDPA) {
		this.kodDPA = kodDPA;
	}

	public String getKodDAK() {
		return kodDAK;
	}

	public void setKodDAK(String kodDAK) {
		this.kodDAK = kodDAK;
	}

	public String getNamaBangunan() {
		return namaBangunan;
	}

	public void setNamaBangunan(String namaBangunan) {
		this.namaBangunan = namaBangunan;
	}

	public double getLuas() {
		return luas;
	}

	public void setLuas(double luas) {
		this.luas = luas;
	}

	public String getNamaKontraktor() {
		return namaKontraktor;
	}

	public void setNamaKontraktor(String namaKontraktor) {
		this.namaKontraktor = namaKontraktor;
	}

	public String getBidangKerjaKontraktor() {
		return bidangKerjaKontraktor;
	}

	public void setBidangKerjaKontraktor(String bidangKerjaKontraktor) {
		this.bidangKerjaKontraktor = bidangKerjaKontraktor;
	}

	public String getNamaPerunding() {
		return namaPerunding;
	}

	public void setNamaPerunding(String namaPerunding) {
		this.namaPerunding = namaPerunding;
	}

	public String getBidangKerjaPerunding() {
		return bidangKerjaPerunding;
	}

	public void setBidangKerjaPerunding(String bidangKerjaPerunding) {
		this.bidangKerjaPerunding = bidangKerjaPerunding;
	}

	public int getTahunSiapBina() {
		return tahunSiapBina;
	}

	public void setTahunSiapBina(int tahunSiapBina) {
		this.tahunSiapBina = tahunSiapBina;
	}

	public String getNoPendaftaran() {
		return noPendaftaran;
	}

	public void setNoPendaftaran(String noPendaftaran) {
		this.noPendaftaran = noPendaftaran;
	}

	public String getKegunaanBangunan() {
		return kegunaanBangunan;
	}

	public void setKegunaanBangunan(String kegunaanBangunan) {
		this.kegunaanBangunan = kegunaanBangunan;
	}

	public double getKosBinaan() {
		return kosBinaan;
	}

	public void setKosBinaan(double kosBinaan) {
		this.kosBinaan = kosBinaan;
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
