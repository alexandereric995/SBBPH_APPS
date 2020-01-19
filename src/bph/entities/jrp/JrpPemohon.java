package bph.entities.jrp;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import bph.entities.kod.Agensi;
import bph.entities.kod.Bandar;

@Entity
@Table(name = "jrp_pemohon")
public class JrpPemohon {

	@Id
	@Column(name = "id")
	private String id;
	
	@Column(name = "nama_pegawai")
	private String namaPegawai;
	
	@ManyToOne
	@JoinColumn(name = "id_agensi")
	private Agensi agensi;
	
	@Column(name = "nama_jabatan")
	private String namaJabatan;
	
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
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_daftar")
	private Date tarikhDaftar;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_luput")
	private Date tarikhLuput;
	
	@Column(name = "flag_aktif")
	private String flagAktif;

	@Column(name = "flag_hq")
	private String flagHQ;
	
	public JrpPemohon() {
		setTarikhDaftar(new Date());
	}
	
	public String getKeteranganFlagAktif() {
		String status = "";
		if (this.flagAktif != null) {
			if (this.flagAktif.equals("Y")) {
				status = "AKTIF";
			} else if (this.flagAktif.equals("T")) {
				status = "TIDAK AKTIF";
			}
		}
		return status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNamaPegawai() {
		return namaPegawai;
	}

	public void setNamaPegawai(String namaPegawai) {
		this.namaPegawai = namaPegawai;
	}

	public Agensi getAgensi() {
		return agensi;
	}

	public void setAgensi(Agensi agensi) {
		this.agensi = agensi;
	}

	public String getNamaJabatan() {
		return namaJabatan;
	}

	public void setNamaJabatan(String namaJabatan) {
		this.namaJabatan = namaJabatan;
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

	public Date getTarikhDaftar() {
		return tarikhDaftar;
	}

	public void setTarikhDaftar(Date tarikhDaftar) {
		this.tarikhDaftar = tarikhDaftar;
	}

	public Date getTarikhLuput() {
		return tarikhLuput;
	}

	public void setTarikhLuput(Date tarikhLuput) {
		this.tarikhLuput = tarikhLuput;
	}

	public String getFlagAktif() {
		return flagAktif;
	}

	public void setFlagAktif(String flagAktif) {
		this.flagAktif = flagAktif;
	}

	public String getFlagHQ() {
		return flagHQ;
	}

	public void setFlagHQ(String flagHQ) {
		this.flagHQ = flagHQ;
	}
}
