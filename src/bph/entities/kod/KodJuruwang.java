package bph.entities.kod;

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

/**
 * @author muhdsyazreen
 */

@Entity
@Table(name = "ruj_kod_juruwang")
public class KodJuruwang {

	@Id
	@Column(name = "id")
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "id_kod_pusat_terima")
	private KodPusatTerima pusatTerima;
	
	@Column(name = "kod_pusat_terima")
	private String kodPusatTerima;
	
	@ManyToOne
	@JoinColumn(name = "id_juruwang")
	private Users juruwang;
	
	@Column(name = "jawatan")
	private String jawatan;

	@Column(name = "kod_juruwang")
	private String kodJuruwang;

	@Column(name = "flag_aktif")
	private String flagAktif;

	@Column(name = "catatan")
	private String catatan;
	
	@Column(name = "flag_juruwang")
	private String flagJuruwang; //KEWANGAN / IR
	
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
	
	public KodJuruwang() {
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

	public KodPusatTerima getPusatTerima() {
		return pusatTerima;
	}

	public void setPusatTerima(KodPusatTerima pusatTerima) {
		this.pusatTerima = pusatTerima;
	}

	public String getKodPusatTerima() {
		return kodPusatTerima;
	}

	public void setKodPusatTerima(String kodPusatTerima) {
		this.kodPusatTerima = kodPusatTerima;
	}

	public Users getJuruwang() {
		return juruwang;
	}

	public void setJuruwang(Users juruwang) {
		this.juruwang = juruwang;
	}

	public String getJawatan() {
		return jawatan;
	}

	public void setJawatan(String jawatan) {
		this.jawatan = jawatan;
	}

	public String getKodJuruwang() {
		return kodJuruwang;
	}

	public void setKodJuruwang(String kodJuruwang) {
		this.kodJuruwang = kodJuruwang;
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
	
	public String getFlagJuruwang() {
		return flagJuruwang;
	}

	public void setFlagJuruwang(String flagJuruwang) {
		this.flagJuruwang = flagJuruwang;
	}
}
