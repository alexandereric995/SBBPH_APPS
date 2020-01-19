package bph.entities.rpp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lebah.template.UID;

@Entity
@Table(name="rpp_sebab_mohon_rt")
public class RppSebabMohonRT {

	@Id
	@Column(name = "id")
	private String id;
	
	@Column(name = "keterangan")
	private String keterangan;
	
	@Column(name = "flag_keutamaan")
	private int flagKeutamaan;
	
	public RppSebabMohonRT() {
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

	public int getFlagKeutamaan() {
		return flagKeutamaan;
	}

	public void setFlagKeutamaan(int flagKeutamaan) {
		this.flagKeutamaan = flagKeutamaan;
	}

}
