package bph.entities.qtr;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lebah.template.UID;
import portal.module.entity.Users;

@Entity
@Table(name = "kua_tetapan_petugas")
public class KuaTetapanPetugas {

	@Id
	@Column(name = "id")
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "id_petugas")
	private Users petugas;
	
	@Column(name="flag_penyelia")
	private String PTPenyelia;
	
	@Column(name="flag_kunci")
	private String PTKunci;
	
	@Column(name="flag_adun")
	private String PTAdun;

	public KuaTetapanPetugas() {
		setId(UID.getUID());
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
	
	public void setPetugas(Users petugas) {
		this.petugas = petugas;
	}

	public Users getPetugas() {
		return petugas;
	}

	public void setPTPenyelia(String pTPenyelia) {
		PTPenyelia = pTPenyelia;
	}

	public String getPTPenyelia() {
		return PTPenyelia;
	}

	public void setPTKunci(String pTKunci) {
		PTKunci = pTKunci;
	}

	public String getPTKunci() {
		return PTKunci;
	}

	public void setPTAdun(String pTAdun) {
		PTAdun = pTAdun;
	}

	public String getPTAdun() {
		return PTAdun;
	}
}
