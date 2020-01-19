package bph.entities.integrasi;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lebah.template.UID;
import portal.module.entity.Users;
import bph.entities.kod.CawanganJANM;

@Entity
@Table(name = "int_janm")
public class IntJANM {

	@Id
	@Column(name = "id")
	private String id;
	
	@Column(name = "file_name")
	private String fileName;
	
	@Column(name = "file_dir")
	private String fileDir;
	
	@Column(name = "type")
	private String type;
	
	@Column(name = "date")
	private String date;
	
	@ManyToOne
	@JoinColumn(name = "ag_branch_code")
	private CawanganJANM agBranch;
	
	@Column(name = "total_record")
	private int totalRecord;
	
	@Column(name = "total_amount")
	private double totalAmount;
	
	@Column(name = "agency_name")
	private String agencyName;
	
	@Column(name = "flag_migrate")
	private String flagMigrate;
	
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
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="janm")
	private List<IntJANMRekod> listRekod;
	
	public IntJANM() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());
		setFlagMigrate("T");
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileDir() {
		return fileDir;
	}

	public void setFileDir(String fileDir) {
		this.fileDir = fileDir;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public CawanganJANM getAgBranch() {
		return agBranch;
	}

	public void setAgBranch(CawanganJANM agBranch) {
		this.agBranch = agBranch;
	}

	public int getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getAgencyName() {
		return agencyName;
	}

	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
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

	public List<IntJANMRekod> getListRekod() {
		return listRekod;
	}

	public void setListRekod(List<IntJANMRekod> listRekod) {
		this.listRekod = listRekod;
	}

	public String getFlagMigrate() {
		return flagMigrate;
	}

	public void setFlagMigrate(String flagMigrate) {
		this.flagMigrate = flagMigrate;
	}
}
