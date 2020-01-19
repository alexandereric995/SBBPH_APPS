/**
 * 
 */
package bph.entities.kod;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ruj_lokasi_kuarters")
public class LokasiKuarters {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "keterangan")
	private String keterangan;
	
	@ManyToOne
	@JoinColumn(name = "id_lokasi_permohonan")
	private LokasiPermohonan lokasi;

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

	public LokasiPermohonan getLokasi() {
		return lokasi;
	}

	public void setLokasi(LokasiPermohonan lokasi) {
		this.lokasi = lokasi;
	}

}
