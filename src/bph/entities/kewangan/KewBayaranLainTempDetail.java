package bph.entities.kewangan;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "kew_bayaran_lain_temp_detail")
public class KewBayaranLainTempDetail {

	@Id
	@Column(name = "id")
	private String id;
	
	@OneToOne
	@JoinColumn(name = "id_bayaran_lain")
	private KewBayaranLainTemp bayaranLain;
	
	@Column(name = "kod")
	private String kod;
	
	@Column(name = "keterangan")
	private String keterangan;
	
	@Column(name = "perihal")
	private String perihal;
	
	@Column(name = "amaun")
	private Double amaun;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public KewBayaranLainTemp getBayaranLain() {
		return bayaranLain;
	}

	public void setBayaranLain(KewBayaranLainTemp bayaranLain) {
		this.bayaranLain = bayaranLain;
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

	public String getPerihal() {
		return perihal;
	}

	public void setPerihal(String perihal) {
		this.perihal = perihal;
	}

	public Double getAmaun() {
		return amaun;
	}

	public void setAmaun(Double amaun) {
		this.amaun = amaun;
	}
}
