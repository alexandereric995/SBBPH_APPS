package bph.entities.utiliti;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lebah.template.UID;
import portal.module.entity.Users;
import bph.entities.kewangan.KewBayaranResit;

@Entity
@Table(name = "util_permohonan")
public class UtilPermohonan {

	@Id
	@Column(name = "id")
	private String id;
	
	@Column(name = "id_tempahan")
	private String  idTempahan;
	
	@Column(name = "tarikh_permohonan")
	@Temporal(TemporalType.DATE)
	private Date tarikhPermohonan;
	
	@ManyToOne
	@JoinColumn(name = "id_dewan")
	private UtilDewan dewan;
	
	@ManyToOne
	@JoinColumn(name = "id_gelanggang")
	private UtilGelanggang gelanggang;
	
	@OneToOne
	@JoinColumn(name = "id_pemohon")
	private Users pemohon;
	
	@Column(name = "tarikh_mula")
	@Temporal(TemporalType.DATE)
	private Date tarikhMula;
	
	@Column(name = "tarikh_tamat")
	@Temporal(TemporalType.DATE)
	private Date tarikhTamat;
	
	@Column(name = "masa_mula")
	private int masaMula;
	
	@Column(name = "masa_tamat")
	private int masaTamat;
	
	@Column(name = "tujuan")
	private String tujuan;	
	
	@Column(name = "jenis_permohonan")
	private String jenisPermohonan;
	
	@Column(name = "jumlah_amaun")
	private double amaun;
	
	@Column(name = "status_bayaran")
	private String statusBayaran;
	
	@Column(name = "tarikh_bayaran")
	@Temporal(TemporalType.DATE)
	private Date tarikhBayaran;
	
	@Column(name = "flag_awam")
	private String flagAwam;
	
	@Column(name = "flag_swasta")
	private String flagSwasta;
	
	@Column(name = "status_permohonan")
	private String statusPermohonan;
	
	@Column(name = "status_aktif")
	private String statusAktif;
	
	@OneToOne
	@JoinColumn(name = "id_resit_sewa")
	private KewBayaranResit resitSewa;
	
	@Column(name = "flag_syspintar")
	private String flagSyspintar;
	
	@OneToOne
	@JoinColumn(name = "id_pemohon_batal")
	private Users pemohonBatal;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_batal")
	private Date tarikhBatal;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "masa_batal")
	private Date masaBatal;	
	
	@Column(name = "sebab_batal")
	private String sebabBatal;
	
	@OneToOne
	@JoinColumn(name = "id_pelulus")
	private Users pelulus;
	
	@Column(name = "tarikh_kelulusan")
	@Temporal(TemporalType.DATE)
	private Date tarikhKelulusan;
	
	public UtilPermohonan() {
		setId(UID.getUID());
		setIdTempahan(getId());
		setFlagSyspintar("T");
		setStatusBayaran("T");
		setStatusAktif("1");
		setFlagAwam("N");
		setFlagSwasta("N");
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdTempahan() {
		return idTempahan;
	}

	public void setIdTempahan(String idTempahan) {
		this.idTempahan = idTempahan;
	}

	public Date getTarikhPermohonan() {
		return tarikhPermohonan;
	}

	public void setTarikhPermohonan(Date tarikhPermohonan) {
		this.tarikhPermohonan = tarikhPermohonan;
	}

	public UtilDewan getDewan() {
		return dewan;
	}

	public void setDewan(UtilDewan dewan) {
		this.dewan = dewan;
	}

	public UtilGelanggang getGelanggang() {
		return gelanggang;
	}

	public void setGelanggang(UtilGelanggang gelanggang) {
		this.gelanggang = gelanggang;
	}

	public Users getPemohon() {
		return pemohon;
	}

	public void setPemohon(Users pemohon) {
		this.pemohon = pemohon;
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

	public int getMasaMula() {
		return masaMula;
	}

	public void setMasaMula(int masaMula) {
		this.masaMula = masaMula;
	}

	public int getMasaTamat() {
		return masaTamat;
	}

	public void setMasaTamat(int masaTamat) {
		this.masaTamat = masaTamat;
	}

	public String getTujuan() {
		return tujuan;
	}

	public void setTujuan(String tujuan) {
		this.tujuan = tujuan;
	}

	public String getJenisPermohonan() {
		return jenisPermohonan;
	}

	public void setJenisPermohonan(String jenisPermohonan) {
		this.jenisPermohonan = jenisPermohonan;
	}

	public double getAmaun() {
		return amaun;
	}

	public void setAmaun(double amaun) {
		this.amaun = amaun;
	}

	public String getStatusBayaran() {
		return statusBayaran;
	}

	public void setStatusBayaran(String statusBayaran) {
		this.statusBayaran = statusBayaran;
	}

	public Date getTarikhBayaran() {
		return tarikhBayaran;
	}

	public void setTarikhBayaran(Date tarikhBayaran) {
		this.tarikhBayaran = tarikhBayaran;
	}

	public String getFlagAwam() {
		return flagAwam;
	}

	public void setFlagAwam(String flagAwam) {
		this.flagAwam = flagAwam;
	}

	public String getFlagSwasta() {
		return flagSwasta;
	}

	public void setFlagSwasta(String flagSwasta) {
		this.flagSwasta = flagSwasta;
	}

	public String getStatusPermohonan() {
		return statusPermohonan;
	}

	public void setStatusPermohonan(String statusPermohonan) {
		this.statusPermohonan = statusPermohonan;
	}

	public String getStatusAktif() {
		return statusAktif;
	}

	public void setStatusAktif(String statusAktif) {
		this.statusAktif = statusAktif;
	}

	public KewBayaranResit getResitSewa() {
		return resitSewa;
	}

	public void setResitSewa(KewBayaranResit resitSewa) {
		this.resitSewa = resitSewa;
	}

	public String getFlagSyspintar() {
		return flagSyspintar;
	}

	public void setFlagSyspintar(String flagSyspintar) {
		this.flagSyspintar = flagSyspintar;
	}

	public Users getPemohonBatal() {
		return pemohonBatal;
	}

	public void setPemohonBatal(Users pemohonBatal) {
		this.pemohonBatal = pemohonBatal;
	}

	public Date getTarikhBatal() {
		return tarikhBatal;
	}

	public void setTarikhBatal(Date tarikhBatal) {
		this.tarikhBatal = tarikhBatal;
	}

	public Date getMasaBatal() {
		return masaBatal;
	}

	public void setMasaBatal(Date masaBatal) {
		this.masaBatal = masaBatal;
	}

	public String getSebabBatal() {
		return sebabBatal;
	}

	public void setSebabBatal(String sebabBatal) {
		this.sebabBatal = sebabBatal;
	}

	public Users getPelulus() {
		return pelulus;
	}

	public void setPelulus(Users pelulus) {
		this.pelulus = pelulus;
	}

	public Date getTarikhKelulusan() {
		return tarikhKelulusan;
	}

	public void setTarikhKelulusan(Date tarikhKelulusan) {
		this.tarikhKelulusan = tarikhKelulusan;
	}
}