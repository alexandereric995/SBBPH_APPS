package bph.entities.rpp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lebah.template.UID;
import bph.entities.kod.GredPerkhidmatan;

@Entity
@Table(name = "rpp_tempoh_bayaran_gred")
public class RppTempohBayaranByGred {

	@Id
	@Column(name = "id")
	private String id;

	@OneToOne
	@JoinColumn(name = "id_gred_perkhidmatan")
	private GredPerkhidmatan gredPerkhidmatan;
	
	@Column(name = "tempoh_bayaran")
	private Integer tempohBayaran; //hari

	
	public RppTempohBayaranByGred() {
		setId(UID.getUID());
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public GredPerkhidmatan getGredPerkhidmatan() {
		return gredPerkhidmatan;
	}

	public void setGredPerkhidmatan(GredPerkhidmatan gredPerkhidmatan) {
		this.gredPerkhidmatan = gredPerkhidmatan;
	}

	public Integer getTempohBayaran() {
		return tempohBayaran;
	}

	public void setTempohBayaran(Integer tempohBayaran) {
		this.tempohBayaran = tempohBayaran;
	}

}
