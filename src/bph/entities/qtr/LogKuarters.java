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
import bph.entities.kod.Fasa;
import bph.entities.kod.JenisKediaman;
import bph.entities.kod.JenisKegunaanKuarters;
import bph.entities.kod.KelasKuarters;
import bph.entities.kod.LokasiDibenar;
import bph.entities.kod.LokasiKuarters;
import bph.entities.kod.StatusKuarters;

@Entity
@Table(name = "kua_kuarters_log")
public class LogKuarters {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_lokasi_kuarters")
	private LokasiKuarters lokasi;

	@ManyToOne
	@JoinColumn(name = "id_kelas_kuarters")
	private KelasKuarters kelas;

	@ManyToOne
	@JoinColumn(name = "id_jenis_kediaman")
	private JenisKediaman jenisKediaman;

	@ManyToOne
	@JoinColumn(name = "id_jenis_kegunaan_kuarters")
	private JenisKegunaanKuarters jenisKegunaanKuarters;

	@ManyToOne
	@JoinColumn(name = "id_status_kuarters")
	private StatusKuarters statusKuarters;

	@ManyToOne
	@JoinColumn(name = "id_lokasi_dibenar")
	private LokasiDibenar lokasiDibenar;

	@ManyToOne
	@JoinColumn(name = "id_bandar")
	private Bandar bandar;

	@Column(name = "no_rujukan")
	private String noRujukan;

	@Column(name = "lot_no")
	private String lotNo;

	@Column(name = "no_unit")
	private String noUnit;

	@Column(name = "blok")
	private String blok;

	@Column(name = "alamat_1")
	private String alamat1;

	@Column(name = "alamat_2")
	private String alamat2;

	@Column(name = "alamat_3")
	private String alamat3;

	@Column(name = "kawasan")
	private String kawasan;

	@Column(name = "poskod")
	private String poskod;

	@Column(name = "kadar_sewa")
	private Double sewa;

	@Column(name = "kadar_deposit")
	private Double deposit;

	@Column(name = "caj_air")
	private int cajAir;

	@Column(name = "kategori_penghuni")
	private String kategoriPenghuni;

	@Column(name = "kapasiti")
	private int kapasiti;

	@Column(name = "kapasiti_semasa")
	private int kapasitiSemasa;

	@Column(name = "kekosongan")
	private int kekosongan;

	@Column(name = "flag_agihan")
	private int flagAgihan;

	@Column(name = "flag_delete")
	private int flagDelete;

	@Column(name = "flag_aktif")
	private int flagAktif;

	@Column(name = "catatan")
	private String catatan;

	@Column(name = "turutan")
	private int turutan;

	public int getTurutan() {
		return turutan;
	}

	public void setTurutan(int turutan) {
		this.turutan = turutan;
	}

	public String getKemaskiniOleh() {
		return kemaskiniOleh;
	}

	public void setKemaskiniOleh(String kemaskiniOleh) {
		this.kemaskiniOleh = kemaskiniOleh;
	}

	@Column(name = "kemaskini_oleh")
	private String kemaskiniOleh;

	public int getFlagDelete() {
		return flagDelete;
	}

	public void setFlagDelete(int flagDelete) {
		this.flagDelete = flagDelete;
	}

	@ManyToOne
	@JoinColumn(name = "id_fasa")
	private Fasa fasa;

	@Column(name = "tarikh_serahan")
	@Temporal(TemporalType.DATE)
	private Date tarikhSerahan;

	@Column(name = "tarikh_kemaskini")
	@Temporal(TemporalType.TIMESTAMP)
	private Date tarikhKemaskini;

	public Date getTarikhKemaskini() {
		return tarikhKemaskini;
	}

	public void setTarikhKemaskini(Date tarikhKemaskini) {
		this.tarikhKemaskini = tarikhKemaskini;
	}

	@ManyToOne
	@JoinColumn(name = "id_masuk")
	private Users idMasuk;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_masuk")
	private Date tarikhMasuk;

	@ManyToOne
	@JoinColumn(name = "id_kemaskini")
	private Users idKemaskini;

	public LogKuarters() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public LokasiKuarters getLokasi() {
		return lokasi;
	}

	public void setLokasi(LokasiKuarters lokasi) {
		this.lokasi = lokasi;
	}

	public KelasKuarters getKelas() {
		return kelas;
	}

	public void setKelas(KelasKuarters kelas) {
		this.kelas = kelas;
	}

	public JenisKediaman getJenisKediaman() {
		return jenisKediaman;
	}

	public void setJenisKediaman(JenisKediaman jenisKediaman) {
		this.jenisKediaman = jenisKediaman;
	}

	public JenisKegunaanKuarters getJenisKegunaanKuarters() {
		return jenisKegunaanKuarters;
	}

	public void setJenisKegunaanKuarters(
			JenisKegunaanKuarters jenisKegunaanKuarters) {
		this.jenisKegunaanKuarters = jenisKegunaanKuarters;
	}

	public StatusKuarters getStatusKuarters() {
		return statusKuarters;
	}

	public void setStatusKuarters(StatusKuarters statusKuarters) {
		this.statusKuarters = statusKuarters;
	}

	public LokasiDibenar getLokasiDibenar() {
		return lokasiDibenar;
	}

	public void setLokasiDibenar(LokasiDibenar lokasiDibenar) {
		this.lokasiDibenar = lokasiDibenar;
	}

	public Bandar getBandar() {
		return bandar;
	}

	public void setBandar(Bandar bandar) {
		this.bandar = bandar;
	}

	public String getNoRujukan() {
		return noRujukan;
	}

	public void setNoRujukan(String noRujukan) {
		this.noRujukan = noRujukan;
	}

	public String getLotNo() {
		return lotNo;
	}

	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}

	public String getNoUnit() {
		return noUnit;
	}

	public void setNoUnit(String noUnit) {
		this.noUnit = noUnit;
	}

	public String getBlok() {
		return blok;
	}

	public void setBlok(String blok) {
		this.blok = blok;
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

	public String getKawasan() {
		return kawasan;
	}

	public void setKawasan(String kawasan) {
		this.kawasan = kawasan;
	}

	public String getPoskod() {
		return poskod;
	}

	public void setPoskod(String poskod) {
		this.poskod = poskod;
	}

	public Double getSewa() {
		return sewa;
	}

	public void setSewa(Double sewa) {
		this.sewa = sewa;
	}

	public Double getDeposit() {
		return deposit;
	}

	public void setDeposit(Double deposit) {
		this.deposit = deposit;
	}

	public int getCajAir() {
		return cajAir;
	}

	public void setCajAir(int cajAir) {
		this.cajAir = cajAir;
	}

	public String getDetailCajAir() {
		String detailCajAir = "TIADA";

		try {
			if (getCajAir() == 1) {
				detailCajAir = "ADA";
			}
		} catch (Exception ex) {

		}

		return detailCajAir;
	}

	public String getKategoriPenghuni() {
		return kategoriPenghuni;
	}

	public void setKategoriPenghuni(String kategoriPenghuni) {
		this.kategoriPenghuni = kategoriPenghuni;
	}

	public int getKapasiti() {
		return kapasiti;
	}

	public void setKapasiti(int kapasiti) {
		this.kapasiti = kapasiti;
	}

	public int getKapasitiSemasa() {
		return kapasitiSemasa;
	}

	public void setKapasitiSemasa(int kapasitiSemasa) {
		this.kapasitiSemasa = kapasitiSemasa;
	}

	public int getKekosongan() {
		return kekosongan;
	}

	public void setKekosongan(int kekosongan) {
		this.kekosongan = kekosongan;
	}

	public String getDetailKekosongan() {
		String detailKekosongan = "KOSONG";

		try {
			if (getCajAir() == 1) {
				detailKekosongan = "BERPENGHUNI";
			}
		} catch (Exception ex) {

		}

		return detailKekosongan;
	}

	public int getFlagAgihan() {
		return flagAgihan;
	}

	public void setFlagAgihan(int flagAgihan) {
		this.flagAgihan = flagAgihan;
	}

	public int getFlagAktif() {
		return flagAktif;
	}

	public void setFlagAktif(int flagAktif) {
		this.flagAktif = flagAktif;
	}

	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}

	public Fasa getFasa() {
		return fasa;
	}

	public void setFasa(Fasa fasa) {
		this.fasa = fasa;
	}

	public void setTarikhSerahan(Date tarikhSerahan) {
		this.tarikhSerahan = tarikhSerahan;
	}

	public Date getTarikhSerahan() {
		return tarikhSerahan;
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

}
