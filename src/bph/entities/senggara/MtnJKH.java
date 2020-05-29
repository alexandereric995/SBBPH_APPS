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

@Entity
@Table(name = "mtn_jkh")
public class MtnJKH {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_kuarters")
	private MtnKuarters kuartersSenggara;

	@Column(name = "no_jkh")
	private String noJKH;

	@Column(name = "tarikh_jkh")
	@Temporal(TemporalType.DATE)
	private Date tarikhJKH;

	@Column(name = "jumlah")
	private Double jumlah;

	@Column(name = "gst")
	private Double gst;

	@Column(name = "jumlah_keseluruhan")
	private Double jumlahKeseluruhan;

	@Column(name = "file_jkh")
	private String fileJKH;

	@ManyToOne
	@JoinColumn(name = "id_penyedia")
	private Users penyedia;

	@Column(name = "catatan_penyedia")
	private String catatanPenyedia;

	@ManyToOne
	@JoinColumn(name = "id_penyemak")
	private Users penyemak;

	@Column(name = "tarikh_semakan")
	@Temporal(TemporalType.DATE)
	private Date tarikhSemakan;

	@Column(name = "catatan_penyemak")
	private String catatanPenyemak;

	@Column(name = "flag_keputusan_penyemak")
	private String flagKeputusanPenyemak;

	@ManyToOne
	@JoinColumn(name = "id_pelulus")
	private Users pelulus;

	@Column(name = "tarikh_kelulusan")
	@Temporal(TemporalType.DATE)
	private Date tarikhKelulusan;

	@Column(name = "catatan_pelulus")
	private String catatanPelulus;

	@Column(name = "flag_keputusan_pelulus")
	private String flagKeputusanPelulus;

	@ManyToOne
	@JoinColumn(name = "id_inden_kerja")
	private MtnIndenKerja indenKerja;

	@Column(name = "status_pembaikan")
	private String statusPembaikan;

	@Column(name = "catatan_pembaikan")
	private String catatanPembaikan;

	@Column(name = "file_laporan_pembaikan")
	private String fileLaporanPembaikan;

	@Column(name = "tarikh_selesai_pembaikan")
	@Temporal(TemporalType.DATE)
	private Date tarikhSelesaiPembaikan;

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

	public MtnJKH() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());
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

	public String getNoJKH() {
		return noJKH;
	}

	public void setNoJKH(String noJKH) {
		this.noJKH = noJKH;
	}

	public Date getTarikhJKH() {
		return tarikhJKH;
	}

	public void setTarikhJKH(Date tarikhJKH) {
		this.tarikhJKH = tarikhJKH;
	}

	public Double getJumlah() {
		return jumlah;
	}

	public void setJumlah(Double jumlah) {
		this.jumlah = jumlah;
	}

	public Double getGst() {
		return gst;
	}

	public void setGst(Double gst) {
		this.gst = gst;
	}

	public Double getJumlahKeseluruhan() {
		return jumlahKeseluruhan;
	}

	public void setJumlahKeseluruhan(Double jumlahKeseluruhan) {
		this.jumlahKeseluruhan = jumlahKeseluruhan;
	}

	public String getFileJKH() {
		return fileJKH;
	}

	public void setFileJKH(String fileJKH) {
		this.fileJKH = fileJKH;
	}

	public Users getPenyedia() {
		return penyedia;
	}

	public void setPenyedia(Users penyedia) {
		this.penyedia = penyedia;
	}

	public String getCatatanPenyedia() {
		return catatanPenyedia;
	}

	public void setCatatanPenyedia(String catatanPenyedia) {
		this.catatanPenyedia = catatanPenyedia;
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

	public String getCatatanPenyemak() {
		return catatanPenyemak;
	}

	public void setCatatanPenyemak(String catatanPenyemak) {
		this.catatanPenyemak = catatanPenyemak;
	}

	public String getFlagKeputusanPenyemak() {
		return flagKeputusanPenyemak;
	}

	public void setFlagKeputusanPenyemak(String flagKeputusanPenyemak) {
		this.flagKeputusanPenyemak = flagKeputusanPenyemak;
	}

	public Users getPelulus() {
		return pelulus;
	}

	public void setPelulus(Users pelulus) {
		this.pelulus = pelulus;
	}

	public Date getTarikhKelulusan() {
		return tarikhKelulusan;
	}

	public void setTarikhKelulusan(Date tarikhKelulusan) {
		this.tarikhKelulusan = tarikhKelulusan;
	}

	public String getCatatanPelulus() {
		return catatanPelulus;
	}

	public void setCatatanPelulus(String catatanPelulus) {
		this.catatanPelulus = catatanPelulus;
	}

	public String getFlagKeputusanPelulus() {
		return flagKeputusanPelulus;
	}

	public void setFlagKeputusanPelulus(String flagKeputusanPelulus) {
		this.flagKeputusanPelulus = flagKeputusanPelulus;
	}

	public MtnIndenKerja getIndenKerja() {
		return indenKerja;
	}

	public void setIndenKerja(MtnIndenKerja indenKerja) {
		this.indenKerja = indenKerja;
	}

	public String getStatusPembaikan() {
		return statusPembaikan;
	}

	public void setStatusPembaikan(String statusPembaikan) {
		this.statusPembaikan = statusPembaikan;
	}

	public String getCatatanPembaikan() {
		return catatanPembaikan;
	}

	public void setCatatanPembaikan(String catatanPembaikan) {
		this.catatanPembaikan = catatanPembaikan;
	}

	public String getFileLaporanPembaikan() {
		return fileLaporanPembaikan;
	}

	public void setFileLaporanPembaikan(String fileLaporanPembaikan) {
		this.fileLaporanPembaikan = fileLaporanPembaikan;
	}

	public Date getTarikhSelesaiPembaikan() {
		return tarikhSelesaiPembaikan;
	}

	public void setTarikhSelesaiPembaikan(Date tarikhSelesaiPembaikan) {
		this.tarikhSelesaiPembaikan = tarikhSelesaiPembaikan;
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
