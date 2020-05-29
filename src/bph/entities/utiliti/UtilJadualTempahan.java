package bph.entities.utiliti;

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
import portal.module.entity.Users;

@Entity
@Table(name = "util_jadual_tempahan")
public class UtilJadualTempahan {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_dewan")
	private UtilDewan dewan;

	@ManyToOne
	@JoinColumn(name = "id_gelanggang")
	private UtilGelanggang gelanggang;

	@Column(name = "tarikh_tempahan")
	@Temporal(TemporalType.DATE)
	private Date tarikhTempahan;

	@Column(name = "masa_mula")
	private int masaMula;

	@Column(name = "masa_tamat")
	private int masaTamat;

	@Column(name = "status")
	private String status;

	@Column(name = "catatan")
	private String catatan;

	@ManyToOne
	@JoinColumn(name = "id_tempahan")
	private UtilPermohonan permohonan;

	@ManyToOne
	@JoinColumn(name = "id_masuk")
	private Users daftarOleh;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_masuk")
	private Date tarikhMasuk;

	@ManyToOne
	@JoinColumn(name = "id_kemaskini")
	private Users kemaskiniOleh;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_kemaskini")
	private Date tarikhKemaskini;

	public UtilJadualTempahan() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getTarikhTempahan() {
		return tarikhTempahan;
	}

	public void setTarikhTempahan(Date tarikhTempahan) {
		this.tarikhTempahan = tarikhTempahan;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setGelanggang(UtilGelanggang gelanggang) {
		this.gelanggang = gelanggang;
	}

	public UtilGelanggang getGelanggang() {
		return gelanggang;
	}

	public void setPermohonan(UtilPermohonan permohonan) {
		this.permohonan = permohonan;
	}

	public UtilPermohonan getPermohonan() {
		return permohonan;
	}

	public void setDewan(UtilDewan dewan) {
		this.dewan = dewan;
	}

	public UtilDewan getDewan() {
		return dewan;
	}

	public void setMasaMula(int masaMula) {
		this.masaMula = masaMula;
	}

	public int getMasaMula() {
		return masaMula;
	}

	public void setMasaTamat(int masaTamat) {
		this.masaTamat = masaTamat;
	}

	public int getMasaTamat() {
		return masaTamat;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}

	public String getCatatan() {
		return catatan;
	}

	public Users getDaftarOleh() {
		return daftarOleh;
	}

	public void setDaftarOleh(Users daftarOleh) {
		this.daftarOleh = daftarOleh;
	}

	public Date getTarikhMasuk() {
		return tarikhMasuk;
	}

	public void setTarikhMasuk(Date tarikhMasuk) {
		this.tarikhMasuk = tarikhMasuk;
	}

	public Users getKemaskiniOleh() {
		return kemaskiniOleh;
	}

	public void setKemaskiniOleh(Users kemaskiniOleh) {
		this.kemaskiniOleh = kemaskiniOleh;
	}

	public Date getTarikhKemaskini() {
		return tarikhKemaskini;
	}

	public void setTarikhKemaskini(Date tarikhKemaskini) {
		this.tarikhKemaskini = tarikhKemaskini;
	}
}
