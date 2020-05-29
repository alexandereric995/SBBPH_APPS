package bph.entities.bgs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "bgs_seq_permohonan")
public class BgsSeqPermohonan {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "id_zon")
	private String idZon;

	@Column(name = "tahun")
	private int tahun;

	@Column(name = "bil_fail")
	private int bilFail;

	@Column(name = "bil_permohonan")
	private int bilPermohonan;

	public BgsSeqPermohonan() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdZon() {
		return idZon;
	}

	public void setIdZon(String idZon) {
		this.idZon = idZon;
	}

	public int getTahun() {
		return tahun;
	}

	public void setTahun(int tahun) {
		this.tahun = tahun;
	}

	public int getBilFail() {
		return bilFail;
	}

	public void setBilFail(int bilFail) {
		this.bilFail = bilFail;
	}

	public int getBilPermohonan() {
		return bilPermohonan;
	}

	public void setBilPermohonan(int bilPermohonan) {
		this.bilPermohonan = bilPermohonan;
	}
}
