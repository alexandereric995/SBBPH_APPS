package bph.entities.rpp;

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
import bph.entities.kod.Status;

@Entity
@Table(name = "rpp_permohonan_bayaran_balik")
public class RppPermohonanBayaranBalik {

	@Id
	@Column(name = "id")
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "id_permohonan")
	private RppPermohonan permohonan;

	@Column(name = "sebab")
	private String sebab;

	@Column(name = "tarikh_permohonan")
	@Temporal(TemporalType.DATE)
	private Date tarikhPermohonan;
	
	@Column(name = "amaun")
	private Double amaun;
	
	@Column(name = "tarikh_bayaran")
	@Temporal(TemporalType.DATE)
	private Date tarikhBayaran;

	@Column(name = "no_rujukan_bayaran")
	private String noRujukanBayaran;

	@ManyToOne
	@JoinColumn(name = "status")
	private Status status;
	
	@Column(name = "catatan")
	private String catatan;
	
	public RppPermohonanBayaranBalik() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public RppPermohonan getPermohonan() {
		return permohonan;
	}

	public void setPermohonan(RppPermohonan permohonan) {
		this.permohonan = permohonan;
	}

	public String getSebab() {
		return sebab;
	}

	public void setSebab(String sebab) {
		this.sebab = sebab;
	}

	public Date getTarikhPermohonan() {
		return tarikhPermohonan;
	}

	public void setTarikhPermohonan(Date tarikhPermohonan) {
		this.tarikhPermohonan = tarikhPermohonan;
	}

	public Double getAmaun() {
		return amaun;
	}

	public void setAmaun(Double amaun) {
		this.amaun = amaun;
	}

	public Date getTarikhBayaran() {
		return tarikhBayaran;
	}

	public void setTarikhBayaran(Date tarikhBayaran) {
		this.tarikhBayaran = tarikhBayaran;
	}

	public String getNoRujukanBayaran() {
		return noRujukanBayaran;
	}

	public void setNoRujukanBayaran(String noRujukanBayaran) {
		this.noRujukanBayaran = noRujukanBayaran;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}
}
