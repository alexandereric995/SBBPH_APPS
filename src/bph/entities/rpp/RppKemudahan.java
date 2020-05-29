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

@Entity
@Table(name = "rpp_kemudahan")
public class RppKemudahan {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_peranginan", nullable = false)
	private RppPeranginan peranginan;

	@Column(name = "nama")
	private String nama;

	@Column(name = "bilangan")
	private int bilangan;

	@Column(name = "kadar_sewa")
	private Double kadarSewa;

	@Column(name = "jenis_kadar_sewa")
	private String jenisKadarSewa;

	@Column(name = "catatan")
	private String catatan;

	@Column(name = "flag_sewa")
	private String flagSewa;

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

	public RppKemudahan() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public RppPeranginan getPeranginan() {
		return peranginan;
	}

	public void setPeranginan(RppPeranginan peranginan) {
		this.peranginan = peranginan;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public int getBilangan() {
		return bilangan;
	}

	public void setBilangan(int bilangan) {
		this.bilangan = bilangan;
	}

	public Double getKadarSewa() {
		return kadarSewa;
	}

	public void setKadarSewa(Double kadarSewa) {
		this.kadarSewa = kadarSewa;
	}

	public String getJenisKadarSewa() {
		return jenisKadarSewa;
	}

	public void setJenisKadarSewa(String jenisKadarSewa) {
		this.jenisKadarSewa = jenisKadarSewa;
	}

	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}

	public String getFlagSewa() {
		return flagSewa;
	}

	public void setFlagSewa(String flagSewa) {
		this.flagSewa = flagSewa;
	}

	public String getKeteranganFlagSewa() {
		String str = "";
		if (this.flagSewa != null) {
			if (this.flagSewa.equalsIgnoreCase("Y")) {
				str = "YA";
			} else {
				str = "TIDAK";
			}
		}
		return str;
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
