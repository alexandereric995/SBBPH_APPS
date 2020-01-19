package bph.entities.kod;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lebah.template.UID;

@Entity
@Table(name = "ruj_jenis_pelanggaran_syarat_utk")
public class JenisPelanggaranSyaratUtk {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "keterangan")
	private String keterangan;
	
	@Column(name = "flag_kes")
	private String flagKes;

	public JenisPelanggaranSyaratUtk() {
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

	public String getFlagKes() {
		return flagKes;
	}

	public void setFlagKes(String flagKes) {
		this.flagKes = flagKes;
	}

}
