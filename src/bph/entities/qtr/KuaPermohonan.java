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
import bph.entities.kod.LokasiPermohonan;
import bph.entities.kod.Status;

@Entity
@Table(name = "kua_permohonan")
public class KuaPermohonan {

	@Id
	@Column(name = "id")
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "id_pemohon")
	private Users pemohon;
	
	@ManyToOne
	@JoinColumn(name = "id_lokasi_permohonan")
	private LokasiPermohonan lokasi;
	
	@Column(name = "no_fail")
	private String noFail;
	
	@Column(name = "no_permohonan")
	private String noPermohonan;
	
	@Column(name = "tarikh_permohonan")
	@Temporal(TemporalType.TIMESTAMP)
	private Date tarikhPermohonan;
	
	@Column(name = "date_permohonan")
	@Temporal(TemporalType.DATE)
	private Date datePermohonan;
	
	@Column(name = "date_mohon_tukar")
	@Temporal(TemporalType.DATE)
	private Date dateMohonTukar;
	
	@Column(name = "date_mohon_upgrade")
	@Temporal(TemporalType.DATE)
	private Date dateMohonUpgrade;
	
	public Date getDateMohonTukar() {
		return dateMohonTukar;
	}

	public void setDateMohonTukar(Date dateMohonTukar) {
		this.dateMohonTukar = dateMohonTukar;
	}

	public Date getDateMohonUpgrade() {
		return dateMohonUpgrade;
	}

	public void setDateMohonUpgrade(Date dateMohonUpgrade) {
		this.dateMohonUpgrade = dateMohonUpgrade;
	}

	public Date getDateMohonDowngrade() {
		return dateMohonDowngrade;
	}

	public void setDateMohonDowngrade(Date dateMohonDowngrade) {
		this.dateMohonDowngrade = dateMohonDowngrade;
	}

	@Column(name = "date_mohon_downgrade")
	@Temporal(TemporalType.DATE)
	private Date dateMohonDowngrade;
	
	@Column(name = "tarikh_kemaskini")
	@Temporal(TemporalType.TIMESTAMP)
	private Date tarikhKemaskini;
	
//	@Column(name = "tarikh_masuk")
//	@Temporal(TemporalType.TIMESTAMP)
//	private Date tarikhMasuk;
	
	@ManyToOne
	@JoinColumn(name = "status")
	private Status status;
	
	@Column(name = "catatan")
	private String catatan;
	
	@Column(name = "status_internal")
	private String statusDalaman;
	
	public String getStatusDalaman() {
		return statusDalaman;
	}

	public void setStatusDalaman(String statusDalaman) {
		this.statusDalaman = statusDalaman;
	}

	@Column(name = "perakuan")
	private int perakuan;
	
	@Column(name = "flag_agihan")
	private String flagAgihan;
	
	@Column(name = "flag_rayuan")
	private String flagRayuan;
	
	@Column(name = "flag_aktif")
	private String flagAktif;
	
	public String getFlagRayuan() {
		return flagRayuan;
	}

	public void setFlagRayuan(String flagRayuan) {
		this.flagRayuan = flagRayuan;
	}

	@Column(name = "flag_downgrade")
	private String flagDowngrade;
	
	@Column(name = "flag_kemaskini")
	private String flagKemaskini;
	
	@Column(name = "flag_mohon_downgrade")
	private String flagMohonDowngrade;
	
	@Column(name = "flag_mohon_upgrade")
	private String flagMohonUpgrade;
	
	@Column(name = "flag_mohon_tukar")
	private String flagMohonTukar;
	
	@Column(name = "flag_hantar_menunggu")
	private String flagHantarMenunggu;
	
	public String getFlagHantarMenunggu() {
		return flagHantarMenunggu;
	}

	public void setFlagHantarMenunggu(String flagHantarMenunggu) {
		this.flagHantarMenunggu = flagHantarMenunggu;
	}

	public String getFlagLulusTukar() {
		return flagLulusTukar;
	}

	public void setFlagLulusTukar(String flagLulusTukar) {
		this.flagLulusTukar = flagLulusTukar;
	}

	public String getFlagLulusUpgrade() {
		return flagLulusUpgrade;
	}

	public void setFlagLulusUpgrade(String flagLulusUpgrade) {
		this.flagLulusUpgrade = flagLulusUpgrade;
	}

	public String getFlagLulusDowngrade() {
		return flagLulusDowngrade;
	}

	public void setFlagLulusDowngrade(String flagLulusDowngrade) {
		this.flagLulusDowngrade = flagLulusDowngrade;
	}

	@Column(name = "flag_lulus_tukar")
	private String flagLulusTukar;
	
	@Column(name = "flag_lulus_upgrade")
	private String flagLulusUpgrade;
	
	@Column(name = "flag_lulus_downgrade")
	private String flagLulusDowngrade;
	
	public String getFlagAgihan() {
		return flagAgihan;
	}

	public void setFlagAgihan(String flagAgihan) {
		this.flagAgihan = flagAgihan;
	}

	public String getFlagAktif() {
		return flagAktif;
	}

	public void setFlagAktif(String flagAktif) {
		this.flagAktif = flagAktif;
	}

	public String getFlagKemaskini() {
		return flagKemaskini;
	}

	public void setFlagKemaskini(String flagKemaskini) {
		this.flagKemaskini = flagKemaskini;
	}

	public String getFlagMohonDowngrade() {
		return flagMohonDowngrade;
	}

	public void setFlagMohonDowngrade(String flagMohonDowngrade) {
		this.flagMohonDowngrade = flagMohonDowngrade;
	}

	public String getFlagMohonUpgrade() {
		return flagMohonUpgrade;
	}

	public void setFlagMohonUpgrade(String flagMohonUpgrade) {
		this.flagMohonUpgrade = flagMohonUpgrade;
	}

	public String getFlagMohonTukar() {
		return flagMohonTukar;
	}

	public void setFlagMohonTukar(String flagMohonTukar) {
		this.flagMohonTukar = flagMohonTukar;
	}

	public String getFlagTawaran() {
		return flagTawaran;
	}

	public void setFlagTawaran(String flagTawaran) {
		this.flagTawaran = flagTawaran;
	}

	@Column(name = "flag_tawaran")
	private String flagTawaran;
	
	@Column(name = "flag_tuntutan")
	private String flagTuntutan;
	
	@Column(name = "kelulusan1")
	private String kelulusan1;
	
	@Column(name = "kelulusan2")
	private String kelulusan2;
	
	@Column(name = "kelulusan3")
	private String kelulusan3;
	
	@Column(name = "pekerjaan")
	private int pekerjaan;
	
	@Column(name = "peribadi")
	private int peribadi;
	
	@Column(name = "pasangan")
	private int pasangan;
	
	public String getKelasLayak() {
		return kelasLayak;
	}

	public void setKelasLayak(String kelasLayak) {
		this.kelasLayak = kelasLayak;
	}

	public String getKelasDowngrade() {
		return kelasDowngrade;
	}

	public void setKelasDowngrade(String kelasDowngrade) {
		this.kelasDowngrade = kelasDowngrade;
	}

	@Column(name = "pinjaman")
	private int pinjaman;
	
	@Column(name = "kelas_layak")
	private String kelasLayak;
	
	@Column(name = "kelas_downgrade")
	private String kelasDowngrade;
	

	public KuaPermohonan() {
		setId(UID.getUID());
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Users getPemohon() {
		return pemohon;
	}

	public void setPemohon(Users pemohon) {
		this.pemohon = pemohon;
	}

	public LokasiPermohonan getLokasi() {
		return lokasi;
	}

	public void setLokasi(LokasiPermohonan lokasi) {
		this.lokasi = lokasi;
	}

	public String getNoFail() {
		return noFail;
	}

	public void setNoFail(String noFail) {
		this.noFail = noFail;
	}

	public String getNoPermohonan() {
		return noPermohonan;
	}

	public void setNoPermohonan(String noPermohonan) {
		this.noPermohonan = noPermohonan;
	}

	public Date getTarikhPermohonan() {
		return tarikhPermohonan;
	}

	public void setTarikhPermohonan(Date tarikhPermohonan) {
		this.tarikhPermohonan = tarikhPermohonan;
	}

	public Date getTarikhKemaskini() {
		return tarikhKemaskini;
	}

	public void setTarikhKemaskini(Date tarikhKemaskini) {
		this.tarikhKemaskini = tarikhKemaskini;
	}

//	public Date getTarikhMasuk() {
//		return tarikhMasuk;
//	}

//	public void setTarikhMasuk(Date tarikhMasuk) {
//		this.tarikhMasuk = tarikhMasuk;
//	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}

	public int getPerakuan() {
		return perakuan;
	}

	public void setPerakuan(int perakuan) {
		this.perakuan = perakuan;
	}

	public String getPerakuanDesc() {
		String perakuan = "TIDAK";
		if ( getPerakuan() == 1 ) {
			perakuan = "YA";
		}
		return perakuan;
	}
	
	public String getFlagDowngrade() {
		return flagDowngrade;
	}

	public void setFlagDowngrade(String flagDowngrade) {
		this.flagDowngrade = flagDowngrade;
	}

	public String getFlagDowngradeDesc() {
		String flagDowngrade = "TIDAK";
		if ( "1".equals(getFlagDowngrade()) ) {
			flagDowngrade = "YA";
		}
		return flagDowngrade;
	}
	
	public String getFlagTuntutan() {
		return flagTuntutan;
	}

	public void setFlagTuntutan(String flagTuntutan) {
		this.flagTuntutan = flagTuntutan;
	}

	public String getKelulusan1() {
		return kelulusan1;
	}

	public void setKelulusan1(String kelulusan1) {
		this.kelulusan1 = kelulusan1;
	}

	public String getKelulusan2() {
		return kelulusan2;
	}

	public void setKelulusan2(String kelulusan2) {
		this.kelulusan2 = kelulusan2;
	}

	public String getKelulusan3() {
		return kelulusan3;
	}

	public void setKelulusan3(String kelulusan3) {
		this.kelulusan3 = kelulusan3;
	}

	public int getPekerjaan() {
		return pekerjaan;
	}

	public void setPekerjaan(int pekerjaan) {
		this.pekerjaan = pekerjaan;
	}

	public int getPeribadi() {
		return peribadi;
	}

	public void setPeribadi(int peribadi) {
		this.peribadi = peribadi;
	}

	public int getPasangan() {
		return pasangan;
	}

	public void setPasangan(int pasangan) {
		this.pasangan = pasangan;
	}

	public int getPinjaman() {
		return pinjaman;
	}

	public void setPinjaman(int pinjaman) {
		this.pinjaman = pinjaman;
	}

	public void setDatePermohonan(Date datePermohonan) {
		this.datePermohonan = datePermohonan;
	}

	public Date getDatePermohonan() {
		return datePermohonan;
	}

}
