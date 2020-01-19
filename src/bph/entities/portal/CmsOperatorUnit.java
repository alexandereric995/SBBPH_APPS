package bph.entities.portal;

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
@Table(name = "cms_operator_unit")
public class CmsOperatorUnit {

	@Id
	@Column(name = "id")
	private String id;
	
	@Column(name = "unit")
	private String unit;
	
	@Column(name = "nama")
	private String nama;	
	
	@Column(name = "no_telefon")
	private String noTelefon;
	
	@Column(name = "emel")
	private String emel;
	
	@Column(name = "flag_aktif")
	private String flagAktif;
	
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
	
	public CmsOperatorUnit() {
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

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getNoTelefon() {
		return noTelefon;
	}

	public void setNoTelefon(String noTelefon) {
		this.noTelefon = noTelefon;
	}

	public String getEmel() {
		return emel;
	}

	public void setEmel(String emel) {
		this.emel = emel;
	}

	public String getFlagAktif() {
		return flagAktif;
	}

	public void setFlagAktif(String flagAktif) {
		this.flagAktif = flagAktif;
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
