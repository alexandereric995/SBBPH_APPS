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

@Entity
@Table(name = "rk_syarikat")
public class RkSyarikat {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "nama")
	private String nama;

	@Column(name = "id_jenis_pemilikan")
	private String idJenisPemilikan;

	@Column(name = "jenis_pemilikan_lain")
	private String jenisPemilikanLain;

	@Column(name = "taraf_bumiputera")
	private String tarafBumiputera;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_penubuhan")
	private Date tarikhPenubuhan;

	@Column(name = "bidang_syarikat")
	private String bidangSyarikat;

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

	public RkSyarikat() {
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

	public String getIdJenisPemilikan() {
		return idJenisPemilikan;
	}

	public void setIdJenisPemilikan(String idJenisPemilikan) {
		this.idJenisPemilikan = idJenisPemilikan;
	}

	public String getJenisPemilikanLain() {
		return jenisPemilikanLain;
	}

	public void setJenisPemilikanLain(String jenisPemilikanLain) {
		this.jenisPemilikanLain = jenisPemilikanLain;
	}

	public String getTarafBumiputera() {
		return tarafBumiputera;
	}

	public void setTarafBumiputera(String tarafBumiputera) {
		this.tarafBumiputera = tarafBumiputera;
	}

	public Date getTarikhPenubuhan() {
		return tarikhPenubuhan;
	}

	public void setTarikhPenubuhan(Date tarikhPenubuhan) {
		this.tarikhPenubuhan = tarikhPenubuhan;
	}

	public String getBidangSyarikat() {
		return bidangSyarikat;
	}

	public void setBidangSyarikat(String bidangSyarikat) {
		this.bidangSyarikat = bidangSyarikat;
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
