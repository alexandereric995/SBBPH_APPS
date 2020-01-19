package bph.entities.kod;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ruj_gred_jawatan")
public class GredJawatan {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "keterangan")
	private String keterangan;

	@ManyToOne
	@JoinColumn(name = "id_pdrm")
	private JawatanPDRM pdrm;
	
	@ManyToOne
	@JoinColumn(name = "id_atm")
	private JawatanATM atm;
	
	@ManyToOne
	@JoinColumn(name = "id_apmm")
	private JawatanAPMM apmm;

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

	public JawatanPDRM getPdrm() {
		return pdrm;
	}

	public void setPdrm(JawatanPDRM pdrm) {
		this.pdrm = pdrm;
	}

	public JawatanATM getAtm() {
		return atm;
	}

	public void setAtm(JawatanATM atm) {
		this.atm = atm;
	}

	public JawatanAPMM getApmm() {
		return apmm;
	}

	public void setApmm(JawatanAPMM apmm) {
		this.apmm = apmm;
	}
	
	

	
}
