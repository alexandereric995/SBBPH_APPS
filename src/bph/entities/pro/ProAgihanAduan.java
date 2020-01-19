package bph.entities.pro;

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
import bph.entities.kod.Seksyen;

@Entity
@Table(name = "pro_agihan_aduan")
public class ProAgihanAduan {

	@Id
	@Column(name = "id")
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "id_aduan")
	private ProAduan aduan;
	
	@ManyToOne
	@JoinColumn(name = "id_pegawai_agihan")
	private Users pegawaiAgihan;

	@ManyToOne
	@JoinColumn(name = "id_seksyen")
	private Seksyen seksyen;
	
	@ManyToOne
	@JoinColumn(name = "id_pegawai_tugasan")
	private Users pegawaiTugasan;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_tugasan")
	private Date tarikhTugasan;
	
	@Column(name = "catatan")
	private String catatan;
	
	@Column(name = "flag_aktif")
	private String flagAktif;
	
	public ProAgihanAduan() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ProAduan getAduan() {
		return aduan;
	}

	public void setAduan(ProAduan aduan) {
		this.aduan = aduan;
	}

	public Users getPegawaiAgihan() {
		return pegawaiAgihan;
	}

	public void setPegawaiAgihan(Users pegawaiAgihan) {
		this.pegawaiAgihan = pegawaiAgihan;
	}

	public Seksyen getSeksyen() {
		return seksyen;
	}

	public void setSeksyen(Seksyen seksyen) {
		this.seksyen = seksyen;
	}

	public Users getPegawaiTugasan() {
		return pegawaiTugasan;
	}

	public void setPegawaiTugasan(Users pegawaiTugasan) {
		this.pegawaiTugasan = pegawaiTugasan;
	}

	public Date getTarikhTugasan() {
		return tarikhTugasan;
	}

	public void setTarikhTugasan(Date tarikhTugasan) {
		this.tarikhTugasan = tarikhTugasan;
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
}
