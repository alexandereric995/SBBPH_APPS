package bph.entities.kewangan;

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
import bph.entities.kew.KewJenisBayaran;
import bph.entities.kod.KodHasil;
import bph.entities.rk.RkFail;
import db.persistence.MyPersistence;

@Entity
@Table(name = "kew_invois")
public class KewInvois {
	
	private static String idJenisBayaranSewaKuarters = "01";
	private static String idJenisBayaranSewaRPP = "02";
	private static String idJenisBayaranSewaGelanggang = "03";
	private static String idJenisBayaranSewaRuangKomersil = "04";
	private static String idJenisBayaranSewaRPLondon = "12";

	@Id
	@Column(name = "id")
	private String id;
	
	/**Generated by stakeholder*/
	@ManyToOne
	@JoinColumn(name = "id_kod_hasil")
	private KodHasil kodHasil;
	
	/**Generated by stakeholder*/
	@Column(name = "flag_bayaran")
	private String flagBayaran; /* SEWA; DEPOSIT*/
	
	/**Generated by stakeholder*/
	@Column(name = "no_invois")
	private String noInvois;
	
	/**Generated by stakeholder*/
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_invois")
	private Date tarikhInvois = new Date();
	
	/**Generated by stakeholder*/
	@Column(name = "no_rujukan")
	private String noRujukan;
	
	/**ID lejar dari stakeholder*/
	@Column(name = "id_lejar")
	private String idLejar;
	
	/**ID pembayar dari stakeholder*/
	@ManyToOne
	@JoinColumn(name = "id_pembayar")
	private Users pembayar;
	
	/**Jenis Bayaran (kod modul)*/
	@ManyToOne
	@JoinColumn(name = "id_jenis_bayaran")
	private KewJenisBayaran jenisBayaran;
	
	/**Generated by stakeholder*/
	@Column(name = "keterangan_bayaran")
	private String keteranganBayaran;
	
	/**Generated by stakeholder*/
	@Column(name = "debit")
	private Double debit = 0d;
	@Column(name = "kredit")
	private Double kredit = 0d;
	
	@Column(name = "flag_bayar")
	private String flagBayar = "T"; // Y : TELAH BAYAR ; T : BELUM BAYAR ; BTL : BATAL
	
	@Column(name = "flag_queue")
	private String flagQueue = "T"; // Y: IN QUEUE ; T : NO
	
	/**Auditrail*/
	@ManyToOne
	@JoinColumn(name = "id_pendaftar")
	private Users userPendaftar;

	@ManyToOne
	@JoinColumn(name = "id_kemaskini")
	private Users userKemaskini;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_daftar")
	private Date tarikhDaftar = new Date();

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_kemaskini")
	private Date tarikhKemaskini;
	
	@Column(name = "catatan_invois")
	private String catatanInvois;
	
	@Column(name = "amaun_pelarasan")
	private Double amaunPelarasan = 0d;
	
	@Column(name = "catatan_pelarasan")
	private String catatanPelarasan;
	
	//dari tarikhmasuk rpp
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_dari")
	private Date tarikhDari;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_hingga")
	private Date tarikhHingga;
	
	@ManyToOne
	@JoinColumn(name = "id_pembayar_lain")
	private PembayarLain pembayarLain;
	
	@Column(name = "flag_jenis_pembayar_lain")
	private String flagJenisPembayarLain; // INDIVIDU / SYARIKAT
	
	public KewInvois() {
		setId(UID.getUID());
	}
	
	public Double tunggakanLebihan(){
		Double baki = 0d;
		if(this.debit != null && this.kredit != null){
			if(this.amaunPelarasan != null && this.amaunPelarasan != 0d){
				baki = this.amaunPelarasan - this.debit;
			} else {
				baki = this.kredit - this.debit;
			}
			
		}
		return baki;
	}
	
	public RkFail getFailRuangKomersil() {
		MyPersistence mp = null;
		RkFail fail = null;
		try {
			mp = new MyPersistence();
			if (this.idLejar != null) {
				fail = (RkFail) mp.find(RkFail.class, this.idLejar);
			}
		} catch (Exception ex){
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		return fail;
	}

	public static String getIdJenisBayaranSewaKuarters() {
		return idJenisBayaranSewaKuarters;
	}

	public static void setIdJenisBayaranSewaKuarters(
			String idJenisBayaranSewaKuarters) {
		KewInvois.idJenisBayaranSewaKuarters = idJenisBayaranSewaKuarters;
	}

	public static String getIdJenisBayaranSewaRPP() {
		return idJenisBayaranSewaRPP;
	}

	public static void setIdJenisBayaranSewaRPP(String idJenisBayaranSewaRPP) {
		KewInvois.idJenisBayaranSewaRPP = idJenisBayaranSewaRPP;
	}

	public static String getIdJenisBayaranSewaGelanggang() {
		return idJenisBayaranSewaGelanggang;
	}

	public static void setIdJenisBayaranSewaGelanggang(
			String idJenisBayaranSewaGelanggang) {
		KewInvois.idJenisBayaranSewaGelanggang = idJenisBayaranSewaGelanggang;
	}

	public static String getIdJenisBayaranSewaRuangKomersil() {
		return idJenisBayaranSewaRuangKomersil;
	}

	public static void setIdJenisBayaranSewaRuangKomersil(
			String idJenisBayaranSewaRuangKomersil) {
		KewInvois.idJenisBayaranSewaRuangKomersil = idJenisBayaranSewaRuangKomersil;
	}

	public static String getIdJenisBayaranSewaRPLondon() {
		return idJenisBayaranSewaRPLondon;
	}

	public static void setIdJenisBayaranSewaRPLondon(
			String idJenisBayaranSewaRPLondon) {
		KewInvois.idJenisBayaranSewaRPLondon = idJenisBayaranSewaRPLondon;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public KodHasil getKodHasil() {
		return kodHasil;
	}

	public void setKodHasil(KodHasil kodHasil) {
		this.kodHasil = kodHasil;
	}

	public String getFlagBayaran() {
		return flagBayaran;
	}

	public void setFlagBayaran(String flagBayaran) {
		this.flagBayaran = flagBayaran;
	}

	public String getNoInvois() {
		return noInvois;
	}

	public void setNoInvois(String noInvois) {
		this.noInvois = noInvois;
	}

	public Date getTarikhInvois() {
		return tarikhInvois;
	}

	public void setTarikhInvois(Date tarikhInvois) {
		this.tarikhInvois = tarikhInvois;
	}

	public String getNoRujukan() {
		return noRujukan;
	}

	public void setNoRujukan(String noRujukan) {
		this.noRujukan = noRujukan;
	}

	public String getIdLejar() {
		return idLejar;
	}

	public void setIdLejar(String idLejar) {
		this.idLejar = idLejar;
	}

	public Users getPembayar() {
		return pembayar;
	}

	public void setPembayar(Users pembayar) {
		this.pembayar = pembayar;
	}

	public KewJenisBayaran getJenisBayaran() {
		return jenisBayaran;
	}

	public void setJenisBayaran(KewJenisBayaran jenisBayaran) {
		this.jenisBayaran = jenisBayaran;
	}

	public String getKeteranganBayaran() {
		return keteranganBayaran;
	}

	public void setKeteranganBayaran(String keteranganBayaran) {
		this.keteranganBayaran = keteranganBayaran;
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

	public String getFlagBayar() {
		return flagBayar;
	}

	public void setFlagBayar(String flagBayar) {
		this.flagBayar = flagBayar;
	}

	public String getFlagQueue() {
		return flagQueue;
	}

	public void setFlagQueue(String flagQueue) {
		this.flagQueue = flagQueue;
	}

	public Users getUserPendaftar() {
		return userPendaftar;
	}

	public void setUserPendaftar(Users userPendaftar) {
		this.userPendaftar = userPendaftar;
	}

	public Users getUserKemaskini() {
		return userKemaskini;
	}

	public void setUserKemaskini(Users userKemaskini) {
		this.userKemaskini = userKemaskini;
	}

	public Date getTarikhDaftar() {
		return tarikhDaftar;
	}

	public void setTarikhDaftar(Date tarikhDaftar) {
		this.tarikhDaftar = tarikhDaftar;
	}

	public Date getTarikhKemaskini() {
		return tarikhKemaskini;
	}

	public void setTarikhKemaskini(Date tarikhKemaskini) {
		this.tarikhKemaskini = tarikhKemaskini;
	}

	public String getCatatanInvois() {
		return catatanInvois;
	}

	public void setCatatanInvois(String catatanInvois) {
		this.catatanInvois = catatanInvois;
	}

	public Double getAmaunPelarasan() {
		return amaunPelarasan;
	}

	public void setAmaunPelarasan(Double amaunPelarasan) {
		this.amaunPelarasan = amaunPelarasan;
	}

	public String getCatatanPelarasan() {
		return catatanPelarasan;
	}

	public void setCatatanPelarasan(String catatanPelarasan) {
		this.catatanPelarasan = catatanPelarasan;
	}

	public Date getTarikhDari() {
		return tarikhDari;
	}

	public void setTarikhDari(Date tarikhDari) {
		this.tarikhDari = tarikhDari;
	}

	public Date getTarikhHingga() {
		return tarikhHingga;
	}

	public void setTarikhHingga(Date tarikhHingga) {
		this.tarikhHingga = tarikhHingga;
	}

	public PembayarLain getPembayarLain() {
		return pembayarLain;
	}

	public void setPembayarLain(PembayarLain pembayarLain) {
		this.pembayarLain = pembayarLain;
	}

	public String getFlagJenisPembayarLain() {
		return flagJenisPembayarLain;
	}

	public void setFlagJenisPembayarLain(String flagJenisPembayarLain) {
		this.flagJenisPembayarLain = flagJenisPembayarLain;
	}
}