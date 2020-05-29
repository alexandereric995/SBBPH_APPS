package bph.entities.bgs;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lebah.template.UID;
import portal.module.entity.Users;

@Entity
@Table(name = "bgs_kertas_pertimbangan")
public class BgsKertasPertimbangan {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_permohonan")
	private BgsPermohonan permohonan;

	@Column(name = "tujuan")
	private String tujuan;

	@Column(name = "lokasi_ruang_dipohon")
	private String lokasiRuangDipohon;

	@Column(name = "keluasan_ruang_dipohon")
	private String keluasanRuangDipohon;

	@Column(name = "lampiranA")
	private String lampiranA;

	@Column(name = "nama_jabatan")
	private String namaJabatan;

	@Column(name = "bil_kakitangan")
	private String bilKakitangan;

	@Column(name = "pejabat_sediaada")
	private String pejabatSediaada;

	@Column(name = "cadangan_ruang")
	private String cadanganRuang;

	@Column(name = "alasan1")
	private String alasan1;

	@Column(name = "alasan2")
	private String alasan2;

	@Column(name = "alasan3")
	private String alasan3;

	@Column(name = "lampiranB")
	private String lampiranB;

	@Column(name = "nama_pengurus_bangunan")
	private String namaPengurusBangunan;

	@Column(name = "ulasan_pengurus_bangunan")
	private String ulasanPengurusBangunan;

	@Column(name = "lampiranC")
	private String lampiranC;

	@Column(name = "nama_pengerusi_bangunan")
	private String namaPengerusiBangunan;

	@Column(name = "ulasan_pengerusi_bangunan")
	private String ulasanPengerusiBangunan;

	@Column(name = "lampiranD")
	private String lampiranD;

	@OneToOne
	@JoinColumn(name = "id_penyedia")
	private Users penyedia;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_penyediaan")
	private Date tarikhPenyediaan;

	@Column(name = "ulasan_penyedia")
	private String ulasanPenyedia;

	@OneToOne
	@JoinColumn(name = "id_penyemak")
	private Users penyemak;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_semakan")
	private Date tarikhSemakan;

	@Column(name = "ulasan_penyemak")
	private String ulasanPenyemak;

	@Column(name = "flag_keputusan_semakan")
	private String flagKeputusanSemakan;

	@OneToOne
	@JoinColumn(name = "id_pengesah")
	private Users pengesah;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_pengesahan")
	private Date tarikhPengesahan;

	@Column(name = "ulasan_pengesah")
	private String ulasanPengesah;

	@Column(name = "flag_keputusan_pengesahan")
	private String flagKeputusanPengesahan;

	@ManyToOne
	@JoinColumn(name = "id_masuk")
	private Users idMasuk;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_masuk")
	private Date tarikhMasuk;

	@ManyToOne
	@JoinColumn(name = "id_kemaskini")
	private Users idKemaskini;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_kemaskini")
	private Date tarikhKemaskini;

	public BgsKertasPertimbangan() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BgsPermohonan getPermohonan() {
		return permohonan;
	}

	public void setPermohonan(BgsPermohonan permohonan) {
		this.permohonan = permohonan;
	}

	public String getTujuan() {
		return tujuan;
	}

	public void setTujuan(String tujuan) {
		this.tujuan = tujuan;
	}

	public String getLokasiRuangDipohon() {
		return lokasiRuangDipohon;
	}

	public void setLokasiRuangDipohon(String lokasiRuangDipohon) {
		this.lokasiRuangDipohon = lokasiRuangDipohon;
	}

	public String getKeluasanRuangDipohon() {
		return keluasanRuangDipohon;
	}

	public void setKeluasanRuangDipohon(String keluasanRuangDipohon) {
		this.keluasanRuangDipohon = keluasanRuangDipohon;
	}

	public String getLampiranA() {
		return lampiranA;
	}

	public void setLampiranA(String lampiranA) {
		this.lampiranA = lampiranA;
	}

	public String getNamaJabatan() {
		return namaJabatan;
	}

	public void setNamaJabatan(String namaJabatan) {
		this.namaJabatan = namaJabatan;
	}

	public String getBilKakitangan() {
		return bilKakitangan;
	}

	public void setBilKakitangan(String bilKakitangan) {
		this.bilKakitangan = bilKakitangan;
	}

	public String getPejabatSediaada() {
		return pejabatSediaada;
	}

	public void setPejabatSediaada(String pejabatSediaada) {
		this.pejabatSediaada = pejabatSediaada;
	}

	public String getCadanganRuang() {
		return cadanganRuang;
	}

	public void setCadanganRuang(String cadanganRuang) {
		this.cadanganRuang = cadanganRuang;
	}

	public String getAlasan1() {
		return alasan1;
	}

	public void setAlasan1(String alasan1) {
		this.alasan1 = alasan1;
	}

	public String getAlasan2() {
		return alasan2;
	}

	public void setAlasan2(String alasan2) {
		this.alasan2 = alasan2;
	}

	public String getAlasan3() {
		return alasan3;
	}

	public void setAlasan3(String alasan3) {
		this.alasan3 = alasan3;
	}

	public String getLampiranB() {
		return lampiranB;
	}

	public void setLampiranB(String lampiranB) {
		this.lampiranB = lampiranB;
	}

	public String getNamaPengurusBangunan() {
		return namaPengurusBangunan;
	}

	public void setNamaPengurusBangunan(String namaPengurusBangunan) {
		this.namaPengurusBangunan = namaPengurusBangunan;
	}

	public String getUlasanPengurusBangunan() {
		return ulasanPengurusBangunan;
	}

	public void setUlasanPengurusBangunan(String ulasanPengurusBangunan) {
		this.ulasanPengurusBangunan = ulasanPengurusBangunan;
	}

	public String getLampiranC() {
		return lampiranC;
	}

	public void setLampiranC(String lampiranC) {
		this.lampiranC = lampiranC;
	}

	public String getNamaPengerusiBangunan() {
		return namaPengerusiBangunan;
	}

	public void setNamaPengerusiBangunan(String namaPengerusiBangunan) {
		this.namaPengerusiBangunan = namaPengerusiBangunan;
	}

	public String getUlasanPengerusiBangunan() {
		return ulasanPengerusiBangunan;
	}

	public void setUlasanPengerusiBangunan(String ulasanPengerusiBangunan) {
		this.ulasanPengerusiBangunan = ulasanPengerusiBangunan;
	}

	public String getLampiranD() {
		return lampiranD;
	}

	public void setLampiranD(String lampiranD) {
		this.lampiranD = lampiranD;
	}

	public Users getPenyedia() {
		return penyedia;
	}

	public void setPenyedia(Users penyedia) {
		this.penyedia = penyedia;
	}

	public Date getTarikhPenyediaan() {
		return tarikhPenyediaan;
	}

	public void setTarikhPenyediaan(Date tarikhPenyediaan) {
		this.tarikhPenyediaan = tarikhPenyediaan;
	}

	public String getUlasanPenyedia() {
		return ulasanPenyedia;
	}

	public void setUlasanPenyedia(String ulasanPenyedia) {
		this.ulasanPenyedia = ulasanPenyedia;
	}

	public Users getPenyemak() {
		return penyemak;
	}

	public void setPenyemak(Users penyemak) {
		this.penyemak = penyemak;
	}

	public Date getTarikhSemakan() {
		return tarikhSemakan;
	}

	public void setTarikhSemakan(Date tarikhSemakan) {
		this.tarikhSemakan = tarikhSemakan;
	}

	public String getUlasanPenyemak() {
		return ulasanPenyemak;
	}

	public void setUlasanPenyemak(String ulasanPenyemak) {
		this.ulasanPenyemak = ulasanPenyemak;
	}

	public String getFlagKeputusanSemakan() {
		return flagKeputusanSemakan;
	}

	public void setFlagKeputusanSemakan(String flagKeputusanSemakan) {
		this.flagKeputusanSemakan = flagKeputusanSemakan;
	}

	public Users getPengesah() {
		return pengesah;
	}

	public void setPengesah(Users pengesah) {
		this.pengesah = pengesah;
	}

	public Date getTarikhPengesahan() {
		return tarikhPengesahan;
	}

	public void setTarikhPengesahan(Date tarikhPengesahan) {
		this.tarikhPengesahan = tarikhPengesahan;
	}

	public String getUlasanPengesah() {
		return ulasanPengesah;
	}

	public void setUlasanPengesah(String ulasanPengesah) {
		this.ulasanPengesah = ulasanPengesah;
	}

	public String getFlagKeputusanPengesahan() {
		return flagKeputusanPengesahan;
	}

	public void setFlagKeputusanPengesahan(String flagKeputusanPengesahan) {
		this.flagKeputusanPengesahan = flagKeputusanPengesahan;
	}

	public Users getIdMasuk() {
		return idMasuk;
	}

	public void setIdMasuk(Users idMasuk) {
		this.idMasuk = idMasuk;
	}

	public Date getTarikhMasuk() {
		return tarikhMasuk;
	}

	public void setTarikhMasuk(Date tarikhMasuk) {
		this.tarikhMasuk = tarikhMasuk;
	}

	public Users getIdKemaskini() {
		return idKemaskini;
	}

	public void setIdKemaskini(Users idKemaskini) {
		this.idKemaskini = idKemaskini;
	}

	public Date getTarikhKemaskini() {
		return tarikhKemaskini;
	}

	public void setTarikhKemaskini(Date tarikhKemaskini) {
		this.tarikhKemaskini = tarikhKemaskini;
	}
}