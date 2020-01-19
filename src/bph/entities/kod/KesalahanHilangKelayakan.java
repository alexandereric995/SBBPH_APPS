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
@Table(name = "ruj_kesalahan_hilang")
public class KesalahanHilangKelayakan {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "jenis_kesalahan")
	private String jenisKesalahan;

	@Column(name = "status")
	private String status;

	@Column(name = "catatan")
	private String catatan;

	public KesalahanHilangKelayakan() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getJenisKesalahan() {
		return jenisKesalahan;
	}

	public void setJenisKesalahan(String jenisKesalahan) {
		this.jenisKesalahan = jenisKesalahan;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}

}
