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
@Table(name = "mtn_kontraktor_senarai_hitam")
public class MtnKontraktorSenaraiHitam {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "no_pendaftaran")
	private MtnKontraktor kontraktor;

	@Column(name = "tarikh_mula")
	@Temporal(TemporalType.DATE)
	private Date tarikhMula;

	@Column(name = "tarikh_tamat")
	@Temporal(TemporalType.DATE)
	private Date tarikhTamat;

	@Column(name = "tarikh_bebas")
	@Temporal(TemporalType.DATE)
	private Date tarikhBebas;

	@Column(name = "gred_prestasi")
	private String gredPrestasi;

	@Column(name = "flag_aktif")
	private String flagAktif;

	@Column(name = "sebab")
	private String sebab;

	@Column(name = "catatan")
	private String catatan;

	@ManyToOne
	@JoinColumn(name = "id_pegawai_bebas")
	private Users pegawaiBebas;

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

	public MtnKontraktorSenaraiHitam() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());

	}

	public String getKeteranganFlagAktif() {
		if (this.flagAktif.equals("Y")) {
			return "AKTIF";
		} else {
			return "TIDAK";
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public MtnKontraktor getKontraktor() {
		return kontraktor;
	}

	public void setKontraktor(MtnKontraktor kontraktor) {
		this.kontraktor = kontraktor;
	}

	public Date getTarikhMula() {
		return tarikhMula;
	}

	public void setTarikhMula(Date tarikhMula) {
		this.tarikhMula = tarikhMula;
	}

	public Date getTarikhTamat() {
		return tarikhTamat;
	}

	public void setTarikhTamat(Date tarikhTamat) {
		this.tarikhTamat = tarikhTamat;
	}

	public Date getTarikhBebas() {
		return tarikhBebas;
	}

	public void setTarikhBebas(Date tarikhBebas) {
		this.tarikhBebas = tarikhBebas;
	}

	public String getGredPrestasi() {
		return gredPrestasi;
	}

	public void setGredPrestasi(String gredPrestasi) {
		this.gredPrestasi = gredPrestasi;
	}

	public String getFlagAktif() {
		return flagAktif;
	}

	public void setFlagAktif(String flagAktif) {
		this.flagAktif = flagAktif;
	}

	public String getSebab() {
		return sebab;
	}

	public void setSebab(String sebab) {
		this.sebab = sebab;
	}

	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}

	public Users getPegawaiBebas() {
		return pegawaiBebas;
	}

	public void setPegawaiBebas(Users pegawaiBebas) {
		this.pegawaiBebas = pegawaiBebas;
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
