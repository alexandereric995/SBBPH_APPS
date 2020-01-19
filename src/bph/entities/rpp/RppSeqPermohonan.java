package bph.entities.rpp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lebah.template.UID;

@Entity
@Table(name = "rpp_seq_permohonan")
public class RppSeqPermohonan {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "tahun")
	private Integer tahun;

	@Column(name = "lokasi")
	private String lokasi;
	
	@Column(name = "bil")
	private Integer bil;
	
	@Column(name = "jenis_permohonan")
	private String jenisPermohonan;
	
	public RppSeqPermohonan() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getTahun() {
		return tahun;
	}

	public void setTahun(Integer tahun) {
		this.tahun = tahun;
	}

	public String getLokasi() {
		return lokasi;
	}

	public void setLokasi(String lokasi) {
		this.lokasi = lokasi;
	}

	public Integer getBil() {
		return bil;
	}

	public void setBil(Integer bil) {
		this.bil = bil;
	}

	public String getJenisPermohonan() {
		return jenisPermohonan;
	}

	public void setJenisPermohonan(String jenisPermohonan) {
		this.jenisPermohonan = jenisPermohonan;
	}

}
