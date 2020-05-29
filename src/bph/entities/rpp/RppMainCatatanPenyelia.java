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
import portal.module.entity.Users;

@Entity
@Table(name = "rpp_main_catatan_penyelia")
public class RppMainCatatanPenyelia {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_peranginan")
	private RppPeranginan peranginan;

	@Column(name = "tajuk")
	private String tajuk;

	@Column(name = "catatan")
	private String catatan;

	@Column(name = "tarikh_catatan")
	@Temporal(TemporalType.DATE)
	private Date tarikhCatatan;

	@ManyToOne
	@JoinColumn(name = "id_pendaftar")
	private Users pendaftar;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "mainCatatanPenyelia", fetch = FetchType.LAZY)
	private List<RppListCatatan> listCatatan;

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

	public RppMainCatatanPenyelia() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public RppPeranginan getPeranginan() {
		return peranginan;
	}

	public void setPeranginan(RppPeranginan peranginan) {
		this.peranginan = peranginan;
	}

	public String getTajuk() {
		return tajuk;
	}

	public void setTajuk(String tajuk) {
		this.tajuk = tajuk;
	}

	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}

	public Users getPendaftar() {
		return pendaftar;
	}

	public void setPendaftar(Users pendaftar) {
		this.pendaftar = pendaftar;
	}

	public List<RppListCatatan> getListCatatan() {
		return listCatatan;
	}

	public void setListCatatan(List<RppListCatatan> listCatatan) {
		this.listCatatan = listCatatan;
	}

	public Date getTarikhCatatan() {
		return tarikhCatatan;
	}

	public void setTarikhCatatan(Date tarikhCatatan) {
		this.tarikhCatatan = tarikhCatatan;
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
