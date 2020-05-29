package bph.entities.qtr;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "vw_kua_agihan")
public class VW_KuaAgihan {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "total_data")
	private int totalData;

	@Column(name = "min_no_giliran")
	private int minNoGiliran;

	@Column(name = "max_no_giliran")
	private int maxNoGiliran;

	@Column(name = "kelas_kuarters")
	private String kelasKuarters;

	@Column(name = "id_lokasi")
	private String idLokasi;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getTotalData() {
		return totalData;
	}

	public void setTotalData(int totalData) {
		this.totalData = totalData;
	}

	public int getMinNoGiliran() {
		return minNoGiliran;
	}

	public void setMinNoGiliran(int minNoGiliran) {
		this.minNoGiliran = minNoGiliran;
	}

	public int getMaxNoGiliran() {
		return maxNoGiliran;
	}

	public void setMaxNoGiliran(int maxNoGiliran) {
		this.maxNoGiliran = maxNoGiliran;
	}

	public String getKelasKuarters() {
		return kelasKuarters;
	}

	public void setKelasKuarters(String kelasKuarters) {
		this.kelasKuarters = kelasKuarters;
	}

	public String getIdLokasi() {
		return idLokasi;
	}

	public void setIdLokasi(String idLokasi) {
		this.idLokasi = idLokasi;
	}

}
