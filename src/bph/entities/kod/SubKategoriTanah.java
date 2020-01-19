package bph.entities.kod;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ruj_subkategori_tanah")
public class SubKategoriTanah {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "keterangan")
	private String keterangan;

	@ManyToOne
	@JoinColumn(name = "id_kategori")
	private KategoriTanah kategoriTanah;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}

	public KategoriTanah getKategoriTanah() {
		return kategoriTanah;
	}

	public void setKategoriTanah(KategoriTanah kategoriTanah) {
		this.kategoriTanah = kategoriTanah;
	}

}
