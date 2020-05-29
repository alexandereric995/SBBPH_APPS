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
@Table(name = "bgs_perjawatan")
public class BgsPerjawatan {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_permohonan")
	private BgsPermohonan permohonan;

	@Column(name = "jawatan")
	private String jawatan;

	@Column(name = "gred")
	private String gred;

	@Column(name = "bilangan_semasa")
	private String bilanganSemasa;

	@Column(name = "luas_semasa")
	private String luasSemasa;

	@Column(name = "bilangan_akan_datang")
	private String bilanganAkanDatang;

	@Column(name = "luas_akan_datang")
	private String luasAkanDatang;

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

	public BgsPerjawatan() {
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

	public String getJawatan() {
		return jawatan;
	}

	public void setJawatan(String jawatan) {
		this.jawatan = jawatan;
	}

	public String getGred() {
		return gred;
	}

	public void setGred(String gred) {
		this.gred = gred;
	}

	public String getBilanganSemasa() {
		return bilanganSemasa;
	}

	public void setBilanganSemasa(String bilanganSemasa) {
		this.bilanganSemasa = bilanganSemasa;
	}

	public String getLuasSemasa() {
		return luasSemasa;
	}

	public void setLuasSemasa(String luasSemasa) {
		this.luasSemasa = luasSemasa;
	}

	public String getBilanganAkanDatang() {
		return bilanganAkanDatang;
	}

	public void setBilanganAkanDatang(String bilanganAkanDatang) {
		this.bilanganAkanDatang = bilanganAkanDatang;
	}

	public String getLuasAkanDatang() {
		return luasAkanDatang;
	}

	public void setLuasAkanDatang(String luasAkanDatang) {
		this.luasAkanDatang = luasAkanDatang;
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