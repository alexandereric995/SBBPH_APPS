package bph.entities.pembangunan;

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
@Table(name = "dev_cadangan")
public class DevCadangan {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_hakmilik")
	private DevHakmilik hakmilik;

	@Column(name = "nama_projek")
	private String namaProjek;

	@Column(name = "keterangan")
	private String keterangan;

	@Column(name = "no_rujukan")
	private String noRujukan;

	@Column(name = "kontraktor")
	private String kontraktor;

	@Column(name = "harga_kontrak")
	private Double hargaKontrak;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_milik_tapak")
	private Date tarikhMilikTapak;

	@Column(name = "tempoh_pelaksanaan")
	private String tempohPelaksanaan;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_siap_asal")
	private Date tarikhSiapAsal;

	@Column(name = "tempoh_tanggungan")
	private String tempohTanggungan;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_siap_semasa")
	private Date tarikhSiapSemasa;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_siap_sebenar")
	private Date tarikhSiapSebenar;

	@Column(name = "catatan_pelaksanaan")
	private String catatanPelaksanaan;

	@Column(name = "status_cadangan")
	private String statusCadangan;

	@Column(name = "flag_aktif")
	private String flagAktif;

	@ManyToOne
	@JoinColumn(name = "id_masuk")
	private Users idMasuk;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_masuk")
	private Date tarikhMasuk;

	@ManyToOne
	@JoinColumn(name = "id_kemaskini")
	private Users idKemaskini;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_kemaskini")
	private Date tarikhKemaskini;

	public DevCadangan() {
		setId(UID.getUID());
		setFlagAktif("Y");
		setTarikhMasuk(new Date());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DevHakmilik getHakmilik() {
		return hakmilik;
	}

	public void setHakmilik(DevHakmilik hakmilik) {
		this.hakmilik = hakmilik;
	}

	public String getNamaProjek() {
		return namaProjek;
	}

	public void setNamaProjek(String namaProjek) {
		this.namaProjek = namaProjek;
	}

	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}

	public String getNoRujukan() {
		return noRujukan;
	}

	public void setNoRujukan(String noRujukan) {
		this.noRujukan = noRujukan;
	}

	public String getKontraktor() {
		return kontraktor;
	}

	public void setKontraktor(String kontraktor) {
		this.kontraktor = kontraktor;
	}

	public Double getHargaKontrak() {
		return hargaKontrak;
	}

	public void setHargaKontrak(Double hargaKontrak) {
		this.hargaKontrak = hargaKontrak;
	}

	public Date getTarikhMilikTapak() {
		return tarikhMilikTapak;
	}

	public void setTarikhMilikTapak(Date tarikhMilikTapak) {
		this.tarikhMilikTapak = tarikhMilikTapak;
	}

	public String getTempohPelaksanaan() {
		return tempohPelaksanaan;
	}

	public void setTempohPelaksanaan(String tempohPelaksanaan) {
		this.tempohPelaksanaan = tempohPelaksanaan;
	}

	public Date getTarikhSiapAsal() {
		return tarikhSiapAsal;
	}

	public void setTarikhSiapAsal(Date tarikhSiapAsal) {
		this.tarikhSiapAsal = tarikhSiapAsal;
	}

	public String getTempohTanggungan() {
		return tempohTanggungan;
	}

	public void setTempohTanggungan(String tempohTanggungan) {
		this.tempohTanggungan = tempohTanggungan;
	}

	public Date getTarikhSiapSemasa() {
		return tarikhSiapSemasa;
	}

	public void setTarikhSiapSemasa(Date tarikhSiapSemasa) {
		this.tarikhSiapSemasa = tarikhSiapSemasa;
	}

	public Date getTarikhSiapSebenar() {
		return tarikhSiapSebenar;
	}

	public void setTarikhSiapSebenar(Date tarikhSiapSebenar) {
		this.tarikhSiapSebenar = tarikhSiapSebenar;
	}

	public String getCatatanPelaksanaan() {
		return catatanPelaksanaan;
	}

	public void setCatatanPelaksanaan(String catatanPelaksanaan) {
		this.catatanPelaksanaan = catatanPelaksanaan;
	}

	public String getStatusCadangan() {
		return statusCadangan;
	}

	public void setStatusCadangan(String statusCadangan) {
		this.statusCadangan = statusCadangan;
	}

	public String getFlagAktif() {
		return flagAktif;
	}

	public void setFlagAktif(String flagAktif) {
		this.flagAktif = flagAktif;
	}

	public Users getIdMasuk() {
		return idMasuk;
	}

	public void setIdMasuk(Users idMasuk) {
		this.idMasuk = idMasuk;
	}

	public Date getTarikhMasuk() {
		return tarikhMasuk;
	}

	public void setTarikhMasuk(Date tarikhMasuk) {
		this.tarikhMasuk = tarikhMasuk;
	}

	public Users getIdKemaskini() {
		return idKemaskini;
	}

	public void setIdKemaskini(Users idKemaskini) {
		this.idKemaskini = idKemaskini;
	}

	public Date getTarikhKemaskini() {
		return tarikhKemaskini;
	}

	public void setTarikhKemaskini(Date tarikhKemaskini) {
		this.tarikhKemaskini = tarikhKemaskini;
	}
}
