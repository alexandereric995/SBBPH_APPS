package bph.entities.qtr;

import java.util.Calendar;
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
import bph.entities.kod.SebabBertukar;

@Entity
@Table(name = "kua_temujanji")
public class KuaTemujanji {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_agihan")
	private KuaAgihan agihan;

	@ManyToOne
	@JoinColumn(name = "id_penghuni")
	private KuaPenghuni penghuni;

	@ManyToOne
	@JoinColumn(name = "id_kuarters")
	private KuaKuarters kuarters;

	@ManyToOne
	@JoinColumn(name = "id_pemohon")
	private Users pemohon;

	@Column(name = "tarikh_mohon_temujanji")
	@Temporal(TemporalType.DATE)
	private Date tarikhMohonTemujanji;

	@Column(name = "tarikh_temujanji")
	@Temporal(TemporalType.DATE)
	private Date tarikhTemujanji;

	@Column(name = "tarikh_mula_temujanji")
	@Temporal(TemporalType.TIMESTAMP)
	private Date tarikhMulaTemujanji;

	@Column(name = "tarikh_akhir_temujanji")
	@Temporal(TemporalType.TIMESTAMP)
	private Date tarikhAkhirTemujanji;

	@ManyToOne
	@JoinColumn(name = "id_sebab_bertukar")
	private SebabBertukar sebabBertukar;

	@Column(name = "keterangan")
	private String keterangan;

	@Column(name = "alamat_1")
	private String alamat1;

	@Column(name = "alamat_2")
	private String alamat2;

	@Column(name = "alamat_3")
	private String alamat3;

	@Column(name = "poskod")
	private String poskod;

	@ManyToOne
	@JoinColumn(name = "id_bandar")
	private Bandar bandar;

	@Column(name = "emel")
	private String emel;

	@Column(name = "no_telefon_bimbit")
	private String noTelefonBimbit;

	@ManyToOne
	@JoinColumn(name = "id_agensi")
	private Agensi agensi;

	@Column(name = "bahagian")
	private String bahagian;

	@Column(name = "alamat_pejabat_1")
	private String alamatPejabat1;

	@Column(name = "alamat_pejabat_2")
	private String alamatPejabat2;

	@Column(name = "alamat_pejabat_3")
	private String alamatPejabat3;

	@Column(name = "poskod_pejabat")
	private String poskodPejabat;

	@ManyToOne
	@JoinColumn(name = "id_bandar_pejabat")
	private Bandar bandarPejabat;

	@Column(name = "no_telefon_pejabat")
	private String noTelefonPejabat;

	@Column(name = "flag_internal")
	private int flagInternal;

	@Column(name = "catatan")
	private String catatan;

	@ManyToOne
	@JoinColumn(name = "petugas")
	private Users petugas;

	@Column(name = "kehadiran")
	private int kehadiran;

	@Column(name = "tarikh_terima_kunci")
	@Temporal(TemporalType.DATE)
	private Date tarikhTerimaKunci;

	@Column(name = "tarikh_keluar_kuarters")
	@Temporal(TemporalType.DATE)
	private Date tarikhKeluarKuarters;

	@Column(name = "tarikh_serah_kunci")
	@Temporal(TemporalType.DATE)
	private Date tarikhSerahKunci;

	@Column(name = "memo_daripada")
	private String memoDaripada;

	@Column(name = "memo_kepada")
	private String memoKepada;

	@Column(name = "memo_perkara")
	private String memoPerkara;

	@Column(name = "memo_rujukan")
	private String memoRujukan;

	@Column(name = "memo_sk")
	private String memoSk;

	@Column(name = "memo_tarikh")
	@Temporal(TemporalType.DATE)
	private Date memoTarikh;

	@Column(name = "status_temujanji")
	private String statusTemujanji;

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

	public KuaTemujanji() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());
		setKehadiran(0);
		setStatusTemujanji("1");

		setMemoDaripada("Unit Kuarters");
		setMemoKepada("PJ(E)S3");
		setMemoPerkara("Sijil Akuan Keluar Rumah");
		setMemoRujukan("BPH.P/46911(   )");
		setMemoSk("Fail");
	}

	public String getDetailFlagInternal() {
		String detailFlagInternal = "";
		if (getFlagInternal() == 1) {
			detailFlagInternal = "INTERNAL";
		} else {
			detailFlagInternal = "PUBLIC";
		}
		return detailFlagInternal;
	}

	public String getKeteranganStatusTemujanji() {
		String keterangan = "";
		if (this.getStatusTemujanji() != null) {
			if (this.getStatusTemujanji().equals("0")) {
				keterangan = "SELESAI";
			} else if (this.getStatusTemujanji().equals("1")) {
				keterangan = "BARU";
			} else if (this.getStatusTemujanji().equals("2")) {
				keterangan = "DALAM TINDAKAN PETUGAS";
			} else if (this.getStatusTemujanji().equals("3")) {
				keterangan = "BATAL";
			} else if (this.getStatusTemujanji().equals("4")) {
				keterangan = "TIDAK SELESAI";
			}
		}
		return keterangan;
	}

	public String getFlagTemujanjiTamat() {
		String flagTamatTemujanji = "T";
		if (this.tarikhMulaTemujanji != null) {
			Calendar currentDate = Calendar.getInstance();
			currentDate.setTime(new Date());
			Calendar calTemujanji = Calendar.getInstance();
			calTemujanji.setTime(this.tarikhMulaTemujanji);

			if (currentDate.after(calTemujanji)) {
				flagTamatTemujanji = "Y";
			}
		}
		return flagTamatTemujanji;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public KuaAgihan getAgihan() {
		return agihan;
	}

	public void setAgihan(KuaAgihan agihan) {
		this.agihan = agihan;
	}

	public KuaPenghuni getPenghuni() {
		return penghuni;
	}

	public void setPenghuni(KuaPenghuni penghuni) {
		this.penghuni = penghuni;
	}

	public KuaKuarters getKuarters() {
		return kuarters;
	}

	public void setKuarters(KuaKuarters kuarters) {
		this.kuarters = kuarters;
	}

	public Users getPemohon() {
		return pemohon;
	}

	public void setPemohon(Users pemohon) {
		this.pemohon = pemohon;
	}

	public Date getTarikhMohonTemujanji() {
		return tarikhMohonTemujanji;
	}

	public void setTarikhMohonTemujanji(Date tarikhMohonTemujanji) {
		this.tarikhMohonTemujanji = tarikhMohonTemujanji;
	}

	public Date getTarikhTemujanji() {
		return tarikhTemujanji;
	}

	public void setTarikhTemujanji(Date tarikhTemujanji) {
		this.tarikhTemujanji = tarikhTemujanji;
	}

	public Date getTarikhMulaTemujanji() {
		return tarikhMulaTemujanji;
	}

	public void setTarikhMulaTemujanji(Date tarikhMulaTemujanji) {
		this.tarikhMulaTemujanji = tarikhMulaTemujanji;
	}

	public Date getTarikhAkhirTemujanji() {
		return tarikhAkhirTemujanji;
	}

	public void setTarikhAkhirTemujanji(Date tarikhAkhirTemujanji) {
		this.tarikhAkhirTemujanji = tarikhAkhirTemujanji;
	}

	public SebabBertukar getSebabBertukar() {
		return sebabBertukar;
	}

	public void setSebabBertukar(SebabBertukar sebabBertukar) {
		this.sebabBertukar = sebabBertukar;
	}

	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
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

	public String getEmel() {
		return emel;
	}

	public void setEmel(String emel) {
		this.emel = emel;
	}

	public String getNoTelefonBimbit() {
		return noTelefonBimbit;
	}

	public void setNoTelefonBimbit(String noTelefonBimbit) {
		this.noTelefonBimbit = noTelefonBimbit;
	}

	public Agensi getAgensi() {
		return agensi;
	}

	public void setAgensi(Agensi agensi) {
		this.agensi = agensi;
	}

	public String getBahagian() {
		return bahagian;
	}

	public void setBahagian(String bahagian) {
		this.bahagian = bahagian;
	}

	public String getAlamatPejabat1() {
		return alamatPejabat1;
	}

	public void setAlamatPejabat1(String alamatPejabat1) {
		this.alamatPejabat1 = alamatPejabat1;
	}

	public String getAlamatPejabat2() {
		return alamatPejabat2;
	}

	public void setAlamatPejabat2(String alamatPejabat2) {
		this.alamatPejabat2 = alamatPejabat2;
	}

	public String getAlamatPejabat3() {
		return alamatPejabat3;
	}

	public void setAlamatPejabat3(String alamatPejabat3) {
		this.alamatPejabat3 = alamatPejabat3;
	}

	public String getPoskodPejabat() {
		return poskodPejabat;
	}

	public void setPoskodPejabat(String poskodPejabat) {
		this.poskodPejabat = poskodPejabat;
	}

	public Bandar getBandarPejabat() {
		return bandarPejabat;
	}

	public void setBandarPejabat(Bandar bandarPejabat) {
		this.bandarPejabat = bandarPejabat;
	}

	public String getNoTelefonPejabat() {
		return noTelefonPejabat;
	}

	public void setNoTelefonPejabat(String noTelefonPejabat) {
		this.noTelefonPejabat = noTelefonPejabat;
	}

	public int getFlagInternal() {
		return flagInternal;
	}

	public void setFlagInternal(int flagInternal) {
		this.flagInternal = flagInternal;
	}

	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}

	public Users getPetugas() {
		return petugas;
	}

	public void setPetugas(Users petugas) {
		this.petugas = petugas;
	}

	public int getKehadiran() {
		return kehadiran;
	}

	public void setKehadiran(int kehadiran) {
		this.kehadiran = kehadiran;
	}

	public Date getTarikhTerimaKunci() {
		return tarikhTerimaKunci;
	}

	public void setTarikhTerimaKunci(Date tarikhTerimaKunci) {
		this.tarikhTerimaKunci = tarikhTerimaKunci;
	}

	public Date getTarikhKeluarKuarters() {
		return tarikhKeluarKuarters;
	}

	public void setTarikhKeluarKuarters(Date tarikhKeluarKuarters) {
		this.tarikhKeluarKuarters = tarikhKeluarKuarters;
	}

	public Date getTarikhSerahKunci() {
		return tarikhSerahKunci;
	}

	public void setTarikhSerahKunci(Date tarikhSerahKunci) {
		this.tarikhSerahKunci = tarikhSerahKunci;
	}

	public String getMemoDaripada() {
		return memoDaripada;
	}

	public void setMemoDaripada(String memoDaripada) {
		this.memoDaripada = memoDaripada;
	}

	public String getMemoKepada() {
		return memoKepada;
	}

	public void setMemoKepada(String memoKepada) {
		this.memoKepada = memoKepada;
	}

	public String getMemoPerkara() {
		return memoPerkara;
	}

	public void setMemoPerkara(String memoPerkara) {
		this.memoPerkara = memoPerkara;
	}

	public String getMemoRujukan() {
		return memoRujukan;
	}

	public void setMemoRujukan(String memoRujukan) {
		this.memoRujukan = memoRujukan;
	}

	public String getMemoSk() {
		return memoSk;
	}

	public void setMemoSk(String memoSk) {
		this.memoSk = memoSk;
	}

	public Date getMemoTarikh() {
		return memoTarikh;
	}

	public void setMemoTarikh(Date memoTarikh) {
		this.memoTarikh = memoTarikh;
	}

	public String getStatusTemujanji() {
		return statusTemujanji;
	}

	public void setStatusTemujanji(String statusTemujanji) {
		this.statusTemujanji = statusTemujanji;
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
