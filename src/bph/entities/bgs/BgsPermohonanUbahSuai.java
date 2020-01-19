package bph.entities.bgs;

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
import bph.entities.kod.Status;

@Entity
@Table(name = "bgs_permohonan_ubahsuai")
public class BgsPermohonanUbahSuai {
	
	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_permohonan")
	private BgsPermohonan permohonan;
	
	@Column(name = "no_fail")
	private String noFail;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_permohonan")
	private Date tarikhPermohonan;
	
	@Column(name = "tujuan")
	private String tujuan;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_ulasan_pengurus_bangunan")
	private Date tarikhUlasanPengurusBangunan;
	
	@Column(name = "ulasan_pengurus_bangunan")
	private String ulasanPengurusBangunan;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_ulasan_kkr")
	private Date tarikhUlasanKKR;
	
	@Column(name = "ulasan_kkr")
	private String ulasanKKR;
	
	@ManyToOne
	@JoinColumn(name = "id_penyedia")
	private Users penyedia;	
	
	@Column(name = "ulasan_penyedia")
	private String ulasanPenyedia;
	
	@ManyToOne
	@JoinColumn(name = "id_penyemak")
	private Users penyemak;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_semakan")
	private Date tarikhSemakan;
	
	@Column(name = "ulasan_penyemak")
	private String ulasanPenyemak;
	
	@Column(name = "flag_keputusan_penyemak")
	private String flagKeputusanPenyemak;
	
	@ManyToOne
	@JoinColumn(name = "id_pelulus")
	private Users pelulus;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_kelulusan")
	private Date tarikhKelulusan;
	
	@Column(name = "ulasan_pelulus")
	private String ulasanPelulus;
	
	@Column(name = "flag_keputusan_pelulus")
	private String flagKeputusanPelulus;
	
	@ManyToOne
	@JoinColumn(name = "id_status")
	private Status status;
	
	public BgsPermohonanUbahSuai() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BgsPermohonan getPermohonan() {
		return permohonan;
	}

	public void setPermohonan(BgsPermohonan permohonan) {
		this.permohonan = permohonan;
	}

	public String getNoFail() {
		return noFail;
	}

	public void setNoFail(String noFail) {
		this.noFail = noFail;
	}

	public Date getTarikhPermohonan() {
		return tarikhPermohonan;
	}

	public void setTarikhPermohonan(Date tarikhPermohonan) {
		this.tarikhPermohonan = tarikhPermohonan;
	}

	public String getTujuan() {
		return tujuan;
	}

	public void setTujuan(String tujuan) {
		this.tujuan = tujuan;
	}

	public Date getTarikhUlasanPengurusBangunan() {
		return tarikhUlasanPengurusBangunan;
	}

	public void setTarikhUlasanPengurusBangunan(Date tarikhUlasanPengurusBangunan) {
		this.tarikhUlasanPengurusBangunan = tarikhUlasanPengurusBangunan;
	}

	public String getUlasanPengurusBangunan() {
		return ulasanPengurusBangunan;
	}

	public void setUlasanPengurusBangunan(String ulasanPengurusBangunan) {
		this.ulasanPengurusBangunan = ulasanPengurusBangunan;
	}

	public Date getTarikhUlasanKKR() {
		return tarikhUlasanKKR;
	}

	public void setTarikhUlasanKKR(Date tarikhUlasanKKR) {
		this.tarikhUlasanKKR = tarikhUlasanKKR;
	}

	public String getUlasanKKR() {
		return ulasanKKR;
	}

	public void setUlasanKKR(String ulasanKKR) {
		this.ulasanKKR = ulasanKKR;
	}

	public Users getPenyedia() {
		return penyedia;
	}

	public void setPenyedia(Users penyedia) {
		this.penyedia = penyedia;
	}

	public String getUlasanPenyedia() {
		return ulasanPenyedia;
	}

	public void setUlasanPenyedia(String ulasanPenyedia) {
		this.ulasanPenyedia = ulasanPenyedia;
	}

	public Users getPenyemak() {
		return penyemak;
	}

	public void setPenyemak(Users penyemak) {
		this.penyemak = penyemak;
	}

	public Date getTarikhSemakan() {
		return tarikhSemakan;
	}

	public void setTarikhSemakan(Date tarikhSemakan) {
		this.tarikhSemakan = tarikhSemakan;
	}

	public String getUlasanPenyemak() {
		return ulasanPenyemak;
	}

	public void setUlasanPenyemak(String ulasanPenyemak) {
		this.ulasanPenyemak = ulasanPenyemak;
	}

	public String getFlagKeputusanPenyemak() {
		return flagKeputusanPenyemak;
	}

	public void setFlagKeputusanPenyemak(String flagKeputusanPenyemak) {
		this.flagKeputusanPenyemak = flagKeputusanPenyemak;
	}

	public Users getPelulus() {
		return pelulus;
	}

	public void setPelulus(Users pelulus) {
		this.pelulus = pelulus;
	}

	public Date getTarikhKelulusan() {
		return tarikhKelulusan;
	}

	public void setTarikhKelulusan(Date tarikhKelulusan) {
		this.tarikhKelulusan = tarikhKelulusan;
	}

	public String getUlasanPelulus() {
		return ulasanPelulus;
	}

	public void setUlasanPelulus(String ulasanPelulus) {
		this.ulasanPelulus = ulasanPelulus;
	}

	public String getFlagKeputusanPelulus() {
		return flagKeputusanPelulus;
	}

	public void setFlagKeputusanPelulus(String flagKeputusanPelulus) {
		this.flagKeputusanPelulus = flagKeputusanPelulus;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}
