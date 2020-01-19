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
import bph.entities.qtr.KuaPenghuni;

@Entity
@Table(name = "utk_kes_peguam")
public class UtkKesPeguam {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_kesalahan")
	private UtkKesalahan kesalahan;
	
	@ManyToOne
	@JoinColumn(name = "id_penghuni")
	private KuaPenghuni penghuni;
	
	@Column(name = "flag_keputusan")
	private String flagKeputusan;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_keputusan")
	private Date tarikhKeputusan;
	
	@Column(name = "catatan")
	private String catatan;
	
	@Column(name = "status_jenis_kes")
	private String statusJenisKes;

	public UtkKesPeguam() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public UtkKesalahan getKesalahan() {
		return kesalahan;
	}

	public void setKesalahan(UtkKesalahan kesalahan) {
		this.kesalahan = kesalahan;
	}

	public String getFlagKeputusan() {
		return flagKeputusan;
	}

	public void setFlagKeputusan(String flagKeputusan) {
		this.flagKeputusan = flagKeputusan;
	}

	public Date getTarikhKeputusan() {
		return tarikhKeputusan;
	}

	public void setTarikhKeputusan(Date tarikhKeputusan) {
		this.tarikhKeputusan = tarikhKeputusan;
	}

	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}

	public KuaPenghuni getPenghuni() {
		return penghuni;
	}

	public void setPenghuni(KuaPenghuni penghuni) {
		this.penghuni = penghuni;
	}

	public String getStatusJenisKes() {
		return statusJenisKes;
	}

	public void setStatusJenisKes(String statusJenisKes) {
		this.statusJenisKes = statusJenisKes;
	}

}
