package bph.entities.kew;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lebah.template.UID;

/** KUA = kuarters 
 *  IR = rumah peranginan
 *  UTIL = utiliti / gelanggang
 *  RK = sewa ruang komersil
 *  RP = sewa ruang pejabat
 *  PRORP = ruang promosi ruang pejabat
 *  KK = kuarters kumpulan
 *  LAIN = bayaran lain
 *  DEPRK = deposit ruang komersil
 *  DEPRP = deposit ruang pejabat
 *  DEPKUA = deposit kuarters
 * */

@Entity
@Table(name = "kew_jenis_bayaran")
public class KewJenisBayaran {

	@Id
	@Column(name = "id")
	private String id;
	
	@Column(name = "kod")
	private String kod;

	@Column(name = "keterangan")
	private String keterangan;

	public KewJenisBayaran() {
		setId(UID.getUID());
	}
	
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

	public String getKod() {
		return kod;
	}

	public void setKod(String kod) {
		this.kod = kod;
	}
	
}
