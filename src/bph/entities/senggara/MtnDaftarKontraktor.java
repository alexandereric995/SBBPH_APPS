
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

	public MtnDaftarKontraktor() {
		setId(UID.getUID());
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
}
