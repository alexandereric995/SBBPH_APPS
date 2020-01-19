package bph.entities.rk;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lebah.template.UID;

@Entity
@Table(name = "rk_maklumat_lain")
public class RkMaklumatLain {

	@Id
	@Column(name = "id")
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "id_permohonan")
	private RkPermohonan permohonan;
	
	@Column(name = "tempoh_pengalaman")
	private String tempohPengalaman;
	
	@Column(name = "bil_pengurus")
	private int bilPengurus;
	
	@Column(name = "bil_penyelia")
	private int bilPenyelia;
	
	@Column(name = "bil_pelayan")
	private int bilPelayan;
	
	@Column(name = "bil_pekerja_am")
	private int bilPekerjaAm;
	
	@Column(name = "flag_pekerja_asing")
	private String flagPekerjaAsing;
	
	@Column(name = "flag_sewa_pihak_lain")
	private String flagSewaPihakLain;
	
	@Column(name = "sebab_disewakan")
	private String sebabDisewakan;
	
	@Column(name = "anggaran_sewaan")
	private Double anggaranSewaan;

	public RkMaklumatLain() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public RkPermohonan getPermohonan() {
		return permohonan;
	}

	public void setPermohonan(RkPermohonan permohonan) {
		this.permohonan = permohonan;
	}

	public String getTempohPengalaman() {
		return tempohPengalaman;
	}

	public void setTempohPengalaman(String tempohPengalaman) {
		this.tempohPengalaman = tempohPengalaman;
	}

	public int getBilPengurus() {
		return bilPengurus;
	}

	public void setBilPengurus(int bilPengurus) {
		this.bilPengurus = bilPengurus;
	}

	public int getBilPenyelia() {
		return bilPenyelia;
	}

	public void setBilPenyelia(int bilPenyelia) {
		this.bilPenyelia = bilPenyelia;
	}

	public int getBilPelayan() {
		return bilPelayan;
	}

	public void setBilPelayan(int bilPelayan) {
		this.bilPelayan = bilPelayan;
	}

	public int getBilPekerjaAm() {
		return bilPekerjaAm;
	}

	public void setBilPekerjaAm(int bilPekerjaAm) {
		this.bilPekerjaAm = bilPekerjaAm;
	}

	public String getFlagPekerjaAsing() {
		return flagPekerjaAsing;
	}

	public void setFlagPekerjaAsing(String flagPekerjaAsing) {
		this.flagPekerjaAsing = flagPekerjaAsing;
	}

	public String getFlagSewaPihakLain() {
		return flagSewaPihakLain;
	}

	public void setFlagSewaPihakLain(String flagSewaPihakLain) {
		this.flagSewaPihakLain = flagSewaPihakLain;
	}

	public String getSebabDisewakan() {
		return sebabDisewakan;
	}

	public void setSebabDisewakan(String sebabDisewakan) {
		this.sebabDisewakan = sebabDisewakan;
	}

	public Double getAnggaranSewaan() {
		return anggaranSewaan;
	}

	public void setAnggaranSewaan(Double anggaranSewaan) {
		this.anggaranSewaan = anggaranSewaan;
	}
}
