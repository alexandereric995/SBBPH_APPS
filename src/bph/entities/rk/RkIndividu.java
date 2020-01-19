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

import portal.module.entity.Users;
import bph.entities.kod.Bandar;
import bph.entities.kod.Warganegara;

@Entity
@Table(name = "rk_individu")
public class RkIndividu {
	
	@Id
	@Column(name = "id")
	private String id;
	
	@Column(name = "nama")
	private String nama;
	
	@Column(name = "jawatan")
	private String jawatan;
	
	@ManyToOne
	@JoinColumn(name = "id_warganegara")
	private Warganegara warganegara;	
	
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
	
	@Column(name = "alamat1_surat")
	private String alamat1Surat;
	
	@Column(name = "alamat2_surat")
	private String alamat2Surat;
	
	@Column(name = "alamat3_surat")
	private String alamat3Surat;
	
	@Column(name = "poskod_surat")
	private String poskodSurat;
	
	@ManyToOne
	@JoinColumn(name = "id_bandar_surat")
	private Bandar bandarSurat;
	
	@Column(name = "no_telefon")
	private String noTelefon;
	
	@Column(name = "no_telefon_bimbit")
	private String noTelefonBimbit;
	
	@Column(name = "no_faks")
	private String noFaks;
	
	@Column(name = "emel")
	private String emel;
	
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
		
	public RkIndividu() {
		setTarikhMasuk(new Date());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getJawatan() {
		return jawatan;
	}

	public void setJawatan(String jawatan) {
		this.jawatan = jawatan;
	}

	public Warganegara getWarganegara() {
		return warganegara;
	}

	public void setWarganegara(Warganegara warganegara) {
		this.warganegara = warganegara;
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

	public String getAlamat1Surat() {
		return alamat1Surat;
	}

	public void setAlamat1Surat(String alamat1Surat) {
		this.alamat1Surat = alamat1Surat;
	}

	public String getAlamat2Surat() {
		return alamat2Surat;
	}

	public void setAlamat2Surat(String alamat2Surat) {
		this.alamat2Surat = alamat2Surat;
	}

	public String getAlamat3Surat() {
		return alamat3Surat;
	}

	public void setAlamat3Surat(String alamat3Surat) {
		this.alamat3Surat = alamat3Surat;
	}

	public String getPoskodSurat() {
		return poskodSurat;
	}

	public void setPoskodSurat(String poskodSurat) {
		this.poskodSurat = poskodSurat;
	}

	public Bandar getBandarSurat() {
		return bandarSurat;
	}

	public void setBandarSurat(Bandar bandarSurat) {
		this.bandarSurat = bandarSurat;
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
