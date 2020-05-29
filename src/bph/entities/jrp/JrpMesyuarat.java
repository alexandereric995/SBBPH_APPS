package bph.entities.jrp;

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
@Table(name = "jrp_mesyuarat")
public class JrpMesyuarat {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_permohonan")
	private JrpPermohonan jrpPermohonan;

	@Column(name = "bil_mesyuarat")
	private String bilMesyuarat;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_mesyuarat")
	private Date tarikhMesyuarat;

	@Column(name = "ulasan")
	private String ulasan;

	@Column(name = "flag_keputusan")
	private String flagKeputusan;

	@OneToOne
	@JoinColumn(name = "urusetia_penyedia")
	private Users urusetiaPenyedia;

	@OneToOne
	@JoinColumn(name = "urusetia_pengesah")
	private Users urusetiaPengesah;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_sah")
	private Date tarikhSah;

	@OneToOne
	@JoinColumn(name = "id_masuk")
	private Users idMasuk;

	@OneToOne
	@JoinColumn(name = "id_kemaskini")
	private Users idKemaskini;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_masuk")
	private Date tarikhMasuk;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_kemaskini")
	private Date tarikhKemaskini;

	@Column(name = "ulasan_urusetia_pengesah")
	private String ulasanUrusetiaPengesah;

	public JrpMesyuarat() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public JrpPermohonan getJrpPermohonan() {
		return jrpPermohonan;
	}

	public void setJrpPermohonan(JrpPermohonan jrpPermohonan) {
		this.jrpPermohonan = jrpPermohonan;
	}

	public String getBilMesyuarat() {
		return bilMesyuarat;
	}

	public void setBilMesyuarat(String bilMesyuarat) {
		this.bilMesyuarat = bilMesyuarat;
	}

	public Date getTarikhMesyuarat() {
		return tarikhMesyuarat;
	}

	public void setTarikhMesyuarat(Date tarikhMesyuarat) {
		this.tarikhMesyuarat = tarikhMesyuarat;
	}

	public String getUlasan() {
		return ulasan;
	}

	public void setUlasan(String ulasan) {
		this.ulasan = ulasan;
	}

	public Users getIdMasuk() {
		return idMasuk;
	}

	public void setIdMasuk(Users idMasuk) {
		this.idMasuk = idMasuk;
	}

	public Users getIdKemaskini() {
		return idKemaskini;
	}

	public void setIdKemaskini(Users idKemaskini) {
		this.idKemaskini = idKemaskini;
	}

	public Date getTarikhMasuk() {
		return tarikhMasuk;
	}

	public void setTarikhMasuk(Date tarikhMasuk) {
		this.tarikhMasuk = tarikhMasuk;
	}

	public Date getTarikhKemaskini() {
		return tarikhKemaskini;
	}

	public void setTarikhKemaskini(Date tarikhKemaskini) {
		this.tarikhKemaskini = tarikhKemaskini;
	}

	public Date getTarikhSah() {
		return tarikhSah;
	}

	public void setTarikhSah(Date tarikhSah) {
		this.tarikhSah = tarikhSah;
	}

	public String getFlagKeputusan() {
		return flagKeputusan;
	}

	public void setFlagKeputusan(String flagKeputusan) {
		this.flagKeputusan = flagKeputusan;
	}

	public Users getUrusetiaPenyedia() {
		return urusetiaPenyedia;
	}

	public void setUrusetiaPenyedia(Users urusetiaPenyedia) {
		this.urusetiaPenyedia = urusetiaPenyedia;
	}

	public Users getUrusetiaPengesah() {
		return urusetiaPengesah;
	}

	public void setUrusetiaPengesah(Users urusetiaPengesah) {
		this.urusetiaPengesah = urusetiaPengesah;
	}

	public String getUlasanUrusetiaPengesah() {
		return ulasanUrusetiaPengesah;
	}

	public void setUlasanUrusetiaPengesah(String ulasanUrusetiaPengesah) {
		this.ulasanUrusetiaPengesah = ulasanUrusetiaPengesah;
	}

}
