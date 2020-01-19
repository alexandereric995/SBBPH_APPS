package bph.entities.ict;

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
@Table(name = "ict_aduan_internal")
public class AduanInternal {

	@Id
	@Column(name = "id")
	private String id;
	
	@Column(name = "butiran")
	private String butiran;

	@ManyToOne
	@JoinColumn(name = "id_masuk")
	private Users idMasuk;
	
	@ManyToOne
	@JoinColumn(name = "id_selesai")
	private Users idSelesai;

	@Column(name = "status")
	private String Status;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_selesai")
	private Date tarikhSelesai;
	
	
	@Column(name = "no_telefon")
	private String noTelefon;
	
	
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_aduan")
	private Date tarikhAduan;
	
	@Column(name = "no_pengenalan")
	private String noPengenalan;
	
	@Column(name = "tajuk")
	private String tajuk;
	
	public String getCatatan() {
		return catatan;
	}


	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}


	@Column(name = "catatan")
	private String catatan;
	
	
	
	public AduanInternal() {
		setId(UID.getUID());
	}
	
	
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public Date getTarikhAduan() {
		return tarikhAduan;
	}


	public void setTarikhAduan(Date tarikhAduan) {
		this.tarikhAduan = tarikhAduan;
	}


	public String getNoPengenalan() {
		return noPengenalan;
	}


	public void setNoPengenalan(String noPengenalan) {
		this.noPengenalan = noPengenalan;
	}


	public String getTajuk() {
		return tajuk;
	}


	public void setTajuk(String tajuk) {
		this.tajuk = tajuk;
	}


	public String getButiran() {
		return butiran;
	}


	public void setButiran(String butiran) {
		this.butiran = butiran;
	}

	public Date getTarikhSelesai() {
		return tarikhSelesai;
	}


	public void setTarikhSelesai(Date tarikhSelesai) {
		this.tarikhSelesai = tarikhSelesai;
	}




	public String getNoTelefon() {
		return noTelefon;
	}

	public void setNoTelefon(String noTelefon) {
		this.noTelefon = noTelefon;
	}

	
	public String getStatus() {
		return Status;
	}


	public void setStatus(String status) {
		Status = status;
	}


	public Users getIdMasuk() {
		return idMasuk;
	}


	public void setIdMasuk(Users idMasuk) {
		this.idMasuk = idMasuk;
	}


	public Users getIdSelesai() {
		return idSelesai;
	}


	public void setIdSelesai(Users idSelesai) {
		this.idSelesai = idSelesai;
	}
}