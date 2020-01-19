package bph.entities.jrp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lebah.template.UID;

@Entity
@Table(name = "jrp_keluasan_ruang")
public class JrpKeluasanRuang {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_permohonan")
	private JrpPermohonan permohonan;
	
	@Column(name = "perkara")
	private String perkara;
	
	@Column(name = "luas_sedia_ada")
	private String luasSediaAda;
	
	@Column(name = "luas_baru")
	private String luasBaru;
	
	public JrpKeluasanRuang() {
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

	public String getPerkara() {
		return perkara;
	}

	public void setPerkara(String perkara) {
		this.perkara = perkara;
	}

	public String getLuasSediaAda() {
		return luasSediaAda;
	}

	public void setLuasSediaAda(String luasSediaAda) {
		this.luasSediaAda = luasSediaAda;
	}

	public String getLuasBaru() {
		return luasBaru;
	}

	public void setLuasBaru(String luasBaru) {
		this.luasBaru = luasBaru;
	}
}