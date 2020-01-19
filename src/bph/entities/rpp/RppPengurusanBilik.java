package bph.entities.rpp;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lebah.template.UID;
import portal.module.entity.Users;

@Entity
@Table(name="rpp_pengurusan_bilik")
public class RppPengurusanBilik {

	@Id
	@Column(name = "id")
	private String id;
	
	@OneToOne
	@JoinColumn(name = "id_permohonan")
	private RppPermohonan permohonan;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_daftar_masuk")
	private Date tarikhDaftarMasuk;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_daftar_keluar")
	private Date tarikhDaftarKeluar;
	
	@Column(name = "catatan_masuk")
	private String catatanMasuk;
	
	@Column(name = "catatan_keluar")
	private String catatanKeluar;
	
	@Column(name = "masa_daftar_jam")
	private int masaDaftarJam;
	
	@Column(name = "masa_daftar_minit")
	private int masaDaftarMinit;
	
	@Column(name = "masa_daftar_ampm")
	private String masaDaftarAmPm;
	
	@Column(name = "masa_keluar_jam")
	private int masaKeluarJam;
	
	@Column(name = "masa_keluar_minit")
	private int masaKeluarMinit;
	
	@Column(name = "masa_keluar_ampm")
	private String masaKeluarAmPm;
	
	@ManyToOne
	@JoinColumn(name = "id_masuk")
	private Users pegawaiDaftarMasuk;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_masuk")
	private Date tarikhMasuk;
	
	@ManyToOne
	@JoinColumn(name = "id_kemaskini")
	private Users pegawaiDaftarKeluar;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_kemaskini")
	private Date tarikhKemaskini;
	
	public RppPengurusanBilik() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public RppPermohonan getPermohonan() {
		return permohonan;
	}

	public void setPermohonan(RppPermohonan permohonan) {
		this.permohonan = permohonan;
	}

	public Date getTarikhDaftarMasuk() {
		return tarikhDaftarMasuk;
	}

	public void setTarikhDaftarMasuk(Date tarikhDaftarMasuk) {
		this.tarikhDaftarMasuk = tarikhDaftarMasuk;
	}

	public Date getTarikhDaftarKeluar() {
		return tarikhDaftarKeluar;
	}

	public void setTarikhDaftarKeluar(Date tarikhDaftarKeluar) {
		this.tarikhDaftarKeluar = tarikhDaftarKeluar;
	}

	public String getCatatanMasuk() {
		return catatanMasuk;
	}

	public void setCatatanMasuk(String catatanMasuk) {
		this.catatanMasuk = catatanMasuk;
	}

	public String getCatatanKeluar() {
		return catatanKeluar;
	}

	public void setCatatanKeluar(String catatanKeluar) {
		this.catatanKeluar = catatanKeluar;
	}

	public int getMasaDaftarJam() {
		return masaDaftarJam;
	}

	public void setMasaDaftarJam(int masaDaftarJam) {
		this.masaDaftarJam = masaDaftarJam;
	}

	public int getMasaDaftarMinit() {
		return masaDaftarMinit;
	}

	public void setMasaDaftarMinit(int masaDaftarMinit) {
		this.masaDaftarMinit = masaDaftarMinit;
	}
	
	public String getMasaDaftarAmPm() {
		return masaDaftarAmPm;
	}

	public void setMasaDaftarAmPm(String masaDaftarAmPm) {
		this.masaDaftarAmPm = masaDaftarAmPm;
	}

	public int getMasaKeluarJam() {
		return masaKeluarJam;
	}

	public void setMasaKeluarJam(int masaKeluarJam) {
		this.masaKeluarJam = masaKeluarJam;
	}

	public int getMasaKeluarMinit() {
		return masaKeluarMinit;
	}

	public void setMasaKeluarMinit(int masaKeluarMinit) {
		this.masaKeluarMinit = masaKeluarMinit;
	}

	public String getMasaKeluarAmPm() {
		return masaKeluarAmPm;
	}

	public void setMasaKeluarAmPm(String masaKeluarAmPm) {
		this.masaKeluarAmPm = masaKeluarAmPm;
	}

	public Users getPegawaiDaftarMasuk() {
		return pegawaiDaftarMasuk;
	}

	public void setPegawaiDaftarMasuk(Users pegawaiDaftarMasuk) {
		this.pegawaiDaftarMasuk = pegawaiDaftarMasuk;
	}

	public Date getTarikhMasuk() {
		return tarikhMasuk;
	}

	public void setTarikhMasuk(Date tarikhMasuk) {
		this.tarikhMasuk = tarikhMasuk;
	}

	public Users getPegawaiDaftarKeluar() {
		return pegawaiDaftarKeluar;
	}

	public void setPegawaiDaftarKeluar(Users pegawaiDaftarKeluar) {
		this.pegawaiDaftarKeluar = pegawaiDaftarKeluar;
	}

	public Date getTarikhKemaskini() {
		return tarikhKemaskini;
	}

	public void setTarikhKemaskini(Date tarikhKemaskini) {
		this.tarikhKemaskini = tarikhKemaskini;
	}

	
}
