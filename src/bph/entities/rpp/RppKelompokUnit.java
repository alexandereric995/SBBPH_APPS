package bph.entities.rpp;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lebah.template.UID;
import portal.module.entity.Users;
import bph.entities.kod.JenisUnitRPP;

@Entity
@Table(name = "rpp_kelompok_unit")
public class RppKelompokUnit {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_permohonan")
	private RppPermohonan permohonan;

	@ManyToOne
	@JoinColumn(name = "id_jenis_unit_rpp")
	private JenisUnitRPP jenisUnitRpp;

	@Column(name = "bil_unit")
	private Integer bilUnit;

	@Column(name = "bil_unit_lulus")
	private Integer bilUnitLulus;

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

	public RppKelompokUnit() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public RppPermohonan getPermohonan() {
		return permohonan;
	}

	public void setPermohonan(RppPermohonan permohonan) {
		this.permohonan = permohonan;
	}

	public JenisUnitRPP getJenisUnitRpp() {
		return jenisUnitRpp;
	}

	public void setJenisUnitRpp(JenisUnitRPP jenisUnitRpp) {
		this.jenisUnitRpp = jenisUnitRpp;
	}

	public Integer getBilUnit() {
		return bilUnit;
	}

	public void setBilUnit(Integer bilUnit) {
		this.bilUnit = bilUnit;
	}

	public Integer getBilUnitLulus() {
		return bilUnitLulus;
	}

	public void setBilUnitLulus(Integer bilUnitLulus) {
		this.bilUnitLulus = bilUnitLulus;
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

}
