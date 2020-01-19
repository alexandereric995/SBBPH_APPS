package bph.entities.bgs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lebah.template.UID;

@Entity
@Table(name = "bgs_keperluan_pejabat")
public class BgsKeperluanPejabat {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_permohonan")
	private BgsPermohonan permohonan;
	
	@Column(name = "perkara")
	private String perkara;
	
	@Column(name = "luas_semasa")
	private String luasSemasa;
	
	@Column(name = "luas_akan_datang")
	private String luasAkanDatang;
	
	public BgsKeperluanPejabat() {
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

	public String getPerkara() {
		return perkara;
	}

	public void setPerkara(String perkara) {
		this.perkara = perkara;
	}

	public String getLuasSemasa() {
		return luasSemasa;
	}

	public void setLuasSemasa(String luasSemasa) {
		this.luasSemasa = luasSemasa;
	}

	public String getLuasAkanDatang() {
		return luasAkanDatang;
	}

	public void setLuasAkanDatang(String luasAkanDatang) {
		this.luasAkanDatang = luasAkanDatang;
	}
}