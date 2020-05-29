package bph.entities.portal;

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

@Entity
@Table(name = "kua_menunggu")
public class Giliran {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "noKP")
	private String noKP;

	// @Column(name = "noDaftar")
	// private String noDaftar;

	@ManyToOne
	@JoinColumn(name = "id_lokasi_permohonan")
	private LokasiPermohonan lokasiPermohonan;

	@Column(name = "jenis_kelas_kuarters")
	private String jenisKelasKuarters;

	@Column(name = "kelas_kuarters")
	private String kelasKuarters;

	@Column(name = "nogiliran")
	private String noGiliran;

	@Column(name = "flag_manual")
	private String flagManual;

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

	public String getNoKP() {
		return noKP;
	}

	public void setNoKP(String noKP) {
		this.noKP = noKP;
	}

	// public String getNoDaftar() {
	// return noDaftar;
	// }
	//
	// public void setNoDaftar(String noDaftar) {
	// this.noDaftar = noDaftar;
	// }

	public String getKelasKuarters() {
		return kelasKuarters;
	}

	public void setKelasKuarters(String kelasKuarters) {
		this.kelasKuarters = kelasKuarters;
	}

	public String getNoGiliran() {
		return noGiliran;
	}

	public void setNoGiliran(String noGiliran) {
		this.noGiliran = noGiliran;
	}

	public Giliran() {
		setTarikhMasuk(new Date());
		setId(UID.getUID());
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setLokasiPermohonan(LokasiPermohonan lokasiPermohonan) {
		this.lokasiPermohonan = lokasiPermohonan;
	}

	public LokasiPermohonan getLokasiPermohonan() {
		return lokasiPermohonan;
	}

	public String getJenisKelasKuarters() {
		return jenisKelasKuarters;
	}

	public void setJenisKelasKuarters(String jenisKelasKuarters) {
		this.jenisKelasKuarters = jenisKelasKuarters;
	}

	public String getFlagManual() {
		return flagManual;
	}

	public void setFlagManual(String flagManual) {
		this.flagManual = flagManual;
	}

	public Date getTarikhKemaskini() {
		return tarikhKemaskini;
	}

	public void setTarikhKemaskini(Date tarikhKemaskini) {
		this.tarikhKemaskini = tarikhKemaskini;
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
}
