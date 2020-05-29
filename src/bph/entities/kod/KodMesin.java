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
 * @author nurasna
 */

@Entity
@Table(name = "ruj_kod_mesin")
public class KodMesin {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "kod_mesin")
	private String kodMesin;

	@ManyToOne
	@JoinColumn(name = "id_pemilik")
	private Users pemilik;

	@Column(name = "kod_pusat_terima")
	private String kodPusatTerima;

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

	public KodMesin() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKodPusatTerima() {
		return kodPusatTerima;
	}

	public void setKodPusatTerima(String kodPusatTerima) {
		this.kodPusatTerima = kodPusatTerima;
	}

	public String getKodMesin() {
		return kodMesin;
	}

	public void setKodMesin(String kodMesin) {
		this.kodMesin = kodMesin;
	}

	public Users getPemilik() {
		return pemilik;
	}

	public void setPemilik(Users pemilik) {
		this.pemilik = pemilik;
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
