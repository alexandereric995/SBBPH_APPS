package bph.entities.kod;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ruj_jawatan_pdrm")
public class JawatanPDRM {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "keterangan")
	private String keterangan;

	@Column(name = "taraf")
	private String taraf;
	
	@Column(name = "kelas_layak")
	private String kelasLayak;
	
	@Column(name = "kelas_downgrade")
	private String kelasDowngrade;
	
	
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
	
	public String getTaraf() {
		return taraf;
	}

	public void setTaraf(String taraf) {
		this.taraf = taraf;
	}

	public String getKelasLayak() {
		return kelasLayak;
	}

	public void setKelasLayak(String kelasLayak) {
		this.kelasLayak = kelasLayak;
	}

	public String getKelasDowngrade() {
		return kelasDowngrade;
	}

	public void setKelasDowngrade(String kelasDowngrade) {
		this.kelasDowngrade = kelasDowngrade;
	}
}
