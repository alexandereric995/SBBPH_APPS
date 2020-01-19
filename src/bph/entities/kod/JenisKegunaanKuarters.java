package bph.entities.kod;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ruj_jenis_kegunaan_kuarters")
public class JenisKegunaanKuarters {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "keterangan")
	private String keterangan;

	@Column(name = "flag_aktif")
	private int flagAktif;
	
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

	public int getFlagAktif() {
		return flagAktif;
	}

	public void setFlagAktif(int flagAktif) {
		this.flagAktif = flagAktif;
	}
	
}
