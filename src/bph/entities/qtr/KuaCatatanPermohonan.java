package bph.entities.qtr;

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
@Table(name = "kua_catatan_permohonan")
public class KuaCatatanPermohonan {

	@Id
	@Column(name = "id")
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "id_permohonan")
	private KuaPermohonan permohonan;
	
	@Column(name = "catatan")
	private String catatan;
	
	@ManyToOne
	@JoinColumn(name = "id_masuk")
	private Users daftarOleh;

	@Column(name = "tarikh_masuk")
	@Temporal(TemporalType.TIMESTAMP)
	private Date tarikhDaftar;
	
	public KuaCatatanPermohonan() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public KuaPermohonan getPermohonan() {
		return permohonan;
	}

	public void setPermohonan(KuaPermohonan permohonan) {
		this.permohonan = permohonan;
	}

	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}

	public Users getDaftarOleh() {
		return daftarOleh;
	}

	public void setDaftarOleh(Users daftarOleh) {
		this.daftarOleh = daftarOleh;
	}

	public Date getTarikhDaftar() {
		return tarikhDaftar;
	}

	public void setTarikhDaftar(Date tarikhDaftar) {
		this.tarikhDaftar = tarikhDaftar;
	}
}
