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
@Table(name = "dev_log_semakan")
public class DevLogSemakan {

	@Id
	@Column(name = "id")
	private String id;	
	
	@ManyToOne
	@JoinColumn(name = "id_semakan")
	private DevSemakan semakan;	
	
	@ManyToOne
	@JoinColumn(name = "id_petugas")
	private Users petugas;

	@ManyToOne
	@JoinColumn(name = "id_pegawai")
	private Users pegawai;
	
	@Column(name = "catatan")
	private String catatan;	

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh")
	private Date tarikh;
	
	@Column(name = "flag_aktif")
	private String flagAktif;
	
	public DevLogSemakan() {
		setId(UID.getUID());
		setTarikh(new Date());
		setFlagAktif("Y");
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DevSemakan getSemakan() {
		return semakan;
	}

	public void setSemakan(DevSemakan semakan) {
		this.semakan = semakan;
	}

	public Users getPetugas() {
		return petugas;
	}

	public void setPetugas(Users petugas) {
		this.petugas = petugas;
	}

	public Users getPegawai() {
		return pegawai;
	}

	public void setPegawai(Users pegawai) {
		this.pegawai = pegawai;
	}

	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}

	public Date getTarikh() {
		return tarikh;
	}

	public void setTarikh(Date tarikh) {
		this.tarikh = tarikh;
	}

	public String getFlagAktif() {
		return flagAktif;
	}

	public void setFlagAktif(String flagAktif) {
		this.flagAktif = flagAktif;
	}
}
