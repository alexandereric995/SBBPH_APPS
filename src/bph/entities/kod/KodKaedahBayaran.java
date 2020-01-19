package bph.entities.kod;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author muhdsyazreen
 * @description kod kaedah bayaran / mod bayaran
 */

@Entity
@Table(name = "ruj_kod_kaedah_bayaran")
public class KodKaedahBayaran {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "kod")
	private String kod;

	@Column(name = "keterangan")
	private String keterangan;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKod() {
		return kod;
	}

	public void setKod(String kod) {
		this.kod = kod;
	}

	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}

}
