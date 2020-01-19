package bph.entities.kod;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ruj_pegawai")
public class Pegawai {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "nama")
	private String nama;

	@Column(name = "jawatan")
	private String jawatan;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getJawatan() {
		return jawatan;
	}

	public void setJawatan(String jawatan) {
		this.jawatan = jawatan;
	}

}
