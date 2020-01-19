package bph.entities.kod;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lebah.template.UID;

@Entity
@Table(name = "ruj_jenis_jaminan")
public class JenisJaminan {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "keterangan")
	private String keterangan;

	public void setId(String id) {
		this.id = id;
	}

	public JenisJaminan(){
		setId(UID.getUID());
	}
	
	public String getId() {
		return id;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}

	public String getKeterangan() {
		return keterangan;
	}
}