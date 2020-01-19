/**
 * 
 */
package bph.entities.kod;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ruj_kondisi_kuarters")
public class KondisiKuarters {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "keterangan")
	private String keterangan;

	@Column(name = "keterangan_bi")
	private String keteranganBI;
	
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

	public String getKeteranganBI() {
		return keteranganBI;
	}

	public void setKeteranganBI(String keteranganBI) {
		this.keteranganBI = keteranganBI;
	}

	public String getFlagAktif() {
		return flagAktif;
	}

	public void setFlagAktif(String flagAktif) {
		this.flagAktif = flagAktif;
	}
	
}
