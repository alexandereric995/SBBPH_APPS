package bph.entities.rk;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import bph.entities.kod.Seksyen;

@Entity
@Table(name = "rk_seq_permohonan")
public class RkSeqPermohonan {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_seksyen")
	private Seksyen seksyen;

	@Column(name = "tahun")
	private Integer tahun;

	@Column(name = "bil")
	private Integer bil;

	public RkSeqPermohonan() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Seksyen getSeksyen() {
		return seksyen;
	}

	public void setSeksyen(Seksyen seksyen) {
		this.seksyen = seksyen;
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
