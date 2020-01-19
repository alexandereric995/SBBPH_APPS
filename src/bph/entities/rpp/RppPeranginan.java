package bph.entities.rpp;


import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lebah.template.UID;
import bph.entities.kod.Bandar;
import bph.entities.kod.JenisBangunan;

@Entity
@Table(name="rpp_peranginan")
public class RppPeranginan {

	@Id
	@Column(name = "id")
	private String id;
	
	@Column(name = "id_premis")
	private String idPremis;
	
	@ManyToOne
	@JoinColumn(name = "id_jenis_peranginan")
	private JenisBangunan jenisPeranginan;
	
	@Column(name = "nama_peranginan")
	private String namaPeranginan;
	
	@Column(name = "kod_lokasi")
	private String kodLokasi;
	
	@Column(name = "kod_unit")
	private String kodUnit;
	
	@Column(name = "alamat_1")
	private String alamat1;
	
	@Column(name = "alamat_2")
	private String alamat2;
	
	@Column(name = "alamat_3")
	private String alamat3;
	
	@Column(name = "poskod")
	private String poskod;
	
	@ManyToOne
	@JoinColumn(name = "id_bandar")
	private Bandar bandar;
	
	@Column(name = "no_telefon")
	private String noTelefon;
	
	@Column(name = "no_faks")
	private String noFaks;
	
	@Column(name = "emel")
	private String emel;
	
	@Column(name = "catatan")
	private String catatan;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_buka_tempahan")
	private Date tarikhBukaTempahan;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_tutup_tempahan")
	private Date tarikhTutupTempahan;
	
	@Column(name = "flag_puncak")
	private String flagPuncak;
	
	@Column(name = "flag_kelulusan_sub")
	private String flagKelulusanSub;
	
	@Column(name = "flag_operator")
	private String flagOperator;
	
	@Column(name = "flag_kelompok")
	private String flagKelompok;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="peranginan", fetch=FetchType.EAGER)
	private List<RppKemudahan> listKemudahan;
	
	public RppPeranginan() {
		setId(UID.getUID());
		setFlagOperator("T");
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdPremis() {
		return idPremis;
	}

	public void setIdPremis(String idPremis) {
		this.idPremis = idPremis;
	}

	public JenisBangunan getJenisPeranginan() {
		return jenisPeranginan;
	}

	public void setJenisPeranginan(JenisBangunan jenisPeranginan) {
		this.jenisPeranginan = jenisPeranginan;
	}

	public String getNamaPeranginan() {
		return namaPeranginan;
	}

	public void setNamaPeranginan(String namaPeranginan) {
		this.namaPeranginan = namaPeranginan;
	}

	public String getAlamat1() {
		return alamat1;
	}

	public void setAlamat1(String alamat1) {
		this.alamat1 = alamat1;
	}

	public String getAlamat2() {
		return alamat2;
	}

	public void setAlamat2(String alamat2) {
		this.alamat2 = alamat2;
	}

	public String getAlamat3() {
		return alamat3;
	}

	public void setAlamat3(String alamat3) {
		this.alamat3 = alamat3;
	}

	public String getPoskod() {
		return poskod;
	}

	public void setPoskod(String poskod) {
		this.poskod = poskod;
	}

	public Bandar getBandar() {
		return bandar;
	}

	public void setBandar(Bandar bandar) {
		this.bandar = bandar;
	}

	public String getNoTelefon() {
		return noTelefon;
	}

	public void setNoTelefon(String noTelefon) {
		this.noTelefon = noTelefon;
	}

	public String getNoFaks() {
		return noFaks;
	}

	public void setNoFaks(String noFaks) {
		this.noFaks = noFaks;
	}

	public String getEmel() {
		return emel;
	}

	public void setEmel(String emel) {
		this.emel = emel;
	}

	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}

	public Date getTarikhBukaTempahan() {
		return tarikhBukaTempahan;
	}

	public void setTarikhBukaTempahan(Date tarikhBukaTempahan) {
		this.tarikhBukaTempahan = tarikhBukaTempahan;
	}

	public Date getTarikhTutupTempahan() {
		return tarikhTutupTempahan;
	}

	public void setTarikhTutupTempahan(Date tarikhTutupTempahan) {
		this.tarikhTutupTempahan = tarikhTutupTempahan;
	}

	public String getFlagPuncak() {
		return flagPuncak;
	}

	public void setFlagPuncak(String flagPuncak) {
		this.flagPuncak = flagPuncak;
	}

	public List<RppKemudahan> getListKemudahan() {
		return listKemudahan;
	}

	public void setListKemudahan(List<RppKemudahan> listKemudahan) {
		this.listKemudahan = listKemudahan;
	}

	public String getFlagKelulusanSub() {
		return flagKelulusanSub;
	}

	public void setFlagKelulusanSub(String flagKelulusanSub) {
		this.flagKelulusanSub = flagKelulusanSub;
	}

	public String getKodLokasi() {
		return kodLokasi;
	}

	public void setKodLokasi(String kodLokasi) {
		this.kodLokasi = kodLokasi;
	}

	public String getKodUnit() {
		return kodUnit;
	}

	public void setKodUnit(String kodUnit) {
		this.kodUnit = kodUnit;
	}

	public String getFlagOperator() {
		return flagOperator;
	}

	public void setFlagOperator(String flagOperator) {
		this.flagOperator = flagOperator;
	}
	
	public String getFlagKelompok() {
		return flagKelompok;
	}

	public void setFlagKelompok(String flagKelompok) {
		this.flagKelompok = flagKelompok;
	}
}
