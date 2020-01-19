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
@Table(name="rpp_kemudahan")
public class RppKemudahan {

	@Id
	@Column(name = "id")
	private String id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_peranginan", nullable = false)
	private RppPeranginan peranginan;
	
	@Column(name = "nama")
	private String nama;
	
	@Column(name = "bilangan")
	private int bilangan;
	
	@Column(name = "kadar_sewa")
	private Double kadarSewa;
	
	@Column(name = "jenis_kadar_sewa")
	private String jenisKadarSewa;
	
	@Column(name = "catatan")
	private String catatan;	
	
	@Column(name = "flag_sewa")
	private String flagSewa;	
	
	
	public RppKemudahan() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public RppPeranginan getPeranginan() {
		return peranginan;
	}

	public void setPeranginan(RppPeranginan peranginan) {
		this.peranginan = peranginan;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public int getBilangan() {
		return bilangan;
	}

	public void setBilangan(int bilangan) {
		this.bilangan = bilangan;
	}

	public Double getKadarSewa() {
		return kadarSewa;
	}

	public void setKadarSewa(Double kadarSewa) {
		this.kadarSewa = kadarSewa;
	}

	public String getJenisKadarSewa() {
		return jenisKadarSewa;
	}

	public void setJenisKadarSewa(String jenisKadarSewa) {
		this.jenisKadarSewa = jenisKadarSewa;
	}

	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}

	public String getFlagSewa() {
		return flagSewa;
	}

	public void setFlagSewa(String flagSewa) {
		this.flagSewa = flagSewa;
	}
	
	public String getKeteranganFlagSewa(){
		String str = "";
		if(this.flagSewa != null){
			if(this.flagSewa.equalsIgnoreCase("Y")){
				str = "YA";
			}else{
				str = "TIDAK";
			}
		}
		return str;
	}
	
}
