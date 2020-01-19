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
import bph.entities.qtr.KuaAkaun;
import bph.entities.qtr.KuaPenghuni;

@Entity
@Table(name = "utk_abt")
public class UtkAbt {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_penghuni")
	private KuaPenghuni penghuni;
	
	@ManyToOne
	@JoinColumn(name = "id_akaun")
	private KuaAkaun akaun;
	
	@Column(name = "amaun")
	private Double amaun;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "flag_status")
	private String flagStatus;

	@Column(name = "flag_bayaran")
	private String flagBayaran;
	
	@Column(name = "catatan")
	private String catatan;
	
	@Column(name = "jumlah_tunggakan")
	private Double jumlahTunggakan;
	
	@Column(name = "jumlah_bayaran")
	private Double jumlahBayaran;
	
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

	@Column(name = "no_fail")
	private String noFail;
	
	@Column(name = "baki_tunggakan")
	private Double bakiTunggakan;
	
	@Column(name = "beza_tunggakan")
	private Double bezaTunggakan;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_mula_tunggakan")
	private Date tarikhMulaTunggakan;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_tamat_tunggakan")
	private Date tarikhTamatTunggakan;
	
	@Column(name = "bulan_tunggakan")
	private int bulanTunggakan;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_bayaran")
	private Date tarikhBayaran;
	
	@Column(name = "kadar_sewa_sebulan")
	private Double kadarSewaSebulan;
	
	public UtkAbt() {
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

	public KuaAkaun getAkaun() {
		return akaun;
	}

	public void setAkaun(KuaAkaun akaun) {
		this.akaun = akaun;
	}

	public String getFlagStatus() {
		return flagStatus;
	}

	public void setFlagStatus(String flagStatus) {
		this.flagStatus = flagStatus;
	}
	
	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}

	public String getFlagBayaran() {
		return flagBayaran;
	}

	public void setFlagBayaran(String flagBayaran) {
		this.flagBayaran = flagBayaran;
	}

	public Double getAmaun() {
		return amaun;
	}

	public void setAmaun(Double amaun) {
		this.amaun = amaun;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getJumlahTunggakan() {
		return jumlahTunggakan;
	}

	public void setJumlahTunggakan(Double jumlahTunggakan) {
		this.jumlahTunggakan = jumlahTunggakan;
	}

	public Double getJumlahBayaran() {
		return jumlahBayaran;
	}

	public void setJumlahBayaran(Double jumlahBayaran) {
		this.jumlahBayaran = jumlahBayaran;
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

	public String getNoFail() {
		return noFail;
	}

	public void setNoFail(String noFail) {
		this.noFail = noFail;
	}

	public Double getBakiTunggakan() {
		return bakiTunggakan;
	}

	public void setBakiTunggakan(Double bakiTunggakan) {
		this.bakiTunggakan = bakiTunggakan;
	}

	public Double getBezaTunggakan() {
		return bezaTunggakan;
	}

	public void setBezaTunggakan(Double bezaTunggakan) {
		this.bezaTunggakan = bezaTunggakan;
	}

	public Date getTarikhMulaTunggakan() {
		return tarikhMulaTunggakan;
	}

	public void setTarikhMulaTunggakan(Date tarikhMulaTunggakan) {
		this.tarikhMulaTunggakan = tarikhMulaTunggakan;
	}

	public Date getTarikhTamatTunggakan() {
		return tarikhTamatTunggakan;
	}

	public void setTarikhTamatTunggakan(Date tarikhTamatTunggakan) {
		this.tarikhTamatTunggakan = tarikhTamatTunggakan;
	}

	public int getBulanTunggakan() {
		return bulanTunggakan;
	}

	public void setBulanTunggakan(int bulanTunggakan) {
		this.bulanTunggakan = bulanTunggakan;
	}

	public Date getTarikhBayaran() {
		return tarikhBayaran;
	}

	public void setTarikhBayaran(Date tarikhBayaran) {
		this.tarikhBayaran = tarikhBayaran;
	}

	public Double getKadarSewa_sebulan() {
		return kadarSewaSebulan;
	}

	public void setKadarSewa_sebulan(Double kadarSewa_sebulan) {
		this.kadarSewaSebulan = kadarSewa_sebulan;
	}
	
}
