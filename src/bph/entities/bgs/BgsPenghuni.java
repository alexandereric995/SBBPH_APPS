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
import bph.entities.kod.Agensi;

@Entity
@Table(name = "bgs_penghuni")
public class BgsPenghuni {

	@Id
	@Column(name = "id")
	private String id;	

	@ManyToOne
	@JoinColumn(name = "id_aras")
	private BgsAras aras;
	
	@ManyToOne
	@JoinColumn(name = "id_permohonan")
	private BgsPermohonan permohonan;
	
	@ManyToOne
	@JoinColumn(name = "id_agensi")
	private Agensi agensi;

	@Column(name = "kadar_sewa")
	private double kadarSewa;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_masuk")
	private Date tarikhMasuk;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_keluar")
	private Date tarikhKeluar;
	
	@Column(name = "ruang")
	private String ruang;
	
	@Column(name = "luas")
	private String luas;

	@Column(name = "catatan")
	private String catatan;

	public BgsPenghuni() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BgsAras getAras() {
		return aras;
	}

	public void setAras(BgsAras aras) {
		this.aras = aras;
	}

	public BgsPermohonan getPermohonan() {
		return permohonan;
	}

	public void setPermohonan(BgsPermohonan permohonan) {
		this.permohonan = permohonan;
	}

	public Agensi getAgensi() {
		return agensi;
	}

	public void setAgensi(Agensi agensi) {
		this.agensi = agensi;
	}

	public double getKadarSewa() {
		return kadarSewa;
	}

	public void setKadarSewa(double kadarSewa) {
		this.kadarSewa = kadarSewa;
	}

	public Date getTarikhMasuk() {
		return tarikhMasuk;
	}

	public void setTarikhMasuk(Date tarikhMasuk) {
		this.tarikhMasuk = tarikhMasuk;
	}

	public Date getTarikhKeluar() {
		return tarikhKeluar;
	}

	public void setTarikhKeluar(Date tarikhKeluar) {
		this.tarikhKeluar = tarikhKeluar;
	}

	public String getRuang() {
		return ruang;
	}

	public void setRuang(String ruang) {
		this.ruang = ruang;
	}

	public String getLuas() {
		return luas;
	}

	public void setLuas(String luas) {
		this.luas = luas;
	}

	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}
}
