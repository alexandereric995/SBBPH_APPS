package bph.entities.kod;

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
@Table(name = "ruj_tetapan_no_fail")
public class TetapanNoFail {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "format")
	private String format;
	
	@ManyToOne
	@JoinColumn(name = "id_seksyen")
	private Seksyen seksyen;
	
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

	public TetapanNoFail() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public Seksyen getSeksyen() {
		return seksyen;
	}

	public void setSeksyen(Seksyen seksyen) {
		this.seksyen = seksyen;
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

}
