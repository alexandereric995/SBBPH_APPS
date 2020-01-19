package bph.entities.rk;

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
import bph.entities.kewangan.KewBayaranResit;
import bph.entities.kewangan.KewInvois;
import bph.entities.kod.KodHasil;

@Entity
@Table(name = "rk_akaun")
public class RkAkaun {

	@Id
	@Column(name = "id")
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "id_fail")
	private RkFail fail;
	
	@ManyToOne
	@JoinColumn(name = "id_permohonan")
	private RkPermohonan permohonan;
	
	@Column(name = "tarikh_invois")
	@Temporal(TemporalType.DATE)
	private Date tarikhInvois;
	
	@Column(name = "tarikh_transaksi")
	@Temporal(TemporalType.DATE)
	private Date tarikhTransaksi;
	
	@ManyToOne
	@JoinColumn(name = "id_kod_hasil")
	private KodHasil kodHasil;

	@Column(name = "id_jenis_transaksi")
	private String idJenisTransaksi;
	
	@Column(name = "debit")
	private double debit;
	
	@Column(name = "kredit")
	private double kredit;	
	
	@Column(name = "keterangan")
	private String keterangan;
	
	@ManyToOne
	@JoinColumn(name = "id_resit")
	private KewBayaranResit resit;
	
	@Column(name = "no_rujukan_pelarasan")
	private String noRujukanPelarasan;
	
	@Column(name = "flag_aktif")
	private String flagAktif;
	
	@ManyToOne
	@JoinColumn(name = "id_kew_invois")
	private KewInvois kewInvois;
	
	@ManyToOne
	@JoinColumn(name = "id_invois")
	private RkInvois invois;
	
	@ManyToOne
	@JoinColumn(name = "id_masuk")
	private Users daftarOleh;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="tarikh_masuk")
	private Date tarikhMasuk;
	
	@ManyToOne
	@JoinColumn(name = "id_kemaskini")
	private Users kemaskiniOleh;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_kemaskini")
	private Date tarikhKemaskini;	
	
	public RkAkaun() {
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

	public RkFail getFail() {
		return fail;
	}

	public void setFail(RkFail fail) {
		this.fail = fail;
	}

	public RkPermohonan getPermohonan() {
		return permohonan;
	}

	public void setPermohonan(RkPermohonan permohonan) {
		this.permohonan = permohonan;
	}

	public Date getTarikhInvois() {
		return tarikhInvois;
	}

	public void setTarikhInvois(Date tarikhInvois) {
		this.tarikhInvois = tarikhInvois;
	}

	public Date getTarikhTransaksi() {
		return tarikhTransaksi;
	}

	public void setTarikhTransaksi(Date tarikhTransaksi) {
		this.tarikhTransaksi = tarikhTransaksi;
	}

	public KodHasil getKodHasil() {
		return kodHasil;
	}

	public void setKodHasil(KodHasil kodHasil) {
		this.kodHasil = kodHasil;
	}

	public String getIdJenisTransaksi() {
		return idJenisTransaksi;
	}

	public void setIdJenisTransaksi(String idJenisTransaksi) {
		this.idJenisTransaksi = idJenisTransaksi;
	}

	public double getDebit() {
		return debit;
	}

	public void setDebit(double debit) {
		this.debit = debit;
	}

	public double getKredit() {
		return kredit;
	}

	public void setKredit(double kredit) {
		this.kredit = kredit;
	}

	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}

	public KewBayaranResit getResit() {
		return resit;
	}

	public void setResit(KewBayaranResit resit) {
		this.resit = resit;
	}

	public String getNoRujukanPelarasan() {
		return noRujukanPelarasan;
	}

	public void setNoRujukanPelarasan(String noRujukanPelarasan) {
		this.noRujukanPelarasan = noRujukanPelarasan;
	}

	public String getFlagAktif() {
		return flagAktif;
	}

	public void setFlagAktif(String flagAktif) {
		this.flagAktif = flagAktif;
	}

	public KewInvois getKewInvois() {
		return kewInvois;
	}

	public void setKewInvois(KewInvois kewInvois) {
		this.kewInvois = kewInvois;
	}

	public RkInvois getInvois() {
		return invois;
	}

	public void setInvois(RkInvois invois) {
		this.invois = invois;
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
