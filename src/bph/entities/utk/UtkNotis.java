package bph.entities.utk;

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
@Table(name = "utk_notis")
public class UtkNotis {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_kesalahan")
	private UtkKesalahan kesalahan;

	@ManyToOne
	@JoinColumn(name = "id_hilang_kelayakan")
	private UtkHilangKelayakan hilangKelayakan;

	@ManyToOne
	@JoinColumn(name = "id_abt")
	private UtkAbt abt;

	@ManyToOne
	@JoinColumn(name = "id_naziran_kebersihan")
	private UtkNaziranKebersihan naziranKebersihan;

	@Column(name = "no_siri")
	private String noSiri;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_hantar")
	private Date tarikhHantar;

	@Column(name = "flag_jenis_notis")
	private String flagJenisNotis;

	@Column(name = "catatan")
	private String catatan;

	@Column(name = "flag_peringatan")
	private String flagPeringatan;

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

	public UtkNotis() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public UtkKesalahan getKesalahan() {
		return kesalahan;
	}

	public void setKesalahan(UtkKesalahan kesalahan) {
		this.kesalahan = kesalahan;
	}

	public Date getTarikhHantar() {
		return tarikhHantar;
	}

	public void setTarikhHantar(Date tarikhHantar) {
		this.tarikhHantar = tarikhHantar;
	}

	public String getFlagJenisNotis() {
		return flagJenisNotis;
	}

	public void setFlagJenisNotis(String flagJenisNotis) {
		this.flagJenisNotis = flagJenisNotis;
	}

	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}

	public String getFlagPeringatan() {
		return flagPeringatan;
	}

	public void setFlagPeringatan(String flagPeringatan) {
		this.flagPeringatan = flagPeringatan;
	}

	public String getNoSiri() {
		return noSiri;
	}

	public void setNoSiri(String noSiri) {
		this.noSiri = noSiri;
	}

	public UtkHilangKelayakan getHilangKelayakan() {
		return hilangKelayakan;
	}

	public void setHilangKelayakan(UtkHilangKelayakan hilangKelayakan) {
		this.hilangKelayakan = hilangKelayakan;
	}

	public UtkAbt getAbt() {
		return abt;
	}

	public void setAbt(UtkAbt abt) {
		this.abt = abt;
	}

	public UtkNaziranKebersihan getNaziranKebersihan() {
		return naziranKebersihan;
	}

	public void setNaziranKebersihan(UtkNaziranKebersihan naziranKebersihan) {
		this.naziranKebersihan = naziranKebersihan;
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
