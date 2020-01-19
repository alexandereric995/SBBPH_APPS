package bph.entities.rpp;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lebah.template.UID;

@Entity
@Table(name="rpp_notifikasi")
public class RppNotifikasi {

	@Id
	@Column(name = "id")
	private String id;
	
	@Column(name = "id_rekod")
	private String idRekod;
	  
	@Column(name = "flag_aktiviti")
	private String flagAktiviti;	
	  
	@Temporal(TemporalType.TIMESTAMP) 
	@Column(name="tarikh_aktiviti")
	private Date tarikhAktiviti;  
	  
	@Column(name = "flag_aktif")
	private String flagAktif;	
	
	@Column(name = "module_class")
	private String moduleClass;
	
	
	public RppNotifikasi() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdRekod() {
		return idRekod;
	}

	public void setIdRekod(String idRekod) {
		this.idRekod = idRekod;
	}

	public String getFlagAktiviti() {
		return flagAktiviti;
	}

	public void setFlagAktiviti(String flagAktiviti) {
		this.flagAktiviti = flagAktiviti;
	}

	public Date getTarikhAktiviti() {
		return tarikhAktiviti;
	}

	public void setTarikhAktiviti(Date tarikhAktiviti) {
		this.tarikhAktiviti = tarikhAktiviti;
	}

	public String getFlagAktif() {
		return flagAktif;
	}

	public void setFlagAktif(String flagAktif) {
		this.flagAktif = flagAktif;
	}

	public String getModuleClass() {
		return moduleClass;
	}

	public void setModuleClass(String moduleClass) {
		this.moduleClass = moduleClass;
	}
	
}
