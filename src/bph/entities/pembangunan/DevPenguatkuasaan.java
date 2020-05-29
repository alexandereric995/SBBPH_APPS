package bph.entities.pembangunan;

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
@Table(name = "dev_penguatkuasaan")
public class DevPenguatkuasaan {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_hakmilik")
	private DevHakmilik hakmilik;

	@Column(name = "tajuk")
	private String tajuk;

	@Column(name = "no_rujukan")
	private String noRujukan;

	@Column(name = "keterangan")
	private String keterangan;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_aduan")
	private Date tarikhAduan;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_siasatan")
	private Date tarikhSiasatan;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_penguatkuasaan")
	private Date tarikhPenguatkuasaan;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_laporan")
	private Date tarikhLaporan;

	@Column(name = "tujuan_laporan")
	private String tujuanLaporan;

	@Column(name = "lokasi_tanah")
	private String lokasiTanah;

	@Column(name = "jalan_hubungan")
	private String jalanHubungan;

	@Column(name = "kawasan_berhampiran")
	private String kawasanBerhampiran;

	@Column(name = "jarak_dari_bandar")
	private String jarakDariBandar;

	@Column(name = "keadaan_muka_bumi")
	private String keadaanMukaBumi;

	@Column(name = "butiran_atas_tanah")
	private String butiranAtasTanah;

	@Column(name = "kemudahan_asas")
	private String kemudahanAsas;

	@Column(name = "utara")
	private String utara;

	@Column(name = "timur")
	private String timur;

	@Column(name = "selatan")
	private String selatan;

	@Column(name = "barat")
	private String barat;

	@Column(name = "laporan_terkini_atas_tanah")
	private String laporanTerkiniAtasTanah;

	@Column(name = "pengambilan_tanah")
	private String pengambilanTanah;

	@Column(name = "pelan_gambar")
	private String pelanGambar;

	@Column(name = "ulasan")
	private String ulasan;

	@Column(name = "syor")
	private String syor;

	@ManyToOne
	@JoinColumn(name = "laporan_oleh")
	private Users laporanOleh;

	@ManyToOne
	@JoinColumn(name = "kemaskini_oleh")
	private Users kemaskiniOleh;

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

	public DevPenguatkuasaan() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DevHakmilik getHakmilik() {
		return hakmilik;
	}

	public void setHakmilik(DevHakmilik hakmilik) {
		this.hakmilik = hakmilik;
	}

	public String getTajuk() {
		return tajuk;
	}

	public void setTajuk(String tajuk) {
		this.tajuk = tajuk;
	}

	public String getNoRujukan() {
		return noRujukan;
	}

	public void setNoRujukan(String noRujukan) {
		this.noRujukan = noRujukan;
	}

	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}

	public Date getTarikhAduan() {
		return tarikhAduan;
	}

	public void setTarikhAduan(Date tarikhAduan) {
		this.tarikhAduan = tarikhAduan;
	}

	public Date getTarikhSiasatan() {
		return tarikhSiasatan;
	}

	public void setTarikhSiasatan(Date tarikhSiasatan) {
		this.tarikhSiasatan = tarikhSiasatan;
	}

	public Date getTarikhPenguatkuasaan() {
		return tarikhPenguatkuasaan;
	}

	public void setTarikhPenguatkuasaan(Date tarikhPenguatkuasaan) {
		this.tarikhPenguatkuasaan = tarikhPenguatkuasaan;
	}

	public Date getTarikhLaporan() {
		return tarikhLaporan;
	}

	public void setTarikhLaporan(Date tarikhLaporan) {
		this.tarikhLaporan = tarikhLaporan;
	}

	public String getTujuanLaporan() {
		return tujuanLaporan;
	}

	public void setTujuanLaporan(String tujuanLaporan) {
		this.tujuanLaporan = tujuanLaporan;
	}

	public String getLokasiTanah() {
		return lokasiTanah;
	}

	public void setLokasiTanah(String lokasiTanah) {
		this.lokasiTanah = lokasiTanah;
	}

	public String getJalanHubungan() {
		return jalanHubungan;
	}

	public void setJalanHubungan(String jalanHubungan) {
		this.jalanHubungan = jalanHubungan;
	}

	public String getKawasanBerhampiran() {
		return kawasanBerhampiran;
	}

	public void setKawasanBerhampiran(String kawasanBerhampiran) {
		this.kawasanBerhampiran = kawasanBerhampiran;
	}

	public String getJarakDariBandar() {
		return jarakDariBandar;
	}

	public void setJarakDariBandar(String jarakDariBandar) {
		this.jarakDariBandar = jarakDariBandar;
	}

	public String getKeadaanMukaBumi() {
		return keadaanMukaBumi;
	}

	public void setKeadaanMukaBumi(String keadaanMukaBumi) {
		this.keadaanMukaBumi = keadaanMukaBumi;
	}

	public String getButiranAtasTanah() {
		return butiranAtasTanah;
	}

	public void setButiranAtasTanah(String butiranAtasTanah) {
		this.butiranAtasTanah = butiranAtasTanah;
	}

	public String getKemudahanAsas() {
		return kemudahanAsas;
	}

	public void setKemudahanAsas(String kemudahanAsas) {
		this.kemudahanAsas = kemudahanAsas;
	}

	public String getUtara() {
		return utara;
	}

	public void setUtara(String utara) {
		this.utara = utara;
	}

	public String getTimur() {
		return timur;
	}

	public void setTimur(String timur) {
		this.timur = timur;
	}

	public String getSelatan() {
		return selatan;
	}

	public void setSelatan(String selatan) {
		this.selatan = selatan;
	}

	public String getBarat() {
		return barat;
	}

	public void setBarat(String barat) {
		this.barat = barat;
	}

	public String getLaporanTerkiniAtasTanah() {
		return laporanTerkiniAtasTanah;
	}

	public void setLaporanTerkiniAtasTanah(String laporanTerkiniAtasTanah) {
		this.laporanTerkiniAtasTanah = laporanTerkiniAtasTanah;
	}

	public String getPengambilanTanah() {
		return pengambilanTanah;
	}

	public void setPengambilanTanah(String pengambilanTanah) {
		this.pengambilanTanah = pengambilanTanah;
	}

	public String getPelanGambar() {
		return pelanGambar;
	}

	public void setPelanGambar(String pelanGambar) {
		this.pelanGambar = pelanGambar;
	}

	public String getUlasan() {
		return ulasan;
	}

	public void setUlasan(String ulasan) {
		this.ulasan = ulasan;
	}

	public String getSyor() {
		return syor;
	}

	public void setSyor(String syor) {
		this.syor = syor;
	}

	public Date getTarikhMasuk() {
		return tarikhMasuk;
	}

	public void setTarikhMasuk(Date tarikhMasuk) {
		this.tarikhMasuk = tarikhMasuk;
	}

	public Users getLaporanOleh() {
		return laporanOleh;
	}

	public void setLaporanOleh(Users laporanOleh) {
		this.laporanOleh = laporanOleh;
	}

	public Date getTarikhKemaskini() {
		return tarikhKemaskini;
	}

	public void setTarikhKemaskini(Date tarikhKemaskini) {
		this.tarikhKemaskini = tarikhKemaskini;
	}

	public Users getKemaskiniOleh() {
		return kemaskiniOleh;
	}

	public void setKemaskiniOleh(Users kemaskiniOleh) {
		this.kemaskiniOleh = kemaskiniOleh;
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
}
