package bph.entities.rpp;

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
import bph.entities.kod.JenisUnitRPP;

@Entity
@Table(name = "rpp_tetapan_kuota")
public class RppTetapanKuota {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_jenis_unit")
	private JenisUnitRPP jenisUnitRpp;

	@Column(name = "hari")
	private Integer hari;

	@Column(name = "kuota")
	private Integer kuota;

	@ManyToOne
	@JoinColumn(name = "id_masuk")
	private Users idMasuk;

	@ManyToOne
	@JoinColumn(name = "id_kemaskini")
	private Users idKemaskini;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_masuk")
	private Date tarikhMasuk;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_kemaskini")
	private Date tarikhKemaskini;

	public RppTetapanKuota() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public JenisUnitRPP getJenisUnitRpp() {
		return jenisUnitRpp;
	}

	public void setJenisUnitRpp(JenisUnitRPP jenisUnitRpp) {
		this.jenisUnitRpp = jenisUnitRpp;
	}

	public Integer getHari() {
		return hari;
	}

	public void setHari(Integer hari) {
		this.hari = hari;
	}

	public Integer getKuota() {
		return kuota;
	}

	public void setKuota(Integer kuota) {
		this.kuota = kuota;
	}

	public Users getIdMasuk() {
		return idMasuk;
	}

	public void setIdMasuk(Users idMasuk) {
		this.idMasuk = idMasuk;
	}

	public Users getIdKemaskini() {
		return idKemaskini;
	}

	public void setIdKemaskini(Users idKemaskini) {
		this.idKemaskini = idKemaskini;
	}

	public Date getTarikhMasuk() {
		return tarikhMasuk;
	}

	public void setTarikhMasuk(Date tarikhMasuk) {
		this.tarikhMasuk = tarikhMasuk;
	}

	public Date getTarikhKemaskini() {
		return tarikhKemaskini;
	}

	public void setTarikhKemaskini(Date tarikhKemaskini) {
		this.tarikhKemaskini = tarikhKemaskini;
	}

}
