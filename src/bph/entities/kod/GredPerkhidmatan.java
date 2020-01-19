package bph.entities.kod;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ruj_gred_perkhidmatan")
public class GredPerkhidmatan {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "keterangan")
	private String keterangan;

	@Column(name = "kelas_kuarters")
	private String kelasKuarters;
	
	@Column(name = "kelas_downgrade")
	private String kelasDowngrade;
	
	@Column(name = "flag_aktif")
	private String flagAktif;
	
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

	public String getKelasKuarters() {
		return kelasKuarters;
	}

	public void setKelasKuarters(String kelasKuarters) {
		this.kelasKuarters = kelasKuarters;
	}

	public String getFlagAktif() {
		return flagAktif;
	}

	public void setFlagAktif(String flagAktif) {
		this.flagAktif = flagAktif;
	}

	public void setKelasDowngrade(String kelasDowngrade) {
		this.kelasDowngrade = kelasDowngrade;
	}

	public String getKelasDowngrade() {
		return kelasDowngrade;
	}
	
	
}
