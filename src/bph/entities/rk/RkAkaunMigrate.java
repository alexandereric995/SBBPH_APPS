package bph.entities.rk;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import portal.module.entity.Users;

@Entity
@Table(name = "rk_akaun_migrate")
public class RkAkaunMigrate {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "no_fail")
	private String noFail;

	@Column(name = "no_resit")
	private String noResit;

	@Column(name = "tarikh_bayaran")
	private String tarikhBayaran;

	@Column(name = "amaun")
	private String amaun;

	@Column(name = "id_kod_hasil")
	private String idKodHasil;

	@Column(name = "flag_migrate")
	private String flagMigrate;

	@Column(name = "msg")
	private String msg;

	@ManyToOne
	@JoinColumn(name = "id_masuk")
	private Users daftarOleh;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_masuk")
	private Date tarikhMasuk;

	@ManyToOne
	@JoinColumn(name = "id_kemaskini")
	private Users kemaskiniOleh;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_kemaskini")
	private Date tarikhKemaskini;

	public RkAkaunMigrate() {
		setTarikhMasuk(new Date());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNoFail() {
		return noFail;
	}

	public void setNoFail(String noFail) {
		this.noFail = noFail;
	}

	public String getNoResit() {
		return noResit;
	}

	public void setNoResit(String noResit) {
		this.noResit = noResit;
	}

	public String getTarikhBayaran() {
		return tarikhBayaran;
	}

	public void setTarikhBayaran(String tarikhBayaran) {
		this.tarikhBayaran = tarikhBayaran;
	}

	public String getAmaun() {
		return amaun;
	}

	public void setAmaun(String amaun) {
		this.amaun = amaun;
	}

	public String getIdKodHasil() {
		return idKodHasil;
	}

	public void setIdKodHasil(String idKodHasil) {
		this.idKodHasil = idKodHasil;
	}

	public String getFlagMigrate() {
		return flagMigrate;
	}

	public void setFlagMigrate(String flagMigrate) {
		this.flagMigrate = flagMigrate;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Users getDaftarOleh() {
		return daftarOleh;
	}

	public void setDaftarOleh(Users daftarOleh) {
		this.daftarOleh = daftarOleh;
	}

	public Date getTarikhMasuk() {
		return tarikhMasuk;
	}

	public void setTarikhMasuk(Date tarikhMasuk) {
		this.tarikhMasuk = tarikhMasuk;
	}

	public Users getKemaskiniOleh() {
		return kemaskiniOleh;
	}

	public void setKemaskiniOleh(Users kemaskiniOleh) {
		this.kemaskiniOleh = kemaskiniOleh;
	}

	public Date getTarikhKemaskini() {
		return tarikhKemaskini;
	}

	public void setTarikhKemaskini(Date tarikhKemaskini) {
		this.tarikhKemaskini = tarikhKemaskini;
	}
}
