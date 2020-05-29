package bph.entities.rpp;

import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lebah.db.Db;
import lebah.template.UID;
import portal.module.entity.Users;
import bph.entities.kew.KewTuntutanDeposit;
import bph.entities.kewangan.KewBayaranResit;
import bph.entities.kewangan.KewDeposit;
import bph.entities.kewangan.KewInvois;
import bph.entities.kewangan.KewResitSenaraiInvois;
import bph.entities.kewangan.KewSubsidiari;
import bph.entities.kod.CaraBayar;
import bph.entities.kod.JenisUnitRPP;
import bph.entities.kod.Negeri;
import bph.entities.kod.Status;
import bph.utils.Util;
import db.persistence.MyPersistence;

@Entity
@Table(name = "rpp_permohonan")
public class RppPermohonan {

	@Id
	@Column(name = "id")
	private String id;

	@OneToOne
	@JoinColumn(name = "id_jenis_unit_rpp")
	private JenisUnitRPP jenisUnitRpp;

	@OneToOne
	@JoinColumn(name = "id_peranginan")
	private RppPeranginan rppPeranginan;

	@OneToOne
	@JoinColumn(name = "id_pemohon")
	private Users pemohon;

	@Column(name = "jenis_pemohon")
	private String jenisPemohon;

	@Column(name = "status_bayaran")
	private String statusBayaran;

	@Column(name = "jenis_permohonan")
	private String jenisPermohonan;

	@Column(name = "no_tempahan")
	private String noTempahan;

	@Column(name = "jumlah_amaun")
	private Double jumlahAmaun;

	@ManyToOne
	@JoinColumn(name = "id_status")
	private Status status;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_permohonan")
	private Date tarikhPermohonan;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_bayaran")
	private Date tarikhBayaran;

	@Column(name = "catatan_pembatalan")
	private String catatanPembatalan;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_pembatalan")
	private Date tarikhPembatalan;

	@ManyToOne
	@JoinColumn(name = "id_masuk")
	private Users idMasuk;

	@ManyToOne
	@JoinColumn(name = "id_kemaskini")
	private Users idKemaskini;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_masuk")
	private Date tarikhMasuk;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_kemaskini")
	private Date tarikhKemaskini;

	@Column(name = "bil_dewasa")
	private Integer bilDewasa;

	@Column(name = "bil_unit")
	private Integer kuantiti;

	@Column(name = "bil_kanak_kanak")
	private Integer bilKanakKanak;

	@Column(name = "bil_tambahan_dewasa")
	private Integer bilTambahanDewasa;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_masuk_rpp")
	private Date tarikhMasukRpp;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_keluar_rpp")
	private Date tarikhKeluarRpp;

	@Column(name = "aktiviti_utama_1")
	private String aktivitiUtama1;

	@Column(name = "aktiviti_utama_2")
	private String aktivitiUtama2;

	@Column(name = "aktiviti_utama_3")
	private String aktivitiUtama3;

	@Column(name = "bahagian_unit")
	private String bahagianUnit;

	@Column(name = "alamat_surat_1")
	private String alamatSurat1;

	@Column(name = "alamat_surat_2")
	private String alamatSurat2;

	@Column(name = "alamat_surat_3")
	private String alamatSurat3;

	@Column(name = "poskod_surat")
	private String poskodSurat;

	@OneToOne
	@JoinColumn(name = "id_negeri_surat")
	private Negeri negeriSurat;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_kelulusan_sub")
	private Date tarikhKelulusanSub;

	@Column(name = "flag_kelulusan_sub")
	private String flagKelulusanSub;

	@Column(name = "catatan_sub")
	private String catatanSub;

	@OneToOne
	@JoinColumn(name = "id_pemohon_batal")
	private Users pemohonBatal;

	@Column(name = "flag_waktu_puncak")
	private String flagWaktuPuncak;

	@Column(name = "catatan_tidak_hadir")
	private String catatanTidakHadir;

	@Column(name = "no_lo_tempahan")
	private String noLoTempahan;

	@Column(name = "catatan_lo")
	private String catatanLo;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_bayaran_lo")
	private Date tarikhBayaranLO;

	@OneToOne
	@JoinColumn(name = "id_permohonan_bayaran_balik")
	private RppPermohonanBayaranBalik permohonanBayaranBalik;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_akhir_bayaran")
	private Date tarikhAkhirBayaran;

	@Column(name = "catatan_penyemak")
	private String catatanPenyemak;

	/** Upload resit pembayaran bagi pd dan langkawi */
	@Column(name = "photofilename")
	private String photofilename;
	@Column(name = "thumbfilename")
	private String thumbfilename;

	@Column(name = "bil_tambahan_kanak_kanak")
	private Integer bilTambahanKanakKanak;

	@Column(name = "catatan_pulang_tempahan")
	private String catatanPulangTempahan;

	// Direct get maklumat resit sewa & deposit.
	// One to one sebab bayar sekali shj
	@OneToOne
	@JoinColumn(name = "id_resit_sewa")
	private KewBayaranResit resitSewa;

	@OneToOne
	@JoinColumn(name = "id_resit_deposit")
	private KewBayaranResit resitDeposit;

	@Column(name = "catatan_kelompok_tidak_lulus")
	private String catatanKelompokTidakLulus;

	@Column(name = "catatan_booking_hq")
	private String catatanBookingHq;

	@ManyToOne
	@JoinColumn(name = "id_pelulus_premier")
	private Users pelulusPremier;

	@ManyToOne
	@JoinColumn(name = "id_sebab_mohon_rt")
	private RppSebabMohonRT sebabMohonRT;

	@Column(name = "flag_daftar_offline")
	private String flagDaftarOffline;

	@Column(name = "flag_syspintar")
	private String flagSyspintar;

	@ManyToOne
	@JoinColumn(name = "id_kaedah_bayaran")
	private CaraBayar kaedahBayaran;

	public RppPermohonan() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCatatanKelompokTidakLulus() {
		return catatanKelompokTidakLulus;
	}

	public void setCatatanKelompokTidakLulus(String catatanKelompokTidakLulus) {
		this.catatanKelompokTidakLulus = catatanKelompokTidakLulus;
	}

	public KewBayaranResit getResitSewa() {
		return resitSewa;
	}

	public void setResitSewa(KewBayaranResit resitSewa) {
		this.resitSewa = resitSewa;
	}

	public KewBayaranResit getResitDeposit() {
		return resitDeposit;
	}

	public void setResitDeposit(KewBayaranResit resitDeposit) {
		this.resitDeposit = resitDeposit;
	}

	public Users getPemohon() {
		return pemohon;
	}

	public Integer getBilTambahanKanakKanak() {
		return bilTambahanKanakKanak;
	}

	public void setBilTambahanKanakKanak(Integer bilTambahanKanakKanak) {
		this.bilTambahanKanakKanak = bilTambahanKanakKanak;
	}

	public void setPemohon(Users pemohon) {
		this.pemohon = pemohon;
	}

	public String getJenisPemohon() {
		return jenisPemohon;
	}

	public void setJenisPemohon(String jenisPemohon) {
		this.jenisPemohon = jenisPemohon;
	}

	public String getStatusBayaran() {
		return statusBayaran;
	}

	public void setStatusBayaran(String statusBayaran) {
		this.statusBayaran = statusBayaran;
	}

	public String getJenisPermohonan() {
		return jenisPermohonan;
	}

	public void setJenisPermohonan(String jenisPermohonan) {
		this.jenisPermohonan = jenisPermohonan;
	}

	public String getNoTempahan() {
		return noTempahan;
	}

	public void setNoTempahan(String noTempahan) {
		this.noTempahan = noTempahan;
	}

	public Double getJumlahAmaun() {
		return jumlahAmaun;
	}

	public void setJumlahAmaun(Double jumlahAmaun) {
		this.jumlahAmaun = jumlahAmaun;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Users getIdMasuk() {
		return idMasuk;
	}

	public void setIdMasuk(Users idMasuk) {
		this.idMasuk = idMasuk;
	}

	public Users getIdKemaskini() {
		return idKemaskini;
	}

	public void setIdKemaskini(Users idKemaskini) {
		this.idKemaskini = idKemaskini;
	}

	public Date getTarikhMasuk() {
		return tarikhMasuk;
	}

	public void setTarikhMasuk(Date tarikhMasuk) {
		this.tarikhMasuk = tarikhMasuk;
	}

	public Date getTarikhKemaskini() {
		return tarikhKemaskini;
	}

	public void setTarikhKemaskini(Date tarikhKemaskini) {
		this.tarikhKemaskini = tarikhKemaskini;
	}

	public RppPeranginan getRppPeranginan() {
		return rppPeranginan;
	}

	public void setRppPeranginan(RppPeranginan rppPeranginan) {
		this.rppPeranginan = rppPeranginan;
	}

	public Date getTarikhPermohonan() {
		return tarikhPermohonan;
	}

	public void setTarikhPermohonan(Date tarikhPermohonan) {
		this.tarikhPermohonan = tarikhPermohonan;
	}

	public String getCatatanPembatalan() {
		return catatanPembatalan;
	}

	public void setCatatanPembatalan(String catatanPembatalan) {
		this.catatanPembatalan = catatanPembatalan;
	}

	public Date getTarikhPembatalan() {
		return tarikhPembatalan;
	}

	public String getCatatanLo() {
		return catatanLo;
	}

	public void setCatatanLo(String catatanLo) {
		this.catatanLo = catatanLo;
	}

	public Date getTarikhBayaranLO() {
		return tarikhBayaranLO;
	}

	public void setTarikhBayaranLO(Date tarikhBayaranLO) {
		this.tarikhBayaranLO = tarikhBayaranLO;
	}

	public void setTarikhPembatalan(Date tarikhPembatalan) {
		this.tarikhPembatalan = tarikhPembatalan;
	}

	public JenisUnitRPP getJenisUnitRpp() {
		return jenisUnitRpp;
	}

	public void setJenisUnitRpp(JenisUnitRPP jenisUnitRpp) {
		this.jenisUnitRpp = jenisUnitRpp;
	}

	public Integer getBilDewasa() {
		return bilDewasa;
	}

	public void setBilDewasa(Integer bilDewasa) {
		this.bilDewasa = bilDewasa;
	}

	public Integer getKuantiti() {
		return kuantiti;
	}

	public void setKuantiti(Integer kuantiti) {
		this.kuantiti = kuantiti;
	}

	public Integer getBilKanakKanak() {
		return bilKanakKanak;
	}

	public void setBilKanakKanak(Integer bilKanakKanak) {
		this.bilKanakKanak = bilKanakKanak;
	}

	public Date getTarikhMasukRpp() {
		return tarikhMasukRpp;
	}

	public void setTarikhMasukRpp(Date tarikhMasukRpp) {
		this.tarikhMasukRpp = tarikhMasukRpp;
	}

	public Date getTarikhKeluarRpp() {
		return tarikhKeluarRpp;
	}

	public void setTarikhKeluarRpp(Date tarikhKeluarRpp) {
		this.tarikhKeluarRpp = tarikhKeluarRpp;
	}

	public Integer getTotalBilMalam() {
		Integer days = 0;
		if (this.tarikhKeluarRpp != null && this.tarikhMasukRpp != null) {
			days = (int) ((tarikhKeluarRpp.getTime() - tarikhMasukRpp.getTime()) / (1000 * 60 * 60 * 24));
		}
		return days;
	}

	public RppPermohonanBayaranBalik getPermohonanBayaranBalik() {
		return permohonanBayaranBalik;
	}

	public void setPermohonanBayaranBalik(
			RppPermohonanBayaranBalik permohonanBayaranBalik) {
		this.permohonanBayaranBalik = permohonanBayaranBalik;
	}

	public String getCatatanKeluar() {
		MyPersistence mp = null;
		String str = "";
		try {
			mp = new MyPersistence();
			RppPengurusanBilik b = (RppPengurusanBilik) mp
					.get("select a from RppPengurusanBilik a where a.permohonan.id = '"
							+ this.id + "' ");
			if (b != null) {
				str = b.getCatatanKeluar();
			}
		} catch (Exception e) {
			System.out.println("Error getCatatanKeluar : " + e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return str;
	}

	public String getAktivitiUtama1() {
		return aktivitiUtama1;
	}

	public void setAktivitiUtama1(String aktivitiUtama1) {
		this.aktivitiUtama1 = aktivitiUtama1;
	}

	public String getAktivitiUtama2() {
		return aktivitiUtama2;
	}

	public void setAktivitiUtama2(String aktivitiUtama2) {
		this.aktivitiUtama2 = aktivitiUtama2;
	}

	public String getAktivitiUtama3() {
		return aktivitiUtama3;
	}

	public void setAktivitiUtama3(String aktivitiUtama3) {
		this.aktivitiUtama3 = aktivitiUtama3;
	}

	public String getBahagianUnit() {
		return bahagianUnit;
	}

	public void setBahagianUnit(String bahagianUnit) {
		this.bahagianUnit = bahagianUnit;
	}

	public String getAlamatSurat1() {
		return alamatSurat1;
	}

	public void setAlamatSurat1(String alamatSurat1) {
		this.alamatSurat1 = alamatSurat1;
	}

	public String getAlamatSurat2() {
		return alamatSurat2;
	}

	public void setAlamatSurat2(String alamatSurat2) {
		this.alamatSurat2 = alamatSurat2;
	}

	public String getPoskodSurat() {
		return poskodSurat;
	}

	public void setPoskodSurat(String poskodSurat) {
		this.poskodSurat = poskodSurat;
	}

	public Negeri getNegeriSurat() {
		return negeriSurat;
	}

	public void setNegeriSurat(Negeri negeriSurat) {
		this.negeriSurat = negeriSurat;
	}

	public String getAlamatSurat3() {
		return alamatSurat3;
	}

	public void setAlamatSurat3(String alamatSurat3) {
		this.alamatSurat3 = alamatSurat3;
	}

	public Date getTarikhKelulusanSub() {
		return tarikhKelulusanSub;
	}

	public void setTarikhKelulusanSub(Date tarikhKelulusanSub) {
		this.tarikhKelulusanSub = tarikhKelulusanSub;
	}

	public String getFlagKelulusanSub() {
		return flagKelulusanSub;
	}

	public void setFlagKelulusanSub(String flagKelulusanSub) {
		this.flagKelulusanSub = flagKelulusanSub;
	}

	public String getCatatanSub() {
		return catatanSub;
	}

	public void setCatatanSub(String catatanSub) {
		this.catatanSub = catatanSub;
	}

	public Users getPemohonBatal() {
		return pemohonBatal;
	}

	public void setPemohonBatal(Users pemohonBatal) {
		this.pemohonBatal = pemohonBatal;
	}

	public Date getTarikhBayaran() {
		return tarikhBayaran;
	}

	public void setTarikhBayaran(Date tarikhBayaran) {
		this.tarikhBayaran = tarikhBayaran;
	}

	public Integer getBilTambahanDewasa() {
		return bilTambahanDewasa;
	}

	public void setBilTambahanDewasa(Integer bilTambahanDewasa) {
		this.bilTambahanDewasa = bilTambahanDewasa;
	}

	public String getFlagWaktuPuncak() {
		return flagWaktuPuncak;
	}

	public void setFlagWaktuPuncak(String flagWaktuPuncak) {
		this.flagWaktuPuncak = flagWaktuPuncak;
	}

	public String getCatatanTidakHadir() {
		return catatanTidakHadir;
	}

	public void setCatatanTidakHadir(String catatanTidakHadir) {
		this.catatanTidakHadir = catatanTidakHadir;
	}

	public String getNoLoTempahan() {
		return noLoTempahan;
	}

	public void setNoLoTempahan(String noLoTempahan) {
		this.noLoTempahan = noLoTempahan;
	}

	public Date getTarikhAkhirBayaran() {
		return tarikhAkhirBayaran;
	}

	public void setTarikhAkhirBayaran(Date tarikhAkhirBayaran) {
		this.tarikhAkhirBayaran = tarikhAkhirBayaran;
	}

	public String getCatatanPenyemak() {
		return catatanPenyemak;
	}

	public void setCatatanPenyemak(String catatanPenyemak) {
		this.catatanPenyemak = catatanPenyemak;
	}

	public String getKeteranganLainValue() {
		MyPersistence mp = null;
		String str = "";
		try {
			mp = new MyPersistence();
			RppKelompokSenaraiAktiviti ku = (RppKelompokSenaraiAktiviti) mp
					.get("select a from RppKelompokSenaraiAktiviti a where a.permohonan.id = '"
							+ this.id + "' and a.aktiviti.id = '06' ");
			if (ku != null) {
				str = ku.getKeteranganAktivitiLain();
			}
		} catch (Exception e) {
			System.out.println("Error getKeteranganLainValue : "
					+ e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return str;
	}

	public String checkVIP() {
		MyPersistence mp = null;
		String vip = "";
		try {
			mp = new MyPersistence();
			RppPemohonVIP obj = null;
			if (this.pemohon != null) {
				obj = (RppPemohonVIP) mp
						.get("select x from RppPemohonVIP x where x.pemohon.id = '"
								+ this.pemohon.getId()
								+ "' and x.flagVip = 'Y' ");
			}

			if (obj != null) {
				vip = "VIP";
			}
		} catch (Exception e) {
			System.out.println("Error checkVIP : " + e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return vip;
	}

	public String statusBayaranLewat() {
		String status = "";
		Date today = new Date();
		if (tarikhAkhirBayaran != null) {
			if (today.after(tarikhAkhirBayaran)
					|| today.equals(tarikhAkhirBayaran)) {
				status = "LEWAT";
			}
		}
		return status;
	}

	public String keteranganStatusBayaran() {
		String status = "";
		if (this.statusBayaran != null
				&& this.statusBayaran.equalsIgnoreCase("Y")) {
			status = "TELAH BAYAR";
		} else {
			status = "BELUM BAYAR";
		}
		return status;
	}

	public Double amaunBayaranBalikWithDeposit() {
		Double amaun = 0d;
		Integer days = 0;
		Date today = new Date();

		// get bilangan hari
		if (this.tarikhMasukRpp != null) {
			days = (int) ((tarikhMasukRpp.getTime() - today.getTime()) / (1000 * 60 * 60 * 24));
		}

		if (days < 14) {
			// bayaran burn
			amaun = 0d;
		} else {
			// 50% bayaran
			amaun = amaunTotalSewaRpWithoutDeposit() * 0.5;

			// check min bayaran
			if (amaun < 50) {
				// amaun = 50.00;
				amaun = 0.00;
				/**
				 * Burn. atas persetujuan mampu - IR masa testing mampu pd
				 * 04122015
				 */
			}
		}
		return amaun + amaunDeposit();
	}

	public Double amaunBayaranBalikWithoutDeposit() {
		Double amaun = 0d;
		Integer days = 0;
		Date today = new Date();

		// get bilangan hari
		if (this.tarikhMasukRpp != null) {
			days = (int) ((tarikhMasukRpp.getTime() - today.getTime()) / (1000 * 60 * 60 * 24));
		}

		if (days < 14) {
			// bayaran burn
			amaun = 0d;
		} else {
			// 50% bayaran
			amaun = amaunTotalSewaRpWithoutDeposit() * 0.5;

			// check min bayaran
			if (amaun < 50) {
				amaun = 50.00;
			}
		}
		return amaun;
	}

	@SuppressWarnings("unchecked")
	public Double amaunTotalSewaRpWithoutDeposit() {
		MyPersistence mp = null;
		Double amaun = 0d;
		try {
			mp = new MyPersistence();
			List<RppAkaun> ak = null;
			if (this.id != null) {
				ak = mp.list("select x from RppAkaun x where x.permohonan.id = '"
						+ this.id + "' and x.kodHasil.id <> '72311' ");
			}

			for (int i = 0; i < ak.size(); i++) {
				amaun = amaun + ak.get(i).getDebit();
			}
		} catch (Exception e) {
			System.out.println("Error amaunTotalSewaRpWithoutDeposit : "
					+ e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return amaun;
	}

	@SuppressWarnings("unchecked")
	public Double amaunTotalSewaRpWithDeposit() {
		MyPersistence mp = null;
		Double amaun = 0d;
		try {
			mp = new MyPersistence();
			List<RppAkaun> ak = null;
			if (this.id != null) {
				ak = mp.list("select x from RppAkaun x where x.permohonan.id = '"
						+ this.id + "' ");
			}

			for (int i = 0; i < ak.size(); i++) {
				amaun = amaun + ak.get(i).getDebit();
			}
		} catch (Exception e) {
			System.out.println("Error amaunTotalSewaRpWithDeposit : "
					+ e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return amaun;
	}

	public Double amaunDeposit() {
		MyPersistence mp = null;
		Double amaun = 0d;
		try {
			mp = new MyPersistence();
			RppAkaun ak = null;
			if (this.id != null) {
				ak = (RppAkaun) mp
						.get("select x from RppAkaun x where x.permohonan.id = '"
								+ this.id + "' and x.kodHasil.id = '72311' ");
			}
			if (ak != null) {
				amaun = ak.getDebit();
			}
		} catch (Exception e) {
			System.out.println("Error amaunDeposit : " + e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return amaun;
	}

	public boolean reminderBayaranKelompok() {
		boolean remind = false;
		int daysBeforeCheckin = 14;
		Date today = new Date();

		Date dateReminder = new Date();
		if (this.tarikhMasukRpp != null
				&& !this.statusBayaran.equalsIgnoreCase("Y")) {
			Calendar c = Calendar.getInstance();
			c.setTime(this.tarikhMasukRpp);
			c.add(Calendar.DATE, -daysBeforeCheckin);
			dateReminder.setTime(c.getTime().getTime());
			if (dateReminder.before(today) || dateReminder.equals(today)) {
				remind = true;
			}
		}
		return remind;
	}

	@SuppressWarnings("unchecked")
	public Integer jumlahBilanganKerosakan() {
		MyPersistence mp = null;
		int bil = 0;
		try {
			mp = new MyPersistence();
			List<RppAduanKerosakan> list = mp
					.list("select x from RppAduanKerosakan x where x.permohonan.id = '"
							+ this.id + "' ");
			for (int i = 0; i < list.size(); i++) {
				bil = bil + list.get(i).getKuantiti();
			}
		} catch (Exception e) {
			System.out.println("Error jumlahBilanganKerosakan : "
					+ e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return bil;
	}

	@SuppressWarnings("unchecked")
	public Double jumlahNilaiKerosakan() {
		MyPersistence mp = null;
		Double jumlah = 0d;
		try {
			mp = new MyPersistence();
			List<RppAduanKerosakan> list = mp
					.list("select x from RppAduanKerosakan x where x.permohonan.id = '"
							+ this.id + "' ");
			for (int i = 0; i < list.size(); i++) {
				jumlah = jumlah + list.get(i).getHarga();
			}
		} catch (Exception e) {
			System.out
					.println("Error jumlahNilaiKerosakan : " + e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return jumlah;
	}

	public Double bakiDeposit() {
		Double baki = amaunDeposit();
		if (jumlahNilaiKerosakan() != 0d) {
			baki = amaunDeposit() - jumlahNilaiKerosakan();
		}
		return baki;
	}

	public Integer getTarikhPermohonanInYear() {
		int year = Integer.parseInt(Util.getCurrentDate("yyyy"));
		if (this.tarikhPermohonan != null) {
			year = Integer.parseInt(Util.getDateTime(this.tarikhPermohonan,
					"yyyy"));
		}
		return year;
	}

	public String getKeteranganStatusBayaran() {
		String statusBayaran = "";
		if (this.statusBayaran != null) {
			if ("Y".equals(this.statusBayaran)) {
				statusBayaran = "TELAH BAYAR";
			} else if ("T".equals(this.statusBayaran)) {
				statusBayaran = "BELUM BAYAR";
			}
		}
		return statusBayaran;
	}

	public String getPhotofilename() {
		return photofilename;
	}

	public void setPhotofilename(String photofilename) {
		this.photofilename = photofilename;
	}

	public String getThumbfilename() {
		return thumbfilename;
	}

	public void setThumbfilename(String thumbfilename) {
		this.thumbfilename = thumbfilename;
	}

	public String getCatatanPulangTempahan() {
		return catatanPulangTempahan;
	}

	public void setCatatanPulangTempahan(String catatanPulangTempahan) {
		this.catatanPulangTempahan = catatanPulangTempahan;
	}

	@SuppressWarnings("unchecked")
	public String getIdResitSewa() {
		MyPersistence mp = null;
		String str = "";
		try {
			mp = new MyPersistence();
			if (this.resitSewa != null) {
				str = this.resitSewa.getId();
			} else {

				RppAkaun oneAk = null;
				KewInvois oneInv = null;
				KewResitSenaraiInvois rsi = null;
				List<RppAkaun> akInvois = mp
						.list("select x from RppAkaun x where x.permohonan.id = '"
								+ this.id + "' and x.kodHasil.id = '74299' ");

				if (akInvois.size() > 0) {
					oneAk = akInvois.get(0);
					if (oneAk != null) {
						oneInv = (KewInvois) mp
								.get("select x from KewInvois x where x.idLejar = '"
										+ oneAk.getId() + "' ");
						if (oneInv != null) {
							rsi = (KewResitSenaraiInvois) mp
									.get("select x from KewResitSenaraiInvois x where x.invois.id = '"
											+ oneInv.getId() + "' ");
							if (rsi != null) {
								str = rsi.getResit().getId();
							}
						}
					}
				}

			}
		} catch (Exception e) {
			System.out.println("Error getIdResitSewa : " + e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return str;
	}

	@SuppressWarnings("unchecked")
	public String getIdResitDeposit() {
		MyPersistence mp = null;
		String str = "";
		try {
			mp = new MyPersistence();
			if (this.resitDeposit != null) {
				str = this.resitDeposit.getId();
			} else {

				RppAkaun oneAk = null;
				KewDeposit oneDep = null;
				KewResitSenaraiInvois rsi = null;
				List<RppAkaun> akInvois = mp
						.list("select x from RppAkaun x where x.permohonan.id = '"
								+ this.id + "' and x.kodHasil.id = '72311' ");

				if (akInvois.size() > 0) {
					oneAk = akInvois.get(0);
					if (oneAk != null) {
						oneDep = (KewDeposit) mp
								.get("select x from KewDeposit x where x.idLejar = '"
										+ oneAk.getId() + "' ");
						if (oneDep != null) {
							rsi = (KewResitSenaraiInvois) mp
									.get("select x from KewResitSenaraiInvois x where x.deposit.id = '"
											+ oneDep.getId() + "' ");
							if (rsi != null) {
								str = rsi.getResit().getId();
							}
						}
					}
				}

			}
		} catch (Exception e) {
			System.out.println("Error getIdResitDeposit : " + e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return str;
	}

	public KewTuntutanDeposit getObjTuntutanDeposit() {
		MyPersistence mp = null;
		KewTuntutanDeposit obj = null;
		try {
			mp = new MyPersistence();
			if (this.id != null) {
				RppAkaun ak = (RppAkaun) mp
						.get("select w from RppAkaun w where w.permohonan.id = '"
								+ this.id + "' and w.kodHasil.id = '72311' ");
				if (ak != null) {
					KewDeposit dep = (KewDeposit) mp
							.get("select x from KewDeposit x where x.idLejar = '"
									+ ak.getId() + "' ");
					if (dep != null) {
						obj = (KewTuntutanDeposit) mp
								.get("select x from KewTuntutanDeposit x where x.deposit.id = '"
										+ dep.getId() + "' ");
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Error getObjTuntutanDeposit : "
					+ e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return obj;
	}

	public String getCatatanBookingHq() {
		return catatanBookingHq;
	}

	public void setCatatanBookingHq(String catatanBookingHq) {
		this.catatanBookingHq = catatanBookingHq;
	}

	public Users getPelulusPremier() {
		return pelulusPremier;
	}

	public void setPelulusPremier(Users pelulusPremier) {
		this.pelulusPremier = pelulusPremier;
	}

	public RppSebabMohonRT getSebabMohonRT() {
		return sebabMohonRT;
	}

	public void setSebabMohonRT(RppSebabMohonRT sebabMohonRT) {
		this.sebabMohonRT = sebabMohonRT;
	}

	public KewSubsidiari getObjKewSubsidiari() {
		MyPersistence mp = null;
		KewSubsidiari obj = null;
		try {
			mp = new MyPersistence();
			if (this.id != null) {
				obj = (KewSubsidiari) mp
						.get("select x from KewSubsidiari x where x.idFail = '"
								+ this.id + "' ");
			}
		} catch (Exception e) {
			System.out.println("Error getObjKewSubsidiari : " + e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return obj;
	}

	// public RppAkaun getObjRppAkaun() {
	// MyPersistence mp = null;
	// RppAkaun akaun= null;
	// try {
	// mp = new MyPersistence();
	// akaun = (RppAkaun)
	// mp.get("select x from RppAkaun x where x.permohonan.id = '"+this.id+"' and x.kodHasil.id='72311'");
	// } catch (Exception e) {
	// System.out.println("Error getObjKewSubsidiari : "+e.getMessage());
	// }finally{
	// if (mp != null) { mp.close(); }
	// }
	// return akaun;
	// }
	//
	// public KewDeposit getObjKewDeposit() {
	// MyPersistence mp = null;
	// RppAkaun akaun= null;
	// KewDeposit d = null;
	// try {
	// mp = new MyPersistence();
	// akaun = (RppAkaun)
	// mp.get("select x from RppAkaun x where x.permohonan.id = '"+this.id+"' and x.kodHasil.id='72311'");
	// d= (KewDeposit)
	// mp.get("select x from KewDeposit x where x.idLejar = '"+akaun.getId()+"' ");
	// } catch (Exception e) {
	// System.out.println("Error getObjKewSubsidiari : "+e.getMessage());
	// }finally{
	// if (mp != null) { mp.close(); }
	// }
	// return d;
	// }

	public KewTuntutanDeposit getObjKewTuntutanDeposit() {
		MyPersistence mp = null;
		RppAkaun akaun = null;
		KewDeposit d = null;
		KewTuntutanDeposit td = null;
		try {
			mp = new MyPersistence();
			akaun = (RppAkaun) mp
					.get("select x from RppAkaun x where x.permohonan.id = '"
							+ this.id + "' and x.kodHasil.id='72311'");
			d = (KewDeposit) mp
					.get("select x from KewDeposit x where x.idLejar = '"
							+ akaun.getId() + "' ");
			td = (KewTuntutanDeposit) mp
					.get("select x from KewTuntutanDeposit x where x.deposit.id = '"
							+ d.getId() + "' ");
		} catch (Exception e) {
			System.out.println("Error getObjKewSubsidiari : " + e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return td;
	}

	public String getFlagDaftarOffline() {
		return flagDaftarOffline;
	}

	public void setFlagDaftarOffline(String flagDaftarOffline) {
		this.flagDaftarOffline = flagDaftarOffline;
	}

	public String getFlagSyspintar() {
		return flagSyspintar;
	}

	public void setFlagSyspintar(String flagSyspintar) {
		this.flagSyspintar = flagSyspintar;
	}

	public Integer getKelompokValue() {
		Db db1 = null;
		int val = 0;
		// double totalHarga = 0d;
		if (this.id != null) {
			try {
				db1 = new Db();
				String sql = "SELECT x.bil_unit, y.kadar_sewa FROM rpp_kelompok_unit x, ruj_jenis_unit_rpp y WHERE x.id_jenis_unit_rpp = y.id AND x.id_permohonan = '"
						+ this.id + "'  ";
				ResultSet rs = db1.getStatement().executeQuery(sql);
				while (rs.next()) {
					val += rs.getInt("bil_unit");
					// totalHarga += (rs.getDouble("kadar_sewa") *
					// rs.getInt("bil_unit"));
				}
			} catch (Exception e) {
				System.out.println("Error getKelompokValue[Total] : "
						+ e.getMessage());
			} finally {
				if (db1 != null)
					db1.close();
			}
		}

		return val;
	}

	public Double getKelompokValueHarga() {
		Db db1 = null;
		double totalHarga = 0d;
		if (this.id != null) {
			try {
				db1 = new Db();
				String sql = "SELECT x.bil_unit, y.kadar_sewa FROM rpp_kelompok_unit x, ruj_jenis_unit_rpp y WHERE x.id_jenis_unit_rpp = y.id AND x.id_permohonan = '"
						+ this.id + "'  ";
				ResultSet rs = db1.getStatement().executeQuery(sql);
				while (rs.next()) {
					totalHarga += (rs.getDouble("kadar_sewa") * rs
							.getInt("bil_unit"));
				}
			} catch (Exception e) {
				System.out.println("Error getKelompokValueHarga[Total] : "
						+ e.getMessage());
			} finally {
				if (db1 != null)
					db1.close();
			}
		}
		return totalHarga;
	}

	public Integer getKelompokLulusValue() {
		Db db1 = null;
		int val = 0;
		if (this.id != null) {
			try {
				db1 = new Db();
				String sql = "SELECT x.bil_unit_lulus FROM rpp_kelompok_unit x WHERE x.id_permohonan = '"
						+ this.id + "'  ";
				ResultSet rs = db1.getStatement().executeQuery(sql);
				while (rs.next()) {
					val += rs.getInt("bil_unit_lulus");
				}
			} catch (Exception e) {
				System.out.println("Error getKelompokLulusValue[Total] : "
						+ e.getMessage());
			} finally {
				if (db1 != null)
					db1.close();
			}
		}
		return val;
	}

	public Double getKelompokLulusValueHarga() {
		Db db1 = null;
		double totalHarga = 0d;
		if (this.id != null) {
			try {
				db1 = new Db();
				String sql = "SELECT x.bil_unit_lulus, y.kadar_sewa FROM rpp_kelompok_unit x, ruj_jenis_unit_rpp y WHERE x.id_jenis_unit_rpp = y.id AND x.id_permohonan = '"
						+ this.id + "'  ";
				ResultSet rs = db1.getStatement().executeQuery(sql);
				while (rs.next()) {
					totalHarga += (rs.getDouble("kadar_sewa") * rs
							.getInt("bil_unit_lulus"));
				}
			} catch (Exception e) {
				System.out.println("Error getKelompokLulusValueHarga[Total] : "
						+ e.getMessage());
			} finally {
				if (db1 != null)
					db1.close();
			}
		}
		return totalHarga;
	}

	public CaraBayar getKaedahBayaran() {
		return kaedahBayaran;
	}

	public void setKaedahBayaran(CaraBayar kaedahBayaran) {
		this.kaedahBayaran = kaedahBayaran;
	}
}
