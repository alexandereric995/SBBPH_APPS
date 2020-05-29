package bph.entities.jrp;

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

@Entity
@Table(name = "jrp_ulasan_teknikal")
public class JrpUlasanTeknikal {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_permohonan")
	private JrpPermohonan jrpPermohonan;

	@ManyToOne
	@JoinColumn(name = "id_agensi")
	private Agensi agensi;

	@Column(name = "cawangan")
	private String cawangan;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_lawatan")
	private Date tarikhLawatan;

	@Column(name = "flag_keputusan")
	private String flagKeputusan;

	@Column(name = "nilaian_mp")
	private Double nilaianMp;

	@Column(name = "luas_mp")
	private String luasMp;

	@Column(name = "nilaian_kp")
	private Double nilaianKp;

	@Column(name = "luas_kp")
	private String luasKp;

	@Column(name = "flag_gst")
	private String flagGst;

	@Column(name = "nilaian_gst_mp")
	private Double nilaianGstMp;

	@Column(name = "nilaian_gst_kp")
	private Double nilaianGstKp;

	@Column(name = "nama_pegawai")
	private String namaPegawai;

	@Column(name = "jawatan_pegawai")
	private String jawatanPegawai;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_ulasan")
	private Date tarikhUlasan;

	@Column(name = "ulasan")
	private String ulasan;

	@Column(name = "syor")
	private String syor;

	@Column(name = "nama_pegawai_periksa")
	private String namaPegawaiPeriksa;

	@Column(name = "syarat_sewa_1")
	private String syaratSewa1;

	@Column(name = "syarat_sewa_2")
	private String syaratSewa2;

	@Column(name = "syarat_sewa_3")
	private String syaratSewa3;

	@Column(name = "syarat_sewa_4")
	private String syaratSewa4;

	@Column(name = "syarat_sewa_5")
	private String syaratSewa5;

	@Column(name = "syarat_sewa_6")
	private String syaratSewa6;

	@Column(name = "syarat_sewa_7")
	private String syaratSewa7;

	@Column(name = "syarat_sewa_8")
	private String syaratSewa8;

	@Column(name = "syarat_sewa_9")
	private String syaratSewa9;

	@Column(name = "syarat_sewa_10")
	private String syaratSewa10;

	@Column(name = "syarat_sewa_11")
	private String syaratSewa11;

	@Column(name = "perakuan_sewa_bulanan")
	private Double perakuanSewaBulanan;

	@Column(name = "tempoh_sewa")
	private Integer tempohSewa;

	@Column(name = "flag_hantar")
	private String flagHantar;

	@Column(name = "nilaian_jpph_mp")
	private Double nilaianJpphMp;

	@Column(name = "luas_jpph_mp")
	private String luasJpphMp;

	@Column(name = "nilaian_jpph_kp")
	private Double nilaianJpphKp;

	@Column(name = "luas_jpph_kp")
	private String luasJpphKp;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_hantar_ulasan")
	private Date tarikhHantarUlasan;

	@Column(name = "flag_aktif")
	private String flagAktif;

	@Column(name = "surat_file_name")
	private String suratFileName;

	@Column(name = "surat_thumb_file")
	private String suratThumbFile;

	@Column(name = "flag_nilaian")
	private String flagNilaian;

	@Column(name = "flag_jenis_sewa")
	private String flagJenisSewa;

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

	public JrpUlasanTeknikal() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public JrpPermohonan getJrpPermohonan() {
		return jrpPermohonan;
	}

	public void setJrpPermohonan(JrpPermohonan jrpPermohonan) {
		this.jrpPermohonan = jrpPermohonan;
	}

	public Agensi getAgensi() {
		return agensi;
	}

	public void setAgensi(Agensi agensi) {
		this.agensi = agensi;
	}

	public String getCawangan() {
		return cawangan;
	}

	public void setCawangan(String cawangan) {
		this.cawangan = cawangan;
	}

	public Date getTarikhLawatan() {
		return tarikhLawatan;
	}

	public void setTarikhLawatan(Date tarikhLawatan) {
		this.tarikhLawatan = tarikhLawatan;
	}

	public String getFlagKeputusan() {
		return flagKeputusan;
	}

	public void setFlagKeputusan(String flagKeputusan) {
		this.flagKeputusan = flagKeputusan;
	}

	public String getLuasMp() {
		return luasMp;
	}

	public void setLuasMp(String luasMp) {
		this.luasMp = luasMp;
	}

	public String getLuasKp() {
		return luasKp;
	}

	public void setLuasKp(String luasKp) {
		this.luasKp = luasKp;
	}

	public String getNamaPegawai() {
		return namaPegawai;
	}

	public void setNamaPegawai(String namaPegawai) {
		this.namaPegawai = namaPegawai;
	}

	public Date getTarikhUlasan() {
		return tarikhUlasan;
	}

	public void setTarikhUlasan(Date tarikhUlasan) {
		this.tarikhUlasan = tarikhUlasan;
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

	public String getNamaPegawaiPeriksa() {
		return namaPegawaiPeriksa;
	}

	public void setNamaPegawaiPeriksa(String namaPegawaiPeriksa) {
		this.namaPegawaiPeriksa = namaPegawaiPeriksa;
	}

	public Double getNilaianMp() {
		return nilaianMp;
	}

	public void setNilaianMp(Double nilaianMp) {
		this.nilaianMp = nilaianMp;
	}

	public Double getNilaianKp() {
		return nilaianKp;
	}

	public void setNilaianKp(Double nilaianKp) {
		this.nilaianKp = nilaianKp;
	}

	public String getSyaratSewa1() {
		return syaratSewa1;
	}

	public void setSyaratSewa1(String syaratSewa1) {
		this.syaratSewa1 = syaratSewa1;
	}

	public String getSyaratSewa2() {
		return syaratSewa2;
	}

	public void setSyaratSewa2(String syaratSewa2) {
		this.syaratSewa2 = syaratSewa2;
	}

	public String getSyaratSewa3() {
		return syaratSewa3;
	}

	public void setSyaratSewa3(String syaratSewa3) {
		this.syaratSewa3 = syaratSewa3;
	}

	public Double getPerakuanSewaBulanan() {
		return perakuanSewaBulanan;
	}

	public void setPerakuanSewaBulanan(Double perakuanSewaBulanan) {
		this.perakuanSewaBulanan = perakuanSewaBulanan;
	}

	public Integer getTempohSewa() {
		return tempohSewa;
	}

	public void setTempohSewa(Integer tempohSewa) {
		this.tempohSewa = tempohSewa;
	}

	public String getFlagHantar() {
		return flagHantar;
	}

	public void setFlagHantar(String flagHantar) {
		this.flagHantar = flagHantar;
	}

	public String getJawatanPegawai() {
		return jawatanPegawai;
	}

	public void setJawatanPegawai(String jawatanPegawai) {
		this.jawatanPegawai = jawatanPegawai;
	}

	public String getFlagGst() {
		return flagGst;
	}

	public void setFlagGst(String flagGst) {
		this.flagGst = flagGst;
	}

	public Double getNilaianGstMp() {
		return nilaianGstMp;
	}

	public void setNilaianGstMp(Double nilaianGstMp) {
		this.nilaianGstMp = nilaianGstMp;
	}

	public Double getNilaianGstKp() {
		return nilaianGstKp;
	}

	public void setNilaianGstKp(Double nilaianGstKp) {
		this.nilaianGstKp = nilaianGstKp;
	}

	public Double getNilaianJpphMp() {
		return nilaianJpphMp;
	}

	public void setNilaianJpphMp(Double nilaianJpphMp) {
		this.nilaianJpphMp = nilaianJpphMp;
	}

	public String getLuasJpphMp() {
		return luasJpphMp;
	}

	public void setLuasJpphMp(String luasJpphMp) {
		this.luasJpphMp = luasJpphMp;
	}

	public Double getNilaianJpphKp() {
		return nilaianJpphKp;
	}

	public void setNilaianJpphKp(Double nilaianJpphKp) {
		this.nilaianJpphKp = nilaianJpphKp;
	}

	public String getLuasJpphKp() {
		return luasJpphKp;
	}

	public void setLuasJpphKp(String luasJpphKp) {
		this.luasJpphKp = luasJpphKp;
	}

	public Date getTarikhHantarUlasan() {
		return tarikhHantarUlasan;
	}

	public void setTarikhHantarUlasan(Date tarikhHantarUlasan) {
		this.tarikhHantarUlasan = tarikhHantarUlasan;
	}

	public String getFlagAktif() {
		return flagAktif;
	}

	public void setFlagAktif(String flagAktif) {
		this.flagAktif = flagAktif;
	}

	public String getSuratFileName() {
		return suratFileName;
	}

	public void setSuratFileName(String suratFileName) {
		this.suratFileName = suratFileName;
	}

	public String getSuratThumbFile() {
		return suratThumbFile;
	}

	public void setSuratThumbFile(String suratThumbFile) {
		this.suratThumbFile = suratThumbFile;
	}

	public String getFlagNilaian() {
		return flagNilaian;
	}

	public void setFlagNilaian(String flagNilaian) {
		this.flagNilaian = flagNilaian;
	}

	public String getSyaratSewa4() {
		return syaratSewa4;
	}

	public void setSyaratSewa4(String syaratSewa4) {
		this.syaratSewa4 = syaratSewa4;
	}

	public String getSyaratSewa5() {
		return syaratSewa5;
	}

	public void setSyaratSewa5(String syaratSewa5) {
		this.syaratSewa5 = syaratSewa5;
	}

	public String getSyaratSewa6() {
		return syaratSewa6;
	}

	public void setSyaratSewa6(String syaratSewa6) {
		this.syaratSewa6 = syaratSewa6;
	}

	public String getSyaratSewa7() {
		return syaratSewa7;
	}

	public void setSyaratSewa7(String syaratSewa7) {
		this.syaratSewa7 = syaratSewa7;
	}

	public String getSyaratSewa8() {
		return syaratSewa8;
	}

	public void setSyaratSewa8(String syaratSewa8) {
		this.syaratSewa8 = syaratSewa8;
	}

	public String getSyaratSewa9() {
		return syaratSewa9;
	}

	public void setSyaratSewa9(String syaratSewa9) {
		this.syaratSewa9 = syaratSewa9;
	}

	public String getSyaratSewa10() {
		return syaratSewa10;
	}

	public void setSyaratSewa10(String syaratSewa10) {
		this.syaratSewa10 = syaratSewa10;
	}

	public String getSyaratSewa11() {
		return syaratSewa11;
	}

	public void setSyaratSewa11(String syaratSewa11) {
		this.syaratSewa11 = syaratSewa11;
	}

	public String getFlagJenisSewa() {
		return flagJenisSewa;
	}

	public void setFlagJenisSewa(String flagJenisSewa) {
		this.flagJenisSewa = flagJenisSewa;
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
