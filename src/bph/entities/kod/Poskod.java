package bph.entities.kod;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ruj_poskod")
public class Poskod {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "bandar")
	private String bandar;

	@Column(name = "negeri")
	private String negeri;

	@Column(name = "id_bandar")
	private String idBandar;

	@Column(name = "id_negeri")
	private String idNegeri;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBandar() {
		return bandar;
	}

	public void setBandar(String bandar) {
		this.bandar = bandar;
	}

	public String getNegeri() {
		return negeri;
	}

	public void setNegeri(String negeri) {
		this.negeri = negeri;
	}

	public String getIdBandar() {
		return idBandar;
	}

	public void setIdBandar(String idBandar) {
		this.idBandar = idBandar;
	}

	public String getIdNegeri() {
		return idNegeri;
	}

	public void setIdNegeri(String idNegeri) {
		this.idNegeri = idNegeri;
	}

}
