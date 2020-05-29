package bph.entities.kontrak;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
import bph.entities.kod.JenisKontrak;
import bph.entities.kod.KaedahPerolehan;
import bph.entities.kod.KategoriKontrak;
import bph.entities.kod.Seksyen;

@Entity
@Table(name = "kontrak_kontrak")
public class KontrakKontrak {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_jenis_kontrak")
	private JenisKontrak jenisKontrak;

	@ManyToOne
	@JoinColumn(name = "id_kategori_kontrak")
	private KategoriKontrak kategoriKontrak;

	@ManyToOne
	@JoinColumn(name = "id_kontraktor")
	private KontrakKontraktor kontraktor;

	@ManyToOne
	@JoinColumn(name = "id_kaedah_perolehan")
	private KaedahPerolehan kaedahPerolehan;

	@Column(name = "no_daftar_kontrak")
	private String noDaftarKontrak;

	@Column(name = "no_rujukan")
	private String noRujukan;

	@Column(name = "perkhidmatan")
	private String perkhidmatan;

	@Column(name = "kod_program")
	private String kodProgram;

	@Column(name = "kod_objek")
	private String kodObjek;

	@Column(name = "tarikh_mula")
	@Temporal(TemporalType.DATE)
	private Date tarikhMula;

	@Column(name = "tempoh")
	private int tempoh;

	@Column(name = "tarikh_tamat")
	@Temporal(TemporalType.DATE)
	private Date tarikhTamat;

	@Column(name = "tarikh_keluar_sst")
	@Temporal(TemporalType.DATE)
	private Date tarikhKeluarSst;

	@Column(name = "tarikh_terima_sst")
	@Temporal(TemporalType.DATE)
	private Date tarikhTerimaSst;

	@Column(name = "amaun_ansuran")
	private double amaunAnsuran;

	@Column(name = "mod_ansuran")
	private String modAnsuran;

	@Column(name = "nilai_kontrak")
	private double nilaiKontrak;

	@Column(name = "flag_gst")
	private String flagGST;

	@Column(name = "nilai_kontrak_gst")
	private double nilaiKontrakGST;

	@ManyToOne
	@JoinColumn(name = "id_seksyen")
	private Seksyen seksyen;

	@Column(name = "id_pelaksana")
	private String idPelaksana;

	@Column(name = "catatan")
	private String catatan;

	@Column(name = "status")
	private String status;

	@Column(name = "status_bayaran")
	private String statusBayaran;

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

	public KontrakKontrak() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());
	}

	public String getKeteranganStatus() {
		String status = "";
		if (this.status != null) {
			if (this.status.equals("01")) {
				status = "DAFTAR KONTRAK";
			} else if (this.status.equals("02")) {
				status = "PENGESAHAN MAKLUMAT KONTRAK";
			} else if (this.status.equals("03")) {
				status = "PINDAAN MAKLUMAT KONTRAK";
			} else if (this.status.equals("04")) {
				status = "KONTRAK AKTIF";
			} else if (this.status.equals("05")) {
				status = "KONTRAK TAMAT";
			} else if (this.status.equals("06")) {
				status = "KONTRAK BATAL";
			}
		}
		return status;
	}

	public String getKeteranganModAnsuran() {
		String status = "";
		if (this.status != null) {
			if (this.status.equals("01")) {
				status = "HARIAN";
			} else if (this.status.equals("02")) {
				status = "MINGGUAN";
			} else if (this.status.equals("03")) {
				status = "BULANAN";
			} else if (this.status.equals("04")) {
				status = "TAHUNAN";
			} else if (this.status.equals("05")) {
				status = "TAMAT KONTRAK";
			} else if (this.status.equals("06")) {
				status = "PERATUSAN SIAP KERJA";
			}
		}
		return status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public JenisKontrak getJenisKontrak() {
		return jenisKontrak;
	}

	public void setJenisKontrak(JenisKontrak jenisKontrak) {
		this.jenisKontrak = jenisKontrak;
	}

	public KategoriKontrak getKategoriKontrak() {
		return kategoriKontrak;
	}

	public void setKategoriKontrak(KategoriKontrak kategoriKontrak) {
		this.kategoriKontrak = kategoriKontrak;
	}

	public KontrakKontraktor getKontraktor() {
		return kontraktor;
	}

	public void setKontraktor(KontrakKontraktor kontraktor) {
		this.kontraktor = kontraktor;
	}

	public KaedahPerolehan getKaedahPerolehan() {
		return kaedahPerolehan;
	}

	public void setKaedahPerolehan(KaedahPerolehan kaedahPerolehan) {
		this.kaedahPerolehan = kaedahPerolehan;
	}

	public String getNoDaftarKontrak() {
		return noDaftarKontrak;
	}

	public void setNoDaftarKontrak(String noDaftarKontrak) {
		this.noDaftarKontrak = noDaftarKontrak;
	}

	public String getNoRujukan() {
		return noRujukan;
	}

	public void setNoRujukan(String noRujukan) {
		this.noRujukan = noRujukan;
	}

	public String getPerkhidmatan() {
		return perkhidmatan;
	}

	public void setPerkhidmatan(String perkhidmatan) {
		this.perkhidmatan = perkhidmatan;
	}

	public String getKodProgram() {
		return kodProgram;
	}

	public void setKodProgram(String kodProgram) {
		this.kodProgram = kodProgram;
	}

	public String getKodObjek() {
		return kodObjek;
	}

	public void setKodObjek(String kodObjek) {
		this.kodObjek = kodObjek;
	}

	public Date getTarikhMula() {
		return tarikhMula;
	}

	public void setTarikhMula(Date tarikhMula) {
		this.tarikhMula = tarikhMula;
	}

	public int getTempoh() {
		return tempoh;
	}

	public void setTempoh(int tempoh) {
		this.tempoh = tempoh;
	}

	public Date getTarikhTamat() {
		return tarikhTamat;
	}

	public void setTarikhTamat(Date tarikhTamat) {
		this.tarikhTamat = tarikhTamat;
	}

	public Date getTarikhKeluarSst() {
		return tarikhKeluarSst;
	}

	public void setTarikhKeluarSst(Date tarikhKeluarSst) {
		this.tarikhKeluarSst = tarikhKeluarSst;
	}

	public Date getTarikhTerimaSst() {
		return tarikhTerimaSst;
	}

	public void setTarikhTerimaSst(Date tarikhTerimaSst) {
		this.tarikhTerimaSst = tarikhTerimaSst;
	}

	public double getAmaunAnsuran() {
		return amaunAnsuran;
	}

	public void setAmaunAnsuran(double amaunAnsuran) {
		this.amaunAnsuran = amaunAnsuran;
	}

	public String getModAnsuran() {
		return modAnsuran;
	}

	public void setModAnsuran(String modAnsuran) {
		this.modAnsuran = modAnsuran;
	}

	public double getNilaiKontrak() {
		return nilaiKontrak;
	}

	public void setNilaiKontrak(double nilaiKontrak) {
		this.nilaiKontrak = nilaiKontrak;
	}

	public String getFlagGST() {
		return flagGST;
	}

	public void setFlagGST(String flagGST) {
		this.flagGST = flagGST;
	}

	public double getNilaiKontrakGST() {
		return nilaiKontrakGST;
	}

	public void setNilaiKontrakGST(double nilaiKontrakGST) {
		this.nilaiKontrakGST = nilaiKontrakGST;
	}

	public Seksyen getSeksyen() {
		return seksyen;
	}

	public void setSeksyen(Seksyen seksyen) {
		this.seksyen = seksyen;
	}

	public String getIdPelaksana() {
		return idPelaksana;
	}

	public void setIdPelaksana(String idPelaksana) {
		this.idPelaksana = idPelaksana;
	}

	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusBayaran() {
		return statusBayaran;
	}

	public void setStatusBayaran(String statusBayaran) {
		this.statusBayaran = statusBayaran;
	}

	@SuppressWarnings("unused")
	public String getTempohKontrak() {

		String tempohKontrak = "";
		Date tarikhMula = this.tarikhMula;
		Date tarikhTamat = this.tarikhTamat;
		int bilHari = 0;

		if (tarikhMula != null && tarikhMula.toString().length() > 0) {

			Calendar calTarikhMula = new GregorianCalendar();
			Date dateTarikhMula = tarikhMula;
			calTarikhMula.setTime(dateTarikhMula);

			Calendar calTarikhTamat = new GregorianCalendar();
			Date dateTarikhTamat = tarikhTamat;
			calTarikhTamat.setTime(dateTarikhTamat);

			// Calendar calCurrent = new GregorianCalendar();
			// Date dateCurrent = new Date();
			// calCurrent.setTime(dateCurrent);

			int diffYear = calTarikhTamat.get(Calendar.YEAR)
					- calTarikhMula.get(Calendar.YEAR);
			// System.out.println("PRINT diffYear YEAR ===" + diffYear);

			int diffMonth = diffYear * 12 + calTarikhTamat.get(Calendar.MONTH)
					- calTarikhMula.get(Calendar.MONTH);
			// System.out.println("PRINT diffMonth MONTH ===" + diffMonth);

			bilHari = daysBetween(calTarikhMula.getTime(),
					calTarikhTamat.getTime());
			// System.out.println("PRINT bilHari HARI ===" + bilHari);

			// BILANGAN HARI STATUS BELUM DIBAYAR
			if (calTarikhTamat.getTime().after(calTarikhMula.getTime())) {
				// tempohKontrak = bilHari + " HARI";
				// tempohKontrak = diffYear + " TAHUN ";
				diffMonth = diffMonth + 1;
				tempohKontrak = diffMonth + " BULAN";
				// System.out.println("PRINT diffMonth HARI ===" + diffMonth);
				// if(bilHari < 365){
				// tempohKontrak = bilHari + " HARI";
				// }
				// else if (bilHari > 30 ) {
				// tempohKontrak = diffMonth + " BULAN";
				// }
				// else if (diffMonth >= 12) {
				// tempohKontrak = diffYear + " TAHUN";
				// }
			}
		}
		return tempohKontrak;
	}

	private int daysBetween(Date date1, Date date2) {
		return (int) ((date2.getTime() - date1.getTime()) / (1000 * 60 * 60 * 24));
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
