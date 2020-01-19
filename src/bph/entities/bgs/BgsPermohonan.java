package bph.entities.bgs;

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
import bph.entities.kod.Agensi;
import bph.entities.kod.Bandar;
import bph.entities.kod.Status;
import bph.entities.pembangunan.DevBangunan;

@Entity
@Table(name = "bgs_permohonan")
public class BgsPermohonan {
	
	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "no_permohonan")
	private String noPermohonan;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_permohonan")
	private Date tarikhPermohonan;
	
	@ManyToOne
	@JoinColumn(name = "id_pemohon")
	private Users pemohon;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_surat")
	private Date tarikhSurat;
	
	@ManyToOne
	@JoinColumn(name = "id_bangunan")
	private DevBangunan bangunan;
	
	@ManyToOne
	@JoinColumn(name = "id_agensi")
	private Agensi agensi;
	
	@Column(name = "jabatan")
	private String jabatan;
	
	@Column(name = "alamat1")
	private String alamat1;
	
	@Column(name = "alamat2")
	private String alamat2;
	
	@Column(name = "alamat3")
	private String alamat3;
	
	@Column(name = "poskod")
	private String poskod;
	
	@ManyToOne
	@JoinColumn(name = "id_bandar")
	private Bandar bandar;
	
	@Column(name = "no_telefon")
	private String noTelefon;
	
	@Column(name = "no_faks")
	private String noFaks;
	
	@Column(name = "nama_pegawai")
	private String namaPegawai;
	
	@Column(name = "emel_pegawai")
	private String emelPegawai;	
	
	@Column(name = "jenis_perkhidmatan")
	private String jenisPerkhidmatan;
	
	@Column(name = "status_ruang_pejabat")
	private String statusRuangPejabat;
	
	@Column(name = "jumlah_perjawatan_semasa")
	private String jumlahPerjawatanSemasa;	
	
	@Column(name = "jumlah_perjawatan_akan_datang")
	private String jumlahPerjawatanAkanDatang;	
	
	@Column(name = "jumlah_keluasan_semasa")
	private String jumlahKeluasanSemasa;	
	
	@Column(name = "jumlah_keluasan_akan_datang")
	private String jumlahKeluasanAkanDatang;
	
	@Column(name = "dokumen_pengerusi")
	private String dokumenPengerusi;
	
	@Column(name = "dokumen_pengurus")
	private String dokumenPengurus;
	
	@Column(name = "no_fail")
	private String noFail;
	
	@ManyToOne
	@JoinColumn(name = "id_pendaftar")
	private Users pendaftar;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_daftar_fail")
	private Date tarikhDaftarFail;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_keputusan")
	private Date tarikhKeputusan;
	
	@Column(name = "flag_keputusan")
	private String flagKeputusan;
	
	@Column(name = "catatan_keputusan")
	private String catatanKeputusan;
	
	@Column(name = "ruang_diluluskan")
	private String ruangDiluluskan;
	
	@Column(name = "luas_diluluskan")
	private String luasDiluluskan;
	
	@ManyToOne
	@JoinColumn(name = "id_status")
	private Status status;	
	
	@ManyToOne
	@JoinColumn(name = "id_pembatal")
	private Users batalOleh;	
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_batal")
	private Date tarikhBatal;
	
	@Column(name = "catatan_pembatalan")
	private String catatanPembatalan;

	public BgsPermohonan() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNoPermohonan() {
		return noPermohonan;
	}

	public void setNoPermohonan(String noPermohonan) {
		this.noPermohonan = noPermohonan;
	}

	public Date getTarikhPermohonan() {
		return tarikhPermohonan;
	}

	public void setTarikhPermohonan(Date tarikhPermohonan) {
		this.tarikhPermohonan = tarikhPermohonan;
	}

	public Users getPemohon() {
		return pemohon;
	}

	public void setPemohon(Users pemohon) {
		this.pemohon = pemohon;
	}

	public Date getTarikhSurat() {
		return tarikhSurat;
	}

	public void setTarikhSurat(Date tarikhSurat) {
		this.tarikhSurat = tarikhSurat;
	}

	public DevBangunan getBangunan() {
		return bangunan;
	}

	public void setBangunan(DevBangunan bangunan) {
		this.bangunan = bangunan;
	}

	public Agensi getAgensi() {
		return agensi;
	}

	public void setAgensi(Agensi agensi) {
		this.agensi = agensi;
	}

	public String getJabatan() {
		return jabatan;
	}

	public void setJabatan(String jabatan) {
		this.jabatan = jabatan;
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

	public Bandar getBandar() {
		return bandar;
	}

	public void setBandar(Bandar bandar) {
		this.bandar = bandar;
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

	public String getNamaPegawai() {
		return namaPegawai;
	}

	public void setNamaPegawai(String namaPegawai) {
		this.namaPegawai = namaPegawai;
	}

	public String getEmelPegawai() {
		return emelPegawai;
	}

	public void setEmelPegawai(String emelPegawai) {
		this.emelPegawai = emelPegawai;
	}

	public String getJenisPerkhidmatan() {
		return jenisPerkhidmatan;
	}

	public void setJenisPerkhidmatan(String jenisPerkhidmatan) {
		this.jenisPerkhidmatan = jenisPerkhidmatan;
	}

	public String getStatusRuangPejabat() {
		return statusRuangPejabat;
	}

	public void setStatusRuangPejabat(String statusRuangPejabat) {
		this.statusRuangPejabat = statusRuangPejabat;
	}

	public String getJumlahPerjawatanSemasa() {
		return jumlahPerjawatanSemasa;
	}

	public void setJumlahPerjawatanSemasa(String jumlahPerjawatanSemasa) {
		this.jumlahPerjawatanSemasa = jumlahPerjawatanSemasa;
	}

	public String getJumlahPerjawatanAkanDatang() {
		return jumlahPerjawatanAkanDatang;
	}

	public void setJumlahPerjawatanAkanDatang(String jumlahPerjawatanAkanDatang) {
		this.jumlahPerjawatanAkanDatang = jumlahPerjawatanAkanDatang;
	}

	public String getJumlahKeluasanSemasa() {
		return jumlahKeluasanSemasa;
	}

	public void setJumlahKeluasanSemasa(String jumlahKeluasanSemasa) {
		this.jumlahKeluasanSemasa = jumlahKeluasanSemasa;
	}

	public String getJumlahKeluasanAkanDatang() {
		return jumlahKeluasanAkanDatang;
	}

	public void setJumlahKeluasanAkanDatang(String jumlahKeluasanAkanDatang) {
		this.jumlahKeluasanAkanDatang = jumlahKeluasanAkanDatang;
	}

	public String getDokumenPengerusi() {
		return dokumenPengerusi;
	}

	public void setDokumenPengerusi(String dokumenPengerusi) {
		this.dokumenPengerusi = dokumenPengerusi;
	}

	public String getDokumenPengurus() {
		return dokumenPengurus;
	}

	public void setDokumenPengurus(String dokumenPengurus) {
		this.dokumenPengurus = dokumenPengurus;
	}

	public String getNoFail() {
		return noFail;
	}

	public void setNoFail(String noFail) {
		this.noFail = noFail;
	}

	public Users getPendaftar() {
		return pendaftar;
	}

	public void setPendaftar(Users pendaftar) {
		this.pendaftar = pendaftar;
	}

	public Date getTarikhDaftarFail() {
		return tarikhDaftarFail;
	}

	public void setTarikhDaftarFail(Date tarikhDaftarFail) {
		this.tarikhDaftarFail = tarikhDaftarFail;
	}

	public Date getTarikhKeputusan() {
		return tarikhKeputusan;
	}

	public void setTarikhKeputusan(Date tarikhKeputusan) {
		this.tarikhKeputusan = tarikhKeputusan;
	}

	public String getFlagKeputusan() {
		return flagKeputusan;
	}

	public void setFlagKeputusan(String flagKeputusan) {
		this.flagKeputusan = flagKeputusan;
	}

	public String getCatatanKeputusan() {
		return catatanKeputusan;
	}

	public void setCatatanKeputusan(String catatanKeputusan) {
		this.catatanKeputusan = catatanKeputusan;
	}

	public String getRuangDiluluskan() {
		return ruangDiluluskan;
	}

	public void setRuangDiluluskan(String ruangDiluluskan) {
		this.ruangDiluluskan = ruangDiluluskan;
	}

	public String getLuasDiluluskan() {
		return luasDiluluskan;
	}

	public void setLuasDiluluskan(String luasDiluluskan) {
		this.luasDiluluskan = luasDiluluskan;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
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

	public String getCatatanPembatalan() {
		return catatanPembatalan;
	}

	public void setCatatanPembatalan(String catatanPembatalan) {
		this.catatanPembatalan = catatanPembatalan;
	}
}
