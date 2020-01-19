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
 * @author roszaineza
 * untuk cawangan dewan dan gelanggang (17/7/2017)
 */

@Entity
@Table(name = "ruj_kod_petugas")
public class KodPetugas {

	@Id
	@Column(name = "id")
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "id_cawangan")
	private KodPusatTerima cawangan;
	
	@Column(name = "kod_cawangan")
	private String kodCawangan;
	
	@ManyToOne
	@JoinColumn(name = "id_petugas")
	private Users petugas;

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
	
	public KodPetugas() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());
	}	
	
//	public String getKeteranganFlagAktif() {
//		String status = "";
//		if (this.flagAktif != null) {
//			if (this.flagAktif.equals("Y")) {
//				status = "AKTIF";
//			} else if (this.flagAktif.equals("T")) {
//				status = "TIDAK AKTIF";
//			}
//		}
//		return status;
//	}

	public String getId() {
		return id;
	}

	public KodPusatTerima getCawangan() {
		return cawangan;
	}

	public void setCawangan(KodPusatTerima cawangan) {
		this.cawangan = cawangan;
	}

	public String getKodCawangan() {
		return kodCawangan;
	}

	public void setKodCawangan(String kodCawangan) {
		this.kodCawangan = kodCawangan;
	}

	public Users getPetugas() {
		return petugas;
	}

	public void setPetugas(Users petugas) {
		this.petugas = petugas;
	}

	public void setId(String id) {
		this.id = id;
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
}
