package bph.entities.pro;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lebah.template.UID;
import portal.module.entity.Users;
import bph.entities.kod.Bandar;
import bph.entities.kod.JenisAduan;
import bph.entities.kod.Status;
import bph.entities.kod.SumberAduan;

@Entity
@Table(name = "pro_aduan")
public class ProAduan {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_sumber_aduan")
	private SumberAduan sumberAduan;

	@ManyToOne
	@JoinColumn(name = "id_jenis_aduan")
	private JenisAduan jenisAduan;

	@Column(name = "no_aduan")
	private String noAduan;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_aduan")
	private Date tarikhAduan;

	@ManyToOne
	@JoinColumn(name = "id_status")
	private Status status;

	@Column(name = "no_pengenalan")
	private String noPengenalan;

	@Column(name = "nama")
	private String nama;

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

	@Column(name = "emel")
	private String emel;

	@Column(name = "tajuk")
	private String tajuk;

	@Column(name = "butiran")
	private String butiran;

	@Column(name = "file_name")
	private String fileName;

	@ManyToOne
	@JoinColumn(name = "id_urusetia")
	private Users urusetia;

	@Column(name = "ulasan_urusetia")
	private String ulasanUrusetia;

	@Column(name = "catatan_urusetia")
	private String catatanUrusetia;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_terima_aduan")
	private Date tarikhTerimaAduan;

	@Column(name = "ulasan_selesai")
	private String ulasanSelesai;

	@Column(name = "catatan_selesai")
	private String catatanSelesai;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_selesai")
	private Date tarikhSelesai;

	@Column(name = "flag_agihan")
	private String flagAgihan;

	@Column(name = "keterangan_teknikal")
	private String keteranganTeknikal;

	@Column(name = "email_fm")
	private String emailFm;

	@Column(name = "email_cc")
	private String emailCc;

	@Column(name = "ulasan_unit")
	private String ulasanUnit;

	@Column(name = "catatan_unit")
	private String catatanUnit;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_maklumbalas_unit")
	private Date tarikhMaklumbalasUnit;

	@Column(name = "sequence")
	private int sequence;

	@ManyToOne
	@JoinColumn(name = "id_urusetia_selesai_aduan")
	private Users urusetiaSelesaiAduan;

	@ManyToOne
	@JoinColumn(name = "id_urusetia_tolak_aduan")
	private Users urusetiaTolakAduan;

	@ManyToOne
	@JoinColumn(name = "id_urusetia_maklumbalas_unit")
	private Users urusetiaMaklumbalasUnit;

	@ManyToOne
	@JoinColumn(name = "id_urusetia_fm")
	private Users urusetiaFm;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_maklumbalas_fm")
	private Date tarikhMaklumbalasFm;

	@Column(name = "email_penerima_aduan")
	private String emailPenerimaAduan;

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

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "aduan")
	private List<ProKategoriTeknikal> listKategoriTeknikal;

	public ProAduan() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());
	}

	public String getStatusPeringatan() {

		String statusPeringatan = "";
		Status status = this.status;
		Date tarikhAduan = this.tarikhAduan;
		Date tarikhTerimaAduan = this.tarikhTerimaAduan;
		int bilHari = 0;

		// BARU
		if (("1434787994722").equals(status.getId())) {
			if (tarikhAduan != null && tarikhAduan.toString().length() > 0) {
				Calendar calCurrent = new GregorianCalendar();
				Date dateCurrent = new Date();
				calCurrent.setTime(dateCurrent);

				Calendar calTerimaAduan = new GregorianCalendar();
				Date dateTerimaAduan = tarikhAduan;
				calTerimaAduan.setTime(dateTerimaAduan);

				int diffYear = calTerimaAduan.get(Calendar.YEAR)
						- calCurrent.get(Calendar.YEAR);
				int diffMonth = diffYear * 12
						+ calTerimaAduan.get(Calendar.MONTH)
						- calCurrent.get(Calendar.MONTH);
				bilHari = daysBetween(calTerimaAduan.getTime(),
						calCurrent.getTime());

				if (calCurrent.getTime().after(calTerimaAduan.getTime())) { // BILANGAN
																			// HARI
																			// STATUS
																			// BARU
					statusPeringatan = bilHari + " HARI";
				}
			}
		}

		// DALAM TINDAKAN
		if (("1434787994725").equals(status.getId())) {
			if (tarikhAduan != null && tarikhAduan.toString().length() > 0) {
				Calendar calCurrent = new GregorianCalendar();
				Date dateCurrent = new Date();
				calCurrent.setTime(dateCurrent);

				Calendar calTerimaAduan = new GregorianCalendar();
				Date dateTerimaAduan = tarikhAduan;
				calTerimaAduan.setTime(dateTerimaAduan);

				int diffYear = calTerimaAduan.get(Calendar.YEAR)
						- calCurrent.get(Calendar.YEAR);
				int diffMonth = diffYear * 12
						+ calTerimaAduan.get(Calendar.MONTH)
						- calCurrent.get(Calendar.MONTH);
				bilHari = daysBetween(calTerimaAduan.getTime(),
						calCurrent.getTime());

				if (calCurrent.getTime().after(calTerimaAduan.getTime())
						&& bilHari >= 3) { // BILANGAN HARI STATUS DALAM
											// TINDAKAN
					statusPeringatan = bilHari + " HARI";
				}
			}
		}
		return statusPeringatan;
	}

	private int daysBetween(Date date1, Date date2) {
		return (int) ((date2.getTime() - date1.getTime()) / (1000 * 60 * 60 * 24));
	}

	// GENERATE GETTERS
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public SumberAduan getSumberAduan() {
		return sumberAduan;
	}

	public void setSumberAduan(SumberAduan sumberAduan) {
		this.sumberAduan = sumberAduan;
	}

	public JenisAduan getJenisAduan() {
		return jenisAduan;
	}

	public void setJenisAduan(JenisAduan jenisAduan) {
		this.jenisAduan = jenisAduan;
	}

	public String getNoAduan() {
		return noAduan;
	}

	public void setNoAduan(String noAduan) {
		this.noAduan = noAduan;
	}

	public Date getTarikhAduan() {
		return tarikhAduan;
	}

	public void setTarikhAduan(Date tarikhAduan) {
		this.tarikhAduan = tarikhAduan;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getNoPengenalan() {
		return noPengenalan;
	}

	public void setNoPengenalan(String noPengenalan) {
		this.noPengenalan = noPengenalan;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
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

	public String getEmel() {
		return emel;
	}

	public void setEmel(String emel) {
		this.emel = emel;
	}

	public String getTajuk() {
		return tajuk;
	}

	public void setTajuk(String tajuk) {
		this.tajuk = tajuk;
	}

	public String getButiran() {
		return butiran;
	}

	public void setButiran(String butiran) {
		this.butiran = butiran;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Users getUrusetia() {
		return urusetia;
	}

	public void setUrusetia(Users urusetia) {
		this.urusetia = urusetia;
	}

	public String getUlasanUrusetia() {
		return ulasanUrusetia;
	}

	public void setUlasanUrusetia(String ulasanUrusetia) {
		this.ulasanUrusetia = ulasanUrusetia;
	}

	public String getCatatanUrusetia() {
		return catatanUrusetia;
	}

	public void setCatatanUrusetia(String catatanUrusetia) {
		this.catatanUrusetia = catatanUrusetia;
	}

	public Date getTarikhTerimaAduan() {
		return tarikhTerimaAduan;
	}

	public void setTarikhTerimaAduan(Date tarikhTerimaAduan) {
		this.tarikhTerimaAduan = tarikhTerimaAduan;
	}

	public String getUlasanSelesai() {
		return ulasanSelesai;
	}

	public void setUlasanSelesai(String ulasanSelesai) {
		this.ulasanSelesai = ulasanSelesai;
	}

	public String getCatatanSelesai() {
		return catatanSelesai;
	}

	public void setCatatanSelesai(String catatanSelesai) {
		this.catatanSelesai = catatanSelesai;
	}

	public Date getTarikhSelesai() {
		return tarikhSelesai;
	}

	public void setTarikhSelesai(Date tarikhSelesai) {
		this.tarikhSelesai = tarikhSelesai;
	}

	public String getFlagAgihan() {
		return flagAgihan;
	}

	public void setFlagAgihan(String flagAgihan) {
		this.flagAgihan = flagAgihan;
	}

	public String getKeteranganTeknikal() {
		return keteranganTeknikal;
	}

	public void setKeteranganTeknikal(String keteranganTeknikal) {
		this.keteranganTeknikal = keteranganTeknikal;
	}

	public String getEmailFm() {
		return emailFm;
	}

	public void setEmailFm(String emailFm) {
		this.emailFm = emailFm;
	}

	public String getEmailCc() {
		return emailCc;
	}

	public void setEmailCc(String emailCc) {
		this.emailCc = emailCc;
	}

	public String getUlasanUnit() {
		return ulasanUnit;
	}

	public void setUlasanUnit(String ulasanUnit) {
		this.ulasanUnit = ulasanUnit;
	}

	public String getCatatanUnit() {
		return catatanUnit;
	}

	public void setCatatanUnit(String catatanUnit) {
		this.catatanUnit = catatanUnit;
	}

	public Date getTarikhMaklumbalasUnit() {
		return tarikhMaklumbalasUnit;
	}

	public void setTarikhMaklumbalasUnit(Date tarikhMaklumbalasUnit) {
		this.tarikhMaklumbalasUnit = tarikhMaklumbalasUnit;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public Users getUrusetiaSelesaiAduan() {
		return urusetiaSelesaiAduan;
	}

	public void setUrusetiaSelesaiAduan(Users urusetiaSelesaiAduan) {
		this.urusetiaSelesaiAduan = urusetiaSelesaiAduan;
	}

	public Users getUrusetiaTolakAduan() {
		return urusetiaTolakAduan;
	}

	public void setUrusetiaTolakAduan(Users urusetiaTolakAduan) {
		this.urusetiaTolakAduan = urusetiaTolakAduan;
	}

	public Users getUrusetiaMaklumbalasUnit() {
		return urusetiaMaklumbalasUnit;
	}

	public void setUrusetiaMaklumbalasUnit(Users urusetiaMaklumbalasUnit) {
		this.urusetiaMaklumbalasUnit = urusetiaMaklumbalasUnit;
	}

	public Users getUrusetiaFm() {
		return urusetiaFm;
	}

	public void setUrusetiaFm(Users urusetiaFm) {
		this.urusetiaFm = urusetiaFm;
	}

	public Date getTarikhMaklumbalasFm() {
		return tarikhMaklumbalasFm;
	}

	public void setTarikhMaklumbalasFm(Date tarikhMaklumbalasFm) {
		this.tarikhMaklumbalasFm = tarikhMaklumbalasFm;
	}

	public String getEmailPenerimaAduan() {
		return emailPenerimaAduan;
	}

	public void setEmailPenerimaAduan(String emailPenerimaAduan) {
		this.emailPenerimaAduan = emailPenerimaAduan;
	}

	public List<ProKategoriTeknikal> getListKategoriTeknikal() {
		return listKategoriTeknikal;
	}

	public void setListKategoriTeknikal(
			List<ProKategoriTeknikal> listKategoriTeknikal) {
		this.listKategoriTeknikal = listKategoriTeknikal;
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
