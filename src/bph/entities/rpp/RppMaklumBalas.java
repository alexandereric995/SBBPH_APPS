package bph.entities.rpp;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lebah.template.UID;
import portal.module.entity.Users;
import bph.entities.kod.Status;

@Entity
@Table(name = "rpp_maklumbalas")
public class RppMaklumBalas {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "no_maklumbalas")
	private String noMaklumbalas;

//	@ManyToOne
//	@JoinColumn(name = "id_jenis_maklumbalas")
//	private JenisMaklumbalas jenisMaklumBalas;
	
	@ManyToOne
	@JoinColumn(name = "id_permohonan")
	private RppPermohonan permohonan;
	
	@OneToOne
	@JoinColumn(name = "id_pemohon")
	private Users pemohon;

	@Column(name = "keterangan")
	private String keterangan;

	@Column(name = "tarikh_maklumbalas")
	@Temporal(TemporalType.DATE)
	private Date tarikhMaklumbalas;

	@Column(name = "ulasan_maklumbalas")
	private String ulasanMaklumbalas;
	
	@Column(name = "tarikh_ulasan")
	@Temporal(TemporalType.DATE)
	private Date tarikhUlasan;

	@OneToOne
	@JoinColumn(name = "id_pengulas")
	private Users idPengulas;

	@OneToOne
	@JoinColumn(name = "id_status")
	private Status status;
	
	@Column(name = "tarikh_terima")
	@Temporal(TemporalType.DATE)
	private Date tarikhTerima;
	
	@Column(name = "tarikh_ulasan_hq")
	@Temporal(TemporalType.DATE)
	private Date tarikhUlasanHq;
	
	@Column(name = "ulasan_hq")
	private String ulasanHq;
	
	
	
	public RppMaklumBalas() {
		setId(UID.getUID());
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}	
	
	public String getNoMaklumbalas() {
		return noMaklumbalas;
	}

	public void setNoMaklumbalas(String noMaklumbalas) {
		this.noMaklumbalas = noMaklumbalas;
	}

//	public JenisMaklumbalas getJenisMaklumBalas() {
//		return jenisMaklumBalas;
//	}
//
//	public void setJenisMaklumBalas(JenisMaklumbalas jenisMaklumBalas) {
//		this.jenisMaklumBalas = jenisMaklumBalas;
//	}

	public RppPermohonan getPermohonan() {
		return permohonan;
	}

	public void setPermohonan(RppPermohonan permohonan) {
		this.permohonan = permohonan;
	}	

	public Users getPemohon() {
		return pemohon;
	}

	public void setPemohon(Users pemohon) {
		this.pemohon = pemohon;
	}

	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}

	public Date getTarikhMaklumbalas() {
		return tarikhMaklumbalas;
	}

	public void setTarikhMaklumbalas(Date tarikhMaklumbalas) {
		this.tarikhMaklumbalas = tarikhMaklumbalas;
	}

	public String getUlasanMaklumbalas() {
		return ulasanMaklumbalas;
	}

	public void setUlasanMaklumbalas(String ulasanMaklumbalas) {
		this.ulasanMaklumbalas = ulasanMaklumbalas;
	}

	public Date getTarikhUlasan() {
		return tarikhUlasan;
	}

	public void setTarikhUlasan(Date tarikhUlasan) {
		this.tarikhUlasan = tarikhUlasan;
	}

	public Users getIdPengulas() {
		return idPengulas;
	}

	public void setIdPengulas(Users idPengulas) {
		this.idPengulas = idPengulas;
	}
	
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Date getTarikhTerima() {
		return tarikhTerima;
	}

	public void setTarikhTerima(Date tarikhTerima) {
		this.tarikhTerima = tarikhTerima;
	}

	public Date getTarikhUlasanHq() {
		return tarikhUlasanHq;
	}

	public void setTarikhUlasanHq(Date tarikhUlasanHq) {
		this.tarikhUlasanHq = tarikhUlasanHq;
	}

	public String getUlasanHq() {
		return ulasanHq;
	}

	public void setUlasanHq(String ulasanHq) {
		this.ulasanHq = ulasanHq;
	}

//	public String getKeteranganMaklumBalas() {
//		if (this.jenisMaklumBalas.getId().equals("01")) {
//			return "ADUAN";
//		} else if (this.jenisMaklumBalas.getId().equals("02")) {
//			return "PERMOHONAN";
//		}else if (this.jenisMaklumBalas.getId().equals("03")) {
//			return "PERTANYAAN";
//		}else if (this.jenisMaklumBalas.getId().equals("04")){
//			return "RAYUAN";
//		}else {
//			return jenisMaklumBalas.getId();
//		}
//	}
}
