package bph.entities.kod;

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
import portal.module.entity.Users;

@Entity
@Table(name = "ruj_kod_pusat_terima")
public class KodPusatTerima {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "unit")
	private String unit;

	@Column(name = "kod_pusat_terima")
	private String kodPusatTerima;
	
	@Column(name = "kod_penyata_pemungut")
	private String kodPenyataPemungut;
	
	@Column(name = "flag_aktif")
	private String flagAktif;
	
	@Column(name = "catatan")
	private String catatan;
	
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
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="pusatTerima", fetch=FetchType.EAGER)
	private List<KodJuruwang> listJuruwang;
	
	public KodPusatTerima() {
		setId(UID.getUID());
		setFlagAktif("Y");
		setTarikhMasuk(new Date());
	}
	
	public String getKeteranganFlagAktif() {
		String status = "";
		if (this.flagAktif != null) {
			if (this.flagAktif.equals("Y")) {
				status = "AKTIF";
			} else if (this.flagAktif.equals("T")) {
				status = "TIDAK AKTIF";
			}
		}
		return status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getKodPusatTerima() {
		return kodPusatTerima;
	}

	public void setKodPusatTerima(String kodPusatTerima) {
		this.kodPusatTerima = kodPusatTerima;
	}

	public String getKodPenyataPemungut() {
		return kodPenyataPemungut;
	}

	public void setKodPenyataPemungut(String kodPenyataPemungut) {
		this.kodPenyataPemungut = kodPenyataPemungut;
	}

	public String getFlagAktif() {
		return flagAktif;
	}

	public void setFlagAktif(String flagAktif) {
		this.flagAktif = flagAktif;
	}

	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
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

	public List<KodJuruwang> getListJuruwang() {
		return listJuruwang;
	}

	public void setListJuruwang(List<KodJuruwang> listJuruwang) {
		this.listJuruwang = listJuruwang;
	}
}
