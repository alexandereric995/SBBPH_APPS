package bph.entities.rk;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lebah.template.UID;

@Entity
@Table(name = "rk_pemohon")
public class RkPemohon {
	
	@Id
	@Column(name = "id")
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "id_individu")
	private RkIndividu individu;
	
	@ManyToOne
	@JoinColumn(name = "id_syarikat")
	private RkSyarikat syarikat;
	
	public RkPemohon() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public RkIndividu getIndividu() {
		return individu;
	}

	public void setIndividu(RkIndividu individu) {
		this.individu = individu;
	}

	public RkSyarikat getSyarikat() {
		return syarikat;
	}

	public void setSyarikat(RkSyarikat syarikat) {
		this.syarikat = syarikat;
	}
}
