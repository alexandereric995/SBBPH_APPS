package bph.entities.qtr;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "kua_agihan_seq")
public class KuaAgihanSeq {

	@Id
	@Column(name = "id")
	private String id;
	
	@Column(name = "id_lokasi")
	private String idLokasi;
	
	@Column(name = "lokasi")
	private String lokasi;
	
	@Column(name = "no_giliran_min")
	private int noGiliranMin;
	
	@Column(name = "kelas_kuarters")
	private String kelasKuarters;
	
	@Column(name = "jumlah_rekod")
	private int jumlahRekod;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdLokasi() {
		return idLokasi;
	}

	public void setIdLokasi(String idLokasi) {
		this.idLokasi = idLokasi;
	}

	public String getLokasi() {
		return lokasi;
	}

	public void setLokasi(String lokasi) {
		this.lokasi = lokasi;
	}

	public int getNoGiliranMin() {
		return noGiliranMin;
	}

	public void setNoGiliranMin(int noGiliranMin) {
		this.noGiliranMin = noGiliranMin;
	}

	public String getKelasKuarters() {
		return kelasKuarters;
	}

	public void setKelasKuarters(String kelasKuarters) {
		this.kelasKuarters = kelasKuarters;
	}

	public int getJumlahRekod() {
		return jumlahRekod;
	}

	public void setJumlahRekod(int jumlahRekod) {
		this.jumlahRekod = jumlahRekod;
	}

}
