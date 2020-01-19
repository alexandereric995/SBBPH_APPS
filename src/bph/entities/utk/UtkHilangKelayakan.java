package bph.entities.utk;

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
import bph.entities.kod.SebabHilangKelayakanUtk;
import bph.entities.qtr.KuaPenghuni;

@Entity
@Table(name = "utk_hilang_kelayakan")
public class UtkHilangKelayakan {

	@Id
	@Column(name = "id")
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "id_penghuni")
	private KuaPenghuni penghuni;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh")
	private Date tarikh;
	
	@ManyToOne
	@JoinColumn(name = "flag_sebab")
	private SebabHilangKelayakanUtk flagSebab;
	
	@Column(name = "catatan")
	private String catatan;

	@Column(name = "status")
	private String status;
	
	@Column(name = "status_penghuni")
	private String statusPenghuni;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_hilang_kelayakan")
	private Date tarikhHilangKelayakan;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_tamat_hilang_kelayakan")
	private Date tarikhTamatHilangKelayakan;
	
	@Column(name = "kadar_biasa")
	private String kadarBiasa;
		
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_mula_biasa")
	private Date tarikhMulaBiasa;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_tamat_biasa")
	private Date tarikhTamatBiasa;
	
	@Column(name = "kadar_pasaran")
	private String kadarPasaran;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_mula_pasaran")
	private Date tarikhMulaPasaran;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_tamat_pasaran")
	private Date tarikhTamatPasaran;
	
	@Column(name = "iwk")
	private String iwk;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_mula_iwk")
	private Date tarikhMulaIwk;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_tamat_iwk")
	private Date tarikhTamatIwk;
	
	@Column(name = "cola")
	private String cola;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_mula_cola")
	private Date tarikhMulaCola;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_tamat_cola")
	private Date tarikhTamatCola;
	
	@Column(name = "surat_hilang_kelayakan")
	private String surathilangKelayakan;

	@Column(name = "slip_gaji")
	private String slipGaji;

	@ManyToOne
	@JoinColumn(name = "id_masuk")
	private Users daftarOleh;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_masuk")
	private Date tarikhMasuk;
	
	@ManyToOne
	@JoinColumn(name = "id_kemaskini")
	private Users kemaskiniOleh;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_kemaskini")
	private Date tarikhKemaskini;
	
	
	public UtkHilangKelayakan() {
		setId(UID.getUID());
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public KuaPenghuni getPenghuni() {
		return penghuni;
	}


	public void setPenghuni(KuaPenghuni penghuni) {
		this.penghuni = penghuni;
	}


	public Date getTarikh() {
		return tarikh;
	}


	public void setTarikh(Date tarikh) {
		this.tarikh = tarikh;
	}


	public SebabHilangKelayakanUtk getFlagSebab() {
		return flagSebab;
	}


	public void setFlagSebab(SebabHilangKelayakanUtk flagSebab) {
		this.flagSebab = flagSebab;
	}


	public String getCatatan() {
		return catatan;
	}


	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getStatusPenghuni() {
		return statusPenghuni;
	}


	public void setStatusPenghuni(String statusPenghuni) {
		this.statusPenghuni = statusPenghuni;
	}


	public Date getTarikhHilangKelayakan() {
		return tarikhHilangKelayakan;
	}


	public void setTarikhHilangKelayakan(Date tarikhHilangKelayakan) {
		this.tarikhHilangKelayakan = tarikhHilangKelayakan;
	}


	public Date getTarikhTamatHilangKelayakan() {
		return tarikhTamatHilangKelayakan;
	}


	public void setTarikhTamatHilangKelayakan(Date tarikhTamatHilangKelayakan) {
		this.tarikhTamatHilangKelayakan = tarikhTamatHilangKelayakan;
	}


	public String getKadarBiasa() {
		return kadarBiasa;
	}


	public void setKadarBiasa(String kadarBiasa) {
		this.kadarBiasa = kadarBiasa;
	}


	public Date getTarikhMulaBiasa() {
		return tarikhMulaBiasa;
	}


	public void setTarikhMulaBiasa(Date tarikhMulaBiasa) {
		this.tarikhMulaBiasa = tarikhMulaBiasa;
	}


	public Date getTarikhTamatBiasa() {
		return tarikhTamatBiasa;
	}


	public void setTarikhTamatBiasa(Date tarikhTamatBiasa) {
		this.tarikhTamatBiasa = tarikhTamatBiasa;
	}


	public String getKadarPasaran() {
		return kadarPasaran;
	}


	public void setKadarPasaran(String kadarPasaran) {
		this.kadarPasaran = kadarPasaran;
	}


	public Date getTarikhMulaPasaran() {
		return tarikhMulaPasaran;
	}


	public void setTarikhMulaPasaran(Date tarikhMulaPasaran) {
		this.tarikhMulaPasaran = tarikhMulaPasaran;
	}


	public Date getTarikhTamatPasaran() {
		return tarikhTamatPasaran;
	}


	public void setTarikhTamatPasaran(Date tarikhTamatPasaran) {
		this.tarikhTamatPasaran = tarikhTamatPasaran;
	}


	public String getIwk() {
		return iwk;
	}


	public void setIwk(String iwk) {
		this.iwk = iwk;
	}


	public Date getTarikhMulaIwk() {
		return tarikhMulaIwk;
	}


	public void setTarikhMulaIwk(Date tarikhMulaIwk) {
		this.tarikhMulaIwk = tarikhMulaIwk;
	}


	public Date getTarikhTamatIwk() {
		return tarikhTamatIwk;
	}


	public void setTarikhTamatIwk(Date tarikhTamatIwk) {
		this.tarikhTamatIwk = tarikhTamatIwk;
	}


	public String getCola() {
		return cola;
	}


	public void setCola(String cola) {
		this.cola = cola;
	}


	public Date getTarikhMulaCola() {
		return tarikhMulaCola;
	}


	public void setTarikhMulaCola(Date tarikhMulaCola) {
		this.tarikhMulaCola = tarikhMulaCola;
	}


	public Date getTarikhTamatCola() {
		return tarikhTamatCola;
	}


	public void setTarikhTamatCola(Date tarikhTamatCola) {
		this.tarikhTamatCola = tarikhTamatCola;
	}


	public String getSurathilangKelayakan() {
		return surathilangKelayakan;
	}


	public void setSurathilangKelayakan(String surathilangKelayakan) {
		this.surathilangKelayakan = surathilangKelayakan;
	}


	public String getSlipGaji() {
		return slipGaji;
	}


	public void setSlipGaji(String slipGaji) {
		this.slipGaji = slipGaji;
	}


	public Users getDaftarOleh() {
		return daftarOleh;
	}


	public void setDaftarOleh(Users daftarOleh) {
		this.daftarOleh = daftarOleh;
	}


	public Date getTarikhMasuk() {
		return tarikhMasuk;
	}


	public void setTarikhMasuk(Date tarikhMasuk) {
		this.tarikhMasuk = tarikhMasuk;
	}


	public Users getKemaskiniOleh() {
		return kemaskiniOleh;
	}


	public void setKemaskiniOleh(Users kemaskiniOleh) {
		this.kemaskiniOleh = kemaskiniOleh;
	}


	public Date getTarikhKemaskini() {
		return tarikhKemaskini;
	}


	public void setTarikhKemaskini(Date tarikhKemaskini) {
		this.tarikhKemaskini = tarikhKemaskini;
	}

	
}
