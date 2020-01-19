package bph.entities.portal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lebah.template.UID;

@Entity
@Table(name = "cms_penyelenggaraan_kuarters")
public class CmsPenyelenggaraanKuarters {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "tajuk")
	private String tajuk;

	@Column(name = "flag_aktif")
	private String flagAktif;

	@Column(name = "document")
	private String document;

	@Column(name = "file_name")
	private String fileName;

	
	public CmsPenyelenggaraanKuarters() {
		setId(UID.getUID());
	}

	public String getKeteranganFlagAktif() {
		String status = "";
		if (this.flagAktif != null) {
			if (this.flagAktif.equals("Y")) {
				status = "AKTIF";
			} else if (this.flagAktif.equals("T")) {
				status = "TIDAK AKTIF";
			}
		}
		return status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTajuk() {
		return tajuk;
	}

	public void setTajuk(String tajuk) {
		this.tajuk = tajuk;
	}

	public String getFlagAktif() {
		return flagAktif;
	}

	public void setFlagAktif(String flagAktif) {
		this.flagAktif = flagAktif;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
