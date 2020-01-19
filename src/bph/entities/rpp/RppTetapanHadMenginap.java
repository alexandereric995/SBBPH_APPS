package bph.entities.rpp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lebah.template.UID;

@Entity
@Table(name = "rpp_tetapan_had_menginap")
public class RppTetapanHadMenginap {

	@Id
	@Column(name = "id")
	private String id;
	
	@Column(name = "bil_hari")
	private Integer bilHari; 
	
	@Column(name = "catatan")
	private String catatan; 
	
	public RppTetapanHadMenginap() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getBilHari() {
		return bilHari;
	}

	public void setBilHari(Integer bilHari) {
		this.bilHari = bilHari;
	}

	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}

}
