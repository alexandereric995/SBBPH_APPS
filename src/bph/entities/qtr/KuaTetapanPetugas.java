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

@Entity
@Table(name = "kua_tetapan_petugas")
public class KuaTetapanPetugas {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_petugas")
	private Users petugas;

	@Column(name = "flag_penyelia")
	private String PTPenyelia;

	@Column(name = "flag_kunci")
	private String PTKunci;

	@Column(name = "flag_adun")
	private String PTAdun;

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

	public KuaTetapanPetugas() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setPetugas(Users petugas) {
		this.petugas = petugas;
	}

	public Users getPetugas() {
		return petugas;
	}

	public void setPTPenyelia(String pTPenyelia) {
		PTPenyelia = pTPenyelia;
	}

	public String getPTPenyelia() {
		return PTPenyelia;
	}

	public void setPTKunci(String pTKunci) {
		PTKunci = pTKunci;
	}

	public String getPTKunci() {
		return PTKunci;
	}

	public void setPTAdun(String pTAdun) {
		PTAdun = pTAdun;
	}

	public String getPTAdun() {
		return PTAdun;
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
