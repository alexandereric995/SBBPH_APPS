package bph.entities.rk;

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
import bph.entities.kod.KodHasil;

@Entity
@Table(name = "rk_invois")
public class RkInvois {

	@Id
	@Column(name = "id")
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "id_fail")
	private RkFail fail;
	
	@ManyToOne
	@JoinColumn(name = "id_permohonan")
	private RkPermohonan permohonan;
	
	@Column(name = "tarikh_invois")
	@Temporal(TemporalType.DATE)
	private Date tarikhInvois;
	
	@Column(name = "tarikh_mula")
	@Temporal(TemporalType.DATE)
	private Date tarikhMula;
	
	@Column(name = "tarikh_akhir")
	@Temporal(TemporalType.DATE)
	private Date tarikhAkhir;
	
	@ManyToOne
	@JoinColumn(name = "id_kod_hasil")
	private KodHasil kodHasil;

	@Column(name = "no_invois")
	private String noInvois;
	
	@Column(name = "keterangan")
	private String keterangan;
	
	@Column(name = "amaun_semasa")
	private double amaunSemasa;
	
	@Column(name = "amaun_tunggakan")
	private double amaunTunggakan;	
	
	@Column(name = "abt")
	private int abt;
	
	@Column(name = "amaun_semasa_iwk")
	private double amaunSemasaIWK;
	
	@Column(name = "amaun_tunggakan_iwk")
	private double amaunTunggakanIWK;	
	
	@Column(name = "abt_iwk")
	private int abtIWK;	
	
	@Column(name = "bil_cetakan")
	private int bilCetakan;	
	
	@Column(name = "catatan")
	private String catatan;	
	
	@Column(name = "flag_aktif")
	private String flagAktif;
	
	@ManyToOne
	@JoinColumn(name = "id_masuk")
	private Users daftarOleh;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="tarikh_masuk")
	private Date tarikhMasuk;
	
	@ManyToOne
	@JoinColumn(name = "id_kemaskini")
	private Users kemaskiniOleh;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_kemaskini")
	private Date tarikhKemaskini;
	
	public RkInvois() {
		setId(UID.getUID());
		setBilCetakan(0);
		setFlagAktif("Y");
		setTarikhMasuk(new Date());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public RkFail getFail() {
		return fail;
	}

	public void setFail(RkFail fail) {
		this.fail = fail;
	}

	public RkPermohonan getPermohonan() {
		return permohonan;
	}

	public void setPermohonan(RkPermohonan permohonan) {
		this.permohonan = permohonan;
	}

	public Date getTarikhInvois() {
		return tarikhInvois;
	}

	public void setTarikhInvois(Date tarikhInvois) {
		this.tarikhInvois = tarikhInvois;
	}

	public Date getTarikhMula() {
		return tarikhMula;
	}

	public void setTarikhMula(Date tarikhMula) {
		this.tarikhMula = tarikhMula;
	}

	public Date getTarikhAkhir() {
		return tarikhAkhir;
	}

	public void setTarikhAkhir(Date tarikhAkhir) {
		this.tarikhAkhir = tarikhAkhir;
	}

	public KodHasil getKodHasil() {
		return kodHasil;
	}

	public void setKodHasil(KodHasil kodHasil) {
		this.kodHasil = kodHasil;
	}

	public String getNoInvois() {
		return noInvois;
	}

	public void setNoInvois(String noInvois) {
		this.noInvois = noInvois;
	}

	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}

	public double getAmaunSemasa() {
		return amaunSemasa;
	}

	public void setAmaunSemasa(double amaunSemasa) {
		this.amaunSemasa = amaunSemasa;
	}

	public double getAmaunTunggakan() {
		return amaunTunggakan;
	}

	public void setAmaunTunggakan(double amaunTunggakan) {
		this.amaunTunggakan = amaunTunggakan;
	}

	public int getAbt() {
		return abt;
	}

	public void setAbt(int abt) {
		this.abt = abt;
	}

	public double getAmaunSemasaIWK() {
		return amaunSemasaIWK;
	}

	public void setAmaunSemasaIWK(double amaunSemasaIWK) {
		this.amaunSemasaIWK = amaunSemasaIWK;
	}

	public double getAmaunTunggakanIWK() {
		return amaunTunggakanIWK;
	}

	public void setAmaunTunggakanIWK(double amaunTunggakanIWK) {
		this.amaunTunggakanIWK = amaunTunggakanIWK;
	}

	public int getAbtIWK() {
		return abtIWK;
	}

	public void setAbtIWK(int abtIWK) {
		this.abtIWK = abtIWK;
	}

	public int getBilCetakan() {
		return bilCetakan;
	}

	public void setBilCetakan(int bilCetakan) {
		this.bilCetakan = bilCetakan;
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
