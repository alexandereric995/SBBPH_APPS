package bph.entities.kod;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ruj_pengkhususan_bidang_kontraktor")
public class PengkhususanBidangKontraktor {

	@Id
	@Column(name = "id")
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "id_kategori")
	private KategoriBidangKontraktor kategori;

	@Column(name = "keterangan")
	private String keterangan;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public KategoriBidangKontraktor getKategori() {
		return kategori;
	}

	public void setKategori(KategoriBidangKontraktor kategori) {
		this.kategori = kategori;
	}

	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}
}
