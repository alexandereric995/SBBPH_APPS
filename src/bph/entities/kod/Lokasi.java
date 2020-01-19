/**
 * 
 */
package bph.entities.kod;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lebah.template.UID;

@Entity
@Table(name = "ruj_lokasi")
public class Lokasi {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "presint")
	private String presint;

	@Column(name = "keterangan")
	private String keterangan;

	@Column(name = "jenis_kuaters")
	private String jenisKuaters;

	@Column(name = "catatan")
	private String catatan;

	@Column(name = "status")
	private String status;

	public Lokasi() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPresint() {
		return presint;
	}

	public void setPresint(String presint) {
		this.presint = presint;
	}

	public String getJenisKuaters() {
		return jenisKuaters;
	}

	public void setJenisKuaters(String jenisKuaters) {
		this.jenisKuaters = jenisKuaters;
	}

	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}

}
