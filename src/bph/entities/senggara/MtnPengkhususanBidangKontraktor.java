package bph.entities.senggara;

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
import bph.entities.kod.GredKontraktor;
import bph.entities.kod.KategoriBidangKontraktor;
import bph.entities.kod.PengkhususanBidangKontraktor;

@Entity
@Table(name = "mtn_pengkhususan_bidang_kontraktor")
public class MtnPengkhususanBidangKontraktor {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "no_pendaftaran")
	private MtnKontraktor kontraktor;

	@ManyToOne
	@JoinColumn(name = "id_gred")
	private GredKontraktor gred;

	@ManyToOne
	@JoinColumn(name = "id_kategori")
	private KategoriBidangKontraktor kategori;

	@ManyToOne
	@JoinColumn(name = "id_pengkhususan")
	private PengkhususanBidangKontraktor pengkhususan;

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

	public MtnPengkhususanBidangKontraktor() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public MtnKontraktor getKontraktor() {
		return kontraktor;
	}

	public void setKontraktor(MtnKontraktor kontraktor) {
		this.kontraktor = kontraktor;
	}

	public GredKontraktor getGred() {
		return gred;
	}

	public void setGred(GredKontraktor gred) {
		this.gred = gred;
	}

	public KategoriBidangKontraktor getKategori() {
		return kategori;
	}

	public void setKategori(KategoriBidangKontraktor kategori) {
		this.kategori = kategori;
	}

	public PengkhususanBidangKontraktor getPengkhususan() {
		return pengkhususan;
	}

	public void setPengkhususan(PengkhususanBidangKontraktor pengkhususan) {
		this.pengkhususan = pengkhususan;
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
