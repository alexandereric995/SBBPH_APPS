package bph.entities.rk;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lebah.template.UID;
import portal.module.entity.Users;
import bph.entities.kod.Status;

@Entity
@Table(name = "rk_permohonan")
public class RkPermohonan {
	
	private static String kodHasilDeposit = "79503"; // DEPOSIT PELBAGAI
	
	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_fail")
	private RkFail fail;
	
	@Column(name = "no_permohonan")
	private String noPermohonan;
	
	@Column(name = "id_jenis_permohonan")
	private String idJenisPermohonan;

	@Temporal(TemporalType.DATE)
	@Column(name="tarikh_permohonan")
	private Date tarikhPermohonan;
	
	@Column(name = "no_rujukan_permohonan")
	private String noRujukanPermohonan;
	
	@Temporal(TemporalType.DATE)
	@Column(name="tarikh_terima_permohonan")
	private Date tarikhTerimaPermohonan;
	
	@Column(name = "id_jenis_sewa")
	private String idJenisSewa;
	
	@Column(name = "kadar_sewa_jpph")
	private double kadarSewaJPPH;
	
	@Column(name = "harga_tawaran_sewa")
	private double hargaTawaranSewa;
	
	@Temporal(TemporalType.DATE)
	@Column(name="tarikh_mula_operasi")
	private Date tarikhMulaOperasi;	
	
	@Column(name = "tempoh")
	private int tempoh;
	
	@Temporal(TemporalType.DATE)
	@Column(name="tarikh_tamat_operasi")
	private Date tarikhTamatOperasi;		
	
	@ManyToOne
	@JoinColumn(name = "id_status")
	private Status status;
	
	@Column(name = "flag_aktif")
	private String flagAktif;

	@Column(name = "catatan")
	private String catatan;
	
	@Column(name = "ulasan_kertas_pertimbangan")
	private String ulasanKertasPertimbangan;
	
	@Column(name = "syor_kertas_pertimbangan")
	private String syorKertasPertimbangan;
	
	@ManyToOne
	@JoinColumn(name = "id_mesyuarat")
	private RkMesyuarat mesyuarat;
	
	@Column(name = "flag_keputusan_mesyuarat")
	private String flagKeputusanMesyuarat;
	
	@Column(name = "catatan_keputusan_mesyuarat")
	private String catatanKeputusanMesyuarat;
	
	@ManyToOne
	@JoinColumn(name = "id_pegawai_batal")
	private Users batalOleh;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="tarikh_batal")
	private Date tarikhBatal;
	
	@Column(name = "catatan_batal")
	private String catatanBatal;	
	
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
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="permohonan", fetch=FetchType.EAGER)
	private List<RkAkaun> listAkaun;
	
	@OneToOne(cascade=CascadeType.ALL, mappedBy="permohonan", fetch=FetchType.EAGER)
	private RkSST sst;

	public RkPermohonan() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());
		setFlagAktif("Y");
	}
	
	public String getJenisPermohonan(){
		
		String idJenisPermohonan = this.idJenisPermohonan;
		String jenisPermohonan = "";
		
		if("1".equals(idJenisPermohonan)){
			jenisPermohonan = "PERMOHONAN BARU";
		} else if("2".equals(idJenisPermohonan)){
			jenisPermohonan = "PERMOHONAN PERLANJUTAN";
		} else if("3".equals(idJenisPermohonan)){
			jenisPermohonan = "PERMOHONAN PENGURANGAN KADAR SEWA";
		} else if("4".equals(idJenisPermohonan)){
			jenisPermohonan = "PERMOHONAN PENGECUALIAN BAYARAN SEWA";
		} else if("5".equals(idJenisPermohonan)){
			jenisPermohonan = "LAIN-LAIN";
		}
		
		return jenisPermohonan;
	}
	
	public String getKeteranganKeputusanMesyuarat() {
		String keputusan = "";
		
		if (this.getFlagKeputusanMesyuarat() != null) {
			if ("L".equals(this.getFlagKeputusanMesyuarat())) {
				keputusan = "LULUS";
			} else if ("LB".equals(this.getFlagKeputusanMesyuarat())) {
				keputusan = "LULUS BERSYARAT";
			} else if ("TG".equals(this.getFlagKeputusanMesyuarat())) {
				keputusan = "TANGGUH";
			} else if ("T".equals(this.getFlagKeputusanMesyuarat())) {
				keputusan = "TOLAK";
			}
		}		
		return keputusan;
	}
	
	public String getStatusBayaranDeposit() {
		String statusBayaran = "T";
		try {
			if (this.sst != null) {
				if (sst.getDeposit() > 0) {
					List<RkAkaun> listAkaun = this.listAkaun;
					if (listAkaun != null) {
						for (RkAkaun akaun : listAkaun) {
							if ("Y".equals(akaun.getFlagAktif())) {
								if (kodHasilDeposit.equals(akaun.getKodHasil().getId())) {
									statusBayaran = "Y";
								}
							} 
						}
					}
				} else {
					statusBayaran = "0";
				}
			}
			
		} catch (Exception ex){
			ex.printStackTrace();
		}
		return statusBayaran;
	}

	public static String getKodHasilDeposit() {
		return kodHasilDeposit;
	}

	public static void setKodHasilDeposit(String kodHasilDeposit) {
		RkPermohonan.kodHasilDeposit = kodHasilDeposit;
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

	public String getNoPermohonan() {
		return noPermohonan;
	}

	public void setNoPermohonan(String noPermohonan) {
		this.noPermohonan = noPermohonan;
	}

	public String getIdJenisPermohonan() {
		return idJenisPermohonan;
	}

	public void setIdJenisPermohonan(String idJenisPermohonan) {
		this.idJenisPermohonan = idJenisPermohonan;
	}

	public Date getTarikhPermohonan() {
		return tarikhPermohonan;
	}

	public void setTarikhPermohonan(Date tarikhPermohonan) {
		this.tarikhPermohonan = tarikhPermohonan;
	}

	public String getNoRujukanPermohonan() {
		return noRujukanPermohonan;
	}

	public void setNoRujukanPermohonan(String noRujukanPermohonan) {
		this.noRujukanPermohonan = noRujukanPermohonan;
	}

	public Date getTarikhTerimaPermohonan() {
		return tarikhTerimaPermohonan;
	}

	public void setTarikhTerimaPermohonan(Date tarikhTerimaPermohonan) {
		this.tarikhTerimaPermohonan = tarikhTerimaPermohonan;
	}

	public String getIdJenisSewa() {
		return idJenisSewa;
	}

	public void setIdJenisSewa(String idJenisSewa) {
		this.idJenisSewa = idJenisSewa;
	}

	public double getKadarSewaJPPH() {
		return kadarSewaJPPH;
	}

	public void setKadarSewaJPPH(double kadarSewaJPPH) {
		this.kadarSewaJPPH = kadarSewaJPPH;
	}

	public double getHargaTawaranSewa() {
		return hargaTawaranSewa;
	}

	public void setHargaTawaranSewa(double hargaTawaranSewa) {
		this.hargaTawaranSewa = hargaTawaranSewa;
	}

	public Date getTarikhMulaOperasi() {
		return tarikhMulaOperasi;
	}

	public void setTarikhMulaOperasi(Date tarikhMulaOperasi) {
		this.tarikhMulaOperasi = tarikhMulaOperasi;
	}

	public int getTempoh() {
		return tempoh;
	}

	public void setTempoh(int tempoh) {
		this.tempoh = tempoh;
	}

	public Date getTarikhTamatOperasi() {
		return tarikhTamatOperasi;
	}

	public void setTarikhTamatOperasi(Date tarikhTamatOperasi) {
		this.tarikhTamatOperasi = tarikhTamatOperasi;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getFlagAktif() {
		return flagAktif;
	}

	public void setFlagAktif(String flagAktif) {
		this.flagAktif = flagAktif;
	}

	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}

	public String getUlasanKertasPertimbangan() {
		return ulasanKertasPertimbangan;
	}

	public void setUlasanKertasPertimbangan(String ulasanKertasPertimbangan) {
		this.ulasanKertasPertimbangan = ulasanKertasPertimbangan;
	}

	public String getSyorKertasPertimbangan() {
		return syorKertasPertimbangan;
	}

	public void setSyorKertasPertimbangan(String syorKertasPertimbangan) {
		this.syorKertasPertimbangan = syorKertasPertimbangan;
	}

	public RkMesyuarat getMesyuarat() {
		return mesyuarat;
	}

	public void setMesyuarat(RkMesyuarat mesyuarat) {
		this.mesyuarat = mesyuarat;
	}

	public String getFlagKeputusanMesyuarat() {
		return flagKeputusanMesyuarat;
	}

	public void setFlagKeputusanMesyuarat(String flagKeputusanMesyuarat) {
		this.flagKeputusanMesyuarat = flagKeputusanMesyuarat;
	}

	public String getCatatanKeputusanMesyuarat() {
		return catatanKeputusanMesyuarat;
	}

	public void setCatatanKeputusanMesyuarat(String catatanKeputusanMesyuarat) {
		this.catatanKeputusanMesyuarat = catatanKeputusanMesyuarat;
	}

	public Users getBatalOleh() {
		return batalOleh;
	}

	public void setBatalOleh(Users batalOleh) {
		this.batalOleh = batalOleh;
	}

	public Date getTarikhBatal() {
		return tarikhBatal;
	}

	public void setTarikhBatal(Date tarikhBatal) {
		this.tarikhBatal = tarikhBatal;
	}

	public String getCatatanBatal() {
		return catatanBatal;
	}

	public void setCatatanBatal(String catatanBatal) {
		this.catatanBatal = catatanBatal;
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

	public List<RkAkaun> getListAkaun() {
		return listAkaun;
	}

	public void setListAkaun(List<RkAkaun> listAkaun) {
		this.listAkaun = listAkaun;
	}

	public RkSST getSst() {
		return sst;
	}

	public void setSst(RkSST sst) {
		this.sst = sst;
	}
}
