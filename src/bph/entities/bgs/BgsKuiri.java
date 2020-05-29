package bph.entities.bgs;

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
@Table(name = "bgs_kuiri")
public class BgsKuiri {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_permohonan")
	private BgsPermohonan permohonan;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_kuiri")
	private Date tarikhKuiri;

	@Column(name = "kuiri")
	private String kuiri;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_jawapan")
	private Date tarikhJawapan;

	@Column(name = "jawapan")
	private String jawapan;

	@Column(name = "flag_jawapan")
	private String flagJawapan;

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

	public BgsKuiri() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BgsPermohonan getPermohonan() {
		return permohonan;
	}

	public void setPermohonan(BgsPermohonan permohonan) {
		this.permohonan = permohonan;
	}

	public Date getTarikhKuiri() {
		return tarikhKuiri;
	}

	public void setTarikhKuiri(Date tarikhKuiri) {
		this.tarikhKuiri = tarikhKuiri;
	}

	public String getKuiri() {
		return kuiri;
	}

	public void setKuiri(String kuiri) {
		this.kuiri = kuiri;
	}

	public Date getTarikhJawapan() {
		return tarikhJawapan;
	}

	public void setTarikhJawapan(Date tarikhJawapan) {
		this.tarikhJawapan = tarikhJawapan;
	}

	public String getJawapan() {
		return jawapan;
	}

	public void setJawapan(String jawapan) {
		this.jawapan = jawapan;
	}

	public String getFlagJawapan() {
		return flagJawapan;
	}

	public void setFlagJawapan(String flagJawapan) {
		this.flagJawapan = flagJawapan;
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
