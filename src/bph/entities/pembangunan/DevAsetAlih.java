/**
 * 
 */
package bph.entities.pembangunan;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lebah.template.UID;

/**
 * @author namira
 * 
 */
@Entity
@Table(name = "dev_aset_alih")
public class DevAsetAlih {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_ruang")
	private DevRuang ruang;

	@Column(name = "keterangan")
	private String keterangan;
	
	@Column(name = "bilangan")
	private String bilangan;

	@Column(name = "catatan")
	private String catatan;

	public DevAsetAlih() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DevRuang getRuang() {
		return ruang;
	}

	public void setRuang(DevRuang ruang) {
		this.ruang = ruang;
	}

	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}

	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}

	public String getBilangan() {
		return bilangan;
	}

	public void setBilangan(String bilangan) {
		this.bilangan = bilangan;
	}
}
