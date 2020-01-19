package bph.entities.jrp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lebah.template.UID;

@Entity
@Table(name = "jrp_kakitangan")
public class JrpKakitangan {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_permohonan")
	private JrpPermohonan permohonan;
	
	@Column(name = "jawatan")
	private String jawatan;
	
	@Column(name = "gred")
	private String gred;
	
	@Column(name = "bilangan_sedia_ada")
	private String bilanganSediaAda;
	
	@Column(name = "luas_sedia_ada")
	private String luasSediaAda;
	
	@Column(name = "bilangan_baru")
	private String bilanganBaru;
	
	@Column(name = "luas_baru")
	private String luasBaru;

	@Column(name = "turutan")
	private int turutan;
	
	public JrpKakitangan() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public JrpPermohonan getPermohonan() {
		return permohonan;
	}

	public void setPermohonan(JrpPermohonan permohonan) {
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

	public String getBilanganSediaAda() {
		return bilanganSediaAda;
	}

	public void setBilanganSediaAda(String bilanganSediaAda) {
		this.bilanganSediaAda = bilanganSediaAda;
	}

	public String getLuasSediaAda() {
		return luasSediaAda;
	}

	public void setLuasSediaAda(String luasSediaAda) {
		this.luasSediaAda = luasSediaAda;
	}

	public String getBilanganBaru() {
		return bilanganBaru;
	}

	public void setBilanganBaru(String bilanganBaru) {
		this.bilanganBaru = bilanganBaru;
	}

	public String getLuasBaru() {
		return luasBaru;
	}

	public void setLuasBaru(String luasBaru) {
		this.luasBaru = luasBaru;
	}

	public int getTurutan() {
		return turutan;
	}

	public void setTurutan(int turutan) {
		this.turutan = turutan;
	}
}