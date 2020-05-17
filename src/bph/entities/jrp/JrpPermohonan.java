package bph.entities.jrp;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import bph.entities.kod.Agensi;
import bph.entities.kod.Bandar;
import bph.entities.kod.Daerah;
import bph.entities.kod.JenisPermohonanJRP;
import bph.entities.kod.Status;

@Entity
@Table(name = "jrp_permohonan")
public class JrpPermohonan {
	
	@Id
	@Column(name = "id")
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "id_pemohon")
	private Users pemohon;

	@Column(name = "no_permohonan")
    private String noPermohonan;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_surat")
	private Date tarikhSurat;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_terima")
	private Date tarikhTerima;
	
	@ManyToOne
	@JoinColumn(name = "id_jenis_permohonan")
	private JenisPermohonanJRP jenisPermohonanJrp;

	@Column(name = "tujuan_permohonan")
	private String tujuanPermohonan;
	
	@Column(name = "alasan_permohonan")
	private String alasanPermohonan;
	
	@ManyToOne
	@JoinColumn(name = "id_agensi")
	private Agensi agensi;
	
	@Column(name = "alamat_1")
	private String alamat1;
	
	@Column(name = "alamat_2")
	private String alamat2;
	
	@Column(name = "alamat_3")
	private String alamat3;
	
	@Column(name = "poskod")
	private String poskod;
	
	@ManyToOne
	@JoinColumn(name = "id_daerah")
	private Daerah daerah;
	
	@ManyToOne
	@JoinColumn(name = "id_bandar")
	private Bandar bandar;
	
	@Column(name = "nama_pegawai_1")
	private String namaPegawai1;
	
	@Column(name = "nama_pegawai_2")
	private String namaPegawai2;
	
	@Column(name = "emel_pegawai_1")
	private String emelPegawai1;
	
	@Column(name = "emel_pegawai_2")
	private String emelPegawai2;
	
	@Column(name = "no_telefon_pegawai_1")
	private String noTelefonPegawai1;
	
	@Column(name = "no_telefon_pegawai_2")
	private String noTelefonPegawai2;
	
	@Column(name = "no_faks")
	private String noFaks;
	
	@Column(name = "flag_perakuan_peruntukan")
	private String flagPerakuanPeruntukan;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_pengesahan_ketua_jabatan")
	private Date tarikhPengesahanKetuaJabatan;

	@OneToOne
	@JoinColumn(name = "id_masuk")
	private Users idMasuk;
	
	@OneToOne
	@JoinColumn(name = "id_kemaskini")
	private Users idKemaskini;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_masuk")
	private Date tarikhMasuk;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_kemaskini")
	private Date tarikhKemaskini;
	
	@ManyToOne
	@JoinColumn(name = "id_status")
	private Status status;
	
	@Column(name = "id_status", insertable=false, updatable=false)
	private String idStatus;
	
	@Column(name = "no_siri_perjanjian")
	private String noSiriPerjanjian;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_mula_perjanjian")
	private Date tarikhMulaPerjanjian;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_tamat_perjanjian")
	private Date tarikhTamatPerjanjian;

	@Column(name = "kadar_sewa")
	private Double kadarSewa;
	
	@OneToOne
	@JoinColumn(name = "id_urusetia")
	private Users urusetia;
	
	@Column(name = "id_urusetia", insertable=false, updatable=false)
	private String idUrusetia;
	
	@Column(name = "catatan_agihan")
	private String catatanAgihan;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_agihan")
	private Date tarikhAgihan;
	
	@Column(name = "catatan_batal")
	private String catatanBatal;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_batal")
	private Date tarikhBatal;
	
	@Column(name = "flag_aktif")
	private String flagAktif;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_mula_duduk_jrp")
	private Date tarikhMulaDudukJrp;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_hantar_ulasan")
	private Date tarikhHantarUlasan;
	
	@OneToOne
	@JoinColumn(name = "id_agensi_hq")
	private Users agensiHq;
	
	@Column(name = "nama_ksu")
	private String namaKSU;
	
	@Column(name = "jawatan_ksu")
	private String jawatanKSU;
	
	public JrpPermohonan() {
		setId(UID.getUID());
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNoPermohonan() {
		return noPermohonan;
	}

	public void setNoPermohonan(String noPermohonan) {
		this.noPermohonan = noPermohonan;
	}

	public Date getTarikhSurat() {
		return tarikhSurat;
	}

	public void setTarikhSurat(Date tarikhSurat) {
		this.tarikhSurat = tarikhSurat;
	}

	public Date getTarikhTerima() {
		return tarikhTerima;
	}

	public void setTarikhTerima(Date tarikhTerima) {
		this.tarikhTerima = tarikhTerima;
	}

	public JenisPermohonanJRP getJenisPermohonanJrp() {
		return jenisPermohonanJrp;
	}

	public void setJenisPermohonanJrp(JenisPermohonanJRP jenisPermohonanJrp) {
		this.jenisPermohonanJrp = jenisPermohonanJrp;
	}

	public String getAlasanPermohonan() {
		return alasanPermohonan;
	}

	public void setAlasanPermohonan(String alasanPermohonan) {
		this.alasanPermohonan = alasanPermohonan;
	}

	public Agensi getAgensi() {
		return agensi;
	}

	public void setAgensi(Agensi agensi) {
		this.agensi = agensi;
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

	public Daerah getDaerah() {
		return daerah;
	}

	public void setDaerah(Daerah daerah) {
		this.daerah = daerah;
	}

	public Bandar getBandar() {
		return bandar;
	}

	public void setBandar(Bandar bandar) {
		this.bandar = bandar;
	}

	public String getNamaPegawai1() {
		return namaPegawai1;
	}

	public void setNamaPegawai1(String namaPegawai1) {
		this.namaPegawai1 = namaPegawai1;
	}

	public String getNamaPegawai2() {
		return namaPegawai2;
	}

	public void setNamaPegawai2(String namaPegawai2) {
		this.namaPegawai2 = namaPegawai2;
	}

	public String getEmelPegawai1() {
		return emelPegawai1;
	}

	public void setEmelPegawai1(String emelPegawai1) {
		this.emelPegawai1 = emelPegawai1;
	}

	public String getEmelPegawai2() {
		return emelPegawai2;
	}

	public void setEmelPegawai2(String emelPegawai2) {
		this.emelPegawai2 = emelPegawai2;
	}

	public String getNoTelefonPegawai1() {
		return noTelefonPegawai1;
	}

	public void setNoTelefonPegawai1(String noTelefonPegawai1) {
		this.noTelefonPegawai1 = noTelefonPegawai1;
	}

	public String getNoTelefonPegawai2() {
		return noTelefonPegawai2;
	}

	public void setNoTelefonPegawai2(String noTelefonPegawai2) {
		this.noTelefonPegawai2 = noTelefonPegawai2;
	}

	public String getNoFaks() {
		return noFaks;
	}

	public void setNoFaks(String noFaks) {
		this.noFaks = noFaks;
	}

	public String getFlagPerakuanPeruntukan() {
		return flagPerakuanPeruntukan;
	}

	public void setFlagPerakuanPeruntukan(String flagPerakuanPeruntukan) {
		this.flagPerakuanPeruntukan = flagPerakuanPeruntukan;
	}

	public Date getTarikhPengesahanKetuaJabatan() {
		return tarikhPengesahanKetuaJabatan;
	}

	public void setTarikhPengesahanKetuaJabatan(Date tarikhPengesahanKetuaJabatan) {
		this.tarikhPengesahanKetuaJabatan = tarikhPengesahanKetuaJabatan;
	}

	public Users getIdMasuk() {
		return idMasuk;
	}

	public void setIdMasuk(Users idMasuk) {
		this.idMasuk = idMasuk;
	}

	public Users getIdKemaskini() {
		return idKemaskini;
	}

	public void setIdKemaskini(Users idKemaskini) {
		this.idKemaskini = idKemaskini;
	}

	public Date getTarikhMasuk() {
		return tarikhMasuk;
	}

	public void setTarikhMasuk(Date tarikhMasuk) {
		this.tarikhMasuk = tarikhMasuk;
	}

	public Date getTarikhKemaskini() {
		return tarikhKemaskini;
	}

	public void setTarikhKemaskini(Date tarikhKemaskini) {
		this.tarikhKemaskini = tarikhKemaskini;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getNoSiriPerjanjian() {
		return noSiriPerjanjian;
	}

	public void setNoSiriPerjanjian(String noSiriPerjanjian) {
		this.noSiriPerjanjian = noSiriPerjanjian;
	}

	public Date getTarikhMulaPerjanjian() {
		return tarikhMulaPerjanjian;
	}

	public void setTarikhMulaPerjanjian(Date tarikhMulaPerjanjian) {
		this.tarikhMulaPerjanjian = tarikhMulaPerjanjian;
	}

	public Date getTarikhTamatPerjanjian() {
		return tarikhTamatPerjanjian;
	}

	public void setTarikhTamatPerjanjian(Date tarikhTamatPerjanjian) {
		this.tarikhTamatPerjanjian = tarikhTamatPerjanjian;
	}

	public Double getKadarSewa() {
		return kadarSewa;
	}

	public void setKadarSewa(Double kadarSewa) {
		this.kadarSewa = kadarSewa;
	}

	public Users getUrusetia() {
		return urusetia;
	}

	public void setUrusetia(Users urusetia) {
		this.urusetia = urusetia;
	}

	public String getCatatanAgihan() {
		return catatanAgihan;
	}

	public void setCatatanAgihan(String catatanAgihan) {
		this.catatanAgihan = catatanAgihan;
	}

	public Date getTarikhAgihan() {
		return tarikhAgihan;
	}

	public void setTarikhAgihan(Date tarikhAgihan) {
		this.tarikhAgihan = tarikhAgihan;
	}

	public String getTujuanPermohonan() {
		return tujuanPermohonan;
	}

	public void setTujuanPermohonan(String tujuanPermohonan) {
		this.tujuanPermohonan = tujuanPermohonan;
	}

	public String getCatatanBatal() {
		return catatanBatal;
	}

	public void setCatatanBatal(String catatanBatal) {
		this.catatanBatal = catatanBatal;
	}

	public Date getTarikhBatal() {
		return tarikhBatal;
	}

	public void setTarikhBatal(Date tarikhBatal) {
		this.tarikhBatal = tarikhBatal;
	}

	public String getFlagAktif() {
		return flagAktif;
	}

	public void setFlagAktif(String flagAktif) {
		this.flagAktif = flagAktif;
	}

	public Date getTarikhMulaDudukJrp() {
		return tarikhMulaDudukJrp;
	}

	public void setTarikhMulaDudukJrp(Date tarikhMulaDudukJrp) {
		this.tarikhMulaDudukJrp = tarikhMulaDudukJrp;
	}

	public Date getTarikhHantarUlasan() {
		return tarikhHantarUlasan;
	}

	public void setTarikhHantarUlasan(Date tarikhHantarUlasan) {
		this.tarikhHantarUlasan = tarikhHantarUlasan;
	}
	
	public String getIdStatus() {
		return idStatus;
	}

	public void setIdStatus(String idStatus) {
		this.idStatus = idStatus;
	}

	public String getIdUrusetia() {
		return idUrusetia;
	}

	public void setIdUrusetia(String idUrusetia) {
		this.idUrusetia = idUrusetia;
	}

	public Users getAgensiHq() {
		return agensiHq;
	}

	public void setAgensiHq(Users agensiHq) {
		this.agensiHq = agensiHq;
	}
	
	public String getKategoriPermohonan() {
		String kategoriPermohonan = "";
		JenisPermohonanJRP jrp = this.jenisPermohonanJrp;
		if(jrp!=null){
			if(jrp.getId().equalsIgnoreCase("JRP5")){
				kategoriPermohonan = "PINDAH";
			}else if(jrp.getId().equalsIgnoreCase("JRP6")){
				kategoriPermohonan = "PINDAH";
			}else if(jrp.getId().equalsIgnoreCase("JRP7")){
				kategoriPermohonan = "LANJUT";
			}else{
				kategoriPermohonan = "BARU";
			}
		}
		return kategoriPermohonan;
	}

	public String getStatusPerjanjian(){
		
		String statusPerjanjian = "";
		Status status = this.status;
		Date tarikhTamat = this.tarikhTamatPerjanjian;
		int bilHari = 0;
		
				if(("1424860634493").equals(status.getId()) || ("1424860634499").equals(status.getId())){
				
				if(tarikhTamat != null && tarikhTamat.toString().length() > 0){
					Calendar calCurrent = new GregorianCalendar();
					Date dateCurrent = new Date();
					calCurrent.setTime(dateCurrent);
					
					Calendar calTamat = new GregorianCalendar();
					Date dateTamat = tarikhTamat;
					calTamat.setTime(dateTamat);
				

					int diffYear = calTamat.get(Calendar.YEAR) - calCurrent.get(Calendar.YEAR);
					int diffMonth = diffYear * 12 + calTamat.get(Calendar.MONTH) - calCurrent.get(Calendar.MONTH);
					bilHari = daysBetween(calTamat.getTime(), calCurrent.getTime());
				
					if (calCurrent.getTime().after(calTamat.getTime())) {
						statusPerjanjian = "PERJANJIAN TAMAT";
//					context.put("status", status);
					} else if (calCurrent.getTime().before(calTamat.getTime()) && bilHari <= 90) {
						statusPerjanjian = bilHari + " HARI LAGI";
//					context.put("status", status);
					}  else if(calCurrent.getTime().before(calTamat.getTime()) && bilHari > 90 && bilHari <= 210){
						statusPerjanjian = diffMonth + " BULAN LAGI";
					}
					} 
				}
				return statusPerjanjian;
}

	private int daysBetween(Date date1, Date date2) {
	return (int) ((date1.getTime() - date2.getTime()) / (1000 * 60 * 60 * 24));
	}

	public String getBilMesyuarat(){
		DbPersistence db = new DbPersistence();
		String bil = "";
		Status status = this.status;
		String id = this.id;
		System.out.println(" id ===== " + id);
		if(("1424860634490").equals(status.getId())){
			bil = (String) db.get("Select x.bilMesyuarat from JrpKertasPertimbangan x where x.jrpPermohonan.id = '" + id + "'");
		}
		System.out.println("ceking mesyuarat ====== " + bil);
		
		return bil;
		
	}

	public String getSewaLatest() throws Exception{
			
		DbPersistence db = new DbPersistence();
		Double sewaLama = 0.00;
		Double sewaBaru = 0.00;
		String persenSewa = "T"; //tak lebih 10% dari sewa lama
		
		try{
		System.out.println("Select x from JrpPermohonanLokasi x where x.permohonan.id = '" + id + "'");
		List<JrpPermohonanLokasi> pl = db.list("Select x from JrpPermohonanLokasi x where x.permohonan.id = '" + id + "'");
		
		if(pl != null){
			sewaLama = (Double) db.get("Select x.sewaSebulan from JrpPermohonanLokasi x where x.permohonan.id = '" + id + "' and x.flagLokasi = 'S'"); //SEWA LAMA
			sewaBaru = (Double) db.get("Select x.sewaSebulan from JrpPermohonanLokasi x where x.permohonan.id = '" + id + "' and x.flagLokasi = 'B'"); //SEWA BARU
			
			Double persent = sewaBaru / sewaLama * 100;
			System.out.println("ceking persent ====== " + persent);
			if(persent >= 110){
				persenSewa = "Y"; //lebih 10% sewa lama
				System.out.println("lebih");
			}
		}else{
			System.out.println("pl is null");
		}
		
		}catch (Exception e) {
			System.err.println("null");
			return persenSewa;
		}
		System.out.println("asd");		
		return persenSewa;
		
	}

	public Users getPemohon() {
		return pemohon;
	}

	public void setPemohon(Users pemohon) {
		this.pemohon = pemohon;
	}

	public String getNamaKSU() {
		return namaKSU;
	}

	public void setNamaKSU(String namaKSU) {
		this.namaKSU = namaKSU;
	}

	public String getJawatanKSU() {
		return jawatanKSU;
	}

	public void setJawatanKSU(String jawatanKSU) {
		this.jawatanKSU = jawatanKSU;
	}
}
