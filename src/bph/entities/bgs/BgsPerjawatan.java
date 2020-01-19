package bph.entities.bgs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lebah.template.UID;

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
	
	public BgsPerjawatan() {
		setId(UID.getUID());
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
}