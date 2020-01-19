package bph.entities.rk;

import java.util.Calendar;
import java.util.Date;

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
import bph.utils.Util;

@Entity
@Table(name = "rk_perjanjian")
public class RkPerjanjian {

	@Id
	@Column(name = "id")
	private String id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_fail", nullable = false)
	private RkFail fail;
	
	@ManyToOne
	@JoinColumn(name = "id_permohonan")
	private RkPermohonan permohonan;
	
	@Column(name = "no_rujukan_sst")
	private String noRujukanSST;
	
	@Temporal(TemporalType.DATE)
	@Column(name="tarikh_mula_sst")
	private Date tarikhMulaSST;
	
	@Temporal(TemporalType.DATE)
	@Column(name="tarikh_tamat_sst")
	private Date tarikhTamatSST;
	
	@Temporal(TemporalType.DATE)
	@Column(name="tarikh_akuan_terima_sst")
	private Date tarikhAkuanTerimaSST;

	@Column(name = "no_rujukan")
	private String noRujukan;

	@Temporal(TemporalType.DATE)
	@Column(name="tarikh_mula")
	private Date tarikhMula;
	
	@Column(name = "tempoh")
	private Integer tempoh;
	
	@Temporal(TemporalType.DATE)
	@Column(name="tarikh_tamat")
	private Date tarikhTamat;
	
	@Column(name = "kadar_sewa")
	private Double kadarSewa;
	
	@Column(name = "deposit")
	private Double deposit;
	
	@Column(name = "flag_caj_iwk")
	private String flagCajIWK;
	
	@Column(name = "kadar_bayaran_iwk")
	private Double kadarBayaranIWK;
	
	@Column(name = "id_jenis_sewa")
	private String idJenisSewa;
	
	@Column(name = "id_jenis_perjanjian")
	private String idJenisPerjanjian;
	
	@Column(name = "catatan")
	private String catatan;
	
	@Column(name = "flag_aktif")
	private String flagAktif;
	
	@Column(name = "flag_perjanjian_semasa")
	private String flagPerjanjianSemasa;
	
	@Column(name = "flag_kutipan_data")
	private String flagKutipanData;
	
	@Column(name = "id_status_perjanjian")
	private String idStatusPerjanjian;
	
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
	
	public RkPerjanjian() {
		setId(UID.getUID());
		setFlagAktif("T");
		setFlagPerjanjianSemasa("T");
		setIdStatusPerjanjian("1");
		setTarikhMasuk(new Date());
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
	
	public String getJenisPerjanjian(){
		
		String idJenisPerjanjian = this.idJenisPerjanjian;
		String jenisPerjanjian = "";
		
		if("1".equals(idJenisPerjanjian)){
			jenisPerjanjian = "BARU";
		} else if("2".equals(idJenisPerjanjian)){
			jenisPerjanjian = "PERLANJUTAN";
		} else if("3".equals(idJenisPerjanjian)){
			jenisPerjanjian = "PENGURANGAN / PENAMBAHAN KADAR SEWA";
		} else if("4".equals(idJenisPerjanjian)){
			jenisPerjanjian = "PENGECUALIAN BAYARAN SEWA";
		} else if("5".equals(idJenisPerjanjian)){
			jenisPerjanjian = "PENGURANGAN / PENAMBAHAN CAJ IWK";
		} else if("6".equals(idJenisPerjanjian)){
			jenisPerjanjian = "PENGECUALIAN CAJ IWK";
		}
		
		return jenisPerjanjian;
	}
	
	public String getStatusPerjanjian(){
		
		String idStatusPerjanjian = this.idStatusPerjanjian;
		String statusPerjanjian = "";
		
		if("1".equals(idStatusPerjanjian)){
			statusPerjanjian = "AKTIF";
		} else if("2".equals(idStatusPerjanjian)){
			statusPerjanjian = "TAMAT";
		} else if("3".equals(idStatusPerjanjian)){
			statusPerjanjian = "BATAL";
		}
		
		return statusPerjanjian;
	}
	
	public String getBilHariPerjanjianAkanTamat(){
		String bilHari = "";
		if (this.flagPerjanjianSemasa.equals("Y") && this.idStatusPerjanjian.equals("1")) {
			Calendar currentDate = Calendar.getInstance();
			currentDate.setTime(new Date());
			Calendar calTamat = Calendar.getInstance();
			calTamat.setTime(this.tarikhTamat);
			
			if (currentDate.before(calTamat)) {
				currentDate.add(Calendar.MONTH, 6);
				if (currentDate.after(calTamat)) {
					currentDate.setTime(new Date());
					bilHari = String.valueOf(Util.daysBetween(currentDate.getTime(), calTamat.getTime()));
				}
			}			
		}		
		return bilHari;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public RkFail getFail() {
		return fail;
	}

	public void setFail(RkFail fail) {
		this.fail = fail;
	}

	public RkPermohonan getPermohonan() {
		return permohonan;
	}

	public void setPermohonan(RkPermohonan permohonan) {
		this.permohonan = permohonan;
	}

	public String getNoRujukanSST() {
		return noRujukanSST;
	}

	public void setNoRujukanSST(String noRujukanSST) {
		this.noRujukanSST = noRujukanSST;
	}

	public Date getTarikhMulaSST() {
		return tarikhMulaSST;
	}

	public void setTarikhMulaSST(Date tarikhMulaSST) {
		this.tarikhMulaSST = tarikhMulaSST;
	}

	public Date getTarikhTamatSST() {
		return tarikhTamatSST;
	}

	public void setTarikhTamatSST(Date tarikhTamatSST) {
		this.tarikhTamatSST = tarikhTamatSST;
	}

	public Date getTarikhAkuanTerimaSST() {
		return tarikhAkuanTerimaSST;
	}

	public void setTarikhAkuanTerimaSST(Date tarikhAkuanTerimaSST) {
		this.tarikhAkuanTerimaSST = tarikhAkuanTerimaSST;
	}

	public String getNoRujukan() {
		return noRujukan;
	}

	public void setNoRujukan(String noRujukan) {
		this.noRujukan = noRujukan;
	}

	public Date getTarikhMula() {
		return tarikhMula;
	}

	public void setTarikhMula(Date tarikhMula) {
		this.tarikhMula = tarikhMula;
	}

	public Integer getTempoh() {
		return tempoh;
	}

	public void setTempoh(Integer tempoh) {
		this.tempoh = tempoh;
	}

	public Date getTarikhTamat() {
		return tarikhTamat;
	}

	public void setTarikhTamat(Date tarikhTamat) {
		this.tarikhTamat = tarikhTamat;
	}

	public Double getKadarSewa() {
		return kadarSewa;
	}

	public void setKadarSewa(Double kadarSewa) {
		this.kadarSewa = kadarSewa;
	}

	public Double getDeposit() {
		return deposit;
	}

	public void setDeposit(Double deposit) {
		this.deposit = deposit;
	}

	public String getFlagCajIWK() {
		return flagCajIWK;
	}

	public void setFlagCajIWK(String flagCajIWK) {
		this.flagCajIWK = flagCajIWK;
	}

	public Double getKadarBayaranIWK() {
		return kadarBayaranIWK;
	}

	public void setKadarBayaranIWK(Double kadarBayaranIWK) {
		this.kadarBayaranIWK = kadarBayaranIWK;
	}

	public String getIdJenisSewa() {
		return idJenisSewa;
	}

	public void setIdJenisSewa(String idJenisSewa) {
		this.idJenisSewa = idJenisSewa;
	}

	public String getIdJenisPerjanjian() {
		return idJenisPerjanjian;
	}

	public void setIdJenisPerjanjian(String idJenisPerjanjian) {
		this.idJenisPerjanjian = idJenisPerjanjian;
	}

	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}

	public String getFlagAktif() {
		return flagAktif;
	}

	public void setFlagAktif(String flagAktif) {
		this.flagAktif = flagAktif;
	}

	public String getFlagPerjanjianSemasa() {
		return flagPerjanjianSemasa;
	}

	public void setFlagPerjanjianSemasa(String flagPerjanjianSemasa) {
		this.flagPerjanjianSemasa = flagPerjanjianSemasa;
	}

	public String getFlagKutipanData() {
		return flagKutipanData;
	}

	public void setFlagKutipanData(String flagKutipanData) {
		this.flagKutipanData = flagKutipanData;
	}

	public String getIdStatusPerjanjian() {
		return idStatusPerjanjian;
	}

	public void setIdStatusPerjanjian(String idStatusPerjanjian) {
		this.idStatusPerjanjian = idStatusPerjanjian;
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
