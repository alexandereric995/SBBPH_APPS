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
import bph.entities.kod.JenisBangunan;

@Entity
@Table(name = "dev_maklumat_pemaju")
public class DevMaklumatPemaju {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_hakmilik")
	private DevHakmilik hakmilik;

	@ManyToOne
	@JoinColumn(name = "id_jenis_bangunan")
	private JenisBangunan jenisBangunan;

	@Column(name = "kontraktor_utama")
	private String kontraktorUtama;

	@Column(name = "bidang_kerja_kontraktor")
	private String bidangKerjaKontraktor;

	@Column(name = "juru_perunding_utama")
	private String juruPerundingUtama;

	@Column(name = "bidang_kerja_perunding")
	private String bidangKerjaPerunding;

	@Column(name = "kegunaan_blok")
	private String kegunaanBlok;

	@Column(name = "kos_bina")
	private String kosBina;

	@Column(name = "saiz_binaan")
	private String saizBinaan;

	@Column(name = "tahun_asal_siap_bina")
	private String tahunAsalSiapBina;

	@Column(name = "usia")
	private String usia;

	@Column(name = "jangka_hayat")
	private String jangkaHayat;

	@Column(name = "sumber_pembiayaan")
	private String sumberPembiayaan;

	@Column(name = "kod_ptj")
	private String kodPTJ;

	@Column(name = "jenis_milikan")
	private String jenisMilikan;

	@Column(name = "populasi")
	private String populasi;

	@Column(name = "jenis_struktur")
	private String jenisStruktur;

	@Column(name = "no_siri_pendaftaran")
	private String noSiriPendaftaran;

	@Column(name = "penggunaan_air")
	private String penggunaanAir;

	@Column(name = "penggunaan_tenaga")
	private String penggunaanTenaga;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_siap_bina")
	private Date tarikhSiapBina;

	public DevMaklumatPemaju() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public JenisBangunan getJenisBangunan() {
		return jenisBangunan;
	}

	public void setJenisBangunan(JenisBangunan jenisBangunan) {
		this.jenisBangunan = jenisBangunan;
	}

	public String getKontraktorUtama() {
		return kontraktorUtama;
	}

	public void setKontraktorUtama(String kontraktorUtama) {
		this.kontraktorUtama = kontraktorUtama;
	}

	public String getBidangKerjaKontraktor() {
		return bidangKerjaKontraktor;
	}

	public void setBidangKerjaKontraktor(String bidangKerjaKontraktor) {
		this.bidangKerjaKontraktor = bidangKerjaKontraktor;
	}

	public String getJuruPerundingUtama() {
		return juruPerundingUtama;
	}

	public void setJuruPerundingUtama(String juruPerundingUtama) {
		this.juruPerundingUtama = juruPerundingUtama;
	}

	public String getBidangKerjaPerunding() {
		return bidangKerjaPerunding;
	}

	public void setBidangKerjaPerunding(String bidangKerjaPerunding) {
		this.bidangKerjaPerunding = bidangKerjaPerunding;
	}

	public String getKegunaanBlok() {
		return kegunaanBlok;
	}

	public void setKegunaanBlok(String kegunaanBlok) {
		this.kegunaanBlok = kegunaanBlok;
	}

	public String getKosBina() {
		return kosBina;
	}

	public void setKosBina(String kosBina) {
		this.kosBina = kosBina;
	}

	public String getSaizBinaan() {
		return saizBinaan;
	}

	public void setSaizBinaan(String saizBinaan) {
		this.saizBinaan = saizBinaan;
	}

	public String getTahunAsalSiapBina() {
		return tahunAsalSiapBina;
	}

	public void setTahunAsalSiapBina(String tahunAsalSiapBina) {
		this.tahunAsalSiapBina = tahunAsalSiapBina;
	}

	public String getUsia() {
		return usia;
	}

	public void setUsia(String usia) {
		this.usia = usia;
	}

	public String getJangkaHayat() {
		return jangkaHayat;
	}

	public void setJangkaHayat(String jangkaHayat) {
		this.jangkaHayat = jangkaHayat;
	}

	public String getSumberPembiayaan() {
		return sumberPembiayaan;
	}

	public void setSumberPembiayaan(String sumberPembiayaan) {
		this.sumberPembiayaan = sumberPembiayaan;
	}

	public String getKodPTJ() {
		return kodPTJ;
	}

	public void setKodPTJ(String kodPTJ) {
		this.kodPTJ = kodPTJ;
	}

	public String getJenisMilikan() {
		return jenisMilikan;
	}

	public void setJenisMilikan(String jenisMilikan) {
		this.jenisMilikan = jenisMilikan;
	}

	public String getPopulasi() {
		return populasi;
	}

	public void setPopulasi(String populasi) {
		this.populasi = populasi;
	}

	public String getJenisStruktur() {
		return jenisStruktur;
	}

	public void setJenisStruktur(String jenisStruktur) {
		this.jenisStruktur = jenisStruktur;
	}

	public String getNoSiriPendaftaran() {
		return noSiriPendaftaran;
	}

	public void setNoSiriPendaftaran(String noSiriPendaftaran) {
		this.noSiriPendaftaran = noSiriPendaftaran;
	}

	public String getPenggunaanAir() {
		return penggunaanAir;
	}

	public void setPenggunaanAir(String penggunaanAir) {
		this.penggunaanAir = penggunaanAir;
	}

	public String getPenggunaanTenaga() {
		return penggunaanTenaga;
	}

	public void setPenggunaanTenaga(String penggunaanTenaga) {
		this.penggunaanTenaga = penggunaanTenaga;
	}

	public Date getTarikhSiapBina() {
		return tarikhSiapBina;
	}

	public void setTarikhSiapBina(Date tarikhSiapBina) {
		this.tarikhSiapBina = tarikhSiapBina;
	}

	public DevHakmilik getHakmilik() {
		return hakmilik;
	}

	public void setHakmilik(DevHakmilik hakmilik) {
		this.hakmilik = hakmilik;
	}

}
