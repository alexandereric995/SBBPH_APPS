package bph.entities.kod;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ruj_lokasi_permohonan")
public class LokasiPermohonan {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "lokasi")
	private String lokasi;
	
	@ManyToOne
	@JoinColumn(name = "id_bandar")
	private Bandar bandar;
	
	@ManyToOne
	@JoinColumn(name = "id_negeri")
	private Negeri negeri;

	@Column(name = "poskod")
	private String poskod;
	
	@Column(name = "mercu_tanda")
	private String mercuTanda;

	@Column(name = "lat")
	private double lat;
	
	@Column(name = "lon")
	private double lon;
	
	@Column(name = "abbrev")
	private String abbrev;
	
	public String getMercuTanda() {
		return mercuTanda;
	}

	public void setMercuTanda(String mercuTanda) {
		this.mercuTanda = mercuTanda;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLokasi() {
		return lokasi;
	}

	public void setLokasi(String lokasi) {
		this.lokasi = lokasi;
	}

	public Bandar getBandar() {
		return bandar;
	}

	public void setBandar(Bandar bandar) {
		this.bandar = bandar;
	}

	public Negeri getNegeri() {
		return negeri;
	}

	public void setNegeri(Negeri negeri) {
		this.negeri = negeri;
	}

	public String getPoskod() {
		return poskod;
	}

	public void setPoskod(String poskod) {
		this.poskod = poskod;
	}

	public String getAbbrev() {
		return abbrev;
	}

	public void setAbbrev(String abbrev) {
		this.abbrev = abbrev;
	}

}
