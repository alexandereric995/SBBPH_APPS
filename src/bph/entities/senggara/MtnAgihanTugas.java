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
import bph.entities.kod.Status;

@Entity
@Table(name = "mtn_agihan_tugas")
public class MtnAgihanTugas {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_kuarters")
	private MtnKuarters kuarters;

	@ManyToOne
	@JoinColumn(name = "id_inden_kerja")
	private MtnIndenKerja indenKerja;

	@ManyToOne
	@JoinColumn(name = "id_pegawai_agihan")
	private Users pegawaiAgihan;

	@ManyToOne
	@JoinColumn(name = "id_pegawai_tugasan")
	private Users pegawaiTugasan;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_tugasan")
	private Date tarikhTugasan;

	@Column(name = "catatan")
	private String catatan;

	@ManyToOne
	@JoinColumn(name = "id_status")
	private Status status;

	@Column(name = "flag_aktif")
	private String flagAktif;

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

	public MtnAgihanTugas() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public MtnKuarters getKuarters() {
		return kuarters;
	}

	public void setKuarters(MtnKuarters kuarters) {
		this.kuarters = kuarters;
	}

	public MtnIndenKerja getIndenKerja() {
		return indenKerja;
	}

	public void setIndenKerja(MtnIndenKerja indenKerja) {
		this.indenKerja = indenKerja;
	}

	public Users getPegawaiAgihan() {
		return pegawaiAgihan;
	}

	public void setPegawaiAgihan(Users pegawaiAgihan) {
		this.pegawaiAgihan = pegawaiAgihan;
	}

	public Users getPegawaiTugasan() {
		return pegawaiTugasan;
	}

	public void setPegawaiTugasan(Users pegawaiTugasan) {
		this.pegawaiTugasan = pegawaiTugasan;
	}

	public Date getTarikhTugasan() {
		return tarikhTugasan;
	}

	public void setTarikhTugasan(Date tarikhTugasan) {
		this.tarikhTugasan = tarikhTugasan;
	}

	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
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
