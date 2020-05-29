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
import bph.entities.kod.Bank;
import bph.entities.kod.CaraBayar;
import bph.entities.kod.KodMesin;

@Entity
@Table(name = "kew_temp_bayar")
public class KewTempBayar {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_pembayar")
	private Users pembayar;

	@ManyToOne
	@JoinColumn(name = "id_mod_bayaran")
	private CaraBayar modBayaran;

	@ManyToOne
	@JoinColumn(name = "id_bank")
	private Bank bank;

	@Column(name = "tempat")
	private String tempat;

	@Column(name = "no_rujukan")
	private String noRujukan;

	@Column(name = "amaun_bayaran")
	private Double amaunBayaran = 0d;

	@ManyToOne
	@JoinColumn(name = "id_mesin")
	private KodMesin mesin;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_cek")
	private Date tarikhCek;

	@Column(name = "no_cek")
	private String noCek;

	@ManyToOne
	@JoinColumn(name = "id_pembayar_lain")
	private PembayarLain pembayarLain;

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

	public KewTempBayar() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Users getPembayar() {
		return pembayar;
	}

	public void setPembayar(Users pembayar) {
		this.pembayar = pembayar;
	}

	public CaraBayar getModBayaran() {
		return modBayaran;
	}

	public void setModBayaran(CaraBayar modBayaran) {
		this.modBayaran = modBayaran;
	}

	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	public String getTempat() {
		return tempat;
	}

	public void setTempat(String tempat) {
		this.tempat = tempat;
	}

	public String getNoRujukan() {
		return noRujukan;
	}

	public void setNoRujukan(String noRujukan) {
		this.noRujukan = noRujukan;
	}

	public Double getAmaunBayaran() {
		return amaunBayaran;
	}

	public void setAmaunBayaran(Double amaunBayaran) {
		this.amaunBayaran = amaunBayaran;
	}

	public KodMesin getMesin() {
		return mesin;
	}

	public void setMesin(KodMesin mesin) {
		this.mesin = mesin;
	}

	public Date getTarikhCek() {
		return tarikhCek;
	}

	public void setTarikhCek(Date tarikhCek) {
		this.tarikhCek = tarikhCek;
	}

	public String getNoCek() {
		return noCek;
	}

	public void setNoCek(String noCek) {
		this.noCek = noCek;
	}

	public PembayarLain getPembayarLain() {
		return pembayarLain;
	}

	public void setPembayarLain(PembayarLain pembayarLain) {
		this.pembayarLain = pembayarLain;
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
