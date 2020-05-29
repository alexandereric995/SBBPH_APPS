package bph.entities.utiliti;

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
import bph.entities.kod.Bandar;
import bph.entities.kod.KodPusatTerima;
import bph.entities.kod.Seksyen;

@Entity
@Table(name = "util_dewan")
public class UtilDewan {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "nama")
	private String nama;

	@Column(name = "alamat1")
	private String alamat1;

	@Column(name = "alamat2")
	private String alamat2;

	@Column(name = "alamat3")
	private String alamat3;

	@Column(name = "poskod")
	private String poskod;

	@Column(name = "lokasi")
	private String lokasi;

	@ManyToOne
	@JoinColumn(name = "bandar")
	private Bandar bandar;

	@Column(name = "catatan")
	private String catatan;

	@Column(name = "kadar_sewa")
	private double kadarSewa;

	@Column(name = "kadar_sewa_awam")
	private double kadarSewaAwam;

	@ManyToOne
	@JoinColumn(name = "id_seksyen")
	private Seksyen seksyen;

	@Column(name = "waktu_buka")
	private String waktuBuka;

	@Column(name = "waktu_tutup")
	private String waktuTutup;

	@Column(name = "nama_pegawai")
	private String namaPegawai;

	@Column(name = "no_telefon_pegawai")
	private String noTelefonPegawai;

	@Column(name = "status")
	private String status;

	@Column(name = "flag_aktif")
	private String flagAktif;

	@Column(name = "seq")
	private Integer seq;

	@ManyToOne
	@JoinColumn(name = "kod_cawangan")
	private KodPusatTerima kodCawangan;

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

	public UtilDewan() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getAlamat1() {
		return alamat1;
	}

	public void setAlamat1(String alamat1) {
		this.alamat1 = alamat1;
	}

	public String getAlamat2() {
		return alamat2;
	}

	public void setAlamat2(String alamat2) {
		this.alamat2 = alamat2;
	}

	public String getAlamat3() {
		return alamat3;
	}

	public void setAlamat3(String alamat3) {
		this.alamat3 = alamat3;
	}

	public String getPoskod() {
		return poskod;
	}

	public void setPoskod(String poskod) {
		this.poskod = poskod;
	}

	public String getLokasi() {
		return lokasi;
	}

	public void setLokasi(String lokasi) {
		this.lokasi = lokasi;
	}

	public Bandar getBandar() {
		return bandar;
	}

	public void setBandar(Bandar bandar) {
		this.bandar = bandar;
	}

	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}

	public double getKadarSewa() {
		return kadarSewa;
	}

	public void setKadarSewa(double kadarSewa) {
		this.kadarSewa = kadarSewa;
	}

	public double getKadarSewaAwam() {
		return kadarSewaAwam;
	}

	public void setKadarSewaAwam(double kadarSewaAwam) {
		this.kadarSewaAwam = kadarSewaAwam;
	}

	public Seksyen getSeksyen() {
		return seksyen;
	}

	public void setSeksyen(Seksyen seksyen) {
		this.seksyen = seksyen;
	}

	public String getWaktuBuka() {
		return waktuBuka;
	}

	public void setWaktuBuka(String waktuBuka) {
		this.waktuBuka = waktuBuka;
	}

	public String getWaktuTutup() {
		return waktuTutup;
	}

	public void setWaktuTutup(String waktuTutup) {
		this.waktuTutup = waktuTutup;
	}

	public String getNamaPegawai() {
		return namaPegawai;
	}

	public void setNamaPegawai(String namaPegawai) {
		this.namaPegawai = namaPegawai;
	}

	public String getNoTelefonPegawai() {
		return noTelefonPegawai;
	}

	public void setNoTelefonPegawai(String noTelefonPegawai) {
		this.noTelefonPegawai = noTelefonPegawai;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public KodPusatTerima getKodCawangan() {
		return kodCawangan;
	}

	public void setKodCawangan(KodPusatTerima kodCawangan) {
		this.kodCawangan = kodCawangan;
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