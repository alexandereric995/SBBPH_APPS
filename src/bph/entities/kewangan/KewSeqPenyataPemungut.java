package bph.entities.kewangan;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lebah.template.UID;

@Entity
@Table(name = "kew_seq_penyata_pemungut")
public class KewSeqPenyataPemungut {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "kod")
	private String kod;
	
	@Column(name = "tahun")
	private int tahun;

	@Column(name = "bil")
	private int bil;
	
	public KewSeqPenyataPemungut() {
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

	public int getTahun() {
		return tahun;
	}

	public void setTahun(int tahun) {
		this.tahun = tahun;
	}

	public int getBil() {
		return bil;
	}

	public void setBil(int bil) {
		this.bil = bil;
	}
}