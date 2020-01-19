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
@Table(name = "cms_informasi")
public class CmsInformasi {

	@Id
	@Column(name = "id")
	private String id;
	
	@Column(name = "tajuk")
	private String tajuk;
	
	@Column(name = "butiran")
	private String butiran;	
	
	@Column(name = "flag_aktif")
	private String flagAktif;
	
	@Column(name = "flag_baru")
	private String flagBaru;
	
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
	
	public CmsInformasi() {
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
	
	public String getKeteranganFlagBaru() {
		String status = "";
		if (this.flagBaru != null) {
			if (this.flagBaru.equals("Y")) {
				status = "AKTIF";
			} else if (this.flagBaru.equals("T")) {
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

	public String getButiran() {
		return butiran;
	}

	public void setButiran(String butiran) {
		this.butiran = butiran;
	}

	public String getFlagAktif() {
		return flagAktif;
	}

	public void setFlagAktif(String flagAktif) {
		this.flagAktif = flagAktif;
	}

	public String getFlagBaru() {
		return flagBaru;
	}

	public void setFlagBaru(String flagBaru) {
		this.flagBaru = flagBaru;
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
