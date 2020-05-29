package bph.entities.kod;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lebah.template.UID;
import portal.module.entity.Users;

@Entity
@Table(name = "ruj_cuti")
public class Cuti {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "perkara")
	private String perkara;

	@Column(name = "keterangan")
	private String keterangan;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_dari")
	private Date tarikhDari;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_hingga")
	private Date tarikhHingga;

	@OneToOne
	@JoinColumn(name = "id_masuk")
	private Users idMasuk;

	@OneToOne
	@JoinColumn(name = "id_kemaskini")
	private Users idKemaskini;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_masuk")
	private Date tarikhMasuk;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_kemaskini")
	private Date tarikhKemaskini;

	public Cuti() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPerkara() {
		return perkara;
	}

	public void setPerkara(String perkara) {
		this.perkara = perkara;
	}

	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}

	public Date getTarikhDari() {
		return tarikhDari;
	}

	public void setTarikhDari(Date tarikhDari) {
		this.tarikhDari = tarikhDari;
	}

	public Date getTarikhHingga() {
		return tarikhHingga;
	}

	public void setTarikhHingga(Date tarikhHingga) {
		this.tarikhHingga = tarikhHingga;
	}

	public Users getIdMasuk() {
		return idMasuk;
	}

	public void setIdMasuk(Users idMasuk) {
		this.idMasuk = idMasuk;
	}

	public Users getIdKemaskini() {
		return idKemaskini;
	}

	public void setIdKemaskini(Users idKemaskini) {
		this.idKemaskini = idKemaskini;
	}

	public Date getTarikhMasuk() {
		return tarikhMasuk;
	}

	public void setTarikhMasuk(Date tarikhMasuk) {
		this.tarikhMasuk = tarikhMasuk;
	}

	public Date getTarikhKemaskini() {
		return tarikhKemaskini;
	}

	public void setTarikhKemaskini(Date tarikhKemaskini) {
		this.tarikhKemaskini = tarikhKemaskini;
	}

	@SuppressWarnings("deprecation")
	public Integer getTotalHariCuti() {
		Integer days = 0;
		if (this.tarikhHingga != null && this.tarikhDari != null) {
			days = this.tarikhHingga.getDate() - this.tarikhDari.getDate();
		}
		return days;
	}

}
