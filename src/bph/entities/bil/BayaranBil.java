package bph.entities.bil;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
import bph.entities.kod.Status;

@Entity
@Table(name="bil_bayaran_bil")
public class BayaranBil {
	
	@Id
	@Column(name="id")
	private String id;
	
	@ManyToOne
	@JoinColumn(name="id_pendaftaran_bil")
	private DaftarBil pendaftaranBil;
	
	@Column(name="bulan")
	private String bulan;
	
	@Column(name="tahun")
	private String tahun;
	
	@Column(name="no_bil")
	private String noBil;
	
	@Temporal(TemporalType.DATE)
	@Column(name="tarikh_bil")
	private Date tarikhBil;
	
	@Temporal(TemporalType.DATE)
	@Column(name="tarikh_terima_bil")
	private Date tarikhTerimaBil;
	
	@Column(name="amaun_tunggakan")
	private Double amaunTunggakan;
	
	@Column(name="amaun_semasa")
	private Double amaunSemasa;
	
	@Column(name="jumlah_bil")
	private Double jumlahBil;

	@Temporal(TemporalType.DATE)
	@Column(name="tarikh_akhir_bayaran")
	private Date tarikhAkhirBayaran;
	
	@ManyToOne
	@JoinColumn(name="disemak_oleh")
	private Users diSemakOleh;
	
	@Temporal(TemporalType.DATE)
	@Column(name="tarikh_semakan")
	private Date tarikhSemakan;
	
	@Temporal(TemporalType.DATE)
	@Column(name="tarikh_bayaran")
	private Date tarikhBayaran;
	
	@Column(name="amaun_bayaran")
	private Double amaunBayaran;
	
	@Column(name="no_rujukan")
	private String noRujukan;
	
	@Column(name="no_eft_bayaran")
	private String noEftBayaran;
	
	@Column(name="catatan")
	private String catatan;
	
	@Column(name="status_bayaran")
	private String statusBayaran;
	
	@Column(name="status_lulus")
	private String statusLulus;
	
	@ManyToOne
	@JoinColumn(name="status_bil")
	private Status statusBil;
	
	@Temporal(TemporalType.DATE)
	@Column(name="tempoh_bil_dari")
	private Date tempohBilDari;
	
	@Temporal(TemporalType.DATE)
	@Column(name="tempoh_bil_hingga")
	private Date tempohBilHingga;
	
	@Temporal(TemporalType.DATE)
	@Column(name="tarikh_terima_bil_kewangan")
	private Date tarikhTerimaBilKewangan;
	
	@Temporal(TemporalType.DATE)
	@Column(name="tarikh_baucer")
	private Date tarikhBaucer;
	
	@Temporal(TemporalType.DATE)
	@Column(name="tarikh_eft")
	private Date tarikhEft;
	
	public BayaranBil(){
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DaftarBil getPendaftaranBil() {
		return pendaftaranBil;
	}

	public void setPendaftaranBil(DaftarBil pendaftaranBil) {
		this.pendaftaranBil = pendaftaranBil;
	}

	public String getBulan() {
		return bulan;
	}

	public void setBulan(String bulan) {
		this.bulan = bulan;
	}

	public String getTahun() {
		return tahun;
	}

	public void setTahun(String tahun) {
		this.tahun = tahun;
	}

	public String getNoBil() {
		return noBil;
	}

	public void setNoBil(String noBil) {
		this.noBil = noBil;
	}

	public Date getTarikhBil() {
		return tarikhBil;
	}

	public void setTarikhBil(Date tarikhBil) {
		this.tarikhBil = tarikhBil;
	}

	public Date getTarikhTerimaBil() {
		return tarikhTerimaBil;
	}

	public void setTarikhTerimaBil(Date tarikhTerimaBil) {
		this.tarikhTerimaBil = tarikhTerimaBil;
	}

	public Double getAmaunTunggakan() {
		return amaunTunggakan;
	}

	public void setAmaunTunggakan(Double amaunTunggakan) {
		this.amaunTunggakan = amaunTunggakan;
	}

	public Double getAmaunSemasa() {
		return amaunSemasa;
	}

	public void setAmaunSemasa(Double amaunSemasa) {
		this.amaunSemasa = amaunSemasa;
	}

	public Double getJumlahBil() {
		return jumlahBil;
	}

	public void setJumlahBil(Double jumlahBil) {
		this.jumlahBil = jumlahBil;
	}

	public Date getTarikhAkhirBayaran() {
		return tarikhAkhirBayaran;
	}

	public void setTarikhAkhirBayaran(Date tarikhAkhirBayaran) {
		this.tarikhAkhirBayaran = tarikhAkhirBayaran;
	}

	public Date getTarikhBayaran() {
		return tarikhBayaran;
	}

	public void setTarikhBayaran(Date tarikhBayaran) {
		this.tarikhBayaran = tarikhBayaran;
	}

	public Double getAmaunBayaran() {
		return amaunBayaran;
	}

	public void setAmaunBayaran(Double amaunBayaran) {
		this.amaunBayaran = amaunBayaran;
	}

	public String getNoRujukan() {
		return noRujukan;
	}

	public void setNoRujukan(String noRujukan) {
		this.noRujukan = noRujukan;
	}

	public String getNoEftBayaran() {
		return noEftBayaran;
	}

	public void setNoEftBayaran(String noEftBayaran) {
		this.noEftBayaran = noEftBayaran;
	}

	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}

	public String getStatusBayaran() {
		return statusBayaran;
	}

	public void setStatusBayaran(String statusBayaran) {
		this.statusBayaran = statusBayaran;
	}

	public String getStatusLulus() {
		return statusLulus;
	}

	public void setStatusLulus(String statusLulus) {
		this.statusLulus = statusLulus;
	}

	public Status getStatusBil() {
		return statusBil;
	}

	public void setStatusBil(Status statusBil) {
		this.statusBil = statusBil;
	}

	public Date getTarikhSemakan() {
		return tarikhSemakan;
	}

	public void setTarikhSemakan(Date tarikhSemakan) {
		this.tarikhSemakan = tarikhSemakan;
	}

	public Users getDiSemakOleh() {
		return diSemakOleh;
	}

	public void setDiSemakOleh(Users diSemakOleh) {
		this.diSemakOleh = diSemakOleh;
	}

	public Date getTempohBilDari() {
		return tempohBilDari;
	}

	public void setTempohBilDari(Date tempohBilDari) {
		this.tempohBilDari = tempohBilDari;
	}

	public Date getTempohBilHingga() {
		return tempohBilHingga;
	}

	public void setTempohBilHingga(Date tempohBilHingga) {
		this.tempohBilHingga = tempohBilHingga;
	}

	public Date getTarikhTerimaBilKewangan() {
		return tarikhTerimaBilKewangan;
	}

	public void setTarikhTerimaBilKewangan(Date tarikhTerimaBilKewangan) {
		this.tarikhTerimaBilKewangan = tarikhTerimaBilKewangan;
	}

	public Date getTarikhBaucer() {
		return tarikhBaucer;
	}

	public void setTarikhBaucer(Date tarikhBaucer) {
		this.tarikhBaucer = tarikhBaucer;
	}

	public Date getTarikhEft() {
		return tarikhEft;
	}

	public void setTarikhEft(Date tarikhEft) {
		this.tarikhEft = tarikhEft;
	}
	
	
	public String getStatusNotifikasiTertunggak(){
		
		String statusNotifikasiTertunggak  = "";
//		String statusBayaran  = "";
//		Status status = this.status;
		Date tarikhBil = this.tarikhBil;
//		Date tarikhAkhirBayaran = this.tarikhAkhirBayaran;
		int bilHari = 0;
		
		if(tarikhBil != null && tarikhBil.toString().length() > 0){
			
			Calendar calTarikhBil = new GregorianCalendar();
			Date dateTerimaAduan = tarikhBil;
			calTarikhBil.setTime(dateTerimaAduan);
			
			Calendar calCurrent = new GregorianCalendar();
			Date dateCurrent = new Date();
			calCurrent.setTime(dateCurrent);
			
		
			int diffYear = calTarikhBil.get(Calendar.YEAR) - calCurrent.get(Calendar.YEAR);
//			System.out.println("PRINT YEAR ===" + diffYear);
			
			int diffMonth = diffYear * 12 + calTarikhBil.get(Calendar.MONTH) - calCurrent.get(Calendar.MONTH);
//			System.out.println("PRINT MONTH ===" + diffMonth);
			
			bilHari = daysBetween(calTarikhBil.getTime(), calCurrent.getTime());
//			System.out.println("PRINT HARI ===" + bilHari);
			
			//BILANGAN HARI STATUS BELUM DIBAYAR
			if (calCurrent.getTime().after(calTarikhBil.getTime())) {   
				statusNotifikasiTertunggak = bilHari + " HARI";
//				statusNotifikasiTertunggak = bilHari + "HARI" + " " + diffMonth + " BULAN";
			}
		}
		return statusNotifikasiTertunggak;
	}
	
	private int daysBetween(Date date1, Date date2) {
		return (int) ((date2.getTime() - date1.getTime()) / (1000 * 60 * 60 * 24));
	}	
	
}
