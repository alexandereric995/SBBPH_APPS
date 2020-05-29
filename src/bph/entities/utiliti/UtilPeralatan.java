package bph.entities.utiliti;

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
@Table(name = "util_peralatan")
public class UtilPeralatan {

	@Id
	@Column(name = "id")
	private String id;

	@JoinColumn(name = "id_dewan")
	private UtilDewan dewan;
	// @Column(name = "id_dewan")
	// private String idDewan;

	@Column(name = "nama")
	private String nama;

	@Column(name = "kuantiti")
	private String kuantiti;

	@Column(name = "catatan")
	private String catatan;

	@Column(name = "kadar_sewa")
	private double kadarSewa;

	@ManyToOne
	@JoinColumn(name = "id_masuk")
	private Users daftarOleh;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_masuk")
	private Date tarikhMasuk;

	@ManyToOne
	@JoinColumn(name = "id_kemaskini")
	private Users kemaskiniOleh;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_kemaskini")
	private Date tarikhKemaskini;

	public UtilPeralatan() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getNama() {
		return nama;
	}

	public String getKuantiti() {
		return kuantiti;
	}

	public void setKuantiti(String kuantiti) {
		this.kuantiti = kuantiti;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}

	public String getCatatan() {
		return catatan;
	}

	public void setKadarSewa(double kadarSewa) {
		this.kadarSewa = kadarSewa;
	}

	public double getKadarSewa() {
		return kadarSewa;
	}

	public void setDewan(UtilDewan dewan) {
		this.dewan = dewan;
	}

	public UtilDewan getDewan() {
		return dewan;
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
