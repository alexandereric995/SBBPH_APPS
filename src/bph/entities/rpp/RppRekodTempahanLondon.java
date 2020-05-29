package bph.entities.rpp;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lebah.template.UID;
import portal.module.entity.Users;
import bph.entities.kewangan.KewBayaranResit;
import bph.entities.kod.JenisUnitRPP;
import bph.entities.kod.Status;

@Entity
@Table(name = "rpp_rekod_tempahan_london")
public class RppRekodTempahanLondon {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "no_tempahan")
	private String noTempahan;

	@ManyToOne
	@JoinColumn(name = "id_jenis_unit_rpp")
	private JenisUnitRPP jenisUnitRpp;

	@ManyToOne
	@JoinColumn(name = "id_pemohon")
	private Users pemohon;

	@Column(name = "tarikh_masuk_rpp")
	@Temporal(TemporalType.DATE)
	private Date tarikhMasukRpp;

	@Column(name = "tarikh_keluar_rpp")
	@Temporal(TemporalType.DATE)
	private Date tarikhKeluarRpp;

	@Column(name = "tarikh_daftar_rekod")
	@Temporal(TemporalType.DATE)
	private Date tarikhDaftarRekod;

	@ManyToOne
	@JoinColumn(name = "id_status")
	// 1425259713412 Status permohonan baru, 1430809277099 permohonan tidak
	// diluluskan, 1430809277102 permohonan diluluskan
	private Status status;

	@Column(name = "catatan_hq")
	private String catatanHq;

	@Column(name = "debit")
	private Double debit;

	@Column(name = "kredit")
	private Double kredit;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_transaksi")
	private Date tarikhTransaksi;

	@Column(name = "flag_bayar")
	private String flagBayar; // Y = TELAH BAYAR / T = BELUM

	/** BAGI PEMOHON YANG TIADA DALAM DB. * */

	@Column(name = "nama_pemohon")
	private String namaPemohon;

	@Column(name = "no_kp")
	private String noKp;

	@Column(name = "no_telefon_pejabat")
	private String noTelefonPejabat;

	@Column(name = "no_telefon_bimbit")
	private String noTelefonBimbit;

	@Column(name = "no_faks")
	private String noFaks;

	@Column(name = "emel")
	private String emel;

	@Column(name = "jawatan_gred")
	private String jawatanGred;

	@Column(name = "kementerian_jabatan")
	private String kementerianJabatan;

	@Column(name = "alamat_pejabat_1")
	private String alamatPejabat1;

	@Column(name = "alamat_pejabat_2")
	private String alamatPejabat2;

	@Column(name = "alamat_pejabat_3")
	private String alamatPejabat3;

	@Column(name = "flag_kelulusan_pmo")
	private String flagKelulusanPmo;

	// Direct get maklumat resit sewa & deposit.
	// One to one sebab bayar sekali shj
	@OneToOne
	@JoinColumn(name = "id_resit_sewa")
	private KewBayaranResit resitSewa;

	/**
	 * TAK PAKAI LAGI
	 * 
	 * @OneToOne
	 * @JoinColumn(name = "id_resit_deposit") private KewBayaranResit
	 *                  resitDeposit;
	 */

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

	public RppRekodTempahanLondon() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public KewBayaranResit getResitSewa() {
		return resitSewa;
	}

	public void setResitSewa(KewBayaranResit resitSewa) {
		this.resitSewa = resitSewa;
	}

	public JenisUnitRPP getJenisUnitRpp() {
		return jenisUnitRpp;
	}

	public void setJenisUnitRpp(JenisUnitRPP jenisUnitRpp) {
		this.jenisUnitRpp = jenisUnitRpp;
	}

	public Users getPemohon() {
		return pemohon;
	}

	public void setPemohon(Users pemohon) {
		this.pemohon = pemohon;
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

	public Date getTarikhDaftarRekod() {
		return tarikhDaftarRekod;
	}

	public void setTarikhDaftarRekod(Date tarikhDaftarRekod) {
		this.tarikhDaftarRekod = tarikhDaftarRekod;
	}

	public String getNamaPemohon() {
		return namaPemohon;
	}

	public void setNamaPemohon(String namaPemohon) {
		this.namaPemohon = namaPemohon;
	}

	public String getNoTelefonPejabat() {
		return noTelefonPejabat;
	}

	public void setNoTelefonPejabat(String noTelefonPejabat) {
		this.noTelefonPejabat = noTelefonPejabat;
	}

	public String getNoTelefonBimbit() {
		return noTelefonBimbit;
	}

	public void setNoTelefonBimbit(String noTelefonBimbit) {
		this.noTelefonBimbit = noTelefonBimbit;
	}

	public String getNoFaks() {
		return noFaks;
	}

	public void setNoFaks(String noFaks) {
		this.noFaks = noFaks;
	}

	public String getEmel() {
		return emel;
	}

	public void setEmel(String emel) {
		this.emel = emel;
	}

	public String getJawatanGred() {
		return jawatanGred;
	}

	public void setJawatanGred(String jawatanGred) {
		this.jawatanGred = jawatanGred;
	}

	public String getKementerianJabatan() {
		return kementerianJabatan;
	}

	public void setKementerianJabatan(String kementerianJabatan) {
		this.kementerianJabatan = kementerianJabatan;
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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getCatatanHq() {
		return catatanHq;
	}

	public void setCatatanHq(String catatanHq) {
		this.catatanHq = catatanHq;
	}

	public Double getDebit() {
		return debit;
	}

	public void setDebit(Double debit) {
		this.debit = debit;
	}

	public Double getKredit() {
		return kredit;
	}

	public void setKredit(Double kredit) {
		this.kredit = kredit;
	}

	public Date getTarikhTransaksi() {
		return tarikhTransaksi;
	}

	public void setTarikhTransaksi(Date tarikhTransaksi) {
		this.tarikhTransaksi = tarikhTransaksi;
	}

	public String getFlagBayar() {
		return flagBayar;
	}

	public void setFlagBayar(String flagBayar) {
		this.flagBayar = flagBayar;
	}

	public String getNoKp() {
		return noKp;
	}

	public void setNoKp(String noKp) {
		this.noKp = noKp;
	}

	public String getNoTempahan() {
		return noTempahan;
	}

	public void setNoTempahan(String noTempahan) {
		this.noTempahan = noTempahan;
	}

	public Integer getTotalBilMalam() {
		Integer days = 0;
		if (this.tarikhKeluarRpp != null && this.tarikhMasukRpp != null) {
			days = (int) ((tarikhKeluarRpp.getTime() - tarikhMasukRpp.getTime()) / (1000 * 60 * 60 * 24));
		}
		return days;
	}

	public Map<String, Object> getMaklumatPemohon() {

		Map<String, Object> map = new HashMap<String, Object>();

		String nokp = "";
		String nama = "";
		String noTelPejabat = "";
		String noTelBimbit = "";
		String emel = "";
		String noFaks = "";
		String jawatanDanGred = "";
		String kementerianJabatan = "";
		String alamatPejabat1 = "";
		String alamatPejabat2 = "";
		String alamatPejabat3 = "";

		if (this.pemohon != null) {
			nokp = this.pemohon.getNoKP() != null ? this.pemohon.getNoKP() : "";
			nama = this.pemohon.getUserName() != null ? this.pemohon
					.getUserName() : "";
			noTelPejabat = this.pemohon.getNoTelefonPejabat() != null ? this.pemohon
					.getNoTelefonPejabat() : "";
			noTelBimbit = this.pemohon.getNoTelefonBimbit() != null ? this.pemohon
					.getNoTelefonBimbit() : "";
			emel = this.pemohon.getEmel() != null ? this.pemohon.getEmel() : "";
			noFaks = this.pemohon.getNoFaks() != null ? this.pemohon
					.getNoFaks() : "";
			jawatanDanGred = this.pemohon.getGredPerkhidmatan() != null ? this.pemohon
					.getGredPerkhidmatan().getKeterangan() : "";
			kementerianJabatan = this.pemohon.getAgensi() != null ? this.pemohon
					.getAgensi().getKeterangan() : "";
			alamatPejabat1 = this.pemohon.getAlamat1() != null ? this.pemohon
					.getAlamat1() : "";
			alamatPejabat2 = this.pemohon.getAlamat2() != null ? this.pemohon
					.getAlamat2() : "";
			alamatPejabat3 = this.pemohon.getAlamat3() != null ? this.pemohon
					.getAlamat3() : "";
		} else {
			nokp = this.getNoKp();
			nama = this.getNamaPemohon();
			noTelPejabat = this.getNoTelefonPejabat();
			noTelBimbit = this.getNoTelefonBimbit();
			emel = this.getEmel();
			noFaks = this.getNoFaks();
			jawatanDanGred = this.getJawatanGred();
			kementerianJabatan = this.getKementerianJabatan();
			alamatPejabat1 = this.getAlamatPejabat1();
			alamatPejabat2 = this.getAlamatPejabat2();
			alamatPejabat3 = this.getAlamatPejabat3();
		}

		map.put("nokp", nokp);
		map.put("nama", nama);
		map.put("noTelPejabat", noTelPejabat);
		map.put("noTelBimbit", noTelBimbit);
		map.put("emel", emel);
		map.put("noFaks", noFaks);
		map.put("jawatanDanGred", jawatanDanGred);
		map.put("kementerianJabatan", kementerianJabatan);
		map.put("alamatPejabat1", alamatPejabat1);
		map.put("alamatPejabat2", alamatPejabat2);
		map.put("alamatPejabat3", alamatPejabat3);

		return map;
	}

	public String keteranganStatusBayaran() {
		String status = "";
		if (this.flagBayar != null && this.flagBayar.equalsIgnoreCase("Y")) {
			status = "TELAH BAYAR";
		} else {
			status = "BELUM BAYAR";
		}
		return status;
	}

	public String getFlagKelulusanPmo() {
		return flagKelulusanPmo;
	}

	public void setFlagKelulusanPmo(String flagKelulusanPmo) {
		this.flagKelulusanPmo = flagKelulusanPmo;
	}

	public String keteranganKelulusanPmo() {
		String status = "";
		if (this.flagKelulusanPmo != null
				&& this.flagKelulusanPmo.equalsIgnoreCase("Y")) {
			status = "DILULUSKAN PMO";
		} else if (this.flagKelulusanPmo != null
				&& this.flagKelulusanPmo.equalsIgnoreCase("T")) {
			status = "TIDAK DILULUSKAN PMO";
		}
		return status;
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
