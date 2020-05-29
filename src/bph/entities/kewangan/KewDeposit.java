package bph.entities.kewangan;

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

import lebah.template.DbPersistence;
import lebah.template.UID;
import portal.module.entity.Users;
import bph.entities.kew.KewJenisBayaran;
import bph.entities.kew.KewTuntutanDeposit;
import bph.entities.kod.CaraBayar;
import bph.entities.kod.KodHasil;
import bph.entities.qtr.KuaAkaun;
import bph.entities.qtr.KuaKuarters;
import bph.entities.qtr.KuaPenghuni;
import bph.entities.rpp.RppAkaun;
import bph.entities.rpp.RppPermohonan;

/**
 * @author muhdsyazreen
 * @description : Deposit semua modul yang terlibat
 */

@Entity
@Table(name = "kew_deposit")
public class KewDeposit {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_kod_hasil")
	private KodHasil kodHasil;

	@ManyToOne
	@JoinColumn(name = "id_jenis_bayaran")
	private KewJenisBayaran jenisBayaran;

	@Column(name = "id_lejar")
	private String idLejar;

	@Column(name = "no_invois")
	private String noInvois;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_dari")
	private Date tarikhDari;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_hingga")
	private Date tarikhHingga;

	@Column(name = "keterangan_deposit")
	private String keteranganDeposit;

	@ManyToOne
	@JoinColumn(name = "id_pendeposit")
	private Users pendeposit;

	@Column(name = "jumlah_deposit")
	private Double jumlahDeposit;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_deposit")
	private Date tarikhDeposit;

	@Column(name = "flag_bayar")
	private String flagBayar = "T"; // Y : TELAH BAYAR ; T : BELUM BAYAR ; BTL :
									// BATAL

	@Column(name = "flag_queue")
	private String flagQueue = "T"; // Y: IN QUEUE ; T : NO

	@Column(name = "no_resit")
	private String noResit;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_resit")
	private Date tarikhResit;

	@OneToOne
	@JoinColumn(name = "id_tuntutan_deposit")
	private KewTuntutanDeposit tuntutanDeposit;

	@OneToOne
	@JoinColumn(name = "id_penyedia")
	private Users penyedia; // unit kewangan

	@OneToOne
	@JoinColumn(name = "id_perakuan")
	private Users perakuan; // PENYEDIA DEPOSIT. CTH : PENYELIA IR

	@Column(name = "flag_pulang_deposit")
	private String flagPulangDeposit; // 'Y = TELAH DIPULANGKAN; T = BELUM
										// DIPULANGKAN; BTL = BATAL'

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_pulangan_deposit")
	private Date tarikhPulanganDeposit;

	@Column(name = "catatan_pulangan_deposit")
	private String catatanPulanganDeposit;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_bayaran")
	private Date tarikhBayaran;

	@OneToOne
	@JoinColumn(name = "id_kaedah_bayaran")
	private CaraBayar kaedahBayaran;

	@Column(name = "no_slip_bank")
	private String noSlipBank;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_kutipan_dari")
	private Date tarikhKutipanDari;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_kutipan_hingga")
	private Date tarikhKutipanHingga;

	@Column(name = "no_statement_kutipan")
	private String noStatementKutipan;

	@Column(name = "baki_deposit")
	private Double bakiDeposit;

	@Column(name = "flag_warta")
	private String flagWarta;

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

	public KewDeposit() {
		setId(UID.getUID());
		setFlagBayar("T");
		setFlagQueue("T");
		setFlagPulangDeposit("T");
		setFlagWarta("T");
		setJumlahDeposit(0D);
		setBakiDeposit(0D);
		setTarikhMasuk(new Date());
	}

	@SuppressWarnings("unchecked")
	public List<KewDepositWarta> getSenaraiDepositWarta() {
		DbPersistence db = new DbPersistence();
		List<KewDepositWarta> list = null;
		if (this.id != null) {
			list = db
					.list("select x from KewDepositWarta x where x.deposit.id = '"
							+ this.id + "' ");
		}
		return list;
	}

	public RppPermohonan getMaklumatPermohonanRPP() {
		DbPersistence db = new DbPersistence();
		RppPermohonan r = new RppPermohonan();
		if (this.idLejar != null && this.jenisBayaran != null) {
			if (this.jenisBayaran.getId().equalsIgnoreCase("02")) {
				RppAkaun ak = db.find(RppAkaun.class, this.idLejar);
				if (ak != null) {
					r = ak.getPermohonan();
				}
			}
		}
		return r;
	}

	public KuaKuarters getMaklumatKuarters() {
		DbPersistence db = new DbPersistence();
		KuaKuarters r = new KuaKuarters();
		if (this.idLejar != null && this.jenisBayaran != null) {
			if (this.jenisBayaran.getId().equalsIgnoreCase("01")) {
				KuaAkaun ak = db.find(KuaAkaun.class, this.idLejar);
				if (ak != null) {
					KuaPenghuni p = (KuaPenghuni) db
							.get("select x from KuaPenghuni x where x.pemohon.id = '"
									+ ak.getIdPembayar() + "' ");
					if (p != null) {
						r = p.getKuarters();
					}
				}
			}
		}
		return r;
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

	public KewJenisBayaran getJenisBayaran() {
		return jenisBayaran;
	}

	public void setJenisBayaran(KewJenisBayaran jenisBayaran) {
		this.jenisBayaran = jenisBayaran;
	}

	public String getIdLejar() {
		return idLejar;
	}

	public void setIdLejar(String idLejar) {
		this.idLejar = idLejar;
	}

	public String getNoInvois() {
		return noInvois;
	}

	public void setNoInvois(String noInvois) {
		this.noInvois = noInvois;
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

	public String getKeteranganDeposit() {
		return keteranganDeposit;
	}

	public void setKeteranganDeposit(String keteranganDeposit) {
		this.keteranganDeposit = keteranganDeposit;
	}

	public Users getPendeposit() {
		return pendeposit;
	}

	public void setPendeposit(Users pendeposit) {
		this.pendeposit = pendeposit;
	}

	public Double getJumlahDeposit() {
		return jumlahDeposit;
	}

	public void setJumlahDeposit(Double jumlahDeposit) {
		this.jumlahDeposit = jumlahDeposit;
	}

	public Date getTarikhDeposit() {
		return tarikhDeposit;
	}

	public void setTarikhDeposit(Date tarikhDeposit) {
		this.tarikhDeposit = tarikhDeposit;
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

	public String getNoResit() {
		return noResit;
	}

	public void setNoResit(String noResit) {
		this.noResit = noResit;
	}

	public Date getTarikhResit() {
		return tarikhResit;
	}

	public void setTarikhResit(Date tarikhResit) {
		this.tarikhResit = tarikhResit;
	}

	public KewTuntutanDeposit getTuntutanDeposit() {
		return tuntutanDeposit;
	}

	public void setTuntutanDeposit(KewTuntutanDeposit tuntutanDeposit) {
		this.tuntutanDeposit = tuntutanDeposit;
	}

	public Users getPenyedia() {
		return penyedia;
	}

	public void setPenyedia(Users penyedia) {
		this.penyedia = penyedia;
	}

	public Users getPerakuan() {
		return perakuan;
	}

	public void setPerakuan(Users perakuan) {
		this.perakuan = perakuan;
	}

	public String getFlagPulangDeposit() {
		return flagPulangDeposit;
	}

	public void setFlagPulangDeposit(String flagPulangDeposit) {
		this.flagPulangDeposit = flagPulangDeposit;
	}

	public Date getTarikhPulanganDeposit() {
		return tarikhPulanganDeposit;
	}

	public void setTarikhPulanganDeposit(Date tarikhPulanganDeposit) {
		this.tarikhPulanganDeposit = tarikhPulanganDeposit;
	}

	public String getCatatanPulanganDeposit() {
		return catatanPulanganDeposit;
	}

	public void setCatatanPulanganDeposit(String catatanPulanganDeposit) {
		this.catatanPulanganDeposit = catatanPulanganDeposit;
	}

	public Date getTarikhBayaran() {
		return tarikhBayaran;
	}

	public void setTarikhBayaran(Date tarikhBayaran) {
		this.tarikhBayaran = tarikhBayaran;
	}

	public CaraBayar getKaedahBayaran() {
		return kaedahBayaran;
	}

	public void setKaedahBayaran(CaraBayar kaedahBayaran) {
		this.kaedahBayaran = kaedahBayaran;
	}

	public String getNoSlipBank() {
		return noSlipBank;
	}

	public void setNoSlipBank(String noSlipBank) {
		this.noSlipBank = noSlipBank;
	}

	public Date getTarikhKutipanDari() {
		return tarikhKutipanDari;
	}

	public void setTarikhKutipanDari(Date tarikhKutipanDari) {
		this.tarikhKutipanDari = tarikhKutipanDari;
	}

	public Date getTarikhKutipanHingga() {
		return tarikhKutipanHingga;
	}

	public void setTarikhKutipanHingga(Date tarikhKutipanHingga) {
		this.tarikhKutipanHingga = tarikhKutipanHingga;
	}

	public String getNoStatementKutipan() {
		return noStatementKutipan;
	}

	public void setNoStatementKutipan(String noStatementKutipan) {
		this.noStatementKutipan = noStatementKutipan;
	}

	public Double getBakiDeposit() {
		return bakiDeposit;
	}

	public void setBakiDeposit(Double bakiDeposit) {
		this.bakiDeposit = bakiDeposit;
	}

	public String getFlagWarta() {
		return flagWarta;
	}

	public void setFlagWarta(String flagWarta) {
		this.flagWarta = flagWarta;
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
