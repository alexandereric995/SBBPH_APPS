package bph.entities.pro;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "pro_sequence_aduan")
public class ProSequenceAduan {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "bulan")
	private int bulan;

	@Column(name = "tahun")
	private int tahun;

	@Column(name = "bilangan")
	private int bilangan;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getBulan() {
		return bulan;
	}

	public void setBulan(int bulan) {
		this.bulan = bulan;
	}

	public int getTahun() {
		return tahun;
	}

	public void setTahun(int tahun) {
		this.tahun = tahun;
	}

	public int getBilangan() {
		return bilangan;
	}

	public void setBilangan(int bilangan) {
		this.bilangan = bilangan;
	}
}
