package bph.entities.rpp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lebah.template.UID;

@Entity
@Table(name = "rpp_kelompok_aktiviti_utama")
public class RppKelompokAktivitiUtama {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_permohonan")
	private RppPermohonan permohonan;
	
	@Column(name = "keterangan")
	private String keterangan;
	
	
	public RppKelompokAktivitiUtama() {
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

	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}

}
