package bph.entities.utk;

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
import bph.entities.kod.BlokUtk;
import bph.entities.kod.Fasa;
import bph.entities.kod.JenisKenderaanUtk;
import bph.entities.kod.JenisKuartersUtk;
import bph.entities.kod.JenisOperasiUtk;
import bph.entities.kod.JenisPelanggaranSyaratUtk;
import bph.entities.kod.KawasanUtk;
import bph.entities.kod.ZonUtk;

@Entity
@Table(name = "utk_operasi")
public class UtkOperasi {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "no_fail")
	private String noFail;

	@Column(name = "no_rujukan_operasi")
	private String noRujukanOperasi;

	@ManyToOne
	@JoinColumn(name = "id_zon")
	private ZonUtk zon;

	@ManyToOne
	@JoinColumn(name = "id_jenis_kuarters")
	private JenisKuartersUtk jenisKuarters;

	@ManyToOne
	@JoinColumn(name = "id_kawasan")
	private KawasanUtk kawasan;

	@ManyToOne
	@JoinColumn(name = "id_jenis_operasi")
	private JenisOperasiUtk jenisOperasi;

	@Column(name = "id_jenis_operasi", insertable = false, updatable = false)
	private String idJenisOperasi;

	@ManyToOne
	@JoinColumn(name = "id_jenis_pelanggaran_syarat")
	private JenisPelanggaranSyaratUtk jenisPelanggaranSyarat;

	@Column(name = "id_jenis_pelanggaran_syarat", insertable = false, updatable = false)
	private String idJenisPelanggaranSyarat;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_operasi")
	private Date tarikhOperasi;

	@Column(name = "masa_mula")
	private String masaMula;

	@Column(name = "masa_tamat")
	private String masaTamat;

	@Column(name = "tajuk")
	private String tajuk;

	@Column(name = "catatan")
	private String catatan;

	@Column(name = "flag_operasi")
	private String flagOperasi;

	@ManyToOne
	@JoinColumn(name = "fasa")
	private Fasa fasa;

	@ManyToOne
	@JoinColumn(name = "blok")
	private BlokUtk blok;

	@ManyToOne
	@JoinColumn(name = "id_jenis_kenderaan")
	private JenisKenderaanUtk jenisKenderaan;

	@Column(name = "no_plat_kenderaan")
	private String noPlatKenderaan;

	@Column(name = "model_kenderaan")
	private String modelKenderaan;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_data")
	private Date tarikhDataMasuk;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_kemaskini_data")
	private Date tarikhKemaskiniData;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_aduan")
	private Date tarikhAduan;

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

	public UtkOperasi() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ZonUtk getZon() {
		return zon;
	}

	public void setZon(ZonUtk zon) {
		this.zon = zon;
	}

	public JenisKuartersUtk getJenisKuarters() {
		return jenisKuarters;
	}

	public void setJenisKuarters(JenisKuartersUtk jenisKuarters) {
		this.jenisKuarters = jenisKuarters;
	}

	public KawasanUtk getKawasan() {
		return kawasan;
	}

	public void setKawasan(KawasanUtk kawasan) {
		this.kawasan = kawasan;
	}

	public JenisOperasiUtk getJenisOperasi() {
		return jenisOperasi;
	}

	public void setJenisOperasi(JenisOperasiUtk jenisOperasi) {
		this.jenisOperasi = jenisOperasi;
	}

	public Date getTarikhOperasi() {
		return tarikhOperasi;
	}

	public void setTarikhOperasi(Date tarikhOperasi) {
		this.tarikhOperasi = tarikhOperasi;
	}

	public String getMasaMula() {
		return masaMula;
	}

	public void setMasaMula(String masaMula) {
		this.masaMula = masaMula;
	}

	public String getMasaTamat() {
		return masaTamat;
	}

	public void setMasaTamat(String masaTamat) {
		this.masaTamat = masaTamat;
	}

	public String getTajuk() {
		return tajuk;
	}

	public void setTajuk(String tajuk) {
		this.tajuk = tajuk;
	}

	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}

	public String getFlagOperasi() {
		return flagOperasi;
	}

	public void setFlagOperasi(String flagOperasi) {
		this.flagOperasi = flagOperasi;
	}

	public String getNoFail() {
		return noFail;
	}

	public void setNoFail(String noFail) {
		this.noFail = noFail;
	}

	public JenisPelanggaranSyaratUtk getJenisPelanggaranSyarat() {
		return jenisPelanggaranSyarat;
	}

	public void setJenisPelanggaranSyarat(
			JenisPelanggaranSyaratUtk jenisPelanggaranSyarat) {
		this.jenisPelanggaranSyarat = jenisPelanggaranSyarat;
	}

	public String getIdJenisOperasi() {
		return idJenisOperasi;
	}

	public void setIdJenisOperasi(String idJenisOperasi) {
		this.idJenisOperasi = idJenisOperasi;
	}

	public String getIdJenisPelanggaranSyarat() {
		return idJenisPelanggaranSyarat;
	}

	public void setIdJenisPelanggaranSyarat(String idJenisPelanggaranSyarat) {
		this.idJenisPelanggaranSyarat = idJenisPelanggaranSyarat;
	}

	public Fasa getFasa() {
		return fasa;
	}

	public void setFasa(Fasa fasa) {
		this.fasa = fasa;
	}

	public BlokUtk getBlok() {
		return blok;
	}

	public void setBlok(BlokUtk blok) {
		this.blok = blok;
	}

	public JenisKenderaanUtk getJenisKenderaan() {
		return jenisKenderaan;
	}

	public void setJenisKenderaan(JenisKenderaanUtk jenisKenderaan) {
		this.jenisKenderaan = jenisKenderaan;
	}

	public String getNoPlatKenderaan() {
		return noPlatKenderaan;
	}

	public void setNoPlatKenderaan(String noPlatKenderaan) {
		this.noPlatKenderaan = noPlatKenderaan;
	}

	public String getModelKenderaan() {
		return modelKenderaan;
	}

	public void setModelKenderaan(String modelKenderaan) {
		this.modelKenderaan = modelKenderaan;
	}

	public Date getTarikhDataMasuk() {
		return tarikhDataMasuk;
	}

	public void setTarikhDataMasuk(Date tarikhDataMasuk) {
		this.tarikhDataMasuk = tarikhDataMasuk;
	}

	public Date getTarikhKemaskiniData() {
		return tarikhKemaskiniData;
	}

	public void setTarikhKemaskiniData(Date tarikhKemaskiniData) {
		this.tarikhKemaskiniData = tarikhKemaskiniData;
	}

	public Date getTarikhAduan() {
		return tarikhAduan;
	}

	public void setTarikhAduan(Date tarikhAduan) {
		this.tarikhAduan = tarikhAduan;
	}

	public String getNoRujukanOperasi() {
		return noRujukanOperasi;
	}

	public void setNoRujukanOperasi(String noRujukanOperasi) {
		this.noRujukanOperasi = noRujukanOperasi;
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
