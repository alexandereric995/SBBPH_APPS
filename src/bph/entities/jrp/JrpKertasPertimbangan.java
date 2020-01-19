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
@Table(name = "jrp_kertas_pertimbangan")
public class JrpKertasPertimbangan {

	@Id
	@Column(name = "id")
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "id_permohonan")
	private JrpPermohonan jrpPermohonan;
	
	@Column(name = "bil_mesyuarat")
	private String bilMesyuarat;
	
	@Column(name = "no_daftar")
	private String noDaftar;
	
	@Column(name = "flag_syor_bersyarat")
	private String flagSyorBersyarat;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_lengkap_permohonan")
	private Date tarikhLengkapPermohonan;
	
	@Column(name = "catatan")
	private String catatan;
	
	@OneToOne
	@JoinColumn(name = "disediakan_oleh")
	private Users disediakanOleh;
	
	@OneToOne
	@JoinColumn(name = "disemak_oleh")
	private Users disemakOleh;
	
	@OneToOne
	@JoinColumn(name = "disahkan_oleh")
	private Users disahkanOleh;
	
	@OneToOne
	@JoinColumn(name = "id_masuk")
	private Users idMasuk;
	
	@OneToOne
	@JoinColumn(name = "id_kemaskini")
	private Users idKemaskini;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_masuk")
	private Date tarikhMasuk;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_kemaskini")
	private Date tarikhKemaskini;
	
	@Column(name = "ulasan_ketua_urusetia")
	private String ulasanKetuaUrusetia;
	
	@Column(name = "ulasan_penolong_urusetia")
	private String ulasanPenolongUrusetia;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_semakan")
	private Date tarikhSemakan;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_pengesahan")
	private Date tarikhPengesahan;
	
	@Column(name = "flag_pindaan")
	private String flagPindaan;
	
	@Column(name = "flag_aktif")
	private String flagAktif;
	
	@Column(name = "ulasan_urusetia")
	private String ulasanUrusetia;
	
	public JrpKertasPertimbangan() {
		setId(UID.getUID());
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

	public String getNoDaftar() {
		return noDaftar;
	}

	public void setNoDaftar(String noDaftar) {
		this.noDaftar = noDaftar;
	}

	public String getFlagSyorBersyarat() {
		return flagSyorBersyarat;
	}

	public void setFlagSyorBersyarat(String flagSyorBersyarat) {
		this.flagSyorBersyarat = flagSyorBersyarat;
	}

	public Date getTarikhLengkapPermohonan() {
		return tarikhLengkapPermohonan;
	}

	public void setTarikhLengkapPermohonan(Date tarikhLengkapPermohonan) {
		this.tarikhLengkapPermohonan = tarikhLengkapPermohonan;
	}

	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
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

	public String getUlasanKetuaUrusetia() {
		return ulasanKetuaUrusetia;
	}

	public void setUlasanKetuaUrusetia(String ulasanKetuaUrusetia) {
		this.ulasanKetuaUrusetia = ulasanKetuaUrusetia;
	}

	public String getUlasanPenolongUrusetia() {
		return ulasanPenolongUrusetia;
	}

	public void setUlasanPenolongUrusetia(String ulasanPenolongUrusetia) {
		this.ulasanPenolongUrusetia = ulasanPenolongUrusetia;
	}

	public Date getTarikhSemakan() {
		return tarikhSemakan;
	}

	public void setTarikhSemakan(Date tarikhSemakan) {
		this.tarikhSemakan = tarikhSemakan;
	}

	public Date getTarikhPengesahan() {
		return tarikhPengesahan;
	}

	public void setTarikhPengesahan(Date tarikhPengesahan) {
		this.tarikhPengesahan = tarikhPengesahan;
	}

	public Users getDisediakanOleh() {
		return disediakanOleh;
	}

	public void setDisediakanOleh(Users disediakanOleh) {
		this.disediakanOleh = disediakanOleh;
	}

	public Users getDisemakOleh() {
		return disemakOleh;
	}

	public void setDisemakOleh(Users disemakOleh) {
		this.disemakOleh = disemakOleh;
	}

	public Users getDisahkanOleh() {
		return disahkanOleh;
	}

	public void setDisahkanOleh(Users disahkanOleh) {
		this.disahkanOleh = disahkanOleh;
	}

	public String getFlagPindaan() {
		return flagPindaan;
	}

	public void setFlagPindaan(String flagPindaan) {
		this.flagPindaan = flagPindaan;
	}

	public String getFlagAktif() {
		return flagAktif;
	}

	public void setFlagAktif(String flagAktif) {
		this.flagAktif = flagAktif;
	}

	public String getUlasanUrusetia() {
		return ulasanUrusetia;
	}

	public void setUlasanUrusetia(String ulasanUrusetia) {
		this.ulasanUrusetia = ulasanUrusetia;
	}
}
