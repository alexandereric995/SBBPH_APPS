package bph.entities.jrp;

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

	public JrpKakitangan() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());
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