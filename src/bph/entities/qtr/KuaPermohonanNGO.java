package bph.entities.qtr;

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
import bph.entities.kod.Status;

@Entity
@Table(name = "kua_permohonan_ngo")
public class KuaPermohonanNGO {

	@Id
	@Column(name = "id")
	private String id;
	
	@Column(name = "no_fail")
	private String noFail;
	
	@Column(name = "jenis_permohonan")
	private String jenisPermohonan;
	
	@ManyToOne
	@JoinColumn(name = "id_status")
	private Status status;
	
	@Column(name = "no_rujukan_permohonan")
	private String noRujukanPermohonan;
	
	@Column(name = "tarikh_permohonan")
	@Temporal(TemporalType.DATE)
	private Date tarikhPermohonan;
	
	@Column(name = "tarikh_terima_permohonan")
	@Temporal(TemporalType.DATE)
	private Date tarikhTerimaPermohonan;
	
	@Column(name = "id_pemohon")
	private String idPemohon;
	
	@Column(name = "nama_pemohon")
	private String namaPemohon;
	
	@Column(name = "jawatan_pemohon")
	private String jawatanPemohon;
	
	@Column(name = "alamat1_pemohon")
	private String alamat1Pemohon;
	
	@Column(name = "alamat2_pemohon")
	private String alamat2Pemohon;
	
	@Column(name = "alamat3_pemohon")
	private String alamat3Pemohon;
	
	@Column(name = "poskod_pemohon")
	private String poskodPemohon;
	
	@ManyToOne
	@JoinColumn(name = "id_bandar_pemohon")
	private Bandar bandarPemohon;
	
	@Column(name = "no_telefon_bimbit")
	private String noTelefonBimbit;
	
	@Column(name = "emel")
	private String emel;
	
	@Column(name = "nama_agensi")
	private String namaAgensi;
	
	@Column(name = "alamat1_agensi")
	private String alamat1Agensi;
	
	@Column(name = "alamat2_agensi")
	private String alamat2Agensi;
	
	@Column(name = "alamat3_agensi")
	private String alamat3Agensi;
	
	@Column(name = "poskod_agensi")
	private String poskodAgensi;
	
	@ManyToOne
	@JoinColumn(name = "id_bandar_agensi")
	private Bandar bandarAgensi;
	
	@Column(name = "no_telefon")
	private String noTelefon;
	
	@Column(name = "no_faks")
	private String noFaks;
	
	@Column(name = "catatan_permohonan")
	private String catatanPermohonan;
	
	@Column(name = "tarikh_hantar_sub")
	@Temporal(TemporalType.DATE)
	private Date tarikhHantarSUB;
	
	@Column(name = "tarikh_terima_keputusan_sub")
	@Temporal(TemporalType.DATE)
	private Date tarikhTerimaKeputusanSUB;
	
	@Column(name = "flag_keputusan_sub")
	private String flagKeputusanSUB;
	
	@Column(name = "catatan_sub")
	private String catatanSUB;
	
	@Column(name = "tarikh_hantar_tksu")
	@Temporal(TemporalType.DATE)
	private Date tarikhHantarTKSU;
	
	@Column(name = "tarikh_terima_keputusan_tksu")
	@Temporal(TemporalType.DATE)
	private Date tarikhTerimaKeputusanTKSU;
	
	@Column(name = "flag_keputusan_tksu")
	private String flagKeputusanTKSU;
	
	@Column(name = "catatan_tksu")
	private String catatanTKSU;
	
	@Column(name = "tarikh_hantar_tksuk")
	@Temporal(TemporalType.DATE)
	private Date tarikhHantarTKSUK;
	
	@Column(name = "tarikh_terima_keputusan_tksuk")
	@Temporal(TemporalType.DATE)
	private Date tarikhTerimaKeputusanTKSUK;
	
	@Column(name = "flag_keputusan_tksuk")
	private String flagKeputusanTKSUK;
	
	@Column(name = "catatan_tksuk")
	private String catatanTKSUK;
	
	@Column(name = "tarikh_hantar_ksn")
	@Temporal(TemporalType.DATE)
	private Date tarikhHantarKSN;
	
	@Column(name = "tarikh_terima_keputusan_ksn")
	@Temporal(TemporalType.DATE)
	private Date tarikhTerimaKeputusanKSN;
	
	@Column(name = "flag_keputusan_ksn")
	private String flagKeputusanKSN;
	
	@Column(name = "catatan_ksn")
	private String catatanKSN;
	
	@ManyToOne
	@JoinColumn(name = "id_batal_permohonan")
	private Users batalOleh;

	@Column(name = "tarikh_batal")
	@Temporal(TemporalType.TIMESTAMP)
	private Date tarikhBatal;
	
	@Column(name = "catatan_batal")
	private String catatanBatal;
	
	@ManyToOne
	@JoinColumn(name = "id_masuk")
	private Users daftarOleh;

	@Column(name = "tarikh_masuk")
	@Temporal(TemporalType.TIMESTAMP)
	private Date tarikhDaftar;
	
	@ManyToOne
	@JoinColumn(name = "id_kemaskini")
	private Users kemaskiniOleh;

	@Column(name = "tarikh_kemaskini")
	@Temporal(TemporalType.TIMESTAMP)
	private Date tarikhKemaskini;
	
	public KuaPermohonanNGO() {
		setId(UID.getUID());
		setJenisPermohonan("1");
		setTarikhDaftar(new Date());
	}
	
	public String getKeteranganJenisPermohonan(){
		
		String idJenisPermohonan = this.jenisPermohonan;
		String jenisPermohonan = "";
		
		if("1".equals(idJenisPermohonan)){
			jenisPermohonan = "PERMOHONAN BARU";
		} else if("2".equals(idJenisPermohonan)){
			jenisPermohonan = "PERMOHONAN PERLANJUTAN";
		} else if("3".equals(idJenisPermohonan)){
			jenisPermohonan = "PERMOHONAN PENGURANGAN KADAR SEWA";
		} else if("4".equals(idJenisPermohonan)){
			jenisPermohonan = "PERMOHONAN PENGECUALIAN BAYARAN SEWA";
		} else if("5".equals(idJenisPermohonan)){
			jenisPermohonan = "PERMOHONAN PENAMATAN SEWA";
		}
		
		return jenisPermohonan;
	}
	
	public String getKeteranganSUB(){
		
		String flagKeputusanSUB = this.flagKeputusanSUB;
		String keputusan = "";
		
		if("L".equals(flagKeputusanSUB)){
			keputusan = "LULUS";
		} else if("T".equals(flagKeputusanSUB)){
			keputusan = "TOLAK";
		} else if("S".equals(flagKeputusanSUB)){
			keputusan = "SYOR TKSU";
		}
		
		return keputusan;
	}
	
	public String getKeteranganTKSU(){
		
		String flagKeputusanTKSU = this.flagKeputusanTKSU;
		String keputusan = "";
		
		if("L".equals(flagKeputusanTKSU)){
			keputusan = "SOKONG";
		} else if("T".equals(flagKeputusanTKSU)){
			keputusan = "TIDAK SOKONG";
		}
		
		return keputusan;
	}
	
	public String getKeteranganTKSUK(){
		
		String flagKeputusanTKSUK = this.flagKeputusanTKSUK;
		String keputusan = "";
		
		if("L".equals(flagKeputusanTKSUK)){
			keputusan = "SOKONG";
		} else if("T".equals(flagKeputusanTKSUK)){
			keputusan = "TIDAK SOKONG";
		}
		
		return keputusan;
	}
	
	public String getKeteranganKSN(){
		
		String flagKeputusanKSN = this.flagKeputusanKSN;
		String keputusan = "";
		
		if("L".equals(flagKeputusanKSN)){
			keputusan = "SOKONG";
		} else if("T".equals(flagKeputusanKSN)){
			keputusan = "TIDAK SOKONG";
		}
		
		return keputusan;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNoFail() {
		return noFail;
	}

	public void setNoFail(String noFail) {
		this.noFail = noFail;
	}

	public String getJenisPermohonan() {
		return jenisPermohonan;
	}

	public void setJenisPermohonan(String jenisPermohonan) {
		this.jenisPermohonan = jenisPermohonan;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getNoRujukanPermohonan() {
		return noRujukanPermohonan;
	}

	public void setNoRujukanPermohonan(String noRujukanPermohonan) {
		this.noRujukanPermohonan = noRujukanPermohonan;
	}

	public Date getTarikhPermohonan() {
		return tarikhPermohonan;
	}

	public void setTarikhPermohonan(Date tarikhPermohonan) {
		this.tarikhPermohonan = tarikhPermohonan;
	}

	public Date getTarikhTerimaPermohonan() {
		return tarikhTerimaPermohonan;
	}

	public void setTarikhTerimaPermohonan(Date tarikhTerimaPermohonan) {
		this.tarikhTerimaPermohonan = tarikhTerimaPermohonan;
	}

	public String getIdPemohon() {
		return idPemohon;
	}

	public void setIdPemohon(String idPemohon) {
		this.idPemohon = idPemohon;
	}

	public String getNamaPemohon() {
		return namaPemohon;
	}

	public void setNamaPemohon(String namaPemohon) {
		this.namaPemohon = namaPemohon;
	}

	public String getJawatanPemohon() {
		return jawatanPemohon;
	}

	public void setJawatanPemohon(String jawatanPemohon) {
		this.jawatanPemohon = jawatanPemohon;
	}

	public String getAlamat1Pemohon() {
		return alamat1Pemohon;
	}

	public void setAlamat1Pemohon(String alamat1Pemohon) {
		this.alamat1Pemohon = alamat1Pemohon;
	}

	public String getAlamat2Pemohon() {
		return alamat2Pemohon;
	}

	public void setAlamat2Pemohon(String alamat2Pemohon) {
		this.alamat2Pemohon = alamat2Pemohon;
	}

	public String getAlamat3Pemohon() {
		return alamat3Pemohon;
	}

	public void setAlamat3Pemohon(String alamat3Pemohon) {
		this.alamat3Pemohon = alamat3Pemohon;
	}

	public String getPoskodPemohon() {
		return poskodPemohon;
	}

	public void setPoskodPemohon(String poskodPemohon) {
		this.poskodPemohon = poskodPemohon;
	}

	public Bandar getBandarPemohon() {
		return bandarPemohon;
	}

	public void setBandarPemohon(Bandar bandarPemohon) {
		this.bandarPemohon = bandarPemohon;
	}

	public String getNoTelefonBimbit() {
		return noTelefonBimbit;
	}

	public void setNoTelefonBimbit(String noTelefonBimbit) {
		this.noTelefonBimbit = noTelefonBimbit;
	}

	public String getEmel() {
		return emel;
	}

	public void setEmel(String emel) {
		this.emel = emel;
	}

	public String getNamaAgensi() {
		return namaAgensi;
	}

	public void setNamaAgensi(String namaAgensi) {
		this.namaAgensi = namaAgensi;
	}

	public String getAlamat1Agensi() {
		return alamat1Agensi;
	}

	public void setAlamat1Agensi(String alamat1Agensi) {
		this.alamat1Agensi = alamat1Agensi;
	}

	public String getAlamat2Agensi() {
		return alamat2Agensi;
	}

	public void setAlamat2Agensi(String alamat2Agensi) {
		this.alamat2Agensi = alamat2Agensi;
	}

	public String getAlamat3Agensi() {
		return alamat3Agensi;
	}

	public void setAlamat3Agensi(String alamat3Agensi) {
		this.alamat3Agensi = alamat3Agensi;
	}

	public String getPoskodAgensi() {
		return poskodAgensi;
	}

	public void setPoskodAgensi(String poskodAgensi) {
		this.poskodAgensi = poskodAgensi;
	}

	public Bandar getBandarAgensi() {
		return bandarAgensi;
	}

	public void setBandarAgensi(Bandar bandarAgensi) {
		this.bandarAgensi = bandarAgensi;
	}

	public String getNoTelefon() {
		return noTelefon;
	}

	public void setNoTelefon(String noTelefon) {
		this.noTelefon = noTelefon;
	}

	public String getNoFaks() {
		return noFaks;
	}

	public void setNoFaks(String noFaks) {
		this.noFaks = noFaks;
	}

	public String getCatatanPermohonan() {
		return catatanPermohonan;
	}

	public void setCatatanPermohonan(String catatanPermohonan) {
		this.catatanPermohonan = catatanPermohonan;
	}

	public Date getTarikhHantarSUB() {
		return tarikhHantarSUB;
	}

	public void setTarikhHantarSUB(Date tarikhHantarSUB) {
		this.tarikhHantarSUB = tarikhHantarSUB;
	}

	public Date getTarikhTerimaKeputusanSUB() {
		return tarikhTerimaKeputusanSUB;
	}

	public void setTarikhTerimaKeputusanSUB(Date tarikhTerimaKeputusanSUB) {
		this.tarikhTerimaKeputusanSUB = tarikhTerimaKeputusanSUB;
	}

	public String getFlagKeputusanSUB() {
		return flagKeputusanSUB;
	}

	public void setFlagKeputusanSUB(String flagKeputusanSUB) {
		this.flagKeputusanSUB = flagKeputusanSUB;
	}

	public String getCatatanSUB() {
		return catatanSUB;
	}

	public void setCatatanSUB(String catatanSUB) {
		this.catatanSUB = catatanSUB;
	}

	public Date getTarikhHantarTKSU() {
		return tarikhHantarTKSU;
	}

	public void setTarikhHantarTKSU(Date tarikhHantarTKSU) {
		this.tarikhHantarTKSU = tarikhHantarTKSU;
	}

	public Date getTarikhTerimaKeputusanTKSU() {
		return tarikhTerimaKeputusanTKSU;
	}

	public void setTarikhTerimaKeputusanTKSU(Date tarikhTerimaKeputusanTKSU) {
		this.tarikhTerimaKeputusanTKSU = tarikhTerimaKeputusanTKSU;
	}

	public String getFlagKeputusanTKSU() {
		return flagKeputusanTKSU;
	}

	public void setFlagKeputusanTKSU(String flagKeputusanTKSU) {
		this.flagKeputusanTKSU = flagKeputusanTKSU;
	}

	public String getCatatanTKSU() {
		return catatanTKSU;
	}

	public void setCatatanTKSU(String catatanTKSU) {
		this.catatanTKSU = catatanTKSU;
	}

	public Date getTarikhHantarTKSUK() {
		return tarikhHantarTKSUK;
	}

	public void setTarikhHantarTKSUK(Date tarikhHantarTKSUK) {
		this.tarikhHantarTKSUK = tarikhHantarTKSUK;
	}

	public Date getTarikhTerimaKeputusanTKSUK() {
		return tarikhTerimaKeputusanTKSUK;
	}

	public void setTarikhTerimaKeputusanTKSUK(Date tarikhTerimaKeputusanTKSUK) {
		this.tarikhTerimaKeputusanTKSUK = tarikhTerimaKeputusanTKSUK;
	}

	public String getFlagKeputusanTKSUK() {
		return flagKeputusanTKSUK;
	}

	public void setFlagKeputusanTKSUK(String flagKeputusanTKSUK) {
		this.flagKeputusanTKSUK = flagKeputusanTKSUK;
	}

	public String getCatatanTKSUK() {
		return catatanTKSUK;
	}

	public void setCatatanTKSUK(String catatanTKSUK) {
		this.catatanTKSUK = catatanTKSUK;
	}

	public Date getTarikhHantarKSN() {
		return tarikhHantarKSN;
	}

	public void setTarikhHantarKSN(Date tarikhHantarKSN) {
		this.tarikhHantarKSN = tarikhHantarKSN;
	}

	public Date getTarikhTerimaKeputusanKSN() {
		return tarikhTerimaKeputusanKSN;
	}

	public void setTarikhTerimaKeputusanKSN(Date tarikhTerimaKeputusanKSN) {
		this.tarikhTerimaKeputusanKSN = tarikhTerimaKeputusanKSN;
	}

	public String getFlagKeputusanKSN() {
		return flagKeputusanKSN;
	}

	public void setFlagKeputusanKSN(String flagKeputusanKSN) {
		this.flagKeputusanKSN = flagKeputusanKSN;
	}

	public String getCatatanKSN() {
		return catatanKSN;
	}

	public void setCatatanKSN(String catatanKSN) {
		this.catatanKSN = catatanKSN;
	}

	public Users getBatalOleh() {
		return batalOleh;
	}

	public void setBatalOleh(Users batalOleh) {
		this.batalOleh = batalOleh;
	}

	public Date getTarikhBatal() {
		return tarikhBatal;
	}

	public void setTarikhBatal(Date tarikhBatal) {
		this.tarikhBatal = tarikhBatal;
	}

	public String getCatatanBatal() {
		return catatanBatal;
	}

	public void setCatatanBatal(String catatanBatal) {
		this.catatanBatal = catatanBatal;
	}

	public Users getDaftarOleh() {
		return daftarOleh;
	}

	public void setDaftarOleh(Users daftarOleh) {
		this.daftarOleh = daftarOleh;
	}

	public Date getTarikhDaftar() {
		return tarikhDaftar;
	}

	public void setTarikhDaftar(Date tarikhDaftar) {
		this.tarikhDaftar = tarikhDaftar;
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
