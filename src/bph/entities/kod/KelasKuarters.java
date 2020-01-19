package bph.entities.kod;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ruj_kelas_kuarters")
public class KelasKuarters {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "gred_mula")
	private String gredMula;

	@Column(name = "gred_akhir")
	private String gredAkhir;

	@Column(name = "kadar_sewa")
	private Double kadarSewa;

	@Column(name = "deposit")
	private Double deposit;

	@Column(name = "flag_aktif")
	private String flagAktif;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGredMula() {
		return gredMula;
	}

	public void setGredMula(String gredMula) {
		this.gredMula = gredMula;
	}

	public String getGredAkhir() {
		return gredAkhir;
	}

	public void setGredAkhir(String gredAkhir) {
		this.gredAkhir = gredAkhir;
	}

	public Double getKadarSewa() {
		return kadarSewa;
	}

	public void setKadarSewa(Double kadarSewa) {
		this.kadarSewa = kadarSewa;
	}

	public Double getDeposit() {
		return deposit;
	}

	public void setDeposit(Double deposit) {
		this.deposit = deposit;
	}

	public String getFlagAktif() {
		return flagAktif;
	}

	public void setFlagAktif(String flagAktif) {
		this.flagAktif = flagAktif;
	}
	
}
