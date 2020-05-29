package bph.entities.qtr;

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
import bph.entities.kod.JenisRayuan;

@Entity
@Table(name = "kua_rayuan")
public class KuaRayuan {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_pemohon")
	private Users pemohon;

	@ManyToOne
	@JoinColumn(name = "id_jenis_rayuan")
	private JenisRayuan jenisRayuan;

	@Column(name = "status_rayuan")
	private String statusRayuan;

	@Column(name = "catatan")
	private String catatan;

	@Column(name = "tarikh_rayuan")
	@Temporal(TemporalType.DATE)
	private Date tarikhRayuan;

	@Column(name = "tarikh_kemaskini")
	@Temporal(TemporalType.TIMESTAMP)
	private Date tarikhKemaskini;

	@Column(name = "tarikh_rayuan_dibuat")
	@Temporal(TemporalType.DATE)
	private Date tarikhRayuanDibuat;

	@Column(name = "tarikh_maklumbalas")
	@Temporal(TemporalType.DATE)
	private Date tarikhMaklumbalas;

	@Column(name = "sebab_rayuan")
	private String sebabRayuan;

	@Column(name = "lain_lain_jenis_rayuan")
	private String lainJenisRayuan;

	@ManyToOne
	@JoinColumn(name = "id_masuk")
	private Users idMasuk;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_masuk")
	private Date tarikhMasuk;

	@ManyToOne
	@JoinColumn(name = "id_kemaskini")
	private Users idKemaskini;

	public KuaRayuan() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Users getPemohon() {
		return pemohon;
	}

	public void setPemohon(Users pemohon) {
		this.pemohon = pemohon;
	}

	public JenisRayuan getJenisRayuan() {
		return jenisRayuan;
	}

	public void setJenisRayuan(JenisRayuan jenisRayuan) {
		this.jenisRayuan = jenisRayuan;
	}

	public String getStatusRayuan() {
		return statusRayuan;
	}

	public void setStatusRayuan(String statusRayuan) {
		this.statusRayuan = statusRayuan;
	}

	public String getDescStatusRayuan() {
		String descStatusRayuan = "";

		if ("01".equals(getStatusRayuan())) {
			descStatusRayuan = "BERJAYA";
		} else if ("02".equals(getStatusRayuan())) {
			descStatusRayuan = "SEDANG DISEMAK";
		} else if ("03".equals(getStatusRayuan())) {
			descStatusRayuan = "GAGAL";
		}

		return descStatusRayuan;
	}

	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}

	public Date getTarikhRayuan() {
		return tarikhRayuan;
	}

	public void setTarikhRayuan(Date tarikhRayuan) {
		this.tarikhRayuan = tarikhRayuan;
	}

	public Date getTarikhKemaskini() {
		return tarikhKemaskini;
	}

	public void setTarikhKemaskini(Date tarikhKemaskini) {
		this.tarikhKemaskini = tarikhKemaskini;
	}

	public Date getTarikhRayuanDibuat() {
		return tarikhRayuanDibuat;
	}

	public void setTarikhRayuanDibuat(Date tarikhRayuanDibuat) {
		this.tarikhRayuanDibuat = tarikhRayuanDibuat;
	}

	public Date getTarikhMaklumbalas() {
		return tarikhMaklumbalas;
	}

	public void setTarikhMaklumbalas(Date tarikhMaklumbalas) {
		this.tarikhMaklumbalas = tarikhMaklumbalas;
	}

	public String getSebabRayuan() {
		return sebabRayuan;
	}

	public void setSebabRayuan(String sebabRayuan) {
		this.sebabRayuan = sebabRayuan;
	}

	public String getLainJenisRayuan() {
		return lainJenisRayuan;
	}

	public void setLainJenisRayuan(String lainJenisRayuan) {
		this.lainJenisRayuan = lainJenisRayuan;
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

}
