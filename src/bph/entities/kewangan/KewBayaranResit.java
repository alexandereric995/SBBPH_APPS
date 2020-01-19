package bph.entities.kewangan;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lebah.template.UID;
import portal.module.entity.Users;
import bph.entities.kod.KodJuruwang;
import db.persistence.MyPersistence;

@Entity
@Table(name = "kew_bayaran_resit")
public class KewBayaranResit {

	@Id
	@Column(name = "id")
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "id_pembayar")
	private Users pembayar;
	
	@Column(name = "flag_jenis_bayaran")
	private String flagJenisBayaran; //'KAUNTER; MANUAL; ONLINE'
	
	@ManyToOne
	@JoinColumn(name = "id_juruwang")
	private KodJuruwang kodJuruwang;
	
	@Column(name = "id_kod_pusat_terima")
	private String kodPusatTerima;
	
	@Column(name = "no_resit")
	private String noResit;
	
	@Column(name = "no_resit_lama")
	private String noResitLama;
	
	@Column(name = "kod_mesin")
	private String kodMesin;
	
	@Column(name = "jumlah_amaun_bayaran")
	private Double jumlahAmaunBayaran;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_bayaran")
	private Date tarikhBayaran; 
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_resit")
	private Date tarikhResit;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "masa_resit")
	private Date masaResit;
	
	@Column(name = "flag_void")
	private String flagVoid; // jika berlaku pembatalan resit. Next. update invois semula
	
	@Column(name = "amaun_void")
	private Double amaunVoid; // jika berlaku pembatalan resit. Next. update invois semula
	
	@Column(name = "catatan_batal_resit")
	private String catatanBatalResit;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_batal_resit")
	private Date tarikhBatalResit;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_cetak_resit")
	private Date tarikhCetakResit;
	
	@Column(name = "bil_cetak_resit")
	private Integer bilCetakResit;
	
	@Column(name = "catatan_bayar_manual")
	private String catatanBayarManual;
	
	@Column(name = "kod_juruwang")
	private String juruwangKod; // kod fix bila kod juruwang berubah
	
	/**Auditrail*/
	@ManyToOne
	@JoinColumn(name = "id_pendaftar")
	private Users userPendaftar;

	@ManyToOne(cascade={CascadeType.PERSIST},fetch=FetchType.LAZY)
	@JoinColumn(name = "id_kemaskini")
	private Users userKemaskini;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_daftar")
	private Date tarikhDaftar;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_kemaskini")
	private Date tarikhKemaskini;
	
	@Column(name = "no_pengenalan_pembayar")
	private String noPengenalanPembayar;
	
	@Column(name = "nama_pembayar")
	private String namaPembayar; //juruwang masukkan nama pembayar untuk cetakan resit(bayaran oleh nama pembayar walaupun permohonan dibuat oleh orang lain)
	
	@Column(name = "alamat_pembayar")
	private String alamatPembayar;
	
	@Column(name = "id_permohonan")
	private String idPermohonan;
	
	@Column(name = "id_transaksi_bank")
	private String idTransaksiBank;
	
	@Column(name = "flag_jenis_resit")
	private String flagJenisResit;
	
	public KewBayaranResit() {
		setId(UID.getUID());
		setTarikhBayaran(new Date());
		setTarikhResit(new Date());
		setMasaResit(new Date());
		setFlagVoid("T");
		setAmaunVoid(0D);
		setJumlahAmaunBayaran(0D);
		setBilCetakResit(0);
		setTarikhDaftar(new Date());
	}
	
	public String getKeteranganFlagJenisResit() {
		String keteranganFlagJenisResit = "";
		if (this.flagJenisResit != null) {
			if (this.flagJenisResit.equals("1")) {
				keteranganFlagJenisResit = "DEPOSIT";
			} else if (this.flagJenisResit.equals("2")) {
				keteranganFlagJenisResit = "SEWA";
			} else if (this.flagJenisResit.equals("3")) {
				keteranganFlagJenisResit = "DEPOSIT DAN SEWA";
			}
		}
		
		return keteranganFlagJenisResit;		
	}
	
	public Map<String, Object> getMaklumatPembayarLain(){
		
		MyPersistence mp = new MyPersistence();
		String id = this.id;		
		Map<String, Object> map = new HashMap<String, Object>();
		
		String noInvois = "";
		String flagJenisPembayarLain = "";
		String nokpsm = "";
		String nama = "";
		String alamat1 = "";
		String alamat2 = "";
		String alamat3 = "";
		String poskod = "";
		String bandar = "";
		String negeri = "";
		String bandarKeterangan = "";
		String negeriKeterangan = "";
		
		try {
			
			KewResitSenaraiInvois senaraiInvois = (KewResitSenaraiInvois) mp.get("select x from KewResitSenaraiInvois x where x.resit.id='" + id + "'");
			if(senaraiInvois != null){
				KewInvois v = (KewInvois) mp.get("select x from KewInvois x where x.id ='" + senaraiInvois.getInvois().getId() + "'");
				noInvois = v.getNoInvois();
				flagJenisPembayarLain = v.getFlagJenisPembayarLain();
				
				PembayarLain obj = (PembayarLain) mp.get("select x from PembayarLain x where x.id='" + v.getPembayarLain().getId() + "'");
				
				nokpsm = obj.getId();
				nama = obj.getNama();
				alamat1 = obj.getAlamat1();
				alamat2 = obj.getAlamat2();
				alamat3 = obj.getAlamat3();
				poskod = obj.getPoskod();
				bandar = obj.getBandar()!=null?obj.getBandar().getId():"";
				negeri = obj.getBandar()!=null?obj.getBandar().getNegeri().getId():"";
				bandarKeterangan = obj.getBandar()!=null?obj.getBandar().getKeterangan():"";
				negeriKeterangan = obj.getBandar()!=null?obj.getBandar().getNegeri().getKeterangan():"";
			
			}
			map.put("noInvois", noInvois);
			map.put("flagJenisPembayarLain", flagJenisPembayarLain);
			map.put("nokpsm", nokpsm);
			map.put("nama", nama);
			map.put("alamat1", alamat1);
			map.put("alamat2", alamat2);
			map.put("alamat3", alamat3);
			map.put("poskod",poskod);
			map.put("bandar", bandar);
			map.put("negeri", negeri);
			map.put("bandarKeterangan", bandarKeterangan);
			map.put("negeriKeterangan", negeriKeterangan);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}

		return map;
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

	public String getFlagJenisBayaran() {
		return flagJenisBayaran;
	}

	public void setFlagJenisBayaran(String flagJenisBayaran) {
		this.flagJenisBayaran = flagJenisBayaran;
	}

	public KodJuruwang getKodJuruwang() {
		return kodJuruwang;
	}

	public void setKodJuruwang(KodJuruwang kodJuruwang) {
		this.kodJuruwang = kodJuruwang;
	}

	public String getKodPusatTerima() {
		return kodPusatTerima;
	}

	public void setKodPusatTerima(String kodPusatTerima) {
		this.kodPusatTerima = kodPusatTerima;
	}

	public String getNoResit() {
		return noResit;
	}

	public void setNoResit(String noResit) {
		this.noResit = noResit;
	}

	public String getNoResitLama() {
		return noResitLama;
	}

	public void setNoResitLama(String noResitLama) {
		this.noResitLama = noResitLama;
	}

	public String getKodMesin() {
		return kodMesin;
	}

	public void setKodMesin(String kodMesin) {
		this.kodMesin = kodMesin;
	}

	public Double getJumlahAmaunBayaran() {
		return jumlahAmaunBayaran;
	}

	public void setJumlahAmaunBayaran(Double jumlahAmaunBayaran) {
		this.jumlahAmaunBayaran = jumlahAmaunBayaran;
	}

	public Date getTarikhBayaran() {
		return tarikhBayaran;
	}

	public void setTarikhBayaran(Date tarikhBayaran) {
		this.tarikhBayaran = tarikhBayaran;
	}

	public Date getTarikhResit() {
		return tarikhResit;
	}

	public void setTarikhResit(Date tarikhResit) {
		this.tarikhResit = tarikhResit;
	}

	public Date getMasaResit() {
		return masaResit;
	}

	public void setMasaResit(Date masaResit) {
		this.masaResit = masaResit;
	}

	public String getFlagVoid() {
		return flagVoid;
	}

	public void setFlagVoid(String flagVoid) {
		this.flagVoid = flagVoid;
	}

	public Double getAmaunVoid() {
		return amaunVoid;
	}

	public void setAmaunVoid(Double amaunVoid) {
		this.amaunVoid = amaunVoid;
	}

	public String getCatatanBatalResit() {
		return catatanBatalResit;
	}

	public void setCatatanBatalResit(String catatanBatalResit) {
		this.catatanBatalResit = catatanBatalResit;
	}

	public Date getTarikhBatalResit() {
		return tarikhBatalResit;
	}

	public void setTarikhBatalResit(Date tarikhBatalResit) {
		this.tarikhBatalResit = tarikhBatalResit;
	}

	public Date getTarikhCetakResit() {
		return tarikhCetakResit;
	}

	public void setTarikhCetakResit(Date tarikhCetakResit) {
		this.tarikhCetakResit = tarikhCetakResit;
	}

	public Integer getBilCetakResit() {
		return bilCetakResit;
	}

	public void setBilCetakResit(Integer bilCetakResit) {
		this.bilCetakResit = bilCetakResit;
	}

	public String getCatatanBayarManual() {
		return catatanBayarManual;
	}

	public void setCatatanBayarManual(String catatanBayarManual) {
		this.catatanBayarManual = catatanBayarManual;
	}

	public String getJuruwangKod() {
		return juruwangKod;
	}

	public void setJuruwangKod(String juruwangKod) {
		this.juruwangKod = juruwangKod;
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

	public String getNoPengenalanPembayar() {
		return noPengenalanPembayar;
	}

	public void setNoPengenalanPembayar(String noPengenalanPembayar) {
		this.noPengenalanPembayar = noPengenalanPembayar;
	}

	public String getNamaPembayar() {
		return namaPembayar;
	}

	public void setNamaPembayar(String namaPembayar) {
		this.namaPembayar = namaPembayar;
	}

	public String getAlamatPembayar() {
		return alamatPembayar;
	}

	public void setAlamatPembayar(String alamatPembayar) {
		this.alamatPembayar = alamatPembayar;
	}

	public String getIdPermohonan() {
		return idPermohonan;
	}

	public void setIdPermohonan(String idPermohonan) {
		this.idPermohonan = idPermohonan;
	}

	public String getIdTransaksiBank() {
		return idTransaksiBank;
	}

	public void setIdTransaksiBank(String idTransaksiBank) {
		this.idTransaksiBank = idTransaksiBank;
	}

	public String getFlagJenisResit() {
		return flagJenisResit;
	}

	public void setFlagJenisResit(String flagJenisResit) {
		this.flagJenisResit = flagJenisResit;
	}
}
