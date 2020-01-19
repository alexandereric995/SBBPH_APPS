package bph.entities.kod;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lebah.template.UID;
import portal.module.entity.Users;

/**
 * @author nurasna
 */

@Entity
@Table(name = "ruj_kod_mesin")
public class KodMesin {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "kod_mesin")
	private String kodMesin;

	@ManyToOne
	@JoinColumn(name = "id_pemilik")
	private Users pemilik;
	
	@Column(name = "kod_pusat_terima")
	private String kodPusatTerima;

	public KodMesin() {
		setId(UID.getUID());
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKodPusatTerima() {
		return kodPusatTerima;
	}

	public void setKodPusatTerima(String kodPusatTerima) {
		this.kodPusatTerima = kodPusatTerima;
	}

	public String getKodMesin() {
		return kodMesin;
	}

	public void setKodMesin(String kodMesin) {
		this.kodMesin = kodMesin;
	}

	public Users getPemilik() {
		return pemilik;
	}

	public void setPemilik(Users pemilik) {
		this.pemilik = pemilik;
	}

}
