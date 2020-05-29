package bph.entities.senggara;

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

@Entity
@Table(name = "mtn_daftar_kontraktor")
public class MtnDaftarKontraktor {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "tarikh_pendaftaran")
	@Temporal(TemporalType.DATE)
	private Date tarikhPendaftaran;

	@ManyToOne
	@JoinColumn(name = "no_pendaftaran")
	private MtnKontraktor kontraktor;

	@Column(name = "tahun")
	private int tahun;

	@Column(name = "tarikh_terima_profil")
	@Temporal(TemporalType.DATE)
	private Date tarikhTerimaProfil;

	@Column(name = "no_profil")
	private String noProfil;

	@Column(name = "turutan")
	private int turutan;

	@Column(name = "turutan_elektrik")
	private int turutanElektrik;

	@Column(name = "flag_hadir_cabutan")
	private String flagHadirCabutan;

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

	public MtnDaftarKontraktor() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getTarikhPendaftaran() {
		return tarikhPendaftaran;
	}

	public void setTarikhPendaftaran(Date tarikhPendaftaran) {
		this.tarikhPendaftaran = tarikhPendaftaran;
	}

	public MtnKontraktor getKontraktor() {
		return kontraktor;
	}

	public void setKontraktor(MtnKontraktor kontraktor) {
		this.kontraktor = kontraktor;
	}

	public int getTahun() {
		return tahun;
	}

	public void setTahun(int tahun) {
		this.tahun = tahun;
	}

	public Date getTarikhTerimaProfil() {
		return tarikhTerimaProfil;
	}

	public void setTarikhTerimaProfil(Date tarikhTerimaProfil) {
		this.tarikhTerimaProfil = tarikhTerimaProfil;
	}

	public String getNoProfil() {
		return noProfil;
	}

	public void setNoProfil(String noProfil) {
		this.noProfil = noProfil;
	}

	public int getTurutan() {
		return turutan;
	}

	public void setTurutan(int turutan) {
		this.turutan = turutan;
	}

	public int getTurutanElektrik() {
		return turutanElektrik;
	}

	public void setTurutanElektrik(int turutanElektrik) {
		this.turutanElektrik = turutanElektrik;
	}

	public String getFlagHadirCabutan() {
		return flagHadirCabutan;
	}

	public void setFlagHadirCabutan(String flagHadirCabutan) {
		this.flagHadirCabutan = flagHadirCabutan;
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
