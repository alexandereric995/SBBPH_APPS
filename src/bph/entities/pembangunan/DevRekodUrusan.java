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
import bph.entities.kod.UrusanJKPTG;

@Entity
@Table(name = "dev_rekod_urusan")
public class DevRekodUrusan {

	@Id
	@Column(name = "id")
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "id_hakmilik")
	private DevHakmilik hakmilik;

	@Column(name = "no_rujukan")
	private String noRujukan;

	@Column(name = "no_fail")
	private String noFail;

	@Column(name = "keterangan")
	private String keterangan;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_mohon")
	private Date tarikhMohon;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_mula")
	private Date tarikhMula;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_akhir")
	private Date tarikhAkhir;	

	@ManyToOne
	@JoinColumn(name = "id_urusan_jkptg")
	private UrusanJKPTG urusanJKPTG;
	
	@Column(name = "flag_urusan")
	private String flagUrusan;
	
	@Column(name = "nilaian")
	private Double nilaian;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_terima_maklumbalas")
	private Date tarikhTerimaMaklumbalas;
	
	@Column(name = "catatan_maklumbalas")
	private String catatanMaklumbalas;
	
	public DevRekodUrusan() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DevHakmilik getHakmilik() {
		return hakmilik;
	}

	public void setHakmilik(DevHakmilik hakmilik) {
		this.hakmilik = hakmilik;
	}

	public String getNoRujukan() {
		return noRujukan;
	}

	public void setNoRujukan(String noRujukan) {
		this.noRujukan = noRujukan;
	}

	public String getNoFail() {
		return noFail;
	}

	public void setNoFail(String noFail) {
		this.noFail = noFail;
	}

	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}

	public Date getTarikhMohon() {
		return tarikhMohon;
	}

	public void setTarikhMohon(Date tarikhMohon) {
		this.tarikhMohon = tarikhMohon;
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

	public UrusanJKPTG getUrusanJKPTG() {
		return urusanJKPTG;
	}

	public void setUrusanJKPTG(UrusanJKPTG urusanJKPTG) {
		this.urusanJKPTG = urusanJKPTG;
	}

	public String getFlagUrusan() {
		return flagUrusan;
	}

	public void setFlagUrusan(String flagUrusan) {
		this.flagUrusan = flagUrusan;
	}

	public Double getNilaian() {
		return nilaian;
	}

	public void setNilaian(Double nilaian) {
		this.nilaian = nilaian;
	}

	public String getCatatanMaklumbalas() {
		return catatanMaklumbalas;
	}

	public void setCatatanMaklumbalas(String catatanMaklumbalas) {
		this.catatanMaklumbalas = catatanMaklumbalas;
	}

	public Date getTarikhTerimaMaklumbalas() {
		return tarikhTerimaMaklumbalas;
	}

	public void setTarikhTerimaMaklumbalas(Date tarikhTerimaMaklumbalas) {
		this.tarikhTerimaMaklumbalas = tarikhTerimaMaklumbalas;
	}
}
