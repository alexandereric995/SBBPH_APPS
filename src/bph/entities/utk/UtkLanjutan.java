package bph.entities.utk;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lebah.template.UID;

@Entity
@Table(name = "utk_lanjutan")
public class UtkLanjutan {

	@Id
	@Column(name = "id")
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "id_rayuan")
	private UtkRayuan rayuan;
	
	@ManyToOne
	@JoinColumn(name = "id_kesalahan")
	private UtkKesalahan kesalahan;
	
	public Date getTarikhKelulusan() {
		return tarikhKelulusan;
	}

	public void setTarikhKelulusan(Date tarikhKelulusan) {
		this.tarikhKelulusan = tarikhKelulusan;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_mula")
	private Date tarikhMula;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_tamat")
	private Date tarikhTamat;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_kelulusan")
	private Date tarikhKelulusan;
	
	@Column(name = "catatan")
	private String catatan;
	

	
	public UtkLanjutan() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public UtkRayuan getRayuan() {
		return rayuan;
	}

	public void setRayuan(UtkRayuan rayuan) {
		this.rayuan = rayuan;
	}

	public UtkKesalahan getKesalahan() {
		return kesalahan;
	}

	public void setKesalahan(UtkKesalahan kesalahan) {
		this.kesalahan = kesalahan;
	}

	public Date getTarikhMula() {
		return tarikhMula;
	}

	public void setTarikhMula(Date tarikhMula) {
		this.tarikhMula = tarikhMula;
	}

	public Date getTarikhTamat() {
		return tarikhTamat;
	}

	public void setTarikhTamat(Date tarikhTamat) {
		this.tarikhTamat = tarikhTamat;
	}


	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}
}
