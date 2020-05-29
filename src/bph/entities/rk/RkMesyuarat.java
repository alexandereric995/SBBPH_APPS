package bph.entities.rk;

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
@Table(name = "rk_mesyuarat")
public class RkMesyuarat {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "tajuk")
	private String tajuk;

	@Column(name = "bil")
	private String bil;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh")
	private Date tarikh;

	@Column(name = "lokasi")
	private String lokasi;

	@Column(name = "catatan")
	private String catatan;

	@Column(name = "status")
	private String status;

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

	public RkMesyuarat() {
		setId(UID.getUID());
		setStatus("B");
		setTarikhMasuk(new Date());
	}

	public String getKeteranganStatus() {
		String status = "";

		if (this.getStatus() != null) {
			if ("B".equals(this.getStatus())) {
				status = "BARU";
			} else if ("S".equals(this.getStatus())) {
				status = "SELESAI";
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

	public String getBil() {
		return bil;
	}

	public void setBil(String bil) {
		this.bil = bil;
	}

	public Date getTarikh() {
		return tarikh;
	}

	public void setTarikh(Date tarikh) {
		this.tarikh = tarikh;
	}

	public String getLokasi() {
		return lokasi;
	}

	public void setLokasi(String lokasi) {
		this.lokasi = lokasi;
	}

	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
