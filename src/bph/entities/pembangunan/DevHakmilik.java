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
import bph.entities.kod.Agensi;
import bph.entities.kod.Daerah;
import bph.entities.kod.JenisHakmilik;
import bph.entities.kod.KategoriTanah;
import bph.entities.kod.Kementerian;
import bph.entities.kod.Lot;
import bph.entities.kod.Luas;
import bph.entities.kod.Mukim;
import bph.entities.kod.Negeri;
import bph.entities.kod.SubKategoriTanah;

@Entity
@Table(name = "dev_hakmilik")
public class DevHakmilik {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "pegangan_hakmilik")
	private String peganganHakmilik;

	@Column(name = "hakmilik_asal")
	private String hakmilikAsal;

	@Column(name = "hakmilik_berikut")
	private String hakmilikBerikut;

	@ManyToOne
	@JoinColumn(name = "id_jenishakmilik")
	private JenisHakmilik jenisHakmilik;

	@Column(name = "no_hakmilik")
	private String noHakmilik;

	@ManyToOne
	@JoinColumn(name = "id_lot")
	private Lot lot;

	@Column(name = "no_lot")
	private String noLot;

	@ManyToOne
	@JoinColumn(name = "id_luas")
	private Luas jenisLuas;

	@Column(name = "luas")
	private String luas;

	@ManyToOne
	@JoinColumn(name = "id_luas_bersamaan")
	private Luas jenisLuasBersamaan;

	@Column(name = "luas_bersamaan")
	private String luasBersamaan;

	@ManyToOne
	@JoinColumn(name = "id_negeri")
	private Negeri negeri;

	@ManyToOne
	@JoinColumn(name = "id_daerah")
	private Daerah daerah;

	@ManyToOne
	@JoinColumn(name = "id_mukim")
	private Mukim mukim;

	@Column(name = "lokasi")
	private String lokasi;

	@ManyToOne
	@JoinColumn(name = "id_kementerian")
	private Kementerian kementerian;

	@ManyToOne
	@JoinColumn(name = "id_agensi")
	private Agensi agensi;

	@ManyToOne
	@JoinColumn(name = "id_kategori")
	private KategoriTanah kategoriTanah;

	@ManyToOne
	@JoinColumn(name = "id_subkategori")
	private SubKategoriTanah subKategoriTanah;

	@Column(name = "no_fail")
	private String noFail;

	@Column(name = "no_syit")
	private String noSyit;

	@Column(name = "no_pelan")
	private String noPelan;

	@Column(name = "no_pu")
	private String noPu;

	@Column(name = "status_daftar")
	private String statusDaftar;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_daftar")
	private Date tarikhDaftar;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_terima_hakmilik")
	private Date tarikhTerimaHakmilik;

	@Column(name = "taraf_hakmilik")
	private String tarafHakmilik;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_luput")
	private Date tarikhLuput;

	@Column(name = "no_warta")
	private String noWarta;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_warta")
	private Date tarikhWarta;

	@Column(name = "syarat")
	private String syarat;

	@Column(name = "sekatan")
	private String sekatan;

	@Column(name = "kegunaan_tanah")
	private String kegunaanTanah;

	@Column(name = "cukai")
	private Double cukai;

	@Column(name = "cukai_terkini")
	private Double cukaiTerkini;

	@Column(name = "catatan")
	private String catatan;

	@Column(name = "pelan_file_name")
	private String pelanFileName;

	@Column(name = "pelan_thumb_file")
	private String pelanThumbFile;

	@Column(name = "flag_aktif")
	private String flagAktif;

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

	public DevHakmilik() {
		setId(UID.getUID());
		setFlagAktif("Y");
		setTarikhMasuk(new Date());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPeganganHakmilik() {
		return peganganHakmilik;
	}

	public void setPeganganHakmilik(String peganganHakmilik) {
		this.peganganHakmilik = peganganHakmilik;
	}

	public String getHakmilikAsal() {
		return hakmilikAsal;
	}

	public void setHakmilikAsal(String hakmilikAsal) {
		this.hakmilikAsal = hakmilikAsal;
	}

	public String getHakmilikBerikut() {
		return hakmilikBerikut;
	}

	public void setHakmilikBerikut(String hakmilikBerikut) {
		this.hakmilikBerikut = hakmilikBerikut;
	}

	public JenisHakmilik getJenisHakmilik() {
		return jenisHakmilik;
	}

	public void setJenisHakmilik(JenisHakmilik jenisHakmilik) {
		this.jenisHakmilik = jenisHakmilik;
	}

	public String getNoHakmilik() {
		return noHakmilik;
	}

	public void setNoHakmilik(String noHakmilik) {
		this.noHakmilik = noHakmilik;
	}

	public Lot getLot() {
		return lot;
	}

	public void setLot(Lot lot) {
		this.lot = lot;
	}

	public String getNoLot() {
		return noLot;
	}

	public void setNoLot(String noLot) {
		this.noLot = noLot;
	}

	public Luas getJenisLuas() {
		return jenisLuas;
	}

	public void setJenisLuas(Luas jenisLuas) {
		this.jenisLuas = jenisLuas;
	}

	public String getLuas() {
		return luas;
	}

	public void setLuas(String luas) {
		this.luas = luas;
	}

	public Luas getJenisLuasBersamaan() {
		return jenisLuasBersamaan;
	}

	public void setJenisLuasBersamaan(Luas jenisLuasBersamaan) {
		this.jenisLuasBersamaan = jenisLuasBersamaan;
	}

	public String getLuasBersamaan() {
		return luasBersamaan;
	}

	public void setLuasBersamaan(String luasBersamaan) {
		this.luasBersamaan = luasBersamaan;
	}

	public Negeri getNegeri() {
		return negeri;
	}

	public void setNegeri(Negeri negeri) {
		this.negeri = negeri;
	}

	public Daerah getDaerah() {
		return daerah;
	}

	public void setDaerah(Daerah daerah) {
		this.daerah = daerah;
	}

	public Mukim getMukim() {
		return mukim;
	}

	public void setMukim(Mukim mukim) {
		this.mukim = mukim;
	}

	public String getLokasi() {
		return lokasi;
	}

	public void setLokasi(String lokasi) {
		this.lokasi = lokasi;
	}

	public Kementerian getKementerian() {
		return kementerian;
	}

	public void setKementerian(Kementerian kementerian) {
		this.kementerian = kementerian;
	}

	public Agensi getAgensi() {
		return agensi;
	}

	public void setAgensi(Agensi agensi) {
		this.agensi = agensi;
	}

	public KategoriTanah getKategoriTanah() {
		return kategoriTanah;
	}

	public void setKategoriTanah(KategoriTanah kategoriTanah) {
		this.kategoriTanah = kategoriTanah;
	}

	public SubKategoriTanah getSubKategoriTanah() {
		return subKategoriTanah;
	}

	public void setSubKategoriTanah(SubKategoriTanah subKategoriTanah) {
		this.subKategoriTanah = subKategoriTanah;
	}

	public String getNoFail() {
		return noFail;
	}

	public void setNoFail(String noFail) {
		this.noFail = noFail;
	}

	public String getNoSyit() {
		return noSyit;
	}

	public void setNoSyit(String noSyit) {
		this.noSyit = noSyit;
	}

	public String getNoPelan() {
		return noPelan;
	}

	public void setNoPelan(String noPelan) {
		this.noPelan = noPelan;
	}

	public String getNoPu() {
		return noPu;
	}

	public void setNoPu(String noPu) {
		this.noPu = noPu;
	}

	public String getStatusDaftar() {
		return statusDaftar;
	}

	public void setStatusDaftar(String statusDaftar) {
		this.statusDaftar = statusDaftar;
	}

	public Date getTarikhDaftar() {
		return tarikhDaftar;
	}

	public void setTarikhDaftar(Date tarikhDaftar) {
		this.tarikhDaftar = tarikhDaftar;
	}

	public Date getTarikhTerimaHakmilik() {
		return tarikhTerimaHakmilik;
	}

	public void setTarikhTerimaHakmilik(Date tarikhTerimaHakmilik) {
		this.tarikhTerimaHakmilik = tarikhTerimaHakmilik;
	}

	public String getTarafHakmilik() {
		return tarafHakmilik;
	}

	public void setTarafHakmilik(String tarafHakmilik) {
		this.tarafHakmilik = tarafHakmilik;
	}

	public Date getTarikhLuput() {
		return tarikhLuput;
	}

	public void setTarikhLuput(Date tarikhLuput) {
		this.tarikhLuput = tarikhLuput;
	}

	public String getNoWarta() {
		return noWarta;
	}

	public void setNoWarta(String noWarta) {
		this.noWarta = noWarta;
	}

	public Date getTarikhWarta() {
		return tarikhWarta;
	}

	public void setTarikhWarta(Date tarikhWarta) {
		this.tarikhWarta = tarikhWarta;
	}

	public String getSyarat() {
		return syarat;
	}

	public void setSyarat(String syarat) {
		this.syarat = syarat;
	}

	public String getSekatan() {
		return sekatan;
	}

	public void setSekatan(String sekatan) {
		this.sekatan = sekatan;
	}

	public String getKegunaanTanah() {
		return kegunaanTanah;
	}

	public void setKegunaanTanah(String kegunaanTanah) {
		this.kegunaanTanah = kegunaanTanah;
	}

	public Double getCukai() {
		return cukai;
	}

	public void setCukai(Double cukai) {
		this.cukai = cukai;
	}

	public Double getCukaiTerkini() {
		return cukaiTerkini;
	}

	public void setCukaiTerkini(Double cukaiTerkini) {
		this.cukaiTerkini = cukaiTerkini;
	}

	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}

	public String getPelanFileName() {
		return pelanFileName;
	}

	public void setPelanFileName(String pelanFileName) {
		this.pelanFileName = pelanFileName;
	}

	public String getPelanThumbFile() {
		return pelanThumbFile;
	}

	public void setPelanThumbFile(String pelanThumbFile) {
		this.pelanThumbFile = pelanThumbFile;
	}

	public String getFlagAktif() {
		return flagAktif;
	}

	public void setFlagAktif(String flagAktif) {
		this.flagAktif = flagAktif;
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
