package bph.entities.senggara;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import bph.entities.kod.LokasiPermohonan;

@Entity
@Table(name = "mtn_seq_inden")
public class MtnSeqInden {

	@Id
	@Column(name = "id")
	private String id;
	
	@OneToOne
	@JoinColumn(name = "id_lokasi_permohonan")
	private LokasiPermohonan lokasiPermohonan;
	
	@Column(name = "tahun")
	private Integer tahun;

	@Column(name = "bil")
	private Integer bil;	
	
	public MtnSeqInden() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public LokasiPermohonan getLokasiPermohonan() {
		return lokasiPermohonan;
	}

	public void setLokasiPermohonan(LokasiPermohonan lokasiPermohonan) {
		this.lokasiPermohonan = lokasiPermohonan;
	}

	public Integer getTahun() {
		return tahun;
	}

	public void setTahun(Integer tahun) {
		this.tahun = tahun;
	}

	public Integer getBil() {
		return bil;
	}

	public void setBil(Integer bil) {
		this.bil = bil;
	}
}
