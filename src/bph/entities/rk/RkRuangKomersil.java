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
import bph.entities.kod.Bandar;
import bph.entities.kod.JenisKegunaanRuang;
import bph.entities.kod.Seksyen;

@Entity
@Table(name = "rk_ruang_komersil")
public class RkRuangKomersil {
	
	@Id
	@Column(name = "id")
	private String id;
	
	@Column(name = "id_ruang")
	private String idRuang;
	
	@Column(name = "id_ruang_lama")
	private String idRuangLama;
	
	@Column(name = "nama_ruang")
	private String namaRuang;
	
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
	
	@Column(name = "lokasi")
	private String lokasi;
	
	@ManyToOne
	@JoinColumn(name="id_seksyen")
	private Seksyen seksyen;
	
	@Column(name = "nama_pegawai")
	private String namaPegawai;
	
	@Column(name = "no_telefon_pegawai")
	private String noTelefonPegawai;
	
	@ManyToOne
	@JoinColumn(name = "id_jenis_kegunaan_ruang")
	private JenisKegunaanRuang jenisKegunaanRuang;
	
	@Column(name = "jenis_kegunaan_lain")
	private String jenisKegunaanLain;
	
	@Column(name = "jenis_meter_air")
	private String jenisMeterAir;
	
	@Column(name = "jenis_meter_elektrik")
	private String jenisMeterElektrik;
	
	@Column(name = "jenis_meter_iwk")
	private String jenisMeterIWK;
	
	@Column(name = "id_jenis_sewa")
	private String idJenisSewa;
		
	@Column(name = "kadar_sewa")
	private double kadarSewa;
	
	@Column(name = "luas_mps")
	private double luasMps;
	
	@Column(name = "luas_kps")
	private double luasKps;
	
	@Column(name = "catatan")
	private String catatan;
	
	@Column(name = "flag_sewa")
	private String flagSewa;
	
	@Column(name = "flag_aktif")
	private String flagAktif;
	
	@Column(name = "flag_syspintar")
	private String flagSyspintar;
	
	@Column(name = "turutan")
	private int turutan;
	
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
	@Column(name="tarikh_kemaskini")
	private Date tarikhKemaskini;
	
	public RkRuangKomersil() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());
		setFlagSyspintar("T");
	}
	
	public String getKeteranganFlagSewa(){
		
		String flagSewa = this.flagSewa;
		String keterangan = "";
		
		if("Y".equals(flagSewa)){
			keterangan = "DISEWA";
		} else if("T".equals(flagSewa)){
			keterangan = "KOSONG";
		}
		
		return keterangan;
	}
	
	public String getKeteranganIdJenisSewa(){
		
		String idJenisSewa = this.idJenisSewa;
		String keterangan = "";
		
		if("H".equals(idJenisSewa)){
			keterangan = "HARIAN";
		} else if("B".equals(idJenisSewa)){
			keterangan = "BULANAN";
		} else if("T".equals(idJenisSewa)){
			keterangan = "TAHUNAN";
		}
		
		return keterangan;
	}
	
	public String getKeteranganJenisMeterAir(){
		
		String jenisMeterAir = this.jenisMeterAir;
		String keterangan = "";
		
		if("B".equals(jenisMeterAir)){
			keterangan = "BERASINGAN";
		} else if("P".equals(jenisMeterAir)){
			keterangan = "PUKAL";
		} else if("T".equals(jenisMeterAir)){
			keterangan = "TIADA";
		}
		
		return keterangan;
	}
	
	public String getKeteranganJenisMeterElektrik(){
		
		String jenisMeterElektrik = this.jenisMeterElektrik;
		String keterangan = "";
		
		if("B".equals(jenisMeterElektrik)){
			keterangan = "BERASINGAN";
		} else if("P".equals(jenisMeterElektrik)){
			keterangan = "PUKAL";
		} else if("T".equals(jenisMeterElektrik)){
			keterangan = "TIADA";
		}
		
		return keterangan;
	}
	
	public String getKeteranganJenisMeterIWK(){
		
		String jenismeterIWK = this.jenisMeterIWK;
		String keterangan = "";
		
		if("B".equals(jenismeterIWK)){
			keterangan = "BERASINGAN";
		} else if("P".equals(jenismeterIWK)){
			keterangan = "PUKAL";
		} else if("T".equals(jenismeterIWK)){
			keterangan = "TIADA";
		}
		
		return keterangan;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdRuang() {
		return idRuang;
	}

	public void setIdRuang(String idRuang) {
		this.idRuang = idRuang;
	}

	public String getIdRuangLama() {
		return idRuangLama;
	}

	public void setIdRuangLama(String idRuangLama) {
		this.idRuangLama = idRuangLama;
	}

	public String getNamaRuang() {
		return namaRuang;
	}

	public void setNamaRuang(String namaRuang) {
		this.namaRuang = namaRuang;
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

	public String getLokasi() {
		return lokasi;
	}

	public void setLokasi(String lokasi) {
		this.lokasi = lokasi;
	}

	public Seksyen getSeksyen() {
		return seksyen;
	}

	public void setSeksyen(Seksyen seksyen) {
		this.seksyen = seksyen;
	}

	public String getNamaPegawai() {
		return namaPegawai;
	}

	public void setNamaPegawai(String namaPegawai) {
		this.namaPegawai = namaPegawai;
	}

	public String getNoTelefonPegawai() {
		return noTelefonPegawai;
	}

	public void setNoTelefonPegawai(String noTelefonPegawai) {
		this.noTelefonPegawai = noTelefonPegawai;
	}

	public JenisKegunaanRuang getJenisKegunaanRuang() {
		return jenisKegunaanRuang;
	}

	public void setJenisKegunaanRuang(JenisKegunaanRuang jenisKegunaanRuang) {
		this.jenisKegunaanRuang = jenisKegunaanRuang;
	}

	public String getJenisKegunaanLain() {
		return jenisKegunaanLain;
	}

	public void setJenisKegunaanLain(String jenisKegunaanLain) {
		this.jenisKegunaanLain = jenisKegunaanLain;
	}

	public String getJenisMeterAir() {
		return jenisMeterAir;
	}

	public void setJenisMeterAir(String jenisMeterAir) {
		this.jenisMeterAir = jenisMeterAir;
	}

	public String getJenisMeterElektrik() {
		return jenisMeterElektrik;
	}

	public void setJenisMeterElektrik(String jenisMeterElektrik) {
		this.jenisMeterElektrik = jenisMeterElektrik;
	}

	public String getJenisMeterIWK() {
		return jenisMeterIWK;
	}

	public void setJenisMeterIWK(String jenisMeterIWK) {
		this.jenisMeterIWK = jenisMeterIWK;
	}

	public String getIdJenisSewa() {
		return idJenisSewa;
	}

	public void setIdJenisSewa(String idJenisSewa) {
		this.idJenisSewa = idJenisSewa;
	}

	public double getKadarSewa() {
		return kadarSewa;
	}

	public void setKadarSewa(double kadarSewa) {
		this.kadarSewa = kadarSewa;
	}

	public double getLuasMps() {
		return luasMps;
	}

	public void setLuasMps(double luasMps) {
		this.luasMps = luasMps;
	}

	public double getLuasKps() {
		return luasKps;
	}

	public void setLuasKps(double luasKps) {
		this.luasKps = luasKps;
	}

	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}

	public String getFlagSewa() {
		return flagSewa;
	}

	public void setFlagSewa(String flagSewa) {
		this.flagSewa = flagSewa;
	}

	public String getFlagAktif() {
		return flagAktif;
	}

	public void setFlagAktif(String flagAktif) {
		this.flagAktif = flagAktif;
	}

	public String getFlagSyspintar() {
		return flagSyspintar;
	}

	public void setFlagSyspintar(String flagSyspintar) {
		this.flagSyspintar = flagSyspintar;
	}

	public int getTurutan() {
		return turutan;
	}

	public void setTurutan(int turutan) {
		this.turutan = turutan;
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
