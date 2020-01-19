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

@Entity
@Table(name = "mtn_lawatan_tapak")
public class MtnLawatanTapak {

	
	@Id
	@Column(name = "id")
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "id_kuarters")
	private MtnKuarters kuartersSenggara;

	@Column(name = "tarikh_lawatan")
	@Temporal(TemporalType.DATE)
	private Date tarikhLawatan;
	
	@Column(name = "ulasan_lawatan")
	private String ulasanLawatan;
	
	@Column(name = "flag_pembaikan")
	private String flagPembaikan;
	
	@Column(name = "kontraktor")
	private String kontraktor;
	
	@Column(name = "kontraktor_lain")
	private String kontraktorLain;
	
	@Column(name = "tarikh_serah_kontraktor")
	@Temporal(TemporalType.DATE)
	private Date tarikhSerahKontraktor;
	
	@Column(name = "tarikh_terima_kontraktor")
	@Temporal(TemporalType.DATE)
	private Date tarikhTerimaKontraktor;
	
	@Column(name = "ulasan_pembaikan_major")
	private String ulasanPembaikanMajor;
	
	@Column(name = "flag_aktif")
	private String flagAktif;
	
	@Column(name = "file_laporan_kerosakan")
	private String fileLaporanKerosakan;
	
	public MtnLawatanTapak() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public MtnKuarters getKuartersSenggara() {
		return kuartersSenggara;
	}

	public void setKuartersSenggara(MtnKuarters kuartersSenggara) {
		this.kuartersSenggara = kuartersSenggara;
	}

	public Date getTarikhLawatan() {
		return tarikhLawatan;
	}

	public void setTarikhLawatan(Date tarikhLawatan) {
		this.tarikhLawatan = tarikhLawatan;
	}

	public String getUlasanLawatan() {
		return ulasanLawatan;
	}

	public void setUlasanLawatan(String ulasanLawatan) {
		this.ulasanLawatan = ulasanLawatan;
	}

	public String getFlagPembaikan() {
		return flagPembaikan;
	}

	public void setFlagPembaikan(String flagPembaikan) {
		this.flagPembaikan = flagPembaikan;
	}

	public String getKontraktor() {
		return kontraktor;
	}

	public void setKontraktor(String kontraktor) {
		this.kontraktor = kontraktor;
	}

	public String getKontraktorLain() {
		return kontraktorLain;
	}

	public void setKontraktorLain(String kontraktorLain) {
		this.kontraktorLain = kontraktorLain;
	}

	public Date getTarikhSerahKontraktor() {
		return tarikhSerahKontraktor;
	}

	public void setTarikhSerahKontraktor(Date tarikhSerahKontraktor) {
		this.tarikhSerahKontraktor = tarikhSerahKontraktor;
	}

	public Date getTarikhTerimaKontraktor() {
		return tarikhTerimaKontraktor;
	}

	public void setTarikhTerimaKontraktor(Date tarikhTerimaKontraktor) {
		this.tarikhTerimaKontraktor = tarikhTerimaKontraktor;
	}

	public String getUlasanPembaikanMajor() {
		return ulasanPembaikanMajor;
	}

	public void setUlasanPembaikanMajor(String ulasanPembaikanMajor) {
		this.ulasanPembaikanMajor = ulasanPembaikanMajor;
	}

	public String getFlagAktif() {
		return flagAktif;
	}

	public void setFlagAktif(String flagAktif) {
		this.flagAktif = flagAktif;
	}

	public String getFileLaporanKerosakan() {
		return fileLaporanKerosakan;
	}

	public void setFileLaporanKerosakan(String fileLaporanKerosakan) {
		this.fileLaporanKerosakan = fileLaporanKerosakan;
	}
}
