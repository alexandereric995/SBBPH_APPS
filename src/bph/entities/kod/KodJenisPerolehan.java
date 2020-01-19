package bph.entities.kod;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lebah.template.UID;

/**
 * @author muhdsyazreen
 */

@Entity
@Table(name = "ruj_jenis_perolehan")
public class KodJenisPerolehan {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "kod")
	private String kod;

	@Column(name = "keterangan")
	private String keterangan;

	public KodJenisPerolehan() {
		setId(UID.getUID());
	}

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
