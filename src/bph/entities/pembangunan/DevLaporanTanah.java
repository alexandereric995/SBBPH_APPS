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

@Entity
@Table(name = "dev_laporan_tanah")
public class DevLaporanTanah {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_hakmilik")
	private DevHakmilik hakmilik;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_lawatan_tapak")
	private Date tarikhLawatanTapak;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_laporan")
	private Date tarikhLaporan;

	@Column(name = "laporan_oleh")
	private String laporanOleh;

	@Column(name = "jawatan_pelapor")
	private String jawatanPelapor;

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

	@Column(name = "catatan")
	private String catatan;

	public DevLaporanTanah() {
		setId(UID.getUID());
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

	public Date getTarikhLawatanTapak() {
		return tarikhLawatanTapak;
	}

	public void setTarikhLawatanTapak(Date tarikhLawatanTapak) {
		this.tarikhLawatanTapak = tarikhLawatanTapak;
	}

	public Date getTarikhLaporan() {
		return tarikhLaporan;
	}

	public void setTarikhLaporan(Date tarikhLaporan) {
		this.tarikhLaporan = tarikhLaporan;
	}

	public String getLaporanOleh() {
		return laporanOleh;
	}

	public void setLaporanOleh(String laporanOleh) {
		this.laporanOleh = laporanOleh;
	}

	public String getJawatanPelapor() {
		return jawatanPelapor;
	}

	public void setJawatanPelapor(String jawatanPelapor) {
		this.jawatanPelapor = jawatanPelapor;
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

	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}
}
