package bph.entities.ict;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lebah.template.UID;

@Entity
@Table(name = "audit_trail")
public class AuditTrailBaru {

	@Id
	@Column(name = "id")
	private String id;
	
	@Column(name = "userID")
	private String userID;
	
	@Column(name = "userName")
	private String userName;
	
	@Column(name = "ipAddress")
	private String ipAddress;

	@Column(name = "jenisAktiviti")
	private String jenisAktiviti;
	
	@Column(name = "keterangan")
	private String keterangan;
	
	@Column(name = "moduleClass")
	private String moduleClass;
	
	@Column(name = "moduleName")
	private String moduleName;
	
	@Column(name = "proses")
	private String proses;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikhAktiviti")
	private Date tarikhAktiviti;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "masaAktiviti")
	private Date masaAktiviti;
	
	public AuditTrailBaru() {
		setId(UID.getUID());
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getIpAddress() {
		return ipAddress;
	}


	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}


	public String getJenisAktiviti() {
		return jenisAktiviti;
	}


	public void setJenisAktiviti(String jenisAktiviti) {
		this.jenisAktiviti = jenisAktiviti;
	}


	public String getKeterangan() {
		return keterangan;
	}


	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}


	public String getModuleClass() {
		return moduleClass;
	}


	public void setModuleClass(String moduleClass) {
		this.moduleClass = moduleClass;
	}


	public String getModuleName() {
		return moduleName;
	}


	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}


	public String getProses() {
		return proses;
	}


	public void setProses(String proses) {
		this.proses = proses;
	}


	public Date getTarikhAktiviti() {
		return tarikhAktiviti;
	}


	public void setTarikhAktiviti(Date tarikhAktiviti) {
		this.tarikhAktiviti = tarikhAktiviti;
	}


	public Date getMasaAktiviti() {
		return masaAktiviti;
	}


	public void setMasaAktiviti(Date masaAktiviti) {
		this.masaAktiviti = masaAktiviti;
	}
}