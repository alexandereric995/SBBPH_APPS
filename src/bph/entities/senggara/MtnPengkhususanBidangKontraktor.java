package bph.entities.senggara;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lebah.template.UID;
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

	public MtnPengkhususanBidangKontraktor() {
		setId(UID.getUID());
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
}
