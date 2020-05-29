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
import bph.entities.kod.AktivitiRpp;

@Entity
@Table(name = "rpp_kelompok_senarai_aktiviti")
public class RppKelompokSenaraiAktiviti {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_permohonan")
	private RppPermohonan permohonan;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_aktiviti_rpp")
	private AktivitiRpp aktiviti;

	@Column(name = "keterangan_aktiviti_lain")
	private String keteranganAktivitiLain;

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

	public RppKelompokSenaraiAktiviti() {
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

	public AktivitiRpp getAktiviti() {
		return aktiviti;
	}

	public void setAktiviti(AktivitiRpp aktiviti) {
		this.aktiviti = aktiviti;
	}

	public String getKeteranganAktivitiLain() {
		return keteranganAktivitiLain;
	}

	public void setKeteranganAktivitiLain(String keteranganAktivitiLain) {
		this.keteranganAktivitiLain = keteranganAktivitiLain;
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
