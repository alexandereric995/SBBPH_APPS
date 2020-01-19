package bph.entities.qtr;

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
import portal.module.entity.UsersJob;
import bph.entities.kod.LokasiPermohonan;
import bph.entities.kod.SebabPenolakan;
import bph.entities.kod.Status;

@Entity
@Table(name = "kua_agihan")
public class KuaAgihan {

	@Id
	@Column(name = "id")
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "id_permohonan")
	private KuaPermohonan permohonan;
	
	@ManyToOne
	@JoinColumn(name = "id_pekerjaan")
	private UsersJob pekerjaan;
	
	@ManyToOne
	@JoinColumn(name = "id_pemohon")
	private Users pemohon;
	
	@ManyToOne
	@JoinColumn(name = "id_kuarters")
	private KuaKuarters kuarters;
		
	@Column(name = "no_giliran")
	private int noGiliran;
	
	@Column(name = "flag_menunggu_bersyarat")
	private int flagMenungguBersyarat;
	
	@Column(name = "flag_semakan_pelulus")
	private int flagSemakanPelulus;
	
	@ManyToOne
	@JoinColumn(name = "status")
	private Status status;
	
	public int getFlagSemakanPelulus() {
		return flagSemakanPelulus;
	}

	public void setFlagSemakanPelulus(int flagSemakanPelulus) {
		this.flagSemakanPelulus = flagSemakanPelulus;
	}

	@Column(name = "tarikh_agih")
	@Temporal(TemporalType.TIMESTAMP)
	private Date tarikhAgih;
	
	public Date getDateAgih() {
		return dateAgih;
	}

	public void setDateAgih(Date dateAgih) {
		this.dateAgih = dateAgih;
	}

	@Column(name = "date_agih")
	@Temporal(TemporalType.DATE)
	private Date dateAgih;
	
	public int getFlagMenungguBersyarat() {
		return flagMenungguBersyarat;
	}

	public void setFlagMenungguBersyarat(int flagMenungguBersyarat) {
		this.flagMenungguBersyarat = flagMenungguBersyarat;
	}

	@Column(name = "tarikh_kemaskini")
	@Temporal(TemporalType.TIMESTAMP)
	private Date tarikhKemaskini;
	
	@ManyToOne
	@JoinColumn(name = "nama_petugas")
	private Users petugas;
	
	@Column(name = "kelas_kuarters")
	private String kelasKuarters;
	
	@Column(name = "jenis_kelas_kuarters")
	private String jenisKelasKuarters;
	
	@ManyToOne
	@JoinColumn(name = "id_pengagih")
	private Users pengagih;
	
	@ManyToOne
	@JoinColumn(name = "id_lokasi")
	private LokasiPermohonan idLokasi;
	
	@ManyToOne
	@JoinColumn(name = "sebab_tolak")
	private SebabPenolakan sebabTolak;
	
	@Column(name = "catatan")
	private String catatan;
	
	public String getFlagAktif() {
		return flagAktif;
	}

	public void setFlagAktif(String flagAktif) {
		this.flagAktif = flagAktif;
	}

	@Column(name = "flag_aktif")
	private String flagAktif;
	
	public KuaAgihan() {
		setId(UID.getUID());
	}
	
	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public KuaPermohonan getPermohonan() {
		return permohonan;
	}

	public void setPermohonan(KuaPermohonan permohonan) {
		this.permohonan = permohonan;
	}

	public UsersJob getPekerjaan() {
		return pekerjaan;
	}

	public void setPekerjaan(UsersJob pekerjaan) {
		this.pekerjaan = pekerjaan;
	}

	public Users getPemohon() {
		return pemohon;
	}

	public void setPemohon(Users pemohon) {
		this.pemohon = pemohon;
	}

	public KuaKuarters getKuarters() {
		return kuarters;
	}

	public void setKuarters(KuaKuarters kuarters) {
		this.kuarters = kuarters;
	}

	public int getNoGiliran() {
		return noGiliran;
	}

	public void setNoGiliran(int noGiliran) {
		this.noGiliran = noGiliran;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Date getTarikhAgih() {
		return tarikhAgih;
	}

	public void setTarikhAgih(Date tarikhAgih) {
		this.tarikhAgih = tarikhAgih;
	}

	public Date getTarikhKemaskini() {
		return tarikhKemaskini;
	}

	public void setTarikhKemaskini(Date tarikhKemaskini) {
		this.tarikhKemaskini = tarikhKemaskini;
	}

	public Users getPetugas() {
		return petugas;
	}

	public void setPetugas(Users petugas) {
		this.petugas = petugas;
	}

	public String getKelasKuarters() {
		return kelasKuarters;
	}

	public void setKelasKuarters(String kelasKuarters) {
		this.kelasKuarters = kelasKuarters;
	}

	public String getJenisKelasKuarters() {
		return jenisKelasKuarters;
	}

	public void setJenisKelasKuarters(String jenisKelasKuarters) {
		this.jenisKelasKuarters = jenisKelasKuarters;
	}

	public Users getPengagih() {
		return pengagih;
	}

	public void setPengagih(Users pengagih) {
		this.pengagih = pengagih;
	}

	public LokasiPermohonan getIdLokasi() {
		return idLokasi;
	}

	public void setIdLokasi(LokasiPermohonan idLokasi) {
		this.idLokasi = idLokasi;
	}

	public SebabPenolakan getSebabTolak() {
		return sebabTolak;
	}

	public void setSebabTolak(SebabPenolakan sebabTolak) {
		this.sebabTolak = sebabTolak;
	}
	
	public String getStatusDalaman() {
		return statusDalaman;
	}

	public void setStatusDalaman(String statusDalaman) {
		this.statusDalaman = statusDalaman;
	}

	@Column(name = "status_dalaman")
	private String statusDalaman;
	
//	public int getNoGiliranBaru() {
//		DbPersistence db = new DbPersistence();
//		int i = 1;
//		String kelasKuarters = getKelasKuarters();
//		String idLokasi = "";
//		//	if ("E1".equals(kelasKuarters)) kelasKuarters = "E";
//		if ( getPermohonan() != null ) {
//			if ( getPermohonan().getLokasi() != null ) {
//				if ( getPermohonan().getLokasi().getId() != null ) {
//					idLokasi = getPermohonan().getLokasi().getId();
//				}
//			}
//		}
//		
//		VW_KuaAgihan agihan = (VW_KuaAgihan) db.get("SELECT x FROM VW_KuaAgihan x WHERE x.idLokasi = '" + idLokasi + "' AND x.kelasKuarters = '" + kelasKuarters + "'");
//	
//		if ( agihan != null ) {
//			if ( agihan.getMinNoGiliran() > 0 ) i = i + (getNoGiliran() - agihan.getMinNoGiliran());
//		} else {
//			i = 0;
//		}
//		
//	return i;
//}
}
