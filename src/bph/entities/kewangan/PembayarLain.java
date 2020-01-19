package bph.entities.kewangan;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import bph.entities.kod.Bandar;
import bph.entities.kod.Bank;

@Entity
@Table(name = "pembayar_lain")
public class PembayarLain {
	  
	@Id
	@Column(name = "id")
	private String id;
	
	@Column(name = "nama")
	private String nama;
	
	@Column(name = "nama_pemilik")
	private String namaPemilik;
	
	@Column(name = "no_kp")
	private String noKp;
	
	@Column(name = "alamat1")
	private String alamat1;
	
	@Column(name = "alamat2")
	private String alamat2;
	
	@Column(name = "alamat3")
	private String alamat3;
	
	@Column(name = "poskod")
	private String poskod;
	
	@ManyToOne
	@JoinColumn(name="id_bandar")
	private Bandar bandar;
	
	@Column(name = "no_telefon")
	private String noTelefon;
	
	@Column(name = "no_telefon_bimbit")
	private String noTelefonBimbit;
	
	@Column(name = "no_faks")
	private String noFaks;
	
	@Column(name = "emel")
	private String emel;
	
	@ManyToOne
	@JoinColumn(name="id_bank")
	private Bank bank;
	
	@Column(name="no_akaun")
	private String noAkaun;
	
	@Column(name="flag_jenis_pembayar_lain")
	private String flagJenisPembayarLain;

	
//	public PembayarSyarikat() {
//		setId(UID.getUID());
//	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getNamaPemilik() {
		return namaPemilik;
	}

	public void setNamaPemilik(String namaPemilik) {
		this.namaPemilik = namaPemilik;
	}

	public String getNoKp() {
		return noKp;
	}

	public void setNoKp(String noKp) {
		this.noKp = noKp;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getFlagJenisPembayarLain() {
		return flagJenisPembayarLain;
	}

	public void setFlagJenisPembayarLain(String flagJenisPembayarLain) {
		this.flagJenisPembayarLain = flagJenisPembayarLain;
	}

}