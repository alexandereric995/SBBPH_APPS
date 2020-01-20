package bph.entities.pro;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lebah.template.UID;
import portal.module.entity.Users;
import bph.entities.kod.Seksyen;

@Entity
@Table(name = "pro_pegawai_unit_kedua")
public class ProPegawaiUnitKedua {

	@Id
	@Column(name = "id")
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "id_pegawai")
	private Users pegawai;

	@ManyToOne
	@JoinColumn(name = "id_seksyen")
	private Seksyen seksyen;
	
	@Column(name = "flag_aktif")
	private String flagAktif;
	
	public ProPegawaiUnitKedua() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Users getPegawai() {
		return pegawai;
	}

	public void setPegawai(Users pegawai) {
		this.pegawai = pegawai;
	}

	public Seksyen getSeksyen() {
		return seksyen;
	}

	public void setSeksyen(Seksyen seksyen) {
		this.seksyen = seksyen;
	}

	public String getFlagAktif() {
		return flagAktif;
	}

	public void setFlagAktif(String flagAktif) {
		this.flagAktif = flagAktif;
	}
}