package bph.entities.senggara;


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
import bph.entities.kod.LokasiPermohonan;
import bph.entities.kod.Status;

@Entity
@Table(name = "mtn_inden_kerja")
public class MtnIndenKerja {

	
	@Id
	@Column(name = "id")
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "id_lokasi")
	private LokasiPermohonan lokasi;
	
	@Column(name = "vot")
	private String vot;
	
	@Column(name = "no_inden")
	private String noInden;
	
	@Column(name = "kerja")
	private String kerja;	

	@Column(name = "tarikh_inden_kerja")
	@Temporal(TemporalType.DATE)
	private Date tarikhIndenKerja;
	
	@ManyToOne
	@JoinColumn(name = "id_kontraktor")
	private MtnKontraktor kontraktor;

	@Column(name = "jumlah")
	private Double jumlah;
	
	@Column(name = "tarikh_tt_pengesyor")
	@Temporal(TemporalType.DATE)
	private Date tarikhTTPengesyor;
	
	@Column(name = "tarikh_tt_kpsu")
	@Temporal(TemporalType.DATE)
	private Date tarikhTTKPSU;
	
	@Column(name = "tarikh_tt_kew")
	@Temporal(TemporalType.DATE)
	private Date tarikhTTKew;
	
	@Column(name = "tarikh_tt_sub")
	@Temporal(TemporalType.DATE)
	private Date tarikhTTSUB;
	
	@Column(name = "tarikh_sst")
	@Temporal(TemporalType.DATE)
	private Date tarikhSST;
	
	@Column(name = "tarikh_akhir_siap_kerja")
	@Temporal(TemporalType.DATE)
	private Date tarikhAkhirSiapKerja;
	
	@Column(name = "no_rujukan_sst")
	private String noRujukanSST;
	
	@Column(name = "tarikh_tt_sst")
	@Temporal(TemporalType.DATE)
	private Date tarikhTTSST;
	
	@ManyToOne
	@JoinColumn(name = "id_penyelia")
	private Users penyelia;
	
	@OneToOne
	@JoinColumn(name = "id_penilaian")
	private MtnPenilaianKontraktor penilaian;
	
	@OneToOne
	@JoinColumn(name = "id_tugasan")
	private MtnAgihanTugas tugasan;	
	
	@Column(name = "tarikh_tuntutan_bayaran")
	@Temporal(TemporalType.DATE)
	private Date tarikhTuntutanBayaran;
	
	@Column(name = "tarikh_hantar_kewangan")
	@Temporal(TemporalType.DATE)
	private Date tarikhHantarKewangan;
	
	@Column(name = "tarikh_terima_kunci")
	@Temporal(TemporalType.DATE)
	private Date tarikhTerimaKunci;
	
	@Column(name = "tarikh_perakuan_siap_kerja")
	@Temporal(TemporalType.DATE)
	private Date tarikhPerakuanSiapKerja;
	
	@Column(name = "tarikh_akhir_waranti")
	@Temporal(TemporalType.DATE)
	private Date tarikhAkhirWaranti;
	
	@OneToOne
	@JoinColumn(name = "id_status")
	private Status status;

	public MtnIndenKerja() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public LokasiPermohonan getLokasi() {
		return lokasi;
	}

	public void setLokasi(LokasiPermohonan lokasi) {
		this.lokasi = lokasi;
	}

	public String getVot() {
		return vot;
	}

	public void setVot(String vot) {
		this.vot = vot;
	}

	public String getNoInden() {
		return noInden;
	}

	public void setNoInden(String noInden) {
		this.noInden = noInden;
	}

	public String getKerja() {
		return kerja;
	}

	public void setKerja(String kerja) {
		this.kerja = kerja;
	}

	public Date getTarikhIndenKerja() {
		return tarikhIndenKerja;
	}

	public void setTarikhIndenKerja(Date tarikhIndenKerja) {
		this.tarikhIndenKerja = tarikhIndenKerja;
	}

	public MtnKontraktor getKontraktor() {
		return kontraktor;
	}

	public void setKontraktor(MtnKontraktor kontraktor) {
		this.kontraktor = kontraktor;
	}

	public Double getJumlah() {
		return jumlah;
	}

	public void setJumlah(Double jumlah) {
		this.jumlah = jumlah;
	}

	public Date getTarikhTTPengesyor() {
		return tarikhTTPengesyor;
	}

	public void setTarikhTTPengesyor(Date tarikhTTPengesyor) {
		this.tarikhTTPengesyor = tarikhTTPengesyor;
	}

	public Date getTarikhTTKPSU() {
		return tarikhTTKPSU;
	}

	public void setTarikhTTKPSU(Date tarikhTTKPSU) {
		this.tarikhTTKPSU = tarikhTTKPSU;
	}

	public Date getTarikhTTKew() {
		return tarikhTTKew;
	}

	public void setTarikhTTKew(Date tarikhTTKew) {
		this.tarikhTTKew = tarikhTTKew;
	}

	public Date getTarikhTTSUB() {
		return tarikhTTSUB;
	}

	public void setTarikhTTSUB(Date tarikhTTSUB) {
		this.tarikhTTSUB = tarikhTTSUB;
	}

	public Date getTarikhSST() {
		return tarikhSST;
	}

	public void setTarikhSST(Date tarikhSST) {
		this.tarikhSST = tarikhSST;
	}

	public Date getTarikhAkhirSiapKerja() {
		return tarikhAkhirSiapKerja;
	}

	public void setTarikhAkhirSiapKerja(Date tarikhAkhirSiapKerja) {
		this.tarikhAkhirSiapKerja = tarikhAkhirSiapKerja;
	}

	public String getNoRujukanSST() {
		return noRujukanSST;
	}

	public void setNoRujukanSST(String noRujukanSST) {
		this.noRujukanSST = noRujukanSST;
	}

	public Date getTarikhTTSST() {
		return tarikhTTSST;
	}

	public void setTarikhTTSST(Date tarikhTTSST) {
		this.tarikhTTSST = tarikhTTSST;
	}

	public Users getPenyelia() {
		return penyelia;
	}

	public void setPenyelia(Users penyelia) {
		this.penyelia = penyelia;
	}

	public MtnPenilaianKontraktor getPenilaian() {
		return penilaian;
	}

	public void setPenilaian(MtnPenilaianKontraktor penilaian) {
		this.penilaian = penilaian;
	}

	public MtnAgihanTugas getTugasan() {
		return tugasan;
	}

	public void setTugasan(MtnAgihanTugas tugasan) {
		this.tugasan = tugasan;
	}

	public Date getTarikhTuntutanBayaran() {
		return tarikhTuntutanBayaran;
	}

	public void setTarikhTuntutanBayaran(Date tarikhTuntutanBayaran) {
		this.tarikhTuntutanBayaran = tarikhTuntutanBayaran;
	}

	public Date getTarikhHantarKewangan() {
		return tarikhHantarKewangan;
	}

	public void setTarikhHantarKewangan(Date tarikhHantarKewangan) {
		this.tarikhHantarKewangan = tarikhHantarKewangan;
	}

	public Date getTarikhTerimaKunci() {
		return tarikhTerimaKunci;
	}

	public void setTarikhTerimaKunci(Date tarikhTerimaKunci) {
		this.tarikhTerimaKunci = tarikhTerimaKunci;
	}

	public Date getTarikhPerakuanSiapKerja() {
		return tarikhPerakuanSiapKerja;
	}

	public void setTarikhPerakuanSiapKerja(Date tarikhPerakuanSiapKerja) {
		this.tarikhPerakuanSiapKerja = tarikhPerakuanSiapKerja;
	}

	public Date getTarikhAkhirWaranti() {
		return tarikhAkhirWaranti;
	}

	public void setTarikhAkhirWaranti(Date tarikhAkhirWaranti) {
		this.tarikhAkhirWaranti = tarikhAkhirWaranti;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}