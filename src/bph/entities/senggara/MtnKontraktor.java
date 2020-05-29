package bph.entities.senggara;

import java.util.Date;
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
import bph.entities.kod.Bank;
import bph.entities.kod.LokasiPermohonan;
import db.persistence.MyPersistence;

@Entity
@Table(name = "mtn_kontraktor")
public class MtnKontraktor {

	@Id
	@Column(name = "no_pendaftaran")
	private String id;

	@Column(name = "flag_awam")
	private String flagAwam;

	@Column(name = "flag_elektrik")
	private String flagElektrik;

	@ManyToOne
	@JoinColumn(name = "id_kawasan")
	private LokasiPermohonan kawasan;

	@Column(name = "nama_kontraktor")
	private String namaKontraktor;

	@Column(name = "nama_pemilik")
	private String namaPemilik;

	@Column(name = "alamat1")
	private String alamat1;

	@Column(name = "alamat2")
	private String alamat2;

	@Column(name = "alamat3")
	private String alamat3;

	@Column(name = "poskod")
	private int poskod;

	@ManyToOne
	@JoinColumn(name = "id_bandar")
	private Bandar bandar;

	@Column(name = "no_telefon")
	private String noTelefon;

	@Column(name = "no_telefon_bimbit")
	private String noTelefonBimbit;

	@Column(name = "no_faks")
	private String noFaks;

	@Column(name = "emel")
	private String emel;

	@Column(name = "tarikh_mula_pp")
	@Temporal(TemporalType.DATE)
	private Date tarikhMulaPP;

	@Column(name = "tarikh_tamat_pp")
	@Temporal(TemporalType.DATE)
	private Date tarikhTamatPP;

	@Column(name = "tarikh_mula_spkk")
	@Temporal(TemporalType.DATE)
	private Date tarikhMulaSPKK;

	@Column(name = "tarikh_tamat_spkk")
	@Temporal(TemporalType.DATE)
	private Date tarikhTamatSPKK;

	@Column(name = "tarikh_mula_stb")
	@Temporal(TemporalType.DATE)
	private Date tarikhMulaSTB;

	@Column(name = "tarikh_tamat_stb")
	@Temporal(TemporalType.DATE)
	private Date tarikhTamatSTB;

	@Column(name = "tarikh_mula_st")
	@Temporal(TemporalType.DATE)
	private Date tarikhMulaST;

	@Column(name = "tarikh_tamat_st")
	@Temporal(TemporalType.DATE)
	private Date tarikhTamatST;

	@ManyToOne
	@JoinColumn(name = "id_bank")
	private Bank bank;

	@Column(name = "no_akaun")
	private String noAkaun;

	@Column(name = "filename_profil")
	private String filenameProfil;

	@Column(name = "filename_bank")
	private String filenameBank;

	@Column(name = "filename_pp")
	private String filenamePP;

	@Column(name = "filename_spkk")
	private String filenameSPKK;

	@Column(name = "filename_stb")
	private String filenameSTB;

	@Column(name = "filename_st")
	private String filenameST;

	@Column(name = "catatan")
	private String catatan;

	@Column(name = "no_pendaftaran_ssm")
	private String noPendaftaranSSM;

	@Column(name = "no_pendaftaran_gst")
	private String noPendaftaranGST;

	@Column(name = "gred_prestasi")
	private String gredPrestasi;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "kontraktor")
	private List<MtnDaftarKontraktor> listDaftarKontraktor;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "kontraktor")
	private List<MtnDokumen> listDokumenKontraktor;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "kontraktor")
	private List<MtnPengkhususanBidangKontraktor> listPengkhususanBidangKontraktor;

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

	public MtnKontraktor() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());

	}

	public String getStatusSenaraiHitam() {
		MyPersistence mp = null;
		String statusSenaraiHitam = "T";
		try {
			mp = new MyPersistence();
			MtnKontraktorSenaraiHitam senaraiHitam = (MtnKontraktorSenaraiHitam) mp
					.get("select x from MtnKontraktorSenaraiHitam x where x.flagAktif = 'Y' and x.kontraktor.id = '"
							+ this.getId() + "'");
			if (senaraiHitam != null) {
				statusSenaraiHitam = "Y";
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return statusSenaraiHitam;
	}

	public String getStatusPP() {
		String statusPP = "T";
		if (this.getFilenamePP() != null) {
			if (this.getTarikhTamatPP() != null) {
				if (this.getTarikhTamatPP().after(new Date())) {
					statusPP = "Y";
				}
			}
		}
		return statusPP;
	}

	public String getStatusSPKK() {
		String statusSPKK = "T";
		if (this.getFilenameSPKK() != null) {
			if (this.getTarikhTamatSPKK() != null) {
				if (this.getTarikhTamatSPKK().after(new Date())) {
					statusSPKK = "Y";
				}
			}
		}
		return statusSPKK;
	}

	public String getStatusSTB() {
		String statusSTB = "T";
		if (this.getFilenameSTB() != null) {
			if (this.getTarikhTamatSTB() != null) {
				if (this.getTarikhTamatSTB().after(new Date())) {
					statusSTB = "Y";
				}
			}
		}
		return statusSTB;
	}

	public String getStatusST() {
		String statusST = "T";
		if (this.getFlagElektrik() != null) {
			if (this.getFlagElektrik().equals("Y")) {
				if (this.getFilenameST() != null) {
					if (this.getTarikhTamatST() != null) {
						if (this.getTarikhTamatST().after(new Date())) {
							statusST = "Y";
						}
					}
				}
			} else {
				statusST = "Y";
			}
		} else {
			statusST = "Y";
		}
		return statusST;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFlagAwam() {
		return flagAwam;
	}

	public void setFlagAwam(String flagAwam) {
		this.flagAwam = flagAwam;
	}

	public String getFlagElektrik() {
		return flagElektrik;
	}

	public void setFlagElektrik(String flagElektrik) {
		this.flagElektrik = flagElektrik;
	}

	public LokasiPermohonan getKawasan() {
		return kawasan;
	}

	public void setKawasan(LokasiPermohonan kawasan) {
		this.kawasan = kawasan;
	}

	public String getNamaKontraktor() {
		return namaKontraktor;
	}

	public void setNamaKontraktor(String namaKontraktor) {
		this.namaKontraktor = namaKontraktor;
	}

	public String getNamaPemilik() {
		return namaPemilik;
	}

	public void setNamaPemilik(String namaPemilik) {
		this.namaPemilik = namaPemilik;
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

	public int getPoskod() {
		return poskod;
	}

	public void setPoskod(int poskod) {
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

	public Date getTarikhMulaPP() {
		return tarikhMulaPP;
	}

	public void setTarikhMulaPP(Date tarikhMulaPP) {
		this.tarikhMulaPP = tarikhMulaPP;
	}

	public Date getTarikhTamatPP() {
		return tarikhTamatPP;
	}

	public void setTarikhTamatPP(Date tarikhTamatPP) {
		this.tarikhTamatPP = tarikhTamatPP;
	}

	public Date getTarikhMulaSPKK() {
		return tarikhMulaSPKK;
	}

	public void setTarikhMulaSPKK(Date tarikhMulaSPKK) {
		this.tarikhMulaSPKK = tarikhMulaSPKK;
	}

	public Date getTarikhTamatSPKK() {
		return tarikhTamatSPKK;
	}

	public void setTarikhTamatSPKK(Date tarikhTamatSPKK) {
		this.tarikhTamatSPKK = tarikhTamatSPKK;
	}

	public Date getTarikhMulaSTB() {
		return tarikhMulaSTB;
	}

	public void setTarikhMulaSTB(Date tarikhMulaSTB) {
		this.tarikhMulaSTB = tarikhMulaSTB;
	}

	public Date getTarikhTamatSTB() {
		return tarikhTamatSTB;
	}

	public void setTarikhTamatSTB(Date tarikhTamatSTB) {
		this.tarikhTamatSTB = tarikhTamatSTB;
	}

	public Date getTarikhMulaST() {
		return tarikhMulaST;
	}

	public void setTarikhMulaST(Date tarikhMulaST) {
		this.tarikhMulaST = tarikhMulaST;
	}

	public Date getTarikhTamatST() {
		return tarikhTamatST;
	}

	public void setTarikhTamatST(Date tarikhTamatST) {
		this.tarikhTamatST = tarikhTamatST;
	}

	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	public String getNoAkaun() {
		return noAkaun;
	}

	public void setNoAkaun(String noAkaun) {
		this.noAkaun = noAkaun;
	}

	public String getFilenameProfil() {
		return filenameProfil;
	}

	public void setFilenameProfil(String filenameProfil) {
		this.filenameProfil = filenameProfil;
	}

	public String getFilenameBank() {
		return filenameBank;
	}

	public void setFilenameBank(String filenameBank) {
		this.filenameBank = filenameBank;
	}

	public String getFilenamePP() {
		return filenamePP;
	}

	public void setFilenamePP(String filenamePP) {
		this.filenamePP = filenamePP;
	}

	public String getFilenameSPKK() {
		return filenameSPKK;
	}

	public void setFilenameSPKK(String filenameSPKK) {
		this.filenameSPKK = filenameSPKK;
	}

	public String getFilenameSTB() {
		return filenameSTB;
	}

	public void setFilenameSTB(String filenameSTB) {
		this.filenameSTB = filenameSTB;
	}

	public String getFilenameST() {
		return filenameST;
	}

	public void setFilenameST(String filenameST) {
		this.filenameST = filenameST;
	}

	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}

	public String getNoPendaftaranSSM() {
		return noPendaftaranSSM;
	}

	public void setNoPendaftaranSSM(String noPendaftaranSSM) {
		this.noPendaftaranSSM = noPendaftaranSSM;
	}

	public String getNoPendaftaranGST() {
		return noPendaftaranGST;
	}

	public void setNoPendaftaranGST(String noPendaftaranGST) {
		this.noPendaftaranGST = noPendaftaranGST;
	}

	public String getGredPrestasi() {
		return gredPrestasi;
	}

	public void setGredPrestasi(String gredPrestasi) {
		this.gredPrestasi = gredPrestasi;
	}

	public List<MtnDaftarKontraktor> getListDaftarKontraktor() {
		return listDaftarKontraktor;
	}

	public void setListDaftarKontraktor(
			List<MtnDaftarKontraktor> listDaftarKontraktor) {
		this.listDaftarKontraktor = listDaftarKontraktor;
	}

	public List<MtnDokumen> getListDokumenKontraktor() {
		return listDokumenKontraktor;
	}

	public void setListDokumenKontraktor(List<MtnDokumen> listDokumenKontraktor) {
		this.listDokumenKontraktor = listDokumenKontraktor;
	}

	public List<MtnPengkhususanBidangKontraktor> getListPengkhususanBidangKontraktor() {
		return listPengkhususanBidangKontraktor;
	}

	public void setListPengkhususanBidangKontraktor(
			List<MtnPengkhususanBidangKontraktor> listPengkhususanBidangKontraktor) {
		this.listPengkhususanBidangKontraktor = listPengkhususanBidangKontraktor;
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
