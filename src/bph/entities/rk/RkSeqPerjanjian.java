package bph.entities.rk;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lebah.template.UID;

@Entity
@Table(name="rk_seq_perjanjian")
public class RkSeqPerjanjian {
	
	@Id
	@Column(name = "id")
	private String id;
	
	@Column(name = "tahun")
	private Integer tahun;
	
	@Column(name = "bil")
	private Integer bil;
	
	public RkSeqPerjanjian() {
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

	public Integer getBil() {
		return bil;
	}

	public void setBil(Integer bil) {
		this.bil = bil;
	}

}
