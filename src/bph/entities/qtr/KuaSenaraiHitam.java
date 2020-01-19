package bph.entities.qtr;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lebah.template.UID;
import portal.module.entity.Users;

@Entity
@Table(name = "kua_senarai_hitam")
public class KuaSenaraiHitam {

	@Id
	@Column(name = "id")
	private String id;

	@OneToOne
	@JoinColumn(name = "id_pemohon")
	private Users pemohon;

	@Column(name = "tarikh_mula")
	@Temporal(TemporalType.DATE)
	private Date tarikhMula;
	
	@Column(name = "tarikh_tamat")
	@Temporal(TemporalType.DATE)
	private Date tarikhTamat;

	@Column(name = "sebab")
	private String sebab;

	@Column(name = "flag_aktif")
	private String flagAktif;

	@Column(name = "catatan")
	private String catatan;

	public KuaSenaraiHitam() {
		setId(UID.getUID());
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSebab() {
		return sebab;
	}

	public void setSebab(String sebab) {
		this.sebab = sebab;
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

	public String getFlagAktif() {
		return flagAktif;
	}

	public void setFlagAktif(String flagAktif) {
		this.flagAktif = flagAktif;
	}

	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}
	
	public Users getPemohon() {
		return pemohon;
	}

	public void setPemohon(Users pemohon) {
		this.pemohon = pemohon;
	}

	public String getKeteranganFlagAktif() {
		if (this.flagAktif.equals("Y")) {
			return "AKTIF";
		} else {
			return "TIDAK AKTIF";
		}
	}

}
