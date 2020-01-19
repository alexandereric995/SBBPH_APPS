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
@Table(name = "cms_keharta_aktiviti")
public class CmsKehartaAktiviti {

	@Id
	@Column(name = "id")
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "id_keharta")
	private CmsKeharta keharta;
	
	@Column(name = "nama_aktiviti")
	private String namaAktiviti;
	
	@Column(name = "keterangan")
	private String keterangan;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_aktiviti")
	private Date tarikhAktiviti;
	
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
	
	public CmsKehartaAktiviti() {
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

	public CmsKeharta getKeharta() {
		return keharta;
	}

	public void setKeharta(CmsKeharta keharta) {
		this.keharta = keharta;
	}

	public String getNamaAktiviti() {
		return namaAktiviti;
	}

	public void setNamaAktiviti(String namaAktiviti) {
		this.namaAktiviti = namaAktiviti;
	}

	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
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
