package bph.entities.kod;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ruj_agensi")
public class Agensi {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "keterangan")
	private String keterangan;

	@ManyToOne
	@JoinColumn(name = "id_kementerian")
	private Kementerian kementerian;

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

	public Kementerian getKementerian() {
		return kementerian;
	}

	public void setKementerian(Kementerian kementerian) {
		this.kementerian = kementerian;
	}

}
