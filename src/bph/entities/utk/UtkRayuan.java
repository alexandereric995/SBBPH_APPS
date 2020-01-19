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
@Table(name = "utk_rayuan")
public class UtkRayuan {

	@Id
	@Column(name = "id")
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "id_kesalahan")
	private UtkKesalahan kesalahan;

	@ManyToOne
	@JoinColumn(name = "id_hilang_kelayakan")
	private UtkHilangKelayakan hilangKelayakan;
	
	@ManyToOne
	@JoinColumn(name = "id_abt")
	private UtkAbt abt;
	
	@ManyToOne
	@JoinColumn(name = "id_naziran_kebersihan")
	private UtkNaziranKebersihan naziranKebersihan;
	
	@Column(name = "no_rayuan")
	private String noRayuan;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_rayuan")
	private Date tarikhRayuan;
	
	@Column(name = "catatan_rayuan")
	private String catatanRayuan;
	
	@Column(name = "flag_jenis_rayuan")
	private String flagJenisRayuan;
	
	@Column(name = "flag_rayuan")
	private String flagRayuan;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_kelulusan")
	private Date tarikhKelulusan;
	
	@Column(name = "flag_kelulusan_sub")
	private String flagKelulusanSub;
	
	@Column(name = "catatan_kelulusan")
	private String catatanKelulusan;
	

	public UtkRayuan() {
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

	public String getNoRayuan() {
		return noRayuan;
	}

	public void setNoRayuan(String noRayuan) {
		this.noRayuan = noRayuan;
	}

	public Date getTarikhRayuan() {
		return tarikhRayuan;
	}

	public void setTarikhRayuan(Date tarikhRayuan) {
		this.tarikhRayuan = tarikhRayuan;
	}

	public String getCatatanRayuan() {
		return catatanRayuan;
	}

	public void setCatatanRayuan(String catatanRayuan) {
		this.catatanRayuan = catatanRayuan;
	}

	public String getFlagRayuan() {
		return flagRayuan;
	}

	public void setFlagRayuan(String flagRayuan) {
		this.flagRayuan = flagRayuan;
	}

	public Date getTarikhKelulusan() {
		return tarikhKelulusan;
	}

	public void setTarikhKelulusan(Date tarikhKelulusan) {
		this.tarikhKelulusan = tarikhKelulusan;
	}

	public String getCatatanKelulusan() {
		return catatanKelulusan;
	}

	public void setCatatanKelulusan(String catatanKelulusan) {
		this.catatanKelulusan = catatanKelulusan;
	}

	public String getFlagKelulusanSub() {
		return flagKelulusanSub;
	}

	public void setFlagKelulusanSub(String flagKelulusanSub) {
		this.flagKelulusanSub = flagKelulusanSub;
	}

	public String getFlagJenisRayuan() {
		return flagJenisRayuan;
	}

	public void setFlagJenisRayuan(String flagJenisRayuan) {
		this.flagJenisRayuan = flagJenisRayuan;
	}

	public UtkHilangKelayakan getHilangKelayakan() {
		return hilangKelayakan;
	}

	public void setHilangKelayakan(UtkHilangKelayakan hilangKelayakan) {
		this.hilangKelayakan = hilangKelayakan;
	}

	public UtkAbt getAbt() {
		return abt;
	}

	public void setAbt(UtkAbt abt) {
		this.abt = abt;
	}

	public UtkNaziranKebersihan getNaziranKebersihan() {
		return naziranKebersihan;
	}

	public void setNaziranKebersihan(UtkNaziranKebersihan naziranKebersihan) {
		this.naziranKebersihan = naziranKebersihan;
	}	
}
