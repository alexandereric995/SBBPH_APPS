package bph.entities.kontrak;

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


@Entity
@Table(name = "kontrak_maklumat_pembayaran")
public class KontrakMaklumatBayaran {

	@Id
	@Column(name = "id")
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "id_kontrak")
	private KontrakKontrak kontrak;
	
	@Column(name = "tarikh_transaksi")
	@Temporal(TemporalType.DATE)
	private Date tarikhTransaksi;
	
	@Column(name = "tarikh_invois")
	@Temporal(TemporalType.DATE)
	private Date tarikhInvois;
		
	@Column(name = "no_invois")
	private String noInvois;
	
	@Column(name = "keterangan")
	private String keterangan;
	
	@Column(name = "tarikh_baucer")
	@Temporal(TemporalType.DATE)
	private Date tarikhBaucer;
	
	@Column(name = "no_baucer")
	private String noBaucer;
	
	@Column(name = "tarikh_luput")
	@Temporal(TemporalType.DATE)
	private Date tarikhLuput;
	
	@Column(name = "debit")
	private double debit;
	
	@Column(name = "kredit")
	private double kredit;
	
	@Column(name = "jumlah")
	private double jumlah;
	
	@Column(name = "tarikh_resit")
	@Temporal(TemporalType.DATE)
	private Date tarikhResit;
		
	@Column(name = "no_resit")
	private String noResit;

	@Column(name = "flag_pelarasan")
	private String flagPelarasan;
	
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
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_bayaran")
	private Date tarikhBayaran;
	
	@ManyToOne
	@JoinColumn(name = "user_transaksi_kewangan_id")
	private Users userTransaksiKewanganId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "user_transaksi_kewangan_tarikh")
	private Date userTransaksiKewanganTarikh;

	@Column(name = "catatan_amaun_potongan_denda")
	private String catatanAmaunPotonganDenda;
	
	@Column(name = "catatan_amaun_potongan_cdc")
	private String catatanAmaunPotonganCdc;
	
	public KontrakMaklumatBayaran() {
		setId(UID.getUID());
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public KontrakKontrak getKontrak() {
		return kontrak;
	}


	public void setKontrak(KontrakKontrak kontrak) {
		this.kontrak = kontrak;
	}


	public Date getTarikhTransaksi() {
		return tarikhTransaksi;
	}


	public void setTarikhTransaksi(Date tarikhTransaksi) {
		this.tarikhTransaksi = tarikhTransaksi;
	}


	public Date getTarikhInvois() {
		return tarikhInvois;
	}


	public void setTarikhInvois(Date tarikhInvois) {
		this.tarikhInvois = tarikhInvois;
	}


	public String getNoInvois() {
		return noInvois;
	}


	public void setNoInvois(String noInvois) {
		this.noInvois = noInvois;
	}


	public String getKeterangan() {
		return keterangan;
	}


	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}


	public Date getTarikhBaucer() {
		return tarikhBaucer;
	}


	public void setTarikhBaucer(Date tarikhBaucer) {
		this.tarikhBaucer = tarikhBaucer;
	}


	public String getNoBaucer() {
		return noBaucer;
	}


	public void setNoBaucer(String noBaucer) {
		this.noBaucer = noBaucer;
	}


	public Date getTarikhLuput() {
		return tarikhLuput;
	}


	public void setTarikhLuput(Date tarikhLuput) {
		this.tarikhLuput = tarikhLuput;
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


	public double getJumlah() {
		return jumlah;
	}


	public void setJumlah(double jumlah) {
		this.jumlah = jumlah;
	}


	public Date getTarikhResit() {
		return tarikhResit;
	}


	public void setTarikhResit(Date tarikhResit) {
		this.tarikhResit = tarikhResit;
	}


	public String getNoResit() {
		return noResit;
	}


	public void setNoResit(String noResit) {
		this.noResit = noResit;
	}


	public String getFlagPelarasan() {
		return flagPelarasan;
	}


	public void setFlagPelarasan(String flagPelarasan) {
		this.flagPelarasan = flagPelarasan;
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


	public String getStatusBayaran() {
		return statusBayaran;
	}


	public void setStatusBayaran(String statusBayaran) {
		this.statusBayaran = statusBayaran;
	}


	public Date getTarikhBayaran() {
		return tarikhBayaran;
	}


	public void setTarikhBayaran(Date tarikhBayaran) {
		this.tarikhBayaran = tarikhBayaran;
	}

	public Date getUserTransaksiKewanganTarikh() {
		return userTransaksiKewanganTarikh;
	}
	
	public void setUserTransaksiKewanganTarikh(Date userTransaksiKewanganTarikh) {
		this.userTransaksiKewanganTarikh = userTransaksiKewanganTarikh;
	}

	public Users getUserTransaksiKewanganId() {
		return userTransaksiKewanganId;
	}

	public void setUserTransaksiKewanganId(Users userTransaksiKewanganId) {
		this.userTransaksiKewanganId = userTransaksiKewanganId;
	}

	public String getCatatanAmaunPotonganDenda() {
		return catatanAmaunPotonganDenda;
	}

	public void setCatatanAmaunPotonganDenda(String catatanAmaunPotonganDenda) {
		this.catatanAmaunPotonganDenda = catatanAmaunPotonganDenda;
	}

	public String getCatatanAmaunPotonganCdc() {
		return catatanAmaunPotonganCdc;
	}

	public void setCatatanAmaunPotonganCdc(String catatanAmaunPotonganCdc) {
		this.catatanAmaunPotonganCdc = catatanAmaunPotonganCdc;
	}
	
}
