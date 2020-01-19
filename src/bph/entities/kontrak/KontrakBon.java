package bph.entities.kontrak;

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
import bph.entities.kod.JenisJaminan;
import bph.entities.kod.StatusBonKontrak;

@Entity
@Table(name = "kontrak_bon")
public class KontrakBon {

	@Id
	@Column(name = "id")
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "id_kontrak")
	private KontrakKontrak kontrak;

	@ManyToOne
	@JoinColumn(name = "id_jenis_jaminan")
	private JenisJaminan jaminan;
	
	@Column(name = "no_rujukan")
	private String noRujukan;
	
	@Column(name = "nilai_bon")
	private Double nilaiBon;
	
	@Column(name = "tarikh_luput")
	@Temporal(TemporalType.DATE)
	private Date tarikhLuput;
	
//	@Column(name = "status_bon")
//	private String statusBon;
	@ManyToOne
	@JoinColumn(name ="status_bon")
	private StatusBonKontrak statusBon;
	
	@Column(name = "tarikh_release_bon")
	@Temporal(TemporalType.DATE)
	private Date tarikhReleaseBon;
	
	@Column(name = "nama_bank")
	private String namaBank;
	
	public KontrakBon(){
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public KontrakKontrak getKontrak() {
		return kontrak;
	}

	public void setKontrak(KontrakKontrak kontrak) {
		this.kontrak = kontrak;
	}

	public JenisJaminan getJaminan() {
		return jaminan;
	}

	public void setJaminan(JenisJaminan jaminan) {
		this.jaminan = jaminan;
	}

	public String getNoRujukan() {
		return noRujukan;
	}

	public void setNoRujukan(String noRujukan) {
		this.noRujukan = noRujukan;
	}

	public Double getNilaiBon() {
		return nilaiBon;
	}

	public void setNilaiBon(Double nilaiBon) {
		this.nilaiBon = nilaiBon;
	}

	public Date getTarikhLuput() {
		return tarikhLuput;
	}

	public void setTarikhLuput(Date tarikhLuput) {
		this.tarikhLuput = tarikhLuput;
	}

	public StatusBonKontrak getStatusBon() {
		return statusBon;
	}

	public void setStatusBon(StatusBonKontrak statusBon) {
		this.statusBon = statusBon;
	}

	public Date getTarikhReleaseBon() {
		return tarikhReleaseBon;
	}

	public void setTarikhReleaseBon(Date tarikhReleaseBon) {
		this.tarikhReleaseBon = tarikhReleaseBon;
	}

	public String getNamaBank() {
		return namaBank;
	}

	public void setNamaBank(String namaBank) {
		this.namaBank = namaBank;
	}

}