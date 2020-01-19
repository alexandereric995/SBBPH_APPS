package bph.entities.kod;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ruj_kategori_pengguna")
public class KategoriPengguna {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "keterangan")
	private String keterangan;
	
	@Column(name = "flag_pengguna")
	private String flagPengguna;

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

	public String getFlagPengguna() {
		return flagPengguna;
	}

	public void setFlagPengguna(String flagPengguna) {
		this.flagPengguna = flagPengguna;
	}
}
