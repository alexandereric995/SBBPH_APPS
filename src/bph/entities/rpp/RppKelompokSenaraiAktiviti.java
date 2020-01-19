package bph.entities.rpp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lebah.template.UID;
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
	
	
	public RppKelompokSenaraiAktiviti() {
		setId(UID.getUID());
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

}
